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
 *
 *  Created on Feb 8, 2007
 */

package com.arahant.services.standard.at.applicantApplicationStatus;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BApplicationStatus;
import com.arahant.annotation.Validation;

public class SaveStatusInput extends TransmitInputBase {
    @Validation(table = "applicant_status", column = "name", required = true)
    private String name;
    @Validation(table = "applicant_status", column = "name", required = true)
    private boolean active;
    @Validation(table = "applicant_status", column = "last_active_date", required = false, type = "date")
    private int inactiveDate;
    @Validation(table = "applicant_status", column = "applicant_status_id", required = true)
    private String id;
    private short phase;

    void setData(BApplicationStatus bc) {
        bc.setName(name);
        bc.setActive(active);
        bc.setInactiveDate(inactiveDate);
        bc.setPhase(phase);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getInactiveDate() {
        return inactiveDate;
    }

    public void setInactiveDate(int inactiveDate) {
        this.inactiveDate = inactiveDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public short getPhase() {
        return phase;
    }

    public void setPhase(short phase) {
        this.phase = phase;
    }
}


