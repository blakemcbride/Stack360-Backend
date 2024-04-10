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
package com.arahant.services.standard.inventory.quoteTemplate;

import com.arahant.business.BQuoteTemplateProduct;


public class ListQuoteTemplateProductsReturnItem {

	private String templateProductId;
	private String productId;
	private double manHours;
	private double retailPrice;
	private String productName;
	private int defaultQuantity;
	private boolean dirty;
	private String sellAs;

	public String getSellAs() {
		return sellAs;
	}

	public void setSellAs(String sellAs) {
		this.sellAs = sellAs;
	}
	public String getTemplateProductId()
	{
		return templateProductId;
	}
	public void setTemplateProductId(String templateProductId)
	{
		this.templateProductId = templateProductId;
	}
	public String getProductId()
	{
		return productId;
	}
	public void setProductId(String productId)
	{
		this.productId = productId;
	}
	public double getManHours()
	{
		return manHours;
	}
	public void setManHours(double manHours)
	{
		this.manHours = manHours;
	}
	public double getRetailPrice()
	{
		return retailPrice;
	}
	public void setRetailPrice(double retailPrice)
	{
		this.retailPrice = retailPrice;
	}
	public String getProductName()
	{
		return productName;
	}
	public void setProductName(String productName)
	{
		this.productName = productName;
	}
	public int getDefaultQuantity()
	{
		return defaultQuantity;
	}
	public void setDefaultQuantity(int defaultQuantity)
	{
		this.defaultQuantity = defaultQuantity;
	}
	public boolean getDirty()
	{
		return dirty;
	}
	public void setDirty(boolean dirty)
	{
		this.dirty = dirty;
	}

	
	public ListQuoteTemplateProductsReturnItem() {
		
	}

	ListQuoteTemplateProductsReturnItem(BQuoteTemplateProduct bc) {
		
		templateProductId = bc.getQuoteTemplateProductId();
		productId = bc.getProduct().getProductId();
		manHours = bc.getProduct().getManHours();
		retailPrice = bc.getProduct().getRetailPrice();
		productName = bc.getProduct().getDescription();
		defaultQuantity = bc.getDefaultQuantity();
		dirty = false;
		sellAs = bc.getSellAsType();
	}
	
}

	
