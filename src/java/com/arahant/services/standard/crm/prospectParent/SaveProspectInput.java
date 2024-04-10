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
package com.arahant.services.standard.crm.prospectParent;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BProspectCompany;
import com.arahant.annotation.Validation;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SaveProspectInput extends TransmitInputBase {

	void setData(BProspectCompany bc)
	{
		
		bc.setName(name);
		bc.setStreet(street1);
		bc.setStreet2(street2);
		bc.setCountry(country);
		bc.setCity(city);
		bc.setState(state);
		bc.setZip(zip);
		bc.setSourceId(source);
		bc.setStatusId(status);
		bc.setStatusId(type);
		bc.setFirstContactDate(firstContact);
		bc.setCertainty((short)certainty);
		bc.setSalesPersonId(salesPerson);
		bc.setIdentifier(identifier);

	}

	void setDataPostCreate(BProspectCompany bc)
	{
	//	bc.setMainContactWorkPhone(phone);
	//	bc.setMainContactWorkFax(fax);
	}
	
	@Validation (table="org_group",column="group_name",required=true)
	private String name;
	@Validation (table="address",column="street",required=false)
	private String street1;
	@Validation (table="address",column="street2",required=false)
	private String street2;
	@Validation (table="address",column="country_code",required=false)
	private String country;
	@Validation (table="address",column="city",required=false)
	private String city;
	@Validation (table="address",column="state",required=false)
	private String state;
	@Validation (table="address",column="zip",required=false)
	private String zip;
	@Validation (table="phone",column="phone_number",required=false)
	private String phone;
	@Validation (table="phone",column="phone_number",required=false)
	private String fax;
	@Validation (required=true)
	private String source;
	@Validation (required=true)
	private String status;
	@Validation (required=true)
	private String type;
	@Validation (type="date",required=false)
	private int firstContact;
	@Validation (min=0,max=100,required=false)
	private int certainty;
	@Validation (required=false)
	private String salesPerson;
	@Validation (required=false)
	private String prospect1Id;
	@Validation (required=false)
	private String prospect2Id;
	@Validation (table="org_group",column="external_id",required=false)
	private String identifier;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
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
	public String getSource()
	{
		return source;
	}
	public void setSource(String source)
	{
		this.source=source;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status=status;
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
	public String getProspect1Id()
	{
		return prospect1Id;
	}
	public void setProspect1Id(String prospect1Id)
	{
		this.prospect1Id=prospect1Id;
	}
	public String getProspect2Id()
	{
		return prospect2Id;
	}
	public void setProspect2Id(String prospect2Id)
	{
		this.prospect2Id=prospect2Id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}

	
