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
 * Created on Feb 8, 2007
 * 
 */
package com.arahant.services.standard.hr.benefit;
import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchServicesInput extends TransmitInputBase{

	@Validation (required=false)
	private String accountingSystemId; // like clause
	@Validation (table="project",column="description",required=false)
	private String description; // like clause
	@Validation (table="hr_benefit",column="benefit_id",required=false)
	private String benefitId;
	
	@Validation (min=2,max=5,required=false)
	private int accountingSystemIdSearchType;
	@Validation (min=2,max=5,required=false)
	private int descriptionSearchType;

	public String getBenefitId() {
		return benefitId;
	}

	public void setBenefitId(String benefitId) {
		this.benefitId = benefitId;
	}
	
	
	/**
	 * @return Returns the accountingSystemIdSearchType.
	 */
	public int getAccountingSystemIdSearchType() {
		return accountingSystemIdSearchType;
	}
	/**
	 * @param accountingSystemIdSearchType The accountingSystemIdSearchType to set.
	 */
	public void setAccountingSystemIdSearchType(final int accountingSystemIdSearchType) {
		this.accountingSystemIdSearchType = accountingSystemIdSearchType;
	}
	/**
	 * @return Returns the descriptionSearchType.
	 */
	public int getDescriptionSearchType() {
		return descriptionSearchType;
	}
	/**
	 * @param descriptionSearchType The descriptionSearchType to set.
	 */
	public void setDescriptionSearchType(final int descriptionSearchType) {
		this.descriptionSearchType = descriptionSearchType;
	}
	/**
	 * @return Returns the accountingSystemId.
	 */
	public String getAccountingSystemId() {
		return modifyForSearch(accountingSystemId, accountingSystemIdSearchType);
	}
	/**
	 * @param accountingSystemId The accountingSystemId to set.
	 */
	public void setAccountingSystemId(final String accountingSystemId) {
		this.accountingSystemId = accountingSystemId;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return modifyForSearch(description, descriptionSearchType);
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(final String description) {
		this.description = description;
	}
}

	
