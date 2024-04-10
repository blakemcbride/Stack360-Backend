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
package com.arahant.services.standard.site.companySecurityOverride;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BProphetLoginOverride;
import com.arahant.annotation.Validation;

public class SaveCompanyOverrideInput extends TransmitInputBase {

	void setData(BProphetLoginOverride bc)
	{
		bc.setCompanyId(companyId);
		bc.setScreenGroupId(screenGroupId);
		bc.setSecurityGroupId(securityGroupId);
	}
	
	@Validation (required=true)
	private String loginExceptionId;
	@Validation (required=true)
	private String companyId;
	@Validation (required=false)
	private String screenGroupId;
	@Validation (required=false)
	private String securityGroupId;
	

	public String getLoginExceptionId()
	{
		return loginExceptionId;
	}
	public void setLoginExceptionId(String loginExceptionId)
	{
		this.loginExceptionId=loginExceptionId;
	}
	public String getCompanyId()
	{
		return companyId;
	}
	public void setCompanyId(String companyId)
	{
		this.companyId=companyId;
	}
	public String getScreenGroupId()
	{
		return screenGroupId;
	}
	public void setScreenGroupId(String screenGroupId)
	{
		this.screenGroupId=screenGroupId;
	}
	public String getSecurityGroupId()
	{
		return securityGroupId;
	}
	public void setSecurityGroupId(String securityGroupId)
	{
		this.securityGroupId=securityGroupId;
	}

}

	
