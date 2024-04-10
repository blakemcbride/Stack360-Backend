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
 * Created on May 14, 2007
 * 
 */
package com.arahant.services.standard.hrConfig.wizardConfigurator;
import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;
import com.arahant.utils.IDGenerator;


/**
 * 
 *
 * Created on May 14, 2007
 *
 */
public class SearchScreensInput extends TransmitInputBase {

	@Validation (table="screen",column="name",required=false)
	private String name;
	@Validation (min=2,max=5,required=false)
	private int nameSearchType;
	@Validation (table="screen_group",column="ext_id",required=false)
        private String extId;

    public String getExtId() {
        return (extId==null || extId.length()==0) ? extId : IDGenerator.expandKey(extId);
    }


    public void setExtId(String extId) {
        this.extId = extId;
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
}

	
