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

package com.arahant.business;

import com.arahant.beans.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.KissConnection;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;
import com.arahant.utils.ExternalFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;


/**
 * Author: Blake McBride
 * Date: 4/7/18
 */
public class BExpenseReceipt  extends SimpleBusinessObjectBase<ExpenseReceipt> {

    public BExpenseReceipt () {}

    public BExpenseReceipt(String id) {
        super(id);
    }

    public BExpenseReceipt(ExpenseReceipt er) {
        bean = er;
    }

    public String getExpenseReceiptId() {
        return bean.getExpenseReceiptId();
    }

    public String getPersonId() {
        return bean.getPersonId();
    }

    public Person getPerson() {
        return bean.getPerson();
    }

    public BExpenseReceipt setPerson(Person p) {
        bean.setPerson(p);
        return this;
    }

    public String getProjectId() {
        return bean.getProjectId();
    }

    public Project getProject() {
        return bean.getProject();
    }

    public BExpenseReceipt setProject(Project p) {
        bean.setProject(p);
        return this;
    }

    public String getExpenseAccountId() {
        return bean.getExpenseAccountId();
    }

    public ExpenseAccount getExpenseAccount() {
        return bean.getExpenseAccount();
    }

    public BExpenseReceipt setExpenseAccount(ExpenseAccount ea) {
        bean.setExpenseAccount(ea);
        return this;
    }

    public int getReceiptDate() {
        return bean.getReceiptDate();
    }

    public BExpenseReceipt setReceiptDate(int dt) {
        bean.setReceiptDate(dt);
        return this;
    }

    public Date getWhenUploaded() {
        return bean.getWhenUploaded();
    }

    public BExpenseReceipt setWhenUploaded(Date dt) {
        bean.setWhenUploaded(dt);
        return this;
    }

    public String getBusinessPurpose() {
        return bean.getBusinessPurpose();
    }

    public BExpenseReceipt setBusinessPurpose(String txt) {
        bean.setBusinessPurpose(txt);
        return this;
    }

    public double getAmount() {
        return bean.getAmount();
    }

    public BExpenseReceipt setAmount(double amount) {
        bean.setAmount(amount);
        return this;
    }

    public byte [] getPicture1() throws IOException {
        return ExternalFile.getBinary(ExternalFile.EXPENSE_RECEIPT_PICTURE1, bean.getExpenseReceiptId(), bean.getFileType());
    }

    public byte [] getPicture2() throws IOException {
        return ExternalFile.getBinary(ExternalFile.EXPENSE_RECEIPT_PICTURE2, bean.getExpenseReceiptId(), bean.getFileType());
    }

    public String getWhoUploadedId() {
        return bean.getWhoUploadedId();
    }

    public Person getWhoUploaded() {
        return bean.getWhoUploaded();
    }

    public BExpenseReceipt setWhoUploaded(Person p) {
        bean.setWhoUploaded(p);
        return this;
    }

    public String getFileType() {
        return bean.getFileType();
    }

    public BExpenseReceipt setFileType(String ftype) {
        bean.setFileType(ftype);
        return this;
    }

    public char getPaymentMethod() {
        return bean.getPaymentMethod();
    }

    public BExpenseReceipt setPaymentMethod(char pm) {
        bean.setPaymentMethod(pm);
        return this;
    }

    public char getApproved() {
        return bean.getApproved();
    }

    public BExpenseReceipt setApproved(char approved) {
        bean.setApproved(approved);
        return this;
    }

    public String getWhoApprovedId() {
        return bean.getWhoApprovedId();
    }

    public Person getWhoApproved() {
        return bean.getWhoApproved();
    }

    public BExpenseReceipt setWhoApproved(Person emp) {
        bean.setWhoApproved(emp);
        return this;
    }

    public Date getWhenApproved() {
        return bean.getWhenApproved();
    }

    public BExpenseReceipt setWhenApproved(Date dt) {
        bean.setWhenApproved(dt);
        return this;
    }

    @Override
    public String create() throws ArahantException {
        bean = new ExpenseReceipt();
        return bean.generateId();
    }

    @Override
    public void load(String key) throws ArahantException {
        bean = ArahantSession.getHSU().get(ExpenseReceipt.class, key);
    }
}
