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
import com.arahant.business.interfaces.IPersonList;
import com.arahant.business.interfaces.RightNames;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;
import com.arahant.utils.Collections;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.*;
import org.hibernate.Query;
import org.kissweb.database.Command;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

public class BusinessLogicBase implements ArahantConstants, RightNames {

	private static final ArahantLogger logger = new ArahantLogger(BusinessLogicBase.class);
	private final HashMap<String, Object> originalData = new HashMap<String, Object>();

	public BusinessLogicBase() {
	}

	static public boolean isEmpty(final String str) {
		return str == null || str.trim().isEmpty();
	}

	public void saveOriginalData(ArahantBean bean) {

		if (bean == null)
			return;

		final Method[] mems = bean.getClass().getMethods();

		for (final Method element : mems) {
			final String name = element.getName();
			if (name.startsWith("get")) {

				if (name.equals("getEventSetDescriptors")
						|| name.equals("getPropertyDescriptors"))
					continue;

				//	final Class pTypes[]=new Class[1];
				//	pTypes[0]=element.getReturnType();
				try {

					Object r1 = element.invoke(bean, (Object[]) null);

					originalData.put(name.substring(3), r1);
				} catch (final Throwable e) {
					continue;
				}
			}
		}
	}

	public List<ChangedFields> compareChangesReturnOldNewValues(ArahantBean b) {
		List<ChangedFields> ret = new LinkedList<ChangedFields>();
		if (b == null)
			return ret;


		final Method[] mems = b.getClass().getMethods();

		for (final Method element : mems) {
			final String name = element.getName();
			if (name.startsWith("get")) {

				if (name.equals("getEventSetDescriptors")
						|| name.equals("getPropertyDescriptors"))
					continue;

				//	final Class pTypes[]=new Class[1];
				//	pTypes[0]=element.getReturnType();
				try {

					Object r1 = element.invoke(b, (Object[]) null);
					Object r2 = originalData.get(name.substring(3));
					//System.out.println("Form Item " + name.substring(3));
					if (!r1.equals(r2)) {

						ChangedFields cf = new ChangedFields();
						cf.setFieldName(name);
						cf.setNewValue(r1.toString());
						cf.setPrevValue(r2.toString());

						logger.info(name.substring(3) + "-" + r1 + "!=" + r2);
						ret.add(cf);
					}
				} catch (final Throwable e) {
					continue;
				}
			}

		}
		return ret;

	}

	public List<ChangedFields> compareChangesReturnOldNewValues(ArahantBean b, Map<String,Object> includedFields) {
		List<ChangedFields> ret = new LinkedList<ChangedFields>();
		if (b == null)
			return ret;

		final Method[] mems = b.getClass().getMethods();

		for (final Method element : mems) {
			final String name = element.getName();
			if (name.startsWith("get")) {
				//System.out.println("Checking field: " + name);
				if (!includedFields.containsKey(name))
					continue;
				//System.out.println("\t found it: " + name);
				//	final Class pTypes[]=new Class[1];
				//	pTypes[0]=element.getReturnType();
				try {

					Object r1 = element.invoke(b, (Object[]) null);
					Object r2 = originalData.get(name.substring(3));
					//System.out.println("Form Item " + name.substring(3));
					if (!r1.equals(r2)) {

						ChangedFields cf = new ChangedFields();
						cf.setFieldName(name.substring(3));
						cf.setNewValue(r1.toString());
						cf.setPrevValue(r2.toString());

						logger.info(name.substring(3) + "-" + r1 + "!=" + r2);
						ret.add(cf);
					}
				} catch (final Throwable ignored) {
				}
			}

		}
		return ret;
	}

	public List<String> compareChanges(ArahantBean b) {
		List<String> ret = new LinkedList<String>();
		if (b == null)
			return ret;

		final Method[] mems = b.getClass().getMethods();

		for (final Method element : mems) {
			final String name = element.getName();
			if (name.startsWith("get")) {

				if (name.equals("getEventSetDescriptors")
						|| name.equals("getPropertyDescriptors"))
					continue;

				//	final Class pTypes[]=new Class[1];
				//	pTypes[0]=element.getReturnType();
				try {

					Object r1 = element.invoke(b, (Object[]) null);
					Object r2 = originalData.get(name.substring(3));

					if (!r1.equals(r2)) {
						logger.info(name.substring(3) + "-" + r1 + "!=" + r2);
						ret.add(name.substring(3));
					}
				} catch (final Throwable ignored) {
				}
			}

		}
		return ret;
	}

