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

import com.arahant.beans.ImportBenefitJoin;
import com.arahant.beans.ImportedBenefit;
import com.arahant.beans.ImportedEnrollee;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import java.util.List;

public class BImportBenefitJoin extends SimpleBusinessObjectBase<ImportBenefitJoin> {

	public BImportBenefitJoin() {
	}

	public BImportBenefitJoin(String key) {
		super(key);
	}

	public BImportBenefitJoin(ImportBenefitJoin o) {
		bean = o;
	}

	@Override
	public String create() throws ArahantException {
		bean = new ImportBenefitJoin();
		return bean.generateId();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ImportBenefitJoin.class, key);
	}

	public ImportedEnrollee getEnrollee() {
		return bean.getEnrollee();
	}

	public ImportedBenefit getBenefit() {
		return bean.getBenefit();
	}

	void setBenefit(ImportedBenefit bene) {
		bean.setBenefit(bene);
	}

	public int getCoverageStartDate() {
		return bean.getCoverageStartDate();
	}

	void setCoverageStartDate(int coverageStart) {
		bean.setCoverageStartDate(coverageStart);
	}

	public int getCoverageEndDate() {
		return bean.getCoverageEndDate();
	}

	void setCoverageEndDate(int coverageEnd) {
		bean.setCoverageEndDate(coverageEnd);
	}

	void setEnrollee(ImportedEnrollee e) {
		bean.setEnrollee(e);
	}

	void setSubscriber(ImportedEnrollee s) {
		bean.setSubscriber(s);
	}

	public String getBenefitName() {
		return bean.getBenefit().getBenefitName();
	}

	public String getBenefitId() {
		return bean.getBenefit().getImportedBenefitId();
	}

	public static BImportBenefitJoin[] makeArray(List<ImportBenefitJoin> l) throws ArahantException {

		final BImportBenefitJoin[] ret = new BImportBenefitJoin[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BImportBenefitJoin(l.get(loop));

		return ret;

	}
}
