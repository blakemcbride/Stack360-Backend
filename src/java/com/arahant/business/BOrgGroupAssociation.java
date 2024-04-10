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

import com.arahant.beans.OrgGroup;
import com.arahant.beans.OrgGroupAssociation;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantJessException;
import com.arahant.exceptions.ArahantSecurityException;
import com.arahant.utils.*;
import java.util.List;

public class BOrgGroupAssociation extends BusinessLogicBase implements IDBFunctions {

	OrgGroupAssociation orgGroupAssociation;

	private static final transient ArahantLogger logger = new ArahantLogger(BOrgGroupAssociation.class);

	public BOrgGroupAssociation() {
	}

	public BOrgGroupAssociation(final String key) {
		internalLoad(key);
	}

	public BOrgGroupAssociation(final OrgGroupAssociation ogh) {
		orgGroupAssociation = ogh;
	}

	private void internalLoad(String key) throws ArahantException {
		orgGroupAssociation = ArahantSession.getHSU().get(OrgGroupAssociation.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(orgGroupAssociation);
	}

	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(orgGroupAssociation);
	}

	@Override
	public void delete() throws ArahantDeleteException, ArahantSecurityException, ArahantJessException {
		ArahantSession.getHSU().delete(orgGroupAssociation);
	}

	@Override
	public String create() throws ArahantException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public BOrgGroup getOrgGroup() {
		return new BOrgGroup(orgGroupAssociation.getOrgGroup());
	}

	public boolean getIsPrimary() {
		return orgGroupAssociation.getPrimaryIndicator() == 'Y' ? true : false;
	}

	public int getStartDate() {
		return orgGroupAssociation.getStartDate();
	}

	public int getFinalDate() {
		return orgGroupAssociation.getFinalDate();
	}

	public static void deleteExpiredAssocations() {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		try {
			hsu.setCurrentPersonToArahant();

			HibernateCriteriaUtil<OrgGroupAssociation> hcu = hsu.createCriteria(OrgGroupAssociation.class).ne(OrgGroupAssociation.FINALDATE, 0).lt(OrgGroupAssociation.FINALDATE, DateUtils.now());

			HibernateScrollUtil<OrgGroupAssociation> hscr = hcu.scroll();

			int count = 0;
			while (hscr.next()) {
				if (++count % 50 == 0)
					logger.info("Organizational Group Association delete count " + count);

				try {
					new BOrgGroupAssociation(hscr.get()).delete();

					hsu.commitTransaction();
				} catch (Exception ignored) {
					hsu.rollbackTransaction();
				}
				hsu.clear();
				hsu.beginTransaction();
			}
			hscr.close();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public static BOrgGroupAssociation[] listOrgGroupAssociations(BEmployee emp) {
		return BOrgGroupAssociation.makeArray(ArahantSession.getHSU().createCriteria(OrgGroupAssociation.class).eq(OrgGroupAssociation.PERSON, emp.getEmployee()).joinTo(OrgGroupAssociation.ORGGROUP).orderBy(OrgGroup.NAME).list());
	}

	public static BOrgGroupAssociation[] makeArray(final List<OrgGroupAssociation> l) {
		final BOrgGroupAssociation[] ogas = new BOrgGroupAssociation[l.size()];

		for (int loop = 0; loop < ogas.length; loop++)
			ogas[loop] = new BOrgGroupAssociation(l.get(loop));

		return ogas;
	}
}
