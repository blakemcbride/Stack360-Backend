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
package com.arahant.services.standard.misc.agencyOrgGroup;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Oct 9, 2009
 *
 */
public class AssignPersonToOrgGroupInput extends TransmitInputBase {
	
	@Validation (required=true)
	private String []personIds;
	@Validation (required=true)
	private String groupId;

	/**
	 * @return Returns the groupId.
	 */
	public String getGroupId() {
		return groupId;
	}
	/**
	 * @param groupId The groupId to set.
	 */
	public void setGroupId(final String groupId) {
		this.groupId = groupId;
	}
	/**
	 * @return Returns the personIds.
	 */
	public String[] getPersonIds() {
            if (personIds==null)
                return new String[0];
		return personIds;
	}
	/**
	 * @param personIds The personIds to set.
	 */
	public void setPersonIds(final String[] personIds) {
		this.personIds = personIds;
	}
	

}

	
