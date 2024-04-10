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

import com.arahant.beans.ExpenseAccount;
import com.arahant.beans.GlAccount;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;

/**
 * Author: Blake McBride
 * Date: 4/2/18
 */
public class BExpenseAccount extends SimpleBusinessObjectBase<ExpenseAccount> {

    public BExpenseAccount() {}

    public BExpenseAccount(String id) {
        super(id);
    }

    public BExpenseAccount(ExpenseAccount ea) {
        bean = ea;
    }

    public String getExpenseAccountId() {
        return bean.getExpenseAccountId();
    }

    public String getExpenseId() {
        return bean.getExpenseId();
    }

    public void setExpenseId(String id) {
        bean.setExpenseId(id);
    }

    public GlAccount getGlAccount() {
        return bean.getGlAccount();
    }

    public void setGlAccount(GlAccount gla) {
        bean.setGlAccount(gla);
    }

    public String getDescription() {
        return bean.getDescription();
    }

    public void setDescription(String desc) {
        bean.setDescription(desc);
    }

    public GlAccount getGlAccountId() {
        return bean.getGlAccount();
    }

    @Override
    public String create() throws ArahantException {
        bean = new ExpenseAccount();
        return bean.generateId();
    }

    @Override
    public void load(String key) throws ArahantException {
        bean = ArahantSession.getHSU().get(ExpenseAccount.class, key);
    }
}
