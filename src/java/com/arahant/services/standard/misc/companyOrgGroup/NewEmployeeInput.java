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

import com.arahant.annotation.Validation;
import com.arahant.business.BEmployee;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitInputBase;

public class NewEmployeeInput extends TransmitInputBase {

	@Validation(table = "phone", column = "phone_number", required = false)
	private String workPhone;
	@Validation(table = "phone", column = "phone_number", required = false)
	private String workFax;
	@Validation(required = false)
	private boolean primaryIndicator;
	@Validation(table = "person", column = "personal_email", required = false)
	private String personalEmail;
	@Validation(table = "person", column = "job_title", required = false)
	private String jobTitle;
	@Validation(table = "person", column = "lname", required = true)
	private String lname;
	@Validation(table = "person", column = "fname", required = true)
	private String fname;
	@Validation(table = "prophet_login", column = "user_login", required = false)
	private String login;
	@Validation(table = "prophet_login", column = "user_password", required = false)
	private String empPassword;
	@Validation(required = false)
	private boolean canLogin;
	@Validation(required = false)
	private String screenGroupId;
	@Validation(required = true)
	private String orgGroupId;
	@Validation(table = "employee", column = "ssn", required = false)
	private String ssn;
	@Validation(table = "phone", column = "phone_number", required = false)
	private String homePhone;
	@Validation(table = "phone", column = "phone_number", required = false)
	private String mobilePhone;
	@Validation(table = "address", column = "street", required = false)
	private String addressLine1;
	@Validation(table = "address", column = "street2", required = false)
	private String addressLine2;
	@Validation(table = "address", column = "city", required = false)
	private String city;
	@Validation(table = "address", column = "state", required = false)
	private String stateProvince;
	@Validation(table = "address", column = "country_code", required = false)
	private String country;
	@Validation(table = "address", column = "county", required = false)
	private String county;
	@Validation(table = "address", column = "zip", required = false)
	private String zipPostalCode;
	@Validation(table = "employee", column = "sex", required = false)
	private String sex;
	@Validation(table = "employee", column = "ext_ref", required = false)
	private String extRef;
	@Validation(required = false)
	private String eeoCategoryId;
	@Validation(required = false)
	private String eeoRaceId;
	@Validation(type = "date", required = false)
	private int dob;
	@Validation(required = false)
	private boolean exempt;
	@Validation(required = false)
	private String securityGroupId;
	@Validation(table = "person", column = "mname", required = false)
	private String middleName;
	@Validation(type = "date", required = true)
	private int employeeStatusDate;
	@Validation(table = "hr_employee_status", column = "status_id", required = true)
	private String employeeStatusId;
	@Validation(required = false)
	private double billingRate;
	@Validation(required = false)
	private String positionId;
	@Validation(required = false)
	private String wageTypeId;
	@Validation(table = "hr_wage", column = "wage_amount", required = false)
	private double wageAmount;
	@Validation(table = "person", column = "citizenship", required = false)
	private String citizenship;

	private boolean i9Completed = false;
	private boolean i9Part1 = false;
	private boolean i9Part2 = false;

	@Validation(table = "person", column = "visa", required = false)
	private String visa;
	@Validation(table = "person", column = "visa_status_date", type = "date", required = false)
	private int visaStatusDate;
	@Validation(table = "person", column = "visa_exp_date", type = "date", required = false)
	private int visaExpirationDate;
	@Validation(table = "person", column = "nickname", required = false)
	private String nickName;
	@Validation(table = "person", column = "smoker", required = true)
	private String tabaccoUse;
	@Validation(table = "person", column = "drivers_license_exp", type = "date", required = false)
	private int driversLicenseExpirationDate;
	@Validation(table = "person", column = "drivers_license_number", required = false)
	private String driversLicenseNumber;
	@Validation(table = "person", column = "drivers_license_state", required = false)
	private String driversLicenseState;
	@Validation(table = "person", column = "auto_insurance_carrier", required = false)
	private String automotiveInsuranceCarrier;
	@Validation(table = "person", column = "auto_insurance_start", type = "date", required = false)
	private int automotiveInsuranceStartDate;
	@Validation(table = "person", column = "auto_insurance_policy", type = "date", required = false)
	private int automotiveInsuranceExpirationDate;
	@Validation(table = "person", column = "auto_insurance_exp", required = false)
	private String automotiveInsurancePolicyNumber;
	@Validation(table = "person", column = "auto_insurance_coverage", required = false)
	private String automotiveInsuranceCoverage;
	@Validation(table = "org_group", column = "default_project_id", required = false)
	private String defaultProjectId;
	@Validation(required = false)
	private String benefitClassId;
	private boolean hrAdmin;
	@Validation(required = false)
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

	/**
	 * @return Returns the employeeStatusDate.
	 */
	public int getEmployeeStatusDate() {
		return employeeStatusDate;
	}

	/**
	 * @param employeeStatusDate The employeeStatusDate to set.
	 */
	public void setEmployeeStatusDate(final int employeeStatusDate) {
		this.employeeStatusDate = employeeStatusDate;
	}

