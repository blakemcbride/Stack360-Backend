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

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BTimeOffAccrualCalc;
import com.arahant.annotation.Validation;

public class SaveBenefitConfigVariantInput extends TransmitInputBase {

	void setData(BTimeOffAccrualCalc bc)
	{
		bc.setMethod(method);
		bc.setFirstActiveDate(firstActive);
		bc.setLastActiveDate(lastActive);
		bc.setTrialPeriod(trialPeriod);
		bc.setStartFlag(startFlag);
		bc.setCarryOverAmount(carryOverAmount);
		bc.setCarryOverPercentage(carryOverPercentage);
		bc.setAccrualType(accrualType.charAt(0));
		bc.setPeriodStartDate(periodStartDate);
	}

	@Validation (table="time_off_accrual_calc",column="accrual_type", required=true)
	private String accrualType;
	@Validation (table="time_off_accrual_calc",column="accrual_method",required=true)
	private String method;
	@Validation (type="date", table="time_off_accrual_calc",column="first_active_date",required=false)
	private int firstActive;
	@Validation (type="date", table="time_off_accrual_calc",column="last_active_date",required=false)
	private int lastActive;
	@Validation (min=0, table="time_off_accrual_calc",column="trial_period",required=false)
	private int trialPeriod;
	@Validation (table="time_off_accrual_calc",column="period_start",required=true)
	private String startFlag;
	@Validation (min=0, table="time_off_accrual_calc",column="max_carry_over_hours",required=false)
	private int carryOverAmount;
	@Validation (min=0, max=100, table="time_off_accrual_calc",column="carry_over_percentage",required=false)
	private int carryOverPercentage;
	@Validation (required=true)
	private String id;
	@Validation (min=101, max=1231, table="time_off_accrual_calc", column="period_start_date", required=false)
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
	public int getFirstActive()
	{
		return firstActive;
	}
	public void setFirstActive(int firstActive)
	{
		this.firstActive=firstActive;
	}
	public int getLastActive()
	{
		return lastActive;
	}
	public void setLastActive(int lastActive)
	{
		this.lastActive=lastActive;
	}
	public int getTrialPeriod()
	{
		return trialPeriod;
	}
	public void setTrialPeriod(int trialPeriod)
	{
		this.trialPeriod=trialPeriod;
	}
	public String getStartFlag()
	{
		return startFlag;
	}
	public void setStartFlag(String startFlag)
	{
		this.startFlag=startFlag;
	}
	public int getCarryOverAmount()
	{
		return carryOverAmount;
	}
	public void setCarryOverAmount(int carryOverAmount)
	{
		this.carryOverAmount=carryOverAmount;
	}

	public int getCarryOverPercentage() {
		return carryOverPercentage;
	}

	public void setCarryOverPercentage(int carryOverPercentage) {
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

	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}

}

	
