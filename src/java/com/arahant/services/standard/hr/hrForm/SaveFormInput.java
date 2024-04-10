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
 *
 *  Created on Feb 8, 2007
*/

package com.arahant.services.standard.hr.hrForm;

import com.arahant.annotation.Validation;

import com.arahant.business.BPersonForm;
import com.arahant.services.TransmitInputBase;

public class SaveFormInput extends TransmitInputBase {

	@Validation (table="person_form",column="person_form_id",required=true)
	private String id;
	@Validation (table="person_form",column="form_type_id",required=true)
	private String formTypeId;
	@Validation (table="person_form",column="comments",required=false)
	private String comments;

	void setData(final BPersonForm bc) {
		bc.setFormTypeId(formTypeId);
		bc.setComments(comments);
	}

	public String getId()
	{
		return id;
	}
	public void setId(final String id)
	{
		this.id=id;
	}
	public String getFormTypeId()
	{
		return formTypeId;
	}
	public void setFormTypeId(final String formTypeId)
	{
		this.formTypeId=formTypeId;
	}
	public String getComments()
	{
		return comments;
	}
	public void setComments(final String comments)
	{
		this.comments=comments;
	}

}

	
