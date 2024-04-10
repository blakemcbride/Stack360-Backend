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
package com.arahant.services.standard.project.projectBilling;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BProject;


/**
 * 
 *
 *
 */
public class GetUpdatedEstimateAndActualReturn extends TransmitReturnBase {

	
	
	private double estimate;
private double actualBillable;
private double actualNonBillable;
;

	public double getEstimate()
	{
		return estimate;
	}
	public void setEstimate(double estimate)
	{
		this.estimate=estimate;
	}
	public double getActualBillable()
	{
		return actualBillable;
	}
	public void setActualBillable(double actualBillable)
	{
		this.actualBillable=actualBillable;
	}
	public double getActualNonBillable()
	{
		return actualNonBillable;
	}
	public void setActualNonBillable(double actualNonBillable)
	{
		this.actualNonBillable=actualNonBillable;
	}

}

	
