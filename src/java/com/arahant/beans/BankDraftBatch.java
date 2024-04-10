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
@Table(name = "bank_draft_batch")
public class BankDraftBatch extends ArahantBean implements Serializable {

	public static final String NAME = "name";
	public static final String PERSON = "persons";
	public static final String BATCH_ID = "bankDraftId";
	private String bankDraftId;
	private String name;
	private Set<BankDraftHistory> histories = new HashSet<BankDraftHistory>(0);
	private Set<Person> persons = new HashSet<Person>(0);
	public static final String COMPANY = "company";
	private CompanyDetail company;
	public static final String COMPANY_ID = "companyId";
	private String companyId;

	@ManyToMany
	@JoinTable(name = "bank_draft_detail",
	joinColumns = {
		@JoinColumn(name = "bank_draft_id")},
	inverseJoinColumns = {
		@JoinColumn(name = "person_id")})
	public Set<Person> getPersons() {
		return persons;
	}

	public void setPersons(Set<Person> persons) {
		this.persons = persons;
	}

	@OneToMany(mappedBy = BankDraftHistory.BATCH, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<BankDraftHistory> getHistories() {
		return histories;
	}

	public void setHistories(Set<BankDraftHistory> histories) {
		this.histories = histories;
	}

	@Id
	@Column(name = "bank_draft_id")
	public String getBankDraftId() {
		return bankDraftId;
	}

	public void setBankDraftId(String bankDraftId) {
		this.bankDraftId = bankDraftId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	public CompanyDetail getCompany() {
		return company;
	}

	public void setCompany(CompanyDetail company) {
		this.company = company;
	}

	@Column(name = "company_id", updatable = false, insertable = false)
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String tableName() {
		return "bank_draft_batch";
	}

	@Override
	public String keyColumn() {
		return "bank_draft_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return bankDraftId = IDGenerator.generate(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final BankDraftBatch other = (BankDraftBatch) obj;
		if (this.bankDraftId != other.getBankDraftId() && (this.bankDraftId == null || !this.bankDraftId.equals(other.getBankDraftId())))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 79 * hash + (this.bankDraftId != null ? this.bankDraftId.hashCode() : 0);
		return hash;
	}
}