	protected Person getCurrentEmployee(final String user) {
		try {
			final ProphetLogin pl = ArahantSession.getHSU().createCriteria(ProphetLogin.class).eq(ProphetLogin.USERLOGIN, user).first();
			return pl.getPerson();
		} catch (final Exception e) {
			return null;
		}
	}

	protected Person getCurrentPerson(final String user) {
		try {
			final HibernateCriteriaUtil<Person> hcu = ArahantSession.getHSU().createCriteria(Person.class);
			hcu.joinTo(Person.PROPHETLOGINS).eq(ProphetLogin.USERLOGIN, user);

			return hcu.first();
		} catch (final Exception e) {
			return null;
		}
	}

	protected static Person getCurrentPerson(final HibernateSessionUtil hsu, final String user) {
		try {
			final HibernateCriteriaUtil<Person> hcu = hsu.createCriteria(Person.class);
			hcu.joinTo(Person.PROPHETLOGINS).eq(ProphetLogin.USERLOGIN, user);

			return hcu.first();
		} catch (final Exception e) {
			return null;
		}
	}

	protected Person getMainContactForOrgGroup(final OrgGroup orgGroup) {
		Connection db = ArahantSession.getKissConnection();
		Command cmd = db.newCommand();
		try {
			Record rec = cmd.fetchOne("select person_id from org_group_association where org_group_id = ? and primary_indicator = 'Y'", orgGroup.getOrgGroupId());
			if (rec == null)
				return null;
			return new BPerson(rec.getString("person_id")).getPerson();
		} catch (Exception e) {
			throw new ArahantException(e);
		}

/*   The following code is dog slow so I re-wrote it in Kiss.
		if (orgGroup.getPrimaryPersons().isEmpty())
			return null;
		Person p = orgGroup.getPrimaryPersons().iterator().next();

		return p;
		/*
		 * return hsu.createCriteria(Person.class)
		 * .joinTo(Person.ORGGROUPASSOCIATIONS)
		 * .eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y')
		 * .eq(OrgGroupAssociation.ORGGROUP, orgGroup) .first();
		 *
		 */
	}

	protected Address getAddress(final Person p, final int type) {
		//  The following caused hibernate proxy errors
//		for (Address addr : p.getAddresses())
//			if (addr.getAddressType() == type)
//				return addr;

		return ArahantSession.getHSU().createCriteria(Address.class).eq(Address.PERSON, p).eq(Address.ADDRESSTYPE, type).first();
		//return null;
		/*
		 * return hsu.createCriteria(Address.class) .eq(Address.PERSON,p)
		 * .eq(Address.ADDRESSTYPE, type) .first();
		 *
		 */
	}

	protected Phone getPhone(final Person p, final int type) {
		if (p == null)
			return null;

		return ArahantSession.getHSU().createCriteria(Phone.class).eq(Phone.PERSON, p).eq(Phone.PHONETYPE, type).first();
	}

	protected static Message internalCreateMessage(final String from_person_id, final String to_person_id, final String message, final String subject) throws ArahantException {
		final Message m = new Message();

		HibernateSessionUtil hsu = ArahantSession.getHSU();

		m.generateId();
		m.setCreatedDate(new java.util.Date());
		m.setMessage(message);
		final Person fromPerson = hsu.get(Person.class, from_person_id);
		final Person toPerson = hsu.get(Person.class, to_person_id);
		m.setPersonByFromPersonId(fromPerson);
		m.setFromShow('Y');
		m.setSubject(subject);
		hsu.saveOrUpdate(m);
		hsu.commitTransaction();
		hsu.beginTransaction();

		final Connection db = new Connection(ArahantSession.getHSU().getConnection());
		final Record rec = db.newRecord("message_to");
		rec.set("message_to_id", IDGenerator.generate("message_to", "message_to_id"));
		rec.set("message_id", m.getMessageId());
		rec.set("to_person_id", toPerson.getPersonId());
		rec.set("send_type", "T");
		rec.set("to_show", "Y");
		rec.set("sent", "Y");
		try {
			rec.addRecord();
		} catch (SQLException throwables) {
			throw new ArahantException(throwables);
		}
		return m;
	}

