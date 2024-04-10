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

import com.arahant.beans.LocationCost;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.List;

public class BLocationCost extends SimpleBusinessObjectBase<LocationCost> {

	public BLocationCost() {
	}

	public BLocationCost(final LocationCost bean) {
		this.bean = bean;
	}

	public BLocationCost(final String key) throws ArahantException {
		internalLoad(key);
	}

	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().update(bean);
	}

	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(bean);
	}

	@Override
	public String create() throws ArahantException {
		bean = new LocationCost();
		bean.generateId();
		return getLocationCostId();
	}

	private void internalLoad(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(LocationCost.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	public static void delete(final String[] locationCostIds) throws ArahantException {
		for (String s : locationCostIds)
			new BLocationCost(s).delete();
	}

	@Override
	public void delete() throws ArahantDeleteException {
		try {
			ArahantSession.getHSU().delete(bean);
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
	}

	public String getDescription() {
		return bean.getDescription();
	}

	public void setDescription(String description) {
		bean.setDescription(description);
	}

	public double getLocationCost() {
		return bean.getLocationCost();
	}

	public void setLocationCost(double locationCost) {
		bean.setLocationCost(locationCost);
	}

	public String getLocationCostId() {
		return bean.getLocationCostId();
	}

	public void setLocationCostId(String locationCostId) {
		bean.setLocationCostId(locationCostId);
	}

	public static BLocationCost[] searchLocations(final int max, final String name) {
		HibernateCriteriaUtil<LocationCost> hcu = ArahantSession.getHSU().createCriteria(LocationCost.class).setMaxResults(max).orderBy(LocationCost.DESCRIPTION);

		if (!isEmpty(name))
			hcu.like(LocationCost.DESCRIPTION, name);

		return makeArray(hcu.list());
	}

	private static BLocationCost[] makeArray(List<LocationCost> l) {
		final BLocationCost[] ret = new BLocationCost[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BLocationCost(l.get(loop));
		return ret;
	}
}
