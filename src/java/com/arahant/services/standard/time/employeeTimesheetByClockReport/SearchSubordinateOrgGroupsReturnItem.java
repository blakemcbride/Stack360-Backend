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
 * Created on Feb 12, 2007
 * 
 */
package com.arahant.services.standard.time.employeeTimesheetByClockReport;
import com.arahant.beans.PaySchedule;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BPaySchedule;


/**
 * 
 *
 * Created on Feb 12, 2007
 *
 */
public class SearchSubordinateOrgGroupsReturnItem  {

	private String orgGroupId;
	private String name;
	private String type;
	private String companyId;
	private int startDate;
	private int endDate;
	
	public SearchSubordinateOrgGroupsReturnItem() {

	}

	/**
	 * @param group
	 */
	SearchSubordinateOrgGroupsReturnItem(final BOrgGroup group) {
		super();
		orgGroupId=group.getOrgGroupId();
		name=group.getName();
		type=group.getType();
		companyId=group.getCompanyId();
		
		PaySchedule ps=group.getPaySchedule();
		if (ps!=null)
		{
			BPaySchedule bps=new BPaySchedule(ps);
			startDate=bps.getCurrentPeriodStart();
			endDate=bps.getCurrentPeriodEnd();
		}
	}

	/**
	 * @return Returns the companyId.
	 */
	public String getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId The companyId to set.
	 */
	public void setCompanyId(final String companyId) {
		this.companyId = companyId;
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

	/**
	 * @return Returns the orgGroupId.
	 */
	public String getOrgGroupId() {
		return orgGroupId;
	}

	/**
	 * @param orgGroupId The orgGroupId to set.
	 */
	public void setOrgGroupId(final String orgGroupId) {
		this.orgGroupId = orgGroupId;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(final String type) {
		this.type = type;
	}

	public int getEndDate() {
		return endDate;
	}

	public void setEndDate(int endDate) {
		this.endDate = endDate;
	}

	public int getStartDate() {
		return startDate;
	}

	public void setStartDate(int startDate) {
		this.startDate = startDate;
	}

}

	
