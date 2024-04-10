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
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class ListBenefitsInput extends TransmitInputBase {

	@Validation (required=false)
	private String categoryId;
	@Validation (required=false)
	private String benefitId;
	@Validation (required=false)
	private boolean forDecline;
	@Validation (required=true)
	private String personId;

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}


	/**
	 * @return Returns the forDecline.
	 */
	public boolean getForDecline() {
		return forDecline;
	}
	/**
	 * @param forDecline The forDecline to set.
	 */
	public void setForDecline(final boolean forDecline) {
		this.forDecline = forDecline;
	}
	public String getCategoryId()
	{
		return categoryId;
	}
	public void setCategoryId(final String categoryId)
	{
		this.categoryId=categoryId;
	}
	public String getBenefitId()
	{
		return benefitId;
	}
	public void setBenefitId(final String benefitId)
	{
		this.benefitId=benefitId;
	}


}

	
