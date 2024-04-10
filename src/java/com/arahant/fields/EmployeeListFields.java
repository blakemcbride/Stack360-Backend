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


package com.arahant.fields;

import com.arahant.beans.Employee;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.OrgGroup;
import com.arahant.beans.OrgGroupAssociation;
import com.arahant.beans.Person;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.util.List;


public class EmployeeListFields {
	public static final String LAST_NAME = "Last Name";
	public static final String FIRST_NAME = "First Name";
	public static final String MIDDLE_NAME = "Middle Name";
	public static final String NICK_NAME = "Nick Name";
	public static final String SEX = "Sex";
	public static final String SSN = "SSN";
	public static final String EXTERNAL_ID = "External ID";
	public static final String DATE_OF_BIRTH = "Date of Birth";
	public static final String EMAIL = "E-mail";
	public static final String ADDRESS_STREET_1 = "Address - Street 1";
	public static final String ADDRESS_STREET_2 = "Address - Street 2";
	public static final String ADDRESS_CITY = "Address - City";
	public static final String ADDRESS_STATE = "Address - State";
	public static final String ADDRESS_ZIP = "Address - Zip";
	public static final String PHONE_HOME = "Phone - Home";
	public static final String PHONE_WORK = "Phone - Work";
	public static final String PHONE_MOBILE = "Phone - Mobile";
	public static final String PHONE_FAX = "Phone - Fax";
	public static final String JOB_TITLE = "Job Title";
	public static final String EEO_CATEGORY = "EEO Category";
	public static final String EEO_RACE = "EEO Race";
	public static final String WAGE_TYPE_CURRENT = "Wage Type (Current)";
	public static final String POSITION_CURRENT = "Position (Current)";
	public static final String STATUS_CURRENT = "Status (Current)";
	public static final String LOGIN_ID = "Login - ID";
	public static final String LOGIN_SCREEN_GROUP = "Login - Screen Group";
	public static final String LOGIN_SECURITY_GROUP = "Login - Security Group";
	public static final String LOGIN_STATUS = "Login - Status";
	public static final String PAYROLL_PAY_PERIODS_PER_YEAR = "Payroll - Pay Periods/Year";
	public static final String PAYROLL_EXPECTED_HOURS_PER_PAY_PERIOD = "Payroll - Expected Hours/Pay Period";
	public static final String PAYROLL_MARITAL_STATUS = "Payroll - Marital Status";
	public static final String PAYROLL_W4_STATUS = "Payroll - W4 Status";
	public static final String PAYROLL_OVERTIME = "Payroll - Overtime";
	public static final String PAYROLL_EARNED_INCOME_CREDIT = "Payroll - Earned Income Credit";
	public static final String PAYROLL_LOCAL_TAX_CODE = "Payroll - Local Tax Code";
	public static final String PAYROLL_BANK_CODE = "Payroll - Bank Code";
	public static final String PAYROLL_FEDERAL_EXEMPTIONS = "Payroll - Federal Exemptions";
	public static final String PAYROLL_STATE_EXEMPTIONS = "Payroll - State Exemptions";
	public static final String PAYROLL_FEDERAL_EXTRA_WITHHELD = "Payroll - Federal Extra Withheld";
	public static final String PAYROLL_STATE_EXTRA_WITHHELD = "Payroll - State Extra Withheld";
	public static final String PAYROLL_TAX_STATE = "Payroll - Tax State";
	public static final String PAYROLL_UNEMPLOYMENT_STATE = "Payroll - Unemployment State";
	public static final String PAYROLL_ADDITIONAL_FEDERAL_INCOME_TAX_TYPE = "Payroll - Additional Federal Income Tax Type";
	public static final String PAYROLL_ADDITIONAL_FEDERAL_INCOME_TAX_AMOUNT = "Payroll - Additional Federal Income Tax Amount";
	public static final String PAYROLL_ADDITIONAL_STATE_INCOME_TAX_TYPE = "Payroll - Additional State Income Tax Type";
	public static final String PAYROLL_ADDITIONAL_STATE_INCOME_TAX_AMOUNT = "Payroll - Additional State Income Tax Amount";
	public static final String PAYROLL_ADDITIONAL_LOCAL_INCOME_TAX_TYPE = "Payroll - Additional Local Income Tax Type";
	public static final String PAYROLL_ADDITIONAL_LOCAL_INCOME_TAX_AMOUNT = "Payroll - Additional Local Income Tax Amount";
	public static final String PAYROLL_ADDITIONAL_STATE_DISABILITY_TAX_TYPE = "Payroll - Additional State Disability Tax Type";
	public static final String PAYROLL_ADDITIONAL_STATE_DISABILITY_TAX_AMOUNT = "Payroll - Additional State Disability Tax Amount";
	public static final String EMPLOYMENT_ELIGIBILITY_CITIZENSHIP = "Employement Eligibility - Citizen of";
	public static final String EMPLOYMENT_ELIGIBILITY_VISA = "Employement Eligibility - Visa";
	public static final String EMPLOYMENT_ELIGIBILITY_VISA_STATUS_DATE = "Employement Eligibility - Visa Status Date";
	public static final String EMPLOYMENT_ELIGIBILITY_VISA_EXPIRATION_DATE = "Employement Eligibility - Visa Expiration Date";
	public static final String EMPLOYMENT_ELIGIBILITY_COMPLETED = "Employement Eligibility - I9 Form Completed";
	public static final String BENEFITS = "Benefits *";
	public static final String BENEFIT_CLASS = "Benefit Class";
	public static final String ACCRUED_TIME_OFF = "Time Off Accruals *";
	public static final String BENEFIT_SUMMARY = "Benefit Summary *";
	public static final String GARNISHMENTS = "Garnishments *";
	public static final String EFT = "Electronic Funds Transfer *";
	public static final String CHECK_LIST = "Check List *";
	public static final String EVENTS = "Events *";
	public static final String FORMS = "Forms *";
	public static final String NOTES = "Notes *";
	public static final String ORG_GROUP = "Organizational Group *";
	public static final String TRAINING = "Training *";
	public static final String POSITION_DATE = "Position Date (Current)";
	public static final String STATUS_DATE = "Status Date (Current)";
	public static final String HIRE_DATE = "Hire Date";
	public static final String WAGE_DATE = "Wage Date (Current)";
	public static final String WAGE_AMOUNT = "Wage Amount (Current)";
	public static final String WAGE_NOTES = "Wage Notes (Current)";
	public static final String TOBACCO_USE = "Tobacco Use";
	public static final String DRIVERS_LICENSE_NUMBER = "Driver's License - Number";
	public static final String DRIVERS_LICENSE_STATE = "Driver's License - State";
	public static final String DRIVERS_LICENSE_EXPIRES = "Driver's License - Expires";
	public static final String AUTOMOTIVE_INSURANCE_CARRIER = "Automotive Insurance - Carrier";
	public static final String AUTOMOTIVE_INSURANCE_POLICY_NUMBER = "Automotive Insurance - Policy Number";
	public static final String AUTOMOTIVE_INSURANCE_BEGINS = "Automotive Insurance - Begins";
	public static final String AUTOMOTIVE_INSURANCE_EXPIRES = "Automotive Insurance - Expires";
	public static final String AUTOMOTIVE_INSURANCE_COVERAGE = "Automotive Insurance - Coverage";
        public static final String COUNTY = "County";
        public static final String ORG_GROUP1 = "Organization Group Level 1";
        public static final String ORG_GROUP2 = "Organization Group Level 2";
        public static final String ORG_GROUP3 = "Organization Group Level 3";
        public static final String ORG_GROUP4 = "Organization Group Level 4";
        public static final String ORG_GROUP5 = "Organization Group Level 5";
        public static final String WORKERS_COMP = "Worker's Compensation Code";

	
	
