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

package com.arahant.services.standard.at.applicant;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BApplicant;
import com.arahant.business.BApplicantContact;
import com.arahant.business.BApplication;
import com.arahant.business.BPersonForm;

public class LoadApplicantReturn extends TransmitReturnBase {
    private String nickName;
    private String city;
    private String homePhone;
    private String mobilePhone;
    private String personalEmail;
    private String sex;
    private String ssn;
    private String stateProvince;
    private String addressLine1;
    private String addressLine2;
    private String workFax;
    private String workPhone;
    private String zipPostalCode;
    private String comments;
    private String companyId;
    private int dob;
    private String citizenship;
    private boolean i9Completed;
    private boolean i9Part1;
    private boolean i9Part2;
    private String visa;
    private int visaStatusDate;
    private int visaExpirationDate;
    private LoadApplicantReturnQuestions[] questionDetails;
    private String country;
    private String county;
    private String eeoRaceId;
    private String militaryBranch;
    private int enlistFromMonth;
    private int enlistToMonth;
    private int enlistFromYear;
    private int enlistToYear;
    private String dischargeRank;
    private int dateAvailable;
    private String dischargeType;
    private String convicted;
    private String convictedDescription;
    private int desiredSalary;
    private String workedFor;
    private String workedForWhen;
    private String dischargeExplain;


    private LoadApplicantReturnApplications[] applications;
    //private LoadApplicantReturnJobTypes []jobTypes;
    private LoadApplicantReturnContact[] contacts;
    private LoadApplicantReturnForms[] forms;

    void setData(BApplicant bc) {
        nickName = bc.getNickName();
        city = bc.getCity();
        homePhone = bc.getHomePhone();
        mobilePhone = bc.getMobilePhone();
        personalEmail = bc.getPersonalEmail();
        sex = bc.getSex();
        ssn = bc.getSsn();
        stateProvince = bc.getState();
        addressLine1 = bc.getStreet();
        addressLine2 = bc.getStreet2();
        workFax = bc.getWorkFaxNumber();
        workPhone = bc.getWorkPhoneNumber();
        zipPostalCode = bc.getZip();
        comments = bc.getComments();
        companyId = bc.getCompanyId();
        dob = bc.getDob();
        citizenship = bc.getCitizenship();
        i9Completed = bc.getI9Part1() && bc.getI9Part2();
        i9Part1 = bc.getI9Part1();
        i9Part2 = bc.getI9Part2();
        visa = bc.getVisa();
        visaStatusDate = bc.getVisaStatusDate();
        visaExpirationDate = bc.getVisaExpirationDate();
        country = bc.getCountry();
        county = bc.getCounty();
        eeoRaceId = bc.getRaceId();
        militaryBranch = bc.getMilitaryBranch() + "";
        enlistFromMonth = bc.getMilitaryStartDate() % 100;
        enlistToMonth = bc.getMilitaryEndDate() % 100;
        enlistFromYear = bc.getMilitaryStartDate() / 100;
        enlistToYear = bc.getMilitaryEndDate() / 100;
        dischargeRank = bc.getMilitaryRank();
        dateAvailable = bc.getDateAvailable();
        dischargeType = bc.getMilitaryDischargeType() + "";
        convicted = bc.getConvictedOfCrime() + "";
        convictedDescription = bc.getConvictedOfWhat();
        desiredSalary = bc.getDesiredSalary();
        workedFor = bc.getWorkedForCompanyBefore() + "";
        workedForWhen = bc.getWorkedForCompanyWhen();
        dischargeExplain = bc.getMilitaryDischargeExplain();
		
	/*	BJobType []jobs=bc.getJobTypes();
		jobTypes=new LoadApplicantReturnJobTypes[jobs.length];
		for (int loop=0;loop<jobs.length;loop++)
			jobTypes[loop]=new LoadApplicantReturnJobTypes(jobs[loop]);
	*/
        BApplication[] apps = bc.getApplications();
        applications = new LoadApplicantReturnApplications[apps.length];
        for (int loop = 0; loop < apps.length; loop++)
            applications[loop] = new LoadApplicantReturnApplications(apps[loop]);

        BApplicantContact[] cons = bc.getApplicantContacts();
        contacts = new LoadApplicantReturnContact[cons.length];
        for (int loop = 0; loop < cons.length; loop++)
            contacts[loop] = new LoadApplicantReturnContact(cons[loop]);

        BPersonForm[] frms = bc.getForms();
        forms = new LoadApplicantReturnForms[frms.length];
        for (int loop = 0; loop < forms.length; loop++)
            forms[loop] = new LoadApplicantReturnForms(frms[loop]);
    }

