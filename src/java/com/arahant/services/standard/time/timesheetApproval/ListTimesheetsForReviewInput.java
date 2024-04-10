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
 * Created on Feb 9, 2007
 * 
 */
package com.arahant.services.standard.time.timesheetApproval;

import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;

/**
 * 
 *
 * Created on Feb 9, 2007
 *
 */
public class ListTimesheetsForReviewInput extends TransmitInputBase {

    @Validation(required = true)
    private String employeeId;
    @Validation(required = false)
    private boolean finalizedFlag;
    @Validation(required = false)
    private boolean nonFinalizedFlag;
    @Validation(required = false)
    private boolean billableFlag;
    @Validation(required = false)
    private boolean nonBillableFlag;
    @Validation(type = "date", required = true)
    private int fromDate;
    @Validation(type = "date", required = true)
    private int toDate;

    /**
     * @return Returns the billableFlag.
     */
    public boolean isBillableFlag() {
        return billableFlag;
    }

    /**
     * @param billableFlag The billableFlag to set.
     */
    public void setBillableFlag(final boolean billableFlag) {
        this.billableFlag = billableFlag;
    }

    /**
     * @return Returns the employeeId.
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId The employeeId to set.
     */
    public void setEmployeeId(final String employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return Returns the finalizedFlag.
     */
    public boolean isFinalizedFlag() {
        return finalizedFlag;
    }

    /**
     * @param finalizedFlag The finalizedFlag to set.
     */
    public void setFinalizedFlag(final boolean finalizedFlag) {
        this.finalizedFlag = finalizedFlag;
    }

    /**
     * @return Returns the nonBillableFlag.
     */
    public boolean isNonBillableFlag() {
        return nonBillableFlag;
    }

    /**
     * @param nonBillableFlag The nonBillableFlag to set.
     */
    public void setNonBillableFlag(final boolean nonBillableFlag) {
        this.nonBillableFlag = nonBillableFlag;
    }

    /**
     * @return Returns the nonFinalizedFlag.
     */
    public boolean isNonFinalizedFlag() {
        return nonFinalizedFlag;
    }

    /**
     * @param nonFinalizedFlag The nonFinalizedFlag to set.
     */
    public void setNonFinalizedFlag(final boolean nonFinalizedFlag) {
        this.nonFinalizedFlag = nonFinalizedFlag;
    }

    public ListTimesheetsForReviewInput() {
        super();
    }

    public int getFromDate() {
        return fromDate;
    }

    public void setFromDate(int fromDate) {
        this.fromDate = fromDate;
    }

    public int getToDate() {
        return toDate;
    }

    public void setToDate(int toDate) {
        this.toDate = toDate;
    }
}

	
