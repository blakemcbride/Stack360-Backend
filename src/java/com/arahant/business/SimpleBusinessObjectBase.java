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
import static com.arahant.business.BusinessLogicBase.isEmpty;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public abstract class SimpleBusinessObjectBase< T extends ArahantBean> extends BusinessLogicBase implements IDBFunctions {

	protected static final ArahantLogger logger = new ArahantLogger(SimpleBusinessObjectBase.class);
	private boolean forceAllCompanies = false;
	T bean;
	private final List<IDBFunctions> pendingInserts = new LinkedList<IDBFunctions>();
	private final List<IDBFunctions> pendingUpdates = new LinkedList<IDBFunctions>();
	protected List<ArahantBean> updates = new LinkedList<ArahantBean>();

	public SimpleBusinessObjectBase() {
	}

	/**
	 *
	 * @param key
	 * @throws ArahantException
     */
	public SimpleBusinessObjectBase(final String key) throws ArahantException {
		if (isEmpty(key))
			throw new ArahantException("ERROR: Empty key passed to constructor.");
		load(key);
		if (bean == null)
			throw new ArahantException("ERROR: Failed to load with key='" + key + "'");
	}

	public void addPendingInsert(IDBFunctions i) {
		pendingInserts.add(i);
	}

	public void addPendingUpdate(IDBFunctions i) {
		pendingUpdates.add(i);
	}

	public T getBean() {
		return bean;
	}

	@Override
	public void delete() throws ArahantDeleteException {
		try {
			ArahantSession.getHSU().delete(bean);
		} catch (final Exception e) {
			throw new ArahantDeleteException();
		}
	}

	@Override
	public void insert() throws ArahantException {
		insertChecks();

		HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (bean instanceof CompanyFiltered) {
			CompanyFiltered f = (CompanyFiltered) bean;
			if (forceAllCompanies || (allCompanies && BRight.checkRight(Right.RIGHT_CAN_ACCESS_ALL_COMPANIES) == BRight.ACCESS_LEVEL_WRITE))
				f.setOrgGroup(null);
			else
				f.setOrgGroup(hsu.getCurrentCompany());
		}

		if (bean instanceof AuditedBean) {
			AuditedBean ab = (AuditedBean) bean;
			ab.setRecordChangeDate(new Date());
			ab.setRecordChangeType('N');
			ab.setRecordPersonId(hsu.getCurrentPerson().getPersonId());
		}

		hsu.insert(bean);
		hsu.update(updates);
		for (IDBFunctions i : pendingInserts)
			i.insert();
		for (IDBFunctions i : pendingUpdates)
			i.update();
	}

	public boolean isForceAllCompanies() {
		return forceAllCompanies;
	}

	public void setForceAllCompanies(boolean forceAllCompanies) {
		this.forceAllCompanies = forceAllCompanies;
	}
	private boolean allCompanies = false;

	public void setAllCompanies(boolean allCompanies) {
		this.allCompanies = allCompanies;
	}

	public boolean getAllCompanies() {
		if (bean instanceof Filtered)
			return ((Filtered) bean).getOrgGroup() == null;
		return true;
	}

	@Override
	public void update() throws ArahantException {
		updateChecks();
        HibernateSessionUtil hsu = ArahantSession.getHSU();

		//only an all company user can change the value
		if (bean instanceof Filtered && BRight.checkRight(Right.RIGHT_CAN_ACCESS_ALL_COMPANIES) == BRight.ACCESS_LEVEL_WRITE) {
			Filtered f = (Filtered) bean;
			if (allCompanies)
				f.setOrgGroup(null);
			else
				f.setOrgGroup(hsu.getCurrentCompany());
		}

		hsu.saveOrUpdate(bean);
		hsu.update(updates);
		for (IDBFunctions i : pendingInserts)
			i.insert();
		for (IDBFunctions i : pendingUpdates)
			i.update();
	}

	public void insertChecks() throws ArahantException {
	}

	public void updateChecks() throws ArahantException {
	}
}
