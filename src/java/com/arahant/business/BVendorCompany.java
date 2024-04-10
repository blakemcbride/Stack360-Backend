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

import com.arahant.beans.*;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.reports.VendorReport;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.util.List;

public class BVendorCompany extends BCompanyBase implements IDBFunctions {

	private static final transient ArahantLogger logger = new ArahantLogger(BVendorCompany.class);
	private VendorCompany vendorCompany;

	public BVendorCompany() {
	}

	/**
	 * @param orgGroupId
	 * @throws ArahantException
	 */
	public BVendorCompany(final String orgGroupId) throws ArahantException {
		internalLoad(orgGroupId);
	}

	/**
	 * @param company
	 * @throws ArahantException
	 */
	public BVendorCompany(final VendorCompany company) throws ArahantException {
		vendorCompany = company;
		initMembers(vendorCompany);
	}

	public BVendorCompany(int interface_id) throws ArahantException {
		load(interface_id);
	}

	public String getExternalId() {
		return vendorCompany.getExternalId();
	}

	@Override
	public VendorCompany getBean() {
		return vendorCompany;
	}

	@Override
	public String getId() {
		return vendorCompany.getOrgGroupId();
	}

	@Override
	public int getOrgGroupType() {
		return VENDOR_TYPE;
	}

	public short getInterfaceId() {
		return vendorCompany.getInterfaceId();
	}

	public void setInterfaceId(short interfaceId) {
		vendorCompany.setInterfaceId(interfaceId);
	}

	@Override
	boolean checkMainContact(final String val) throws ArahantException {
		if (getMainContact() == null) {
			if (isEmpty(val))
				return false;

			logger.debug("Decided to make a new contact because of value of '" + val + "'");

			final BVendorContact be = new BVendorContact();
			be.create();
			setMainContact(be);
		}
		return true;
	}


	/*
	 * (non-Javadoc) @see com.arahant.business.BCompanyBase#getOrgType()
	 */
	@Override
	public String getOrgType() {
		return "Vendor";
	}

	/**
	 * throw new UnsupportedOperationException("Not yet implemented");
	 *
	 * @param hsu
	 * @param vendorCompanyId
	 * @throws ArahantException
	 */
	public static void deleteCompanies(final HibernateSessionUtil hsu, final String[] vendorCompanyId) throws ArahantException {
		for (final String element : vendorCompanyId)
			new BVendorCompany(element).delete();
	}

	@Override
	public String create() throws ArahantException {
		vendorCompany = new VendorCompany();
		company_base = vendorCompany;
		final String id = super.create();
		vendorCompany.setOrgGroupId(id);
		vendorCompany.setOrgGroupType(VENDOR_TYPE);
		vendorCompany.setAssociatedCompany(ArahantSession.getHSU().getCurrentCompany());
		return id;
	}

	private void internalLoad(final String key) throws ArahantException {
		logger.debug("Loading " + key);
		vendorCompany = ArahantSession.getHSU().get(VendorCompany.class, key);
		initMembers(vendorCompany);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
		initMembers(vendorCompany);
	}

