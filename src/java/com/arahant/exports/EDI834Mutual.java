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
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileReader;
import com.arahant.utils.FixedLengthFileWriter;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.HibernateSessionUtil;
import org.kissweb.StringUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class EDI834Mutual extends AEDI834 {

	public EDI834Mutual(String fileType)
	{
		super();
		this.fileType = fileType;
		ediLog = new EDILog("MUTUAL_EDI");
		vendorCode = "Mutual of Omaha";
		logger = new ArahantLogger(EDI834Mutual.class);
	}

	int count = 0;

    public String[] dumpEDI(CompanyBase vendor, BEDITransaction tran, boolean debug) {
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

		try {
			String filename = new BProperty("LargeReportDir").getValue() + File.separator + "EDI" + vendor.getInterchangeReceiverId().replace('-', '_') + "_" + icn + "_" + DateUtils.now() + "_" + (filenames.size() + 1);

			HibernateCriteriaUtil<Employee> hcu = hsu.createCriteria(Employee.class).orderBy(Employee.LNAME)
																					.orderBy(Employee.FNAME)
																					.joinTo(Employee.HR_BENEFIT_JOINS_WHERE_PAYING)
																					.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
																					.joinTo(HrBenefitConfig.HR_BENEFIT)
																					.eq(HrBenefit.BENEFIT_PROVIDER, vendor);
			HibernateCriteriaUtil<HrBenefit> nonDentalHcu = hsu.createCriteria(HrBenefit.class).eq(HrBenefit.BENEFIT_PROVIDER, vendor)
																							.joinTo(HrBenefit.BENEFIT_CATEGORY)
																							.ne(HrBenefitCategory.TYPE, HrBenefitCategory.DENTAL);
			if(!nonDentalHcu.exists()) {
				HibernateCriteriaUtil<HrBenefit> dentalHcu = hsu.createCriteria(HrBenefit.class).eq(HrBenefit.BENEFIT_PROVIDER, vendor)
																								.joinTo(HrBenefit.BENEFIT_CATEGORY)
																								.eq(HrBenefitCategory.TYPE, HrBenefitCategory.DENTAL);
				List<HrBenefit> dentalBenefitsToDo = dentalHcu.list();

				edi = new EDI(filename + ".edi.txt");
				edi.X12_setup('*', '~', '\n');
				isa_seg(EDI.makecn(icn), debug, vendor.getInterchangeSenderId(), vendor.getInterchangeReceiverId());

				int dentalBenefitCount = 0;

				for (HrBenefit bene : dentalBenefitsToDo) {
					gs_seg(EDI.makecn(gcn), vendor.getApplicationSenderId(), vendor.getApplicationReceiverId());

					groupNumber = bene.getGroupId();
					header(transactionSetNumber, vendorCode, vendor.getInterchangeSenderId(), cd.getName(), vendor.getInterchangeReceiverId(), gcn); //cd.getFederalEmployerId()

					doInactives(bene); //deal with history records - policies that have ended
					doActives(bene);

					transactionSetTrailer(transactionSetNumber);
					transactionSetNumber++;
					functionalGroupTrailer(EDI.makecn(gcn));
					gcn++;

					dentalBenefitCount++;
				}
				filenames.add(filename + ".edi.txt");
				functionInterchangeTrailer(EDI.makecn(icn), dentalBenefitCount);
				edi.close();
			}
			else {
				HibernateScrollUtil<Employee> scr = hcu.scroll();
				List<Employee> employeesDone = new ArrayList<Employee>();
				fw = new FixedLengthFileWriter(filename + ".600.txt");
				while(scr.next()) {
					Employee emp = scr.get();
					if(employeesDone.contains(emp))
						continue;

					doInactives600(emp, vendor); //deal with history records - policies that have ended
					doActives600(emp, vendor);
					employeesDone.add(emp);
				}
				gcn++;
				transactionSetNumber++;
				filenames.add(filename + ".600.txt");
				fw.close();
			}
			ediLog.close();
		}
		catch (FileNotFoundException ex) {
			throw new ArahantException("Can't create EDI file");
		}
		catch (IOException ioe) {
			throw new ArahantException("Can't create EDI file");
		}
        tran.setTransactionSetNumber(transactionSetNumber - 1);
        tran.setGCN(gcn - 1);
        tran.setICN(icn);

        mils = new Date().getTime() - mils;

        logger.info("took " + (mils / 1000) + " seconds");

        return filenames.toArray(new String[filenames.size()]);
    }

    private void doInactives(HrBenefit bene) {
        benefitName = bene.getName();
		String currentSubscriber = "";

        HibernateCriteriaUtil<HrBenefitJoinH> hcu = hsu.createCriteria(HrBenefitJoinH.class);
        hcu.orderBy(HrBenefitJoinH.PAYING_PERSON_ID);
        hcu.eq(HrBenefitJoinH.RECORD_CHANGE_TYPE, 'D');
        hcu.ne(HrBenefitJoinH.POLICY_END_DATE, 0);
        hcu.gt(HrBenefitJoinH.RECORD_CHANGE_DATE, lastDumpDate);
        hcu.joinTo(HrBenefitJoinH.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bene);
        hcu.ne(HrBenefitJoinH.COVERAGE_START_DATE, 0);

        benefitType = hsu.createCriteria(HrBenefitCategory.class).selectFields(HrBenefitCategory.TYPE).joinTo(HrBenefitCategory.HRBENEFIT).eq(HrBenefit.BENEFITID, bene.getBenefitId()).intValue();

        HibernateScrollUtil<HrBenefitJoinH> hscr = hcu.scroll();
		List<HrBenefitJoinH> depJoins = new ArrayList<HrBenefitJoinH>();

        while (hscr.next()) {
            if (++count % 50 == 0)
                logger.info(count);

            HrBenefitJoinH bj = hscr.get();

			if(!(bj.getPayingPerson() instanceof Employee)) {
				logger.info(bj.getPayingPerson().getNameFML() + " was not transmitted because they are paying for a policy, but is not an employee.");
				continue;
			}

			maintenanceTypeCode = "024";

			if(!bj.getPayingPersonId().equals(currentSubscriber)) {
				currentSubscriber = bj.getPayingPersonId();

				Collections.sort(depJoins, comparator);
				setCoverageLevel(depJoins);
				for(HrBenefitJoinH h : depJoins) {
					if(h.getCoverageEndDate() == 0)
						h.setCoverageEndDate(h.getPolicyEndDate());
					try {
						setBenefitJoinData(h, benefitType);
					}
					catch (Throwable t) {
						continue;
					}
					ediMemberLevelDetail();
				}
				depJoins.clear();
				flushData();
			}

			depJoins.add(bj);
        }

        hscr.close();
    }

    private List<String> doActives(HrBenefit bene) {
        benefitName = bene.getName();
		String currentSubscriber = "";

        HibernateCriteriaUtil<HrBenefitJoin> hcu = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.BENEFIT_DECLINED, 'N').eq(HrBenefitJoin.APPROVED, 'Y');
        hcu.orderBy(HrBenefitJoin.POLICY_START_DATE);
        hcu.joinTo(HrBenefitJoin.PAYING_PERSON).orderBy(Person.PERSONID);
        hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bene);
        hcu.ne(HrBenefitJoin.COVERAGE_START_DATE, 0);
		hcu.gtOrEq(HrBenefitJoin.COVERAGE_END_DATE, DateUtils.now(), 0);

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

            int covdate = bj.getCoverageStartDate();
            int covend = bj.getCoverageEndDate();
            if (covdate != 0 && bj.getRecordChangeDate().after(lastDumpDate))
                maintenanceTypeCode = "021";
			else if (covend != 0 && bj.getRecordChangeDate().after(lastDumpDate))
                maintenanceTypeCode = "024";
			else if (bj.getPolicyEndDate() != 0 && bj.getRecordChangeDate().after(lastDumpDate))
                maintenanceTypeCode = "024";
			else if (bj.getRecordChangeDate().after(lastDumpDate))
				maintenanceTypeCode = "001";
			else if (covend != 0 && bj.getRecordChangeDate().before(lastDumpDate))
                continue;

            if (bj.getPolicyEndDate() != 0 && bj.getRecordChangeDate().before(lastDumpDate))
                continue;

            if(!(bj.getPayingPerson() instanceof Employee)) {
				logger.info(bj.getPayingPerson().getNameFML() + " was not transmitted because they are paying for a policy, but is not an employee.");
				continue;
			}

			if(!bj.getPayingPersonId().equals(currentSubscriber)) {
				flushData();
				currentSubscriber = bj.getPayingPersonId();
				Collections.sort(depJoins, comparator);
				for(HrBenefitJoin j : depJoins) {
					if(j.getCoverageEndDate() == 0)
						j.setCoverageEndDate(j.getPolicyEndDate());
					setBenefitJoinData(j, benefitType);
					ediMemberLevelDetail();
				}
				depJoins.clear();
			}
			depJoins.add(bj);
			continue;
        }
        hscr.close();

        return payingPersons;
    }

	private void doInactives600(Employee emp, CompanyBase vendor) {
		List<HrEmplDependent> deps = hsu.createCriteria(HrEmplDependent.class).eq(HrEmplDependent.EMPLOYEE, emp).list();
		for(int i = -1; i < deps.size(); i++) {
			if (++count % 50 == 0)
				logger.info("600: " + count);
			boolean first = true;
			boolean hasInactives = false;
			boolean[] benefits = {false, false, false, false, false, false, false, false};
			HashMap<Integer, IHrBenefitJoin> beneJoins = new HashMap<Integer, IHrBenefitJoin>();
			HibernateCriteriaUtil<HrBenefitJoinH> hcu = hsu.createCriteria(HrBenefitJoinH.class);
			hcu.eq(HrBenefitJoinH.RECORD_CHANGE_TYPE, 'D');
			hcu.ne(HrBenefitJoinH.POLICY_END_DATE, 0);
			hcu.gt(HrBenefitJoinH.RECORD_CHANGE_DATE, lastDumpDate);
			hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFIT_PROVIDER, vendor)
																						  .joinTo(HrBenefit.BENEFIT_CATEGORY)
																						  .ne(HrBenefitCategory.TYPE, HrBenefitCategory.DENTAL);
			hcu.ne(HrBenefitJoinH.COVERAGE_START_DATE, 0);
			hcu.eq(HrBenefitJoinH.PAYING_PERSON, emp);
			if(i != -1)
				hcu.eq(HrBenefitJoinH.COVERED_PERSON_ID, deps.get(i).getDependentId());

			HibernateScrollUtil<HrBenefitJoinH> hscr = hcu.scroll();

			while (hscr.next()) {
				HrBenefitJoinH bjh = hscr.get();
				HrBenefit bene = bjh.getHrBenefitConfig().getHrBenefit();
				benefitType = hsu.createCriteria(HrBenefitCategory.class).selectFields(HrBenefitCategory.TYPE)
																		 .joinTo(HrBenefitCategory.HRBENEFIT)
																		 .eq(HrBenefit.BENEFITID, bene.getBenefitId())
																		 .intValue();
				benefitName = bene.getName();
				setBenefitJoinData(bjh, benefitType);
				int covdate = bjh.getCoverageStartDate();
				int covend = bjh.getCoverageEndDate();
				if (covdate != 0 && bjh.getRecordChangeDate().after(lastDumpDate))
					maintenanceTypeCode = "021";
				if (covend != 0 && bjh.getRecordChangeDate().after(lastDumpDate))
					maintenanceTypeCode = "024";
				if (bjh.getPolicyEndDate() != 0 && bjh.getRecordChangeDate().after(lastDumpDate))
					maintenanceTypeCode = "024";
				if (covend != 0 && bjh.getRecordChangeDate().before(lastDumpDate))
					continue;
				if (bjh.getPolicyEndDate() != 0 && bjh.getRecordChangeDate().before(lastDumpDate))
					continue;

				hasInactives = true;
				if(first && (subscriber || i != -1)) {
					demo_seg();
					bill_group_seg();
					employ_data_seg();
					salary_seg();
					class_data_seg();
					smoking_status_seg();
					first = false;
				}

				String beneCatName = bene.getHrBenefitCategory().getDescription().toLowerCase();
				String beneName = bene.getName();

				if(beneCatName.contains("dental") || beneName.contains("dental")) {
					beneJoins.put(0, bjh);
					benefits[0] = true;
				}
				else if((beneCatName.contains("basic") && beneCatName.contains("life")) || (beneName.contains("basic") && beneName.contains("life"))) {
					beneJoins.put(1, bjh);
					benefits[1] = true;
				}
				else if((beneCatName.contains("ad") && beneCatName.contains("&")) || (beneName.contains("ad") && beneName.contains("&"))) {
					beneJoins.put(2, bjh);
					benefits[2] = true;
				}
				else if((beneCatName.contains("std") || beneCatName.contains("short term")) || (beneName.contains("std") || beneName.contains("short term"))) {
					beneJoins.put(3, bjh);
					benefits[3] = true;
				}
				else if((beneCatName.contains("ltd") || beneCatName.contains("long term")) || (beneName.contains("ltd") || beneName.contains("long term"))) {
					beneJoins.put(4, bjh);
					benefits[4] = true;
				}
				else if(((beneCatName.contains("supplemental") || beneCatName.contains("voluntary")) && beneCatName.contains("employee") && beneCatName.contains("life")) ||
						((beneName.contains("supplemental") || beneName.contains("voluntary")) && beneName.contains("employee") && beneName.contains("life"))) {
					beneJoins.put(5, bjh);
					benefits[5] = true;
				}
				else if(((beneCatName.contains("supplemental") || beneCatName.contains("voluntary")) && beneCatName.contains("spous") && beneCatName.contains("life") && !bene.getName().toLowerCase().contains("metlife")) ||
						((beneName.contains("supplemental") || beneName.contains("voluntary")) && beneName.contains("spous") && beneName.contains("life") && !beneName.toLowerCase().contains("metlife"))) {
					beneJoins.put(6, bjh);
					benefits[6] = true;
				}
				else if(((beneCatName.contains("supplemental") || beneCatName.contains("voluntary")) && (beneCatName.contains("dependent") || beneCatName.contains("child")) && beneCatName.contains("life")) ||
						((beneName.contains("supplemental") || beneName.contains("voluntary")) && (beneName.contains("dependent") || beneName.contains("child")) && beneName.contains("life"))) {
					beneJoins.put(7, bjh);
					benefits[7] = true;
				}
				else if(((beneCatName.contains("supplemental") || beneCatName.contains("voluntary")) && beneCatName.contains("life")) ||
						((beneName.contains("supplemental") || beneName.contains("voluntary")) && beneName.contains("life"))) {
					beneJoins.put(5, bjh);
					benefits[5] = true;
				}
			}
			if(hasInactives) {
				eligibility_seg(beneJoins, benefits, i == -1);
				end_seg();
			}

			flushData();
			hscr.close();
		}
    }

    private void doActives600(Employee emp, CompanyBase vendor) {
		List<HrEmplDependent> deps = hsu.createCriteria(HrEmplDependent.class).eq(HrEmplDependent.EMPLOYEE, emp)
																			  .eq(HrEmplDependent.DATE_INACTIVE, 0)
																			  .list();
		for(int i = -1; i < deps.size(); i++) {
			if (++count % 50 == 0)
				logger.info("600: " + count);
			boolean first = true;
			boolean hasActives = false;
			boolean[] benefits = {false, false, false, false, false, false, false, false};
			HashMap<Integer, IHrBenefitJoin> beneJoins = new HashMap<Integer, IHrBenefitJoin>();
			HibernateCriteriaUtil<HrBenefitJoin> hcu = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.BENEFIT_DECLINED, 'N').eq(HrBenefitJoin.APPROVED, 'Y');
			hcu.orderBy(HrBenefitJoin.POLICY_START_DATE);
			hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFIT_PROVIDER, vendor);
			hcu.ne(HrBenefitJoin.COVERAGE_START_DATE, 0);
			hcu.eq(HrBenefitJoin.PAYING_PERSON, emp);
			if(i != -1)
				hcu.eq(HrBenefitJoinH.COVERED_PERSON_ID, deps.get(i).getDependentId());

			HibernateScrollUtil<HrBenefitJoin> hscr = hcu.scroll();

			while (hscr.next()) {
				HrBenefitJoin bj = hscr.get();
				HrBenefit bene = bj.getHrBenefitConfig().getHrBenefit();
				benefitType = new BHRBenefitCategory(new BHRBenefitJoin(bj).getBenefitCategoryId()).getTypeId();

				benefitName = bene.getName();
				setBenefitJoinData(bj, benefitType);
				int covdate = bj.getCoverageStartDate();
				int covend = bj.getCoverageEndDate();
				if (covdate != 0 && bj.getRecordChangeDate().after(lastDumpDate))
					maintenanceTypeCode = "021";
				if (covend != 0 && bj.getRecordChangeDate().after(lastDumpDate))
					maintenanceTypeCode = "024";
				if (bj.getPolicyEndDate() != 0 && bj.getRecordChangeDate().after(lastDumpDate))
					maintenanceTypeCode = "024";
				if (covend != 0 && bj.getRecordChangeDate().before(lastDumpDate))
					continue;
				if (bj.getPolicyEndDate() != 0 && bj.getRecordChangeDate().before(lastDumpDate))
					continue;

				String beneCatName = bene.getHrBenefitCategory().getDescription().toLowerCase();
				String beneName = bene.getName().toLowerCase();
				hasActives = true;

				if(first && (subscriber || i != -1)) {
					demo_seg();
					bill_group_seg();
					employ_data_seg();
					salary_seg();
					class_data_seg();
					smoking_status_seg();
					first = false;
				}

				if(beneCatName.contains("dental") || beneName.contains("dental")) {
					beneJoins.put(0, bj);
					benefits[0] = true;
				}
				else if((beneCatName.contains("basic") && beneCatName.contains("life")) || (beneName.contains("basic") && beneName.contains("life"))) {
					beneJoins.put(1, bj);
					benefits[1] = true;
				}
				else if((beneCatName.contains("ad") && beneCatName.contains("&")) || (beneName.contains("ad") && beneName.contains("&"))) {
					beneJoins.put(2, bj);
					benefits[2] = true;
				}
				else if((beneCatName.contains("std") || beneCatName.contains("short term")) || (beneName.contains("std") || beneName.contains("short term"))) {
					beneJoins.put(3, bj);
					benefits[3] = true;
				}
				else if((beneCatName.contains("ltd") || beneCatName.contains("long term")) || (beneName.contains("ltd") || beneName.contains("long term"))) {
					beneJoins.put(4, bj);
					benefits[4] = true;
				}
				else if(((beneCatName.contains("supplemental") || beneCatName.contains("voluntary")) && beneCatName.contains("employee") && beneCatName.contains("life")) ||
						((beneName.contains("supplemental") || beneName.contains("voluntary")) && beneName.contains("employee") && beneName.contains("life"))) {
					beneJoins.put(5, bj);
					benefits[5] = true;
				}
				else if(((beneCatName.contains("supplemental") || beneCatName.contains("voluntary")) && beneCatName.contains("spous") && beneCatName.contains("life") && !bene.getName().toLowerCase().contains("metlife")) ||
						((beneName.contains("supplemental") || beneName.contains("voluntary")) && beneName.contains("spous") && beneName.contains("life") && !beneName.toLowerCase().contains("metlife"))) {
					beneJoins.put(6, bj);
					benefits[6] = true;
				}
				else if(((beneCatName.contains("supplemental") || beneCatName.contains("voluntary")) && (beneCatName.contains("dependent") || beneCatName.contains("child")) && beneCatName.contains("life")) ||
						((beneName.contains("supplemental") || beneName.contains("voluntary")) && (beneName.contains("dependent") || beneName.contains("child")) && beneName.contains("life"))) {
					beneJoins.put(7, bj);
					benefits[7] = true;
				}
				else if(((beneCatName.contains("supplemental") || beneCatName.contains("voluntary")) && beneCatName.contains("life")) ||
						((beneName.contains("supplemental") || beneName.contains("voluntary")) && beneName.contains("life"))) {
					beneJoins.put(5, bj);
					benefits[5] = true;
				}
			}
			hscr.close();

			if(hasActives) {
				if(first) {
					HrBenefitJoin beneJoin = new HrBenefitJoin();
					for(int j = 0; j< benefits.length; j++)
						if(benefits[j])
							beneJoin = (HrBenefitJoin)beneJoins.get(j);

					subscriber = true;
					setPayingPerson(emp.getPersonId(), new BHRBenefitJoin(beneJoin).getBenefitId());
					setCoveredPerson(emp.getPersonId());
					demo_seg();
					bill_group_seg();
					employ_data_seg();
					salary_seg();
					class_data_seg();
					smoking_status_seg();
					first = false;
				}
				eligibility_seg(beneJoins, benefits, i == -1);
				end_seg();
			}
		}
	}

	@Override
	protected void ediMemberLevelDetail() {
		INS_insured_benefit();
		REF_SSN_subscriber();
		REF_subscriber_group_number();  //May not be needed
		REF_subscriber_class();
		REF_subscriber_subgroup_number();
//                REF_subscriber_mutually_defined();   //Might need this segment
		DTP_employment_date();
		DTP_eligibility_date();
		DTP_employment_term_date();
		NM1_member_name();
//                PER_contact_info();
		N3_address_information();
		N4_geographic_location();
		DMG_demographic_info();
		HD_health_coverage();
		DTP_benefit_start_date();
		DTP_benefit_end_date();
	}

	@Override
	protected void header(int transactionSetNumber, String vendor, String vendorId, String sponsorName, String sponsorId, int gcn) {
        edi.SEG("ST");
        edi.ST(1, "834");
        edi.ST(2, EDI.makecn(transactionSetNumber));
        edi.EOS();

        edi.SEG("BGN");
        edi.ST(1, "00");
        edi.ST(2, String.format("%06d", gcn));//reference number
        edi.D8(3);
        edi.CT(4);
        edi.ST(5, "");
        edi.ST(6, "");
        edi.ST(7, "");
        edi.ST(8, "4"); //2 change file, 4 full file
        edi.EOS();

        edi.SEG("REF");
        edi.ST(1, "38");
        edi.ST(2, groupNumber);
        edi.EOS();

        edi.SEG("DTP");
        edi.SF(1, "303");
        edi.ST(2, "D8");
//        policyStart = 0;
//        if (0 == policyStart)  //TODO: find out what to use instead of policyStart
//            logger.info("Policy start date missing");
        edi.SF(3, DateUtils.getDateCCYYMMDD(DateUtils.now()));
        edi.EOS();

        edi.SEG("N1");
        edi.ST(1, "P5");
        edi.ST(2, sponsorName);
        edi.ST(3, "FI");
        edi.ST(4, stripDashes(sponsorId));
        edi.EOS();

        edi.SEG("N1");
        edi.ST(1, "IN");
        edi.ST(2, vendor);
        edi.ST(3, "FI");
        edi.ST(4, stripDashes(vendorId));
        edi.EOS();

//		edi.SEG("N1");
//        edi.ST(1, "TV");
//        edi.ST(2, vendor);
//        edi.ST(3, "FI");
//        edi.ST(4, stripDashes(vendorId));
//        edi.EOS();
    }

	@Override
	protected void HD_health_coverage() {
		edi.SEG("HD");
		edi.SF(1, maintenanceTypeCode);
		edi.SF(2, "");
		if(benefitName.toLowerCase().contains("dental"))
			edi.SF(3, "DEN");
		edi.SF(4, plan);
		edi.SF(5, coverageLevelCode);
		edi.EOS();
	}

	private void demo_seg() {
		try {
			subscriber = stripDashes(depSSN).equals(stripDashes(subscriberIdentifier));

			fw.writeField(1, 8, DateUtils.now());								//Date of transaction
			fw.writeField(2, 8, groupNumber);									//Group Id
			String relationCode;
			switch(Integer.parseInt(relationshipCode)) {
				case 1: relationCode = (genderCode.equals("M") ? "H" : "W"); break;
				case 19: relationCode = (genderCode.equals("M") ? "S" : "D"); break;
				case 18: relationCode = "M"; break;
				default: relationCode = "I";
			}
			fw.writeField(3, 1, relationCode);									//Relationship code
			fw.writeField(4, 9, stripDashes(subscriberIdentifier));				//Subscriber ID/SSN
			fw.writeField(5, 1, "");											//Filler
			fw.writeField(6, 35, memberLastName);								//Last Name
			fw.writeField(7, 15, memberFirstName);								//First Name
			fw.writeField(8, 1, "");											//Filler
			fw.writeField(9, 10, "");											//Filler
			fw.writeField(10, 1, genderCode);									//Gender
			fw.writeField(11, 8, memberBirthDate);								//DOB
			fw.writeField(12, 1, maritalStatusCode);							//Marital status
			if(subscriber) {
				fw.writeField(13, 10, memberExtRef);								//Employee ID
				if(isEmpty(memberAddressLine1.trim()) && isEmpty(memberAddressLine2.trim())) {
					memberAddressLine1 = memberAddressLine2;
					memberAddressLine2 = "";
				}
				fw.writeField(14, 30, memberAddressLine1);							//Address 1
				fw.writeField(15, 10, "");											//Filter
				fw.writeField(16, 30, memberAddressLine2);							//Address 2
				fw.writeField(17, 10, "");											//Filter
				fw.writeField(18, 40, "");											//Address 3
				fw.writeField(19, 19, memberAddressCity);							//City
				fw.writeField(20, 2, memberAddressState);							//State
				fw.writeField(21, 11, memberAddressZip.length() > 5 ? memberAddressZip.substring(0,5) : memberAddressZip);	//Zip code
				fw.writeField(22, 4, "");											//Country Code
				if(!isEmpty(homePhone))
					fw.writeField(23, 20, stripDashes(stripParatheses(homePhone)).replaceAll(" ", ""));	//Home phone
				else
					fw.writeField(23, 20, "");
				if(!isEmpty(workPhone))
					fw.writeField(24, 20, stripDashes(stripParatheses(workPhone)).replaceAll(" ", ""));	//Work phone
				else
					fw.writeField(24, 20, "");
				fw.writeField(25, 4, "");										//Work extension
				fw.writeField(26, 8, employmentStartDate);						//Hire date
			}
			else {
				fw.writeField(13, 10, "");
				fw.writeField(14, 30, "");
				fw.writeField(15, 10, "");
				fw.writeField(16, 30, "");
				fw.writeField(17, 10, "");
				fw.writeField(18, 40, "");
				fw.writeField(19, 19, "");
				fw.writeField(20, 2, "");
				fw.writeField(21, 11, "");
				fw.writeField(22, 4, "");
				fw.writeField(23, 20, "");
				fw.writeField(24, 20, "");
				fw.writeField(25, 4, "");
				fw.writeField(26, 8, "");
			}
										
			fw.writeField(27, 8, Integer.parseInt(coverageStartDate) > employmentStartDate ?
								 Integer.parseInt(coverageStartDate) : employmentStartDate);	//Member effective date
			fw.writeField(28, 9, isValidSSN(stripDashes(depSSN).trim()) ? stripDashes(depSSN) : "");	//Member SSN
			fw.writeField(29, 1, "");											//Filler
			fw.writeField(30, 8, "");											//Full-time student data
			fw.writeField(31, 50, "");											//Filler
		}
		catch (IOException e) {
			throw new ArahantException("Can't write demographics segment to 600 file");
		}
	}

	private void bill_group_seg() {
		try {
			if(subscriber) {
				fw.writeField(32, 8, Integer.parseInt(coverageStartDate) > employmentStartDate ?
									  Integer.parseInt(coverageStartDate) : employmentStartDate);	//Member effective date
				fw.writeField(33, 4, isEmpty(groupVendorId) ? "0001" : groupVendorId);				//Subgroup ID ("0001" as default for main company)
			}
			else {
				fw.writeField(32, 8, "");
				fw.writeField(33, 4, "");
			}
		}
		catch(IOException e) {
			throw new ArahantException("Can't write billing group segment to 600 file");
		}
	}

	private void employ_data_seg() {
		try {
			fw.writeField(35, 8, "");											//Fillers
			fw.writeField(36, 2, "");
			fw.writeField(36, 20, "");
			fw.writeField(36, 8, "");
			fw.writeField(36, 8, "");
			fw.writeField(36, 1, "");
			fw.writeField(36, 23, "");
		}
		catch(IOException e) {
			throw new ArahantException("Can't write billing group segment to 600 file");
		}
	}

	private void salary_seg() {
		try {
			String salary = "";

			if(subscriber) {
				fw.writeField(43, 8, salaryEffectiveDate > employmentStartDate ?
									 salaryEffectiveDate : employmentStartDate);	//Wage effective date
				fw.writeField(44, 1, salaryPeriod);									//Wage pay period (H - Hourly, A - Annual)
				for(int i = 0; i < (16 - String.valueOf((int)(salaryAmount * 100)).length()); i++)
					salary = salary.concat("0");
				fw.writeField(45, 16, salary.concat(String.valueOf((int)(salaryAmount * 100))));	//Wage amount
			}
			else {
				fw.writeField(43, 8, "");
				fw.writeField(44, 1, "");
				fw.writeField(45, 16, "");
			}
			
			fw.writeField(47, 8, "");
			fw.writeField(48, 1, "");
			fw.writeField(49, 2, "");
			fw.writeField(50, 16, "");
			fw.writeField(51, 8, "");
			fw.writeField(52, 1, "");
			fw.writeField(53, 2, "");
			fw.writeField(54, 16, "");
			fw.writeField(55, 8, "");
			fw.writeField(56, 1, "");
			fw.writeField(57, 2, "");
			fw.writeField(58, 16, "");
			fw.writeField(59, 8, "");
			fw.writeField(60, 1, "");
			fw.writeField(61, 2, "");
			fw.writeField(62, 16, "");
		}
		catch(IOException e) {
			throw new ArahantException("Can't write salary segment to 600 file");
		}
	}

	private void class_data_seg() {
		try {
			if(subscriber) {
				fw.writeField(63, 8, Integer.parseInt(coverageStartDate) > employmentStartDate ?
									  Integer.parseInt(coverageStartDate) : employmentStartDate);	//Member effective date
				fw.writeField(64, 4, benefitStatusCode + "001");
			}
			else {
				fw.writeField(63, 8,"");
				fw.writeField(64, 4, "");
			}
		}
		catch(IOException e) {
			throw new ArahantException("Can't write class data segment to 600 file");
		}
	}

	private void smoking_status_seg() {
		try {
			fw.writeField(65, 8, "");											//Fillers
			fw.writeField(66, 1, "");
			fw.writeField(67, 3, "");
			fw.writeField(68, 1, "");
			fw.writeField(69, 2, "");
			fw.writeField(70, 30, "");
			fw.writeField(71, 90, "");
		}
		catch(IOException e) {
			throw new ArahantException("Can't write class data segment to 600 file");
		}
	}

	private void eligibility_seg(HashMap<Integer, IHrBenefitJoin> beneJoins, boolean[] benefits, boolean employee) {
		final HashMap<Integer, String> beneCodes =
			new HashMap<Integer, String>() {
				{
					put(0, "D");
					put(1, "1");
					put(2, "a");
					put(3, "S");
					put(4, "T");
					put(5, "3");
					put(6, "4");
					put(7, "5");
				}
			};

		try {
			for(int i = 0; i < benefits.length; i++) {
				if(benefits[i] == true) {
					Employee emp = hsu.createCriteria(Employee.class).eq(Employee.SSN, subscriberIdentifier).first();
					int coverageStartDate = beneJoins.get(i).getCoverageStartDate();
					int coverageEndDate = beneJoins.get(i).getCoverageEndDate();
					double coverageAmount = beneJoins.get(i).getAmountCovered();
					setCoverageLevel(beneJoins.get(i));
					String changeReason = beneJoins.get(i).getChangeDescription();
					String planId = beneJoins.get(i).getHrBenefitConfig().getHrBenefit().getPlanId();
					String amount = "";

					boolean activeTerm = (coverageEndDate != 0 && coverageEndDate <= DateUtils.now())   //Coverage has ended for dependent
										 &&
										 (hsu.createCriteria(IHrBenefitJoin.class).eq(IHrBenefitJoin.PAYING_PERSON, emp)
																				  .eq(IHrBenefitJoin.COVERED_PERSON, emp)
																				  .eq(IHrBenefitJoin.HR_BENEFIT_CONFIG_ID, beneJoins.get(i).getHrBenefitConfigId())
																				  .gtOrEq(IHrBenefitJoin.COVERAGE_END_DATE, DateUtils.now(), 0)
																				  .first() != null);   //Policy join is still active (Employee still enrolled)
					if(employee || activeTerm) {
						fw.writeField(1, beneCodes.get(i));
						fw.writeField(8, coverageStartDate);
						String eligibility = subscriber ? (changeReason.toLowerCase().contains("term") ? "TM" : "EN") : (changeReason.toLowerCase().contains("term") ? "SE" : "EN");
						fw.writeField(2, eligibility);
						fw.writeField(10, planId);
						fw.writeField(1, coverageLevelCode);
						if(Character.isDigit(beneCodes.get(i).charAt(0)) && !beneCodes.get(i).equals("1")) {
							fw.writeField(4, "");									//Filler
							fw.writeField(8, policyStart);							//Coverage amount effective date
							for(int j = 0; j < (10 - String.valueOf((int)(coverageAmount)).length()); j++)
								amount = amount.concat("0");
							fw.writeField(10, amount.concat(String.valueOf((int)(coverageAmount)))); //Coverage amount
							fw.writeField(11, "");									//Filler
						}
						else
							fw.writeField(33, "");
					}
					else {
						fw.writeField(1, "");
						fw.writeField(8, "");
						fw.writeField(2, "");
						fw.writeField(10, "");
						fw.writeField(1, "");
						fw.writeField(33, "");
					}
				}
				else {
					fw.writeField(1, "");
					fw.writeField(8, "");
					fw.writeField(2, "");
					fw.writeField(10, "");
					fw.writeField(1, "");
					fw.writeField(33, "");
				}
			}
		}
		catch(IOException e) {
			throw new ArahantException("Can't write eligibility segment to 600 file");
		}
	}

	private void end_seg() {
		try {
			fw.writeField("\n");
		}
		catch(IOException e) {
			throw new ArahantException("Can't write end segment to 600 file");
		}
	}

	@Override
	protected void setCoverageLevel(IHrBenefitJoin bj) {
		coverageLevelCode = "C";

		HrBenefitConfig config = bj.getHrBenefitConfig();

		if((config.getSpouseEmployee() == 'Y' ||
		    config.getSpouseNonEmployee() == 'Y') &&
		    config.getMaxChildren() == 1)
				coverageLevelCode = "B";
		if(((config.getSpouseNonEmpOrChildren() == 'Y') ||
			(config.getSpouseEmpOrChildren() == 'Y')))
			if(config.getMaxChildren() > 1 || config.getMaxChildren() == 0)
				coverageLevelCode = "A";
			else if(config.getMaxChildren() == 1)
				coverageLevelCode = "B";
		if(config.getChildren() == 'Y' && config.getMaxChildren() == 1)
				coverageLevelCode = "D";
	}

	protected <T extends IHrBenefitJoin> void setCoverageLevel(List<T> bjList) {
		if(!bjList.isEmpty()) {
			if(bjList.size() == 1)
				coverageLevelCode = "C";
			else if(bjList.size() > 2)
				coverageLevelCode = "A";
			else {
				IHrBenefitJoin bj = bjList.get(1);
				BHREmplDependent dep = new BHREmplDependent(bj.getPayingPersonId(), bj.getCoveredPersonId());

				if (bj.getHrBenefitConfig().getSpouseEmployee() == 'Y' ||
					bj.getHrBenefitConfig().getSpouseNonEmployee() == 'Y')
					coverageLevelCode = "B";

				if ((bj.getHrBenefitConfig().getSpouseNonEmpOrChildren() == 'Y') ||
						(bj.getHrBenefitConfig().getSpouseEmpOrChildren() == 'Y') ||
						(bj.getHrBenefitConfig().getChildren() == 'Y')) {
					if(bj.getHrBenefitConfig().getMaxChildren() != 1)
						coverageLevelCode = "A";
					else if(dep.getRelationship() == 'S')
						coverageLevelCode = "B";
					else if(dep.getRelationship() == 'C')
						coverageLevelCode = "D";
					else
						coverageLevelCode = "D";
				}

				if (bj.getHrBenefitConfig().getChildren() == 'Y' && bj.getHrBenefitConfig().getMaxChildren() == 1)
					coverageLevelCode = "D";

				if (bj.getHrBenefitConfig().getChildren() == 'Y' && bj.getHrBenefitConfig().getMaxChildren() == 1 &&
						(bj.getHrBenefitConfig().getSpouseEmployee() == 'Y' || bj.getHrBenefitConfig().getSpouseNonEmployee() == 'Y'))
					coverageLevelCode = "A";
			}
		}
	}

    public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.setCurrentCompany(hsu.get(CompanyDetail.class,"00001-0000073334"));
		List<String> notFound = new ArrayList<String>();

		HashMap<String, String> map = new HashMap<String, String>();
		HibernateCriteriaUtil<BenefitClass> hcu = hsu.createCriteria(BenefitClass.class);
		HibernateScrollUtil<BenefitClass> scr = hcu.scroll();
		while(scr.next()) {
			BBenefitClass bb = new BBenefitClass(scr.get());
			map.put(bb.getName(), bb.getId());
			System.out.println("HRSP:     " + bb.getName());
		}

		DelimitedFileReader fr = new DelimitedFileReader("/home/xichen/Desktop/G000AGVV_Subgroup_07072011.csv");
		while(fr.nextLine()) {
			System.out.println("      " + fr.getString(1));
			String id = map.get(fr.getString(1));
			BBenefitClass bbc = new BBenefitClass();

			if(!StringUtils.isEmpty(id))
				System.out.println("insert into vendor_group values (\"" + bbc.create() + "\", " + "\"00001-0000073334\"" + ", \"" + id + "\", \"" + fr.getString(0) + "\");");
			else
				notFound.add(fr.getString(1));
		}