	protected static boolean isSupervisor(final Person person) {
		for (OrgGroupAssociation oga : person.getOrgGroupAssociations())
			if (oga.getPrimaryIndicator() == 'Y')
				return true;
		return false;
	}

	protected static Person getSupervisor(final Person person) {
		final Person ret = ArahantSession.getHSU().createCriteria(Person.class).joinTo(Person.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y').ne(OrgGroupAssociation.PERSON, person).joinTo(OrgGroupAssociation.ORGGROUP).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, person).first();

		if (ret != null)
			return ret;

		return person; //no supervisor found, they are on their own
	}

	public static List<Person> getSupervisors(final Person person) {
		/*
		 * List<Person>ret= ArahantSession.getHSU().createCriteria(Person.class)
		 * .joinTo(Person.ORGGROUPASSOCIATIONS)
		 * .eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y')
		 * .ne(OrgGroupAssociation.PERSON, person)
		 * .joinTo(OrgGroupAssociation.ORGGROUP)
		 * .joinTo(OrgGroup.ORGGROUPASSOCIATIONS)
		 * .eq(OrgGroupAssociation.PERSON, person) .list();
		 */
		return getSupervisors(person, ArahantSession.getHSU().createCriteria(OrgGroup.class).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, person).list());

		//	return ret;
	}

	protected static List<Person> getSupervisors(Person person, List<OrgGroup> ogs) {
		List<Person> ret = ArahantSession.getHSU().createCriteria(Person.class).joinTo(Person.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y').ne(OrgGroupAssociation.PERSON, person).in(OrgGroupAssociation.ORGGROUP, ogs).list();

		//if I didn't have a supervisor in my groups, look at parents and up
		if (ret.isEmpty()) {
			List<OrgGroup> parents = ArahantSession.getHSU().createCriteria(OrgGroup.class).joinTo(OrgGroup.ORGGROUPHIERARCHIESFORPARENTGROUPID).in(OrgGroupHierarchy.CHILD, ogs).list();

			if (!parents.isEmpty())
				return getSupervisors(person, parents);

		}
		return ret;
	}

	protected static Person getPersonByLoginId(final String user, final HibernateSessionUtil hsu) {
		try {
			final ProphetLogin pl = hsu.createCriteria(ProphetLogin.class).eq(ProphetLogin.USERLOGIN, user).first();
			return pl.getPerson();
		} catch (final Exception e) {
			return null;
		}
	}

	protected static List<Person> getSubordinateList(final boolean includeUser) throws ArahantException {
		final List<Person> ret = new LinkedList<Person>();

		final IPersonList[] pl = getSubordinates(includeUser, true);

		for (final IPersonList element : pl)
			ret.add(ArahantSession.getHSU().get(Person.class, element.getPersonId()));

		return ret;
	}

	public static List<Person> getSubordinateList(final boolean includeUser, final boolean active) throws ArahantException {
		final List<Person> ret = new LinkedList<Person>();

		final IPersonList[] pl = getSubordinates(includeUser, active);

		for (final IPersonList element : pl)
			ret.add(ArahantSession.getHSU().get(Person.class, element.getPersonId()));

		return ret;
	}

	protected static IPersonList[] getSubordinates(final boolean includeUser) throws ArahantException {
		return getSubordinates(includeUser, true);
	}

	@SuppressWarnings("unchecked")
	public static List<String> getSubordinateIds(boolean includeUser, final boolean active) throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		final Person currentUser = hsu.getCurrentPerson();

		if (BRight.checkRight(SEE_ALL_EMPLOYEES_IN_LISTS) == ACCESS_LEVEL_WRITE) {

			final HibernateCriteriaUtil hcu = hsu.createCriteria(Employee.class).selectFields(Person.PERSONID);

			final List<String> plist = hcu.list();

			if (!includeUser)
				plist.remove(currentUser.getPersonId());

			//always remove the Arahant user
		//	plist.remove(getPersonByLoginId(ARAHANT_SUPERUSER, hsu).getPersonId());
			plist.remove("00000-0000000000");

			return plist;
		}

		if (BRight.checkRight(SEE_SUBORDINATES_IN_LISTS) != ACCESS_LEVEL_WRITE) {
			final List<String> personList = new LinkedList<String>();
			personList.add(currentUser.getPersonId());
			return personList;
		}

		if (currentUser.getOrgGroupAssociations() != null) {
			final Iterator<OrgGroupAssociation> orgGroupAssocItr = currentUser.getOrgGroupAssociations().iterator();

			final Set<Person> plist = new HashSet<Person>();

			while (orgGroupAssocItr.hasNext()) {
				final OrgGroupAssociation oga = (OrgGroupAssociation) orgGroupAssocItr.next();

				if (oga.getPrimaryIndicator() != 'Y')
					continue;

				plist.addAll(getAllPeopleInOrgGroupHierarchy(hsu, oga.getOrgGroup(), active));
			}

			if (!includeUser)
				plist.remove(currentUser);

			final Iterator<Person> plistItr = plist.iterator();

			final List<String> retl = new ArrayList<String>(plist.size());


			while (plistItr.hasNext())
				retl.add(plistItr.next().getPersonId());
			return retl;
		}

		return new LinkedList<String>();
	}

