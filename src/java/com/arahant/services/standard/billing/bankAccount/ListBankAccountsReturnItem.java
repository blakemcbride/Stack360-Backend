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
package com.arahant.services.standard.billing.bankAccount;
import com.arahant.business.BBankAccount;


/**
 * 
 *
 *
 */
public class ListBankAccountsReturnItem {
	
	public ListBankAccountsReturnItem()
	{
	}

	ListBankAccountsReturnItem (BBankAccount bc)
	{
		
		id=bc.getId();
		orgGroupId=bc.getOrgGroupId();
		orgGroupName=bc.getOrgGroupName();
		code=bc.getCode();
		name=bc.getName();
		routingNumber=bc.getRoutingNumber();
		accountNumber=bc.getAccountNumber();
		type=bc.getType();
		inactiveDate=bc.getInactiveDate();

	}
	
	private String id;
	private String orgGroupId;
	private String orgGroupName;
	private String code;
	private String name;
	private String routingNumber;
	private String accountNumber;
	private String type;
	private int inactiveDate;
	

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getOrgGroupId()
	{
		return orgGroupId;
	}
	public void setOrgGroupId(String orgGroupId)
	{
		this.orgGroupId=orgGroupId;
	}
	public String getOrgGroupName()
	{
		return orgGroupName;
	}
	public void setOrgGroupName(String orgGroupName)
	{
		this.orgGroupName=orgGroupName;
	}
	public String getCode()
	{
		return code;
	}
	public void setCode(String code)
	{
		this.code=code;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public String getRoutingNumber()
	{
		return routingNumber;
	}
	public void setRoutingNumber(String routingNumber)
	{
		this.routingNumber=routingNumber;
	}
	public String getAccountNumber()
	{
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber)
	{
		this.accountNumber=accountNumber;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type=type;
	}
	public int getInactiveDate()
	{
		return inactiveDate;
	}
	public void setInactiveDate(int inactiveDate)
	{
		this.inactiveDate=inactiveDate;
	}

}

	
