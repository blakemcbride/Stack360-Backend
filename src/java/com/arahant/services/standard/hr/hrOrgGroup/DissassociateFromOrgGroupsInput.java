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
 */
package com.arahant.services.standard.hr.hrOrgGroup;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class DissassociateFromOrgGroupsInput extends TransmitInputBase {

	
	@Validation (table="person",column="person_id",required=true)
	private String employeeId;
	@Validation (required=false)
	private String orgGroupIds[];

	public String getEmployeeId()
	{
		return employeeId;
	}
	public void setEmployeeId(final String employeeId)
	{
		this.employeeId=employeeId;
	}
	public String[] getOrgGroupIds()
	{
            if (orgGroupIds==null)
                return new String[0];
		return orgGroupIds;
	}
	public void setOrgGroupIds(final String orgGroupIds[])
	{
		this.orgGroupIds=orgGroupIds;
	}

}

	
