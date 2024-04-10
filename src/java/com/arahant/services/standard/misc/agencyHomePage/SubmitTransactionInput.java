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
package com.arahant.services.standard.misc.agencyHomePage;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;

public class SubmitTransactionInput extends TransmitInputBase {

	@Validation (required=true)
	private String group;
	@Validation (required=true)
	private String employee;
	@Validation (required=true)
	private String carrier;
	@Validation (required=true)
	private String transactionType;
	

	public String getGroup()
	{
		return group;
	}
	public void setGroup(String group)
	{
		this.group=group;
	}
	public String getEmployee()
	{
		return employee;
	}
	public void setEmployee(String employee)
	{
		this.employee=employee;
	}
	public String getCarrier()
	{
		return carrier;
	}
	public void setCarrier(String carrier)
	{
		this.carrier=carrier;
	}
	public String getTransactionType()
	{
		return transactionType;
	}
	public void setTransactionType(String transactionType)
	{
		this.transactionType=transactionType;
	}


}

	
