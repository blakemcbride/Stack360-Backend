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
@Table(name = "bank_account")
public class BankAccount extends ArahantBean implements Serializable {

	public static final String BANK_NAME = "bankName";
	public static final String LAST_ACTIVE_DATE = "lastActiveDate";
	public static final String BANK_ACCOUNT_ID = "bankAccountId";
	public static final String ORG_GROUP = "orgGroup";
	private String bankAccountId;
	private OrgGroup orgGroup;
	private String bankId;
	private String bankName;
	private String bankRoute;
	private String bankAccount;
	private char accountType = 'C';
	private int lastActiveDate;

	@Override
	public String tableName() {
		return "bank_account";
	}

	@Override
	public String keyColumn() {
		return "bank_account_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return bankAccountId = IDGenerator.generate(this);
	}

	@Column(name = "account_type")
	public char getAccountType() {
		return accountType;
	}

	public void setAccountType(char accountType) {
		this.accountType = accountType;
	}

	@Column(name = "bank_account")
	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	@Id
	@Column(name = "bank_account_id")
	public String getBankAccountId() {
		return bankAccountId;
	}

	public void setBankAccountId(String bankAccountId) {
		this.bankAccountId = bankAccountId;
	}

	@Column(name = "bank_id")
	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	@Column(name = "bank_name")
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	@Column(name = "bank_route")
	public String getBankRoute() {
		return bankRoute;
	}

	public void setBankRoute(String bankRoute) {
		this.bankRoute = bankRoute;
	}

	@Column(name = "last_active_date")
	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_group_id")
	public OrgGroup getOrgGroup() {
		return orgGroup;
	}

	public void setOrgGroup(OrgGroup orgGroup) {
		this.orgGroup = orgGroup;
	}
}
