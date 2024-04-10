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
import com.arahant.lisp.ABCL;
import com.arahant.reports.OrgGroupProjectStatusReport;
import com.arahant.utils.*;
import org.kissweb.database.Command;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.util.*;
import java.util.Collections;

public class BOrgGroup extends BusinessLogicBase implements IDBFunctions {

	private static final transient ArahantLogger logger = new ArahantLogger(BOrgGroup.class);
	OrgGroup orgGroup;
	private Address address;
	private Phone mainPhone;
	private Phone mainFax;

	public static String[] getAllCodes(String[] excludeCodes) {
		HashSet<String> codes = new HashSet<>();

		for (OrgGroup og : ArahantSession.getHSU().createCriteria(OrgGroup.class).eq(OrgGroup.ORGGROUPTYPE, COMPANY_TYPE).notIn(OrgGroup.EXTERNAL_REF, excludeCodes).list())
			if (og.getExternalId() != null)
				codes.add(og.getExternalId());

		ArrayList<String> al = new ArrayList<>(codes.size());

		al.addAll(codes);

		Collections.sort(al);

		return al.toArray(new String[0]);
	}

	public BOrgGroup() {
	}

	public BOrgGroup(BCompanyBase comp) {
		orgGroup = comp.company_base;
	}

	public BOrgGroup(final String key) {
		if (key.equals("ReqOrg")) {
			orgGroup = new OrgGroup();
			orgGroup.setName("Requesting Org Group");
			orgGroup.setOrgGroupId("ReqOrg");
		} else
			internalLoad(key);
	}

	public BOrgGroup(final OrgGroup og) {
		orgGroup = og;
	}

	public void destroy() throws ArahantDeleteException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		// these are cascaded
		//hsu.delete(orgGroup.getAddresses());
		//hsu.delete(orgGroup.getPhones());

		//remove associations
		if (orgGroup.getOrgGroupAssociations() != null) {
			for (OrgGroupAssociation oga : orgGroup.getOrgGroupAssociations()) {
				final Person p = oga.getPerson();
				hsu.delete(oga);
				try {
					final BPerson bp = new BPerson(p);
					bp.delete();
				} catch (final Exception e) {
					//could be multiple delete of same thing
				}
			}
		}
		//remove links to parent
		hsu.delete(orgGroup.getOrgGroupHierarchiesForChildGroupId());


		//remove children
		if (orgGroup.getOrgGroupHierarchiesForParentGroupId() != null) {
			for (OrgGroupHierarchy ogh : orgGroup.getOrgGroupHierarchiesForParentGroupId()) {
				final BOrgGroup bog = new BOrgGroup(ogh.getOrgGroupByChildGroupId());
				bog.destroy();
			}
		}

