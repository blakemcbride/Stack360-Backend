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
import com.arahant.utils.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import jess.JessException;
import jess.Rete;
import org.kissweb.StringUtils;

public class EDI834Consociates {

	public EDI834Consociates(String fileType)
	{
		this.fileType = fileType;
	}

	private String fileType = "F";
	private static final ArahantLogger logger = new ArahantLogger(EDI834Consociates.class);
	private Rete r = new Rete();

	protected IHrBenefitJoin getPreviousBenefitJoin(HibernateSessionUtil hsu, IHrBenefitJoin bj) {
		IHrBenefitJoin ibj = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, bj.getPayingPersonId()).eq(HrBenefitJoin.COVERED_PERSON_ID, bj.getCoveredPersonId()).eq(HrBenefitJoin.POLICY_END_DATE, DateUtils.addDays(bj.getPolicyStartDate(), -1)).gt(HrBenefitJoin.HISTORY_DATE, lastDumpDate).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).ne(HrBenefit.BENEFITID, "00001-0000000026") //not in hospital supplemental
				.eq(HrBenefit.BENEFIT_CATEGORY, bj.getHrBenefitConfig().getHrBenefit().getHrBenefitCategory()).first();

		if (ibj == null)
			ibj = hsu.createCriteria(HrBenefitJoinH.class).eq(HrBenefitJoinH.PAYING_PERSON_ID, bj.getPayingPersonId()).eq(HrBenefitJoinH.COVERED_PERSON_ID, bj.getCoveredPersonId()).eq(HrBenefitJoinH.POLICY_END_DATE, DateUtils.addDays(bj.getPolicyStartDate(), -1)).ne(HrBenefitJoinH.POLICY_START_DATE, bj.getPolicyStartDate()).gt(HrBenefitJoinH.HISTORY_DATE, lastDumpDate).eq(HrBenefitJoinH.RECORD_CHANGE_TYPE, 'D').joinTo(HrBenefitJoinH.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).ne(HrBenefit.BENEFITID, "00001-0000000026") //not in hospital supplemental
					.eq(HrBenefit.BENEFIT_CATEGORY, bj.getHrBenefitConfig().getHrBenefit().getHrBenefitCategory()).first();
		return ibj;
	}

	private class EDIDumpPersonalInfo implements Comparable<EDIDumpPersonalInfo> {

		private EDI edi;
		private String coverageEndDate;
		private String frequencyCode = "U";
		private String raceOrEthnicity = "7";
		private int memberBirthDate;
		private int memberOldBirthDate;
		private String memberAddressZip;
		private String memberAddressState;
		private String memberAddressLine1;
		private String workPhone;
		private String homePhone;
		private String memberMiddleName;
		private String memberFirstName;
		private String memberLastName;
		private String memberOldMiddleName;
		private String memberOldFirstName;
		private String memberOldLastName;
		private String memberAddressCity;
		private String maritalStatusCode;
		private String coverageStartDate;
		private String policyStartDate;
		private String policyEndDate;
		private String citizenStatusCode = "1";
		private String entityIdentifierCode = "I";
		private String insuranceLineCode;
		private String coverageLevelCode;
		private String genderCode;
		private String memberAddressLine2;
		private String statusInformationEffectiveDate;
		private String subscriberIdentifier;
		private String dateTimeQualifier;
		private String handicap;
		private String studentStatus;
		private String relationshipCode;
		private String benefitStatusCode;
		private String employmentCode;
		private int employmentStartDate;
		private int employmentEndDate;
		private String depSSN;
		private String maintenanceTypeCode = "001";
		private String groupNumber;
		private String subGroupNumber;
		private int benefitType;
		private String benefitName;
		private String personId;
		private String primaryCarePhysician;
		private String changeReason;
		private IHrBenefitJoin termBenefit = null;
		private boolean nameChange;
		private boolean dobChange;
		private boolean onCobra;
		private Person payingPerson;
		private IHrBenefitJoin benefitJoin;
		private boolean statusChange;
		private boolean orgGroupChange;
		private String benefitChangeCode = "";

		public EDIDumpPersonalInfo(EDI edi) {
			this.edi = edi;
		}

		private void setCoveredPerson(Person coveredPerson) {
			personId = coveredPerson.getPersonId();
			memberLastName = coveredPerson.getLname();
			memberFirstName = coveredPerson.getFname();
			memberMiddleName = coveredPerson.getMname();
			depSSN = coveredPerson.getUnencryptedSsn();
			if ("999-99-9999".equals(depSSN))
				depSSN = "000-00-0000";
			memberBirthDate = coveredPerson.getDob();
			studentStatus = "";
			handicap = coveredPerson.getHandicap() + "";
			genderCode = coveredPerson.getSex() + "";

			//check history, did name change?
			//ArahantSession.getHSU().createCriteria(PersonH.class);
		}

		private void setPayingPerson(Person payingPerson, String benefitId) {
			this.payingPerson = payingPerson;
			subscriberIdentifier = payingPerson.getUnencryptedSsn();
			//		r.watchAll();
			if ("999-99-9999".equals(subscriberIdentifier))
				subscriberIdentifier = "000-00-0000";
			JessBean jb = new JessBean();
			jb.loadTableFacts("hr_empl_status_history", " where employee_id='" + payingPerson.getPersonId() + "'");

			int effectiveDate = benefitJoin.getPolicyStartDate();
			if (DateUtils.now() > effectiveDate)  //in case they retired, etc, use now, or future dates, no past dates
				effectiveDate = DateUtils.now();
			AIProperty groupProp = new AIProperty(r, "InsuranceGroupIds", benefitId, payingPerson.getPersonId(), effectiveDate);
			groupNumber = groupProp.getValue();
			subGroupNumber = groupProp.getValue2();
			//	groupNumber="";
			//	subGroupNumber="";
			if (groupNumber == null) {
				groupNumber = "";//	throw new Error("no group number");
				logger.info("No group number for " + payingPerson.getPersonId() + " " + payingPerson.getNameLFM() + " " + benefitName);
			}
			if (subGroupNumber == null)
				subGroupNumber = "";
		}

		@Override
		public String toString() {
			return memberLastName + ", " + memberFirstName + "\n";
		}

		private void IDC_insuranceCard() {
			edi.SEG("IDC");
			edi.SF(1, "0");
			edi.SF(2, "H");
			edi.SF(3, "");
			edi.SF(4, nameChange ? "2" : "1");

			edi.EOS();
		}

		private boolean isEmpty(String s) {
			return s == null || s.trim().equals("");
		}

		private void setAddress(BPerson p, BPerson dep) {

			if (!isEmpty(dep.getHomePhone()))
				homePhone = dep.getHomePhone();
			else
				homePhone = p.getHomePhone();

			if (!isEmpty(dep.getWorkPhoneNumber()))
				homePhone = dep.getWorkPhoneNumber();
			else
				homePhone = p.getWorkPhoneNumber();


			if (!isEmpty(dep.getStreet())) {
				memberAddressLine1 = dep.getStreet();
				memberAddressLine2 = dep.getStreet2();

				memberAddressCity = dep.getCity();
				memberAddressState = dep.getState();
				memberAddressZip = dep.getZip();
			} else {
				memberAddressLine1 = p.getStreet();
				memberAddressLine2 = p.getStreet2();

				memberAddressCity = p.getCity();
				memberAddressState = p.getState();
				memberAddressZip = p.getZip();
			}
		}

		private EDIDumpPersonalInfo setBenefitJoinData(IHrBenefitJoin bj, int benefitType) {

			//set end dates BEFORE falling back to any prior joins
			int covend = bj.getCoverageEndDate();
			int policyend = bj.getPolicyEndDate();

			if (policyend != 0)
				policyend = DateUtils.addDays(policyend, 1);

			if (covend != 0)
				covend = DateUtils.addDays(covend, 1);

			coverageEndDate = DateUtils.getDateCCYYMMDD(covend);
			policyEndDate = DateUtils.getDateCCYYMMDD(policyend);



			HibernateSessionUtil hsu = ArahantSession.getHSU();
			//while I have previous that is the same thing other than billing
			IHrBenefitJoin tbj = getPreviousBenefitJoin(hsu, bj);

			//roll back to first of this sequence with same coverage and benefit
			while (tbj != null) {
				if (getCoverageLevel(tbj).equals(getCoverageLevel(bj)) && tbj.getUsingCOBRA() == bj.getUsingCOBRA() && tbj.getHrBenefitConfig().getHrBenefit().getBenefitId().equals(bj.getHrBenefitConfig().getHrBenefit().getBenefitId()))
					bj = tbj;
				else
					break;

				tbj = getPreviousBenefitJoin(hsu, tbj);
			}
			benefitJoin = bj;
			setLocation(bj);
			benefitName = bj.getHrBenefitConfig().getHrBenefit().getName();
			setRelationshipCode(bj);

			benefitStatusCode = (bj.getUsingCOBRA() == 'Y') ? "C" : "A";
			this.benefitType = benefitType;
			onCobra = bj.getUsingCOBRA() == 'Y';

			setPCP(bj);

			BPerson p = new BPerson(bj.getPayingPersonId());


			//	if (p.getLastName().equalsIgnoreCase("KNOTTS"))
			//		logger.info("At knotts");

			BEmployee emp = null;
			if (p.isEmployee())
				emp = new BEmployee(bj.getPayingPersonId());


			setEmploymentCode(bj, emp);


			setPayingPerson(hsu.get(Person.class, bj.getPayingPersonId()), hsu.get(HrBenefitConfig.class, bj.getHrBenefitConfigId()).getHrBenefit().getBenefitId());


			Person coveredPerson = hsu.get(Person.class, bj.getCoveredPersonId());
			setCoveredPerson(coveredPerson);

			PersonH personHistory = null;
			if (coveredPerson.getRecordChangeDate().after(lastDumpDate))
				//something changed, find the history record that is latest, but preceeds current dump date
				personHistory = ArahantSession.getHSU().createCriteria(PersonH.class).lt(PersonH.RECORD_CHANGE_DATE, lastDumpDate).eq(PersonH.PERSONID, bj.getCoveredPersonId()).orderByDesc(PersonH.RECORD_CHANGE_DATE).first();
			if (personHistory != null) {
				//now I should have the earliest person history for the current person
				//that comes after the last export date
				memberOldFirstName = personHistory.getFname();
				memberOldLastName = personHistory.getLname();
				memberOldMiddleName = personHistory.getMname();
				memberOldBirthDate = personHistory.getDob();

				dobChange = memberOldBirthDate != memberBirthDate;
				nameChange = !memberOldFirstName.equals(memberFirstName) || !memberOldLastName.equals(memberLastName) || !memberOldMiddleName.equals(memberMiddleName);
			}

			dateTimeQualifier = "300";

			if (bj.getUsingCOBRA() == 'Y')
				dateTimeQualifier = "340";

			statusInformationEffectiveDate = DateUtils.getDateCCYYMMDD(bj.getPolicyStartDate());


			BPerson d = new BPerson(bj.getCoveredPersonId());

			setAddress(p, d);

			setMaritalStatus(emp);

			HrBenefitConfig hbc = ArahantSession.getHSU().get(HrBenefitConfig.class, bj.getHrBenefitConfigId());

			insuranceLineCode = hbc.getHrBenefit().getInsuranceCode();

			setCoverageLevel(bj);

			int covdate = bj.getCoverageStartDate();

			coverageStartDate = DateUtils.getDateCCYYMMDD(covdate);

			if (coverageStartDate.equals("") && bj.getCoverageEndDate() == 0)
				logger.info("coverage start date missing");


			policyStartDate = DateUtils.getDateCCYYMMDD(bj.getPolicyStartDate());

			changeReason = bj.getChangeDescription();

			//	logger.info(bj.getCoveredPerson().getNameLFM());

			if (covdate > 0)
				if (bj.getRecordChangeType() == 'N' && bj.getRecordChangeDate().after(lastDumpDate)) {
					//check to see if they had this BENEFIT in the past
					if (!hsu.createCriteria(HrBenefitJoinH.class).eq(HrBenefitJoinH.PAYING_PERSON, bj.getPayingPerson()).eq(HrBenefitJoinH.COVERED_PERSON_ID, bj.getCoveredPersonId()).ne(HrBenefitJoinH.BENEFIT_JOIN_ID, bj.getBenefitJoinId()).eq(HrBenefitJoinH.COVERAGE_START_DATE, bj.getCoverageStartDate()).le(HrBenefitJoinH.HISTORY_DATE, lastDumpDate).joinTo(HrBenefitJoinH.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bj.getHrBenefitConfig().getHrBenefit()).exists())
						maintenanceTypeCode = "021";
				} else
					//check history to see if the new was since the last run
					if (hsu.createCriteria(HrBenefitJoinH.class).eq(HrBenefitJoinH.BENEFIT_JOIN_ID, bj.getBenefitJoinId()).eq(HrBenefitJoinH.RECORD_CHANGE_TYPE, 'N').gt(HrBenefitJoinH.HISTORY_DATE, lastDumpDate).exists()) {
						if (!hsu.createCriteria(HrBenefitJoinH.class).eq(HrBenefitJoinH.PAYING_PERSON, bj.getPayingPerson()).eq(HrBenefitJoinH.COVERED_PERSON_ID, bj.getCoveredPersonId()).ne(HrBenefitJoinH.BENEFIT_JOIN_ID, bj.getBenefitJoinId()).le(HrBenefitJoinH.HISTORY_DATE, lastDumpDate).eq(HrBenefitJoinH.COVERAGE_START_DATE, bj.getCoverageStartDate()).joinTo(HrBenefitJoinH.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bj.getHrBenefitConfig().getHrBenefit()).exists())
							maintenanceTypeCode = "021";
					} else {
						//since the last run, did coverage start change from 0 to non-zero?
						HrBenefitJoinH hist = hsu.createCriteria(HrBenefitJoinH.class).eq(HrBenefitJoinH.BENEFIT_JOIN_ID, bj.getBenefitJoinId()).gt(HrBenefitJoinH.HISTORY_DATE, lastDumpDate).orderBy(HrBenefitJoinH.HISTORY_DATE).first();

						if (hist != null && hist.getCoverageStartDate() == 0)
							if (!hsu.createCriteria(HrBenefitJoinH.class).eq(HrBenefitJoinH.PAYING_PERSON, bj.getPayingPerson()).eq(HrBenefitJoinH.COVERED_PERSON_ID, bj.getCoveredPersonId()).ne(HrBenefitJoinH.BENEFIT_JOIN_ID, bj.getBenefitJoinId()).le(HrBenefitJoinH.HISTORY_DATE, lastDumpDate).eq(HrBenefitJoinH.COVERAGE_START_DATE, bj.getCoverageStartDate()).joinTo(HrBenefitJoinH.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bj.getHrBenefitConfig().getHrBenefit()).exists())
								maintenanceTypeCode = "021";

						//did the last history have a coverage end date but now it was cleared out?
						if(!maintenanceTypeCode.equals("021"))
						{
							hist = hsu.createCriteria(HrBenefitJoinH.class).eq(HrBenefitJoinH.BENEFIT_JOIN_ID, bj.getBenefitJoinId()).orderByDesc(HrBenefitJoinH.HISTORY_DATE).first();
							if(hist != null && hist.getCoverageEndDate() > 0 && bj.getCoverageEndDate() == 0)
								maintenanceTypeCode = "025"; //reinstate!
						}
					}

			//if it's a 21 - new, check to see if ever had cancelled same health in past
			//if so, its a 25, reinstatement
			if (maintenanceTypeCode.equals("021"))
				if (hsu.createCriteria(HrBenefitJoinH.class).ne(HrBenefitJoinH.COVERAGE_END_DATE, 0).ne(HrBenefitJoinH.COVERAGE_START_DATE, 0).eq(HrBenefitJoinH.COVERED_PERSON_ID, bj.getCoveredPersonId()).eq(HrBenefitJoinH.HR_BENEFIT_CONFIG, bj.getHrBenefitConfig()).exists())
					maintenanceTypeCode = "025";
				else {
					//can't be a name change on a new
					nameChange = false;
					dobChange = false;
				}


			if (policyend != 0)
				maintenanceTypeCode = "024";

			if (covend != 0)
				maintenanceTypeCode = "024";


			if (bj.getPayingPersonId().equals(bj.getCoveredPersonId())) {
				//Did my status change between last time and this time?

				statusChange = hsu.createCriteria(HrEmplStatusHistory.class).eq(HrEmplStatusHistory.EMPLOYEE, bj.getPayingPerson()).gt(HrEmplStatusHistory.EFFECTIVEDATE, bj.getPolicyStartDate()).exists();

				orgGroupChange = hsu.createCriteria(OrgGroupAssociation.class).eq(OrgGroupAssociation.PERSON, bj.getPayingPerson()).gt(OrgGroupAssociation.HISTORY_DATE, lastDumpDate).exists();

			}

			//if I'm a 24 and there is a newer benefit, set the benefit change code to 22
			if (ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).ne(HrBenefitJoin.HR_BENEFIT_CONFIG, benefitJoin.getHrBenefitConfig()).eq(HrBenefitJoin.PAYING_PERSON, benefitJoin.getPayingPerson()).gt(HrBenefitJoin.POLICY_START_DATE, benefitJoin.getPolicyStartDate()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFIT_CATEGORY, benefitJoin.getHrBenefitConfig().getHrBenefit().getHrBenefitCategory()).exists())
				benefitChangeCode = "22";

			return this;

		}

		private void setCoverageLevel(IHrBenefitJoin bj) {
			coverageLevelCode = getCoverageLevel(bj);
		}

		@SuppressWarnings("empty-statement")
		private void setEmploymentCode(IHrBenefitJoin bj, BEmployee emp) {
			employmentCode = "TE";

			if (emp != null) {
				BHREmplStatusHistory x = emp.getLastStatusHistory();
				if (x.getStatusId().equals("00001-0000000001")) //TODO: move to AI engine property
					employmentCode = "FT";
				if (x.getStatusId().equals("00001-0000000000")) //TODO: move to AI engine property
					employmentCode = "TE";
				if (x.getStatusId().equals("00001-0000000003")) //TODO: move to AI engine property
					employmentCode = "L1";
				if (x.getStatusId().equals("00001-0000000002")) //TODO: move to AI engine property
					employmentCode = "RT";
				if (x.getStatusId().equals("00001-0000000004")) //TODO: move to AI engine property
					employmentCode = "PT";

				employmentStartDate = emp.getEmploymentDate();
			}
			if (employmentCode.equals("TE")) {
				try {
					employmentStartDate = emp.getLastActiveStatusHistory().getEffectiveDate();
				} catch (Exception e) {
					try {
						employmentStartDate = emp.getEmploymentDate();
					} catch (Exception x) {
					}
					;
				}
				try {
					employmentEndDate = emp.getLastStatusDate();
				} catch (Exception e) {
				}
			}
		}

		private void setLocation(IHrBenefitJoin bj) {

			if (bj.getUsingCOBRA() == 'Y')
				//if this is a dependent, fake the location
				if (bj.getPayingPerson().getOrgGroupAssociations().size() == 0) {
					HrEmplDependent dep = ArahantSession.getHSU().createCriteria(HrEmplDependent.class).eq(HrEmplDependent.PERSON, bj.getPayingPerson()).first();
					if (dep != null) {
						String ogid = dep.getEmployee().getOrgGroupAssociations().iterator().next().getOrgGroupId();
						ArahantSession.AICmd("(assert (org_group_association (person_id \"" + bj.getPayingPersonId() + "\")(org_group_id \"" + ogid + "\")))");

					}
				}

		}

		private void setMaritalStatus(BEmployee emp) {
			maritalStatusCode = "I";

			if (emp != null)
				if (emp.getActiveSpouse(DateUtils.now()) != null)
					maritalStatusCode = "M";
				else if (emp.getActiveSpouse(0) != null)
					maritalStatusCode = "U";
		}

		private void setPCP(IHrBenefitJoin bj) {
			if (bj.getComments() == null || "".equals(bj.getComments().trim()))
				primaryCarePhysician = null;
			else
				primaryCarePhysician = bj.getComments().trim();
		}

		private void setRelationshipCode(IHrBenefitJoin bj) {
			relationshipCode = "18";
			if (!bj.getPayingPersonId().equals(bj.getCoveredPersonId())) {
				BHREmplDependent dep = new BHREmplDependent(bj.getPayingPersonId(), bj.getCoveredPersonId());

				switch (dep.getRelationship()) {
					case 'S':
						relationshipCode = "01";
						break;
					case 'C':
						relationshipCode = "19";
						break;
					default:
						//TODO: not a legal relationship - we need them to fix the others
						//	logger.info("Skipping relationship "+dep.getRelationship());
						relationshipCode = "19";
					//	continue;
				}
			}
		}

		private void setTermBenefit(IHrBenefitJoin ibj) {
			termBenefit = ibj;

			/*		if (termBenefit==null)
			return;
			if (coverageLevelCode.equals(getCoverageLevel(termBenefit)) 
			&& termBenefit.getHrBenefitConfig().getHrBenefit().getName().equals(benefitName))
			{
			termBenefit=null;
			setBenefitJoinData(ibj, benefitType);

			}
			 */
		}

		private String stripDashes(String s) {
			String ret = "";

			if (s == null)
				return "";

			StringTokenizer toks = new StringTokenizer(s, "-");
			while (toks.hasMoreTokens())
				ret += toks.nextToken();

			return ret;
		}

		private void REF_SSN_subscriber() {
			edi.SEG("REF");
			edi.SF(1, "0F");//Subscriber Number Qualifier 	
			edi.SF(2, stripDashes(subscriberIdentifier));
			edi.EOS();
		}

		private void REF_subscriber_group_number() {
			edi.SEG("REF");
			edi.SF(1, "1L");
			edi.SF(2, groupNumber);
			edi.EOS();
		}

		private void REF_subscriber_subgroup_number() {
			//Consociates says don't do this for dependents,
			//Delta Dental wants it
			if (stripDashes(depSSN).equals(stripDashes(subscriberIdentifier))) {
				edi.SEG("REF");
				edi.SF(1, "17");
				edi.SF(2, subGroupNumber);
				edi.EOS();
			}
		}

		private void DTP_employment_date() {
			if (employmentStartDate != 0) {
				edi.SEG("DTP");
				edi.SF(1, "336");
				edi.ST(2, "D8");
				edi.DC(3, employmentStartDate);
				edi.EOS();
			} else if (maintenanceTypeCode.equals("021")) {
				edi.SEG("DTP");
				edi.SF(1, "336");
				edi.ST(2, "D8");
				edi.ST(3, coverageStartDate);
				edi.EOS();
			}
		}

		private void DTP_employment_term_date() {
			if (employmentEndDate != 0) {
				edi.SEG("DTP");
				edi.SF(1, "357");
				edi.ST(2, "D8");
				edi.DC(3, employmentEndDate);
				edi.EOS();
			}
		}

		private void NM1_member_name() {
			int commaPos = memberLastName.indexOf(',');
			String suffix = "";

			if (commaPos != -1) {
				try {
					suffix = memberLastName.substring(commaPos + 1).trim();
				} catch (Exception e) {
					//last character was comma, don't worry about it
				}
				memberLastName = memberLastName.substring(0, commaPos).trim();

			}
			edi.SEG("NM1");
			edi.SF(1, "IL");
			edi.SF(2, "1");
			edi.SF(3, memberLastName);
			edi.SF(4, memberFirstName);
			edi.SF(5, memberMiddleName);
			edi.SF(6, "");
			edi.SF(7, suffix);

			if (!"000000000".equals(stripDashes(depSSN))) {
				edi.SF(8, "34");
				edi.SF(9, stripDashes(depSSN));
			} else {
				edi.SF(8, "");
				edi.SF(9, "");
			}

			edi.EOS();
		}

		private void NM1_member_old_name() {
			int commaPos = memberOldLastName.indexOf(',');
			String suffix = "";

			if (commaPos != -1) {
				try {
					suffix = memberOldLastName.substring(commaPos + 1).trim();
				} catch (Exception e) {
					//last character was comma, don't worry about it
				}
				memberOldLastName = memberOldLastName.substring(0, commaPos).trim();

			}
			edi.SEG("NM1");
			edi.SF(1, "70");
			edi.SF(2, "1");
			edi.SF(3, memberOldLastName);
			edi.SF(4, memberOldFirstName);
			edi.SF(5, memberOldMiddleName);
			edi.SF(6, "");
			edi.SF(7, suffix);

			if (!"000000000".equals(stripDashes(depSSN))) {
				edi.SF(8, "34");
				edi.SF(9, stripDashes(depSSN));
			} else {
				edi.SF(8, "");
				edi.SF(9, "");
			}

			edi.EOS();
		}

		private void NM1_member_new_name() {
			int commaPos = memberLastName.indexOf(',');
			String suffix = "";

			if (commaPos != -1) {
				try {
					suffix = memberLastName.substring(commaPos + 1).trim();
				} catch (Exception e) {
					//last character was comma, don't worry about it
				}
				memberLastName = memberLastName.substring(0, commaPos).trim();

			}
			edi.SEG("NM1");
			edi.SF(1, "74");
			edi.SF(2, "1");
			edi.SF(3, memberLastName);
			edi.SF(4, memberFirstName);
			edi.SF(5, memberMiddleName);
			edi.SF(6, "");
			edi.SF(7, suffix);

			if (!"000000000".equals(stripDashes(depSSN))) {
				edi.SF(8, "34");
				edi.SF(9, stripDashes(depSSN));
			} else {
				edi.SF(8, "");
				edi.SF(9, "");
			}

			edi.EOS();
		}

		private void N3_address_information() {
			edi.SEG("N3");
			//shift address if messed up
			if (memberAddressLine1 == null || memberAddressLine1.equals("")) {
				memberAddressLine1 = memberAddressLine2;
				memberAddressLine2 = "";
			}
			edi.SF(1, memberAddressLine1);
			edi.SF(2, memberAddressLine2);
			edi.EOS();
		}

		private void N4_geographic_location() {
			edi.SEG("N4");
			edi.SF(1, memberAddressCity);
			edi.SF(2, memberAddressState);
			edi.SF(3, stripDashes(memberAddressZip).trim());
			edi.EOS();
		}

		private void DMG_demographic_info() {
			edi.SEG("DMG");
			if (memberBirthDate != 0)
				edi.SF(1, "D8");
			else
				edi.SF(1, "");
			edi.DC(2, memberBirthDate);
			edi.SF(3, genderCode);
			edi.EOS();
		}

		private void DMG_old_demographic_info() {
			edi.SEG("DMG");
			if (memberOldBirthDate != 0)
				edi.SF(1, "D8");
			else
				edi.SF(1, "");
			edi.DC(2, memberOldBirthDate);
			edi.SF(3, genderCode);
			edi.EOS();
		}

		private void PCP_provider() {
			if (primaryCarePhysician != null && benefitType != HrBenefitCategory.DENTAL && !maintenanceTypeCode.equals("024")) {
				edi.SEG("LX");
				edi.SF(1, "1");
				edi.EOS();
				edi.SEG("NM1");
				edi.SF(1, "P3");
				edi.SF(2, "2");
				edi.SF(3, primaryCarePhysician);
				edi.SF(4, "");
				edi.SF(5, "");
				edi.SF(6, "");
				edi.SF(7, "");
				edi.SF(8, "");
				edi.SF(9, "");
				edi.SF(10, "72");
				edi.SF(11, "");
				edi.EOS();
			}
		}

		private void HD_coverage_change() {
			//if change from one to same cov, dont show change

			if (termBenefit == null)
				return;

			//do this only when changing deductible to deductible, but not copay to deduct, for example
			if (!termBenefit.getHrBenefitConfig().getHrBenefit().getBenefitId().equals(benefitJoin.getHrBenefitConfig().getHrBenefit().getBenefitId())) {
				benefitChangeCode = "22";
				return;
			}

			edi.SEG("HD");
			edi.SF(1, "024"); //Consociates just wants a 030

			edi.SF(2, "");

			String beneName = termBenefit.getHrBenefitConfig().getHrBenefit().getName();

			switch (benefitType) {
				case HrBenefitCategory.DENTAL:
					edi.SF(3, "DEN");  //Should be D for dental
					edi.SF(4, beneName); //30 for Dental
					break;
				case HrBenefitCategory.HEALTH:
					edi.SF(3, "HLT");
					edi.SF(4, beneName);
					break;
				case HrBenefitCategory.LONG_TERM_CARE:
					edi.SF(3, "LTC");
					edi.SF(4, beneName);
					break;
				case HrBenefitCategory.SHORT_TERM_CARE:
					edi.SF(3, "STC");
					edi.SF(4, beneName);
					break;
				case HrBenefitCategory.VISION:
					edi.SF(3, "VIS");
					edi.SF(4, beneName);
					break;

				default:
					edi.SF(3, insuranceLineCode);
					edi.SF(4, beneName);
			}

			if (stripDashes(depSSN).equals(stripDashes(subscriberIdentifier)))
				edi.SF(5, getCoverageLevel(termBenefit));
			else
				edi.SF(5, "");
			edi.EOS();

			edi.SEG("DTP");
			edi.SF(1, "348");
			edi.ST(2, "D8");

			edi.SF(3, DateUtils.getDateCCYYMMDD(termBenefit.getPolicyStartDate()));
			edi.EOS();

			edi.SEG("DTP");
			edi.SF(1, "349");
			edi.ST(2, "D8");
			edi.SF(3, policyStartDate);  //end this one same day as the other one starts, per Aaron at consociates
			edi.EOS();
		}

		private void HD_health_coverage() {
			edi.SEG("HD");
			//has there been a change?

			//"021" addition, "024" cancel or term, "030" no change
			//String changeCode="030";	

			edi.SF(1, maintenanceTypeCode); //Consociates just wants a 030

			edi.SF(2, "");


			/*
			 *AG - Preventive Care/Wellness 
			AH - 24 Hour Care
			AJ - Medicare Risk
			AK - Mental Health
			DCP - Dental Capitation
			DEN - Dental
			EPO - Exclusive Provider Organization
			FAC - Facility
			HE - Hearing
			HLT - Health
			HMO - Health Maintenance Organization
			LTC - Long-Term Care
			LTD - Long-Term Disability
			MM - Major Medical
			MOD - Mail Order Drug
			PDG - Prescription Drug
			POS - Point of Service
			PPO - Preferred Provider Organization
			PRA - Practitioners
			STD - Short-Term Disability
			UR - Utilization Review
			VIS - Vision

			 */
			switch (benefitType) {
				case HrBenefitCategory.DENTAL:
					edi.SF(3, "DEN");  //Should be D for dental
					edi.SF(4, benefitName); //30 for Dental
					break;
				case HrBenefitCategory.HEALTH:
					edi.SF(3, "HLT");
					edi.SF(4, benefitName);
					break;
				case HrBenefitCategory.LONG_TERM_CARE:
					edi.SF(3, "LTC");
					edi.SF(4, benefitName);
					break;
				case HrBenefitCategory.SHORT_TERM_CARE:
					edi.SF(3, "STC");
					edi.SF(4, benefitName);
					break;
				case HrBenefitCategory.VISION:
					edi.SF(3, "VIS");
					edi.SF(4, benefitName);
					break;

				default:
					edi.SF(3, insuranceLineCode);
					edi.SF(4, benefitName);

			}

			if (stripDashes(depSSN).equals(stripDashes(subscriberIdentifier)))
				edi.SF(5, coverageLevelCode);
			else
				edi.SF(5, "");
			edi.EOS();
		}

		private void DTP_benefit_start_date() {
			edi.SEG("DTP");
			edi.SF(1, "348");
			edi.ST(2, "D8");
			if ("".equals(policyStartDate))
				logger.info("Policy start date missing");

			edi.SF(3, policyStartDate);
			edi.EOS();
		}

		private void DTP_benefit_end_date() {
			if ("024".equals(maintenanceTypeCode)) {
				edi.SEG("DTP");
				edi.SF(1, "349");
				edi.ST(2, "D8");
				if (!policyEndDate.equals(""))
					edi.SF(3, policyEndDate);
				else
					edi.SF(3, coverageEndDate);

				edi.EOS();
			}
		}

		private void INS_insured_benefit() {
			edi.SEG("INS");

			edi.ST(1, stripDashes(depSSN).equals(stripDashes(subscriberIdentifier)) ? "Y" : "N");
			edi.ST(2, relationshipCode);
			edi.ST(3, (termBenefit == null) ? maintenanceTypeCode : "001");


			String reasonCode = "AI";//means not giving reason for change
			if (changeReason.trim().equalsIgnoreCase("Divorce"))
				reasonCode = "01";
			if (changeReason.trim().equalsIgnoreCase("Birth, Adoption or Legal Custody of a Child"))
				reasonCode = "02";
			if (changeReason.trim().equalsIgnoreCase("Death"))
				reasonCode = "03";
			if (changeReason.trim().startsWith("Terming"))
				reasonCode = "08";
			if (changeReason.trim().equalsIgnoreCase("Elected COBRA"))
				reasonCode = "41";
			if (changeReason.trim().equalsIgnoreCase("Open Enrollment"))
				reasonCode = "14";
			if (changeReason.trim().equalsIgnoreCase("Spouse Gaining Other Coverage"))
				reasonCode = "14";
			if (changeReason.trim().equalsIgnoreCase("Gaining Other Coverage"))
				reasonCode = "14";
			if (changeReason.trim().equalsIgnoreCase("New Hire"))
				reasonCode = "28";
			if (changeReason.trim().equalsIgnoreCase("Marriage"))
				reasonCode = "32";
			if (changeReason.trim().equalsIgnoreCase("LOA - Declined Benefits"))
				reasonCode = "38";
			if (changeReason.trim().equalsIgnoreCase("LOA - Termed for Nonpayment"))
				reasonCode = "38";
			if (changeReason.trim().equalsIgnoreCase("Rehired"))
				reasonCode = "41";
			if (changeReason.trim().equalsIgnoreCase("LOA - Returned to Work"))
				reasonCode = "41";
			if (changeReason.trim().equalsIgnoreCase("Dependent Returned to College"))
				reasonCode = "41";
			if (changeReason.trim().equalsIgnoreCase("Part Time"))
				reasonCode = "AI";

			if (!maintenanceTypeCode.equals("024"))
				if (reasonCode.equals("08") || reasonCode.equals("03") || reasonCode.equals("14") || reasonCode.equals("01"))
					reasonCode = "AI";


			if (maintenanceTypeCode.equals("001"))
				reasonCode = "AI";

			if (termBenefit != null && !termBenefit.getHrBenefitConfig().getHrBenefit().getBenefitId().equals(benefitJoin.getHrBenefitConfig().getHrBenefit().getBenefitId()))
				benefitChangeCode = "22";

			if (!isEmpty(benefitChangeCode))
				reasonCode = benefitChangeCode;

			edi.ST(4, reasonCode);

			edi.ST(5, benefitStatusCode);
			edi.ST(6, "");

			String code = "";

			//if already cobra, don't set cobra letter statuses
			if (maintenanceTypeCode.equals("024") && !onCobra) {
				if (changeReason.trim().startsWith("Dependent Ineligible"))
					code = "7";
				if (changeReason.trim().equalsIgnoreCase("Divorce"))
					code = "5";
				if (changeReason.trim().equalsIgnoreCase("Part Time"))
					code = "2";
				if (changeReason.trim().startsWith("Terming"))
					code = "1";
				if (changeReason.trim().equalsIgnoreCase("Death"))
					code = "4";
			}

			//If death and retiree, don't set cobra letter statuses
			if (code.equals("4")) {
				//if the paying person is retiree, don't send
				BPerson bp = new BPerson(payingPerson);
				if (bp.isEmployee()) {
					BEmployee bemp = bp.getBEmployee();

					if (bemp.getLastStatusId().equals("00001-0000000002")) //WMCO specific, retiree
						code = "";
				}
			}

			edi.ST(7, code);
			edi.ST(8, employmentCode);
			edi.ST(9, "");  //student status off for Consociates
			edi.ST(10, (handicap == null) ? "N" : handicap);
			edi.EOS();
		}

		private void REF_subscriber_carrier_id() {
			edi.SEG("REF");

			if (benefitType == HrBenefitCategory.DENTAL) {
				edi.ST(1, "DX");
				edi.ST(2, "DDPTN");
			} else {
				if (isEmpty(subGroupNumber))
					throw new ArahantException("Subgroups missing");
				edi.ST(1, "DX");
				edi.ST(2, subGroupNumber);
			}

			edi.EOS();
		}

		private void DTP_individual_start_date() {
			edi.SEG("DTP");
			edi.SF(1, "356");
			edi.ST(2, "D8");
			if ("".equals(coverageStartDate))
				logger.info("Coverage start date missing");

			edi.SF(3, coverageStartDate);
			edi.EOS();
		}

		private void DTP_individual_end_date() {
			if (!"".equals(coverageEndDate)) {
				edi.SEG("DTP");
				edi.SF(1, "357");
				edi.ST(2, "D8");
				edi.SF(3, coverageEndDate);
				edi.EOS();
			}

		}

		private void ediMemberLevelDetail() {
			//logger.info("Ed det 1");
			INS_insured_benefit();
			REF_SSN_subscriber();
			REF_subscriber_group_number();
			REF_subscriber_subgroup_number();
			REF_subscriber_carrier_id();//member carrier id

			//logger.info("Ed det 2");


			//eligibility end date
			DTP_employment_date();
			DTP_individual_start_date();
			DTP_individual_end_date();
			if (nameChange)
				NM1_member_new_name();
			else
				NM1_member_name();
			N3_address_information();

			N4_geographic_location();
			DMG_demographic_info();

			if (nameChange || dobChange)
				NM1_member_old_name();

			if (dobChange)
				DMG_old_demographic_info();

			//logger.info("Ed det 3");
			//Incorrect member name
			HD_coverage_change();
			HD_health_coverage();
			DTP_benefit_start_date(); //benefit begin
			DTP_benefit_end_date();//benefit end


			//if it's a name change or new benefit, issue a card
			if (nameChange || maintenanceTypeCode.equals("021"))
				IDC_insuranceCard();


			//TODO: pcp turned off until they can handle it	PCP_provider(); //primary care provider

			//benefit group number
			//benefit subgroup id			
			//logger.info("Ed det 4");
		}

		@Override
		public int compareTo(EDIDumpPersonalInfo o) {

			if (relationshipCode.equals("18")) //payer
				return -1;

			if (o.relationshipCode.equals("18"))
				return 1;

			if (relationshipCode.equals("01")) //spouse
				return -1;

			if (o.relationshipCode.equals("01"))
				return 1;

			return 0; //19 - child

		}
	}

	private EDI edi;

	private void loadEngineWithOrgHierarchies() {
		try {

			ArahantSession.loadMinimumRules(r);
			ArahantSession.setAI(r);

			JessBean jb = new JessBean();

			jb.loadTableFacts("org_group_hierarchy");
			jb.loadTableFacts("org_group_association");
			jb.loadTableFacts("hr_employee_status");
			jb.loadTableFacts("hr_empl_status_history", "where employee_id='00001-0000005729'"); //load a default template, using laurie


			jb.getAIEngine().batch("WmCoEDIRules.jess");
		} catch (JessException ex) {
			JessUtils.reportError(ex, logger);
		}
	}

	private Date lastDumpDate;

	public String[] dumpEDI(CompanyBase vendor, BEDITransaction tran, boolean debug) {
		System.out.println("Dump edi");

		List<String> filenames = new LinkedList<String>();
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.dontAIIntegrate();

		int icn = tran.getStartingICN();
		int gcn = tran.getStartingGCN();
		int transactionSetNumber = tran.getStartingTscn();

		System.out.println("Dump edi 2");

		Calendar cal = Calendar.getInstance();
		cal.setTime(tran.getLastExportDate());
		cal.add(Calendar.HOUR_OF_DAY, -3); //take off a few hours because move to history may have happened
		lastDumpDate = cal.getTime();


//		lastDumpDate=DateUtils.getDate(20081130);

		loadEngineWithOrgHierarchies();

		long mils = new Date().getTime();


		AIProperty p = new AIProperty("SponsorId", vendor.getOrgGroupId());
		String sponsorId = p.getValue();

		System.out.println("Dump edi 3");

		List<HrBenefit> benefitsToDo = hsu.createCriteria(HrBenefit.class).eq(HrBenefit.BENEFIT_PROVIDER, vendor).list();

		System.out.println("Dump edi 4");

		for (HrBenefit bene : benefitsToDo) {
			payingPersons.clear();
			System.out.println("Doing benefit " + bene.getName());
			String filename = "";
			try {
				filename = new BProperty("LargeReportDir").getValue() + File.separator + "EDI" + vendor.getInterchangeReceiverId().replace('-', '_') + "_" + icn + "_" + DateUtils.now() + "_" + (filenames.size() + 1) + ".edi.txt";
				edi = new EDI(filename);
			} catch (IOException ex) {
				throw new ArahantException("Can't create EDI file");
			}
			edi.X12_setup('*', '~', '\n');
			isa_seg(EDI.makecn(icn), debug, vendor.getInterchangeSenderId(), vendor.getInterchangeReceiverId());
			gs_seg(EDI.makecn(gcn), vendor.getApplicationSenderId(), vendor.getApplicationReceiverId());

			header(transactionSetNumber, bene.getPlanName(), bene.getPayerId(), bene.getPlanId(), sponsorId, gcn);

			doActivesOnly(hsu, bene);
			doInactives(hsu, bene); //deal with history records - policies that have ended

			transactionSetTrailer(transactionSetNumber);
			transactionSetNumber++;
			functionalGroupTrailer(EDI.makecn(gcn));
			functionInterchangeTrailer(EDI.makecn(icn));
			edi.close();
			gcn++;
			filenames.add(filename);
		}

		System.out.println("Dump edi 5");

		//delete changes
		//  It seems these deletes never actually occur.  Further down in the code there is a rollback that nullifies this code (and the records are still there)
		hsu.createCriteria(EmployeeChanged.class).eq(EmployeeChanged.INTERFACEID, (short) 1).delete();

		//DO a dental run
		benefitsToDo.clear();
		benefitsToDo.addAll(hsu.createCriteria(HrBenefit.class).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.TYPE, HrBenefitCategory.DENTAL).list());
		for (HrBenefit bene : benefitsToDo) {
			System.out.println("Doing benefit " + bene.getName());

			String filename = "";
			try {
				filename = new BProperty("LargeReportDir").getValue() + File.separator + "EDI" + vendor.getInterchangeReceiverId().replace('-', '_') + "_" + icn + "_" + DateUtils.now() + "_" + (filenames.size() + 1) + ".edi.txt";
				edi = new EDI(filename);
			} catch (IOException ex) {
				throw new ArahantException("Can't create EDI file");
			}
			edi.X12_setup('*', '~', '\n');
			isa_seg(EDI.makecn(icn), debug, vendor.getInterchangeSenderId(), vendor.getInterchangeReceiverId());
			gs_seg(EDI.makecn(gcn), vendor.getApplicationSenderId(), vendor.getApplicationReceiverId());

			header(transactionSetNumber, bene.getPlanName(), bene.getPayerId(), bene.getPlanId(), sponsorId, gcn);
			doActivesDental(hsu, bene);
			doInactivesDental(hsu, bene); //deal with history records - policies that have ended 

			transactionSetTrailer(transactionSetNumber);
			transactionSetNumber++;
			functionalGroupTrailer(EDI.makecn(gcn));
			functionInterchangeTrailer(EDI.makecn(icn));
			edi.close();
			gcn++;
			filenames.add(filename);
		}

		System.out.println("Dump edi 6");

		tran.setTransactionSetNumber(transactionSetNumber - 1);
		tran.setGCN(gcn - 1);
		tran.setICN(icn);

		mils = new Date().getTime() - mils;

		logger.info("took " + (mils / 1000) + " seconds");

		return filenames.toArray(new String[filenames.size()]);
	}

	private void doInactives(HibernateSessionUtil hsu, HrBenefit bene) {

		System.out.println("Doing inactive");
		//ArahantSession.getAI().watchAll();
		int count = 0;

		List configIds = hsu.createCriteria(HrBenefitConfig.class).selectFields(HrBenefitConfig.BENEFIT_CONFIG_ID).eq(HrBenefitConfig.HR_BENEFIT, bene).list();

		System.out.println("Doing inactive 1");

		List<Person> peopleToDo = hsu.createCriteria(Person.class).joinTo(Person.EMPLOYEE_CHANGES).eq(EmployeeChanged.INTERFACEID, (short) 1).list();

		System.out.println("Doing inactive 2");

		//need to add any sponsors to the list
		List<Person> peopleToDo2 = hsu.createCriteria(Person.class).joinTo(Person.HR_BENEFIT_JOINS_WHERE_PAYING).in(HrBenefitJoin.COVERED_PERSON, peopleToDo).list();
		peopleToDo2.removeAll(peopleToDo);
		peopleToDo.addAll(peopleToDo2);

		System.out.println("Doing inactive 3");

		HibernateCriteriaUtil<HrBenefitJoinH> hcu = hsu.createCriteria(HrBenefitJoinH.class);
		hcu.notIn(HrBenefitJoinH.PAYING_PERSON_ID, payingPersons);
		hcu.orderBy(HrBenefitJoinH.PAYING_PERSON_ID);
		hcu.eq(HrBenefitJoinH.RECORD_CHANGE_TYPE, 'D');
		hcu.ne(HrBenefitJoinH.POLICY_END_DATE, 0);
		hcu.gt(HrBenefitJoinH.RECORD_CHANGE_DATE, lastDumpDate);
		hcu.in(HrBenefitJoinH.BENEFIT_CONFIG_ID, configIds);
		hcu.ne(HrBenefitJoinH.COVERAGE_START_DATE, 0);
		hcu.in(HrBenefitJoinH.PAYING_PERSON, peopleToDo);

		int benefitType = hsu.createCriteria(HrBenefitCategory.class).selectFields(HrBenefitCategory.TYPE).joinTo(HrBenefitCategory.HRBENEFIT).eq(HrBenefit.BENEFITID, bene.getBenefitId()).intValue();
		System.out.println("Doing inactive 4");

		HibernateScrollUtil<HrBenefitJoinH> hscr = hcu.scroll();

		List<EDIDumpPersonalInfo> batch = new LinkedList<EDIDumpPersonalInfo>();

		String currentPayingPerson = "";
		int currentStartDate = 0;

		System.out.println("Doing inactive 5");

		while (hscr.next()) {

			if (++count % 50 == 0) {
				logger.info(count);
				//break;
			}

			HrBenefitJoinH bj = hscr.get();

			if (payingPersons.contains(bj.getPayingPersonId()))
				continue; //already processed


			//if I have a newer benefit for this paying person, don't run this set
			if (bj.getPolicyEndDate() > 0)
				//For don, first time in, this query should fire as true and cause him to be skipped
				//logger.info("Checking for newer benefit on "+DateUtils.addDays(bj.getPolicyEndDate(),1));
				if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, bj.getPayingPersonId()).eq(HrBenefitJoin.POLICY_START_DATE, DateUtils.addDays(bj.getPolicyEndDate(), 1)).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bj.getHrBenefitConfig().getHrBenefit()).exists())
					continue;

			if (!bj.getPayingPersonId().equals(currentPayingPerson) || bj.getPolicyStartDate() != currentStartDate) {
				if (batch.size() > 0)
					payingPersons.add(currentPayingPerson);

				Collections.sort(batch);
				for (EDIDumpPersonalInfo i : batch)
					i.ediMemberLevelDetail();


				batch.clear();
				currentPayingPerson = bj.getPayingPersonId();
				currentStartDate = bj.getPolicyStartDate();
			}

			if (bj.getCoverageStartDate() == 0 && bj.getCoverageEndDate() == 0) //I have nothing I can say about it
				continue;

			EDIDumpPersonalInfo info = new EDIDumpPersonalInfo(edi);

			info.setBenefitJoinData(bj, benefitType);

			info.maintenanceTypeCode = "024"; //make sure it's a 024

			batch.add(info);
		}

		if (batch.size() > 0)
			payingPersons.add(currentPayingPerson);

		Collections.sort(batch);
		for (EDIDumpPersonalInfo i : batch)
			i.ediMemberLevelDetail();

		hscr.close();
	}

	private String notDentalCobraReasons[] = new String[]{"Open Enrollment", "Spouse Gaining Other Coverage", "Gaining Other Coverage", "LOA - Declined Benefits", "Elected COBRA"};

	private void doInactivesDental(HibernateSessionUtil hsu, HrBenefit bene) {

		System.out.println("Doing inactive dental");

		String benefitName = bene.getName();

		int count = 0;

		List configIds = hsu.createCriteria(HrBenefitConfig.class).selectFields(HrBenefitConfig.BENEFIT_CONFIG_ID).eq(HrBenefitConfig.HR_BENEFIT, bene).list();

		HibernateCriteriaUtil<HrBenefitJoinH> hcu = hsu.createCriteria(HrBenefitJoinH.class);
		//hcu.notIn(HrBenefitJoinH.PAYING_PERSON_ID, payingPersons);
		hcu.orderBy(HrBenefitJoinH.PAYING_PERSON_ID);
		hcu.eq(HrBenefitJoinH.RECORD_CHANGE_TYPE, 'D');
		hcu.ne(HrBenefitJoinH.POLICY_END_DATE, 0);
		hcu.gt(HrBenefitJoinH.RECORD_CHANGE_DATE, lastDumpDate);
		hcu.in(HrBenefitJoinH.BENEFIT_CONFIG_ID, configIds);
		hcu.ne(HrBenefitJoinH.COVERAGE_START_DATE, 0);
		hcu.eq(HrBenefitJoinH.USING_COBRA, 'N');
		hcu.ne(HrBenefitJoinH.COVERAGE_END_DATE, 0);
		hcu.notIn(HrBenefitJoinH.CHANGE_REASON_STRING, notDentalCobraReasons);

		int benefitType = hsu.createCriteria(HrBenefitCategory.class).selectFields(HrBenefitCategory.TYPE).joinTo(HrBenefitCategory.HRBENEFIT).eq(HrBenefit.BENEFITID, bene.getBenefitId()).intValue();

		HibernateScrollUtil<HrBenefitJoinH> hscr = hcu.scroll();

		List<EDIDumpPersonalInfo> batch = new LinkedList<EDIDumpPersonalInfo>();

		String currentPayingPerson = "";


		while (hscr.next()) {


			if (++count % 50 == 0) {
				logger.info(count);
				//break;
			}

			HrBenefitJoinH bj = hscr.get();

			if (!bj.getPayingPersonId().equals(currentPayingPerson)) {
				Collections.sort(batch);
				for (EDIDumpPersonalInfo i : batch)
					i.ediMemberLevelDetail();

				batch.clear();
				currentPayingPerson = bj.getPayingPersonId();
			}

			if (skipDentalBecauseTheyStillHaveIt(bj))
				continue;

			EDIDumpPersonalInfo info = new EDIDumpPersonalInfo(edi);



			info.benefitName = benefitName;

			//get relationship code


			if (bj.getCoverageStartDate() == 0 && bj.getCoverageEndDate() == 0) //I have nothing I can say about it
				continue;

			info.setBenefitJoinData(bj, benefitType);


			info.maintenanceTypeCode = "024";


			batch.add(info);
		}

		Collections.sort(batch);
		for (EDIDumpPersonalInfo i : batch)
			i.ediMemberLevelDetail();

		hscr.close();
	}

	private boolean skipDentalBecauseTheyStillHaveIt(IHrBenefitJoin bj) {
		if (bj.getHrBenefitConfig() == null)
			return true;

		//if they currently have a dental, then skip it
		return ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, bj.getCoveredPerson()).dateInside(HrBenefitJoin.COVERAGE_START_DATE, HrBenefitJoin.COVERAGE_END_DATE, DateUtils.now()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bj.getHrBenefitConfig().getHrBenefit()).exists();
	}

	private void doActivesDental(HibernateSessionUtil hsu, HrBenefit bene) {

		System.out.println("Doing dental");
		int count = 0;
		String benefitName = bene.getName();

		HibernateCriteriaUtil<HrBenefitJoin> hcu = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'Y').eq(HrBenefitJoin.BENEFIT_DECLINED, 'N');
		hcu.orderBy(HrBenefitJoin.POLICY_START_DATE);
		hcu.joinTo(HrBenefitJoin.PAYING_PERSON).orderBy(Person.PERSONID);
		hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bene);
		hcu.ne(HrBenefitJoin.COVERAGE_START_DATE, 0);
		hcu.ne(HrBenefitJoin.COVERAGE_END_DATE, 0);
		hcu.eq(HrBenefitJoin.USING_COBRA, 'N');
		hcu.notIn(HrBenefitJoin.CHANGE_REASON_STRING, notDentalCobraReasons);
		hcu.gt(HrBenefitJoin.HISTORY_DATE, lastDumpDate);

		///TEST CODE
		//hcu.gt(HrBenefitJoinH.RECORD_CHANGE_DATE, lastDumpDate);
		//END TEST CODE

		int benefitType = hsu.createCriteria(HrBenefitCategory.class).selectFields(HrBenefitCategory.TYPE).joinTo(HrBenefitCategory.HRBENEFIT).eq(HrBenefit.BENEFITID, bene.getBenefitId()).intValue();


		HibernateScrollUtil<HrBenefitJoin> hscr = hcu.scroll();



		List<EDIDumpPersonalInfo> batch = new LinkedList<EDIDumpPersonalInfo>();

		String currentPayingPerson = "";

		while (hscr.next()) {

			if (++count % 50 == 0) {
				logger.info(count);
				//break;
			}


			//get relationship code
			HrBenefitJoin bj = hscr.get();

			if (!bj.getPayingPersonId().equals(currentPayingPerson)) {
				Collections.sort(batch);
				for (EDIDumpPersonalInfo i : batch)
					i.ediMemberLevelDetail();

				batch.clear();
				currentPayingPerson = bj.getPayingPersonId();
			}

			if (skipDentalBecauseTheyStillHaveIt(bj))
				continue;


			EDIDumpPersonalInfo info = new EDIDumpPersonalInfo(edi);

			info.benefitName = benefitName;

			info.setBenefitJoinData(bj, benefitType);

			int covdate = bj.getCoverageStartDate();

			int covend = bj.getCoverageEndDate();
			if (covend != 0)
				covend = DateUtils.addDays(covend, 1);


			info.maintenanceTypeCode = "024";


			//	logger.info(info);

			//info.ediMemberLevelDetail();



			batch.add(info);


		}

		Collections.sort(batch);
		for (EDIDumpPersonalInfo i : batch)
			i.ediMemberLevelDetail();

		hscr.close();
	}

	private HashSet<String> payingPersons = new HashSet<String>();

	private void doActivesOnly(HibernateSessionUtil hsu, HrBenefit bene) {
		System.out.println("doing actives");

		int count = 0;

		//List<String> peopleToDo = (List) hsu.createCriteria(Person.class).selectFields(Person.PERSONID).joinTo(Person.EMPLOYEE_CHANGES).eq(EmployeeChanged.INTERFACEID, (short) 1).list();
		List<Person> peopleToDo = hsu.createCriteria(Person.class).joinTo(Person.EMPLOYEE_CHANGES).eq(EmployeeChanged.INTERFACEID, (short) 1).list();

		//need to add any sponsors to the list
		//peopleToDo.addAll((List) hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'Y').eq(HrBenefitJoin.BENEFIT_DECLINED, 'N').selectFields(HrBenefitJoin.PAYING_PERSON_ID).in(HrBenefitJoin.COVERED_PERSON_ID, peopleToDo).list());
		List<Person> peopleToDo2 = hsu.createCriteria(Person.class).joinTo(Person.HR_BENEFIT_JOINS_WHERE_PAYING).eq(HrBenefitJoin.APPROVED, 'Y').eq(HrBenefitJoin.BENEFIT_DECLINED, 'N').in(HrBenefitJoin.COVERED_PERSON, peopleToDo).list();
		peopleToDo2.removeAll(peopleToDo);
		peopleToDo.addAll(peopleToDo2);

		HibernateCriteriaUtil<HrBenefitJoin> hcu = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'Y').eq(HrBenefitJoin.BENEFIT_DECLINED, 'N');
		hcu.orderBy(HrBenefitJoin.PAYING_PERSON_ID);
		hcu.orderByDesc(HrBenefitJoin.POLICY_START_DATE);
		hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bene);
		hcu.ne(HrBenefitJoin.COVERAGE_START_DATE, 0);
		hcu.in(HrBenefitJoin.PAYING_PERSON, peopleToDo);


		int benefitType = hsu.createCriteria(HrBenefitCategory.class).selectFields(HrBenefitCategory.TYPE).joinTo(HrBenefitCategory.HRBENEFIT).eq(HrBenefit.BENEFITID, bene.getBenefitId()).intValue();


		HibernateScrollUtil<HrBenefitJoin> hscr = hcu.scroll();



		List<EDIDumpPersonalInfo> batch = new LinkedList<EDIDumpPersonalInfo>();

		String currentPayingPerson = "";
		int currentStartDate = 0;

		while (hscr.next()) {

			if (++count % 50 == 0) {
				logger.info(count);
				//break;
			}

			//TODO change sentry to have benefit category too
			//TODO if changing benefits, make sure to put reason code in termed benefits as well
			//as new benefit

			//get relationship code
			IHrBenefitJoin bj = hscr.get();

			if (payingPersons.contains(bj.getPayingPersonId()))
				continue; //already processed


			//if I have a newer benefit for this paying person in this benefit (Not category), don't run this set
			if (bj.getPolicyEndDate() > 0)
				//For don, first time in, this query should fire as true and cause him to be skipped
				//logger.info("Checking for newer benefit on "+DateUtils.addDays(bj.getPolicyEndDate(),1));
				if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, bj.getPayingPersonId()).eq(HrBenefitJoin.POLICY_START_DATE, DateUtils.addDays(bj.getPolicyEndDate(), 1)).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bj.getHrBenefitConfig().getHrBenefit()).exists())
					continue;



			//logger.info(bj.getPayingPerson().getNameLFM()+" "+bj.getHrBenefitConfig().getName());

			if (bj.getPayingPersonId().equals(currentPayingPerson) && DateUtils.addDays(bj.getPolicyEndDate(), 1) == currentStartDate)
				//he changed coverage
				//I should have handled this, so skip it
				continue;
			if (!bj.getPayingPersonId().equals(currentPayingPerson) || bj.getPolicyStartDate() != currentStartDate) {
				if (batch.size() > 0)
					payingPersons.add(currentPayingPerson);

				Collections.sort(batch);
				for (EDIDumpPersonalInfo i : batch)
					i.ediMemberLevelDetail();

				batch.clear();
				currentPayingPerson = bj.getPayingPersonId();
				currentStartDate = bj.getPolicyStartDate();
			}

			EDIDumpPersonalInfo info = new EDIDumpPersonalInfo(edi);


			info.setBenefitJoinData(bj, benefitType);


			//if I am the master record, look to see if I changed benefits
			if (bj.getPayingPersonId().equals(bj.getCoveredPersonId())) {
				IHrBenefitJoin ibj = getPreviousBenefitJoin(hsu, bj);

				while (ibj != null)
					if (getCoverageLevel(ibj).equals(getCoverageLevel(bj)) && ibj.getUsingCOBRA() == bj.getUsingCOBRA() && ibj.getHrBenefitConfig().getHrBenefit().getBenefitId().equals(bj.getHrBenefitConfig().getHrBenefit().getBenefitId()))
						ibj = getPreviousBenefitJoin(hsu, ibj);
					else
						break;

				if (ibj != null) {
					//TODO: if the termed benefit is new, need to do regular add of it first
					IHrBenefitJoin tbj = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.BENEFIT_JOIN_ID, ibj.getBenefitJoinId()).eq(HrBenefitJoinH.RECORD_CHANGE_TYPE, 'N').ge(HrBenefitJoinH.RECORD_CHANGE_DATE, lastDumpDate).first();

					if (tbj == null)
						tbj = hsu.createCriteria(HrBenefitJoinH.class).eq(HrBenefitJoin.BENEFIT_JOIN_ID, ibj.getBenefitJoinId()).eq(HrBenefitJoinH.RECORD_CHANGE_TYPE, 'N').ge(HrBenefitJoinH.RECORD_CHANGE_DATE, lastDumpDate).first();

					if (tbj != null) //I have an old record that is new since the last dump
					{				//so I need to add it
						HibernateCriteriaUtil<HrBenefitJoin> hc = hsu.createCriteria(HrBenefitJoin.class);
						hc.eq(HrBenefitJoin.POLICY_START_DATE, tbj.getPolicyStartDate());
						hc.eq(HrBenefitJoin.HR_BENEFIT_CONFIG, tbj.getHrBenefitConfig());
						hc.ne(HrBenefitJoin.COVERAGE_START_DATE, 0);

						List<EDIDumpPersonalInfo> bat = new LinkedList<EDIDumpPersonalInfo>();
						for (HrBenefitJoin oldBj : hc.list()) {
							EDIDumpPersonalInfo in = new EDIDumpPersonalInfo(edi);
							in.setBenefitJoinData(oldBj, benefitType);
							if (oldBj.getPayingPersonId().equals(oldBj.getCoveredPersonId()))
								in.setTermBenefit(getPreviousBenefitJoin(hsu, oldBj)); //handle switch
							bat.add(in);
						}
						Collections.sort(bat);
						for (EDIDumpPersonalInfo i : batch)
							i.ediMemberLevelDetail();
					}

					info.setTermBenefit(ibj);

					List<IHrBenefitJoin> terms;
					//now I need to know anybody that was covered that isn't now
					//who just recently got not covered

					List ids = hsu.createCriteria(HrBenefitJoin.class).selectFields(HrBenefitJoin.COVERED_PERSON_ID).eq(HrBenefitJoin.PAYING_PERSON_ID, bj.getPayingPersonId()).eq(HrBenefitJoin.POLICY_START_DATE, bj.getPolicyStartDate()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, bj.getHrBenefitConfig()).list();

					if (ibj.getRecordChangeType() == 'D') //look in history
					{
						List t = hsu.createCriteria(HrBenefitJoinH.class).eq(HrBenefitJoinH.PAYING_PERSON_ID, ibj.getPayingPersonId()).eq(HrBenefitJoinH.HR_BENEFIT_CONFIG_ID, ibj.getHrBenefitConfigId()).eq(HrBenefitJoinH.POLICY_START_DATE, ibj.getPolicyStartDate()).eq(HrBenefitJoinH.RECORD_CHANGE_TYPE, 'D').notIn(HrBenefitJoinH.COVERED_PERSON_ID, ids).list();
						terms = t;
					} else //look in current
					{
						List t = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, ibj.getPayingPersonId()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, ibj.getHrBenefitConfigId()).eq(HrBenefitJoin.POLICY_START_DATE, ibj.getPolicyStartDate()).notIn(HrBenefitJoinH.COVERED_PERSON_ID, ids).list();
						terms = t;
					}

					//how I have the list of benefit joins that termed

					for (IHrBenefitJoin termBJ : terms)
						batch.add(new EDIDumpPersonalInfo(edi).setBenefitJoinData(termBJ, benefitType));
				}
			}

			//		System.out.println(bj.getRecordChangeDate());

			//if I'm a 24 and I termed in the past, skip me
			if (info.maintenanceTypeCode.equals("024") && bj.getRecordChangeDate().before(lastDumpDate))
				continue;
			//	logger.info(info);

			//info.ediMemberLevelDetail();

			batch.add(info);


		}
		if (batch.size() > 0)
			payingPersons.add(currentPayingPerson);

		Collections.sort(batch);
		for (EDIDumpPersonalInfo i : batch)
			i.ediMemberLevelDetail();

		hscr.close();
	}

	private void isa_seg(String icn, boolean test, String account, String interchangeReceiverId) {
		edi.SEG("ISA");
		edi.ST(1, "00");
		edi.SW(2, "", 10);
		edi.ST(3, "00");
		edi.SW(4, "", 10);
		edi.ST(5, "ZZ");
		edi.SW(6, account, 15);
		edi.ST(7, "ZZ");
		edi.SW(8, interchangeReceiverId, 15);
		edi.CD(9);
		edi.CT(10);
		edi.CH(11, 'U');
		edi.ST(12, "00401");
		edi.ST(13, icn);
		edi.CH(14, '0');
		edi.CH(15, test ? 'T' : 'P');

		String delim = AIProperty.getValue("Delim" + interchangeReceiverId);

		//	if (delim==null || "".equals(delim))
		//		edi.NV(16); //need the delimiter there
		//	else
		edi.ST(16, ":");

		edi.EOS();
	}

	private void gs_seg(String gcn, String account, String applicationRecieverId) {
		edi.SEG("GS");
		edi.ST(1, "BE");
		edi.ST(2, StringUtils.rightStrip(account));
		edi.ST(3, applicationRecieverId);
		edi.D8(4);
		edi.CT(5);
		edi.ST(6, gcn);
		edi.CH(7, 'X');
		edi.ST(8, "004010X095A1");
		edi.EOS();
	}

	private void transactionSetTrailer(int transactionSetNumber) {
		edi.SEG("SE");
		edi.NL(1, edi.X12_numb_segments);
		edi.ST(2, EDI.makecn(transactionSetNumber));
		edi.EOS();
	}

	private void functionalGroupTrailer(String transactionSetNumber) {
		edi.SEG("GE");
		edi.NL(1, edi.X12_numb_st_segments);
		edi.ST(2, transactionSetNumber);
		edi.EOS();
	}

	private void functionInterchangeTrailer(String icn) {
		edi.SEG("IEA");
		edi.NL(1, 1);
		edi.ST(2, icn);
		edi.EOS();
	}

	private void header(int transactionSetNumber, String payer, String payerId, String sponsorName, String sponsorId, int gcn) {
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
		edi.ST(8, "2"); //2 is changes  4 is full file	
		edi.EOS();

		//Sponsor
		edi.SEG("N1");
		edi.ST(1, "P5");
		edi.ST(2, sponsorName);//Plan name
		edi.ST(3, "FI");//Identifier code  FI= fed id, ZZ= mutually defined
		edi.ST(4, sponsorId);
		edi.EOS();

		//Payer
		edi.SEG("N1");
		edi.ST(1, "IN");
		edi.ST(2, payer);
		edi.ST(3, "FI");//Identifier code  FI= fed id, ZZ= mutually defined
		edi.ST(4, payerId);
		edi.EOS();

		/* May not need this
		//Broker
		edi.SEG("N1");
		edi.ST(1, "IN"); //BO - broker, TV - third party admin
		edi.ST(2, "BROKER NAME"); //TODO: get broker name
		edi.ST(3, "F1");//Identifier code  F1= fed id, ZZ= mutually defined, 94 org code
		edi.ST(4, "0000000000"); //TODO: organization id
		edi.EOS();
		 */
	}

	private String getCoverageLevel(IHrBenefitJoin bj) {
		String ret = "EMP";

		if (bj.getHrBenefitConfig().getSpouseEmployee() == 'Y' ||
				bj.getHrBenefitConfig().getSpouseNonEmployee() == 'Y')
			ret = "ESP";

		if ((bj.getHrBenefitConfig().getSpouseNonEmpOrChildren() == 'Y') ||
				(bj.getHrBenefitConfig().getSpouseEmpOrChildren() == 'Y') ||
				(bj.getHrBenefitConfig().getChildren() == 'Y'))
			ret = "FAM";

		if (bj.getHrBenefitConfig().getMaxChildren() == 1)
			ret = "ESP";

		if (bj.getHrBenefitConfig().getChildren() == 'Y' && bj.getHrBenefitConfig().getMaxChildren() == 1 &&
				bj.getHrBenefitConfig().getSpouseEmployee() == 'Y')
			ret = "FAM";

		return ret;
	}

	public static void main(String args[]) {

		logger.info(new Date().getTime() / 1000);
		/*	ArahantSession.getHSU().setCurrentPersonToArahant();

		

		final BEDITransaction x=new BEDITransaction();
		x.create();

		x.setReceiver(ArahantSession.getHSU().getFirst(VendorCompany.class).getOrgGroupId());
		x.setStatus(0);
		x.setStatusDesc("Started");

		x.insert();
		
		ArahantSession.getHSU().commitTransaction();
		ArahantSession.getHSU().beginTransaction();


		x.send834Insurance();
		 */

		//ed.dumpEDI("00001-0000000003","Delta Dental Plan of Michigan","381791480");
		//"62-6000913","62-6000913","62-6000913","62-6000913",
		//"076334622      ","076334622 ");

	}
}
