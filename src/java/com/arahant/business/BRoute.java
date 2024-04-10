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
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.util.*;

public class BRoute extends SimpleBusinessObjectBase<Route> {

	public BRoute() {
	}

	/**
	 * @param routeId
	 * @throws ArahantException
	 */
	public BRoute(final String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 * @param route
	 */
	public BRoute(final Route route) {
		bean = route;
	}

	public String getCompanyId() {
		try {
			if (bean.getInitalRouteStop().getOrgGroup() == null)
				return "ReqCo";
			BOrgGroup borg = new BOrgGroup(bean.getInitalRouteStop().getOrgGroup());

			return borg.getCompanyId();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getCompanyName() {
		try {
			if (bean.getInitalRouteStop().getOrgGroup() == null)
				return "Requesting Company";
			BOrgGroup borg = new BOrgGroup(bean.getInitalRouteStop().getOrgGroup());
			if (borg.isCompany())
				return borg.getName();
			return borg.getCompanyName();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getDefaultCompanyName() {
		try {
			if (bean.getInitalRouteStop().getOrgGroup() == null)
				return "Requesting Company";
			if (bean.getInitalRouteStop().getOrgGroup().getOwningCompany() != null)
				return bean.getInitalRouteStop().getOrgGroup().getOwningCompany().getName();
			return bean.getInitalRouteStop().getOrgGroup().getName();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getDefaultOrgGroupName() {
		try {
			if (bean.getInitalRouteStop().getOrgGroup() == null)
				return "Requesting Organizational Group";
			if (bean.getInitalRouteStop().getOrgGroup().getOwningCompany() != null)
				return bean.getInitalRouteStop().getOrgGroup().getName();
			return "";
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * @return @see com.arahant.beans.Route#getDescription()
	 */
	public String getDescription() {
		return bean.getDescription();
	}

	/**
	 * @return @see com.arahant.beans.Route#getName()
	 */
	public String getName() {
		return bean.getName();
	}

	/**
	 * @return @see com.arahant.beans.Route#getRouteId()
	 */
	public String getRouteId() {
		return bean.getRouteId();
	}

	public Set<RouteStop> getRouteStops() {
		return bean.getRouteStops();
	}

	public boolean hasInitialRouteStop() {
		return bean.getInitalRouteStop() != null;
	}

	public boolean hasInitialStatus() {
		return bean.getInitialProjectStatus() != null;
	}

	public boolean hasRequestingCompanies() {
		return ArahantSession.getHSU().createCriteria(RouteStop.class).isNull(RouteStop.ORG_GROUP).eq(RouteStop.ROUTE, bean).exists();
	}

	public BCompanyBase[] searchCompanies(String name, int highCap) {
		return BCompanyBase.makeArrayEx(ArahantSession.getHSU().createCriteria(CompanyBase.class).like(CompanyBase.NAME, name).setMaxResults(highCap).orderBy(CompanyBase.NAME).joinTo(CompanyBase.ORGGROUPS).joinTo(OrgGroup.ROUTESTOPS).eq(RouteStop.ROUTE, bean).list());
	}

	/**
	 * @param description
	 * @see com.arahant.beans.Route#setDescription(java.lang.String)
	 */
	public void setDescription(final String description) {
		bean.setDescription(description);
	}

	/**
	 * @param name
	 * @see com.arahant.beans.Route#setName(java.lang.String)
	 */
	public void setName(final String name) {
		bean.setName(name);
	}

	/**
	 * @param routeId
	 * @see com.arahant.beans.Route#setRouteId(java.lang.String)
	 */
	public void setRouteId(final String routeId) {
		bean.setRouteId(routeId);
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#create()
	 */
	@Override
	public String create() throws ArahantException {
		bean = new Route();
		bean.setRouteId(bean.generateId());
		return bean.getRouteId();
	}

	private void internalLoad(final String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(Route.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	@Override
	public void insert() throws ArahantException {
		if (ArahantSession.getHSU().createCriteria(Route.class).eq(Route.NAME, bean.getName()).exists())
			throw new ArahantException("Name is already in use.");
		super.insert();
	}

	/**
	 * @param hsu
	 * @param ids
	 * @throws ArahantException
	 * @throws ArahantDeleteException
	 */
	public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantDeleteException, ArahantException {
		for (String element : ids)
			new BRoute(element).delete();
	}

	/**
	 * @param cap
	 * @return
	 */
	public static BRoute[] list() {

		BRoute[] ret;

		final HibernateSessionUtil hsu = ArahantSession.getHSU();

		final List<Route> l = hsu.createCriteria(Route.class).orderBy(Route.NAME).list();
		ret = new BRoute[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BRoute(l.get(loop));

		return ret;
	}

	/**
	 * @return
	 */
	public BOrgGroup[] listOrgGroups() {

		final List<OrgGroup> l = new LinkedList<OrgGroup>();

		//need to add a dummy if any route stops have a null org group
		if (ArahantSession.getHSU().createCriteria(RouteStop.class).isNull(RouteStop.ORG_GROUP).eq(RouteStop.ROUTE, bean).exists()) {
			OrgGroup dummy = new OrgGroup();
			dummy.setName("Requesting Organizational Group");
			dummy.setOrgGroupId("ReqOrg");
			CompanyBase dum2 = new CompanyBase();
			dum2.setName("Requesting Company");
			dum2.setOrgGroupId("ReqCo");
			OrgGroupHierarchy ogh = new OrgGroupHierarchy();
			ogh.setOrgGroupByChildGroupId(dummy);
			ogh.setOrgGroupByParentGroupId(dum2);
			dummy.getOrgGroupHierarchiesForChildGroupId().add(ogh);
			dum2.getOrgGroupHierarchiesForParentGroupId().add(ogh);
			dummy.setOwningCompany(dum2);
			l.add(dummy);
		}


		l.addAll(ArahantSession.getHSU().createCriteria(OrgGroup.class).orderBy(OrgGroup.NAME).joinTo(OrgGroup.ROUTESTOPS).eq(RouteStop.ROUTE, bean).list());


		final BOrgGroup[] ret = new BOrgGroup[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BOrgGroup(l.get(loop));


		return ret;
	}

	/**
	 * @param name
	 * @param i
	 * @param i
	 * @return
	 */
	public BOrgGroup[] searchOrgGroups(final String name, final int orgGroupType, final int max) {
		final HibernateCriteriaUtil<OrgGroup> hcu = ArahantSession.getHSU().createCriteria(OrgGroup.class).like(OrgGroup.NAME, name);

		if (orgGroupType > 0)
			hcu.eq(OrgGroup.ORGGROUPTYPE, orgGroupType);

		final List<OrgGroup> l1 = hcu.joinTo(OrgGroup.ROUTESTOPS).eq(RouteStop.ROUTE, bean).list();


		final HibernateCriteriaUtil<OrgGroup> hcu2 = ArahantSession.getHSU().createCriteria(OrgGroup.class).setMaxResults(max).like(OrgGroup.NAME, name);

		if (orgGroupType > 0)
			hcu2.eq(OrgGroup.ORGGROUPTYPE, orgGroupType);


		final List<OrgGroup> l = hcu2.list();

		l.removeAll(l1);


		final BOrgGroup[] ret = new BOrgGroup[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BOrgGroup(l.get(loop));


		return ret;
	}

	/**
	 * @param orgGroupId
	 * @param cap
	 * @return
	 */
	public BRouteStop[] listRouteStops(final String orgGroupId, final int cap) {

		HibernateCriteriaUtil<RouteStop> hcu = ArahantSession.getHSU().createCriteria(RouteStop.class).setMaxResults(cap).eq(RouteStop.ROUTE, bean);
		if (!isEmpty(orgGroupId) && !"ReqOrg".equals(orgGroupId) && !"ReqCo".equals(orgGroupId))
			hcu.joinTo(RouteStop.ORG_GROUP).eq(OrgGroup.ORGGROUPID, orgGroupId);
		else
			hcu.isNull(RouteStop.ORG_GROUP);



		final List<RouteStop> l = hcu.list();

		final BRouteStop[] ret = new BRouteStop[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BRouteStop(l.get(loop));

		return ret;
	}

	/**
	 * @throws ArahantDeleteException
	 *
	 */
	public void deleteAllRouteTypes() throws ArahantDeleteException {
		ArahantSession.getHSU().delete(bean.getRouteTypeAssociations());
		ArahantSession.getHSU().flush();
	}

	/**
	 * @return
	 */
	public Set<RouteTypeAssoc> getRouteTypeAssociations() {

		return bean.getRouteTypeAssociations();
	}

	@Override
	public void delete() throws ArahantDeleteException {
		for (final RouteStop rs : bean.getRouteStops())
			new BRouteStop(rs).delete();
		ArahantSession.getHSU().delete(bean.getRouteTypeAssociations());
		super.delete();
	}

	/**
	 * @param orgGroupType
	 * @param routeStopName
	 * @param orgGroupName
	 * @param cap
	 * @return
	 */
	public BRouteStop[] search(final int orgGroupType, final String routeStopName, final String orgGroupName, final int cap) {

		final HibernateSessionUtil hsu = ArahantSession.getHSU();

		final HibernateCriteriaUtil<RouteStop> hcu = hsu.createCriteria(RouteStop.class).orderBy(RouteStop.NAME).setMaxResults(cap).eq(RouteStop.ROUTE, bean).like(RouteStop.NAME, routeStopName);
		final HibernateCriteriaUtil h2 = hcu.joinTo(RouteStop.ORG_GROUP).like(OrgGroup.NAME, orgGroupName);

		if (orgGroupType != 0)
			h2.eq(OrgGroup.ORGGROUPTYPE, orgGroupType);

		final List<RouteStop> l = hcu.list();

		final BRouteStop[] ret = new BRouteStop[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BRouteStop(l.get(loop));

		return ret;
	}

	public BRouteStop[] search(final int orgGroupType, final String routeStopName, final String orgGroupName, String companyName, int searchType, final int cap) {

		final HibernateCriteriaUtil<RouteStop> hcu = ArahantSession.getHSU().createCriteria(RouteStop.class).orderBy(RouteStop.NAME).setMaxResults(cap).eq(RouteStop.ROUTE, bean).like(RouteStop.NAME, routeStopName);


		if (searchType == 1)
			hcu.isNull(RouteStop.ORG_GROUP);

		if (searchType == 2) {
			final HibernateCriteriaUtil h2 = hcu.joinTo(RouteStop.ORG_GROUP).like(OrgGroup.NAME, orgGroupName).joinTo(OrgGroup.OWNINGCOMPANY).like(CompanyBase.NAME, companyName);

			if (orgGroupType != 0)
				h2.eq(OrgGroup.ORGGROUPTYPE, orgGroupType);
		}

		final List<RouteStop> l = hcu.list();

		final BRouteStop[] ret = new BRouteStop[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BRouteStop(l.get(loop));

		return ret;
	}

	public BRouteStop[] search(final int orgGroupType, final String routeStopName, final String orgGroupName, String companyName, int searchType, String companyId, String orgGroupId, final int cap) {

		final HibernateCriteriaUtil<RouteStop> hcu = ArahantSession.getHSU().createCriteria(RouteStop.class).orderBy(RouteStop.NAME).setMaxResults(cap).eq(RouteStop.ROUTE, bean).like(RouteStop.NAME, routeStopName);


		if (searchType == 1)
			hcu.isNull(RouteStop.ORG_GROUP);
		else
			if (searchType == 2) {
				final HibernateCriteriaUtil h2 = hcu.joinTo(RouteStop.ORG_GROUP).like(OrgGroup.NAME, orgGroupName);
				final HibernateCriteriaUtil h3 = h2.joinTo(OrgGroup.OWNINGCOMPANY).like(CompanyBase.NAME, companyName);

				if (!isEmpty(companyId))
					h3.eq(CompanyBase.ORGGROUPID, companyId);

				if (!isEmpty(orgGroupId))
					h2.eq(OrgGroup.ORGGROUPID, orgGroupId);

				if (orgGroupType != 0)
					h3.eq(OrgGroup.ORGGROUPTYPE, orgGroupType);
			}

		final List<RouteStop> l = hcu.list();

		final BRouteStop[] ret = new BRouteStop[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BRouteStop(l.get(loop));

		return ret;
	}

	public BRouteStop[] search(final int orgGroupType, final String routeStopName, final String orgGroupName, boolean searchRequesters, final int cap) {

		if (!searchRequesters)
			return search(orgGroupType, routeStopName, orgGroupName, cap);


		final HibernateCriteriaUtil<RouteStop> hcu = ArahantSession.getHSU().createCriteria(RouteStop.class).orderBy(RouteStop.NAME).setMaxResults(cap).eq(RouteStop.ROUTE, bean).like(RouteStop.NAME, routeStopName).isNull(RouteStop.ORG_GROUP);


		final List<RouteStop> l = hcu.list();

		final BRouteStop[] ret = new BRouteStop[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BRouteStop(l.get(loop));

		return ret;
	}

	/**
	 * @param defaultRouteStopId
	 */
	public void setDefaultRouteStop(final String defaultRouteStopId) {
		bean.setInitalRouteStop(ArahantSession.getHSU().get(RouteStop.class, defaultRouteStopId));
	}

	/**
	 * @param defaultStatusId
	 */
	public void setDefaultStatusId(final String defaultStatusId) {
		bean.setInitialProjectStatus(ArahantSession.getHSU().get(ProjectStatus.class, defaultStatusId));
	}

	/**
	 * @return
	 */
	public String getRouteName() {

		return bean.getName();
	}

	public CompanyDetail getCompany() {
		return bean.getCompany();
	}

	public void setCompany(CompanyDetail company) {
		bean.setCompany(company);
	}

	public int getLastActiveDate() {
		return bean.getLastActiveDate();
	}

	public void setLastActiveDate(int lastActiveDate) {
		bean.setLastActiveDate(lastActiveDate);
	}

	/**
	 * @return
	 */
	public String getRouteStopId() {

		try {
			return bean.getInitalRouteStop().getRouteStopId();
		} catch (final Exception e) {
			return "";
		}
	}

	public BRouteStop getRouteStop() {

		try {
			return new BRouteStop(bean.getInitalRouteStop().getRouteStopId());
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * @return
	 */
	public String getOrgGroupName() {
		try {
			if (bean.getInitalRouteStop().getOrgGroup() == null)
				return "Requesting Group";
			BOrgGroup borg = new BOrgGroup(bean.getInitalRouteStop().getOrgGroup());
			if (borg.isCompany())
				return "";
			return borg.getName();
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * @return
	 */
	public String getRouteStopName() {
		try {
			return bean.getInitalRouteStop().getDescription();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getRouteStopTypeFormatted() {
		try {
			return new BRouteStop(bean.getInitalRouteStop()).getTypeFormatted();
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * @return
	 */
	public String getProjectStatusId() {

		try {
			return bean.getInitialProjectStatus().getProjectStatusId();
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * @return
	 */
	public String getProjectStatusCode() {
		try {
			return bean.getInitialProjectStatus().getCode();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getProjectStatusDescription() {
		try {
			return bean.getInitialProjectStatus().getDescription();
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * @param name
	 * @param cap
	 * @return
	 */
	public static BRoute[] search(final String name, final int cap) {

		final List<Route> l = ArahantSession.getHSU().createCriteria(Route.class).orderBy(Route.NAME).like(Route.NAME, name).setMaxResults(cap).list();

		final BRoute[] ret = new BRoute[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BRoute(l.get(loop));

		return ret;
	}

	/**
	 * @return
	 */
	public String getOrgGroupId() {

		try {
			if (bean.getInitalRouteStop().getOrgGroup() == null)
				return "ReqOrg";
			return bean.getInitalRouteStop().getOrgGroup().getOrgGroupId();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getOnlyOrgGroupId() {

		try {
			BOrgGroup borg = new BOrgGroup(bean.getInitalRouteStop().getOrgGroup());
			if (borg.isCompany())
				return "";
			return bean.getInitalRouteStop().getOrgGroup().getOrgGroupId();
		} catch (final Exception e) {
			return "";
		}
	}

	public BOrgGroup[] searchOrgGroups(int type, String companyName, boolean includeOrgGroups, String orgGroupName, int cap) {

		List orgids = ArahantSession.getHSU().createCriteria(OrgGroup.class).selectFields(OrgGroup.ORGGROUPID).orderBy(OrgGroup.NAME).joinTo(OrgGroup.ROUTESTOPS).eq(RouteStop.ROUTE, bean).list();

		//get all the companies that match first
		Set<OrgGroup> res = new HashSet<OrgGroup>();

		HibernateCriteriaUtil<CompanyBase> cbhcu = ArahantSession.getHSU().createCriteria(CompanyBase.class).setMaxResults(cap).like(CompanyBase.NAME, companyName);

		if (type != 0)
			cbhcu.eq(CompanyBase.ORGGROUPTYPE, type);

		cbhcu.notIn(CompanyBase.ORGGROUPID, orgids);

		res.addAll(cbhcu.list());


		if (includeOrgGroups) {
			HibernateCriteriaUtil<OrgGroup> hcu = ArahantSession.getHSU().createCriteria(OrgGroup.class).setMaxResults(cap - res.size());
			HibernateCriteriaUtil hcu2 = hcu.like(OrgGroup.NAME, orgGroupName).joinTo(OrgGroup.OWNINGCOMPANY).like(CompanyBase.NAME, companyName);

			if (type != 0)
				hcu2.eq(CompanyBase.ORGGROUPTYPE, type);

			hcu.notIn(CompanyBase.ORGGROUPID, orgids);

			res.addAll(hcu.list());
		}

		List<OrgGroup> l = new ArrayList<OrgGroup>();
		l.addAll(res);
		Collections.sort(l);
		return BOrgGroup.makeArray(res);
	}

	public BOrgGroup[] searchOrgGroups(String companyId, String name, int cap) {
		HibernateCriteriaUtil<OrgGroup> hcu = ArahantSession.getHSU().createCriteria(OrgGroup.class).orderBy(OrgGroup.NAME).like(OrgGroup.NAME, name).setMaxResults(cap);
		hcu.joinTo(OrgGroup.ROUTESTOPS).eq(RouteStop.ROUTE, bean);
		hcu.joinTo(OrgGroup.OWNINGCOMPANY).eq(CompanyBase.ORGGROUPID, companyId);

		return BOrgGroup.makeArray(hcu.list());

	}

	public BProjectStatus[] getInitialRouteStopStatuses() {
		return BProjectStatus.makeArray(bean.getInitalRouteStop().getAllowedStatuses());
	}
}
