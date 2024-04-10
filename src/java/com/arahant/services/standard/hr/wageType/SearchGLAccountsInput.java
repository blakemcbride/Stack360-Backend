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

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchGLAccountsInput extends TransmitInputBase {

	@Validation (required=false)
	private String accountNumber;
	@Validation (min=2,max=5,required=false)
	private int accountNumberSearchType;
	@Validation (required=false)
	private String accountName;
	@Validation (min=2,max=5,required=false)
	private int accountNameSearchType;
	@Validation (required=false)
	private String selectFromId;
	@Validation (required=false)
	private String selectFromProductServiceId;
	@Validation (required=false)
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	

	public String getAccountNumber()
	{
		return modifyForSearch(accountNumber,accountNumberSearchType);
	}
	public void setAccountNumber(String accountNumber)
	{
		this.accountNumber=accountNumber;
	}
	public int getAccountNumberSearchType()
	{
		return accountNumberSearchType;
	}
	public void setAccountNumberSearchType(int accountNumberSearchType)
	{
		this.accountNumberSearchType=accountNumberSearchType;
	}
	public String getAccountName()
	{
		return modifyForSearch(accountName,accountNameSearchType);
	}
	public void setAccountName(String accountName)
	{
		this.accountName=accountName;
	}
	public int getAccountNameSearchType()
	{
		return accountNameSearchType;
	}
	public void setAccountNameSearchType(int accountNameSearchType)
	{
		this.accountNameSearchType=accountNameSearchType;
	}
	public String getSelectFromId()
	{
		return selectFromId;
	}
	public void setSelectFromId(String selectFromId)
	{
		this.selectFromId=selectFromId;
	}
	public String getSelectFromProductServiceId() {
		return selectFromProductServiceId;
	}
	public void setSelectFromProductServiceId(String selectFromProductServiceId) {
		this.selectFromProductServiceId = selectFromProductServiceId;
	}
	


}

	
