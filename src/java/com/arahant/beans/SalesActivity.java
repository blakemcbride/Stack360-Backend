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
 */

package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name=SalesActivity.TABLE_NAME)
public class SalesActivity extends SetupWithEndDate implements java.io.Serializable {

	public static final String TABLE_NAME = "sales_activity";
	public static final String SEQNO = "seqno";
	public static final String ACTIVITY_CODE = "activityCode";
	public static final String DESCRIPTION = "description";
	public static final String SALES_POINTS = "salesPoints";
	public static final String ID = "salesActivityId";

	private String salesActivityId;
	private short seqno;
	private String activityCode;
	private String description;
	private short salesPoints;

	@Column(name="activity_code")
	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	@Column(name="description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Id
	@Column(name="sales_activity_id")
	public String getSalesActivityId() {
		return salesActivityId;
	}

	public void setSalesActivityId(String salesActivityId) {
		this.salesActivityId = salesActivityId;
	}

	@Column(name="sales_points")
	public short getSalesPoints() {
		return salesPoints;
	}

	public void setSalesPoints(short salesPoints) {
		this.salesPoints = salesPoints;
	}

	@Column(name="seqno")
	public short getSeqno() {
		return seqno;
	}

	public void setSeqno(short seqno) {
		this.seqno = seqno;
	}

	@Override
	@Column(name="last_active_date")
	public int getLastActiveDate() {
		return lastActiveDate;
	}

	@Override
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="company_id")
	public OrgGroup getOrgGroup() {
		return orgGroup;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "sales_activity_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return salesActivityId = IDGenerator.generate(this);
	}

}
