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
 *
 * Created on Feb 20, 2007
*/

package com.arahant.services.standard.project.projectCurrentStatus;

import com.arahant.beans.Person;
import com.arahant.business.BProject;
import com.arahant.business.BRouteStop;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.ArahantSession;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.util.List;


public class LoadCurrentStatusReturn extends TransmitReturnBase {

	private int companyPriority;
	private int orgGroupPriority;
	private int clientPriority;
	private String routeStopCompanyId;
	private String routeStopOrgGroupId;
	private String routeStopId;
	private String projectStatusId;
	private LoadCurrentStatusReturnItem [] item;
	private double estimatedHours;
	private short requiredWorkers;
	private int projectFirstDate;
	private int projectLastDate;

	public LoadCurrentStatusReturn() {
		super();
	}

	public double getEstimatedHours() {
		return estimatedHours;
	}

	public void setEstimatedHours(double estimatedHours) {
		this.estimatedHours = estimatedHours;
	}

	public String getProjectStatusId() {
		return projectStatusId;
	}

	public void setProjectStatusId(String projectStatusId) {
		this.projectStatusId = projectStatusId;
	}

	public String getRouteStopCompanyId() {
		return routeStopCompanyId;
	}

	public void setRouteStopCompanyId(String routeStopCompanyId) {
		this.routeStopCompanyId = routeStopCompanyId;
	}

	public String getRouteStopId() {
		return routeStopId;
	}

	public void setRouteStopId(String routeStopId) {
		this.routeStopId = routeStopId;
	}

	public String getRouteStopOrgGroupId() {
		return routeStopOrgGroupId;
	}

	public void setRouteStopOrgGroupId(String routeStopOrgGroupId) {
		this.routeStopOrgGroupId = routeStopOrgGroupId;
	}

	public int getClientPriority() {
		return clientPriority;
	}

	public void setClientPriority(int clientPriority) {
		this.clientPriority = clientPriority;
	}

	public int getCompanyPriority() {
		return companyPriority;
	}

	public void setCompanyPriority(int companyPriority) {
		this.companyPriority = companyPriority;
	}

	public int getOrgGroupPriority() {
		return orgGroupPriority;
	}

	public void setOrgGroupPriority(int orgGroupPriority) {
		this.orgGroupPriority = orgGroupPriority;
	}

	public short getRequiredWorkers() {
		return requiredWorkers;
	}

	public void setRequiredWorkers(short requiredWorkers) {
		this.requiredWorkers = requiredWorkers;
	}

	void setData(final BProject bp, final String shiftId) throws Exception {
		companyPriority = bp.getCompanyPriority();
		orgGroupPriority = bp.getOrgGroupPriority();
		clientPriority = bp.getClientPriority();
		projectFirstDate = bp.getEstimatedFirstDate();
		projectLastDate = bp.getEstimatedLastDate();

		if (bp.getRouteStopId() != null && !bp.getRouteStopId().equals("")) {
			BRouteStop brs = new BRouteStop(bp.getRouteStopId());
			routeStopCompanyId = brs.getCompanyId();
			if (!brs.getOrgGroupId().equals(routeStopCompanyId))
				routeStopOrgGroupId = brs.getOrgGroupId();
			else
				routeStopOrgGroupId = "";
			routeStopId = bp.getRouteStopId();
		} else {
			routeStopCompanyId = "";
			routeStopOrgGroupId = "";
			routeStopId = "";
		}
		Connection db = ArahantSession.getKissConnection();
		String projectId = bp.getProjectId();
		projectStatusId = bp.getProjectStatusId();
		List<Person> persons = bp.getAssignedPersons2();
		item = new LoadCurrentStatusReturnItem[persons.size()];
		for (int loop = 0; loop < persons.size(); loop++)
			item[loop] = new LoadCurrentStatusReturnItem(db, projectId, persons.get(loop));
		estimatedHours = bp.getEstimateHours();

		if (shiftId != null && !shiftId.isEmpty()) {
			Record rec = db.fetchOne("select required_workers from project_shift where project_shift_id=?", shiftId);
			requiredWorkers = rec != null ? rec.getShort("required_workers") : 0;
		} else {
			Record rec = db.fetchOne("select sum(required_workers) required_workers " +
					"from project_shift  " +
					"where project_id=?", bp.getProjectId());
			Long v = rec.getLong("required_workers");
			long lval = v == null ? 0 : v;
			requiredWorkers = (short) lval;
		}
	}

	public LoadCurrentStatusReturnItem[] getItem() {
		return item;
	}

	public void setItem(LoadCurrentStatusReturnItem[] item) {
		this.item = item;
	}

	public int getProjectFirstDate() {
		return projectFirstDate;
	}

	public void setProjectFirstDate(int projectFirstDate) {
		this.projectFirstDate = projectFirstDate;
	}

	public int getProjectLastDate() {
		return projectLastDate;
	}

	public void setProjectLastDate(int projectLastDate) {
		this.projectLastDate = projectLastDate;
	}
}

	
