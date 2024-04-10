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

import com.arahant.beans.Employee;
import com.arahant.beans.ProspectCompany;
import com.arahant.beans.ProspectStatus;
import com.arahant.beans.SalesPoints;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import java.util.Date;
import java.util.List;

public class BSalesPoints extends SimpleBusinessObjectBase<SalesPoints> {

	public BSalesPoints() {
	}

	public BSalesPoints(final SalesPoints bean) {
		this.bean = bean;
	}

	public BSalesPoints(final String key) throws ArahantException {
		internalLoad(key);
	}

	public String getSalesPointsId() {
		return bean.getSalesPointsId();
	}

	public Employee getEmployee() {
		return bean.getEmployee();
	}

	public void setEmployee(Employee employee) {
		bean.setEmployee(employee);
	}

	public Date getPointDate() {
		return bean.getPointDate();
	}

	public void setPointDate(Date pointDate) {
		bean.setPointDate(pointDate);
	}

	public ProspectCompany getProspect() {
		return bean.getProspect();
	}

	public void setProspect(ProspectCompany prospect) {
		bean.setProspect(prospect);
	}

	public ProspectStatus getProspestStatus() {
		return bean.getProspestStatus();
	}

	public void setProspestStatus(ProspectStatus prospestStatus) {
		bean.setProspestStatus(prospestStatus);
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
	public void delete() throws ArahantDeleteException {
		try {
			ArahantSession.getHSU().delete(bean);
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
	}

	public static void delete(final String[] salesPointsIds) throws ArahantException {
		for (final String element : salesPointsIds)
			new BSalesPoints(element).delete();
	}

	@Override
	public String create() throws ArahantException {
		bean = new SalesPoints();
		bean.generateId();
		return getSalesPointsId();
	}

	private void internalLoad(String key) throws ArahantException {
		logger.debug("Loading " + key);
		bean = ArahantSession.getHSU().get(SalesPoints.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	public static BSalesPoints[] list(final int max) {
		return makeArray(ArahantSession.getHSU().createCriteria(SalesPoints.class).orderByDesc(SalesPoints.POINT_DATE).setMaxResults(max).list());
	}

	public static BSalesPoints[] makeArray(final List<SalesPoints> l) {
		final BSalesPoints[] ret = new BSalesPoints[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BSalesPoints(l.get(loop));
		return ret;
	}
}
