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
import com.arahant.business.BBenefitChangeReasonDoc;


/**
 *
 *
 *
 */
public class ListBcrDocumentsReturnItem   {

	public ListBcrDocumentsReturnItem() {}

	private String bcrDocumentId;
	private String formName;
	private String formType;
	private String companyFormId;
	private String bcrName;
	private String bcrId;
	private String instructions;

	ListBcrDocumentsReturnItem(final BBenefitChangeReasonDoc bcrd) {
		if(bcrd.getCompanyForm() != null)
		{
			String name = bcrd.getCompanyForm().getSource();
			name = name.substring(name.lastIndexOf("/") + 1);
			formName = name;
			formType = bcrd.getCompanyForm().getFormType().getDescription();
		}
		else
		{
			formName = "";
			formType = "";
		}
		bcrDocumentId = bcrd.getBcrDocumentId();
		companyFormId = bcrd.getCompanyFormId();
		bcrName = bcrd.getBenefitChangeReason().getDescription();
		bcrId = bcrd.getBenefitChangeReasonId();
		instructions = bcrd.getInstructions();
	}

	public String getBcrId() {
		return bcrId;
	}

	public void setBcrId(String bcrId) {
		this.bcrId = bcrId;
	}

	public String getBcrName() {
		return bcrName;
	}

	public void setBcrName(String bcrName) {
		this.bcrName = bcrName;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getBcrDocumentId() {
		return bcrDocumentId;
	}

	public void setBcrDocumentId(String bcrDocumentId) {
		this.bcrDocumentId = bcrDocumentId;
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
}

	
