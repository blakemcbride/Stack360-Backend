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
package com.arahant.services.standard.hr.hrForm;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BPersonForm;
import com.arahant.utils.DateUtils;

import java.io.IOException;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class CopyFormInput extends TransmitInputBase {

	void setData(BPersonForm bc) throws IOException {
		BPersonForm src=new BPersonForm(formId);
		bc.setFormData(src.getImage(), src.getExtension());
		bc.setComments(src.getComments());
		bc.setPersonId(personId);
		bc.setFormTypeId(formTypeId);
		bc.setFormDate(DateUtils.now());
		bc.setExtension(src.getExtension());
	}
	
	@Validation (required=true)
	private String formId;
	@Validation (required=true)
	private String personId;
	@Validation (required=true)
	private String formTypeId;


	public String getFormId()
	{
		return formId;
	}
	public void setFormId(String formId)
	{
		this.formId=formId;
	}
	public String getPersonId()
	{
		return personId;
	}
	public void setPersonId(String personId)
	{
		this.personId=personId;
	}
	public String getFormTypeId()
	{
		return formTypeId;
	}
	public void setFormTypeId(String formTypeId)
	{
		this.formTypeId=formTypeId;
	}

}

	
