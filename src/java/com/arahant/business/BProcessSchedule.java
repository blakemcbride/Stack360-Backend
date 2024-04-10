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

import com.arahant.beans.ProcessHistory;
import com.arahant.beans.ProcessSchedule;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import java.util.List;

/**
 *
 */
public class BProcessSchedule extends SimpleBusinessObjectBase<ProcessSchedule> {

	public static void delete(String[] ids) {
		for (String id : ids)
			new BProcessSchedule(id).delete();
	}

	private static BProcessSchedule[] makeArray(List<ProcessSchedule> l) {
		BProcessSchedule[] ret = new BProcessSchedule[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BProcessSchedule(l.get(loop));
		return ret;
	}

	public BProcessSchedule(ProcessSchedule o) {
		bean = o;
	}

	@Override
	public void delete() {
		ArahantSession.getHSU().createCriteria(ProcessHistory.class)
				.eq(ProcessHistory.SCHEDULE, bean)
				.delete();
		super.delete();
	}

	public BProcessSchedule() {
	}

	public BProcessSchedule(String id) {
		super(id);
	}

	@Override
	public String create() throws ArahantException {
		bean = new ProcessSchedule();
		return bean.generateId();
	}

	public String getDescription() {
		return bean.getJavaClass(); //TODO: map to a nice description
	}

	public String getId() {
		return bean.getProcessScheduleId();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ProcessSchedule.class, key);
	}

	public BProcessHistory[] listHistory(int cap) {
		return BProcessHistory.makeArray(
				ArahantSession.getHSU().createCriteria(ProcessHistory.class)
				.eq(ProcessHistory.SCHEDULE, bean)
				.orderByDesc(ProcessHistory.RUN_TIME)
				.list());
	}

	public static BProcessSchedule[] list() {
		return makeArray(ArahantSession.getHSU().createCriteria(ProcessSchedule.class)
				.orderBy(ProcessSchedule.NAME)
				.list());
	}

	public void setDaysOfMonth(String dom) {
		bean.setRunDaysOfMonth(dom);
	}

	public void setDaysOfWeek(String dow) {
		bean.setRunDaysOfWeek(dow);
	}

	public void setHours(int hour) {
		bean.setRunHours(hour + "");
	}

	public void setJavaClass(String jc) {
		bean.setJavaClass(jc);
	}

	public void setMinutes(int minutes) {
		bean.setRunMinutes(minutes + "");
	}

	public void setMonths(String m) {
		bean.setRunMonths(m);
	}

	public void setPerformMissingRuns(boolean doMissingRuns) {
		bean.setPerformMissingRuns(doMissingRuns ? 'Y' : 'N');
	}

	public void setStartDate(int startDate) {
		bean.setStartDate(startDate);
	}

	public void setStartTime(int startTime) {
		bean.setStartTime(startTime);
	}
}
