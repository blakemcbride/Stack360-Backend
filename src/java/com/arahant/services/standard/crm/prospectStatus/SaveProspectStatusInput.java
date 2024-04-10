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



package com.arahant.services.standard.crm.prospectStatus;

import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;
import com.arahant.business.BProspectStatus;
import com.arahant.utils.ArahantSession;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SaveProspectStatusInput extends TransmitInputBase {
	
	@Validation (min=1,max=16,required=true)
	private String id;
	@Validation (table="prospect_status",column="code",required=true)
	private String code;
	@Validation (table="prospect_status",column="description",required=true)
	private String description;
	@Validation (table="prospect_status", column="certainty", required=false, min=0, max=100)
	private short certainty;
	@Validation (table="prospect_status",column="active",required=true)
	private String active;
	@Validation (required=false)
	private boolean includeInPipeline;
	@Validation (table="prospect_status",column="fallback_days",required=false)
	private short fallbackTime;
	@Validation (table="prospect_status",column="sales_points",required=false)
	private short salesPoints;
	@Validation (required=false)
	private boolean allCompanies;
	@Validation(table = "prospect_status", column = "last_active_date", required = false, type = "date")
	private int lastActiveDate;


	void setData(BProspectStatus bc)
	{
		bc.setCode(code);
		bc.setDescription(description);
		bc.setCertainty(certainty);
		bc.setActive(active);
		bc.setFallbackDays(fallbackTime);
		bc.setShowInSalesPipeline(includeInPipeline?'Y':'N');
		bc.setSalesPoints(salesPoints);
		bc.setLastActiveDate(lastActiveDate);
		if(allCompanies)
			bc.setCompanyId(null);
		else
			bc.setCompanyId(ArahantSession.getHSU().getCurrentCompany().getCompanyId());
	}
	
	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}
	
	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id=id;
	}
	
	public String getCode()
	{
		return code;
	}
	
	public void setCode(String code)
	{
		this.code=code;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description=description;
	}

	public short getCertainty() {
		return certainty;
	}

	public void setCertainty(short certainty) {
		this.certainty = certainty;
	}

	public String getActive()
	{
		return active;
	}
	
	public void setActive(String active)
	{
		this.active=active;
	}

	public short getFallbackTime() {
		return fallbackTime;
	}

	public void setFallbackTime(short fallbackTime) {
		this.fallbackTime = fallbackTime;
	}

	public boolean isIncludeInPipeline() {
		return includeInPipeline;
	}

	public void setIncludeInPipeline(boolean includeInPipeline) {
		this.includeInPipeline = includeInPipeline;
	}

	public short getSalesPoints() {
		return salesPoints;
	}

	public void setSalesPoints(short salesPoints) {
		this.salesPoints = salesPoints;
	}

	public boolean isAllCompanies() {
		return allCompanies;
	}

	public void setAllCompanies(boolean allCompanies) {
		this.allCompanies = allCompanies;
	}
}

	
