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
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "bank_draft_history")
public class BankDraftHistory extends ArahantBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String DATE = "dateMade";
	public static final String BATCH = "batch";
	private String bankDraftHistoryId;
	private int dateMade;
	private String receipt;
	private BankDraftBatch batch;
	private Set<Receipt> receipts = new HashSet<Receipt>(0);

	public BankDraftHistory() {
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bank_draft_id")
	public BankDraftBatch getBatch() {
		return batch;
	}

	public void setBatch(BankDraftBatch batch) {
		this.batch = batch;
	}

	@Id
	@Column(name = "bank_draft_history_id")
	public String getBankDraftHistoryId() {
		return bankDraftHistoryId;
	}

	public void setBankDraftHistoryId(String bankDraftHistoryId) {
		this.bankDraftHistoryId = bankDraftHistoryId;
	}

	@Column(name = "date_made")
	public int getDateMade() {
		return dateMade;
	}

	public void setDateMade(int dateMade) {
		this.dateMade = dateMade;
	}

	@Column(name = "receipt")
	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	@OneToMany(mappedBy = Receipt.BANK_DRAFT_HISTORY, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<Receipt> getReceipts() {
		return receipts;
	}

	public void setReceipts(Set<Receipt> receipts) {
		this.receipts = receipts;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (bankDraftHistoryId != null ? bankDraftHistoryId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof BankDraftHistory))
			return false;
		BankDraftHistory other = (BankDraftHistory) object;
		if ((this.bankDraftHistoryId == null && other.getBankDraftHistoryId() != null) || (this.bankDraftHistoryId != null && !this.bankDraftHistoryId.equals(other.getBankDraftHistoryId())))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "com.arahant.beans.BankDraftHistory[bankDraftHistoryId=" + bankDraftHistoryId + "]";
	}

	@Override
	public String tableName() {
		return "bank_draft_history";
	}

	@Override
	public String keyColumn() {
		return "bank_draft_history_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return bankDraftHistoryId = IDGenerator.generate(this);
	}
}
