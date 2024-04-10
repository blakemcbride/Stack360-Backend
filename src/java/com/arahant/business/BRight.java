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
import com.arahant.business.interfaces.RightNames;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantJessException;
import com.arahant.reports.SecurityTokenReport;
import com.arahant.utils.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import jess.Fact;
import jess.JessException;

public class BRight extends BusinessLogicBase implements IDBFunctions, BRightOrSecurityGroup, RightNames, Comparable<BRight> {

	private short accessLevel = 0;
	private Right right;
	private static final transient ArahantLogger logger = new ArahantLogger(BRight.class);

	public BRight() {
	}

	/**
	 * @param right
	 */
	public BRight(final Right right) {
		this.right = right;
	}

    /**
     *
     * @param key
     * @throws ArahantException
     */
	public BRight(final String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 * @param association
	 */
	public BRight(final RightsAssociation association) {
		right = association.getRight();
		accessLevel = association.getAccessLevel();
	}

    /**
     *
     * @param key
     * @param lvl
     * @throws ArahantException
     */
	public BRight(final String key, final int lvl) throws ArahantException {
		internalLoad(key);
		accessLevel = (short) lvl;
	}

	@Override
	public String create() throws ArahantException {
		right = new Right();
		right.setRightId(IDGenerator.generate(right));
		return getRightId();
	}

	@Override
	public void delete() throws ArahantDeleteException {
		ArahantSession.getHSU().delete(right);
	}

	@Override
	public void insert() throws ArahantException {
		final HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (hsu.createCriteria(Right.class).eq(Right.IDENTIFIER, right.getIdentifier()).exists())
			throw new ArahantException("Can't add token; token already exists.");
		hsu.insert(right);
	}

	private void internalLoad(final String key) throws ArahantException {
		logger.debug("Loading " + key);
		right = ArahantSession.getHSU().get(Right.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(right);
	}

	public static BRight[] list() {
		final HibernateSessionUtil hsu = ArahantSession.getHSU();

		final List<Right> l = hsu.createCriteria(Right.class).orderBy(Right.IDENTIFIER).list();

		final BRight[] ret = new BRight[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BRight(l.get(loop));
		return ret;
	}

	public static String getCurrentSecurityGroupId(String companyId) {
	    HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (hsu.currentlyArahantUser())
			return "";

		String user = hsu.getCurrentPerson().getProphetLogin().getUserLogin();

		Person p = hsu.createCriteriaNoCompanyFilter(Person.class).joinTo(Person.PROPHETLOGINS).eq(ProphetLogin.USERLOGIN, user).first();

		if (p == null)
			return "";

		//if there are no overrides for this user, just go the old way
		ProphetLoginOverride po = hsu.createCriteria(ProphetLoginOverride.class).eq(ProphetLoginOverride.PERSON, p).joinTo(ProphetLoginOverride.COMPANY).eq(CompanyDetail.ORGGROUPID, companyId).first();

		String securityGroupId;
		if (po == null) {
			SecurityGroup sg = BPerson.getCurrent().getProphetLogin().getSecurityGroup();
			if (sg == null)
				securityGroupId = "";
			else
				securityGroupId = sg.getSecurityGroupId();
		} else
			securityGroupId = po.getSecurityGroup().getSecurityGroupId();

		return securityGroupId;
	}

	public static int checkAIRight(final HibernateSessionUtil hsu, final String rightName) {
		return checkAIRight(hsu, rightName, getCurrentSecurityGroupId(hsu.getCurrentCompany().getOrgGroupId()));
	}

	public static int checkAIRight(final HibernateSessionUtil hsu, final String rightName, String securityGroupId) {
		int level = ACCESS_LEVEL_NOT_VISIBLE;
		try {
			final JessBean jb = new JessBean(ArahantSession.getSecurityAI());
			//	jb.getAIEngine().watchAll();
			jb.runAIEngine();

			final Map<String, String> q = new HashMap<String, String>();

			q.put("security_group_id", "\"" + securityGroupId + "\"");


			final String rightId = hsu.createCriteria(Right.class).eq(Right.IDENTIFIER, rightName).first().getRightId();

			q.put("right_id", "\"" + rightId + "\"");

			final List<Fact> facts = jb.queryForFacts("rights_association", q);

			if (facts.size() > 0)
				level = jb.getFactIntValue(facts.iterator().next(), "access_level");

			//jb.executeAICommand("(facts)");
		} catch (final NullPointerException npe) {
			logger.warn("Right is missing : " + rightName);
			//logger.error("NPE caused by right "+rightName);
			//logger.error(npe);
			return 0;
		} catch (final JessException je) {
			throw new ArahantJessException(je);
		}
		return level;
	}

	@Override
	public String getDescription() {
		return right.getDescription();
	}

	public String getIdentifier() {
		return right.getIdentifier();
	}

	/**
	 * @param description
	 * @see com.arahant.beans.Right#setDescription(java.lang.String)
	 */
	public void setDescription(final String description) {
		right.setDescription(description);
	}

	/**
	 * @param identifier
	 * @see com.arahant.beans.Right#setIdentifier(java.lang.String)
	 */
	public void setIdentifier(final String identifier) {
		right.setIdentifier(identifier);
	}

	public String getRightId() {
		return right.getRightId();
	}

	/**
	 * @param ids
	 * @throws ArahantDeleteException
	 */
	public static void delete(final String[] ids) throws ArahantDeleteException {
		final HibernateSessionUtil hsu = ArahantSession.getHSU();

		final List<Right> rights = hsu.createCriteria(Right.class).in(Right.RIGHTID, ids).list();

		for (final Right right : rights)
			new BRight(right).delete();
	}

	/**
	 * @return Returns the accessLevel.
	 */
	public short getAccessLevel() {
		return accessLevel;
	}

	/**
	 * @param accessLevel The accessLevel to set.
	 */
	public void setAccessLevel(final short accessLevel) {
		this.accessLevel = accessLevel;
	}

	@Override
	public String getTokenAccessLevel() {
		switch (accessLevel) {
			case 0:
				return "None";
			case 1:
				return "Read";
			case 2:
				return "Write";
			default:
				return "Error";
		}
	}

	@Override
	public String getDbId() {
		return getRightId();
	}

	@Override
	public String getName() {
		return getIdentifier();
	}

	@Override
	public String getType() {
		return "Token";
	}

	public static String getReport() throws ArahantException {
		return new SecurityTokenReport().getReport(ArahantSession.getHSU().createCriteria(Right.class).orderBy(Right.IDENTIFIER).list());
	}

	public static int checkRight(final String rightName) throws ArahantJessException {

		final HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (hsu.currentlyArahantUser())
			return ACCESS_LEVEL_WRITE;

		final String companyId = hsu.getCurrentCompany().getCompanyId();

		String securityGroupId = getCurrentSecurityGroupId(companyId);

		//see if token exists and add it if it doesn't
		if (!hsu.createCriteria(Right.class).eq(Right.IDENTIFIER, rightName).exists()) {
			Right st = new Right();
			st.generateId();
			st.setIdentifier(rightName);
			st.setDescription(rightName);
			hsu.insert(st);
		}

		boolean superUser = checkAIRight(hsu, Right.RIGHT_SUPER_USER, companyId) == ACCESS_LEVEL_WRITE;

		//access all even more exclusive than super user
		if (!rightName.equals(Right.RIGHT_CAN_ACCESS_ALL_COMPANIES) && superUser)
			return ACCESS_LEVEL_WRITE;

		//if I'm checking for access all companies, need to look at main sec group, not overrides
		if (rightName.equals(Right.RIGHT_CAN_ACCESS_ALL_COMPANIES))
			securityGroupId = BPerson.getCurrent().getProphetLogin().getSecurityGroup().getSecurityGroupId();

		return checkAIRight(hsu, rightName, securityGroupId);
	}

    /**
     *
     * @param rightName
     * @param access_level
     * @return
     * @throws ArahantJessException
     */
	public static List<Person> getAllPeopleWithRight(final String rightName, final int access_level) throws ArahantJessException {
		final HibernateSessionUtil hsu = ArahantSession.getHSU();

		final List<String> securityGroups = new LinkedList<String>();

		List<Person> ret;

		try {
			final JessBean jb = new JessBean();
			jb.loadTableFacts("security_group_hierarchy");
			jb.loadTableFacts("rights_association");
			jb.loadScript("SecurityGroupRules.jess");
			jb.runAIEngine();

			final Map<String, String> q = new HashMap<String, String>();

			//q.put("security_group_id", "\""+hsu.getCurrentPerson().getProphetLogin().getSecurityGroup().getSecurityGroupId()+"\"");

			final String rightId = hsu.createCriteria(Right.class).eq(Right.IDENTIFIER, rightName).first().getRightId();

			q.put("right_id", "\"" + rightId + "\"");

			final List<Fact> facts = jb.queryForFacts("rights_association", q);

			if (facts.size() > 0) {
				final Fact fact = facts.iterator().next();
				final int level = jb.getFactIntValue(fact, "access_level");
				if (level >= access_level)
					securityGroups.add(jb.getFactStringValue(fact, "security_group_id"));
			}
			ret = hsu.createCriteria(Person.class).joinTo(Person.PROPHETLOGINS).joinTo(ProphetLogin.SECURITYGROUP).in(SecurityGroup.SECURITYGROUPID, securityGroups).list();
		} catch (final NullPointerException npe) {
			logger.error("NPE caused by right " + rightName);
			logger.error(npe);
			throw npe;
		} catch (final JessException je) {
			throw new ArahantJessException(je);
		}
		return ret;
	}

	@Override
	public int compareTo(final BRight o) {
		return getName().compareTo(o.getName());
	}
}