	/**
	 * @return Returns the employeeStatusId.
	 */
	public String getEmployeeStatusId() {
		return employeeStatusId;
	}

	/**
	 * @param employeeStatusId The employeeStatusId to set.
	 */
	public void setEmployeeStatusId(final String employeeStatusId) {
		this.employeeStatusId = employeeStatusId;
	}

	/**
	 * @return Returns the middleName.
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName The middleName to set.
	 */
	public void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return Returns the dob.
	 */
	public int getDob() {
		return dob;
	}

	/**
	 * @param dob The dob to set.
	 */
	public void setDob(final int dob) {
		this.dob = dob;
	}

	/**
	 * @return Returns the exempt.
	 */
	public boolean isExempt() {
		return exempt;
	}

	/**
	 * @param exempt The exempt to set.
	 */
	public void setExempt(final boolean exempt) {
		this.exempt = exempt;
	}

	/**
	 * @param be
	 * @throws ArahantException
	 */
	void makeEmployee(final BEmployee be) throws ArahantException {
		be.setWorkFax(workFax);
		be.setWorkPhone(workPhone);
		be.setPersonalEmail(personalEmail);
		be.setJobTitle(jobTitle);
		be.setLastName(lname);
		be.setFirstName(fname);
		be.setUserLogin(login);
		be.setUserPassword(empPassword, canLogin);
		be.setScreenGroupId(screenGroupId);
		be.setSecurityGroupId(securityGroupId);
		be.setSsn(ssn);
		be.setHomePhone(homePhone);
		be.setMobilePhone(mobilePhone);
		be.setStreet(addressLine1);
		be.setCity(city);
		be.setState(stateProvince);
		be.setZip(zipPostalCode);
		be.setCountry(country);
		be.setCounty(county);
		
		be.setEEOCategoryId(eeoCategoryId);
		be.setEEORaceId(eeoRaceId);
		
		be.setSex(sex);
		be.setExempt(exempt);
		be.setDob(dob);
		
		be.setExtRef(extRef);
		be.setMiddleName(middleName);
		be.setStreet2(addressLine2);
		be.setNickName(nickName);
		// be.setBillingRate((float) billingRate); No single employee billing rate anymore
		
		be.setCitizenship(citizenship);

		/*
		     Try to support the Flash and HTML interfaces (old and new schema).
		     When the Flash front-end was built, it used i9Completed.  i9Completed
		     was later split into i9Part1 and i9Part2.  The HTML front-end uses those
		     but the Flash still depends on i9Completed.  This code is an attempt to support
		     both interfaces assuming that the HTML version would be used in production.
		 */
		be.setI9Part1(false);
		be.setI9Part2(false);
		if (i9Completed) {
			be.setI9Part1(i9Completed);
			be.setI9Part2(i9Completed);
		} else {
			if (i9Part1)
				be.setI9Part1(i9Part1);
			if (i9Part2)
				be.setI9Part1(i9Part2);
		}

		be.setVisa(visa);
		be.setVisaStatusDate(visaStatusDate);
		be.setVisaExpirationDate(visaExpirationDate);
		
		be.setTabaccoUse(tabaccoUse);
		be.setDriversLicenseExpirationDate(driversLicenseExpirationDate);
		be.setDriversLicenseNumber(driversLicenseNumber);
		be.setDriversLicenseState(driversLicenseState);
		be.setAutomotiveInsuranceCarrier(automotiveInsuranceCarrier);
		be.setAutomotiveInsuranceStartDate(automotiveInsuranceStartDate);
		be.setAutomotiveInsuranceExpirationDate(automotiveInsuranceExpirationDate);
		be.setAutomotiveInsurancePolicyNumber(automotiveInsurancePolicyNumber);
		be.setAutomotiveInsuranceCoverage(automotiveInsuranceCoverage);
		
		be.setDefaultProjectId(defaultProjectId);
		
		be.setBenefitClassId(benefitClassId);
		be.setHrAdmin(hrAdmin ? 'Y' : 'N');
		be.setEmploymentType(workerType.charAt(0));
	}

	/**
	 * @return Returns the canLogin.
	 */
	public boolean isCanLogin() {
		return canLogin;
	}

	/**
	 * @param canLogin The canLogin to set.
	 */
	public void setCanLogin(final boolean canLogin) {
		this.canLogin = canLogin;
	}

	/**
	 * @return Returns the city.
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city The city to set.
	 */
	public void setCity(final String city) {
		this.city = city;
	}

	/**
	 * @return Returns the fname.
	 */
	public String getFname() {
		return fname;
	}

	/**
	 * @param fname The fname to set.
	 */
	public void setFname(final String fname) {
		this.fname = fname;
	}

	/**
	 * @return Returns the homePhone.
	 */
	public String getHomePhone() {
		return homePhone;
	}

	/**
	 * @param homePhone The homePhone to set.
	 */
	public void setHomePhone(final String homePhone) {
		this.homePhone = homePhone;
	}

