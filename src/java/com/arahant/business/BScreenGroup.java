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

import com.arahant.beans.Screen;
import com.arahant.beans.ScreenGroup;
import com.arahant.beans.ScreenGroupHierarchy;
import com.arahant.beans.SecurityGroup;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.reports.SiteMapReport;
import com.arahant.utils.*;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.util.*;
import java.util.Collections;

public class BScreenGroup extends BScreenOrGroup implements IDBFunctions {

	private static final transient ArahantLogger logger = new ArahantLogger(BScreenGroup.class);
	private ScreenGroup screenGroup;
	private int seqNo = 0;

	public BScreenGroup() {}

	/**
	 * @param group
	 */
	public BScreenGroup(final ScreenGroup group) {
		screenGroup = group;
	}

	/**
	 * @param screenGroupId
	 */
	public BScreenGroup(final String screenGroupId) {
		internalLoad(screenGroupId);
	}

	/**
	 * @param hierarchy
	 */
	public BScreenGroup(final ScreenGroupHierarchy hierarchy) {
		screenGroup = hierarchy.getChildScreenGroup();
		seqNo = hierarchy.getSeqNo();
	}

	public ScreenGroup getBean() {
		return screenGroup;
	}

	private void internalLoad(final String key) {
		logger.debug("Loading " + key);
		screenGroup = ArahantSession.getHSU().get(ScreenGroup.class, key);
	}

	@Override
	public void load(final String key) {
		internalLoad(key);
	}

	public static List<BScreenOrGroup> getEnrollmentWizardGroups() {
		List<ScreenGroup> sgl = ArahantSession.getHSU().createCriteria(ScreenGroup.class).eq(ScreenGroup.WIZARD_TYPE, 'E').list();
		List<BScreenOrGroup> bsgl = new ArrayList<BScreenOrGroup>();
//		if(sgl.size() == 0)
//		{
//			BScreenGroup bsg = new BScreenGroup();
//			bsg.create();
//			bsg.setDescription("Enrollment Wizard");
//			bsg.setName("Enrollment Wizard");
//			bsg.setWizardType("E");
//			bsg.insert();
//			bsgl.add(bsg);
//		}

		for (ScreenGroup sg : sgl)
			bsgl.add(new BScreenGroup(sg));

		return bsgl;
	}

	private static List<ScreenGroup> filterGroupsThatAreChildren(List<ScreenGroup> orgList, final ScreenGroup og) {
		if (ArahantSession.getHSU().currentlySuperUser())
			return orgList;

		if (og == null)
			return orgList;

        for (ScreenGroupHierarchy ogh : og.getScreenGroupHierarchiesForParentScreenGroup()) {
            orgList.remove(ogh.getChildScreenGroup());

            if (og.equals(ogh.getChildScreenGroup()))
                continue;

            orgList = filterGroupsThatAreChildren(orgList, ogh.getChildScreenGroup());
        }

		return orgList;
	}

	private static List<ScreenGroup> filterGroupsThatAreParents(List<ScreenGroup> orgList, final ScreenGroup og) {
		if (ArahantSession.getHSU().currentlySuperUser())
			return orgList;

		if (og == null)
			return orgList;

        for (ScreenGroupHierarchy ogh : og.getScreenGroupHierarchiesForChildScreenGroup()) {
            orgList.remove(ogh.getParentScreenGroup());

            if (og.equals(ogh.getParentScreenGroup()))
                continue;

            orgList = filterGroupsThatAreParents(orgList, ogh.getParentScreenGroup());
        }

		return orgList;
	}

	public static BScreenOrGroup[] searchScreenGroupsWithParents(final HibernateSessionUtil hsu, final String name, final String extId, final int assocInd, final String screenGroupId, final int type, final int max) {
		BScreenOrGroup [] ret;

		BScreenGroup [] st = new BScreenGroup[0];

		if (type == 0 || type == 2) {
			final HibernateCriteriaUtil<ScreenGroup> hcu = hsu.createCriteria(ScreenGroup.class).distinct();
			hcu.orderBy(ScreenGroup.NAME);

			if (max > 0)
				hcu.setMaxResults(max);


			hcu.like(ScreenGroup.NAME, name);

			if (!isEmpty(extId))
				hcu.eq(ScreenGroup.SCREENGROUPID, IDGenerator.expandKey(extId));

			switch (assocInd) {
				case 0:
					break;
				case 1:
					hcu.sizeNe(ScreenGroup.SCREENGROUPHIERARCHIESFORCHILDSCREENGROUP, 0);
					break;
				case 2:
					hcu.sizeEq(ScreenGroup.SCREENGROUPHIERARCHIESFORCHILDSCREENGROUP, 0);
					break;
			}

			List<ScreenGroup> sgList;
			if (!isEmpty(screenGroupId)) {
				final ScreenGroup sg = hsu.get(ScreenGroup.class, screenGroupId);
				sgList = hcu.list();
				sgList = filterGroupsThatAreChildren(sgList, sg);
				sgList = filterGroupsThatAreParents(sgList, sg);

				if (!ArahantSession.getHSU().currentlySuperUser())
					sgList.remove(sg);

			} else
				sgList = hcu.list();

			if (!sgList.isEmpty()) {

				st = new BScreenGroup[sgList.size()];

				for (int loop = 0; loop < st.length; loop++)
					st[loop] = new BScreenGroup(sgList.get(loop));
			}
		}

		BScreen [] sct = new BScreen[0];
		if (type == 0 || type == 1) {
			HibernateCriteriaUtil<Screen> hcu = hsu.createCriteria(Screen.class).distinct();
			hcu.orderBy(Screen.NAME);
			hcu.ne(Screen.SCREENTYPE, (short) 5);

			//hcu.ne(Screen.SCREENTYPE, (short)2); allow parent screens

			if (max > 0)
				hcu.setMaxResults(max - st.length);

			if (!isEmpty(name))
				hcu.like(Screen.NAME, name);

			if (!isEmpty(extId))
				hcu.eq(Screen.SCREENID, IDGenerator.expandKey(extId));

			switch (assocInd) {
				case 0:
					break;
				case 1:
					hcu.makeCriteria().or(hcu.makeCriteria().sizeNe(Screen.SCREENGROUPHIERARCHIES, 0), hcu.makeCriteria().sizeNe(Screen.CHILDSCREENGROUPS, 0)).add();
					break;
				case 2:
					hcu.makeCriteria().and(hcu.makeCriteria().sizeEq(Screen.SCREENGROUPHIERARCHIES, 0), hcu.makeCriteria().sizeEq(Screen.CHILDSCREENGROUPS, 0)).add();
					break;
			}

			if (!isEmpty(screenGroupId))
				hcu.notIn(Screen.SCREENID, hsu.createCriteria(Screen.class).selectFields(Screen.SCREENID).joinTo(Screen.SCREENGROUPHIERARCHIES).joinTo(ScreenGroupHierarchy.PARENTSCREENGROUP).eq(ScreenGroup.SCREENGROUPID, screenGroupId).list());

			sct = BScreen.makeArray(hcu.list());
		}

		ret = new BScreenOrGroup[sct.length + st.length];
		System.arraycopy(st, 0, ret, 0, st.length);
		System.arraycopy(sct, 0, ret, st.length, sct.length);

		return ret;
	}

