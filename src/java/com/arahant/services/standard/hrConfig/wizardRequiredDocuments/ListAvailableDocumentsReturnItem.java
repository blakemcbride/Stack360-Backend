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


package com.arahant.services.standard.hrConfig.wizardRequiredDocuments;
import com.arahant.business.BBenefitDocument;
import com.arahant.business.BCompanyForm;


/**
 *
 *
 *
 */
public class ListAvailableDocumentsReturnItem   {

	public ListAvailableDocumentsReturnItem() {}

	private String formName;
	private String formType;
	private String companyFormId;
	private String comments;

	ListAvailableDocumentsReturnItem(final BCompanyForm form) {
		String name = form.getSource();
		name = name.substring(name.lastIndexOf("/") + 1);

		formName = name;// + form.getExtension();
		formType = form.getFormType().getDescription();
		companyFormId = form.getCompanyFormId();
		comments = form.getComments();
	}

	public String getCompanyFormId() {
		return companyFormId;
	}

	public void setCompanyFormId(String companyFormId) {
		this.companyFormId = companyFormId;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}

	
