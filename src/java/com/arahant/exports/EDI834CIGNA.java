/*
    STACK360 - Web-based Business Management System
    Copyright (C) 2024 Arahant LLC

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see https://www.gnu.org/licenses.
*/

/*
*/


package com.arahant.exports;

import com.arahant.beans.*;
import com.arahant.business.*;
import com.arahant.beans.CompanyBase;
import com.arahant.business.BEDITransaction;
import com.arahant.edi.EDI;
import com.arahant.exceptions.ArahantException;
import com.arahant.lisp.LispPackage;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileReader;
import org.kissweb.DelimitedFileWriter;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class EDI834CIGNA extends AEDI834 {

	public EDI834CIGNA(String fileType)
	{
		super();
		this.fileType = fileType;
		ediLog = new EDILog("CIGNA_EDI");
		vendorCode = "CIGNA";
		try {
			lp = new LispPackage("WmCo Insurance Division Codes", "WmCo-Insurance-Division-Codes");
		}
		catch(Throwable t) {
			lp = null;
		}
		logger = new ArahantLogger(EDI834CIGNA.class);
	}

	private DelimitedFileWriter dfw_ADD;
	private DelimitedFileWriter dfw_TERM;

    public String[] dumpEDI(CompanyBase vendor, BEDITransaction tran, boolean debug) throws CloneNotSupportedException {
        List<String> filenames = new LinkedList<String>();
        hsu.dontAIIntegrate();

        int icn = tran.getStartingICN();
		int gcn = tran.getStartingGCN();
        int transactionSetNumber = tran.getStartingTscn();

        lastDumpDate = tran.getLastExportDate();

        long mils = new Date().getTime();

        //Work around to get the correct company.  Need to find out why the App is not filtering by company.
        BVendorCompany bv = new BVendorCompany(vendor.getOrgGroupId());
        CompanyDetail cd = bv.getAssociatedCompany();
        ArahantSession.getHSU().setCurrentCompany(cd);

        List<HrBenefit> benefitsToDo = hsu.createCriteria(HrBenefit.class).eq(HrBenefit.BENEFIT_PROVIDER, vendor).list();

		account = bv.getAccountNumber();
		int benefitCount = 0;
		String filename = "";
		try {
			filename = new BProperty("LargeReportDir").getValue() + File.separator + "XO16000__xo18027i.21913.williamson" + DateUtils.now() + DateUtils.nowTimeToMinute() + ".edi";  //.txt
			edi = new EDI(filename);
			dfw_ADD = new DelimitedFileWriter(filename + ".adds.csv");
			dfw_TERM = new DelimitedFileWriter(filename + ".terms.csv");
			writeAddsHeader();
			writeTermsHeader();
		}
		catch (Exception ex) {
			throw new ArahantException("Couldn't write to CSV files");
		}

		edi.X12_setup('*', '~', '\n');
		isa_seg(EDI.makecn(icn), debug, vendor.getInterchangeSenderId(), vendor.getInterchangeReceiverId());

        for (HrBenefit bene : benefitsToDo) {
            gs_seg(EDI.makecn(gcn), vendor.getApplicationSenderId(), vendor.getApplicationReceiverId());

            header(transactionSetNumber, vendorCode, vendor.getInterchangeSenderId(), cd.getName(), vendor.getInterchangeReceiverId(), gcn); //cd.getName() cd.getFederalEmployerId()

			try {
				doInactives(bene); //deal with history records - policies that have ended
				doActives(bene);
			}
			catch(IOException ioe) {
				System.err.println("Could not write to export.");
			}
			catch(Exception e) {
				System.err.println("Could not write to CSV.");
				e.printStackTrace();
			}

            transactionSetTrailer(transactionSetNumber);
            transactionSetNumber++;
            functionalGroupTrailer(EDI.makecn(gcn));
            gcn++;
			benefitCount++;
        }
		filenames.add(filename);
		functionInterchangeTrailer(EDI.makecn(icn));

		try {
			edi.close();
			ediLog.close();
			dfw_ADD.close();
			dfw_TERM.close();
		}
		catch (IOException ex) {
			//Logger.getLogger(EDI834Humana.class.getName()).log(Level.SEVERE, null, ex);
		}
        tran.setTransactionSetNumber(transactionSetNumber - 1);
        tran.setGCN(gcn - 1);
        tran.setICN(icn);

        mils = new Date().getTime() - mils;

        logger.info("took " + (mils / 1000) + " seconds");

		if(lp != null)
			lp.packageDone();
        return filenames.toArray(new String[filenames.size()]);
    }

    private void doInactives(HrBenefit bene) throws CloneNotSupportedException, IOException, Exception {
        int count = 0;
        benefitName = bene.getName();
		String currentSubscriber = "";
		List<String> doneIds = new ArrayList<String>();
		CompanyBase v = bene.getProvider();

		HibernateCriteriaUtil<HrBenefitJoinH> hcu = hsu.createCriteria(HrBenefitJoinH.class);
        hcu.orderByDesc(HrBenefitJoin.POLICY_START_DATE);
        hcu.joinTo(HrBenefitJoinH.PAYING_PERSON).orderBy(Person.PERSONID);
        hcu.eq(HrBenefitJoinH.RECORD_CHANGE_TYPE, 'D');
        hcu.ne(HrBenefitJoinH.POLICY_END_DATE, 0);
		hcu.gtJoinedField(HrBenefitJoinH.POLICY_END_DATE, HrBenefitJoinH.POLICY_START_DATE);
        hcu.gt(HrBenefitJoinH.RECORD_CHANGE_DATE, lastDumpDate);
        hcu.joinTo(HrBenefitJoinH.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bene);
        hcu.ne(HrBenefitJoinH.COVERAGE_START_DATE, 0);

        benefitType = hsu.createCriteria(HrBenefitCategory.class).selectFields(HrBenefitCategory.TYPE)
																 .joinTo(HrBenefitCategory.HRBENEFIT)
																 .eq(HrBenefit.BENEFITID, bene.getBenefitId())
																 .intValue();

        HibernateScrollUtil<HrBenefitJoinH> hscr = hcu.scroll();
		List<HrBenefitJoinH> depJoins = new ArrayList<HrBenefitJoinH>();

        while (hscr.next()) {
            if (++count % 50 == 0)
                logger.info(count);

            HrBenefitJoinH bj = hscr.get();

			boolean activeBene = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, bj.getCoveredPerson())
																		.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
																		.joinTo(HrBenefitConfig.HR_BENEFIT)
																		.eq(HrBenefit.BENEFIT_PROVIDER, v)
																		.first() != null;
			if(activeBene)
				continue;

			if(!(new BPerson(bj.getPayingPersonId()).isEmployee())) {
				logger.info(bj.getPayingPerson().getNameFML() + " was not transmitted because they are paying for a policy, but is not an employee.");
				continue;
			}

			maintenanceTypeCode = "024";

			if(!bj.getPayingPersonId().equals(currentSubscriber)) {
				currentSubscriber = bj.getPayingPersonId();

				Collections.sort(depJoins, comparator);
				setCoverageLevel(depJoins);
				for(HrBenefitJoinH h : depJoins) {
					if(!doneIds.contains(h.getCoveredPersonId())) {
						try {
							if(h.getCoverageEndDate() == 0)
								h.setCoverageEndDate(h.getPolicyEndDate());
							setBenefitJoinData(h, benefitType, true);
							ediMemberLevelDetail(true);
							doneIds.add(h.getCoveredPersonId());
						}
						catch(BranchCodeException b) {
							System.out.println(new BPerson(h.getCoveredPersonId()).getNameFML() + " was not sent due to missing requisite data.");
							ediLog.write(new BPerson(h.getCoveredPersonId()).getNameFML() + " was not sent due to missing requisite data.");
							continue;
						}
						catch(NullPointerException n) {
							ediLog.write(new BPerson(h.getCoveredPersonId()).getNameFML() + " was not sent due to erroneous data.");
							n.printStackTrace();
							continue;
						}
						catch(Exception ex) {
							ediLog.write(new BPerson(h.getCoveredPersonId()).getNameFML() + " was not sent.");
							ex.printStackTrace();
							continue;
						}
					}
					else {
						writeTerms(h);
						continue;
					}
				}
				depJoins.clear();
				flushData();
			}

			depJoins.add(bj);
        }
		Collections.sort(depJoins, comparator);
		setCoverageLevel(depJoins);
		for(HrBenefitJoinH h : depJoins) {
			try {
				if(h.getCoverageEndDate() == 0)
					h.setCoverageEndDate(h.getPolicyEndDate());
				setBenefitJoinData(h, benefitType, true);
				ediMemberLevelDetail(true);
				doneIds.add(h.getCoveredPersonId());
			}
			catch(BranchCodeException b) {
				System.out.println(new BPerson(h.getCoveredPersonId()).getNameFML() + " was not sent due to missing requisite data.");
				ediLog.write(new BPerson(h.getCoveredPersonId()).getNameFML() + " was not sent due to missing requisite data.");
				continue;
			}
			catch(NullPointerException n) {
				ediLog.write(new BPerson(h.getCoveredPersonId()).getNameFML() + " was not sent due to erroneous data.");
				n.printStackTrace();
				continue;
			}
			catch(Exception ex) {
				ediLog.write(new BPerson(h.getCoveredPersonId()).getNameFML() + " was not sent.");
				ex.printStackTrace();
				continue;
			}
		}
        hscr.close();
    }

    private List<String> doActives(HrBenefit bene) throws CloneNotSupportedException, IOException, Exception {
        int count = 0;
        benefitName = bene.getName();
		String currentSubscriber = "";
		List<String> doneIds = new ArrayList<String>();

		HibernateCriteriaUtil<HrBenefitJoin> hcu = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.BENEFIT_DECLINED, 'N').eq(HrBenefitJoin.APPROVED, 'Y');
        hcu.orderByDesc(HrBenefitJoin.POLICY_START_DATE);
        hcu.joinTo(HrBenefitJoin.PAYING_PERSON).orderBy(Person.PERSONID);
        hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bene);
        hcu.ne(HrBenefitJoin.COVERAGE_START_DATE, 0);
		hcu.gtOrEq(HrBenefitJoin.COVERAGE_END_DATE, DateUtils.now(), 0);
