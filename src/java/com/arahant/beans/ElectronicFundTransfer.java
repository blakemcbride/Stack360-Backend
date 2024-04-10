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
@Table(name="electronic_fund_transfer")
public class ElectronicFundTransfer extends ArahantBean implements  ArahantSaveNotify, Serializable {
	public static final String PERSON="person";
	public static final String SEQNO="seqno";
	public static final String ID="eftId";
	public static final String BANK_ACCOUNT="bankAccount";
	public static final String AMOUNT_TYPE="amountType";
	public static final String AMOUNT="amount";
	public static final String WAGE_TYPE="wageType";

	
	public static final char TYPE_PERCENTAGE='P';
	public static final char TYPE_FIXED='F';

	private String eftId;
	private Person person;
	private short seqno;
	private String bankRoute;
	private String bankAccount;
	private char accountType;
	private char amountType;
	private float amount;
	private String personId;
	private WageType wageType;

	@ManyToOne
	@JoinColumn(name="wage_type_id")
	public WageType getWageType() {
		return wageType;
	}

	public void setWageType(WageType wageType) {
		this.wageType = wageType;
	}

			
	@Override
	public String tableName() {
		return "electronic_fund_transfer";
	}

	@Override
	public String keyColumn() {
		return "eft_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return eftId=IDGenerator.generate(this);
	}

	@Column (name="account_type")
	public char getAccountType() {
		return accountType;
	}

	public void setAccountType(char accountType) {
		this.accountType = accountType;
	}

	@Column (name="amount")
	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	@Column (name="amount_type")
	public char getAmountType() {
		return amountType;
	}

	public void setAmountType(char amountType) {
		this.amountType = amountType;
	}

	@Column (name="bank_account")
	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	@Column (name="bank_route")
	public String getBankRoute() {
		return bankRoute;
	}

	public void setBankRoute(String bankRoute) {
		this.bankRoute = bankRoute;
	}

	@Id
	@Column (name="eft_id")
	public String getEftId() {
		return eftId;
	}

	public void setEftId(String eftId) {
		firePropertyChange("eftId", this.eftId, eftId);
		this.eftId = eftId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="person_id")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Column (name="seqno")
	public short getSeqno() {
		return seqno;
	}

	public void setSeqno(short seqno) {
		this.seqno = seqno;
	}

	@Column (name="person_id",insertable=false,updatable=false)
	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		firePropertyChange("personId", this.personId, personId);
		this.personId = personId;
	}

	public String notifyId() {
		return eftId;
	}

	public String notifyClassName() {
		return "ElectronicFundTransfer";
	}

	
	
}
