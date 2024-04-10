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

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BProduct;


/**
 * 
 *
 *
 */
public class LoadProductReturn extends TransmitReturnBase {

	void setData(BProduct bc)
	{
		description=bc.getDescription();
		sku=bc.getSku();
		cost=bc.getCost();
		wholesalePrice=bc.getWholesalePrice();
		retailPrice=bc.getRetailPrice();
		availabilityDate=bc.getAvailabilityDate();
		termDate=bc.getTermDate();
		manHours=bc.getManHours();
		sellAs=bc.getSellAsType();
	}
	
	private String description;
	private String sku;
	private double cost;
	private double wholesalePrice;
	private double retailPrice;
	private int availabilityDate;
	private int termDate;
	private double manHours;
	private String sellAs;

	public String getSellAs() {
		return sellAs;
	}

	public void setSellAs(String sellAs) {
		this.sellAs = sellAs;
	}
	

	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}
	public String getSku()
	{
		return sku;
	}
	public void setSku(String sku)
	{
		this.sku=sku;
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
	public double getManHours() {
		return manHours;
	}

	public void setManHours(double manHours) {
		this.manHours = manHours;
	}
}

	
