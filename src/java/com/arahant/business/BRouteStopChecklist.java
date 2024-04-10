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

import com.arahant.beans.RouteStop;
import com.arahant.beans.RouteStopChecklist;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import java.util.List;

public class BRouteStopChecklist extends SimpleBusinessObjectBase<RouteStopChecklist> {

	public BRouteStopChecklist() {
	}

	public BRouteStopChecklist(RouteStopChecklist b) {
		bean = b;
	}

	public BRouteStopChecklist(String id) {
		super(id);
	}

	@Override
	public String create() throws ArahantException {
		bean = new RouteStopChecklist();
		return bean.generateId();
	}

	public int getActiveDate() {
		return bean.getDateActive();
	}

	public String getActiveFormatted() {

		if (bean.getDateActive() <= DateUtils.now() && (bean.getDateInactive() == 0 || bean.getDateInactive() >= DateUtils.now()))
			return "Yes";
		return "No";
	}

	public String getCheckListId() {
		return bean.getRouteStopChecklistId();
	}

	public String getDescription() {
		return bean.getItemDescription();
	}

	public String getDetail() {
		return bean.getItemDetail();
	}

	public String getId() {
		return bean.getRouteStopChecklistId();
	}

	public int getInactiveDate() {
		return bean.getDateInactive();
	}

	public boolean getRequired() {
		return bean.getItemRequired() == 'Y';
	}

	public String getRequiredFormatted() {
		return getRequired() ? "Yes" : "No";
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(RouteStopChecklist.class, key);
	}

	public static BRouteStopChecklist[] list(String routeStopId) {
		return makeArray(ArahantSession.getHSU().createCriteria(RouteStopChecklist.class).orderBy(RouteStopChecklist.PRIORITY).joinTo(RouteStopChecklist.ROUTE_STOP).eq(RouteStop.ROUTE_STOP_ID, routeStopId).list());
	}

	static BRouteStopChecklist[] makeArray(List<RouteStopChecklist> l) {
		BRouteStopChecklist[] ret = new BRouteStopChecklist[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BRouteStopChecklist(l.get(loop));

		return ret;
	}

	public void setActiveDate(int activeDate) {
		bean.setDateActive(activeDate);
	}

	public void setDescription(String description) {
		bean.setItemDescription(description);
	}

	public void setDetail(String detail) {
		bean.setItemDetail(detail);
	}

	public void setInactiveDate(int inactiveDate) {
		bean.setDateInactive(inactiveDate);
	}

	public void setPriority(int priority) {
		bean.setItemPriority((short) priority);
	}

	public void setRequired(boolean required) {
		bean.setItemRequired(required ? 'Y' : 'N');
	}

	void setRouteStop(RouteStop rs) {
		bean.setRouteStop(rs);
	}
}
