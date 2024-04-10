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

import com.arahant.beans.Project;
import com.arahant.beans.ProjectChecklistDetail;
import com.arahant.beans.RouteStop;
import com.arahant.beans.RouteStopChecklist;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BProjectChecklistDetail extends SimpleBusinessObjectBase<ProjectChecklistDetail> {

	public static void delete(String[] checkListDetailIds) {
		for (String id : checkListDetailIds)
			new BProjectChecklistDetail(id).delete();
	}

	private static BProjectChecklistDetail[] makeArray(List<ProjectChecklistDetail> l) {
		BProjectChecklistDetail[] ret = new BProjectChecklistDetail[l.size()];
		for (int loop = 0; loop < l.size(); loop++)
			ret[loop] = new BProjectChecklistDetail(l.get(loop));
		return ret;
	}

	public BProjectChecklistDetail() {
	}

	public BProjectChecklistDetail(String key) {
		super(key);
	}

	private BProjectChecklistDetail(ProjectChecklistDetail pcd) {
		bean = pcd;
	}

	@Override
	public String create() throws ArahantException {
		bean = new ProjectChecklistDetail();
		return bean.generateId();
	}

	public String getCheckListDetailId() {
		return bean.getProjectChecklistDetailId();
	}

	public String getCheckListId() {
		return bean.getRouteStopChecklist().getRouteStopChecklistId();
	}

	public String getComments() {
		return bean.getEntryComments();
	}

	public int getCompletedDate() {
		return bean.getDateCompleted();
	}

	public String getCompletedName() {
		return bean.getCompletedBy();
	}

	public String getDescription() {
		return bean.getRouteStopChecklist().getItemDescription();
	}

	public String getDetail() {
		return bean.getRouteStopChecklist().getItemDetail();
	}

	public String getEntryDateTimeFormatted() {
		return DateUtils.getDateTimeFormatted(bean.getEntryTimestamp());
	}

	public String getEntryNameFormatted() {
		return bean.getPerson().getNameLFM();
	}

	public String getRequired() {
		return bean.getRouteStopChecklist().getItemRequired() == 'Y' ? "Yes" : "No";
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ProjectChecklistDetail.class, key);
	}

	public static BProjectChecklistDetail[] list(String projectId, String routeStopId) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		HibernateCriteriaUtil<ProjectChecklistDetail> hcu = hsu.createCriteria(ProjectChecklistDetail.class);

		hcu.joinTo(ProjectChecklistDetail.PROJECT).eq(Project.PROJECTID, projectId);
		hcu.joinTo(ProjectChecklistDetail.ROUTE_STOP_CHECKLIST).orderBy(RouteStopChecklist.PRIORITY).joinTo(RouteStopChecklist.ROUTE_STOP).eq(RouteStop.ROUTE_STOP_ID, routeStopId);

		List<ProjectChecklistDetail> l = hcu.list();

		List<RouteStopChecklist> rl = hsu.createCriteria(RouteStopChecklist.class).orderBy(RouteStopChecklist.PRIORITY).dateInside(RouteStopChecklist.START_DATE, RouteStopChecklist.END_DATE, DateUtils.now()).joinTo(RouteStopChecklist.ROUTE_STOP).eq(RouteStop.ROUTE_STOP_ID, routeStopId).list();

		Project proj = hsu.get(Project.class, projectId);

		List<ProjectChecklistDetail> ret = new ArrayList<ProjectChecklistDetail>(rl.size());

		//need to make a dummy checklist detail for every missing route stop check list
		for (RouteStopChecklist rsc : rl) {
			//do I have corresponding project detail
			ProjectChecklistDetail found = null;
			for (ProjectChecklistDetail pcd : l)
				if (pcd.getRouteStopChecklist().equals(rsc)) {
					found = pcd;
					break;
				}
			if (found != null) {
				l.remove(found);
				ret.add(found);
			} else {
				//make a dummy record
				ProjectChecklistDetail pcd = new ProjectChecklistDetail();
				pcd.setProject(proj);
				pcd.setRouteStopChecklist(rsc);
				ret.add(pcd);
			}
		}

		ret.addAll(l);

		return makeArray(ret);

	}

	public void setCheckListId(String checkListId) {
		bean.setRouteStopChecklist(ArahantSession.getHSU().get(RouteStopChecklist.class, checkListId));
	}

	public void setComments(String comments) {
		bean.setEntryComments(comments);
	}

	public void setCompletedDate(int completedDate) {
		if (bean.getDateCompleted() != completedDate && completedDate != 0) {
			bean.setEntryTimestamp(new Date());
			bean.setPerson(ArahantSession.getHSU().getCurrentPerson());
		}
		bean.setDateCompleted(completedDate);
	}

	public void setCompletedName(String completedName) {
		bean.setCompletedBy(completedName);
	}

	public void setProjectId(String projectId) {
		bean.setProject(ArahantSession.getHSU().get(Project.class, projectId));
	}
}
