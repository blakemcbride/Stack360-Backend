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

package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Author: Blake McBride
 * Date: 11/19/16
 */

@Entity
@Table(name=EmployeeRate.TABLE_NAME)
public class EmployeeRate extends ArahantBean implements Serializable {

    public static final String TABLE_NAME = "employee_rate";
    public static final String PERSON = "person";
    public static final String RATE_TYPE = "rateType";

    private String employeeRateId;
    private Person person;
    private RateType rateType;
    private double rate;

    public EmployeeRate() {}


    @Id
    @Column(name="employee_rate_id")
    public String getEmployeeRateId() {
        return employeeRateId;
    }

    public void setEmployeeRateId(String employeeRateId) {
        this.employeeRateId = employeeRateId;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "person_id")
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "rate_type_id")
    public RateType getRateType() {
        return rateType;
    }

    public void setRateType(RateType rateType) {
        this.rateType = rateType;
    }

    @Column(name="rate")
    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public String tableName() {
        return TABLE_NAME;
    }

    @Override
    public String keyColumn() {
        return "employee_rate_id";
    }

    @Override
    public String generateId() throws ArahantException {
        return employeeRateId = IDGenerator.generate(this);
    }

}
