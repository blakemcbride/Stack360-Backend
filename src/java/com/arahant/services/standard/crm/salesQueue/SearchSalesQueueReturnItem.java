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
import com.arahant.business.BProspectCompany;
import com.arahant.business.BProspectLog;
import com.arahant.utils.DateUtils;


/**
 * 
 *
 *
 */
public class SearchSalesQueueReturnItem {
	
	public SearchSalesQueueReturnItem()
	{
		
	}

	SearchSalesQueueReturnItem (BProspectCompany bc)
	{
		BProspectLog bpl = bc.getLastLog();

		prospectName=bc.getName();
		prospectStatus=bc.getStatus().getCode();

		activityId="";
		resultId="";

		String text = "";
		if (bpl != null && bpl.getBean()!=null)
		{
			logId=bpl.getProspectLogId();
			lastContactDate=bc.getLastLogDate();
			activity=bpl.getSalesActivity().getActivityCode();
			result=bpl.getSalesActivityResult().getDescription();
			activityId=bpl.getSalesActivity().getSalesActivityId();
			resultId=bpl.getSalesActivityResult().getSalesActivityResultId();

			if (bc.getNextContactDate() != 0)
			{
				int days = (int)DateUtils.getDaysBetween(bc.getNextContactDate(), DateUtils.now());

				if (days > 1)
					text = "In " + days + " Days";
				else if (days == 1)
					text = "Tomorrow";
				else if (days == 0)
					text = "Today";
				else if (days == -1)
					text = "Yesterday";
				else
					text = Math.abs(days) + " Days Ago";
			}
			else
				text = "No Follow Up";
		}
		else
		{
			lastContactDate=0;
			activity="";
			result="";
			text = "Never Contacted";
		}

		scheduledContact=text;
		prospectId=bc.getCompanyId();

		addedDate=DateUtils.getDate(bc.getWhenAdded());

	}
	
	private String prospectName;
	private String prospectStatus;
	private int lastContactDate;
	private String activity;
	private String result;
	private String scheduledContact;
	private String prospectId;
	private String logId;
	private int addedDate;
	private String activityId;
	private String resultId;
	
	public String getProspectName()
	{
		return prospectName;
	}
	public void setProspectName(String prospectName)
	{
		this.prospectName=prospectName;
	}
	public String getProspectStatus()
	{
		return prospectStatus;
	}
	public void setProspectStatus(String prospectStatus)
	{
		this.prospectStatus=prospectStatus;
	}
	public int getLastContactDate()
	{
		return lastContactDate;
	}
	public void setLastContactDate(int lastContactDate)
	{
		this.lastContactDate=lastContactDate;
	}
	public String getActivity()
	{
		return activity;
	}
	public void setActivity(String activity)
	{
		this.activity=activity;
	}
	public String getResult()
	{
		return result;
	}
	public void setResult(String result)
	{
		this.result=result;
	}
	public String getScheduledContact()
	{
		return scheduledContact;
	}
	public void setScheduledContact(String scheduledContact)
	{
		this.scheduledContact=scheduledContact;
	}
	public String getProspectId()
	{
		return prospectId;
	}
	public void setProspectId(String prospectId)
	{
		this.prospectId=prospectId;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public int getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(int addedDate) {
		this.addedDate = addedDate;
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

	
