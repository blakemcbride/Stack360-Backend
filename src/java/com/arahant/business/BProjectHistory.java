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

import com.arahant.beans.Person;
import com.arahant.beans.Project;
import com.arahant.beans.ProjectHistory;
import com.arahant.beans.ProjectStatus;
import com.arahant.beans.RouteStop;
import com.arahant.exceptions.ArahantException;
import com.arahant.reports.ProjectHistoryReport;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.itextpdf.text.DocumentException;
import java.io.FileNotFoundException;
import java.util.List;

public class BProjectHistory extends SimpleBusinessObjectBase<ProjectHistory> {

	/**
	 * @param history
	 */
	public BProjectHistory(final ProjectHistory history) {
		bean = history;
	}

	public BProjectHistory() {
	}

	/**
	 * @param historyId
	 * @throws ArahantException
	 */
	public BProjectHistory(final String historyId) throws ArahantException {
		internalLoad(historyId);
	}

	public BProjectHistory(String personId, int date, int time) {
		bean = ArahantSession.getHSU().createCriteria(ProjectHistory.class).eq(ProjectHistory.DATE, date).eq(ProjectHistory.TIME, time).joinTo(ProjectHistory.CHANGED_BY).eq(Person.PERSONID, personId).first();
	}

	@Override
	public String create() throws ArahantException {
		bean = new ProjectHistory();
		bean.setPerson(ArahantSession.getHSU().getCurrentPerson());
		bean.setDateChanged(DateUtils.now());
		bean.setTimeChanged(DateUtils.nowTime());
		return bean.generateId();
	}

	public String getChangedById() {
		return bean.getPerson().getPersonId();
	}

	public int getDateChanged() {
		return bean.getDateChanged();
	}

	public int getTimeChanged() {
		return bean.getTimeChanged();
	}