//		hcu.lt(HrBenefitJoin.POLICY_START_DATE, 20120101);  //HARDCODED FOR NOW (Remove upon given date by WMCO)

        benefitType = hsu.createCriteria(HrBenefitCategory.class).selectFields(HrBenefitCategory.TYPE)
																 .joinTo(HrBenefitCategory.HRBENEFIT)
																 .eq(HrBenefit.BENEFITID, bene.getBenefitId())
																 .intValue();
        HibernateScrollUtil<HrBenefitJoin> hscr = hcu.scroll();
		List<HrBenefitJoin> depJoins = new ArrayList<HrBenefitJoin>();

        while (hscr.next()) {
            if (++count % 50 == 0)
                logger.info(count);

            HrBenefitJoin bj = hscr.get();
			BHRBenefitJoin bbj = new BHRBenefitJoin(bj);

			int covend = bj.getCoverageEndDate();
			if (covend != 0 && bj.getRecordChangeDate().before(lastDumpDate))
				continue;
			if (bj.getPolicyEndDate() != 0 && bj.getRecordChangeDate().before(lastDumpDate))
				continue;

			if(!(new BPerson(bbj.getPayingPersonId()).isEmployee())) {
				logger.info(bj.getPayingPerson().getNameFML() + " was not transmitted because they are paying for a policy, but is not an employee.");
				continue;
			}

			if(!bj.getPayingPersonId().equals(currentSubscriber)) {
				currentSubscriber = bj.getPayingPersonId();
				Collections.sort(depJoins, comparator);
				setCoverageLevel(depJoins);
				for(HrBenefitJoin j : depJoins) {
					if(new BHRBenefitJoin(j).isDependentBenefitJoin()) {
//						if(new BHRBenefitJoin(j).getPolicyBenefitJoin() == null)
//							j.getPayingPersonId();
						if(new BHRBenefitJoin(j).getPolicyBenefitJoin().getEmployeeCovered() == 'N')
							employeeCovered = false;
					}
//					System.out.print(new BPerson(j.getPayingPersonId()).getNameFML());
//					if(j.getCoveredPersonId() == null)
//						j.getCoveredPersonId();
//					System.out.println(" " + new BPerson(j.getCoveredPersonId()).getNameFML());
					if(!doneIds.contains(j.getCoveredPersonId())) {
						try {
							if(j.getCoverageEndDate() == 0)
								j.setCoverageEndDate(j.getPolicyEndDate());
							setBenefitJoinData(j, benefitType, true);
							ediMemberLevelDetail(true);
							doneIds.add(j.getCoveredPersonId());
						}
						catch(BranchCodeException b) {
							System.out.println(new BPerson(j.getCoveredPersonId()).getNameFML() + " was not sent due to missing requisite data.");
							ediLog.write(new BPerson(j.getCoveredPersonId()).getNameFML() + " was not sent due to missing requisite data.");
							continue;
						}
						catch(NullPointerException n) {
							ediLog.write(new BPerson(j.getCoveredPersonId()).getNameFML() + " was not sent due to erroneous data.");
							n.printStackTrace();
							continue;
						}
						catch(Exception ex) {
							ediLog.write(new BPerson(j.getCoveredPersonId()).getNameFML() + " was not sent.");
							ex.printStackTrace();
							continue;
						}
					}
					else {
						writeAdds(j);
						continue;
					}
				}
				depJoins.clear();
				flushData();
			}

			depJoins.add(bj);
		}
		Collections.sort(depJoins, comparator);
		setCoverageLevel(depJoins);
		for(HrBenefitJoin j : depJoins) {
			try {
				if(j.getCoverageEndDate() == 0)
					j.setCoverageEndDate(j.getPolicyEndDate());
				setBenefitJoinData(j, benefitType, true);
				ediMemberLevelDetail(true);
				doneIds.add(j.getCoveredPersonId());
			}
			catch(BranchCodeException b) {
				System.out.println(new BPerson(j.getCoveredPersonId()).getNameFML() + " was not sent due to missing requisite data.");
				ediLog.write(new BPerson(j.getCoveredPersonId()).getNameFML() + " was not sent due to missing requisite data.");
				continue;
			}
			catch(NullPointerException n) {
				ediLog.write(new BPerson(j.getCoveredPersonId()).getNameFML() + " was not sent due to erroneous data.");
				n.printStackTrace();
				continue;
			}
			catch(Exception ex) {
				ediLog.write(new BPerson(j.getCoveredPersonId()).getNameFML() + " was not sent.");
				ex.printStackTrace();
				continue;
			}
		}
        hscr.close();

        return payingPersons;
    }

	private void writeAddsHeader() throws Exception {
		dfw_ADD.writeField("Branch Code");
		dfw_ADD.writeField("BENOP Code");
		dfw_ADD.writeField("Effective Date");
		dfw_ADD.writeField("Employee SSN");
		dfw_ADD.writeField("Member SSN");
		dfw_ADD.writeField("Member Last Name");
		dfw_ADD.writeField("Member First Name");
		dfw_ADD.writeField("Member Birth Date");
		dfw_ADD.writeField("Member Gender");
		dfw_ADD.writeField("Relationship Code");
		dfw_ADD.writeField("Full Time Student");
		dfw_ADD.writeField("Street Address 1");
		dfw_ADD.writeField("Street Address 2");
		dfw_ADD.endRecord();
	}

	private void writeAdds(HrBenefitJoin bj) throws Exception {
		BEmployee be = new BEmployee(bj.getPayingPersonId());
		BPerson bp = new BPerson(bj.getCoveredPersonId());
		BHRBenefitConfig c = new BHRBenefitConfig(bj.getHrBenefitConfigId());
		BHRBenefit b = new BHRBenefit(c.getBenefitId());
		Address add = bp.getHomeAddress();
		if(isEmpty(add.getStreet()))
			add = be.getHomeAddress();

		if(bj.getRecordChangeDate().after(lastDumpDate)) {
			HrBenefitJoinH oldBj = hsu.createCriteria(HrBenefitJoinH.class).eq(HrBenefitJoinH.PAYING_PERSON_ID, bj.getPayingPersonId())
																		   .eq(HrBenefitJoinH.COVERED_PERSON_ID, bj.getCoveredPersonId())
																		   .eq(HrBenefitJoinH.POLICY_START_DATE, bj.getPolicyStartDate())
																		   .joinTo(HrBenefitJoinH.HR_BENEFIT_CONFIG)
																		   .joinTo(HrBenefitConfig.HR_BENEFIT)
																		   .eq(HrBenefit.BENEFIT_CATEGORY_ID, b.getBenefitCategoryId())
																		   .joinTo(HrBenefit.BENEFIT_PROVIDER, b.getUnderwriter())
	//																	   .eq(HrBenefitConfig.HR_BENEFIT_ID, b.getId())
																		   .first();
			BHRBenefitConfig oldC = null;
			BHRBenefit oldB = null;
			if(oldBj != null) {
				oldC = new BHRBenefitConfig(oldBj.getHrBenefitConfigId());
				oldB = new BHRBenefit(oldC.getBenefitId());
			}

			if(bj.getRecordChangeType() == 'N') {
				setBranchCode(be.getPersonId(), bj);
				String planKey = branch.substring(0,2);
				String plan = b.getPlan();
				if(planKey.equals("10") || planKey.equals("40") || planKey.equals("60") || planKey.equals("70")) {
					if(payingAge > 64) {
						if(memberAddressState.equals("NC") || memberAddressState.equals("CA"))
							plan += "C";
						else
							plan += "R";
					}
				}
				dfw_ADD.writeField(branch);
				dfw_ADD.writeField(plan);
				dfw_ADD.writeField(bj.getPolicyStartDate() > bj.getCoverageStartDate() ? DateUtils.getDateFormatted(bj.getPolicyStartDate())
																					   : DateUtils.getDateFormatted(bj.getCoverageStartDate()));
				dfw_ADD.writeField(be.getSsn());
				dfw_ADD.writeField(bp.getSsn());
				dfw_ADD.writeField(bp.getLastName());
				dfw_ADD.writeField(bp.getFirstName());
				dfw_ADD.writeField(DateUtils.getDateFormatted(bp.getDob()));
				dfw_ADD.writeField(bp.getSex());  //gender
				dfw_ADD.writeField(bj.getRelationship() == null ? "E" : bj.getRelationship().getRelationshipType() + "");  //relationship
				dfw_ADD.writeField(bp.getStudent() ? "Y" : "N");  //student?
				dfw_ADD.writeField(add.getStreet());
				dfw_ADD.writeField(add.getCity() + ", " + add.getState() + " " + add.getZip());
				dfw_ADD.endRecord();
			}
		}
	}

	private void writeTermsHeader() throws Exception {
		dfw_TERM.writeField("Branch Code");
		dfw_TERM.writeField("Employee SSN");
		dfw_TERM.writeField("Member SSN");
		dfw_TERM.writeField("Member Last Name");
		dfw_TERM.writeField("Member First Name");
		dfw_TERM.writeField("Cancellation Date");
		dfw_TERM.endRecord();
	}

	private void writeTerms(HrBenefitJoinH bj) throws Exception {
		BPerson bp = new BPerson(bj.getCoveredPersonId());
		BEmployee be = new BEmployee(bj.getPayingPersonId());
		BHRBenefit b = new BHRBenefit(bj.getHrBenefitConfig().getBenefitId());

		if(bj.getRecordChangeDate().after(lastDumpDate)) {
			setBranchCode(be.getPersonId(), bj);
			String planKey = branch.substring(0,2);
			String plan =b.getPlan();
			if(planKey.equals("10") || planKey.equals("40") || planKey.equals("60") || planKey.equals("70")) {
				if(payingAge > 64) {
					if(memberAddressState.equals("NC") || memberAddressState.equals("CA"))
						plan += "C";
					else
						plan += "R";
				}
			}
			dfw_TERM.writeField(branch);
			dfw_TERM.writeField(be.getSsn());
			dfw_TERM.writeField(bp.getSsn());
			dfw_TERM.writeField(bp.getLastName());
			dfw_TERM.writeField(bp.getFirstName());
			dfw_TERM.writeField(bj.getPolicyEndDate() < bj.getCoverageEndDate() ? DateUtils.getDateFormatted(bj.getPolicyEndDate())
																			   : DateUtils.getDateFormatted(bj.getCoverageEndDate()));
			dfw_TERM.endRecord();
		}
	}

	protected void ediMemberLevelDetail(boolean fake) throws BranchCodeException {
		INS_insured_benefit();
		REF_SSN_subscriber();
		REF_HIC_number();
		//REF_subscriber_group_number();
		//REF_subscriber_class();
		//REF_subscriber_subgroup_number();
		//IDC_identification_card();
		DTP_employment_date();
		DTP_employment_term_date();
		DTP_retirement_date();
		NM1_member_name();
		PER_contact_info();
		N3_address_information();
		N4_geographic_location();
		DMG_demographic_info();
		HD_health_coverage();
		DTP_benefit_start_date();
		DTP_benefit_end_date();
		if(lp != null)
			REF_1L_structure();
	}

	protected void setBenefitJoinData(IHrBenefitJoin bj, int benefitType, boolean fake) throws BranchCodeException {
		BPerson p = new BPerson(bj.getPayingPersonId());
		BEmployee emp = null;
		if (p.isEmployee())
			emp = new BEmployee(bj.getPayingPersonId());
		BPerson d = new BPerson(bj.getCoveredPersonId());
		String benefitId = bj.getHrBenefitConfig().getHrBenefit().getBenefitId();

		setLocation(bj);
		setRelationshipCode(bj);
//		setPCP(bj);
		setEmploymentCode(bj, emp.getPersonId());
		setPayingPerson(bj.getPayingPersonId(), benefitId);
		setCoveredPerson(bj.getCoveredPersonId());
		setAddress(p, d);
		setMaritalStatus(emp.getPersonId());
		if((lp != null && subscriber) || isEmpty(branch))
			setBranchCode(emp.getPersonId(), bj);
		setDates(bj);

		HrBenefitConfig hbc = hsu.get(HrBenefitConfig.class, bj.getHrBenefitConfigId());
		insuranceLineCode = hbc.getHrBenefit().getInsuranceCode();
		benefitStatusCode = (bj.getUsingCOBRA() == 'Y') ? "C" : "A";
		changeReason = bj.getChangeDescription();
		benefitName = hbc.getHrBenefit().getName();
		this.benefitType = benefitType;

		int covdate = bj.getCoverageStartDate();
		int covend = bj.getCoverageEndDate();
		if (bj.getRecordChangeDate().after(lastDumpDate))
			maintenanceTypeCode = "001";
		if (covdate != 0 && bj.getRecordChangeDate().after(lastDumpDate))
			maintenanceTypeCode = "021";
		if (covend != 0 && bj.getRecordChangeDate().after(lastDumpDate))
			maintenanceTypeCode = "024";
		if (bj.getPolicyEndDate() != 0 && bj.getRecordChangeDate().after(lastDumpDate))
			maintenanceTypeCode = "024";
	}

	@Override
	protected void setPayingPerson(String payingPersonId, String benefitId) {
		Person payingPerson = new BPerson(payingPersonId).getPerson();
//		System.out.println(payingPerson.getNameFML());
		subscriberIdentifier = payingPerson.getUnencryptedSsn();

		if ("999-99-9999".equals(subscriberIdentifier))
			subscriberIdentifier = "000-00-0000";
		int efd = 99999999;

		if (policyStart > efd)
			efd = policyStart;

		BHRBenefit bhr = new BHRBenefit(benefitId);

		groupNumber = bhr.getGroupId();
		subGroupNumber = bhr.getSubGroupId();
		groupAccountId = bhr.getGroupAccountId();
        benefitClassId = bhr.getCoveredUnderCOBRA() ? "C001" : "A001";

		BEmployee be = new BEmployee(payingPerson.getPersonId());
		medicareType = be.getMedicare() + "";
		if(medicareType.equals("2"))
			medicareType = "C";
		else if(medicareType.equals("U"))
			medicareType = "D";
		else if(medicareType.equals("N"))
			medicareType = "E";
		hicNumber = isEmpty(payingPerson.getHicNumber()) ? "" : payingPerson.getHicNumber();

		plan = bhr.getPlan();
		planName = bhr.getPlanName();
		payerId = bhr.getPayerId();
		ediFieldValue1 = bhr.getEdiFieldValue1();
		ediFieldValue2 = bhr.getEdiFieldValue2();
		ediFieldValue3 = bhr.getEdiFieldValue3();
		ediFieldValue4 = bhr.getEdiFieldValue4();
		ediFieldValue5 = bhr.getEdiFieldValue5();

		if (groupNumber == null)
			groupNumber = "";

		if (groupVendorId == null)
			groupVendorId = "";

		if (benefitClassId == null)
			benefitClassId = "";

		payingAge = new BPerson(payingPerson).getAgeAsOf(DateUtils.now());
	}

	protected <T extends IHrBenefitJoin> void setCoverageLevel(List<T> bj) {
		if(!bj.isEmpty()) {
			if(bj.size() == 1)
				coverageLevelCode = "EMP";
			else if(bj.size() > 2) {
				List<HrEmplDependent> spouse = hsu.createCriteria(HrEmplDependent.class).eq(HrEmplDependent.EMPLOYEE, bj.get(1).getPayingPerson())
																						.eq(HrEmplDependent.RELATIONSHIP_TYPE, 'S')
																						.list();
				if(spouse == null || spouse.isEmpty())
					coverageLevelCode = "ECH";
				else
					coverageLevelCode = "FAM";
			}
			else {
				try {
				HrEmplDependent dep = hsu.createCriteria(HrEmplDependent.class).eq(HrEmplDependent.PERSON, bj.get(1).getCoveredPerson())
																			   .eq(HrEmplDependent.EMPLOYEE, bj.get(1).getPayingPerson()).first();
				char relationship = dep.getRelationshipType();
				if(relationship == 'C')
					coverageLevelCode = "ECH";
				else
					coverageLevelCode = "ESP";
				}
				catch (Exception ex) {
					for(int i = 0; i < bj.size(); i++)
						System.out.println("    " + bj.get(i).getPayingPerson().getNameFML() + ", " + (bj.get(i).getCoveredPerson().getNameFML()));
				}
			}
		}
	}

	@Override
	protected void setCoverageLevel(IHrBenefitJoin bj) {
		coverageLevelCode = "EMP";

		HrBenefitConfig config = bj.getHrBenefitConfig();

		if((config.getSpouseEmployee() == 'Y' ||
		   config.getSpouseNonEmployee() == 'Y') &&
		   config.getMaxChildren() == 1)
			coverageLevelCode = "ESP";
		if(((config.getSpouseNonEmpOrChildren() == 'Y') ||
			(config.getSpouseEmpOrChildren() == 'Y')))
			if(config.getMaxChildren() > 1 || config.getMaxChildren() == 0)
				coverageLevelCode = "FAM";
			else if(config.getMaxChildren() == 1)
				coverageLevelCode = "ESP";
		if(config.getChildren() == 'Y' && config.getMaxChildren() == 1)
				coverageLevelCode = "ECH";
	}

	protected void setBranchCode(String empId, IHrBenefitJoin bj) throws ArahantException, BranchCodeException {
		BEmployee emp = new BEmployee(empId);
		BOrgGroup bog = new BOrgGroup(emp.getOrgGroupAssociations().iterator().next().getOrgGroup());
		String empStatusId = "";

		if(bj instanceof HrBenefitJoinH)
			empStatusId = emp.getLastActiveStatusHistory().getStatusId();
		else
			empStatusId = emp.getEmployeeStatusId();

		String branch = lp.executeLispReturnString("get-division-code", bj.getHrBenefitConfig().getHrBenefit().getBenefitId(), bog.getOrgGroupId(), empStatusId);
		while (isEmpty(branch)) {
			bog = bog.getParent();
			if (bog.getParent() == null)
				break;
			branch = lp.executeLispReturnString("get-division-code", bj.getHrBenefitConfig().getHrBenefit().getBenefitId(), bog.getOrgGroupId(), empStatusId);
		}
//		if(isEmpty(branch))
//			throw new BranchCodeException();
		this.branch = branch;
	}

	@Override
	protected void setMaritalStatus(String empId) {
		BEmployee emp = new BEmployee(empId);
		maritalStatusCode = "I";

		if (emp != null)
			if (emp.getActiveSpouse(DateUtils.now()) != null)
				maritalStatusCode = "M";
			else if (emp.getActiveSpouse(0) != null)
				maritalStatusCode = "U";
	}

	protected void REF_subscriber_group_account() throws IOException {
		edi.SEG("REF");
		edi.SF(1, "1L");
		edi.SF(2, groupAccountId);
		edi.EOS();
	}

	protected void REF_subscriber_group_vendor() throws IOException {
		edi.SEG("REF");
		edi.SF(1, "DX");
		edi.SF(2, groupVendorId);
		edi.EOS();
	}

	protected void REF_insurance_code() throws IOException {
		edi.SEG("REF");
		edi.SF(1, "17");
		edi.SF(2, insuranceLineCode);
		edi.EOS();
	}

	protected void REF_1L_structure() throws BranchCodeException {
		if(isEmpty(branch)) {
			System.out.println("NO BRANCH CODE GENERATED: " + memberFirstName + " " + memberLastName + ": " + branch + "/" + plan + " " + payingAge);
			throw new BranchCodeException("NO BRANCH CODE GENERATED: " + memberFirstName + " " + memberLastName + ": " + branch + "/" + plan + " " + payingAge);
		}
		String code = "";

		String planKey = branch.substring(0,2);
		if(planKey.equals("10") || planKey.equals("40")/* || planKey.equals("60") || planKey.equals("70")*/) {
			if(payingAge > 64) {
				if(memberAddressState.equals("NC") || memberAddressState.equals("CA"))
					plan += "C";
				else
					plan += "R";
			}
		}
		if(memberAddressState.equals("NC") || memberAddressState.equals("CA"))
			if(Character.isDigit(plan.charAt(plan.length() - 1)))
				plan = plan.replaceAll(plan.substring(plan.length() - 1), "C" + plan.substring(plan.length() - 1));

		code += ((account + "       ").substring(0, 7));
		code += ((branch + "      ").substring(0, 6));
		code += ((plan + "     ").substring(0, 5));

		edi.SEG("REF");
		edi.SF(1, "1L");
		edi.ST(2, code); //account number (7), branch(6, fill spaces), benefit option(5, fill spaces), network code(5 , optional)
		edi.EOS();
	}

	@Override
	protected void DTP_benefit_start_date() {
		int modifiedCoverageStartDate = 0;
		if(Integer.valueOf(coverageStartDate) < 20110701) {
			if(20110701 < policyStart)
				modifiedCoverageStartDate = policyStart;
			else
				modifiedCoverageStartDate = 20110701;
		}
		else
			modifiedCoverageStartDate = Integer.valueOf(coverageStartDate);

		edi.SEG("DTP");
		edi.SF(1, "348");
		edi.ST(2, "D8");
		if ("".equals(coverageStartDate))
			logger.info("Coverage start date missing");
		edi.SF(3, String.valueOf(modifiedCoverageStartDate));
		edi.EOS();
	}

	class BranchCodeException extends Exception {
		public BranchCodeException() {}

		public BranchCodeException(String msg) {
			super(msg);
		}
	}


    public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
		/****
		 * Test code for LISP functionality
		 ****/
