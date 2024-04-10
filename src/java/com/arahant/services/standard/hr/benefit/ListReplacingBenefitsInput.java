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
 *
 */
package com.arahant.services.standard.hr.benefit;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;

public class ListReplacingBenefitsInput extends TransmitInputBase {

	@Validation (required=false)
	private String benefitId;
	@Validation (required=true)
	private String categoryId;
	

	public String getBenefitId()
	{
		return benefitId;
	}
	
	public void setBenefitId(String benefitId)
	{
		this.benefitId=benefitId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

}

	
