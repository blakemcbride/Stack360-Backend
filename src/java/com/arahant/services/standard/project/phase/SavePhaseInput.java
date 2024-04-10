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
package com.arahant.services.standard.project.phase;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BProjectPhase;
import com.arahant.utils.ArahantSession;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SavePhaseInput extends TransmitInputBase {

	void setData(BProjectPhase bc)
	{
		
		bc.setDescription(description);
		bc.setCode(code);
		bc.setId(id);
		bc.setCategoryId(categoryId);
		bc.setLastActiveDate(lastActiveDate);
		if(allCompanies)
			bc.setCompany(null);
		else
			bc.setCompany(ArahantSession.getHSU().getCurrentCompany());
	}
	
	@Validation (required=true)
	private String description;
	@Validation (required=true)
	private String code;
	@Validation (required=true)
	private String id;
	@Validation (required=false)
	private int categoryId;
	@Validation (required=false, type="date")
	private int lastActiveDate;
	@Validation (required=false)
	private boolean allCompanies;

	public boolean getAllCompanies() {
		return allCompanies;
	}

	public void setAllCompanies(boolean allCompanies) {
		this.allCompanies = allCompanies;
	}

	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
	

	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}
	public String getCode()
	{
		return code;
	}
	public void setCode(String code)
	{
		this.code=code;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}

}

	
