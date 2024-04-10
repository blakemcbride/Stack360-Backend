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
 * Created on Mar 13, 2007
 * 
 */
package com.arahant.services.standard.hr.employeeProfile;
import com.arahant.business.BEmployee;
import com.arahant.business.BProperty;
import com.arahant.exceptions.ArahantJessException;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.ArahantSession;



/**
 * 
 *
 * Created on Mar 13, 2007
 *
 */
public class LoadEmployeeProfileReturn extends TransmitReturnBase {


	public LoadEmployeeProfileReturn() {
		super();
	}

	private boolean ssnRequired = !BProperty.getBoolean("SSN Not Required");

	private String first, last, email, position, jobTitle, status, 
		w4status, wageType, addressLine1, city, stateProvince, zipPostalCode, homePhone,
		workPhone, mobilePhone, fax, loginId, loginStatus,sex;
	
	private int dob;
	private boolean exempt;
	
	private String addressLine2;
	private String county;
	private String country;
	
	private String eeoCategory,	eeoRace;
	
	private LoadEmployeeProfileItem []item;
	
	private String extRef;

	private String middleName;
	
	private String citizenship;
	private boolean i9Completed;
	private boolean i9Part1;
	private boolean i9Part2;
	private String visa;
	private int visaStatusDate;
	private int visaExpirationDate;
	private int statusDate;
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
	private String ssn;
	private boolean timeLog;
	private boolean overtimeLogout;
	private double workHours;
	private double breakHours;

	public boolean getSsnRequired() {
		return ssnRequired;
	}

	public void setSsnRequired(boolean ssnRequired) {
		this.ssnRequired = ssnRequired;
	}

	public double getBreakHours() {
		return breakHours;
	}

	public void setBreakHours(double breakHours) {
		this.breakHours = breakHours;
	}

	public boolean isOvertimeLogout() {
		return overtimeLogout;
	}

	public void setOvertimeLogout(boolean overtimeLogout) {
		this.overtimeLogout = overtimeLogout;
	}

	public boolean isTimeLog() {
		return timeLog;
	}

	public void setTimeLog(boolean timeLog) {
		this.timeLog = timeLog;
	}

	public double getWorkHours() {
		return workHours;
	}

	public void setWorkHours(double workHours) {
		this.workHours = workHours;
	}



	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
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
	
	public String getNickName() {
		return nickName;
	}
	
