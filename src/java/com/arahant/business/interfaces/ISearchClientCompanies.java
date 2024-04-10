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
 * Created on Feb 15, 2007
 * 
 */
package com.arahant.business.interfaces;


/**
 * 
 *
 * Created on Feb 15, 2007
 *
 */
public interface ISearchClientCompanies {


	/**
	 * @return Returns the mainContactFirstName.
	 */
	public abstract String getMainContactFirstName();

	/**
	 * @param mainContactFirstName The mainContactFirstName to set.
	 */
	public abstract void setMainContactFirstName(String mainContactFirstName);

	/**
	 * @return Returns the mainContactLastName.
	 */
	public abstract String getMainContactLastName();

	/**
	 * @param mainContactLastName The mainContactLastName to set.
	 */
	public abstract void setMainContactLastName(String mainContactLastName);

	/**
	 * @return Returns the vendorId.
	 */
	public abstract String getId();

	/**
	 * @param vendorId The vendorId to set.
	 */
	public abstract void setId(String vendorId);

	/**
	 * @return Returns the vendorName.
	 */
	public abstract String getName();

	public abstract void setName(String name);

	/**
	 * @return Returns the status.
	 */
	public abstract int getStatus();

	/**
	 * @param status The status to set.
	 */
	public abstract void setStatus(int status);

}
