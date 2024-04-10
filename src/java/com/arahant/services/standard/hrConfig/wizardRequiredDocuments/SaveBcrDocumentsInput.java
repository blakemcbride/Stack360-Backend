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
package com.arahant.services.standard.hrConfig.wizardRequiredDocuments;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;


public class SaveBcrDocumentsInput extends TransmitInputBase {

	@Validation (required = false)
	private String companyFormId;
	@Validation (required = false)
	private String instructions;
	@Validation (required = true)
	private String bcrDocumentId;
	

	public String getCompanyFormId()
	{
		return companyFormId;
	}
	public void setCompanyFormId(String companyFormId)
	{
		this.companyFormId = companyFormId;
	}
	public String getInstructions()
	{
		return instructions;
	}
	public void setInstructions(String instructions)
	{
		this.instructions = instructions;
	}
	public String getBcrDocumentId()
	{
		return bcrDocumentId;
	}
	public void setBcrDocumentId(String bcrDocumentId)
	{
		this.bcrDocumentId = bcrDocumentId;
	}


}

	
