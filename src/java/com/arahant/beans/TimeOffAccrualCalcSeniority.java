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
import javax.persistence.*;

/**
 *
 */
@Entity
@Table(name = TimeOffAccrualCalcSeniority.TABLE_NAME)
public class TimeOffAccrualCalcSeniority extends ArahantBean implements Serializable {

	public static final String TABLE_NAME = "time_off_accrual_seniority";
	public static final String ID = "timeOffAccrualCalcSeniorityId";
	public static final String CALC = "timeOffCalc";
	public static final String YEARS_OF_SERVICE = "yearsOfService";
	private TimeOffAccrualCalc timeOffCalc;
	private short yearsOfService;
	private float hoursAccrued;  //this is either the annual amount accrued, or hours accrued per hour worked
	private String timeOffAccrualCalcSeniorityId;

	public TimeOffAccrualCalcSeniority() {
	}

	@Column(name = "hours_accrued")
	public float getHoursAccrued() {
		return hoursAccrued;
	}

	public void setHoursAccrued(float hoursAccrued) {
		this.hoursAccrued = hoursAccrued;
	}

	@Id
	@Column(name = "accrual_seniority_id")
	public String getTimeOffAccrualCalcSeniorityId() {
		return timeOffAccrualCalcSeniorityId;
	}

	public void setTimeOffAccrualCalcSeniorityId(String timeOffAccrualCalcSeniorityId) {
		this.timeOffAccrualCalcSeniorityId = timeOffAccrualCalcSeniorityId;
	}

	@ManyToOne
	@JoinColumn(name = "time_off_accrual_calc_id")
	public TimeOffAccrualCalc getTimeOffCalc() {
		return timeOffCalc;
	}

	public void setTimeOffCalc(TimeOffAccrualCalc timeOffCalc) {
		this.timeOffCalc = timeOffCalc;
	}

	@Column(name = "years_of_service")
	public short getYearsOfService() {
		return yearsOfService;
	}

	public void setYearsOfService(short yearsOfService) {
		this.yearsOfService = yearsOfService;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "accrual_seniority_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return timeOffAccrualCalcSeniorityId = IDGenerator.generate(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TimeOffAccrualCalcSeniority other = (TimeOffAccrualCalcSeniority) obj;
		if ((this.timeOffAccrualCalcSeniorityId == null) ? (other.timeOffAccrualCalcSeniorityId != null) : !this.timeOffAccrualCalcSeniorityId.equals(other.timeOffAccrualCalcSeniorityId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 17 * hash + (this.timeOffAccrualCalcSeniorityId != null ? this.timeOffAccrualCalcSeniorityId.hashCode() : 0);
		return hash;
	}
}
