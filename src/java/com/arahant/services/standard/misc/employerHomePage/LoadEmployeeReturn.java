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
package com.arahant.services.standard.misc.employerHomePage;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BEmployee;

public class LoadEmployeeReturn extends TransmitReturnBase {

	void setData(BEmployee be)
	{
		workFax = be.getWorkFax();
        workPhone = be.getWorkPhone();
        personalEmail = be.getPersonalEmail();
        jobTitle = be.getJobTitle();
        lname = be.getLastName();
        fname = be.getFirstName();
        login = be.getUserLogin();
        empPassword = be.getUserPassword();
		if (be.getCanLogin() == 'Y')
			canLogin = true;
		else if (be.getCanLogin() == 'N')
			canLogin = false;

        screenGroupId = be.getScreenGroupId();
        securityGroupId = be.getSecurityGroupId();
        ssn = be.getSsn();
        homePhone = be.getHomePhone();
        mobilePhone = be.getMobilePhone();
        addressLine1 = be.getStreet();
        city = be.getCity();
        stateProvince = be.getState();
        zipPostalCode = be.getZip();

        eeoCategoryId = be.getEEOCategoryId();
        eeoRaceId = be.getEEORaceId();

        sex = be.getSex();
        dob = be.getDob();
        extRef = be.getExtRef();
        middleName = be.getMiddleName();
        nickName = be.getNickName();
        addressLine2 = be.getStreet2();
        country = be.getCountry();
        county = be.getCounty();

        citizenship = be.getCitizenship();
        i9Completed = be.getI9Part1() && be.getI9Part2();
        i9Part1 = be.getI9Part1();
        i9Part2 = be.getI9Part2();
        visa = be.getVisa();
        visaStatusDate = be.getVisaStatusDate();
        visaExpirationDate = be.getVisaExpirationDate();

        tabaccoUse = be.getTabaccoUse();
        driversLicenseExpirationDate = be.getDriversLicenseExpirationDate();
        driversLicenseNumber = be.getDriversLicenseNumber();
        driversLicenseState = be.getDriversLicenseState();
        automotiveInsuranceCarrier = be.getAutomotiveInsuranceCarrier();
        automotiveInsuranceStartDate = be.getAutomotiveInsuranceStartDate();
        automotiveInsuranceExpirationDate = be.getAutomotiveInsuranceExpirationDate();
        automotiveInsurancePolicyNumber = be.getAutomotiveInsurancePolicyNumber();
        automotiveInsuranceCoverage = be.getAutomotiveInsuranceCoverage();

        defaultProjectId = be.getDefaultProjectId();

        benefitClass = be.getBenefitClassId();

        height = be.getHeight();
        weight = be.getWeight();

		employeeStatusId = be.getEmployeeStatusId();
		wageTypeId = be.getWageTypeId();
		wageAmount = be.getWageAmount();
		positionId = be.getPositionId();
		employeeStatusDate = be.getEmployeeStatusDate();
		medicare = be.getMedicare() + "";
		hrAdmin = be.getHrAdmin()=='Y'?true:false;

	}

    private String workPhone;
    private String workFax;
    private String personalEmail;
    private String jobTitle;
    private String lname;
    private String fname;
    private String login;
    private String empPassword;
    private boolean canLogin;
    private String screenGroupId;
    private String securityGroupId;
    private String ssn;
    private String homePhone;
    private String mobilePhone;
    private String addressLine1;
    private String city;
    private String stateProvince;
    private String zipPostalCode;
    private String orgGroupId;
    private String sex;
    private String addressLine2;
    private String eeoCategoryId;
    private String eeoRaceId;
    private int dob;
    private String extRef;
    private String middleName;
    private String employeeStatusId;
    private String citizenship;

    private boolean i9Completed;
	private boolean i9Part1;
	private boolean i9Part2;

    private String visa;
    private int visaStatusDate;
    private int visaExpirationDate;
    private String positionId;
    private String wageTypeId;
    private double wageAmount;
    private int employeeStatusDate;
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
    private String defaultProjectId;
    private String country;
    private String county;
    private String benefitClass;
    private short height;
    private short weight;
	private String medicare;
	private boolean hrAdmin;


	public boolean getHrAdmin() {
		return hrAdmin;
	}

	public void setHrAdmin(boolean hrAdmin) {
		this.hrAdmin = hrAdmin;
	}

	public String getMedicare() {
		return medicare;
	}

