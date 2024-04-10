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
 * Created on Feb 8, 2007
*/

package com.arahant.services.standard.crm.prospectSalesPersonParent;

import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;
import com.arahant.business.BProspectCompany;

public class NewProspectInput extends TransmitInputBase {
	@Validation (min=0,max=100,required=false)
	private int certainty;
	@Validation (table="address",column="city",required=false)
	private String city;
	@Validation (type="date",required=false)
	private int firstContactDate;
	@Validation (table="company_base",column="identifier",required=false)
	private String identifier;
	@Validation (table="person",column="fname",required=false)
	private String mainContactFirstName;
	@Validation (table="person",column="lname",required=false)
	private String mainContactLastName;
	@Validation (table="person",column="personal_email",required=false)
	private String mainContactPersonalEmail;
	@Validation (table="person",column="job_title",required=false)
	private String mainContactJobTitle;
	@Validation (table="phone",column="phone_number",required=false)
	private String mainContactWorkFax;
	@Validation (table="phone",column="phone_number",required=false)
	private String mainContactWorkPhone;
	@Validation (table="phone",column="phone_number",required=false)
	private String mainFaxNumber;
	@Validation (table="phone",column="phone_number",required=false)
	private String mainPhoneNumber;
	@Validation (table="org_group",column="name",required=true)
	private String name;
	@Validation (table="prospect",column="source_detail",required=false)
	private String sourceDetail;
	@Validation (min=1,max=16,required=true)
	private String sourceId;
	@Validation (table="address",column="state",required=false)
	private String state;
	@Validation (min=1,max=16,required=true)
	private String statusId;
	@Validation (table="address",column="street",required=false)
	private String street;
	@Validation (table="address",column="street2",required=false)
	private String street2;
	@Validation (table="address",column="zip",required=false)
	private String zip;
	@Validation (required=true)
	private String salesPersonId;
	@Validation (table="address",column="country_code",required=false)
	private String country;
	@Validation (table="phone",column="phone_number",required=false)
	private String mainContactHomePhone;
	@Validation (table="phone",column="phone_number",required=false)
	private String mainContactMobilePhone;
	@Validation (required=false)
	private int mainContactType;
	private String prospectType;

	void setData(BProspectCompany bc)
	{
		bc.setCertainty((short)certainty);
		bc.setCity(city);
		bc.setFirstContactDate(firstContactDate);
		bc.setIdentifier(identifier);
		bc.setMainContactFname(mainContactFirstName);
		bc.setMainContactLname(mainContactLastName);
		bc.setMainContactPersonalEmail(mainContactPersonalEmail);
		bc.setMainContactJobTitle(mainContactJobTitle);
		bc.setMainContactWorkFax(mainContactWorkFax);
		bc.setMainContactWorkPhone(mainContactWorkPhone);
		bc.setMainFaxNumber(mainFaxNumber);
		bc.setMainPhoneNumber(mainPhoneNumber);
		bc.setName(name);
		bc.setSourceDetail(sourceDetail);
		bc.setSourceId(sourceId);
		bc.setState(state);
		bc.setStatusId(statusId);
		bc.setStreet(street);
		bc.setStreet2(street2);
		bc.setZip(zip);
		bc.setSalesPersonId(salesPersonId);
		bc.setCountry(country);
		bc.setMainContactHomePhone(mainContactHomePhone);
		bc.setMainContactMobilePhone(mainContactMobilePhone);
		bc.setMainContactProspectType(mainContactType);
		bc.setProspectTypeId(prospectType);
	}

	public String getSalesPersonId() {
		return salesPersonId;
	}

	public void setSalesPersonId(String salesPersonId) {
		this.salesPersonId = salesPersonId;
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

	public String getMainContactJobTitle()
	{
		return mainContactJobTitle;
	}

	public void setMainContactJobTitle(String mainContactJobTitle)
	{
		this.mainContactJobTitle=mainContactJobTitle;
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

	public String getName ()
	{
		return name ;
	}

	public void setName (String name )
	{
		this.name =name ;
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

	public String getMainContactHomePhone() {
		return mainContactHomePhone;
	}

	public void setMainContactHomePhone(String mainContactHomePhone) {
		this.mainContactHomePhone = mainContactHomePhone;
	}

	public String getMainContactMobilePhone() {
		return mainContactMobilePhone;
	}

	public void setMainContactMobilePhone(String mainContactMobilePhone) {
		this.mainContactMobilePhone = mainContactMobilePhone;
	}

	public int getMainContactType() {
		return mainContactType;
	}

	public void setMainContactType(int mainContactType) {
		this.mainContactType = mainContactType;
	}

	public String getProspectType() {
		return prospectType;
	}

	public void setProspectType(String prospectType) {
		this.prospectType = prospectType;
	}
}

	
