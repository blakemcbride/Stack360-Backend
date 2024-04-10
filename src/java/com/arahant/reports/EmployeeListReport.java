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


package com.arahant.reports;

import com.arahant.beans.ElectronicFundTransfer;
import com.arahant.beans.Employee;
import com.arahant.beans.Garnishment;
import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitCategory;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.HrChecklistDetail;
import com.arahant.beans.HrChecklistItem;
import com.arahant.beans.HrEmployeeEvent;
import com.arahant.beans.HrNoteCategory;
import com.arahant.beans.HrTrainingCategory;
import com.arahant.beans.HrTrainingDetail;
import com.arahant.beans.OrgGroup;
import com.arahant.beans.OrgGroupAssociation;
import java.io.FileNotFoundException;

import com.arahant.beans.PersonForm;
import com.arahant.beans.PersonNote;
import com.arahant.business.BEmployee;
import com.arahant.exceptions.ArahantException;
import com.arahant.fields.EmployeeListFields;
import com.arahant.services.standard.hr.employeeListReport.ListHierarchyInBreathFirstSearch;
import com.arahant.utils.*;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.List;
import java.util.Set;

public class EmployeeListReport extends ReportBase {

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
    private List<String> orgList = null;

    public EmployeeListReport(String[] ids, String lastNameFrom, String lastNameTo, int dobFrom, int dobTo, int sortType, boolean sortAsc, int statusType, String[] orgGroupIds, String[] orgGroupCodes, String[] configIds) throws ArahantException {
        super("EmpList", "Employee List");

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

    public String build() throws FileNotFoundException, DocumentException {
        try {
            HibernateCriteriaUtil<Employee> hcu = EmployeeListFields.getHCU(ids, lastNameFrom, lastNameTo, dobFrom, dobTo, sortType, sortAsc, statusType, orgGroupIds, orgGroupIds, configIds);
            HibernateScrollUtil<Employee> scrollUtil = hcu.scroll();
            PdfPTable table = makeTable(new int[]{30, 70});
            int recordIdx = 1;

            addHeaderLine();
            String lastId = "";

            while (scrollUtil.next()) {
                BEmployee employee = new BEmployee(scrollUtil.get());
                orgList = null; //reset everytime we get new employee
                if (employee.getPersonId().equals(lastId)) {
                    continue;
                }

                lastId = employee.getPersonId();

                writeColHeaderBold(table, "Employee " + recordIdx, Element.ALIGN_LEFT, 10.0f);
                writeColHeaderBold(table, " ", Element.ALIGN_LEFT, 10.0f);

                for (String id : ids) {
                    if (id.equals(EmployeeListFields.WORKERS_COMP)){
                        write(table, EmployeeListFields.WORKERS_COMP ,employee.getWorkersCompCode());
                    }

                    else if (id.equals(EmployeeListFields.LAST_NAME)) {
                        writeBoldRight(table, EmployeeListFields.LAST_NAME, 10.0f);
                        write(table, employee.getLastName());
                    } else if (id.equals(EmployeeListFields.FIRST_NAME)) {
                        writeBoldRight(table, EmployeeListFields.FIRST_NAME, 10.0f);
                        write(table, employee.getFirstName());
                    } else if (id.equals(EmployeeListFields.MIDDLE_NAME)) {
                        writeBoldRight(table, EmployeeListFields.MIDDLE_NAME, 10.0f);
                        write(table, employee.getMiddleName());
                    } else if (id.equals(EmployeeListFields.NICK_NAME)) {
                        writeBoldRight(table, EmployeeListFields.NICK_NAME, 10.0f);
                        write(table, employee.getNickName());
                    } else if (id.equals(EmployeeListFields.SEX)) {
                        writeBoldRight(table, EmployeeListFields.SEX, 10.0f);
                        write(table, employee.getSex().equals("F") ? "Female" : "Male");
                    } else if (id.equals(EmployeeListFields.SSN)) {
                        writeBoldRight(table, EmployeeListFields.SSN, 10.0f);
                        write(table, employee.getSsn());
                    } else if (id.equals(EmployeeListFields.EXTERNAL_ID)) {
                        writeBoldRight(table, EmployeeListFields.EXTERNAL_ID, 10.0f);
                        write(table, employee.getExtRef());
                    } else if (id.equals(EmployeeListFields.DATE_OF_BIRTH)) {
                        writeBoldRight(table, EmployeeListFields.DATE_OF_BIRTH, 10.0f);
                        write(table, DateUtils.getDateFormatted(employee.getDob()));
                    } else if (id.equals(EmployeeListFields.EMAIL)) {
                        writeBoldRight(table, EmployeeListFields.EMAIL, 10.0f);
                        write(table, employee.getPersonalEmail());
                    } else if (id.equals(EmployeeListFields.ADDRESS_STREET_1)) {
                        writeBoldRight(table, EmployeeListFields.ADDRESS_STREET_1, 10.0f);
                        write(table, employee.getStreet());
                    } else if (id.equals(EmployeeListFields.ADDRESS_STREET_2)) {
                        writeBoldRight(table, EmployeeListFields.ADDRESS_STREET_2, 10.0f);
                        write(table, employee.getStreet2());
                    } else if (id.equals(EmployeeListFields.ADDRESS_CITY)) {
                        writeBoldRight(table, EmployeeListFields.ADDRESS_CITY, 10.0f);
                        write(table, employee.getCity());
                    } else if (id.equals(EmployeeListFields.ADDRESS_STATE)) {
                        writeBoldRight(table, EmployeeListFields.ADDRESS_STATE, 10.0f);
                        write(table, employee.getState());
                    } else if (id.equals(EmployeeListFields.ADDRESS_ZIP)) {
                        writeBoldRight(table, EmployeeListFields.ADDRESS_ZIP, 10.0f);
                        write(table, employee.getZip());
                    } else if (id.equals(EmployeeListFields.PHONE_HOME)) {
                        writeBoldRight(table, EmployeeListFields.PHONE_HOME, 10.0f);
                        write(table, employee.getHomePhone());
                    } else if (id.equals(EmployeeListFields.PHONE_WORK)) {
                        writeBoldRight(table, EmployeeListFields.PHONE_WORK, 10.0f);
                        write(table, employee.getWorkPhoneNumber());
                    } else if (id.equals(EmployeeListFields.PHONE_MOBILE)) {
                        writeBoldRight(table, EmployeeListFields.PHONE_MOBILE, 10.0f);
                        write(table, employee.getMobilePhone());
                    } else if (id.equals(EmployeeListFields.PHONE_FAX)) {
                        writeBoldRight(table, EmployeeListFields.PHONE_FAX, 10.0f);
                        write(table, employee.getWorkFaxNumber());
                    } else if (id.equals(EmployeeListFields.JOB_TITLE)) {
                        writeBoldRight(table, EmployeeListFields.JOB_TITLE, 10.0f);
                        write(table, employee.getJobTitle());
                    } else if (id.equals(EmployeeListFields.EEO_CATEGORY)) {
                        writeBoldRight(table, EmployeeListFields.EEO_CATEGORY, 10.0f);
                        write(table, employee.getEEOCategory());
                    } else if (id.equals(EmployeeListFields.EEO_RACE)) {
                        writeBoldRight(table, EmployeeListFields.EEO_RACE, 10.0f);
                        write(table, employee.getEEORace());
                    } else if (id.equals(EmployeeListFields.WAGE_TYPE_CURRENT)) {
                        writeBoldRight(table, EmployeeListFields.WAGE_TYPE_CURRENT, 10.0f);
                        write(table, employee.getWageTypeName());
                    } else if (id.equals(EmployeeListFields.POSITION_CURRENT)) {
                        writeBoldRight(table, EmployeeListFields.POSITION_CURRENT, 10.0f);
                        write(table, employee.getPositionName());
                    } else if (id.equals(EmployeeListFields.STATUS_CURRENT)) {
                        writeBoldRight(table, EmployeeListFields.STATUS_CURRENT, 10.0f);
                        write(table, employee.getStatus());
                    } else if (id.equals(EmployeeListFields.LOGIN_ID)) {
                        writeBoldRight(table, EmployeeListFields.LOGIN_ID, 10.0f);
                        write(table, employee.getUserLogin());
                    } else if (id.equals(EmployeeListFields.LOGIN_SCREEN_GROUP)) {
                        writeBoldRight(table, EmployeeListFields.LOGIN_SCREEN_GROUP, 10.0f);
                        write(table, employee.getScreenGroupName());
                    } else if (id.equals(EmployeeListFields.LOGIN_SECURITY_GROUP)) {
                        writeBoldRight(table, EmployeeListFields.LOGIN_SECURITY_GROUP, 10.0f);
                        write(table, employee.getSecurityGroupName());
                    } else if (id.equals(EmployeeListFields.LOGIN_STATUS)) {
                        writeBoldRight(table, EmployeeListFields.LOGIN_STATUS, 10.0f);
                        write(table, employee.canLogin() ? "Active" : "Inactive");
					} else if (id.equals(EmployeeListFields.BENEFIT_CLASS)) {
					   writeBoldRight(table, EmployeeListFields.BENEFIT_CLASS, 10.0f);
					   write(table, employee.getBenefitClass() != null ? employee.getBenefitClass().getName() : "(None Assigned)");
					} else if (id.equals(EmployeeListFields.PAYROLL_PAY_PERIODS_PER_YEAR)) {
                        writeBoldRight(table, EmployeeListFields.PAYROLL_PAY_PERIODS_PER_YEAR, 10.0f);
                        writeLeft(table, employee.getPayPeriodsPerYear() + "", false);
                    } else if (id.equals(EmployeeListFields.PAYROLL_EXPECTED_HOURS_PER_PAY_PERIOD)) {
                        writeBoldRight(table, EmployeeListFields.PAYROLL_EXPECTED_HOURS_PER_PAY_PERIOD, 10.0f);
                        writeLeft(table, employee.getExpectedHoursPerPayPeriod() + "", false);
                    } else if (id.equals(EmployeeListFields.PAYROLL_MARITAL_STATUS)) {
                        writeBoldRight(table, EmployeeListFields.PAYROLL_MARITAL_STATUS, 10.0f);
                        write(table, employee.getMaritalStatusName());
                    } else if (id.equals(EmployeeListFields.PAYROLL_W4_STATUS)) {
                        writeBoldRight(table, EmployeeListFields.PAYROLL_W4_STATUS, 10.0f);
                        write(table, employee.getW4StatusName());
                    } else if (id.equals(EmployeeListFields.PAYROLL_OVERTIME)) {
                        writeBoldRight(table, EmployeeListFields.PAYROLL_OVERTIME, 10.0f);
                        write(table, employee.getExempt() ? "Exempt" : "Non-Exempt");
                    } else if (id.equals(EmployeeListFields.PAYROLL_EARNED_INCOME_CREDIT)) {
                        writeBoldRight(table, EmployeeListFields.PAYROLL_EARNED_INCOME_CREDIT, 10.0f);
                        write(table, employee.getEarnedIncomeCreditStatusName());
                    } else if (id.equals(EmployeeListFields.PAYROLL_LOCAL_TAX_CODE)) {
                        writeBoldRight(table, EmployeeListFields.PAYROLL_LOCAL_TAX_CODE, 10.0f);
                        write(table, employee.getLocalTaxCode());
                    } else if (id.equals(EmployeeListFields.PAYROLL_BANK_CODE)) {
                        writeBoldRight(table, EmployeeListFields.PAYROLL_BANK_CODE, 10.0f);
                        write(table, employee.getPayrollBankCode());
                    } else if (id.equals(EmployeeListFields.PAYROLL_FEDERAL_EXEMPTIONS)) {
                        writeBoldRight(table, EmployeeListFields.PAYROLL_FEDERAL_EXEMPTIONS, 10.0f);
                        writeLeft(table, employee.getFederalExemptions() + "", false);
                    } else if (id.equals(EmployeeListFields.PAYROLL_STATE_EXEMPTIONS)) {
                        writeBoldRight(table, EmployeeListFields.PAYROLL_STATE_EXEMPTIONS, 10.0f);
                        writeLeft(table, employee.getStateExemptions() + "", false);
                    } else if (id.equals(EmployeeListFields.PAYROLL_FEDERAL_EXTRA_WITHHELD)) {
                        writeBoldRight(table, EmployeeListFields.PAYROLL_FEDERAL_EXTRA_WITHHELD, 10.0f);
                        writeLeft(table, MoneyUtils.formatMoney(employee.getFederalExtraWithheld()), false);
                    } else if (id.equals(EmployeeListFields.PAYROLL_STATE_EXTRA_WITHHELD)) {
                        writeBoldRight(table, EmployeeListFields.PAYROLL_STATE_EXTRA_WITHHELD, 10.0f);
                        writeLeft(table, MoneyUtils.formatMoney(employee.getStateExtraWithheld()), false);
                    } else if (id.equals(EmployeeListFields.PAYROLL_TAX_STATE)) {
                        writeBoldRight(table, EmployeeListFields.PAYROLL_TAX_STATE, 10.0f);
                        write(table, employee.getTaxState());
                    } else if (id.equals(EmployeeListFields.PAYROLL_UNEMPLOYMENT_STATE)) {
                        writeBoldRight(table, EmployeeListFields.PAYROLL_UNEMPLOYMENT_STATE, 10.0f);
                        write(table, employee.getUnemploymentState());
                    } else if (id.equals(EmployeeListFields.PAYROLL_ADDITIONAL_FEDERAL_INCOME_TAX_TYPE)) {
                        writeBoldRight(table, EmployeeListFields.PAYROLL_ADDITIONAL_FEDERAL_INCOME_TAX_TYPE, 10.0f);
                        write(table, employee.getAddFederalIncomeTaxTypeName());
                    } else if (id.equals(EmployeeListFields.PAYROLL_ADDITIONAL_FEDERAL_INCOME_TAX_AMOUNT)) {
                        writeBoldRight(table, EmployeeListFields.PAYROLL_ADDITIONAL_FEDERAL_INCOME_TAX_AMOUNT, 10.0f);
                        writeLeft(table, employee.getAddFederalIncomeTaxAmountFormatted(), false);
                    } else if (id.equals(EmployeeListFields.PAYROLL_ADDITIONAL_STATE_INCOME_TAX_TYPE)) {
                        writeBoldRight(table, EmployeeListFields.PAYROLL_ADDITIONAL_STATE_INCOME_TAX_TYPE, 10.0f);
                        write(table, employee.getAddStateIncomeTaxTypeName());
                    } else if (id.equals(EmployeeListFields.PAYROLL_ADDITIONAL_STATE_INCOME_TAX_AMOUNT)) {
                        writeBoldRight(table, EmployeeListFields.PAYROLL_ADDITIONAL_STATE_INCOME_TAX_AMOUNT, 10.0f);
                        writeLeft(table, employee.getAddStateIncomeTaxAmountFormatted(), false);
                    } else if (id.equals(EmployeeListFields.PAYROLL_ADDITIONAL_LOCAL_INCOME_TAX_TYPE)) {
                        writeBoldRight(table, EmployeeListFields.PAYROLL_ADDITIONAL_LOCAL_INCOME_TAX_TYPE, 10.0f);
                        write(table, employee.getAddLocalIncomeTaxTypeName());
                    } else if (id.equals(EmployeeListFields.PAYROLL_ADDITIONAL_LOCAL_INCOME_TAX_AMOUNT)) {
                        writeBoldRight(table, EmployeeListFields.PAYROLL_ADDITIONAL_LOCAL_INCOME_TAX_AMOUNT, 10.0f);
                        writeLeft(table, employee.getAddLocalIncomeTaxAmountFormatted(), false);
                    } else if (id.equals(EmployeeListFields.PAYROLL_ADDITIONAL_STATE_DISABILITY_TAX_TYPE)) {
                        writeBoldRight(table, EmployeeListFields.PAYROLL_ADDITIONAL_STATE_DISABILITY_TAX_TYPE, 10.0f);
                        write(table, employee.getAddStateDisabilityTaxTypeName());
                    } else if (id.equals(EmployeeListFields.PAYROLL_ADDITIONAL_STATE_DISABILITY_TAX_AMOUNT)) {
                        writeBoldRight(table, EmployeeListFields.PAYROLL_ADDITIONAL_STATE_DISABILITY_TAX_AMOUNT, 10.0f);
                        writeLeft(table, employee.getAddStateDisabilityTaxAmountFormatted(), false);
                    } else if (id.equals(EmployeeListFields.EMPLOYMENT_ELIGIBILITY_CITIZENSHIP)) {
                        writeBoldRight(table, EmployeeListFields.EMPLOYMENT_ELIGIBILITY_CITIZENSHIP, 10.0f);
                        writeLeft(table, employee.getCitizenship(), false);
                    } else if (id.equals(EmployeeListFields.EMPLOYMENT_ELIGIBILITY_VISA)) {
                        writeBoldRight(table, EmployeeListFields.EMPLOYMENT_ELIGIBILITY_VISA, 10.0f);
                        writeLeft(table, employee.getVisa(), false);
                    } else if (id.equals(EmployeeListFields.EMPLOYMENT_ELIGIBILITY_VISA_STATUS_DATE)) {
                        writeBoldRight(table, EmployeeListFields.EMPLOYMENT_ELIGIBILITY_VISA_STATUS_DATE, 10.0f);
                        writeLeft(table, DateUtils.getDateFormatted(employee.getVisaStatusDate()), false);
                    } else if (id.equals(EmployeeListFields.EMPLOYMENT_ELIGIBILITY_VISA_EXPIRATION_DATE)) {
                        writeBoldRight(table, EmployeeListFields.EMPLOYMENT_ELIGIBILITY_VISA_EXPIRATION_DATE, 10.0f);
                        writeLeft(table, DateUtils.getDateFormatted(employee.getVisaExpirationDate()), false);
                    } else if (id.equals(EmployeeListFields.EMPLOYMENT_ELIGIBILITY_COMPLETED)) {
                        writeBoldRight(table, EmployeeListFields.EMPLOYMENT_ELIGIBILITY_COMPLETED, 10.0f);
                        writeLeft(table, employee.getI9Part1() && employee.getI9Part2() ? "Yes" : "No", false);
                    } else if (id.equals(EmployeeListFields.POSITION_DATE)) {
                        write(table, EmployeeListFields.POSITION_DATE, DateUtils.getDateFormatted(employee.getLastPositionDate()));
                    } else if (id.equals(EmployeeListFields.STATUS_DATE)) {
                        write(table, EmployeeListFields.STATUS_DATE, DateUtils.getDateFormatted(employee.getLastStatusDate()));
                    } else if (id.equals(EmployeeListFields.HIRE_DATE)) {
                        write(table, EmployeeListFields.HIRE_DATE, DateUtils.getDateFormatted(employee.getEmploymentDate()));
                    } else if (id.equals(EmployeeListFields.WAGE_DATE)) {
                        write(table, EmployeeListFields.WAGE_DATE, DateUtils.getDateFormatted(employee.getLastRaiseDate()));
                    } else if (id.equals(EmployeeListFields.WAGE_AMOUNT)) {
                        write(table, EmployeeListFields.WAGE_AMOUNT, Formatting.formatNumber(employee.getCurrentSalary(), 2));
                    } else if (id.equals(EmployeeListFields.WAGE_NOTES)) {
                        write(table, EmployeeListFields.WAGE_NOTES, employee.getCurrentWageNotes());
                    } else if (id.equals(EmployeeListFields.TOBACCO_USE)) {
                        write(table, EmployeeListFields.TOBACCO_USE, employee.getTabaccoUse().equals("U") ? "Unknown" : (employee.getTabaccoUse().equals("Y") ? "Yes" : "No"));
                    } else if (id.equals(EmployeeListFields.DRIVERS_LICENSE_NUMBER)) {
                        write(table, EmployeeListFields.DRIVERS_LICENSE_NUMBER, employee.getDriversLicenseNumber());
                    } else if (id.equals(EmployeeListFields.DRIVERS_LICENSE_STATE)) {
                        write(table, EmployeeListFields.DRIVERS_LICENSE_STATE, employee.getDriversLicenseState());
                    } else if (id.equals(EmployeeListFields.DRIVERS_LICENSE_EXPIRES)) {
                        write(table, EmployeeListFields.DRIVERS_LICENSE_EXPIRES, DateUtils.getDateFormatted(employee.getDriversLicenseExpirationDate()));
                    } else if (id.equals(EmployeeListFields.AUTOMOTIVE_INSURANCE_POLICY_NUMBER)) {
                        write(table, EmployeeListFields.AUTOMOTIVE_INSURANCE_POLICY_NUMBER, employee.getAutomotiveInsurancePolicyNumber());
                    } else if (id.equals(EmployeeListFields.AUTOMOTIVE_INSURANCE_COVERAGE)) {
                        write(table, EmployeeListFields.AUTOMOTIVE_INSURANCE_COVERAGE, employee.getAutomotiveInsuranceCoverage());
                    } else if (id.equals(EmployeeListFields.AUTOMOTIVE_INSURANCE_CARRIER)) {
                        write(table, EmployeeListFields.AUTOMOTIVE_INSURANCE_CARRIER, employee.getAutomotiveInsuranceCarrier());
                    } else if (id.equals(EmployeeListFields.AUTOMOTIVE_INSURANCE_BEGINS)) {
                        write(table, EmployeeListFields.AUTOMOTIVE_INSURANCE_BEGINS, DateUtils.getDateFormatted(employee.getAutomotiveInsuranceStartDate()));
                    } else if (id.equals(EmployeeListFields.AUTOMOTIVE_INSURANCE_EXPIRES)) {
                        write(table, EmployeeListFields.AUTOMOTIVE_INSURANCE_EXPIRES, DateUtils.getDateFormatted(employee.getAutomotiveInsuranceExpirationDate()));
                    } else if (id.equals(EmployeeListFields.COUNTY)) {
                        write(table, EmployeeListFields.COUNTY, employee.getCounty());
                    } else if (id.equals(EmployeeListFields.EFT)) {

                        for (ElectronicFundTransfer eft : hsu.createCriteria(ElectronicFundTransfer.class).eq(ElectronicFundTransfer.PERSON, employee.getEmployee()).orderBy(ElectronicFundTransfer.SEQNO).list()) {
                            write(table, "Electronic Funds Transfer", "");
                            write(table, "Account Type", eft.getAccountType() + "");
                            write(table, "Bank Route", eft.getBankRoute() + "");
                            write(table, "Bank Account", eft.getBankAccount() + "");
                            write(table, "Amount Type", eft.getAmountType() + "");
                            write(table, "Amount", Formatting.formatNumber(eft.getAmount(), 2));
                        }

                    } else if (id.equals(EmployeeListFields.GARNISHMENTS)) {
                        for (Garnishment garn : hsu.createCriteria(Garnishment.class).eq(Garnishment.EMPLOYEE, employee.getEmployee()).orderBy(Garnishment.PRIORITY).list()) {
                            write(table, "Garnishment", "");
                            writeDate(table, "Start Date", garn.getStartDate());
                            writeDate(table, "End Date", garn.getEndDate());
                            write(table, "Code", garn.getGarnishmentType().getWageType().getPayrollInterfaceCode());
                            write(table, "Net or Gross", garn.getNetOrGross() + "");

                            if (garn.getDeductionAmount() < .01) {
                                write(table, "Percentage", Formatting.formatPercentage(garn.getDeductionPercentage(), 2));
                                write(table, "Max Amount", MoneyUtils.formatMoney(garn.getMaxDollars()));
                            } else {
                                write(table, "Amount", MoneyUtils.formatMoney(garn.getDeductionAmount()));
                                write(table, "Max Percentage", Formatting.formatPercentage(garn.getMaxPercent(), 2));
                            }
                            write(table, "Docket Number", garn.getDocketNumber());
                            write(table, "FIPS", garn.getFipsCode());
                            write(table, "Issue State", garn.getIssueState());
                            write(table, "Collecting State", garn.getCollectionState());
                            write(table, "Remit To", garn.getRemitToName());
                            write(table, "", garn.getRemitTo().getStreet());
                            write(table, "", garn.getRemitTo().getStreet2());
                            write(table, "", garn.getRemitTo().getCity());
                            write(table, "", garn.getRemitTo().getState());
                            write(table, "", garn.getRemitTo().getZip());
                            write(table, "", garn.getRemitTo().getCountry());

                        }
                    }
					else if (id.equals(EmployeeListFields.BENEFITS)) {
                        for (HrBenefitJoin bj : hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, employee.getEmployee()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).joinTo(HrBenefit.BENEFIT_CATEGORY).orderBy(HrBenefitCategory.DESCRIPTION).list()) {

                            write(table, "Benefit", bj.getHrBenefitConfig().getName());

                            writeDate(table, "Policy Start Date", bj.getPolicyStartDate());
                            writeDate(table, "Policy End Date", bj.getPolicyEndDate());
                            write(table, "Insurance ID", bj.getInsuranceId());
                            writeDate(table, "Coverage Start Date", bj.getCoverageStartDate());
                            writeDate(table, "Coverage End Date", bj.getCoverageEndDate());
                            write(table, "Amount Covered", MoneyUtils.formatMoney(bj.getAmountCovered()));
                            write(table, "Amount Paid Type", bj.getAmountPaidType() + "");
                            write(table, "Amount Paid", com.arahant.utils.Formatting.formatNumber(bj.getAmountPaid(), 2));
                            write(table, "Using COBRA", bj.getUsingCOBRA() + "");
                            writeDate(table, "Accepted COBRA Date", bj.getAcceptedDateCOBRA());
                            write(table, "Max Months on COBRA", bj.getMaxMonthsCOBRA() + "");
                            write(table, "Change Reason", bj.getChangeDescription());
                        }

                    } else if (id.equals(EmployeeListFields.ACCRUED_TIME_OFF)) {

                        final String[] types = employee.getPaidTimeOffTypes();



                        for (int loop = 0; loop < types.length; loop++) {
                            write(table, types[loop], com.arahant.utils.Formatting.formatNumber(employee.getHoursLeftOnBenefit(types[loop]), 2));

                            //     ArahantSession.resetAI();
                        }

                        /*

                        for (HrBenefit b:hsu.createCriteria(HrBenefit.class)
                        .eq(HrBenefit.TIMERELATED, 'Y')
                        .orderBy(HrBenefit.NAME)
                        .joinTo(HrBenefit.HR_BENEFIT_CONFIGS)
                        .joinTo(HrBenefitConfig.HR_BENEFIT_JOINS)
                        .eq(HrBenefitJoin.PAYING_PERSON, employee.getEmployee())
                        .list())
                        {

                        write(table,b.getName(),com.arahant.utils.Formatting.formatNumber(employee.getHoursLeftOnBenefit(b.getName()),2));
                        }
                         * */
                    } else if (id.equals(EmployeeListFields.BENEFIT_SUMMARY)) {
                        for (HrBenefitJoin bj : hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, employee.getEmployee()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).joinTo(HrBenefit.BENEFIT_CATEGORY).orderBy(HrBenefitCategory.DESCRIPTION).list()) {

                            write(table, "Benefit", bj.getHrBenefitConfig().getName());
                            if (bj.getAmountPaidType() == 'P') {
                                write(table, "Amount Paid", com.arahant.utils.Formatting.formatPercentage(bj.getAmountPaid(), 2));
                            } else {
                                write(table, "Amount Paid", MoneyUtils.formatMoney(bj.getAmountPaid()));
                            }
                        }
                    } else if (id.equals(EmployeeListFields.EVENTS)) {
                        for (HrEmployeeEvent ee : hsu.createCriteria(HrEmployeeEvent.class).eq(HrEmployeeEvent.EMPLOYEEID, employee.getEmployee()).orderBy(HrEmployeeEvent.EVENTDATE).list()) {
                            write(table, "Event", "");
                            writeDate(table, "Event Date", ee.getEventDate());
                            write(table, "Summary", ee.getSummary());
                            write(table, "Supervisor", ee.getSupervisorId().getNameLFM());
                            write(table, "Employee Notified", ee.getEmployeeNotified() + "");
                            writeDate(table, "Date Notified", ee.getDateNotified());
                            write(table, "Detail", ee.getDetail());
                        }

                    } else if (id.equals(EmployeeListFields.NOTES)) {
                        for (PersonNote cd : hsu.createCriteria(PersonNote.class).eq(PersonNote.PERSON, employee.getEmployee()).joinTo(PersonNote.HRNOTECATEGORY).orderBy(HrNoteCategory.CATID).list()) {

                            write(table, cd.getHrNoteCategory().getName(), cd.getNote());
                        }
                    } else if (id.equals(EmployeeListFields.TRAINING)) {
                        for (HrTrainingDetail cd : hsu.createCriteria(HrTrainingDetail.class).eq(HrTrainingDetail.EMPLOYEE, employee.getEmployee()).joinTo(HrTrainingDetail.HRTRAININGCATEGORY).orderBy(HrTrainingCategory.CATID).list()) {
                            write(table, "Training", cd.getHrTrainingCategory().getName());
                            writeDate(table, "Training Date", cd.getTrainingDate());
                            write(table, "Training Hours", cd.getTrainingHours() + "");
                            writeDate(table, "Training Expire Hours", cd.getExpireDate());
                        }
                    } else if (id.equals(EmployeeListFields.ORG_GROUP)) {
                        Set<OrgGroupAssociation> assocs = employee.getOrgGroupAssociations();

                        if (assocs.size() > 0) {
                            OrgGroupAssociation oga = assocs.iterator().next();
                            OrgGroup og = oga.getOrgGroup();
                            write(table, "Organization Group", og.getName());
                            write(table, "Organization Group Code", og.getExternalId());
                        }
                    } else if (id.equals(EmployeeListFields.ORG_GROUP1)) {
                        this.printOrgGroups(employee, table, 1);
                    } else if (id.equals(EmployeeListFields.ORG_GROUP2)) {
                        this.printOrgGroups(employee, table, 2);
                    } else if (id.equals(EmployeeListFields.ORG_GROUP3)) {
                        this.printOrgGroups(employee, table, 3);
                    } else if (id.equals(EmployeeListFields.ORG_GROUP4)) {
                        this.printOrgGroups(employee, table, 4);
                    } else if (id.equals(EmployeeListFields.ORG_GROUP5)) {
                        this.printOrgGroups(employee, table, 5);
                    } else if (id.equals(EmployeeListFields.FORMS)) {
                        for (PersonForm ee : hsu.createCriteria(PersonForm.class).eq(PersonForm.PERSON, employee.getEmployee()).orderBy(PersonForm.DATE).list()) {
                            write(table, "Form", "");
                            write(table, "Form Code", ee.getFormType().getFormCode());
                            write(table, "Form Type", ee.getFormType().getDescription());
                            writeDate(table, "Form Date", ee.getFormDate());
                            write(table, "Comments", ee.getComments());
                        }

                    } else if (id.equals(EmployeeListFields.CHECK_LIST)) {
                        for (HrChecklistDetail cd : hsu.createCriteria(HrChecklistDetail.class).eq(HrChecklistDetail.EMPLOYEEBYEMPLOYEEID, employee.getEmployee()).joinTo(HrChecklistDetail.HRCHECKLISTITEM).orderBy(HrChecklistItem.SEQ).list()) {
                            write(table, cd.getHrChecklistItem().getName(), "");
                            writeDate(table, "Date Completed", cd.getDateCompleted());
                            write(table, "Supervisor", cd.getEmployeeBySupervisorId().getNameLFM());
                        }
                    } 
                }

                // write empty row between employees
                write(table, " ");
                write(table, " ");

                recordIdx++;
            }

            scrollUtil.close();

            addTable(table);
        } finally {
            close();
        }

