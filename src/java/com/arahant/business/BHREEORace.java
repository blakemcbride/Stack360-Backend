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

import com.arahant.beans.HrEeoRace;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.reports.HREEORaceReport;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.HibernateSessionUtil;
import java.io.File;
import java.util.List;

public class BHREEORace extends BusinessLogicBase implements IDBFunctions {

	private HrEeoRace hrEEORace;

	public BHREEORace() {
	}

	/**
	 * @param string
	 * @throws ArahantException
	 */
	public BHREEORace(final String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 * @param account
	 */
	public BHREEORace(final HrEeoRace account) {
		hrEEORace = account;
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#create()
	 */
	@Override
	public String create() throws ArahantException {
		hrEEORace = new HrEeoRace();
		hrEEORace.generateId();

		return getEEORaceId();
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#delete()
	 */
	@Override
	public void delete() throws ArahantDeleteException {
		ArahantSession.getHSU().delete(hrEEORace);
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#insert()
	 */
	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(hrEEORace);
	}

	private void internalLoad(final String key) throws ArahantException {
		hrEEORace = ArahantSession.getHSU().get(HrEeoRace.class, key);
	}

	/*
	 * (non-Javadoc) @see
	 * com.arahant.business.interfaces.IDBFunctions#load(java.lang.String)
	 */
	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#update()
	 */
	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(hrEEORace);
	}

	/**
	 * @return @see com.arahant.beans.HrEeoRace#getEEORaceId()
	 */
	public String getEEORaceId() {
		return hrEEORace.getEeoId();
	}

	/**
	 * @return @see com.arahant.beans.HrEeoRace#getName()
	 */
	public String getName() {
		return hrEEORace.getName();
	}

	/**
	 * @param EEORaceId
	 * @see com.arahant.beans.HrEeoRace#setEEORaceId(java.lang.String)
	 */
	public void setEEORaceId(final String EEORaceId) {
		hrEEORace.setEeoId(EEORaceId);
	}

	/**
	 * @param name
	 * @see com.arahant.beans.HrEeoRace#setName(java.lang.String)
	 */
	public void setName(final String name) {
		hrEEORace.setName(name);
	}

	/**
	 * @param hsu
	 * @param ids
	 * @throws ArahantException
	 */
	public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantException {
		for (final String element : ids)
			new BHREEORace(element).delete();
	}

	/**
	 * @param hsu
	 * @return
	 */
	public static BHREEORace[] list(final HibernateSessionUtil hsu) {

		return makeArray(hsu.createCriteria(HrEeoRace.class).orderBy(HrEeoRace.NAME).list());
	}

	/**
	 * @param l
	 * @return
	 */
	private static BHREEORace[] makeArray(final List<HrEeoRace> l) {
		final BHREEORace[] ret = new BHREEORace[l.size()];
		for (int loop = 0; loop < l.size(); loop++)
			ret[loop] = new BHREEORace(l.get(loop));
		return ret;
	}

	/**
	 * @return
	 */
	public static String getReport(final HibernateSessionUtil hsu) throws Exception {
		final File fyle = FileSystemUtils.createTempFile("HREEORRept", ".pdf");

		new HREEORaceReport().build(hsu, fyle);

		return FileSystemUtils.getHTTPPath(fyle);
	}

	public static String findOrMake(String name) {
		HrEeoRace race = ArahantSession.getHSU().createCriteria(HrEeoRace.class).eq(HrEeoRace.NAME, name).first();

		if (race != null)
			return race.getEeoId();

		BHREEORace br = new BHREEORace();
		br.create();
		br.setName(name);
		br.insert();

		return br.getEEORaceId();
	}
}
