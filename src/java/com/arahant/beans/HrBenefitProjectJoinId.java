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

import javax.persistence.Column;
import javax.persistence.Embeddable;



@Embeddable
public class HrBenefitProjectJoinId implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "hr_benefit_project_join";

	

	// Fields    

	private String benefitConfigId;

	public static final String BENEFITID = "benefitConfigId";

	private String projectId;

	public static final String PROJECTID = "projectId";

	// Constructors

	/** default constructor */
	public HrBenefitProjectJoinId() {
	}


	/**
	 * @return Returns the benefitConfigId.
	 */
	@Column(name="benefit_config_id")
	public String getBenefitConfigId() {
		return benefitConfigId;
	}


	/**
	 * @param benefitConfigId The benefitConfigId to set.
	 */
	public void setBenefitConfigId(final String benefitConfigId) {
		this.benefitConfigId = benefitConfigId;
	}

	@Column(name="project_id")
	public String getProjectId() {
		return this.projectId;
	}

	public void setProjectId(final String projectId) {
		this.projectId = projectId;
	}

	@Override
	public boolean equals(final Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof HrBenefitProjectJoinId))
			return false;
		final HrBenefitProjectJoinId castOther = (HrBenefitProjectJoinId) other;

		return ((this.getBenefitConfigId() == castOther.getBenefitConfigId()) || (this
				.getBenefitConfigId() != null
				&& castOther.getBenefitConfigId() != null && this.getBenefitConfigId()
				.equals(castOther.getBenefitConfigId())))
				&& ((this.getProjectId() == castOther.getProjectId()) || (this
						.getProjectId() != null
						&& castOther.getProjectId() != null && this
						.getProjectId().equals(castOther.getProjectId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getBenefitConfigId() == null ? 0 : this.getBenefitConfigId().hashCode());
		result = 37 * result
				+ (getProjectId() == null ? 0 : this.getProjectId().hashCode());
		return result;
	}

}
