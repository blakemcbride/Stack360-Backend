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
public interface IOrgGroupSearchCriteria {

	/**
	 * @return Returns the associatedIndicator.
	 */
	public abstract int getAssociatedIndicator();

	/**
	 * @param associatedIndicator The associatedIndicator to set.
	 */
	public abstract void setAssociatedIndicator(int associatedIndicator);

	/**
	 * @return Returns the name.
	 */
	public abstract String getName();

	/**
	 * @param name The name to set.
	 */
	public abstract void setName(String name);

	/**
	 * @return Returns the orgGroupId.
	 */
	public abstract String getOrgGroupId();

	/**
	 * @param orgGroupId The orgGroupId to set.
	 */
	public abstract void setOrgGroupId(String orgGroupId);

	/**
	 * @return Returns the orgGroupType.
	 */
	public abstract int getOrgGroupType();

	/**
	 * @param orgGroupType The orgGroupType to set.
	 */
	public abstract void setOrgGroupType(int orgGroupType);

}
