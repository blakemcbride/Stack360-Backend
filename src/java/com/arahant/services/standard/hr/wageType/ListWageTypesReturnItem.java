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
import com.arahant.business.BWageType;


/**
 * 
 *
 *
 */
public class ListWageTypesReturnItem {
	
	public ListWageTypesReturnItem()
	{
		
	}

	ListWageTypesReturnItem (BWageType bc)
	{
		
		id=bc.getId();
		periodType=bc.getPeriodType();
		type=bc.getType();
		expenseAccountId=bc.getExpenseAccountId();
		liabilityAccountId=bc.getLiabilityAccountId();
		firstActiveDate=bc.getFirstActiveDate();
		lastActiveDate=bc.getLastActiveDate();
		deduction=bc.getIsDeduction();
        name=bc.getName();
		payrollInterfaceCode=bc.getPayrollInterfaceCode();
	}
	
	private String id;
	private int periodType;
	private int type;
	private String expenseAccountId;
	private String liabilityAccountId;
	private int firstActiveDate;
	private int lastActiveDate;
	private boolean deduction;
    private String name;
	private String payrollInterfaceCode;

	public String getPayrollInterfaceCode() {
		return payrollInterfaceCode;
	}

	public void setPayrollInterfaceCode(String payrollInterfaceCode) {
		this.payrollInterfaceCode = payrollInterfaceCode;
	}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
	

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public int getPeriodType()
	{
		return periodType;
	}
	public void setPeriodType(int periodType)
	{
		this.periodType=periodType;
	}
	public int getType()
	{
		return type;
	}
	public void setType(int type)
	{
		this.type=type;
	}
	public String getExpenseAccountId()
	{
		return expenseAccountId;
	}
	public void setExpenseAccountId(String expenseAccountId)
	{
		this.expenseAccountId=expenseAccountId;
	}
	public String getLiabilityAccountId()
	{
		return liabilityAccountId;
	}
	public void setLiabilityAccountId(String liabilityAccountId)
	{
		this.liabilityAccountId=liabilityAccountId;
	}
	public int getFirstActiveDate()
	{
		return firstActiveDate;
	}
	public void setFirstActiveDate(int firstActiveDate)
	{
		this.firstActiveDate=firstActiveDate;
	}
	public int getLastActiveDate()
	{
		return lastActiveDate;
	}
	public void setLastActiveDate(int lastActiveDate)
	{
		this.lastActiveDate=lastActiveDate;
	}

	public boolean isDeduction() {
		return deduction;
	}

	public void setDeduction(boolean deduction) {
		this.deduction = deduction;
	}
	

}

	