//		ABCL.init();
//		LispPackage lp = new LispPackage("WmCo Insurance Division Codes", "com/arahant/lisp/WmCo-Insurance-Division-Codes");
//		String val = lp.executeLispReturnString("GET-DIVISION-CODE", "00001-0000000023", "00001-0000000010", "00001-0000000002");
//		lp.packageDone();
//		System.out.println(val);

		/****
		 * Main method to import in HIC humbers
		 * from WMCO-supplied spreadsheet (5 - 10 minutes)
		 ****/
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.setCurrentCompany(new BCompany("00001-0000000000").getBean());
		List<Employee> emps = hsu.createCriteria(Employee.class).list();
		HashMap<String, Employee> empHash = new HashMap<String, Employee>();
		for(Employee e : emps)
			empHash.put(e.getUnencryptedSsn(), e);

		DelimitedFileReader fr = new DelimitedFileReader("/home/xichen/Downloads/HIC Number file.csv");
		Set<String> ssns = empHash.keySet();

		hsu.beginTransaction();
		fr.nextLine();
		while(fr.nextLine()) {
			if(ssns.contains(fr.getString(2))) {
				BEmployee be = new BEmployee(empHash.get(fr.getString(2)));
//				be.setHicNumber(stripDashes(fr.getString(3)));
				be.setMedicare(fr.getString(4).charAt(0));
				be.update();
			}
		}
		hsu.commitTransaction();
		hsu.close();
    }
}
