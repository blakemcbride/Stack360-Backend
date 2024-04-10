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

import com.arahant.beans.SalesActivity;
import com.arahant.beans.SalesActivityResult;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantJessException;
import com.arahant.exceptions.ArahantSecurityException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.List;

public class BSalesActivityResult extends SimpleBusinessObjectBase<SalesActivityResult> {

	public BSalesActivityResult() {
	}

	public BSalesActivityResult(final SalesActivityResult o) {
		bean = o;
	}

	public BSalesActivityResult(final String key) throws ArahantException {
		internalLoad(key);
	}

	public String getDescription() {
		return bean.getDescription();
	}

	public void setDescription(String description) {
		bean.setDescription(description);
	}

	public short getFirstFollowUpDays() {
		return bean.getFirstFollowUpDays();
	}

	public void setFirstFollowUpDays(short firstFollowUpDays) {
		bean.setFirstFollowUpDays(firstFollowUpDays);
	}

	public String getFirstFollowUpTask() {
		return bean.getFirstFollowUpTask();
	}

	public void setFirstFollowUpTask(String firstFollowUpTask) {
		bean.setFirstFollowUpTask(firstFollowUpTask);
	}

	public short getSecondFollowUpDays() {
		return bean.getSecondFollowUpDays();
	}

	public void setSecondFollowUpDays(short secondFollowUpDays) {
		bean.setSecondFollowUpDays(secondFollowUpDays);
	}

	public String getSecondFollowUpTask() {
		return bean.getSecondFollowUpTask();
	}

	public void setSecondFollowUpTask(String secondFollowUpTask) {
		bean.setSecondFollowUpTask(secondFollowUpTask);
	}

	public short getThirdFollowUpDays() {
		return bean.getThirdFollowUpDays();
	}

	public void setThirdFollowUpDays(short thirdFollowUpDays) {
		bean.setThirdFollowUpDays(thirdFollowUpDays);
	}

	public String getThirdFollowUpTask() {
		return bean.getThirdFollowUpTask();
	}

	public void setThirdFollowUpTask(String thirdFollowUpTask) {
		bean.setThirdFollowUpTask(thirdFollowUpTask);
	}

	public int getLastActiveDate() {
		return bean.getLastActiveDate();
	}

	public void setLastActiveDate(int lastActiveDate) {
		bean.setLastActiveDate(lastActiveDate);
	}

	public SalesActivity getSalesActivity() {
		return bean.getSalesActivity();
	}

	public void setSalesActivity(SalesActivity salesActivity) {
		bean.setSalesActivity(salesActivity);
	}

	public String getSalesActivityResultId() {
		return bean.getSalesActivityResultId();
	}

	public short getSequence() {
		return bean.getSequence();
	}

