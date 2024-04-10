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
package com.arahant.services.standard.time.timesheetEntryWeeklySummary;

import com.arahant.business.BEmployee;
import com.arahant.business.BPerson;
import com.arahant.business.BTimesheet;

public class TimesheetTransmit {

	private String state;
	private String finalized;
	private String personId;
	private String workerId;
	private String workerName;
	private String timesheetId;
	private double totalHours;
	private double totalExpenses;
	private double totalPay;
    private String homePhone;
	private String cellPhone;


	public TimesheetTransmit() {
	}

	TimesheetTransmit(final BTimesheet t) {
		state = t.getState() + "";
		finalized = t.getFinalized();
		timesheetId = t.getTimesheetId();
		personId = t.getPersonId();
		BEmployee be = new BEmployee(t.getPersonId());
		workerId = be.getExtRef();
		workerName = t.getTimesheet().getPerson() == null ? be.getNameLFM() : t.getTimesheet().getPerson().getNameLFM();
		totalHours = t.getTotalHours();
		totalExpenses = t.getTotalExpenses();
		totalPay = t.getFixedPay();
        BPerson bp = new BPerson(t.getPersonId());
        homePhone = bp.getHomePhone();
        cellPhone = bp.getMobilePhone();
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getState() {
		return state;
	}

	public void setState(final String statusCode) {
		this.state = statusCode;
	}

	public String getFinalized() {
		return finalized;
	}

	public void setFinalized(final String finalized) {
		this.finalized = finalized;
	}

	public String getTimesheetId() {
		return timesheetId;
	}

	public void setTimesheetId(final String timesheetId) {
		this.timesheetId = timesheetId;
	}

	public double getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(final double totalHours) {
		this.totalHours = totalHours;
	}

	public double getTotalExpenses() {
		return totalExpenses;
	}

	public void setTotalExpenses(double totalExpenses) {
		this.totalExpenses = totalExpenses;
	}

	public double getTotalPay() {
		return totalPay;
	}

	public void setTotalPay(double totalPay) {
		this.totalPay = totalPay;
	}

	public String getWorkerId() {
		return workerId;
	}

	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }
}
