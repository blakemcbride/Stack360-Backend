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
package com.arahant.services.standard.inventory.quote;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BLocationCost;
import com.arahant.annotation.Validation;


public class NewLocationCostInput extends TransmitInputBase {

	@Validation (table = "location_cost",column = "description",required = true)
	private String description;
	@Validation (table = "location_cost",column = "location_cost",required = false)
	private double cost;
	

	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public double getCost()
	{
		return cost;
	}
	public void setCost(double cost)
	{
		this.cost = cost;
	}

	void setData(BLocationCost bc)
	{
		bc.setDescription(description);
		bc.setLocationCost(cost);
	}
	
}

	
