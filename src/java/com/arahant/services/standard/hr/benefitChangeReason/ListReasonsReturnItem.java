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
/**
/**
 * 
 */
package com.arahant.services.standard.hr.benefitChangeReason;
import com.arahant.business.BHRBenefitChangeReason;
import com.arahant.utils.DateUtils;


/**
 * 
 *
 *
 */
public class ListReasonsReturnItem {
	
	private String reasonId;
	private String reasonName;
	private String startDateFormatted;
	private String endDateFormatted;
	private String effectiveDateFormatted;
	private String typeName;
	private boolean multipleCompanies;

	public boolean getMultipleCompanies() {
		return multipleCompanies;
	}

	public void setMultipleCompanies(boolean scope) {
		this.multipleCompanies = scope;
	}

	/**
	 * @return Returns the typeName.
	 */
	public String getTypeName() {
		return typeName;
	}
	/**
	 * @param typeName The typeName to set.
	 */
	public void setTypeName(final String typeName) {
		this.typeName = typeName;
	}
	public ListReasonsReturnItem()
	{
		
	}
	/**
	 * @param reason
	 */
	ListReasonsReturnItem(final BHRBenefitChangeReason reason) {
		reasonId=reason.getHrBenefitChangeReasonId();
		reasonName=reason.getDescription();
		startDateFormatted=DateUtils.getDateFormatted(reason.getStartDate());
		endDateFormatted=DateUtils.getDateFormatted(reason.getEndDate());
		effectiveDateFormatted=DateUtils.getDateFormatted(reason.getEffectiveDate());
		typeName=reason.getTypeName();
		multipleCompanies=reason.getAllCompanies();
	}
	public String getReasonId()
	{
		return reasonId;
	}
	public void setReasonId(final String reasonId)
	{
		this.reasonId=reasonId;
	}
	public String getReasonName()
	{
		return reasonName;
	}
	public void setReasonName(final String reasonName)
	{
		this.reasonName=reasonName;
	}
	public String getStartDateFormatted()
	{
		return startDateFormatted;
	}
	public void setStartDateFormatted(final String startDateFormatted)
	{
		this.startDateFormatted=startDateFormatted;
	}
	public String getEndDateFormatted()
	{
		return endDateFormatted;
	}
	public void setEndDateFormatted(final String endDateFormatted)
	{
		this.endDateFormatted=endDateFormatted;
	}
	public String getEffectiveDateFormatted()
	{
		return effectiveDateFormatted;
	}
	public void setEffectiveDateFormatted(final String effectiveDateFormatted)
	{
		this.effectiveDateFormatted=effectiveDateFormatted;
	}
	
	
}

	