	public static BScreenOrGroup[] searchScreensAndGroupsForTasks(final String name, final String extId, final boolean includeScreens, final boolean includeScreenGroups, final boolean includeWizards, final int max) {
		BScreenOrGroup [] ret;
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		BScreenGroup [] st = new BScreenGroup[0];

		if (includeScreenGroups) {
			final HibernateCriteriaUtil<ScreenGroup> hcu = hsu.createCriteria(ScreenGroup.class).notNull(ScreenGroup.PARENT_SCREEN).distinct();
			hcu.orderBy(ScreenGroup.NAME);

			if (max > 0)
				hcu.setMaxResults(max);

			hcu.like(ScreenGroup.NAME, name);

			if (!isEmpty(extId))
				hcu.eq(ScreenGroup.SCREENGROUPID, IDGenerator.expandKey(extId));

			List<ScreenGroup> sgList;

			sgList = hcu.list();

			if (!sgList.isEmpty()) {

				st = new BScreenGroup[sgList.size()];

				for (int loop = 0; loop < st.length; loop++)
					st[loop] = new BScreenGroup(sgList.get(loop));
			}
		}

		BScreen [] sct = new BScreen[0];
		if (includeScreens) {
			HibernateCriteriaUtil<Screen> hcu = hsu.createCriteria(Screen.class).distinct();
			hcu.orderBy(Screen.NAME);
			hcu.ne(Screen.SCREENTYPE, (short) 5);

			//hcu.ne(Screen.SCREENTYPE, (short)2); allow parent screens

			if (max > 0)
				hcu.setMaxResults(max - st.length);

			if (!isEmpty(name))
				hcu.like(Screen.NAME, name);

			if (!isEmpty(extId))
				hcu.eq(Screen.SCREENID, IDGenerator.expandKey(extId));

			sct = BScreen.makeArray(hcu.list());

		}

		BScreenGroup [] wiz = new BScreenGroup[0];

		if (includeWizards) {
			List<Character> wizardTypes = new ArrayList<Character>();
			wizardTypes.add('O');
			wizardTypes.add('E');
			final HibernateCriteriaUtil<ScreenGroup> hcu = hsu.createCriteria(ScreenGroup.class).in(ScreenGroup.WIZARD_TYPE, wizardTypes).distinct();
			hcu.orderBy(ScreenGroup.NAME);

			if (max > 0)
				hcu.setMaxResults(max);

			hcu.like(ScreenGroup.NAME, name);

			if (!isEmpty(extId))
				hcu.eq(ScreenGroup.SCREENGROUPID, IDGenerator.expandKey(extId));

			List<ScreenGroup> sgList;

			sgList = hcu.list();

			if (!sgList.isEmpty()) {

				wiz = new BScreenGroup[sgList.size()];

				for (int loop = 0; loop < wiz.length; loop++)
					wiz[loop] = new BScreenGroup(sgList.get(loop));
			}
		}

		ret = new BScreenOrGroup[sct.length + st.length + wiz.length];
		System.arraycopy(st, 0, ret, 0, st.length);
		System.arraycopy(sct, 0, ret, st.length, sct.length);

		for (int loop = 0; loop < wiz.length; loop++)
			ret[loop + st.length + sct.length] = wiz[loop];

		return ret;
	}

	public static BScreenOrGroup[] searchScreenGroups(final HibernateSessionUtil hsu, final String name, final String extId, final int assocInd, final String screenGroupId, final int type, final int max) {
		return searchScreenGroups(hsu, name, extId, assocInd, screenGroupId, type, max, true);
	}

	private static BScreenOrGroup[] searchScreenGroups(final HibernateSessionUtil hsu, final String name, final String extId, final int assocInd, final String screenGroupId, final int type, final int max, boolean includeChildScreens) {
		return searchScreenGroups(hsu, name, extId, assocInd, screenGroupId, type, max, includeChildScreens, ArahantSession.getHSU().getCurrentCompany().getOrgGroupId());
	}

	public static BScreenOrGroup[] searchScreenGroups(final HibernateSessionUtil hsu, final String name, final String extId, final int assocInd, final String screenGroupId, final int type, final int max, boolean includeChildScreens, String companyId) {
		BScreenOrGroup [] ret;

		BScreenGroup [] st = new BScreenGroup[0];

		if (type == 0 || type == 2) {
			final HibernateCriteriaUtil<ScreenGroup> hcu = hsu.createCriteria(ScreenGroup.class).distinct();
			hcu.orderBy(ScreenGroup.NAME);

			if (max > 0)
				hcu.setMaxResults(max);


			hcu.like(ScreenGroup.NAME, name);

			if (!isEmpty(extId))
				hcu.eq(ScreenGroup.SCREENGROUPID, IDGenerator.expandKey(extId));

			switch (assocInd) {
				case 0:
					break;
				case 1:
					hcu.sizeNe(ScreenGroup.SCREENGROUPHIERARCHIESFORCHILDSCREENGROUP, 0);
					break;
				case 2:
					hcu.sizeEq(ScreenGroup.SCREENGROUPHIERARCHIESFORCHILDSCREENGROUP, 0);
					break;
			}

			if (!(hsu.currentlyArahantUser() || (!ArahantSession.multipleCompanySupport && hsu.currentlySuperUser(companyId))))
				hcu.joinTo(ScreenGroup.SECURITY_GROUPS).eq(SecurityGroup.SECURITYGROUPID, BPerson.getCurrent().getSecurityGroupId(companyId));

			List<ScreenGroup> sgList;
			if (!isEmpty(screenGroupId)) {
				final ScreenGroup sg = hsu.get(ScreenGroup.class, screenGroupId);
				sgList = hcu.list();
				sgList = filterGroupsThatAreChildren(sgList, sg);
				sgList = filterGroupsThatAreParents(sgList, sg);


				sgList.remove(sg);

			} else
				sgList = hcu.list();

			if (!sgList.isEmpty()) {

				st = new BScreenGroup[sgList.size()];

				for (int loop = 0; loop < st.length; loop++)
					st[loop] = new BScreenGroup(sgList.get(loop));
			}
		}

		BScreen [] sct = new BScreen[0];
		if (type == 0 || type == 1) {
			HibernateCriteriaUtil<Screen> hcu = hsu.createCriteria(Screen.class).distinct();
			hcu.orderBy(Screen.NAME);
			hcu.ne(Screen.SCREENTYPE, (short) 5);


			if (!includeChildScreens && (isEmpty(screenGroupId) || isEmpty(new BScreenGroup(screenGroupId).getParentScreenId())))
				hcu = isNotChildCriteria(hcu);

			hcu.ne(Screen.SCREENTYPE, (short) 2);


			if (max > 0)
				hcu.setMaxResults(max - st.length);

			if (!isEmpty(name))
				hcu.like(Screen.NAME, name);

			if (!isEmpty(extId))
				hcu.eq(Screen.SCREENID, IDGenerator.expandKey(extId));

			switch (assocInd) {
				case 0:
					break;
				case 1:
					hcu.makeCriteria().or(hcu.makeCriteria().sizeNe(Screen.SCREENGROUPHIERARCHIES, 0), hcu.makeCriteria().sizeNe(Screen.CHILDSCREENGROUPS, 0)).add();
					break;
				case 2:
					hcu.makeCriteria().and(hcu.makeCriteria().sizeEq(Screen.SCREENGROUPHIERARCHIES, 0), hcu.makeCriteria().sizeEq(Screen.CHILDSCREENGROUPS, 0)).add();
					break;
			}

			if (!isEmpty(screenGroupId))
				hcu.notIn(Screen.SCREENID, hsu.createCriteria(Screen.class).selectFields(Screen.SCREENID).joinTo(Screen.SCREENGROUPHIERARCHIES).joinTo(ScreenGroupHierarchy.PARENTSCREENGROUP).eq(ScreenGroup.SCREENGROUPID, screenGroupId).list());

			sct = BScreen.makeArray(hcu.list());
		}

		ret = new BScreenOrGroup[sct.length + st.length];
		System.arraycopy(st, 0, ret, 0, st.length);
		System.arraycopy(sct, 0, ret, st.length, sct.length);

		return ret;
	}

