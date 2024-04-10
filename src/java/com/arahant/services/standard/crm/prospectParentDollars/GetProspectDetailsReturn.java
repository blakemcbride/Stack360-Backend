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
package com.arahant.services.standard.crm.prospectParentDollars;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BProspectCompany;


/**
 * 
 *
 *
 */
public class GetProspectDetailsReturn extends TransmitReturnBase {

	void setData(BProspectCompany bc)
	{
		
		name=bc.getName();
		street1=bc.getStreet();
		street2=bc.getStreet2();
		country=bc.getCountry();
		city=bc.getCity();
		state=bc.getState();
		zip=bc.getZip();
		phone=bc.getMainContactWorkPhone();
		fax=bc.getMainContactWorkFax();
		identifier=bc.getIdentifier();
		source=bc.getSourceCode();
		firstContact=bc.getFirstContactDate();
		certainty=bc.getCertainty();
		salesPerson=bc.getSalesPerson().getNameLFM();
		sourceDetail=bc.getSourceDetail();

	}
	
	private String name;
	private String street1;
	private String street2;
	private String country;
	private String city;
	private String state;
	private String zip;
	private String phone;
	private String fax;
	private String identifier;
	private String source;
	private int firstContact;
	private int certainty;
	private String salesPerson;
	private String sourceDetail;

	public String getSourceDetail() {
		return sourceDetail;
	}

	public void setSourceDetail(String sourceDetail) {
		this.sourceDetail = sourceDetail;
	}


	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public String getStreet1()
	{
		return street1;
	}
	public void setStreet1(String street1)
	{
		this.street1=street1;
	}
	public String getStreet2()
	{
		return street2;
	}
	public void setStreet2(String street2)
	{
		this.street2=street2;
	}
	public String getCountry()
	{
		return country;
	}
	public void setCountry(String country)
	{
		this.country=country;
	}
	public String getCity()
	{
		return city;
	}
	public void setCity(String city)
	{
		this.city=city;
	}
	public String getState()
	{
		return state;
	}
	public void setState(String state)
	{
		this.state=state;
	}
	public String getZip()
	{
		return zip;
	}
	public void setZip(String zip)
	{
		this.zip=zip;
	}
	public String getPhone()
	{
		return phone;
	}
	public void setPhone(String phone)
	{
		this.phone=phone;
	}
	public String getFax()
	{
		return fax;
	}
	public void setFax(String fax)
	{
		this.fax=fax;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getSource()
	{
		return source;
	}
	public void setSource(String source)
	{
		this.source=source;
	}
	public int getFirstContact()
	{
		return firstContact;
	}
	public void setFirstContact(int firstContact)
	{
		this.firstContact=firstContact;
	}
	public int getCertainty()
	{
		return certainty;
	}
	public void setCertainty(int certainty)
	{
		this.certainty=certainty;
	}
	public String getSalesPerson()
	{
		return salesPerson;
	}
	public void setSalesPerson(String salesPerson)
	{
		this.salesPerson=salesPerson;
	}

}

	
