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
package com.arahant.services.standard.hr.employeeGarnishment;
import com.arahant.business.BGarnishment;


/**
 * 
 *
 *
 */
public class ListGarnishmentsReturnItem {
	
	public ListGarnishmentsReturnItem()
	{
		
	}

	ListGarnishmentsReturnItem (BGarnishment bc)
	{
		
		deductType=bc.getDeductType();
		amountType=bc.getAmountType();
		maxAmount=bc.getMaxAmount();
		amount=bc.getAmount();
		finalDate=bc.getFinalDate();
		startDate=bc.getStartDate();
		docketNumber=bc.getDocketNumber();
		id=bc.getId();
		typeDescription=bc.getTypeDescription();
		typeId=bc.getTypeId();

	}
	
	private String deductType;
	private String amountType;
	private double maxAmount;
	private double amount;
	private int finalDate;
	private int startDate;
	private String docketNumber;
	private String id;
	private String typeDescription;
	private String typeId;
	

	public String getAmountType()
	{
		return amountType;
	}
	public void setAmountType(String amountType)
	{
		this.amountType=amountType;
	}
	public double getMaxAmount()
	{
		return maxAmount;
	}
	public void setMaxAmount(double maxAmount)
	{
		this.maxAmount=maxAmount;
	}
	public double getAmount()
	{
		return amount;
	}
	public void setAmount(double amount)
	{
		this.amount=amount;
	}
	public int getFinalDate()
	{
		return finalDate;
	}
	public void setFinalDate(int finalDate)
	{
		this.finalDate=finalDate;
	}
	public int getStartDate()
	{
		return startDate;
	}
	public void setStartDate(int startDate)
	{
		this.startDate=startDate;
	}
	public String getDocketNumber()
	{
		return docketNumber;
	}
	public void setDocketNumber(String docketNumber)
	{
		this.docketNumber=docketNumber;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeductType() {
		return deductType;
	}

	public void setDeductType(String deductType) {
		this.deductType = deductType;
	}

	public String getTypeDescription() {
		return typeDescription;
	}

	public void setTypeDescription(String typeDescription) {
		this.typeDescription = typeDescription;
	}


	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

}

	
