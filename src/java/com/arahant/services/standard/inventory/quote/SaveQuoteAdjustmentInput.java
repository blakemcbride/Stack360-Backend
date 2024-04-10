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
import com.arahant.business.BQuoteAdjustment;
import com.arahant.annotation.Validation;


public class SaveQuoteAdjustmentInput extends TransmitInputBase {

	@Validation (table = "quote_adjustment",column = "adjustment_description",required = true)
	private String description;
	@Validation (table = "quote_adjustment",column = "quantity",required = false)
	private short quantity;
	@Validation (table = "quote_adjustment",column = "adjusted_cost",required = true)
	private double cost;
	@Validation (table = "quote_adjustment",column = "quote_adjustment_id",required = true)
	private String quoteAdjustmentId;
	

	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public short getQuantity()
	{
		return quantity;
	}
	public void setQuantity(short quantity)
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
	public String getQuoteAdjustmentId()
	{
		return quoteAdjustmentId;
	}
	public void setQuoteAdjustmentId(String quoteAdjustmentId)
	{
		this.quoteAdjustmentId = quoteAdjustmentId;
	}


	void setData(BQuoteAdjustment bc)
	{
		bc.setAdjustmentDescription(description);
		bc.setQuantity(quantity);
		bc.setAdjustedCost(cost);
	}
	
}

	
