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
package com.arahant.services.standard.hr.hrParent;
import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchProjectTypesInput extends TransmitInputBase{

	@Validation (table="project_type",column="code",required=false)
	private String code; // like clause
	@Validation (table="project_type",column="description",required=false)
	private String description; // like clause
	
	@Validation (min=2,max=5,required=false)
	private int codeSearchType;
	@Validation (min=2,max=5,required=false)
	private int descriptionSearchType;
	
	/**
	 * @return Returns the codeSearchType.
	 */
	public int getCodeSearchType() {
		return codeSearchType;
	}
	/**
	 * @param codeSearchType The codeSearchType to set.
	 */
	public void setCodeSearchType(final int codeSearchType) {
		this.codeSearchType = codeSearchType;
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
	 * @return Returns the code.
	 */
	public String getCode() {
		return modifyForSearch(code, codeSearchType);
	}
	/**
	 * @param code The code to set.
	 */
	public void setCode(final String code) {
		this.code = code;
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

	
