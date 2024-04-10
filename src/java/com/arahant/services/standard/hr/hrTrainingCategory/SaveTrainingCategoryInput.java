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

package com.arahant.services.standard.hr.hrTrainingCategory;

import com.arahant.annotation.Validation;
import com.arahant.beans.OrgGroup;
import com.arahant.business.BHRTrainingCategory;
import com.arahant.services.TransmitInputBase;

/**
 * Created on Feb 22, 2007
 */
public class SaveTrainingCategoryInput extends TransmitInputBase {

    @Validation(required = true)
    private String clientId;

    @Validation(required = true)
    private String id;

    @Validation(table = "hr_training_category", column = "name", required = true)
    private String name;

    @Validation(table = "hr_training_category", column = "training_type", required = true)
    private int type;

    @Validation(required = false, min = 0)
    private float hours;

    @Validation(required = false)
    private String required;

    @Validation(type = "date", required = false)
    private int lastActiveDate;

    public int getLastActiveDate() {
        return lastActiveDate;
    }

    public void setLastActiveDate(int lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    /**
     * @return Returns the type.
     */
    public int getType() {
        return type;
    }

    /**
     * @param type The type to set.
     */
    public void setType(final int type) {
        this.type = type;
    }

    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(final String name) {
        this.name = name;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public float getHours() {
        return hours;
    }

    public void setHours(float hours) {
        this.hours = hours;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    /**
     * @param x
     */
    void setData(final BHRTrainingCategory x, final OrgGroup client) {
        x.setClient(client);
        x.setName(name);
        x.setType((short) type);
        x.setHours(hours);
        x.setRequired(required.charAt(0));
        x.setLastActiveDate(lastActiveDate);
    }
}

	
