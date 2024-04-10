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

package com.arahant.services.standard.project.projectCurrentStatus;

import com.arahant.services.TransmitReturnBase;

/**
 * Author: Blake McBride
 * Date: 11/5/20
 */
public class CheckForWorkerCollisionReturn extends TransmitReturnBase {

    private boolean hasConflict;  //  is already assigned to a different project with an overlapping period
    private boolean i9part1Complete;

    public boolean isHasConflict() {
        return hasConflict;
    }

    public void setHasConflict(boolean hasConflict) {
        this.hasConflict = hasConflict;
    }

    public boolean isI9part1Complete() {
        return i9part1Complete;
    }

    public void setI9part1Complete(boolean i9part1Complete) {
        this.i9part1Complete = i9part1Complete;
    }
}
