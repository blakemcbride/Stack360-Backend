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

import com.arahant.business.BOrgGroup;
import com.arahant.services.TransmitReturnBase;

public class LoadGroupReturn extends TransmitReturnBase {

	private String addressLine1;
	private String addressLine2;
	private String city;
	private String stateProvince;
	private String zipPostalCode;
	private String country;
	private String county;
	private String mainFaxNumber;
	private String mainPhoneNumber;
	private boolean eeoEstablishment;
	private boolean eeoHQ;
	private boolean eeoFiledLastYear;
	private String eeoUnitNumber;
	private String payScheduleId;
	private int payPeriodsPerYear;
	private int parentPayPeriodsPerYear;
	private int evalEmailNotifyDays;
	private int evalEmailFirstDays;
	private String evalEmailNotifySendDays;
	private String evalEmailNotify;
	private int evalEmailNotifyDaysInherited;
	private int evalEmailFirstDaysInherited;
	private String evalEmailNotifySendDaysInherited;
	private String evalEmailNotifyInherited;
	private int newWeekBeginDay;
	private String defaultBenefitClassId;
	private String parentBenefitClassFormatted;
	private String timesheetPeriodType;
	private int timesheetPeriodStartDate;
	private String timesheetShowBillable;

	public LoadGroupReturn() {
	}

	LoadGroupReturn(final BOrgGroup orgGroup) {
		addressLine1 = orgGroup.getStreet();
		addressLine2 = orgGroup.getStreet2();
		city = orgGroup.getCity();
		stateProvince = orgGroup.getState();
		zipPostalCode = orgGroup.getZip();
		country = orgGroup.getCountry();
		county = orgGroup.getCounty();
		mainFaxNumber = orgGroup.getMainFaxNumber();
		mainPhoneNumber = orgGroup.getMainPhoneNumber();
		eeoEstablishment = orgGroup.getEeo1Establishment();
		eeoHQ = orgGroup.getEeo1Headquarters();
		eeoFiledLastYear = orgGroup.getEeo1FiledLastYear();
		eeoUnitNumber = orgGroup.getEeo1UnitId();
		payScheduleId = orgGroup.getExplicitPayScheduleId();
		payPeriodsPerYear = orgGroup.getNonInheritedPayPeriodsPerYear();
		parentPayPeriodsPerYear = orgGroup.getParentPayPeriodsPerYear();
		evalEmailNotify = orgGroup.getEvalEmailNotify();
		evalEmailNotifyDays = orgGroup.getEvalEmailNotifyDays();
		evalEmailNotifySendDays = orgGroup.getEvalEmailNotifySendDays();
		evalEmailFirstDays = orgGroup.getEvalEmailFirstDays();
		evalEmailNotifyInherited = orgGroup.getEvalEmailNotifyInherited();
		evalEmailNotifyDaysInherited = orgGroup.getEvalEmailNotifyDaysInherited();
		evalEmailNotifySendDaysInherited = orgGroup.getEvalEmailNotifySendDaysInherited();
		evalEmailFirstDaysInherited = orgGroup.getEvalEmailFirstDaysInherited();
		newWeekBeginDay = orgGroup.getNewWeekBeginDay();
		defaultBenefitClassId = orgGroup.getDefaultBenefitClassId();
		parentBenefitClassFormatted = orgGroup.getInheritedBenefitClassName();
		if (isEmpty(parentBenefitClassFormatted))
			parentBenefitClassFormatted = "(None)";
		timesheetPeriodType = orgGroup.getTimesheetPeriodType() + "";
		timesheetPeriodStartDate = orgGroup.getTimesheetPeriodStartDate();
		timesheetShowBillable = orgGroup.getTimesheetShowBillable() + "";
	}

	public String getParentBenefitClassFormatted() {
		return parentBenefitClassFormatted;
	}

	public void setParentBenefitClassFormatted(String parentBenefitClassFormatted) {
		this.parentBenefitClassFormatted = parentBenefitClassFormatted;
	}

	public String getDefaultBenefitClassId() {
		return defaultBenefitClassId;
	}

	public void setDefaultBenefitClassId(String defaultBenefitClassId) {
		this.defaultBenefitClassId = defaultBenefitClassId;
	}

	public int getNewWeekBeginDay() {
		return newWeekBeginDay;
	}

	public void setNewWeekBeginDay(int newWeekBeginDay) {
		this.newWeekBeginDay = newWeekBeginDay;
	}

	public int getEvalEmailFirstDaysInherited() {
		return evalEmailFirstDaysInherited;
	}

	public void setEvalEmailFirstDaysInherited(int evalEmailFirstDaysInherited) {
		this.evalEmailFirstDaysInherited = evalEmailFirstDaysInherited;
	}

	public int getEvalEmailNotifyDaysInherited() {
		return evalEmailNotifyDaysInherited;
	}

	public void setEvalEmailNotifyDaysInherited(int evalEmailNotifyDaysInherited) {
		this.evalEmailNotifyDaysInherited = evalEmailNotifyDaysInherited;
	}

	public String getEvalEmailNotifyInherited() {
		return evalEmailNotifyInherited;
	}

	public void setEvalEmailNotifyInherited(String evalEmailNotifyInherited) {
		this.evalEmailNotifyInherited = evalEmailNotifyInherited;
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

	public void setEvalEmailNotify(String evalEmailNotify) {
		this.evalEmailNotify = evalEmailNotify;
	}

	public int getEvalEmailNotifyDays() {
		return evalEmailNotifyDays;
	}

	public void setEvalEmailNotifyDays(int evalEmailNotifyDays) {
		this.evalEmailNotifyDays = evalEmailNotifyDays;
	}

	public String getEvalEmailNotifySendDays() {
		return evalEmailNotifySendDays;
	}

	public void setEvalEmailNotifySendDays(String evalEmailNotifySendDays) {
		this.evalEmailNotifySendDays = evalEmailNotifySendDays;
	}

	public String getEvalEmailNotifySendDaysInherited() {
		return evalEmailNotifySendDaysInherited;
	}

	public void setEvalEmailNotifySendDaysInherited(String evalEmailNotifySendDaysInherited) {
		this.evalEmailNotifySendDaysInherited = evalEmailNotifySendDaysInherited;
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

	public int getParentPayPeriodsPerYear() {
		return parentPayPeriodsPerYear;
	}

	public void setParentPayPeriodsPerYear(int parentPayPeriodsPerYear) {
		this.parentPayPeriodsPerYear = parentPayPeriodsPerYear;
	}

	public int getPayPeriodsPerYear() {
		return payPeriodsPerYear;
	}

	public void setPayPeriodsPerYear(int payPeriodsPerYear) {
		this.payPeriodsPerYear = payPeriodsPerYear;
	}

	public String getPayScheduleId() {
		return payScheduleId;
	}

	public void setPayScheduleId(String payScheduleId) {
		this.payScheduleId = payScheduleId;
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