    public LoadApplicantReturnForms[] getForms() {
        return forms;
    }

    public void setForms(LoadApplicantReturnForms[] forms) {
        this.forms = forms;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getPersonalEmail() {
        return personalEmail;
    }

    public void setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public int getDob() {
        return dob;
    }

    public void setDob(int dob) {
        this.dob = dob;
    }

    public LoadApplicantReturnApplications[] getApplications() {
        return applications;
    }

    public void setApplications(LoadApplicantReturnApplications[] applications) {
        this.applications = applications;
    }

    public LoadApplicantReturnContact[] getContacts() {
        return contacts;
    }

    public void setContacts(LoadApplicantReturnContact[] contacts) {
        this.contacts = contacts;
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


    public LoadApplicantReturnQuestions[] getQuestionDetails() {
        return questionDetails;
    }

    public void setQuestionDetails(LoadApplicantReturnQuestions[] questionDetails) {
        this.questionDetails = questionDetails;
    }

    public String getEeoRaceId() {
        return eeoRaceId;
    }

    public void setEeoRaceId(String eeoRaceId) {
        this.eeoRaceId = eeoRaceId;
    }

    public int getDateAvailable() {
        return dateAvailable;
    }

    public void setDateAvailable(int dateAvailable) {
        this.dateAvailable = dateAvailable;
    }

    public String getDischargeRank() {
        return dischargeRank;
    }

    public void setDischargeRank(String dischargeRank) {
        this.dischargeRank = dischargeRank;
    }

    public String getDischargeType() {
        return dischargeType;
    }

    public void setDischargeType(String dischargeType) {
        this.dischargeType = dischargeType;
    }

    public int getEnlistFromMonth() {
        return enlistFromMonth;
    }

    public void setEnlistFromMonth(int enlistFromMonth) {
        this.enlistFromMonth = enlistFromMonth;
    }

    public int getEnlistFromYear() {
        return enlistFromYear;
    }

    public void setEnlistFromYear(int enlistFromYear) {
        this.enlistFromYear = enlistFromYear;
    }

    public int getEnlistToMonth() {
        return enlistToMonth;
    }

    public void setEnlistToMonth(int enlistToMonth) {
        this.enlistToMonth = enlistToMonth;
    }

    public int getEnlistToYear() {
        return enlistToYear;
    }

    public void setEnlistToYear(int enlistToYear) {
        this.enlistToYear = enlistToYear;
    }

    public String getMilitaryBranch() {
        return militaryBranch;
    }

    public void setMilitaryBranch(String militaryBranch) {
        this.militaryBranch = militaryBranch;
    }

    public String getConvicted() {
        return convicted;
    }

    public void setConvicted(String convicted) {
        this.convicted = convicted;
    }

    public String getConvictedDescription() {
        return convictedDescription;
    }

    public void setConvictedDescription(String convictedDescription) {
        this.convictedDescription = convictedDescription;
    }

    public int getDesiredSalary() {
        return desiredSalary;
    }

    public void setDesiredSalary(int desiredSalary) {
        this.desiredSalary = desiredSalary;
    }

    public String getWorkedFor() {
        return workedFor;
    }

    public void setWorkedFor(String workedFor) {
        this.workedFor = workedFor;
    }

    public String getWorkedForWhen() {
        return workedForWhen;
    }

    public void setWorkedForWhen(String workedForWhen) {
        this.workedForWhen = workedForWhen;
    }

    public String getDischargeExplain() {
        return dischargeExplain;
    }

    public void setDischargeExplain(String dischargeExplain) {
        this.dischargeExplain = dischargeExplain;
    }
}

	
