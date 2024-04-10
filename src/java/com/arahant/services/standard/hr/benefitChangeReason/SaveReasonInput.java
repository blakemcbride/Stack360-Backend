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
import com.arahant.annotation.Validation;

import com.arahant.business.BHRBenefitChangeReason;
import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SaveReasonInput extends TransmitInputBase {

	void setData(final BHRBenefitChangeReason bc)
	{
		
		bc.setDescription(reasonName);
		bc.setStartDate(startDate);
		bc.setEndDate(endDate);
		bc.setEffectiveDate(effectiveDate);
		bc.setTypeId(typeId);
		bc.getBean().setEventType(eventId);
		bc.setAllCompanies(multipleCompanies);
		bc.setInstructions(instructions);

	}
	
	@Validation (required=true)
	private String reasonName;
	@Validation (min=19000101,max=30000101,type="date",required=false)
	private int startDate;
	@Validation (min=19000101,max=30000101,type="date",required=false)
	private int endDate;
	@Validation (min=19000101,max=30000101,type="date",required=false)
	private int effectiveDate;
	@Validation (required=true)
	private String reasonId;
	@Validation (min=1,max=4,required=true)
	private int typeId;
	@Validation (required=false)
	private short eventId;
	@Validation (required=false)
	private boolean multipleCompanies;
	@Validation (required=false)
	private String instructions;

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public boolean getMultipleCompanies() {
		return multipleCompanies;
	}

	public void setMultipleCompanies(boolean multipleCompanies) {
		this.multipleCompanies = multipleCompanies;
	}

	/**
	 * @return Returns the typeId.
	 */
	public int getTypeId() {
		return typeId;
	}
	/**
	 * @param typeId The typeId to set.
	 */
	public void setTypeId(final int typeId) {
		this.typeId = typeId;
	}
	public String getReasonName()
	{
		return reasonName;
	}
	public void setReasonName(final String reasonName)
	{
		this.reasonName=reasonName;
	}
	public int getStartDate()
	{
		return startDate;
	}
	public void setStartDate(final int startDate)
	{
		this.startDate=startDate;
	}
	public int getEndDate()
	{
		return endDate;
	}
	public void setEndDate(final int endDate)
	{
		this.endDate=endDate;
	}
	public int getEffectiveDate()
	{
		return effectiveDate;
	}
	public void setEffectiveDate(final int effectiveDate)
	{
		this.effectiveDate=effectiveDate;
	}
	public String getReasonId()
	{
		return reasonId;
	}
	public void setReasonId(final String reasonId)
	{
		this.reasonId=reasonId;
	}

    /**
     * @return the eventId
     */
    public short getEventId() {
        return eventId;
    }

    /**
     * @param eventId the eventId to set
     */
    public void setEventId(short eventId) {
        this.eventId = eventId;
    }
 
}
