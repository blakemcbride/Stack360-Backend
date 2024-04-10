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

import javax.persistence.*;

/**
 * Author: Blake McBride
 * Date: 4/2/18
 */
@Entity
@Table(name = ExpenseAccount.TABLE_NAME)
public class ExpenseAccount extends ArahantBean {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "expense_account";

    private String expenseAccountId;
    public static final String EXPENSE_ACCOUNT_ID = "expenseAccountId";
    private String expenseId;
    private String description;
    public static final String DESCRIPTION = "description";
    private String glAccountId;
    public static final String GLAccountId = "glAccountId";
    private GlAccount glAccount;
    public static final String GLAccount = "glAccount";

    public ExpenseAccount () {}

    @Id
    @Column(name = "expense_account_id")
    public String getExpenseAccountId() {
        return expenseAccountId;
    }

    public void setExpenseAccountId(String expenseAccountId) {
        this.expenseAccountId = expenseAccountId;
    }

    @Column(name = "expense_id")
    public String getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "gl_account_id", insertable = false, updatable = false)
    public String getGlAccountId() {
        return glAccountId;
    }

    public void setGlAccountId(String glAccountId) {
        this.glAccountId = glAccountId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gl_account_id")
    public GlAccount getGlAccount() {
        return glAccount;
    }

    public void setGlAccount(GlAccount acct) {
        glAccount = acct;
    }

    @Override
    public String tableName() {
        return TABLE_NAME;
    }

    @Override
    public String keyColumn() {
        return "expense_account_id";
    }

    @Override
    public String generateId() throws ArahantException {
        return expenseAccountId = IDGenerator.generate(this);
    }

}
