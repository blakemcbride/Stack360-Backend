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
package com.arahant.services.standard.hr.hrOrgGroup;

import com.arahant.business.BOrgGroupAssociation;


/**
 * 
 *
 *
 */
public class ListOrgGroupsReturnItem {
	
	public ListOrgGroupsReturnItem()
	{
		;
	}

	ListOrgGroupsReturnItem (final BOrgGroupAssociation oga)
	{

		orgGroupId=oga.getOrgGroup().getOrgGroupId();
		orgGroupName=oga.getOrgGroup().getName();
		supervisorFormatted=oga.getIsPrimary()?"Yes":"No";
		externalId=oga.getOrgGroup().getExternalId();
		orgGroupHierarchy=oga.getOrgGroup().getOrgGroupHierarchyString();
        startDate=oga.getStartDate();
        finalDate=oga.getFinalDate();

	}

	private String orgGroupId;
	private String orgGroupName;
	private String supervisorFormatted;
	private String externalId;
	private String orgGroupHierarchy;
    private int startDate;
    private int finalDate;

	public String getSupervisorFormatted() {
		return supervisorFormatted;
	}

	public void setSupervisorFormatted(final String supervisorFormatted) {
		this.supervisorFormatted = supervisorFormatted;
	}

	public String getOrgGroupId()
	{
		return orgGroupId;
	}
	public void setOrgGroupId(final String orgGroupId)
	{
		this.orgGroupId=orgGroupId;
	}
	public String getOrgGroupName()
	{
		return orgGroupName;
	}
	public void setOrgGroupName(final String orgGroupName)
	{
		this.orgGroupName=orgGroupName;
	}
	public String getExternalId()
	{
		return externalId;
	}
	public void setExternalId(String externalId)
	{
		this.externalId=externalId;
	}

	public String getOrgGroupHierarchy() {
		return orgGroupHierarchy;
	}

	public void setOrgGroupHierarchy(String orgGroupHierarchy) {
		this.orgGroupHierarchy = orgGroupHierarchy;
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
}

	
