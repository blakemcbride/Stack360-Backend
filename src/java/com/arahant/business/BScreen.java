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
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.IDGenerator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BScreen extends BScreenOrGroup implements IDBFunctions {

	private static final transient ArahantLogger logger = new ArahantLogger(BScreen.class);
	private Screen screen;
	private int seqNo = 0;


	public static BScreenOrGroup getByFilename(String filename) {
		Screen scr = ArahantSession.getHSU().createCriteria(Screen.class)
				.eq(Screen.FILENAME, filename)
				.first();

		return new BScreen(scr);
	}

	public BScreen(final Screen scrn) {
		screen = scrn;
	}

	public BScreen(final String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 */
	public BScreen() {
	}

	public Screen getBean() {
		return screen;
	}

	/**
	 * @return @see com.arahant.beans.Screen#getAuthCode()
	 */
	public String getAuthCode() {
		return screen.getAuthCode();
	}

	/**
	 * @return @see com.arahant.beans.Screen#getDescription()
	 */
	@Override
	public String getDescription() {
		return screen.getDescription();
	}

	/**
	 * @return @see com.arahant.beans.Screen#getFilename()
	 */
	public String getFilename() {
		return screen.getFilename();
	}

	/**
	 * @return @see com.arahant.beans.Screen#getName()
	 */
	@Override
	public String getName() {
		return screen.getName();
	}

	@Override
	public String getId() {
		return screen.getScreenId();
	}

	public void setId(final String screenId) {
		screen.setScreenId(screenId);
	}

	/**
	 * @return @see com.arahant.beans.Screen#getScreenId()
	 */
	public String getScreenId() {
		return screen.getScreenId();
	}

	/**
	 * @param authCode
	 * @see com.arahant.beans.Screen#setAuthCode(java.lang.String)
	 */
	public void setAuthCode(final String authCode) {
		screen.setAuthCode(authCode);
	}

	/**
	 * @param description
	 * @see com.arahant.beans.Screen#setDescription(java.lang.String)
	 */
	public void setDescription(final String description) {
		screen.setDescription(description);
	}

	/**
	 * @param filename
	 * @see com.arahant.beans.Screen#setFilename(java.lang.String)
	 */
	public void setFilename(final String filename) {
		screen.setFilename(filename);
	}

	/**
	 * @param name
	 * @see com.arahant.beans.Screen#setName(java.lang.String)
	 */
	public void setName(final String name) {
		screen.setName(name);
	}

	/**
	 * @param screenId
	 * @see com.arahant.beans.Screen#setScreenId(java.lang.String)
	 */
	public void setScreenId(final String screenId) {
		screen.setScreenId(screenId);
	}

	public char getTechnology() {
		return screen.getTechnology();
	}

	public void setTechnology(char tech) {
		screen.setTechnology(tech);
	}

	private Screen getParentScreen() {
		final ScreenGroupHierarchy sgh = ArahantSession.getHSU().createCriteria(ScreenGroupHierarchy.class)
				.notNull(ScreenGroupHierarchy.SCREEN)
				.joinTo(ScreenGroupHierarchy.PARENTSCREENGROUP)
				.joinTo(ScreenGroup.SCREENGROUPHIERARCHIESFORCHILDSCREENGROUP)
				.eq(ScreenGroupHierarchy.SCREEN, screen).first();

		if (sgh == null)
			return null;

		return sgh.getScreen();
	}

	public int getScreenType() {
		return screen.getScreenType();
	}

	/**
	 * @return
	 */
	public String getType() {
		switch (screen.getScreenType()) {
			case 1:
				return "Normal";
			case 2:
				return "Parent";
			case 3:
				return "Child";
			case 4:
				return "Wizard";
			case 5:
				return "Wizard Page";
		}
		return "Unknown";
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

	@Override
	public String create() throws ArahantException {
		screen = new Screen();
		screen.generateId();
		return getScreenId();
	}

	@Override
	public void delete() throws ArahantDeleteException {
		if (screen != null && screen.getScreenGroupHierarchies().size() > 0)
			throw new ArahantDeleteException("Can not delete screen that is in screen group.");
		if (screen != null && screen.getName().equals("Screen.swf"))
			throw new ArahantDeleteException("Can not delete required screen.");
		try {
			ArahantSession.getHSU().delete(screen);
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
	}

	@Override
	public void insert() throws ArahantException {
		if (isEmpty(getFilename()))
			throw new ArahantException("Filename is required!");
		ArahantSession.getHSU().insert(screen);
	}

	private void internalLoad(final String key) throws ArahantException {
		logger.debug("Loading " + key);
		screen = ArahantSession.getHSU().get(Screen.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	/* (non-Javadoc)
	 * @see com.arahant.business.interfaces.IDBFunctions#update()
	 */
	@Override
	public void update() throws ArahantException {
		if (isEmpty(getFilename()))
			throw new ArahantException("Filename is required!");
		ArahantSession.getHSU().saveOrUpdate(screen);
	}

	/**
	 * @param screenIds
	 * @throws ArahantException
	 * @throws ArahantDeleteException
	 */
	public static void delete(final HibernateSessionUtil hsu, final String[] screenIds) throws ArahantDeleteException, ArahantException {
		for (final String element : screenIds)
			new BScreen(element).delete();
	}

	static BScreen[] makeArray(final List<Screen> l) {
		final BScreen[] ret = new BScreen[l.size()];

		int loop = 0;

		final Iterator itr = l.iterator();
		while (itr.hasNext())
			ret[loop++] = new BScreen((Screen) itr.next());

		return ret;
	}

	/**
	 * @param hsu
	 * @param max
	 */
	public static BScreen[] list(final HibernateSessionUtil hsu, final int max) {
		return makeArray(hsu.createCriteria(Screen.class).orderBy(Screen.NAME).
				setMaxResults(max).list());
	}

	public static BScreen[] search(final HibernateSessionUtil hsu, String name, String extId, String fileName, boolean includeNormal, boolean includeParent, boolean includeChild, boolean includeWizard, boolean includeWizardPage, final int max) {

		while (true) {
			HibernateCriteriaUtil<Screen> hcu = hsu.createCriteria(Screen.class).orderBy(Screen.NAME).setMaxResults(max);

			hcu.like(Screen.NAME, name);
			hcu.like(Screen.FILENAME, fileName);

			if (!isEmpty(extId))
				hcu.eq(Screen.SCREENID, IDGenerator.expandKey(extId));

			ArrayList<Short> types = new ArrayList<Short>();
			if (includeNormal)
				types.add(Screen.NORMAL_TYPE);

			if (includeParent)
				types.add(Screen.PARENT_TYPE);

			if (includeChild)
				types.add(Screen.CHILD_TYPE);

			if (includeWizard)
				types.add(Screen.WIZARD_TYPE);

			if (includeWizardPage)
				types.add(Screen.WIZARD_PAGE_TYPE);

			hcu.in(Screen.SCREENTYPE, types);

			/*
			 * I discovered that, for whatever reason, the screen names and descriptions have a leading space character.
			 * This code corrects that, and should do nothing otherwise.
			 */
			List<Screen> lst = hcu.list();
			if (lst.size() > 0 && lst.get(0).getName().charAt(0) == ' ') {
				for (Screen rec : lst) {
					rec.setName(rec.getName().trim());
					rec.setDescription(rec.getDescription().trim());
				}
				hsu.commitTransaction();
			} else
				return makeArray(lst);
		}
	}

	/**
	 * @return
	 */
	public String getParentScreenFileName() {
		final Screen ps = getParentScreen();

		if (ps != null)
			return ps.getFilename();
		return "";
	}

	/**
	 * @param i
	 * @return
	 */
	public String getDescription(final int i) {
		final String d = getDescription();
		if (d.length() < i)
			return d;
		else
			return d.substring(0, i);
	}

	/**
	 *
	 */
	public String getParentScreenId() {
		final Screen ps = getParentScreen();

		if (ps != null)
			return ps.getScreenId();
		return "";
	}

	public BScreen[] getChildScreens() {
		return makeArray(getChildren());
	}

	private List<Screen> getChildren() {
		return ArahantSession.getHSU().createCriteria(Screen.class).orderBy(Screen.NAME)
				.joinTo(Screen.SCREENGROUPHIERARCHIES)
				.joinTo(ScreenGroupHierarchy.PARENTSCREENGROUP)
				.joinTo(ScreenGroup.SCREENGROUPHIERARCHIESFORCHILDSCREENGROUP)
				.eq(ScreenGroupHierarchy.SCREEN, screen).list();
	}

	/**
	 * @param hsu
	 * @param screenId
	 * @param max
	 * @return
	 */
	public static BScreen[] listParentScreens(final HibernateSessionUtil hsu, final String screenId, final int max) {

		//this search should only return screens that do not already 
		// have a parent and should exclude the passed in screen, capped at 50
		// NOTE the passed in screen may be empty, which means it does not yet exist

		return makeArray(hsu.createCriteria(Screen.class)
				.ne(Screen.SCREENID, screenId)
				.orderBy(Screen.NAME)
				.joinTo(Screen.SCREENGROUPHIERARCHIES)
				.joinTo(ScreenGroupHierarchy.CHILDSCREENGROUP)
				.joinTo(ScreenGroup.SCREENGROUPHIERARCHIESFORPARENTSCREENGROUP)
				.isNull(ScreenGroupHierarchy.SCREEN)
				.setMaxResults(max)
				.list());
	}

	/**
	 * @param screenType
	 */
	public void setScreenType(final int screenType) {
		screen.setScreenType((short) screenType);
	}

	/**
	 * @param hsu
	 * @param name
	 * @return
	 */
	public static BScreen[] searchParentScreens(final HibernateSessionUtil hsu, final String name, String extId) {
		return makeArray(hsu.createCriteria(Screen.class)
				.orderBy(Screen.NAME)
				.eq(Screen.SCREENTYPE, (short) 2)
				.like(Screen.NAME, name)
				.like(Screen.SCREENID, extId)
				.list());
	}

	public static BScreen[] searchScreens(final String name, String extId) {
		return makeArray(ArahantSession.getHSU().createCriteria(Screen.class)
				.orderBy(Screen.NAME)
				.like(Screen.NAME, name)
				.like(Screen.SCREENID, extId)
				.list());
	}

	/**
	 * @param screenGroupId
	 * @return
	 */
	public boolean getIsDefaultScreenFor(final String screenGroupId) {
		if (isEmpty(screenGroupId))
			return false;
		return ArahantSession.getHSU().createCriteria(ScreenGroupHierarchy.class)
				.eq(ScreenGroupHierarchy.SCREEN, screen)
				.eq(ScreenGroupHierarchy.DEFAULTSCREEN, 'Y')
				.joinTo(ScreenGroupHierarchy.PARENTSCREENGROUP)
				.eq(ScreenGroup.SCREENGROUPID, screenGroupId).exists();
	}

	/**
	 * @return
	 */
	public String getChildGroup() {
		try {
			return screen.getChildScreenGroups().iterator().next().getScreenGroupId();
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * @return
	 */
	public String getExtId() {
		return IDGenerator.shrinkKey(screen.getScreenId());
	}

	public String getExtIdExpanded() {
		return screen.getScreenId();
	}

	public String getAvatarPath() {
		return screen.getAvatarPath();
	}

	public void setAvatarPath(String avatarPath) {
		screen.setAvatarPath(avatarPath);
	}
	/**
	 * @return
	 *
	 * public boolean getDefault() { return screen.getDefaultScreen()=='Y'; }
	 *
	 * /
	 **
	 * @param b
	 *
	 * public void setDefault(boolean dflt) {
	 *
	 * if (!dflt) screen.setDefaultScreen('N'); else { final Screen
	 * parent=getParentScreen();
	 *
	 * if (parent!=null) { final BScreen p=new BScreen(hsu,parent); final
	 * Iterator childrenItr=p.getChildren().iterator();
	 *
	 * while (childrenItr.hasNext()) { final Screen
	 * child=(Screen)childrenItr.next(); if (child.getDefaultScreen()=='Y' &&
	 * !child.equals(screen)) { child.setDefaultScreen('N');
	 * hsu.saveOrUpdate(child); } } } screen.setDefaultScreen('Y'); }
	 *
	 * }
	 */
}