	public static BScreenGroup[] searchScreenGroups(final HibernateSessionUtil hsu, final String name, final String extId, final int assocInd, final String screenGroupId, final int max, final String[] excludeIds) {
		BScreenGroup [] ret;

		BScreenGroup [] st = new BScreenGroup[0];

		final HibernateCriteriaUtil<ScreenGroup> hcu = hsu.createCriteria(ScreenGroup.class).distinct();
		hcu.orderBy(ScreenGroup.NAME);

		if (max > 0)
			hcu.setMaxResults(max);

		hcu.like(ScreenGroup.NAME, name);

		hcu.notIn(ScreenGroup.SCREENGROUPID, excludeIds);

		if (!isEmpty(extId))
			hcu.eq(ScreenGroup.SCREENGROUPID, IDGenerator.expandKey(extId));

		switch (assocInd) {
			case 0:
				break;
			case 1:
				hcu.sizeNe(ScreenGroup.SCREENGROUPHIERARCHIESFORCHILDSCREENGROUP, 0);
				break;
			case 2:
				hcu.sizeEq(ScreenGroup.SCREENGROUPHIERARCHIESFORCHILDSCREENGROUP, 0);
				break;
		}

		List<ScreenGroup> sgList;
		if (!isEmpty(screenGroupId)) {
			final ScreenGroup sg = hsu.get(ScreenGroup.class, screenGroupId);
			sgList = hcu.list();
			sgList = filterGroupsThatAreChildren(sgList, sg);
			sgList = filterGroupsThatAreParents(sgList, sg);


			sgList.remove(sg);

		} else
			sgList = hcu.list();

		if (!sgList.isEmpty()) {

			st = new BScreenGroup[sgList.size()];

			for (int loop = 0; loop < st.length; loop++)
				st[loop] = new BScreenGroup(sgList.get(loop));
		}

		ret = new BScreenGroup[st.length];
		System.arraycopy(st, 0, ret, 0, st.length);

		return ret;
	}

	/**
	 * @return @see com.arahant.beans.ScreenGroup#getExtId()
	 */
	public String getExtId() {
		//return screenGroup.getExtId();
		return IDGenerator.shrinkKey(screenGroup.getScreenGroupId());
	}

	/**
	 * @return @see com.arahant.beans.ScreenGroup#getName()
	 */
	@Override
	public String getName() {
		return screenGroup.getName();
	}

	/**
	 * @return @see com.arahant.beans.ScreenGroup#getDescription()
	 */
	@Override
	public String getDescription() {
		return screenGroup.getDescription();
	}

	/**
	 * @param description
	 * @see com.arahant.beans.ScreenGroup#getDescription()
	 */
	public void setDescription(final String description) {
		screenGroup.setDescription(description);
	}

	@Override
	public String getId() {
		return screenGroup.getScreenGroupId();
	}

	public void setId(final String screenGroupId) {
		screenGroup.setScreenGroupId(screenGroupId);
	}

	/**
	 * @return @see com.arahant.beans.ScreenGroup#getScreenGroupId()
	 */
	public String getScreenGroupId() {
		return screenGroup.getScreenGroupId();
	}

	/**
	 * @param name
	 * @see com.arahant.beans.ScreenGroup#setName(java.lang.String)
	 */
	public void setName(final String name) {
		screenGroup.setName(name);
	}

	/**
	 * @param screenGroupId
	 * @see com.arahant.beans.ScreenGroup#setScreenGroupId(java.lang.String)
	 */
	public void setScreenGroupId(final String screenGroupId) {
		screenGroup.setScreenGroupId(screenGroupId);
	}

	public char getTechnology() {
		return screenGroup.getTechnology();
	}

	public void setTechnology(char tech) {
		screenGroup.setTechnology(tech);
	}

	public BScreenOrGroup[] listChildren() {
		List<ScreenGroupHierarchy> spa;

		//removed the order by's because I'll turn right back around and sort anyway

		final HibernateCriteriaUtil<ScreenGroupHierarchy> hcu = ArahantSession.getHSU().createCriteria(ScreenGroupHierarchy.class).eq(ScreenGroupHierarchy.PARENTSCREENGROUP, screenGroup).notNull(ScreenGroupHierarchy.CHILDSCREENGROUP);
		//.orderBy(ScreenGroupHierarchy.SEQNO);

		spa = hcu.list();

		spa.addAll(ArahantSession.getHSU().createCriteria(ScreenGroupHierarchy.class).eq(ScreenGroupHierarchy.PARENTSCREENGROUP, screenGroup).isNull(ScreenGroupHierarchy.CHILDSCREENGROUP) //.orderBy(ScreenGroupHierarchy.SEQNO)
				.joinTo(ScreenGroupHierarchy.SCREEN).ne(Screen.SCREENTYPE, (short) 5).list());

		Collections.sort(spa);

		final BScreenOrGroup[] ret = new BScreenOrGroup[spa.size()];

		for (int loop = 0; loop < ret.length; loop++) {

			final ScreenGroupHierarchy sgh = spa.get(loop);

			if (sgh.getScreen() != null)
				ret[loop] = new BScreen(sgh.getScreen());

			if (sgh.getChildScreenGroup() != null)
				ret[loop] = new BScreenGroup(sgh.getChildScreenGroup());
		}
		return ret;
	}

