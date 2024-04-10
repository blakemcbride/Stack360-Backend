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
@Table(name = "benefit_change_reason_doc")
public class BenefitChangeReasonDoc extends ArahantBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "benefit_change_reason_doc";
	public static final String BENEFIT_DOCUMENT_ID = "bcrDocumentId";
	private String bcrDocumentId;
	public static final String BENEFIT_CHANGE_REASON = "bcr";
	private HrBenefitChangeReason bcr;
	public static final String BENEFIT_CHANGE_REASON_ID = "bcrId";
	private String bcrId;
	public static final String COMPANY_FORM = "companyForm";
	private CompanyForm companyForm;
	public static final String COMPANY_FORM_ID = "companyFormId";
	private String companyFormId;
	public static final String INSTRUCTIONS = "instructions";
	private String instructions;

	@Id
	@Column(name = "bcr_document_id")
	public String getBcrDocumentId() {
		return bcrDocumentId;
	}

	public void setBcrDocumentId(String bcrDocumentId) {
		this.bcrDocumentId = bcrDocumentId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bcr_id")
	public HrBenefitChangeReason getBcr() {
		return bcr;
	}

	public void setBcr(HrBenefitChangeReason bcr) {
		this.bcr = bcr;
	}

	@Column(name = "bcr_id", insertable = false, updatable = false)
	public String getBcrId() {
		return bcrId;
	}

	public void setBcrId(String bcrId) {
		this.bcrId = bcrId;
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

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	//Default constructor
	public BenefitChangeReasonDoc() {
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "bcr_document_id";
	}

	@Override
	public String generateId() throws ArahantException {
		setBcrDocumentId(IDGenerator.generate(this));
		return bcrDocumentId;
	}

	@Override
	public BenefitChangeReasonDoc clone() {
		BenefitChangeReasonDoc bd = new BenefitChangeReasonDoc();
		bd.generateId();
		bd.setBcrId(bcrId);
		bd.setCompanyFormId(companyFormId);
		bd.setInstructions(instructions);
		return bd;
	}

	@Override
	public boolean equals(Object o) {
		if (bcrDocumentId == null && o == null)
			return true;
		if (bcrDocumentId != null && o instanceof BenefitChangeReasonDoc)
			return bcrDocumentId.equals(((BenefitChangeReasonDoc) o).getBcrDocumentId());

		return false;
	}

	@Override
	public int hashCode() {
		if (bcrDocumentId == null)
			return 0;
		return bcrDocumentId.hashCode();
	}
}