		//ok, remove the group now
		hsu.delete(orgGroup);
	}

	public static void delete(final HibernateSessionUtil hsu, final String [] ids) throws ArahantException {
		for (final String element : ids)
			new BOrgGroup(element).delete();
	}

	public void assignToThisGroup(final String childGroupID) throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		final OrgGroup c = hsu.get(OrgGroup.class, childGroupID);
		if (orgGroup.getOrgGroupType() != c.getOrgGroupType())
			throw new ArahantException("Can't associate different types of groups.");


		if (c.getOwningCompany() == null)
			c.setOwningCompany(orgGroup.getOwningCompany());

		if (orgGroup instanceof CompanyBase)
			c.setOwningCompany((CompanyBase) orgGroup);


		if (c.getOwningCompany() != null && orgGroup.getOwningCompany() != null && !c.getOwningCompany().getOrgGroupId().equals(orgGroup.getOwningCompany().getOrgGroupId()))
			throw new ArahantException("Can't associatiate different company groups");


		final OrgGroupHierarchyId oghi = new OrgGroupHierarchyId(orgGroup.getOrgGroupId(), c.getOrgGroupId());
		final OrgGroupHierarchy ogh = new OrgGroupHierarchy();
		ogh.setId(oghi);
		ogh.setOrgGroupType(c.getOrgGroupType());
		ogh.setOrgGroupByParentGroupId(orgGroup);
		ogh.setOrgGroupByChildGroupId(c);

		hsu.saveOrUpdate(ogh);
	}

	public void assignToThisGroup(final String[] orgGroupIds) throws ArahantException {
		for (final String element : orgGroupIds)
			assignToThisGroup(element);
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#create()
	 */
	@Override
	public String create() throws ArahantException {
		orgGroup = new OrgGroup();
		orgGroup.generateId();
		return orgGroup.getOrgGroupId();
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#delete()
	 */
	@Override
	public void delete() throws ArahantDeleteException {
		//if I get a delete and I'm a company, do the company routine

		try {

			if (orgGroup instanceof CompanyBase) {
				if (orgGroup instanceof ClientCompany)
					new BClientCompany((ClientCompany) orgGroup).delete();

				if (orgGroup instanceof VendorCompany)
					new BVendorCompany((VendorCompany) orgGroup).delete();

				if (orgGroup instanceof CompanyDetail)
					new BCompany((CompanyDetail) orgGroup).delete();

				return;
			}
			HibernateSessionUtil hsu = ArahantSession.getHSU();
			List<ArahantBean> dels = new LinkedList<>();
			dels.addAll(orgGroup.getAddresses());
			dels.addAll(orgGroup.getPhones());
			orgGroup.getPhones().clear();
			orgGroup.getAddresses().clear();
			hsu.delete(dels);

			hsu.delete(orgGroup.getOrgGroupAssociations());
			hsu.delete(orgGroup.getOrgGroupHierarchiesForChildGroupId());
			hsu.delete(orgGroup.getOrgGroupHierarchiesForParentGroupId());
			if (orgGroup.getVendorGroups() != null)
				hsu.delete(orgGroup.getVendorGroups());

			hsu.delete(orgGroup);
		} catch (final ArahantDeleteException e) {
			throw e;
		} catch (final Exception e) {
			throw new ArahantDeleteException();
		}
	}

	public String getCompanyName() {
		try {
			if (!isCompany())
				return orgGroup.getOwningCompany().getName();
			else
				return getName();

		} catch (Exception e) {
			return "";
		}
	}

	public String getDefaultPayScheduleName() {
		PaySchedule sched = this.getPaySchedule();

		if (sched == null)
			return "";
		else
			return sched.getScheduleName();
	}

	public String getExplicitPayScheduleId() {
		if (orgGroup.getPaySchedule() == null)
			return "";
		return orgGroup.getPaySchedule().getPayScheduleId();
	}

	public String getExternalId() {
		return orgGroup.getExternalId();
	}

	public String getInheritedDefaultPayScheduleName() {
		for (OrgGroupHierarchy ogh : orgGroup.getOrgGroupHierarchiesForChildGroupId()) {
			PaySchedule ps = getPaySchedule(ogh.getOrgGroupByParentGroupId());
			if (ps != null)
				return ps.getScheduleName();
		}
		return "";
	}

	public String getInheritedDefaultProjectId() {
		//try a parent
		for (OrgGroupHierarchy ogh : orgGroup.getOrgGroupHierarchiesForChildGroupId()) {
			Project proj = getDefaultProject(ogh.getOrgGroupByParentGroupId());
			if (proj != null)
				return proj.getProjectId();
		}
		return "";
	}

	public int getMaxDepth() {
		//my max depth is 1 + max depth of my children
		int max = 0;
		for (OrgGroupHierarchy ogh : orgGroup.getOrgGroupHierarchiesForParentGroupId()) {
			int t = new BOrgGroup(ogh.getOrgGroupByChildGroupId()).getMaxDepth();
			if (t > max)
				max = t;
		}
		return max + 1;
	}

	public int getNonInheritedPayPeriodsPerYear() {
		return orgGroup.getPayPeriodsPerYear();
	}
	
	/**
	 * The following code returns a list of the given org group and all of its parents.
	 * The list is in breadth-first order.  It correctly handles multiple parents
	 * and looping hierarchies.
	 * 
	 * The starting group is first on the list, its parents are next, and then
	 * their parents and so on.
	 * 
	 * This routine may not actually do what you want in multiple inheritance 
	 * situations because the group hierarchy table is not ordered.
	 */
	public LinkedList<OrgGroup> getOrgGroupHierarchyBreadthFirst() {
		LinkedList<OrgGroup> q = new LinkedList<OrgGroup>();
		LinkedList<OrgGroup> lst = new LinkedList<OrgGroup>();
		q.add(orgGroup);
		lst.add(orgGroup);
		while (!q.isEmpty()) {
			OrgGroup og = q.remove();
			Set<OrgGroupHierarchy> oghs = og.getOrgGroupHierarchiesForChildGroupId();
			for (OrgGroupHierarchy ogh : oghs) {
				og = ogh.getOrgGroupByParentGroupId();
				String ogId = og.getOrgGroupId();
				boolean inList = false;
				for (OrgGroup t : lst)
					if (t.getOrgGroupId().equals(ogId)) {
						inList = true;
						break;
					}
				if (!inList) {
					q.add(og);
					lst.add(og);
				}
			}
		}
		return lst;
	}
	
	/**
	 * Get the timesheetPeriodType.  If it is marked as inherited do a breadth first 
	 * search up the org group hierarchy.
	 */
	public char getInheritableTimesheetPeriodType() {
		char pt = orgGroup.getTimesheetPeriodType();
		if (pt != 'I')
			return pt;
		LinkedList<OrgGroup> lst = getOrgGroupHierarchyBreadthFirst();
		lst.remove();  //  remove the one we already checked
		for (OrgGroup og : lst) {
			pt = og.getTimesheetPeriodType();
			if (pt != 'I')
				break;
		}
		return pt;
	}
	
	/**
	 * Get the timesheetPeriodStartDate.  If it is 0 do a breadth first 
	 * search up the org group hierarchy.
	 */
	public int getInheritableTimesheetPeriodStartDate() {
		int sd = orgGroup.getTimesheetPeriodStartDate();
		if (sd != 0)
			return sd;
		LinkedList<OrgGroup> lst = getOrgGroupHierarchyBreadthFirst();
		lst.remove();  //  remove the one we already checked
		for (OrgGroup og : lst) {
			sd = og.getTimesheetPeriodStartDate();
			if (sd != 0)
				break;
		}
		return sd;
	}
	
	/**
	 * Get the timesheetPeriodType.  If it is marked as inherited do a breadth first 
	 * search up the org group hierarchy.
	 */
	public char getInheritableTimesheetShowBillable() {
		char pt = orgGroup.getTimesheetShowBillable();
		if (pt != 'I')
			return pt;
		LinkedList<OrgGroup> lst = getOrgGroupHierarchyBreadthFirst();
		lst.remove();  //  remove the one we already checked
		for (OrgGroup og : lst) {
			pt = og.getTimesheetShowBillable();
			if (pt != 'I')
				break;
		}
		return pt;
	}

	/**
	 * Just return a parent, could be multiples, but we can just return 1
	 */
	public BOrgGroup getParent() {
		try {
			if (orgGroup.getOrgGroupHierarchiesForChildGroupId().isEmpty())
				return null;
			return new BOrgGroup(orgGroup.getOrgGroupHierarchiesForChildGroupId().iterator().next().getOrgGroupByParentGroupId());
		} catch (Exception e) {
			return null;
		}
	}

	public void setExternalId(final String externalId) {
		orgGroup.setExternalId(externalId);
	}

	public String getId() {
		return orgGroup.getOrgGroupId();
	}

	public String getNameFormatted() {
		if (orgGroup.getOrgGroupId() == null || orgGroup.getOrgGroupId().equals("ReqOrg"))
			return "Requesting Company (Requesting Organizational Group)";
		if (orgGroup.getOwningCompany().getOrgGroupId().equals(orgGroup.getOrgGroupId()))
			return orgGroup.getName();
		return orgGroup.getOwningCompany().getName() + " (" + orgGroup.getName() + ")";
	}

	public String getNameFormattedDashes() {
		if (orgGroup.getOrgGroupId() == null || orgGroup.getOrgGroupId().equals("ReqOrg"))
			return "Requesting Company - Requesting Organizational Group";
		if (orgGroup.getOwningCompany().getOrgGroupId().equals(orgGroup.getOrgGroupId()))
			return orgGroup.getName();
		return orgGroup.getOwningCompany().getName() + " - " + orgGroup.getName();
	}

	public OrgGroup getOrgGroup() {
		return orgGroup;
	}

	public String getOrgGroupHierarchyString() {
		/*
		 * - e.g. "ADS > ADS 2 > Nashville Installers (215)" or "Arahant, LLC >
		 * Quality Assurance" - rules: - walk up from the org group to top and
		 * include the company org group - concatenate org groups via " > " - if
		 * an org group has a non-empty code field, add "(code)", otherwise
		 * don't (not even parentheses) - if org group has multiple parents,
		 * just select one for now till we get a better display (eventually may
		 * do a tree or something)
		 */
		String ret = "";//orgGroup.getName();

		//if (!isEmpty(orgGroup.getExternalId()))
		//	ret+=" ("+orgGroup.getExternalId()+")";

		OrgGroup current = orgGroup;
		boolean first = true;
		while (current.getOrgGroupHierarchiesForChildGroupId().size() > 0) {
			current = current.getOrgGroupHierarchiesForChildGroupId().iterator().next().getOrgGroupByParentGroupId();

			String t = current.getName();

			if (!isEmpty(current.getExternalId()))
				t += " (" + current.getExternalId() + ")";

			if (first)
				ret = t;
			else
				ret = t + " > " + ret;

			first = false;

		}
		return ret;
	}

	public String getTypeFormatted() {
		switch (orgGroup.getOrgGroupType()) {
			case COMPANY_TYPE:
				return "Company";
			case CLIENT_TYPE:
				return "Client";
			case VENDOR_TYPE:
				return "Vendor";
			default:
				return "";
		}
	}

	@Override
	public void insert() throws ArahantException {
		if (!orgGroup.getOrgGroupId().equals("ReqOrg")) {
			HibernateSessionUtil hsu = ArahantSession.getHSU();
			hsu.insert(orgGroup);

			if (address != null)
				hsu.insert(address);
			if (mainPhone != null)
				hsu.insert(mainPhone);
			if (mainFax != null)
				hsu.insert(mainFax);
		}
	}

	public boolean isCompany() {
		if (orgGroup.getOrgGroupId() == null || orgGroup.getOrgGroupId().equals("ReqCo"))
			return (orgGroup.getName().contains("Requesting Company"));
		return orgGroup.getOwningCompany() == null || orgGroup.getOwningCompany().getOrgGroupId().equals(orgGroup.getOrgGroupId());
	}

	public BPerson[] listPeople(int cap) {
		Connection db = ArahantSession.getKissConnection();
		Command cmd = db.newCommand();
		List<Record> recs;
		try {
			recs = cmd.fetchAll(cap, "select oga.person_id " +
					"from org_group_association oga " +
					"join person p " +
					" on oga.person_id = p.person_id " +
					"where oga.org_group_id = ? " +
					"order by oga.primary_indicator desc, p.lname, p.fname ", orgGroup.getOrgGroupId());
			BPerson [] bpa = new BPerson[recs.size()];
			int i = 0;
			for (Record rec : recs)
				bpa[i++] = new BPerson(rec.getString("person_id"));
			return bpa;
		} catch (Exception e) {
			throw new ArahantException(e);
		}

		/*  the following old code was replaced because is very slow
		HibernateCriteriaUtil<Person> hcu = ArahantSession.getHSU().createCriteria(Person.class);
		if (cap > 0)
			hcu.setMaxResults(cap);
		hcu.joinTo(Person.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.ORGGROUP, orgGroup).orderByDesc(OrgGroupAssociation.PRIMARYINDICATOR);
		hcu.orderBy(Person.LNAME).orderBy(Person.FNAME);
		return BPerson.makeArray(hcu.list());
		 */
	}

	public BPerson[] listPeople2(int cap) {
		Connection db = ArahantSession.getKissConnection();
		Command cmd = db.newCommand();
		List<Record> recs;
		try {
				recs = cmd.fetchAll(cap, "select oga.person_id " +
						"from org_group_association oga " +
						"join person p " +
						" on oga.person_id = p.person_id " +
						"where oga.org_group_id = ? " +
						"order by oga.primary_indicator desc, p.lname, p.fname ", orgGroup.getOrgGroupId());
				BPerson [] bpa = new BPerson[recs.size()];
				int i = 0;
				for (Record rec : recs)
					bpa[i++] = new BPerson(rec.getString("person_id"));
				return bpa;
		} catch (Exception e) {
			throw new ArahantException(e);
		}

		/*  the following old code was replaced because is very slow
		HibernateCriteriaUtil<Person> hcu = ArahantSession.getHSU().createCriteria(Person.class);
		if (cap > 0)
			hcu.setMaxResults(cap);
		hcu.joinTo(Person.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.ORGGROUP, orgGroup).orderBy(OrgGroupAssociation.PRIMARYINDICATOR);
		hcu.orderBy(Person.LNAME).orderBy(Person.FNAME);
		return BPerson.makeArray(hcu.list());
		 */
	}

	public void setPayScheduleId(String payScheduleId) {
		orgGroup.setPaySchedule(ArahantSession.getHSU().get(PaySchedule.class, payScheduleId));
	}

	@Override
	public void update() throws ArahantException {
		if (!orgGroup.getOrgGroupId().equals("ReqOrg")) {
			HibernateSessionUtil hsu = ArahantSession.getHSU();
			hsu.saveOrUpdate(orgGroup);

			if (address != null)
				hsu.saveOrUpdate(address);

			if (mainPhone != null)
				hsu.saveOrUpdate(mainPhone);

			if (mainFax != null)
				hsu.saveOrUpdate(mainFax);
		}
	}

	private void internalLoad(final String key) {
		logger.debug("Loading " + key);
		orgGroup = ArahantSession.getHSU().get(OrgGroup.class, key);
		if (orgGroup == null)
			throw new ArahantException("Failed to load org group with key " + key);
	}

	@Override
	public void load(final String key) {
		internalLoad(key);
	}

	public static BOrgGroup[] listAssociatedGroups(final HibernateSessionUtil hsu, final String groupId, final int groupType) {
		return listAssociatedGroups(hsu, groupId, groupType, "%");
	}

	@SuppressWarnings("unchecked")
	public static BOrgGroup[] listAssociatedOrgGroups(final HibernateSessionUtil hsu, final String groupId, final int groupType, final String name, final int cap) {

		BOrgGroup[] ret;

		Set<String> compIds = getAllowedOrgGroupIds();

		if (!isEmpty(groupId)) {
			final HibernateCriteriaUtil<OrgGroup> hcu = hsu.createCriteriaNoCompanyFilter(OrgGroup.class).orderBy(OrgGroup.NAME);

			hcu.joinTo(OrgGroup.ORGGROUPHIERARCHIESFORCHILDGROUPID).joinTo(OrgGroupHierarchy.ORGGROUPBYPARENTGROUPID).eq(OrgGroup.ORGGROUPID, groupId).setMaxResults(cap);

			hcu.like(OrgGroup.NAME, name);

			ret = BOrgGroup.makeArray(filterOrgGroupsThatAreParents(hcu.list(), hsu.get(OrgGroup.class, groupId)));

		} else {
			//I need all top level org groups
			//that would be any org group that doesn't have a
			//OrgGroupHierarchy where it is the child

			final HibernateCriteriaUtil<OrgGroup> hcuOg = hsu.createCriteriaNoCompanyFilter(OrgGroup.class).orderBy(OrgGroup.NAME).eq(OrgGroup.ORGGROUPTYPE, groupType).sizeEq(OrgGroup.ORGGROUPHIERARCHIESFORCHILDGROUPID, 0).setMaxResults(cap);

			hcuOg.like(OrgGroup.NAME, name);


			if (!hsu.currentlyArahantUser())
				hcuOg.in(OrgGroup.ORGGROUPID, compIds);

			List<OrgGroup> spa = hcuOg.list();

			//if this list is empty, need to list company groups employee is in

			if (spa.isEmpty())
				spa = (List) hsu.createCriteriaNoCompanyFilter(CompanyDetail.class).orderBy(CompanyDetail.NAME).joinTo(CompanyDetail.ORGGROUPS).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, hsu.getCurrentPerson()).list();

			//remove any that are just floating
			final LinkedList<OrgGroup> res = new LinkedList<OrgGroup>();

			for (OrgGroup og : spa) {
				if (og.getOwningCompany() == null || og.getOwningCompany().getOrgGroupId().equals(og.getOrgGroupId()))
					res.add(og);
			}
			ret = BOrgGroup.makeArray(res);
		}
		return ret;
	}

	public static BOrgGroup[] listAssociatedGroups(final HibernateSessionUtil hsu, final String groupId, final int groupType, final String name) {
		BOrgGroup[] ret;

		/* From Blake:  This section was a mess because there was confusion about what org_group.owning_entity_id meant.
						There was an errant attempt to make it mean "tenant" but that column originally just meant
						"parent group".  So, originally, a null parent_group meant it was a top-level group.
						I am changing the code to go back to its original meaning.  This breaks some of the multi-tenant
						code but fixes the single-tenant code.

						In the future, if multi-tenant support is desired, a new column will have to be added titled "tenant_id".

						Also, I am re-writing this to use KISS because I just can't get hibernate to do what I want.
		 */

		Connection db = KissConnection.get();
		Command cmd = db.newCommand();
		ArrayList<Object> args = new ArrayList<>();
		String select;

		if (groupId != null && !groupId.isEmpty()) {
			select = "select og.* " +
					"from org_group og " +
					"inner join org_group_hierarchy ogh " +
					"  on og.org_group_id = ogh.child_group_id " +
					"where og.org_group_type = ? and ogh.parent_group_id = ? " +
					"      and og.group_name like ? ";
			args.add(groupType);
			args.add(groupId);
			args.add(name);
		} else {
			select = "select og.* from org_group og " +
					"where og.org_group_type = ? and og.group_name like ? " +
					"and (og.owning_entity_id is null OR og.owning_entity_id = og.org_group_id) ";
			args.add(groupType);
			args.add(name);
		}

		select += "order by og.group_name";

		try {
			List<Record> recs = cmd.fetchAll(select, args);
			ret = new BOrgGroup[recs.size()];
			int i = 0;
			for (Record rec : recs)
				ret[i++] = new BOrgGroup(rec.getString("org_group_id"));
		} catch (Exception throwables) {
			throw new ArahantException(throwables);
		}

		return ret;
	}

	@SuppressWarnings("unchecked")
	public static BOrgGroup[] listAssociatedOrgGroups(final HibernateSessionUtil hsu, final String groupId, final String folderId, final int groupType, final String name, final String[] excludeIds, final int cap) {

		BOrgGroup[] ret;

		Set<String> compIds = getAllowedOrgGroupIds();

		if (!isEmpty(groupId)) {
			final HibernateCriteriaUtil<OrgGroup> hcu = hsu.createCriteria(OrgGroup.class).orderBy(OrgGroup.NAME);

			hcu.joinTo(OrgGroup.ORGGROUPHIERARCHIESFORCHILDGROUPID).joinTo(OrgGroupHierarchy.ORGGROUPBYPARENTGROUPID).eq(OrgGroup.ORGGROUPID, groupId).setMaxResults(cap);

			hcu.notIn(OrgGroup.ORGGROUPID, excludeIds);
			hcu.like(OrgGroup.NAME, name);

			ret = BOrgGroup.makeArray(filterOrgGroupsThatAreParents(hcu.list(), hsu.get(OrgGroup.class, groupId)));

		} else {
			//I need all top level org groups
			//that would be any org group that doesn't have a
			//OrgGroupHierarchy where it is the child

			BCompanyFormFolder bcf = new BCompanyFormFolder(folderId);

			final HibernateCriteriaUtil<OrgGroup> hcuOg = hsu.createCriteria(OrgGroup.class).eq(OrgGroup.ORGGROUPID, bcf.getBean().getOrgGroup().getOrgGroupId()).orderBy(OrgGroup.NAME).eq(OrgGroup.ORGGROUPTYPE, groupType).notIn(OrgGroup.ORGGROUPID, excludeIds).sizeEq(OrgGroup.ORGGROUPHIERARCHIESFORCHILDGROUPID, 0).setMaxResults(cap);

			hcuOg.like(OrgGroup.NAME, name);


			if (!hsu.currentlyArahantUser())
				hcuOg.in(OrgGroup.ORGGROUPID, compIds);

			List<OrgGroup> spa = hcuOg.list();

			//if this list is empty, need to list company groups employee is in

			if (spa.isEmpty())
				spa = (List) hsu.createCriteriaNoCompanyFilter(CompanyDetail.class).orderBy(CompanyDetail.NAME).joinTo(CompanyDetail.ORGGROUPS).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, hsu.getCurrentPerson()).list();


			//remove any that are just floating
			final LinkedList<OrgGroup> res = new LinkedList<OrgGroup>();

			for (OrgGroup og : spa) {
				if (og.getOwningCompany() == null || og.getOwningCompany().getOrgGroupId().equals(og.getOrgGroupId()))
					res.add(og);
			}
			ret = BOrgGroup.makeArray(res);
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	private static Set<String> getAllowedOrgGroupIds() {
		HashSet<String> compIds = new HashSet<>();
		final boolean byPass = true;  //  change this to false once I create the HTML security screens and can manage this correctly

		HibernateSessionUtil hsu = ArahantSession.getHSU();

		if (byPass || BRight.checkRight(Right.RIGHT_CAN_ACCESS_ALL_COMPANIES) == ACCESS_LEVEL_WRITE) {
			HashSet<String> ids = new HashSet<>();
			HibernateScrollUtil<OrgGroup> scr = hsu.createCriteriaNoCompanyFilter(OrgGroup.class).selectFields(OrgGroup.ORGGROUPID).scroll();
			while (scr.next())
				ids.add(scr.getString(0));
			return ids;
		}

		//need to find out what companies they are in or have access to
		List<OrgGroup> orgls = (List) hsu.createCriteriaNoCompanyFilter(CompanyDetail.class)
				.joinTo(CompanyDetail.ORGGROUPS)
				.joinTo(OrgGroup.ORGGROUPASSOCIATIONS)
				.eq(OrgGroupAssociation.PERSON, hsu.getCurrentPerson())
				.list();

		for (OrgGroup og : orgls) {
			BOrgGroup borg = new BOrgGroup(og);
			compIds.add(borg.getOrgGroupId());
			compIds.addAll(borg.getAllOrgGroupsInHierarchy());

			compIds.addAll((List) hsu.createCriteria(OrgGroup.class).selectFields(OrgGroup.ORGGROUPID).joinTo(OrgGroup.OWNINGCOMPANY).eq(CompanyBase.ORGGROUPID, og.getOrgGroupId()).list());
		}

		//now I need to find out what meta groups they are in and all orgId's under them
		orgls = hsu.createCriteriaNoCompanyFilter(OrgGroup.class).isNull(OrgGroup.OWNINGCOMPANY).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, hsu.getCurrentPerson()).list();

		if (ArahantSession.getCurrentPerson() instanceof Agent) {
			BAgent ba = new BAgent((Agent) ArahantSession.getCurrentPerson());
			for (BCompany bo : ba.getAgentCompanies())
				orgls.add(bo.getBean());
		}

		for (OrgGroup og : orgls) {
			BOrgGroup borg = new BOrgGroup(og);
			compIds.add(borg.getOrgGroupId());
			compIds.addAll(borg.getAllOrgGroupsInHierarchy());
		}

		return compIds;
	}

	@SuppressWarnings("unchecked")
	private static Set<String> getAllowedAgencyIds() {
		HashSet<String> compIds = new HashSet<String>();

		HibernateSessionUtil hsu = ArahantSession.getHSU();

		if (BRight.checkRight(Right.RIGHT_CAN_ACCESS_ALL_COMPANIES) == ACCESS_LEVEL_WRITE) {

			HashSet<String> ids = new HashSet<String>();

			ids.addAll((List) hsu.createCriteriaNoCompanyFilter(Agency.class).selectFields(Agency.ORGGROUPID).list());

			return ids;
		}

		//need to find out what companies they are in or have access too
		List<OrgGroup> orgls = (List) hsu.createCriteriaNoCompanyFilter(Agency.class).joinTo(Agency.ORGGROUPS).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, hsu.getCurrentPerson()).list();

		for (OrgGroup og : orgls) {
			BOrgGroup borg = new BOrgGroup(og);
			compIds.add(borg.getOrgGroupId());
			compIds.addAll(borg.getAllOrgGroupsInHierarchy());
		}

		//now I need to find out what meta groups they are in and all orgId's under them
		orgls = hsu.createCriteriaNoCompanyFilter(OrgGroup.class).isNull(OrgGroup.OWNINGCOMPANY).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, hsu.getCurrentPerson()).list();

		for (OrgGroup og : orgls) {
			BOrgGroup borg = new BOrgGroup(og);
			compIds.add(borg.getOrgGroupId());
			compIds.addAll(borg.getAllOrgGroupsInHierarchy());
		}
		return compIds;
	}

	public static BOrgGroup[] listAssociatedCompanyGroups(final HibernateSessionUtil hsu, final String groupId, final int groupType, final int cap) {
		return listAssociatedCompanyGroups(hsu, groupId, groupType, cap, new String[0]);
	}

	@SuppressWarnings("unchecked")
	public static BOrgGroup[] listAssociatedCompanyGroups(final HibernateSessionUtil hsu, final String groupId, final int groupType, final int cap, final String[] excludeIds) {
		BOrgGroup[] ret;

		Set<String> compIds = getAllowedOrgGroupIds();

		if (!isEmpty(groupId)) {
			final HibernateCriteriaUtil<OrgGroup> hcu = hsu.createCriteriaNoCompanyFilter(OrgGroup.class).orderBy(OrgGroup.NAME).joinTo(OrgGroup.ORGGROUPHIERARCHIESFORCHILDGROUPID).joinTo(OrgGroupHierarchy.ORGGROUPBYPARENTGROUPID).eq(OrgGroup.ORGGROUPID, groupId).setMaxResults(cap);

			ret = BOrgGroup.makeArray(filterOrgGroupsThatAreParents(hcu.list(), hsu.get(OrgGroup.class, groupId)), excludeIds);

		} else {
			//I need all top level org groups
			//that would be any org group that doesn't have a
			//OrgGroupHierarchy where it is the child

			final HibernateCriteriaUtil<OrgGroup> hcuOg = hsu.createCriteriaNoCompanyFilter(OrgGroup.class).orderBy(OrgGroup.NAME).eq(OrgGroup.ORGGROUPTYPE, groupType).sizeEq(OrgGroup.ORGGROUPHIERARCHIESFORCHILDGROUPID, 0).setMaxResults(cap);

			if (!hsu.currentlyArahantUser())
				hcuOg.in(OrgGroup.ORGGROUPID, compIds);

			List<OrgGroup> spa = hcuOg.list();

			//if this list is empty, need to list company groups employee is in

			if (spa.isEmpty())
				spa = (List) hsu.createCriteriaNoCompanyFilter(CompanyDetail.class).orderBy(CompanyDetail.NAME).joinTo(CompanyDetail.ORGGROUPS).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, hsu.getCurrentPerson()).list();

			ret = BOrgGroup.makeArray(spa, excludeIds);
		}
		return ret;
	}

	/**
	 * Return a list of parent org groups ordered from top to bottom, depth first, including the given org group.
	 * This goes up all the way, not just one level.
	 * 
	 * @return The list of org groups
	 */
	public List<OrgGroup> getAllParentOrgGroups() {
		List<OrgGroup> ret = new ArrayList<OrgGroup>();
		for (OrgGroup o : ArahantSession.getHSU().createCriteria(OrgGroup.class).joinTo(OrgGroup.ORGGROUPHIERARCHIESFORPARENTGROUPID).eq(OrgGroupHierarchy.CHILD, orgGroup).list()) {
			BOrgGroup bog = new BOrgGroup(o);
			ret.addAll(bog.getAllParentOrgGroups());
		}
		ret.add(orgGroup);
		return ret;
	}

	/**
	 * @return @see com.arahant.beans.OrgGroup#getName()
	 */
	public String getName() {
		return orgGroup.getName();
	}

	/**
	 * @return @see com.arahant.beans.OrgGroup#getOrgGroupId()
	 */
	public String getOrgGroupId() {
		return orgGroup.getOrgGroupId();
	}

	/**
	 * @return @see com.arahant.beans.OrgGroup#getOrgGroupType()
	 */
	public int getOrgGroupType() {
		return orgGroup.getOrgGroupType();
	}

	public void setName(final String name) {
		orgGroup.setName(name);
	}

	public void setOrgGroupId(final String orgGroupId) {
		orgGroup.setOrgGroupId(orgGroupId);
	}

	public void setOrgGroupType(final int orgGroupType) {
		orgGroup.setOrgGroupType(orgGroupType);
	}

	public String getCompanyId() {
		try {
			return orgGroup.getOwningCompany().getOrgGroupId();
		} catch (final RuntimeException e) {
			return "";
		}
	}

	public String getType() {
		try {
			if ((orgGroup.getOrgGroupType() == 16) && (orgGroup.getOrgGroupId().equals(orgGroup.getOwningCompany().getOrgGroupId())))
				return "Agency";
			if (orgGroup.getOrgGroupId().equals(orgGroup.getOwningCompany().getOrgGroupId()))
				return "Company";
			/*
			 * if (hsu.createCriteria(CompanyBase.class)
			 * .eq(CompanyBase.ORGGROUPID, orgGroup.getOrgGroupId()) .exists())
			 * return "Company";
			 */
			return "Group";
		} catch (Exception e) {
			return "Group";
		}
	}

	public boolean isSuperGroup() {
		return (!(orgGroup instanceof CompanyBase)) && orgGroup.getOwningCompany() == null;
	}

	public void setCompanyId(final String companyId) {
		orgGroup.setOwningCompany(ArahantSession.getHSU().get(CompanyBase.class, companyId));
	}

	static public BOrgGroup[] searchOrgGroupsGeneric(final HibernateSessionUtil hsu, final String name, final int assocIndicator, final int type, final int max) {
		final HibernateCriteriaUtil<OrgGroup> hcu = hsu.createCriteria(OrgGroup.class).orderBy(OrgGroup.NAME).setMaxResults(max).like(OrgGroup.NAME, name);

		if (BRight.checkRight("SeeAllOrgGroups") != BRight.ACCESS_LEVEL_WRITE) {
			Set<OrgGroupAssociation> oga = ArahantSession.getHSU().getCurrentPerson().getOrgGroupAssociations();
			Set<OrgGroup> ogs = new HashSet<OrgGroup>();
			for (OrgGroupAssociation o : oga)
				ogs.addAll(new BOrgGroup(o.getOrgGroup()).getAllOrgGroupsInHierarchy2());
			List<String> orgIds = new ArrayList<String>();
			for (OrgGroup og : ogs)
				orgIds.add(og.getOrgGroupId());

			hcu.in(OrgGroup.ORGGROUPID, orgIds);
		}

		switch (assocIndicator) {
			case 0:
				break;//all
			case 1: 		//associated
				hcu.sizeNe(OrgGroup.ORGGROUPHIERARCHIESFORCHILDGROUPID, 0);
				break;
			case 2:			//not associated
				hcu.sizeEq(OrgGroup.ORGGROUPHIERARCHIESFORCHILDGROUPID, 0);
				break;
		}

		if (type != 0) {
			LinkedList<Integer> types = new LinkedList<Integer>();
			if ((type & COMPANY_TYPE) > 0)
				types.add(COMPANY_TYPE);
			if ((type & PROSPECT_TYPE) > 0)
				types.add(PROSPECT_TYPE);
			if ((type & CLIENT_TYPE) > 0)
				types.add(CLIENT_TYPE);
			if ((type & VENDOR_TYPE) > 0)
				types.add(VENDOR_TYPE);
			hcu.in(OrgGroup.ORGGROUPTYPE, types);
		}
		return BOrgGroup.makeArray(hcu.list());

	}

	static public BOrgGroup[] searchOrgGroupsGenericNoFilter(final HibernateSessionUtil hsu, final String name, final int assocIndicator, final int type, final int max) {
		final HibernateCriteriaUtil<OrgGroup> hcu = hsu.createCriteriaNoCompanyFilter(OrgGroup.class).orderBy(OrgGroup.NAME).setMaxResults(max).like(OrgGroup.NAME, name);

		switch (assocIndicator) {
			case 0:
				break;//all
			case 1: 		//associated
				hcu.sizeNe(OrgGroup.ORGGROUPHIERARCHIESFORCHILDGROUPID, 0);
				break;
			case 2:			//not associated
				hcu.sizeEq(OrgGroup.ORGGROUPHIERARCHIESFORCHILDGROUPID, 0);
				break;
		}

		if (type != 0)
			hcu.eq(OrgGroup.ORGGROUPTYPE, type);

		return BOrgGroup.makeArray(hcu.list());
	}

	public static BOrgGroup[] searchOrgGroups(final HibernateSessionUtil hsu, final String name, final String orgGroupId, final int assocInd, final int groupType, final int max) throws ArahantException {

		Connection db = hsu.getKissConnection();
		List<Object> args = new ArrayList<>();
		Command cmd = db.newCommand();
		String select = "select og.org_group_id " +
				"from org_group og " +
				"where og.org_group_type = ? ";
		args.add(groupType);
		if (!isEmpty(name)) {
			select += "and og.group_name like ? ";
			args.add(name);
		}
		if (!isEmpty(orgGroupId)) {
			select += "and og.owning_entity_id = ? ";
			args.add(orgGroupId);
		}
		switch (assocInd) {
			case 0:         // all
				break;
			case 1: 		// associated
				select += "and og.org_group_id in (select child_group_id from org_group_hierarchy) ";
				break;
			case 2:			// not associated
				select += "and og.org_group_id not in (select child_group_id from org_group_hierarchy) " +
						"and og.org_group_id <> og.owning_entity_id ";
				break;
		}
		select += "order by og.group_name ";
		try {
			List<Record> recs = cmd.fetchAll(max, select, args);
			BOrgGroup[] res = new BOrgGroup[recs.size()];
			int i = 0;
			for (Record rec : recs)
				res[i++] = new BOrgGroup(rec.getString("org_group_id"));
			return res;
		} catch (Exception e) {
			throw new ArahantException(e);
		}



/*


		final HibernateCriteriaUtil<OrgGroup> hcu = hsu.createCriteriaNoCompanyFilter(OrgGroup.class);

		if (BRight.checkRight(Right.RIGHT_CAN_ACCESS_ALL_COMPANIES) != ACCESS_LEVEL_WRITE)
			hcu.in(OrgGroup.ORGGROUPID, getAllowedOrgGroupIds());

		hcu.orderBy(OrgGroup.NAME);

		if (max > 0)
			hcu.setMaxResults(max);

		if (!isEmpty(name))
			hcu.like(OrgGroup.NAME, name);

		switch (assocInd) {
			case 0:
				break;//all
			case 1: 		//associated
				hcu.sizeNe(OrgGroup.ORGGROUPHIERARCHIESFORCHILDGROUPID, 0);
				break;
			case 2:			//not associated
				hcu.sizeEq(OrgGroup.ORGGROUPHIERARCHIESFORCHILDGROUPID, 0);
				break;
		}

		CompanyBase matchCompany;
		OrgGroup currentOg = null;

		if (!isEmpty(orgGroupId)) {
			currentOg = hsu.get(OrgGroup.class, orgGroupId);

			if (currentOg == null)
				throw new ArahantException("Can't find org group in search Org Groups.");

			hcu.eq(OrgGroup.ORGGROUPTYPE, currentOg.getOrgGroupType());

			matchCompany = currentOg.getOwningCompany();
			if (matchCompany != null)
				hcu.eq(OrgGroup.OWNINGCOMPANY, matchCompany);

		} else if (groupType != 0)
			hcu.eq(OrgGroup.ORGGROUPTYPE, groupType);

		List<OrgGroup> orgRes = hcu.list();

		if (currentOg != null) {
			orgRes = filterOrgGroupsThatAreChildren(orgRes, currentOg);
			orgRes = filterOrgGroupsThatAreParents(orgRes, currentOg);
			orgRes = filterOrgGroupsThatAreCurrentGroup(orgRes, currentOg);
		}

		return BOrgGroup.makeArray(orgRes);

 */
	}

	protected static List<OrgGroup> filterOrgGroupsThatAreParents(List<OrgGroup> orgList, final OrgGroup og) {
		for (OrgGroupHierarchy ogh : og.getOrgGroupHierarchiesForChildGroupId()) {
			orgList.remove(ogh.getOrgGroupByParentGroupId());
			if (og.equals(ogh.getOrgGroupByParentGroupId()))
				continue;
			orgList = filterOrgGroupsThatAreParents(orgList, ogh.getOrgGroupByParentGroupId());
		}
		return orgList;
	}

	protected static List<OrgGroup> filterOrgGroupsThatAreChildren(List<OrgGroup> orgList, final OrgGroup og) {
		for (OrgGroupHierarchy ogh : og.getOrgGroupHierarchiesForParentGroupId()) {
			orgList.remove(ogh.getOrgGroupByChildGroupId());
			if (og.equals(ogh.getOrgGroupByChildGroupId()))
				continue;
			orgList = filterOrgGroupsThatAreChildren(orgList, ogh.getOrgGroupByChildGroupId());
		}
		return orgList;
	}

	protected static List<OrgGroup> filterOrgGroupsThatAreCurrentGroup(final List<OrgGroup> orgRes, final OrgGroup currentOg) {
		final LinkedList<OrgGroup> res = new LinkedList<OrgGroup>();
		for (OrgGroup og : orgRes) {
			if (og.equals(currentOg))
				continue;
			res.add(og);
		}
		return res;
	}

	public void removeGroups(final String[] orgGroupId) throws ArahantDeleteException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		for (final String element : orgGroupId) {
			final HibernateCriteriaUtil<OrgGroupHierarchy> hcu = hsu.createCriteria(OrgGroupHierarchy.class);
			hcu.joinTo(OrgGroupHierarchy.ORGGROUPBYCHILDGROUPID).eq(OrgGroup.ORGGROUPID, element);
			hcu.eq(OrgGroupHierarchy.ORGGROUPBYPARENTGROUPID, orgGroup);
			hsu.delete(hcu.list());
		}
	}

	public void assignPeopleToGroup(final String [] personIds) throws ArahantException {
		for (final String element : personIds)
			new BPerson(element).assignToOrgGroup(orgGroup.getOrgGroupId(), false);
	}

	public void removePeopleFromGroup(final String [] personIds) throws ArahantDeleteException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		for (final String element : personIds) {
			final HibernateCriteriaUtil<OrgGroupAssociation> hcu = hsu.createCriteria(OrgGroupAssociation.class);
			hcu.joinTo(OrgGroupAssociation.PERSON).eq(Person.PERSONID, element);
			hcu.joinTo(OrgGroupAssociation.ORGGROUP).eq(OrgGroup.ORGGROUPID, orgGroup.getOrgGroupId());
			hsu.delete(hcu.list());
		}
	}

	@SuppressWarnings("unchecked")
	public Set<String> getAllPersonIdsForOrgGroupHierarchy(final boolean activeOnly) throws ArahantException {

		return getAllPersonIdsForOrgGroupHierarchy(activeOnly, true);
	}

	@SuppressWarnings("unchecked")
	public Set<String> getAllPersonIdsForOrgGroupHierarchy(final boolean activeOnly, boolean forQuery) throws ArahantException {

		final Set<String> ids = new HashSet<String>();
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		ids.addAll((List<String>) (List) hsu.createCriteria(Employee.class).selectFields(Employee.PERSONID).joinTo(Employee.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.ORGGROUP, orgGroup).list());

		for (OrgGroupHierarchy ogh : hsu.createCriteria(OrgGroupHierarchy.class).eq(OrgGroupHierarchy.PARENT, orgGroup).list()) //	for (OrgGroupHierarchy ogh : orgGroup.getOrgGroupHierarchiesForParentGroupId())
		
			ids.addAll(new BOrgGroup(ogh.getOrgGroupByChildGroupId()).getAllPersonIdsForOrgGroupHierarchy(activeOnly, forQuery));

		if (ids.isEmpty() && forQuery) //make sure there is something there so when fed to query, it doesn't blow up
		
			ids.add("");

		return ids;
	}

	public static BOrgGroup[] makeArray(final List<OrgGroup> l, final String[] excludeIds) {
		List<OrgGroup> ogl = new ArrayList<OrgGroup>();
		for (OrgGroup group : l) {
			BOrgGroup borg = new BOrgGroup(group);
			boolean match = false;
			if (excludeIds != null)
				for (String id : excludeIds)
					if (borg.getOrgGroupId().equals(id)) {
						match = true;
						break;
					}
			if (!match)
				ogl.add(borg.getOrgGroup());
		}
		return makeArray(ogl);
	}

	public String getOrgGroupGuid() {
		return orgGroup.getOrgGroupGuid();
	}

	public void setOrgGroupGuid(String orgGroupGuid) {
		orgGroup.setOrgGroupGuid(orgGroupGuid);
	}

	public static BOrgGroup[] makeArray(final List<OrgGroup> l) {
		final BOrgGroup[] ogs = new BOrgGroup[l.size()];

		for (int loop = 0; loop < ogs.length; loop++)
			ogs[loop] = new BOrgGroup(l.get(loop));

		return ogs;
	}

	public static BOrgGroup[] makeArray(final Set<OrgGroup> orgGroups) {
		final List<OrgGroup> l = new ArrayList<OrgGroup>(orgGroups.size());
		l.addAll(orgGroups);
		java.util.Collections.sort(l);
		return makeArray(l);
	}

	public void setPayPeriodsPerYear(int payPeriodsPerYear) {
		orgGroup.setPayPeriodsPerYear((short) payPeriodsPerYear);
	}

	public int getPayPeriodsPerYear() {
		if (orgGroup == null)
			return 12; //default to monthly
		if (orgGroup.getPayPeriodsPerYear() == 0)
			return getParentPayPeriodsPerYear();

		return orgGroup.getPayPeriodsPerYear();
	}

	public int getParentPayPeriodsPerYear() {
		for (OrgGroupHierarchy ogh : orgGroup.getOrgGroupHierarchiesForChildGroupId()) {
			int ppy = getParentPayPeriod(ogh.getOrgGroupByParentGroupId());
			if (ppy != 0)
				return ppy;
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	public String[] getAllGroupsForCompany() {
		List ogs = ArahantSession.getHSU().createCriteria(OrgGroup.class).selectFields(OrgGroup.ORGGROUPID).eq(OrgGroup.OWNINGCOMPANY, orgGroup).list();
		ogs.add(orgGroup.getOrgGroupId());
		String [] ret = new String[ogs.size()];
		ret = (String[]) ogs.toArray(ret);
		return ret;
	}

	public void setDefaultProjectId(String defaultProjectId) {
		Project project = null;

		if (!isEmpty(defaultProjectId))
			project = ArahantSession.getHSU().get(Project.class, defaultProjectId);

		orgGroup.setDefaultProject(project);
	}

	public String getDefaultProjectId() {
		Project defaultProject = this.getDefaultProject();

		if (defaultProject == null)
			return "";
		else
			return defaultProject.getProjectId();
	}

	public String getExplicitProjectId() {
		if (orgGroup.getDefaultProject() == null)
			return "";
		return orgGroup.getDefaultProject().getProjectId();
	}

	public Project getDefaultProject() {
		if (orgGroup.getDefaultProject() != null)
			return orgGroup.getDefaultProject();

		//try a parent
		for (OrgGroupHierarchy ogh : orgGroup.getOrgGroupHierarchiesForChildGroupId()) {
			Project proj = getDefaultProject(ogh.getOrgGroupByParentGroupId());
			if (proj != null)
				return proj;
		}
		return null;
	}

	String getFirstSupervisorId() {
		OrgGroupAssociation oga = ArahantSession.getHSU().createCriteria(OrgGroupAssociation.class).eq(OrgGroupAssociation.ORGGROUP, orgGroup).eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y').first();

		if (oga == null)
			throw new ArahantException("The supervisor is missing from org group " + orgGroup.getName());

		return oga.getPersonId();
	}

	public PaySchedule getPaySchedule() {
		if (orgGroup.getPaySchedule() != null)
			return orgGroup.getPaySchedule();

		for (OrgGroupHierarchy ogh : orgGroup.getOrgGroupHierarchiesForChildGroupId()) {
			PaySchedule ps = getPaySchedule(ogh.getOrgGroupByParentGroupId());
			if (ps != null)
				return ps;
		}
		return null;
	}

	private Project getDefaultProject(OrgGroup og) {
		if (og.getDefaultProject() != null)
			return og.getDefaultProject();

		for (OrgGroupHierarchy ogh : og.getOrgGroupHierarchiesForChildGroupId()) {
			Project proj = getDefaultProject(ogh.getOrgGroupByParentGroupId());
			if (proj != null)
				return proj;
		}
		return null;
	}

	private int getParentPayPeriod(OrgGroup og) {
		if (og.getPayPeriodsPerYear() != 0)
			return og.getPayPeriodsPerYear();

		for (OrgGroupHierarchy ogh : og.getOrgGroupHierarchiesForChildGroupId()) {
			int ppy = getParentPayPeriod(ogh.getOrgGroupByParentGroupId());
			if (ppy != 0)
				return ppy;
		}
		return 0;
	}

	/**
	 * Creates a parent/child org group relationship.
	 * Should be called AFTER both org groups have been created.
	 * 
	 * @param orgGroupId parent org group ID
	 */
	public void setParent(String orgGroupId) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		OrgGroupHierarchy ogh = new OrgGroupHierarchy();
		OrgGroupHierarchyId id = new OrgGroupHierarchyId();
		id.setChildGroupId(orgGroup.getOrgGroupId());
		id.setParentGroupId(orgGroupId);
		ogh.setId(id);
		ogh.setOrgGroupType(orgGroup.getOrgGroupType());
		hsu.insert(ogh);

		OrgGroup parent = hsu.get(OrgGroup.class, orgGroupId);
		orgGroup.setOrgGroupType(parent.getOrgGroupType());
		orgGroup.setOwningCompany(parent.getOwningCompany());
		hsu.saveOrUpdate(orgGroup);
	}

	/**
	 * Gets the "Location" org group (top level org group under the company org
	 * group) using the current org group
	 */
	public BOrgGroup getLocation() {
		try {
			OrgGroup loc = orgGroup;
			OrgGroup current = loc;

			while (!current.getOrgGroupHierarchiesForChildGroupId().isEmpty()) {
				loc = current;
				current = current.getOrgGroupHierarchiesForChildGroupId().iterator().next().getOrgGroupByParentGroupId();
			}

			return new BOrgGroup(loc.getOrgGroupId());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Get all "Location" org groups (top level org group under the company org
	 * group)
	 */
	/*
	 * List <CompanyDetail>
	 * cdl=ArahantSession.getHSU().getAll(CompanyDetail.class);
	 *
	 * return
	 * BOrgGroup.makeArray(ArahantSession.getHSU().createCriteria(OrgGroup.class)
	 * .orderBy(OrgGroup.NAME).eq(OrgGroup.ORGGROUPTYPE, COMPANY_TYPE)
	 * .joinTo(OrgGroup.ORGGROUPHIERARCHIESFORCHILDGROUPID)
	 * .in(OrgGroupHierarchy.ORGGROUPBYPARENTGROUPID,cdl) .list());
     *
	 */
	public static BOrgGroup[] getLocations() {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		HibernateCriteriaUtil<OrgGroup> hcu = hsu.createCriteria(OrgGroup.class).orderBy(OrgGroup.NAME).eq(OrgGroup.ORGGROUPTYPE, COMPANY_TYPE);

		hcu.joinTo(OrgGroup.ORGGROUPHIERARCHIESFORCHILDGROUPID).eq(OrgGroupHierarchy.ORGGROUPBYPARENTGROUPID, hsu.getCurrentCompany());
		//.sizeEq(OrgGroup.ORGGROUPHIERARCHIESFORCHILDGROUPID, 0);

		hcu.restrictOrgGroups();

		List<OrgGroup> ogl = hcu.list();

		if (BProperty.getBoolean("IncludeCompanyWithLocations"))
			ogl.add(hsu.getCurrentCompany());

		return BOrgGroup.makeArray(ogl);
	}

	//(call com.arahant.business.BOrgGroup loadHierarchy ?child ?parent)
	public static OrgGroupHierarchy loadHierarchy(String child, String parent) {
		if (isEmpty(child) || isEmpty(parent))
			return null;
		OrgGroupHierarchy o = ArahantSession.getHSU().createCriteria(OrgGroupHierarchy.class).eq(OrgGroupHierarchy.CHILD_ID, child).eq(OrgGroupHierarchy.PARENT_ID, parent).first();
		return o;
	}

	public static BOrgGroup[] search(String companyName, String orgGroupName, boolean includeOrgGroups, int type, int cap) {

		//get all the companies that match first
		Set<OrgGroup> res = new HashSet<OrgGroup>();

		HibernateCriteriaUtil<CompanyBase> cbhcu = ArahantSession.getHSU().createCriteria(CompanyBase.class).setMaxResults(cap).like(CompanyBase.NAME, companyName);

		if (type != 0)
			cbhcu.eq(CompanyBase.ORGGROUPTYPE, type);

		res.addAll(cbhcu.list());


		if (includeOrgGroups) {
			HibernateCriteriaUtil<OrgGroup> hcu = ArahantSession.getHSU().createCriteria(OrgGroup.class).setMaxResults(cap - res.size());
			HibernateCriteriaUtil hcu2 = hcu.like(OrgGroup.NAME, orgGroupName).joinTo(OrgGroup.OWNINGCOMPANY).like(CompanyBase.NAME, companyName);

			if (type != 0)
				hcu2.eq(CompanyBase.ORGGROUPTYPE, type);

			res.addAll(hcu.list());
		}

		List<OrgGroup> l = new ArrayList<OrgGroup>(res.size());
		l.addAll(res);

		Collections.sort(l);

		return makeArray(l);
	}

	public String getProjectStatusReport() {
		return new OrgGroupProjectStatusReport().build(this);
	}

	/**
	 * Returns a list of child org group id's from the given one down.  
	 * The list starts with the given group with consecutive elements provided depth first.
	 *  
	 * @return the list of org groups
	 */
	public List<String> getAllOrgGroupsInHierarchy() {
		LinkedList<String> ret = new LinkedList<String>();
		ret.add(orgGroup.getOrgGroupId());

		for (OrgGroupHierarchy ogh : ArahantSession.getHSU().createCriteria(OrgGroupHierarchy.class).eq(OrgGroupHierarchy.ORGGROUPBYPARENTGROUPID, orgGroup).list())
			ret.addAll(new BOrgGroup(ogh.getOrgGroupByChildGroupId()).getAllOrgGroupsInHierarchy());

		return ret;
	}

	/**
	 * Returns a list of child org groups from the given one down.  
	 * The list starts with the given group with consecutive elements provided depth first.
	 *  
	 * @return the list of org groups
	 */
	public List<OrgGroup> getAllOrgGroupsInHierarchy2() {
		LinkedList<OrgGroup> ret = new LinkedList<OrgGroup>();
		ret.add(orgGroup);

		for (OrgGroupHierarchy ogh : ArahantSession.getHSU().createCriteria(OrgGroupHierarchy.class).eq(OrgGroupHierarchy.ORGGROUPBYPARENTGROUPID, orgGroup).list())
			ret.addAll(new BOrgGroup(ogh.getOrgGroupByChildGroupId()).getAllOrgGroupsInHierarchy2());

		return ret;
	}

	public static BOrgGroup[] search(String name, boolean includeProspects, int max) {
		HibernateCriteriaUtil<OrgGroup> hcu = ArahantSession.getHSU().createCriteria(OrgGroup.class).setMaxResults(max).like(OrgGroup.NAME, name).orderBy(OrgGroup.NAME);
		if (!includeProspects)
			hcu.ne(OrgGroup.ORGGROUPTYPE, PROSPECT_TYPE);
		return makeArray(hcu.list());
	}

	public static BOrgGroup[] searchCompanySpecific(String name, boolean includeProspects, int max) {
		HibernateCriteriaUtil<OrgGroup> hcu = ArahantSession.getHSU().createCriteria(OrgGroup.class).in(OrgGroup.COMPANY_ID, BCompanyBase.searchCompanySpecificIds("%", includeProspects, 10000)).setMaxResults(max).like(OrgGroup.NAME, name).orderBy(OrgGroup.NAME);
		return makeArray(hcu.list());
	}

	public static BOrgGroup[] search(String name, int orgGroupType, int max) {
		HibernateCriteriaUtil<OrgGroup> hcu = ArahantSession.getHSU().createCriteria(OrgGroup.class).setMaxResults(max).like(OrgGroup.NAME, name).eq(OrgGroup.ORGGROUPTYPE, orgGroupType).orderBy(OrgGroup.NAME);
		return makeArray(hcu.list());
	}

	public BOrgGroup[] getChildren(String[] excludeIds) {
		return makeArray(ArahantSession.getHSU().createCriteria(OrgGroup.class).orderBy(OrgGroup.NAME).notIn(OrgGroup.ORGGROUPID, excludeIds).joinTo(OrgGroup.ORGGROUPHIERARCHIESFORCHILDGROUPID).eq(OrgGroupHierarchy.ORGGROUPBYPARENTGROUPID, orgGroup).list());
	}

	public BOrgGroup[] getChildren(String nameStartsWith) {
		return makeArray(ArahantSession.getHSU().createCriteria(OrgGroup.class).orderBy(OrgGroup.NAME).like(OrgGroup.NAME, nameStartsWith + "%").joinTo(OrgGroup.ORGGROUPHIERARCHIESFORCHILDGROUPID).eq(OrgGroupHierarchy.ORGGROUPBYPARENTGROUPID, orgGroup).list());
	}

	public static BOrgGroup[] listTopLevel(String[] excludeIds) {
		return makeArray(ArahantSession.getHSU().createCriteria(OrgGroup.class).eq(OrgGroup.ORGGROUPTYPE, COMPANY_TYPE).orderBy(OrgGroup.NAME).notIn(OrgGroup.ORGGROUPID, excludeIds).sizeEq(OrgGroup.ORGGROUPHIERARCHIESFORCHILDGROUPID, 0).list());
	}

	private PaySchedule getPaySchedule(OrgGroup og) {
		if (og.getPaySchedule() != null)
			return og.getPaySchedule();

		for (OrgGroupHierarchy ogh : og.getOrgGroupHierarchiesForChildGroupId()) {
			PaySchedule ps = getPaySchedule(ogh.getOrgGroupByParentGroupId());
			if (ps != null)
				return ps;
		}
		return null;
	}

	public static BOrgGroup[] listAll(int type) {
		return makeArray(ArahantSession.getHSU().createCriteria(OrgGroup.class).eq(OrgGroup.ORGGROUPTYPE, type).orderBy(OrgGroup.NAME).list());
	}

	public boolean getEeo1Establishment() {
		return orgGroup.getEeo1Establishment() == 'Y';
	}

	public void setEeo1Establishment(boolean eeo1Establishment) {
		orgGroup.setEeo1Establishment(eeo1Establishment ? 'Y' : 'N');
	}

	public boolean getEeo1FiledLastYear() {
		return orgGroup.getEeo1FiledLastYear() == 'Y';
	}

	public void setEeo1FiledLastYear(boolean eeo1FiledLastYear) {
		orgGroup.setEeo1FiledLastYear(eeo1FiledLastYear ? 'Y' : 'N');
	}

	public boolean getEeo1Headquarters() {
		return orgGroup.getEeo1Headquarters() == 'Y';
	}

	public void setEeo1Headquarters(boolean eeo1Headquarters) {
		orgGroup.setEeo1Headquarters(eeo1Headquarters ? 'Y' : 'N');
	}

	public String getEeo1UnitId() {
		return orgGroup.getEeo1UnitId();
	}

	public void setEeo1UnitId(String eeo1UnitId) {
		orgGroup.setEeo1UnitId(eeo1UnitId);
	}

	protected Address getAddress() throws ArahantException {
		if (address != null)
			return address;

		try {
			if (orgGroup.getAddresses().size() > 0)
				address = orgGroup.getAddresses().iterator().next();
			else {
				address = new Address();
				address.setAddressId(IDGenerator.generate(address));
				address.setAddressType(ADDR_WORK);
				address.setOrgGroup(orgGroup);
				address.setCity("");
				address.setState("");
				address.setStreet("");
				address.setZip("");
//				hsu.insert(address);
			}

			return address;
		} catch (final RuntimeException e) {
			return null;
		}
	}

	public void setStreet(final String street) {
		getAddress().setStreet(street);
	}

	public String getStreet() {
		if (getAddress().getStreet() == null)
			return "";
		return getAddress().getStreet();
	}

	public void setStreet2(final String street2) {
		getAddress().setStreet2(street2);
	}

	public String getStreet2() {

		if (getAddress().getStreet2() == null)
			return "";
		return getAddress().getStreet2();
	}

	public void setCity(final String city) {
		getAddress().setCity(city);
	}

	public String getCity() {
		if (getAddress().getCity() == null)
			return "";
		return getAddress().getCity();
	}

	public void setCountry(String country) {
		getAddress().setCountry(country);
	}

	public String getCountry() {
		if (getAddress().getCountry() == null)
			return "";
		return getAddress().getCountry();
	}

	public void setState(final String state) {
		getAddress().setState(state);
	}

	public String getState() {
		if (getAddress().getState() == null)
			return "";
		return getAddress().getState();
	}

	public void setZip(final String zip) {
		getAddress().setZip(zip);
	}

	public String getZip() {
		if (getAddress().getZip() == null)
			return "";
		return getAddress().getZip();
	}

	public void setCounty(final String county) {
		getAddress().setCounty(county);
	}

	public String getCounty() {
		if (getAddress().getCounty() == null)
			return "";
		return getAddress().getCounty();
	}

	public void setMainPhoneNumber(final String mainPhoneNumber) throws ArahantException {
		if (getPhoneByMainPhoneId() == null) {
			mainPhone = new Phone();
			mainPhone.setPhoneId(IDGenerator.generate(mainPhone));
			mainPhone.setPhoneType(PHONE_WORK);
			orgGroup.getPhones().add(mainPhone);
			mainPhone.setOrgGroup(orgGroup);
		}
		getPhoneByMainPhoneId().setPhoneNumber(mainPhoneNumber);
	}

	public String getMainPhoneNumber() {
		if (getPhoneByMainPhoneId() == null || (getPhoneByMainPhoneId().getPhoneNumber() == null))
			return "";

		return getPhoneByMainPhoneId().getPhoneNumber();
	}

	public void setMainFaxNumber(final String mainFaxNumber) throws ArahantException {
		if (getPhoneByMainFaxId() == null) {
			mainFax = new Phone();
			mainFax.setPhoneId(IDGenerator.generate(mainFax));
			orgGroup.getPhones().add(mainFax);
			mainFax.setOrgGroup(orgGroup);
			mainFax.setPhoneType(PHONE_FAX);
		}
		getPhoneByMainFaxId().setPhoneNumber(mainFaxNumber);
	}

	public String getMainFaxNumber() {
		if (getPhoneByMainFaxId() == null)
			return "";
		return getPhoneByMainFaxId().getPhoneNumber();
	}

	protected Phone getPhoneByMainPhoneId() {
		for (Phone p : orgGroup.getPhones())
			if (p.getPhoneType() == PHONE_WORK)
				return p;

		return null;
	}

	protected Phone getPhoneByMainFaxId() {
		for (Phone p : orgGroup.getPhones())
			if (p.getPhoneType() == PHONE_FAX)
				return p;

		return null;
	}

	public Set<CompanyFormFolder> getFolders() {
		return orgGroup.getFolders();
	}

	public void setFolders(Set<CompanyFormFolder> folders) {
		orgGroup.setFolders(folders);
	}

	public static BOrgGroup[] listEEO1Establishments() {
		return makeArray(ArahantSession.getHSU().createCriteria(OrgGroup.class).eq(OrgGroup.ORGGROUPTYPE, COMPANY_TYPE).eq(OrgGroup.EEO1_ESTABLISHMENT, 'Y').orderBy(OrgGroup.NAME).list());
	}

	public void associateTo(String folderId) {
		orgGroup.getFolders().add(ArahantSession.getHSU().get(CompanyFormFolder.class, folderId));
		update();
	}

	public void remove(String fromFolderId) {
		orgGroup.getFolders().remove(ArahantSession.getHSU().get(CompanyFormFolder.class, fromFolderId));
		update();
	}

	public BOrgGroup[] listTopLevelOrgGroups(String orgId, int cap) {

		HibernateCriteriaUtil<OrgGroup> hcu = ArahantSession.getHSU().createCriteria(OrgGroup.class).orderBy(OrgGroup.NAME).setMaxResults(cap);

		if (!isEmpty(orgId))
			//BOrgGroup borg=new BOrgGroup(orgId);
			//List<String> ids=borg.getAllOrgGroupsInHierarchy();
			//	hcu.in(OrgGroup.ORGGROUPID, ids);
			hcu.joinTo(OrgGroup.ORGGROUPHIERARCHIESFORCHILDGROUPID).eq(OrgGroupHierarchy.PARENT_ID, orgId);
		else {
			//Removed for Drury Group
			hcu.sizeEq(OrgGroup.ORGGROUPHIERARCHIESFORCHILDGROUPID, 0);
			hcu.eq(OrgGroup.ORGGROUPTYPE, COMPANY_TYPE);
			//  hcu.joinTo(OrgGroup.ORGGROUPHIERARCHIESFORPARENTGROUPID).isNull(OrgGroupHierarchy.PARENT_ID);
		}

		return BOrgGroup.makeArray(hcu.list());
	}

	/**
	 * Gets all the parent org groups for the list supplied, to all levels to the top.  Returns list from bottom to top of hierarchy.
	 * Does not de-dup.
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getAllOrgGroupParents(List<String> orgGroupIds) {
		List<String> ret = new LinkedList<String>();

		if (orgGroupIds.isEmpty())
			return ret;

		ret.addAll(orgGroupIds);

		ret.addAll(getAllOrgGroupParents((List<String>) (List) ArahantSession.getHSU().createCriteria(OrgGroupHierarchy.class).selectFields(OrgGroupHierarchy.PARENT_ID).in(OrgGroupHierarchy.CHILD_ID, orgGroupIds).list()));

		return ret;
	}

	public int getChildCount() {
		return orgGroup.getOrgGroupHierarchiesForParentGroupId().size();
	}

	public int getEvalEmailFirstDays() {
		return orgGroup.getEvalEmailFirstDays();
	}

	public int getEvalEmailFirstDaysInherited() {
		Set<OrgGroupHierarchy> parentalz = orgGroup.getOrgGroupHierarchiesForChildGroupId();

		for (OrgGroupHierarchy orgGroupHierarchy : parentalz) {
			BOrgGroup parent = new BOrgGroup(orgGroupHierarchy.getParentGroupId());
			if (!parent.getEvalEmailNotify().equals("I"))
				return parent.getEvalEmailFirstDays();
		}

		for (OrgGroupHierarchy orgGroupHierarchy : parentalz) {
			BOrgGroup parent = new BOrgGroup(orgGroupHierarchy.getParentGroupId());
			return parent.getEvalEmailFirstDaysInherited();
		}
		return 0; // if all parents are inherited ("I") then just say "N" for off
	}

	public void setEvalEmailFirstDays(int evalEmailFirstDays) {
		orgGroup.setEvalEmailFirstDays(evalEmailFirstDays);
	}

	public String getEvalEmailNotify() {
		return orgGroup.getEvalEmailNotify() + "";
	}

	public String getEvalEmailNotifyInherited() {
		Set<OrgGroupHierarchy> parentalz = orgGroup.getOrgGroupHierarchiesForChildGroupId();

		for (OrgGroupHierarchy orgGroupHierarchy : parentalz) {
			BOrgGroup parent = new BOrgGroup(orgGroupHierarchy.getParentGroupId());
			if (!parent.getEvalEmailNotify().equals("I"))
				return parent.getEvalEmailNotify();
		}

		for (OrgGroupHierarchy orgGroupHierarchy : parentalz) {
			BOrgGroup parent = new BOrgGroup(orgGroupHierarchy.getParentGroupId());
			return parent.getEvalEmailNotifyInherited();
		}
		return "N"; // if all parents are inherited ("I") then just say "N" for off
	}

	public void setEvalEmailNotify(String evalEmailNotify) {
		if (isEmpty(evalEmailNotify))
			orgGroup.setEvalEmailNotify('I');
		else
			orgGroup.setEvalEmailNotify(evalEmailNotify.charAt(0));
	}

	public int getEvalEmailNotifyDays() {
		return orgGroup.getEvalEmailNotifyDays();
	}

	public int getEvalEmailNotifyDaysInherited() {
		Set<OrgGroupHierarchy> parentalz = orgGroup.getOrgGroupHierarchiesForChildGroupId();

		for (OrgGroupHierarchy orgGroupHierarchy : parentalz) {
			BOrgGroup parent = new BOrgGroup(orgGroupHierarchy.getParentGroupId());
			if (!parent.getEvalEmailNotify().equals("I"))
				return parent.getEvalEmailNotifyDays();
		}

		for (OrgGroupHierarchy orgGroupHierarchy : parentalz) {
			BOrgGroup parent = new BOrgGroup(orgGroupHierarchy.getParentGroupId());
			return parent.getEvalEmailNotifyDaysInherited();
		}
		return 0; // if all parents are inherited ("I") then just say "N" for off
	}

	public void setEvalEmailNotifyDays(int evalEmailNotifyDays) {
		orgGroup.setEvalEmailNotifyDays(evalEmailNotifyDays);
	}

	public String getEvalEmailNotifySendDays() {
		return orgGroup.getEvalEmailNotifySendDays();
	}

	public String getEvalEmailNotifySendDaysInherited() {
		Set<OrgGroupHierarchy> parentalz = orgGroup.getOrgGroupHierarchiesForChildGroupId();

		for (OrgGroupHierarchy orgGroupHierarchy : parentalz) {
			BOrgGroup parent = new BOrgGroup(orgGroupHierarchy.getParentGroupId());
			if (!parent.getEvalEmailNotify().equals("I"))
				return parent.getEvalEmailNotifySendDays();
		}

		for (OrgGroupHierarchy orgGroupHierarchy : parentalz) {
			BOrgGroup parent = new BOrgGroup(orgGroupHierarchy.getParentGroupId());
			return parent.getEvalEmailNotifySendDaysInherited();
		}
		return "NNNNN"; // if all parents are inherited then just say "NNNNN" for off
	}

	public void setEvalEmailNotifySendDays(String evalEmailNotifySendDays) {
		orgGroup.setEvalEmailNotifySendDays(evalEmailNotifySendDays);
	}

	public void setNewWeekBeginDay(int day) {
		orgGroup.setNewWeekBeginDay(day);
	}

	public int getNewWeekBeginDay() {
		return orgGroup.getNewWeekBeginDay();
	}

	public String getNewWeekBeginDayFormatted() {
		int x = orgGroup.getNewWeekBeginDay();
		switch (x) {
			case 1:
				return "Sunday";
			case 2:
				return "Monday";
			case 3:
				return "Tuesday";
			case 4:
				return "Wednesday";
			case 5:
				return "Thursday";
			case 6:
				return "Friday";
			case 7:
				return "Saturday";
			default:
				return "Unspecified";
		}
	}

	public String getInheritedNewWeekBeginDayFormatted() {
		BOrgGroup parent = getParent();
		if (parent != null) {
			if (!parent.getNewWeekBeginDayFormatted().equals("Unspecified"))
				return parent.getNewWeekBeginDayFormatted();
			return parent.getInheritedNewWeekBeginDayFormatted();
		}
		return getNewWeekBeginDayFormatted();
	}

	public String getDefaultBenefitClassId() {
		if (orgGroup.getBenefitClass() != null)
			return orgGroup.getBenefitClass().getBenefitClassId();
		return "";
	}

	public BenefitClass getDefaultBenefitClass() {
		return orgGroup.getBenefitClass();
	}

	public void setDefaultBenefitClass(BenefitClass bc) {
		orgGroup.setBenefitClass(bc);
	}

	public String getInheritedBenefitClassName() {
		BOrgGroup parent = getParent();
		if (parent == null)
			return "";
		if (parent.getDefaultBenefitClass() != null)
			return parent.getDefaultBenefitClass().getName();
		return parent.getInheritedBenefitClassName();
	}

	public String getInheritedBenefitClassId() {
		BOrgGroup parent = getParent();
		if (parent == null)
			return "";
		if (!isEmpty(parent.getDefaultBenefitClassId()))
			return parent.getDefaultBenefitClassId();
		return parent.getInheritedBenefitClassId();
	}

	public BenefitClass getInheritedBenefitClass() {
		BOrgGroup parent = getParent();
		if (parent == null)
			return null;
		if (parent.getDefaultBenefitClass() != null)
			return parent.getDefaultBenefitClass();
		return parent.getInheritedBenefitClass();
	}

	/**
	 * Gets all the parent org groups to all the org group ID's passed.  Only goes one level up.  Dedupped.
	 * 
	 * @param from Set of org_group ID's to get parents of
	 * @return the parent org group ID's
	 */
	public static Set<String> getImmediateParentIDs(Set<String> from) {
		Set<String> ret = new HashSet<String>();
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		for (String ogid : from) {
			Set<OrgGroupHierarchy> oghs = hsu.get(OrgGroup.class, ogid).getOrgGroupHierarchiesForChildGroupId();
			for (OrgGroupHierarchy ogh : oghs)
				ret.add(ogh.getParentGroupId());
		}
		return ret;
	}
	
	/**
	 * Gets all the parent org groups to all the org group passed.  Only goes one level up.  Dedupped.
	 * 
	 * @param from Set of org_groups to get parents of
	 * @return the parent org groups
	 */
	public static Set<BOrgGroup> getImmediateParents(Set<BOrgGroup> from) {
		Set<BOrgGroup> ret = new HashSet<BOrgGroup>();
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		for (BOrgGroup ogid : from) {
			Set<OrgGroupHierarchy> oghs = hsu.get(OrgGroup.class, ogid.getOrgGroupId()).getOrgGroupHierarchiesForChildGroupId();
			for (OrgGroupHierarchy ogh : oghs)
				ret.add(new BOrgGroup(ogh.getParentGroupId()));
		}
		return ret;
	}
	
	/**
	 * Gets all the parent org group to the org group ID passed.  Only goes one level up.  Dedupped.
	 * 
	 * @param from org_group ID to get parents of
	 * @return the parent org group ID's
	 */		
	public static Set<String> getImmediateParentIDs(String from) {
		Set<String> set = new HashSet<String>(1);
		set.add(from);
		return getImmediateParentIDs(set);
	}
	
	/**
	 * Gets all the parent org group to the org group ID passed.  Only goes one level up.  Dedupped.
	 * 
	 * @param from org_group ID to get parents of
	 * @return the parent org group ID's
	 */		
	public static Set<BOrgGroup> getImmediateParents(String from) {
		Set<BOrgGroup> set = new HashSet<BOrgGroup>(1);
		set.add(new BOrgGroup(from));
		return getImmediateParents(set);
	}
	
	/**
	 * Gets all the child org groups to all the org group ID's passed.  Only goes one level down.  Dedupped.
	 * 
	 * @param from Set of org_group ID's to get children of
	 * @return the child org group ID's
	 */
	public static Set<String> getImmediateChildrenIDs(Set<String> from) {
		Set<String> ret = new HashSet<String>();
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		for (String ogid : from) {
			Set<OrgGroupHierarchy> oghs = hsu.get(OrgGroup.class, ogid).getOrgGroupHierarchiesForParentGroupId();
			for (OrgGroupHierarchy ogh : oghs)
				ret.add(ogh.getChildGroupId());
		}
		return ret;
	}
	
	/**
	 * Gets all the child org groups to the org group ID passed.  Only goes one level down.  Dedupped.
	 * 
	 * @param from Set of org_group ID's to get children of
	 * @return the child org group ID's
	 */		
	public static Set<String> getImmediateChildrenIDs(String from) {
		Set<String> set = new HashSet<String>(1);
		set.add(from);
		return getImmediateChildrenIDs(set);
	}

	/**
	 * Is the object a parent of the child org group ID passed?  Keeps going all the way up the hierarchy until at can't go any higher.
	 * In other words, checks to see if it is a parent or a distant parent.  Works with multiple parents.
	 * 
	 * @param child child org group ID
	 * @return true if it is, false otherwise
	 */
	public boolean isParentOf(String child) {
		String parent = orgGroup.getOrgGroupId();
		Set<String> parentList = getImmediateParentIDs(child);
		while (!parentList.isEmpty()) {
			for (String pog : parentList)
				if (pog.equals(parent))
					return true;
			parentList = getImmediateParentIDs(parentList);
		}
		return false;
	}
	
	/**
	 * Is the object an immediate parent of the child org group ID passed?  Only goes one level up.
	 * Works with multiple parents.
	 * 
	 * @param child child org group ID
	 * @return true if it is, false otherwise
	 */
	public boolean isImmediateParentOf(String child) {
		String parent = orgGroup.getOrgGroupId();
		for (String pog : getImmediateParentIDs(child))
			if (pog.equals(parent))
				return true;
		return false;
	}
	
	/**
	 * Is the object an immediate parent of the child org group passed?  Only goes one level up.
	 * Works with multiple parents.
	 * 
	 * @param child child org group
	 * @return true if it is, false otherwise
	 */
	public boolean isImmediateParentOf(BOrgGroup child) {
		return isImmediateParentOf(child.getOrgGroupId());
	}
	
	/**
	 * Is the object an immediate parent of the child org group passed?  Only goes one level up.
	 * Works with multiple parents.
	 * 
	 * @param child child org group
	 * @return true if it is, false otherwise
	 */
	public boolean isImmediateParentOf(OrgGroup child) {
		return isImmediateParentOf(child.getOrgGroupId());
	}
	
	/**
	 * Is the object an immediate child of the parent org group ID passed?  Only goes one level up.
	 * Works with multiple children.
	 * 
	 * @param parent parent org group ID
	 * @return true if it is, false otherwise
	 */
	public boolean isImmediateChildOf(String parent) {
		String child = orgGroup.getOrgGroupId();
		for (String pog : getImmediateParentIDs(child))
			if (pog.equals(parent))
				return true;
		return false;
	}
	
	/**
	 * Is the object an immediate child of the parent org group passed?  Only goes one level up.
	 * Works with multiple children.
	 * 
	 * @param parent parent org group
	 * @return true if it is, false otherwise
	 */
	public boolean isImmediateChildOf(BOrgGroup parent) {
		return isImmediateChildOf(parent.getOrgGroupId());
	}
	
	/**
	 * Is the object an immediate child of the parent org group passed?  Only goes one level up.
	 * Works with multiple children.
	 * 
	 * @param parent parent org group
	 * @return true if it is, false otherwise
	 */
	public boolean isImmediateChildOf(OrgGroup parent) {
		return isImmediateChildOf(parent.getOrgGroupId());
	}
	
	/**
	 * Is the object a child of the parent org group ID passed?  Keeps going all the way down the hierarchy until at can't go any further.
	 * In other words, checks to see if it is a child or a distant child.  Works with multiple children.
	 * 
	 * @param parent parent org group ID
	 * @return true if it is, false otherwise
	 */
	public boolean isChildOf(String parent) {
		return (new BOrgGroup(parent)).isParentOf(this);
	}
	
	/**
	 * Returns the relationship between the parent org group (this) and a Set of children.  The returned value
	 * is 0 if one of the children is in the given parent group.  1 is returned if one of the child
	 *
	 * @return the relationship level number
	 */
	public int getMinimumMemberLevel(Set<BOrgGroup> children) {
		int level = 0;
		String parent = orgGroup.getOrgGroupId();
		
		while (!children.isEmpty()) {
			for (BOrgGroup og : children)
				if (parent.equals(og.getOrgGroupId()))
					return level;  // in the exact group
			children = getImmediateParents(children);
			level++;
		}
		return -1;	//  not in any	
	}
	
	/**
	 * Is the object a child of the parent org group passed?  Keeps going all the way down the hierarchy until at can't go any further.
	 * In other words, checks to see if it is a child or a distant child.  Works with multiple children.
	 * 
	 * @param parent parent org group
	 * @return true if it is, false otherwise
	 */
	public boolean isChildOf(BOrgGroup parent) {
		return parent.isParentOf(this);
	}
	
	/**
	 * Is the object a child of the parent org group passed?  Keeps going all the way down the hierarchy until at can't go any further.
	 * In other words, checks to see if it is a child or a distant child.  Works with multiple children.
	 * 
	 * @param parent parent org group
	 * @return true if it is, false otherwise
	 */
	public boolean isChildOf(OrgGroup parent) {
		return (new BOrgGroup(parent)).isParentOf(this);
	}
	
	/**
	 * Is the object a parent of the child org group passed?  Keeps going all the way up the hierarchy until at can't go any higher.
	 * In other words, checks to see if it is a parent or a distant parent.  Works with multiple parents.
	 * 
	 * @param child child org group
	 * @return true if it is, false otherwise
	 */
	public boolean isParentOf(BOrgGroup child) {
		return isParentOf(child.getOrgGroupId());
	}
	
	/**
	 * Is the object a parent of the child org group passed?  Keeps going all the way up the hierarchy until at can't go any higher.
	 * In other words, checks to see if it is a parent or a distant parent.  Works with multiple parents.
	 * 
	 * @param child child org group
	 * @return true if it is, false otherwise
	 */
	public boolean isParentOf(OrgGroup child) {
		return isParentOf(child.getOrgGroupId());
	}
	
	public char getTimesheetPeriodType() {
		return orgGroup.getTimesheetPeriodType();
	}

	public void setTimesheetPeriodType(char timesheetPeriodType) {
		orgGroup.setTimesheetPeriodType(timesheetPeriodType);
	}

	public int getTimesheetPeriodStartDate() {
		return orgGroup.getTimesheetPeriodStartDate();
	}

	public void setTimesheetPeriodStartDate(int timesheetPeriodStartDate) {
		orgGroup.setTimesheetPeriodStartDate(timesheetPeriodStartDate);
	}

	public char getTimesheetShowBillable() {
		return orgGroup.getTimesheetShowBillable();
	}

	public void setTimesheetShowBillable(char timesheetShowBillable) {
		orgGroup.setTimesheetShowBillable(timesheetShowBillable);
	}

	public static void main(String[] args) {
		ABCL.init();
		BOrgGroup bog;
		List<String> slst;
		List<OrgGroup> oglst;
		
		bog = new BOrgGroup("00001-0000000276");
		List<OrgGroup> lst = bog.getOrgGroupHierarchyBreadthFirst();
		for (OrgGroup og : lst) 
			System.out.println(og.getName());
		if (true)
			return;
		
		
		bog = new BOrgGroup("00001-0000000238");
		slst = bog.getAllOrgGroupsInHierarchy();
		for (String sog : slst) {
			bog = new BOrgGroup(sog);
			System.out.println(bog.getOrgGroupId() + "  " + bog.getName());
		}
		System.out.println();
		
		bog = new BOrgGroup("00001-0000000247");
		oglst = bog.getAllParentOrgGroups();
		for (OrgGroup og : oglst)
			System.out.println(og.getOrgGroupId() + "  " + og.getName());
		System.out.println();
		
		bog = new BOrgGroup("00001-0000000245");
		Set<OrgGroupHierarchy>  ohglst = bog.getOrgGroup().getOrgGroupHierarchiesForParentGroupId();
		for (OrgGroupHierarchy ogh : ohglst)
			System.out.println(ogh.getParentGroupId() + "  " + ogh.getChildGroupId());
		System.out.println();
		
		slst = new LinkedList<String>();
		slst.add("00001-0000000247");
		slst.add("00001-0000000255");
		slst = getAllOrgGroupParents(slst);
		for (String s : slst) {
			System.out.println(s);
		}
		System.out.println();
		
		Set<String> ss = getImmediateParentIDs("00001-0000000247");
		for (String s : ss)
			System.out.println(s);
		System.out.println();
		
		ss = new HashSet<String>();
		ss.add("00001-0000000247");
		ss.add("00001-0000000248");
		ss = getImmediateParentIDs(ss);
		for (String s : ss)
			System.out.println(s);
		System.out.println();
		
		bog = new BOrgGroup("00001-0000000245");
		System.out.println(bog.isParentOf("00001-0000000247"));
		System.out.println(bog.isParentOf("00001-0000000238"));
		System.out.println(bog.isParentOf("00001-0000000254"));
		System.out.println();
		
		bog = new BOrgGroup("00001-0000000238");
		System.out.println(bog.isParentOf("00001-0000000247"));
		System.out.println(bog.isParentOf("00001-0000000238"));
		System.out.println(bog.isParentOf("00001-0000000254"));
		System.out.println(bog.isParentOf("00001-0000000245"));
		System.out.println();
		
		bog = new BOrgGroup("00001-0000000245");
		System.out.println(bog.isChildOf("00001-0000000247"));
		System.out.println(bog.isChildOf("00001-0000000238"));
		System.out.println(bog.isChildOf("00001-0000000254"));
		System.out.println();
		
		bog = new BOrgGroup("00001-0000000245");
		System.out.println(bog.isImmediateParentOf("00001-0000000247"));
		System.out.println(bog.isImmediateParentOf("00001-0000000238"));
		System.out.println(bog.isImmediateParentOf("00001-0000000254"));
		System.out.println();
	
		ss = getImmediateChildrenIDs("00001-0000000238");
		for (String s : ss)
			System.out.println(s);
		System.out.println();
		
		Set<BOrgGroup> bogs = new HashSet<BOrgGroup>();
		bogs.add(new BOrgGroup("00001-0000000246"));  //  child
		System.out.println(new BOrgGroup("00001-0000000253").getMinimumMemberLevel(bogs));
		System.out.println();
	}
	
}
