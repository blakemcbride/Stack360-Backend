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
import com.arahant.business.interfaces.BRightOrSecurityGroup;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantJessException;
import com.arahant.exceptions.ArahantSecurityException;
import com.arahant.utils.*;
import java.util.*;
import java.util.Collections;
import jess.Fact;
import jess.JessException;

public class BSecurityGroup extends BusinessLogicBase implements IDBFunctions, BRightOrSecurityGroup {

	public BSecurityGroup() {
	}

	/**
	 * @param group
	 */
	public BSecurityGroup(final SecurityGroup group) {
		securityGroup = group;
	}

	/**
	 * @param groupId
	 * @throws ArahantException
	 */
	public BSecurityGroup(final String key) throws ArahantException {
		internalLoad(key);
	}
	
	private SecurityGroup securityGroup;

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#create()
	 */
	@Override
	public String create() throws ArahantException {

		securityGroup = new SecurityGroup();
		securityGroup.setSecurityGroupId(IDGenerator.generate(securityGroup));

		return securityGroup.getSecurityGroupId();
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#delete()
	 */
	@Override
	public void delete() throws ArahantDeleteException, ArahantSecurityException {
		final HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.delete(securityGroup.getRightsAssociations());
		hsu.delete(securityGroup.getSecurityGroupHierarchiesForChildSecurityGroupId());
		hsu.delete(securityGroup);
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#insert()
	 */
	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(securityGroup);
	}

	private void internalLoad(final String key) throws ArahantException {
		securityGroup = ArahantSession.getHSU().get(SecurityGroup.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#update()
	 */
	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(securityGroup);
	}

	/**
	 * @return @see com.arahant.beans.SecurityGroup#getId()
	 */
	public String getId() {
		return securityGroup.getId();
	}

	/**
	 * @return @see com.arahant.beans.SecurityGroup#getName()
	 */
	@Override
	public String getName() {
		return securityGroup.getId();
	}

	/**
	 * @return @see com.arahant.beans.SecurityGroup#getSecurityGroupId()
	 */
	public String getSecurityGroupId() {
		return securityGroup.getSecurityGroupId();
	}

	/**
	 * @param id
	 * @see com.arahant.beans.SecurityGroup#setId(java.lang.String)
	 */
	public void setId(final String id) {
		securityGroup.setId(id);
	}

	/**
	 * @param name
	 * @see com.arahant.beans.SecurityGroup#setName(java.lang.String)
	 */
	public void setName(final String name) {
		securityGroup.setId(name);
	}

	/**
	 * @param securityGroupId
	 * @see com.arahant.beans.SecurityGroup#setSecurityGroupId(java.lang.String)
	 */
	public void setSecurityGroupId(final String securityGroupId) {
		securityGroup.setSecurityGroupId(securityGroupId);
	}

	/**
	 * @return
	 */
	public static BSecurityGroup[] list() {
		BSecurityGroup[] ret;

		final HibernateSessionUtil hsu = ArahantSession.getHSU();

		final List<SecurityGroup> l = hsu.createCriteria(SecurityGroup.class).orderBy(SecurityGroup.ID).list();

		ret = new BSecurityGroup[l.size()];

		for (int loop = 0; loop < l.size(); loop++)
			ret[loop] = new BSecurityGroup(l.get(loop));

		return ret;
	}

	/**
	 * @param cap
	 * @throws ArahantException
	 */
	public BPerson[] listMembers(final int cap) throws ArahantException {
		BPerson[] ret;
		final List<Person> l = ArahantSession.getHSU().createCriteria(Person.class).orderBy(Person.LNAME).orderBy(Person.FNAME).setMaxResults(cap).joinTo(Person.PROPHETLOGINS).eq(ProphetLogin.SECURITYGROUP, securityGroup).list();

		ret = new BPerson[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BPerson(l.get(loop));

		return ret;
	}

	/**
	 * @return
	 */
	public BRightOrSecurityGroup[] listRights() {
		BRightOrSecurityGroup[] ret;

		final List<RightsAssociation> l = ArahantSession.getHSU().createCriteria(RightsAssociation.class).eq(RightsAssociation.SECURITYGROUP, securityGroup).joinTo(RightsAssociation.RIGHT).orderBy(Right.IDENTIFIER).list();


		final List<SecurityGroup> sgl = ArahantSession.getHSU().createCriteria(SecurityGroup.class).orderBy(SecurityGroup.ID).joinTo(SecurityGroup.SECURITYGROUPHIERARCHIESFORPARENTSECURITYGROUPID).eq(SecurityGroupHierarchy.SECURITYGROUPBYCHILDSECURITYGROUPID, securityGroup).list();


		ret = new BRightOrSecurityGroup[l.size() + sgl.size()];

		for (int loop = 0; loop < sgl.size(); loop++)
			ret[loop] = new BSecurityGroup(sgl.get(loop));

		for (int loop = sgl.size(); loop < l.size() + sgl.size(); loop++)
			ret[loop] = new BRight(l.get(loop - sgl.size()));

		return ret;
	}

	/*
	 * (non-Javadoc) @see
	 * com.arahant.business.interfaces.BRightOrSecurityGroup#getDbId()
	 */
	@Override
	public String getDbId() {

		return getSecurityGroupId();
	}

	/*
	 * (non-Javadoc) @see
	 * com.arahant.business.interfaces.BRightOrSecurityGroup#getDescription()
	 */
	@Override
	public String getDescription() {

		return securityGroup.getName();
	}

	/*
	 * (non-Javadoc) @see
	 * com.arahant.business.interfaces.BRightOrSecurityGroup#getTokenAccessLevel()
	 */
	@Override
	public String getTokenAccessLevel() {

		return "Group";
	}

	/*
	 * (non-Javadoc) @see
	 * com.arahant.business.interfaces.BRightOrSecurityGroup#getType()
	 */
	@Override
	public String getType() {

		return "Group";
	}

	/**
	 * @param description
	 */
	public void setDescription(final String description) {
		securityGroup.setName(description);
	}

	/**
	 * @param groupIds
	 * @throws ArahantException
	 * @throws ArahantSecurityException
	 * @throws ArahantDeleteException
	 */
	public static void delete(final String[] groupIds) throws ArahantDeleteException, ArahantSecurityException, ArahantException {
		for (final String element : groupIds)
			new BSecurityGroup(element).delete();
	}

	/**
	 * @param name
	 * @param parentGroupId
	 * @param typeIndicator
	 * @param cap
	 * @return
	 */
	public static BRightOrSecurityGroup[] search(final String name, final String parentGroupId, final int typeIndicator, final int cap) {

		final HibernateSessionUtil hsu = ArahantSession.getHSU();

		BRightOrSecurityGroup[] ret;

		List<BRightOrSecurityGroup> l;

		final List<BRight> rl = new LinkedList<BRight>();
		final List<BSecurityGroup> sgl = new LinkedList<BSecurityGroup>();


		// need to filter out ones already assigned
		if (typeIndicator == 0 || typeIndicator == 1) {
			final List<Right> rlist = hsu.createCriteria(Right.class).orderBy(Right.IDENTIFIER).like(Right.IDENTIFIER, name).setMaxResults(cap).list();

			final List<Right> r2 = hsu.createCriteria(Right.class).joinTo(Right.RIGHTSASSOCIATIONS).joinTo(RightsAssociation.SECURITYGROUP).eq(SecurityGroup.SECURITYGROUPID, parentGroupId).list();

			rlist.removeAll(r2);

			for (final Right right : rlist)
				rl.add(new BRight(right));
		}

//		 need to filter out ones already assigned
		if (typeIndicator == 0 || typeIndicator == 2) {
			final List<SecurityGroup> rlist = hsu.createCriteria(SecurityGroup.class).like(SecurityGroup.ID, name).orderBy(SecurityGroup.ID).setMaxResults(cap - rl.size()).list();

			final List<SecurityGroup> r2 = hsu.createCriteria(SecurityGroup.class).joinTo(SecurityGroup.SECURITYGROUPHIERARCHIESFORPARENTSECURITYGROUPID).joinTo(SecurityGroupHierarchy.SECURITYGROUPBYCHILDSECURITYGROUPID).eq(SecurityGroup.SECURITYGROUPID, parentGroupId).list();

			rlist.removeAll(r2);
			rlist.remove(hsu.get(SecurityGroup.class, parentGroupId));

			for (final SecurityGroup group : rlist)
				sgl.add(new BSecurityGroup(group));
		}



		l = new ArrayList<BRightOrSecurityGroup>(rl.size() + sgl.size());
		l.addAll(rl);
		l.addAll(sgl);

		ret = new BRightOrSecurityGroup[l.size()];

		for (int loop = 0; loop < l.size(); loop++)
			ret[loop] = l.get(loop);

		return ret;
	}

	/**
	 * @param tokenIds
	 * @param tokenAccessLevel
	 */
	public void assignRights(final String[] tokenIds, final int tokenAccessLevel) {
		for (final String element : tokenIds)
			setAccessLevel(element, tokenAccessLevel);

	}

	/**
	 * @param groupIds
	 */
	public void assignGroups(final String[] groupIds) {
		for (final String element : groupIds) {
			final SecurityGroupHierarchy sgh = new SecurityGroupHierarchy();
			sgh.setSecurityGroupByChildSecurityGroupId(securityGroup);
			sgh.setSecurityGroupByParentSecurityGroupId(ArahantSession.getHSU().get(SecurityGroup.class, element));
			final SecurityGroupHierarchyId sghid = new SecurityGroupHierarchyId();
			sghid.setChildSecurityGroupId(getSecurityGroupId());
			sghid.setParentSecurityGroupId(element);
			sgh.setId(sghid);
			ArahantSession.getHSU().saveOrUpdate(sgh);
		}
	}

	/**
	 * @param tokenIds
	 * @throws ArahantDeleteException
	 */
	public void unassignRights(final String[] tokenIds) throws ArahantDeleteException {
		ArahantSession.getHSU().createCriteria(RightsAssociation.class).eq(RightsAssociation.SECURITYGROUP, securityGroup).joinTo(RightsAssociation.RIGHT).in(Right.RIGHTID, tokenIds).delete();
	}

	/**
	 * @param groupIds
	 * @throws ArahantDeleteException
	 */
	public void unassignGroups(final String[] groupIds) throws ArahantDeleteException {
		ArahantSession.getHSU().createCriteria(SecurityGroupHierarchy.class).eq(SecurityGroupHierarchy.SECURITYGROUPBYPARENTSECURITYGROUPID, securityGroup).joinTo(SecurityGroupHierarchy.SECURITYGROUPBYCHILDSECURITYGROUPID).in(SecurityGroup.SECURITYGROUPID, groupIds).delete();
	}

	/**
	 * @param tokenId
	 * @param accessLevel
	 */
	public void setAccessLevel(final String tokenId, final int tokenAccessLevel) {

		final RightsAssociation ra = new RightsAssociation();
		ra.setAccessLevel((short) tokenAccessLevel);
		ra.setSecurityGroup(securityGroup);
		ra.setRight(ArahantSession.getHSU().get(Right.class, tokenId));
		final RightsAssociationId rid = new RightsAssociationId();
		rid.setRightId(tokenId);
		rid.setSecurityGroupId(getSecurityGroupId());
		ra.setId(rid);
		ArahantSession.getHSU().saveOrUpdate(ra);

	}

	public BRight[] getEffectiveRightList() throws ArahantException {
		try {
			BRight[] ret;

			final JessBean jb = new JessBean(ArahantSession.getSecurityAI());
			jb.runAIEngine();

			final Map<String, String> q = new HashMap<String, String>();

			q.put("security_group_id", "\"" + getSecurityGroupId() + "\"");

			final List<Fact> facts = jb.queryForFacts("rights_association", q);

			ret = new BRight[facts.size()];

			for (int loop = 0; loop < ret.length; loop++)
				ret[loop] = new BRight(jb.getFactStringValue(facts.get(loop), "right_id"), jb.getFactIntValue(facts.get(loop), "access_level"));


			final List<BRight> brList = new ArrayList<BRight>(ret.length);
			Collections.addAll(brList, ret);

			Collections.sort(brList);

			return brList.toArray(ret);
		} catch (final JessException je) {
			throw new ArahantJessException(je);
		}
	}

	/**
	 * @param name
	 * @param cap
	 * @return
	 */
	public static BSecurityGroup[] searchSecurityGroups(final String name, final int cap) {
		return searchSecurityGroups(name, cap, ArahantSession.getHSU().getCurrentCompany().getOrgGroupId());
	}

	public static BSecurityGroup[] searchSecurityGroups(final String name, final int cap, String companyId) {
		BSecurityGroup[] ret;

		final HibernateSessionUtil hsu = ArahantSession.getHSU();
		final List<SecurityGroup> l;


		if (!(hsu.currentlyArahantUser() || (!ArahantSession.multipleCompanySupport && hsu.currentlySuperUser(companyId))))
			l = hsu.createCriteria(SecurityGroup.class).like(SecurityGroup.ID, name).orderBy(SecurityGroup.ID).joinTo(SecurityGroup.SECURITY_GROUPS).eq(SecurityGroup.SECURITYGROUPID, BPerson.getCurrent().getSecurityGroupId(companyId)).setMaxResults(cap).list();
		else
			l = hsu.createCriteria(SecurityGroup.class).like(SecurityGroup.ID, name).orderBy(SecurityGroup.ID).setMaxResults(cap).list();

		ret = new BSecurityGroup[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BSecurityGroup(l.get(loop));

		return ret;
	}

	public static BSecurityGroup[] searchSecurityGroups(final String name, final int cap, final String[] excludeIds) {
		BSecurityGroup[] ret;

		final HibernateSessionUtil hsu = ArahantSession.getHSU();
		final List<SecurityGroup> l = hsu.createCriteria(SecurityGroup.class).like(SecurityGroup.ID, name).notIn(SecurityGroup.SECURITYGROUPID, excludeIds).orderBy(SecurityGroup.ID).setMaxResults(cap).list();

		ret = new BSecurityGroup[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BSecurityGroup(l.get(loop));

		return ret;
	}

	public BScreenGroup[] listAssociatedScreenGroups() {
		HibernateCriteriaUtil<ScreenGroup> hcu = ArahantSession.getHSU().createCriteria(ScreenGroup.class).orderBy(ScreenGroup.NAME);
		hcu.joinTo(ScreenGroup.SECURITY_GROUPS).eq(SecurityGroup.SECURITYGROUPID, securityGroup.getSecurityGroupId());
		return BScreenGroup.makeArray(hcu.list());
	}

	public BSecurityGroup[] listAssociatedSecurityGroups() {
		HibernateCriteriaUtil<SecurityGroup> hcu = ArahantSession.getHSU().createCriteria(SecurityGroup.class).orderBy(SecurityGroup.NAME);
		hcu.joinTo(SecurityGroup.SECURITY_GROUPS).eq(SecurityGroup.SECURITYGROUPID, securityGroup.getSecurityGroupId());
		return makeArray(hcu.list());
	}

	private static BSecurityGroup[] makeArray(List<SecurityGroup> l) {
		BSecurityGroup[] ret = new BSecurityGroup[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BSecurityGroup(l.get(loop));
		return ret;
	}

	public void associateToSecurityGroups(final String[] securityGroupIds) throws ArahantException {
		securityGroup.getAllowedGroups().clear();

		for (final String element : securityGroupIds)
			securityGroup.getAllowedGroups().add(ArahantSession.getHSU().get(SecurityGroup.class, element));
		update();
	}

	public void associateToScreenGroups(final String[] screenGroupIds) throws ArahantException {
		securityGroup.getScreenGroups().clear();
		for (final String element : screenGroupIds)
			securityGroup.getScreenGroups().add(ArahantSession.getHSU().get(ScreenGroup.class, element));
		update();
	}

	public BSecurityGroup[] listAvailableSecurityGroups(final String[] excludeIds) {
		HibernateCriteriaUtil<SecurityGroup> hcu = ArahantSession.getHSU().createCriteria(SecurityGroup.class).orderBy(SecurityGroup.NAME);
		hcu.notIn(SecurityGroup.SECURITYGROUPID, excludeIds);
		return makeArray(hcu.list());
	}
}