	private void load(int interface_id) throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		logger.debug("Loading " + interface_id);
		HibernateCriteriaUtil<VendorCompany> hcu = hsu.createCriteria(VendorCompany.class);
		hcu.eq(VendorCompany.INTERFACE_ID, (short) interface_id);
		vendorCompany = hcu.first();
		initMembers(vendorCompany);
	}

	@Override
	public void update() throws ArahantDeleteException {
		super.update();
		ArahantSession.getHSU().saveOrUpdate(vendorCompany);
	}

	@Override
	public void insert() throws ArahantException {
		company_base.setOwningCompany(null);
		ArahantSession.getHSU().insert(vendorCompany);
		super.insert();
	}

	@Override
	public void delete() throws ArahantDeleteException {
		super.delete();
		ArahantSession.getHSU().delete(vendorCompany);
	}

	/**
	 * @return
	 */
	public String getOrgGroupTypeName() {
		return "Vendor";
	}

	/**
	 * @param hsu
	 * @param name
	 * @param accountNumber
	 * @param glAccount
	 * @param mainContactFirstName
	 * @param mainContactLastName
	 * @param max
	 * @param inactiveDate
	 * @param inactiveDateSearchType
	 * @return
	 * @throws ArahantException
	 */
	public static BVendorCompany[] search(final HibernateSessionUtil hsu, final String name, final String mainContactFirstName,
			final String mainContactLastName, final String identifier, final String glAccountId, final String accountNumber, final int max) throws ArahantException {

		final HibernateCriteriaUtil hcu = hsu.createCriteria(VendorCompany.class);
		if (!isEmpty(accountNumber))
			hcu.like(VendorCompany.ACCOUNTNUMBER, accountNumber);
		if (!isEmpty(glAccountId))
			hcu.joinTo(VendorCompany.GLACCOUNTBYDFLTEXPENSEACCT).eq(GlAccount.GLACCOUNTID, glAccountId);

		hcu.setMaxResults(max);

		hcu.like(OrgGroup.NAME, name);
		hcu.like(CompanyBase.EXTERNAL_REF, identifier);
		hcu.orderBy(OrgGroup.NAME);

		if ((!isEmpty(mainContactFirstName) && !mainContactFirstName.equals("%")) || (!isEmpty(mainContactLastName) && !mainContactLastName.equals("%"))) {
			final HibernateCriteriaUtil joinOrgAssoc = hcu.joinTo(OrgGroup.ORGGROUPASSOCIATIONS);
			final HibernateCriteriaUtil joinPerson = joinOrgAssoc.joinTo(OrgGroupAssociation.PERSON);
			joinPerson.like(Person.FNAME, mainContactFirstName);
			joinPerson.like(Person.LNAME, mainContactLastName);
		}

		final List results = hcu.list();

		final BVendorCompany[] ret = new BVendorCompany[results.size()];
		for (int loop = 0; loop < results.size(); loop++)
			ret[loop] = new BVendorCompany((VendorCompany) results.get(loop));

		return ret;
	}

	/**
	 * @return
	 */
	public String getGLExpenseAccount() {
		if (vendorCompany.getGlAccountByDfltExpenseAcct() != null)
			return vendorCompany.getGlAccountByDfltExpenseAcct().getGlAccountId();
		return "";
	}

	public CompanyDetail getAssociatedCompany() {
		return vendorCompany.getAssociatedCompany();
	}

	/**
	 * @return
	 */
	public String getAccountNumber() {
		return vendorCompany.getAccountNumber();
	}

	/**
	 * @param accountNumber
	 */
	public void setAccountNumber(final String accountNumber) {
		vendorCompany.setAccountNumber(accountNumber);
	}

	/**
	 * @param expenseGLAccountId
	 */
	public void setExpenseGLAccount(final String expenseGLAccountId) {
		vendorCompany.setGlAccountByDfltExpenseAcct(ArahantSession.getHSU().get(GlAccount.class, expenseGLAccountId));
	}

	/**
	 * @param hsu
	 * @throws ArahantException
	 */
	public static String getReport(final HibernateSessionUtil hsu, final boolean addr, final boolean phone, final boolean ident, final boolean contact, final boolean accountNumber, final int sortType, final boolean sortAsc) throws ArahantException {

		final HibernateCriteriaUtil hcu = hsu.createCriteria(VendorCompany.class);

		//0=idenftifer, 1=name, 2=contract
		if (sortAsc)
			switch (sortType) {
				case 0:
					hcu.orderBy(CompanyBase.IDENTIFIER);
					break;
				case 1:
					hcu.orderBy(OrgGroup.NAME);
					break;
			}
		else
			switch (sortType) {
				case 0:
					hcu.orderByDesc(CompanyBase.IDENTIFIER);
					break;
				case 1:
					hcu.orderByDesc(OrgGroup.NAME);
					break;
			}

		final List l = hcu.list();
		final BVendorCompany[] c = new BVendorCompany[l.size()];
		for (int loop = 0; loop < c.length; loop++)
			c[loop] = new BVendorCompany((VendorCompany) l.get(loop));

		final VendorReport vr = new VendorReport();

		return vr.getReport(c, addr, phone, ident, contact, accountNumber, sortType, sortAsc);
	}

	public static BVendorCompany[] listInsuranceProviders(String name, int max) {
		return makeArray(ArahantSession.getHSU().createCriteria(VendorCompany.class).like(VendorCompany.NAME, name).setMaxResults(max).sizeNe(VendorCompany.PROVIDED_BENEFITS, 0).list());
	}

	public static BVendorCompany[] searchVendors(String name, int max) {
		return makeArray(ArahantSession.getHSU().createCriteria(VendorCompany.class).like(VendorCompany.NAME, name).setMaxResults(max).orderBy(VendorCompany.NAME).list());
	}

	public static BVendorCompany[] searchVendors(String name, String[] excludeIds, int max) {
		return makeArray(ArahantSession.getHSU().createCriteria(VendorCompany.class).like(VendorCompany.NAME, name).notIn(VendorCompany.ORGGROUPID, excludeIds).setMaxResults(max).orderBy(VendorCompany.NAME).list());
	}

	public static BVendorCompany[] makeArray(List<VendorCompany> l) {
		BVendorCompany[] ret = new BVendorCompany[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BVendorCompany(l.get(loop));

		return ret;
	}
}
