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

import com.arahant.beans.ServiceSubscribed;
import com.arahant.beans.ServiceSubscribedJoin;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.List;

public class BServiceSubscribed extends SimpleBusinessObjectBase<ServiceSubscribed> implements IDBFunctions {

	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	private static final transient ArahantLogger logger = new ArahantLogger(BServiceSubscribed.class);

	public BServiceSubscribed() {
	}

	public BServiceSubscribed(final ServiceSubscribed type) {
		bean = type;
	}

	public BServiceSubscribed(final String key) throws ArahantException {
		internalLoad(key);
	}

	@Override
	public String create() throws ArahantException {
		bean = new ServiceSubscribed();
		bean.generateId();
		return getServiceId();
	}

	private void internalLoad(final String key) throws ArahantException {
		logger.debug("Loading " + key);
		bean = ArahantSession.getHSU().get(ServiceSubscribed.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	public String getDescription() {
		return bean.getDescription();
	}

	public short getInterfaceCode() {
		return bean.getInterfaceCode();
	}

	public void setInterfaceCode(short code) {
		bean.setInterfaceCode(code);
	}

	public void setDescription(final String description) {
		bean.setDescription(description);
	}

	public String getServiceId() {
		return bean.getServiceId();
	}

	public void setServiceId(final String bean) {
		this.bean.setServiceId(bean);
	}

	public String getOrgGroupId() {
		return bean.getOrgGroup().getOrgGroupId();
	}

	public String getServiceName() {
		return bean.getServiceName();
	}

	public void setServiceName(String serviceName) {
		bean.setServiceName(serviceName);
	}

	public int getFirstActiveDate() {
		return bean.getFirstActiveDate();
	}

	public void setFirstActiveDate(int firstActiveDate) {
		bean.setFirstActiveDate(firstActiveDate);
	}

	public int getLastActiveDate() {
		return bean.getLastActiveDate();
	}

	public void setLastActiveDate(int lastActiveDate) {
		bean.setLastActiveDate(lastActiveDate);
	}

	@SuppressWarnings("unchecked")
	public static BServiceSubscribed[] search(final String name, final boolean showActive, final boolean showInactive, int cap) {
		final HibernateCriteriaUtil hcu = ArahantSession.getHSU().createCriteria(ServiceSubscribed.class).orderBy(ServiceSubscribed.SERVICENAME).like(ServiceSubscribed.SERVICENAME, name);

		if ((showActive) && (!showInactive))
			hcu.dateInside(ServiceSubscribed.FIRSTACTIVEDATE, ServiceSubscribed.LASTACTIVEDATE, DateUtils.now());

		if ((showInactive) && (!showActive))
			hcu.dateOutside(ServiceSubscribed.FIRSTACTIVEDATE, ServiceSubscribed.LASTACTIVEDATE, DateUtils.now());

		hcu.setMaxResults(cap);

		return makeArray(hcu.list());
	}

	private static BServiceSubscribed[] makeArray(final List<ServiceSubscribed> l) {
		final BServiceSubscribed[] ret = new BServiceSubscribed[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BServiceSubscribed(l.get(loop));

		return ret;
	}

	public static BServiceSubscribed[] searchAvailableServices(final String name, int cap) {
		final HibernateCriteriaUtil hcu = ArahantSession.getHSU().createCriteria(ServiceSubscribed.class)
				.selectFields(ServiceSubscribed.SERVICEID)
				.like(ServiceSubscribed.SERVICENAME, name)
				.joinTo(ServiceSubscribed.SUBSCRIBED_JOINS)
				.eq(ServiceSubscribedJoin.COMPANY, ArahantSession.getHSU().getCurrentCompany())
				.gtOrEq(ServiceSubscribedJoin.LASTDATE, DateUtils.now(), 0);


		return makeArray(ArahantSession.getHSU().createCriteria(ServiceSubscribed.class)
				.setMaxResults(cap)
				.orderBy(ServiceSubscribed.SERVICENAME)
				.like(ServiceSubscribed.SERVICENAME, name)
				.dateInside(ServiceSubscribed.FIRSTACTIVEDATE, ServiceSubscribed.LASTACTIVEDATE, DateUtils.now())
				.notIn(ServiceSubscribed.SERVICEID, hcu.list())
				.list());
	}

	public static void delete(final String[] serviceIds) throws ArahantException {

		if (ArahantSession.getHSU().createCriteria(ServiceSubscribedJoin.class)
				.joinTo(ServiceSubscribedJoin.SERVICE)
				.in(ServiceSubscribed.SERVICEID, serviceIds)
				.exists())
			throw new ArahantWarning("You may not delete a service after it has been assigned to a company.");

		for (final String element : serviceIds)
			new BServiceSubscribed(element).delete();
	}
}
