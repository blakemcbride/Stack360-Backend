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
import java.util.Date;
import javax.persistence.*;



@Entity
@Table(name="edi_transaction")
public class EDITransaction extends ArahantBean implements Serializable
{
	public static String STATUS="transactionStatus";
	private static final long serialVersionUID = 1L;
	
	private String ediTransactionId;
	private CompanyBase company;
	private int tscn = 1;//  Transaction set control number
	private Date transactionDatetime;
	private int transactionStatus; 
	private String transactionStatusDesc;
	private int gcn = 1;// Group control number
	private int icn = 1;//  Interchange control number
			 
	public static final String TRANSACTION_DATE="transactionDatetime";
	public static final String COMPANY="company";
	
	public EDITransaction() { }
	
	
	@Override
	public String tableName() {
		return "edi_transaction";
	}

	@Override
	public String keyColumn() {
		return "edi_transaction_id";
	}

	@Override
	public String generateId() throws ArahantException {
		ediTransactionId=IDGenerator.generate(this);
		return ediTransactionId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="org_group_id")
	public CompanyBase getCompany() {
		return company;
	}

	public void setCompany(CompanyBase company) {
		this.company = company;
	}

	@Id
	@Column (name="edi_transaction_id")
	public String getEdiTransactionId() {
		return ediTransactionId;
	}

	public void setEdiTransactionId(String ediTransactionId) {
		this.ediTransactionId = ediTransactionId;
	}

	@Column (name="gcn")
	public int getGcn() {
		return gcn;
	}

	public void setGcn(int gcn) {
		this.gcn = gcn;
	}

	@Column (name="icn")
	public int getIcn() {
		return icn;
	}

	public void setIcn(int icn) {
		this.icn = icn;
	}

	@Column (name="transaction_datetime")
	@Temporal (TemporalType.TIMESTAMP)
	public Date getTransactionDatetime() {
		return transactionDatetime;
	}

	public void setTransactionDatetime(Date transactionDatetime) {
		this.transactionDatetime = transactionDatetime;
	}

	@Column (name="transaction_status")
	public int getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(int transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	@Column (name="transaction_status_desc")
	public String getTransactionStatusDesc() {
		return transactionStatusDesc;
	}

	public void setTransactionStatusDesc(String transactionStatusDesc) {
		this.transactionStatusDesc = transactionStatusDesc;
	}

	@Column (name="tscn")
	public int getTscn() {
		return tscn;
	}

	public void setTscn(int tscn) {
		this.tscn = tscn;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final EDITransaction other = (EDITransaction) obj;
		if (this.ediTransactionId != other.getEdiTransactionId() && (this.ediTransactionId == null || !this.ediTransactionId.equals(other.getEdiTransactionId()))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 97 * hash + (this.ediTransactionId != null ? this.ediTransactionId.hashCode() : 0);
		return hash;
	}
	
}
