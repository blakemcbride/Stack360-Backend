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
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Fetch;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;

/**
 *
 */
@Entity
@Table(name="hr_billing_status_history")
public class HrBillingStatusHistory extends ArahantBean implements Serializable {

	public static final String DATE="startDate";
	public static final String FINAL_DATE="finalDate";
	public static final String PERSON="person";
	public static final String BILLING_STATUS="billingStatus";
	public static final String ID="billingStatusHistoryId";
	
	private String billingStatusHistoryId;
	private Person person;
	private HrBillingStatus billingStatus;
	private int startDate;
	private int finalDate;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="billing_status_id")
	public HrBillingStatus getBillingStatus() {
		return billingStatus;
	}

	public void setBillingStatus(HrBillingStatus billingStatus) {
		this.billingStatus = billingStatus;
	}

	@Id
	@Column (name="billing_status_hist_id")
	public String getBillingStatusHistoryId() {
		return billingStatusHistoryId;
	}

	public void setBillingStatusHistoryId(String billingStatusHistoryId) {
		this.billingStatusHistoryId = billingStatusHistoryId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="person_id")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Column (name="start_date")
	public int getStartDate() {
		return startDate;
	}

	public void setStartDate(int startDate) {
		this.startDate = startDate;
	}

	@Column (name="final_date")
	public int getFinalDate() {
		return finalDate;
	}

	public void setFinalDate(int finalDate) {
		this.finalDate = finalDate;
	}
	
	
	@Override
	public String tableName() {
		return "hr_billing_status_history";
	}

	@Override
	public String keyColumn() {
		return "billing_status_hist_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return billingStatusHistoryId=IDGenerator.generate(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final HrBillingStatusHistory other = (HrBillingStatusHistory) obj;
		if (this.billingStatusHistoryId != other.getBillingStatusHistoryId() && (this.billingStatusHistoryId == null || !this.billingStatusHistoryId.equals(other.getBillingStatusHistoryId()))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 37 * hash + (this.billingStatusHistoryId != null ? this.billingStatusHistoryId.hashCode() : 0);
		return hash;
	}

	
	
}
