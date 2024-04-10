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


package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Formula;

/**
 *
 */
@Entity
@Table(name = Applicant.TABLE_NAME)
public class Applicant extends ArahantBean implements Serializable {

	public static final String APPLICANT_SOURCE = "applicantSource";
	public static final String APPLICANT_STATUS = "applicantStatus";
	public static final String FIRST_AWARE_DATE = "firstAwareDate";
//	public static final String JOB_TYPES="jobTypeInterests";
	private static final long serialVersionUID = -8954906138180888278L;
	public static final String APPLICATIONS = "applications";
	public static final String ANSWERS = "answers";
	public static final String PERSON = "person";
	public static final String PERSONID = "personId";
	public static final String IS_EMPLOYEE = "isEmployee";
	public static final String TABLE_NAME = "applicant";
	private ApplicantSource applicantSource;
	private ApplicantStatus applicantStatus;
	private int firstAwareDate;
	private String comments;
//	private Set<JobType> jobTypeInterests=new HashSet<JobType>();
	private Set<ApplicantApplication> applications = new HashSet<ApplicantApplication>();
	private Set<ApplicantAnswer> answers = new HashSet<ApplicantAnswer>();
	private Person person;
	private String personId;
	private int isEmployee; //0==not an employee
	private HrEeoRace hrEeoRace;
	public static final String HREEORACE = "hrEeoRace";
	private int dateAvailable;
	public static final String DATE_AVAILABLE = "dateAvailable";
	private int desiredSalary;
	private String referredBy;
	private Short yearsExperience;
	private Character dayShift;
	private Character nightShift;
	private Character veteran;
	private String signature;
	private Date whenSigned;
	private char travelPersonal = 'N';
	private char travelFriend = 'N';
    private char travelPublic = 'N';
	private char travelUnknown = 'N';
	private char agrees = 'N';
	private String agreementName;
	private Date agreementDate;
	private char backgroundCheckAuthorized = 'N';

	@OneToMany(mappedBy = ApplicantAnswer.APPLICANT, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<ApplicantAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(Set<ApplicantAnswer> answers) {
		this.answers = answers;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "applicant_source_id")
	public ApplicantSource getApplicantSource() {
		return applicantSource;
	}

	public void setApplicantSource(ApplicantSource applicantSource) {
		this.applicantSource = applicantSource;
	}

	@Formula("(select count(*) from employee where employee.person_id=person_id)")
	public int getIsEmployee() {
		return isEmployee;
	}

	public void setIsEmployee(int isEmployee) {
		this.isEmployee = isEmployee;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "applicant_status_id")
	public ApplicantStatus getApplicantStatus() {
		return applicantStatus;
	}

	public void setApplicantStatus(ApplicantStatus applicantStatus) {
		this.applicantStatus = applicantStatus;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Column(name = "first_aware_date")
	public int getFirstAwareDate() {
		return firstAwareDate;
	}

	public void setFirstAwareDate(int firstAwareDate) {
		this.firstAwareDate = firstAwareDate;
	}

	@OneToMany(mappedBy = ApplicantApplication.APPLICANT, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<ApplicantApplication> getApplications() {
		return applications;
	}

	public void setApplications(Set<ApplicantApplication> applications) {
		this.applications = applications;
	}

	@OneToOne
	@JoinColumn(name = "person_id", insertable = false, updatable = false)
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
		if (person != null)
			personId = person.getPersonId();
	}

	/**
	 * @return Returns the hrEeoRace.
	 */
	@ManyToOne
	@JoinColumn(name = "eeo_race_id")
	public HrEeoRace getHrEeoRace() {
		return hrEeoRace;
	}

	/**
	 * @param hrEeoRace The hrEeoRace to set.
	 */
	public void setHrEeoRace(HrEeoRace hrEeoRace) {
		this.hrEeoRace = hrEeoRace;
	}

	@Column(name = "date_available")
	public int getDateAvailable() {
		return dateAvailable;
	}

	public void setDateAvailable(int dateAvailable) {
		this.dateAvailable = dateAvailable;
	}

	@Column(name = "desired_salary")
	public int getDesiredSalary() {
		return desiredSalary;
	}

	public void setDesiredSalary(int desiredSalary) {
		this.desiredSalary = desiredSalary;
	}

	@Column(name = "referred_by")
	public String getReferredBy() {
		return referredBy;
	}

	public void setReferredBy(String referredBy) {
		this.referredBy = referredBy;
	}

	@Column(name = "years_experience")
	public Short getYearsExperience() {
		return yearsExperience;
	}

	public void setYearsExperience(Short yearsExperience) {
		this.yearsExperience = yearsExperience;
	}

	@Column(name = "day_shift")
	public Character getDayShift() {
		return dayShift;
	}

	public void setDayShift(Character dayShift) {
		this.dayShift = dayShift;
	}

	@Column(name = "night_shift")
	public Character getNightShift() {
		return nightShift;
	}

	public void setNightShift(Character nightShift) {
		this.nightShift = nightShift;
	}

	@Column(name = "veteran")
	public Character getVeteran() {
		return veteran;
	}

	public void setVeteran(Character veteran) {
		this.veteran = veteran;
	}

	@Column(name = "signature")
	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	@Column(name = "when_signed")
	public Date getWhenSigned() {
		return whenSigned;
	}

	public void setWhenSigned(Date whenSigned) {
		this.whenSigned = whenSigned;
	}

	@Column(name = "travel_personal")
	public char getTravelPersonal() {
		return travelPersonal;
	}

	public void setTravelPersonal(char travelPersonal) {
		this.travelPersonal = travelPersonal;
	}

	@Column(name = "travel_friend")
	public char getTravelFriend() {
		return travelFriend;
	}

	public void setTravelFriend(char travelFriend) {
		this.travelFriend = travelFriend;
	}

	@Column(name = "travel_public")
	public char getTravelPublic() {
		return travelPublic;
	}

	public void setTravelPublic(char travelPublic) {
		this.travelPublic = travelPublic;
	}

	@Column(name = "travel_unknown")
	public char getTravelUnknown() {
		return travelUnknown;
	}

	public void setTravelUnknown(char travelUnknown) {
		this.travelUnknown = travelUnknown;
	}

	@Column(name = "agrees")
	public char getAgrees() {
		return agrees;
	}

	public void setAgrees(char agrees) {
		this.agrees = agrees;
	}

	@Column(name = "agreement_name")
	public String getAgreementName() {
		return agreementName;
	}

	public void setAgreementName(String agreementName) {
		this.agreementName = agreementName;
	}

	@Column(name = "agreement_date")
	public Date getAgreementDate() {
		return agreementDate;
	}

	@Column(name = "background_check_authorized")
	public char getBackgroundCheckAuthorized() {
		return backgroundCheckAuthorized;
	}

	public void setBackgroundCheckAuthorized(char backgroundCheckAuthorized) {
		this.backgroundCheckAuthorized = backgroundCheckAuthorized;
	}

	public void setAgreementDate(Date agreementDate) {
		this.agreementDate = agreementDate;
	}

	@Id
	@Column(name = "person_id")
	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		throw new UnsupportedOperationException("Don't generate key like that.");
	}

	@Override
	public String generateId() throws ArahantException {
		throw new UnsupportedOperationException("Don't generate key like that.");
	}
}
