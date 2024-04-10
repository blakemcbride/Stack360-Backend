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
package com.arahant.services.standard.hr.mailingLabelListReport;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class GetReportInput extends TransmitInputBase {

	@Validation (required=false)
	private String []statusIds;
	@Validation (required=false)
	private String []benefitIds;
	@Validation (min=1,required=true)
	private String []orgGroupIds;
	@Validation (required=true)
	private boolean includeEmployeeId;

	public String[] getStatusIds()
	{
		if (statusIds==null)
			return new String[0];
		return statusIds;
	}
	public void setStatusIds(String[] statusIds)
	{
		this.statusIds=statusIds;
	}

	public String[] getBenefitIds() {
		if (benefitIds==null)
			return new String[0];
		return benefitIds;
	}

	public void setBenefitIds(String[] benefitIds) {
		this.benefitIds = benefitIds;
	}

	public boolean isIncludeEmployeeId() {
		return includeEmployeeId;
	}

	public void setIncludeEmployeeId(boolean includeEmployeeId) {
		this.includeEmployeeId = includeEmployeeId;
	}

	public String[] getOrgGroupIds() {
		if (orgGroupIds==null)
			return new String[0];
		return orgGroupIds;
	}

	public void setOrgGroupIds(String[] orgGroupIds) {
		this.orgGroupIds = orgGroupIds;
	}


}

	
