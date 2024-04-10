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
package com.arahant.services.standard.misc.interfaceLog;
import com.arahant.business.BInterfaceLog;
import com.arahant.utils.DateUtils;
import com.arahant.beans.InterfaceLog;
import com.arahant.business.BCompany;


/**
 * 
 *
 *
 */
public class ListInterfaceLogsReturnItem {
	
	private String interfaceCode;
	private int lastRun;
	private String statusMessage;
	private String statusCode;
	private String companyName;
	private String companyId;

	public ListInterfaceLogsReturnItem()
	{
		
	}

	ListInterfaceLogsReturnItem (BInterfaceLog bc)
	{
		interfaceCode = (bc.getInterface() == InterfaceLog.INTERFACE_COBRA_GUARD) ? "Cobra Guard" : "Unknown";
		lastRun = DateUtils.getDate(bc.getLastRun());
		statusMessage = bc.getMessage();
		if(bc.getStatus() == InterfaceLog.STATUS_ERROR)
			statusCode = "Error";
		else if(bc.getStatus() == InterfaceLog.STATUS_OK)
			statusCode = "Successful";
		companyName = new BCompany(bc.getCompanyId()).getName();
		companyId = bc.getCompanyId();
	}

	public String getInterfaceCode() {
		return interfaceCode;
	}

	public void setInterfaceCode(String interfaceCode) {
		this.interfaceCode = interfaceCode;
	}

	public int getLastRun() {
		return lastRun;
	}

	public void setLastRun(int lastRun) {
		this.lastRun = lastRun;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}


}

	