	public void setSequence(short sequence) {
		bean.setSequence(sequence);
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
	public void delete() throws ArahantDeleteException, ArahantSecurityException, ArahantJessException {
		try {
			ArahantSession.getHSU().delete(bean);
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException("Unable to delete current Sales Activity Result.");
		}
	}

	public static void delete(final String[] salesActivityResultIds) throws ArahantException {
		for (final String element : salesActivityResultIds)
			new BSalesActivityResult(element).delete();
	}

	@Override
	public String create() throws ArahantException {
		bean = new SalesActivityResult();
		bean.generateId();
		return getSalesActivityResultId();
	}

	private void internalLoad(String key) throws ArahantException {
		logger.debug("Loading " + key);
		bean = ArahantSession.getHSU().get(SalesActivityResult.class, key);
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
		if (bean.getSequence() > 0) {
			SalesActivityResult bc = ArahantSession.getHSU().createCriteria(SalesActivityResult.class)
					.geOrEq(SalesActivityResult.LAST_ACTIVE_DATE, DateUtils.now(), 0)
					.eq(SalesActivityResult.SEQUENCE, (short) (bean.getSequence() - 1))
					.eq(SalesActivityResult.SALES_ACTIVITY, bean.getSalesActivity())
					.first();

			short temp = bean.getSequence();
			bc.setSequence((short) 99999);
			ArahantSession.getHSU().saveOrUpdate(bc);
			ArahantSession.getHSU().flush();
			bean.setSequence((short) (bean.getSequence() - 1));
			ArahantSession.getHSU().saveOrUpdate(bean);
			ArahantSession.getHSU().flush();
			bc.setSequence(temp);
			ArahantSession.getHSU().saveOrUpdate(bc);
		} else //shift them all
		{
			List<SalesActivityResult> l = ArahantSession.getHSU().createCriteria(SalesActivityResult.class)
					.orderBy(SalesActivityResult.SEQUENCE)
					.geOrEq(SalesActivityResult.LAST_ACTIVE_DATE, DateUtils.now(), 0)
					.eq(SalesActivityResult.SALES_ACTIVITY, bean.getSalesActivity())
					.list();

			l.get(0).setSequence((short) 99999);
			ArahantSession.getHSU().saveOrUpdate(l.get(0));
			ArahantSession.getHSU().flush();
			for (int loop = 1; loop < l.size(); loop++) {
				l.get(loop).setSequence((short) (l.get(loop).getSequence() - 1));
				ArahantSession.getHSU().saveOrUpdate(l.get(loop));
				ArahantSession.getHSU().flush();
			}

			l.get(0).setSequence((short) (l.size() - 1));
			ArahantSession.getHSU().saveOrUpdate(l.get(0));
		}
	}

	public void moveDown() {
		if (bean.getSequence() != ArahantSession.getHSU().createCriteria(SalesActivityResult.class)
				.geOrEq(SalesActivityResult.LAST_ACTIVE_DATE, DateUtils.now(), 0)
				.eq(SalesActivityResult.SALES_ACTIVITY, bean.getSalesActivity())
				.count() - 1) {
			SalesActivityResult bc = ArahantSession.getHSU().createCriteria(SalesActivityResult.class)
					.geOrEq(SalesActivityResult.LAST_ACTIVE_DATE, DateUtils.now(), 0)
					.eq(SalesActivityResult.SEQUENCE, (short) (bean.getSequence() + 1))
					.eq(SalesActivityResult.SALES_ACTIVITY, bean.getSalesActivity())
					.first();

			short temp = bean.getSequence();
			bc.setSequence((short) 999999);
			ArahantSession.getHSU().saveOrUpdate(bc);
			ArahantSession.getHSU().flush();
			bean.setSequence((short) (bean.getSequence() + 1));
			ArahantSession.getHSU().saveOrUpdate(bean);
			bc.setSequence(temp);
			ArahantSession.getHSU().saveOrUpdate(bc);
		} else //shift them all
		{
			List<SalesActivityResult> l = ArahantSession.getHSU().createCriteria(SalesActivityResult.class)
					.geOrEq(SalesActivityResult.LAST_ACTIVE_DATE, DateUtils.now(), 0)
					.orderBy(SalesActivityResult.SEQUENCE)
					.eq(SalesActivityResult.SALES_ACTIVITY, bean.getSalesActivity())
					.list();

			l.get(l.size() - 1).setSequence((short) 99999);
			ArahantSession.getHSU().saveOrUpdate(l.get(l.size() - 1));
			ArahantSession.getHSU().flush();

			for (int loop = l.size() - 2; loop > -1; loop--) {
				l.get(loop).setSequence((short) (loop + 1));
				ArahantSession.getHSU().saveOrUpdate(l.get(loop));
				ArahantSession.getHSU().flush();
			}

			l.get(l.size() - 1).setSequence((short) 0);
			ArahantSession.getHSU().saveOrUpdate(l.get(l.size() - 1));
		}
	}

	public static BSalesActivityResult[] list(final int max) {
		return makeArray(ArahantSession.getHSU().createCriteria(SalesActivityResult.class).orderBy(SalesActivityResult.SEQUENCE).setMaxResults(max).list());
	}

	public static BSalesActivityResult[] list(final int max, final boolean showActive, final boolean showInactive, final String activityId) {
		HibernateCriteriaUtil<SalesActivityResult> hcu = ArahantSession.getHSU().createCriteria(SalesActivityResult.class)
				.orderBy(SalesActivityResult.SEQUENCE)
				.setMaxResults(max);

		hcu.joinTo(SalesActivityResult.SALES_ACTIVITY)
				.eq(SalesActivity.ID, activityId);

		if (showActive && showInactive)
			return makeArray(hcu.list());

		if (showActive)
			return makeArray(hcu.geOrEq(SalesActivityResult.LAST_ACTIVE_DATE, DateUtils.now(), 0).list());

		if (showInactive)
			return makeArray(hcu.ltAndNeq(SalesActivityResult.LAST_ACTIVE_DATE, DateUtils.now(), 0).list());

		return new BSalesActivityResult[0];
	}

	public static BSalesActivityResult[] listActives(final int max) {
		HibernateCriteriaUtil<SalesActivityResult> hcu = ArahantSession.getHSU().createCriteria(SalesActivityResult.class)
				.orderBy(SalesActivityResult.SEQUENCE)
				.setMaxResults(max);

		return makeArray(hcu.geOrEq(SalesActivityResult.LAST_ACTIVE_DATE, DateUtils.now(), 0).list());
	}

	public static BSalesActivityResult[] listActives(final int max, final String activityId) {
		HibernateCriteriaUtil<SalesActivityResult> hcu = ArahantSession.getHSU().createCriteria(SalesActivityResult.class)
				.orderBy(SalesActivityResult.SEQUENCE)
				.setMaxResults(max)
				.geOrEq(SalesActivityResult.LAST_ACTIVE_DATE, DateUtils.now(), 0)
				.joinTo(SalesActivityResult.SALES_ACTIVITY)
				.eq(SalesActivity.ID, activityId);

		return makeArray(hcu.geOrEq(SalesActivityResult.LAST_ACTIVE_DATE, DateUtils.now(), 0).list());
	}

	public static BSalesActivityResult[] listInactives(final int max) {
		HibernateCriteriaUtil<SalesActivityResult> hcu = ArahantSession.getHSU().createCriteria(SalesActivityResult.class)
				.orderBy(SalesActivityResult.SEQUENCE)
				.setMaxResults(max);

		return makeArray(hcu.ltAndNeq(SalesActivityResult.LAST_ACTIVE_DATE, DateUtils.now(), 0).list());
	}

	public static List<SalesActivityResult> listSalesTasks(final int max) {
		HibernateCriteriaUtil<SalesActivityResult> hcu = ArahantSession.getHSU().createCriteria(SalesActivityResult.class)
				.orderBy(SalesActivityResult.SEQUENCE)
				.setMaxResults(max);

		return hcu.list();
	}

	public static BSalesActivityResult[] makeArray(final List<SalesActivityResult> l) {
		final BSalesActivityResult[] ret = new BSalesActivityResult[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BSalesActivityResult(l.get(loop));
		return ret;
	}
}
