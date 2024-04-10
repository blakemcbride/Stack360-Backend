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
import com.arahant.business.BEmployee;
import com.arahant.business.BOrgGroup;
import com.arahant.exceptions.ArahantException;
import com.arahant.fields.EmployeeListFields;
import com.arahant.services.standard.hr.employeeListReport.ListHierarchyInBreathFirstSearch;
import com.arahant.utils.*;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.kissweb.DelimitedFileWriter;

public class EmployeeListExport {

	private String[] ids;
	private int dobFrom;
	private int dobTo;
	private String lastNameFrom;
	private String lastNameTo;
	private boolean sortAsc;
	private int sortType;
	private int statusType;
	private String[] orgGroupIds;
	private String[] orgGroupCodes;
	private String[] configIds;

	public EmployeeListExport(String[] ids, String lastNameFrom, String lastNameTo, int dobFrom, int dobTo, int sortType, boolean sortAsc, int statusType, String[] orgGroupIds, String[] orgGroupCodes, String[] configIds) throws ArahantException {
		this.ids = ids;
		this.dobFrom = dobFrom;
		this.dobTo = dobTo;
		this.lastNameTo = lastNameTo;
		this.lastNameFrom = lastNameFrom;
		this.sortAsc = sortAsc;
		this.sortType = sortType;
		this.statusType = statusType;
		this.orgGroupCodes = orgGroupCodes;
		this.orgGroupIds = orgGroupIds;
		this.configIds = configIds;
	}
	private HibernateSessionUtil hsu = ArahantSession.getHSU();

	private class BenefitOutput {

		String id;
		boolean category;

		public BenefitOutput(String id, boolean category) {
			this.id = id;
			this.category = category;
		}
	}

	private class CheckListOutput {

		String id;

		public CheckListOutput(String id) {
			this.id = id;
		}
	}

	private class NoteOutput {

		String id;

		public NoteOutput(String id) {
			this.id = id;
		}
	}

	private class TrainingOutput {

		String id;

		public TrainingOutput(String id) {
			this.id = id;
		}
	}
	private List<BenefitOutput> benefitList = new LinkedList<BenefitOutput>();
	private List<CheckListOutput> checkList = new LinkedList<CheckListOutput>();
	private List<NoteOutput> noteList = new LinkedList<NoteOutput>();
	private List<TrainingOutput> trainingList = new LinkedList<TrainingOutput>();
	private int maxGarn = 0;
	private int maxEft = 0;
	private long maxEvents = 0;
	private long maxForms = 0;
	private int maxOrgGroupDepth = 0;
	private List orgList = null;

	private void printOrgLevelName(int level, DelimitedFileWriter writer) {
		try {
			writer.writeField("Organization Group Level " + level);
		} catch (Exception ex) {
			Logger.getLogger(EmployeeListExport.class.getName()).log(Level.SEVERE, "printOrgLevelName", ex);
		}
	}

	private void printOrgLevelPath(int level, BEmployee employee, String orgId, DelimitedFileWriter writer) {
		try {
			ListHierarchyInBreathFirstSearch h = new ListHierarchyInBreathFirstSearch();
			//get the list of org if we haven't
			if (orgList == null)
				orgList = h.getEmployeeGroupAssociationHierarchy(employee.getPersonId(), orgId);
			//make sure we have something to print
			if (orgList != null && orgList.size() > level)
				writer.writeField(orgList.get(level).toString());
			else
				writer.writeField("");
		} catch (Exception ex) {
			Logger.getLogger(EmployeeListExport.class.getName()).log(Level.SEVERE, "printOrgLevelPath", ex);
		}
	}

	private String getGroupId(BEmployee emp) {
		if (orgGroupIds.length > 0)
			return orgGroupIds[0];
		else
			//get employee company id
			return emp.getEmployee().getCompanyBase().getCompanyId();
	}

	private void printOrgLevelValue(int level, BEmployee employee, DelimitedFileWriter writer) {
		try {
			ListHierarchyInBreathFirstSearch h = new ListHierarchyInBreathFirstSearch();
			writer.writeField(h.getOrganizationLevel(employee, level));
		} catch (Exception ex) {
			Logger.getLogger(EmployeeListExport.class.getName()).log(Level.SEVERE, "printOrgLevelValue", ex);
		}
	}

