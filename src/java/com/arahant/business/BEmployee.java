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

package com.arahant.business;

import com.arahant.beans.*;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantJessException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.lisp.LispPackage;
import com.arahant.reports.*;
import com.arahant.utils.*;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;
import com.itextpdf.text.DocumentException;
import java.io.Serializable;
import java.util.*;
import jess.Fact;
import jess.JessException;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;


public class BEmployee extends BPerson {

	private static final transient ArahantLogger logger = new ArahantLogger(BEmployee.class);
	
	public static final String OPENING_BALANCE = "Opening Balance";
	public static final String CLOSING_BALANCE = "Closing Balance";

	private boolean enrolled = false;
	private double amountCovered;

	Employee employee;

	public BEmployee() {
		this(ArahantSession.getHSU());
	}

	public BEmployee(final HibernateSessionUtil hsu) {
	}

	public BEmployee(final String key) throws ArahantException {
		internalLoad(key);
	}

	public BEmployee(final Employee emp, final String orgGroupId) throws ArahantException {
		super(emp, orgGroupId);
		employee = emp;
	}

	/**
	 * @param personId
	 * @param groupId
	 * @throws ArahantException
	 */
	public BEmployee(final String personId, final String groupId) throws ArahantException {
		super(ArahantSession.getHSU().get(Person.class, personId), groupId);
		employee = ArahantSession.getHSU().get(Employee.class, personId);
	}

	public BEmployee(final Employee emp) throws ArahantException {
		super(emp);
		employee = emp;
	}

	/**
	 * @param current
	 * @throws ArahantException
	 */
	public BEmployee(final BPerson current) throws ArahantException {
		this(current.person.getPersonId());
	}

	public HrEmployeeStatus getStatusCurrent() {
		return employee.getStatus();
	}

	public void setStatus(HrEmployeeStatus status) {
		employee.setStatus(status);
	}

	public int getStatusEffectiveDate() {
		return employee.getStatusEffectiveDate();
	}

	public void setStatusEffectiveDate(int statusEffectiveDate) {
		employee.setStatusEffectiveDate(statusEffectiveDate);
	}

	@Override
	public boolean getHandicap() {
		return employee.getHandicap() == 'Y';
	}

	@Override
	public void setHandicap(boolean handicap) {
		employee.setHandicap(handicap ? 'Y' : 'N');
	}

	public String getStatusId() {
		return employee.getStatusId();
	}

	public void setStatusId(String statusId) {
		employee.setStatus(ArahantSession.getHSU().get(HrEmployeeStatus.class, statusId));
	}

	public void checkOvertimeLogout() {
		checkOvertimeLogout(DateUtils.now(), DateUtils.nowTime());
	}

	public void checkOvertimeLogout(int day, int time) {
		//how long have I been logged in
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		float worked = hsu.createCriteria(Timesheet.class).sum(Timesheet.TOTALHOURS).eq(Timesheet.PERSON, employee).eq(Timesheet.WORKDATE, day).floatVal();

		float approvedTime = hsu.createCriteria(OvertimeApproval.class).sum(OvertimeApproval.APPROVED_HOURS).eq(OvertimeApproval.EMPLOYEE, employee).eq(OvertimeApproval.WORK_DATE, day).floatVal();


		//make sure I don't make any new timesheets if they keep trying
		if (employee.getLengthOfBreaks() + employee.getLengthOfWorkDay() + approvedTime <= worked) {
			ArahantSession.addReturnMessage("You have reached your working time limit for the day.\nYou will be logged out now.");
			ArahantSession.setReturnCode(100);
		}

		Timesheet lastSheet = hsu.createCriteria(Timesheet.class).eq(Timesheet.PERSON, employee).eq(Timesheet.WORKDATE, day).eq(Timesheet.ENDTIME, -1).eq(Timesheet.TOTALHOURS, (double) 0).first();

		if (lastSheet != null) {
			BTimesheet bt = new BTimesheet(lastSheet);

			Calendar cal = DateUtils.getCalendar(lastSheet.getWorkDate());
			cal.set(Calendar.HOUR_OF_DAY, DateUtils.getHour(lastSheet.getBeginningTime()));
			cal.set(Calendar.MINUTE, DateUtils.getMinutes(lastSheet.getBeginningTime()));

			//	SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
			//	System.out.println(sdf.format(cal.getTime()));
			Calendar now = DateUtils.getNow();

			long dif = now.getTimeInMillis() - cal.getTimeInMillis();

			float totalWorked = ((float) dif) / 1000 / 60 / 60 + worked; //figure out how much they have worked

			if (employee.getLengthOfBreaks() + employee.getLengthOfWorkDay() + approvedTime <= totalWorked) {
				//punch them out at length of work day + approved time - worked
				float punchOutHours = employee.getLengthOfWorkDay() + approvedTime - worked;

				bt.setTotalHours(punchOutHours);
				bt.setEndDate(day);
				bt.setEndTime(time);
				bt.update();

				ArahantSession.addReturnMessage("You have reached your working time limit for the day.\nYou will be logged out now.");
				ArahantSession.setReturnCode(100);
			}
		} else {
			float totalWorked = worked; // figure out how much they have worked

			if (employee.getLengthOfBreaks() + employee.getLengthOfWorkDay() + approvedTime <= totalWorked) {
				//punch them out at length of work day + approved time - worked
				float punchOutHours = employee.getLengthOfWorkDay() + approvedTime - worked;
				if (punchOutHours > 0.01) {
					BTimesheet bt = new BTimesheet();
					bt.create();
					bt.setTotalHours(punchOutHours);
					bt.setEndDate(day);
					bt.setEndTime(time);
					bt.setPersonId(person.getPersonId());
					bt.setStartDate(day);

					int startTime = time;

					int hrs = (int) punchOutHours;
					startTime -= hrs * 100 * 100;
					int mins = (int) (punchOutHours - hrs) * 60;
					startTime -= mins * 100;
					bt.setStartTime(startTime);
					bt.setDescription("Auto clock in by Login");
					bt.setBeginningEntryDate(new Date());
					bt.setWageTypeId(getWageTypeId());

					try {
						Project pr = getDefaultProject();
						bt.setBillable(pr.getBillable());
	//					bt.setProjectId(pr.getProjectId());
						if (true) throw new ArahantException("XXYY");
					} catch (Exception e) {
						throw new ArahantWarning("Please set up default project for this user to enable clock-in.");
					}

					bt.update();
				}

				ArahantSession.addReturnMessage("You have reached your working time limit for the day.\nYou will be logged out now.");
				ArahantSession.setReturnCode(100);
			}
		}
	}

	@Override
	public String create() throws ArahantException {
		employee = new Employee();
		person = employee;
		person.generateId();
		person.setOrgGroupType(COMPANY_TYPE);

		this.createOther();

		return getPersonId();
	}

	public double getAddFederalIncomeTaxAmount() {
		return employee.getAddFederalIncomeTax();
	}

	public String getAddFederalIncomeTaxAmountFormatted() {
		switch (employee.getAddFederalIncomeTaxType()) {
			case 'N':
				return "";
			case 'F':
				return MoneyUtils.formatMoney(employee.getAddFederalIncomeTax());
			case 'P':
				return Formatting.formatPercentage(employee.getAddFederalIncomeTax(), 2);
			case 'A':
				return MoneyUtils.formatMoney(employee.getAddFederalIncomeTax());
			default:
				return "";
		}
	}

	public String getAddFederalIncomeTaxType() {
		return employee.getAddFederalIncomeTaxType() + "";
	}

	public String getAddFederalIncomeTaxTypeName() {
		switch (employee.getAddFederalIncomeTaxType()) {
			case 'N':
				return "None";
			case 'F':
				return "Flat Amount";
			case 'P':
				return "Percent Deduction";
			case 'A':
				return "Additional Amount";
			default:
				return "";
		}
	}

	public double getAddLocalIncomeTaxAmount() {
		return employee.getAddLocalIncomeTax();
	}

	public String getAddLocalIncomeTaxAmountFormatted() {
		switch (employee.getAddLocalIncomeTaxType()) {
			case 'N':
				return "";
			case 'F':
				return MoneyUtils.formatMoney(employee.getAddLocalIncomeTax());
			case 'P':
				return Formatting.formatPercentage(employee.getAddLocalIncomeTax(), 2);
			case 'A':
				return MoneyUtils.formatMoney(employee.getAddLocalIncomeTax());
			default:
				return "";
		}
	}

	public String getAddLocalIncomeTaxType() {
		return employee.getAddLocalIncomeTaxType() + "";
	}

	public String getAddLocalIncomeTaxTypeName() {
		switch (employee.getAddLocalIncomeTaxType()) {
			case 'N':
				return "None";
			case 'F':
				return "Flat Amount";
			case 'P':
				return "Percent Deduction";
			case 'A':
				return "Additional Amount";
			default:
				return "";
		}
	}

	public double getAddStateDisabilityTaxAmount() {
		return employee.getAddStateDisabilityTax();
	}

	public String getAddStateDisabilityTaxAmountFormatted() {
		switch (employee.getAddStateDisabilityTaxType()) {
			case 'N':
				return "";
			case 'F':
				return MoneyUtils.formatMoney(employee.getAddStateDisabilityTax());
			case 'P':
				return Formatting.formatPercentage(employee.getAddStateDisabilityTax(), 2);
			case 'A':
				return MoneyUtils.formatMoney(employee.getAddStateDisabilityTax());
			default:
				return "";
		}
	}

	public String getAddStateDisabilityTaxType() {
		return employee.getAddStateDisabilityTaxType() + "";
	}

	public String getAddStateDisabilityTaxTypeName() {
		switch (employee.getAddStateDisabilityTaxType()) {
			case 'N':
				return "None";
			case 'F':
				return "Flat Amount";
			case 'P':
				return "Percent Deduction";
			case 'A':
				return "Additional Amount";
			default:
				return "";
		}
	}

	public double getAddStateIncomeTaxAmount() {
		return employee.getAddStateIncomeTax();
	}

	public String getAddStateIncomeTaxAmountFormatted() {
		switch (employee.getAddStateIncomeTaxType()) {
			case 'N':
				return "";
			case 'F':
				return MoneyUtils.formatMoney(employee.getAddStateIncomeTax());
			case 'P':
				return Formatting.formatPercentage(employee.getAddStateIncomeTax(), 2);
			case 'A':
				return MoneyUtils.formatMoney(employee.getAddStateIncomeTax());
			default:
				return "";
		}
	}

	public String getAddStateIncomeTaxType() {
		return employee.getAddStateIncomeTaxType() + "";
	}

	public String getAddStateIncomeTaxTypeName() {
		switch (employee.getAddStateIncomeTaxType()) {
			case 'N':
				return "None";
			case 'F':
				return "Flat Amount";
			case 'P':
				return "Percent Deduction";
			case 'A':
				return "Additional Amount";
			default:
				return "";
		}
	}

	/**
	 * Lists configs that are valid for this employee based on his benefit class
	 * and their active dates. There is a similar function in BHRBenefit, but it
	 * also checks approved and active dependents and filters the list further.
	 *
	 */
	public BHRBenefitConfig[] getAllValidConfigs(String benefitId) {
		final HibernateCriteriaUtil<HrBenefitConfig> hcu = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class);
		
		
//		hcu.dateInside(HrBenefitConfig.START_DATE, HrBenefitConfig.END_DATE, DateUtils.now())
//				.orderBy(HrBenefitConfig.SEQ)
//				.joinTo(HrBenefitConfig.HR_BENEFIT)
//				.eq(HrBenefit.BENEFITID, benefitId)
//				.orderBy(HrBenefit.NAME)
//				.dateInside(HrBenefit.START_DATE, HrBenefit.END_DATE, DateUtils.now());
		
		
		//  should be able to elect benefits that are not active yet in an open enrollment situation
		hcu.gtOrEq(HrBenefitConfig.END_DATE, DateUtils.now(), 0)
				.orderBy(HrBenefitConfig.SEQ)
				.joinTo(HrBenefitConfig.HR_BENEFIT)
				.eq(HrBenefit.BENEFITID, benefitId)
				.orderBy(HrBenefit.NAME)
				.gtOrEq(HrBenefit.END_DATE, DateUtils.now(), 0)
				.gtOrEq(HrBenefit.LAST_ENROLLMENT_DATE, DateUtils.now(), 0);

		//check benefit classes
		if (getBenefitClass() != null) {
			//either has no classes specified or they match
			HibernateCriterionUtil orcri = hcu.makeCriteria();
			HibernateCriterionUtil cri1 = hcu.makeCriteria();

			HibernateCriteriaUtil classHcu = hcu.leftJoinTo(HrBenefitConfig.BENEFIT_CLASSES, "classes");

			HibernateCriterionUtil cri2 = classHcu.makeCriteria();

			cri1.sizeEq(HrBenefitConfig.BENEFIT_CLASSES, 0);
			cri2.eq("classes." + BenefitClass.BENEFIT_CLASS_ID, getBenefitClass().getBenefitClassId());

			orcri.or(cri1, cri2);

			orcri.add();
		}

