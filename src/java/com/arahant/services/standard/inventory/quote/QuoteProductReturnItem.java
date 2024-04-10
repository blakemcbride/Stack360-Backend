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
 *
 */

package com.arahant.services.standard.inventory.quote;

import com.arahant.beans.QuoteProduct;
import com.arahant.business.BClientCompany;
import com.arahant.utils.ArahantSession;


public class QuoteProductReturnItem
{
	private String productName;
	private String productId;
	private int quantity;
	private double retailPrice;
	private double adjustedRetailPrice;
	private String quoteProductId;
	private double manHours;
	private double cost;
	private String sellAs;
	private double defaultRate;

	public QuoteProductReturnItem() {
	}

	public QuoteProductReturnItem(QuoteProduct qp)
	{
		productName = qp.getProduct().getDescription();
		productId = qp.getProduct().getProductId();
		quantity = qp.getQuantity();
		retailPrice = qp.getRetailPrice();
		
		quoteProductId = qp.getQuoteProductId();
		manHours = qp.getProduct().getManHours();
		sellAs = qp.getSellAsType() + "";
		
		defaultRate = new BClientCompany(qp.getQuote().getClient().getOrgGroupId()).getBillingRate();
		if(defaultRate == 0)
		{
			defaultRate = ArahantSession.getHSU().getCurrentCompany().getBillingRate();
		}
		adjustedRetailPrice = qp.getAdjustedRetailPrice();
		if(adjustedRetailPrice == 0)
		{
			if(sellAs.equals("S"))
			{
				adjustedRetailPrice = defaultRate;
			}
			else if(sellAs.equals("P"))
			{
				adjustedRetailPrice = retailPrice;
			}
			else if(sellAs.equals("B"))
			{
				adjustedRetailPrice = 0;
			}
		}
		if(sellAs.equals("S"))
		{
			cost = adjustedRetailPrice * quantity * manHours;
		}
		else if(sellAs.equals("P"))
		{
			cost = adjustedRetailPrice * quantity;
		}
		else if(sellAs.equals("B"))
		{
			cost = (retailPrice * quantity) + (defaultRate * quantity * manHours);
		}
	}

	public double getDefaultRate() {
		return defaultRate;
	}

	public void setDefaultRate(double defaultRate) {
		this.defaultRate = defaultRate;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public String getSellAs() {
		return sellAs;
	}

	public void setSellAs(String sellAs) {
		this.sellAs = sellAs;
	}

	
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(double retailPrice) {
		this.retailPrice = retailPrice;
	}

	public double getAdjustedRetailPrice() {
		return adjustedRetailPrice;
	}

	public void setAdjustedRetailPrice(double adjustedRetailPrice) {
		this.adjustedRetailPrice = adjustedRetailPrice;
	}

	public String getQuoteProductId() {
		return quoteProductId;
	}

	public void setQuoteProductId(String quoteProductId) {
		this.quoteProductId = quoteProductId;
	}

	public double getManHours() {
		return manHours;
	}

	public void setManHours(double manHours) {
		this.manHours = manHours;
	}
}