	@SuppressWarnings("unchecked")
	protected static IPersonList[] getSubordinates(boolean includeUser, final boolean active) throws ArahantException {
		PersonList[] personList = null;
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		final Person currentUser = hsu.getCurrentPerson();

		if (BRight.checkRight(SEE_ALL_EMPLOYEES_IN_LISTS) == ACCESS_LEVEL_WRITE) {

			final int yesterday = DateUtils.getDate(DateUtils.getYesterday());

			//TODO:  We want to return inactive employees from the last 60 days as well, not active only
			final Query q = hsu.createQuery("select distinct emp from Employee emp"
					+ " join emp." + Employee.HREMPLSTATUSHISTORIES + " hist "
					+ " join emp." + Employee.ORGGROUPASSOCIATIONS + " oga "
					+ "where "
					+ "  oga." + OrgGroupAssociation.ORGGROUP + "." + OrgGroup.OWNINGCOMPANY + "." + OrgGroup.ORGGROUPID + "='" + hsu.getCurrentCompany().getOrgGroupId() + "' and "
					+ " hist.hrEmployeeStatus.active='Y' and "
					+ "(hist.effectiveDate=(select max(effectiveDate) from HrEmplStatusHistory hist2 where hist2.employee=emp)"
					+ " or hist.effectiveDate=(select max(effectiveDate) from HrEmplStatusHistory hist3 where hist3.employee=emp "
					+ "and hist3.hrEmployeeStatus.active='Y' and hist3.effectiveDate >= " + yesterday + "))");
			//final HibernateCriteriaUtil<Employee> hcu=hsu.createCriteria(Employee.class).in(Employee.PERSONID, getShowEmployeeList(hsu)).distinct();


			q.setMaxResults(100);

			final List<Employee> plist = q.list();

			if (!includeUser && currentUser instanceof Employee)
				plist.remove((Employee) currentUser);

			personList = new PersonList[plist.size()];

			int index = 0;

			for (Employee employee : plist)
				personList[index++] = new PersonList(employee);

		} else if (BRight.checkRight(SEE_SUBORDINATES_IN_LISTS) != ACCESS_LEVEL_WRITE) {
			personList = new PersonList[1];
			personList[0] = new PersonList(currentUser);
		} else if (currentUser.getOrgGroupAssociations() != null) {
			final Iterator<OrgGroupAssociation> orgGroupAssocItr = currentUser.getOrgGroupAssociations().iterator();

			final Set<Person> plist = new HashSet<Person>();

			while (orgGroupAssocItr.hasNext()) {
				final OrgGroupAssociation oga = (OrgGroupAssociation) orgGroupAssocItr.next();

				if (oga.getPrimaryIndicator() != 'Y')
					continue;

				plist.addAll(getAllPeopleInOrgGroupHierarchy(hsu, oga.getOrgGroup(), active));
			}

			if (!includeUser)
				plist.remove(currentUser);

			int index = 0;
			personList = new PersonList[plist.size()];

			for (Person person : plist)
				personList[index++] = new PersonList(person);
		}
		return sortPersonList(personList);
	}

