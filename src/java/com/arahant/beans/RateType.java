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
@Table(name=RateType.TABLE_NAME)
public class RateType extends ArahantBean implements Serializable {

    public static final String TABLE_NAME = "rate_type";

    public static final String RATE_TYPE_ID = "rateTypeId";
    public static final String RATE_CODE = "rateCode";
    public static final String TYPE = "rateType";
    public static final String ID = "rateTypeId";
    public static final char TYPE_PERSON='E';
    public static final char TYPE_PROJECT='P';
    public static final char TYPE_BOTH='B';
    private String rateTypeId;
    private String rateTypeCode;
    private String description;
    public static final String DESCRIPTION = "description";
    public static final String LAST_ACTIVE_DATE = "lastActiveDate";
    private int lastActiveDate;
    private char rateType; // P = Project, E = Person, B = Both
    public static final String COMPANY_ID = "companyId";
    private String companyId;


    @Id
    @Column(name="rate_type_id")
    public String getRateTypeId() {
        return rateTypeId;
    }

    public void setRateTypeId(String rateTypeId) {
        this.rateTypeId = rateTypeId;
    }

    @Column(name="rate_code")
    public String getRateCode() {
        return rateTypeCode;
    }

    public void setRateCode(String rateTypeCode) {
        this.rateTypeCode = rateTypeCode;
    }

    @Column(name="description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String tableName() {
        return TABLE_NAME;
    }

    @Override
    public String keyColumn() {
        return "rate_type_id";
    }

    @Override
    public String generateId() throws ArahantException {
        return rateTypeId = IDGenerator.generate(this);
    }

    @Column(name="last_active_date")
    public int getLastActiveDate() {
        return lastActiveDate;
    }

    public void setLastActiveDate(int lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    @Column(name = "rate_type")
    public char getRateType() {
        return rateType;
    }

    public void setRateType(char rateType) {
        this.rateType = rateType;
    }

    @Column(name = "company_id", updatable = false, insertable = false)
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
