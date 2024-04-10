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
 * Created on Feb 19, 2007
 * 
 */
package com.arahant.business.interfaces;

/**
 * 
 *
 * Created on Feb 19, 2007
 *
 */
public interface ISearchProjects {

	/**
	 * @return Returns the category.
	 */
	public abstract String getCategory();

	/**
	 * @param category The category to set.
	 */
	public abstract void setCategory(String category);

	/**
	 * @return Returns the companyId.
	 */
	public abstract String getCompanyId();

	/**
	 * @param companyId The companyId to set.
	 */
	public abstract void setCompanyId(String companyId);

	/**
	 * @return Returns the fromDate.
	 */
	public abstract int getFromDate();

	/**
	 * @param fromDate The fromDate to set.
	 */
	public abstract void setFromDate(int fromDate);

	/**
	 * @return Returns the personId.
	 */
	public abstract String getPersonId();

	/**
	 * @param personId The personId to set.
	 */
	public abstract void setPersonId(String personId);

	/**
	 * @return Returns the projectName.
	 */
	public abstract String getProjectName();

	/**
	 * @param projectName The projectName to set.
	 */
	public abstract void setProjectName(String projectName);

	/**
	 * @return Returns the quickList.
	 */
	public abstract boolean isQuickList();

	/**
	 * @param quickList The quickList to set.
	 */
	public abstract void setQuickList(boolean quickList);

	/**
	 * @return Returns the status.
	 */
	public abstract String getStatus();

	/**
	 * @param status The status to set.
	 */
	public abstract void setStatus(String status);

	/**
	 * @return Returns the summary.
	 */
	public abstract String getSummary();

	/**
	 * @param summary The summary to set.
	 */
	public abstract void setSummary(String summary);

	/**
	 * @return Returns the toDate.
	 */
	public abstract int getToDate();

	/**
	 * @param toDate The toDate to set.
	 */
	public abstract void setToDate(int toDate);

	/**
	 * @return Returns the type.
	 */
	public abstract String getType();

	/**
	 * @param type The type to set.
	 */
	public abstract void setType(String type);

	public abstract String getUser();
}
