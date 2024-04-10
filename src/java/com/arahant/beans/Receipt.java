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

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
@Entity
@Table(name = "receipt")
public class Receipt extends ArahantBean implements Serializable {

    public static final String DATE = "receiptDate";
    public static final String PERSON = "person";
    public static final String AMOUNT = "amount";
    public static final String TYPE = "receiptType";
    public static final char TYPE_ADJUSTMENT = 'A';
    public static final char TYPE_BANK_DEPOSIT = 'D';
    public static final char TYPE_CHECK = 'C';
    public static final String RECEIPT_JOIN = "receiptJoins";
    public static final String RECEIPT_ID = "receiptId";
    public static final String BANK_DRAFT_HISTORY = "bankDraftHistory";
    private String receiptId;
    private int receiptDate;
    private char receiptType;
    private String reference;
    private double amount;
    private char source = 'C';
    private CompanyBase company;
    private Person person;
    private Set<ReceiptJoin> receiptJoins = new HashSet<ReceiptJoin>(0);
    private BankDraftHistory bankDraftHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_draft_history_id")
    public BankDraftHistory getBankDraftHistory() {
        return bankDraftHistory;
    }

    public void setBankDraftHistory(BankDraftHistory bankDraftHistory) {
        this.bankDraftHistory = bankDraftHistory;
    }

    @Column(name = "amount")
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    public CompanyBase getCompany() {
        return company;
    }

    public void setCompany(CompanyBase company) {
        this.company = company;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Column(name = "receipt_date")
    public int getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(int receiptDate) {
        this.receiptDate = receiptDate;
    }

    @Id
    @Column(name = "receipt_id")
    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    @Column(name = "receipt_type")
    public char getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(char receiptType) {
        this.receiptType = receiptType;
    }

    @Column(name = "reference")
    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Column(name = "source")
    public char getSource() {
        return source;
    }

    public void setSource(char source) {
        this.source = source;
    }

    @OneToMany(mappedBy = ReceiptJoin.RECEIPT, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<ReceiptJoin> getReceiptJoins() {
        return receiptJoins;
    }

    public void setReceiptJoins(Set<ReceiptJoin> receiptJoins) {
        this.receiptJoins = receiptJoins;
    }

    @Override
    public String tableName() {
        return "receipt";
    }

    @Override
    public String keyColumn() {
        return "receipt_id";
    }

    @Override
    public String generateId() throws ArahantException {
        return receiptId = IDGenerator.generate(this);
    }
}