	public String build() throws Exception {
		HibernateCriteriaUtil<Employee> hcu = EmployeeListFields.getHCU(ids, lastNameFrom, lastNameTo, dobFrom, dobTo, sortType, sortAsc, statusType, orgGroupIds, orgGroupCodes, configIds);
		HibernateScrollUtil<Employee> scrollUtil = hcu.scroll();
		File csvFile = FileSystemUtils.createTempFile("EmpList", ".csv");
		DelimitedFileWriter writer = new DelimitedFileWriter(csvFile.getAbsolutePath(), false);


		try {
			// Pass one is to write out all used columns
			for (String id : ids)
				if (id.equals(EmployeeListFields.WORKERS_COMP))
					writer.writeField(EmployeeListFields.WORKERS_COMP);
				else if (id.equals(EmployeeListFields.ORG_GROUP)) {
					//what is the maximum depth of company org groups

					CompanyDetail cd = hsu.createCriteria(CompanyDetail.class).first();

					BOrgGroup borg = new BOrgGroup(cd);

					maxOrgGroupDepth = borg.getMaxDepth();

					for (int loop = 1; loop <= maxOrgGroupDepth; loop++) {
						writer.writeField("Organization Level " + loop + " Name");
						writer.writeField("Organization Level " + loop + " Code");
						writer.writeField("Organization Level " + loop + " Supervisor");

					}
				} else if (id.equals(EmployeeListFields.ORG_GROUP1))
					printOrgLevelName(1, writer);
				else if (id.equals(EmployeeListFields.ORG_GROUP2))
					printOrgLevelName(2, writer);
				else if (id.equals(EmployeeListFields.ORG_GROUP3))
					printOrgLevelName(3, writer);
				else if (id.equals(EmployeeListFields.ORG_GROUP4))
					printOrgLevelName(4, writer);
				else if (id.equals(EmployeeListFields.ORG_GROUP5))
					printOrgLevelName(5, writer);
				else if (id.equals(EmployeeListFields.TRAINING)) {
					//what is the maximum number of notes for any employee

					List<HrTrainingCategory> cl = hsu.createCriteria(HrTrainingCategory.class).orderBy(HrTrainingCategory.NAME).list();


					for (HrTrainingCategory nc : cl) {
						writer.writeField(nc.getName() + " Date");
						writer.writeField(nc.getName() + " Hours");
						writer.writeField(nc.getName() + " Expiration");
						trainingList.add(new TrainingOutput(nc.getCatId()));
					}
				} else if (id.equals(EmployeeListFields.NOTES)) {
					//what is the maximum number of notes for any employee

					List<HrNoteCategory> cl = hsu.createCriteria(HrNoteCategory.class).orderBy(HrNoteCategory.NAME).list();


					for (HrNoteCategory nc : cl) {
						writer.writeField(nc.getName());
						noteList.add(new NoteOutput(nc.getCatId()));
					}
				} else if (id.equals(EmployeeListFields.FORMS)) {
					//what is the maximum number of forms for any employee

					Query q = hsu.createQuery("select count(*) from PersonForm group by " + PersonForm.PERSON + " order by count(*) desc ");
					ScrollableResults sr = q.scroll();

					if (sr.next())
						maxForms = sr.getLong(0);

					sr.close();


					for (int loop = 1; loop <= maxForms; loop++) {
						writer.writeField("Form " + loop + " Code");
						writer.writeField("Form " + loop + " Description");
						writer.writeField("Form " + loop + " Date");
						writer.writeField("Form " + loop + " Comments");

					}
				} else if (id.equals(EmployeeListFields.CHECK_LIST)) {
					List<HrChecklistItem> l = hsu.createCriteria(HrChecklistItem.class).orderBy(HrChecklistItem.HREMPLOYEESTATUS).orderBy(HrChecklistItem.SEQ).list();

					for (HrChecklistItem i : l) {
						String name = i.getName();
						if (name.indexOf("\n") != -1)
							name = name.substring(0, name.indexOf('\n'));
						name = name.trim();
						/*
						 * if (name.length()>25)
						 * name=name.substring(0,name.indexOf(' ', 15)); if
						 * (name.length()>30) name=name.substring(30); if
						 * (i.getName().startsWith("Nashville"))
						 * logger.info(i.getName());
						 *
						 */
						writer.writeField(name + " " + i.getHrEmployeeStatus().getName());
						writer.writeField(name + " " + i.getHrEmployeeStatus().getName() + " Completed By");
						checkList.add(new CheckListOutput(i.getItemId()));
					}
				} else if (id.equals(EmployeeListFields.EVENTS)) {
					//what is the maximum number of events for any employee


					Query q = hsu.createQuery("select count(*) from HrEmployeeEvent group by " + HrEmployeeEvent.EMPLOYEEID + " order by count(*) desc ");
					ScrollableResults sr = q.scroll();

					if (sr.next())
						maxEvents = sr.getLong(0);

					sr.close();


					for (int loop = 1; loop <= maxEvents; loop++) {
						writer.writeField("Event " + loop + " Date");
						writer.writeField("Event " + loop + " Summary");
						writer.writeField("Event " + loop + " Supervisor");
						writer.writeField("Event " + loop + " Notified");
						writer.writeField("Event " + loop + " Notified Date");
						writer.writeField("Event " + loop + " Details");
					}
				} else if (id.equals(EmployeeListFields.EFT)) {
					ElectronicFundTransfer eft = hsu.createCriteria(ElectronicFundTransfer.class).orderByDesc(ElectronicFundTransfer.SEQNO).first();

					if (eft != null)
						maxEft = eft.getSeqno();

					for (int loop = 1; loop <= maxEft; loop++) {
						writer.writeField("EFT " + loop + " Account Type");
						writer.writeField("EFT " + loop + " Routing Code");
						writer.writeField("EFT " + loop + " Account Number");
						writer.writeField("EFT " + loop + " Amount Type");
						writer.writeField("EFT " + loop + " Amount");
					}
				} else if (id.equals(EmployeeListFields.GARNISHMENTS)) {
					Garnishment g = hsu.createCriteria(Garnishment.class).orderByDesc(Garnishment.PRIORITY).first();

					if (g != null)
						maxGarn = g.getPriority() + 1;

					for (int loop = 1; loop <= maxGarn; loop++) {
						writer.writeField("Garnishment " + loop + " Start Date");
						writer.writeField("Garnishment " + loop + " Final Date");
						writer.writeField("Garnishment " + loop + " Type");
						writer.writeField("Garnishment " + loop + " Deduction Type");
						writer.writeField("Garnishment " + loop + " Amount Type");
						writer.writeField("Garnishment " + loop + " Amount");
						writer.writeField("Garnishment " + loop + " Maximum Amount");
						writer.writeField("Garnishment " + loop + " Docket Number");
						writer.writeField("Garnishment " + loop + " FIPS Code");
						writer.writeField("Garnishment " + loop + " Issue State");
						writer.writeField("Garnishment " + loop + " Collecting State");
						writer.writeField("Garnishment " + loop + " Remit To Name");
						writer.writeField("Garnishment " + loop + " Remit To Street 1");
						writer.writeField("Garnishment " + loop + " Remit To Street 2");
						writer.writeField("Garnishment " + loop + " Remit To City");
						writer.writeField("Garnishment " + loop + " Remit To State");
						writer.writeField("Garnishment " + loop + " Remit To Zip");
						writer.writeField("Garnishment " + loop + " Remit To Country");

					}
				} else if (id.equals(EmployeeListFields.ACCRUED_TIME_OFF))
					for (HrBenefit b : hsu.createCriteria(HrBenefit.class).eq(HrBenefit.TIMERELATED, 'Y').orderBy(HrBenefit.NAME).list())
						writer.writeField(b.getName());
				else if (id.equals(EmployeeListFields.BENEFITS)) {
					//get all benefit categories by type
					List<HrBenefitCategory> catList = hsu.createCriteria(HrBenefitCategory.class).ne(HrBenefitCategory.TYPE, HrBenefitCategory.MISC).orderBy(HrBenefitCategory.TYPE).list();

					for (HrBenefitCategory cat : catList)
						if (cat.getAllowsMultipleBenefitsBoolean()) {
							//get all the benefits in this category by name order
							List<HrBenefit> bl = hsu.createCriteria(HrBenefit.class).orderBy(HrBenefit.NAME).eq(HrBenefit.BENEFIT_CATEGORY, cat).list();

							for (HrBenefit b : bl) {
								//	logger.info(b.getName());
								writer.writeField(b.getName());
								benefitList.add(new BenefitOutput(b.getBenefitId(), false));
								writer.writeField(b.getName() + " Policy Start");
								writer.writeField(b.getName() + " Policy End");
								writer.writeField(b.getName() + " Insurance ID");
								writer.writeField(b.getName() + " Coverage Start");
								writer.writeField(b.getName() + " Coverage End");
								writer.writeField(b.getName() + " Covered Amount");
								writer.writeField(b.getName() + " Annual Cost Type");
								writer.writeField(b.getName() + " Annual Cost");
								writer.writeField(b.getName() + " Is COBRA");
								writer.writeField(b.getName() + " COBRA accepted date");
								writer.writeField(b.getName() + " Maximum Months");
								writer.writeField(b.getName() + " Change Reason");
							}

						} else {
							//stuff like medical that needs to be combined
							//logger.info(cat.getDescription());
							writer.writeField(cat.getDescription());
							benefitList.add(new BenefitOutput(cat.getBenefitCatId(), true));
							writer.writeField(cat.getDescription() + " Policy Start");
							writer.writeField(cat.getDescription() + " Policy End");
							writer.writeField(cat.getDescription() + " Insurance ID");
							writer.writeField(cat.getDescription() + " Coverage Start");
							writer.writeField(cat.getDescription() + " Coverage End");
							writer.writeField(cat.getDescription() + " Covered Amount");
							writer.writeField(cat.getDescription() + " Annual Cost Type");
							writer.writeField(cat.getDescription() + " Annual Cost");
							writer.writeField(cat.getDescription() + " Is COBRA");
							writer.writeField(cat.getDescription() + " COBRA accepted date");
							writer.writeField(cat.getDescription() + " Maximum Months");
							writer.writeField(cat.getDescription() + " Change Reason");

						}


				} else if (id.equals(EmployeeListFields.BENEFIT_SUMMARY)) {
					//get all benefit categories by type
					List<HrBenefitCategory> catList = hsu.createCriteria(HrBenefitCategory.class).ne(HrBenefitCategory.TYPE, HrBenefitCategory.MISC).orderBy(HrBenefitCategory.TYPE).list();

					for (HrBenefitCategory cat : catList)
						if (cat.getAllowsMultipleBenefitsBoolean()) {
							//get all the benefits in this category by name order
							List<HrBenefit> bl = hsu.createCriteria(HrBenefit.class).orderBy(HrBenefit.NAME).eq(HrBenefit.BENEFIT_CATEGORY, cat).list();

							for (HrBenefit b : bl) {
								//	logger.info(b.getName());
								writer.writeField(b.getName());
								benefitList.add(new BenefitOutput(b.getBenefitId(), false));
								writer.writeField(b.getName() + " Annual Cost");
							}

						} else {
							//stuff like medical that needs to be combined
							//logger.info(cat.getDescription());
							writer.writeField(cat.getDescription());
							benefitList.add(new BenefitOutput(cat.getBenefitCatId(), true));
							writer.writeField(cat.getDescription() + " Annual Cost");
						}


				} else
					writer.writeField(id);

			writer.endRecord();
			String lastId = "";

			// Pass two is to write out data for used columns
			while (scrollUtil.next()) {
				BEmployee employee = new BEmployee(scrollUtil.get());
				orgList = null; //reset this list everytime we get a new employee
				if (employee.getPersonId().equals(lastId))
					continue;

				lastId = employee.getPersonId();

				for (String id : ids)
					if (id.equals(EmployeeListFields.WORKERS_COMP))
						writer.writeField(employee.getWorkersCompCode());
					else if (id.equals(EmployeeListFields.LAST_NAME))
						writer.writeField(employee.getLastName());
					else if (id.equals(EmployeeListFields.FIRST_NAME))
						writer.writeField(employee.getFirstName());
					else if (id.equals(EmployeeListFields.MIDDLE_NAME))
						writer.writeField(employee.getMiddleName());
					else if (id.equals(EmployeeListFields.NICK_NAME))
						writer.writeField(employee.getNickName());
					else if (id.equals(EmployeeListFields.SEX))
						writer.writeField(employee.getSex().equals("F") ? "Female" : "Male");
					else if (id.equals(EmployeeListFields.SSN))
						writer.writeField(employee.getSsn());
					else if (id.equals(EmployeeListFields.EXTERNAL_ID))
						writer.writeField(employee.getExtRef());
					else if (id.equals(EmployeeListFields.DATE_OF_BIRTH))
						writer.writeDate(employee.getDob());
					else if (id.equals(EmployeeListFields.EMAIL))
						writer.writeField(employee.getPersonalEmail());
					else if (id.equals(EmployeeListFields.ADDRESS_STREET_1))
						writer.writeField(employee.getStreet());
					else if (id.equals(EmployeeListFields.ADDRESS_STREET_2))
						writer.writeField(employee.getStreet2());
					else if (id.equals(EmployeeListFields.ADDRESS_CITY))
						writer.writeField(employee.getCity());
					else if (id.equals(EmployeeListFields.ADDRESS_STATE))
						writer.writeField(employee.getState());
					else if (id.equals(EmployeeListFields.ADDRESS_ZIP))
						writer.writeField(employee.getZip());
					else if (id.equals(EmployeeListFields.PHONE_HOME))
						writer.writeField(employee.getHomePhone());
					else if (id.equals(EmployeeListFields.PHONE_WORK))
						writer.writeField(employee.getWorkPhoneNumber());
					else if (id.equals(EmployeeListFields.PHONE_MOBILE))
						writer.writeField(employee.getMobilePhone());
					else if (id.equals(EmployeeListFields.PHONE_FAX))
						writer.writeField(employee.getWorkFaxNumber());
					else if (id.equals(EmployeeListFields.JOB_TITLE))
						writer.writeField(employee.getJobTitle());
					else if (id.equals(EmployeeListFields.EEO_CATEGORY))
						writer.writeField(employee.getEEOCategory());
					else if (id.equals(EmployeeListFields.EEO_RACE))
						writer.writeField(employee.getEEORace());
					else if (id.equals(EmployeeListFields.WAGE_TYPE_CURRENT))
						writer.writeField(employee.getWageTypeName());
					else if (id.equals(EmployeeListFields.POSITION_CURRENT))
						writer.writeField(employee.getPositionName());
					else if (id.equals(EmployeeListFields.STATUS_CURRENT))
						writer.writeField(employee.getStatus());
					else if (id.equals(EmployeeListFields.LOGIN_ID))
						writer.writeField(employee.getUserLogin());
					else if (id.equals(EmployeeListFields.LOGIN_SCREEN_GROUP))
						writer.writeField(employee.getScreenGroupName());
					else if (id.equals(EmployeeListFields.LOGIN_SECURITY_GROUP))
						writer.writeField(employee.getSecurityGroupName());
					else if (id.equals(EmployeeListFields.LOGIN_STATUS))
						writer.writeField(employee.canLogin() ? "Active" : "Inactive");
					else if (id.equals(EmployeeListFields.PAYROLL_PAY_PERIODS_PER_YEAR))
						writer.writeField(employee.getPayPeriodsPerYear());
					else if (id.equals(EmployeeListFields.PAYROLL_EXPECTED_HOURS_PER_PAY_PERIOD))
						writer.writeField(employee.getExpectedHoursPerPayPeriod());
					else if (id.equals(EmployeeListFields.PAYROLL_MARITAL_STATUS))
						writer.writeField(employee.getMaritalStatusName());
					else if (id.equals(EmployeeListFields.PAYROLL_W4_STATUS))
						writer.writeField(employee.getW4StatusName());
					else if (id.equals(EmployeeListFields.PAYROLL_OVERTIME))
						writer.writeField(employee.getExempt() ? "Exempt" : "Non-Exempt");
					else if (id.equals(EmployeeListFields.PAYROLL_EARNED_INCOME_CREDIT))
						writer.writeField(employee.getEarnedIncomeCreditStatusName());
					else if (id.equals(EmployeeListFields.PAYROLL_LOCAL_TAX_CODE))
						writer.writeField(employee.getLocalTaxCode());
					else if (id.equals(EmployeeListFields.PAYROLL_BANK_CODE))
						writer.writeField(employee.getPayrollBankCode());
					else if (id.equals(EmployeeListFields.PAYROLL_FEDERAL_EXEMPTIONS))
						writer.writeField(employee.getFederalExemptions());
					else if (id.equals(EmployeeListFields.PAYROLL_STATE_EXEMPTIONS))
						writer.writeField(employee.getStateExemptions());
					else if (id.equals(EmployeeListFields.PAYROLL_FEDERAL_EXTRA_WITHHELD))
						writer.writeField(employee.getFederalExtraWithheld());
					else if (id.equals(EmployeeListFields.PAYROLL_STATE_EXTRA_WITHHELD))
						writer.writeField(employee.getStateExtraWithheld());
					else if (id.equals(EmployeeListFields.PAYROLL_TAX_STATE))
						writer.writeField(employee.getTaxState());
					else if (id.equals(EmployeeListFields.PAYROLL_UNEMPLOYMENT_STATE))
						writer.writeField(employee.getUnemploymentState());
					else if (id.equals(EmployeeListFields.PAYROLL_ADDITIONAL_FEDERAL_INCOME_TAX_TYPE))
						writer.writeField(employee.getAddFederalIncomeTaxTypeName());
					else if (id.equals(EmployeeListFields.PAYROLL_ADDITIONAL_FEDERAL_INCOME_TAX_AMOUNT))
						writer.writeField(employee.getAddFederalIncomeTaxAmountFormatted());
					else if (id.equals(EmployeeListFields.PAYROLL_ADDITIONAL_STATE_INCOME_TAX_TYPE))
						writer.writeField(employee.getAddStateIncomeTaxTypeName());
					else if (id.equals(EmployeeListFields.PAYROLL_ADDITIONAL_STATE_INCOME_TAX_AMOUNT))
						writer.writeField(employee.getAddStateIncomeTaxAmountFormatted());
					else if (id.equals(EmployeeListFields.PAYROLL_ADDITIONAL_LOCAL_INCOME_TAX_TYPE))
						writer.writeField(employee.getAddLocalIncomeTaxTypeName());
					else if (id.equals(EmployeeListFields.PAYROLL_ADDITIONAL_LOCAL_INCOME_TAX_AMOUNT))
						writer.writeField(employee.getAddLocalIncomeTaxAmountFormatted());
					else if (id.equals(EmployeeListFields.PAYROLL_ADDITIONAL_STATE_DISABILITY_TAX_TYPE))
						writer.writeField(employee.getAddStateDisabilityTaxTypeName());
					else if (id.equals(EmployeeListFields.PAYROLL_ADDITIONAL_STATE_DISABILITY_TAX_AMOUNT))
						writer.writeField(employee.getAddStateDisabilityTaxAmountFormatted());
					else if (id.equals(EmployeeListFields.EMPLOYMENT_ELIGIBILITY_CITIZENSHIP))
						writer.writeField(employee.getCitizenship());
					else if (id.equals(EmployeeListFields.EMPLOYMENT_ELIGIBILITY_VISA))
						writer.writeField(employee.getVisa());
					else if (id.equals(EmployeeListFields.EMPLOYMENT_ELIGIBILITY_VISA_STATUS_DATE))
						writer.writeDate(employee.getVisaStatusDate());
					else if (id.equals(EmployeeListFields.EMPLOYMENT_ELIGIBILITY_VISA_EXPIRATION_DATE))
						writer.writeDate(employee.getVisaExpirationDate());
					else if (id.equals(EmployeeListFields.EMPLOYMENT_ELIGIBILITY_COMPLETED))
						writer.writeField(employee.getI9Part1() && employee.getI9Part2() ? "Yes" : "No");
					else if (id.equals(EmployeeListFields.POSITION_DATE))
						writer.writeField(DateUtils.getDateFormatted(employee.getLastPositionDate()));
					else if (id.equals(EmployeeListFields.STATUS_DATE))
						writer.writeField(DateUtils.getDateFormatted(employee.getLastStatusDate()));
					else if (id.equals(EmployeeListFields.HIRE_DATE))
						writer.writeField(DateUtils.getDateFormatted(employee.getEmploymentDate()));
					else if (id.equals(EmployeeListFields.WAGE_DATE))
						writer.writeField(DateUtils.getDateFormatted(employee.getLastRaiseDate()));
					else if (id.equals(EmployeeListFields.WAGE_AMOUNT))
						writer.writeField(Formatting.formatNumber(employee.getCurrentSalary(), 2));
					else if (id.equals(EmployeeListFields.WAGE_NOTES))
						writer.writeField(employee.getCurrentWageNotes());
					else if (id.equals(EmployeeListFields.TOBACCO_USE))
						writer.writeField(employee.getTabaccoUse().equals("U") ? "Unknown" : (employee.getTabaccoUse().equals("Y") ? "Yes" : "No"));
					else if (id.equals(EmployeeListFields.DRIVERS_LICENSE_NUMBER))
						writer.writeField(employee.getDriversLicenseNumber());
					else if (id.equals(EmployeeListFields.DRIVERS_LICENSE_STATE))
						writer.writeField(employee.getDriversLicenseState());
					else if (id.equals(EmployeeListFields.DRIVERS_LICENSE_EXPIRES))
						writer.writeField(DateUtils.getDateFormatted(employee.getDriversLicenseExpirationDate()));
					else if (id.equals(EmployeeListFields.AUTOMOTIVE_INSURANCE_POLICY_NUMBER))
						writer.writeField(employee.getAutomotiveInsurancePolicyNumber());
					else if (id.equals(EmployeeListFields.AUTOMOTIVE_INSURANCE_COVERAGE))
						writer.writeField(employee.getAutomotiveInsuranceCoverage());
					else if (id.equals(EmployeeListFields.AUTOMOTIVE_INSURANCE_CARRIER))
						writer.writeField(employee.getAutomotiveInsuranceCarrier());
					else if (id.equals(EmployeeListFields.AUTOMOTIVE_INSURANCE_BEGINS))
						writer.writeField(DateUtils.getDateFormatted(employee.getAutomotiveInsuranceStartDate()));
					else if (id.equals(EmployeeListFields.AUTOMOTIVE_INSURANCE_EXPIRES))
						writer.writeField(DateUtils.getDateFormatted(employee.getAutomotiveInsuranceExpirationDate()));
					else if (id.equals(EmployeeListFields.COUNTY))
						writer.writeField(employee.getCounty());
					else if (id.equals(EmployeeListFields.BENEFIT_CLASS))
						writer.writeField(employee.getBenefitClass() == null ? "(No Class Assigned)" : employee.getBenefitClass().getName());
					else if (id.equals(EmployeeListFields.EFT))
						for (short loop = 1; loop < maxEft; loop++) {
							ElectronicFundTransfer eft = hsu.createCriteria(ElectronicFundTransfer.class).eq(ElectronicFundTransfer.PERSON, employee.getEmployee()).eq(ElectronicFundTransfer.SEQNO, loop).first();

							if (eft != null) {
								writer.writeField(eft.getAccountType() + "");
								writer.writeField(eft.getBankRoute());
								writer.writeField(eft.getBankAccount());
								writer.writeField(eft.getAmountType());
								writer.writeField(Formatting.formatNumber(eft.getAmount(), 2));
							} else
								for (int skip = 0; skip < 5; skip++)
									writer.writeField("");
						}
					else if (id.equals(EmployeeListFields.GARNISHMENTS)) {
						Garnishment g = hsu.createCriteria(Garnishment.class).orderByDesc(Garnishment.PRIORITY).first();

						for (short loop = 0; loop < maxGarn; loop++) {
							Garnishment garn = hsu.createCriteria(Garnishment.class).eq(Garnishment.EMPLOYEE, employee.getEmployee()).eq(Garnishment.PRIORITY, loop).first();

							if (garn != null) {
								writer.writeDate(garn.getStartDate());
								writer.writeField(garn.getEndDate());
								writer.writeField(garn.getGarnishmentType().getWageType().getPayrollInterfaceCode());
								writer.writeField(garn.getNetOrGross() + "");
								if (garn.getDeductionAmount() < .01) {
									writer.writeField("P");
									writer.writeField(Formatting.formatNumber(garn.getDeductionPercentage(), 2));
									writer.writeField(Formatting.formatNumber(garn.getMaxDollars(), 2));
								} else {
									writer.writeField("D");
									writer.writeField(Formatting.formatNumber(garn.getDeductionAmount(), 2));
									writer.writeField(Formatting.formatNumber(garn.getMaxPercent(), 2));
								}
								writer.writeField(garn.getDocketNumber());
								writer.writeField(garn.getFipsCode());
								writer.writeField(garn.getIssueState());
								writer.writeField(garn.getCollectionState());
								writer.writeField(garn.getRemitToName());
								writer.writeField(garn.getRemitTo().getStreet());
								writer.writeField(garn.getRemitTo().getStreet2());
								writer.writeField(garn.getRemitTo().getCity());
								writer.writeField(garn.getRemitTo().getState());
								writer.writeField(garn.getRemitTo().getZip());
								writer.writeField(garn.getRemitTo().getCountry());
							} else
								for (int skip = 0; skip < 18; skip++)
									writer.writeField("");

						}
					} else if (id.equals(EmployeeListFields.ACCRUED_TIME_OFF))
						for (HrBenefit b : hsu.createCriteria(HrBenefit.class).eq(HrBenefit.TIMERELATED, 'Y').orderBy(HrBenefit.NAME).list())
							writer.writeField(com.arahant.utils.Formatting.formatNumber(employee.getHoursLeftOnBenefit(b.getName()), 2));
					else if (id.equals(EmployeeListFields.BENEFITS))
						for (BenefitOutput bo : benefitList)
							if (bo.category) {
								//what is the benefit for this category
								HrBenefitJoin bj = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, employee.getEmployee()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, bo.id).first();

								if (bj == null) {
									writer.writeField("No");
									for (int loop = 0; loop < 12; loop++)
										writer.writeField("");
								} else {
									writer.writeField(bj.getHrBenefitConfig().getName());

									writer.writeDate(bj.getPolicyStartDate());
									writer.writeDate(bj.getPolicyEndDate());
									writer.writeField(bj.getInsuranceId());
									writer.writeDate(bj.getCoverageStartDate());
									writer.writeDate(bj.getCoverageEndDate());
									writer.writeField(com.arahant.utils.Formatting.formatNumber(bj.getAmountCovered(), 2));
									writer.writeField(bj.getAmountPaidType() + "");
									writer.writeField(bj.getAmountPaid() > 0 ? com.arahant.utils.Formatting.formatNumber(bj.getAmountPaid(), 2) : bj.getCalculatedCostAnnualString());
									writer.writeField(bj.getUsingCOBRA() + "");
									writer.writeDate(bj.getAcceptedDateCOBRA());
									writer.writeField(bj.getMaxMonthsCOBRA());
									writer.writeField(bj.getChangeDescription());
								}


							} else {
								//do they have benefit
								HrBenefitJoin bj = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, employee.getEmployee()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFITID, bo.id).first();

								if (bj == null) {
									writer.writeField("No");
									for (int loop = 0; loop < 12; loop++)
										writer.writeField("");
								} else {
									writer.writeField("Yes");


									writer.writeDate(bj.getPolicyStartDate());
									writer.writeDate(bj.getPolicyEndDate());
									writer.writeField(bj.getInsuranceId());
									writer.writeDate(bj.getCoverageStartDate());
									writer.writeDate(bj.getCoverageEndDate());
									writer.writeField(bj.getAmountCovered());
									writer.writeField(bj.getAmountPaidType() + "");
									writer.writeField(bj.getAmountPaid() > 0 ? com.arahant.utils.Formatting.formatNumber(bj.getAmountPaid(), 2) : bj.getCalculatedCostAnnualString());
									writer.writeField(bj.getUsingCOBRA() + "");
									writer.writeDate(bj.getAcceptedDateCOBRA());
									writer.writeField(bj.getMaxMonthsCOBRA());
									writer.writeField(bj.getChangeDescription());
								}
							}
					else if (id.equals(EmployeeListFields.BENEFIT_SUMMARY))
						for (BenefitOutput bo : benefitList)
							if (bo.category) {
								//what is the benefit for this category
								HrBenefitJoin bj = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, employee.getEmployee()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, bo.id).first();

								if (bj == null) {
									writer.writeField("No");
									for (int loop = 0; loop < 1; loop++)
										writer.writeField("");
								} else {
									writer.writeField(bj.getHrBenefitConfig().getName());
									writer.writeField(com.arahant.utils.Formatting.formatNumber(bj.getAmountPaid(), 2));
								}
							} else {
								//do they have benefit
								HrBenefitJoin bj = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, employee.getEmployee()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFITID, bo.id).first();

