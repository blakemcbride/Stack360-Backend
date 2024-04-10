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

import com.arahant.beans.BenefitChangeReasonDoc;
import com.arahant.beans.CompanyForm;
import com.arahant.beans.HrBenefitChangeReason;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantJessException;
import com.arahant.exceptions.ArahantSecurityException;
import com.arahant.utils.ArahantSession;
import java.util.List;

public class BBenefitChangeReasonDoc extends SimpleBusinessObjectBase<BenefitChangeReasonDoc> implements IDBFunctions {

	public BBenefitChangeReasonDoc() {
	}

	public BBenefitChangeReasonDoc(final String bcrDocumentId) throws ArahantException {
		internalLoad(bcrDocumentId);
	}

	public BBenefitChangeReasonDoc(final BenefitChangeReasonDoc bd) {
		bean = bd;
	}

	public String getBcrDocumentId() {
		return bean.getBcrDocumentId();
	}

	public void setBcrDocumentId(String bcrDocumentId) {
		bean.setBcrDocumentId(bcrDocumentId);
	}

	public HrBenefitChangeReason getBenefitChangeReason() {
		return bean.getBcr();
	}

	public void setBenefitChangeReason(HrBenefitChangeReason bcr) {
		bean.setBcr(bcr);
	}

	public String getBenefitChangeReasonId() {
		return bean.getBcrId();
	}

	public void setBenefitChangeReasonId(String bcrId) {
		bean.setBcr(ArahantSession.getHSU().get(HrBenefitChangeReason.class, bcrId));
	}

	public CompanyForm getCompanyForm() {
		return bean.getCompanyForm();
	}

	public void setCompanyForm(CompanyForm companyForm) {
		bean.setCompanyForm(companyForm);
	}

	public String getCompanyFormId() {
		return bean.getCompanyFormId();
	}

	public void setCompanyFormId(String companyFormId) {
		this.setCompanyForm(ArahantSession.getHSU().get(CompanyForm.class, companyFormId));
	}

	public String getInstructions() {
		return bean.getInstructions();
	}

	public void setInstructions(String instructions) {
		bean.setInstructions(instructions);
	}

	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(bean);
	}

	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(bean);
	}

	@Override
	public void delete() throws ArahantDeleteException, ArahantSecurityException, ArahantJessException {
		try {
			ArahantSession.getHSU().delete(bean);
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
	}

	public static void delete(String[] ids) throws ArahantDeleteException, ArahantSecurityException, ArahantJessException {
		for (String id : ids)
			new BBenefitChangeReasonDoc(id).delete();
	}

	@Override
	public String create() throws ArahantException {
		bean = new BenefitChangeReasonDoc();
		bean.generateId();
		return getBcrDocumentId();
	}

	private void internalLoad(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(BenefitChangeReasonDoc.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	public static BBenefitChangeReasonDoc[] makeArray(final List<BenefitChangeReasonDoc> l) throws ArahantException {

		final BBenefitChangeReasonDoc[] ret = new BBenefitChangeReasonDoc[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BBenefitChangeReasonDoc(l.get(loop));

		return ret;
	}
}
