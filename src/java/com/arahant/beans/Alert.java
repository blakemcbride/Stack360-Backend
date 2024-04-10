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
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 *
 */
@Entity
@Table(name = Alert.TABLE_NAME)
public class Alert extends ArahantBean implements Serializable {

	public final static String TABLE_NAME = "alert";
	public final static String START_DATE = "startDate";
	public final static String END_DATE = "lastDate";
	public final static String DISTRIBUTION = "alertDistribution";
	public final static String CHANGE_PERSON = "lastChangePerson";
	public final static String ORG_GROUP = "orgGroup";
	public final static String PERSONS = "persons";
	public final static short DIST_ALL_EMPLOYEES = 1;
	public final static short DIST_PARTICULAR_EMPLOYEES = 2;
	public final static short DIST_ALL_EMPLOYEES_FROM_AGENT = 3;
	public final static short DIST_ALL_AGENTS_COMPANY = 4;
	public final static short DIST_ALL_AGENTS = 5;
	public final static short DIST_ALL_COMPANY_MAIN_CONTACTS = 6;
	/*
	 * 1 = to all employees of a company 2 = particular employees in a company 3
	 * = to all employees of a company (from agent) 4 = to all members of an
	 * agent''s company 5 = to all agents 6 = all company main contacts';
	 */
	private String alertId;
	private short alertDistribution;
	private int startDate;
	private int lastDate;
	private String alertShort;
	private String alertLong;
	private OrgGroup orgGroup;
	private Person person;
	private Person lastChangePerson;
	private Date lastChangeDateTime;
	private Set<Person> persons = new HashSet<Person>(0);

	@ManyToMany
	@JoinTable(name = "alert_person_join",
	joinColumns = {
		@JoinColumn(name = "alert_id")},
	inverseJoinColumns = {
		@JoinColumn(name = "person_id")})
	@OrderBy(value = "lname")
	public Set<Person> getPersons() {
		return persons;
	}

	public void setPersons(Set<Person> persons) {
		this.persons = persons;
	}

	@Column(name = "alert_distribution")
	public short getAlertDistribution() {
		return alertDistribution;
	}

	public void setAlertDistribution(short alertDistribution) {
		this.alertDistribution = alertDistribution;
	}

	@Id
	@Column(name = "alert_id")
	public String getAlertId() {
		return alertId;
	}

	public void setAlertId(String alertId) {
		this.alertId = alertId;
	}

	public void setAlert(String alert) {
		if (alert.length() > 2000) {
			setAlertLong(alert);
			setAlertShort(null);
		} else {
			setAlertShort(alert);
			setAlertLong(null);
		}
	}

	@Transient
	public String getAlert() {
		String alert = getAlertShort();
		if (alert != null && !alert.equals(""))
			return alert;
		else
			return getAlertLong();
	}

	@Column(name = "alert_long")
	public String getAlertLong() {
		return alertLong;
	}

	public void setAlertLong(String alertLong) {
		this.alertLong = alertLong;
	}

	@Column(name = "alert_short")
	public String getAlertShort() {
		return alertShort;
	}

	public void setAlertShort(String alertShort) {

		this.alertShort = alertShort;
	}

	@Column(name = "last_chage_datetime")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getLastChangeDateTime() {
		return lastChangeDateTime;
	}

	public void setLastChangeDateTime(Date lastChangeDateTime) {
		this.lastChangeDateTime = lastChangeDateTime;
	}

	@ManyToOne
	@JoinColumn(name = "last_change_person_id")
	public Person getLastChangePerson() {
		return lastChangePerson;
	}

	public void setLastChangePerson(Person lastChangePerson) {
		this.lastChangePerson = lastChangePerson;
	}

	@Column(name = "last_date")
	public int getLastDate() {
		return lastDate;
	}

	public void setLastDate(int lastDate) {
		this.lastDate = lastDate;
	}

	@ManyToOne
	@JoinColumn(name = "org_group_id")
	public OrgGroup getOrgGroup() {
		return orgGroup;
	}

	public void setOrgGroup(OrgGroup orgGroup) {
		this.orgGroup = orgGroup;
	}

	@ManyToOne
	@JoinColumn(name = "person_id")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Column(name = "start_date")
	public int getStartDate() {
		return startDate;
	}

	public void setStartDate(int startDate) {
		this.startDate = startDate;
	}

	/*
	 * alert_distribution smallint NOT NULL, start_date integer NOT NULL,
	 * last_date integer NOT NULL, alert_short character varying(2000),
	 * alert_long text, org_group_id character(16), person_id character(16),
	 * last_change_person_id character(16) NOT NULL, last_chage_datetime
	 */
	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "alert_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return alertId = IDGenerator.generate(this);
	}
}