	private static HibernateCriteriaUtil<Screen> isNotChildCriteria(final HibernateCriteriaUtil<Screen> hcu) {
		hcu.ne(Screen.SCREENTYPE, (short) 3);
		return hcu;
	}

	public static BScreenOrGroup[] listWithoutChildren(final HibernateSessionUtil hsu) {
		final List<ScreenGroup> sgl = hsu.createCriteria(ScreenGroup.class).sizeEq(ScreenGroup.SCREENGROUPHIERARCHIESFORCHILDSCREENGROUP, 0).orderBy(ScreenGroup.NAME).list();
		/*
		 * HibernateCriteriaUtil<Screen> hcu=hsu.createCriteria(Screen.class)
		 * .ne(Screen.SCREENTYPE, (short)5) .orderBy(Screen.NAME);
		 *
		 * hcu.makeCriteria().and(hcu.makeCriteria().sizeEq(Screen.SCREENGROUPHIERARCHIES,0),
		 * hcu.makeCriteria().sizeEq(Screen.CHILDSCREENGROUPS,0)).add();
		 *
		 * hcu=isNotChildCriteria(hcu);;
		 *
		 * final List<Screen> sl= hcu.list();
		 *
		 * final BScreenOrGroup [] ret= new
		 * BScreenOrGroup[sgl.size()+sl.size()];
		 *
		 * for (int loop=0;loop<sgl.size();loop++) ret[loop]=new
		 * BScreenGroup(sgl.get(loop));
		 *
		 * for (int loop=0;loop<sl.size();loop++) ret[loop+sgl.size()]=new
		 * BScreen(sl.get(loop));
		 */

		final BScreenOrGroup[] ret = new BScreenOrGroup[sgl.size()];

		for (int loop = 0; loop < sgl.size(); loop++)
			ret[loop] = new BScreenGroup(sgl.get(loop));

		return ret;
	}

	public static BScreenOrGroup[] list(final HibernateSessionUtil hsu) {
		final List<ScreenGroup> sgl = hsu.createCriteria(ScreenGroup.class).sizeEq(ScreenGroup.SCREENGROUPHIERARCHIESFORCHILDSCREENGROUP, 0).orderBy(ScreenGroup.NAME).list();

		final List<Screen> sl = hsu.createCriteria(Screen.class).orderBy(Screen.NAME).sizeEq(Screen.SCREENGROUPHIERARCHIES, 0).list();

		final BScreenOrGroup[] ret = new BScreenOrGroup[sgl.size() + sl.size()];

		for (int loop = 0; loop < sgl.size(); loop++)
			ret[loop] = new BScreenGroup(sgl.get(loop));

		for (int loop = 0; loop < sl.size(); loop++)
			ret[loop + sgl.size()] = new BScreen(sl.get(loop));

		return ret;
	}

	public static BScreenGroup[] listTopLevel() {
		return makeArray(ArahantSession.getHSU().createCriteria(ScreenGroup.class).orderBy(ScreenGroup.NAME).sizeEq(ScreenGroup.SCREENGROUPHIERARCHIESFORCHILDSCREENGROUP, 0).list());
	}

	public static BScreenGroup[] makeArray(List<ScreenGroup> l) {
		BScreenGroup[] ret = new BScreenGroup[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BScreenGroup(l.get(loop));
		return ret;
	}

	/**
	 * @return Returns the seqNo.
	 */
	public int getSeqNo() {
		return seqNo;
	}

	/**
	 * @param seqNo The seqNo to set.
	 */
	public void setSeqNo(final int seqNo) {
		this.seqNo = seqNo;
	}
	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#create()
	 */

	@Override
	public String create() throws ArahantException {
		screenGroup = new ScreenGroup();
		screenGroup.generateId();

		return getScreenGroupId();
	}
	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#delete()
	 */
	@Override
	public void delete() throws ArahantDeleteException {
		try {
			HibernateSessionUtil hsu = ArahantSession.getHSU();
            for (ScreenGroupHierarchy sgh : screenGroup.getScreenGroupHierarchiesForChildScreenGroup()) {
                //				final short x = sgh.getSeqNo();
                final ScreenGroup parent = sgh.getParentScreenGroup();
                hsu.delete(screenGroup.getScreenGroupHierarchiesForChildScreenGroup());
                new BScreenGroup(parent).renumberSequence();
            }
			hsu.delete(screenGroup.getScreenGroupHierarchiesForParentScreenGroup());
			hsu.delete(screenGroup);
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
	}
	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#insert()
	 */

	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(screenGroup);
	}
	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#update()
	 */

	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(screenGroup);
	}

	/**
	 * @throws ArahantDeleteException
	 */
	public static void delete(final HibernateSessionUtil hsu, final String[] groupIds) throws ArahantDeleteException {
		for (final String element : groupIds)
			new BScreenGroup(element).delete();
	}

	/**
	 * @throws ArahantException
	 *
	 */
	public void moveScreenUp(final String screenId) throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		ScreenGroupHierarchy screen = hsu.createCriteria(ScreenGroupHierarchy.class).eq(ScreenGroupHierarchy.PARENTSCREENGROUP, screenGroup).joinTo(ScreenGroupHierarchy.SCREEN).eq(Screen.SCREENID, screenId).first();

		if (screen == null)
			throw new ArahantException("Screen not in group");

		short seq = screen.getSeqNo();

		ScreenGroupHierarchy sgh = hsu.createCriteria(ScreenGroupHierarchy.class).eq(ScreenGroupHierarchy.PARENTSCREENGROUP, screenGroup).lt(ScreenGroupHierarchy.SEQNO, seq).orderByDesc(ScreenGroupHierarchy.SEQNO).first();

