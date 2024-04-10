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
package com.arahant.services.standard.crm.salesPipeline;
import com.arahant.business.BProspectStatus;
import java.util.StringTokenizer;

public class GetMetaReturnItem {
	
	public GetMetaReturnItem()
	{
		
	}

	GetMetaReturnItem (BProspectStatus bc)
	{
		statusName=bc.getCode();
		statusId=bc.getId();
		statusVar=makeVar(bc.getCode());
		statusDays="" + bc.getFallbackDays();
	}
	
	private String statusName;
	private String statusId;
	private String statusVar;
	private String statusDays;
	
	public String getStatusName()
	{
		return statusName;
	}
	public void setStatusName(String statusName)
	{
		this.statusName=statusName;
	}
	public String getStatusId()
	{
		return statusId;
	}
	public void setStatusId(String statusId)
	{
		this.statusId=statusId;
	}

	public String makeVar(String x)
	{
		String var = "";
		StringTokenizer st = new StringTokenizer(x);
		String temp = "";
		char f;

		//do first word
		temp = st.nextToken();
		temp = temp.toLowerCase();
		var += temp;

		//do the rest
		while (st.hasMoreTokens()) {
			temp = st.nextToken();
			temp = temp.toUpperCase();
			f = temp.charAt(0);
			temp = temp.toLowerCase();
			temp = f + temp.substring(1, temp.length());
			var += temp;
		}

		return var;
	}

	public String getStatusVar() {
		return statusVar;
	}

	public void setStatusVar(String statusVar) {
		this.statusVar = statusVar;
	}

	public String getStatusDays() {
		return statusDays;
	}

	public void setStatusDays(String statusDays) {
		this.statusDays = statusDays;
	}
}

	
