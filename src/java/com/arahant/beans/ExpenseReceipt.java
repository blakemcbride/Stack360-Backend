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
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.FetchType.*;

/**
 * Author: Blake McBride
 * Date: 4/6/18
 */
@Entity
@Table(name = ExpenseReceipt.TABLE_NAME)
public class ExpenseReceipt extends ArahantBean {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "expense_receipt";

    private String expenseReceiptId;
    public static final String EXPENSE_RECEIPT_ID = "expenseReceiptId";
    private String personId;
    public static final String PERSON_ID = "personId";
    private Person person;
    public static final String PERSON = "person";
    private String projectId;
    public static final String PROJECT_ID = "projectId";
    private Project project;
    public static final String PROJECT = "project";
    private String expenseAccountId;
    public static final String EXPENSE_ACCOUNT_ID = "expenseAccountId";
    private ExpenseAccount expenseAccount;
    public static final String EXPENSE_ACCOUNT = "expenseAccount";
    private int receiptDate;
    public static final String RECEIPT_DATE = "receiptDate";
    private Date whenUploaded;
    public final static String WHEN_UPLOADED = "whenUploaded";
    private String businessPurpose;
    private double amount;
    private String whoUploadedId;
    private Person whoUploaded;
    private String fileType;
    public static final String PAYMENT_METHOD = "paymentMethod";
    private char paymentMethod;
    private char approved = 'N';
    private String whoApprovedId;
    private Person whoApproved;
    private Date whenApproved;

    public ExpenseReceipt () {}

    @Id
    @Column(name = "expense_receipt_id")
    public String getExpenseReceiptId() {
        return expenseReceiptId;
    }

    public void setExpenseReceiptId(String expenseReceiptId) {
        this.expenseReceiptId = expenseReceiptId;
    }

    @Column(name = "person_id", insertable = false, updatable = false)
    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "person_id")
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Column(name = "project_id", insertable = false, updatable = false)
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "project_id")
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Column(name = "expense_account_id", insertable = false, updatable = false)
    public String getExpenseAccountId() {
        return expenseAccountId;
    }

    public void setExpenseAccountId(String expenseAccountId) {
        this.expenseAccountId = expenseAccountId;
    }

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "expense_account_id")
    public ExpenseAccount getExpenseAccount() {
        return expenseAccount;
    }

    public void setExpenseAccount(ExpenseAccount ea) {
        expenseAccount = ea;
    }

    @Column(name = "receipt_date")
    public int getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(int receiptDate) {
        this.receiptDate = receiptDate;
    }

    @Column(name = "when_uploaded")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getWhenUploaded() {
        return whenUploaded;
    }

    public void setWhenUploaded(Date whenUploaded) {
        this.whenUploaded = whenUploaded;
    }

    @Column(name = "business_purpose")
    public String getBusinessPurpose() {
        return businessPurpose;
    }

    public void setBusinessPurpose(String businessPurpose) {
        this.businessPurpose = businessPurpose;
    }

    @Column(name = "amount")
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Column(name = "who_uploaded", insertable = false, updatable = false)
    public String getWhoUploadedId() {
        return whoUploadedId;
    }

    public void setWhoUploadedId(String whoUploaded) {
        this.whoUploadedId = whoUploaded;
    }

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "who_uploaded")
    public Person getWhoUploaded() {
        return whoUploaded;
    }

    public void setWhoUploaded(Person whoUploaded) {
        this.whoUploaded = whoUploaded;
    }

    @Column(name = "file_type")
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Column(name = "payment_method")
    public char getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(char paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Column(name = "approved")
    public char getApproved() {
        return approved;
    }

    public void setApproved(char approved) {
        this.approved = approved;
    }

    @Column(name = "who_approved", insertable = false, updatable = false)
    public String getWhoApprovedId() {
        return whoApprovedId;
    }

    public void setWhoApprovedId(String whoApproved) {
        this.whoApprovedId = whoApproved;
    }

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "who_approved")
    public Person getWhoApproved() {
        return whoApproved;
    }

    public void setWhoApproved(Person whoApproved) {
        this.whoApproved = whoApproved;
    }

    @Column(name = "when_approved")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getWhenApproved() {
        return whenApproved;
    }

    public void setWhenApproved(Date whenApproved) {
        this.whenApproved = whenApproved;
    }

    @Override
    public String tableName() {
        return TABLE_NAME;
    }

    @Override
    public String keyColumn() {
        return "expense_receipt_id";
    }

    @Override
    public String generateId() throws ArahantException {
        return expenseReceiptId = IDGenerator.generate(this);
    }

}
