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

package com.arahant.services.standard.at.applicantQuestion;

/**
 * Author: Blake McBride
 * Date: 3/9/23
 */
public class SearchPositionsReturnItem {

    private String positionId;
    private int firstActiveDate;
    private int lastActiveDate;
    private String positionName;

    public SearchPositionsReturnItem(String positionId, String positionName, int firstActiveDate, int lastActiveDate) {
        this.positionId = positionId;
        this.positionName = positionName;
        this.firstActiveDate = firstActiveDate;
        this.lastActiveDate = lastActiveDate;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public int getFirstActiveDate() {
        return firstActiveDate;
    }

    public void setFirstActiveDate(int firstActiveDate) {
        this.firstActiveDate = firstActiveDate;
    }

    public int getLastActiveDate() {
        return lastActiveDate;
    }

    public void setLastActiveDate(int lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
}
