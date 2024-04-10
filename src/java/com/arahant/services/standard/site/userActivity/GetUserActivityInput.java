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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.services.standard.site.userActivity;

import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;
import java.util.Date;

/**
 *
 * Arahant
 */
public class GetUserActivityInput extends TransmitInputBase{

    // The array of subordiante employees sent to the back end is different from the array of employees originally sent to the front end, because
    // the array sent back is only the subordinate employees (and possibly the user his/herself) that the user has chosen from the DataGrid
    @Validation(required=false)
    private String[] subordinateIds;
    @Validation(required=false)
    private boolean includeUser = false;
    @Validation(required=false, type="date")
    private int startingDate;
    @Validation(required=false, type="date")
    private int endingDate;

    public boolean isIncludeUser() {
        return includeUser;
    }

    public void setIncludeUser(boolean includeUser) {
        this.includeUser = includeUser;
    }

    public String[] getSubordinateIds() {
        return subordinateIds;
    }

    public void setSubordinateIds(String[] subordinateIds) {
        this.subordinateIds = subordinateIds;
    }

    public int getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(int startingDate) {
        this.startingDate = startingDate;
    }

    public int getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(int endingDate) {
        this.endingDate = endingDate;
    }
}
