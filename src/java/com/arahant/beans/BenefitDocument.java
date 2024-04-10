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


package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import javax.persistence.*;

/**
 *
 */
@Entity
@Table(name = "benefit_document")
public class BenefitDocument extends ArahantBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "benefit_document";
	public static final String BENEFIT_DOCUMENT_ID = "benefitDocumentId";
	private String benefitDocumentId;
	public static final String BENEFIT = "benefit";
	private HrBenefit benefit;
	public static final String BENEFIT_ID = "benefitId";
	private String benefitId;
	public static final String COMPANY_FORM = "companyForm";
	private CompanyForm companyForm;
	public static final String COMPANY_FORM_ID = "companyFormId";
	private String companyFormId;
	public static final String INSTRUCTIONS = "instructions";
	private String instructions;

	@Id
	@Column(name = "benefit_document_id")
	public String getBenefitDocumentId() {
		return benefitDocumentId;
	}

	public void setBenefitDocumentId(String benefitDocumentId) {
		this.benefitDocumentId = benefitDocumentId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "benefit_id")
	public HrBenefit getBenefit() {
		return benefit;
	}

	public void setBenefit(HrBenefit benefit) {
		this.benefit = benefit;
	}

	@Column(name = "benefit_id", insertable = false, updatable = false)
	public String getBenefitId() {
		return benefitId;
	}

	public void setBenefitId(String benefitId) {
		this.benefitId = benefitId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_form_id")
	public CompanyForm getCompanyForm() {
		return companyForm;
	}

	public void setCompanyForm(CompanyForm companyForm) {
		this.companyForm = companyForm;
	}

	@Column(name = "company_form_id", insertable = false, updatable = false)
	public String getCompanyFormId() {
		return companyFormId;
	}

	public void setCompanyFormId(String companyFormId) {
		this.companyFormId = companyFormId;
	}

	@Column(name = "instructions")
	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	//Default constructor
	public BenefitDocument() {
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "benefit_document_id";
	}

	@Override
	public String generateId() throws ArahantException {
		setBenefitDocumentId(IDGenerator.generate(this));
		return benefitDocumentId;
	}

	@Override
	public BenefitDocument clone() {
		BenefitDocument bd = new BenefitDocument();
		bd.generateId();
		bd.setBenefitId(benefitId);
		bd.setCompanyFormId(companyFormId);
		bd.setInstructions(instructions);
		return bd;
	}

	@Override
	public boolean equals(Object o) {
		if (benefitDocumentId == null && o == null)
			return true;
		if (benefitDocumentId != null && o instanceof BenefitDocument)
			return benefitDocumentId.equals(((BenefitDocument) o).getBenefitDocumentId());

		return false;
	}

	@Override
	public int hashCode() {
		if (benefitDocumentId == null)
			return 0;
		return benefitDocumentId.hashCode();
	}
}
