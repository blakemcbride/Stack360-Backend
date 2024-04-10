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
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 *
 */
public class LoadReasonReturn extends TransmitReturnBase {

	void setData(final BHRBenefitChangeReason bc)
	{
		
		reasonName=bc.getDescription();
		startDate=bc.getStartDate();
		endDate=bc.getEndDate();
		effectiveDate=bc.getEffectiveDate();
		typeId=bc.getTypeId();
		eventId = bc.getBean().getEventType();
		multipleCompanies = bc.getAllCompanies();
		instructions = bc.getInstructions();
	}
	
	private String reasonName;
	private int startDate;
	private int endDate;
	private int effectiveDate;
	private int typeId;
	private short eventId;
	private boolean multipleCompanies;
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
