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

import javax.persistence.*;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;


@Entity
@Table(name=HrTrainingDetail.TABLE_NAME)
public class HrTrainingDetail extends ArahantBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "hr_training_detail";

	// Fields    

	private String trainingId;

	public static final String TRAININGID = "trainingId";

	private Employee employee;

	public static final String EMPLOYEE = "employee";

	private HrTrainingCategory hrTrainingCategory;

	public static final String HRTRAININGCATEGORY = "hrTrainingCategory";

	private int trainingDate;

	public static final String TRAININGDATE = "trainingDate";

	private int expireDate;

	public static final String EXPIREDATE = "expireDate";

	private float trainingHours;

	public static final String TRAININGHOURS = "trainingHours";

	// Constructors

	/**
	 * default constructor
	 */
	public HrTrainingDetail() {
	}

	/**
	 * full constructor
	 */
	public HrTrainingDetail(final String trainingId, final Employee employee,
							final HrTrainingCategory hrTrainingCategory, final int trainingDate,
							final int expireDate, final short trainingHours) {
		this.trainingId = trainingId;
		this.employee = employee;
		this.hrTrainingCategory = hrTrainingCategory;
		this.trainingDate = trainingDate;
		this.expireDate = expireDate;
		this.trainingHours = trainingHours;
	}

	// Property accessors
	@Id
	@Column(name = "training_id")
	public String getTrainingId() {
		return this.trainingId;
	}

	public void setTrainingId(final String trainingId) {
		this.trainingId = trainingId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id")
	public Employee getEmployee() {
		return this.employee;
	}

	public void setEmployee(final Employee employee) {
		this.employee = employee;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cat_id")
	public HrTrainingCategory getHrTrainingCategory() {
		return this.hrTrainingCategory;
	}

	public void setHrTrainingCategory(final HrTrainingCategory hrTrainingCategory) {
		this.hrTrainingCategory = hrTrainingCategory;
	}

	@Column(name = "training_date")
	public int getTrainingDate() {
		return this.trainingDate;
	}

	public void setTrainingDate(final int trainingDate) {
		this.trainingDate = trainingDate;
	}

	@Column(name = "expire_date")
	public int getExpireDate() {
		return this.expireDate;
	}

	public void setExpireDate(final int expireDate) {
		this.expireDate = expireDate;
	}

	@Column(name = "training_hours")
	public float getTrainingHours() {
		return this.trainingHours;
	}

	public void setTrainingHours(final float trainingHours) {
		this.trainingHours = trainingHours;
	}

	/* (non-Javadoc)
	 * @see com.arahant.beans.ArahantBean#keyColumn()
	 */
	public String keyColumn() {
		return "training_id";
	}

	/* (non-Javadoc)
	 * @see com.arahant.beans.ArahantBean#tableName()
	 */
	public String tableName() {
		return TABLE_NAME;
	}


	/* (non-Javadoc)
	 * @see com.arahant.beans.ArahantBean#generateId()
	 */
	public String generateId() throws ArahantException {
		setTrainingId(IDGenerator.generate(this));
		return trainingId;
	}


	public boolean equals(Object o) {
		if (trainingId == null && o == null)
			return true;
		if (trainingId != null && o instanceof HrTrainingDetail)
			return trainingId.equals(((HrTrainingDetail) o).getTrainingId());
		return false;
	}

	public int hashCode() {
		if (trainingId == null)
			return 0;
		return trainingId.hashCode();
	}
}
