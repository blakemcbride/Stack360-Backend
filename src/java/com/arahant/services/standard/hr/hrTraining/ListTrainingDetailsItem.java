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

package com.arahant.services.standard.hr.hrTraining;

import com.arahant.beans.OrgGroup;
import com.arahant.business.BHRTrainingCategory;
import com.arahant.business.BHRTrainingDetail;
import com.arahant.utils.DateUtils;

/**
 * Created on Feb 25, 2007
 * Modified on Dec 12, 2018
 */
public class ListTrainingDetailsItem {

    private String trainingId;
    private String clientId;
    private String trainingCategoryId;
    private int trainingDate;
    private int expireDate;
    private float trainingHours;
    private String trainingDateFormatted;
    private String expireDateFormatted;
    private String categoryName;
    private String clientName;

    public ListTrainingDetailsItem() {
        super();
    }

    ListTrainingDetailsItem(final BHRTrainingDetail detail, final OrgGroup orgGroup) {
        super();

        clientId = orgGroup.getOrgGroupId();
        clientName = orgGroup.getName();
        trainingId = detail.getTrainingId();
        trainingCategoryId = detail.getTrainingCategoryId();
        trainingDate = detail.getTrainingDate();
        expireDate = detail.getExpireDate();
        trainingHours = detail.getTrainingHours();
        expireDateFormatted = DateUtils.getDateFormatted(expireDate);
        trainingDateFormatted = DateUtils.getDateFormatted(trainingDate);
        categoryName = detail.getTrainingCategoryName();
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * @return Returns the expireDateFormatted.
     */
    public String getExpireDateFormatted() {
        return expireDateFormatted;
    }

    /**
     * @param expireDateFormatted The expireDateFormatted to set.
     */
    public void setExpireDateFormatted(final String expireDateFormatted) {
        this.expireDateFormatted = expireDateFormatted;
    }

    /**
     * @return Returns the trainingDateFormatted.
     */
    public String getTrainingDateFormatted() {
        return trainingDateFormatted;
    }

    /**
     * @param trainingDateFormatted The trainingDateFormatted to set.
     */
    public void setTrainingDateFormatted(final String trainingDateFormatted) {
        this.trainingDateFormatted = trainingDateFormatted;
    }

    /**
     * @return Returns the expireDate.
     */
    public int getExpireDate() {
        return expireDate;
    }

    /**
     * @param expireDate The expireDate to set.
     */
    public void setExpireDate(final int expireDate) {
        this.expireDate = expireDate;
    }

    /**
     * @return Returns the trainingCategoryId.
     */
    public String getTrainingCategoryId() {
        return trainingCategoryId;
    }

    /**
     * @param trainingCategoryId The trainingCategoryId to set.
     */
    public void setTrainingCategoryId(final String trainingCategoryId) {
        this.trainingCategoryId = trainingCategoryId;
    }

    /**
     * @return Returns the trainingDate.
     */
    public int getTrainingDate() {
        return trainingDate;
    }

    /**
     * @param trainingDate The trainingDate to set.
     */
    public void setTrainingDate(final int trainingDate) {
        this.trainingDate = trainingDate;
    }

    /**
     * @return Returns the trainingHours.
     */
    public float getTrainingHours() {
        return trainingHours;
    }

    /**
     * @param trainingHours The trainingHours to set.
     */
    public void setTrainingHours(final float trainingHours) {
        this.trainingHours = trainingHours;
    }

    /**
     * @return Returns the trainingId.
     */
    public String getTrainingId() {
        return trainingId;
    }

    /**
     * @param trainingId The trainingId to set.
     */
    public void setTrainingId(final String trainingId) {
        this.trainingId = trainingId;
    }

    /**
     * @return Returns the categoryName.
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * @param categoryName The categoryName to set.
     */
    public void setCategoryName(final String categoryName) {
        this.categoryName = categoryName;
    }
}

	
