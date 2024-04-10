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
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;
import java.io.Serializable;
import java.util.*;

public class BAlert extends SimpleBusinessObjectBase<Alert> {

	public BAlert() {
	}

	public BAlert(Alert o) {
		bean = o;
	}

	public BAlert(final String alertId) throws ArahantException {
		internalLoad(alertId);
	}

	@Override
	public String create() throws ArahantException {
		bean = new Alert();
		bean.setLastChangePerson(ArahantSession.getHSU().getCurrentPerson());
		bean.setLastChangeDateTime(new Date());
		return bean.generateId();
	}
	
	private void internalLoad(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(Alert.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	@Override
	public void updateChecks() {
		bean.setLastChangePerson(ArahantSession.getHSU().getCurrentPerson());
		bean.setLastChangeDateTime(new Date());
	}

	public void setAlert(String alert) {
		bean.setAlert(alert);
	}

	public String getAlert() {
		return bean.getAlert();
	}

	public int getStartDate() {
		return bean.getStartDate();
	}

	public void setStartDate(int startDate) {
		bean.setStartDate(startDate);
	}

	public int getLastDate() {
		return bean.getLastDate();
	}

	public void setLastDate(int lastDate) {
		bean.setLastDate(lastDate);
	}

	public String getAlertId() {
		return bean.getAlertId();
	}

	public String getLastChangePerson() {
		return bean.getLastChangePerson().getNameFL();
	}

	public Date getLastChangeDateTime() {
		return bean.getLastChangeDateTime();
	}

	public short getAlertDistribution() {
		return bean.getAlertDistribution();
	}

	public void setAlertDistribution(short alertDistribution) {
		bean.setAlertDistribution(alertDistribution);
	}

    /*
	1 = to all employees of a company
	2 = particular employees in a company
	3 = to all employees of a company (from agent)
	4 = to all members of an agent''s company
	5 = to all agents
	6 = all company main contacts';
	 */
	public static void hrSendToAllEmployees(String message, int startDate, int endDate) {
		BAlert ba = new BAlert();
		ba.create();
		ba.setAlert(message);
		ba.setStartDate(startDate);
		ba.setLastDate(endDate);
		ba.bean.setAlertDistribution(Alert.DIST_ALL_EMPLOYEES);
		ba.bean.setOrgGroup(ArahantSession.getHSU().getCurrentCompany());
		ba.insert();

		//send the message to all employees of company
		HibernateScrollUtil<Employee> scr=ArahantSession.getHSU().createCriteriaNoCompanyFilter(Employee.class)
			.activeEmployee()
			.eq(Employee.COMPANYBASE, ArahantSession.getHSU().getCurrentCompany())
			.scroll();

		while (scr.next())
			BMessage.send(ArahantSession.getCurrentPerson(), scr.get(), "Alert", message);

		scr.close();
	}

	public static void hrSendToSomeEmployees(String message, int startDate, int endDate, String[] personIds) {
		BAlert ba = new BAlert();
		ba.create();
		ba.setAlert(message);
		ba.setStartDate(startDate);
		ba.setLastDate(endDate);
		ba.bean.setAlertDistribution(Alert.DIST_PARTICULAR_EMPLOYEES);
		ba.bean.setOrgGroup(ArahantSession.getHSU().getCurrentCompany());
		List<Person> persons = ArahantSession.getHSU().createCriteria(Person.class).in(Person.PERSONID, personIds).list();
		Set<Person> pset = new HashSet<Person>(persons.size());
		pset.addAll(persons);
		ba.bean.setPersons(pset);
		ba.insert();

		//send the message to selected employees
		HibernateScrollUtil<Employee> scr=ArahantSession.getHSU().createCriteriaNoCompanyFilter(Employee.class)
			.in(Employee.PERSONID, personIds)
			.scroll();

		while (scr.next())
			BMessage.send(ArahantSession.getCurrentPerson(), scr.get(), "Alert", message);

		scr.close();

	}

	public static void agentSendToAllEmployees(String message, int startDate, int endDate, String companyId) {
		BAlert ba = new BAlert();
		ba.create();
		ba.setAlert(message);
		ba.setStartDate(startDate);
		ba.setLastDate(endDate);
		ba.bean.setAlertDistribution(Alert.DIST_ALL_EMPLOYEES_FROM_AGENT);
		ba.bean.setOrgGroup(ArahantSession.getHSU().get(CompanyDetail.class, companyId));
		ba.insert();

		//send the message to all employees of company
		HibernateScrollUtil<Employee> scr=ArahantSession.getHSU().createCriteriaNoCompanyFilter(Employee.class)
			.activeEmployee()
			.joinTo(Employee.COMPANYBASE)
			.eq(OrgGroup.ORGGROUPID, companyId)
			.scroll();

		while (scr.next())
			BMessage.send(ArahantSession.getCurrentPerson(), scr.get(), "Alert", message);

		scr.close();
	}

	public static void agentSendToAllAgents(String message, int startDate, int endDate) {
		BAlert ba = new BAlert();
		ba.create();
		ba.setAlert(message);
		ba.setStartDate(startDate);
		ba.setLastDate(endDate);
		ba.bean.setAlertDistribution(Alert.DIST_ALL_AGENTS_COMPANY);
		Agency ag = ArahantSession.getHSU().createCriteria(Agency.class).joinTo(Agency.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, ArahantSession.getHSU().getCurrentPerson()).first();
		ba.bean.setOrgGroup(ag);
		ba.insert();
	}

	public static void managingCompanySendToAllAgents(String message, int startDate, int endDate) {
		BAlert ba = new BAlert();
		ba.create();
		ba.setAlert(message);
		ba.setStartDate(startDate);
		ba.setLastDate(endDate);
		ba.bean.setAlertDistribution(Alert.DIST_ALL_AGENTS);
		ba.insert();
	}

	public static void managingCompanySendToAllMainContacts(String message, int startDate, int endDate) {
		BAlert ba = new BAlert();
		ba.create();
		ba.setAlert(message);
		ba.setStartDate(startDate);
		ba.setLastDate(endDate);
		ba.bean.setAlertDistribution(Alert.DIST_ALL_COMPANY_MAIN_CONTACTS);
		ba.insert();


		//send the message to selected employees
		HibernateScrollUtil<Employee> scr=ArahantSession.getHSU().createCriteriaNoCompanyFilter(Employee.class)
			.joinTo(Employee.ORGGROUPASSOCIATIONS)
			.eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y')
			.joinTo(OrgGroupAssociation.ORGGROUP)
			.eq(OrgGroup.ORGGROUPTYPE,COMPANY_TYPE)
			.eqJoinedField(OrgGroup.ORGGROUPID, OrgGroup.COMPANY_ID)
			.scroll();

		while (scr.next())
			BMessage.send(ArahantSession.getCurrentPerson(), scr.get(), "Alert", message);

		scr.close();
	}

	public static BAlert[] getAllAlertsToShowUser() {
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		//First - am I an agent
		List<Alert> alerts = new ArrayList<Alert>();
		if (BPerson.getCurrent().getOrgGroupType() == AGENT_TYPE) {
			alerts.addAll(hsu.createCriteria(Alert.class).eq(Alert.DISTRIBUTION, Alert.DIST_ALL_AGENTS).dateInside(Alert.START_DATE, Alert.END_DATE, DateUtils.now()).list());

			alerts.addAll(hsu.createCriteria(Alert.class).eq(Alert.DISTRIBUTION, Alert.DIST_ALL_AGENTS_COMPANY).dateInside(Alert.START_DATE, Alert.END_DATE, DateUtils.now()).joinTo(Alert.ORG_GROUP).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, hsu.getCurrentPerson()).list());

		} else {
			alerts.addAll(hsu.createCriteria(Alert.class).in(Alert.DISTRIBUTION, new short[]{Alert.DIST_ALL_EMPLOYEES_FROM_AGENT, Alert.DIST_ALL_EMPLOYEES}).dateInside(Alert.START_DATE, Alert.END_DATE, DateUtils.now()).eq(Alert.ORG_GROUP, hsu.getCurrentPerson().getCompanyBase()).list());

			alerts.addAll(hsu.createCriteria(Alert.class).eq(Alert.DISTRIBUTION, Alert.DIST_ALL_COMPANY_MAIN_CONTACTS).dateInside(Alert.START_DATE, Alert.END_DATE, DateUtils.now()).joinTo(Alert.ORG_GROUP).joinTo(OrgGroup.OWNINGCOMPANY).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y').eq(OrgGroupAssociation.PERSON, hsu.getCurrentPerson()).list());

			alerts.addAll(hsu.createCriteria(Alert.class).eq(Alert.DISTRIBUTION, Alert.DIST_PARTICULAR_EMPLOYEES).dateInside(Alert.START_DATE, Alert.END_DATE, DateUtils.now()).joinTo(Alert.PERSONS).eq(Person.PERSONID, hsu.getCurrentPerson().getPersonId()).list());

		}
		return makeArray(alerts);
	}

	public static BAlert[] getAllAlertsHRUserCanEdit() {
		return makeArray(ArahantSession.getHSU().createCriteria(Alert.class).in(Alert.DISTRIBUTION, new short[]{Alert.DIST_PARTICULAR_EMPLOYEES, Alert.DIST_ALL_EMPLOYEES}).orderBy(Alert.START_DATE).list());
	}

	public static BAlert[] getAllAlertsHRUserCanEdit(final int alertDate, final boolean personFilter, final int cap) {
		final HibernateCriteriaUtil<Alert> hcu = ArahantSession.getHSU().createCriteria(Alert.class).eq(Alert.ORG_GROUP, ArahantSession.getHSU().getCurrentCompany()).in(Alert.DISTRIBUTION, new short[]{Alert.DIST_PARTICULAR_EMPLOYEES, Alert.DIST_ALL_EMPLOYEES}).orderBy(Alert.START_DATE).setMaxResults(cap);

		if (alertDate != 0)
			hcu.dateInside(Alert.START_DATE, Alert.END_DATE, alertDate);

		if (personFilter)
			hcu.eq(Alert.CHANGE_PERSON, ArahantSession.getHSU().getCurrentPerson());

		return makeArray(hcu.list());
	}

	public static BAlert[] getAllAlertsManagingCompanyUserCanEdit(final int alertDate, final boolean personFilter, final int cap) {
		final HibernateCriteriaUtil<Alert> hcu = ArahantSession.getHSU().createCriteria(Alert.class).in(Alert.DISTRIBUTION, new short[]{Alert.DIST_ALL_AGENTS, Alert.DIST_ALL_COMPANY_MAIN_CONTACTS}).orderBy(Alert.START_DATE).setMaxResults(cap);

		if (alertDate != 0)
			hcu.dateInside(Alert.START_DATE, Alert.END_DATE, alertDate);

		if (personFilter)
			hcu.eq(Alert.CHANGE_PERSON, ArahantSession.getHSU().getCurrentPerson());

		return makeArray(hcu.list());

	/*
	ArahantSession.getHSU().createCriteria(Alert.class)
	.in(Alert.DISTRIBUTION, new short[]{Alert.DIST_ALL_AGENTS, Alert.DIST_ALL_COMPANY_MAIN_CONTACTS})
	.orderBy(Alert.START_DATE)
	.list());
	 */
	}

	public static BAlert[] getAllAlertsAgentUserCanEdit(final int alertDate, final boolean personFilter, final int cap) {

		final HibernateCriteriaUtil<Alert> hcu = ArahantSession.getHSU().createCriteria(Alert.class).eq(Alert.DISTRIBUTION, Alert.DIST_ALL_AGENTS_COMPANY);

		if (alertDate != 0) {
			hcu.dateInside(Alert.START_DATE, Alert.END_DATE, alertDate);
		}

		if (personFilter) {
			hcu.eq(Alert.CHANGE_PERSON, ArahantSession.getHSU().getCurrentPerson());
		}

		List<Alert> l = hcu.list();

		//get everybody in same org groups companies as the agent
		List<CompanyBase> comps = ArahantSession.getHSU().createCriteria(CompanyBase.class).joinTo(CompanyBase.ORGGROUPS).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, ArahantSession.getHSU().getCurrentPerson()).list();

		List<String> pids = (List) ArahantSession.getHSU().createCriteria(Person.class).selectFields(Person.PERSONID).joinTo(Person.ORGGROUPASSOCIATIONS).joinTo(OrgGroupAssociation.ORGGROUP).in(OrgGroup.OWNINGCOMPANY, comps).list();

		if (personFilter) {
			pids = new LinkedList();
			pids.add(ArahantSession.getHSU().getCurrentPerson().getPersonId());
		}

		final HibernateCriteriaUtil<Alert> hcu2 = ArahantSession.getHSU().createCriteria(Alert.class).eq(Alert.DISTRIBUTION, Alert.DIST_ALL_EMPLOYEES_FROM_AGENT);

		hcu2.joinTo(Alert.CHANGE_PERSON).in(Person.PERSONID, pids);

		if (alertDate != 0)
			hcu2.dateInside(Alert.START_DATE, Alert.END_DATE, alertDate);

		l.addAll(hcu2.list());

		java.util.Collections.sort(l, new AlertComparator());

		return makeArray(l);

	}

	public static BAlert[] getAllAlertsAgentUserCanEdit() {
		List<Alert> l = ArahantSession.getHSU().createCriteria(Alert.class).eq(Alert.DISTRIBUTION, Alert.DIST_ALL_AGENTS_COMPANY).list();

		//get everybody in same org groups companies as the agent
		List<CompanyBase> comps = ArahantSession.getHSU().createCriteria(CompanyBase.class).joinTo(CompanyBase.ORGGROUPS).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, ArahantSession.getHSU().getCurrentPerson()).list();

		List<String> pids = (List) ArahantSession.getHSU().createCriteria(Person.class).selectFields(Person.PERSONID).joinTo(Person.ORGGROUPASSOCIATIONS).joinTo(OrgGroupAssociation.ORGGROUP).in(OrgGroup.OWNINGCOMPANY, comps).list();


		l.addAll(ArahantSession.getHSU().createCriteria(Alert.class).eq(Alert.DISTRIBUTION, Alert.DIST_ALL_EMPLOYEES_FROM_AGENT).joinTo(Alert.CHANGE_PERSON).in(Person.PERSONID, pids).list());

		java.util.Collections.sort(l, new AlertComparator());

		return makeArray(l);

	}

	public static void deleteAlerts(final HibernateSessionUtil hsu, final String[] alertId) throws ArahantException {
		for (final String element : alertId)
			new BAlert(element).delete();
	}

	static BAlert[] makeArray(List<Alert> l) {
		BAlert[] ret = new BAlert[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BAlert(l.get(loop));
		return ret;
	}

	static class AlertComparator implements Comparator<Alert>, Serializable {
		@Override
		public int compare(final Alert arg0, final Alert arg1) {
			return (arg0.getStartDate() - arg1.getStartDate());
		}
	}
}