		return BHRBenefitConfig.makeArray(hcu.list());
	}

	public String getBankAccountId() {
		if (employee.getPayrollBankAccount() == null)
			return "";
		return employee.getPayrollBankAccount().getBankAccountId();
	}

	public BenefitClass getBenefitClass() {
		return getBenefitClass(true);
	}

	public BenefitClass getBenefitClass(boolean inheritedIfNull) {
		if (employee.getBenefitClass() != null)
			return employee.getBenefitClass();
		return inheritedIfNull ? getInheritedBenefitClass() : null;
	}

	public String getBenefitClassId() {
		return getBenefitClassId(true);
	}

	public String getBenefitClassId(boolean inheritedIfNull) {
		if (employee.getBenefitClass() != null)
			return employee.getBenefitClass().getBenefitClassId();
		return inheritedIfNull ? getInheritedBenefitClassId() : "";
	}

	public double getBreakHours() {
		return employee.getLengthOfBreaks();
	}

	public int getCurrentPayPeriodEndDate() {
		PaySchedule ps = getPaySchedule();

		if (ps == null)
			return 0;

		BPaySchedule bps = new BPaySchedule(ps);
		return bps.getCurrentPeriodEnd();
	}

	public int getCurrentPayPeriodStartDate() {
		PaySchedule ps = getPaySchedule();

		if (ps == null)
			return 0;

		BPaySchedule bps = new BPaySchedule(ps);
		return bps.getCurrentPeriodStart();
	}

	public boolean getOvertimeLogout() {
		return employee.getAutoOvertimeLogout() == 'Y';
	}

	public String getPayrollBankCode() {
		if (employee.getPayrollBankAccount() == null)
			return "";
		return employee.getPayrollBankAccount().getBankId();
	}

	public boolean getDirty() {
		return !employee.getEmployeeChanges().isEmpty();
	}

	public String getEarnedIncomeCreditStatus() {
		return employee.getEarnedIncomeCreditStatus() + "";
	}

	public String getEarnedIncomeCreditStatusName() {
		switch (employee.getEarnedIncomeCreditStatus()) {
			case 'I':
				return "Individual";
			case 'J':
				return "Joint";
			case 'W':
				return "Married filing w/o Spouse";
			default:
				return "";
		}
	}

	public Employee getEmployee() {
		return employee;
	}

	public int getEmployeeStatusDate() {
		try {
			return getLastStatusHistory().getEffectiveDate();
		} catch (final Exception e) {
		}
		return 0;
	}

	public double getExpectedHoursPerPayPeriod() {
		return employee.getExpectedHoursPerPeriod();
	}

	public int getFederalExemptions() {
		return employee.getNumberFederalExemptions();
	}

	public double getFederalExtraWithheld() {
		return employee.getFederalExtraWithhold();
	}

	public String getLocalTaxCode() {
		return employee.getLocalTaxCode();
	}

	public String getMaritalStatus() {
		return employee.getMaritalStatus() + "";
	}

	public String getMaritalStatusName() {
		switch (employee.getMaritalStatus()) {
			case 'S':
				return "Single";
			case 'M':
				return "Married";
			case 'D':
				return "Divorced";
			case 'W':
				return "Widowed";
			default:
				return "";
		}
	}

	public int getPayPeriodsPerYear() {
		if (employee.getPayPeriodsPerYear() == 0)
			//check the org group
			if (!employee.getOrgGroupAssociations().isEmpty()) {
				OrgGroup og = ArahantSession.getHSU().createCriteria(OrgGroup.class).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, employee).first();
				BOrgGroup borg = new BOrgGroup(og);
				return borg.getPayPeriodsPerYear();
			} else if (employee.getCompanyBase() != null  &&  employee.getCompanyBase().getPayPeriodsPerYear() > 0)
				return employee.getCompanyBase().getPayPeriodsPerYear();
		return employee.getPayPeriodsPerYear();
	}

	public int getStateExemptions() {
		return employee.getNumberStateExemptions();
	}

	public double getStateExtraWithheld() {
		return employee.getStateExtraWithhold();
	}

	public double getWorkHours() {
		return employee.getLengthOfWorkDay();
	}

	public boolean hasPension() {
		return ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, employee).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.TYPE, HrBenefitCategory.PENSION).exists();
	}

	public boolean isAutoOvertimeLogout() {
		return employee.getAutoOvertimeLogout() == 'Y';
	}

	public void setAddFederalIncomeTaxAmount(double addFederalIncomeTaxAmount) {
		employee.setAddFederalIncomeTax((float) addFederalIncomeTaxAmount);
	}

	public void setAddFederalIncomeTaxType(String addFederalIncomeTaxType) {
		if (addFederalIncomeTaxType == null || addFederalIncomeTaxType.trim().equals(""))
			addFederalIncomeTaxType = " ";
		employee.setAddFederalIncomeTaxType(addFederalIncomeTaxType.charAt(0));
	}

	public void setAddLocalIncomeTaxAmount(double addLocalIncomeTaxAmount) {
		employee.setAddLocalIncomeTax((float) addLocalIncomeTaxAmount);
	}

	public void setAddLocalIncomeTaxType(String addLocalIncomeTaxType) {
		if (addLocalIncomeTaxType == null || addLocalIncomeTaxType.trim().equals(""))
			addLocalIncomeTaxType = " ";
		employee.setAddLocalIncomeTaxType(addLocalIncomeTaxType.charAt(0));
	}

	public void setAddStateDisabilityTaxAmount(double addStateDisabilityTaxAmount) {
		employee.setAddStateDisabilityTax((float) addStateDisabilityTaxAmount);
	}

	public void setAddStateDisabilityTaxType(String addStateDisabilityTaxType) {
		if (addStateDisabilityTaxType == null || addStateDisabilityTaxType.trim().equals(""))
			addStateDisabilityTaxType = " ";
		employee.setAddStateDisabilityTaxType(addStateDisabilityTaxType.charAt(0));
	}

	public void setAddStateIncomeTaxAmount(double addStateIncomeTaxAmount) {
		employee.setAddStateIncomeTax((float) addStateIncomeTaxAmount);
	}

	public void setAddStateIncomeTaxType(String addStateIncomeTaxType) {
		if (addStateIncomeTaxType == null || addStateIncomeTaxType.trim().equals(""))
			addStateIncomeTaxType = " ";
		employee.setAddStateIncomeTaxType(addStateIncomeTaxType.charAt(0));
	}

	public void setBenefitClassId(String benefitClassId) {
		employee.setBenefitClass(ArahantSession.getHSU().get(BenefitClass.class, benefitClassId));
	}

	public void setBreakHours(double breakHours) {
		employee.setLengthOfBreaks((float) breakHours);
	}

	public void setEarnedIncomeCreditStatus(String earnedIncomeCreditStatus) {
		if (earnedIncomeCreditStatus == null || earnedIncomeCreditStatus.trim().equals(""))
			earnedIncomeCreditStatus = " ";
		employee.setEarnedIncomeCreditStatus(earnedIncomeCreditStatus.charAt(0));
	}

	public void setExpectedHoursPerPayPeriod(double expectedHoursPerPayPeriod) {
		employee.setExpectedHoursPerPeriod((float) expectedHoursPerPayPeriod);
	}

	public void setFederalExemptions(int federalExemptions) {
		employee.setNumberFederalExemptions((short) federalExemptions);
	}

	public void setFederalExtraWithheld(double federalExtraWithheld) {
		employee.setFederalExtraWithhold((float) federalExtraWithheld);
	}

	public void setLocalTaxCode(String localTaxCode) {
		employee.setLocalTaxCode(localTaxCode);
	}

	public void setMaritalStatus(String maritalStatus) {
		if (maritalStatus == null || maritalStatus.trim().equals(""))
			maritalStatus = " ";
		employee.setMaritalStatus(maritalStatus.charAt(0));
	}

	public void setOvertimeLogout(boolean overtimeLogout) {
		employee.setAutoOvertimeLogout(overtimeLogout ? 'Y' : 'N');
	}

	public void setPayPeriodsPerYear(int payPeriodsPerYear) {
		employee.setPayPeriodsPerYear((short) payPeriodsPerYear);
	}

	public void setPayrollBankAccountId(String bankAccountId) {
		employee.setPayrollBankAccount(ArahantSession.getHSU().get(BankAccount.class, bankAccountId));
	}

	public void setScreenGroup(ScreenGroup screenGroup) {
		prophetLogin.setScreenGroup(screenGroup);
	}

	public void setSecurityGroup(SecurityGroup secGroup) {
		prophetLogin.setSecurityGroup(secGroup);
	}

	public void setStateExemptions(int stateExemptions) {
		employee.setNumberStateExemptions((short) stateExemptions);
	}

	public void setStateExtraWithheld(double stateExtraWithheld) {
		employee.setStateExtraWithhold((float) stateExtraWithheld);
	}

	public void setTaxState(String taxState) {
		employee.setTaxState(taxState);
	}

	public void setTimeLog(boolean timeLog) {
		employee.setClockInTimeLog(timeLog ? 'Y' : 'N');
	}

	public void setUnemploymentState(String unemploymentState) {
		employee.setUnemploymentState(unemploymentState);
	}

	public void setWorkHours(double workHours) {
		employee.setLengthOfWorkDay((float) workHours);
	}

	@Override
	protected void createOther() throws ArahantException {
		super.createOther();
		// employee specific creation stuff goes here
	}

	@Override
	public void insert() throws ArahantException {
		insert(false);
	}

	public void insert(boolean noOrgGroupAssociation) throws ArahantException {
		super.insert();
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		if (employee.getPayPeriodsPerYear() == 0)
			employee.setPayPeriodsPerYear((short) BProperty.getInt(StandardProperty.DefaultPPY));

		if (employee.getHrEeoCategory() == null)
			employee.setHrEeoCategory(hsu.get(HrEeoCategory.class, BProperty.get(StandardProperty.DefaultEEOCategoryId)));

		if (employee.getHrEeoRace() == null)
			employee.setHrEeoRace(hsu.get(HrEeoRace.class, BProperty.get(StandardProperty.DefaultEEORaceId)));


		if (employee.getCompanyBase() == null)
			employee.setCompanyBase(hsu.getCurrentCompany());

		hsu.insert(employee);

		if (hsu.aiIntegrate())
			employee.linkToEngine();

		if (!noOrgGroupAssociation)
			//if the company has no sub org groups, go ahead and associate them to company group
			if (employee.getCompanyBase().getOrgGroupHierarchiesForParentGroupId().isEmpty())
				assignToOrgGroup(hsu.getCurrentCompany().getOrgGroupId(), false);

		//hsu.flush();//tell it to insert the person before I try to make the project

		//	ArahantSession.getAI().watchAll();
		//ArahantSession.AIEval("(assert (InsertedNewEmployee \""+employee.getPersonId()+"\"))");
		//	ArahantSession.AIEval("(facts)");
	}

	public void insertNoDefaults() throws ArahantException {
		super.insertNoChecks();

		if (employee.getCompanyBase() == null)
			employee.setCompanyBase(ArahantSession.getHSU().getCurrentCompany());

		ArahantSession.getHSU().insert(employee);
	}

	public void makeLoginDefaults() throws ArahantException {
		makeLoginDefaults(false);
	}

	public void makeLoginDefaults(boolean skipPasswordValidation) throws ArahantException {
		String middleSubstring = isEmpty(getMiddleName()) ? "" : getMiddleName().substring(0, 1);
		String loginName = (getFirstName().substring(0, 1) + middleSubstring + getLastName()).toLowerCase();

		if (loginName.length() > 18)
			loginName = loginName.substring(0, 18);

		final Random rand = new Random();
		int val = rand.nextInt(99);

		HibernateSessionUtil hsu = ArahantSession.getHSU();
		while (hsu.createCriteria(ProphetLogin.class).eq(ProphetLogin.USERLOGIN, loginName + val).exists())
			val = rand.nextInt(99);

		setUserLogin(loginName + val);

		//we are going to use a five digit number per Blake
		setUserPassword(String.format("%05d", rand.nextInt(99999)), true, skipPasswordValidation);
		String securityGroupId = BProperty.get(StandardProperty.DEFAULT_SEC_GROUP);
		String screenGroupId = BProperty.get(StandardProperty.DEFAULT_SCREEN_GROUP);
		if (isEmpty(screenGroupId))
			System.out.println("MakeLoginDefaults ERROR (" + DateUtils.getDateAndTimeFormatted(new Date()) + "): Property " + StandardProperty.DEFAULT_SCREEN_GROUP + " is empty so username/password was not generated for " + getNameFML());
		if (isEmpty(securityGroupId))
			System.out.println("MakeLoginDefaults ERROR (" + DateUtils.getDateAndTimeFormatted(new Date()) + "): Property " + StandardProperty.DEFAULT_SEC_GROUP + " is empty so username/password was not generated for " + getNameFML());
		setSecurityGroupId(securityGroupId);
		setScreenGroupId(screenGroupId);
		prophetLogin.setPasswordEffectiveDate(DateUtils.now());
	}

	public static String makeUserLogin(String firstName, String lastName) {
		String loginName = (firstName.charAt(0) + lastName).toLowerCase();

		if (loginName.length() > 18)
			loginName = loginName.substring(0, 18);

		final Random rand = new Random();
		int val = rand.nextInt(99);

		HibernateSessionUtil hsu = ArahantSession.getHSU();
		while (hsu.createCriteria(ProphetLogin.class).eq(ProphetLogin.USERLOGIN, loginName + val).exists())
			val = rand.nextInt(99);

		return loginName + val;
	}

	public static String makeUserPassword() {
		final Random rand = new Random();
		return String.format("%05d", rand.nextInt(99999));
	}

	@Override
	public void update() throws ArahantException {
		super.update();
		ArahantSession.getHSU().saveOrUpdate(employee);
	}
	
	private void internalLoad(final String key) throws ArahantException {
		logger.debug("Loading " + key);
		super.load(key);
		//employee=hsu.get(Employee.class, key);
		try {
			employee = (Employee) person;
		} catch (ClassCastException e) {
//			throw new ArahantException("You are logged in as the Arahant user. You must log in as an employee to access this screen");
			throw new ArahantException("Person is not an employee.");
		}
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	public static void delete(final HibernateSessionUtil hsu, final String [] personIds) throws ArahantException {
		for (final String element : personIds)
			new BEmployee(element).delete();
	}

	@Override
	public void delete() throws ArahantDeleteException {
		//hsu.delete(employee);
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.delete(employee.getHrBenefitJoinsWhereCovered());
		hsu.createCriteria(HrWage.class).eq(HrWage.EMPLOYEE, employee).delete();
		super.delete();
	}

	public static BEmployee[] listEmployees(final HibernateSessionUtil hsu, final String orgGroupId) throws ArahantException {
		return BEmployee.makeArray(hsu.createCriteria(Employee.class).orderBy(Person.LNAME).orderBy(Person.FNAME).joinTo(Person.ORGGROUPASSOCIATIONS).joinTo(OrgGroupAssociation.ORGGROUP).eq(OrgGroup.ORGGROUPID, orgGroupId).list());
	}

	public static BEmployee[] listEmployees(final String[] excludeIds, final String orgGroupId, final String lNameStartsWith, final int cap) throws ArahantException {
		if (isEmpty(orgGroupId))
			return BEmployee.makeArray(ArahantSession.getHSU().createCriteria(Employee.class).orderBy(Person.LNAME).orderBy(Person.FNAME).notIn(Person.PERSONID, excludeIds).like(Person.LNAME, lNameStartsWith + "%").joinTo(Person.ORGGROUPASSOCIATIONS).joinTo(OrgGroupAssociation.ORGGROUP).eq(OrgGroup.ORGGROUPID, ArahantSession.getHSU().getCurrentCompany().getOrgGroupId()).setMaxResults(cap).list());

		return BEmployee.makeArray(ArahantSession.getHSU().createCriteria(Employee.class).orderBy(Person.LNAME).orderBy(Person.FNAME).notIn(Person.PERSONID, excludeIds).like(Person.LNAME, lNameStartsWith + "%").joinTo(Person.ORGGROUPASSOCIATIONS).joinTo(OrgGroupAssociation.ORGGROUP).eq(OrgGroup.ORGGROUPID, orgGroupId).setMaxResults(cap).list());
	}

	public static BEmployee[] listEmployees(final HibernateSessionUtil hsu, final String orgGroupId, final String lastName, final boolean supervisor, final int max) throws ArahantException {
		//TODO: this should filter based on all companies available to the person
		final HibernateCriteriaUtil<Employee> hcu = hsu.createCriteriaNoCompanyFilter(Employee.class).orderBy(Person.LNAME).orderBy(Person.FNAME).setMaxResults(max).like(Employee.LNAME, lastName);

		final HibernateCriteriaUtil ogaHcu = hcu.joinTo(Person.ORGGROUPASSOCIATIONS);

		if (supervisor)
			ogaHcu.eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y');

		ogaHcu.joinTo(OrgGroupAssociation.ORGGROUP).eq(OrgGroup.ORGGROUPID, orgGroupId);

		final BEmployee[] bemps = BEmployee.makeArray(hcu.list());

		//set the primary flag
		final List<OrgGroupAssociation> ogaList = hsu.createCriteria(OrgGroupAssociation.class).eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y').joinTo(OrgGroupAssociation.ORGGROUP).eq(OrgGroup.ORGGROUPID, orgGroupId).list();

		for (final OrgGroupAssociation oga : ogaList)
			for (BEmployee element : bemps)
				if (element.getPersonId().equals(oga.getPersonId()))
					element.setPrimaryIndicator(true);

		return bemps;
	}

	public static BEmployee[] listEmployees(final HibernateSessionUtil hsu, final String groupId, final int cap, final String name) throws ArahantException {
		final HibernateCriteriaUtil<Employee> hcu = hsu.createCriteriaNoCompanyFilter(Employee.class).orderBy(Person.LNAME).orderBy(Person.FNAME).like(Person.LNAME, name).setMaxResults(cap);

		final HibernateCriteriaUtil ogaHcu = hcu.joinTo(Person.ORGGROUPASSOCIATIONS);

		ogaHcu.joinTo(OrgGroupAssociation.ORGGROUP).eq(OrgGroup.ORGGROUPID, groupId).list();

		return makeArray(hcu.list(), groupId);
	}

	static BEmployee[] makeArray(final HibernateScrollUtil<Employee> scr, final String groupId) throws ArahantException {
		List<Employee> l = new ArrayList<Employee>();
		while (scr.next())
			l.add(scr.get());

		return BEmployee.makeArray(l, groupId);
	}

	static BEmployee[] makeArray(final List<Employee> plist, final String groupId) throws ArahantException {
		final BEmployee[] ret = new BEmployee[plist.size()];

		for (int loop = 0; loop < plist.size(); loop++)
			ret[loop] = new BEmployee(plist.get(loop), groupId);
		return ret;
	}

	public int getEmploymentDate() {
		//look in status history and return first status that is active
		final HrEmplStatusHistory h = ArahantSession.getHSU().createCriteria(HrEmplStatusHistory.class).eq(HrEmplStatusHistory.EMPLOYEE, employee).setMaxResults(1).orderBy(HrEmplStatusHistory.EFFECTIVEDATE).joinTo(HrEmplStatusHistory.HREMPLOYEESTATUS).eq(HrEmployeeStatus.ACTIVE, 'Y').first();

		if (h == null)
			return 0;

		return h.getEffectiveDate();
	}

	public int getHireDate() {
		return getEmploymentDate();
	}

	public int getTimesheetFinalDate() {
		return employee.getTimesheetFinalDate();
	}

	public static BEmployee[] searchEmployeesNoCompanyFilter(final HibernateSessionUtil hsu, final String ssn, final String fname, final String lname, final String orgGroupId, final int assocInd, final int max, final String notInOrgGroup) throws ArahantException {
		return searchEmployeesNoCompanyFilter(hsu, ssn, fname, lname, orgGroupId, assocInd, max, -1, notInOrgGroup);
	}

	public static BEmployee[] searchEmployees(final HibernateSessionUtil hsu, final String ssn, final String fname, final String lname, final String orgGroupId, final int assocInd, final int max, final String notInOrgGroup) throws ArahantException {
		return searchEmployees(hsu, ssn, fname, lname, orgGroupId, assocInd, max, -1, notInOrgGroup);
	}

	public static BEmployee[] searchEmployeesAssignScreen(final HibernateSessionUtil hsu, final String ssn, final String fname, final String lname, final int assocInd, final int max, final String notInOrgGroup) throws ArahantException {
		//	return searchEmployees(hsu, ssn, fname, lname, orgGroupId, assocInd, max, -1, null, notInOrgGroup);

		HibernateCriteriaUtil<Person> hcu = hsu.createCriteriaNoCompanyFilter(Person.class).like(Employee.FNAME, fname).like(Employee.LNAME, lname);

		//	hcu.isEmployee();

		if (!isEmpty(ssn))
			hcu.eq(Employee.SSN, ssn);


		BOrgGroup notInOG = new BOrgGroup(notInOrgGroup);

		if (!isEmpty(notInOG.getCompanyId()))
			hcu.joinTo(Employee.COMPANYBASE).eq(CompanyBase.ORGGROUPID, notInOG.getCompanyId());
		else
			hcu.in(Employee.COMPANYBASE, BPerson.getCurrent().getAllowedCompanies());

		if (!isEmpty(notInOrgGroup))
			hcu.notIn(Employee.PERSONID, (List) hsu.createCriteria(Person.class).selectFields(Person.PERSONID).joinTo(Person.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.ORG_GROUP_ID, notInOrgGroup).list());

		if (assocInd == 1)
			//hcu.isNotNull(Person.ORGGROUPASSOCIATIONS);
			hcu.sizeNe(Person.ORGGROUPASSOCIATIONS, 0);
		if (assocInd == 2)
			//hcu.isNull(Person.ORGGROUPASSOCIATIONS);
			hcu.sizeEq(Person.ORGGROUPASSOCIATIONS, 0);

		hcu.activeEmployee60Days();

		return BPerson.makeEmployeeArray(hcu.list());
	}

	public static List<Employee> searchEmployeesByWizardStatus(String fname, String lname, int fromDate, int toDate, String wizardStatus, int max) {
		HibernateCriteriaUtil<Employee> hcu = ArahantSession.getHSU().createCriteria(Employee.class).eq(Employee.RECORD_TYPE, 'R').orderByDesc(Employee.BENEFIT_WIZARD_DATE).orderBy(Employee.LNAME).orderBy(Employee.FNAME).setMaxResults(max);

		if (!isEmpty(fname))
			hcu.like(Employee.FNAME, fname);
		if (!isEmpty(lname))
			hcu.like(Employee.LNAME, lname);
		if (fromDate > 0 || toDate > 0)
			hcu.dateBetween(Employee.BENEFIT_WIZARD_DATE, fromDate, toDate);
		if (!isEmpty(wizardStatus))
			hcu.eq(Employee.BENEFIT_WIZARD_STATUS, wizardStatus.charAt(0));

		return hcu.list();
	}

	public static BEmployee[] searchEmployeesNoCompanyFilter(final HibernateSessionUtil hsu, final String ssn, final String fname, final String lname, final String orgGroupId, final int assocInd, final int max, final int activeIndictator, final String notInOrgGroup) throws ArahantException {
		return searchEmployees(hsu, ssn, fname, lname, orgGroupId, assocInd, max, activeIndictator, null, notInOrgGroup);
	}

	public static BEmployee[] searchEmployees(final HibernateSessionUtil hsu, final String ssn, final String fname, final String lname, final String orgGroupId, final int assocInd, final int max, final int activeIndictator, final String notInOrgGroup) throws ArahantException {
		return searchEmployees(hsu, ssn, fname, lname, orgGroupId, assocInd, max, activeIndictator, null, notInOrgGroup);
	}

	public static BEmployee[] searchSalesPeople(final String firstName, final String lastName, final String ssn, final int cap) throws ArahantException {
		List<Employee> emps = new ArrayList<Employee>();
		emps.addAll(ArahantSession.getHSU().createCriteria(Employee.class).activeEmployee().like(Person.FNAME, firstName).like(Person.LNAME, lastName).like(Person.SSN, ssn).ne(Person.FNAME, ArahantSession.systemName()).setMaxResults(cap).orderBy(Person.LNAME).orderBy(Person.FNAME).list());
		emps.addAll(ArahantSession.getHSU().createCriteria(Employee.class).inactiveEmployee().like(Person.FNAME, firstName).like(Person.LNAME, lastName).like(Person.SSN, ssn).ne(Person.FNAME, ArahantSession.systemName()).setMaxResults(cap).orderBy(Person.LNAME).orderBy(Person.FNAME).sizeGt(Employee.PROSPECTS, 0).list());

		return makeArray(emps);
	}

	public static BEmployee[] searchEmployees(final String firstName, final String lastName, final String ssn, final int cap) throws ArahantException {
		return makeArray(ArahantSession.getHSU().createCriteria(Employee.class).like(Person.FNAME, firstName).like(Person.LNAME, lastName).like(Person.SSN, ssn).ne(Person.FNAME, ArahantSession.systemName()).setMaxResults(cap).orderBy(Person.LNAME).orderBy(Person.FNAME).list());
	}

	public static BEmployee[] searchEmployees(final String firstName, final String lastName, final String[] exclude, final int cap) throws ArahantException {
		return makeArray(ArahantSession.getHSU().createCriteria(Employee.class).like(Person.FNAME, firstName).like(Person.LNAME, lastName).notIn(Person.PERSONID, exclude).ne(Person.FNAME, ArahantSession.systemName()).setMaxResults(cap).orderBy(Person.LNAME).orderBy(Person.FNAME).orderBy(Person.MNAME).list());
	}

	public static BEmployee[] searchEmployees(final String firstName, final String lastName, final int cap) throws ArahantException {
		return makeArray(ArahantSession.getHSU().createCriteria(Employee.class).like(Person.FNAME, firstName).like(Person.LNAME, lastName).ne(Person.FNAME, ArahantSession.systemName()).setMaxResults(cap).orderBy(Person.LNAME).orderBy(Person.FNAME).orderBy(Person.MNAME).list());
	}

	public boolean getClockInTimeLog() {
		return employee.getClockInTimeLog() == 'Y';
	}

	public HrEmplStatusHistory getCurrentStatusHistory() {
		return ArahantSession.getHSU().createCriteria(HrEmplStatusHistory.class).eq(HrEmplStatusHistory.EMPLOYEE, employee).le(HrEmplStatusHistory.EFFECTIVEDATE, DateUtils.now()).orderByDesc(HrEmplStatusHistory.EFFECTIVEDATE).first();
	}
	
	/**
	 * Get employee current status from the history records.
	 *
	 */
	public HrEmployeeStatus getCurrentStatus() {
		HrEmplStatusHistory sh = getCurrentStatusHistory();
		if (sh != null)
			return sh.getHrEmployeeStatus();
		return null;
	}

	public int isActive() {
//		if the latest status is an active type, then return true
		final HrEmplStatusHistory hr = ArahantSession.getHSU().createCriteria(HrEmplStatusHistory.class).eq(HrEmplStatusHistory.EMPLOYEE, employee).le(HrEmplStatusHistory.EFFECTIVEDATE, DateUtils.now()).orderByDesc(HrEmplStatusHistory.EFFECTIVEDATE).first();

		if (hr == null)
			return -1;
		HrEmployeeStatus es = hr.getHrEmployeeStatus();
		if (es == null)
			return -1;
		return es.getActive() == 'Y' ? 0 : 1;
	}

	public boolean isActiveOn(int asOfDate) {
//		if the latest status is an active type, then return true
		final HrEmplStatusHistory hr = ArahantSession.getHSU().createCriteria(HrEmplStatusHistory.class).eq(HrEmplStatusHistory.EMPLOYEE, employee).le(HrEmplStatusHistory.EFFECTIVEDATE, asOfDate).orderByDesc(HrEmplStatusHistory.EFFECTIVEDATE).first();

		if (hr == null || hr.getHrEmployeeStatus() == null)
			return false;

		return hr.getHrEmployeeStatus().getActive() == 'Y';
	}

	@SuppressWarnings("element-type-mismatch")
	public BEmployee[] listSubordinates(boolean includeUser) throws ArahantException {
		final Employee currentUser = employee;
		final String user = prophetLogin.getUserLogin();
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		BEmployee[] personList1 = new BEmployee[0];

		if (BRight.checkRight(SEE_ALL_EMPLOYEES_IN_LISTS) == ACCESS_LEVEL_WRITE) {

			final HibernateCriteriaUtil<Employee> hcu = hsu.createCriteria(Employee.class);

			//if(!user.equals(ARAHANT_SUPERUSER))
			//	addInactiveEmployeeFilter(hcu);

			final List<Employee> plist = hcu.list();

			if (!includeUser)
				plist.remove(currentUser);

			//always remove the Arahant user
			plist.remove(getPersonByLoginId(ARAHANT_SUPERUSER, hsu));

			personList1 = new BEmployee[plist.size()];

			int index = 0;

			for (Employee value : plist)
				personList1[index++] = new BEmployee(value);

		} else if (BRight.checkRight(SEE_SUBORDINATES_IN_LISTS) == ACCESS_LEVEL_WRITE) {
			personList1 = new BEmployee[1];
			personList1[0] = new BEmployee(currentUser.getPersonId());
		} else if (currentUser.getOrgGroupAssociations() != null) {
			final Set<Person> plist = new HashSet<Person>();

			for (OrgGroupAssociation oga : currentUser.getOrgGroupAssociations()) {
				if (oga.getPrimaryIndicator() != 'Y')
					continue;
				plist.addAll(getAllPeopleInOrgGroupHierarchy(hsu, oga.getOrgGroup(), true));
			}

			if (!includeUser)
				plist.remove(currentUser);

			int index = 0;
			personList1 = new BEmployee[plist.size()];

			for (Person value : plist)
				personList1[index++] = new BEmployee(value.getPersonId());
		}

		final List<BEmployee> people = new LinkedList<BEmployee>();

		for (final BEmployee element : personList1)
			if (element.showInLists())
				people.add(element);

		//Collections.addAll(people, personList1);


		java.util.Collections.sort(people, new BEmployeeComparator());

		int index = 0;
		for (BEmployee bEmployee : people)
			personList1[index++] = bEmployee;
		return personList1;
	}

	/**
     * The newTimeEntry with the timeTypeId should be used.
	 * 
	 * @param beginningTime
	 * @param billable
	 * @param description
	 * @param endTime
	 * @param personId
	 * @param projectId
     * @param shiftId
	 * @param totalHours
	 * @param workDate
	 * @param endDate (or -1)
	 * @return
	 * @throws ArahantException 
     * 
     * @deprecated
	 */
    @Deprecated
	public String newTimeEntry(final int beginningTime, final String billable, final String description, final int endTime, final String personId, final String projectId, final String shiftId, final double totalHours, final int workDate, final int endDate) throws ArahantException {
		return newTimeEntry(beginningTime, billable, description, endTime, personId, projectId, shiftId, totalHours, workDate, endDate, "", 0, 0, 0);
	}
    
    /**
     * The newTimeEntry with the timeTypeId should be used.
     * 
     * @deprecated
     */
    @Deprecated
    public String newTimeEntry(final int beginningTime, final String billable, final String description, final int endTime, final String personId, final String projectId, 
            final String shiftId, final double totalHours, final int workDate, final int endDate, final String privateDescription, double expenses, double pay, float nonbillableHours) throws ArahantException {
        HibernateSessionUtil hsu = ArahantSession.getHSU();
        TimeType defaultTimeType = hsu.createCriteria(TimeType.class).eq(TimeType.DEFAULT_TYPE, 'Y').first();
        if (defaultTimeType == null)
            defaultTimeType = hsu.get(TimeType.class, "00001-0000000001");
        if (defaultTimeType == null)
            throw new ArahantException("No default time type defined");   
        return newTimeEntry(beginningTime, billable, description, endTime, personId, projectId, 
             shiftId, totalHours, workDate, endDate, privateDescription, expenses, pay, nonbillableHours, defaultTimeType.getTimeTypeId());   
    }


	/**
	 * nonbillable is being phased out.  Don't use it.  Nonbillable should be recorded by using billable="N" and totalHours.
	 * Billable and non-billable are kept on separate timesheet records - not the same.
	 * So, if you have billable and non-billable for the same person, day, and project there will be two separate records.
	 * Eve more if hours are being recorded.
	 * <br><br>
	 * <code>projectId</code> is not used but is kept for historical reasons.  
     * <br><br>
     * We want the code to detect a null
	 * <code>shiftId</code> and throw an exception so that we can find the problem.
	 *
	 * @param beginningTime
	 * @param billable
	 * @param description
	 * @param endTime
	 * @param personId
	 * @param projectId
     * @param shiftId
	 * @param totalHours
	 * @param workDate
	 * @param endDate
	 * @param privateDescription
	 * @param expenses
	 * @param pay
	 * @param nonbillableHours being phased out.  Don't use.
     * @param timeTypeId
	 * @return
	 * @throws ArahantException
	 */
	public String newTimeEntry(final int beginningTime, final String billable, final String description, final int endTime, final String personId, final String projectId, 
            final String shiftId, final double totalHours, final int workDate, final int endDate, final String privateDescription, double expenses, double pay, float nonbillableHours, final String timeTypeId) throws ArahantException {
		BTimesheet time;

		Person p;

		if (!isEmpty(personId))
			p = ArahantSession.getHSU().get(Person.class, personId);
		else
			p = person;
		time = new BTimesheet();
		time.create();
		time.setPersonId(p.getPersonId());
		time.setProjectShiftId(shiftId);
		if (shiftId == null || shiftId.isEmpty())
			throw new ArahantException("Missing shift");

		time.setDescription(description);
		time.setBeginningTime(beginningTime);
		time.setEndTime(endTime);
		time.setBillable(billable.charAt(0));
		time.setTotalHours(totalHours);
		time.setTotalExpenses((float)expenses);
		time.setFixedPay(pay);
		time.setWorkDate(workDate);
		if (endDate != -1)
			time.setEndDate(endDate);
		time.setPrivateDescription(privateDescription);
        time.setTimeTypeId(timeTypeId);
		time.insert();

		if (nonbillableHours > .009) {
			time = new BTimesheet();
			time.create();
			time.setPersonId(p.getPersonId());
			time.setProjectShiftId(shiftId);
			time.setDescription(description);
			time.setBeginningTime(beginningTime);
			time.setEndTime(endTime);
			time.setBillable('N');
			time.setTotalHours(nonbillableHours);
			time.setTotalExpenses(0);
			time.setFixedPay(0);
			time.setWorkDate(workDate);
			if (endDate != -1)
				time.setEndDate(endDate);
			time.setPrivateDescription(privateDescription);
			time.insert();
		}

		//check to see if this made the user run over
		if (time.hasRunOver())
			return time.getBenefitId();
		else
			return null;
	}

	/**
	 * projectId is not used but, importantly, kept to prevent the possibility of old code calling this method
	 *         with a projectId rather than a shiftId.
	 * 
	 * @param beginningTime
	 * @param billable
	 * @param description
	 * @param endTime
	 * @param projectId
     * @param shiftId
	 * @param totalHours
	 * @param workDate
	 * @param endDate  (-1 means default from workDate)
	 * @param timesheetId
	 * @return
	 * @throws ArahantException 
	 */
    public String saveTimeEntry(final int beginningTime, final String billable, final String description, final int endTime, final String projectId, final String shiftId, final double totalHours, final int workDate, final int endDate, final String timesheetId) throws ArahantException {
        return saveTimeEntry(beginningTime, billable, description, endTime, projectId, shiftId, totalHours, workDate, endDate, timesheetId, "", 0, 0, 0);
    }

	/*
        projectId is not used but, importantly, kept to prevent the possibility of old code calling this method
        with a projectId rather than a shiftId.
    */
    public String saveTimeEntry(final int beginningTime, final String billable, final String description, final int endTime, final String projectId, final String shiftId, final double totalHours, final int workDate, final int endDate, final String timesheetId, float nonbillableHours) throws ArahantException {
        return saveTimeEntry(beginningTime, billable, description, endTime, projectId, shiftId, totalHours, workDate, endDate, timesheetId, "", 0, 0, nonbillableHours);
    }

	/*
	        projectId is not used but, importantly, kept to prevent the possibility of old code calling this method
	        with a projectId rather than a shiftId.
	 */
    public String saveTimeEntry(final int beginningTime, final String billable, final String description, final int endTime, final String projectId, final String shiftId, final double totalHours, final int workDate, final int endDate, final String timesheetId, final String privateDescription, float expenses, double pay, float nonbillableHours) throws ArahantException {
		BTimesheet time = new BTimesheet(timesheetId);
		time.setProjectShiftId(shiftId);
		time.setDescription(description);
		time.setBeginningTime(beginningTime);
		time.setEndTime(endTime);
		time.setBillable(billable.charAt(0));
		time.setTotalHours(totalHours);
		time.setTotalExpenses(expenses);
		time.setFixedPay(pay);
		time.setPrivateDescription(privateDescription);
		if (workDate != 0)
			time.setWorkDate(workDate);
		if (endDate != -1)
			time.setEndDate(endDate);

		time.update();

		if (nonbillableHours > .009) {
			time = new BTimesheet(timesheetId);
			time.setProjectShiftId(shiftId);
			time.setDescription(description);
			time.setBeginningTime(beginningTime);
			time.setEndTime(endTime);
			time.setBillable('N');
			time.setTotalHours(nonbillableHours);
			time.setTotalExpenses(0);
			time.setFixedPay(0);
			time.setPrivateDescription(privateDescription);
			if (workDate != 0)
				time.setWorkDate(workDate);
			if (endDate != -1)
				time.setEndDate(endDate);
			time.update();
		}

//		check to see if this made the user run over
		if (time.hasRunOver())
			return time.getBenefitId();
		else
			return null;
	}

	/**
	 * @param date
	 */
	public void finalizeTime(final int date) {
		final char [] badStates = {TIMESHEET_REJECTED, TIMESHEET_PROBLEM};

		Timesheet badTime = ArahantSession.getHSU().createCriteria(Timesheet.class).eq(Timesheet.PERSON, employee).le(Timesheet.WORKDATE, date).in(Timesheet.STATE, badStates).first();
		if (badTime != null)
			throw new ArahantWarning("Can't finalize time due to prior rejected time on " + DateUtils.getDateFormatted(badTime.getWorkDate()) + ".");

		badTime = ArahantSession.getHSU().createCriteria(Timesheet.class).eq(Timesheet.PERSON, employee).le(Timesheet.WORKDATE, date).eq(Timesheet.TOTALHOURS, (double) 0).first();

		if (badTime != null)
			throw new ArahantWarning("Can't finalize time when a timesheet entry has 0 hours - see date " + DateUtils.getDateFormatted(badTime.getWorkDate()) + ".\nIf you are using the time clock system make sure the employee has clocked out on all days.");

		employee.setTimesheetFinalDate(date);
	}

	public int getRejectedDayCount() {
		return person.getTimeRejects().size();
	}

	public boolean isDayRejected(final long timesheetDate) {
		for (TimeReject tr : person.getTimeRejects()) {
			if (tr.getRejectDate() == timesheetDate)
				return true;
		}
		return false;
	}

	public String getFinalizeDate() {
		return DateUtils.getDateFormatted(employee.getTimesheetFinalDate());
	}

	public boolean showInLists() {
		if (isActive() < 1)
			return true;
		else
			return (DateUtils.getDate(getCurrentStatusHistory().getEffectiveDate()).after(DateUtils.getSixtyDaysAgo()));
	}

    public BTimesheet[] listTimesheetsForReview(final boolean billable, final boolean finalized, final boolean nonbillable, final boolean nonfinalized, final BClientCompany clientCompany,
            final boolean approved, final boolean nonapproved, int begDateRange, int endDateRange) {
        final Employee e = employee;

        List<Timesheet> timeList;

        HibernateCriteriaUtil<Timesheet> crit = ArahantSession.getHSU().createCriteria(Timesheet.class).eq(Timesheet.PERSON, e);

        if (begDateRange > 0)
            crit.ge(Timesheet.WORKDATE, begDateRange);
        if (endDateRange > 0)
            crit.le(Timesheet.WORKDATE, endDateRange);

        if (billable && !nonbillable)
            crit.eq(Timesheet.BILLABLE, 'Y');
        else if (!billable && nonbillable)
            crit.eq(Timesheet.BILLABLE, 'N');
        else if (!billable && !nonbillable)
            crit.eq(Timesheet.BILLABLE, 'Z');  // none
        
        if (finalized && !nonfinalized) {
            int finalDate = e.getTimesheetFinalDate();
            crit.le(Timesheet.WORKDATE, finalDate);
        } else if (!finalized && nonfinalized) {
            int finalDate = e.getTimesheetFinalDate();
            crit.gt(Timesheet.WORKDATE, finalDate);
        } else if (!finalized && !nonfinalized)
            crit.eq(Timesheet.BILLABLE, 'Z');  // none

        if (approved && !nonapproved) {
            HibernateCriterionUtil cu1 = crit.makeCriteria();
            HibernateCriterionUtil cu2 = crit.makeCriteria();
            HibernateCriterionUtil cu3 = crit.makeCriteria();
            HibernateCriterionUtil or = crit.makeCriteria();
            cu1.eq(Timesheet.STATE, 'A');
            cu2.eq(Timesheet.STATE, 'D');
            cu3.eq(Timesheet.STATE, 'I');
            or.or(cu1, cu2, cu3);
            or.add();
        } else if (!approved && nonapproved) {
            HibernateCriterionUtil cu1 = crit.makeCriteria();
            HibernateCriterionUtil cu2 = crit.makeCriteria();
            HibernateCriterionUtil cu3 = crit.makeCriteria();
            HibernateCriterionUtil and = crit.makeCriteria();
            cu1.ne(Timesheet.STATE, 'A');
            cu2.ne(Timesheet.STATE, 'D');
            cu3.ne(Timesheet.STATE, 'I');
            and.and(cu1, cu2, cu3);
            and.add();
        } else if (!approved && !nonapproved)
            crit.eq(Timesheet.BILLABLE, 'Z');  // none
        
        if (clientCompany == null)
            timeList = crit.orderBy(Timesheet.WORKDATE).orderBy(Timesheet.BEGINNINGTIME).setMaxResults(500).list();
        else
            timeList = crit.orderBy(Timesheet.WORKDATE).orderBy(Timesheet.BEGINNINGTIME)
                    .joinTo(Timesheet.PROJECTSHIFT)
                    .joinTo(ProjectShift.PROJECT).in(Project.REQUESTING_ORG_GROUP, new BOrgGroup(clientCompany).getAllOrgGroupsInHierarchy2()).setMaxResults(500).list();

        final List<Timesheet> tList = new LinkedList<Timesheet>();

        final Iterator<Timesheet> timeItr = timeList.iterator();

        boolean checkPassed;

        while (timeItr.hasNext()) {
            final Timesheet ts = timeItr.next();
            /*
            checkPassed = finalized && e.getTimesheetFinalDate() >= ts.getWorkDate();
            if (nonfinalized && e.getTimesheetFinalDate() < ts.getWorkDate())
                checkPassed = true;
            if (!checkPassed)
                continue;
            checkPassed = billable && ts.getBillable() == 'Y';
            if (nonbillable && ts.getBillable() != 'Y')
                checkPassed = true;
            if (!checkPassed)
                continue;

            checkPassed = false;
            char state = ts.getState();
            boolean isApproved = state == 'A' || state == 'D' || state == 'I';
            if (approved && isApproved)
                checkPassed = true;
            if (nonapproved && !isApproved)
                checkPassed = true;
            if (!checkPassed)
                continue;
*/
            tList.add(ts);
        }
        return BTimesheet.makeArray(tList);
    }

	public BTimesheet[] listTimesheetsForReview(final int toDate, final int fromDate, final boolean billable, final boolean finalized, final boolean nonbillable, final boolean nonfinalized) {
		final Employee e = employee;

		final Collection<Character> states = new LinkedList<Character>();
		states.add(ArahantConstants.TIMESHEET_NEW);
		states.add(ArahantConstants.TIMESHEET_REJECTED);
		states.add(ArahantConstants.TIMESHEET_PROBLEM);
		states.add(ArahantConstants.TIMESHEET_FIXED);
		states.add(ArahantConstants.TIMESHEET_CHANGED);

		final List<Timesheet> timeList = ArahantSession.getHSU().createCriteria(Timesheet.class).eq(Timesheet.PERSON, e).in(Timesheet.STATE, states).dateBetween(Timesheet.WORKDATE, fromDate, toDate).orderBy(Timesheet.WORKDATE).orderBy(Timesheet.BEGINNINGTIME).list();

		final List<Timesheet> tList = new LinkedList<Timesheet>();

		final Iterator<Timesheet> timeItr = timeList.iterator();

		boolean finalCheckPassed;

		while (timeItr.hasNext()) {
			final Timesheet ts = timeItr.next();

			finalCheckPassed = finalized && e.getTimesheetFinalDate() >= ts.getWorkDate();
			if (nonfinalized && e.getTimesheetFinalDate() < ts.getWorkDate())
				finalCheckPassed = true;

			if (!finalCheckPassed)
				continue;

			if (billable && ts.getBillable() == 'Y') {
				tList.add(ts);
				continue;
			}

			if (nonbillable && ts.getBillable() != 'Y') {
				tList.add(ts);
			}

		}

		return BTimesheet.makeArray(tList);
	}

	public void removeCompaniesFromEmployee(String[] ids) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.createCriteria(OrgGroupAssociation.class).eq(OrgGroupAssociation.PERSON, employee).joinTo(OrgGroupAssociation.ORGGROUP).joinTo(OrgGroup.OWNINGCOMPANY).in(CompanyBase.ORGGROUPID, ids).delete();

		for (String id : ids)
			if (id.equals(employee.getCompanyBase().getOrgGroupId())) {
				CompanyBase cb = hsu.createCriteria(CompanyBase.class).joinTo(CompanyBase.ORGGROUPS).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, employee).first();

				employee.setCompanyBase(cb);
			}
	}

	public void setWorkersCompCode(String wci) {
		employee.setWorkersCompCode(wci);
	}

	public String getWorkersCompCode() {
		return employee.getWorkersCompCode();
	}
	
	public char getEmploymentType() {
		return employee.getEmploymentType();
	}

	public String getAdpId() {
		return employee.getAdpId();
	}

	public void setAdpId(String id) {
		employee.setAdpId(id);
	}
	
	public void setEmploymentType(char type) {
		employee.setEmploymentType(type);
	}

	public String getOrgGroupRef() {
		Set<OrgGroupAssociation> ogaSet = employee.getOrgGroupAssociations();

		if (ogaSet.isEmpty())
			return "";
		else if (ogaSet.size() > 1) {
			BEmployee be = new BEmployee(employee);
			return ArahantSession.getHSU().createCriteria(OrgGroup.class).eq(OrgGroup.DEFAULT_PROJECT, be.getDefaultProject()).first().getExternalId();
		} else
			return employee.getOrgGroupAssociations().iterator().next().getOrgGroup().getExternalId();
	}

	public void setW4Status(String w4StatusId) {
		if (isEmpty(w4StatusId))
			employee.setW4status('U');
		else
			employee.setW4status(w4StatusId.charAt(0));
	}

	public HrEmplStatusHistory getFirstActiveStatusHistory() {
		return ArahantSession.getHSU().createCriteria(HrEmplStatusHistory.class).eq(HrEmplStatusHistory.EMPLOYEE, employee).orderBy(HrEmplStatusHistory.EFFECTIVEDATE).joinTo(HrEmplStatusHistory.HREMPLOYEESTATUS).eq(HrEmployeeStatus.ACTIVE, 'Y').first();
	}

	public BWizardConfiguration getWizardConfiguration(String wizardType) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		String current_company_id = hsu.getCurrentCompany().getOrgGroupId();
		if (!getCompany().getCompanyId().equals(current_company_id))
			throw new ArahantException("User logged into isn't an employee of the current company.");
 		if (!isEmployee())
			throw new ArahantException("User logged into is not an employee.");
        if (getPayPeriodsPerYear() == 0)
			throw new ArahantException("User logged into does not have pay periods defined.");

		HibernateCriteriaUtil<WizardConfiguration> hcu = hsu.createCriteria(WizardConfiguration.class).eqOrNull(WizardConfiguration.COMPANY_ID, current_company_id).eq(WizardConfiguration.WIZARD_TYPE, wizardType.charAt(0));

		if (getBenefitClass() == null)
			hcu.isNull(WizardConfiguration.BENEFIT_CLASS);
		else
			hcu.eq(WizardConfiguration.BENEFIT_CLASS, getBenefitClass());

		WizardConfiguration wc = hcu.first();
		if (wc == null) {
			/* assume it failed because we didn't have a matching employee class.  
			 * Is there a Wizard for no specified class?
			 */
			hcu = hsu.createCriteria(WizardConfiguration.class).eqOrNull(WizardConfiguration.COMPANY_ID, current_company_id).eq(WizardConfiguration.WIZARD_TYPE, wizardType.charAt(0));
			hcu.isNull(WizardConfiguration.BENEFIT_CLASS);
			wc = hcu.first();
			if (wc == null)
				throw new ArahantException("Wizard not set up for benefit class: " + (getBenefitClass() != null ? getBenefitClass().getName() : "(No Benefit Class)"));
		}
		return new BWizardConfiguration(wc);
	}

	public List<Person> getRealAndChangePerson() {
		PersonCR cr;
		HibernateCriteriaUtil<PersonCR> hcu = ArahantSession.getHSU().createCriteria(PersonCR.class);
		HibernateCriterionUtil crit1 = hcu.makeCriteria();
		HibernateCriterionUtil crit2 = hcu.makeCriteria();
		HibernateCriterionUtil crit3 = hcu.makeCriteria();
		crit3.or(crit1.eq(PersonCR.PERSON, getPerson()), crit2.eq(PersonCR.PERSON_PENDING, getPerson()));
		crit3.add();
		cr = hcu.first();
		if (cr == null)
			//System.out.println("Tried to get Real/Change records for " + getNameFML() + " but returned a null.");
			return new ArrayList<Person>();
		else {
			List<Person> ret = new ArrayList<Person>();
			ret.add(cr.getRealRecord());
			ret.add(cr.getChangeRecord());
			return ret;
		}
	}

	public String getInheritedBenefitClassId() {

		List<OrgGroup> ogl = ArahantSession.getHSU().createCriteria(OrgGroup.class).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, person).list();
		for (OrgGroup og : ogl) {
			BOrgGroup bog = new BOrgGroup(og);
			if (bog.getDefaultBenefitClass() != null)
				return bog.getDefaultBenefitClass().getBenefitClassId();
			else
				return bog.getInheritedBenefitClassId();
		}
		return "";
	}

	public String getInheritedBenefitClassName() {
		List<OrgGroup> ogl = ArahantSession.getHSU().createCriteria(OrgGroup.class).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, person).list();
		for (OrgGroup og : ogl) {
			BOrgGroup bog = new BOrgGroup(og);
			if (bog.getDefaultBenefitClass() != null)
				return bog.getDefaultBenefitClass().getName();
			else
				return bog.getInheritedBenefitClassName();
		}
		return "";
	}

	public BenefitClass getInheritedBenefitClass() {
		List<OrgGroup> ogl = ArahantSession.getHSU().createCriteria(OrgGroup.class).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, person).list();
		for (OrgGroup og : ogl) {
			BOrgGroup bog = new BOrgGroup(og);
			if (bog.getDefaultBenefitClass() != null)
				return bog.getDefaultBenefitClass();
			else
				return bog.getInheritedBenefitClass();
		}
		return null;
	}

	public static class BEmployeeComparator implements Comparator<BPerson>, Serializable {

		private static final long serialVersionUID = 7379078855864831097L;

		/*
		 * (non-Javadoc) @see java.util.Comparator#compare(java.lang.Object,
		 * java.lang.Object)
		 */
		@Override
		public int compare(final BPerson arg0, final BPerson arg1) {
			if (arg0.getLastName() == null)
				arg0.setLastName("");
			if (arg1.getLastName() == null)
				arg1.setLastName("");
			if (arg0.getFirstName() == null)
				arg0.setFirstName("");
			if (arg1.getFirstName() == null)
				arg1.setFirstName("");

			int ret = arg0.getLastName().compareTo(arg1.getLastName());

			if (ret == 0)
				ret = arg0.getFirstName().compareTo(arg1.getFirstName());

			return ret;
		}
	}

	public void unassignBenefitConfigs(final String[] benefitConfigs) throws ArahantDeleteException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		final List<HrBenefitJoin> joins = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, employee).eq(HrBenefitJoin.PAYING_PERSON, employee).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).in(HrBenefitConfig.BENEFIT_CONFIG_ID, benefitConfigs).list();

		for (final HrBenefitJoin join : joins) {
			hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, employee).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, join.getHrBenefitConfig()).delete();

			hsu.delete(join.getBeneficiaries());
		}
		hsu.delete(joins);
	}

	public BHRBenefitConfig[] listNotBenefitCategories() {

//		 get all assigned benefit categories
		final List assignedList = ArahantSession.getHSU().createCriteria(HrBenefit.class).selectFields(HrBenefit.BENEFITID).joinTo(HrBenefit.HR_BENEFIT_CONFIGS).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).eq(HrBenefitJoin.COVERED_PERSON, employee).list();

		// get benefit categories that are not assigned

		return BHRBenefitConfig.makeArray(ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).joinTo(HrBenefitConfig.HR_BENEFIT).orderBy(HrBenefit.NAME).notIn(HrBenefit.BENEFITID, assignedList).list());
	}

	public String getBenefitsReport() throws Exception {
		return new HRBenefitsReport().build(ArahantSession.getHSU(), employee);
	}

	public String getAccruedTimeOffReport(final String benefitId, final int startDate, final int endDate) throws Exception {
		final HRAccruedTimeOffReport rep = new HRAccruedTimeOffReport();
		rep.build(ArahantSession.getHSU(),
				this.listTimeOff(benefitId, startDate, endDate, 1000), this.getPersonId(), benefitId, startDate, endDate);

		return rep.getFilename();
	}

	public BHRTrainingDetail[] listTrainingDetails() {
		return BHRTrainingDetail.makeArray(ArahantSession.getHSU().createCriteria(HrTrainingDetail.class).eq(HrTrainingDetail.EMPLOYEE, employee).list());
	}

	public void setEEOCategoryId(final String eeoCategoryId) {
		employee.setHrEeoCategory(ArahantSession.getHSU().get(HrEeoCategory.class, eeoCategoryId));
	}

	public void setEEORaceId(final String eeoRaceId) {
		employee.setHrEeoRace(ArahantSession.getHSU().get(HrEeoRace.class, eeoRaceId));
	}

	public String getEEOCategoryId() {
		if (employee.getHrEeoCategory() != null)
			return employee.getHrEeoCategory().getEeoCategoryId();
		return "";
	}

	public String getEEORaceId() {
		if (employee.getHrEeoRace() != null)
			return employee.getHrEeoRace().getEeoId();
		return "";
	}

	public String getEmployeeStatusName() {
		try {
			return getLastStatusName();
			/*
			 * final HrEmplStatusHistory
			 * hist=hsu.createCriteria(HrEmplStatusHistory.class)
			 * .eq(HrEmplStatusHistory.EMPLOYEE, employee)
			 * .orderByDesc(HrEmplStatusHistory.EFFECTIVEDATE) .first(); if
			 * (hist!=null)
			 *
			 * return hist.getHrEmployeeStatus().getName();
			 */
		} catch (final Exception e) {
		}
		return "";
	}

	public String getEmployeeStatusId() {
		try {
			return getLastStatusHistory().getHrEmployeeStatus().getStatusId();
		} catch (final Exception e) {
		}
		return "";
	}

	public char getW4Status() {
		return employee.getW4status();
	}

	public void setW4Status(char status) {
		employee.setW4status(status);
	}

	public int getLastPositionDate() {
		try {
			List<HrWage> wl = ArahantSession.getHSU().createCriteria(HrWage.class).eq(HrWage.EMPLOYEE, employee).orderByDesc(HrWage.EFFECTIVEDATE).list();

			if (!wl.isEmpty()) //look for earliest date on that position
			{
				HrWage w = wl.get(0);

				for (HrWage hw : wl)
					if (!hw.getHrPosition().getPositionId().equals(w.getHrPosition().getPositionId()))
						break;
				return w.getEffectiveDate();
			}
		} catch (final Exception e) {
		}
		return 0;
	}

	public String getLastPositionId() {
		HrWage wl = ArahantSession.getHSU().createCriteria(HrWage.class).eq(HrWage.EMPLOYEE, employee).orderByDesc(HrWage.EFFECTIVEDATE).first();

		if (wl == null)
			return "";

		if (wl.getHrPosition() == null)
			return "";

		return wl.getHrPosition().getPositionId();
	}

	public String getPositionName() {
		try {
			final HrWage w = ArahantSession.getHSU().createCriteria(HrWage.class).eq(HrWage.EMPLOYEE, employee).orderByDesc(HrWage.EFFECTIVEDATE).first();
			return w.getHrPosition().getName();
		} catch (final Exception e) {
		}
		return "";
	}

	public String getPositionNameOnOrBefore(int date) {
		try {
			final HrWage w = ArahantSession.getHSU().createCriteria(HrWage.class).eq(HrWage.EMPLOYEE, employee).le(HrWage.EFFECTIVEDATE, date).orderByDesc(HrWage.EFFECTIVEDATE).first();
			return w.getHrPosition().getName();
		} catch (final Exception e) {
		}
		return "";
	}

	public String getWageTypeName() {
		final HrWage w = ArahantSession.getHSU().createCriteria(HrWage.class).eq(HrWage.EMPLOYEE, employee).orderByDesc(HrWage.EFFECTIVEDATE).first();
		if (w != null)
			return w.getWageType().getWageName();

		return "Unknown";
	}

	public double getTimeOffCurrentPeriod(final String benefitId) throws ArahantJessException {
		final BHRAccruedTimeOff[] toff = listTimeOff(benefitId, getLastAnniversaryDate(), 0, 10000);

		if (toff == null || toff.length == 0)
			return 0;

		return toff[toff.length - 1].getRunningTotal();
	}

	public double getTimeOffByDates(final String benefitId, int startDate, int endDate) throws ArahantJessException {
		final BHRAccruedTimeOff[] toff = listTimeOff(benefitId, startDate, endDate, 10000);

		if (toff == null || toff.length == 0)
			return 0;

		return toff[toff.length - 1].getRunningTotal();
	}

	public BHRAccruedTimeOff[] listTimeOff(final String benefitId, final int startDate, int endDate, final int max) throws ArahantJessException {
		return listTimeOff(benefitId, startDate, endDate, max, true);
	}

	public BHRAccruedTimeOff[] listTimeOff(final String benefitId, int startDate, int endDate, final int max, boolean proRate) throws ArahantJessException {

		//now I need to assert something to activate the rule associated with hraccrual
		try {

			//	if (startDate==DateUtils.now())
			//		startDate=DateUtils.add(startDate, -1);

			//	ArahantSession.getAI().watchAll();
			HibernateSessionUtil hsu = ArahantSession.getHSU();
			final HrBenefit bene = hsu.get(HrBenefit.class, benefitId);

			//	final JessBean jb=new JessBean();
			//	jb.executeAICommand("(batch BusinessRules.jess)");


			boolean showClosingBalance = true;

			if (!hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, employee).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bene).exists()) {
				//employee doesn't have this benefit
				final BHRAccruedTimeOff[] ret = new BHRAccruedTimeOff[1];
				final HrAccrual hra = new HrAccrual();
				hra.setAccrualDate(startDate);
				hra.setAccrualHours(0);
				hra.setDescription("Employee does not have this benefit.");
				ret[0] = new BHRAccruedTimeOff(hra);
				return ret;
			}

			//See if I have DB Rules to use instead of AI Rules
			TimeOffAccrualCalc calc = hsu.createCriteria(TimeOffAccrualCalc.class).dateInside(TimeOffAccrualCalc.FIRST_ACTIVE, TimeOffAccrualCalc.LAST_ACTIVE, DateUtils.now()).joinTo(TimeOffAccrualCalc.BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bene).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).eq(HrBenefitJoin.COVERED_PERSON, employee).first();

			if (calc != null)
				return new BTimeOffAccrualCalc(calc).listTimeOff(this, startDate, endDate, max, proRate);

			if (endDate == 0) {	//If no ending date, calculate from start date to end of period start date is in.
				final Calendar eDate = DateUtils.getCalendar(startDate);
				final Calendar now = DateUtils.getNow();

				logger.info(DateUtils.getDateFormatted(eDate.getTime()));
				eDate.set(Calendar.YEAR, now.get(Calendar.YEAR));


				if (!eDate.after(now))
					eDate.add(Calendar.YEAR, 1);

				eDate.add(Calendar.DAY_OF_YEAR, -1);

				showClosingBalance = false;

				logger.info(DateUtils.getDateFormatted(eDate.getTime()));
				endDate = DateUtils.getDate(eDate);
				proRate = false;
			}

			if (!proRate)
				bene.executeAICommand("(assert (accDesc Allotted))");
			else {
				bene.executeAICommand("(assert (accDesc Accrued))");
				bene.executeAICommand("(assert (canProRate))");
			}

			//Kalvin 4/11/2010: when getting time off list, employee could have been terminated and rehired
			//so, we'll check it somebody has been rehired first, if not, then get the getEmploymentDate
			Integer effectiveDate = getCurrentHiredDate();

			//	System.out.println(effectiveDate+" "+endDate+" "+bene.getRuleName());
			bene.executeAICommand("(assert (reportEndDate " + (endDate + 1) + "))");
			bene.executeAICommand("(assert (benefitRule (name \"" + bene.getRuleName() + "\")"
					+ " (startDateDays " + effectiveDate + ")"
					+ " (personId \"" + getPersonId() + "\")"
					+ " (startDateDays " + effectiveDate + ")"
					+ "  (resetTotal true)"
					+ " ))");

			//ArahantSession.getAI().watchAll();
			//check for 6 months
			Calendar sixmonths = DateUtils.getCalendar(effectiveDate);
			sixmonths.add(Calendar.MONTH, 6);
			bene.executeAICommand("(assert (sixMonthDate " + DateUtils.getDate(sixmonths) + " .5 ))");


			Calendar ninetyDays = DateUtils.getCalendar(effectiveDate);
			ninetyDays.add(Calendar.DAY_OF_YEAR, 90);

			bene.executeAICommand("(assert (anniversaryDate " + DateUtils.getDate(ninetyDays) + " .25 ))");


			//assert the anniversary dates
			Calendar c = DateUtils.getCalendar(effectiveDate);
			Calendar n = DateUtils.getCalendar(endDate);
			int ly = 0;
			while (c.before(n)) {
				bene.executeAICommand("(assert (anniversaryDate " + DateUtils.getDate(c) + " " + ly + "))");
				c.add(Calendar.YEAR, 1);
				ly++;
			}


			//need to assert all active two week periods
			Calendar ppc = DateUtils.getCalendar(effectiveDate);
			while (ppc.before(n)) {
				bene.executeAICommand("(assert (payPeriod " + DateUtils.getDate(ppc) + "))");
				ppc.add(Calendar.WEEK_OF_YEAR, 2);
			}


			//need to assert all calendar years
			Calendar calenYear = DateUtils.getCalendar(effectiveDate);
			calenYear.set(Calendar.DAY_OF_YEAR, 1);
			while (calenYear.before(n)) {
				bene.executeAICommand("(assert (holidayYear \"" + getPersonId() + "\" " + DateUtils.getDate(calenYear) + "))");
				calenYear.add(Calendar.YEAR, 1);
			}

			//bene.executeAICommand("(assert (employeeStartDateDays "+DateUtils.getDays(effectiveDate)+"))");


			final List<HrAccrual> l = hsu.createCriteria(HrAccrual.class).eq(HrAccrual.EMPLOYEE, employee).orderByDesc(HrAccrual.ACCRUALDATE).eq(HrAccrual.HRBENEFIT, bene).list();

			for (HrAccrual ha : l) {
				final String d = ha.getDescription().replaceAll("\"", "\\\\\"");
				bene.executeAICommand("(assert (benefitRule (name \"" + bene.getRuleName() + "\")"
						+ " (amount " + ha.getAccrualHours() + ")"
						+ " (longevity 0)"
						+ " (accrualId \"" + ha.getAccrualId() + "\")"
						+ " (didProRate true)"
						+ " (personId \"" + getPersonId() + "\")"
						+ " (description \"" + d + "\")"
						+ " (startDateDays " + ha.getAccrualDate() + ")"
						+ " (resetTotal false)))");
			}

			final List<Timesheet> lt = hsu.createCriteria(Timesheet.class).lt(Timesheet.BEGINNING_ENTRY_DATE, new Date()).eq(Timesheet.PERSON, employee)
					.joinTo(Timesheet.PROJECTSHIFT)
					.joinTo(ProjectShift.PROJECT)
					.joinTo(Project.HRBENEFITPROJECTJOINS)
					.in(HrBenefitProjectJoin.HR_BENEFIT_CONFIG, hsu.createCriteria(HrBenefitConfig.class)
							.eq(HrBenefitConfig.HR_BENEFIT, bene).list()).list();

			for (Timesheet t : lt) {
				final String d = t.getDescription().replaceAll("\"", "\\\\\"");
				bene.executeAICommand("(assert (benefitRule (name \"" + bene.getRuleName() + "\")"
						+ " (amount " + -t.getTotalHours() + ")"
						+ " (longevity 0)"
						+ " (accrualId \"" + t.getTimesheetId() + "\")"
						+ " (didProRate true)"
						+ " (personId \"" + getPersonId() + "\")"
						+ " (description \"" + d + "\")"
						+ " (startDateDays " + t.getEndDate() + ")"
						+ " (resetTotal false)))");
			}

			List<BHRAccruedTimeOff> l3 = new LinkedList<BHRAccruedTimeOff>();

			bene.runAIEngine();

			//ArahantSession.AIEval("(facts)");

			//	now get out the results
			final HashMap<String, String> keys = new HashMap<String, String>();
			keys.put("name", bene.getRuleName());
			final List facts = bene.queryForFacts("benefitRule");

			for (Object fact : facts) {
				final Fact f = (Fact) fact;

				String desc = bene.getFactStringValue(f, "description");

				if (isEmpty(desc) || "nil".equals(desc))
					desc = "Time Used";

				try {
					if (!bene.getRuleName().equals(bene.getFactStringValue(f, "name")))
						continue;
				} catch (Exception e) {
					//continue
				}

				HrAccrual hra = new HrAccrual();
				//	final Date d=new java.sql.Date((long)(bene.getFactLongValue(f, "startDateDays")*24*60*60*1000+.5));
				//	final Date d=DateUtils.getDate((int)bene.getFactLongValue(f, "startDateDays"));
				hra.setAccrualDate((int) (bene.getFactLongValue(f, "startDateDays")));
				hra.setAccrualHours(bene.getFactFloatValue(f, "amount"));
				hra.setDescription(desc);
				if (hra.getDescription().equals("nil"))
					hra.setDescription("");
				hra.setHrBenefit(bene);

				BHRAccruedTimeOff b = new BHRAccruedTimeOff(hra);
				boolean reset = bene.getFactStringValue(f, "resetTotal").equals("true");
				b.setResetTotal(false);
				b.setCapValue(bene.getFactFloatValue(f, "capValue"));
				l3.add(b);
				//DO NOT DO A SAVE ON THIS, IT'S A FAKE RECORD FOR THE REPORT
				if (reset) {
					hra = new HrAccrual();
					//	final Date d=new java.sql.Date((long)(bene.getFactLongValue(f, "startDateDays")*24*60*60*1000+.5));
					//	final Date d=DateUtils.getDate((int)bene.getFactLongValue(f, "startDateDays"));
					hra.setAccrualDate(DateUtils.addDays((int) (bene.getFactLongValue(f, "startDateDays")), -1));
					//hra.setAccrualHours(bene.getFactFloatValue(f, "amount"));
					hra.setDescription("End of period");

					hra.setHrBenefit(bene);

					b = new BHRAccruedTimeOff(hra);
					b.setResetTotal(true);
					b.setCapValue(bene.getFactFloatValue(f, "capValue"));
					l3.add(b);
				}

			}

			//Set opening balance
			HrAccrual hra = new HrAccrual();
			hra.setAccrualDate(startDate);
			hra.setAccrualHours(0);
			hra.setDescription(OPENING_BALANCE);
			l3.add(new BHRAccruedTimeOff(hra));

			//set closing balance
			if (showClosingBalance) {
				hra = new HrAccrual();
				hra.setAccrualDate(endDate);
				hra.setAccrualHours(0);
				hra.setDescription(CLOSING_BALANCE);
				l3.add(new BHRAccruedTimeOff(hra));
			}

			java.util.Collections.sort(l3, new AccrualComparator());

			final List<BHRAccruedTimeOff> l2 = new ArrayList<BHRAccruedTimeOff>(l.size());

			double prevTime = 0;

			for (BHRAccruedTimeOff b : l3) {
				b.setPreviousTotal(prevTime);
				prevTime = b.getRunningTotal();

				if (startDate <= b.getAccrualDate() && b.getAccrualDate() <= endDate)
					l2.add(b);
			}

			//need to spin through to start date now
			l3 = l2;

			final BHRAccruedTimeOff[] ret = new BHRAccruedTimeOff[l3.size()];
			for (int loop = 0; loop < ret.length; loop++)
				ret[loop] = l3.get(loop);

			//ArahantSession.AIEval("(facts)");

			return ret;

		} catch (final JessException e) {
			logger.error(e);
			throw new ArahantJessException(e);
		}
	}

	public static class AccrualComparator implements Comparator<BHRAccruedTimeOff> {

		@Override
		public int compare(final BHRAccruedTimeOff arg0, final BHRAccruedTimeOff arg1) {

			int val = arg0.getAccrualDate() - arg1.getAccrualDate();

			if (val == 0) {
				if (arg0.getResetTotal())
					return -1;
				if (arg1.getResetTotal())
					return 1;
				if (arg0.getDescription().equals(OPENING_BALANCE))
					val = -1;
				if (arg1.getDescription().equals(OPENING_BALANCE))
					val = 1;
				if (arg0.getDescription().equals(CLOSING_BALANCE))
					val = 1;
				if (arg1.getDescription().equals(CLOSING_BALANCE))
					val = -1;
			}
			return val;
		}
	}

	public String getW4StatusName() {
		switch (employee.getW4status()) {
			case 'S':
				return "Single";
			case 'M':
				return "Married";
			case 'H':
				return "Married - Single Rate";
			default:
				return "Unknown";
		}
	}

	public int getLastAnniversaryDate() {
		final Calendar dt = DateUtils.getCalendar(getEmploymentDate());

		final Calendar now = DateUtils.getNow();

		dt.set(Calendar.YEAR, now.get(Calendar.YEAR));

		if (dt.after(now))
			dt.add(Calendar.YEAR, -1);

		return DateUtils.getDate(dt);
	}

	/**
	 * @param benefitPackageId
	 * @throws ArahantException
	 */
	public void assignBenefitPackageId(final String benefitPackageId) throws ArahantException {
		//TODO: this doesn't work right now
		/*
		 * //first wipe out all their old benefits
		 * hsu.delete(employee.getHrEmployeeBenefitJoins());
		 *
		 * //now add in the ones from the benefit package final HrBenefitPackage
		 * pack=hsu.get(HrBenefitPackage.class, benefitPackageId);
		 *
		 * if (pack!=null && pack.getHrBenefitPackageJoins()!=null) { final
		 * Iterator itr=pack.getHrBenefitPackageJoins().iterator(); while
		 * (itr.hasNext()) { final HrBenefitPackageJoin
		 * j=(HrBenefitPackageJoin)itr.next(); final HrEmployeeBenefitJoin
		 * bj=new HrEmployeeBenefitJoin(); bj.setEmployee(employee);
		 * bj.setHrBenefitConfig(j.getHrBenefit());
		 * bj.setEmployeeBenefitId(j.generateId()); hsu.insert(bj); } }
		 */
	}

	public int getLastRaiseDate() {
		final HrWage w = ArahantSession.getHSU().createCriteria(HrWage.class).eq(HrWage.EMPLOYEE, employee).orderByDesc(HrWage.EFFECTIVEDATE).joinTo(HrWage.WAGETYPE).ne(WageType.PERIOD_TYPE, WageType.PERIOD_ONE_TIME).first();

		if (w == null)
			return 0;

		return w.getEffectiveDate();
	}

	public short getCurrentSalaryType() {
		final HrWage w = ArahantSession.getHSU().createCriteria(HrWage.class).eq(HrWage.EMPLOYEE, employee).orderByDesc(HrWage.EFFECTIVEDATE).joinTo(HrWage.WAGETYPE).ne(WageType.PERIOD_TYPE, WageType.PERIOD_ONE_TIME).first();

		if (w == null)
			return 0;

		return w.getWageType().getPeriodType();
	}

	public double getCurrentSalary() {
		final HrWage w = ArahantSession.getHSU().createCriteria(HrWage.class).eq(HrWage.EMPLOYEE, employee).orderByDesc(HrWage.EFFECTIVEDATE).joinTo(HrWage.WAGETYPE).ne(WageType.PERIOD_TYPE, WageType.PERIOD_ONE_TIME).first();

		if (w == null)
			return 0;

		return w.getWageAmount();
	}

	public double getSalaryAsOf(int asOfDate) {
		final HrWage w = ArahantSession.getHSU().createCriteria(HrWage.class).eq(HrWage.EMPLOYEE, employee).orderByDesc(HrWage.EFFECTIVEDATE).le(HrWage.EFFECTIVEDATE, asOfDate).joinTo(HrWage.WAGETYPE).ne(WageType.PERIOD_TYPE, WageType.PERIOD_ONE_TIME).first();

		if (w == null)
			return 0;
		if (w.getWageType().getPeriodType() == WageType.PERIOD_HOURLY)
			return w.getWageAmount() * 2080;

		return w.getWageAmount();
	}

	public short getSalaryTypeAsOf(int asOfDate) {
		final HrWage w = ArahantSession.getHSU().createCriteria(HrWage.class).eq(HrWage.EMPLOYEE, employee).orderByDesc(HrWage.EFFECTIVEDATE).le(HrWage.EFFECTIVEDATE, asOfDate).joinTo(HrWage.WAGETYPE).ne(WageType.PERIOD_TYPE, WageType.PERIOD_ONE_TIME).first();

		if (w == null)
			return 0;

		return w.getWageType().getPeriodType();
	}

	public HrWage getCurrentWageBean() {
		return ArahantSession.getHSU().createCriteria(HrWage.class).eq(HrWage.EMPLOYEE, employee).orderByDesc(HrWage.EFFECTIVEDATE).joinTo(HrWage.WAGETYPE).ne(WageType.PERIOD_TYPE, WageType.PERIOD_ONE_TIME).first();
	}

	public String getCurrentWageNotes() {
		final HrWage w = ArahantSession.getHSU().createCriteria(HrWage.class).eq(HrWage.EMPLOYEE, employee).orderByDesc(HrWage.EFFECTIVEDATE).joinTo(HrWage.WAGETYPE).ne(WageType.PERIOD_TYPE, WageType.PERIOD_ONE_TIME).first();

		if (w == null)
			return "";

		return w.getNotes();
	}

	public double getLastRaiseAmount() {
		final List<HrWage> wList = ArahantSession.getHSU().createCriteria(HrWage.class).eq(HrWage.EMPLOYEE, employee).orderByDesc(HrWage.EFFECTIVEDATE).joinTo(HrWage.WAGETYPE).ne(WageType.PERIOD_TYPE, WageType.PERIOD_ONE_TIME).setMaxResults(2).list();

		if (wList.size() < 2)
			return 0;

		final HrWage w1 = wList.get(0);
		final HrWage w2 = wList.get(1);

		return w1.getWageAmount() - w2.getWageAmount();
	}

	public int getLastEvaluationDate() {
		final HrEmployeeEval ev = ArahantSession.getHSU().createCriteria(HrEmployeeEval.class).eq(HrEmployeeEval.EMPLOYEEBYEMPLOYEEID, employee).orderByDesc(HrEmployeeEval.EVALDATE).first();
		if (ev == null)
			return 0;
		return ev.getEvalDate();
	}

	public HrEmployeeEval getLastEvaluation() {
		return ArahantSession.getHSU().createCriteria(HrEmployeeEval.class).eq(HrEmployeeEval.EMPLOYEEBYEMPLOYEEID, employee).orderByDesc(HrEmployeeEval.EVALDATE).first();
	}

	public int getNextEvaluationDate() {
		final HrEmployeeEval ev = ArahantSession.getHSU().createCriteria(HrEmployeeEval.class).eq(HrEmployeeEval.EMPLOYEEBYEMPLOYEEID, employee).orderByDesc(HrEmployeeEval.EVALDATE).first();

		if (ev == null)
			return 0;

		return ev.getNextEvalDate();
	}

	public long getTimeWithCompany() //returns days
	{
		final Date d = DateUtils.getDate(getEmploymentDate());
		final Date now = new Date();

		return (now.getTime() - d.getTime()) / 1000 / 60 / 60 / 24;
	}

	public long getTimeSinceLastRaise() //returns days
	{
		final Date d = DateUtils.getDate(getLastRaiseDate());
		final Date now = new Date();

		return (now.getTime() - d.getTime()) / 1000 / 60 / 60 / 24;
	}

	public long getTimeSinceLastReview() //returns days
	{
		final Date d = DateUtils.getDate(getLastEvaluationDate());
		final Date now = new Date();

		return (now.getTime() - d.getTime()) / 1000 / 60 / 60 / 24;
	}

	public int getWageType() {
		return getCurrentSalaryType();
	}

	public double getHourlyRate() {
		if (getWageType() == 1)
			return getWageAmount();

		return getWageAmount() / 2080;
	}

	public double getAnnualRate() {
		if (getWageType() == 2)
			return getWageAmount();

		return getWageAmount() * 2080;
	}

	@Override
	public String getPositionId() {
		try {
			final HrWage w = ArahantSession.getHSU().createCriteria(HrWage.class).eq(HrWage.EMPLOYEE, employee).orderByDesc(HrWage.EFFECTIVEDATE).first();
			return w.getHrPosition().getPositionId();
		} catch (final Exception e) {
		}
		return "";
	}

	public String[] getTimeOffTypes() {
		final List benes = ArahantSession.getHSU().createCriteria(HrBenefit.class).selectFields(HrBenefit.NAME).orderBy(HrBenefit.NAME).eq(HrBenefit.TIMERELATED, 'Y').joinTo(HrBenefit.HR_BENEFIT_CONFIGS).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).eq(HrBenefitJoin.COVERED_PERSON, employee).list();

		final String [] ret = new String[benes.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = benes.get(loop).toString();

		return ret;
	}

	public String[] getPaidTimeOffTypes() {
//		final List benes = ArahantSession.getHSU().createCriteria(HrBenefit.class).selectFields(HrBenefit.NAME).orderBy(HrBenefit.NAME).eq(HrBenefit.TIMERELATED, 'Y').eq(HrBenefit.PAIDBENEFIT, 'Y').joinTo(HrBenefit.HR_BENEFIT_CONFIGS).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).eq(HrBenefitJoin.COVERED_PERSON, employee).list();
		final List benes = ArahantSession.getHSU().createCriteria(HrBenefit.class).selectFields(HrBenefit.NAME).orderBy(HrBenefit.NAME).eq(HrBenefit.TIMERELATED, 'Y').joinTo(HrBenefit.HR_BENEFIT_CONFIGS).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).eq(HrBenefitJoin.COVERED_PERSON, employee).list();

		final String [] ret = new String[benes.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = benes.get(loop).toString();

		return ret;
	}

	public double getHoursLeftOnBenefit(final String beneName) throws ArahantJessException {
		//First, what's the bene ID
		final HrBenefit bene = ArahantSession.getHSU().createCriteria(HrBenefit.class).eq(HrBenefit.NAME, beneName).joinTo(HrBenefit.HR_BENEFIT_CONFIGS).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).eq(HrBenefitJoin.COVERED_PERSON, employee).first();

		if (bene == null) {
			logger.debug("Employee does not have benefit");
			return 0;
		}

		logger.debug("Getting time off for " + bene.getName() + " date is " + getLastAnniversaryDate());
		
		final BHRAccruedTimeOff [] ba = listTimeOff(bene.getBenefitId(), getLastAnniversaryDate(), 0, 10000, false);

		logger.debug(bene.getName() + " Total entries " + ba.length);

		if (ba.length == 0)
			return 0.0;
		
		// find the last applicable date
		int index = ba.length - 1;
		int now = DateUtils.now();
		while (index >= 0  &&  ba[index].getAccrualDate() > now)
			index--;
		if (index < 0)
			return 0.0;
		return ba[index].getRunningTotal();
	}

	public double getHoursLeftOnBenefit(final String beneName, int startDate, int endDate) throws ArahantJessException {
		//First, what's the bene ID
		final HrBenefit bene = ArahantSession.getHSU().createCriteria(HrBenefit.class).eq(HrBenefit.NAME, beneName).joinTo(HrBenefit.HR_BENEFIT_CONFIGS).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).eq(HrBenefitJoin.COVERED_PERSON, employee).first();

		if (bene == null) {
			logger.debug("Employee does not have benefit");
			return 0;
		}

		logger.debug("Getting time off for " + bene.getName() + " date is " + getLastAnniversaryDate());

		final BHRAccruedTimeOff [] ba = listTimeOff(bene.getBenefitId(), startDate, endDate, 10000, false);

		logger.debug(bene.getName() + " Total entries " + ba.length);

		if (ba.length == 0)
			return 0.0;
		
		// find the last applicable date
		int index = ba.length - 1;
		int now = DateUtils.now();
		while (index >= 0  &&  ba[index].getAccrualDate() > now)
			index--;
		if (index < 0)
			return 0.0;
		return ba[index].getRunningTotal();
	}

	public boolean getExempt() {
		return employee.getOvertimePay() == 'N';
	}

	public void setExempt(final boolean b) {
		employee.setOvertimePay(b ? 'N' : 'Y');
	}
	/*
	 * employee name - true/false - get fname lname
	 *
	 * start date - true/false - getEmploymentDate
	 *
	 * last raise date - true/false - getLastRaiseDate
	 *
	 * current salary - true/false
	 *
	 * last raise amount - true/false
	 *
	 * last evaluation date - true/false
	 *
	 * time with company - true/false
	 *
	 * time since last raise - true/false
	 *
	 * time since last review - true/false
	 *
	 */

	public String getEEOCategory() {
		if (employee.getHrEeoCategory() != null)
			return employee.getHrEeoCategory().getName();
		return "";
	}

	public String getEEORace() {
		if (employee.getHrEeoRace() != null)
			return employee.getHrEeoRace().getName();
		return "";
	}

	/**
	 * @return
	 *
	 * public String getEmployeeTypeString() { switch
	 * (employee.getEmployeeType()) { case MANAGER_TYPE : return "Manager"; case
	 * OWNER_TYPE : return "Owner"; case ACCOUNTANT_TYPE : return "Accountant";
	 * case EMPLOYEE_TYPE : return "Employee"; } return ""; }
	 */
	protected boolean isSupervisor() {
		for (OrgGroupAssociation oga : employee.getOrgGroupAssociations()) {
			if (oga.getPrimaryIndicator() == 'Y')
				return true;
		}
		return false;
	}

	public boolean getHasTimeReadyForApproval() {
		return ArahantSession.getHSU().createCriteria(Timesheet.class).in(Timesheet.STATE, new char[]{TIMESHEET_FIXED, TIMESHEET_CHANGED, TIMESHEET_NEW, TIMESHEET_SUBMITTED}).eq(Timesheet.PERSON, employee).le(Timesheet.WORKDATE, getTimesheetFinalDate()).exists();
	}

	public void setExtRef(final String extRef) {
		employee.setExtRef(extRef);
	}

	public String getExtRef() {
		return employee.getExtRef();
	}

	/**
	 * This method returns all dependents including Real or Change requests as well as inactive dependents.
	 * It can also return the same dependent multiple times if there are change and real records.
	 * 
	 * @return @throws ArahantException
	 */
	public BHREmplDependent[] listDependents() throws ArahantException {
		final List<HrEmplDependent> deplst = ArahantSession.getHSU().createCriteria(HrEmplDependent.class).eq(HrEmplDependent.EMPLOYEE, employee).list();

		final List<BHREmplDependent> l2 = new ArrayList<BHREmplDependent>(deplst.size());

		for (HrEmplDependent dep : deplst)
			l2.add(new BHREmplDependent(dep));

		return BHREmplDependent.sort(l2);
	}

	/**
	 * @return @throws ArahantException
	 */
	public String getReport() throws ArahantException {
		return new HRDependentReport().getReport(this);
	}

	public String getActive() {
		switch (isActive()) {
			case -1:
				return "No Status";
			case 0:
				return "Active";
			case 1:
				return "Inactive";
			default:
				return "Unknown";
		}
	}

	public static BEmployee[] makeArray(final HibernateScrollUtil<Employee> scr) throws ArahantException {
		List<Employee> l = new ArrayList<Employee>();
		while (scr.next())
			l.add(scr.get());
		return BEmployee.makeArray(l);
	}

	public static BEmployee[] makeArray(final List<Employee> l) throws ArahantException {
		final BEmployee[] ret = new BEmployee[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BEmployee(l.get(loop));
		return ret;
	}

	/**
	 * @param currentPerson
	 * @param cap
	 * @return
	 * @throws ArahantException
	 */
	public static BEmployee[] listEmployeesInPersonsGroup(final Person currentPerson, final int cap) throws ArahantException {
		List<OrgGroup> orgs = ArahantSession.getHSU().createCriteriaNoCompanyFilter(OrgGroup.class).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, currentPerson).list();

		BEmployee[] ret = makeArray(ArahantSession.getHSU().createCriteriaNoCompanyFilter(Employee.class).setMaxResults(cap).activeEmployee().orderBy(Person.LNAME).orderBy(Person.FNAME).joinTo(Person.ORGGROUPASSOCIATIONS).in(OrgGroupAssociation.ORGGROUP, orgs) //.joinTo(OrgGroup.ORGGROUPASSOCIATIONS,"orggroup2")
				//.joinTo("orggroup2."+OrgGroupAssociation.PERSON)
				//.eq(Person.PERSONID, currentPerson.getPersonId())
				.list());
		/*
		 * ArrayList<BEmployee> beml=new ArrayList<BEmployee>();
		 *
		 * for (BEmployee b : ret) { if (b.isActiveOn(DateUtils.now()))
		 * beml.add(b); }
		 *
		 * return beml.toArray(new BEmployee[beml.size()]);
		 *
		 */
		return ret;
	}

	public String getEnrolled() {
		return enrolled ? "Yes" : "No";
	}

	public void setEnrolled(final boolean b) {
		enrolled = b;
	}

	public String getTerminationDate() {
		try {
			return DateUtils.getDateFormatted(getTermDate());
		} catch (final Exception e) {
			return "";
		}
	}

	public int getTermDate() {
		try {
			HrEmplStatusHistory h = ArahantSession.getHSU().createCriteria(HrEmplStatusHistory.class).eq(HrEmplStatusHistory.EMPLOYEE, employee).orderByDesc(HrEmplStatusHistory.EFFECTIVEDATE).joinTo(HrEmplStatusHistory.HREMPLOYEESTATUS).eq(HrEmployeeStatus.ACTIVE, 'N').first();

			if (h == null)
				return 0;

			//make sure there is not an active after this - rehire
			if (ArahantSession.getHSU().createCriteria(HrEmplStatusHistory.class).eq(HrEmplStatusHistory.EMPLOYEE, employee).ge(HrEmplStatusHistory.EFFECTIVEDATE, h.getEffectiveDate()).joinTo(HrEmplStatusHistory.HREMPLOYEESTATUS).eq(HrEmployeeStatus.ACTIVE, 'Y').exists())
				return 0;

			return h.getEffectiveDate();

		} catch (final Exception e) {
			//e.printStackTrace();
			return 0;
		}
	}

	public String getHasMedical() {
		final HrBenefitJoin beneJoin = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, employee).eq(HrBenefitJoin.POLICY_END_DATE, 0).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.TYPE, HrBenefitCategory.HEALTH).first();

		if (beneJoin == null)
			return "No";

		return beneJoin.getHrBenefitConfig().getName();
	}

	public String getHasDental() {
		final HrBenefitJoin beneJoin = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, employee).eq(HrBenefitJoin.POLICY_END_DATE, 0).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.TYPE, HrBenefitCategory.DENTAL).first();

		if (beneJoin == null)
			return "No";

		return beneJoin.getHrBenefitConfig().getName();
	}

	public String getHasVision() {
		final HrBenefitJoin beneJoin = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, employee).eq(HrBenefitJoin.POLICY_END_DATE, 0).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.TYPE, HrBenefitCategory.VISION).first();

		if (beneJoin == null)
			return "No";

		return beneJoin.getHrBenefitConfig().getName();
	}

	public String getStartDate() {
		try {
			return DateUtils.getDateFormatted(ArahantSession.getHSU().createCriteria(HrEmplStatusHistory.class).eq(HrEmplStatusHistory.EMPLOYEE, employee).orderBy(HrEmplStatusHistory.EFFECTIVEDATE).joinTo(HrEmplStatusHistory.HREMPLOYEESTATUS).eq(HrEmployeeStatus.ACTIVE, 'Y').first().getEffectiveDate());
		} catch (final Exception e) {
			return "";
		}
	}

	public int getStartDateInt() {
		try {
			return ArahantSession.getHSU().createCriteria(HrEmplStatusHistory.class).eq(HrEmplStatusHistory.EMPLOYEE, employee).orderBy(HrEmplStatusHistory.EFFECTIVEDATE).joinTo(HrEmplStatusHistory.HREMPLOYEESTATUS).eq(HrEmployeeStatus.ACTIVE, 'Y').first().getEffectiveDate();
		} catch (final Exception e) {
			return 0;
		}
	}

	public String getLastStatusNote() {
		try {
			return getLastStatusHistory().getNotes();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getPriorStatus(int startDate, int endDate, String statusId) {
		try {
			//get the effective status date for the date range
			HrEmplStatusHistory h = ArahantSession.getHSU().createCriteria(HrEmplStatusHistory.class).orderBy(HrEmplStatusHistory.EFFECTIVEDATE).ge(HrEmplStatusHistory.EFFECTIVEDATE, startDate).le(HrEmplStatusHistory.EFFECTIVEDATE, endDate).eq(HrEmplStatusHistory.EMPLOYEE, employee).joinTo(HrEmplStatusHistory.HREMPLOYEESTATUS).eq(HrEmployeeStatus.STATUSID, statusId).first();

			HrEmplStatusHistory h2 = ArahantSession.getHSU().createCriteria(HrEmplStatusHistory.class).orderBy(HrEmplStatusHistory.EFFECTIVEDATE).lt(HrEmplStatusHistory.EFFECTIVEDATE, h.getEffectiveDate()).eq(HrEmplStatusHistory.EMPLOYEE, employee).joinTo(HrEmplStatusHistory.HREMPLOYEESTATUS).ne(HrEmployeeStatus.STATUSID, statusId).first();

			return h2.getHrEmployeeStatus().getName();
		} catch (Exception e) {
			return "";
		}
	}

	public String getLastStatusDateFormatted() {
		try {
			return DateUtils.getDateFormatted(getLastStatusHistory().getEffectiveDate());
			/*
			 * return
			 * DateUtils.getDateFormatted(hsu.createCriteria(HrEmplStatusHistory.class)
			 * .eq(HrEmplStatusHistory.EMPLOYEE, employee)
			 * .orderByDesc(HrEmplStatusHistory.EFFECTIVEDATE)
			 * .joinTo(HrEmplStatusHistory.HREMPLOYEESTATUS)
			 * .first().getEffectiveDate());
			 */
		} catch (final Exception e) {
			return "";
		}
	}

	public String getLastStatusName() {
		try {
			return getLastStatusHistory().getHrEmployeeStatus().getName();
			/*
			 * return hsu.createCriteria(HrEmplStatusHistory.class)
			 * .eq(HrEmplStatusHistory.EMPLOYEE, employee)
			 * .orderByDesc(HrEmplStatusHistory.EFFECTIVEDATE)
			 * .joinTo(HrEmplStatusHistory.HREMPLOYEESTATUS)
			 * .first().getHrEmployeeStatus().getName();
			 */
		} catch (final Exception e) {
			return "";
		}
	}

	public String getPriorStatusName() {
		List<HrEmplStatusHistory> l = ArahantSession.getHSU().createCriteria(HrEmplStatusHistory.class).eq(HrEmplStatusHistory.EMPLOYEE, employee).orderByDesc(HrEmplStatusHistory.EFFECTIVEDATE).setMaxResults(2).list();

		if (l.size() < 2)
			return "";
		return l.get(1).getHrEmployeeStatus().getName();

	}

	public BOrgGroup[] listOrgGroups() {
		return BOrgGroup.makeArray(ArahantSession.getHSU().createCriteria(OrgGroup.class).orderBy(OrgGroup.NAME).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, employee).list());
	}

	/**
	 * @param orgGroupIds
	 * @throws ArahantDeleteException
	 */
	public void unassignToOrgGroup(final String[] orgGroupIds) throws ArahantDeleteException {
		ArahantSession.getHSU().createCriteria(OrgGroupAssociation.class).eq(OrgGroupAssociation.PERSON, employee).joinTo(OrgGroupAssociation.ORGGROUP).in(OrgGroup.ORGGROUPID, orgGroupIds).delete();
	}

	/**
	 * @param orgGroupIds
	 * @throws ArahantException
	 */
	public void assignToOrgGroup(final String[] orgGroupIds, final boolean isPrimary, final int startDate, final int finalDate) throws ArahantException {
		for (String element : orgGroupIds)
			assignToOrgGroup(element, isPrimary, startDate, finalDate);
	}

	/**
	 * @param bc
	 * @return
	 */
	public boolean isPrimary(final BOrgGroup bc) {
		try {
			return ArahantSession.getHSU().createCriteria(OrgGroupAssociation.class).eq(OrgGroupAssociation.ORGGROUP, bc.orgGroup).eq(OrgGroupAssociation.PERSON, employee).first().getPrimaryIndicator() == 'Y';
		} catch (final Exception e) {
			//just return false
		}
		return false;
	}

	/**
	 * @param name
	 * @param cap
	 * @return
	 */
	public BOrgGroup[] searchNotAssignedOrgGroups(final String name, final int cap) {
		final List ids = ArahantSession.getHSU().createCriteria(OrgGroup.class).selectFields(OrgGroup.ORGGROUPID).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, employee).list();

		final List<OrgGroup> l = ArahantSession.getHSU().createCriteria(OrgGroup.class).orderBy(OrgGroup.NAME).like(OrgGroup.NAME, name).eq(OrgGroup.ORGGROUPTYPE, COMPANY_TYPE).setMaxResults(cap).notIn(OrgGroup.ORGGROUPID, ids).list();

		return BOrgGroup.makeArray(l);
	}

	/**
	 * @param firstName
	 * @param lastName
	 * @param ssn
	 * @param cap
	 * @return
	 * @throws ArahantException
	 */
	@SuppressWarnings("unchecked")
	public BPerson[] searchEmployeesForDependent(final String firstName, final String lastName, final String ssn, final int cap) throws ArahantException {
		final List depIds = ArahantSession.getHSU().createCriteria(Person.class).selectFields(Person.PERSONID).joinTo(Person.DEP_JOINS_AS_DEPENDENT).eq(HrEmplDependent.EMPLOYEE, employee).list();

		depIds.add(employee.getPersonId());

//		List<String> orgs = AIProperty.getList("RestrictedOrgGroups");
//
//		Set<String> orgSet = new HashSet<String>();
//
//		if (orgs.size() > 0) {
//			orgSet = new HashSet<String>(orgs);
//			orgSet.addAll(orgs);
//
//			for (String o : orgs) {
//				orgSet.addAll(new BOrgGroup(o).getAllOrgGroupsInHierarchy());
//			}
//
//		}

		final List<Person> l = ArahantSession.getHSU().createCriteria(Person.class).like(Person.FNAME, firstName).like(Person.LNAME, lastName).like(Person.SSN, ssn).notIn(Person.PERSONID, depIds).eq(Person.COMPANYBASE, ArahantSession.getHSU().getCurrentCompany()).setMaxResults(cap).orderBy(Person.LNAME).orderBy(Person.FNAME) //.joinTo(Person.ORGGROUPASSOCIATIONS)
				//.in(OrgGroupAssociation.ORG_GROUP_ID, orgSet)
				.list();


		return makeArray(l);
	}

	/**
	 * @param account
	 * @return
	 */
	public int getBenefitStartDate(final BHRBenefit account) {
		//TODO: try making these sets where I can hash into them
		for (final HrBenefitJoin ebj : employee.getHrBenefitJoinsWhereCovered())
			if (ebj.getHrBenefitConfig().getHrBenefit().getBenefitId().equals(account.getBenefitId()))
				return ebj.getPolicyStartDate();

		return 0;
	}

	/**
	 * @param account
	 * @return
	 */
	public int getBenefitEndDate(final BHRBenefit account) {
		//TODO: try making these sets where I can hash into them
		for (final HrBenefitJoin ebj : employee.getHrBenefitJoinsWhereCovered())
			if (ebj.getHrBenefitConfig().getHrBenefit().getBenefitId().equals(account.getBenefitId()))
				return ebj.getPolicyEndDate();

		return 0;
	}

	public int getRehireDate() {
		//were they ever inactive?
		HrEmplStatusHistory inactive = ArahantSession.getHSU().createCriteria(HrEmplStatusHistory.class).eq(HrEmplStatusHistory.EMPLOYEE, employee).orderByDesc(HrEmplStatusHistory.EFFECTIVEDATE).joinTo(HrEmplStatusHistory.HREMPLOYEESTATUS).eq(HrEmployeeStatus.ACTIVE, 'N').first();

		if (inactive == null)
			return 0;

		//is there an active after this?
		HrEmplStatusHistory active = ArahantSession.getHSU().createCriteria(HrEmplStatusHistory.class).eq(HrEmplStatusHistory.EMPLOYEE, employee).gt(HrEmplStatusHistory.EFFECTIVEDATE, inactive.getEffectiveDate()).orderBy(HrEmplStatusHistory.EFFECTIVEDATE).joinTo(HrEmplStatusHistory.HREMPLOYEESTATUS).eq(HrEmployeeStatus.ACTIVE, 'Y').first();

		if (active == null)
			return 0;

		return active.getEffectiveDate();
	}

	public BHREmplStatusHistory getLastActiveStatusHistory() {
		final List<HrEmplStatusHistory> l = ArahantSession.getHSU().createCriteria(HrEmplStatusHistory.class).eq(HrEmplStatusHistory.EMPLOYEE, employee).orderByDesc(HrEmplStatusHistory.EFFECTIVEDATE).joinTo(HrEmplStatusHistory.HREMPLOYEESTATUS).eq(HrEmployeeStatus.ACTIVE, 'Y').list();

		if (l.isEmpty())
			return null;

		final BHREmplStatusHistory [] ar = BHREmplStatusHistory.makeArray(l);

		//return ar[ar.length-1];
		return ar[0];
	}

	public BHREmplStatusHistory getLastInactiveStatusHistory() {
		final List<HrEmplStatusHistory> l = ArahantSession.getHSU().createCriteria(HrEmplStatusHistory.class).eq(HrEmplStatusHistory.EMPLOYEE, employee).orderByDesc(HrEmplStatusHistory.EFFECTIVEDATE).joinTo(HrEmplStatusHistory.HREMPLOYEESTATUS).eq(HrEmployeeStatus.ACTIVE, 'N').list();

		if (l.isEmpty())
			return null;

		final BHREmplStatusHistory [] ar = BHREmplStatusHistory.makeArray(l);

		//return ar[ar.length-1];
		return ar[0];
	}

	public BHREmplStatusHistory getLastStatusHistory() {
		HrEmplStatusHistory hist = ArahantSession.getHSU().createCriteria(HrEmplStatusHistory.class).eq(HrEmplStatusHistory.EMPLOYEE, employee).orderByDesc(HrEmplStatusHistory.EFFECTIVEDATE).joinTo(HrEmplStatusHistory.HREMPLOYEESTATUS).first();

		if (hist == null)
			return null;

		//Arahant 1/31/12
		//Added a new field in Employee table that contains their current status and status effective date
		//If these fields don't correspond with the last status history, lets change them to keep them consistent
		//This will likely change after fully converting to using the new field
		BHREmplStatusHistory bhist = new BHREmplStatusHistory(hist);
//		int histEffDate = bhist.getEffectiveDate();
//		int empEffDate = employee.getStatusEffectiveDate();
//		if(histEffDate != empEffDate || !bhist.getStatusId().equals(employee.getStatusId())){
//
//			HrEmployeeStatus stat = bhist.getHrEmployeeStatus();
//			System.out.println(this.getNameFML() + "  Status History = " + stat.getName() + " effective " + histEffDate);
//			System.out.println(this.getNameFML() + " Employee Status = " + (employee.getStatus() != null ? employee.getStatus().getName() : "NULL") + " effective " + empEffDate);
		//employee.setStatus(stat);
		//employee.setStatusEffectiveDate(histEffDate);
		//ArahantSession.getHSU().getSession().update(employee);
//		}
		return bhist;
	}

	public BHREmplStatusHistory getLastStatusHistoryBefore(int date) {
		HrEmplStatusHistory hist = ArahantSession.getHSU().createCriteria(HrEmplStatusHistory.class).eq(HrEmplStatusHistory.EMPLOYEE, employee).le(HrEmplStatusHistory.EFFECTIVEDATE, date).orderByDesc(HrEmplStatusHistory.EFFECTIVEDATE).first();

		if (hist == null)
			return null;

		return new BHREmplStatusHistory(hist);
	}

	public String getLastStatusId() {
		try {
			return getLastStatusHistory().getHrEmployeeStatus().getStatusId();
		} catch (final Exception e) {
			return "";
		}
	}

	public int getLastStatusDate() {
		try {
			return getLastStatusHistory().getEffectiveDate();
		} catch (final Exception e) {
			return 0;
		}
	}

	/**
	 * @param statusId
	 * @param statusDate
	 * @param statusNote
	 * @throws ArahantException
	 */
	public void updateStatus(final String statusId, final int statusDate, final String statusNote, Boolean skipCascadedUpdates) throws ArahantException {
		BHREmplStatusHistory bhist = getLastStatusHistory();

		if (bhist != null && bhist.getStatusId().equals(statusId)) {
			if (bhist.getEffectiveDate() != statusDate || !statusNote.equals(bhist.getNotes())) {
				bhist.setEffectiveDate(statusDate);
				bhist.setNotes(statusNote);
				bhist.update(skipCascadedUpdates);
			}
		} else {
			//make a new entry
			bhist = new BHREmplStatusHistory();
			bhist.create();
			bhist.setEmployee(employee);
			bhist.setHrEmployeeStatus(ArahantSession.getHSU().get(HrEmployeeStatus.class, statusId));
			bhist.setEffectiveDate(statusDate);
			bhist.setNotes(statusNote);
			bhist.insert(skipCascadedUpdates);
		}
	}

	public boolean getSmoker() {
		return employee.getSmoker() == 'Y';
	}

	boolean getSpouseSmoker() {
		try {
			return getSpouse().getPerson().getSmoker() == 'Y';
		} catch (Exception e) {
			return false;
		}
	}

	void updateForStatusChange(BHREmplStatusHistory bStatusHistory, boolean newStatusInserted) {
		//ArahantSession.getAI().watchAll();
		if (newStatusInserted)
			if (!BProperty.getBoolean("UsingLISP"))
				ArahantSession.AIEval("(assert (EmployeeStatusChanged \"" + employee.getPersonId() + "\" \"" + bStatusHistory.getHrEmployeeStatus().getStatusId() + "\" \"" + DateUtils.getDateFormatted(bStatusHistory.getEffectiveDate()) + "\"))");
			else
				LispPackage.executeLispMethod("WmCoAutoGenerateCallLogs", "WmCo-AutoGenerateCallLogs", "GenerateCallLogs", employee, bStatusHistory.getBean(), bStatusHistory.getHrEmployeeStatus());

		BHREmplStatusHistory bLastStatusHistory = getLastStatusHistory();
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		// check for new status inserted as the most recent status
		if (bStatusHistory != null && bLastStatusHistory != null && newStatusInserted && bLastStatusHistory.getStatusHistId().equals(bStatusHistory.getStatusHistId())) {
			HrEmployeeStatus status = hsu.get(HrEmployeeStatus.class, bLastStatusHistory.getStatusId());

			// is it an active status that supports benefits?
			if (status.getActive() == 'Y' && status.getBenefitType() == 'B') {
				//	auto assign any auto benefit that they don't have and qualify for
				int startDate = bLastStatusHistory.getEffectiveDate();

				HibernateCriteriaUtil<HrBenefitConfig> hcu = hsu.createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.AUTO_ASSIGN, 'Y');
				HibernateCriterionUtil orcri = hcu.makeCriteria();
				HibernateCriterionUtil cri1 = hcu.makeCriteria();

				HibernateCriteriaUtil classHcu = hcu.leftJoinTo(HrBenefitConfig.BENEFIT_CLASSES, "classes");

				HibernateCriterionUtil cri2 = classHcu.makeCriteria();

				cri1.sizeEq(HrBenefitConfig.BENEFIT_CLASSES, 0);
				cri2.eq("classes." + BenefitClass.BENEFIT_CLASS_ID, getBenefitClassId());

				orcri.or(cri1, cri2);

				orcri.add();

				String changeReasonId = "";

				HrBenefitChangeReason cr = hsu.createCriteria(HrBenefitChangeReason.class).eq(HrBenefitChangeReason.TYPE, HrBenefitChangeReason.NEW_HIRE).dateInside(HrBenefitChangeReason.START_DATE, HrBenefitChangeReason.END_DATE, DateUtils.now()).first();

				if (cr != null)
					changeReasonId = cr.getHrBenefitChangeReasonId();

				if (isEmpty(changeReasonId)) //if no new hire status, use internal edit
				{
					cr = hsu.createCriteria(HrBenefitChangeReason.class).eq(HrBenefitChangeReason.TYPE, HrBenefitChangeReason.INTERNAL_STAFF_EDIT).dateInside(HrBenefitChangeReason.START_DATE, HrBenefitChangeReason.END_DATE, DateUtils.now()).first();
					if (cr != null)
						changeReasonId = cr.getHrBenefitChangeReasonId();
				}

				for (final HrBenefitConfig config : hcu.list())
					if (!hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, config).eq(HrBenefitJoin.PAYING_PERSON, employee).geOrEq(HrBenefitJoin.POLICY_END_DATE, startDate, 0).exists())
						assignBenefit(config, startDate, changeReasonId, false);
			}
		}

		// we need to re-evaluate the benefits against the modified status history list
		final List<StatusHistorySummary> statusHistorySummaries = this.getStatusHistorySummaries();
		final BHRBenefitJoin[] bBenefitJoins = this.getBenefitJoins();
		final HashMap<String, String> alreadyProcessedBenefitJoins = new HashMap<String, String>();

		boolean isFirstStatus = true;

		// spin through each employee status for this person
		for (StatusHistorySummary statusHistorySummary : statusHistorySummaries) {
			// spin through each benefit join covering this person
			for (BHRBenefitJoin bBenefitJoin : bBenefitJoins) {
				// skip dependent benefit joins, we are only comparing status to policies the employee owns
				if (!bBenefitJoin.isPolicyBenefitJoin())
					continue;

				// special check against first status
				if (isFirstStatus)
					this.checkBenefitAgainstFirstStatus(bBenefitJoin, statusHistorySummary, true, false);

				// skip benefits that have already been adjusted
				if (!alreadyProcessedBenefitJoins.containsKey(bBenefitJoin.getBenefitJoinId()))
					// check benefit against status and adjust if required
					if (!isEmpty(this.checkBenefitAgainstStatus(bBenefitJoin, statusHistorySummary, true, false)))
						// benefit was adjusted, skip this benefit on other statuses
						alreadyProcessedBenefitJoins.put(bBenefitJoin.getBenefitJoinId(), bBenefitJoin.getBenefitJoinId());
			}

			// no longer processing first status, whether we currently were or not
			isFirstStatus = false;
		}
	}

	public static class EnrollItem {

		String firstName;
		String lastName;
		String relationship;
		String benefitCategoryName;
		String benefitCategoryCategoryName;
		String startDateFormatted;
		String endDateFormatted;
		String ssn;

		/**
		 * @return Returns the benefitCategoryCategoryName.
		 */
		public String getBenefitCategoryCategoryName() {
			return benefitCategoryCategoryName;
		}

		/**
		 * @param benefitCategoryCategoryName The benefitCategoryCategoryName to
		 * set.
		 */
		public void setBenefitCategoryCategoryName(final String benefitCategoryCategoryName) {
			this.benefitCategoryCategoryName = benefitCategoryCategoryName;
		}

		/**
		 * @return Returns the benefitCategoryName.
		 */
		public String getBenefitCategoryName() {
			return benefitCategoryName;
		}

		/**
		 * @param benefitCategoryName The benefitCategoryName to set.
		 */
		public void setBenefitCategoryName(final String benefitCategoryName) {
			this.benefitCategoryName = benefitCategoryName;
		}

		/**
		 * @return Returns the endDateFormatted.
		 */
		public String getEndDateFormatted() {
			return endDateFormatted;
		}

		/**
		 * @param endDateFormatted The endDateFormatted to set.
		 */
		public void setEndDateFormatted(final String endDateFormatted) {
			this.endDateFormatted = endDateFormatted;
		}

		/**
		 * @return Returns the firstName.
		 */
		public String getFirstName() {
			return firstName;
		}

		/**
		 * @param firstName The firstName to set.
		 */
		public void setFirstName(final String firstName) {
			this.firstName = firstName;
		}

		/**
		 * @return Returns the lastName.
		 */
		public String getLastName() {
			return lastName;
		}

		/**
		 * @param lastName The lastName to set.
		 */
		public void setLastName(final String lastName) {
			this.lastName = lastName;
		}

		/**
		 * @return Returns the relationship.
		 */
		public String getRelationship() {
			return relationship;
		}

		/**
		 * @param relationship The relationship to set.
		 */
		public void setRelationship(final String relationship) {
			this.relationship = relationship;
		}

		/**
		 * @return Returns the ssn.
		 */
		public String getSsn() {
			return ssn;
		}

		/**
		 * @param ssn The ssn to set.
		 */
		public void setSsn(final String ssn) {
			this.ssn = ssn;
		}

		/**
		 * @return Returns the startDateFormatted.
		 */
		public String getStartDateFormatted() {
			return startDateFormatted;
		}

		/**
		 * @param startDateFormatted The startDateFormatted to set.
		 */
		public void setStartDateFormatted(final String startDateFormatted) {
			this.startDateFormatted = startDateFormatted;
		}
	}

	public String getFirstOrgGroup() {
		try {
			return employee.getOrgGroupAssociations().iterator().next().getOrgGroup().getOrgGroupId();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getSpouseDefaultSex() {
		return (employee.getSex() == 'M') ? "F" : "M";
	}

	public String isBenefitActive(final BHRBenefit bene) {
		if (ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, employee).dateInside(HrBenefitJoin.POLICY_START_DATE, HrBenefitJoin.POLICY_END_DATE, DateUtils.now()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bene.getHrBenefit()).exists())
			return "Y";

		return "N";
	}

	public HrBenefitConfig getBenefitOf(final HrBenefitCategory beneCat) {
		for (final HrBenefitJoin ebj : employee.getHrBenefitJoinsWhereCovered())
			try {
				if (ebj.getHrBenefitConfig().getHrBenefitCategory().equals(beneCat))
					return ebj.getHrBenefitConfig();
			} catch (final Exception e) {
				// ignore
			}
		return null;
	}

	public BHRBenefit[] getNonCategoryBenefits() {
		return BHRBenefit.makeArray(ArahantSession.getHSU().createCriteria(HrBenefit.class).isNull(HrBenefit.BENEFIT_CATEGORY).orderBy(HrBenefit.NAME).joinTo(HrBenefit.HREMPLOYEEBENEFITJOINS).eq(HrBenefitJoin.COVERED_PERSON, employee).list());
	}

	public BHRBenefitConfig[] getBenefitConfigs() {
		return BHRBenefitConfig.makeArray(ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).orderBy(HrBenefitConfig.NAME).joinTo(HrBenefit.HREMPLOYEEBENEFITJOINS).eq(HrBenefitJoin.COVERED_PERSON, employee).list());
	}

	/**
	 * This method returns all dependents including Real or Change requests as well as inactive dependents.
	 * It can also return the same dependent multiple times if there are change and real records.
	 * 
	 * @return @throws ArahantException
	 */
	public BHREmplDependent[] getDependents() throws ArahantException {
		return listDependents();
	}

	/**
	 * 
	 * @return number of real dependents plus number of proposed dependents (not yet accepted)
	 * @throws ArahantException 
	 */
	public int getDependentCount() throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		List<HrEmplDependent> deplst = hsu.createCriteria(HrEmplDependent.class).eq(HrEmplDependent.RECORD_TYPE, 'R').eq(HrEmplDependent.EMPLOYEE, employee).eq(HrEmplDependent.DATE_INACTIVE, 0).list();
		List<Person> people = new ArrayList<Person>();
		for (HrEmplDependent dep : deplst)
			people.add(dep.getPerson());
		int count = hsu.createCriteria(HrEmplDependent.class).eq(HrEmplDependent.RECORD_TYPE, 'C').eq(HrEmplDependent.EMPLOYEE, employee).eq(HrEmplDependent.DATE_INACTIVE, 0).notIn(HrEmplDependent.PERSON, people).count();
		count += deplst.size();
		return count;
		//HibernateCriteriaUtil<HrEmplDependentPending> hcuChange = ArahantSession.getHSU().createCriteria(HrEmplDependentPending.class).eq(HrEmplDependent.EMPLOYEE, be.getEmployee());
	}

	public static String getBenefitStatements(final String[] statusIdArray, boolean includeCredentials) throws ArahantException, HibernateException, DocumentException {
		return new EmployeeGroupBenefitStatement().build(statusIdArray, includeCredentials, DateUtils.now());
	}

	public BHRBenefitJoin[] getBenefitJoins() {
		return BHRBenefitJoin.makeArray(employee.getHrBenefitJoinsWhereCovered());
	}

	public BHRBenefitJoin[] getApprovedBenefitJoins() {
		return BHRBenefitJoin.makeArray(ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, employee).eq(HrBenefitJoin.APPROVED, 'Y').list());
	}

	public BHRBenefitJoin[] getApprovedBenefitJoins(int date) {
		return BHRBenefitJoin.makeArray(ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, employee).eq(HrBenefitJoin.APPROVED, 'Y').dateInside(HrBenefitJoin.POLICY_START_DATE, HrBenefitJoin.POLICY_END_DATE, date).list());
	}

	public BHRBenefitJoin[] getUnapprovedBenefitJoins(int date) {
		return BHRBenefitJoin.makeArray(ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, employee).eq(HrBenefitJoin.APPROVED, 'N').dateInside(HrBenefitJoin.POLICY_START_DATE, HrBenefitJoin.POLICY_END_DATE, date).list());
	}

	public BHRBenefitJoin[] getApprovedBenefitJoinsNonDeclines(int date) {
		return BHRBenefitJoin.makeArray(ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, employee).eq(HrBenefitJoin.APPROVED, 'Y').dateInside(HrBenefitJoin.POLICY_START_DATE, HrBenefitJoin.POLICY_END_DATE, date).isNotNull(HrBenefitJoin.HR_BENEFIT_CONFIG_ID).list());
	}

	public BHRBenefitJoin[] getNonTimeRelatedBenefitJoins(int date) {
		return BHRBenefitJoin.makeArray(ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, employee).eq(HrBenefitJoin.APPROVED, 'Y').dateInside(HrBenefitJoin.POLICY_START_DATE, HrBenefitJoin.POLICY_END_DATE, date).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.TIMERELATED, 'N').list());
	}

	public String getOrgGroupName() {
		try {

			return employee.getOrgGroupAssociations().iterator().next().getOrgGroup().getName();
		} catch (final Exception e) {
			return "";
		}
	}

	public Integer getCurrentHiredDate() {
		//employee can be terminated and get rehired as part time and then full time
		//so we need to figure out the current hired date
		//which is the date after the last terminated date
		int effectiveDate = getRehireDate();
		if (effectiveDate == 0)
			effectiveDate = getEmploymentDate();

		//System.out.println(employee.getNameLFM()+" "+employee.getPersonId()+" "+effectiveDate);
		return effectiveDate;
	}

	public boolean getIsNewHire() {

		//TODO: check for new active status in last 31 days also

		if (!AIProperty.getBoolean("IsWmCo")) {
			BWizardConfiguration wc = getWizardConfiguration("E");
			int started = getCurrentHiredDate();
			int now = DateUtils.now();
			int daysBefore = wc.getHdeDaysBefore();
			int daysAfter = wc.getHdeDaysAfter();
			switch (wc.getHdeType()) {
				case 1:  // hire date, do nothing
					break;
				case 2: 
					started = DateUtils.addDays(started, wc.getHdePeriod());
					if (DateUtils.getDay(started) != 1) {
						started = DateUtils.addMonths(started, 1);
						started = DateUtils.setDay(started, 1);
					}
					break;
				case 3:
					started = DateUtils.addMonths(started, wc.getHdePeriod());
					if (DateUtils.getDay(started) != 1) {
						started = DateUtils.addMonths(started, 1);
						started = DateUtils.setDay(started, 1);
					}
					break;
				case 4:
					started = DateUtils.addDays(started, wc.getHdePeriod());
					break;
				case 5:
					started = DateUtils.addMonths(started, wc.getHdePeriod());
					break;					
			}
			return DateUtils.addDays(started, -daysBefore) <= now  &&  DateUtils.addDays(started, daysAfter) >= now;
		} else {

			boolean ret = !ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'Y').eq(HrBenefitJoin.COVERED_PERSON, employee).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.AUTO_ASSIGN, 'N').exists();

			if (!ret)
				return ret;

			//since declines are auto-approved, gotta check if there is a decline that doesn't use the "new hire" bcr type
			HrBenefitJoin j = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'Y').eq(HrBenefitJoin.COVERED_PERSON, employee).joinTo(HrBenefitJoin.HRBENEFIT).first();
			if (j != null) {
				if (j.getBenefitChangeReason() != null)
					if (j.getBenefitChangeReason().getType() == HrBenefitChangeReason.NEW_HIRE)
						ret = true;
				if (j.getLifeEvent() != null && j.getLifeEvent().getChangeReason() != null)
					if (j.getLifeEvent().getChangeReason().getType() == HrBenefitChangeReason.NEW_HIRE)
						ret = true;
			}

			if (!ret)
				return ret;

			j = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'Y').eq(HrBenefitJoin.COVERED_PERSON, employee).joinTo(HrBenefitJoin.HR_BENEFIT_CATEGORY).first();
			if (j != null)
				if (j.getBenefitChangeReason() != null)
					ret = j.getBenefitChangeReason().getType() == HrBenefitChangeReason.NEW_HIRE;
				else if (j.getLifeEvent() != null)
					ret = j.getLifeEvent().getChangeReason().getType() == HrBenefitChangeReason.NEW_HIRE;
			return ret;
		}
	}

	public boolean getIsOpenEnrollment() {
		return ArahantSession.getHSU().createCriteria(HrBenefitChangeReason.class).dateInside(HrBenefitChangeReason.START_DATE, HrBenefitChangeReason.END_DATE, DateUtils.now()).eq(HrBenefitChangeReason.TYPE, HrBenefitChangeReason.OPEN_ENROLLMENT).exists();
	}

	/**
	 * @param cap
	 * @return
	 * @throws ArahantException
	 */
	public BHREmplDependent[] listInactiveDependents(final int cap) throws ArahantException {
		return BHREmplDependent.makeArray(ArahantSession.getHSU().createCriteria(HrEmplDependent.class).eq(HrEmplDependent.EMPLOYEE, employee).lt(HrEmplDependent.DATE_INACTIVE, DateUtils.now()).list());
	}

	public BHREmplDependent[] listActiveDependents() throws ArahantException {
		return BHREmplDependent.makeArray(ArahantSession.getHSU().createCriteria(HrEmplDependent.class).eq(HrEmplDependent.EMPLOYEE, employee).geOrEq(HrEmplDependent.DATE_INACTIVE, DateUtils.now(), 0).list());
	}

	public BHREmplDependent[] listEligibleEnrollees(final HrBenefitConfig bene, final int cap) throws ArahantException {
		final List<HrEmplDependent> deps = new ArrayList<HrEmplDependent>();

		if (bene.getEmployee() == 'Y') {
			//somehow add employee to return list
			final HrEmplDependent emp = new HrEmplDependent();
			emp.setPerson(employee);
			emp.setEmployee(employee);
			emp.setRelationshipType('E');
			deps.add(emp);
		}

		if (bene.getSpouseNonEmpOrChildren() == 'Y') {
//			add spouse to return list
			if (getActiveSpouse(DateUtils.now()) != null)
				deps.add(getActiveSpouse(DateUtils.now()));
//			add children to return list
			deps.addAll(getChildren());
		} else {
			if (bene.getSpouseNonEmployee() == 'Y')
				//add spouse to return list
				if (getActiveSpouse(DateUtils.now()) != null)
					deps.add(getActiveSpouse(DateUtils.now()));
			if (bene.getChildren() == 'Y')
				//add children to return list
				deps.addAll(getChildren());
		}

		final BHREmplDependent[] ret = new BHREmplDependent[deps.size()];

		int loop = 0;

		for (final HrEmplDependent dependent : deps)
			ret[loop++] = new BHREmplDependent(dependent);

		return ret;
	}

	public Collection<? extends HrEmplDependent> getChildren() {
		final List<HrEmplDependent> deps = new LinkedList<HrEmplDependent>();
		for (final HrEmplDependent dep : employee.getHrEmplDependents())
			if (dep.getRelationshipType() == 'C' && dep.getDateInactive() == 0)
				deps.add(dep);
		return deps;
	}

	/**
	 * This method will return an active Real or Change request spouse.
	 */
	public HrEmplDependent getSpouse() {
		return getSpouse(false);
	}
	
	/**
	 * Returns the relationship type the passed in person has to the employee.
	 * 
	 * @param p
	 * @return Employee / Spouse / Child / Other / Unknown
	 */
	public char getRelationshipType(Person p) {
		String depId = p.getPersonId();
		if (getPersonId().equals(depId))
			return 'E';  //  the employee
		for (BHREmplDependent dep : getDependents())
			if (depId.equals(dep.getDependentId()))
				return dep.getRelationship();
		return 'U';  //  unknown	
	}
	
	/**
	 * This method only returns an active spouse.
	 * 
	 * @param realOnly controls whether to include change records or not
	 * @return an active spouse
	 */

	public HrEmplDependent getSpouse(boolean realOnly) {
		int now = DateUtils.now();
		for (BHREmplDependent dep : this.getDependents())
			if (dep.getRelationshipType().equals("S") && 
					(dep.getInactiveDate() > now || dep.getInactiveDate() == 0)
					&& (!realOnly || (realOnly && dep.getRecordType() == 'R')))
				return dep.bean;
		return null;
	}

	/*
	 * public void enroll(final String benefitId, final String[] enrollees,
	 * final int effectiveDate) throws ArahantException {
	 *
	 * final HrBenefitConfig bene=hsu.get(HrBenefitConfig.class, benefitId);
	 *
	 * final HrBenefitJoin ebj=assignBenefit(bene, effectiveDate);
	 *
	 * ebj.setCoverageStartDate(0);
	 *
	 * for (final String key : enrollees) if (key.equals(getPersonId()))
	 * ebj.setCoverageStartDate(effectiveDate);
	 *
	 * hsu.saveOrUpdate(ebj);
	 *
	 * for (final String key : enrollees) if (!key.equals(getPersonId())) new
	 * BHREmplDependent(key).assignBenefit(ebj);
	 *
	 * }
	 */

	public boolean hasBenefit(final String benefitId) {
		final HibernateCriteriaUtil<HrBenefitJoin> hcu = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, employee).ge(HrBenefitJoin.COVERAGE_START_DATE, DateUtils.now());

		final HibernateCriterionUtil cri1 = hcu.makeCriteria();
		final HibernateCriterionUtil cri2 = hcu.makeCriteria();
		final HibernateCriterionUtil cri3 = hcu.makeCriteria();

		cri1.le(HrBenefitJoin.COVERAGE_END_DATE, DateUtils.now());
		cri2.eq(HrBenefitJoin.COVERAGE_END_DATE, 0);

		cri3.or(cri1, cri2);

		cri3.add();

		return hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFITID, benefitId).exists();
	}

	public String getOpenEnrollmentId() {
		return BHRBenefitChangeReason.getOpenEnrollmentId();
	}

	public String getNewHireId() {
		return BHRBenefitChangeReason.getNewHireId();
	}

	public int getEffectiveDateStandard(final String reasonId, int benefitCategory, int qualifyingDate) {
		//TODO: this is wmco specific
		final HrBenefitChangeReason reason = ArahantSession.getHSU().get(HrBenefitChangeReason.class, reasonId);
		try {
			short days;

			switch (benefitCategory) {
				//case BHRBenefitCategory.HEALTH: days=31; break;  //31 days from start date
				//case BHRBenefitCategory.DENTAL: days=31; break;  //31 days from start date
				//case BHRBenefitCategory.VISION: days=31; break;  //31 days from start date
				//case BHRBenefitCategory.FLEX_TYPE: days=31; break;
				case HrBenefitCategory.VOLUNTARY: {
					//1st of month after 60 days of employment
					Calendar cal = DateUtils.getCalendar(getStartDateInt());
					//add the 60 days
					cal.add(Calendar.DAY_OF_YEAR, 60);

					//move to the next month
					cal.add(Calendar.MONTH, 1);

					//set the day to the first
					cal.set(Calendar.DAY_OF_MONTH, 1);

					//get the difference between these days and start date
					long millis = cal.getTimeInMillis() - DateUtils.getCalendar(getStartDateInt()).getTimeInMillis();

					days = (short) (millis / 1000 / 60 / 60 / 24);

					break;
				}

				default:
					days = 31;
					break;  //31 days from start date
			}

			//	BHRBenefitCategory.FLEX_TYPE
			switch (reason.getType()) {
				case HrBenefitChangeReason.QUALIFYING_EVENT:
					return qualifyingDate;
				case HrBenefitChangeReason.OPEN_ENROLLMENT:
					return reason.getEffectiveDate();
				case HrBenefitChangeReason.NEW_HIRE:
					return DateUtils.addDays(getStartDateInt(), days);
				case HrBenefitChangeReason.INTERNAL_STAFF_EDIT:
					return DateUtils.now();
				default:
					return DateUtils.now();
			}
		} catch (final Exception e) {
			return DateUtils.now();
		}
	}

	public int getEffectiveDateDecline(final String reasonId) {
		final HrBenefitChangeReason reason = ArahantSession.getHSU().get(HrBenefitChangeReason.class, reasonId);
		try {
			switch (reason.getType()) {
				case 1:
					return DateUtils.now();
				case 2:
					return reason.getEffectiveDate();
				case 3:
					return DateUtils.addDays(getStartDateInt(), 30);

				default:
					return DateUtils.now();
			}
		} catch (final Exception e) {
			return DateUtils.now();
		}
	}

	public boolean hasSpouse() {
		return getSpouse() != null;
	}
	
	public boolean hasChildren() {
		return getChildren().size() > 0;
	}

	/**
	 * @return Returns the amountCovered.
	 */
	public double getAmountCovered() {
		return amountCovered;
	}

	/**
	 * @param amountCovered The amountCovered to set.
	 */
	public void setAmountCovered(final double amountCovered) {
		this.amountCovered = amountCovered;
	}

	public HrBenefitJoin getBenefitJoin(final String benefitConfigId) {
		return ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, employee).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.BENEFIT_CONFIG_ID, benefitConfigId).first();
	}

	/**
	 * @return a list of benefits that need beneficiaries
	 */
	public BHRBenefitConfig[] listBeneficiaryBenefits() {
		return BHRBenefitConfig.makeArray(ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.HAS_BENEFICIARIES, 'Y').orderBy(HrBenefit.NAME).list());
	}

	public String getProvidedBySSN(final BHRBenefitConfig bc) {
		HrBenefitJoin bj = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, employee).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, bc.bean).first();

		if (bj == null || bj.getCoveredPerson().equals(bj.getPayingPerson()))
			return "";

		return bj.getPayingPerson().getUnencryptedSsn();
	}

	public PaySchedule getPaySchedule() {
		//just go until I find a pay schedule
		for (BOrgGroup og : BOrgGroup.makeArray(ArahantSession.getHSU().createCriteria(OrgGroup.class).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, employee).list())) {
			PaySchedule ps = og.getPaySchedule();
			if (ps != null)
				return ps;
		}
		return null;
	}

	public int getPPYremaining(int fromDate) {
		//just go until I find a pay schedule

		//set to last day of year
		int toDate;
		Calendar toDateCal = DateUtils.getCalendar(fromDate);
		toDateCal.set(Calendar.MONTH, Calendar.DECEMBER);
		toDateCal.set(Calendar.DAY_OF_MONTH, 31);
		toDate = DateUtils.getDate(toDateCal);

		for (BOrgGroup og : BOrgGroup.makeArray(ArahantSession.getHSU().createCriteria(OrgGroup.class).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, employee).list())) {
			PaySchedule ps = og.getPaySchedule();

			if (ps != null) {
				int count = 0;
				for (PaySchedulePeriod psp : ps.getPeriods())
					if (psp.getLastDate() > fromDate && psp.getLastDate() <= toDate)
						count++;
				return count;
			}
		}
		return -1;
	}

	private short lookForPPY(OrgGroup og) {
		if (og.getPayPeriodsPerYear() != 0)
			return og.getPayPeriodsPerYear();

		for (OrgGroupHierarchy ogh : og.getOrgGroupHierarchiesForChildGroupId()) {
			short ret = lookForPPY(ogh.getOrgGroupByParentGroupId());

			if (ret != 0)
				return ret;
		}
		return 0;
	}

	public double getCostFromAmount(double amount, int amountType) {
		double ret;
		short ppy = 0;

		for (OrgGroupAssociation oga : employee.getOrgGroupAssociations()) {
			ppy = lookForPPY(oga.getOrgGroup());
			if (ppy != 0)
				break;
		}

		if (ppy == 0) //if they don't have pay periods, just return amount?
				return amount;

		if (amountType == 0) //I have annual amount, need to return per pay period
			ret = amount / ppy;
		else //I have pay period amount, need to return annual
			ret = amount * ppy;

		return ret;
	}

	private int getActiveRelType(char type, int effectiveDate) {
		int count = 0;
		for (HrEmplDependent dep : employee.getHrEmplDependents())
			if (dep.getRelationshipType() == type && (dep.getDateInactive() == 0 || dep.getDateInactive() > effectiveDate))
				count++;
		return count;
	}

	public int getActiveChildren(int effectiveDate) {
		return getActiveRelType('C', effectiveDate);
	}

	public int getActiveOther() {
		return getActiveRelType('O', DateUtils.now());
	}

	/**
	 * @return @throws ArahantException
	 */
	public boolean getActiveEmployeeSpouse(int asOfDate) throws ArahantException {
		if (getSpouse() == null)
			return false;

		Employee emp = ArahantSession.getHSU().get(Employee.class, getSpouse().getPerson().getPersonId());
		boolean spouseIsEmp = false;

		if (emp != null) {
			BEmployee spEmp = new BEmployee(emp);
			spouseIsEmp = spEmp.isActiveOn(asOfDate);
		}

		return spouseIsEmp;
	}

	public boolean hasPendingBenefitConfig(String benefitConfigId) {
		final HibernateCriteriaUtil<HrBenefitJoin> hcu = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, employee).eq(HrBenefitJoin.APPROVED, 'N');

		return hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.BENEFIT_CONFIG_ID, benefitConfigId).exists();
	}

	public boolean hasApprovedBenefitConfig(String benefitConfigId) {
		return ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, employee).eq(HrBenefitJoin.APPROVED, 'Y').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.BENEFIT_CONFIG_ID, benefitConfigId).exists();
	}

	public boolean declinedBenefit(String benefitId) {
//		have I picked this as unapproved?
		boolean assigned = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, employee).eq(HrBenefitJoin.APPROVED, 'N').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFITID, benefitId).exists();

		if (assigned)
			return false;

		HrBenefitJoin ebj1 = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, employee).joinTo(HrBenefitJoin.HRBENEFIT).eq(HrBenefit.BENEFITID, benefitId).first();

		//check dates for config

		HrBenefitJoin ebj2 = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, employee).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFITID, benefitId).first();

		if (ebj1 == null)
			return false;

		if (ebj2 == null)
			return true;

		return ebj1.getPolicyStartDate() > ebj2.getPolicyStartDate();
	}

	public boolean declinedCategory(String categoryId) {
		//have I picked this as unapproved?
		boolean assigned = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, employee).eq(HrBenefitJoin.APPROVED, 'N').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, categoryId).exists();

		if (assigned)
			return false;

		return ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, employee).joinTo(HrBenefitJoin.HR_BENEFIT_CATEGORY).eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, categoryId).exists();
	}

	public HrEmplDependent getActiveSpouse(int effectiveDate) {
		if (getSpouse() != null && (getSpouse().getDateInactive() == 0 || getSpouse().getDateInactive() < effectiveDate))
			return getSpouse();

		return null;
	}

	public HrBenefitJoin getWizardBenefitJoin(String benefitConfigId) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		HrBenefitConfig conf = hsu.get(HrBenefitConfig.class, benefitConfigId);

		HrBenefitJoin ebj = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.COVERED_PERSON, employee).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, conf).first();

		if (ebj != null)
			return ebj;

		//do I have a declined unapproved?

		if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.COVERED_PERSON, employee).eq(HrBenefitJoin.HR_BENEFIT_CATEGORY, conf.getHrBenefitCategory()).exists())
			return null;

		if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.COVERED_PERSON, employee).eq(HrBenefitJoin.HRBENEFIT, conf.getHrBenefit()).exists())
			return null;


		HrBenefitJoin ebj1 = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, employee).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, conf).first();

		HrBenefitJoin ebj2 = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, employee).eq(HrBenefitJoin.HRBENEFIT, conf.getHrBenefit()).first();

		HrBenefitJoin ebj3 = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, employee).eq(HrBenefitJoin.HR_BENEFIT_CATEGORY, conf.getHrBenefitCategory()).first();

		if (ebj1 != null) {

//			if this is a category based decline, do I have an unapproved in the category
			if (!ebj1.getHrBenefitConfig().getHrBenefitCategory().getAllowsMultipleBenefitsBoolean() && hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.COVERED_PERSON, employee).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefitJoin.HR_BENEFIT_CATEGORY, ebj1.getHrBenefitConfig().getHrBenefitCategory()).exists())
				return null;

