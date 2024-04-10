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
 * Created on Feb 8, 2007
 * 
 */
package com.arahant.services.standard.billing.holiday;

import com.arahant.annotation.Validation;

import com.arahant.business.BHoliday;
import com.arahant.services.TransmitInputBase;

/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class AddHolidayInput extends TransmitInputBase {

    @Validation(table = "holiday", column = "description", required = true)
    private String name;
    @Validation(type = "date", required = true)
    private int date;
    @Validation(required = true)
    private boolean applyToAll;
	@Validation(table = "holiday", column = "part_of_day", required = true)
	private String partOfDay;

    /**
     * @return Returns the date.
     */
    public int getDate() {
        return date;
    }

    /**
     * @param date The date to set.
     */
    public void setDate(final int date) {
        this.date = date;
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

    /**
     * @param bh
     */
    void makeHoliday(final BHoliday bh) {
        bh.setDescription(name);
        bh.setHdate(date);
		bh.setPartOfDay(partOfDay.charAt(0));
    }

    public boolean isApplyToAll() {
        return applyToAll;
    }

    public void setApplyToAll(boolean applyToAll) {
        this.applyToAll = applyToAll;
    }

	public String getPartOfDay() {
		return partOfDay;
	}

	public void setPartOfDay(String partOfDay) {
		this.partOfDay = partOfDay;
	}
}

	
