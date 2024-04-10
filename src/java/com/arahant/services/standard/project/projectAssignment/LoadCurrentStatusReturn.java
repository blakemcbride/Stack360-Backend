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

package com.arahant.services.standard.project.projectAssignment;

import com.arahant.beans.Employee;
import com.arahant.business.BPerson;
import com.arahant.business.BProject;
import com.arahant.business.BRouteStop;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import org.kissweb.database.Command;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.util.ArrayList;
import java.util.List;


public class LoadCurrentStatusReturn extends TransmitReturnBase {

	private int companyPriority;
	private int orgGroupPriority;
	private int clientPriority;
	private String routeStopCompanyId;
	private String routeStopOrgGroupId;
	private String routeStopId;
	private String projectStatusId;
	private LoadCurrentStatusReturnItem[] item;
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

	void setData(final BProject bp, final String shiftId) {
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
		projectStatusId = bp.getProjectStatusId();
		String projectId = bp.getProjectId();
		List<LoadCurrentStatusReturnItem> pers = new ArrayList<>();
		try {
			final Connection db = ArahantSession.getKissConnection();
			final Command cmd = db.newCommand();

			List<Record> recs;
			if (shiftId != null && !shiftId.isEmpty())
				recs = cmd.fetchAll("select pej.project_employee_join_id, pej.person_id, ps.project_shift_id " +
						"from project_employee_join pej " +
						"join person p " +
						"  on pej.person_id = p.person_id " +
						"join project_shift ps " +
						"  on pej.project_shift_id = ps.project_shift_id " +
						"where pej.manager='Y' " +
						"      and pej.hours = 'N' " +
						"      and pej.project_shift_id = ? " +
						"order by lower(p.lname), lower(p.fname), lower(p.mname)", shiftId);
			else
				recs = cmd.fetchAll("select pej.project_employee_join_id, pej.person_id, ps.project_shift_id " +
						"from project_employee_join pej " +
						"join person p " +
						"  on pej.person_id = p.person_id " +
						"join project_shift ps " +
						"  on pej.project_shift_id = ps.project_shift_id " +
						"where ps.project_id=? " +
						"      and pej.manager='Y' " +
						"      and pej.hours = 'N' " +
						"order by lower(p.lname), lower(p.fname), lower(p.mname)", projectId);
			for (Record rec : recs)
				pers.add(new LoadCurrentStatusReturnItem(db, rec.getString("project_employee_join_id"), projectId, (new BPerson(rec.getString("person_id"))).getPerson(), bp, rec.getString("project_shift_id")));

			if (shiftId != null && !shiftId.isEmpty())
				recs = cmd.fetchAll("select pej.project_employee_join_id, pej.person_id, ps.project_shift_id " +
						"from project_employee_join pej " +
						"join person p " +
						"  on pej.person_id = p.person_id " +
						"join project_shift ps " +
						"  on pej.project_shift_id = ps.project_shift_id " +
						"where ps.project_id=? " +
						"      and pej.manager='Y' " +
						"      and pej.hours = 'Y' " +
						"      and pej.project_shift_id = ? " +
						"order by lower(p.lname), lower(p.fname), lower(p.mname)", projectId, shiftId);
			else
				recs = cmd.fetchAll("select pej.project_employee_join_id, pej.person_id, ps.project_shift_id " +
						"from project_employee_join pej " +
						"join person p " +
						"  on pej.person_id = p.person_id " +
						"join project_shift ps " +
						"  on pej.project_shift_id = ps.project_shift_id " +
						"where ps.project_id=? " +
						"      and pej.manager='Y' " +
						"      and pej.hours = 'Y' " +
						"order by lower(p.lname), lower(p.fname), lower(p.mname)", projectId);
			for (Record rec : recs)
				pers.add(new LoadCurrentStatusReturnItem(db, rec.getString("project_employee_join_id"), projectId, (new BPerson(rec.getString("person_id"))).getPerson(), bp, rec.getString("project_shift_id")));

			if (shiftId != null && !shiftId.isEmpty())
				recs = cmd.fetchAll("select pej.project_employee_join_id, pej.person_id, ps.project_shift_id " +
						"from project_employee_join pej " +
						"join person p " +
						"  on pej.person_id = p.person_id " +
						"join project_shift ps " +
						"  on pej.project_shift_id = ps.project_shift_id " +
						"where ps.project_id=? " +
						"      and pej.manager='N' " +
						"      and pej.project_shift_id = ? " +
						"order by lower(p.lname), lower(p.fname), lower(p.mname)", projectId, shiftId);
			else
				recs = cmd.fetchAll("select pej.project_employee_join_id, pej.person_id, ps.project_shift_id " +
						"from project_employee_join pej " +
						"join person p " +
						"  on pej.person_id = p.person_id " +
						"join project_shift ps " +
						"  on pej.project_shift_id = ps.project_shift_id " +
						"where ps.project_id=? " +
						"      and pej.manager='N' " +
						"order by lower(p.lname), lower(p.fname), lower(p.mname)", projectId);
			for (Record rec : recs) {
				BPerson bp2 = new BPerson(rec.getString("person_id"));
				pers.add(new LoadCurrentStatusReturnItem(db, rec.getString("project_employee_join_id"), projectId, bp2.getPerson(), bp, rec.getString("project_shift_id")));
			}
			estimatedHours = bp.getEstimateHours();
			if (shiftId != null  &&  !shiftId.isEmpty()) {
				Record rec = cmd.fetchOne("select required_workers from project_shift where project_shift_id = ?", shiftId);
				Integer rw = rec.getInt("required_workers");
				requiredWorkers = rw == null ? 0 : rec.getShort("required_workers");
			} else {
				Record rec = cmd.fetchOne("select sum(required_workers) required_workers from project_shift where project_id = ?", projectId);
				Long rw = rec.getLong("required_workers");
				requiredWorkers = rw == null ? 0 : (short) (long) rw;
			}
		} catch (Exception e) {
			throw new ArahantException(e);
		}
		item = pers.toArray(new LoadCurrentStatusReturnItem[0]);
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

	
