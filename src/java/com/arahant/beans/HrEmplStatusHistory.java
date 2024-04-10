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
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = HrEmplStatusHistory.TABLE_NAME)
public class HrEmplStatusHistory extends ArahantBean implements java.io.Serializable, ArahantSaveNotify {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "hr_empl_status_history";
	// Fields    
	private String statusHistId;
	public static final String STATUSHISTID = "statusHistId";
	private Person employee;
	public static final String EMPLOYEE = "employee";
	public static final String EMPLOYEE_ID = "employeeId";
	private HrEmployeeStatus hrEmployeeStatus;
	public static final String HREMPLOYEESTATUS = "hrEmployeeStatus";
	private int effectiveDate;
	public static final String EFFECTIVEDATE = "effectiveDate";
	private String notes;
	public static final String NOTES = "notes";
	public static final String STATUS_ID = "statusId";
	private String employeeId;
	private String statusId;
	public static final String RECORD_CHANGE_DATE = "recordChangeDate";
	private Date recordChangeDate = new java.util.Date();

	// Constructors
	/**
	 * default constructor
	 */
	public HrEmplStatusHistory() {
	}

	/**
	 * @return Returns the statusId.
	 */
	@Column(name = "status_id", insertable = false, updatable = false)
	public String getStatusId() {
		return statusId;
	}

	/**
	 * @param statusId The statusId to set.
	 */
	public void setStatusId(String statusId) {
		firePropertyChange("statusId", this.statusId, statusId);
		this.statusId = statusId;
	}

	/**
	 * @return Returns the employeeId.
	 */
	@Column(name = "employee_id", insertable = false, updatable = false)
	public String getEmployeeId() {
		return employeeId;
	}

	/**
	 * @param employeeId The employeeId to set.
	 */
	public void setEmployeeId(String employeeId) {
		firePropertyChange("employeeId", this.employeeId, employeeId);
		this.employeeId = employeeId;
	}

	// Property accessors
	@Id
	@Column(name = "status_hist_id")
	public String getStatusHistId() {
		return this.statusHistId;
	}

	public void setStatusHistId(final String statusHistId) {
		this.statusHistId = statusHistId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id")
	public Person getEmployee() {
		return this.employee;
	}

	public void setEmployee(final Person employee) {
		if (employee != null)
			setEmployeeId(employee.getPersonId());
		this.employee = employee;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	public HrEmployeeStatus getHrEmployeeStatus() {
		return this.hrEmployeeStatus;
	}

	public void setHrEmployeeStatus(final HrEmployeeStatus hrEmployeeStatus) {
		this.hrEmployeeStatus = hrEmployeeStatus;
	}

	@Column(name = "effective_date")
	public int getEffectiveDate() {
		return this.effectiveDate;
	}

	public void setEffectiveDate(final int effectiveDate) {
		firePropertyChange("effectiveDate", this.effectiveDate, effectiveDate);
		this.effectiveDate = effectiveDate;
	}

	@Column(name = "notes")
	public String getNotes() {
		return this.notes;
	}

	public void setNotes(final String notes) {
		firePropertyChange("notes", this.notes, notes);
		this.notes = notes;
	}

	@Column(name = "record_change_date")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getRecordChangeDate() {
		return recordChangeDate;
	}

	public void setRecordChangeDate(Date recordChangeDate) {
		this.recordChangeDate = recordChangeDate;
	}

	/* (non-Javadoc)
	 * @see com.arahant.beans.ArahantBean#keyColumn()
	 */
	@Override
	public String keyColumn() {
		return "status_hist_id";
	}

	/* (non-Javadoc)
	 * @see com.arahant.beans.ArahantBean#tableName()
	 */
	@Override
	public String tableName() {
		return TABLE_NAME;
	}


	/* (non-Javadoc)
	 * @see com.arahant.beans.ArahantBean#generateId()
	 */
	@Override
	public String generateId() throws ArahantException {
		setStatusHistId(IDGenerator.generate(this));
		return statusHistId;
	}

	@Override
	public boolean equals(Object o) {
		if (statusHistId == null && o == null)
			return true;
		if (statusHistId != null && o instanceof HrEmplStatusHistory)
			return statusHistId.equals(((HrEmplStatusHistory) o).getStatusHistId());

		return false;
	}

	@Override
	public int hashCode() {
		if (statusHistId == null)
			return 0;
		return statusHistId.hashCode();
	}

	@Override
	public String notifyId() {
		return statusHistId;
	}

	@Override
	public String notifyClassName() {
		return "HrEmplStatusHistory";
	}
}