//        boolean testingEDI = false;
//+
//        if (testingEDI) {
//            logger.info(new Date().getTime() / 1000);
//
//            //String vendorId = "00001-0000072600";  //Blue Care Network DRC
//            String vendorId = "00001-0000072577";  //BCN New Passages
//            HibernateSessionUtil hsu = ArahantSession.getHSU();
//
////            hsu.beginTransaction();
////            BProperty bp = new BProperty("BCNID");
////            bp.setValue(vendorId);
////            bp.update();
////            hsu.commitTransaction();
//
//
//            //ArahantSession.getHSU().setCurrentPerson(ArahantSession.getHSU().get(Person.class, personId));
//            //hsu.setCurrentPersonToArahant();
//            //hsu.setCurrentCompany(new BCompany("00000-0000000005").getBean());  //DRC
//
//            hsu.setCurrentPerson(new BPerson("00001-0000389211").getPerson());
//            hsu.setCurrentCompany(new BCompany("00001-0000072555").getBean());  //New Passages
//
//            final BEDITransaction x = new BEDITransaction();
//            x.create();
//
//            x.setReceiver(vendorId);
//            x.setStatus(0);
//            x.setStatusDesc("Started");
//
//            hsu.beginTransaction();
//
//            x.send834Insurance();
//
//            hsu.commitTransaction();
//
//            hsu.close();
//        }
//
//        File logFile = new File("Mutual_EDI_LOG.txt");
//
//        try {
//            boolean fileCreated = logFile.createNewFile();
//
//            if (fileCreated) {
//                BufferedWriter out = new BufferedWriter(new FileWriter(logFile));
//                out.write("New File Test" + "\n");
//                out.flush();
//                out.close();
//            } else {
//                BufferedWriter out = new BufferedWriter(new FileWriter(logFile, true));
//                out.write("Append Test" + "\n");
//                out.flush();
//                out.close();
//            }
//
//        } catch (IOException ex) {
//            Logger.getLogger(EDI834Mutual.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }
}
