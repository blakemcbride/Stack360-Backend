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

import com.arahant.beans.Employee;
import com.arahant.beans.Expense;
import com.arahant.beans.Person;
import com.arahant.beans.Project;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantJessException;
import com.arahant.exceptions.ArahantSecurityException;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;

/**
 * Author: Blake McBride
 * Date: 2/15/18
 */
public class BExpense implements IDBFunctions {

    private static final transient ArahantLogger logger = new ArahantLogger(BExpense.class);
    private Expense bean;

    public BExpense () {}

    public BExpense(String expense_id) {
        internalLoad(expense_id);
    }

    public BExpense(Expense expense) {
        bean = expense;
    }

    public Expense getExpense() {
        return bean;
    }

    public String getExpenseId() {
        return bean.getExpenseId();
    }

    public Employee getEmployee() {
        return bean.getEmployee();
    }

    public Project getProject() {
        return bean.getProject();
    }

    public int getDatePaid() {
        return bean.getDatePaid();
    }

    public void setDatePaid(int val) {
        bean.setDatePaid(val);
    }

    public int getWeekPaidFor() {
        return bean.getWeekPaidFor();
    }

    public void setWeekPaidFor(int val) {
        bean.setWeekPaidFor(val);
    }

    public float getPerDiemAmount() {
        return bean.getPerDiemAmount();
    }

    public void setPerDiemAmount(float val) {
        bean.setPerDiemAmount(val);
    }

    public float getExpenseAmount() {
        return bean.getExpenseAmount();
    }

    public void setExpenseAmount(float val) {
        bean.setExpenseAmount(val);
    }

    public float getAdvanceAmount() {
        return bean.getAdvanceAmount();
    }

    public void setAdvanceAmount(float val) {
        bean.setAdvanceAmount(val);
    }

    public char getPaymentMethod() {
        return bean.getPaymentMethod();
    }

    public String getComments() {
        return bean.getComments();
    }

    public String getSchedulingComments() {
        return bean.getSchedulingComments();
    }

    public int getAuthDate() {
        return bean.getAuthDate();
    }

    public Person getAuthPerson () {
        return bean.getAuthPerson();
    }

    public float getPerDiemReturn() {
        return bean.getPerDiemReturn();
    }

    public void setPerDiemReturn(float val) {
        bean.setPerDiemReturn(val);
    }

    public float getHotelAmount() {
        return bean.getHotelAmount();
    }

    public void setHotelAmount(float val) {
        bean.setHotelAmount(val);
    }

    @Override
    public void update() throws ArahantException {
        ArahantSession.getHSU().saveOrUpdate(bean);
    }

    @Override
    public void insert() throws ArahantException {
        bean.generateId();
        ArahantSession.getHSU().insert(bean);
    }

    @Override
    public void delete() throws ArahantDeleteException, ArahantSecurityException, ArahantJessException {
        try {
            ArahantSession.getHSU().delete(bean);
        } catch (final RuntimeException e) {
            throw new ArahantDeleteException();
        }
    }

    @Override
    public String create() throws ArahantException {
        bean = new Expense();
        bean.getExpenseId();
        return bean.getExpenseId();
    }

    @Override
    public void load(String key) throws ArahantException {
        internalLoad(key);
    }

    private void internalLoad(final String key) throws ArahantException {
        bean = ArahantSession.getHSU().get(Expense.class, key);
    }
}
