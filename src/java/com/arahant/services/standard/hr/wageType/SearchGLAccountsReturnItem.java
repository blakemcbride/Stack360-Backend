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
package com.arahant.services.standard.hr.wageType;
import com.arahant.business.BGlAccount;


/**
 * 
 *
 *
 */
public class SearchGLAccountsReturnItem {
	
	public SearchGLAccountsReturnItem()
	{
	}

	SearchGLAccountsReturnItem (BGlAccount bc)
	{
		
		accountNumber=bc.getAccountNumber();
		accountName=bc.getAccountName();
		accountTypeFormatted=bc.getAccountTypeFormatted();
		id=bc.getGlAccountId();

	}
	
	private String accountNumber;
	private String accountName;
	private String accountTypeFormatted;
	private String id;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccountNumber()
	{
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber)
	{
		this.accountNumber=accountNumber;
	}
	public String getAccountName()
	{
		return accountName;
	}
	public void setAccountName(String accountName)
	{
		this.accountName=accountName;
	}
	public String getAccountTypeFormatted()
	{
		return accountTypeFormatted;
	}
	public void setAccountTypeFormatted(String accountTypeFormatted)
	{
		this.accountTypeFormatted=accountTypeFormatted;
	}

}

	
