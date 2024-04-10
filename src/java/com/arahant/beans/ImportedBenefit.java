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

package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 */
@Entity
@Table(name=ImportedBenefit.TABLE_NAME)
public class ImportedBenefit extends Filtered implements Serializable {
	public static final String TABLE_NAME="drc_import_benefit";

	public static final String NAME="benefitName";
	public static final String IMPORT_FILE_TYPE="importFileType";
	public static final String ID="importedBenefitId";
	public static final String BENEFIT_JOINS="benefitJoins";

	private String importedBenefitId;
	private String benefitName;
	private ImportType importFileType;

	private Set<ImportBenefitJoin> benefitJoins=new HashSet<ImportBenefitJoin>();

	@OneToMany
	@JoinColumn(name="import_benefit_id")
	public Set<ImportBenefitJoin> getBenefitJoins() {
		return benefitJoins;
	}

	public void setBenefitJoins(Set<ImportBenefitJoin> benefitJoins) {
		this.benefitJoins = benefitJoins;
	}


	@ManyToOne
	@JoinColumn(name="import_file_type_id")
	public ImportType getImportFileType() {
		return importFileType;
	}

	public void setImportFileType(ImportType importFileType) {
		this.importFileType = importFileType;
	}

	@Id
	@Column(name="imported_benefit_id")
	public String getImportedBenefitId() {
		return importedBenefitId;
	}

	public void setImportedBenefitId(String importedBenefitId) {
		this.importedBenefitId = importedBenefitId;
	}

	@Column(name="benefit_name")
	public String getBenefitName() {
		return benefitName;
	}

	public void setBenefitName(String benefitName) {
		this.benefitName = benefitName;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "imported_benefit_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return importedBenefitId=IDGenerator.generate(this);
	}

	@Override
	@ManyToOne
	@JoinColumn(name="company_id")
	public OrgGroup getOrgGroup() {
		return orgGroup;
	}




}