	private void internalLoad(final String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ProjectHistory.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ProjectHistory.class, key);
	}

	public String getHistoryId() {
		return bean.getProjectHistoryId();
	}

	public String getProjectName() {
		return bean.getProject().getProjectName();
	}

	public String getDateTimeFormatted() {
		return DateUtils.getDateTimeFormatted(bean.getDateChanged(), bean.getTimeChanged());
	}

	/*
	 * @return
	 *
	 * public String getToLastName() {
	 *
	 * try { return bean.getToPerson().getLname(); } catch (final Exception e) {
	 * return ""; } }
	 *
	 * /
	 *
	 * @return
	 *
	 * public String getToFirstName() {
	 *
	 * try { return bean.getToPerson().getFname(); } catch (final Exception e) {
	 * return ""; } }
	 *
	 * /
	 *
	 * @return
	 */
	public String getByFirstName() {
		return bean.getPerson().getFname();
	}

	public String getByLastName() {
		return bean.getPerson().getLname();
	}

	public static BProjectHistory[] list(final String projectId) {
		return makeArray(ArahantSession.getHSU().createCriteria(ProjectHistory.class).orderBy(ProjectHistory.DATE).orderBy(ProjectHistory.TIME).joinTo(ProjectHistory.PROJECT).eq(Project.PROJECTID, projectId).list());
	}

	private static BProjectHistory[] makeArray(final List<ProjectHistory> l) {
		final BProjectHistory[] ret = new BProjectHistory[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BProjectHistory(l.get(loop));
		return ret;
	}

	/*
	 * @param assignedTo
	 *
	 * public void setFromPerson(final Person fromPerson) {
	 * bean.setFromPerson(fromPerson); }
	 *
	 * /
	 **
	 * @param currentPerson
	 *
	 * public void setToPerson(final Person toPerson) {
	 * bean.setToPerson(toPerson); }
	 *
	 *
	 * /
	 *
	 * @param project
	 */
	public void setProject(final Project project) {
		bean.setProject(project);
	}

	/**
	 * @param projectStatus
	 */
	public void setFromProjectStatus(final ProjectStatus projectStatus) {
		bean.setFromStatus(projectStatus);
	}

	/**
	 * @param projectStatus
	 */
	public void setToProjectStatus(final ProjectStatus projectStatus) {
		bean.setToStatus(projectStatus);
	}

	/**
	 * @param routeStop
	 */
	public void setFromRouteStop(final RouteStop routeStop) {
		bean.setFromStop(routeStop);
	}

	/**
	 * @param routeStop
	 */
	public void setToRouteStop(final RouteStop routeStop) {
		bean.setToStop(routeStop);
	}

	/**
	 * @param projectId
	 * @return
	 * @throws ArahantException
	 */
	public static String getReport(final String projectId) throws ArahantException {
		return new ProjectHistoryReport().build(makeArray(ArahantSession.getHSU().
				createCriteria(ProjectHistory.class).orderByDesc(ProjectHistory.DATE).orderByDesc(ProjectHistory.TIME).joinTo(ProjectHistory.PROJECT).eq(Project.PROJECTID, projectId).list()));
	}

	/**
	 * @return
	 */
	public String getFromStatusCode() {
		return bean.getFromStatus().getCode();
	}

	/*
	 * @return
	 *
	 * public String getFromFirstName() {
	 *
	 * try { return bean.getFromPerson().getFname(); } catch (final Exception e)
	 * { return ""; } }
	 *
	 *
	 * /
	 *
	 * @return
	 */
	public String getToStatusCode() {
		return bean.getToStatus().getCode();
	}

	public String getToNames() {
		String ret = "";

		return ret;
	}

	/**
	 * @return
	 */
	public String getToRouteStopOrgGroupName() {
		try {
			return bean.getToStop().getOrgGroup().getName();
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * @return
	 */
	public String getFromRouteStopOrgGroupName() {
		try {
			return bean.getFromStop().getOrgGroup().getName();
		} catch (final Exception e) {
			return "";
		}
	}

	/*
	 * @return
	 *
	 * public String getFromLastName() { try { return
	 * bean.getFromPerson().getLname(); } catch (final Exception e) { return "";
	 * } }
	 *
	 *
	 * /
	 *
	 * @return
	 */
	public String getToRouteStopName() {
		try {
			return bean.getToStop().getDescription();
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * @return
	 */
	public String getFromRouteStopName() {
		try {
			return bean.getFromStop().getDescription();
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * @param currentPerson
	 */
	public void setAssignedBy(final Person currentPerson) {
		bean.setPerson(currentPerson);
	}

	/*
	 * public String getAssignedToDisplayName() { try { return
	 * bean.getToPerson().getNameLFM(); } catch (final Exception e)	{ return "";
	 * } }
	 *
	 *
	 * public String getAssignedFromDisplayName() { try { return
	 * bean.getFromPerson().getNameLFM(); } catch (final Exception e)	{ return
	 * ""; } }
	 */
	/**
	 * @return
	 */
	public String getChangeType() {
		StringBuilder sb = new StringBuilder();
		String tmp1;
		String tmp2;

		tmp1 = (bean.getFromStatus() == null) ? "" : bean.getFromStatus().getProjectStatusId();
		tmp2 = (bean.getToStatus() == null) ? "" : bean.getToStatus().getProjectStatusId();
		if (!tmp1.equals(tmp2))
			sb.append("Status");

		tmp1 = (bean.getFromStop() == null) ? "" : bean.getFromStop().getRouteStopId();
		tmp2 = (bean.getToStop() == null) ? "" : bean.getToStop().getRouteStopId();
		if (!tmp1.equals(tmp2)) {
			if (sb.length() > 0)
				sb.append(", ");
			sb.append("Route Stop");
		}
		return sb.toString();
	}

	/*
	 * @return
	 *
	 * public String getToNameLFM() {
	 *
	 * try { return bean.getToPerson().getNameLFM(); } catch (final Exception e)
	 * { return ""; } }
	 *
	 *
	 * /
	 *
	 * @return
	 */
	public String getByNameLFM() {
		return bean.getPerson().getNameLFM();
	}

	void setDateChanged(int date) {
		bean.setDateChanged(date);
	}

	void setTimeChanged(int time) {
		bean.setTimeChanged(time);
	}
}
