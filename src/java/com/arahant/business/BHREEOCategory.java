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

import com.arahant.beans.HrEeoCategory;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.reports.HREEOCategoryReport;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.HibernateSessionUtil;
import java.io.File;
import java.util.List;

public class BHREEOCategory extends BusinessLogicBase implements IDBFunctions {

	private HrEeoCategory hrEeoCategory;

	public BHREEOCategory() {
	}

	/**
	 * @param key
	 * @throws ArahantException
	 */
	public BHREEOCategory(final String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 * @param account
	 */
	public BHREEOCategory(final HrEeoCategory account) {
		hrEeoCategory = account;
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#create()
	 */
	@Override
	public String create() throws ArahantException {
		hrEeoCategory = new HrEeoCategory();
		hrEeoCategory.generateId();

		return getEeoCategoryId();
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#delete()
	 */
	@Override
	public void delete() throws ArahantDeleteException {
		ArahantSession.getHSU().delete(hrEeoCategory);
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#insert()
	 */
	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(hrEeoCategory);
	}

	private void internalLoad(final String key) throws ArahantException {
		hrEeoCategory = ArahantSession.getHSU().get(HrEeoCategory.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(hrEeoCategory);
	}

	public String getEeoCategoryId() {
		return hrEeoCategory.getEeoCategoryId();
	}

	public String getName() {
		return hrEeoCategory.getName();
	}

	/**
	 * @param EeoCategoryId
	 * @see com.arahant.beans.HrEeoCategory#setEeoCategoryId(java.lang.String)
	 */
	public void setEeoCategoryId(final String EeoCategoryId) {
		hrEeoCategory.setEeoCategoryId(EeoCategoryId);
	}

	/**
	 * @param name
	 * @see com.arahant.beans.HrEeoCategory#setName(java.lang.String)
	 */
	public void setName(final String name) {
		hrEeoCategory.setName(name);
	}

	/**
	 * @param hsu
	 * @param ids
	 * @throws ArahantException
	 */
	public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantException {
		for (final String element : ids)
			new BHREEOCategory(element).delete();
	}

	/**
	 * @param hsu
	 * @return
	 */
	public static BHREEOCategory[] list(final HibernateSessionUtil hsu) {
		return makeArray(hsu.createCriteria(HrEeoCategory.class).orderBy(HrEeoCategory.NAME).list());
	}

	/**
	 * @param l
	 * @return
	 */
	static BHREEOCategory[] makeArray(final List<HrEeoCategory> l) {
		final BHREEOCategory[] ret = new BHREEOCategory[l.size()];
		for (int loop = 0; loop < l.size(); loop++)
			ret[loop] = new BHREEOCategory(l.get(loop));
		return ret;
	}

	/**
	 * @return
	 */
	public static String getReport(final HibernateSessionUtil hsu) throws Exception {
		final File fyle = FileSystemUtils.createTempFile("HREEOCatRept", ".pdf");

		new HREEOCategoryReport().build(hsu, fyle);

		return FileSystemUtils.getHTTPPath(fyle);
	}

	public HrEeoCategory getBean() {
		return hrEeoCategory;
	}
}
