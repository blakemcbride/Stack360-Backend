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
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 */
@Entity
@Table (name=OvertimeApproval.TABLE_NAME)
public class OvertimeApproval extends ArahantBean implements Serializable {

	public static final String TABLE_NAME="overtime_approval";

	public static final String APPROVED_HOURS="overtimeHours";
	public static final String EMPLOYEE="employee";
	public static final String WORK_DATE="overtimeDate";
	public static final String ID="overtimeApprovalId";

	private String overtimeApprovalId;
	private Employee employee;
	private Employee supervisor;
	private float overtimeHours;
	private Date recordChangeDate;
	private int overtimeDate;

	@Column(name="overtime_date")
	public int getOvertimeDate() {
		return overtimeDate;
	}

	public void setOvertimeDate(int overtimeDate) {
		this.overtimeDate = overtimeDate;
	}



	@ManyToOne()
	@JoinColumn(name="employee_id")
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Id
	@Column(name="overtime_approval_id")
	public String getOvertimeApprovalId() {
		return overtimeApprovalId;
	}

	public void setOvertimeApprovalId(String overtimeApprovalId) {
		this.overtimeApprovalId = overtimeApprovalId;
	}

	@Column(name="overtime_hours")
	public float getOvertimeHours() {
		return overtimeHours;
	}

	public void setOvertimeHours(float overtimeHours) {
		this.overtimeHours = overtimeHours;
	}

	@Column(name="record_change_date")
	@Temporal(value=TemporalType.TIMESTAMP)
	public Date getRecordChangeDate() {
		return recordChangeDate;
	}

	public void setRecordChangeDate(Date recordChangeDate) {
		this.recordChangeDate = recordChangeDate;
	}

	@ManyToOne()
	@JoinColumn(name="supervisor_id")
	public Employee getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(Employee supervisor) {
		this.supervisor = supervisor;
	}


	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "overtime_approval_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return overtimeApprovalId=IDGenerator.generate(this);
	}

}
