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
package com.arahant.services.standard.hr.hrParent;

import com.arahant.business.BPerson;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 *
 */
public class LoadDependentReturn extends TransmitReturnBase {

	void setData(final BPerson bc)
	{
		canLogin=bc.getCanLogin()=='Y';
		city=bc.getCity();
		dob=bc.getDob();
		firstName=bc.getFirstName();
		homePhone=bc.getHomePhone();
		lastName=bc.getLastName();
		login=bc.getUserLogin();
		middleName=bc.getMiddleName();
		mobilePhone=bc.getMobilePhone();
		password=bc.getUserPassword();
		personalEmail=bc.getPersonalEmail();
		//screenGroupExtId=bc.getScreenGroupExtId();
		screenGroupId=bc.getScreenGroupId();
		//screenGroupName=bc.getScreenGroupName();
		securityGroupId=bc.getSecurityGroupId();
		//securityGroupName=bc.getSecurityGroupName();
		sex=bc.getSex();
		ssn=bc.getSsn();
		stateProvince=bc.getState();
		addressLine1=bc.getStreet();
		addressLine2=bc.getStreet2();
		jobTitle=bc.getJobTitle();
		workFax=bc.getWorkFaxNumber();
		workPhone=bc.getWorkPhoneNumber();
		zipPostalCode=bc.getZip();
		nickName=bc.getNickName();
		tabaccoUse = bc.getTabaccoUse();
		driversLicenseExpirationDate =  bc.getDriversLicenseExpirationDate();
		driversLicenseNumber = bc.getDriversLicenseNumber();
		driversLicenseState = bc.getDriversLicenseState();
		automotiveInsuranceCarrier = bc.getAutomotiveInsuranceCarrier();
		automotiveInsuranceStartDate = bc.getAutomotiveInsuranceStartDate();
		automotiveInsuranceExpirationDate = bc.getAutomotiveInsuranceExpirationDate();
		automotiveInsurancePolicyNumber = bc.getAutomotiveInsurancePolicyNumber();
		automotiveInsuranceCoverage = bc.getAutomotiveInsuranceCoverage();
		country=bc.getCountry();
		county=bc.getCounty();
		hicNumber = bc.getHicNumber();
	}
	
	private boolean canLogin;
	private String city;
	private int dob;
	private String firstName;
	private String homePhone;
	private String lastName;
	private String login;
	private String middleName;
	private String mobilePhone;
	private String password;
	private String personalEmail;
	//private String screenGroupExtId;
	private String screenGroupId;
	//private String screenGroupName;
	private String securityGroupId;
	//private String securityGroupName;
	private String sex;
	private String ssn;
	private String addressLine1;
	private String addressLine2;
	private String stateProvince;
	private String country;
	private String county;
	private String zipPostalCode;
	private String jobTitle;
	private String workFax;
	private String workPhone;
	private String nickName;
	private String tabaccoUse;
	private int driversLicenseExpirationDate;
	private String driversLicenseNumber;
	private String driversLicenseState;
	private String automotiveInsuranceCarrier;
	private int automotiveInsuranceStartDate;
	private int automotiveInsuranceExpirationDate;
	private String automotiveInsurancePolicyNumber;
	private String automotiveInsuranceCoverage;
	private String hicNumber;

	public String getHicNumber() {
		return hicNumber;
	}

