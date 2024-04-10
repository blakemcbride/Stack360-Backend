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
import com.arahant.business.BTimeOffAccrualCalcSeniority;
import com.arahant.annotation.Validation;

public class SaveBenefitConfigVariantSeniorityInput extends TransmitInputBase {

	void setData(BTimeOffAccrualCalcSeniority bc)
	{
		bc.setYearsOfService(yearsOfService);
		bc.setHoursAccrued(hoursAccrued);
	}
	
	@Validation (table="time_off_accrual_seniority",column="max_years_of_service",required=false)
	private int yearsOfService;
	@Validation (table="time_off_accrual_seniority",column="hours_accrued",required=true)
	private double hoursAccrued;
	@Validation (min=1,max=16,required=true)
	private String id;
	

	public int getYearsOfService()
	{
		return yearsOfService;
	}
	public void setYearsOfService(int yearsOfService)
	{
		this.yearsOfService=yearsOfService;
	}
	public double getHoursAccrued()
	{
		return hoursAccrued;
	}
	public void setHoursAccrued(double hoursAccrued)
	{
		this.hoursAccrued=hoursAccrued;
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

	
