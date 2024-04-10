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
package com.arahant.services.standard.misc.companyOrgGroup;

import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;

public class NewGroupInput extends TransmitInputBase {

	@Validation(table = "org_group", column = "name", required = true)
	private String name;
	@Validation(required = false)
	private String parentGroupId;
	@Validation(table = "org_group", column = "external_id", required = false)
	private String externalId;
	@Validation(min = 0, max = 366, required = false)
	private int payPeriodsPerYear;
	@Validation(table = "org_group", column = "default_project_id", required = false)
	private String defaultProjectId;
	@Validation(required = false)
	private String payScheduleId;
	@Validation(table = "address", column = "street", required = false)
	private String addressLine1;
	@Validation(table = "address", column = "street2", required = false)
	private String addressLine2;
	@Validation(table = "address", column = "city", required = false)
	private String city;
	@Validation(table = "address", column = "state", required = false)
	private String stateProvince;
	@Validation(table = "address", column = "zip", required = false)
	private String zipPostalCode;
	@Validation(table = "address", column = "country_code", required = false)
	private String country;
	@Validation(table = "address", column = "county", required = false)
	private String county;
	@Validation(table = "phone", column = "phone_number", required = false)
	private String mainPhoneNumber;
	@Validation(table = "phone", column = "phone_number", required = false)
	private String mainFaxNumber;
	@Validation(table = "org_group", column = "eeo1_unit", required = false)
	private String eeoUnitNumber;
	@Validation(required = false)
	private boolean eeoEstablishment;
	@Validation(required = false)
	private boolean eeoHQ;
	@Validation(required = false)
	private boolean eeoFiledLastYear;
	@Validation(table = "org_group", column = "eval_email_notify_days", required = false)
	private int evalEmailNotifyDays;
	@Validation(table = "org_group", column = "eval_email_first_days", required = false)
	private int evalEmailFirstDays;
	@Validation(table = "org_group", column = "eval_email_send_days", required = false)
	private String evalEmailNotifySendDays;
	@Validation(table = "org_group", column = "eval_email_notify", required = false)
	private String evalEmailNotify;
	@Validation(table = "org_group", column = "new_week_begin_day", required = false)
	private int newWeekBeginDay;
	@Validation(table = "org_group", column = "benefit_class_id", required = false)
	private String benefitClassId;
	@Validation(table = "org_group", column = "timesheet_period_type", required = true)
	private String timesheetPeriodType;
	@Validation(type="date", required = false)
	private int timesheetPeriodStartDate;
	@Validation(table = "org_group", column = "timesheet_show_billable", required = true)
	private String timesheetShowBillable;

	public String getBenefitClassId() {
		return benefitClassId;
	}

	public void setBenefitClassId(String benefitClassId) {
		this.benefitClassId = benefitClassId;
	}

	public int getNewWeekBeginDay() {
		return newWeekBeginDay;
	}

	public void setNewWeekBeginDay(int newWeekBeginDay) {
		this.newWeekBeginDay = newWeekBeginDay;
	}

	public int getEvalEmailFirstDays() {
		return evalEmailFirstDays;
	}

	public void setEvalEmailFirstDays(int evalEmailFirstDays) {
		this.evalEmailFirstDays = evalEmailFirstDays;
	}

	public String getEvalEmailNotify() {
		return evalEmailNotify;
	}

	public String getEvalEmailNotifySendDays() {
		return evalEmailNotifySendDays;
	}

	public void setEvalEmailNotifySendDays(String evalEmailNotifySendDays) {
		this.evalEmailNotifySendDays = evalEmailNotifySendDays;
	}

	public void setEvalEmailNotify(String evalEmailNotify) {
		this.evalEmailNotify = evalEmailNotify;
	}

	public int getEvalEmailNotifyDays() {
		return evalEmailNotifyDays;
	}

	public void setEvalEmailNotifyDays(int evalEmailNotifyDays) {
		this.evalEmailNotifyDays = evalEmailNotifyDays;
	}

	public boolean getEeoEstablishment() {
		return eeoEstablishment;
	}

	public void setEeoEstablishment(boolean eeoEstablishment) {
		this.eeoEstablishment = eeoEstablishment;
	}

	public boolean getEeoFiledLastYear() {
		return eeoFiledLastYear;
	}

	public void setEeoFiledLastYear(boolean eeoFiledLastYear) {
		this.eeoFiledLastYear = eeoFiledLastYear;
	}

	public boolean getEeoHQ() {
		return eeoHQ;
	}

	public void setEeoHQ(boolean eeoHQ) {
		this.eeoHQ = eeoHQ;
	}

	public String getEeoUnitNumber() {
		return eeoUnitNumber;
	}

	public void setEeoUnitNumber(String eeoUnitNumber) {
		this.eeoUnitNumber = eeoUnitNumber;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getStateProvince() {
		return stateProvince;
	}

	public void setStateProvince(String stateProvince) {
		this.stateProvince = stateProvince;
	}

	public String getZipPostalCode() {
		return zipPostalCode;
	}

	public void setZipPostalCode(String zipPostalCode) {
		this.zipPostalCode = zipPostalCode;
	}

	public String getMainFaxNumber() {
		return mainFaxNumber;
	}

	public void setMainFaxNumber(String mainFaxNumber) {
		this.mainFaxNumber = mainFaxNumber;
	}

	public String getMainPhoneNumber() {
		return mainPhoneNumber;
	}

	public void setMainPhoneNumber(String mainPhoneNumber) {
		this.mainPhoneNumber = mainPhoneNumber;
	}

	public String getPayScheduleId() {
		return payScheduleId;
	}

	public void setPayScheduleId(String payScheduleId) {
		this.payScheduleId = payScheduleId;
	}

	public String getDefaultProjectId() {
		return defaultProjectId;
	}

	public void setDefaultProjectId(String defaultProjectId) {
		this.defaultProjectId = defaultProjectId;
	}

	/**
	 * @return Returns the payPeriodsPerYear.
	 */
	public int getPayPeriodsPerYear() {
		return payPeriodsPerYear;
	}

	/**
	 * @param payPeriodsPerYear The payPeriodsPerYear to set.
	 */
	public void setPayPeriodsPerYear(int payPeriodsPerYear) {
		this.payPeriodsPerYear = payPeriodsPerYear;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(final String externalId) {
		this.externalId = externalId;
	}

	/**
	 * @return Returns the parentGroupId.
	 */
	public String getParentGroupId() {
		return parentGroupId;
	}

	/**
	 * @param parentGroupId The parentGroupId to set.
	 */
	public void setParentGroupId(final String parentGroupId) {
		this.parentGroupId = parentGroupId;
	}

	public String getTimesheetPeriodType() {
		return timesheetPeriodType;
	}

	public void setTimesheetPeriodType(String timesheetPeriodType) {
		this.timesheetPeriodType = timesheetPeriodType;
	}

	public int getTimesheetPeriodStartDate() {
		return timesheetPeriodStartDate;
	}

	public void setTimesheetPeriodStartDate(int timesheetPeriodStartDate) {
		this.timesheetPeriodStartDate = timesheetPeriodStartDate;
	}

	public String getTimesheetShowBillable() {
		return timesheetShowBillable;
	}

	public void setTimesheetShowBillable(String timesheetShowBillable) {
		this.timesheetShowBillable = timesheetShowBillable;
	}
}