	public void setHicNumber(String hicNumber) {
		this.hicNumber = hicNumber;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getStateProvince() {
		return stateProvince;
	}

	public void setStateProvince(String stateProvince) {
		this.stateProvince = stateProvince;
	}

	public String getZipPostalCode() {
		return zipPostalCode;
	}

	public void setZipPostalCode(String zipPostalCode) {
		this.zipPostalCode = zipPostalCode;
	}



	public Boolean getCanLogin() 
	{
		return canLogin;
	}
	public void setCanLogin(Boolean canLogin) 
	{
		this.canLogin = canLogin;
	}
	public String getCity() 
	{
		return city;
	}
	public void setCity(String city) 
	{
		this.city = city;
	}
	public int getDob()
	{
		return dob;
	}
	public void setDob(final int dob)
	{
		this.dob=dob;
	}
	public String getFirstName()
	{
		return firstName;
	}
	public void setFirstName(final String firstName)
	{
		this.firstName=firstName;
	}
	public String getHomePhone() 
	{
		return homePhone;
	}
	public void setHomePhone(String homePhone) 
	{
		this.homePhone = homePhone;
	}
	public String getLastName()
	{
		return lastName;
	}
	public void setLastName(final String lastName)
	{
		this.lastName=lastName;
	}
	public String getLogin()
	{
		return login;
	}
	public void setLogin(final String login)
	{
		this.login=login;
	}
	public String getMiddleName()
	{
		return middleName;
	}
	public void setMiddleName(final String middleName)
	{
		this.middleName=middleName;
	}
	public String getMobilePhone()
	{
		return mobilePhone;
	}
	public void setMobilePhone(final String mobilePhone)
	{
		this.mobilePhone=mobilePhone;
	}
	public String getPassword() 
	{
		return password;
	}
	public void setPassword(String password) 
	{
		this.password = password;
	}
	
	public String getScreenGroupId() 
	{
		return screenGroupId;
	}
	public void setScreenGroupId(String screenGroupId) 
	{
		this.screenGroupId = screenGroupId;
	}
	
	public String getSecurityGroupId() 
	{
		return securityGroupId;
	}
	public void setSecurityGroupId(String securityGroupId) 
	{
		this.securityGroupId = securityGroupId;
	}
	
	public String getPersonalEmail() 
	{
		return personalEmail;
	}
	public void setPersonalEmail(String personalEmail) 
	{
		this.personalEmail = personalEmail;
	}
	public String getSex()
	{
		return sex;
	}
	public void setSex(final String sex)
	{
		this.sex=sex;
	}
	public String getSsn()
	{
		return ssn;
	}
	public void setSsn(final String ssn)
	{
		this.ssn=ssn;
	}

	
	public boolean isCanLogin() {
		return canLogin;
	}

	public void setCanLogin(boolean canLogin) {
		this.canLogin = canLogin;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	
	public String getWorkFax() 
	{
		return workFax;
	}
	public void setWorkFax(String workFax) 
	{
		this.workFax = workFax;
	}
	public String getWorkPhone() 
	{
		return workPhone;
	}
	public void setWorkPhone(String workPhone) 
	{
		this.workPhone = workPhone;
	}

	public String getNickName() 
	{
		return nickName;
	}
	public void setNickName(String nickName) 
	{
		this.nickName = nickName;
	}

	public String getAutomotiveInsuranceCarrier() {
		return automotiveInsuranceCarrier;
	}

	public void setAutomotiveInsuranceCarrier(String automotiveInsuranceCarrier) {
		this.automotiveInsuranceCarrier = automotiveInsuranceCarrier;
	}

	public int getAutomotiveInsuranceStartDate() {
		return automotiveInsuranceStartDate;
	}

	public void setAutomotiveInsuranceStartDate(int automotiveInsuranceStartDate) {
		this.automotiveInsuranceStartDate = automotiveInsuranceStartDate;
	}

	public int getAutomotiveInsuranceExpirationDate() {
		return automotiveInsuranceExpirationDate;
	}

	public void setAutomotiveInsuranceExpirationDate(int automotiveInsuranceExpirationDate) {
		this.automotiveInsuranceExpirationDate = automotiveInsuranceExpirationDate;
	}

	public String getAutomotiveInsurancePolicyNumber() {
		return automotiveInsurancePolicyNumber;
	}

	public void setAutomotiveInsurancePolicyNumber(String automotiveInsurancePolicyNumber) {
		this.automotiveInsurancePolicyNumber = automotiveInsurancePolicyNumber;
	}

	public String getAutomotiveInsuranceCoverage() {
		return automotiveInsuranceCoverage;
	}

	public void setAutomotiveInsuranceCoverage(String automotiveInsuranceCoverage) {
		this.automotiveInsuranceCoverage = automotiveInsuranceCoverage;
	}

	public int getDriversLicenseExpirationDate() {
		return driversLicenseExpirationDate;
	}

	public void setDriversLicenseExpirationDate(int driversLicenseExpirationDate) {
		this.driversLicenseExpirationDate = driversLicenseExpirationDate;
	}

	public String getDriversLicenseNumber() {
		return driversLicenseNumber;
	}

	public void setDriversLicenseNumber(String driversLicenseNumber) {
		this.driversLicenseNumber = driversLicenseNumber;
	}

	public String getDriversLicenseState() {
		return driversLicenseState;
	}

	public void setDriversLicenseState(String driversLicenseState) {
		this.driversLicenseState = driversLicenseState;
	}

	public String getTabaccoUse() {
		return tabaccoUse;
	}

	public void setTabaccoUse(String tabaccoUse) {
		this.tabaccoUse = tabaccoUse;
	}
}

	
