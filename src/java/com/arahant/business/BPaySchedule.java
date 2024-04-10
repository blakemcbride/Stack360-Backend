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

import com.arahant.beans.PaySchedule;
import com.arahant.beans.PaySchedulePeriod;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import java.util.Calendar;
import java.util.List;

public class BPaySchedule extends SimpleBusinessObjectBase<PaySchedule> {

	public BPaySchedule(PaySchedule o) {
		bean = o;
	}

	public BPaySchedule() {
	}

	public BPaySchedule(String id) {
		super(id);
	}

	@Override
	public String create() throws ArahantException {
		bean = new PaySchedule();
		return bean.generateId();
	}

	@Override
	public void insert() throws ArahantException {
		if (bean.getCompany() == null)
			bean.setCompany(ArahantSession.getHSU().getCurrentCompany());
		super.insert();
	}

	public int getCurrentPeriodStart() {

		PaySchedulePeriod period = ArahantSession.getHSU().createCriteria(PaySchedulePeriod.class)
				.eq(PaySchedulePeriod.PAY_SCHEDULE, bean)
				.ge(PaySchedulePeriod.PERIOD_END, DateUtils.now())
				.orderBy(PaySchedulePeriod.PERIOD_END)
				.first();

		if (period == null)
			return 0;
		return new BPaySchedulePeriod(period).getStartDate();
	}

	public int getLastPayPeriodDate() {
		PaySchedulePeriod p = ArahantSession.getHSU().createCriteria(PaySchedulePeriod.class)
				.eq(PaySchedulePeriod.PAY_SCHEDULE, bean)
				.orderByDesc(PaySchedulePeriod.PAY_DATE)
				.first();
		if (p == null)
			return 0;
		return p.getPayDate();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(PaySchedule.class, key);
	}

	@Override
	public void delete() {
		ArahantSession.getHSU().createCriteria(PaySchedulePeriod.class)
				.eq(PaySchedulePeriod.PAY_SCHEDULE, bean)
				.delete();
		super.delete();
	}

	public static BPaySchedule[] makeArray(List<PaySchedule> l) {
		BPaySchedule[] ret = new BPaySchedule[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BPaySchedule(l.get(loop));

		return ret;
	}

	public static void delete(String[] ids) {
		for (String id : ids)
			new BPaySchedule(id).delete();
	}

	public String getName() {
		return bean.getScheduleName();
	}

	public String getDescription() {
		return bean.getDescription();
	}

	public void setName(String name) {
		bean.setScheduleName(name);
	}

	public void setDescription(String description) {
		bean.setDescription(description);
	}

	public String getId() {
		return bean.getPayScheduleId();
	}

	public void generateWeekly(int startDate, int endDate, int dayOfWeek, int weeks, boolean first) {
		Calendar cal = DateUtils.getCalendar(startDate);
		Calendar start = DateUtils.getCalendar(startDate);
		Calendar end = DateUtils.getCalendar(endDate);

		cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);

		if (cal.before(start))
			cal.add(Calendar.WEEK_OF_YEAR, 1);

		if (!ArahantSession.getHSU().createCriteria(PaySchedulePeriod.class)
				.lt(PaySchedulePeriod.PERIOD_END, DateUtils.getDate(cal))
				.exists()) {
			cal.add(Calendar.WEEK_OF_YEAR, -1);
			BPaySchedulePeriod per = new BPaySchedulePeriod();
			per.create();
			per.setEndDate(cal);
			per.setPayDate(cal);
			per.setSchedule(bean);
			per.setBeginningOfYear(false);
			per.insert();
			cal.add(Calendar.WEEK_OF_YEAR, 1);
		}
		while (!cal.after(end)) {
			if (ArahantSession.getHSU().createCriteria(PaySchedulePeriod.class)
					.eq(PaySchedulePeriod.PAY_SCHEDULE, bean)
					.eq(PaySchedulePeriod.PERIOD_END, DateUtils.getDate(cal))
					.exists())
				continue;
			BPaySchedulePeriod per = new BPaySchedulePeriod();
			per.create();
			per.setEndDate(cal);
			per.setPayDate(cal);
			per.setSchedule(bean);
			per.setBeginningOfYear(first);
			first = false;
			per.insert();

			cal.add(Calendar.WEEK_OF_YEAR, weeks);
		}
	}

	public void generateMonthly(int periodStart, int fromDate, int toDate, boolean first) {
		Calendar cal = DateUtils.getCalendar(fromDate);
		Calendar end = DateUtils.getCalendar(toDate);

		cal.set(Calendar.DAY_OF_MONTH, periodStart);
		//cal.add(Calendar.DAY_OF_YEAR, -1);


		if (!ArahantSession.getHSU().createCriteria(PaySchedulePeriod.class)
				.lt(PaySchedulePeriod.PERIOD_END, DateUtils.getDate(cal))
				.exists()) {
			cal.add(Calendar.MONTH, -1);
			BPaySchedulePeriod per = new BPaySchedulePeriod();
			per.create();
			per.setEndDate(cal);
			per.setPayDate(cal);
			per.setSchedule(bean);
			per.setBeginningOfYear(false);
			per.insert();
			cal.add(Calendar.MONTH, 1);
		}

		while (!cal.after(end)) {
			if (ArahantSession.getHSU().createCriteria(PaySchedulePeriod.class)
					.eq(PaySchedulePeriod.PAY_SCHEDULE, bean)
					.eq(PaySchedulePeriod.PERIOD_END, DateUtils.getDate(cal))
					.exists())
				continue;
			BPaySchedulePeriod per = new BPaySchedulePeriod();
			per.create();
			per.setEndDate(cal);
			per.setPayDate(cal);
			per.setSchedule(bean);
			per.setBeginningOfYear(first);
			first = false;
			per.insert();

			cal.add(Calendar.MONTH, 1);
		}
	}

