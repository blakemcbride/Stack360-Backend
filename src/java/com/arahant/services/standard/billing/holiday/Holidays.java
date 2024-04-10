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
 * Created on Feb 11, 2007
 * 
 */
package com.arahant.services.standard.billing.holiday;
import com.arahant.business.BHoliday;
import com.arahant.utils.DateUtils;


/**
 * 
 *
 * Created on Feb 11, 2007
 *
 */
public class Holidays {

	public Holidays() {
		
	}
	
	private String id;
	private String dateFormatted;
	private String name;
	private int date;
	private String partOfDay;
	
	/**
	 * @param holiday
	 */
	public Holidays(final BHoliday h) {
		super();
		id=h.getHolidayId();
		dateFormatted=DateUtils.getDateFormatted(h.getHdate());
		name=h.getDescription();
		date=h.getHdate();
		partOfDay=h.getPartOfDay() + "";
	}

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
	 * @return Returns the dateFormatted.
	 */
	public String getDateFormatted() {
		return dateFormatted;
	}

	/**
	 * @param dateFormatted The dateFormatted to set.
	 */
	public void setDateFormatted(final String dateFormatted) {
		this.dateFormatted = dateFormatted;
	}

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(final String id) {
		this.id = id;
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

	public String getPartOfDay() {
		return partOfDay;
	}

	public void setPartOfDay(String partOfDay) {
		this.partOfDay = partOfDay;
	}
}

	
