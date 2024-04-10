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


/**
 *
 *
 */

package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name=SalesPoints.TABLE_NAME)
public class SalesPoints extends ArahantBean implements Serializable {

	public static final String TABLE_NAME = "sales_points";

	public static final String SALES_POINTS_ID = "salesPointsId";
	public static final String EMPLOYEE = "employee";
	public static final String POINT_DATE = "pointDate";
	public static final String PROSPECT = "prospect";
	public static final String PROSPECT_STATUS = "prospectStatus";

	private String salesPointsId;
	private Employee employee;
	private Date pointDate;
	private ProspectCompany prospect;
	private ProspectStatus prospestStatus;

	@Id
	@Column(name="sales_points_id")
	public String getSalesPointsId() {
		return salesPointsId;
	}

	public void setSalesPointsId(String salesPointsId) {
		this.salesPointsId = salesPointsId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="employee_id")
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Column(name="point_date")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getPointDate() {
		return pointDate;
	}

	public void setPointDate(Date pointDate) {
		this.pointDate = pointDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="prospect_id")
	public ProspectCompany getProspect() {
		return prospect;
	}

	public void setProspect(ProspectCompany prospect) {
		this.prospect = prospect;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="prospect_status_id")
	public ProspectStatus getProspestStatus() {
		return prospestStatus;
	}

	public void setProspestStatus(ProspectStatus prospestStatus) {
		this.prospestStatus = prospestStatus;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "sales_points_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return salesPointsId = IDGenerator.generate(this);
	}

}
