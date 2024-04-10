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


/**
 * 
 */
package com.arahant.services.standard.time.timesheetByPayPeriod;

import com.arahant.beans.HrBenefit;
import com.arahant.business.BEmployee;
import com.arahant.business.BPerson;
import com.arahant.business.BTimesheet;
import com.arahant.utils.ArahantSession;


public class SearchTimesheetsReturnTimeRelated {
	SearchTimesheetsReturnTimeRelated() {

	}

	SearchTimesheetsReturnTimeRelated(BTimesheet bc, int startDate, int endDate) {
		employeeId = bc.getPersonId();
		employeeName = new BPerson(bc.getPersonId()).getNameLFM();
		timeOffType = bc.getProjectName();
		hoursUsed = bc.getTotalHours();
		hoursAvailable = bc.getProject().getBenefitAccrual(new BEmployee(bc.getPersonId()));
		benefitId = bc.getBenefitId();
	}
	
	SearchTimesheetsReturnTimeRelated(BEmployee be, String benefitId, double hours, int startDate, int endDate) {
		employeeId = be.getPersonId();
		employeeName = be.getNameLFM();
		timeOffType = ArahantSession.getHSU().createCriteria(HrBenefit.class).eq(HrBenefit.BENEFITID, benefitId).first().getName();
		hoursUsed = hours;
		this.benefitId = benefitId;
		int max = 10000;
		hoursAvailable = be.getTimeOffByDates(benefitId, startDate, endDate);//be.getTimeOffCurrentPeriod(benefitId);
	}

	private String employeeId;
	private String employeeName;
	private String timeOffType;
	private String benefitId;
	private double hoursUsed;
	private double hoursAvailable;

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public double getHoursAvailable() {
		return hoursAvailable;
	}

	public void setHoursAvailable(double hoursAvailable) {
		this.hoursAvailable = hoursAvailable;
	}

	public double getHoursUsed() {
		return hoursUsed;
	}

	public void setHoursUsed(double hoursUsed) {
		this.hoursUsed = hoursUsed;
	}

	public String getBenefitId() {
		return benefitId;
	}

	public void setBenefitId(String benefitId) {
		this.benefitId = benefitId;
	}

	public String getTimeOffType() {
		return timeOffType;
	}

	public void setTimeOffType(String timeOffType) {
		this.timeOffType = timeOffType;
	}
}

	
