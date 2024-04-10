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

package com.arahant.business;

import com.arahant.beans.EmployeeRate;
import com.arahant.beans.Person;
import com.arahant.beans.RateType;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;

import java.io.File;

/**
 * Author: Blake McBride
 * Date: 3/28/17
 */
public class BEmployeeRate extends BusinessLogicBase implements IDBFunctions {

    private EmployeeRate employeeRate;

    public BEmployeeRate() {
    }

    public BEmployeeRate(final String key) throws ArahantException {
        internalLoad(key);
    }

    public BEmployeeRate(final EmployeeRate er) {
        employeeRate = er;
    }

    public void stub() {
        employeeRate = new EmployeeRate();
    }

    @Override
    public String create() throws ArahantException {
        employeeRate = new EmployeeRate();
        employeeRate.generateId();
        return employeeRate.getEmployeeRateId();
    }

    @Override
    public void delete() throws ArahantDeleteException {
        ArahantSession.getHSU().delete(employeeRate);
    }

    @Override
    public void insert() throws ArahantException {
        ArahantSession.getHSU().insert(employeeRate);
    }

    private void internalLoad(final String key) throws ArahantException {
        employeeRate = ArahantSession.getHSU().get(EmployeeRate.class, key);
    }

    @Override
    public void load(final String key) throws ArahantException {
        internalLoad(key);
    }

    @Override
    public void update() throws ArahantException {
        ArahantSession.getHSU().saveOrUpdate(employeeRate);
    }

    public String getEmployeeRateId() {
        return employeeRate.getEmployeeRateId();
    }

    public Person getPerson() {
        return employeeRate.getPerson();
    }

    public void setPerson(Person p) {
        employeeRate.setPerson(p);
    }


    public RateType getRateType() {
        return employeeRate.getRateType();
    }

    public void setRateType(RateType p) {
        employeeRate.setRateType(p);
    }

    public double getRate() {
        return employeeRate.getRate();
    }

    public void setRate(double p) {
        employeeRate.setRate(p);
    }

    public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantDeleteException, ArahantException {
        for (final String element : ids)
            new BEmployeeRate(element).delete();
    }

    public void setPersonId(final String personId) {
        employeeRate.setPerson(ArahantSession.getHSU().get(Person.class, personId));
    }

    public String getPersonId() {
        return employeeRate.getPerson().getPersonId();
    }


}