								if (bj == null) {
									writer.writeField("No");
									for (int loop = 0; loop < 1; loop++)
										writer.writeField("");
								} else {
									writer.writeField("Yes");
									writer.writeField(com.arahant.utils.Formatting.formatNumber(bj.getAmountPaid(), 2));
								}
							}
					else if (id.equals(EmployeeListFields.EVENTS)) {
						//what is the maximum number of events for any employee

						List<HrEmployeeEvent> el = hsu.createCriteria(HrEmployeeEvent.class).eq(HrEmployeeEvent.EMPLOYEEID, employee.getEmployee()).orderBy(HrEmployeeEvent.EVENTDATE).list();

						int count = 0;
						for (HrEmployeeEvent ee : el) {
							count++;
							writer.writeDate(ee.getEventDate());
							writer.writeField(ee.getSummary());
							writer.writeField(ee.getSupervisorId().getNameLFM());
							writer.writeField(ee.getEmployeeNotified() + "");
							writer.writeDate(ee.getDateNotified());
							writer.writeField(ee.getDetail());
						}

						for (int loop = count; loop < maxEvents; loop++)
							for (int skip = 0; skip < 6; skip++)
								writer.writeField("");
					} else if (id.equals(EmployeeListFields.NOTES))
						for (NoteOutput co : noteList) {

							//what is the benefit for this category
							PersonNote cd = hsu.createCriteria(PersonNote.class).eq(PersonNote.PERSON, employee.getEmployee()).joinTo(PersonNote.HRNOTECATEGORY).eq(HrNoteCategory.CATID, co.id).first();

							if (cd == null)
								for (int loop = 0; loop < 1; loop++)
									writer.writeField("");
							else
								writer.writeField(cd.getNote());

						}
					else if (id.equals(EmployeeListFields.TRAINING))
						for (TrainingOutput co : trainingList) {

							//what is the benefit for this category
							HrTrainingDetail cd = hsu.createCriteria(HrTrainingDetail.class).eq(HrTrainingDetail.EMPLOYEE, employee.getEmployee()).joinTo(HrTrainingDetail.HRTRAININGCATEGORY).eq(HrTrainingCategory.CATID, co.id).first();

							if (cd == null)
								for (int loop = 0; loop < 3; loop++)
									writer.writeField("");
							else {
								writer.writeDate(cd.getTrainingDate());
								writer.writeField(cd.getTrainingHours());
								writer.writeDate(cd.getExpireDate());
							}

						}
					else if (id.equals(EmployeeListFields.ORG_GROUP)) {
						Set<OrgGroupAssociation> assocs = employee.getOrgGroupAssociations();

						int count = 0;

						List<OrgGroup> ogList = new ArrayList<OrgGroup>();

						if (assocs.size() > 0) {
							OrgGroupAssociation oga = assocs.iterator().next();
							OrgGroup og = oga.getOrgGroup();
							ogList.add(og);

							while (og.getOrgGroupHierarchiesForChildGroupId().size() > 0) {
								og = og.getOrgGroupHierarchiesForChildGroupId().iterator().next().getOrgGroupByParentGroupId();
								ogList.add(og);
							}

							//now I have my org groups in backwards order

							for (int loop = ogList.size() - 1; loop >= 0; loop--) {
								og = ogList.get(loop);
								count++;
								writer.writeField(og.getName());
								writer.writeField(og.getExternalId());
								if (oga.getPrimaryIndicator() == 'Y' && oga.getOrgGroupId().equals(og.getOrgGroupId()))
									writer.writeField("Y");
								else
									writer.writeField("N");
							}

						}


						for (int loop = count; loop <= maxOrgGroupDepth; loop++)
							for (int skip = 0; skip < 3; skip++)
								writer.writeField("");

						/*
						 * writer.writeField("Organization Level "+loop+"
						 * Name"); writer.writeField("Organization Level
						 * "+loop+" Code"); writer.writeField("Organization
						 * Level "+loop+" Supervisor");
						 *
						 */
					} else if (id.equals(EmployeeListFields.ORG_GROUP1))
						//printOrgLevelValue(1, employee, writer);
						printOrgLevelPath(0, employee, getGroupId(employee), writer);
					else if (id.equals(EmployeeListFields.ORG_GROUP2))
						//printOrgLevelValue(1, employee, writer);
						printOrgLevelPath(1, employee, getGroupId(employee), writer);
					else if (id.equals(EmployeeListFields.ORG_GROUP3))
						//printOrgLevelValue(3, employee, writer);
						printOrgLevelPath(2, employee, getGroupId(employee), writer);
					else if (id.equals(EmployeeListFields.ORG_GROUP4))
						//printOrgLevelValue(4, employee, writer);
						printOrgLevelPath(3, employee, getGroupId(employee), writer);
					else if (id.equals(EmployeeListFields.ORG_GROUP5))
						//printOrgLevelValue(5, employee, writer);
						printOrgLevelPath(4, employee, getGroupId(employee), writer);
					else if (id.equals(EmployeeListFields.FORMS)) {
						//what is the maximum number of events for any employee

						List<PersonForm> el = hsu.createCriteria(PersonForm.class).eq(PersonForm.PERSON, employee.getEmployee()).orderBy(PersonForm.DATE).list();

						int count = 0;
						for (PersonForm ee : el) {
							count++;
							writer.writeField(ee.getFormType().getFormCode());
							writer.writeField(ee.getFormType().getDescription());
							writer.writeDate(ee.getFormDate());
							writer.writeField(ee.getComments());
						}

						for (int loop = count; loop < maxForms; loop++)
							for (int skip = 0; skip < 4; skip++)
								writer.writeField("");
					} else if (id.equals(EmployeeListFields.CHECK_LIST))
						for (CheckListOutput co : checkList) {

							//what is the benefit for this category
							HrChecklistDetail cd = hsu.createCriteria(HrChecklistDetail.class).eq(HrChecklistDetail.EMPLOYEEBYEMPLOYEEID, employee.getEmployee()).joinTo(HrChecklistDetail.HRCHECKLISTITEM).eq(HrChecklistItem.ITEMID, co.id).first();

							if (cd == null)
								for (int loop = 0; loop < 2; loop++)
									writer.writeField("");
							else {
								writer.writeDate(cd.getDateCompleted());
								writer.writeField(cd.getEmployeeBySupervisorId().getNameLFM());
							}

						}

				writer.endRecord();
			}
		} finally {
			try {
				scrollUtil.close();
			} catch (Exception ignored) {
			}

			try {
				writer.close();
			} catch (Exception ignored) {
			}
		}
		return FileSystemUtils.getHTTPPath(csvFile);
	}
}
