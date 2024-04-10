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
import com.arahant.beans.SalesActivity;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.util.List;

public class BSalesActivity extends SimpleBusinessObjectBase<SalesActivity> {

	public BSalesActivity() {
	}

	public BSalesActivity(final SalesActivity o) {
		bean = o;
	}

	public BSalesActivity(final String key) throws ArahantException {
		internalLoad(key);
	}

	public static BSalesActivity findOrMake(String code) {

		HibernateSessionUtil hsu = ArahantSession.getHSU();

		SalesActivity sa = hsu.createCriteria(SalesActivity.class)
				.eq(SalesActivity.ACTIVITY_CODE, code)
				.first();

		if (sa != null)
			return new BSalesActivity(sa);


		BSalesActivity bsa = new BSalesActivity();
		bsa.create();

		bsa.setActivityCode(code);
		bsa.setDescription(code);
		bsa.setSalesPoints((short) 0);
		bsa.setSeqno((short) hsu.createCriteria(SalesActivity.class).count());
		bsa.setLastActiveDate(0);

		bsa.insert();

		return bsa;
	}

	public String getCompanyId() {
		if (bean.getOrgGroup() != null)
			return bean.getOrgGroup().getOrgGroupId();
		return "";
	}

	public void setCompanyId(String companyId) {
		setOrgGroup(ArahantSession.getHSU().get(OrgGroup.class, companyId));
	}

	public String getActivityCode() {
		return bean.getActivityCode();
	}

	public void setActivityCode(String activityCode) {
		bean.setActivityCode(activityCode);
	}

	public String getDescription() {
		return bean.getDescription();
	}

	public void setDescription(String description) {
		bean.setDescription(description);
	}

	public String getSalesActivityId() {
		return bean.getSalesActivityId();
	}

	public short getSalesPoints() {
		return bean.getSalesPoints();
	}

	public void setSalesPoints(short salesPoints) {
		bean.setSalesPoints(salesPoints);
	}

	public short getSeqno() {
		return bean.getSeqno();
	}

	public void setSeqno(short seqno) {
		bean.setSeqno(seqno);
	}

	public int getLastActiveDate() {
		return bean.getLastActiveDate();
	}

	public void setLastActiveDate(int lastActiveDate) {
		bean.setLastActiveDate(lastActiveDate);
	}

	public OrgGroup getOrgGroup() {
		return bean.getOrgGroup();
	}

