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
package com.arahant.services.standard.crm.salesQueue;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;
import com.arahant.business.BSearchMetaInput;
import com.arahant.services.SearchMetaInput;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchSalesQueueInput extends TransmitInputBase {

	@Validation (required=false)
	private String prospectName;
	@Validation (min=2,max=5,required=false)
	private int prospectNameSearchType;
	@Validation (required=false)
	private String prospectStatusId;
	@Validation (type="date",required=false)
	private int contactDate;
	@Validation (type="date",required=false)
	private int lastContactFrom;
	@Validation (type="date",required=false)
	private int lastContactTo;
	@Validation (required=false)
	private String activityId;
	@Validation (required=false)
	private String resultId;
	@Validation(required=false)
	private boolean active;
	@Validation (required=false)
	private SearchMetaInput searchMeta;
	@Validation(required=false)
	private String employeeId;
	

	public String getProspectName()
	{
		return modifyForSearch(prospectName,prospectNameSearchType);
	}
	public void setProspectName(String prospectName)
	{
		this.prospectName=prospectName;
	}
	public int getProspectNameSearchType()
	{
		return prospectNameSearchType;
	}
	public void setProspectNameSearchType(int prospectNameSearchType)
	{
		this.prospectNameSearchType=prospectNameSearchType;
	}
	public String getProspectStatusId()
	{
		return prospectStatusId;
	}
	public void setProspectStatusId(String prospectStatusId)
	{
		this.prospectStatusId=prospectStatusId;
	}
	public int getContactDate()
	{
		return contactDate;
	}
	public void setContactDate(int contactDate)
	{
		this.contactDate=contactDate;
	}
	public int getLastContactFrom()
	{
		return lastContactFrom;
	}
	public void setLastContactFrom(int lastContactFrom)
	{
		this.lastContactFrom=lastContactFrom;
	}
	public int getLastContactTo()
	{
		return lastContactTo;
	}
	public void setLastContactTo(int lastContactTo)
	{
		this.lastContactTo=lastContactTo;
	}
	public String getActivityId()
	{
		return activityId;
	}
	public void setActivityId(String activityId)
	{
		this.activityId=activityId;
	}
	public String getResultId()
	{
		return resultId;
	}
	public void setResultId(String resultId)
	{
		this.resultId=resultId;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public SearchMetaInput getSearchMeta() {
		return searchMeta;
	}

	public void setSearchMeta(SearchMetaInput searchMeta) {
		this.searchMeta = searchMeta;
	}

	BSearchMetaInput getSearchMetaInput() {
		if (searchMeta == null) {
			return new BSearchMetaInput(0, true, false, 0);
		} else {
			return new BSearchMetaInput(searchMeta,new String[]{"prospectName", "prospectStatus","lastContactDate","activity","result","scheduledContact", "addedDate"});
		 }

	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
}

	
