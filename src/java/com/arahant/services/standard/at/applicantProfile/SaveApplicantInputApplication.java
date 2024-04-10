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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.services.standard.at.applicantProfile;

public class SaveApplicantInputApplication {
	private String positionId;
	private String statusId;
	private String sourceId;
	private int date;
	private String id;
	private SaveApplicantInputContacts[] contacts;
	private String applicantPositionId;
	private String title;
	private Float payRate;
	private String applicationPositionId;

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SaveApplicantInputContacts[] getContacts() {
		if (contacts==null)
			contacts=new SaveApplicantInputContacts[0];
		return contacts;
	}

	public void setContacts(SaveApplicantInputContacts[] contacts) {
		this.contacts = contacts;
	}

	public String getApplicantPositionId() {
		return applicantPositionId;
	}

	public void setApplicantPositionId(String applicantPositionId) {
		this.applicantPositionId = applicantPositionId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Float getPayRate() {
		return payRate;
	}

	public void setPayRate(Float payRate) {
		this.payRate = payRate;
	}

	public String getApplicationPositionId() {
		return applicationPositionId;
	}

	public void setApplicationPositionId(String applicationPositionId) {
		this.applicationPositionId = applicationPositionId;
	}
}
