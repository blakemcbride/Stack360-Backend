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
package com.arahant.services.standard.misc.agentApproval;
import com.arahant.business.BAgentJoin;

public class ListAgentsForCompanyReturnItem {
	
	public ListAgentsForCompanyReturnItem()
	{
		
	}

	ListAgentsForCompanyReturnItem (BAgentJoin bc)
	{
		agentName=bc.getAgent().getNameLFM();
		agencyName=bc.getCompany().getName();
		agentJoinId=bc.getAgentJoinId();
		select=bc.getApproved()=='Y';
	}
	
	private String agentName;
	private String agencyName;
	private String agentJoinId;
	private boolean select;
	

	public String getAgentName()
	{
		return agentName;
	}
	public void setAgentName(String agentName)
	{
		this.agentName=agentName;
	}
	public String getAgencyName()
	{
		return agencyName;
	}
	public void setAgencyName(String agencyName)
	{
		this.agencyName=agencyName;
	}
	public String getAgentJoinId() {
		return agentJoinId;
	}

	public void setAgentJoinId(String agentJoinId) {
		this.agentJoinId = agentJoinId;
	}
	public boolean getSelect()
	{
		return select;
	}
	public void setSelect(boolean select)
	{
		this.select=select;
	}

}

	
