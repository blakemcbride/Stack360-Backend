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
package com.arahant.services.standard.crm.prospectAssignment;
import com.arahant.business.BProspectCompany;

public class SearchProspectsReturnItem {
	
	public SearchProspectsReturnItem()
	{
		
	}

	SearchProspectsReturnItem (BProspectCompany bc)
	{
		name=bc.getName();
		id=bc.getId();
		salesPerson=bc.getSalesPerson().getNameLFM();
		city=bc.getCity();
		state=bc.getState();
		zip=bc.getZip();
	}
	
	private String name;
	private String id;
	private String salesPerson;
	private String city;
	private String state;
	private String zip;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getSalesPerson() {
		return salesPerson;
	}

	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}
}

	