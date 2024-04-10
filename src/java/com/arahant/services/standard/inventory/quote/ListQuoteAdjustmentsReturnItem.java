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

import com.arahant.business.BQuoteAdjustment;


public class ListQuoteAdjustmentsReturnItem
{
	private String quoteAdjustmentId;
	private String description;
	private double quantity;
	private double cost;
	

	public String getQuoteAdjustmentId()
	{
		return quoteAdjustmentId;
	}
	public void setQuoteAdjustmentId(String quoteAdjustmentId)
	{
		this.quoteAdjustmentId = quoteAdjustmentId;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public double getQuantity()
	{
		return quantity;
	}
	public void setQuantity(double quantity)
	{
		this.quantity = quantity;
	}
	public double getCost()
	{
		return cost;
	}
	public void setCost(double cost)
	{
		this.cost = cost;
	}

	public ListQuoteAdjustmentsReturnItem() {
		
	}

	ListQuoteAdjustmentsReturnItem(BQuoteAdjustment bc)
	{
		quoteAdjustmentId = bc.getQuoteAdjustmentId();
		description = bc.getAdjustmentDescription();
		quantity = bc.getQuantity();
		cost = bc.getAdjustedCost();
	}
	
}

	
