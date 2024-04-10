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


package com.arahant.services.standard.hrConfig.wizardProjectTemplate;
import com.arahant.business.BProjectTemplateBenefit;


/**
 *
 *
 *
 */
public class SearchWizardProjectTemplatesReturnProjectTemplate {

	public SearchWizardProjectTemplatesReturnProjectTemplate() {}

	private String description;
	private String projectStatus;
	private String projectStatusId;
	private String projectType;
	private String projectTypeId;
	private String projectCategory;
	private String projectCategoryId;
	private String projectTemplateId;
	private String benefitId;
	private String benefitName;
	private String bcrId;
	private String bcrName;
	private String employeeStatusId;
	private String orgGroupId;

	SearchWizardProjectTemplatesReturnProjectTemplate(BProjectTemplateBenefit bene)
	{
			description = bene.getProjectDescription();
			projectStatus = bene.getProjectStatus().getCode();
			projectStatusId = bene.getProjectStatusId();
			projectType = bene.getProjectType().getCode();
			projectTypeId = bene.getProjectTypeId();
			projectCategory = bene.getProjectCategory().getCode();
			projectCategoryId = bene.getProjectCategoryId();
			benefitId = bene.getBenefit() != null ? bene.getBenefitId() : "";
			benefitName = bene.getBenefit() != null ? bene.getBenefit().getName() : "(Any)";
			bcrId = bene.getBcr() != null ? bene.getBcrId() : "";
			bcrName = bene.getBcr() != null ? bene.getBcr().getDescription() : "(Any)";
			projectTemplateId = bene.getBenefitTemplateId();
			orgGroupId = bene.getOrgGroupId();
			employeeStatusId = bene.getEmployeeStatusId();
	}

	public String getEmployeeStatusId() {
		return employeeStatusId;
	}

	public void setEmployeeStatusId(String employeeStatusId) {
		this.employeeStatusId = employeeStatusId;
	}

	public String getOrgGroupId() {
		return orgGroupId;
	}

	public void setOrgGroupId(String orgGroupId) {
		this.orgGroupId = orgGroupId;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProjectCategory() {
		return projectCategory;
	}

	public void setProjectCategory(String projectCategory) {
		this.projectCategory = projectCategory;
	}

	public String getProjectCategoryId() {
		return projectCategoryId;
	}

	public void setProjectCategoryId(String projectCategoryId) {
		this.projectCategoryId = projectCategoryId;
	}

	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

	public String getProjectStatusId() {
		return projectStatusId;
	}

	public void setProjectStatusId(String projectStatusId) {
		this.projectStatusId = projectStatusId;
	}

	public String getProjectTemplateId() {
		return projectTemplateId;
	}

	public void setProjectTemplateId(String projectTemplateId) {
		this.projectTemplateId = projectTemplateId;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getProjectTypeId() {
		return projectTypeId;
	}

	public void setProjectTypeId(String projectTypeId) {
		this.projectTypeId = projectTypeId;
	}
}