	public int getCurrentPeriodEnd() {

		PaySchedulePeriod period = ArahantSession.getHSU().createCriteria(PaySchedulePeriod.class)
				.eq(PaySchedulePeriod.PAY_SCHEDULE, bean)
				.ge(PaySchedulePeriod.PERIOD_END, DateUtils.now())
				.orderBy(PaySchedulePeriod.PERIOD_END)
				.first();

		if (period == null)
			return 0;
		return new BPaySchedulePeriod(period).getEndDate();
	}

	private Calendar getLastDayOfMonth(int date) {
		Calendar t1 = DateUtils.getCalendar(date);
		setToLastDayOfMonth(t1);
		return t1;
	}

	private void setToLastDayOfMonth(Calendar t1) {
		t1.set(Calendar.MONTH, t1.get(Calendar.MONTH) + 1);
		t1.set(Calendar.DAY_OF_MONTH, 1);
		t1.add(Calendar.DAY_OF_YEAR, -1);
	}

	public void generateTwiceMonthly(int periodStart, int periodStart2, int fromDate, int toDate, boolean first) {
		Calendar cal = DateUtils.getCalendar(fromDate);
		Calendar cal2 = DateUtils.getCalendar(fromDate);
		Calendar end = DateUtils.getCalendar(toDate);

		cal.set(Calendar.DAY_OF_MONTH, periodStart);
		cal2.set(Calendar.DAY_OF_MONTH, periodStart2);

		if (!ArahantSession.getHSU().createCriteria(PaySchedulePeriod.class)
				.lt(PaySchedulePeriod.PERIOD_END, DateUtils.getDate(cal))
				.exists()) {
			cal2.add(Calendar.MONTH, -1);
			if (periodStart2 == 31)
				setToLastDayOfMonth(cal2);
			BPaySchedulePeriod per = new BPaySchedulePeriod();
			per.create();
			per.setEndDate(cal2);
			per.setPayDate(cal2);
			per.setSchedule(bean);
			per.setBeginningOfYear(false);
			per.insert();
			cal.add(Calendar.MONTH, 1);
		}

		if (periodStart2 == 31)
			setToLastDayOfMonth(cal2);

		while (!cal.after(end)) {
			if (ArahantSession.getHSU().createCriteria(PaySchedulePeriod.class)
					.eq(PaySchedulePeriod.PAY_SCHEDULE, bean)
					.eq(PaySchedulePeriod.PERIOD_END, DateUtils.getDate(cal))
					.exists()) {
				cal.add(Calendar.MONTH, 1);
				cal2.add(Calendar.MONTH, 1);
				if (periodStart2 == 31)
					setToLastDayOfMonth(cal2);
				continue;
			}
			BPaySchedulePeriod per = new BPaySchedulePeriod();
			per.create();
			per.setEndDate(cal);
			per.setPayDate(cal);
			per.setSchedule(bean);
			per.setBeginningOfYear(first);
			first = false;
			per.insert();

			per = new BPaySchedulePeriod();
			per.create();
			per.setEndDate(cal2);
			per.setPayDate(cal2);
			per.setSchedule(bean);
			per.insert();

			cal.add(Calendar.MONTH, 1);
			cal2.add(Calendar.MONTH, 1);
			if (periodStart2 == 31)
				setToLastDayOfMonth(cal2);
		}
	}

	public static BPaySchedule[] list() {
		return makeArray(BPaySchedule.list(null));
	}

	public static List<PaySchedule> list(String orgGroupId) {
		if (!isEmpty(orgGroupId)) {
			BOrgGroup bo = new BOrgGroup(orgGroupId);
			if (!isEmpty(bo.getCompanyId())) {
				BCompany bc = new BCompany(bo.getCompanyId());
				return ArahantSession.getHSU().createCriteriaNoCompanyFilter(PaySchedule.class)
						.eqOrNull(PaySchedule.COMPANY, bc.getBean())
						.orderBy(PaySchedule.NAME)
						.list();
			} else {
				BCompany bc = new BCompany(orgGroupId);
				return ArahantSession.getHSU().createCriteriaNoCompanyFilter(PaySchedule.class)
						.eqOrNull(PaySchedule.COMPANY, bc.getBean())
						.orderBy(PaySchedule.NAME)
						.list();
			}

		} else
			return ArahantSession.getHSU().createCriteria(PaySchedule.class)
					.orderBy(PaySchedule.NAME)
					.list();
	}
}
