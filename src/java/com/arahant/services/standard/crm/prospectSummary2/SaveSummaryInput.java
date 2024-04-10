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
 *
 *  Created on Feb 8, 2007
*/



package com.arahant.services.standard.crm.prospectSummary2;

import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BProspectCompany;

public class SaveSummaryInput extends TransmitInputBase {
	
	@Validation (min=0,max=100,required=false)
	private int certainty;
	@Validation (min=1,max=16,required=true)
	private String id;
	@Validation (table="address",column="city",required=false)
	private String city;
	@Validation (type="date",required=false)
	private int firstContactDate;
	@Validation (required=false)
	private String identifier;
	@Validation (required=false)
	private String mainFaxNumber;
	@Validation (required=false)
	private String mainPhoneNumber;
	@Validation (required=false)
	private String name;
	@Validation (table="prospect",column="source_detail",required=false)
	private String sourceDetail;
	@Validation (min=1,max=16,required=true)
	private String sourceId;
	@Validation (min=1,max=16,required=true)
	private String typeId;
	@Validation (table="address",column="state",required=false)
	private String state;
	@Validation (min=1,max=16,required=true)
	private String statusId;
	@Validation (required=false)
	private String street;
	@Validation (required=false)
	private String street2;
	@Validation (required=false)
	private String zip;
	@Validation (required=true)
	private String salesPersonId;
	@Validation (table="address",column="country_code",required=false)
	private String country;
	@Validation (table="prospect",column="next_contact_date",type="date",required=false)
	private int nextContactDate;
	@Validation (table="prospect",column="last_contact_date",type="date",required=false)
	private int lastContactDate;
	@Validation (table="address",column="time_zone_offset",required=false)
	private short timeZone;
	@Validation (min=0,max=10000000,required=false)
	private double opportunityValue;
	private int numberOfEmployees;
	private int grossIncome;
	private String website;

	void setData(BProspectCompany bc)
	{
		bc.setCertainty((short)certainty);
		bc.setCity(city);
		bc.setFirstContactDate(firstContactDate);
		bc.setIdentifier(identifier);
		bc.setMainFaxNumber(mainFaxNumber);
		bc.setMainPhoneNumber(mainPhoneNumber);
		bc.setName(name);
		bc.setSourceDetail(sourceDetail);
		bc.setSourceId(sourceId);
		bc.setState(state);
		bc.setStatusId(statusId);
		bc.setProspectTypeId(typeId);
		bc.setStreet(street);
		bc.setStreet2(street2);
		bc.setZip(zip);
		bc.setSalesPersonId(salesPersonId);
		bc.setCountry(country);
		bc.setNextContactDate(nextContactDate);
		bc.setLastContactDate(lastContactDate);
		bc.setTimeZone(timeZone);
		bc.setOpportunityValue(opportunityValue);
		bc.setNumberOfEmployees(numberOfEmployees);
		bc.setGrossIncome(grossIncome);
		bc.setWebsite(website);
	}
	
	public short getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(short timeZone) {
		this.timeZone = timeZone;
	}

	public String getSalesPersonId() {
		return salesPersonId;
	}

	public void setSalesPersonId(String salesPersonId) {
		this.salesPersonId = salesPersonId;
	}
	
	public String getId()
	{
		return id;
	}

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

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	public void setId(String id)
	{
		this.id=id;
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

	public int getLastContactDate() {
		return lastContactDate;
	}

	public void setLastContactDate(int lastContactDate) {
		this.lastContactDate = lastContactDate;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public double getOpportunityValue() {
		return opportunityValue;
	}

	public void setOpportunityValue(double opportunityValue) {
		this.opportunityValue = opportunityValue;
	}

	public int getNumberOfEmployees() {
		return numberOfEmployees;
	}

	public void setNumberOfEmployees(int numberOfEmployees) {
		this.numberOfEmployees = numberOfEmployees;
	}

	public int getGrossIncome() {
		return grossIncome;
	}

	public void setGrossIncome(int grossIncome) {
		this.grossIncome = grossIncome;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}
}

	
