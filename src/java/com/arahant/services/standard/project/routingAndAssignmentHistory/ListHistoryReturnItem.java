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

import com.arahant.business.BProjectHistory;

public class ListHistoryReturnItem {
	
	private String dateTimeChangedFormatted;
	private String changeType;
	private String changedByLastName;
	private String changedByFirstName;
	private String historyId;

	public ListHistoryReturnItem() {
	}

	ListHistoryReturnItem(BProjectHistory h) {
		dateTimeChangedFormatted = h.getDateTimeFormatted();
		changeType = h.getChangeType();
		changedByLastName = h.getByLastName();
		changedByFirstName = h.getByFirstName();
		historyId = h.getHistoryId();
	}

	public String getDateTimeChangedFormatted() {
		return dateTimeChangedFormatted;
	}

	public void setDateTimeChangedFormatted(String dateTimeChangedFormatted) {
		this.dateTimeChangedFormatted = dateTimeChangedFormatted;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getChangedByLastName() {
		return changedByLastName;
	}

	public void setChangedByLastName(String changedByLastName) {
		this.changedByLastName = changedByLastName;
	}

	public String getChangedByFirstName() {
		return changedByFirstName;
	}

	public void setChangedByFirstName(String changedByFirstName) {
		this.changedByFirstName = changedByFirstName;
	}

	public String getHistoryId() {
		return historyId;
	}

	public void setHistoryId(String historyId) {
		this.historyId = historyId;
	}

}
