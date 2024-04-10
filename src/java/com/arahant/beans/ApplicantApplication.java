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
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = ApplicantApplication.TABLE_NAME)
public class ApplicantApplication extends ArahantBean implements Serializable {

	public static final String TABLE_NAME = "applicant_application";
	public static final String STATUS = "appStatus";
	public static final String SOURCE = "applicantSource";
	public static final String APPLICANT = "applicant";
	public static final String DATE = "applicationDate";
	public static final String ID = "applicantApplicationId";
	public static final String APPLICANT_POSITION = "applicantPosition";
	public static final String POSITION_TYPE = "position";

	private String applicantApplicationId;// character(16) NOT NULL,
	private Applicant applicant; //person_id character(16) NOT NULL,
	private ApplicantPosition applicantPosition;
	private int applicationDate;// integer NOT NULL DEFAULT 0,
	private ApplicantAppStatus appStatus; //applicant_app_status_id character(16) NOT NULL,
	private Date offerFirstGenerated;
	private Date offerLastGenerated;
	private Date offerFirstEmailed;
	private Date offerLastEmailed;
    private Date offerElectronicallySignedDate;
	private String offerElectronicallySignedIp;
	private Float payRate;
	private HrPosition position;
	private short phase = 0;  // internal status or phase
	private Date offerDeclinedDate;
	private Date offerRetractedDate;
	private Date offerLastViewedDate;

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "applicant_application_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return applicantApplicationId = IDGenerator.generate(this);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "applicant_app_status_id")
	public ApplicantAppStatus getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(ApplicantAppStatus appStatus) {
		this.appStatus = appStatus;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id")
	public Applicant getApplicant() {
		return applicant;
	}

	public void setApplicant(Applicant applicant) {
		this.applicant = applicant;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "applicant_position_id")
	public ApplicantPosition getApplicantPosition() {
		return applicantPosition;
	}

	public void setApplicantPosition(ApplicantPosition applicantPosition) {
		this.applicantPosition = applicantPosition;
	}

	@Id
	@Column(name = "applicant_application_id")
	public String getApplicantApplicationId() {
		return applicantApplicationId;
	}

	public void setApplicantApplicationId(String applicantApplicationId) {
		this.applicantApplicationId = applicantApplicationId;
	}

	@Column(name = "application_date")
	public int getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(int applicationDate) {
		this.applicationDate = applicationDate;
	}

	@Column(name = "offer_first_generated")
	public Date getOfferFirstGenerated() {
		return offerFirstGenerated;
	}

	public void setOfferFirstGenerated(Date offerFirstGenerated) {
		this.offerFirstGenerated = offerFirstGenerated;
	}

	@Column(name = "offer_last_generated")
	public Date getOfferLastGenerated() {
		return offerLastGenerated;
	}

	public void setOfferLastGenerated(Date offerLastGenerated) {
		this.offerLastGenerated = offerLastGenerated;
	}

	@Column(name = "offer_first_emailed")
	public Date getOfferFirstEmailed() {
		return offerFirstEmailed;
	}

	public void setOfferFirstEmailed(Date offerFirstEmailed) {
		this.offerFirstEmailed = offerFirstEmailed;
	}

	@Column(name = "offer_last_emailed")
	public Date getOfferLastEmailed() {
		return offerLastEmailed;
	}

	public void setOfferLastEmailed(Date offerLastEmailed) {
		this.offerLastEmailed = offerLastEmailed;
	}

	@Column(name = "offer_elec_signed_date")
	public Date getOfferElectronicallySignedDate() {
		return offerElectronicallySignedDate;
	}

	public void setOfferElectronicallySignedDate(Date offerElectronicallySignedDate) {
		this.offerElectronicallySignedDate = offerElectronicallySignedDate;
	}

	@Column(name = "offer_elec_signed_ip")
	public String getOfferElectronicallySignedIp() {
		return offerElectronicallySignedIp;
	}

	public void setOfferElectronicallySignedIp(String offerElectronicallySignedIp) {
		this.offerElectronicallySignedIp = offerElectronicallySignedIp;
	}

	@Column(name = "pay_rate")
	public Float getPayRate() {
		return payRate;
	}

	public void setPayRate(Float payRate) {
		this.payRate = payRate;
	}

	@Column(name = "phase")
	public short getPhase() {
		return phase;
	}

	public void setPhase(short phase) {
		this.phase = phase;
	}

	@Column(name = "offer_declined_date")
	public Date getOfferDeclinedDate() {
		return offerDeclinedDate;
	}

	public void setOfferDeclinedDate(Date offerDeclinedDate) {
		this.offerDeclinedDate = offerDeclinedDate;
	}

	@Column(name = "offer_retracted_date")
	public Date getOfferRetractedDate() {
		return offerRetractedDate;
	}

	public void setOfferRetractedDate(Date offerRetractedDate) {
		this.offerRetractedDate = offerRetractedDate;
	}

	@Column(name = "offer_last_viewed_date")
	public Date getOfferLastViewedDate() {
		return offerLastViewedDate;
	}

	public void setOfferLastViewedDate(Date offerLastViewedDate) {
		this.offerLastViewedDate = offerLastViewedDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "position_id")
	public HrPosition getPosition() {
		return position;
	}

	public void setPosition(HrPosition position) {
		this.position = position;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ApplicantApplication other = (ApplicantApplication) obj;
		return this.applicantApplicationId.equals(other.getApplicantApplicationId()) || (this.applicantApplicationId != null && this.applicantApplicationId.equals(other.getApplicantApplicationId()));
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 11 * hash + (this.applicantApplicationId != null ? this.applicantApplicationId.hashCode() : 0);
		return hash;
	}

}
