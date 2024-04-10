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
package com.arahant.services.standard.hr.employeeListReport;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class GetReportInput extends TransmitInputBase {

	@Validation (required=false)
	private String[] ids;
	@Validation (min=0,max=2,required=false)
	private int statusType;
	@Validation (type="date",required=false)
	private int dobFrom;
	@Validation (type="date",required=false)
	private int dobTo;
	@Validation (table="person",column="lname",required=false)
	private String lastNameStartsWithFrom;
	@Validation (table="person",column="lname",required=false)
	private String lastNameStartsWithTo;
	@Validation (required=false)
	private int sortType;
	@Validation (required=false)
	private boolean sortAsc;
	@Validation (min=0,required=false)
	private String[] orgGroupIds;
	@Validation (min=0,required=false)
	private String[] orgGroupCodes;
	@Validation (min=0,required=false)
	private String[] configIds;
	
	
	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}
	
	public int getStatusType() {
		return statusType;
	}

	public void setStatusType(int statusType) {
		this.statusType = statusType;
	}
	
	public int getDobFrom() {
		return dobFrom;
	}
	
	public void setDobFrom(int dobFrom) {
		this.dobFrom = dobFrom;
	}
	
	public int getDobTo() {
		return dobTo;
	}
	
	public void setDobTo(int dobTo) {
		this.dobTo = dobTo;
	}

	public String getLastNameStartsWithFrom() {
		return lastNameStartsWithFrom;
	}

	public void setLastNameStartsWithFrom(String lastNameStartsWithFrom) {
		this.lastNameStartsWithFrom = lastNameStartsWithFrom;
	}

	public String getLastNameStartsWithTo() {
		return lastNameStartsWithTo;
	}

	public void setLastNameStartsWithTo(String lastNameStartsWithTo) {
		this.lastNameStartsWithTo = lastNameStartsWithTo;
	}
	
	public int getSortType() {
		return sortType;
	}
	
	public void setSortType(int sortType) {
		this.sortType = sortType;
	}
	
	public boolean isSortAsc() {
		return sortAsc;
	}
	
	public void setSortAsc(boolean sortAsc) {
		this.sortAsc = sortAsc;
	}
	
	public String[] getOrgGroupCodes() {
		if (orgGroupCodes==null)
			orgGroupCodes=new String[0];
		return orgGroupCodes;
	}

	public void setOrgGroupCodes(String[] orgGroupCodes) {
		this.orgGroupCodes = orgGroupCodes;
	}

	public String[] getOrgGroupIds() {
		if (orgGroupIds==null)
			orgGroupIds=new String[0];
		return orgGroupIds;
	}

	public void setOrgGroupIds(String[] orgGroupIds) {
		this.orgGroupIds = orgGroupIds;
	}

	public String[] getConfigIds() {
		if (configIds==null)
			configIds=new String[0];
		return configIds;
	}

	public void setConfigIds(String[] configIds) {
		this.configIds = configIds;
	}
	
	
}

	