		if (sgh == null) {
			screen.setSeqNo((short) 1000);
			hsu.flush();
			hsu.clear();
			renumberSequence();
		} else
			swapHierarchy(sgh, screen);
	}

	private void swapHierarchy(final ScreenGroupHierarchy sgh1, final ScreenGroupHierarchy sgh2) {
		final short x = sgh1.getSeqNo();
		final short x2 = sgh2.getSeqNo();
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		sgh1.setSeqNo((short) -100);
		hsu.flush();
		sgh2.setSeqNo(x);
		hsu.flush();
		sgh1.setSeqNo(x2);
		hsu.flush();
	}

	/**
	 * @param screenId
	 * @throws ArahantException
	 */
	public void moveScreenDown(final String screenId) throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		ScreenGroupHierarchy screen = hsu.createCriteria(ScreenGroupHierarchy.class).eq(ScreenGroupHierarchy.PARENTSCREENGROUP, screenGroup).joinTo(ScreenGroupHierarchy.SCREEN).eq(Screen.SCREENID, screenId).first();
		if (screen == null)
			throw new ArahantException("Screen not in group");

		short seq = screen.getSeqNo();

		ScreenGroupHierarchy sgh = hsu.createCriteria(ScreenGroupHierarchy.class).eq(ScreenGroupHierarchy.PARENTSCREENGROUP, screenGroup).gt(ScreenGroupHierarchy.SEQNO, seq).orderBy(ScreenGroupHierarchy.SEQNO).first();

		if (sgh == null) {
			screen.setSeqNo((short) -1);
			hsu.flush();
			hsu.clear();
			renumberSequence();
		} else
			swapHierarchy(sgh, screen);
	}

	/**
	 * @param childScreenGroupId
	 * @throws ArahantException
	 */
	public void moveScreenGroupUp(final String childScreenGroupId) throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		ScreenGroupHierarchy screenGH = hsu.createCriteria(ScreenGroupHierarchy.class).eq(ScreenGroupHierarchy.PARENTSCREENGROUP, screenGroup).joinTo(ScreenGroupHierarchy.CHILDSCREENGROUP).eq(ScreenGroup.SCREENGROUPID, childScreenGroupId).first();

		if (screenGH == null)
			throw new ArahantException("Screen Group not in group!");

		short seq = screenGH.getSeqNo();

		ScreenGroupHierarchy sgh = hsu.createCriteria(ScreenGroupHierarchy.class).eq(ScreenGroupHierarchy.PARENTSCREENGROUP, screenGroup).lt(ScreenGroupHierarchy.SEQNO, seq).orderByDesc(ScreenGroupHierarchy.SEQNO).first();

		if (sgh == null) {
			screenGH.setSeqNo((short) 1000);
			hsu.flush();
			hsu.clear();
			renumberSequence();
		} else
			swapHierarchy(sgh, screenGH);
	}

	/**
	 * @param childScreenGroupId
	 * @throws ArahantException
	 */
	public void moveScreenGroupDown(final String childScreenGroupId) throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		ScreenGroupHierarchy screenGH = hsu.createCriteria(ScreenGroupHierarchy.class).eq(ScreenGroupHierarchy.PARENTSCREENGROUP, screenGroup).joinTo(ScreenGroupHierarchy.CHILDSCREENGROUP).eq(ScreenGroup.SCREENGROUPID, childScreenGroupId).first();

		if (screenGH == null)
			throw new ArahantException("Screen Group not in group!");

		short seq = screenGH.getSeqNo();

		final ScreenGroupHierarchy sgh = hsu.createCriteria(ScreenGroupHierarchy.class).eq(ScreenGroupHierarchy.PARENTSCREENGROUP, screenGroup).gt(ScreenGroupHierarchy.SEQNO, seq).orderBy(ScreenGroupHierarchy.SEQNO).first();

		if (sgh == null) {
			screenGH.setSeqNo((short) -1);
			hsu.flush();
			hsu.clear();
			renumberSequence();
		} else
			swapHierarchy(sgh, screenGH);
	}

	/**
	 * @param screenGroupIds
	 * @param screenIds
	 * @throws ArahantDeleteException
	 */
	public void removeScreensAndGroups(final String[] screenGroupIds, final String[] screenIds) throws ArahantDeleteException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		removeScreenFromGroup(screenIds, screenGroup.getScreenGroupId(), hsu);
		for (final String element : screenGroupIds) {
			final HibernateCriteriaUtil<ScreenGroupHierarchy> hcu = hsu.createCriteria(ScreenGroupHierarchy.class);
			hcu.joinTo(ScreenGroupHierarchy.CHILDSCREENGROUP).eq(ScreenGroup.SCREENGROUPID, element);
			hcu.joinTo(ScreenGroupHierarchy.PARENTSCREENGROUP).eq(ScreenGroup.SCREENGROUPID, screenGroup.getScreenGroupId());

			final List<ScreenGroupHierarchy> l = hcu.list();
            for (ScreenGroupHierarchy sgh : l) {
                hsu.delete(sgh);
            }
		}
	}

	/**
	 * @param screen_id
	 * @param screen_group_id
	 * @param hsu
	 * @throws ArahantDeleteException
	 */
	protected void removeScreenFromGroup(final String[] screen_id, final String screen_group_id, final HibernateSessionUtil hsu) throws ArahantDeleteException {
		for (final String element : screen_id) {
			final HibernateCriteriaUtil<ScreenGroupHierarchy> hcu = hsu.createCriteria(ScreenGroupHierarchy.class);
			hcu.joinTo(ScreenGroupHierarchy.SCREEN).eq(Screen.SCREENID, element);
			hcu.joinTo(ScreenGroupHierarchy.PARENTSCREENGROUP).eq(ScreenGroup.SCREENGROUPID, screen_group_id);

			final ScreenGroupHierarchy screenAssoc = hcu.first();

			if (screenAssoc == null)
				continue;

			hsu.delete(screenAssoc);
		}
	}

	/**
	 * Renumbers all of the children to the current screenGroup
	 * starting from 0
	 */
	private void renumberSequence() {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		short seq = -100;
		List<ScreenGroupHierarchy> sgs = hsu.createCriteria(ScreenGroupHierarchy.class).eq(ScreenGroupHierarchy.PARENTSCREENGROUP, screenGroup).orderBy(ScreenGroupHierarchy.SEQNO).list();
		for (ScreenGroupHierarchy sg : sgs)
			sg.setSeqNo(seq++);
		hsu.flush();
		seq = 0;
		sgs = hsu.createCriteria(ScreenGroupHierarchy.class).eq(ScreenGroupHierarchy.PARENTSCREENGROUP, screenGroup).orderBy(ScreenGroupHierarchy.SEQNO).list();
		for (ScreenGroupHierarchy sg : sgs)
			sg.setSeqNo(seq++);
	}


	/**
	 * @param groupIds
	 * @param screenIds
	 * @return
	 * @throws ArahantException
	 */
	public void add(final String[] groupIds, final String[] screenIds) throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		short max = -1;
		Set<ScreenGroupHierarchy> sghs = screenGroup.getScreenGroupHierarchiesForParentScreenGroup();
		for (ScreenGroupHierarchy sgh1 : sghs)
			if (sgh1.getSeqNo() > max)
				max = sgh1.getSeqNo();
		max++;

		for (final String element : groupIds) {
			final ScreenGroupHierarchy sgh = new ScreenGroupHierarchy();
			sgh.generateId();
			sgh.setParentScreenGroup(screenGroup);
			sgh.setChildScreenGroup(hsu.get(ScreenGroup.class, element));
			sgh.setSeqNo(max++);
			sgh.setDefaultScreen('N');
			hsu.saveOrUpdate(sgh);
		}
		//If it gets a screen id that is a parent screen, 
		//it has to do the nasty work of auto-creating a group, 
		//putting that screen as the parent screen, naming it the same
		//name as the parent screen name (not file name, but title), 
		//and of course associating it.
		for (final String element : screenIds) {
			Screen s = hsu.get(Screen.class, element);
			if (s.getScreenType() == Screen.PARENT_TYPE) {
				BScreenGroup bsg = new BScreenGroup();
				bsg.create();
				bsg.setName(s.getName());
				bsg.setParentScreenId(element);
                bsg.setTechnology(getCurrentTechnology());
				bsg.insert();

				final ScreenGroupHierarchy sgh = new ScreenGroupHierarchy();
				sgh.generateId();
				sgh.setParentScreenGroup(screenGroup);
				sgh.setChildScreenGroup(bsg.screenGroup);
				sgh.setSeqNo(max++);
				sgh.setDefaultScreen('N');
				hsu.saveOrUpdate(sgh);
			} else {
				final ScreenGroupHierarchy sa = new ScreenGroupHierarchy();
				sa.generateId();
				sa.setParentScreenGroup(screenGroup);
				sa.setScreen(s);
				sa.setSeqNo(max++);
				sa.setDefaultScreen('N');
				hsu.saveOrUpdate(sa);
			}
		}
	}

	/**
	 * @param hsu
	 * @param name
	 * @param extId
	 * @param associatedIndicator
	 * @param screenGroupId
	 * @param typeIndicator
	 * @param cap
	 * @return
	 */
	public static BScreenOrGroup[] searchScreenGroupsNoChildren(final HibernateSessionUtil hsu, final String name, final String extId, final int associatedIndicator, final String screenGroupId, final int typeIndicator, final int cap) {
		return searchScreenGroups(hsu, name, extId, associatedIndicator, screenGroupId, typeIndicator, cap, false);
	}

	/**
	 * @return
	 */
	public String getParentScreenName() {
		if (screenGroup.getParentScreen() == null)
			return "";
		return screenGroup.getParentScreen().getName();
	}

	/**
	 * @return
	 */
	public String getParentScreenId() {
		if (screenGroup.getParentScreen() == null)
			return "";
		return screenGroup.getParentScreen().getScreenId();
	}

	/**
	 * @param parentScreenId
	 */
	public void setParentScreenId(final String parentScreenId) {
		screenGroup.setParentScreen(ArahantSession.getHSU().get(Screen.class, parentScreenId));
	}

	/**
	 * Sets a default screen flag for either a parent screen group or a screen
	 *
	 * @param parentScreenScreenGroupId
	 * @param screenId
	 * @param defaultState
	 */
	public void setScreenDefault(final String parentScreenScreenGroupId, final String screenId, final boolean defaultState) throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		ScreenGroupHierarchy sgh;

		// sanity check expected args, should only pass one of these
		if (!(isEmpty(parentScreenScreenGroupId) && !isEmpty(screenId))
				&& !(!isEmpty(parentScreenScreenGroupId) && isEmpty(screenId)))
			throw new ArahantException("Invalid arguments");

		// if we are giving the specified "parent screen" screen group or 
		// screen the default screen flag, clear existing
		if (defaultState) {
			sgh = hsu.createCriteria(ScreenGroupHierarchy.class).eq(ScreenGroupHierarchy.PARENTSCREENGROUP, screenGroup).eq(ScreenGroupHierarchy.DEFAULTSCREEN, 'Y').first();
			if (sgh != null) {
				sgh.setDefaultScreen('N');
				hsu.saveOrUpdate(sgh);
			}
		}
		hsu.flush(); //tracking down a constraint violation

		// now set the default flag for the specified "parent screen" screen group or screen

		if (isEmpty(parentScreenScreenGroupId)) {
			// get the sgh from specified screen
			sgh = hsu.createCriteria(ScreenGroupHierarchy.class).eq(ScreenGroupHierarchy.PARENTSCREENGROUP, screenGroup).joinTo(ScreenGroupHierarchy.SCREEN).eq(Screen.SCREENID, screenId).first();
			if (sgh != null) {
				sgh.setDefaultScreen(defaultState ? 'Y' : 'N');
				hsu.saveOrUpdate(sgh);
				hsu.flush(); //tracking down a constraint violation
			}
		} else {
			// get the sgh from specified "parent screen" screen group
//			sgh = hsu.createCriteria(ScreenGroupHierarchy.class).eq(ScreenGroupHierarchy.PARENTSCREENGROUP, screenGroup).eq(ScreenGroupHierarchy.CHILDSCREENGROUP, parentScreenScreenGroupId).first();

			Connection db = KissConnection.get();
			try {
				Record rec = db.fetchOne("select * from screen_group_hierarchy " +
						"where parent_screen_group_id = ? " +
						"and child_screen_group_id = ?", screenGroup.getScreenGroupId(), parentScreenScreenGroupId);
				rec.set("default_screen", defaultState ? 'Y' : 'N');
				rec.update();
			} catch (Exception e) {
				throw new ArahantException(e);
			}
		}


		/*
		 * if (defaultState) { final ScreenGroupHierarchy
		 * sgh=hsu.createCriteria(ScreenGroupHierarchy.class)
		 * .eq(ScreenGroupHierarchy.SCREENGROUPBYPARENTSCREENGROUPID,
		 * screenGroup) .eq(ScreenGroupHierarchy.DEFAULTSCREEN,'Y')
		 * .joinTo(ScreenGroupHierarchy.SCREEN) .ne(Screen.SCREENID, screenId)
		 * .first(); if (sgh!=null) { sgh.setDefaultScreen('N');
		 * hsu.saveOrUpdate(sgh); } }
		 *
		 * // set new default state final ScreenGroupHierarchy
		 * sgh=hsu.createCriteria(ScreenGroupHierarchy.class)
		 * .eq(ScreenGroupHierarchy.SCREENGROUPBYPARENTSCREENGROUPID,
		 * screenGroup) .joinTo(ScreenGroupHierarchy.SCREEN)
		 * .eq(Screen.SCREENID, screenId) .first(); if (sgh!=null) {
		 * sgh.setDefaultScreen(defaultState?'Y':'N'); hsu.saveOrUpdate(sgh); }
		 */
	}

	public void setScreenHierarchyData(final String parentScreenScreenGroupId, final String screenId, final boolean defaultState, String buttonName) throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		ScreenGroupHierarchy sgh;

		// sanity check expected args, should only pass one of these
		if (!(isEmpty(parentScreenScreenGroupId) && !isEmpty(screenId))
				&& !(!isEmpty(parentScreenScreenGroupId) && isEmpty(screenId)))
			throw new ArahantException("Invalid arguments");

		// if we are giving the specified "parent screen" screen group or 
		// screen the default screen flag, clear existing
		if (defaultState) {
			sgh = hsu.createCriteria(ScreenGroupHierarchy.class).eq(ScreenGroupHierarchy.PARENTSCREENGROUP, screenGroup).eq(ScreenGroupHierarchy.DEFAULTSCREEN, 'Y').first();
			if (sgh != null) {
				sgh.setDefaultScreen('N');
				hsu.saveOrUpdate(sgh);
				hsu.flush(); //tracking down a constraint violation
			}
		}

		// now set the default flag for the specified "parent screen" screen group or screen

		if (isEmpty(parentScreenScreenGroupId))
			// get the sgh from specified screen
			sgh = hsu.createCriteria(ScreenGroupHierarchy.class).eq(ScreenGroupHierarchy.PARENTSCREENGROUP, screenGroup).joinTo(ScreenGroupHierarchy.SCREEN).eq(Screen.SCREENID, screenId).first();
		else
			// get the sgh from specified "parent screen" screen group
			sgh = hsu.createCriteria(ScreenGroupHierarchy.class).eq(ScreenGroupHierarchy.PARENTSCREENGROUP, screenGroup).joinTo(ScreenGroupHierarchy.CHILDSCREENGROUP).eq(ScreenGroup.SCREENGROUPID, parentScreenScreenGroupId).first();

		if (sgh != null) {
			//	System.out.println("Loaded "+sgh.getScreenGroupHierarchyId()+" with seq number of "+sgh.getSeqNo());
			sgh.setDefaultScreen(defaultState ? 'Y' : 'N');
			sgh.setButtonName(buttonName);
			hsu.saveOrUpdate(sgh);
			hsu.flush(); //tracking down a constraint violation
		}



		/*
		 * if (defaultState) { final ScreenGroupHierarchy
		 * sgh=hsu.createCriteria(ScreenGroupHierarchy.class)
		 * .eq(ScreenGroupHierarchy.SCREENGROUPBYPARENTSCREENGROUPID,
		 * screenGroup) .eq(ScreenGroupHierarchy.DEFAULTSCREEN,'Y')
		 * .joinTo(ScreenGroupHierarchy.SCREEN) .ne(Screen.SCREENID, screenId)
		 * .first(); if (sgh!=null) { sgh.setDefaultScreen('N');
		 * hsu.saveOrUpdate(sgh); } }
		 *
		 * // set new default state final ScreenGroupHierarchy
		 * sgh=hsu.createCriteria(ScreenGroupHierarchy.class)
		 * .eq(ScreenGroupHierarchy.SCREENGROUPBYPARENTSCREENGROUPID,
		 * screenGroup) .joinTo(ScreenGroupHierarchy.SCREEN)
		 * .eq(Screen.SCREENID, screenId) .first(); if (sgh!=null) {
		 * sgh.setDefaultScreen(defaultState?'Y':'N'); hsu.saveOrUpdate(sgh); }
		 */
	}

	public boolean hasParentScreen() {
		return screenGroup.getParentScreen() != null;
	}

	public String getParentScreenFile() {
		return screenGroup.getParentScreen().getFilename();
	}

	/**
	 * @param screenGroupId
	 * @return
	 */
	public boolean getIsDefaultScreenFor(final String screenGroupId) {
		if (isEmpty(screenGroupId))
			return false;
		return ArahantSession.getHSU().createCriteria(ScreenGroupHierarchy.class).eq(ScreenGroupHierarchy.CHILDSCREENGROUP, screenGroup).eq(ScreenGroupHierarchy.DEFAULTSCREEN, 'Y').joinTo(ScreenGroupHierarchy.PARENTSCREENGROUP).eq(ScreenGroup.SCREENGROUPID, screenGroupId).exists();
	}

	/**
	 * @param screenId
	 * @param buttonName
	 */
	public void setScreenButtonName(String screenId, String buttonName) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		ScreenGroupHierarchy sgh = hsu.createCriteria(ScreenGroupHierarchy.class).eq(ScreenGroupHierarchy.PARENTSCREENGROUP, screenGroup).joinTo(ScreenGroupHierarchy.SCREEN).eq(Screen.SCREENID, screenId).first();

		sgh.setButtonName(buttonName);
		hsu.saveOrUpdate(sgh);
		hsu.flush(); //tracking down a constraint violation
	}

	/**
	 * @param screenGroupId
	 * @param buttonName
	 */
	public void setScreenGroupButtonName(String screenGroupId, String buttonName) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		ScreenGroupHierarchy sgh = hsu.createCriteria(ScreenGroupHierarchy.class).eq(ScreenGroupHierarchy.PARENTSCREENGROUP, screenGroup).joinTo(ScreenGroupHierarchy.CHILDSCREENGROUP).eq(ScreenGroup.SCREENGROUPID, screenGroupId).first();

		if (sgh != null) {
			sgh.setButtonName(buttonName);
			hsu.saveOrUpdate(sgh);
		}
	}

	/**
	 * @param screenGroupId
	 * @return
	 */
	public String getScreenGroupButtonName(String screenGroupId) {
		ScreenGroupHierarchy sgh =
				ArahantSession.getHSU().createCriteria(ScreenGroupHierarchy.class).eq(ScreenGroupHierarchy.PARENTSCREENGROUP, screenGroup).joinTo(ScreenGroupHierarchy.CHILDSCREENGROUP).eq(ScreenGroup.SCREENGROUPID, screenGroupId).first();

		if (sgh == null)
			throw new ArahantException("Screen group id " + screenGroupId + " does not have a valid name under " + screenGroup.getScreenGroupId());

		return sgh.getButtonName();
	}

	/**
	 * @param screenId
	 * @return
	 */
	public String getScreenButtonName(String screenId) {
		ScreenGroupHierarchy sgh =
				ArahantSession.getHSU().createCriteria(ScreenGroupHierarchy.class).eq(ScreenGroupHierarchy.PARENTSCREENGROUP, screenGroup).joinTo(ScreenGroupHierarchy.SCREEN).eq(Screen.SCREENID, screenId).first();

		return sgh.getButtonName();
	}

	private void deepCopy(BScreenGroup bsg, Map<String, String> groupMap) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		//	logger.info("Copying children of "+bsg.getName());
		// copy the hierarchy with this screenGroup as parent
		Set<ScreenGroupHierarchy> hierarchies = screenGroup.getScreenGroupHierarchiesForParentScreenGroup();

		for (ScreenGroupHierarchy oldHierarchy : hierarchies)
			if (oldHierarchy.getScreen() != null) {
				ScreenGroupHierarchy newHierarchy = new ScreenGroupHierarchy();
				HibernateSessionUtil.copyCorresponding(newHierarchy, oldHierarchy);
				newHierarchy.generateId();
				newHierarchy.setParentScreenGroup(bsg.screenGroup);

				hsu.insert(newHierarchy);
			} else {

				BScreenGroup oldbsg = new BScreenGroup(oldHierarchy.getChildScreenGroup());
				BScreenGroup newbsg;

				boolean recurse = false;
				if (groupMap.containsKey(oldHierarchy.getChildScreenGroup().getScreenGroupId()))
					newbsg = new BScreenGroup(groupMap.get(oldHierarchy.getChildScreenGroup().getScreenGroupId()));
				else {
					newbsg = new BScreenGroup();

					// make a copy of the screen group
					newbsg.create();
					newbsg.setName(oldbsg.getName());
					newbsg.setParentScreenId(oldbsg.getParentScreenId());
					newbsg.setWizardType(oldbsg.getWizardType());
                    newbsg.setTechnology(getCurrentTechnology());
					newbsg.insert();
					//logger.info("Making new screen group "+newbsg.getName());
					groupMap.put(oldHierarchy.getChildScreenGroup().getScreenGroupId(), newbsg.getScreenGroupId());

					recurse = true;
				}
				ScreenGroupHierarchy newHierarchy = new ScreenGroupHierarchy();
				HibernateSessionUtil.copyCorresponding(newHierarchy, oldHierarchy);
				newHierarchy.generateId();
				newHierarchy.setParentScreenGroup(bsg.screenGroup);
				newHierarchy.setChildScreenGroup(newbsg.screenGroup);

				hsu.insert(newHierarchy);

				if (recurse)
					oldbsg.deepCopy(newbsg, groupMap);

			}
	}

    /**
     * This is just a placeholder for the upcoming, correct code.
     * @return 'F'
     */
	public static char getCurrentTechnology() {
        return 'F';
    }

	/**
	 * @param screenGroupName
	 * @param associate
	 * @param parentScreenGroupId
	 */
	public String copyScreenGroup(String screenGroupName, boolean associate, String parentScreenGroupId, boolean shallowCopy) throws ArahantException {
		Screen parentScreen = screenGroup.getParentScreen();
		BScreenGroup bsg = new BScreenGroup();

		// make a copy of the screen group
		bsg.create();
		bsg.setName(screenGroupName);
		bsg.setParentScreenId(parentScreen == null ? "" : parentScreen.getScreenId());
		bsg.setTechnology(getCurrentTechnology());
		bsg.insert();

		if (shallowCopy) {
			HibernateSessionUtil hsu = ArahantSession.getHSU();
			// copy the hierarchy with this screenGroup as parent
			Set<ScreenGroupHierarchy> hierarchies = screenGroup.getScreenGroupHierarchiesForParentScreenGroup();
			ScreenGroupHierarchy newHierarchy;
			for (ScreenGroupHierarchy oldHierarchy : hierarchies) {
				newHierarchy = new ScreenGroupHierarchy();
				HibernateSessionUtil.copyCorresponding(newHierarchy, oldHierarchy);
				newHierarchy.generateId();
				newHierarchy.setParentScreenGroup(bsg.screenGroup);

				hsu.insert(newHierarchy);
			}
		} else
			deepCopy(bsg, new HashMap<String, String>());

		// optionally associate this screen group into the same parent group, just below
		if (associate && !isEmpty(parentScreenGroupId)) {
			BScreenGroup parentSG = new BScreenGroup(parentScreenGroupId);
			String[] groupIds = new String[1];

			groupIds[0] = bsg.getScreenGroupId();

			// TODO: move the order of the group just added such that it is an insert right after source group
			parentSG.add(groupIds, new String[0]);
		}

		return bsg.getScreenGroupId();
	}

	public static String getReport(String startFromScreenGroupId, boolean showIds, boolean showLabels, boolean showSubHeaders) throws ArahantException {
		return new SiteMapReport().build(startFromScreenGroupId, showIds, showLabels, showSubHeaders);
	}

	private void deepDelete() {
		//If I still have a screen group that is my parent, I can't be deleted
		//so must set return message and go back
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (hsu.createCriteria(ScreenGroupHierarchy.class).eq(ScreenGroupHierarchy.CHILDSCREENGROUP, screenGroup).exists())
			//They don't care
			//ArahantSession.addReturnMessage("Could not delete "+screenGroup.getName()+" because it is also in another hierarchy.");
			return;

		for (ScreenGroupHierarchy sgh : screenGroup.getScreenGroupHierarchiesForParentScreenGroup()) {
			ScreenGroup sg = sgh.getChildScreenGroup();
			hsu.delete(sgh);
			if (sg != null)
				new BScreenGroup(sg).deepDelete();
		}

		delete();
	}

	public static void deepDelete(String screenGroupId) {
		Connection db = new Connection(ArahantSession.getHSU().getConnection());
		try {
			List<Record> sghRecs = db.fetchAll("select * from screen_group_hierarchy where parent_screen_group_id=?", screenGroupId);
			for (Record sghRec : sghRecs) {
				String childScreenGroupId = sghRec.getString("child_screen_group_id");
				if (childScreenGroupId != null  &&  !isEmpty(childScreenGroupId))
					deepDelete(childScreenGroupId);
				sghRec.delete();
			}
			sghRecs = db.fetchAll("select * from screen_group_hierarchy where child_screen_group_id=?", screenGroupId);
			for (Record sghRec : sghRecs)
				sghRec.delete();
			db.execute("delete from screen_group where screen_group_id=?", screenGroupId);
		} catch (Exception throwables) {
			throw new ArahantException(throwables);
		}
	}

	public static void deepDelete(String[] screenGroupIds) {
		for (String id : screenGroupIds)
			deepDelete(id);
		/*
		//remove any parent screen group hierarchies
		ArahantSession.getHSU().createCriteria(ScreenGroupHierarchy.class).joinTo(ScreenGroupHierarchy.CHILDSCREENGROUP).in(ScreenGroup.SCREENGROUPID, screenGroupIds).delete();

		for (String id : screenGroupIds)
			new BScreenGroup(id).deepDelete();
		 */
	}

	public BScreenGroup[] listAvailableScreenGroups(final String[] excludeIds) {
		HibernateCriteriaUtil<ScreenGroup> hcu = ArahantSession.getHSU().createCriteria(ScreenGroup.class);
		hcu.sizeEq(ScreenGroup.SCREENGROUPHIERARCHIESFORCHILDSCREENGROUP, 0);
		hcu.notIn(ScreenGroup.SCREENGROUPID, excludeIds);

		return makeArray(hcu.list());
	}

	public String getWizardType() {
		return screenGroup.getWizardType() + "";
	}

	public void setWizardType(String wizardType) {
		screenGroup.setWizardType(wizardType.charAt(0));
	}
}