	@SuppressWarnings("unchecked")
	public static List<String> getDirectSubordinateIds(boolean includeUser, final boolean active) throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		final Person currentUser = hsu.getCurrentPerson();

		if (BRight.checkRight(SEE_SUBORDINATES_IN_LISTS) != ACCESS_LEVEL_WRITE) {
			final List<String> personList = new LinkedList<String>();
			personList.add(currentUser.getPersonId());
			return personList;
		}

		if (currentUser.getOrgGroupAssociations() != null) {
			final Iterator<OrgGroupAssociation> orgGroupAssocItr = currentUser.getOrgGroupAssociations().iterator();

			final Set<Person> plist = new HashSet<Person>();

			while (orgGroupAssocItr.hasNext()) {
				final OrgGroupAssociation oga = (OrgGroupAssociation) orgGroupAssocItr.next();

				if (oga.getPrimaryIndicator() != 'Y')
					continue;

				plist.addAll(hsu.createCriteria(Employee.class).activeEmployee().joinTo(Employee.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.ORGGROUP, oga.getOrgGroup()).list());
				//now add people in subgroups with no supervisor
				plist.addAll(getSubEmpsNoSupervisor(hsu, oga.getOrgGroup()));

			}

			if (!includeUser)
				plist.remove(currentUser);

			final Iterator<Person> plistItr = plist.iterator();

			final List<String> retl = new ArrayList<String>(plist.size());

			while (plistItr.hasNext())
				retl.add(plistItr.next().getPersonId());
			return retl;
		}
		return new LinkedList<String>();
	}

	private static List<Employee> getSubEmpsNoSupervisor(HibernateSessionUtil hsu, OrgGroup og) {
		List<Employee> ret = new LinkedList<Employee>();

		for (OrgGroup org : hsu.createCriteria(OrgGroup.class).joinTo(OrgGroup.ORGGROUPHIERARCHIESFORCHILDGROUPID).eq(OrgGroupHierarchy.PARENT, og).list()) {
			//if anybody at this level is a supervisor, skip them all
			List<Employee> l = hsu.createCriteria(Employee.class).joinTo(Employee.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y').eq(OrgGroupAssociation.ORGGROUP, org).list();

			ret.addAll(l);  //add the managers because they are direct reports

			if (!l.isEmpty()) //somebody else is manager of the rest of them
				continue;

			//add the employees
			ret.addAll(hsu.createCriteria(Employee.class).joinTo(Employee.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.ORGGROUP, org).eq(OrgGroupAssociation.PRIMARYINDICATOR, 'N').list());

			//check all the children of this one
			ret.addAll(getSubEmpsNoSupervisor(hsu, org));
		}

		/*
		 * for (Employee e : ret) { System.out.println(e.getNameLFM()); }
		 */
		return ret;
	}

	protected static Set<Person> getAllPeopleInOrgGroupHierarchy(final HibernateSessionUtil hsu, final OrgGroup og) throws ArahantException {
		return getAllPeopleInOrgGroupHierarchy(hsu, og, true);
	}

	protected static Set<Person> getAllPeopleInOrgGroupHierarchy(final HibernateSessionUtil hsu, final OrgGroup og, final boolean active) throws ArahantException {
		final Set<String> ids = new BOrgGroup(og).getAllPersonIdsForOrgGroupHierarchy(active);

		return new HashSet<Person>(hsu.createCriteria(Person.class).in(Person.PERSONID, ids).distinct().setMaxResults(100).list());
	}

	public static boolean isActive(final HibernateSessionUtil hsu, final Person employee) {
//		if the latest status is an active type, then return true

		HrEmplStatusHistory hr = hsu.createCriteria(HrEmplStatusHistory.class).eq(HrEmplStatusHistory.EMPLOYEE, employee).le(HrEmplStatusHistory.EFFECTIVEDATE, DateUtils.now()).orderByDesc(HrEmplStatusHistory.EFFECTIVEDATE).first();

		//If they were inactive today, see what they were yesterday
		//inactive only really counts the day after
		if (hr != null && hr.getHrEmployeeStatus() != null && hr.getEffectiveDate() == DateUtils.now() && hr.getHrEmployeeStatus().getActive() != 'Y')
			hr = hsu.createCriteria(HrEmplStatusHistory.class).eq(HrEmplStatusHistory.EMPLOYEE, employee).le(HrEmplStatusHistory.EFFECTIVEDATE, DateUtils.now() - 1).orderByDesc(HrEmplStatusHistory.EFFECTIVEDATE).first();

		if (hr == null || hr.getHrEmployeeStatus() == null)
			return true; //TODO: someday change this - like this until we get a status for everybody
		return hr.getHrEmployeeStatus().getActive() == 'Y';
	}

	protected static IPersonList[] sortPersonList(final IPersonList[] pl) {
		if (pl == null || pl.length == 0)
			return pl;

		final List<IPersonList> people = new LinkedList<IPersonList>();
		Collections.addAll(people, pl);
		people.sort(new PersonListComparator());

		int index = 0;
		for (IPersonList person : people)
			pl[index++] = person;

		return pl;
	}

	/**
	 *
	 * protected static void addInactiveEmployeeFilter(final
	 * HibernateCriteriaUtil hcu) { hcu.makeCriteria().or(
	 * hcu.makeCriteria().gt(Employee.INACTIVEDATE,
	 * DateUtils.getDate(DateUtils.getSixtyDaysAgo())),
	 * hcu.makeCriteria().eq(Employee.INACTIVEDATE,0)) .add();
	 *
	 * hcu.joinTo(Employee.HREMPLSTATUSHISTORIES)
	 * .joinTo(HrEmplStatusHistory.HREMPLOYEESTATUS)
	 * .eq(HrEmployeeStatus.ACTIVE, 'N') }
	 */
	private static class PersonListComparator implements Comparator<IPersonList>, Serializable {

		private static final long serialVersionUID = 7379078855864831097L;

		@Override
		public int compare(final IPersonList arg0, final IPersonList arg1) {
			final PersonList g1 = (PersonList) arg0;
			final PersonList g2 = (PersonList) arg1;

			if (g1.getLname() == null)
				g1.setLname("");
			if (g2.getLname() == null)
				g2.setLname("");
			if (g1.getFname() == null)
				g1.setFname("");
			if (g2.getFname() == null)
				g2.setFname("");

			int ret = g1.getLname().compareTo(g2.getLname());

			if (ret == 0)
				ret = g1.getFname().compareTo(g2.getFname());

			return ret;
		}
	}

	private static final class PersonList implements IPersonList {

		private int orgGroupType;
		private String hasLogin;
		private String personId;
		private String lname;
		private String fname;

		public PersonList() {
		}

		public PersonList(final Person p) {
			personId = p.getPersonId();
			lname = p.getLname();
			fname = p.getFname();

			orgGroupType = p.getOrgGroupType();

			if (p.getProphetLogin() != null)
				setHasLogin("Y");
			else
				setHasLogin("N");
		}

		@Override
		public Person makePerson(final Person p) {
			if (p == null)
				return null;
			p.setPersonId(personId);
			p.setLname(lname);
			p.setFname(fname);
			return p;
		}
		// Property accessors

		@Override
		public int getOrgGroupType() {
			return orgGroupType;
		}

		@Override
		public void setOrgGroupType(final int orgGroupType) {
			this.orgGroupType = orgGroupType;
		}

		@Override
		public String getHasLogin() {
			return hasLogin;
		}

		@Override
		public void setHasLogin(final String hasLogin) {
			this.hasLogin = hasLogin;
		}

		@Override
		public String getFname() {
			return fname;
		}

		/**
		 * @param fname The fname to set.
		 */
		@Override
		public void setFname(final String fname) {
			this.fname = fname;
		}

		/**
		 * @return Returns the lname.
		 */
		@Override
		public String getLname() {
			return lname;
		}

		/**
		 * @param lname The lname to set.
		 */
		@Override
		public void setLname(final String lname) {
			this.lname = lname;
		}

		/**
		 * @return Returns the personId.
		 */
		@Override
		public String getPersonId() {
			return personId;
		}

		/**
		 * @param personId The personId to set.
		 */
		@Override
		public void setPersonId(final String personId) {
			this.personId = personId;
		}
	}
}
