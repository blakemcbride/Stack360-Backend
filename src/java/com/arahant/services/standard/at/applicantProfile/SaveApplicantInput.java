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

package com.arahant.services.standard.at.applicantProfile;

import com.arahant.business.*;
import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;
import com.arahant.utils.Mail;

import java.util.LinkedList;
import java.util.List;
import org.kissweb.StringUtils;

public class SaveApplicantInput extends TransmitInputBase {

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
    @Validation(table = "address", column = "street", required = false)
    private String addressLine1;
    @Validation(table = "address", column = "street2", required = false)
    private String addressLine2;
    @Validation(type = "date", required = false)
    private int dob;
    @Validation(table = "phone", column = "phone_number", required = false)
    private String homePhone;
    @Validation(table = "phone", column = "phone_number", required = false)
    private String mobilePhone;
    @Validation(table = "phone", column = "phone_number", required = false)
    private String workFax;
    @Validation(table = "phone", column = "phone_number", required = false)
    private String workPhone;
    @Validation(table = "person", column = "fname", required = true)
    private String firstName;
    @Validation(table = "person", column = "lname", required = true)
    private String lastName;
    @Validation(table = "person", column = "mname", required = false)
    private String middleName;
    @Validation(table = "person", column = "nickname", required = false)
    private String nickName;
    @Validation(table = "person", column = "personal_email", required = false)
    private String personalEmail;
    @Validation(table = "person", column = "sex", required = false)
    private String sex;
    @Validation(table = "person", column = "ssn", required = false)
    private String ssn;
    @Validation(min = 0, max = 16, required = true)
    private String applicantStatusId;
    @Validation(min = 0, max = 16, required = true)
    private String applicantSourceId;
    @Validation(table = "applicant", column = "comments", required = false)
    private String comments;
    @Validation(type = "date", required = true)
    private int firstAwareDate;
    @Validation(min = 1, max = 16, required = true)
    private String id;
    //	@Validation (type="array", required=false)
//	private String []jobTypeIds;
    @Validation(type = "array", required = false)
    private SaveApplicantInputApplication[] applications;
    @Validation(required = false, type = "array")
    private SaveApplicantInputContacts[] contacts;
    @Validation(table = "person", column = "citizenship", required = false)
    private String citizenship;

    private boolean i9Completed = false;
    private boolean i9Part1 = false;
    private boolean i9Part2 = false;

    @Validation(table = "person", column = "visa", required = false)
    private String visa;
    @Validation(table = "person", column = "visa_status_date", required = false, type = "data")
    private int visaStatusDate;
    @Validation(table = "person", column = "visa_exp_date", required = false, type = "data")
    private int visaExpirationDate;
    @Validation(type = "array", required = false)
    private SaveApplicantInputQuestion[] questionDetails;
    @Validation(type = "array", required = false)
    private SaveApplicantInputForm[] forms;
    @Validation(required = false)
    private String eeoRaceId;
    @Validation(table = "person", column = "military_branch", required = false)
    private String militaryBranch;
    @Validation(required = false)
    private int enlistFromMonth;
    @Validation(required = false)
    private int enlistToMonth;
    @Validation(required = false)
    private int enlistFromYear;
    @Validation(required = false)
    private int enlistToYear;
    @Validation(table = "person", column = "military_rank", required = false)
    private String dischargeRank;
    @Validation(type = "date", required = true)
    private int dateAvailable;
    @Validation(table = "person", column = "military_discharge_type", required = false)
    private String dischargeType;
    @Validation(table = "person", column = "convicted_of_crime", required = false)
    private String convicted;
    @Validation(table = "person", column = "convicted_of_what", required = false)
    private String convictedDescription;
    @Validation(table = "applicant", column = "desired_salary", required = false)
    private int desiredSalary;
    @Validation(table = "person", column = "worked_for_company_before", required = true)
    private String workedFor;
    @Validation(table = "person", column = "worked_for_company_when", required = false)
    private String workedForWhen;
    @Validation(table = "person", column = "military_discharge_explain", required = false)
    private String dischargeExplain;
    @Validation(required = true)
    private boolean sendEmail;
    private String referredBy;
    private String veteran;


