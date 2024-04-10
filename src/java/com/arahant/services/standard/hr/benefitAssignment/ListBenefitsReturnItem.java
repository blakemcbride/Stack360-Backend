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
package com.arahant.services.standard.hr.benefitAssignment;
import com.arahant.business.BHRBenefit;


/**
 * 
 *
 *
 */
public class ListBenefitsReturnItem {
	
	public ListBenefitsReturnItem()
	{
		;
	}

	ListBenefitsReturnItem (final BHRBenefit bc)
	{
		
		benefitId=bc.getBenefitId();
		benefitName=bc.getName();
		timeRelated=bc.getTimeRelated();
		coveredUnderCOBRA=bc.getCoveredUnderCOBRA();
	}
	
	private String benefitId;
	private String benefitName;
	private boolean timeRelated;
	private boolean coveredUnderCOBRA;

	public String getBenefitId()
	{
		return benefitId;
	}
	public void setBenefitId(final String benefitId)
	{
		this.benefitId=benefitId;
	}
	public String getBenefitName()
	{
		return benefitName;
	}
	public void setBenefitName(final String benefitName)
	{
		this.benefitName=benefitName;
	}
	public boolean getTimeRelated()
	{
		return timeRelated;
	}
	public void setTimeRelated(final boolean timeRelated)
	{
		this.timeRelated=timeRelated;
	}
	public boolean getCoveredUnderCOBRA()
	{
		return coveredUnderCOBRA;
	}
	public void setCoveredUnderCOBRA(final boolean coveredUnderCOBRA)
	{
		this.coveredUnderCOBRA=coveredUnderCOBRA;
	}

}

	
