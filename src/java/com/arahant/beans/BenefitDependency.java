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
 * Arahant
 */
@Entity
@Table(name = "benefit_dependency")
public class BenefitDependency extends ArahantBean implements java.io.Serializable, ArahantSaveNotify {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "benefit_dependency";
	// Fields    
	private String benefitDependencyId;
	public static final String BENEFIT_DEPENDENCY_ID = "benefitDependencyId";
	private HrBenefit requiredBenefit;
	public static final String REQUIRED_BENEFIT = "requiredBenefit";
	private String requiredBenefitId;
	;

	public static final String REQUIRED_BENEFIT_ID = "requiredBenefitId";
	private HrBenefit dependentBenefit;
	public static final String DEPENDENT_BENEFIT = "dependentBenefit";
	private String dependentBenefitId;
	;

	public static final String DEPENDENT_BENEFIT_ID = "dependentBenefitId";
	public static final String REQUIRED = "required";
	private char required = 'N';
	public static final String HIDDEN = "hidden";
	private char hidden = 'N';

	// Constructors
	/**
	 * default constructor
	 */
	public BenefitDependency() {
	}

	@Id
	@Column(name = "benefit_dependency_id")
	public String getBenefitDependencyId() {
		return benefitDependencyId;
	}

	public void setBenefitDependencyId(String benefitDependencyId) {
		this.benefitDependencyId = benefitDependencyId;
	}

	@Column(name = "required")
	public char getRequired() {
		return required;
	}

	public void setRequired(char required) {
		this.required = required;
	}

	@Column(name = "hidden")
	public char getHidden() {
		return hidden;
	}

	public void setHidden(char hidden) {
		this.hidden = hidden;
	}

	@ManyToOne
	@JoinColumn(name = "benefit_id")
	public HrBenefit getDependentBenefit() {
		return dependentBenefit;
	}

	public void setDependentBenefit(HrBenefit dependentBenefit) {
		this.dependentBenefit = dependentBenefit;
	}

	@Column(name = "benefit_id", insertable = false, updatable = false)
	public String getDependentBenefitId() {
		return dependentBenefitId;
	}

	public void setDependentBenefitId(String dependentBenefitId) {
		this.dependentBenefitId = dependentBenefitId;
	}

	@ManyToOne
	@JoinColumn(name = "required_benefit_id")
	public HrBenefit getRequiredBenefit() {
		return requiredBenefit;
	}

	public void setRequiredBenefit(HrBenefit requiredBenefit) {
		this.requiredBenefit = requiredBenefit;
	}

	@Column(name = "required_benefit_id", insertable = false, updatable = false)
	public String getRequiredBenefitId() {
		return requiredBenefitId;
	}

	public void setRequiredBenefitId(String requiredBenefitId) {
		this.requiredBenefitId = requiredBenefitId;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "benefit_dependency_id";
	}

	@Override
	public String generateId() throws ArahantException {
		setBenefitDependencyId(IDGenerator.generate(this));
		return benefitDependencyId;
	}

	@Override
	public boolean equals(Object o) {
		if (benefitDependencyId == null && o == null)
			return true;
		if (benefitDependencyId != null && o instanceof BenefitDependency)
			return benefitDependencyId.equals(((BenefitDependency) o).getBenefitDependencyId());

		return false;
	}

	@Override
	public int hashCode() {
		if (benefitDependencyId == null)
			return 0;
		return benefitDependencyId.hashCode();
	}

	@Override
	public String notifyId() {
		return benefitDependencyId;
	}

	@Override
	public String notifyClassName() {
		return "BenefitDependency";
	}
}
