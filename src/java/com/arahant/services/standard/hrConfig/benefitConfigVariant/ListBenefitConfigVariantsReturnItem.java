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
package com.arahant.services.standard.hrConfig.benefitConfigVariant;
import com.arahant.business.BTimeOffAccrualCalc;

public class ListBenefitConfigVariantsReturnItem {
	
	public ListBenefitConfigVariantsReturnItem()
	{
		
	}

	ListBenefitConfigVariantsReturnItem (BTimeOffAccrualCalc bc)
	{
		if (bc.getMethod() == 'A')
			method="Annual";
		else if (bc.getMethod() == 'H')
			method="Hourly";
		else if(bc.getMethod() == 'P')
			method = "Pay Period";
		else if(bc.getMethod() == 'M')
			method = "Monthly";
		firstActive=bc.getFirstActiveDate();
		lastActive=bc.getLastActiveDate();
		id=bc.getTimeOffAccrualCalcId();
		trialPeriod=bc.getTrialPeriod();
		if (bc.getStartFlag() == 'C')
			startFlag="Calendar Year";
		else if (bc.getStartFlag() == 'S')
			startFlag="Specific Date";
		else if (bc.getStartFlag() == 'F')
			startFlag="Fiscal Year";
		else if (bc.getStartFlag() == 'H')
			startFlag="Hire Year";
		carryOverAmount=bc.getCarryOverAmount();
		carryOverPercentage=bc.getCarryOverPercentage();
		periodStartDate=bc.getPeriodStartDate();
		if(bc.getAccrualType().equals("D"))
			accrualType="D";
		else
			accrualType="R";
	}
	private String accrualType;
	private String method;
	private int firstActive;
	private int lastActive;
	private String id;
	private int trialPeriod;
	private String startFlag;
	private int carryOverAmount;
	private float carryOverPercentage;
	private int periodStartDate;

	public String getAccrualType() {
		return accrualType;
	}

	public void setAccrualType(String accrualType) {
		this.accrualType = accrualType;
	}

	public String getMethod()
	{
		return method;
	}
	public void setMethod(String method)
	{
		this.method=method;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getStartFlag()
	{
		return startFlag;
	}
	public void setStartFlag(String startFlag)
	{
		this.startFlag=startFlag;
	}

	public int getCarryOverAmount() {
		return carryOverAmount;
	}

	public void setCarryOverAmount(int carryOverAmount) {
		this.carryOverAmount = carryOverAmount;
	}

	public float getCarryOverPercentage() {
		return carryOverPercentage;
	}

	public void setCarryOverPercentage(float carryOverPercentage) {
		this.carryOverPercentage = carryOverPercentage;
	}
	
	public int getPeriodStartDate()
	{
		return periodStartDate;
	}
	public void setPeriodStartDate(int periodStartDate)
	{
		this.periodStartDate=periodStartDate;
	}

	public int getFirstActive() {
		return firstActive;
	}

	public void setFirstActive(int firstActive) {
		this.firstActive = firstActive;
	}

	public int getLastActive() {
		return lastActive;
	}

	public void setLastActive(int lastActive) {
		this.lastActive = lastActive;
	}

	public int getTrialPeriod() {
		return trialPeriod;
	}

	public void setTrialPeriod(int trialPeriod) {
		this.trialPeriod = trialPeriod;
	}

}

	
