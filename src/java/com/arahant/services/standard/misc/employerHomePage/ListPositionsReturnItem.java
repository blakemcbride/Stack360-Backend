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
package com.arahant.services.standard.misc.employerHomePage;
import com.arahant.business.BHRPosition;


/**
 * 
 *
 *
 */
public class ListPositionsReturnItem {
	
	public ListPositionsReturnItem()
	{
		
	}

	ListPositionsReturnItem (BHRPosition bc)
	{
		
		positionId=bc.getPositionId();
		positionName=bc.getName();
		benefitClassId=bc.getBenefitClassId();
	}
	
	private String positionId;
	private String positionName;
	private String benefitClassId;

	public String getBenefitClassId() {
		return benefitClassId;
	}

	public void setBenefitClassId(String benefitClassId) {
		this.benefitClassId = benefitClassId;
	}


	public String getPositionId()
	{
		return positionId;
	}
	public void setPositionId(String positionId)
	{
		this.positionId=positionId;
	}
	public String getPositionName()
	{
		return positionName;
	}
	public void setPositionName(String positionName)
	{
		this.positionName=positionName;
	}

}

	
