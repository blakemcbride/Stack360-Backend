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

import com.arahant.beans.HrNoteCategory;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.util.List;

public class BHRNoteCategory extends BusinessLogicBase implements IDBFunctions {

	private HrNoteCategory hrNoteCategory;

	public BHRNoteCategory() {
	}

	/**
	 * @param key String
	 * @throws ArahantException
	 */
	public BHRNoteCategory(final String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 * @param account
	 */
	public BHRNoteCategory(final HrNoteCategory account) {
		hrNoteCategory = account;
	}

	@Override
	public String create() throws ArahantException {
		hrNoteCategory = new HrNoteCategory();
		hrNoteCategory.setOrgGroup(ArahantSession.getHSU().getCurrentCompany());
		hrNoteCategory.generateId();
		return getNoteCategoryId();
	}

	@Override
	public void delete() throws ArahantDeleteException {
		ArahantSession.getHSU().delete(hrNoteCategory);
	}

	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(hrNoteCategory);
	}

	private void internalLoad(final String key) throws ArahantException {
		hrNoteCategory = ArahantSession.getHSU().get(HrNoteCategory.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	public void setFirstActiveDate(int firstActiveDate) {
		hrNoteCategory.setFirstActiveDate(firstActiveDate);
	}

	public void setLastActiveDate(int lastActiveDate) {
		hrNoteCategory.setLastActiveDate(lastActiveDate);
	}

	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(hrNoteCategory);
	}

	public String getNoteCategoryId() {
		return hrNoteCategory.getCatId();
	}

	public String getName() {
		return hrNoteCategory.getName();
	}

	public int getFirstActiveDate() {
		return hrNoteCategory.getFirstActiveDate();
	}

	public int getLastActiveDate() {
		return hrNoteCategory.getLastActiveDate();
	}

	public void setNoteCategoryId(final String NoteCategoryId) {
		hrNoteCategory.setCatId(NoteCategoryId);
	}

	public void setName(final String name) {
		hrNoteCategory.setName(name);
	}

	public String getCatCode() {
	    return hrNoteCategory.getCatCode();
    }

    public void setCatCode(String code) {
	    hrNoteCategory.setCatCode(code);
    }

	/**
	 * @param hsu
	 * @param ids
	 * @throws ArahantException
	 */
	public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantException {
		for (final String element : ids)
			new BHRNoteCategory(element).delete();
	}

	/**
	 * @param hsu
	 * @return
	 */
	public static BHRNoteCategory[] list(final HibernateSessionUtil hsu) {
		return makeArray(hsu.createCriteria(HrNoteCategory.class)
				.orderBy(HrNoteCategory.NAME)
				.dateInside(HrNoteCategory.FIRSTACTIVEDATE, HrNoteCategory.LASTACTIVEDATE, DateUtils.now())
				.list());
	}

	public static BHRNoteCategory[] list(int activeType) {
		HibernateCriteriaUtil<HrNoteCategory> hcu = ArahantSession.getHSU()
				.createCriteria(HrNoteCategory.class)
				.orderBy(HrNoteCategory.NAME);

		if (activeType == 1)
			hcu.dateInside(HrNoteCategory.FIRSTACTIVEDATE, HrNoteCategory.LASTACTIVEDATE, DateUtils.now());
		if (activeType == 2)
			hcu.dateOutside(HrNoteCategory.FIRSTACTIVEDATE, HrNoteCategory.LASTACTIVEDATE, DateUtils.now());

		return makeArray(hcu.list());
	}

	static BHRNoteCategory[] makeArray(List<HrNoteCategory> l) {
		final BHRNoteCategory[] ret = new BHRNoteCategory[l.size()];
		for (int loop = 0; loop < l.size(); loop++)
			ret[loop] = new BHRNoteCategory(l.get(loop));
		return ret;
	}

	public static String findOrMake(String string) {
		HrNoteCategory cat = ArahantSession.getHSU().createCriteria(HrNoteCategory.class).eq(HrNoteCategory.NAME, string).first();
		if (cat == null) {
			BHRNoteCategory newCat = new BHRNoteCategory();
			newCat.create();
			newCat.setName(string);
			newCat.insert();
			return newCat.getNoteCategoryId();
		}
		return cat.getCatId();
	}
}
