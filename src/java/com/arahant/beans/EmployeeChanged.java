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
import javax.persistence.*;

@Entity
@Table(name = "person_changed")
public class EmployeeChanged extends ArahantBean implements Serializable {

	public static final String INTERFACEID = "interfaceId";
	public static final String EMPLOYEE = "employee";
	public static final String DATE = "earliestChangeDate";
	public static final String EMPLOYEE_ID = "employeeId";
	public static final short TYPE_COMPUPAY_INTERFACE = 0;
	public static final short TYPE_CONSOCIATES_EDI = 1;
	public static final short TYPE_EVOLUTION_INTERFACE = 2;
	public static final short TYPE_NOTIFY_LATE_BILLING = 3;
	public static final short TYPE_NOTIFY_LATE_BILLING_TIER_2 = 4;
	private EmployeeChangedId id;
	private Date earliestChangeDate;
	private short interfaceId;
	private Person employee;
	private String employeeId;

	@Column(name = "earliest_change_date")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getEarliestChangeDate() {
		return earliestChangeDate;
	}

	public void setEarliestChangeDate(Date earliestChangeDate) {
		this.earliestChangeDate = earliestChangeDate;
	}

	@Column(name = "person_id", insertable = false, updatable = false)
	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id", insertable = false, updatable = false)
	public Person getEmployee() {
		return employee;
	}

	public void setEmployee(Person employee) {
		if (id == null)
			id = new EmployeeChangedId();
		if (employee != null)
			id.setPersonId(employee.getPersonId());
		else
			id.setPersonId(null);
		this.employee = employee;
	}

	@Id
	public EmployeeChangedId getId() {
		return id;
	}

	public void setId(EmployeeChangedId id) {
		this.id = id;
	}

	@Column(name = "interface_id", insertable = false, updatable = false)
	public short getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(short interfaceId) {

		if (id == null)
			id = new EmployeeChangedId();
		id.setInterfaceId(interfaceId);

		this.interfaceId = interfaceId;
	}

	@Override
	public String tableName() {
		throw new UnsupportedOperationException("Not used like this");
	}

	@Override
	public String keyColumn() {
		throw new UnsupportedOperationException("Not used like this");
	}

	@Override
	public String generateId() throws ArahantException {
		throw new UnsupportedOperationException("Not used like this");
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final EmployeeChanged other = (EmployeeChanged) obj;
		if (this.id != other.id && (this.id == null || !this.id.equals(other.id)))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 67 * hash + (this.id != null ? this.id.hashCode() : 0);
		return hash;
	}
}
