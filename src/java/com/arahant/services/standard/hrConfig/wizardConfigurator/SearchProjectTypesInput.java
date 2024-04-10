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


/**
 * 
 */
package com.arahant.services.standard.hrConfig.wizardConfigurator;

import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;

/**
 * 
 *
 * Created on jan 1, 2008
 *
 */
public class SearchProjectTypesInput extends TransmitInputBase {

    @Validation(table = "project_type", column = "code", required = false)
    private String code;
    @Validation(table = "project_type", column = "description", required = false)
    private String description;
    @Validation(min = 2, max = 5, required = false)
    private int codeSearchType;
    @Validation(min = 2, max = 5, required = false)
    private int descriptionSearchType;
    @Validation (table="wizard_configuration", column="wizard_configuration_id", required=false)
	private String wizardConfigurationId;
    @Validation(required = false)
    private String projectId;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

	public String getWizardConfigurationId() {
		return wizardConfigurationId;
	}

	public void setWizardConfigurationId(String wizardConfigurationId) {
		this.wizardConfigurationId = wizardConfigurationId;
	}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCodeSearchType() {
        return codeSearchType;
    }

    public void setCodeSearchType(int codeSearchType) {
        this.codeSearchType = codeSearchType;
    }

    public int getDescriptionSearchType() {
        return descriptionSearchType;
    }

    public void setDescriptionSearchType(int descriptionSearchType) {
        this.descriptionSearchType = descriptionSearchType;
    }
}

	