    void setData(BApplicant bc) throws Exception {
        bc.setCity(city);
        bc.setState(stateProvince);
        bc.setZip(zipPostalCode);
        bc.setStreet(addressLine1);
        bc.setStreet2(addressLine2);
        bc.setCountry(country);
        bc.setCounty(county);
        bc.setDob(dob);
        bc.setHomePhone(homePhone);
        bc.setMobilePhone(mobilePhone);
        bc.setWorkFax(workFax);
        bc.setWorkPhone(workPhone);
        bc.setFirstName(firstName);
        bc.setLastName(lastName);
        bc.setMiddleName(middleName);
        bc.setNickName(nickName);
        bc.setPersonalEmail(personalEmail);
        bc.setSex(sex);
        bc.setSsn(ssn != null && !ssn.isEmpty() ? ssn : null);
        BChangeLog.applicantStatusChange(bc.getPersonId(), bc.getApplicantStatusId(), applicantStatusId, null);
        bc.setApplicantStatusId(applicantStatusId);
        bc.setApplicantSourceId(applicantSourceId);
        bc.setComments(comments);
        bc.setFirstAwareDate(firstAwareDate);
        bc.setCitizenship(citizenship);

		/*
		     Try to support the Flash and HTML interfaces (old and new schema).
		     When the Flash front-end was built, it used i9Completed.  i9Completed
		     was later split into i9Part1 and i9Part2.  The HTML front-end uses those
		     but the Flash still depends on i9Completed.  This code is an attempt to support
		     both interfaces assuming that the HTML version would be used in production.
		 */
        bc.setI9Part1(false);
        bc.setI9Part2(false);
        if (i9Completed) {
            bc.setI9Part1(i9Completed);
            bc.setI9Part2(i9Completed);
        } else {
            if (i9Part1)
                bc.setI9Part1(i9Part1);
            if (i9Part2)
                bc.setI9Part2(i9Part2);
        }

        bc.setVisa(visa);
        bc.setVisaStatusDate(visaStatusDate);
        bc.setVisaExpirationDate(visaExpirationDate);
        bc.setRaceId(eeoRaceId);

        if (!isEmpty(militaryBranch)) {
            bc.setMilitaryBranch(militaryBranch.charAt(0));
            bc.setMilitaryStartDate((enlistFromYear * 100) + enlistFromMonth);
            bc.setMilitaryEndDate((enlistToYear * 100) + enlistToMonth);
            bc.setMilitaryRank(dischargeRank);
        } else {
            bc.setMilitaryBranch('U');
            bc.setMilitaryStartDate(0);
            bc.setMilitaryEndDate(0);
            bc.setMilitaryRank("");
        }

        if (!isEmpty(dischargeType)) {
            bc.setMilitaryDischargeType(dischargeType.charAt(0));
            bc.setMilitaryDischargeExplain(dischargeExplain);
        } else {
            bc.setMilitaryDischargeType('U');
            bc.setMilitaryDischargeExplain("");
        }

        if (!isEmpty(convicted)) {
            bc.setConvictedOfCrime(convicted.charAt(0));
            bc.setConvictedOfWhat(convictedDescription);
        } else {
            bc.setConvictedOfCrime('U');
            bc.setConvictedOfWhat("");
        }

        bc.setDateAvailable(dateAvailable);
        bc.setDesiredSalary(desiredSalary);
        bc.setWorkedForCompanyBefore(workedFor.charAt(0));
        bc.setWorkedForCompanyWhen(workedForWhen);

        bc.setReferredBy(referredBy);
        bc.setVeteran(StringUtils.stringToCharacter(veteran));

        //bc.setJobTypes(getJobTypeIds());
        //bc.clearApplicationAnswers();

        final List<String> ids = new LinkedList<String>();

        //handle changes
        for (int loop = 0; loop < getApplications().length; loop++) {
            SaveApplicantInputApplication ai = applications[loop];
            if (applications[loop].getId() == null || ai.getId().trim().equals(""))
                continue;

            ids.add(ai.getId());
            BApplication app = new BApplication(ai.getId());
            BChangeLog.applicantAppStatusChange(bc.getPersonId(), app.getStatusId(), ai.getStatusId(), null);
            app.setApplicantPosition(new BApplicantPosition(ai.getApplicantPositionId()).getBean());
            app.setDate(ai.getDate());
            app.setStatusId(ai.getStatusId());
            app.setPayRate(ai.getPayRate());
            app.setPosition(new BHRPosition(ai.getApplicationPositionId()).getBean());
            bc.addPendingUpdate(app);

            List<String> contactIds = new LinkedList<String>();

            for (int loop2 = 0; loop2 < ai.getContacts().length; loop2++) {
                if (ai.getContacts()[loop2].getId() == null ||
                        ai.getContacts()[loop2].getId().trim().equals(""))
                    continue;
                contactIds.add(ai.getContacts()[loop2].getId());
                BApplicantContact con = new BApplicantContact(ai.getContacts()[loop2].getId());
                con.setApplication(app);
                con.setDate(ai.getContacts()[loop2].getDate());
                con.setTime(ai.getContacts()[loop2].getTime());
                con.setDescription(ai.getContacts()[loop2].getDescription());
                con.setMode(ai.getContacts()[loop2].getMode());
                con.setStatus(ai.getContacts()[loop2].getStatus());
                app.addPendingUpdate(con);
            }

            //now delete ones that weren't sent up
            app.deleteContactsNotIn(contactIds);

            //now add new ones
            for (int loop2 = 0; loop2 < ai.getContacts().length; loop2++) {
                if (ai.getContacts()[loop2].getId() != null &&
                        !ai.getContacts()[loop2].getId().trim().equals(""))
                    continue;
                BApplicantContact con = new BApplicantContact();
                con.create();
                con.setApplication(app);
                con.setDate(ai.getContacts()[loop2].getDate());
                con.setTime(ai.getContacts()[loop2].getTime());
                con.setDescription(ai.getContacts()[loop2].getDescription());
                con.setMode(ai.getContacts()[loop2].getMode());
                con.setStatus(ai.getContacts()[loop2].getStatus());
                app.addPendingInsert(con);
            }
        }

        //now delete ones that weren't sent up
        bc.deleteApplicationsNotIn(ids);

        //handle new entries
        for (int loop = 0; loop < getApplications().length; loop++) {
            SaveApplicantInputApplication ai = applications[loop];
            if (ai.getId() != null && !ai.getId().trim().equals(""))
                continue;

            BApplication app = new BApplication();
            app.create();
            app.setApplicant(bc);
            app.setApplicantPosition(new BApplicantPosition(ai.getApplicantPositionId()).getBean());
            app.setDate(ai.getDate());
            app.setStatusId(ai.getStatusId());
            app.setPayRate(ai.getPayRate());
            app.setPosition(new BHRPosition(ai.getApplicationPositionId()).getBean());
            bc.addPendingInsert(app);
            BChangeLog.applicantAppStatusChange(bc.getPersonId(), null, ai.getStatusId(), "New job application created");

            for (int loop2 = 0; loop2 < ai.getContacts().length; loop2++) {
                BApplicantContact con = new BApplicantContact();
                SaveApplicantInputContacts ci = ai.getContacts()[loop2];
                con.create();
                con.setApplication(app);
                con.setDate(ci.getDate());
                con.setTime(ci.getTime());
                con.setDescription(ci.getDescription());
                con.setMode(ci.getMode());
                con.setStatus(ci.getStatus());
                app.addPendingInsert(con);
            }
        }

        List<String> contactIds = new LinkedList<String>();

        for (int loop2 = 0; loop2 < getContacts().length; loop2++) {
            SaveApplicantInputContacts ci = getContacts()[loop2];
            if (ci.getId() == null || ci.getId().trim().equals(""))
                continue;
            contactIds.add(ci.getId());
            BApplicantContact con = new BApplicantContact(getContacts()[loop2].getId());
            con.setApplicant(bc);
            con.setDate(ci.getDate());
            con.setTime(ci.getTime());
            con.setDescription(ci.getDescription());
            con.setMode(ci.getMode());
            con.setStatus(ci.getStatus());
            bc.addPendingUpdate(con);
        }

        //now delete ones that weren't sent up
        bc.deleteContactsNotIn(contactIds);

        //now add new ones
        for (int loop2 = 0; loop2 < getContacts().length; loop2++) {
            if (getContacts()[loop2].getId() != null &&
                    !getContacts()[loop2].getId().trim().equals(""))
                continue;
            BApplicantContact con = new BApplicantContact();
            con.create();
            con.setApplicant(bc);
            con.setDate(getContacts()[loop2].getDate());
            con.setTime(getContacts()[loop2].getTime());
            con.setDescription(getContacts()[loop2].getDescription());
            con.setMode(getContacts()[loop2].getMode());
            con.setStatus(getContacts()[loop2].getStatus());
            bc.addPendingInsert(con);
        }

        SaveApplicantInputQuestion[] qd = getQuestionDetails();
        for (SaveApplicantInputQuestion q : qd) {
            if (!bc.hasPendingAnswer(q.getApplicantQuestionId())) {
                BApplicantAnswer bans = new BApplicantAnswer();
                bans.create();
                bans.setQuestionId(q.getApplicantQuestionId());
                bans.setAnswer(q.getAnswerType(), q.getTextBasedAnswer(), q.getNumberBasedAnswer(), q.getListBasedAnswerId());
                bans.setApplicant(bc);
                bc.addPendingInsert(bans);
            }
        }

        List<String> formIds = new LinkedList<String>();
        for (SaveApplicantInputForm f : getForms()) {
            formIds.add(f.getId());
            BPersonForm bf = new BPersonForm(f.getId());
            f.setData(bf);
            bf.update();
        }
        bc.deleteFormsNotIn(formIds);

        if (sendEmail && !isEmpty(bc.getPersonalEmail())) {
            BApplicantStatus bs = new BApplicantStatus(applicantStatusId);
            Mail.send(bs.getEmailSource(), bc.getPersonalEmail(), bs.getEmailSubject(), bs.getEmailText());
        }
    }

