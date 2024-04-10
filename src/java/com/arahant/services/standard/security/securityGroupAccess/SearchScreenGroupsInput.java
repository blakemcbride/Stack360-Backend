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
 *
 */
package com.arahant.services.standard.security.securityGroupAccess;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;

public class SearchScreenGroupsInput extends TransmitInputBase  {

	@Validation (table="screen_group",column="name",required=false)
	private String name;//  (like clause)
	@Validation (table="screen_group",column="ext_id",required=false)
	private String extId;
	@Validation (required=false)
	private String [] excludeIds;
	@Validation (min=2,max=5,required=false)
	private int nameSearchType;
	@Validation (required=true)
	private boolean searchTopLevelOnly;

	public boolean getSearchTopLevelOnly() {
		return searchTopLevelOnly;
	}

	public void setSearchTopLevelOnly(boolean searchTopLevelOnly) {
		this.searchTopLevelOnly = searchTopLevelOnly;
	}

	/**
	 * @return Returns the nameSearchType.
	 */
	public int getNameSearchType() {
		return nameSearchType;
	}
	/**
	 * @param nameSearchType The nameSearchType to set.
	 */
	public void setNameSearchType(final int nameSearchType) {
		this.nameSearchType = nameSearchType;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return modifyForSearch(name, nameSearchType);
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return Returns the extId.
	 */
	public String getExtId() {
		return this.extId;
	}
	/**
	 * @param extId The extId to set.
	 */
	public void setExtId(final String extId) {
		this.extId = extId;
	}

	public String[] getExcludeIds() {
		return excludeIds;
	}

	public void setExcludeIds(String[] excludeIds) {
		this.excludeIds = excludeIds;
	}
}

	