	public static final String[] displayIds = new String[] {
		LAST_NAME, FIRST_NAME, MIDDLE_NAME, NICK_NAME, SEX, SSN, EXTERNAL_ID, DATE_OF_BIRTH, EMAIL, ADDRESS_STREET_1, 
		ADDRESS_STREET_2, ADDRESS_CITY, ADDRESS_STATE, ADDRESS_ZIP, PHONE_HOME, PHONE_WORK, PHONE_MOBILE, PHONE_FAX,
		JOB_TITLE, EEO_CATEGORY, EEO_RACE, WAGE_TYPE_CURRENT, POSITION_CURRENT, STATUS_CURRENT,
		LOGIN_ID, LOGIN_SCREEN_GROUP, LOGIN_SECURITY_GROUP, LOGIN_STATUS, PAYROLL_PAY_PERIODS_PER_YEAR, PAYROLL_EXPECTED_HOURS_PER_PAY_PERIOD, 
		PAYROLL_MARITAL_STATUS, PAYROLL_W4_STATUS, PAYROLL_OVERTIME, PAYROLL_EARNED_INCOME_CREDIT, PAYROLL_LOCAL_TAX_CODE,
		PAYROLL_BANK_CODE, PAYROLL_FEDERAL_EXEMPTIONS, PAYROLL_STATE_EXEMPTIONS, PAYROLL_FEDERAL_EXTRA_WITHHELD,
		PAYROLL_STATE_EXTRA_WITHHELD, PAYROLL_TAX_STATE, PAYROLL_UNEMPLOYMENT_STATE, PAYROLL_ADDITIONAL_FEDERAL_INCOME_TAX_TYPE,
		PAYROLL_ADDITIONAL_FEDERAL_INCOME_TAX_AMOUNT, PAYROLL_ADDITIONAL_STATE_INCOME_TAX_TYPE, PAYROLL_ADDITIONAL_STATE_INCOME_TAX_AMOUNT,
		PAYROLL_ADDITIONAL_LOCAL_INCOME_TAX_TYPE, PAYROLL_ADDITIONAL_LOCAL_INCOME_TAX_AMOUNT, PAYROLL_ADDITIONAL_STATE_DISABILITY_TAX_TYPE,
		PAYROLL_ADDITIONAL_STATE_DISABILITY_TAX_AMOUNT, EMPLOYMENT_ELIGIBILITY_CITIZENSHIP, EMPLOYMENT_ELIGIBILITY_VISA,
		EMPLOYMENT_ELIGIBILITY_VISA_STATUS_DATE, EMPLOYMENT_ELIGIBILITY_VISA_EXPIRATION_DATE, EMPLOYMENT_ELIGIBILITY_COMPLETED,BENEFITS, BENEFIT_CLASS,
		ACCRUED_TIME_OFF, GARNISHMENTS, EFT, CHECK_LIST, EVENTS, FORMS, NOTES, ORG_GROUP, TRAINING, POSITION_DATE, STATUS_DATE, HIRE_DATE,
		WAGE_DATE, WAGE_AMOUNT, WAGE_NOTES,BENEFIT_SUMMARY, TOBACCO_USE, DRIVERS_LICENSE_NUMBER, DRIVERS_LICENSE_STATE,
		DRIVERS_LICENSE_EXPIRES, AUTOMOTIVE_INSURANCE_CARRIER, AUTOMOTIVE_INSURANCE_POLICY_NUMBER, AUTOMOTIVE_INSURANCE_BEGINS,
		AUTOMOTIVE_INSURANCE_EXPIRES, AUTOMOTIVE_INSURANCE_COVERAGE,COUNTY,ORG_GROUP1,ORG_GROUP2,ORG_GROUP3,ORG_GROUP4,ORG_GROUP5,WORKERS_COMP
	};
	
