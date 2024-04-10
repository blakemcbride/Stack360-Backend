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

package com.arahant.services.standard.crm.prospectSummary;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BProspectCompany;

public class LoadSummaryReturn extends TransmitReturnBase {
	
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
	private short timeZone;
	private int nextContactDate;
	private int lastContactDate;
	private int statusCertainty;
	private double opportunityValue;
	private int numberOfEmployees;
	private int grossIncome;
	private String website;
	private long whenAdded;
	private long changeDate;

	void setData(BProspectCompany bc) {
		certainty = bc.getCertainty();
		city = bc.getCity();
		firstContactDate = bc.getFirstContactDate();
		identifier = bc.getIdentifier();
		mainFaxNumber = bc.getMainFaxNumber();
		mainPhoneNumber = bc.getMainPhoneNumber();
		name = bc.getName();
		sourceDetail = bc.getSourceDetail();
		state = bc.getState();
		street = bc.getStreet();
		street2 = bc.getStreet2();
		zip = bc.getZip();
		country = bc.getCountry();
		timeZone = bc.getTimeZone();
		nextContactDate = bc.getNextContactDate();
		lastContactDate = bc.getLastContactDate();
		opportunityValue = bc.getOpportunityValue();
		statusCertainty = bc.getCertainty();
		numberOfEmployees = bc.getNumberOfEmployees();
		grossIncome = bc.getGrossIncome();
		website = bc.getWebsite();
		whenAdded = bc.getWhenAdded().getTime();
		changeDate = bc.getStatusChangeDate().getTime();
	}
	
	public short getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(short timeZone) {
		this.timeZone = timeZone;
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

	public int getLastContactDate() {
		return lastContactDate;
	}

	public void setLastContactDate(int lastContactDate) {
		this.lastContactDate = lastContactDate;
	}

	public double getOpportunityValue() {
		return opportunityValue;
	}

	public void setOpportunityValue(double opportunityValue) {
		this.opportunityValue = opportunityValue;
	}

	public int getStatusCertainty() {
		return statusCertainty;
	}

	public void setStatusCertainty(int statusCertainty) {
		this.statusCertainty = statusCertainty;
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

	public long getWhenAdded() {
		return whenAdded;
	}

	public void setWhenAdded(long whenAdded) {
		this.whenAdded = whenAdded;
	}

	public long getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(long changeDate) {
		this.changeDate = changeDate;
	}
}

	