	public void setMedicare(String medicare) {
		this.medicare = medicare;
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

	public String getAutomotiveInsuranceCarrier() {
		return automotiveInsuranceCarrier;
	}

	public void setAutomotiveInsuranceCarrier(String automotiveInsuranceCarrier) {
		this.automotiveInsuranceCarrier = automotiveInsuranceCarrier;
	}

	public String getAutomotiveInsuranceCoverage() {
		return automotiveInsuranceCoverage;
	}

	public void setAutomotiveInsuranceCoverage(String automotiveInsuranceCoverage) {
		this.automotiveInsuranceCoverage = automotiveInsuranceCoverage;
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

	public int getAutomotiveInsuranceStartDate() {
		return automotiveInsuranceStartDate;
	}

	public void setAutomotiveInsuranceStartDate(int automotiveInsuranceStartDate) {
		this.automotiveInsuranceStartDate = automotiveInsuranceStartDate;
	}

	public String getBenefitClass() {
		return benefitClass;
	}

	public void setBenefitClass(String benefitClass) {
		this.benefitClass = benefitClass;
	}

	public boolean isCanLogin() {
		return canLogin;
	}

	public void setCanLogin(boolean canLogin) {
		this.canLogin = canLogin;
	}

	public String getCitizenship() {
		return citizenship;
	}

	public void setCitizenship(String citizenship) {
		this.citizenship = citizenship;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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

	public String getDefaultProjectId() {
		return defaultProjectId;
	}

	public void setDefaultProjectId(String defaultProjectId) {
		this.defaultProjectId = defaultProjectId;
	}

	public int getDob() {
		return dob;
	}

	public void setDob(int dob) {
		this.dob = dob;
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

	public String getEeoCategoryId() {
		return eeoCategoryId;
	}

	public void setEeoCategoryId(String eeoCategoryId) {
		this.eeoCategoryId = eeoCategoryId;
	}

	public String getEeoRaceId() {
		return eeoRaceId;
	}

	public void setEeoRaceId(String eeoRaceId) {
		this.eeoRaceId = eeoRaceId;
	}

	public String getEmpPassword() {
		return empPassword;
	}

	public void setEmpPassword(String empPassword) {
		this.empPassword = empPassword;
	}

	public int getEmployeeStatusDate() {
		return employeeStatusDate;
	}

	public void setEmployeeStatusDate(int employeeStatusDate) {
		this.employeeStatusDate = employeeStatusDate;
	}

	public String getEmployeeStatusId() {
		return employeeStatusId;
	}

	public void setEmployeeStatusId(String employeeStatusId) {
		this.employeeStatusId = employeeStatusId;
	}

	public String getExtRef() {
		return extRef;
	}

	public void setExtRef(String extRef) {
		this.extRef = extRef;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public short getHeight() {
		return height;
	}

	public void setHeight(short height) {
		this.height = height;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public boolean isI9Completed() {
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

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getOrgGroupId() {
		return orgGroupId;
	}

	public void setOrgGroupId(String orgGroupId) {
		this.orgGroupId = orgGroupId;
	}

	public String getPersonalEmail() {
		return personalEmail;
	}

	public void setPersonalEmail(String personalEmail) {
		this.personalEmail = personalEmail;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getScreenGroupId() {
		return screenGroupId;
	}

	public void setScreenGroupId(String screenGroupId) {
		this.screenGroupId = screenGroupId;
	}

	public String getSecurityGroupId() {
		return securityGroupId;
	}

	public void setSecurityGroupId(String securityGroupId) {
		this.securityGroupId = securityGroupId;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getStateProvince() {
		return stateProvince;
	}

	public void setStateProvince(String stateProvince) {
		this.stateProvince = stateProvince;
	}

	public String getTabaccoUse() {
		return tabaccoUse;
	}

	public void setTabaccoUse(String tabaccoUse) {
		this.tabaccoUse = tabaccoUse;
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

	public double getWageAmount() {
		return wageAmount;
	}

	public void setWageAmount(double wageAmount) {
		this.wageAmount = wageAmount;
	}

	public String getWageTypeId() {
		return wageTypeId;
	}

	public void setWageTypeId(String wageTypeId) {
		this.wageTypeId = wageTypeId;
	}

	public short getWeight() {
		return weight;
	}

	public void setWeight(short weight) {
		this.weight = weight;
	}

	public String getWorkFax() {
		return workFax;
	}

	public void setWorkFax(String workFax) {
		this.workFax = workFax;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public String getZipPostalCode() {
		return zipPostalCode;
	}

	public void setZipPostalCode(String zipPostalCode) {
		this.zipPostalCode = zipPostalCode;
	}
	
}

	
