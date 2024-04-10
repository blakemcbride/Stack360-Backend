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
package com.arahant.services.scanner;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BPersonForm;
import com.arahant.utils.Base64;
import com.arahant.utils.DateUtils;

import java.io.IOException;

/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class ReplacePersonFormInput extends TransmitInputBase {

	void setData(BPersonForm bc) throws IOException {
		bc.setFormData(Base64.decode(formData), "pdf");
		bc.setFormDate(DateUtils.now());
		if (formComment!=null && !formComment.trim().isEmpty())
			bc.setComments(formComment);
		bc.setExtension("pdf");
	}
	@Validation (required=false)
	private String formId;
	@Validation (required=false)
	private String formData;
	@Validation (required=false)
	private String formComment;

	public String getFormData() {
		return formData;
	}

	public void setFormData(String formData) {
		this.formData = formData;
	}

	public String getFormComment() {
		return formComment;
	}

	public void setFormComment(String formComment) {
		this.formComment = formComment;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}
/*
	public byte[] getFormData() {
		return formData;
	}

	public void setFormData(byte[] formData) {
		this.formData = formData;
	}
*/
 }

	
