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
package com.arahant.services.standard.site.screen;

import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;

public class SearchScreensInput extends TransmitInputBase {

	@Validation(min = 0, max = 16, required = false)
	private String extId;
	@Validation(table = "screen", column = "filename", required = false)
	private String fileName;
	@Validation(min = 2, max = 5, required = false)
	private int fileNameSearchType;
	@Validation(required = false)
	private boolean includeChild;
	@Validation(required = false)
	private boolean includeParent;
	@Validation(required = false)
	private boolean includeNormal;
	@Validation(required = false)
	private boolean includeWizard;
	@Validation(required = false)
	private boolean includeWizardPage;
	@Validation(table = "screen", column = "name", required = false)
	private String name;
	@Validation(min = 2, max = 5, required = false)
	private int nameSearchType;

	public String getExtId() {
		return extId;
	}

	public void setExtId(String extId) {
		this.extId = extId;
	}

	public String getFileName() {
		return modifyForSearch(fileName, fileNameSearchType);
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getFileNameSearchType() {
		return fileNameSearchType;
	}

	public void setFileNameSearchType(int fileNameSearchType) {
		this.fileNameSearchType = fileNameSearchType;
	}

	public boolean getIncludeChild() {
		return includeChild;
	}

	public void setIncludeChild(boolean includeChild) {
		this.includeChild = includeChild;
	}

	public boolean getIncludeNormal() {
		return includeNormal;
	}

	public void setIncludeNormal(boolean includeNormal) {
		this.includeNormal = includeNormal;
	}

	public boolean getIncludeParent() {
		return includeParent;
	}

	public void setIncludeParent(boolean includeParent) {
		this.includeParent = includeParent;
	}

	public boolean getIncludeWizard() {
		return includeWizard;
	}

	public void setIncludeWizard(boolean includeWizard) {
		this.includeWizard = includeWizard;
	}

	public boolean getIncludeWizardPage() {
		return includeWizardPage;
	}

	public void setIncludeWizardPage(boolean includeWizardPage) {
		this.includeWizardPage = includeWizardPage;
	}

	public String getName() {
		return modifyForSearch(name, nameSearchType);
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNameSearchType() {
		return nameSearchType;
	}

	public void setNameSearchType(int nameSearchType) {
		this.nameSearchType = nameSearchType;
	}

}
