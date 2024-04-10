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
package com.arahant.services.standard.crm.prospectStatus;
import com.arahant.business.BProspectStatus;
import org.kissweb.StringUtils;


/**
 * 
 *
 *
 */
public class ListProspectStatusesReturnItem {
	
	private String id;
	private String code;
	private String description;
	private short certainty;
	private String active;
	private int sequence;
	private boolean includeInPipeline;
	private short fallbackTime;
	private short salesPoints;
	private boolean allCompanies;
	private int lastActiveDate;
	
	public ListProspectStatusesReturnItem()
	{
	}

	ListProspectStatusesReturnItem (BProspectStatus bc)
	{
		id=bc.getId();
		code=bc.getCode();
		description=bc.getDescription();
		certainty=bc.getCertainty();
		active=bc.getActive();
		sequence=bc.getSequence();
		includeInPipeline=bc.getShowInSalesPipeline()=='Y'?true:false;
		fallbackTime=bc.getFallbackDays();
		salesPoints=bc.getSalesPoints();
		allCompanies=StringUtils.isEmpty(bc.getCompanyId());
		lastActiveDate=bc.getLastActiveDate();
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

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
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

	
