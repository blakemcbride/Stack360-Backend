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
package com.arahant.services.standard.inventory.productByType;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BProduct;
import com.arahant.annotation.Validation;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SaveProductInput extends TransmitInputBase {

	void setData(BProduct bc)
	{
		bc.setProductTypeId(productTypeId);
		bc.setVendorId(vendorId);
		bc.setSku(sku);
		bc.setDescription(description);
		bc.setCost(cost);
		bc.setWholesalePrice(wholesalePrice);
		bc.setRetailPrice(retailPrice);
		bc.setAvailabilityDate(availabilityDate);
		bc.setTermDate(termDate);
		bc.setManHours(manHours);
		bc.setSellAsType(sellAs);
	}
	
	@Validation (required=true)
	private String productTypeId;
	@Validation (required=true)
	private String vendorId;
	@Validation (table="product",column="sku",required=false)
	private String sku;
	@Validation (table="product",column="product_service",required=true)
	private String description;
	@Validation (required=false)
	private double cost;
	@Validation (required=false)
	private double wholesalePrice;
	@Validation (required=false)
	private double retailPrice;
	@Validation (type="date",required=false)
	private int availabilityDate;
	@Validation (type="date",required=false)
	private int termDate;
	@Validation (min=1,max=16,required=true)
	private String id;
	@Validation (table="product", column="man_hours", required=false)
	private double manHours;
	@Validation (table="product", column="sell_as_type", required=true)
	private String sellAs;

	public String getSellAs() {
		return sellAs;
	}

	public void setSellAs(String sellAs) {
		this.sellAs = sellAs;
	}
	

	public String getProductTypeId()
	{
		return productTypeId;
	}
	public void setProductTypeId(String productTypeId)
	{
		this.productTypeId=productTypeId;
	}
	public String getVendorId()
	{
		return vendorId;
	}
	public void setVendorId(String vendorId)
	{
		this.vendorId=vendorId;
	}
	public String getSku()
	{
		return sku;
	}
	public void setSku(String sku)
	{
		this.sku=sku;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}
	public double getCost()
	{
		return cost;
	}
	public void setCost(double cost)
	{
		this.cost=cost;
	}
	public double getWholesalePrice()
	{
		return wholesalePrice;
	}
	public void setWholesalePrice(double wholesalePrice)
	{
		this.wholesalePrice=wholesalePrice;
	}
	public double getRetailPrice()
	{
		return retailPrice;
	}
	public void setRetailPrice(double retailPrice)
	{
		this.retailPrice=retailPrice;
	}
	public int getAvailabilityDate()
	{
		return availabilityDate;
	}
	public void setAvailabilityDate(int availabilityDate)
	{
		this.availabilityDate=availabilityDate;
	}
	public int getTermDate()
	{
		return termDate;
	}
	public void setTermDate(int termDate)
	{
		this.termDate=termDate;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}

	public double getManHours() {
		return manHours;
	}

	public void setManHours(double manHours) {
		this.manHours = manHours;
	}
}

	
