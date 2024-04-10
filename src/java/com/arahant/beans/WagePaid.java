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

@Entity
@Table(name = "wage_paid")
public class WagePaid extends ArahantBean implements Serializable {

	public static final short METHOD_CHECK = 1;
	public static final short METHOD_DEPOSIT = 2;
	public static final short METHOD_CASH = 3;
	public static final String EMPLOYEE = "employee";
	public static final String PAY_DATE = "datePaid";
	public static final String CHECK_NUMBER = "checkNumber";
	private String wagePaidId;
	private Employee employee;
	private int beginPeriod;
	private int endPeriod;
	private int datePaid;
	private short paymentMethod;
	private int checkNumber;

	public WagePaid() {
	}

	@Column(name = "beg_period")
	public int getBeginPeriod() {
		return beginPeriod;
	}

	public void setBeginPeriod(int beginPeriod) {
		this.beginPeriod = beginPeriod;
	}

	@Column(name = "check_number")
	public int getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(int checkNumber) {
		this.checkNumber = checkNumber;
	}

	@Column(name = "date_paid")
	public int getDatePaid() {
		return datePaid;
	}

	public void setDatePaid(int datePaid) {
		this.datePaid = datePaid;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id")
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Column(name = "end_period")
	public int getEndPeriod() {
		return endPeriod;
	}

	public void setEndPeriod(int endPeriod) {
		this.endPeriod = endPeriod;
	}

	@Column(name = "payment_method")
	public short getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(short paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	@Id
	@Column(name = "wage_paid_id")
	public String getWagePaidId() {
		return wagePaidId;
	}

	public void setWagePaidId(String wagePaidId) {
		this.wagePaidId = wagePaidId;
	}

	@Override
	public String tableName() {
		return "wage_paid";
	}

	@Override
	public String keyColumn() {
		return "wage_paid_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return wagePaidId = IDGenerator.generate(this);
	}
}
