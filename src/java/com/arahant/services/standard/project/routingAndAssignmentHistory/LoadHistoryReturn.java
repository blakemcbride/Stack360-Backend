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

package com.arahant.services.standard.project.routingAndAssignmentHistory;

import com.arahant.business.BPerson;
import com.arahant.business.BProjectHistory;
import com.arahant.services.TransmitReturnBase;

public class LoadHistoryReturn extends TransmitReturnBase {
	
	private LoadHistoryReturnItem[] assignedFrom;
	private LoadHistoryReturnItem[] assignedTo;
	private String changedByFormatted;
	private String dateTimeFormatted;
	private String fromDecisionPointName;
	private String fromOrgGroupName;
	private String fromStatusCode;
	private String toDecisionPointName;
	private String toOrgGroupName;
	private String toStatusCode;
	
	void setData(BProjectHistory bc) {
		changedByFormatted = bc.getByNameLFM();
		dateTimeFormatted = bc.getDateTimeFormatted();
		fromDecisionPointName = bc.getFromRouteStopName();
		fromOrgGroupName = bc.getFromRouteStopOrgGroupName();
		fromStatusCode = bc.getFromStatusCode();
		toDecisionPointName = bc.getToRouteStopName();
		toOrgGroupName = bc.getToRouteStopOrgGroupName();
		toStatusCode = bc.getToStatusCode();
	}

	public LoadHistoryReturnItem[] getAssignedFrom() {
		return assignedFrom;
	}

	public void setAssignedFrom(LoadHistoryReturnItem[] assignedFrom) {
		this.assignedFrom = assignedFrom;
	}

	public LoadHistoryReturnItem[] getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(LoadHistoryReturnItem[] assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getChangedByFormatted() {
		return changedByFormatted;
	}

	public void setChangedByFormatted(String changedByFormatted) {
		this.changedByFormatted = changedByFormatted;
	}

	public String getDateTimeFormatted() {
		return dateTimeFormatted;
	}

	public void setDateTimeFormatted(String dateTimeFormatted) {
		this.dateTimeFormatted = dateTimeFormatted;
	}

	public String getFromDecisionPointName() {
		return fromDecisionPointName;
	}

	public void setFromDecisionPointName(String fromDecisionPointName) {
		this.fromDecisionPointName = fromDecisionPointName;
	}

	public String getFromOrgGroupName() {
		return fromOrgGroupName;
	}

	public void setFromOrgGroupName(String fromOrgGroupName) {
		this.fromOrgGroupName = fromOrgGroupName;
	}

	public String getFromStatusCode() {
		return fromStatusCode;
	}

	public void setFromStatusCode(String fromStatusCode) {
		this.fromStatusCode = fromStatusCode;
	}

	public String getToDecisionPointName() {
		return toDecisionPointName;
	}

	public void setToDecisionPointName(String toDecisionPointName) {
		this.toDecisionPointName = toDecisionPointName;
	}

	public String getToOrgGroupName() {
		return toOrgGroupName;
	}

	public void setToOrgGroupName(String toOrgGroupName) {
		this.toOrgGroupName = toOrgGroupName;
	}

	public String getToStatusCode() {
		return toStatusCode;
	}

	public void setToStatusCode(String toStatusCode) {
		this.toStatusCode = toStatusCode;
	}

}
