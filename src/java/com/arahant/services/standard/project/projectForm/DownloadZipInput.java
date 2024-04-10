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

package com.arahant.services.standard.project.projectForm;

import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;


public class DownloadZipInput extends TransmitInputBase {

	@Validation (required=true)
	private String projectId;
	@Validation(required=true)
	private boolean allFormTypes;
	@Validation(required=true)
	private boolean includeInternal;
	@Validation(required=false)
	private String formType;
 
	public String getProjectId()
	{
		return projectId;
	}
	public void setProjectId(final String projectId)
	{
		this.projectId = projectId;
	}

	public boolean isAllFormTypes() {
		return allFormTypes;
	}

	public void setAllFormTypes(boolean allFormTypes) {
		this.allFormTypes = allFormTypes;
	}

	public boolean isIncludeInternal() {
		return includeInternal;
	}

	public void setIncludeInternal(boolean includeInternal) {
		this.includeInternal = includeInternal;
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}
}

	
