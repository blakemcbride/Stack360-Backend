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

import com.arahant.beans.HrEvalCategory;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.util.List;

public class BHREvalCategory extends SimpleBusinessObjectBase<HrEvalCategory> {

	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	private static final ArahantLogger logger = new ArahantLogger(BHREvalCategory.class);

	public BHREvalCategory() {
		logger.debug("Created");
	}

	public BHREvalCategory(final String key) throws ArahantException {
		super(key);
	}

	public BHREvalCategory(final HrEvalCategory account) {
		bean = account;
	}

	@Override
	public String create() throws ArahantException {
		bean = new HrEvalCategory();
		bean.setOrgGroup(ArahantSession.getHSU().getCurrentCompany());
		return bean.generateId();
	}

	public int getInactiveDate() {
		return bean.getLastActiveDate();
	}

	public int getFirstActiveDate() {
		return bean.getFirstActiveDate();
	}

	public int getLastActiveDate() {
		return bean.getLastActiveDate();
	}

	@Override
	public void load(final String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(HrEvalCategory.class, key);
	}

	public void setFirstActiveDate(int firstActiveDate) {
		bean.setFirstActiveDate(firstActiveDate);
	}

	public void setInactiveDate(int inactiveDate) {
		bean.setLastActiveDate(inactiveDate);
	}

	public String getEvalCategoryId() {
		return bean.getEvalCatId();
	}

	public String getName() {
		return bean.getName();
	}

	public void setEvalCategoryId(final String EvalCategoryId) {
		bean.setEvalCatId(EvalCategoryId);
	}

	public void setLastActiveDate(int lastActiveDate) {
		bean.setLastActiveDate(lastActiveDate);
	}

	public void setName(final String name) {
		bean.setName(name);
	}

	public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantException {
		for (final String element : ids)
			new BHREvalCategory(element).delete();
	}

	public static BHREvalCategory[] list(int activeType) {
		HibernateCriteriaUtil<HrEvalCategory> hcu = ArahantSession.getHSU()
				.createCriteria(HrEvalCategory.class)
				.orderBy(HrEvalCategory.NAME);

		if (activeType == 1) //actives
			hcu.dateInside(HrEvalCategory.FIRST_ACTIVE_DATE, HrEvalCategory.LAST_ACTIVE_DATE, DateUtils.now());
		else if (activeType == 2) //inactives
			hcu.dateOutside(HrEvalCategory.FIRST_ACTIVE_DATE, HrEvalCategory.LAST_ACTIVE_DATE, DateUtils.now());

		final List<HrEvalCategory> l = hcu.list();
		final BHREvalCategory[] ret = new BHREvalCategory[l.size()];
		for (int loop = 0; loop < l.size(); loop++)
			ret[loop] = new BHREvalCategory(l.get(loop));
		return ret;
	}

	public short getWeight() {
		return bean.getWeight();
	}

	public void setWeight(final short weight) {
		bean.setWeight(weight);
	}

	public String getDescription() {
		return bean.getDescription();
	}

	public void setDescription(final String description) {
		bean.setDescription(description);
	}
}
