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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.business;

import com.arahant.beans.BenefitDocument;
import com.arahant.beans.CompanyForm;
import com.arahant.beans.HrBenefit;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantJessException;
import com.arahant.exceptions.ArahantSecurityException;
import com.arahant.utils.ArahantSession;
import java.util.List;

/**
 *
 */
public class BBenefitDocument extends SimpleBusinessObjectBase<BenefitDocument> implements IDBFunctions {

    /**
     */
    public BBenefitDocument() {
        super();
    }

	/**
     * @param benefitDocumentId
     * @throws ArahantException
     */
    public BBenefitDocument(final String benefitDocumentId) throws ArahantException {
        super();
        load(benefitDocumentId);
    }

	/**
	 * @param BenefitDocument
	 */
	public BBenefitDocument(final BenefitDocument bd) {
		super();
		bean = bd;
	}

	public String getBenefitDocumentId() {
		return bean.getBenefitDocumentId();
	}

	public void setBenefitDocumentId(String benefitDocumentId) {
		bean.setBenefitDocumentId(benefitDocumentId);
	}

	public HrBenefit getBenefit() {
		return bean.getBenefit();
	}

	public void setBenefit(HrBenefit benefit) {
		bean.setBenefit(benefit);
	}

	public String getBenefitId() {
		return bean.getBenefitId();
	}

	public void setBenefitId(String benefitId) {
		bean.setBenefit(ArahantSession.getHSU().get(HrBenefit.class, benefitId));
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
		}
		catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
	}

	public static void delete(String[] ids) throws ArahantDeleteException, ArahantSecurityException, ArahantJessException {
		for(String id : ids)
			new BBenefitDocument(id).delete();
	}

	@Override
	public String create() throws ArahantException {
        bean = new BenefitDocument();
        bean.generateId();
        return getBenefitDocumentId();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean=ArahantSession.getHSU().get(BenefitDocument.class, key);
	}

	/**
	 * @param name
	 * @return
	 * @throws ArahantException
	 */
	public static BBenefitDocument[] makeArray(final List<BenefitDocument> l) throws ArahantException {

		final BBenefitDocument[] ret = new BBenefitDocument[l.size()];

		for (int loop = 0; loop < ret.length; loop++) {
			ret[loop] = new BBenefitDocument(l.get(loop));
		}

		return ret;
	}

}
