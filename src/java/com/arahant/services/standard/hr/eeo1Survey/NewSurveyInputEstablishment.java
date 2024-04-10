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


package com.arahant.services.standard.hr.eeo1Survey;

public class NewSurveyInputEstablishment {
	private String id;
	private boolean headquarters;
	private boolean filedLastYear;
	private String unitNumber;

    public boolean getFiledLastYear() {
        return filedLastYear;
    }

    public void setFiledLastYear(boolean filedLastYear) {
        this.filedLastYear = filedLastYear;
    }

    public boolean isHeadquarters() {
        return headquarters;
    }

    public void setHeadquarters(boolean headquarters) {
        this.headquarters = headquarters;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }
}
