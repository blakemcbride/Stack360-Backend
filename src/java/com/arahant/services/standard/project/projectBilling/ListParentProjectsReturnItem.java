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
import com.arahant.business.BProject;


/**
 * 
 *
 *
 */
public class ListParentProjectsReturnItem {
	
	public ListParentProjectsReturnItem()
	{
		;
	}

	ListParentProjectsReturnItem (BProject bc, final BProject child)
	{
		
		id=bc.getId();
		name=bc.getName();
		description=bc.getDescription();
		billableInsideEligible=bc.getBillableInsideEligible(child);
		billingRate=bc.getBillingRate();

	}
	
	private String id;
	private String name;
	private String description;
	private boolean billableInsideEligible;
	private double billingRate;


	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}
	public boolean getBillableInsideEligible()
	{
		return billableInsideEligible;
	}
	public void setBillableInsideEligible(boolean billableInsideEligible)
	{
		this.billableInsideEligible=billableInsideEligible;
	}
	public double getBillingRate()
	{
		return billingRate;
	}
	public void setBillingRate(double billingRate)
	{
		this.billingRate=billingRate;
	}

}

	
