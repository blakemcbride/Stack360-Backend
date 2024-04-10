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


/**
 * Created on Feb 22, 2007
 *
 */
package com.arahant.services.standard.hr.hrTrainingCategory;

import com.arahant.business.BHRTrainingCategory;


/**
 * Created on Feb 22, 2007
 */
public class ListTrainingCategoryItem {

    private String trainingCategoryId;
    private String name;
    private int type;
    private String typeFormatted;
    private int lastActiveDate;
    private String clientId;
    private String required;
    private float hours;

    /**
     * @return Returns the typeFormatted.
     */
    public String getTypeFormatted() {
        return typeFormatted;
    }

    /**
     * @param typeFormatted The typeFormatted to set.
     */
    public void setTypeFormatted(final String typeFormatted) {
        this.typeFormatted = typeFormatted;
    }

    public ListTrainingCategoryItem() {

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
     * @return Returns the accrualAccountId.
     */
    public String getTrainingCategoryId() {
        return trainingCategoryId;
    }

    /**
     * @param accrualAccountId The accrualAccountId to set.
     */
    public void setTrainingCategoryId(final String accrualAccountId) {
        this.trainingCategoryId = accrualAccountId;
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

    public int getLastActiveDate() {
        return lastActiveDate;
    }

    public void setLastActiveDate(int lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public float getHours() {
        return hours;
    }

    public void setHours(float hours) {
        this.hours = hours;
    }

    /**
     * @param account
     */
    ListTrainingCategoryItem(final BHRTrainingCategory account) {
        trainingCategoryId = account.getTrainingCategoryId();
        name = account.getName();
        type = account.getType();
        if (getType() == 1)
            typeFormatted = "Training";
        else if (getType() == 2)
            typeFormatted = "Certification";
        else
            typeFormatted = "Unknown";
        lastActiveDate = account.getLastActiveDate();
        clientId = account.getClientId();
        required = String.valueOf(account.getRequired());
        hours = account.getHours();
    }
}

	
