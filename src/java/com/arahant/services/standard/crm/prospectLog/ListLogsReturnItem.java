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
package com.arahant.services.standard.crm.prospectLog;
import com.arahant.business.BProspectLog;
import com.arahant.services.TransmitReturnBase;


/**
 *
 *
 *
 */
public class ListLogsReturnItem {
	
	public ListLogsReturnItem()
	{
		
	}

	ListLogsReturnItem (BProspectLog bc)
	{
		contactDate=bc.getContactDate();
		contactTime=bc.getContactTime();
		contactText=TransmitReturnBase.preview(bc.getContactText());
		id=bc.getProspectLogId();
		if (bc.getSalesActivity() != null)
			activityId=bc.getSalesActivity().getSalesActivityId();
		else
			activityId="";

		if (bc.getSalesActivityResult() != null)
			resultId=bc.getSalesActivityResult().getSalesActivityResultId();
		else
			resultId="";
	}
	
	private int contactDate;
	private int contactTime;
	private String contactText;
	private String id;
	private String activityId;
	private String resultId;

	public int getContactDate()
	{
		return contactDate;
	}
	public void setContactDate(int contactDate)
	{
		this.contactDate=contactDate;
	}
	public int getContactTime()
	{
		return contactTime;
	}
	public void setContactTime(int contactTime)
	{
		this.contactTime=contactTime;
	}
	public String getContactText()
	{
		return contactText;
	}
	public void setContactText(String contactText)
	{
		this.contactText=contactText;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getResultId() {
		return resultId;
	}

	public void setResultId(String resultId) {
		this.resultId = resultId;
	}

}

	