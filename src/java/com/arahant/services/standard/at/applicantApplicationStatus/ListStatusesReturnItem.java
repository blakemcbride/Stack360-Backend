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

package com.arahant.services.standard.at.applicantApplicationStatus;

import com.arahant.business.BApplicationStatus;

public class ListStatusesReturnItem {

    private String id;
    private String name;
    private boolean active;
    private int inactiveDate;
    private short phase;

    public ListStatusesReturnItem() {
    }

    ListStatusesReturnItem(BApplicationStatus bc) {
        id = bc.getId();
        name = bc.getName();
        active = bc.getActive();
        inactiveDate = bc.getInactiveDate();
        phase = bc.getPhase();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public short getPhase() {
        return phase;
    }

    public void setPhase(short phase) {
        this.phase = phase;
    }
}


