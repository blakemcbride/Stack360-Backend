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
 * Created on Oct 9, 2009
 * 
 */
package com.arahant.services.standard.misc.agency;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Oct 9, 2009
 *
 */
public class SearchScreenGroupsInput extends TransmitInputBase {

	@Validation (table="screen_group",column="name",required=false)
	private String name;//  (like clause)
	@Validation (table="screen_group",column="ext_id",required=false)
	private String extId;
	
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
	/* (non-Javadoc)
	 * @see com.arahant.services.client.ISearchScreenGroups#getName()
	 */
	public String getName() {
		return modifyForSearch(name, nameSearchType);
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.client.ISearchScreenGroups#setName(java.lang.String)
	 */
	public void setName(final String name) {
		this.name = name;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.client.ISearchScreenGroups#getExtId()
	 */
	public String getExtId() {
		return this.extId;
	}
	/* (non-Javadoc)
	 * @see com.arahant.services.client.ISearchScreenGroups#setExtId(java.lang.String)
	 */
	public void setExtId(final String extId) {
		this.extId = extId;
	} 
}

	
