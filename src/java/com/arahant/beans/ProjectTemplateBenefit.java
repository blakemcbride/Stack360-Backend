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
@Table(name = "project_template_benefit")
public class ProjectTemplateBenefit extends ArahantBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "project_template_benefit";
	public static final String BENEFIT_TEMPLATE_ID = "projectTemplateId";
	private String projectTemplateId;
	public static final String BENEFIT = "benefit";
	private HrBenefit benefit;
	public static final String BENEFIT_ID = "benefitId";
	private String benefitId;
	public static final String BCR = "bcr";
	private HrBenefitChangeReason bcr;
	public static final String BCR_ID = "bcrId";
	private String bcrId;
	public static final String PROJECT_STATUS = "projectStatus";
	private ProjectStatus projectStatus;
	public static final String PROJECT_STATUS_ID = "projectStatusId";
	private String projectStatusId;
	public static final String PROJECT_CATEGORY = "projectCategory";
	private ProjectCategory projectCategory;
	public static final String PROJECT_CATEGORY_ID = "projectCategoryId";
	private String projectCategoryId;
	public static final String PROJECT_TYPE = "projectType";
	private ProjectType projectType;
	public static final String PROJECT_TYPE_ID = "projectTypeId";
	private String projectTypeId;
	public static final String PROJECT_DESCRIPTION = "projectDescription";
	private String projectDescription;
	private HrEmployeeStatus employeeStatus;
	private String employeeStatusId;
	private OrgGroup orgGroup;
	private String orgGroupId;
	public static final String EMPLOYEE_STATUS = "employeeStatus";
	public static final String EMPLOYEE_STATUS_ID = "employeeStatusId";
	public static final String ORG_GROUP = "orgGroup";
	public static final String ORG_GROUP_ID = "orgGroupId";

	//Default constructor
	public ProjectTemplateBenefit() {
	}

	@Column(name = "benefit_id", insertable = false, updatable = false)
	public String getBenefitId() {
		return benefitId;
	}

	public void setBenefitId(String benefitId) {
		this.benefitId = benefitId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "benefit_id")
	public HrBenefit getBenefit() {
		return benefit;
	}

	public void setBenefit(HrBenefit benefit) {
		this.benefit = benefit;
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

	@Id
	@Column(name = "project_template_id")
	public String getProjectTemplateId() {
		return projectTemplateId;
	}

	public void setProjectTemplateId(String projectTemplateId) {
		this.projectTemplateId = projectTemplateId;
	}

	@Column(name = "project_category_id", insertable = false, updatable = false)
	public String getProjectCategoryId() {
		return projectCategoryId;
	}

	public void setProjectCategoryId(String projectCategoryId) {
		this.projectCategoryId = projectCategoryId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_category_id")
	public ProjectCategory getProjectCategory() {
		return projectCategory;
	}

	public void setProjectCategory(ProjectCategory projectCategory) {
		this.projectCategory = projectCategory;
	}

	@Column(name = "project_description")
	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	@Column(name = "project_status_id", insertable = false, updatable = false)
	public String getProjectStatusId() {
		return projectStatusId;
	}

	public void setProjectStatusId(String projectStatusId) {
		this.projectStatusId = projectStatusId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_status_id")
	public ProjectStatus getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(ProjectStatus projectStatus) {
		this.projectStatus = projectStatus;
	}

	@Column(name = "project_type_id", insertable = false, updatable = false)
	public String getProjectTypeId() {
		return projectTypeId;
	}

	public void setProjectTypeId(String projectTypeId) {
		this.projectTypeId = projectTypeId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_type_id")
	public ProjectType getProjectType() {
		return projectType;
	}

	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	public HrEmployeeStatus getEmployeeStatus() {
		return employeeStatus;
	}

	public void setEmployeeStatus(HrEmployeeStatus employeeStatus) {
		this.employeeStatus = employeeStatus;
	}

	@Column(name = "status_id", insertable = false, updatable = false)
	public String getEmployeeStatusId() {
		return employeeStatusId;
	}

	public void setEmployeeStatusId(String employeeStatusId) {
		this.employeeStatusId = employeeStatusId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_group_id")
	public OrgGroup getOrgGroup() {
		return orgGroup;
	}

	public void setOrgGroup(OrgGroup orgGroup) {
		this.orgGroup = orgGroup;
	}

	@Column(name = "org_group_id", insertable = false, updatable = false)
	public String getOrgGroupId() {
		return orgGroupId;
	}

	public void setOrgGroupId(String orgGroupId) {
		this.orgGroupId = orgGroupId;
	}

	@Override
	public ProjectTemplateBenefit clone() {
		ProjectTemplateBenefit b = new ProjectTemplateBenefit();
		b.generateId();
		b.setBenefit(benefit);
		b.setProjectCategory(projectCategory);
		b.setProjectDescription(projectDescription);
		b.setProjectStatus(projectStatus);
		b.setProjectType(projectType);

		return b;
	}

	@Override
	public boolean equals(Object o) {
		if (projectTemplateId == null && o == null)
			return true;
		if (projectTemplateId != null && o instanceof ProjectTemplateBenefit)
			return projectTemplateId.equals(((ProjectTemplateBenefit) o).getProjectTemplateId());

		return false;
	}

	@Override
	public int hashCode() {
		if (projectTemplateId == null)
			return 0;
		return projectTemplateId.hashCode();
	}

	@Override
	public String generateId() throws ArahantException {
		setProjectTemplateId(IDGenerator.generate(this));
		return projectTemplateId;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "project_template_id";
	}
}