	/**
	 * @return Returns the lname.
	 */
	public String getLname() {
		return lname;
	}

	/**
	 * @param lname The lname to set.
	 */
	public void setLname(final String lname) {
		this.lname = lname;
	}

	/**
	 * @return Returns the login.
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login The login to set.
	 */
	public void setLogin(final String login) {
		this.login = login;
	}

	/**
	 * @return Returns the mobilePhone.
	 */
	public String getMobilePhone() {
		return mobilePhone;
	}

	/**
	 * @param mobilePhone The mobilePhone to set.
	 */
	public void setMobilePhone(final String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	/**
	 * @return Returns the orgGroupId.
	 */
	public String getOrgGroupId() {
		return orgGroupId;
	}

	/**
	 * @param orgGroupId The orgGroupId to set.
	 */
	public void setOrgGroupId(final String orgGroupId) {
		this.orgGroupId = orgGroupId;
	}

	/**
	 * @return Returns the personalEmail.
	 */
	public String getPersonalEmail() {
		return personalEmail;
	}

	/**
	 * @param personalEmail The personalEmail to set.
	 */
	public void setPersonalEmail(final String personalEmail) {
		this.personalEmail = personalEmail;
	}

	/**
	 * @return Returns the primaryIndicator.
	 */
	public boolean isPrimaryIndicator() {
		return primaryIndicator;
	}

	/**
	 * @param primaryIndicator The primaryIndicator to set.
	 */
	public void setPrimaryIndicator(final boolean primaryIndicator) {
		this.primaryIndicator = primaryIndicator;
	}

	/**
	 * @return Returns the screenGroupId.
	 */
	public String getScreenGroupId() {
		return screenGroupId;
	}

	/**
	 * @param screenGroupId The screenGroupId to set.
	 */
	public void setScreenGroupId(final String screenGroupId) {
		this.screenGroupId = screenGroupId;
	}

	/**
	 * @return Returns the ssn.
	 */
	public String getSsn() {
		return ssn;
	}

	/**
	 * @param ssn The ssn to set.
	 */
	public void setSsn(final String ssn) {
		this.ssn = ssn;
	}
	
	public String getJobTitle() {
		return jobTitle;
	}
	
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	/**
	 * @return Returns the workFax.
	 */
	public String getWorkFax() {
		return workFax;
	}

	/**
	 * @param workFax The workFax to set.
	 */
	public void setWorkFax(final String workFax) {
		this.workFax = workFax;
	}

	/**
	 * @return Returns the workPhone.
	 */
	public String getWorkPhone() {
		return workPhone;
	}

	/**
	 * @param workPhone The workPhone to set.
	 */
	public void setWorkPhone(final String workPhone) {
		this.workPhone = workPhone;
	}

	/**
	 * @return Returns the empPassword.
	 */
	public String getEmpPassword() {
		return empPassword;
	}

	/**
	 * @param empPassword The empPassword to set.
	 */
	public void setEmpPassword(final String empPassword) {
		this.empPassword = empPassword;
	}

	/**
	 * @return Returns the eeoCategoryId.
	 */
	public String getEeoCategoryId() {
		return eeoCategoryId;
	}

	/**
	 * @param eeoCategoryId The eeoCategoryId to set.
	 */
	public void setEeoCategoryId(final String eeoCategoryId) {
		this.eeoCategoryId = eeoCategoryId;
	}

	/**
	 * @return Returns the eeoRaceId.
	 */
	public String getEeoRaceId() {
		return eeoRaceId;
	}

	/**
	 * @param eeoRaceId The eeoRaceId to set.
	 */
	public void setEeoRaceId(final String eeoRaceId) {
		this.eeoRaceId = eeoRaceId;
	}

	/**
	 * @return Returns the sex.
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @param sex The sex to set.
	 */
	public void setSex(final String sex) {
		this.sex = sex;
	}

	/**
	 * @return Returns the extRef.
	 */
	public String getExtRef() {
		return extRef;
	}

	/**
	 * @param extRef The extRef to set.
	 */
	public void setExtRef(final String extRef) {
		this.extRef = extRef;
	}

	/**
	 * @return Returns the securityGroupId.
	 */
	public String getSecurityGroupId() {
		return securityGroupId;
	}

	/**
	 * @param securityGroupId The securityGroupId to set.
	 */
	public void setSecurityGroupId(final String securityGroupId) {
		this.securityGroupId = securityGroupId;
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
	
	public void setNickName(String nickName) {
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
	
	public String getDefaultProjectId() {
		return defaultProjectId;
	}
	
	public void setDefaultProjectId(String defaultProjectId) {
		this.defaultProjectId = defaultProjectId;
	}
	
	public String getPositionId() {
		return positionId;
	}
	
	public void setPositionId(String positionId) {
		this.positionId = positionId;
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
	
	public String getWorkerType() {
		return workerType;
	}
	
	public void setWorkerType(String workerType) {
		this.workerType = workerType;
	}
}