        return getFilename();
    }

    private String getGroupId(BEmployee emp) {
        if (orgGroupIds.length > 0) {
            return orgGroupIds[0];
        } else {
            //get employee company id
            return emp.getEmployee().getCompanyBase().getCompanyId();
        }
    }

    private void printOrgGroups(BEmployee emp, PdfPTable table, int orgNumber) {
        ListHierarchyInBreathFirstSearch bh = new ListHierarchyInBreathFirstSearch();
        //write(table,"Organization Group Level " + orgNumber + ":", bh.getOrganizationLevel(emp, orgNumber));
        if (orgList == null) {
            orgList = bh.getEmployeeGroupAssociationHierarchy(emp.getEmployee().getPersonId(), getGroupId(emp));
        }
        if (orgList.size()>=orgNumber){
        write(table, "Organization Group Level " + orgNumber + ":", orgList.get(orgNumber-1));
        } else {
            write(table, "Organization Group Level " + orgNumber + ":","");
        }

    }

    private void write(PdfPTable table, String header, String value) {
        if (isEmpty(header) && isEmpty(value)) {
            return;
        }
        writeBoldRight(table, header, 10.0f);
        writeLeft(table, value, false);
    }

    private void writeDate(PdfPTable table, String header, int date) {
        write(table, header, DateUtils.getDateFormatted(date));
    }
}

	
