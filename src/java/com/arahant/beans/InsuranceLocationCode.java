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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 */
@Entity
@Table(name=InsuranceLocationCode.TABLE_NAME)
public class InsuranceLocationCode extends ArahantBean {

	public static final String TABLE_NAME="insurance_location_code";

	public static final String BENEFIT_CLASS="benefitClass";
	public static final String EMPLOYEE_STATUS="emplStatus";
	public static final String BENEFIT="benefit";

	
	private String insuranceLocationCodeId;

	private HrBenefit benefit;
	private HrEmployeeStatus emplStatus;
	private BenefitClass benefitClass;

	private String insLocCode;

	@ManyToOne
	@JoinColumn(name="benefit_id")
	public HrBenefit getBenefit() {
		return benefit;
	}

	public void setBenefit(HrBenefit benefit) {
		this.benefit = benefit;
	}

	@ManyToOne
	@JoinColumn(name="benefit_class_id")
	public BenefitClass getBenefitClass() {
		return benefitClass;
	}

	public void setBenefitClass(BenefitClass benefitClass) {
		this.benefitClass = benefitClass;
	}

	@ManyToOne
	@JoinColumn(name="employee_status_id")
	public HrEmployeeStatus getEmplStatus() {
		return emplStatus;
	}

	public void setEmplStatus(HrEmployeeStatus emplStatus) {
		this.emplStatus = emplStatus;
	}

	@Column(name="ins_location_code")
	public String getInsLocCode() {
		return insLocCode;
	}

	public void setInsLocCode(String insLocCode) {
		this.insLocCode = insLocCode;
	}

	@Id
	@Column(name="insurance_location_code_id")
	public String getInsuranceLocationCodeId() {
		return insuranceLocationCodeId;
	}

	public void setInsuranceLocationCodeId(String insuranceLocationCodeId) {
		this.insuranceLocationCodeId = insuranceLocationCodeId;
	}




	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "insurance_location_code_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return insuranceLocationCodeId=IDGenerator.generate(this);
	}

}
