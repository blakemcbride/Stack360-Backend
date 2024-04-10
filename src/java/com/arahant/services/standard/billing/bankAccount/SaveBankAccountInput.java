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

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BBankAccount;
import com.arahant.annotation.Validation;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SaveBankAccountInput extends TransmitInputBase {

	void setData(BBankAccount bc)
	{
		
		bc.setOrgGroupId(orgGroupId);
		bc.setCode(code);
		bc.setName(name);
		bc.setAccountNumber(accountNumber);
		bc.setRoutingNumber(routingNumber);
		bc.setType(type);
		bc.setInactiveDate(inactiveDate);

	}
	
	@Validation (table="bank_account",column="org_group_id",required=true)
	private String orgGroupId;
	@Validation (table="bank_account",column="bank_id",required=true)
	private String code;
	@Validation (table="bank_account",column="bank_name",required=true)
	private String name;
	@Validation (table="bank_account",column="bank_account",required=true)
	private String accountNumber;
	@Validation (table="bank_account",column="bank_route",required=true)
	private String routingNumber;
	@Validation (table="bank_account",column="account_type",required=true)
	private String type;
	@Validation (required=false, type="date")
	private int inactiveDate;
	@Validation (min=1,max=16,required=true)
	private String id;
	

	public String getOrgGroupId()
	{
		return orgGroupId;
	}
	public void setOrgGroupId(String orgGroupId)
	{
		this.orgGroupId=orgGroupId;
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
	public String getAccountNumber()
	{
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber)
	{
		this.accountNumber=accountNumber;
	}
	public String getRoutingNumber()
	{
		return routingNumber;
	}
	public void setRoutingNumber(String routingNumber)
	{
		this.routingNumber=routingNumber;
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
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}

}

	
