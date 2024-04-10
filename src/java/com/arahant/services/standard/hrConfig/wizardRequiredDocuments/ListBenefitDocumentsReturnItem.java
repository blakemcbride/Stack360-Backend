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


/**
 *
 *
 *
 */
public class ListBenefitDocumentsReturnItem   {

	public ListBenefitDocumentsReturnItem() {}

	private String benefitDocumentId;
	private String formName;
	private String formType;
	private String companyFormId;
	private String benefitName;
	private String benefitId;
	private String instructions;

	ListBenefitDocumentsReturnItem(final BBenefitDocument bd) {
		if(bd.getCompanyForm() != null)
		{
			String name = bd.getCompanyForm().getSource();
			name = name.substring(name.lastIndexOf("/") + 1);
			formName = name;
			formType = bd.getCompanyForm().getFormType().getDescription();
		}
		else
		{
			formName = "";
			formType = "";
		}
		benefitDocumentId = bd.getBenefitDocumentId();
		companyFormId = bd.getCompanyFormId();
		benefitName = bd.getBenefit().getName();
		benefitId = bd.getBenefitId();
		instructions = bd.getInstructions();
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getBenefitId() {
		return benefitId;
	}

	public void setBenefitId(String benefitId) {
		this.benefitId = benefitId;
	}

	public String getBenefitName() {
		return benefitName;
	}

	public void setBenefitName(String benefitName) {
		this.benefitName = benefitName;
	}

	public String getBenefitDocumentId() {
		return benefitDocumentId;
	}

	public void setBenefitDocumentId(String benefitDocumentId) {
		this.benefitDocumentId = benefitDocumentId;
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

	
