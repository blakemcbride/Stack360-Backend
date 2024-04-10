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
 * Created on Feb 22, 2007
 */
package com.arahant.services.standard.hr.hrNoteCategory;

import com.arahant.business.BHRNoteCategory;

/**
 * 
 *
 * Created on Feb 22, 2007
 *
 */
public class ListNoteCategoryItem {

	private String accrualAccountId;
	private String name;
	private int firstActiveDate;
	private int lastActiveDate;
	private String catCode;
	
	public ListNoteCategoryItem()
	{
	}

	/**
	 * @return Returns the accrualAccountId.
	 */
	public String getNoteCategoryId() {
		return accrualAccountId;
	}

	/**
	 * @param accrualAccountId The accrualAccountId to set.
	 */
	public void setNoteCategoryId(final String accrualAccountId) {
		this.accrualAccountId = accrualAccountId;
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

	public String getAccrualAccountId() {
		return accrualAccountId;
	}

	public void setAccrualAccountId(String accrualAccountId) {
		this.accrualAccountId = accrualAccountId;
	}

    public String getCatCode() {
        return catCode;
    }

    public void setCatCode(String catCode) {
        this.catCode = catCode;
    }

    /**
	 * @param account
	 */
	ListNoteCategoryItem(final BHRNoteCategory account) {
        accrualAccountId = account.getNoteCategoryId();
        name = account.getName();
        lastActiveDate = account.getLastActiveDate();
        firstActiveDate = account.getFirstActiveDate();
        catCode = account.getCatCode();
    }
}

	
