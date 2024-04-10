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
 * 
 */
package com.arahant.services.standard.misc.alertAgent;

import com.arahant.business.BAlert;
import com.arahant.utils.DateUtils;

public class SearchAlertsReturnItem {

    public SearchAlertsReturnItem() {
    }

    SearchAlertsReturnItem(BAlert ba) {

        id = ba.getAlertId();

        startDate = ba.getStartDate();
        endDate = ba.getLastDate();
        lastChangePerson = ba.getLastChangePerson();
        lastChangeDate = DateUtils.getDateTimeFormatted(ba.getLastChangeDateTime());

        if (ba.getAlert().length()>100)
			detail=ba.getAlert().substring(0,100).replace("\n", " ");
		else
			detail=ba.getAlert().replace("\n", " ");

    }
    
    private String id;
    private String name;
    private String detail;
    private int startDate;
    private int endDate;
    private String lastChangePerson;
    private String lastChangeDate;

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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public int getEndDate() {
        return endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }

    public String getLastChangeDate() {
        return lastChangeDate.toString();
    }

    public void setLastChangeDate(String lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
    }

    public String getLastChangePerson() {
        return lastChangePerson;
    }

    public void setLastChangePerson(String lastChangePerson) {
        this.lastChangePerson = lastChangePerson;
    }
}

	
