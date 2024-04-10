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

public class BPaySchedulePeriod extends SimpleBusinessObjectBase<PaySchedulePeriod> {

	public static BPaySchedulePeriod[] search(String id, int fromDate, int toDate, int cap) {
		return makeArray(ArahantSession.getHSU().createCriteria(PaySchedulePeriod.class)
				.orderBy(PaySchedulePeriod.PERIOD_END)
				.setMaxResults(cap)
				.dateBetween(PaySchedulePeriod.PAY_DATE, fromDate, toDate)
				.joinTo(PaySchedulePeriod.PAY_SCHEDULE)
				.eq(PaySchedule.ID, id)
				.list());
	}

	public BPaySchedulePeriod(PaySchedulePeriod o) {
		bean = o;
	}

	public BPaySchedulePeriod() {
	}

	public BPaySchedulePeriod(String id) {
		super(id);
	}

	@Override
	public String create() throws ArahantException {
		bean = new PaySchedulePeriod();
		return bean.generateId();
	}

	public boolean getBeginningOfYear() {
		return bean.getBeginningOfYear() == 'Y';
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(PaySchedulePeriod.class, key);
	}

	public void setBeginningOfYear(boolean beginningOfYear) {
		bean.setBeginningOfYear(beginningOfYear ? 'Y' : 'N');
	}

	public void setScheduleId(String id) {
		bean.setPaySchedule(ArahantSession.getHSU().get(PaySchedule.class, id));
	}

	static BPaySchedulePeriod[] makeArray(List<PaySchedulePeriod> l) {
		BPaySchedulePeriod[] ret = new BPaySchedulePeriod[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BPaySchedulePeriod(l.get(loop));

		return ret;
	}

	public static void delete(String[] ids) {
		for (String id : ids)
			new BPaySchedulePeriod(id).delete();
	}

	public int getStartDate() {
		//get the last one
		PaySchedulePeriod prior = ArahantSession.getHSU().createCriteria(PaySchedulePeriod.class)
				.eq(PaySchedulePeriod.PAY_SCHEDULE, bean.getPaySchedule())
				.lt(PaySchedulePeriod.PERIOD_END, bean.getLastDate())
				.orderByDesc(PaySchedulePeriod.PERIOD_END)
				.first();

		if (prior == null)
			return 0;

		return DateUtils.addDays(prior.getLastDate(), 1);
	}

	public int getEndDate() {
		return bean.getLastDate();
	}

	public int getPayDate() {
		return bean.getPayDate();
	}

	public void setEndDate(int endDate) {
		bean.setLastDate(endDate);
	}

	public void setPayDate(int payDate) {
		bean.setPayDate(payDate);
	}

	public String getId() {
		return bean.getPayPeriodId();
	}

	void setEndDate(Calendar cal) {
		setEndDate(DateUtils.getDate(cal));
	}

	void setPayDate(Calendar cal) {
		setPayDate(DateUtils.getDate(cal));
	}

	void setSchedule(PaySchedule ps) {
		bean.setPaySchedule(ps);
	}
}
