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

package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 */
@Entity
@Table(name=LifeEvent.TABLE_NAME)
public class LifeEvent extends ArahantBean implements Serializable {

	public static final String TABLE_NAME="life_event";
	public static final String LIFE_EVENT_ID = "lifeEventId";
	public static final String PERSON="person";
	public static final String EVENT_DATE="eventDate";
	public static final String CHANGE_REASON="changeReason";
	public static final String DESCRIPTION="description";
	public static final String DATE_REPORTED="dateReported";
	public static final String REPORTING_PERSON="reportingPerson";
	public static final String BENEFIT_JOINS="benefitJoins";

	private String lifeEventId;
	private Person person;
	private int eventDate;
	private HrBenefitChangeReason changeReason;
	private String description;
	private int dateReported;
	private Person reportingPerson;
	private Set<HrBenefitJoin> benefitJoins=new HashSet<HrBenefitJoin>();


	@ManyToOne
	@JoinColumn(name="bcr_id")
	public HrBenefitChangeReason getChangeReason() {
		return changeReason;
	}

	public void setChangeReason(HrBenefitChangeReason changeReason) {
		this.changeReason = changeReason;
	}

	@Column(name="date_reported")
	public int getDateReported() {
		return dateReported;
	}

	public void setDateReported(int dateReported) {
		this.dateReported = dateReported;
	}

	@Column(name="description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name="event_date")
	public int getEventDate() {
		return eventDate;
	}

	public void setEventDate(int eventDate) {
		this.eventDate = eventDate;
	}

	@Id
	@Column(name="life_event_id")
	public String getLifeEventId() {
		return lifeEventId;
	}

	public void setLifeEventId(String lifeEventId) {
		this.lifeEventId = lifeEventId;
	}

	@ManyToOne
	@JoinColumn(name="person_id")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@ManyToOne
	@JoinColumn(name="reporting_person_id")
	public Person getReportingPerson() {
		return reportingPerson;
	}

	public void setReportingPerson(Person reportingPerson) {
		this.reportingPerson = reportingPerson;
	}

	@OneToMany
	@JoinColumn(name="life_event_id")
	public Set<HrBenefitJoin> getBenefitJoins() {
		return benefitJoins;
	}

	public void setBenefitJoins(Set<HrBenefitJoin> benefitJoins) {
		this.benefitJoins = benefitJoins;
	}



	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "life_event_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return lifeEventId=IDGenerator.generate(this);
	}

}
