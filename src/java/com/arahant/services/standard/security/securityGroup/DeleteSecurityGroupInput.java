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
 * Created on Jun 7, 2007
 * 
 */
package com.arahant.services.standard.security.securityGroup;
import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Jun 7, 2007
 *
 */
public class DeleteSecurityGroupInput extends TransmitInputBase {

@Validation (required=false)
	private String groupIds[];

	/**
	 * @return Returns the groupIds.
	 */
	public String[] getGroupIds() {
            if (groupIds==null)
                return new String[0];
		return groupIds;
	}

	/**
	 * @param groupIds The groupIds to set.
	 */
	public void setGroupIds(final String[] groupIds) {
		this.groupIds = groupIds;
	}
}

	
