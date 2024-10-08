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
package com.arahant.services.standard.billing.bankDraftBatch;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BBillingBatch;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class PostBatchInput extends TransmitInputBase {
	
	@Validation (min=1,max=16,required=true)
	private String id;
	@Validation (type="date",required=true)
	private int date;
	@Validation (table="receipt",column="reference",required=false)
	private String confirmationNumber;
	@Validation (min=0,required=false)
	private double amountCheck;
	

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public int getDate()
	{
		return date;
	}
	public void setDate(int date)
	{
		this.date=date;
	}
	public String getConfirmationNumber()
	{
		return confirmationNumber;
	}
	public void setConfirmationNumber(String confirmationNumber)
	{
		this.confirmationNumber=confirmationNumber;
	}
	public double getAmountCheck()
	{
		return amountCheck;
	}
	public void setAmountCheck(double amountCheck)
	{
		this.amountCheck=amountCheck;
	}

}

	
