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

import com.arahant.beans.GlAccount;
import com.arahant.beans.ProductService;
import com.arahant.beans.Service;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.reports.ProductServiceReport;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import com.itextpdf.text.DocumentException;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;

public class BService extends SimpleBusinessObjectBase<Service> {

	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	private static final transient ArahantLogger logger = new ArahantLogger(BService.class);

	public BService() {
	}

	/**
	 * @param service
	 */
	public BService(final Service service) {
		bean = service;
	}

	/**
	 * @param productId
	 */
	public BService(final String key) {
		super(key);
	}

	public String getAccsysAccount() {
		if (bean.getExpenseAccount() == null)
			return "";
		return bean.getExpenseAccount().getAccountNumber();
	}

	@Override
	public void load(final String key) {
		logger.debug("Loading " + key);
		bean = ArahantSession.getHSU().get(Service.class, key);
	}

	public static BService[] list(final HibernateSessionUtil hsu) {
		final List plist = hsu.createCriteria(Service.class).orderBy(ProductService.DESCRIPTION).list();

		final BService[] pst = new BService[plist.size()];


		int index = 0;

		final Iterator plistItr = plist.iterator();

		while (plistItr.hasNext())
			pst[index++] = new BService((Service) plistItr.next());

		return pst;
	}

	/**
	 * @return @see com.arahant.beans.ProductService#getAccsysId()
	 */
	public String getAccsysId() {
		return bean.getAccsysId();
	}

	/**
	 * @return @see com.arahant.beans.ProductService#getDescription()
	 */
	public String getDescription() {
		return bean.getDescription();
	}

	/**
	 * @return @see com.arahant.beans.ProductService#getProductId()
	 */
	public String getProductId() {
		return bean.getProductId();
	}

	/**
	 * @return @see com.arahant.beans.ProductService#getServiceType()
	 */
	public int getProductType() {
		return bean.getServiceType();
	}

	public void setAccount(GlAccount account) {
		bean.setExpenseAccount(account);
	}

	public void setAccsysAccount(String accsysAccount) {
		setAccsysAccountFromGLAccountId(ArahantSession.getHSU().createCriteria(GlAccount.class).eq(GlAccount.ACCOUNTNUMBER, accsysAccount).first().getGlAccountId());
	}

	public void setAccsysAccountFromGLAccountId(String glAccountId) {
		bean.setExpenseAccount(ArahantSession.getHSU().get(GlAccount.class, glAccountId));
	}

	/**
	 * @param accsysId
	 * @see com.arahant.beans.ProductService#setAccsysId(java.lang.String)
	 */
	public void setAccsysId(final String accsysId) {
		bean.setAccsysId(accsysId);
	}

	/**
	 * @param description
	 * @see com.arahant.beans.ProductService#setDescription(java.lang.String)
	 */
	public void setDescription(final String description) {
		bean.setDescription(description);
	}

	/**
	 * @param productId
	 * @see com.arahant.beans.ProductService#setProductId(java.lang.String)
	 */
	public void setProductId(final String productId) {
		bean.setProductId(productId);
	}

	/**
	 * @param productType
	 * @see com.arahant.beans.ProductService#setServiceType(int)
	 */
	public void setProductType(final int productType) {
		bean.setServiceType(productType);
	}

	public static BService[] search(final HibernateSessionUtil hsu, final String acctId, final String description, final int max) {
		final HibernateCriteriaUtil hcu = hsu.createCriteria(Service.class);

		if (max > 0)
			hcu.setMaxResults(max);

		hcu.like(Service.ACCSYSID, acctId);
		hcu.like(Service.DESCRIPTION, description);
		hcu.orderBy(Service.ACCSYSID);

		final List psl = hcu.list();

		final BService[] pst = new BService[psl.size()];

		for (int loop = 0; loop < pst.length; loop++)
			pst[loop] = new BService((Service) psl.get(loop));

		return pst;
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#create()
	 */
	@Override
	public String create() throws ArahantException {
		bean = new Service();
		bean.setAssociatedCompany(ArahantSession.getHSU().getCurrentCompany());
		bean.generateId();
		return bean.getProductId();
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#delete()
	 */
	@Override
	public void delete() throws ArahantDeleteException {
		ArahantSession.getHSU().delete(bean);
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#insert()
	 */
	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(bean);
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#update()
	 */
	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(bean);
	}

	/**
	 * @param productServiceIds
	 * @throws ArahantDeleteException
	 */
	public static void delete(final String[] productServiceIds) throws ArahantDeleteException {
		for (final String key : productServiceIds)
			new BService(key).delete();
	}

	/**
	 * @param accSysId
	 * @param description
	 * @return
	 * @throws ArahantException
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 */
	public static String getReport(final String accSysId, final String description) throws FileNotFoundException, DocumentException, ArahantException {
		return new ProductServiceReport().build(search(ArahantSession.getHSU(), accSysId, description, -1));
	}

	public BGlAccount getGlAccount() {
		if (bean == null  ||  bean.getExpenseAccount() == null)
			return null;
		else
			return new BGlAccount(bean.getExpenseAccount());
	}
}