    public SaveApplicantInputApplication[] getApplications() {
        if (applications == null)
            applications = new SaveApplicantInputApplication[0];
        return applications;
    }

    public void setApplications(SaveApplicantInputApplication[] applications) {
        this.applications = applications;
    }

    public SaveApplicantInputForm[] getForms() {
        if (forms == null)
            forms = new SaveApplicantInputForm[0];
        return forms;
    }

    public void setForms(SaveApplicantInputForm[] forms) {
        this.forms = forms;
    }

    /*
        public String[] getJobTypeIds() {
            if (jobTypeIds==null)
                jobTypeIds=new String[0];
            return jobTypeIds;
        }

        public void setJobTypeIds(String[] jobTypeIds) {
            this.jobTypeIds = jobTypeIds;
        }
    */
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
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

    public int getDob() {
        return dob;
    }

    public void setDob(int dob) {
        this.dob = dob;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public String getApplicantStatusId() {
        return applicantStatusId;
    }

    public void setApplicantStatusId(String applicantStatusId) {
        this.applicantStatusId = applicantStatusId;
    }

    public String getApplicantSourceId() {
        return applicantSourceId;
    }

    public void setApplicantSourceId(String applicantSourceId) {
        this.applicantSourceId = applicantSourceId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getFirstAwareDate() {
        return firstAwareDate;
    }

    public void setFirstAwareDate(int firstAwareDate) {
        this.firstAwareDate = firstAwareDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SaveApplicantInputContacts[] getContacts() {
        if (contacts == null)
            contacts = new SaveApplicantInputContacts[0];
        return contacts;
    }

    public void setContacts(SaveApplicantInputContacts[] contacts) {
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


    public SaveApplicantInputQuestion[] getQuestionDetails() {
        if (questionDetails == null)
            questionDetails = new SaveApplicantInputQuestion[0];
        return questionDetails;
    }

    public void setQuestionDetails(SaveApplicantInputQuestion[] questionDetails) {
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

    public boolean isSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(boolean sendEmail) {
        this.sendEmail = sendEmail;
    }

    public String getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(String referredBy) {
        this.referredBy = referredBy;
    }

    public String getVeteran() {
        return veteran;
    }

    public void setVeteran(String veteran) {
        this.veteran = veteran;
    }

}

	