	public void setNickName(final String nickName) {
		this.nickName = nickName;
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
	 * @param employee
	 * @throws ArahantJessException 
	 */
	void setData(final BEmployee e) throws ArahantJessException {
		first=e.getFirstName();
		last=e.getLastName();
		email=e.getPersonalEmail();
		position=e.getPositionName();
		jobTitle=e.getJobTitle();
		status=e.getEmployeeStatusName();
		w4status=e.getW4StatusName();
		wageType=e.getWageTypeName();
		addressLine1=e.getStreet();
		city=e.getCity();
		stateProvince=e.getState();
		zipPostalCode=e.getZip();
		homePhone=e.getHomePhone();
		workPhone=e.getWorkPhoneNumber();
		mobilePhone=e.getMobilePhone();
		fax=e.getWorkFaxNumber();
		loginId=e.getUserLogin();
		loginStatus=e.getCanLogin()+"";
		sex=e.getSex();
		dob=e.getDob();
		exempt=e.getExempt();
		eeoCategory=e.getEEOCategory();
		eeoRace=e.getEEORace();
		extRef=e.getExtRef();
		middleName=e.getMiddleName();
		addressLine2=e.getStreet2();
		country=e.getCountry();
		county=e.getCounty();
		citizenship=e.getCitizenship();
		i9Completed=e.getI9Part1() && e.getI9Part2();
		i9Part1 = e.getI9Part1();
		i9Part2 = e.getI9Part2();
		visa=e.getVisa();
		visaStatusDate=e.getVisaStatusDate();
		visaExpirationDate=e.getVisaExpirationDate();
		statusDate=e.getEmployeeStatusDate();
		nickName=e.getNickName();
		tabaccoUse = e.getTabaccoUse();
		driversLicenseExpirationDate =  e.getDriversLicenseExpirationDate();
		driversLicenseNumber = e.getDriversLicenseNumber();
		driversLicenseState = e.getDriversLicenseState();
		automotiveInsuranceCarrier = e.getAutomotiveInsuranceCarrier();
		automotiveInsuranceStartDate = e.getAutomotiveInsuranceStartDate();
		automotiveInsuranceExpirationDate = e.getAutomotiveInsuranceExpirationDate();
		automotiveInsurancePolicyNumber = e.getAutomotiveInsurancePolicyNumber();
		automotiveInsuranceCoverage = e.getAutomotiveInsuranceCoverage();
		ssn=e.getSsn();
		
		final String []types=e.getPaidTimeOffTypes();
		
		item=new LoadEmployeeProfileItem[types.length];
		
		for (int loop=0;loop<types.length;loop++)
		{
			item[loop]=new LoadEmployeeProfileItem();
			item[loop].setAccrualAccountName(types[loop]);
			item[loop].setHours(e.getHoursLeftOnBenefit(types[loop]));
                   //     ArahantSession.resetAI();
		}


		timeLog=e.getClockInTimeLog();
		overtimeLogout=e.getOvertimeLogout();
		workHours=e.getWorkHours();
		breakHours=e.getBreakHours();
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
	 * @return Returns the email.
	 */
	public String getEmail() {
		return email;
	}




	/**
	 * @param email The email to set.
	 */
	public void setEmail(final String email) {
		this.email = email;
	}




	/**
	 * @return Returns the fax.
	 */
	public String getFax() {
		return fax;
	}




	/**
	 * @param fax The fax to set.
	 */
	public void setFax(final String fax) {
		this.fax = fax;
	}




	/**
	 * @return Returns the first.
	 */
	public String getFirst() {
		return first;
	}




	/**
	 * @param first The first to set.
	 */
	public void setFirst(final String first) {
		this.first = first;
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
	 * @return Returns the item.
	 */
	public LoadEmployeeProfileItem[] getItem() {
		return item;
	}




	/**
	 * @param item The item to set.
	 */
	public void setItem(final LoadEmployeeProfileItem[] item) {
		this.item = item;
	}




	/**
	 * @return Returns the last.
	 */
	public String getLast() {
		return last;
	}




	/**
	 * @param last The last to set.
	 */
	public void setLast(final String last) {
		this.last = last;
	}




	/**
	 * @return Returns the loginId.
	 */
	public String getLoginId() {
		return loginId;
	}




	/**
	 * @param loginId The loginId to set.
	 */
	public void setLoginId(final String loginId) {
		this.loginId = loginId;
	}




	/**
	 * @return Returns the loginStatus.
	 */
	public String getLoginStatus() {
		return loginStatus;
	}




	/**
	 * @param loginStatus The loginStatus to set.
	 */
	public void setLoginStatus(final String loginStatus) {
		this.loginStatus = loginStatus;
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
	 * @return Returns the position.
	 */
	public String getPosition() {
		return position;
	}




	/**
	 * @param position The position to set.
	 */
	public void setPosition(final String position) {
		this.position = position;
	}


	/**
	 * @return Returns the status.
	 */
	public String getStatus() {
		return status;
	}




	/**
	 * @param status The status to set.
	 */
	public void setStatus(final String status) {
		this.status = status;
	}





	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}


	/**
	 * @return Returns the w4status.
	 */
	public String getW4status() {
		return w4status;
	}




	/**
	 * @param w4status The w4status to set.
	 */
	public void setW4status(final String w4status) {
		this.w4status = w4status;
	}




	/**
	 * @return Returns the wageType.
	 */
	public String getWageType() {
		return wageType;
	}




	/**
	 * @param wageType The wageType to set.
	 */
	public void setWageType(final String wageType) {
		this.wageType = wageType;
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
	 * @return Returns the eeoCategory.
	 */
	public String getEeoCategory() {
		return eeoCategory;
	}



	/**
	 * @param eeoCategory The eeoCategory to set.
	 */
	public void setEeoCategory(final String eeoCategory) {
		this.eeoCategory = eeoCategory;
	}


	/**
	 * @return Returns the eeoRace.
	 */
	public String getEeoRace() {
		return eeoRace;
	}


	/**
	 * @param eeoRace The eeoRace to set.
	 */
	public void setEeoRace(final String eeoRace) {
		this.eeoRace = eeoRace;
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

	public int getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(int statusDate) {
		this.statusDate = statusDate;
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

	
