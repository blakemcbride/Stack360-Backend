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
package com.arahant.services.standard.hr.hrOrgGroupHistory;
import com.arahant.business.BEmployee.OrgGroupAssociationHistory;


/**
 * 
 *
 *
 */
public class ListOrgGroupHistoryReturnItem {
	
	public ListOrgGroupHistoryReturnItem()
	{
		
	}

	ListOrgGroupHistoryReturnItem (OrgGroupAssociationHistory history)
	{
		dateTimeFormatted=history.getDateTimeFormatted();
		changeType=history.getChangeType();
		orgGroupName=history.getOrgGroupName();
		supervisor=history.getSupervisor();
		personName=history.getPersonName();
		externalId=history.getExternalId();
        startDate=history.getStartDate();
        finalDate=history.getFinalDate();
		historyId=history.getHistoryId();
	}
	
	private String dateTimeFormatted;
	private String changeType;
	private String orgGroupName;
	private String supervisor;
	private String personName;
	private String externalId;
    private int startDate;
    private int finalDate;
	private String historyId;

	public String getDateTimeFormatted()
	{
		return dateTimeFormatted;
	}
	public void setDateTimeFormatted(String dateTimeFormatted)
	{
		this.dateTimeFormatted=dateTimeFormatted;
	}
	public String getChangeType()
	{
		return changeType;
	}
	public void setChangeType(String changeType)
	{
		this.changeType=changeType;
	}
	public String getOrgGroupName()
	{
		return orgGroupName;
	}
	public void setOrgGroupName(String orgGroupName)
	{
		this.orgGroupName=orgGroupName;
	}
	public String getSupervisor()
	{
		return supervisor;
	}
	public void setSupervisor(String supervisor)
	{
		this.supervisor=supervisor;
	}
	public String getPersonName()
	{
		return personName;
	}
	public void setPersonName(String personName)
	{
		this.personName=personName;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

    public int getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(int finalDate) {
        this.finalDate = finalDate;
    }

    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

	public String getHistoryId() {
		return historyId;
	}

	public void setHistoryId(String historyId) {
		this.historyId = historyId;
	}
}

	
