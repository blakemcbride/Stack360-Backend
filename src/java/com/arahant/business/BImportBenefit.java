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
package com.arahant.business;

import com.arahant.beans.ImportType;
import com.arahant.beans.ImportedBenefit;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;

public class BImportBenefit extends SimpleBusinessObjectBase<ImportedBenefit> {

	public BImportBenefit(String id) {
		super(id);
	}

	public BImportBenefit() {
	}

	public BImportBenefit(ImportedBenefit b) {
		bean = b;
	}

	@Override
	public String create() throws ArahantException {
		bean = new ImportedBenefit();
		return bean.generateId();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ImportedBenefit.class, key);
	}

	public String getId() {
		return bean.getImportedBenefitId();
	}

	public void setBenefitName(String benefitName) {
		bean.setBenefitName(benefitName);
	}

	public void setType(ImportType type) {
		bean.setImportFileType(type);
	}

	public ImportType getImportType() {
		return bean.getImportFileType();
	}
}
