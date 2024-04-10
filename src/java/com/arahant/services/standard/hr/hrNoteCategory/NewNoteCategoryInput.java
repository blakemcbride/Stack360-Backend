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

package com.arahant.services.standard.hr.hrNoteCategory;

import com.arahant.annotation.Validation;
import com.arahant.business.BHRNoteCategory;
import com.arahant.services.TransmitInputBase;

/**
 * 
 *
 * Created on Feb 22, 2007
 *
 */
public class NewNoteCategoryInput extends TransmitInputBase {

	@Validation (table="hr_note_category",column="name",required=true)
	private String name;
	@Validation (table="hr_note_category",column="cat_code", required=false)
	private String catCode;
	@Validation(type="date", required=false)
	private int firstActiveDate;
	@Validation(type="date", required=false)
	private int lastActiveDate;

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

	public String getCatCode() {
		return catCode;
	}

	public void setCatCode(String catCode) {
		this.catCode = catCode;
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
	 * @param x
	 */
	void setData(final BHRNoteCategory x) {
		x.setName(name);
		x.setFirstActiveDate(firstActiveDate);
		x.setLastActiveDate(lastActiveDate);
		x.setCatCode(catCode);
	}
}

	
