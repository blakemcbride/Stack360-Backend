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
package com.arahant.services.standard.crm.clientParent;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BProspectCompany;


/**
 * 
 *
 *
 */
public class LoadProspectReturn extends TransmitReturnBase {

	void setData(BProspectCompany bc)
	{
		
		city=bc.getCity();
		mainContactFirstName=bc.getMainContactFname();
		mainContactLastName=bc.getMainContactLname();
		mainContactPersonalEmail=bc.getMainContactPersonalEmail();
		mainContactJobTitle=bc.getMainContactJobTitle();
		mainContactWorkFax=bc.getMainContactWorkFax();
		mainContactWorkPhone=bc.getMainContactWorkPhone();
		mainFaxNumber=bc.getMainFaxNumber();
		mainPhoneNumber=bc.getMainPhoneNumber();
		name=bc.getName();
		state=bc.getState();
		street=bc.getStreet();
		street2=bc.getStreet2();
		zip=bc.getZip();
		identifier=bc.getIdentifier();

	}
	
	private String city;
	private String mainContactFirstName;
	private String mainContactLastName;
	private String mainContactPersonalEmail;
	private String mainContactJobTitle;
	private String mainContactWorkFax;
	private String mainContactWorkPhone;
	private String mainFaxNumber;
	private String mainPhoneNumber;
	private String name;
	private String state;
	private String street;
	private String street2;
	private String zip;
	private String identifier;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getCity()
	{
		return city;
	}
	public void setCity(String city)
	{
		this.city=city;
	}
	public String getMainContactFirstName()
	{
		return mainContactFirstName;
	}
	public void setMainContactFirstName(String mainContactFirstName)
	{
		this.mainContactFirstName=mainContactFirstName;
	}
	public String getMainContactLastName()
	{
		return mainContactLastName;
	}
	public void setMainContactLastName(String mainContactLastName)
	{
		this.mainContactLastName=mainContactLastName;
	}
	public String getMainContactPersonalEmail()
	{
		return mainContactPersonalEmail;
	}
	public void setMainContactPersonalEmail(String mainContactPersonalEmail)
	{
		this.mainContactPersonalEmail=mainContactPersonalEmail;
	}

	public String getMainContactJobTitle() {
		return mainContactJobTitle;
	}

	public void setMainContactJobTitle(String mainContactJobTitle) {
		this.mainContactJobTitle = mainContactJobTitle;
	}
	
	public String getMainContactWorkFax()
	{
		return mainContactWorkFax;
	}
	public void setMainContactWorkFax(String mainContactWorkFax)
	{
		this.mainContactWorkFax=mainContactWorkFax;
	}
	public String getMainContactWorkPhone()
	{
		return mainContactWorkPhone;
	}
	public void setMainContactWorkPhone(String mainContactWorkPhone)
	{
		this.mainContactWorkPhone=mainContactWorkPhone;
	}
	public String getMainFaxNumber()
	{
		return mainFaxNumber;
	}
	public void setMainFaxNumber(String mainFaxNumber)
	{
		this.mainFaxNumber=mainFaxNumber;
	}
	public String getMainPhoneNumber()
	{
		return mainPhoneNumber;
	}
	public void setMainPhoneNumber(String mainPhoneNumber)
	{
		this.mainPhoneNumber=mainPhoneNumber;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public String getState()
	{
		return state;
	}
	public void setState(String state)
	{
		this.state=state;
	}
	public String getStreet()
	{
		return street;
	}
	public void setStreet(String street)
	{
		this.street=street;
	}
	public String getStreet2()
	{
		return street2;
	}
	public void setStreet2(String street2)
	{
		this.street2=street2;
	}
	public String getZip()
	{
		return zip;
	}
	public void setZip(String zip)
	{
		this.zip=zip;
	}

}

	
