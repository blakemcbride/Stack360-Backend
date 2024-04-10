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

package com.arahant.services.standard.hr.spousalVerification;

import com.arahant.business.BSpousalVerification;

/**
 *
 */
public class LoadSpousalStatusReturnItem {
	private String id;
	private int year;
	private int yearHalf;
	private int dateReceived;

	public LoadSpousalStatusReturnItem()
	{

	}

	LoadSpousalStatusReturnItem(BSpousalVerification o) {
		id=o.getId();
		year=o.getYear();
		yearHalf=o.getYearHalf();
		dateReceived=o.getDateReceived();
	}

	public int getYearHalf() {
		return yearHalf;
	}

	public void setYearHalf(int yearHalf) {
		this.yearHalf = yearHalf;
	}

	public int getDateReceived() {
		return dateReceived;
	}

	public void setDateReceived(int dateReceived) {
		this.dateReceived = dateReceived;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}


}