//			if this is a benefit based decline, do I have an unapproved in the benefit
			if (ebj1.getHrBenefitConfig().getHrBenefit().getRequiresDecline() == 'Y' && hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.COVERED_PERSON, employee).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, ebj1.getHrBenefitConfig().getHrBenefit()).exists())
				return null;

			if (ebj2 != null && ebj1.getPolicyStartDate() > ebj2.getPolicyStartDate())
				return ebj1;

			if (ebj3 != null && ebj1.getPolicyStartDate() > ebj3.getPolicyStartDate())
				return ebj1;

			if (ebj2 == null && ebj3 == null)
				return ebj1;
		}

		return null;
	}

	public int getEffectiveDateStandard(String reasonId, String configId, int qualifyingDate) throws ArahantException {
		return getEffectiveDateStandard(reasonId, new BHRBenefitConfig(configId).getBenefitCategoryType(), qualifyingDate);
	}

	public static String getLastHistoryId(String empId) {
		try {
			HibernateCriteriaUtil<HrEmplStatusHistory> hcu = ArahantSession.getHSU().createCriteria(HrEmplStatusHistory.class).setMaxResults(1).selectFields(HrEmplStatusHistory.EFFECTIVEDATE, HrEmplStatusHistory.STATUS_ID).orderByDesc(HrEmplStatusHistory.EFFECTIVEDATE);

			hcu.joinTo(HrEmplStatusHistory.EMPLOYEE).eq(Employee.PERSONID, empId);


			HibernateScrollUtil<HrEmplStatusHistory> hscrl = hcu.scroll();

			if (hscrl.next()) {
				String ret = hscrl.getString(1);
				hscrl.close();
				return ret;
			}

			hscrl.close();
			return "";

		} catch (Exception e) {
			return "";
		}
	}

	public static int getLastHistoryDate(String empId) {
		try {
			HibernateCriteriaUtil<HrEmplStatusHistory> hcu = ArahantSession.getHSU().createCriteria(HrEmplStatusHistory.class).selectFields(HrEmplStatusHistory.EFFECTIVEDATE, HrEmplStatusHistory.STATUS_ID).orderByDesc(HrEmplStatusHistory.EFFECTIVEDATE);

			hcu.joinTo(HrEmplStatusHistory.EMPLOYEE).eq(Employee.PERSONID, empId);

			HibernateScrollUtil<HrEmplStatusHistory> hscrl = hcu.scroll();

			if (hscrl.next())
				return hscrl.getInt(0);

			hscrl.close();
			return 0;

		} catch (Throwable e) {
			return 0;
		}
	}

	public boolean isCobra() {
		return getLastStatusId().equals("00001-0000000005");
	}

	public boolean isRetired() {
		return getLastStatusId().equals("00001-0000000002");
	}

	public void setStatusId(String employeeStatusId, int date) throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		HrEmployeeStatus stat = hsu.get(HrEmployeeStatus.class, employeeStatusId);

		if (stat == null)
			return;

		HibernateCriteriaUtil<Employee> hcu = hsu.createCriteria(Employee.class).eq(Employee.PERSONID, employee.getPersonId());
		hcu.employeeCurrentStatus(employeeStatusId);
		if (hcu.exists())
			return;

		BHREmplStatusHistory statHist = new BHREmplStatusHistory();
		statHist.create();
		statHist.setEffectiveDate(date);
		statHist.setEmployee(employee);
		statHist.setHrEmployeeStatus(stat);
		statHist.setNotes("");

		statHist.insert();

		this.updateForStatusChange(statHist, true);
	}

	public void setStatusId(String employeeStatusId, int date, String notes) throws ArahantException {
		HrEmployeeStatus stat = ArahantSession.getHSU().get(HrEmployeeStatus.class, employeeStatusId);

		if (stat == null)
			return;

		HibernateCriteriaUtil<Employee> hcu = ArahantSession.getHSU().createCriteria(Employee.class).eq(Employee.PERSONID, employee.getPersonId());
		hcu.employeeCurrentStatus(employeeStatusId);
		if (hcu.exists())
			return;

		BHREmplStatusHistory statHist = new BHREmplStatusHistory();
		statHist.create();
		statHist.setEffectiveDate(date);
		statHist.setEmployee(employee);
		statHist.setHrEmployeeStatus(stat);
		statHist.setNotes(notes);

		statHist.insert();

		this.updateForStatusChange(statHist, true);
	}

	public OrgGroupStatusHistory[] listLocationAndStatusHistory() {

		List<HrEmplStatusHistory> histList = ArahantSession.getHSU().createCriteria(HrEmplStatusHistory.class).eq(HrEmplStatusHistory.EMPLOYEE, employee).orderBy(HrEmplStatusHistory.EFFECTIVEDATE).list();

		List<OrgGroupStatusHistory> retList = new LinkedList<OrgGroupStatusHistory>();

		for (HrEmplStatusHistory hist : histList)
			retList.add(new OrgGroupStatusHistory(hist));


		List<OrgGroupAssociationH> orgList = ArahantSession.getHSU().createCriteria(OrgGroupAssociationH.class).eq(OrgGroupAssociationH.PERSON_ID, employee.getPersonId()).ne(OrgGroupAssociationH.RECORD_CHANGE_TYPE, 'D').orderBy(OrgGroupAssociationH.RECORD_CHANGE_DATE).list();

		for (OrgGroupAssociationH hist : orgList) {
			BOrgGroup borg = new BOrgGroup(hist.getOrgGroupId());
			OrgGroupStatusHistory osh = new OrgGroupStatusHistory(hist);
			osh.name = borg.getLocation().getName();
			retList.add(osh);
		}
		for (OrgGroupAssociation oga : employee.getOrgGroupAssociations()) {
			OrgGroupStatusHistory osh = new OrgGroupStatusHistory(oga);
			osh.name = new BOrgGroup(oga.getOrgGroup()).getLocation().getName();
			retList.add(osh);
		}

		java.util.Collections.sort(retList);

		/*
		 * List<OrgGroupStatusHistory> retList2=new
		 * LinkedList<OrgGroupStatusHistory>();
		 *
		 * if (retList.size()>0) retList2.add(retList.get(0));
		 *
		 * for (int loop=1;loop<retList.size();loop++) { if
		 * (!retList.get(loop).name.equals(retList.get(loop-1).name))
		 * retList2.add(retList.get(loop-1)); }
		 */
		OrgGroupStatusHistory [] ogs = new OrgGroupStatusHistory[retList.size()];

		return retList.toArray(ogs);
	}

	public static class OrgGroupStatusHistory implements Comparable<OrgGroupStatusHistory> {
		public String historyId = "";
		public String historyType = "";
		public int historyDate = 0;
		public String name = "";
		public String statusNote = "";

		public OrgGroupStatusHistory(HrEmplStatusHistory histy) {
			BHREmplStatusHistory hist = new BHREmplStatusHistory(histy);
			historyId = hist.getStatusHistId();
			historyType = "Status";
			historyDate = hist.getEffectiveDate();
			name = hist.getHrEmployeeStatus().getName();
			statusNote = hist.getNotes();
		}

		/**
		 * @param hist
		 */
		public OrgGroupStatusHistory(OrgGroupAssociationH hist) {
			historyId = hist.getHistory_id();
			historyType = "Location";
			historyDate = DateUtils.getDate(hist.getRecordChangeDate());
			name = new BOrgGroup(hist.getOrgGroupId()).getName();
		}

		/**
		 * @param oga
		 */
		public OrgGroupStatusHistory(OrgGroupAssociation oga) {
			historyType = "Location";
			historyDate = DateUtils.getDate(oga.getRecordChangeDate());
			name = oga.getOrgGroup().getName();
		}
		/*
		 * (non-Javadoc) @see java.lang.Comparable#compareTo(java.lang.Object)
		 */

		@Override
		public int compareTo(OrgGroupStatusHistory o) {

			return o.historyDate - historyDate;
		}
	}

	/**
	 * @param cap
	 * @return
	 */
	public OrgGroupAssociationHistory[] listOrgGroupHistory(int cap) {

		List<OrgGroupAssociationHistory> retList = new LinkedList<OrgGroupAssociationHistory>();

		List<OrgGroupAssociationH> orgList = ArahantSession.getHSU().createCriteria(OrgGroupAssociationH.class).eq(OrgGroupAssociationH.PERSON_ID, employee.getPersonId()).orderBy(OrgGroupAssociationH.RECORD_CHANGE_DATE).list();

		for (OrgGroupAssociationH hist : orgList)
			retList.add(new OrgGroupAssociationHistory(hist));

		for (OrgGroupAssociation oga : employee.getOrgGroupAssociations())
			retList.add(new OrgGroupAssociationHistory(oga));

		OrgGroupAssociationHistory ogs[] = new OrgGroupAssociationHistory[retList.size()];

		java.util.Collections.sort(retList);

		return retList.toArray(ogs);
	}

	public static class OrgGroupAssociationHistory implements Comparable<OrgGroupAssociationHistory> {

		private Date date;
		private String dateTimeFormatted;
		private String changeType;
		private String orgGroupName;
		private String supervisor;
		private String personName;
		private String externalId;
		private int startDate;
		private int finalDate;
		private String historyId;

		/**
		 * @param hist
		 */
		public OrgGroupAssociationHistory(OrgGroupAssociationH hist) {
			date = hist.getRecordChangeDate();
			dateTimeFormatted = DateUtils.getDateTimeFormatted(date);
			changeType = hist.getChangeTypeFormatted();

			try {
				BOrgGroup borg = new BOrgGroup(hist.getOrgGroupId());
				orgGroupName = borg.getName();
				externalId = borg.getExternalId();
			} catch (Exception ignored) {
				// this occurs when the org group no longer exists
				orgGroupName = "Unknown";
				externalId = "";
			}
			personName = new BPerson(hist.getRecordPersonId()).getNameLFM();
			supervisor = hist.getPrimaryIndicator() == 'Y' ? "Yes" : "No";
			startDate = hist.getStartDate();
			finalDate = hist.getFinalDate();
			historyId = hist.getHistory_id();
		}

		public OrgGroupAssociationHistory(OrgGroupAssociation hist) {
			date = hist.getRecordChangeDate();
			dateTimeFormatted = DateUtils.getDateTimeFormatted(date);
			changeType = hist.getChangeTypeFormatted();
			BOrgGroup borg = new BOrgGroup(hist.getOrgGroupId());
			orgGroupName = borg.getName();
			externalId = borg.getExternalId();
			personName = new BPerson(hist.getRecordPersonId()).getNameLFM();
			supervisor = hist.getPrimaryIndicator() == 'Y' ? "Yes" : "No";
			startDate = hist.getStartDate();
			finalDate = hist.getFinalDate();
			historyId = "";
		}

		@Override
		public int compareTo(OrgGroupAssociationHistory o) {
			return o.date.compareTo(date);
		}

		public String getChangeType() {
			return changeType;
		}

		public String getDateTimeFormatted() {
			return dateTimeFormatted;
		}

		public String getExternalId() {
			return externalId;
		}

		public String getOrgGroupName() {
			return orgGroupName;
		}

		public String getPersonName() {
			return personName;
		}

		public String getSupervisor() {
			return supervisor;
		}

		public int getStartDate() {
			return startDate;
		}

		public int getFinalDate() {
			return finalDate;
		}

		public String getHistoryId() {
			return historyId;
		}

		public void setHistoryId(String historyId) {
			this.historyId = historyId;
		}
	}

	/**
	 * Gets confirmation messages for what would happen if the status history
	 * changes that have been made in memory to this point (should be rolled
	 * back shortly after this call) were to have been made with cascaded
	 * updates.
	 */
	public String[] getConfirmationsForStatusChange() {
		final ArrayList<String> confirmations = new ArrayList<String>();
		List<StatusHistorySummary> statusHistorySummaries = this.getStatusHistorySummaries();
		final BHRBenefitJoin[] bBenefitJoins = this.getBenefitJoins();
		final HashMap<String, String> alreadyProcessedBenefitJoins = new HashMap<String, String>();
		boolean isFirstStatus = true;
		String message;

		// spin through each employee status for this person
		for (StatusHistorySummary statusHistorySummary : statusHistorySummaries) {
			// spin through each benefit join covering this person
			for (BHRBenefitJoin bBenefitJoin : bBenefitJoins) {
				// skip dependent benefit joins, we are only comparing status to policies the employee owns
				if (!bBenefitJoin.isPolicyBenefitJoin())
					continue;

				// special check against first status
				if (isFirstStatus) {
					message = this.checkBenefitAgainstFirstStatus(bBenefitJoin, statusHistorySummary, true, true);

					if (!isEmpty(message))
						confirmations.add(message);
				}

				// skip benefits that have already been adjusted
				if (!alreadyProcessedBenefitJoins.containsKey(bBenefitJoin.getBenefitJoinId())) {
					message = this.checkBenefitAgainstStatus(bBenefitJoin, statusHistorySummary, true, true);

					// check if the benefit was adjusted
					if (!isEmpty(message)) {
						confirmations.add(message);

						// skip this benefit on other statuses
						alreadyProcessedBenefitJoins.put(bBenefitJoin.getBenefitJoinId(), bBenefitJoin.getBenefitJoinId());
					}
				}
			}

			// no longer processing first status, whether we currently were or not
			isFirstStatus = false;
		}

		String[] confirmationsArray = new String[confirmations.size()];
		return confirmations.toArray(confirmationsArray);
	}

	public String[] getErrorsForBenefitChange(BHRBenefitJoin bBenefitJoin) {
		final ArrayList<String> errors = new ArrayList<String>();
		List<StatusHistorySummary> statusHistorySummaries = this.getStatusHistorySummaries();
		boolean isFirstStatus = true;
		String message;

		// spin through each employee status for this person
		for (StatusHistorySummary statusHistorySummary : statusHistorySummaries) {
			// special check against first status
			if (isFirstStatus) {
				message = this.checkBenefitAgainstFirstStatus(bBenefitJoin, statusHistorySummary, false, true);

				if (!isEmpty(message))
					errors.add(message);

				isFirstStatus = false;
			}

			message = this.checkBenefitAgainstStatus(bBenefitJoin, statusHistorySummary, false, true);

			// check if the benefit was adjusted
			if (!isEmpty(message)) {
				errors.add(message);

				break;
			}
		}

		String[] errorsArray = new String[errors.size()];
		return errors.toArray(errorsArray);
	}

	protected static class StatusHistorySummary {
		public String statusId;
		public char benefitType;
		public int startDate;
		public int finalDate;
	}

	/**
	 * Gets summary information of the statuses for this employee. A status
	 * final date is added for easier processing, and all dates are normalized
	 * (what they actually are in db instead of -1/+1).
	 */
	protected List<StatusHistorySummary> getStatusHistorySummaries() {
		final List<StatusHistorySummary> statusHistorySummaries = new ArrayList<StatusHistorySummary>();
		final List<HrEmplStatusHistory> currentHrEmplStatusHistories = ArahantSession.getHSU().createCriteria(HrEmplStatusHistory.class).eq(HrEmplStatusHistory.EMPLOYEE, employee).orderBy(HrEmplStatusHistory.EFFECTIVEDATE).joinTo(HrEmplStatusHistory.HREMPLOYEESTATUS).list();

		// NOTE: the dates being used here are not yet adjusted for the -1 if it is a final date, which is what we want

		// set final dates to make processing easier ... added complexity that system currently
		// allows status histories to be on same day
		List<StatusHistorySummary> prevStatusHistorySummaries = new ArrayList<StatusHistorySummary>();
		for (HrEmplStatusHistory currentHrEmplStatusHistory : currentHrEmplStatusHistories) {
			// first create the status history summary and add it to our return
			StatusHistorySummary currStatusHistorySummary = new StatusHistorySummary();
			currStatusHistorySummary.statusId = currentHrEmplStatusHistory.getHrEmployeeStatus().getStatusId();
			currStatusHistorySummary.startDate = currentHrEmplStatusHistory.getEffectiveDate();
			currStatusHistorySummary.benefitType = currentHrEmplStatusHistory.getHrEmployeeStatus().getBenefitType();
			statusHistorySummaries.add(currStatusHistorySummary);

			// check if we have at least one previous summary that has a different start date than the current we are processing
			if (prevStatusHistorySummaries.size() > 0 && prevStatusHistorySummaries.get(0).startDate != currStatusHistorySummary.startDate) {
				// we do, so set all previous summaries' final dates as the day before current's start date
				for (StatusHistorySummary prevStatusHistorySummary : prevStatusHistorySummaries)
					prevStatusHistorySummary.finalDate = currStatusHistorySummary.startDate - 1;

				// clear previous summaries
				prevStatusHistorySummaries.clear();
			}

			// add current as a previous status history summary
			prevStatusHistorySummaries.add(currStatusHistorySummary);
		}

		return statusHistorySummaries;
	}

	/**
	 * Checks a benefit against the first status summary to determine if the
	 * start date is invalid
	 *
	 * @param bBenefitJoin BHRBenefitJoin to check
	 * @param statusHistorySummary StatusHistorySummary to check
	 * @param adjustingStatus true if adjusting status, false if adjusting
	 * benefit
	 * @param messageOnly true if a message should be returned on what will
	 * happen, false if it should take action instead
	 * @return if messageOnly is true the message of what will happen/the error
	 * or null if no message (no action will need to be taken or no error)
	 */
	protected String checkBenefitAgainstFirstStatus(BHRBenefitJoin bBenefitJoin, StatusHistorySummary statusHistorySummary, Boolean adjustingStatus, Boolean messageOnly) {
		int benefitStartDate;
		int benefitFinalDate;
		String message = null;

		// first get start/end dates ... check join type
		if (bBenefitJoin.isPolicyBenefitJoin()) {
			// policy benefit join which means whole policy will be affected, so use policy start date/end date as
			benefitStartDate = bBenefitJoin.getPolicyStartDate();
			benefitFinalDate = bBenefitJoin.getPolicyEndDate();
		} else {
			// dependent benefit join which means only this join will be affected, so for start date we need to use
			// max of policy start date or coverage start date (this is because the coverage start date can currently
			// be before the policy start date -how we are currently tracking contiguos coverage- or after the policy
			// start date -enrolled later on during policy-)
			benefitStartDate = Math.max(bBenefitJoin.getCoverageStartDate(), bBenefitJoin.getPolicyStartDate());
			benefitFinalDate = bBenefitJoin.getCoverageEndDate();
		}

		// now check if the start date is prior to the first status
		if (benefitStartDate < statusHistorySummary.startDate)
			// it is, check if the person was an active dependent of someone during this time as that would be the only
			// case where this benefit would be valid (dependent carrying own benefits - COBRA)
			if (adjustingStatus) {
				message = this.buildActionMessage(bBenefitJoin, statusHistorySummary, benefitStartDate, benefitFinalDate, 3);

				if (!messageOnly) {
					int newBenefitStartDate = statusHistorySummary.startDate;

					bBenefitJoin.adjustStartDate(newBenefitStartDate, true);
				}
			} else
				message = this.buildErrorMessage(bBenefitJoin, statusHistorySummary, benefitStartDate, benefitFinalDate, false);

		return message;
	}

	/**
	 * Checks a benefit against a status summary to determine, in the case of a
	 * date overlap, if it is invalid.
	 *
	 * @param bBenefitJoin BHRBenefitJoin to check
	 * @param statusHistorySummary StatusHistorySummary to check
	 * @param adjustingStatus true if adjusting status, false if adjusting
	 * benefit
	 * @param messageOnly true if a message should be returned on what will
	 * happen, false if it should take action instead
	 * @return if messageOnly is true the message of what will happen/the error
	 * or null if no message (no action will need to be taken or no error)
	 */
	protected String checkBenefitAgainstStatus(BHRBenefitJoin bBenefitJoin, StatusHistorySummary statusHistorySummary, Boolean adjustingStatus, Boolean messageOnly) {
		int benefitStartDate;
		int benefitFinalDate;

		BHREmployeeStatus bStatus = new BHREmployeeStatus(statusHistorySummary.statusId);

		// skip unapproved benefits
		if (!bBenefitJoin.getBenefitApproved())
			return null;

		// skip declines
		if (bBenefitJoin.isDecline())
			return null;

		// check if status's benefit type allows this benefit and skip if it does
		if ((statusHistorySummary.benefitType == 'B' && !bBenefitJoin.getUsingCOBRA())
				|| (statusHistorySummary.benefitType == 'C' && bBenefitJoin.getUsingCOBRA()))
			return null;

		// benefit type does not match this status, so see if the benefit coverage exists during this status

		// first get start/end dates ... check join type
		if (bBenefitJoin.isPolicyBenefitJoin()) {
			// policy benefit join which means whole policy will be affected, so use policy start date/end date as
			benefitStartDate = bBenefitJoin.getPolicyStartDate();
			benefitFinalDate = bBenefitJoin.getPolicyEndDate();
		} else {
			// dependent benefit join which means only this join will be affected, so for start date we need to use
			// max of policy start date or coverage start date (this is because the coverage start date can currently
			// be before the policy start date -how we are currently tracking contiguos coverage- or after the policy
			// start date -enrolled later on during policy-)
			benefitStartDate = Math.max(bBenefitJoin.getCoverageStartDate(), bBenefitJoin.getPolicyStartDate());
			benefitFinalDate = bBenefitJoin.getCoverageEndDate();
		}

		// second compare the dates

		// Type 1 Check - When adjusting status, term benefit as of day before start of status
		//     Cases:              Case 1      Case 2
		//     Status Dates:        x x         x
		//     Benefit Dates:      x           x
		if (benefitFinalDate == 0 && benefitStartDate < statusHistorySummary.startDate) {
			String message;

			if (adjustingStatus) {
				message = this.buildActionMessage(bBenefitJoin, statusHistorySummary, benefitStartDate, benefitFinalDate, 0);

				if (!messageOnly) {
					int newBenefitFinalDate = statusHistorySummary.startDate;

					if (!bStatus.getDateType().equals("S"))
						newBenefitFinalDate = DateUtils.addDays(statusHistorySummary.startDate, -1);

					bBenefitJoin.terminate(newBenefitFinalDate);
				}
			} else
				message = this.buildErrorMessage(bBenefitJoin, statusHistorySummary, benefitStartDate, benefitFinalDate, true);

			return message;
		}

		// Type 2 Check - When adjusting status, unassign benefit
		//     Cases:              Case 1      Case 2
		//     Status Dates:       x           x
		//     Benefit Dates:       x x         x
		if (statusHistorySummary.finalDate == 0 && benefitStartDate > statusHistorySummary.startDate) {
			String message;

			if (adjustingStatus) {
				message = this.buildActionMessage(bBenefitJoin, statusHistorySummary, benefitStartDate, benefitFinalDate, 1);

				if (!messageOnly) {
					bBenefitJoin.setCoverageEndDate(getLastStatusDate());
					bBenefitJoin.setPolicyEndDate(getLastStatusDate());
					bBenefitJoin.update();
					bBenefitJoin.delete();
				}
			} else
				message = this.buildErrorMessage(bBenefitJoin, statusHistorySummary, benefitStartDate, benefitFinalDate, true);

			return message;
		}

		// Type 3 Check - When adjusting status, unassign benefit
		//     Cases:              Case 1      Case 2      Case 3
		//     Status Dates:       x           x           x x
		//     Benefit Dates:      x x         x           x
		if (benefitStartDate == statusHistorySummary.startDate) {
			String message;

			if (adjustingStatus) {
				message = this.buildActionMessage(bBenefitJoin, statusHistorySummary, benefitStartDate, benefitFinalDate, 1);

				if (!messageOnly) {
					bBenefitJoin.setCoverageEndDate(getLastStatusDate());
					bBenefitJoin.setPolicyEndDate(getLastStatusDate());
					bBenefitJoin.update();
					bBenefitJoin.delete();
				}
			} else
				message = this.buildErrorMessage(bBenefitJoin, statusHistorySummary, benefitStartDate, benefitFinalDate, true);

			return message;
		}

		// Type 4 Check - When adjusting status, unassign benefit
		//     Cases:              Case 1      Case 2      Case 3      Case 4      Case 5      Case 6
		//     Status Dates:       x x         x x         x x         x x         x   x       x  x
		//     Benefit Dates:       x x          x x        x            x          x x           x x
		if (statusHistorySummary.finalDate != 0 && benefitStartDate > statusHistorySummary.startDate && benefitStartDate <= statusHistorySummary.finalDate) {
			String message;

			if (adjustingStatus) {
				message = this.buildActionMessage(bBenefitJoin, statusHistorySummary, benefitStartDate, benefitFinalDate, 1);

				if (!messageOnly) {
					bBenefitJoin.setCoverageEndDate(getLastStatusDate());
					bBenefitJoin.setPolicyEndDate(getLastStatusDate());
					bBenefitJoin.update();
					bBenefitJoin.delete();
				}
			} else
				message = this.buildErrorMessage(bBenefitJoin, statusHistorySummary, benefitStartDate, benefitFinalDate, true);

			return message;
		}

		// Type 5 Check - When adjusting status, move benefit end date back to day before start of status
		//     Cases:              Case 1      Case 2      Case 3      Case 4      Case 5      Case 6
		//     Status Dates:        x x          x x        x            x          x x          x x
		//     Benefit Dates:      x x         x x         x x         x x         x   x       x   x
		if (benefitFinalDate != 0 && benefitStartDate < statusHistorySummary.startDate && benefitFinalDate >= statusHistorySummary.startDate) {
			String message;

			if (adjustingStatus) {
				message = this.buildActionMessage(bBenefitJoin, statusHistorySummary, benefitStartDate, benefitFinalDate, 2);

				if (!messageOnly) {
					int newBenefitFinalDate = statusHistorySummary.startDate;

					if (!bStatus.getDateType().equals("S"))
						newBenefitFinalDate = DateUtils.addDays(statusHistorySummary.startDate, -1);
					bBenefitJoin.terminate(newBenefitFinalDate);
				}
			} else
				message = this.buildErrorMessage(bBenefitJoin, statusHistorySummary, benefitStartDate, benefitFinalDate, true);

			return message;
		}

		return null;
	}

	/**
	 * Builds an action message for what will happen to a benefit based on the
	 * status in which it is defined, if a status change is committed for this
	 * employee.
	 */
	private String buildActionMessage(BHRBenefitJoin bBenefitJoin, StatusHistorySummary statusHistorySummary, int benefitStartDate, int benefitFinalDate, int actionType) {
		String benefitNameForMessage = this.getBenefitNameForMessage(bBenefitJoin);
		StringBuilder message = new StringBuilder();

		BHREmployeeStatus bStatus = new BHREmployeeStatus(statusHistorySummary.statusId);

		message.append("Assigned ");
		message.append(benefitNameForMessage);
		message.append(" with ");

		if (bBenefitJoin.isPolicyBenefitJoin())
			message.append("Policy Start Date of ");
		else
			message.append("Coverage Start Date of ");
		message.append(DateUtils.getDateFormatted(benefitStartDate));

		// check the action type
		if (actionType == 0) { // set benefit final date day before status start date
			int newBenefitFinalDate = statusHistorySummary.startDate;
			if (!bStatus.getDateType().equals("S"))
				newBenefitFinalDate = DateUtils.addDays(statusHistorySummary.startDate, -1);

			if (bBenefitJoin.isPolicyBenefitJoin())
				message.append(" will be given a Policy Final Date of ");
			else
				message.append(" will be given a Coverage Final Date of ");

			message.append(DateUtils.getDateFormatted(newBenefitFinalDate));
			message.append(".");
		} else if (actionType == 1) // unassign benefit
			message.append(" will be unassigned from the current employee.");
		else if (actionType == 2) { // move benefit final date back
			int newBenefitFinalDate = statusHistorySummary.startDate;
			if (!bStatus.getDateType().equals("S"))
				newBenefitFinalDate = DateUtils.addDays(statusHistorySummary.startDate, -1);

			if (bBenefitJoin.isPolicyBenefitJoin())
				message.append(" will have its Policy Final Date changed from ");
			else
				message.append(" will have its Coverage Final Date changed from ");

			message.append(DateUtils.getDateFormatted(benefitFinalDate));
			message.append(" to ");
			message.append(DateUtils.getDateFormatted(newBenefitFinalDate));
			message.append(".");
		} else { // set benefit start date to first status date
			int newBenefitStartDate = statusHistorySummary.startDate;

			if (bBenefitJoin.isPolicyBenefitJoin())
				message.append(" will be given a Policy Start Date of ");
			else
				message.append(" will be given a Coverage Start Date of ");

			message.append(DateUtils.getDateFormatted(newBenefitStartDate));
			message.append(".");
		}

		if (bBenefitJoin.isDependentBenefitJoin()) {
			message.append("  The Policy Owner is ");
			message.append(bBenefitJoin.getProvidedByDisplayName());
			message.append(" (");
			message.append(bBenefitJoin.getProvidedBySSN());
			message.append(").");
		}

		// explain why
		message.append("  The Employee Status of ");
		message.append(new BHREmployeeStatus(statusHistorySummary.statusId).getName());
		message.append(" beginning on ");
		message.append(DateUtils.getDateFormatted(statusHistorySummary.startDate));

		if (statusHistorySummary.finalDate != 0) {
			message.append(" and ending on ");
			message.append(DateUtils.getDateFormatted(statusHistorySummary.finalDate));
		}
		if (actionType == 0 || actionType == 1 || actionType == 2)
			if (statusHistorySummary.benefitType == 'N')
				message.append(" does not allow any Benefits to be assigned to the Employee.");
			else if (statusHistorySummary.benefitType == 'B')
				message.append(" only allows Benefits that are NOT covered under COBRA to be assigned to the Employee.");
			else //(statusHistorySummary.benefitType == 'C')
			
				message.append(" only allows Benefits that are covered under COBRA to be assigned to the Employee.");
		else
			message.append(" is the first Employee Status.  Benefits can not begin before the first Employee Status.");

		return message.toString();
	}

	/**
	 * Builds an error message for a benefit (new/insert) based on the status in
	 * which it is defined.
	 *
	 * @param bBenefitJoin
	 * @param statusHistorySummary
	 * @param benefitStartDate
	 * @param benefitFinalDate
	 * @param typeConflict
	 * @return
	 */
	private String buildErrorMessage(BHRBenefitJoin bBenefitJoin, StatusHistorySummary statusHistorySummary, int benefitStartDate, int benefitFinalDate, boolean typeConflict) {
		String benefitNameForMessage = this.getBenefitNameForMessage(bBenefitJoin);
		StringBuilder message = new StringBuilder();

		message.append("Assigned ");
		message.append(benefitNameForMessage);
		message.append(" is invalid.");

		if (bBenefitJoin.isDependentBenefitJoin()) {
			message.append("  The Policy Owner is ");
			message.append(bBenefitJoin.getProvidedByDisplayName());
			message.append(" (");
			message.append(bBenefitJoin.getProvidedBySSN());
			message.append(").");
		}

		// explain why
		message.append("  The Employee Status of ");
		message.append(new BHREmployeeStatus(statusHistorySummary.statusId).getName());
		message.append(" beginning on ");
		message.append(DateUtils.getDateFormatted(statusHistorySummary.startDate));

		if (statusHistorySummary.finalDate != 0) {
			message.append(" and ending on ");
			message.append(DateUtils.getDateFormatted(statusHistorySummary.finalDate));
		}

		if (typeConflict)
			if (statusHistorySummary.benefitType == 'N')
				message.append(" does not allow any Benefits to be assigned to the Employee.");
			else if (statusHistorySummary.benefitType == 'B')
				message.append(" only allows Benefits that are NOT covered under COBRA to be assigned to the Employee.");
			else //(statusHistorySummary.benefitType == 'C')
			
				message.append(" only allows Benefits that are covered under COBRA to be assigned to the Employee.");
		else
			message.append(" is the first Employee Status.  Benefits can not begin before the first Employee Status.");

		return message.toString();
	}

	private String getBenefitNameForMessage(BHRBenefitJoin bBenefitJoin) {
		if (bBenefitJoin.isBenefitCategoryDecline())
			return "Decline for Benefit Category " + bBenefitJoin.getBenefitCategoryName();
		else if (bBenefitJoin.isBenefitDecline())
			return "Decline for Benefit " + bBenefitJoin.getBenefitName();
		else
			return "Benefit " + bBenefitJoin.getBenefitConfig().getConfigName();
	}

	/**
	 * A location is the first org group they are in, under their main company
	 * org group
	 *
	 */
	public String getLocationId() {
		try {
			BOrgGroup bOrgGroup = new BOrgGroup(employee.getOrgGroupAssociations().iterator().next().getOrgGroup());
			return bOrgGroup.getLocation().getOrgGroupId();
		} catch (Exception e) {
			return "";
		}
	}

	public String getLocationName() {
		try {
			BOrgGroup bOrgGroup = new BOrgGroup(employee.getOrgGroupAssociations().iterator().next().getOrgGroup());
			return bOrgGroup.getLocation().getName();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * @param firstName
	 * @param lastName
	 * @param ssn
	 * @param cap
	 * @return
	 * @throws ArahantException
	 */
	public static BPerson[] searchRetirees(final String firstName, final String lastName, final String ssn, final int cap) throws ArahantException {
		HibernateCriteriaUtil<Employee> hcu = ArahantSession.getHSU().createCriteria(Employee.class).like(Employee.FNAME, firstName).like(Employee.LNAME, lastName).like(Employee.SSN, ssn).ne(Employee.FNAME, ArahantSession.systemName()).setMaxResults(cap).orderBy(Employee.LNAME).orderBy(Employee.FNAME);
		//		.list());

		hcu.employeeCurrentStatus("00001-0000000002");  //TODO: wmco specific - move to rules

		return BEmployee.makeArray(hcu.list());
	}

	public static BPerson[] searchLOAs(final String firstName, final String lastName, final String ssn, final int cap) throws ArahantException {
		HibernateCriteriaUtil<Employee> hcu = ArahantSession.getHSU().createCriteria(Employee.class).like(Employee.FNAME, firstName).like(Employee.LNAME, lastName).like(Employee.SSN, ssn).ne(Employee.FNAME, ArahantSession.systemName()).setMaxResults(cap).orderBy(Employee.LNAME).orderBy(Employee.FNAME);
		//		.list());

		hcu.employeeCurrentStatus("00001-0000000003");  //TODO: wmco specific - move to rules

		return BEmployee.makeArray(hcu.list());
	}

	public static BSearchOutput<BEmployee> search(BSearchMetaInput bSearchMetaInput, String firstName, String lastName) {
		BSearchOutput<BEmployee> bSearchOutput = new BSearchOutput<BEmployee>(bSearchMetaInput);
		HibernateCriteriaUtil<Employee> hcu = search(firstName, lastName, bSearchMetaInput.getSortType(), bSearchMetaInput.isSortAsc(), true);

		HibernateScrollUtil<Employee> employees;
		if (bSearchMetaInput.isUsingPaging())
			employees = hcu.getPage(bSearchMetaInput.getPagingFirstItemIndex(), bSearchMetaInput.getItemsPerPage());
		else
			employees = hcu.scroll();

		// set output
		bSearchOutput.setItems(makeArray(employees));
		bSearchOutput.setSortAsc(bSearchMetaInput.isSortAsc());
		bSearchOutput.setSortType((bSearchMetaInput.getSortType() >= 2 && bSearchMetaInput.getSortType() <= 3) ? bSearchMetaInput.getSortType() : 1);
		if (bSearchMetaInput.isUsingPaging()) {
			hcu = search(firstName, lastName, bSearchMetaInput.getSortType(), bSearchMetaInput.isSortAsc(), false);

			int totalRecords = hcu.count(Person.LNAME);

			// TODO - THIS IS A PROBLEM - WE NEED A WAY TO DETERMINE WHAT THE ACTUAL START INDEX IS
			// this is the record to start from if using paging, but note that it may be asking for an
			// index that is now out of bounds in which case it must back up to earlier page boundary
			// (e.g. 50 items per page, was initially 102 items -> asking for first index of 100, but
			// now only 98 items o should return first index of 50)
			bSearchOutput.setPagingFirstItemIndex(bSearchMetaInput.getPagingFirstItemIndex());
			bSearchOutput.setTotalItemsPaging(totalRecords);
			bSearchOutput.setItemsPerPage(bSearchMetaInput.getItemsPerPage());
		}

		return bSearchOutput;
	}

	private static HibernateCriteriaUtil<Employee> search(String firstName, String lastName, int sortType, boolean sortAsc, boolean includeSorting) {
		HibernateCriteriaUtil<Employee> hcu = ArahantSession.getHSU().createCriteria(Employee.class).like(Person.FNAME, firstName).like(Person.LNAME, lastName).ne(Person.FNAME, ArahantSession.systemName());

		if (includeSorting)
			// establish sort
			switch (sortType) {
				default: // last name
					if (sortAsc)
						hcu.orderBy(Person.LNAME);
					else
						hcu.orderByDesc(Person.LNAME);
					break;
				case 2: // first name
					if (sortAsc)
						hcu.orderBy(Person.FNAME);
					else
						hcu.orderByDesc(Person.FNAME);
					break;
				case 3: // middle name
					if (sortAsc)
						hcu.orderBy(Person.MNAME);
					else
						hcu.orderByDesc(Person.MNAME);
					break;
			}

		return hcu;
	}

	public String getTaxState() {
		return employee.getTaxState();
	}

	public String getUnemploymentState() {
		return employee.getUnemploymentState();
	}

//    public static String getBirthdayListReport(int month, String[] statusIds) {
//        return new BirthdayListReport().build(month, statusIds);
//    }
	public static String getBirthdayListReport(int dateFrom, int dateTo, int month, String[] statusIds) {
		return new BirthdayListReport().build(dateFrom, dateTo, month, statusIds);
	}

	public String getSeniority(int before) {
		try {

			String query = "select distinct emp, h.effectiveDate from Employee emp join emp.hrEmplStatusHistories h ";


			String where = "where h.effectiveDate=(select min(h2.effectiveDate) from HrEmplStatusHistory h2 where h2.employee=emp"
					+ " and (h2.effectiveDate>(select max(h5.effectiveDate) from HrEmplStatusHistory h5 where h5.employee=emp"
					+ " and h5.hrEmployeeStatus.active='N' and h5.effectiveDate<" + before + ")"
					+ " or null=(select max(h6.effectiveDate) from HrEmplStatusHistory h6 where h6.employee=emp and h6.hrEmployeeStatus.active='N'"
					+ " and h6.effectiveDate<" + before + " ))) "
					+ " and emp.personId='" + getPersonId() + "' ";

			Query q = ArahantSession.getHSU().createQuery(query + where);

			ScrollableResults s = q.scroll();
			String seniority = "0y, 0m";

			if (s.next()) {

				int employeeHireDate = (Integer) s.get(1);

				Calendar toCalendar = Calendar.getInstance();
				int yearsOfService = (int) DateUtils.getYearsBetween(employeeHireDate, DateUtils.getDate(toCalendar));
				seniority = yearsOfService + "y";


				Calendar fromCalendar = DateUtils.getCalendar(employeeHireDate);
				fromCalendar.add(Calendar.YEAR, yearsOfService);

				int monthsOfService = (int) DateUtils.getMonthsBetween(DateUtils.getDate(fromCalendar), DateUtils.getDate(toCalendar));
				seniority += ", " + monthsOfService + "m";
			}
			return seniority;
		} catch (Exception e) {
			return "0y, 0m";
		}
	}

	public static String getBenefitsString(String personId) {
		StringBuilder benefits = new StringBuilder("\n\n");
		BEmployee bemp = new BEmployee(personId);
		ArahantSession.setCalcDate(DateUtils.now());
		for (BHRBenefitJoin bj : bemp.listBenefitConfigs(true, false, false)) {
			benefits.append("Benefit: ").append(bj.getBenefitName()).append("\n");
			benefits.append("Configuration: ").append(bj.getBenefitConfigName()).append("\n");
			benefits.append("Policy Start: ").append(DateUtils.getDateFormatted(bj.getPolicyStartDate())).append("\n");
			benefits.append("Policy End: ").append(DateUtils.getDateFormatted(bj.getPolicyEndDate())).append("\n");
			benefits.append("Coverage Start: ").append(DateUtils.getDateFormatted(bj.getCoverageStartDate())).append("\n");
			benefits.append("Coverage End: ").append(DateUtils.getDateFormatted(bj.getCoverageEndDate())).append("\n");
			benefits.append("Amount Covered: ").append(MoneyUtils.formatMoney(bj.getAmountCovered())).append("\n");
			benefits.append("Amount Paid: ").append(MoneyUtils.formatMoney(bj.getAmountPaid())).append("\n");
			benefits.append("\n");
		}

		return benefits.toString();
	}

	public static String getServiceListReport(int month) {
		return new ServiceListReport().build(month);
	}

	public BOrgGroup[] listNotInOrgGroups(String orgId, int cap) {

		HibernateCriteriaUtil<OrgGroup> hcu = ArahantSession.getHSU().createCriteria(OrgGroup.class).orderBy(OrgGroup.NAME).setMaxResults(cap);

		if (!isEmpty(orgId))
			//BOrgGroup borg=new BOrgGroup(orgId);
			//List<String> ids=borg.getAllOrgGroupsInHierarchy();
			//	hcu.in(OrgGroup.ORGGROUPID, ids);
			hcu.joinTo(OrgGroup.ORGGROUPHIERARCHIESFORCHILDGROUPID).eq(OrgGroupHierarchy.PARENT_ID, orgId);
		else
			//Removed for Drury Group
			//	hcu.sizeEq(OrgGroup.ORGGROUPHIERARCHIESFORCHILDGROUPID, 0);
			hcu.eq(OrgGroup.ORGGROUPTYPE, COMPANY_TYPE);
		List<String> ogas = new LinkedList<String>();

		for (OrgGroupAssociation oga : employee.getOrgGroupAssociations())
			ogas.add(oga.getOrgGroupId());

		hcu.notIn(OrgGroup.ORGGROUPID, ogas);

		return BOrgGroup.makeArray(hcu.list());
	}

	public BPerson getCurrentSpouse() {
		Person p = ArahantSession.getHSU().createCriteria(Person.class).joinTo(Person.DEP_JOINS_AS_DEPENDENT).geOrEq(HrEmplDependent.DATE_INACTIVE, DateUtils.now(), 0).eq(HrEmplDependent.RELATIONSHIP_TYPE, HrEmplDependent.TYPE_SPOUSE).eq(HrEmplDependent.EMPLOYEE, employee).first();

		if (p == null)
			return null;

		return new BPerson(p);
	}

	public static int getPPY(String personId) {
		try {
			BPerson bp = new BPerson(personId);
			if (bp.isEmployee())
				return bp.getBEmployee().getPayPeriodsPerYear();
		} catch (Throwable e) {
			logger.error(e);
		}
		return 12;  //default to monthly billing
	}

	public static int getPPY(String personId, int date) {
		try {
			BPerson bp = new BPerson(personId);

			if (bp.isEmployee()) {
				BHREmplStatusHistory bhist = bp.getBEmployee().getLastStatusHistoryBefore(date);
				if (bhist != null && bhist.getHrEmployeeStatus().getActive() == 'N')
					return 12; //retired or cobra or something where its monthly
				return bp.getBEmployee().getPayPeriodsPerYear();
			}
		} catch (Throwable e) {
			logger.error(e);
		}
		return 12;  //default to monthly billing
	}

	public static int getPPY(Person p) {
		try {
			BPerson bp = new BPerson(p);
			if (bp.isEmployee())
				return bp.getBEmployee().getPayPeriodsPerYear();
		} catch (Exception e) {
			logger.error(e);
		}
		return 12;  //default to monthly billing
	}

	public BCompany[] searchCompaniesNotIn(final String name, final int max) {
		List<String> excludeIds = new LinkedList<String>();
		excludeIds.add(employee.getCompanyBase().getOrgGroupId());

		for (OrgGroupAssociation oga : employee.getOrgGroupAssociations())
			excludeIds.add(oga.getOrgGroup().getOwningCompany().getOrgGroupId());

		final HibernateCriteriaUtil<CompanyDetail> hcu = ArahantSession.getHSU().createCriteriaNoCompanyFilter(CompanyDetail.class).orderBy(CompanyDetail.NAME).like(CompanyDetail.NAME, name).notIn(CompanyDetail.ORGGROUPID, excludeIds).setMaxResults(max);

		return BCompany.makeArray(hcu.list());
	}

	public char getAutoLogTime() {
		return employee.getAutoLogTime();
	}

	public void setAutoLogTime(char autoLogTime) {
		employee.setAutoLogTime(autoLogTime);
	}

	public char getMedicare() {
		return employee.getMedicare();
	}

	public void setMedicare(char medicare) {
		employee.setMedicare(medicare);
	}

	public char getHrAdmin() {
		return employee.getHrAdmin();
	}

	public void setHrAdmin(char hrAdmin) {
		employee.setHrAdmin(hrAdmin);
	}

	public String getBenefitWizardStatus() {
		return employee.getBenefitWizardStatus() + "";
	}

	public void setBenefitWizardStatus(String value) {
		if (isEmpty(value))
			employee.setBenefitWizardStatus('N');
		else
			employee.setBenefitWizardStatus(value.charAt(0));
		setBenefitWizardDate(DateUtils.now());
	}

	public int getBenefitWizardDate() {
		return employee.getBenefitWizardDate();
	}

	public void setBenefitWizardDate(int value) {
		employee.setBenefitWizardDate(value);
	}

	public short getHoursPerWeek() {
		return employee.getHours_per_week();
	}
	
	public void setHoursPerWeek(short hpw) {
		employee.setHours_per_week(hpw);
	}
	
	/**
	 * Untested and unused code (written by Blake) to update the employee status fields in the employee record.
	 * 
	 * The problem is that storing only one status in the employee record is only applicable to one date.
	 * It may be accurate at one point in time but there may be a termination set for a future date.
	 * The employee record would have to be updated each time the status history table is changed and
	 * also when the date changes.  Then it'll be an accurate status for that date only.
	 */
	public void updateEmployeeStatus() {
		HrEmplStatusHistory esh = getCurrentStatusHistory();  // status as of TODAY
		if (esh == null)
			return;
		HrEmployeeStatus es = esh.getHrEmployeeStatus();
		if (es == null)
			return;
		setStatus(es);
		setStatusEffectiveDate(esh.getEffectiveDate());
	}

	/**
	 * This routine (taken from services.standard.hr.benefitWizardStatus.BenefitWizardStatusOps.approveChanges())
	 * is used to approve demographic, dependent, and benefit change requests.
	 *
	 */
	public static void approveChanges(String empId, boolean approveDemographics, boolean approveDependents, boolean approveBenefits) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		BEmployee be = new BEmployee(empId);
		be.loadPending(empId);
		be.apply(approveDependents, approveDemographics);
		List<Person> realAndChangePerson = be.getRealAndChangePerson();
		if (approveBenefits) {
			for (HrBenefitJoin join : hsu.createCriteria(HrBenefitJoin.class).eqJoinedField(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON).in(HrBenefitJoin.PAYING_PERSON, realAndChangePerson).eq(HrBenefitJoin.APPROVED, 'N').list()) {
				BHRBenefitJoin ebj = new BHRBenefitJoin(join);
//				empId = ebj.getPayingPersonId();
				HrBenefitJoin oldbj = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'Y').eq(HrBenefitJoin.PAYING_PERSON_ID, ebj.getPayingPersonId()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, ebj.getBean().getHrBenefitConfig()).eq(HrBenefitJoin.HRBENEFIT, ebj.getBean().getHrBenefit()).eq(HrBenefitJoin.HR_BENEFIT_CATEGORY, ebj.getBean().getHrBenefitCategory()).eq(HrBenefitJoin.POLICY_START_DATE, ebj.getPolicyStartDate()).first();

				if (oldbj != null)
					new BHRBenefitJoin(oldbj).delete();

				ebj.setPolicyApproved(true);
			}

			hsu.commitTransaction();
			hsu.beginTransaction();

			//had to commit so we could accurately query for any other unapproved joins
			boolean hasUnapproved = hsu.createCriteria(HrBenefitJoin.class).in(HrBenefitJoin.PAYING_PERSON, be.getRealAndChangePerson()).eq(HrBenefitJoin.APPROVED, 'N').exists();

			hsu.rollbackTransaction();
			hsu.beginTransaction();

			if (!hasUnapproved) {
				be.setBenefitWizardStatus(Employee.BENEFIT_WIZARD_STATUS_PROCESSED + "");
				//be.update();
				hsu.saveOrUpdate(be.getEmployee());
			}
		}
	}

	public EmployeeRate getBillingRate(String rateTypeId) {
        return ArahantSession.getHSU().createCriteria(EmployeeRate.class).eq(EmployeeRate.PERSON, employee).eq(EmployeeRate.RATE_TYPE, new BRateType(rateTypeId).getRateType()).first();
    }

    /**
     * Find all the projects an employee is assigned to during the selected period.
     *
     * @param begDate YYYYMMDD
     * @param endDate  YYYYMMDD
     */
    public List<BProject> getAssignedProjects(int begDate, int endDate) {
	    List<BProject> pl = new ArrayList<>();
        HibernateSessionUtil hsu = ArahantSession.getHSU();
        Connection con = KissConnection.get();
        try {
            List<Record> recs = con.fetchAll("select p.project_id from project p " +
                            "join project_employee_join j " +
                            "  on p.project_id = j.project_id " +
                            "where j.person_id = ? and " +
                            "  p.estimated_first_date <= ? and " +
                            "  p.estimated_last_date >= ?",
                    employee.getPersonId(), begDate, endDate);
            for (Record r : recs)
                pl.add(new BProject(r.getString("project_id")));
        } catch (Exception e) {
            // ignore
        }
	    return pl;
    }

	/**
	 * Apply employee labels that are designated auto-add to new employees.
	 * This will only work _after_ the employee record has been added to a database
	 * (or as part of the same transaction).
	 */
	public void applyLabels() {
		BEmployee.applyLabels(employee.getPersonId());
	}

	/**
	 * Apply employee labels that are designated auto-add to new employees.
	 * This will only work _after_ the employee record has been added to a database
	 * (or as part of the same transaction).
	 */
	public static void applyLabels(String employeeId) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		String supervisorId = hsu.getCurrentPerson().getPersonId();
		Connection db = KissConnection.get();
		int today = DateUtils.today();
		try {
			List<Record> labels = db.fetchAll("select employee_label_id, name from employee_label where auto_add_new_employee = 'Y'");
			for (Record lbl : labels) {
				Record ass = db.newRecord("employee_label_association");
				ass.set("employee_id", employeeId);
				ass.set("employee_label_id", lbl.getString("employee_label_id"));
				ass.set("who_added", supervisorId);
				ass.addRecord();

				BHREmployeeEvent bev = new BHREmployeeEvent();
				bev.create();
				bev.setEmployeeId(employeeId);
				bev.setSupervisorId(supervisorId);
				bev.setEventDate(today);
				bev.setEmployeeNotified('N');
				bev.setEventType('L');
				bev.setSummary(lbl.getString("name") + " Label Added");
				bev.insert();
			}
		} catch (Exception throwables) {
			throw new ArahantException(throwables);
		}
	}

	public static void main(String[] args) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		BEmployee be = new BEmployee("00001-0000000290");
		hsu.beginTransaction();
		hsu.delete(be.getLastStatusHistory().getBean());
		hsu.commitTransaction();
		hsu.beginTransaction();
		BHREmplStatusHistory bhist = new BHREmplStatusHistory();
		bhist.create();
		bhist.setEmployee(be.employee);
		bhist.setHrEmployeeStatus(hsu.get(HrEmployeeStatus.class, "00001-0000000002"));
		bhist.setEffectiveDate(20110901);
		bhist.setNotes("Sam's Test");
		bhist.insert(false);

		ArahantSession.AIEval("(assert (EmployeeStatusChanged \"" + "00001-0000000290" + "\" \"" + "00001-0000000002" + "\" \"" + "20110901" + "\"))");
		ArahantSession.runAI();
	}
	
}
