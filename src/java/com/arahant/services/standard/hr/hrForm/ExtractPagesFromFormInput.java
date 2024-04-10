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

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BPersonForm;
import com.arahant.utils.DateUtils;

import java.io.IOException;

public class ExtractPagesFromFormInput extends TransmitInputBase {

	@Validation (required=true)
	private String formId;
	@Validation (required=true)
	private String formTypeId;
	@Validation (min=1,required=true)
	private int [] pageNumbers;

	void setData(BPersonForm bc) throws IOException {
		BPersonForm bpf=new BPersonForm(formId);
		bc.setPersonId(bpf.getPersonId());
		bc.setComments(bpf.getComments());
		bc.setFormDate(DateUtils.now());
		bc.setFormTypeId(formTypeId);
		bc.setFormData(bpf.extractPages(pageNumbers), bpf.getExtension());
		bc.setExtension(bpf.getExtension());
	}

	public String getFormId()
	{
		return formId;
	}

	public void setFormId(String formId)
	{
		this.formId=formId;
	}

	public String getFormTypeId()
	{
		return formTypeId;
	}

	public void setFormTypeId(String formTypeId)
	{
		this.formTypeId=formTypeId;
	}

	public int [] getPageNumbers()
	{
		return pageNumbers;
	}

	public void setPageNumbers(int [] pageNumbers)
	{
		this.pageNumbers=pageNumbers;
	}

}

	
