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
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 *
 */
@Entity
@Table(name = "appointment_location")
public class AppointmentLocation extends ArahantBean implements Serializable {

	public static final String CODE = "code";
	public static final String DESCRIPTION = "description";
	private String locationId;
	private String code;
	private String description;
	private Set<Appointment> appointments = new HashSet<Appointment>();
	public static String COMPANYID = "companyId";
	private String companyId;
	private CompanyDetail company;
	public static final String COMPANY = "company";
	public static final String LAST_ACTIVE_DATE = "lastActiveDate";

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	public CompanyDetail getCompany() {
		return company;
	}

	public void setCompany(CompanyDetail company) {
		this.company = company;
	}

	@Column(name = "company_id", insertable = false, updatable = false)
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	private int lastActiveDate = 0;

	@Column(name = "last_active_date")
	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	@Override
	public String tableName() {
		return "appointment_location";
	}

	@Override
	public String keyColumn() {
		return "location_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return locationId = IDGenerator.generate(this);
	}

	@Column(name = "code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Id
	@Column(name = "location_id")
	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	@OneToMany(mappedBy = Appointment.LOCATION, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(Set<Appointment> appointments) {
		this.appointments = appointments;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AppointmentLocation other = (AppointmentLocation) obj;
		if (this.locationId != other.getLocationId() && (this.locationId == null || !this.locationId.equals(other.getLocationId())))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 11 * hash + (this.locationId != null ? this.locationId.hashCode() : 0);
		return hash;
	}
}
