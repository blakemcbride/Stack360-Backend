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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.services.standard.at.applicant;

import com.arahant.business.BPersonForm;

/**
 *
 */
public class LoadApplicantReturnForms {

	private String id;
	private String comment;
	private int date;
	private String formTypeCode;
	private String formTypeDescription;
	private String formTypeId;
	private String extension;

	public LoadApplicantReturnForms()
	{

	}

	public LoadApplicantReturnForms(BPersonForm f)
	{
		id=f.getId();
		comment=f.getComments();
		date=f.getDate();
		formTypeCode=f.getFormCode();
		formTypeDescription=f.getDescription();
		formTypeId=f.getFormTypeId();
		extension=f.getExtension();

	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}


	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public String getFormTypeCode() {
		return formTypeCode;
	}

	public void setFormTypeCode(String formTypeCode) {
		this.formTypeCode = formTypeCode;
	}

	public String getFormTypeDescription() {
		return formTypeDescription;
	}

	public void setFormTypeDescription(String formTypeDescription) {
		this.formTypeDescription = formTypeDescription;
	}

	public String getFormTypeId() {
		return formTypeId;
	}

	public void setFormTypeId(String formTypeId) {
		this.formTypeId = formTypeId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

}
