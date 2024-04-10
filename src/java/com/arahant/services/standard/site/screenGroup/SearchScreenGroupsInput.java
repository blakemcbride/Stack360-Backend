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
 * Created on Feb 4, 2007
 * 
 */
package com.arahant.services.standard.site.screenGroup;
import com.arahant.annotation.Validation;

import com.arahant.business.interfaces.ISearchScreenGroups;
import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 4, 2007
 *
 */
public class SearchScreenGroupsInput extends TransmitInputBase implements ISearchScreenGroups {
	@Validation (min=1,max=2,required=false)
	private int associatedIndicator;// (0=both  1=assocaited  2=dissassociated)
	@Validation (min=1,max=2,required=false)
	private int typeIndicator; // (0=both  1=screen  2=screen group)
	@Validation (table="screen_group",column="name",required=false)
	private String name;//  (like clause)
	@Validation (required=false)
	private String screenGroupId;
	@Validation (table="screen_group",column="ext_id",required=false)
	private String extId;
	
	@Validation (min=2,max=5,required=false)
	private int nameSearchType;
	
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
	 * @return Returns the associatedIndicator.
	 */
	public int getAssociatedIndicator() {
		return associatedIndicator;
	}
	/**
	 * @param associatedIndicator The associatedIndicator to set.
	 */
	public void setAssociatedIndicator(final int associatedIndicator) {
		this.associatedIndicator = associatedIndicator;
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
	 * @return Returns the screenGroupId.
	 */
	public String getScreenGroupId() {
		return screenGroupId;
	}
	/**
	 * @param screenGroupId The screenGroupId to set.
	 */
	public void setScreenGroupId(final String screenGroupId) {
		this.screenGroupId = screenGroupId;
	}
	/**
	 * @return Returns the typeIndicator.
	 */
	public int getTypeIndicator() {
		return typeIndicator;
	}
	/**
	 * @param typeIndicator The typeIndicator to set.
	 */
	public void setTypeIndicator(final int typeIndicator) {
		this.typeIndicator = typeIndicator;
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
	
}

	
