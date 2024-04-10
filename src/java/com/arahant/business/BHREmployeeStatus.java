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
import com.arahant.reports.HREmployeeStatusChangedReport;
import com.arahant.reports.HREmployeesByStatusReport;
import com.arahant.utils.*;
import com.itextpdf.text.DocumentException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class BHREmployeeStatus extends BusinessLogicBase implements IDBFunctions {

	private HrEmployeeStatus hrEmployeeStatus;

	public BHREmployeeStatus() {
	}

	public BHREmployeeStatus(final String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 * @param account
	 */
	public BHREmployeeStatus(final HrEmployeeStatus account) {
		hrEmployeeStatus = account;
	}

	public HrEmployeeStatus getBean() {
		return hrEmployeeStatus;
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#create()
	 */
	/*
	 * Do not use until Java 6 @Override
	 */
	@Override
	public String create() throws ArahantException {
		hrEmployeeStatus = new HrEmployeeStatus();
		hrEmployeeStatus.generateId();
		hrEmployeeStatus.setOrgGroup(ArahantSession.getHSU().getCurrentCompany());
		return getEmployeeStatusId();
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#delete()
	 */
	/*
	 * Do not use until Java 6 @Override
	 */
	@Override
	public void delete() throws ArahantDeleteException {
		ArahantSession.getHSU().delete(hrEmployeeStatus);
	}

	public String getId() {
		return hrEmployeeStatus.getStatusId();
	}

	public int getLastActiveDate() {
		return hrEmployeeStatus.getLastActiveDate();
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#insert()
	 */
	/*
	 * Do not use until Java 6 @Override
	 */
	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(hrEmployeeStatus);
	}

	private void internalLoad(final String key) throws ArahantException {
		hrEmployeeStatus = ArahantSession.getHSU().get(HrEmployeeStatus.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	public void setLastActiveDate(int lastActiveDate) {
		hrEmployeeStatus.setLastActiveDate(lastActiveDate);
	}

	public void setOrgGroupId(String orgGroupId) {
		hrEmployeeStatus.setOrgGroup(ArahantSession.getHSU().get(OrgGroup.class, orgGroupId));
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#update()
	 */
	/*
	 * Do not use until Java 6 @Override
	 */
	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(hrEmployeeStatus);
	}

	/**
	 * @return @see com.arahant.beans.HrEmployeeStatus#getEmployeeStatusId()
	 */
	public String getEmployeeStatusId() {
		return hrEmployeeStatus.getStatusId();
	}

	/**
	 * @return @see com.arahant.beans.HrEmployeeStatus#getName()
	 */
	public String getName() {
		return hrEmployeeStatus.getName();
	}

	/**
	 * @param EmployeeStatusId
	 * @see
	 * com.arahant.beans.HrEmployeeStatus#setEmployeeStatusId(java.lang.String)
	 */
	public void setEmployeeStatusId(final String EmployeeStatusId) {
		hrEmployeeStatus.setStatusId(EmployeeStatusId);
	}

	/**
	 * @param name
	 * @see com.arahant.beans.HrEmployeeStatus#setName(java.lang.String)
	 */
	public void setName(final String name) {
		hrEmployeeStatus.setName(name);
	}

	/**
	 * @return
	 */
	public char getBenefitType() {
		return hrEmployeeStatus.getBenefitType();
	}

	public void setBenefitType(final char type) {
		hrEmployeeStatus.setBenefitType(type);
	}

	/**
	 * @return
	 */
	public String getBenefitTypeFormatted() {
		if (hrEmployeeStatus.getBenefitType() == 'B')
			return "Only Benefits NOT covered under COBRA";
		else if (hrEmployeeStatus.getBenefitType() == 'C')
			return "Only Benefits covered under COBRA";
		else
			return "No Benefits";
	}

	/**
	 * @param hsu
	 * @param ids
	 * @throws ArahantException
	 */
	public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantException {
		for (final String element : ids)
			new BHREmployeeStatus(element).delete();
	}

	/**
	 * @param hsu
	 * @return
	 */
	public static BHREmployeeStatus[] list(final HibernateSessionUtil hsu) {
		return makeArray(hsu.createCriteria(HrEmployeeStatus.class).orderBy(HrEmployeeStatus.NAME).geOrEq(HrEmployeeStatus.LASTACTIVEDATE, DateUtils.now(), 0).list());
	}

	public static BHREmployeeStatus[] list(int activeType) {

		HibernateCriteriaUtil<HrEmployeeStatus> hcu = ArahantSession.getHSU().createCriteria(HrEmployeeStatus.class).orderBy(HrEmployeeStatus.NAME);

		if (activeType == 1) //actives
			hcu.geOrEq(HrEmployeeStatus.LASTACTIVEDATE, DateUtils.now(), 0);
		if (activeType == 2) //inactives
			hcu.ltAndNeq(HrEmployeeStatus.LASTACTIVEDATE, DateUtils.now(), 0);

		return makeArray(hcu.list());
	}

	public static String[] listIds(int activeType) {

		HibernateCriteriaUtil<HrEmployeeStatus> hcu = ArahantSession.getHSU().createCriteria(HrEmployeeStatus.class).orderBy(HrEmployeeStatus.NAME);

		if (activeType == 1) //actives
			hcu.geOrEq(HrEmployeeStatus.LASTACTIVEDATE, DateUtils.now(), 0);
		if (activeType == 2) //inactives
			hcu.ltAndNeq(HrEmployeeStatus.LASTACTIVEDATE, DateUtils.now(), 0);

		List<HrEmployeeStatus> l = hcu.list();
		String[] sa = new String[l.size()];
		int count = 0;
		for (HrEmployeeStatus s : l)
			sa[count++] = s.getStatusId();
		return sa;
	}

	public static BHREmployeeStatus[] list(String[] excludeIds) {
		HibernateCriteriaUtil<HrEmployeeStatus> hcu = ArahantSession.getHSU().createCriteria(HrEmployeeStatus.class).geOrEq(HrEmployeeStatus.LASTACTIVEDATE, DateUtils.now(), 0).notIn(HrEmployeeStatus.STATUSID, excludeIds).orderBy(HrEmployeeStatus.NAME);
		return makeArray(hcu.list());
	}

	public static BHREmployeeStatus[] list(String companyId, final HibernateSessionUtil hsu) {
		List<HrEmployeeStatus> l =
				hsu.createCriteriaNoCompanyFilter(HrEmployeeStatus.class).joinTo(HrEmployeeStatus.ORG_GROUP).eq(OrgGroup.ORGGROUPID, companyId).orderBy(HrEmployeeStatus.NAME).list();

		return makeArray(l);
	}

	/**
	 * @param l
	 * @return
	 */
	private static BHREmployeeStatus[] makeArray(final List<HrEmployeeStatus> l) {
		final BHREmployeeStatus[] ret = new BHREmployeeStatus[l.size()];
		for (int loop = 0; loop < l.size(); loop++)
			ret[loop] = new BHREmployeeStatus(l.get(loop));
		return ret;
	}

	/**
	 * @return @see com.arahant.beans.HrEmployeeStatus#getActiveFlag()
	 */
	public char getActiveFlag() {
		return hrEmployeeStatus.getActive();
	}

	public void setActiveFlag(final char hide) {
		hrEmployeeStatus.setActive(hide);
	}

	/**
	 * @param orgGroupId
	 * @return
	 * @throws ArahantException
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 */
	public String getEmployeeByStatusReport(final String orgGroupId, int date, int depth, boolean subGroups) throws ArahantException, FileNotFoundException, DocumentException {
		return new HREmployeesByStatusReport().build(orgGroupId, hrEmployeeStatus, date, depth, subGroups);
	}

	public String getChangedReport(final int startDate, final int endDate, String[] orgGroupIds) throws FileNotFoundException, DocumentException, ArahantException {
		return new HREmployeeStatusChangedReport().build(startDate, endDate, orgGroupIds, hrEmployeeStatus.getStatusId());
	}

	//Kalvin 5/7/2010 report now uses include sub group and depth
	public String getChangedReport(final int startDate, final int endDate, String orgGroupIds, int depth, boolean subGroup) throws FileNotFoundException, DocumentException, ArahantException {
		return new HREmployeeStatusChangedReport().build(startDate, endDate, orgGroupIds, hrEmployeeStatus.getStatusId(), depth, subGroup);
	}

	public String getDateType() {
		return hrEmployeeStatus.getDateType() + "";
	}

	public String getDateTypeFormatted() {
		return hrEmployeeStatus.getDateType() == 'S' ? "Ask for Start Date" : "Ask for Final Date";
	}

	/**
	 * @param dateType
	 */
	public void setDateType(final String dateType) {
		if (!isEmpty(dateType))
			hrEmployeeStatus.setDateType(dateType.charAt(0));
	}

	public static BHREmployeeStatus[] listInactiveStatuses() {
		return makeArray(ArahantSession.getHSU().createCriteria(HrEmployeeStatus.class).eq(HrEmployeeStatus.ACTIVE, 'N').list());
	}

	public static String findOrMake(String name, boolean active) {
		HrEmployeeStatus es = ArahantSession.getHSU().createCriteria(HrEmployeeStatus.class).eq(HrEmployeeStatus.NAME, name).eq(HrEmployeeStatus.ORG_GROUP, ArahantSession.getHSU().getCurrentCompany()).first();

		if (es != null)
			return es.getStatusId();

		BHREmployeeStatus bes = new BHREmployeeStatus();
		String ret = bes.create();
		bes.setName(name);
		bes.setActiveFlag(active ? 'Y' : 'N');
		bes.setDateType(active ? "S" : "F");
		bes.setOrgGroupId(ArahantSession.getHSU().getCurrentCompany().getOrgGroupId());
		bes.setBenefitType('B');
		bes.insert();

		return ret;
	}

	public static void clone(BCompany from_company, BCompany to_company) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		HibernateCriteriaUtil<HrEmployeeStatus> crit = hsu.createCriteriaNoCompanyFilter(HrEmployeeStatus.class);
		crit.eq(HrEmployeeStatus.ORGGROUPID, from_company.getCompanyId());
		crit.le(HrEmployeeStatus.LASTACTIVEDATE, DateUtils.today());
		HibernateScrollUtil<HrEmployeeStatus> scr = crit.scroll();
		while (scr.next()) {
			HrEmployeeStatus es = scr.get();
			HrEmployeeStatus nrec = new HrEmployeeStatus();
			HibernateSessionUtil.copyCorresponding(nrec, es, "statusId", "orgGroupId");
			nrec.generateId();
			nrec.setOrgGroup(to_company.getBean());
			hsu.insert(nrec);
		}
	}
}
