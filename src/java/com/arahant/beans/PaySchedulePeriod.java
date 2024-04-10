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
import javax.persistence.*;
import org.hibernate.annotations.Fetch;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;

/**
 *
 */
@Entity
@Table(name="pay_schedule_period")
public class PaySchedulePeriod extends ArahantBean {
	public static final String PAY_SCHEDULE="paySchedule";
	public static final String PERIOD_END="lastDate";
	public static final String PAY_DATE="payDate";
	private String payPeriodId;
	private PaySchedule paySchedule;
	private int lastDate;
	private int payDate;
	private char beginningOfYear='N';

	@Column (name="last_date")
	public int getLastDate() {
		return lastDate;
	}

	public void setLastDate(int lastDate) {
		this.lastDate = lastDate;
	}

	@Column (name="pay_date")
	public int getPayDate() {
		return payDate;
	}

	public void setPayDate(int payDate) {
		this.payDate = payDate;
	}

	@Id
	@Column (name="pay_period_id")
	public String getPayPeriodId() {
		return payPeriodId;
	}

	public void setPayPeriodId(String payPeriodId) {
		this.payPeriodId = payPeriodId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="pay_schedule_id")
	public PaySchedule getPaySchedule() {
		return paySchedule;
	}

	public void setPaySchedule(PaySchedule paySchedule) {
		this.paySchedule = paySchedule;
	}

	@Column (name="beginning_of_year")
	public char getBeginningOfYear() {
		return beginningOfYear;
	}

	public void setBeginningOfYear(char beginningOfYear) {
		this.beginningOfYear = beginningOfYear;
	}

	@Override
	public String tableName() {
		return "pay_schedule_period";
	}

	@Override
	public String keyColumn() {
		return "pay_period_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return payPeriodId=IDGenerator.generate(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PaySchedulePeriod other = (PaySchedulePeriod) obj;
		if (this.payPeriodId != other.payPeriodId && (this.payPeriodId == null || !this.payPeriodId.equals(other.payPeriodId))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 11 * hash + (this.payPeriodId != null ? this.payPeriodId.hashCode() : 0);
		return hash;
	}
	
	
}
