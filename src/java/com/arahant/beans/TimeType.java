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
 * All rights reserved.
 */
package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 *
 */
@Entity
@Table(name = TimeType.TABLE_NAME)
public class TimeType extends ArahantBean implements java.io.Serializable, ArahantSaveNotify {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "time_type";
    
    private String timeTypeId;
    private String description;
    private int lastActiveDate = 0;
    private char defaultBillable = 'U';
    private char defaultType = 'N';
    public static final String DEFAULT_TYPE = "defaultType";
    
    public TimeType() {}

    @Override
    public String tableName() {
       return TABLE_NAME;
    }

    @Override
    public String keyColumn() {
        return "time_type_id";
    }

    @Override
    public String generateId() throws ArahantException {
        timeTypeId = IDGenerator.generate(this);
		return timeTypeId;
    }

    @Override
    public String notifyId() {
        return "timeTypeId";
    }

    @Override
    public String notifyClassName() {
        return "TimeType";
    }

    @Id
    @Column(name = "time_type_id")
    public String getTimeTypeId() {
        return timeTypeId;
    }

    public void setTimeTypeId(String timeTypeId) {
        this.timeTypeId = timeTypeId;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "last_active_date")
    public int getLastActiveDate() {
        return lastActiveDate;
    }

    public void setLastActiveDate(int lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    @Column(name = "default_billable")
    public char getDefaultBillable() {
        return defaultBillable;
    }

    public void setDefaultBillable(char defaultBillable) {
        this.defaultBillable = defaultBillable;
    }

    @Column(name = "default_type")
    public char getDefaultType() {
        return defaultType;
    }

    public void setDefaultType(char defaultType) {
        this.defaultType = defaultType;
    }

}
