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

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SaveOrgGroupAssociationInput extends TransmitInputBase {

	@Validation (table="org_group_association",column="org_group_id",required=true)
	private String orgGroupId;
	@Validation (table="org_group_association",column="person_id",required=true)
	private String personId;
	@Validation (required=true)
	private boolean supervisor;
    @Validation (table="org_group_association",column="start_date",required=false,type="date")
	private int startDate;
    @Validation (table="org_group_association",column="final_date",required=false,type="date")
	private int finalDate;
	

	public String getOrgGroupId()
	{
		return orgGroupId;
	}
	public void setOrgGroupId(String orgGroupId)
	{
		this.orgGroupId=orgGroupId;
	}
	public String getPersonId()
	{
		return personId;
	}
	public void setPersonId(String personId)
	{
		this.personId=personId;
	}
	public boolean getSupervisor()
	{
		return supervisor;
	}
	public void setSupervisor(boolean supervisor)
	{
		this.supervisor=supervisor;
	}
    public int getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(int finalDate) {
        this.finalDate = finalDate;
    }

    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }
}

	
