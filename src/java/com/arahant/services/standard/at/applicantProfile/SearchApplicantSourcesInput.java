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
package com.arahant.services.standard.at.applicantProfile;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchApplicantSourcesInput extends TransmitInputBase {

	@Validation (table="applicant_source",column="description",required=false)
	private String description;
	@Validation (min=2,max=5,required=false)
	private int descriptionSearchType;
	@Validation (min=0,max=16,required=false)
	private String id;

	public String getDescription()
	{
		return modifyForSearch(description,descriptionSearchType);
	}
	public void setDescription(String description)
	{
		this.description=description;
	}
	public int getDescriptionSearchType()
	{
		return descriptionSearchType;
	}
	public void setDescriptionSearchType(int descriptionSearchType)
	{
		this.descriptionSearchType=descriptionSearchType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


}

	