	public void setOrgGroup(OrgGroup orgGroup) {
		bean.setOrgGroup(orgGroup);
	}

	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(bean);
	}

	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(bean);
	}

	@Override
	public void delete() throws ArahantDeleteException {
		try {
			ArahantSession.getHSU().delete(bean);
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException("Unable to delete sales activity because one or more items are in use. To disable set final date.");
		}
	}

	public static void delete(final String[] salesActivityIds) throws ArahantException {
		for (final String element : salesActivityIds)
			new BSalesActivity(element).delete();
	}

	@Override
	public String create() throws ArahantException {
		bean = new SalesActivity();
		bean.generateId();
		return getSalesActivityId();
	}

	private void internalLoad(String key) throws ArahantException {
		logger.debug("Loading " + key);
		bean = ArahantSession.getHSU().get(SalesActivity.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	public void moveUp(boolean moveUp) {
		if (moveUp)
			moveUp();
		else
			moveDown();
	}

	public void moveUp() {
		if (bean.getSeqno() > 0) {
			SalesActivity bc = ArahantSession.getHSU().createCriteria(SalesActivity.class)
					.geOrEq(SalesActivity.LASTACTIVEDATE, DateUtils.now(), 0)
					.eq(SalesActivity.SEQNO, (short) (bean.getSeqno() - 1))
					.first();

			short temp = bean.getSeqno();
			bc.setSeqno((short) 99999);
			ArahantSession.getHSU().saveOrUpdate(bc);
			ArahantSession.getHSU().flush();
			bean.setSeqno((short) (bean.getSeqno() - 1));
			ArahantSession.getHSU().saveOrUpdate(bean);
			ArahantSession.getHSU().flush();
			bc.setSeqno(temp);
			ArahantSession.getHSU().saveOrUpdate(bc);
		} else //shift them all
		{
			List<SalesActivity> l = ArahantSession.getHSU().createCriteria(SalesActivity.class)
					.orderBy(SalesActivity.SEQNO)
					.geOrEq(SalesActivity.LASTACTIVEDATE, DateUtils.now(), 0)
					.list();

			l.get(0).setSeqno((short) 99999);
			ArahantSession.getHSU().saveOrUpdate(l.get(0));
			ArahantSession.getHSU().flush();
			for (int loop = 1; loop < l.size(); loop++) {
				l.get(loop).setSeqno((short) (l.get(loop).getSeqno() - 1));
				ArahantSession.getHSU().saveOrUpdate(l.get(loop));
				ArahantSession.getHSU().flush();
			}

			l.get(0).setSeqno((short) (l.size() - 1));
			ArahantSession.getHSU().saveOrUpdate(l.get(0));
		}
	}

	public void moveDown() {
		if (bean.getSeqno() != ArahantSession.getHSU().createCriteria(SalesActivity.class)
				.geOrEq(SalesActivity.LASTACTIVEDATE, DateUtils.now(), 0)
				.count() - 1) {
			SalesActivity bc = ArahantSession.getHSU().createCriteria(SalesActivity.class)
					.geOrEq(SalesActivity.LASTACTIVEDATE, DateUtils.now(), 0)
					.eq(SalesActivity.SEQNO, (short) (bean.getSeqno() + 1))
					.first();

			short temp = bean.getSeqno();
			bc.setSeqno((short) 999999);
			ArahantSession.getHSU().saveOrUpdate(bc);
			ArahantSession.getHSU().flush();
			bean.setSeqno((short) (bean.getSeqno() + 1));
			ArahantSession.getHSU().saveOrUpdate(bean);
			bc.setSeqno(temp);
			ArahantSession.getHSU().saveOrUpdate(bc);
		} else //shift them all
		{
			List<SalesActivity> l = ArahantSession.getHSU().createCriteria(SalesActivity.class)
					.geOrEq(SalesActivity.LASTACTIVEDATE, DateUtils.now(), 0)
					.orderBy(SalesActivity.SEQNO)
					.list();

			l.get(l.size() - 1).setSeqno((short) 99999);
			ArahantSession.getHSU().saveOrUpdate(l.get(l.size() - 1));
			ArahantSession.getHSU().flush();

			for (int loop = l.size() - 2; loop > -1; loop--) {
				l.get(loop).setSeqno((short) (loop + 1));
				ArahantSession.getHSU().saveOrUpdate(l.get(loop));
				ArahantSession.getHSU().flush();
			}

			l.get(l.size() - 1).setSeqno((short) 0);
			ArahantSession.getHSU().saveOrUpdate(l.get(l.size() - 1));
		}
	}

	public static BSalesActivity[] list(final int max) {
		return makeArray(ArahantSession.getHSU().createCriteria(SalesActivity.class).orderBy(SalesActivity.SEQNO).setMaxResults(max).list());
	}

	//last active after now they are active, before now inactive
	public static BSalesActivity[] list(final int max, final boolean active, final boolean inactive) {
		HibernateCriteriaUtil<SalesActivity> hcu = ArahantSession.getHSU().createCriteria(SalesActivity.class)
				.orderBy(SalesActivity.SEQNO)
				.setMaxResults(max);

		if (active && inactive)
			return makeArray(hcu.list());

		if (active)
			return makeArray(hcu.geOrEq(SalesActivity.LASTACTIVEDATE, DateUtils.now(), 0).list());

		if (inactive)
			return makeArray(hcu.ltAndNeq(SalesActivity.LASTACTIVEDATE, DateUtils.now(), 0).list());

		return new BSalesActivity[0];
	}

	public static BSalesActivity[] makeArray(final List<SalesActivity> l) {
		final BSalesActivity[] ret = new BSalesActivity[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BSalesActivity(l.get(loop));
		return ret;
	}
}
