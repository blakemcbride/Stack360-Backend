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

package com.arahant.services.standard.misc.companyOrgGroup;

import com.arahant.business.BEmployee;
import com.arahant.services.TransmitReturnBase;

public class LoadEmployeeReturn extends TransmitReturnBase  {

	final static String fakePassword = "***********";

	private String workPhone;
	private String workFax;
	private boolean primaryIndicator;
	private String personalEmail;
	private String jobTitle;
	private String personId;
	private String lname;
	private String fname;
	private String login;
	private String password;
	private boolean canLogin;
	private String screenGroupId;
	private String screenGroupName;
	private String screenGroupExtId;

	private String ssn;

	private String homePhone;
	private String mobilePhone;
	private String city;
	private String stateProvince;
	private String zipPostalCode;
	private String addressLine1,addressLine2;
	private String country;
	private String county;
	
	private String eeoCategoryId;
	private String positionName;
	private String positionId;
	private String eeoRaceId;
    private double wageAmount;
	private String wageTypeName;
	private String wageTypeId;
	private int employeeStatusDate;
	private String employeeStatusName;
	private String employeeStatusId;
	
	private String sex;
	
	private int dob;
	private boolean exempt;
	
	private String extRef;
	
	private String securityGroupId;
	private String securityGroupName;
	private String middleName;

	
	private String citizenship;

	private boolean i9Completed;
	private boolean i9Part1;
	private boolean i9Part2;

	private String visa;
	private int visaStatusDate;
	private int visaExpirationDate;
	
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
	private String benefitClassId;
	private boolean hrAdmin;
	private String workerType;

	public boolean isHrAdmin() {
		return hrAdmin;
	}

	public void setHrAdmin(boolean hrAdmin) {
		this.hrAdmin = hrAdmin;
	}
	
	public String getBenefitClassId() {
		return benefitClassId;
	}

	public void setBenefitClassId(String benefitClassId) {
		this.benefitClassId = benefitClassId;
	}

	public double getBillingRate() {
		return billingRate;
	}

	public void setBillingRate(double billingRate) {
		this.billingRate = billingRate;
	}
	private double billingRate;

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	public String getExtRef() {
		return extRef;
	}

	public void setExtRef(final String extRef) {
		this.extRef = extRef;
	}

	public boolean isExempt() {
		return exempt;
	}

	public void setExempt(final boolean exempt) {
		this.exempt = exempt;
	}

	public int getDob() {
		return dob;
	}

