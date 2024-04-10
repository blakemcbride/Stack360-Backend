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



package com.arahant.services.standard.hr.wageType;

import com.arahant.annotation.Validation;
import com.arahant.business.BWageType;
import com.arahant.services.TransmitInputBase;


public class SaveWageTypeInput extends TransmitInputBase {
	@Validation(table = "wage_type", column = "wage_name", required = true)
	private String name;
	@Validation(min = 1, max = 3, required = true)
	private int periodType;
	@Validation(min=1, required = true)
	private int typex;
	@Validation(required = false)
	private String expenseAccountId;
	@Validation(required = false)
	private String liabilityAccountId;
	@Validation(type = "date", required = false)
	private int firstActiveDate;
	@Validation(type = "date", required = false)
	private int lastActiveDate;
	@Validation(required = true)
	private boolean deduction;
	@Validation(min = 1, max = 16, required = true)
	private String id;
	@Validation(required = false, table = "wage_type", column = "wage_code")
	private String payrollInterfaceCode;

	void setData(BWageType bc) {
		bc.setName(name);
		bc.setPeriodType(periodType);
		bc.setType(typex);
		bc.setExpenseAccountId(expenseAccountId);
		bc.setLiabilityAccountId(liabilityAccountId);
		bc.setFirstActiveDate(firstActiveDate);
		bc.setLastActiveDate(lastActiveDate);
		bc.setIsDeduction(deduction);
		bc.setPayrollInterfaceCode(payrollInterfaceCode);
	}

	public String getPayrollInterfaceCode() {
		return payrollInterfaceCode;
	}

	public void setPayrollInterfaceCode(String payrollInterfaceCode) {
		this.payrollInterfaceCode = payrollInterfaceCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPeriodType() {
		return periodType;
	}

	public void setPeriodType(int periodType) {
		this.periodType = periodType;
	}

	public int getTypex() {
		return typex;
	}

	public void setTypex(int type) {
		this.typex = type;
	}

	public String getExpenseAccountId() {
		return expenseAccountId;
	}

	public void setExpenseAccountId(String expenseAccountId) {
		this.expenseAccountId = expenseAccountId;
	}

	public String getLiabilityAccountId() {
		return liabilityAccountId;
	}

	public void setLiabilityAccountId(String liabilityAccountId) {
		this.liabilityAccountId = liabilityAccountId;
	}

	public int getFirstActiveDate() {
		return firstActiveDate;
	}

	public void setFirstActiveDate(int firstActiveDate) {
		this.firstActiveDate = firstActiveDate;
	}

	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	public boolean getDeduction() {
		return deduction;
	}

	public void setDeduction(boolean deduction) {
		this.deduction = deduction;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
