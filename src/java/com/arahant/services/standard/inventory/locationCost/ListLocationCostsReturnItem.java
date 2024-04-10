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
package com.arahant.services.standard.inventory.locationCost;

import com.arahant.business.BLocationCost;


public class ListLocationCostsReturnItem {

	private String locationCostId;
	private String description;
	private double locationCost;

	public String getLocationCostId() {
		return locationCostId;
	}

	public void setLocationCostId(String locationCostId) {
		this.locationCostId = locationCostId;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public double getLocationCost()
	{
		return locationCost;
	}
	public void setLocationCost(double locationCost)
	{
		this.locationCost = locationCost;
	}

	public ListLocationCostsReturnItem() {
		
	}

	ListLocationCostsReturnItem(BLocationCost bc)
	{
		locationCostId = bc.getLocationCostId();
		description = bc.getDescription();
		locationCost = bc.getLocationCost();
	}
	
}

	