	public void setDob(final int dob) {
		this.dob = dob;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(final String sex) {
		this.sex = sex;
	}

	public int getEmployeeStatusDate() {
		return employeeStatusDate;
	}

	public void setEmployeeStatusDate(final int employeeStatusDate) {
		this.employeeStatusDate = employeeStatusDate;
	}

	public String getEmployeeStatusName() {
		return employeeStatusName;
	}

	public void setEmployeeStatusName(final String employeeStatusName) {
		this.employeeStatusName = employeeStatusName;
	}

	public String getEmployeeStatusId() {
		return employeeStatusId;
	}

	public void setEmployeeStatusId(String employeeStatusId) {
		this.employeeStatusId = employeeStatusId;
	}

	public String getEeoCategoryId() {
		return eeoCategoryId;
	}

	public void setEeoCategoryId(final String eeoCategoryId) {
		this.eeoCategoryId = eeoCategoryId;
	}

	public String getEeoRaceId() {
		return eeoRaceId;
	}

	public void setEeoRaceId(final String eeoRaceId) {
		this.eeoRaceId = eeoRaceId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(final String city) {
		this.city = city;
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

	public LoadEmployeeReturn() {
		super();
	}

	LoadEmployeeReturn(final BEmployee employee) {
		workPhone = employee.getWorkPhoneNumber();
		workFax = employee.getWorkFaxNumber();
		primaryIndicator = employee.isPrimary();
		personalEmail = employee.getPersonalEmail();
		jobTitle = employee.getJobTitle();
		personId = employee.getPersonId();
		lname = employee.getLastName();
		fname = employee.getFirstName();
		login = employee.getUserLogin();
		password = employee.getUserPassword();
		if (password != null && !password.isEmpty())
			password = fakePassword;  //  don't send the real password
		canLogin = employee.getCanLogin() == 'Y';
		screenGroupId = employee.getScreenGroupId();
		screenGroupName = employee.getScreenGroupName();
		screenGroupExtId = employee.getScreenGroupExtId();

		securityGroupId = employee.getSecurityGroupId();
		securityGroupName = employee.getSecurityGroupName();

		ssn = employee.getSsn();

		homePhone = employee.getHomePhone();
		mobilePhone = employee.getMobilePhone();
		city = employee.getCity();
		stateProvince = employee.getState();
		addressLine1 = employee.getStreet();
		zipPostalCode = employee.getZip();
		county = employee.getCounty();
		country = employee.getCountry();


		eeoCategoryId = employee.getEEOCategoryId();
		positionName = employee.getPositionName();
		positionId = employee.getPositionId();
		eeoRaceId = employee.getEEORaceId();
		wageAmount = employee.getWageAmount();
		wageTypeName = employee.getWageTypeName();
		wageTypeId = employee.getWageTypeId();
		employeeStatusDate = employee.getEmployeeStatusDate();
		employeeStatusName = employee.getEmployeeStatusName();
		employeeStatusId = employee.getEmployeeStatusId();

		sex = employee.getSex();
		dob = employee.getDob();
		exempt = employee.getExempt();

		extRef = employee.getExtRef();
		middleName = employee.getMiddleName();

		addressLine2 = employee.getStreet2();
		billingRate = 0;  // employee.getBillingRate();  No single employee billing rate anymore

		citizenship = employee.getCitizenship();
		i9Completed = employee.getI9Part1() && employee.getI9Part2();
		i9Part1 = employee.getI9Part1();
		i9Part2 = employee.getI9Part2();

		visa = employee.getVisa();
		visaStatusDate = employee.getVisaStatusDate();
		visaExpirationDate = employee.getVisaExpirationDate();

		nickName = employee.getNickName();
		tabaccoUse = employee.getTabaccoUse();
		driversLicenseExpirationDate = employee.getDriversLicenseExpirationDate();
		driversLicenseNumber = employee.getDriversLicenseNumber();
		driversLicenseState = employee.getDriversLicenseState();
		automotiveInsuranceCarrier = employee.getAutomotiveInsuranceCarrier();
		automotiveInsuranceStartDate = employee.getAutomotiveInsuranceStartDate();
		automotiveInsuranceExpirationDate = employee.getAutomotiveInsuranceExpirationDate();
		automotiveInsurancePolicyNumber = employee.getAutomotiveInsurancePolicyNumber();
		automotiveInsuranceCoverage = employee.getAutomotiveInsuranceCoverage();

		benefitClassId = employee.getBenefitClassId();
		hrAdmin = employee.getHrAdmin() == 'Y';
		workerType = employee.getEmploymentType() + "";
	}

	public boolean isCanLogin() {
		return canLogin;
	}

	public void setCanLogin(final boolean canLogin) {
		this.canLogin = canLogin;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(final String fname) {
		this.fname = fname;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(final String homePhone) {
		this.homePhone = homePhone;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(final String lname) {
		this.lname = lname;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(final String login) {
		this.login = login;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(final String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getPersonalEmail() {
		return personalEmail;
	}

	public void setPersonalEmail(final String personalEmail) {
		this.personalEmail = personalEmail;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(final String personId) {
		this.personId = personId;
	}

	public boolean isPrimaryIndicator() {
		return primaryIndicator;
	}

	public void setPrimaryIndicator(final boolean primaryIndicator) {
		this.primaryIndicator = primaryIndicator;
	}

	public String getScreenGroupId() {
		return screenGroupId;
	}

	public void setScreenGroupId(final String screenGroupId) {
		this.screenGroupId = screenGroupId;
	}

	public String getScreenGroupName() {
		return screenGroupName;
	}

	public void setScreenGroupName(final String screenGroupName) {
		this.screenGroupName = screenGroupName;
	}

	public String getScreenGroupExtId() {
		return screenGroupExtId;
	}

	public void setScreenGroupExtId(final String screenGroupExtId) {
		this.screenGroupExtId = screenGroupExtId;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(final String ssn) {
		this.ssn = ssn;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getWorkFax() {
		return workFax;
	}

	public void setWorkFax(final String workFax) {
		this.workFax = workFax;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(final String workPhone) {
		this.workPhone = workPhone;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(final String positionName) {
		this.positionName = positionName;
	}

	public double getWageAmount() {
		return wageAmount;
	}

	public void setWageAmount(final double wageAmount) {
		this.wageAmount = wageAmount;
	}

	public String getWageTypeName() {
		return wageTypeName;
	}

	public void setWageTypeName(final String wageTypeName) {
		this.wageTypeName = wageTypeName;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getWageTypeId() {
		return wageTypeId;
	}

	public void setWageTypeId(String wageTypeId) {
		this.wageTypeId = wageTypeId;
	}

	public String getSecurityGroupId() {
		return securityGroupId;
	}

	public void setSecurityGroupId(final String securityGroupId) {
		this.securityGroupId = securityGroupId;
	}

	public String getSecurityGroupName() {
		return securityGroupName;
	}

	public void setSecurityGroupName(final String securityGroupName) {
		this.securityGroupName = securityGroupName;
	}

	public String getCitizenship() {
		return citizenship;
	}

	public void setCitizenship(String citizenship) {
		this.citizenship = citizenship;
	}

	public boolean getI9Completed() {
		return i9Completed;
	}

	public void setI9Completed(boolean i9Completed) {
		this.i9Completed = i9Completed;
	}

	public boolean isI9Part1() {
		return i9Part1;
	}

	public void setI9Part1(boolean i9Part1) {
		this.i9Part1 = i9Part1;
	}

	public boolean isI9Part2() {
		return i9Part2;
	}

	public void setI9Part2(boolean i9Part2) {
		this.i9Part2 = i9Part2;
	}

	public String getVisa() {
		return visa;
	}

	public void setVisa(String visa) {
		this.visa = visa;
	}

	public int getVisaExpirationDate() {
		return visaExpirationDate;
	}

	public void setVisaExpirationDate(int visaExpirationDate) {
		this.visaExpirationDate = visaExpirationDate;
	}

	public int getVisaStatusDate() {
		return visaStatusDate;
	}

	public void setVisaStatusDate(int visaStatusDate) {
		this.visaStatusDate = visaStatusDate;
	}
	
	public String getNickName() {
		return nickName;
	}
	
	public void setNickName(final String nickName) {
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

	public String getWorkerType() {
		return workerType;
	}

	public void setWorkerType(String workerType) {
		this.workerType = workerType;
	}
	
}

	
