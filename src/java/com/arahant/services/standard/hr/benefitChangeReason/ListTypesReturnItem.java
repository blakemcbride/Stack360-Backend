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
import com.arahant.business.BHRBenefitChangeReason.BenefitChangeReasonType;


/**
 * 
 *
 *
 */
public class ListTypesReturnItem {
	
	public ListTypesReturnItem()
	{
		;
	}

	ListTypesReturnItem (final BenefitChangeReasonType bc)
	{
		typeId=bc.type;
		typeName=bc.name;

	}
	private int typeId;
	private String typeName;
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

	
	
}
