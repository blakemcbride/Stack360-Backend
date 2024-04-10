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
@Table(name = WageType.TABLE_NAME)
public class WageType extends Setup implements Comparable<WageType>, ArahantSaveNotify, Serializable {

	public final static String TABLE_NAME = "wage_type";
	public final static short PERIOD_HOURLY = 1;
	public final static short PERIOD_SALARY = 2;
	public final static short PERIOD_ONE_TIME = 3;
	public final static short TYPE_REGULAR = 1;
	public final static short TYPE_OVERTIME = 2;
	public final static short TYPE_COMMISSION = 3;
	public final static short TYPE_VACATION = 4;
	public final static short TYPE_SICK = 5;
	public final static short TYPE_BONUS = 6;
	public final static short TYPE_FIT = 101;
	public final static short TYPE_FICA = 102;
	public final static short TYPE_MEDICARE = 103;
	public final static short TYPE_SIT = 104;
	public static int TYPE_ADDITION = 999;
	public static int TYPE_DEDUCTION = 999;
	public static int TYPE_COMPANY_CONTRIBUTION = 999;
	public static int TYPE_TAX = 999;
	public static int TYPE_DIRECT_DEPOSIT = 999;
	public final static String ID = "wageTypeId";
	public final static String PERIOD_TYPE = "periodType";
	public final static String NAME = "wageName";
	public final static String IS_DEDUCTION = "isDeduction";
	public final static String WAGE_CAT = "wageType";
	public final static String EXPENSE_ACCOUNT = "expenseAccount";
	public final static String LIABILITY_ACCOUNT = "liabilityAccount";
	public final static String PAYROLL_CODE = "payrollInterfaceCode";
	private String wageTypeId;
	private String wageName;
	private short periodType;
	private short wageType;
	private GlAccount expenseAccount;
	private GlAccount liabilityAccount;
	private char isDeduction;
	private String payrollInterfaceCode;
	
	public WageType() {
	}

	@Column(name = "wage_code")
	public String getPayrollInterfaceCode() {
		return payrollInterfaceCode;
	}

	public void setPayrollInterfaceCode(String payrollInterfaceCode) {
		this.payrollInterfaceCode = payrollInterfaceCode;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "liability_account")
	public GlAccount getLiabilityAccount() {
		return liabilityAccount;
	}

	public void setLiabilityAccount(GlAccount liabilityAccount) {
		this.liabilityAccount = liabilityAccount;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "expense_account")
	public GlAccount getExpenseAccount() {
		return expenseAccount;
	}

	public void setExpenseAccount(GlAccount expenseAccount) {
		this.expenseAccount = expenseAccount;
	}

	@Column(name = "is_deduction")
	public char getIsDeduction() {
		return isDeduction;
	}

	public void setIsDeduction(char isDeduction) {
		this.isDeduction = isDeduction;
	}

	@Column(name = "period_type")
	public short getPeriodType() {
		return periodType;
	}

	public void setPeriodType(short periodType) {
		this.periodType = periodType;
	}

	@Column(name = "wage_name")
	public String getWageName() {
		return wageName;
	}

	public void setWageName(String wageName) {
		this.wageName = wageName;
	}

	@Column(name = "wage_type")
	public short getWageType() {
		return wageType;
	}

	public void setWageType(short wageType) {
		this.wageType = wageType;
	}

	@Column(name = "last_active_date")
	@Override
	public int getLastActiveDate() {
		return lastActiveDate;
	}

	@Column(name = "first_active_date")
	@Override
	public int getFirstActiveDate() {
		return firstActiveDate;
	}

	@ManyToOne
	@JoinColumn(name = "org_group_id")
	@Override
	public OrgGroup getOrgGroup() {
		return orgGroup;
	}

	@Id
	@Column(name = "wage_type_id")
	public String getWageTypeId() {
		return wageTypeId;
	}

	public void setWageTypeId(String wageTypeId) {
		this.wageTypeId = wageTypeId;
	}

	@Override
	public String tableName() {
		return "wage_type";
	}

	@Override
	public String keyColumn() {
		return "wage_type_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return wageTypeId = IDGenerator.generate(this);
	}

	@Override
	public int compareTo(WageType o) {
		if (wageName == null)
			return 1;
		if (o == null)
			return -1;
		if (o.wageName == null)
			return -1;
		return wageName.compareTo(o.wageName);
	}

	@Override
	public String notifyId() {
		return wageTypeId;
	}

	@Override
	public String notifyClassName() {
		return "WageType";
	}
}
