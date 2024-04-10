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
package com.arahant.services.standard.crm.salesTasks;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BProspectCompany;


/**
 * 
 *
 *
 */
public class LoadSummaryReturn extends TransmitReturnBase {

	void setData(BProspectCompany bc)
	{
		
		certainty=bc.getCertainty();
		city=bc.getCity();
		firstContactDate=bc.getFirstContactDate();
		identifier=bc.getIdentifier();
		mainFaxNumber=bc.getMainFaxNumber();
		mainPhoneNumber=bc.getMainPhoneNumber();
		name=bc.getName();
		sourceDetail=bc.getSourceDetail();
		state=bc.getState();
		street=bc.getStreet();
		street2 =bc.getStreet2 ();
		zip=bc.getZip();
		country=bc.getCountry();
		nextContactDate=bc.getNextContactDate();

	}
	
	private int certainty;
	private String city;
	private int firstContactDate;
	private String identifier;
	private String mainFaxNumber;
	private String mainPhoneNumber;
	private String name;
	private String sourceDetail;
	private String state;
	private String street;
	private String street2;
	private String zip;
	private String country;
	private int nextContactDate;

	public int getCertainty() {
		return certainty;
	}

	public void setCertainty(int certainty) {
		this.certainty = certainty;
	}

	public int getFirstContactDate() {
		return firstContactDate;
	}

	public void setFirstContactDate(int firstContactDate) {
		this.firstContactDate = firstContactDate;
	}

	public String getSourceDetail() {
		return sourceDetail;
	}

	public void setSourceDetail(String sourceDetail) {
		this.sourceDetail = sourceDetail;
	}

	public String getCity()
	{
		return city;
	}
	public void setCity(String city)
	{
		this.city=city;
	}
	public String getIdentifier()
	{
		return identifier;
	}
	public void setIdentifier(String identifier)
	{
		this.identifier=identifier;
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
	public String getStreet2 ()
	{
		return street2 ;
	}
	public void setStreet2 (String street2 )
	{
		this.street2 =street2 ;
	}
	public String getZip()
	{
		return zip;
	}
	public void setZip(String zip)
	{
		this.zip=zip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getNextContactDate() {
		return nextContactDate;
	}

	public void setNextContactDate(int nextContactDate) {
		this.nextContactDate = nextContactDate;
	}
}

	