	public static HibernateCriteriaUtil<Employee> getHCU(String[] ids, String lastNameFrom, String lastNameTo, int dobFrom, int dobTo, int sortType, boolean sortAsc, int statusType,
			String []orgGroupIds, String []orgGroupCodes, String []configIds) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		HibernateCriteriaUtil<Employee> hcu = hsu.createCriteria(Employee.class);
		
		if (configIds.length>0)
		{
			hcu.joinTo(Person.HR_BENEFIT_JOINS_WHERE_PAYING)
				.dateInside(HrBenefitJoin.POLICY_START_DATE, HrBenefitJoin.POLICY_END_DATE, DateUtils.now())
				.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
				.in(HrBenefitConfig.BENEFIT_CONFIG_ID, configIds);
		}

		if (lastNameFrom != null && lastNameFrom.length() > 0) {
			hcu.ge(Person.LNAME, lastNameFrom);
		}

		if (lastNameTo != null && lastNameTo.length() > 0) {
			char l = lastNameTo.charAt(lastNameTo.length() - 1);
			l++;
			lastNameTo = lastNameTo.substring(0, lastNameTo.length() - 1) + l;
			hcu.le(Person.LNAME, lastNameTo);
		}

		if (orgGroupIds.length>0 || orgGroupCodes.length>0)
		{
			HibernateCriteriaUtil ogaHCU=hcu.joinTo(Person.ORGGROUPASSOCIATIONS);
			if (orgGroupIds.length>0){
				ogaHCU.in(OrgGroupAssociation.ORG_GROUP_ID, orgGroupIds);
                        } else if (orgGroupCodes.length>0)  {
				ogaHCU.joinTo(OrgGroupAssociation.ORGGROUP)
					.in(OrgGroup.EXTERNAL_REF, orgGroupCodes);
                        }
		}

		hcu.ge(Person.DOB, dobFrom);

		if (dobTo != 0) {
			hcu.le(Person.DOB, dobTo);
		}

		switch (statusType) {
			case 0: hcu.isEmployee();
					break;
			case 1: hcu.activeEmployee();
					break;
			case 2: hcu.inactiveEmployee();
					break;
		}

		switch (sortType) {
			case 0:
				if (sortAsc) {
					hcu.orderBy(Person.LNAME);
					hcu.orderBy(Person.FNAME);
				} else {
					hcu.orderByDesc(Person.LNAME);
					hcu.orderByDesc(Person.FNAME);
				}
				break;
			case 1:
				hcu = sortAsc ? hcu.orderBy(Person.SSN) : hcu.orderByDesc(Person.SSN);
				break;

			case 2:
				hcu = sortAsc ? hcu.orderBy(Person.DOB) : hcu.orderByDesc(Person.DOB);
				break;
		}

		
		
		return hcu;
	}
}

	
