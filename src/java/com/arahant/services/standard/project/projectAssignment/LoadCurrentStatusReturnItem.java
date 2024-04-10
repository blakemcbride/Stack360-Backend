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

package com.arahant.services.standard.project.projectAssignment;

import com.arahant.beans.Address;
import com.arahant.beans.Person;
import com.arahant.business.BEmployee;
import com.arahant.business.BPerson;
import com.arahant.business.BProject;
import com.arahant.utils.ZipCodeDistance;
import org.kissweb.DateTime;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.sql.SQLException;
import java.util.Date;
import java.util.Set;

public class LoadCurrentStatusReturnItem {
	private String projectEmployeeJoinId;
	private String personId;
	private String lastName; 
	private String middleName;
	private String firstName;
	private int startDate;
	private int projectHours;
	private int allHours;
	private String position;

	private boolean confirmed;
	private String confirmedDate;
	private String confirmedPerson;
	private boolean verified;
	private String verifiedDate;
	private String verifiedPerson;
	private boolean manager;  //  is this a manager for this project
	private boolean reportHours;  //  does this person report hours on this project
	private int milesFromProject;
	private long checkinDateTime;
	private int checkinDistance;
	private String projectShiftId;
	private String shiftStart;

	public LoadCurrentStatusReturnItem()
	{
	}

	LoadCurrentStatusReturnItem(Connection db, String projectEmployeeJoinId, String projectId, Person pers, BProject bproj, String shiftId) {
		this.projectEmployeeJoinId = projectEmployeeJoinId;
		BEmployee bemp = new BEmployee(pers.getPersonId());
		personId = pers.getPersonId();
		lastName = pers.getLname();
		middleName = pers.getMname();
		firstName = pers.getFname();
		position = bemp.getPositionName();
		try {
			Record rec;

			/*
			if (shiftId != null && !shiftId.isEmpty())
				rec = db.fetchOne("select sum(ts.total_hours) total_hours " +
						"                 from timesheet ts " +
						"                 join project_shift ps " +
						"                   on ts.project_shift_id = ps.project_shift_id " +
						"                 where ps.project_shift_id = ? " +
						"                       and ts.billable = 'Y' " +
						"                       and ts.person_id = ?", shiftId, personId);
			else

			 */

			// get all project hours regardless of shift
				rec = db.fetchOne("select sum(ts.total_hours) total_hours " +
						"                 from timesheet ts " +
						"                 join project_shift ps " +
						"                   on ts.project_shift_id = ps.project_shift_id " +
						"                 where ps.project_id = ? " +
						"                       and ts.billable = 'Y' " +
						"                       and ts.person_id = ?", projectId, personId);
			if (rec != null) {
				Double v = rec.getDouble("total_hours");
				if (v != null)
					projectHours = (int) v.doubleValue();
			}

			rec = db.fetchOne("select sum(total_hours) total_hours " +
					"          from timesheet " +
					"          where billable = 'Y' " +
					"                and person_id = ?", personId);
			if (rec != null) {
				Double v = rec.getDouble("total_hours");
				if (v != null)
					allHours = (int) v.doubleValue();
			}

			if (shiftId != null && !shiftId.isEmpty()) {
				rec = db.fetchOne("select pej.start_date, pej.confirmed_date, pej.confirmed_person_id, " +
						"                 pej.verified_date, pej.verified_person_id, pej.manager, pej.hours, " +
						"                 ps.project_shift_id, ps.shift_start" +
						"          from project_employee_join pej " +
						"          join project_shift ps" +
						"            on pej.project_shift_id = ps.project_shift_id" +
						"          where pej.person_id=? " +
						"                and pej.project_shift_id=?", personId, shiftId);
				projectShiftId = rec.getString("project_shift_id");
				shiftStart = rec.getString("shift_start");
			} else {
				// never happens
				rec = db.fetchOne("select start_date, confirmed_date, confirmed_person_id, verified_date, verified_person_id, manager, hours " +
						"          from project_employee_join pej " +
						"          join project_shift ps " +
						"            on pej.project_shift_id = ps.project_shift_id " +
						"          where pej.person_id=? " +
						"                and ps.project_id=?", personId, projectId);
				projectShiftId = shiftStart = "";
			}
			Date cd = rec.getDateTime("confirmed_date");
			String cpid = rec.getString("confirmed_person_id");
			Date vd = rec.getDateTime("verified_date");
			String vpid = rec.getString("verified_person_id");

			startDate = rec.getInt("start_date");
			if (cd == null || cpid == null || cpid.isEmpty())
				confirmed = false;
			else {
				confirmed = true;
				confirmedDate = DateTime.format(cd);
				confirmedPerson = (new BPerson(cpid)).getNameLFM();
			}
			if (vd == null || vpid == null || vpid.isEmpty())
				verified = false;
			else {
				verified = true;
				verifiedDate = DateTime.format(vd);
				verifiedPerson = (new BPerson(vpid)).getNameLFM();
			}
			manager = rec.getChar("manager") == 'Y';
			reportHours = rec.getChar("hours") == 'Y';

			Set<Address> persAddresses = pers.getAddresses();
			Address persAddress = null;
			for (Address ad : persAddresses) {
				if (ad.getAddressType() == 2 && ad.getRecordType() == 'R') {
					persAddress = ad;
					break;
				}
			}
			Address projAddress = bproj.getAddress();

			milesFromProject = ZipCodeDistance.distance(projAddress == null ? null : projAddress.getZip(), persAddress == null ? null : persAddress.getZip());

			if (shiftId.isEmpty())
				rec = db.fetchOne("select wc.confirmation_time, wc.distance " +
						"          from worker_confirmation wc" +
						"          join project_shift ps" +
						"            on wc.project_shift_id = ps.project_shift_id" +
						"          where wc.person_id = ?" +
						"                and ps.project_id = ?" +
						"          order by wc.confirmation_time desc", personId, projectId);
			else
				rec = db.fetchOne("select confirmation_time, distance " +
						"          from worker_confirmation" +
						"          where person_id = ?" +
						"                and project_shift_id = ?" +
						"          order by confirmation_time desc", personId, shiftId);
			if (rec != null) {
				checkinDateTime = rec.getDateTime("confirmation_time").getTime();
				checkinDistance = rec.getInt("distance");
				if (checkinDistance < 0)
					checkinDistance = 0;
			}
		} catch (Exception throwables) {
			projectHours = allHours = 0;
		}
	}

	public String getProjectEmployeeJoinId() {
		return projectEmployeeJoinId;
	}

	public void setProjectEmployeeJoinId(String projectEmployeeJoinId) {
		this.projectEmployeeJoinId = projectEmployeeJoinId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public int getStartDate() {
		return startDate;
	}

	public void setStartDate(int startDate) {
		this.startDate = startDate;
	}

	public int getProjectHours() {
		return projectHours;
	}

	public void setProjectHours(int projectHours) {
		this.projectHours = projectHours;
	}

	public int getAllHours() {
		return allHours;
	}

	public void setAllHours(int allHours) {
		this.allHours = allHours;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public String getConfirmedDate() {
		return confirmedDate;
	}

	public void setConfirmedDate(String confirmedDate) {
		this.confirmedDate = confirmedDate;
	}

	public String getConfirmedPerson() {
		return confirmedPerson;
	}

	public void setConfirmedPerson(String confirmedPerson) {
		this.confirmedPerson = confirmedPerson;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public String getVerifiedDate() {
		return verifiedDate;
	}

	public void setVerifiedDate(String verifiedDate) {
		this.verifiedDate = verifiedDate;
	}

	public String getVerifiedPerson() {
		return verifiedPerson;
	}

	public void setVerifiedPerson(String verifiedPerson) {
		this.verifiedPerson = verifiedPerson;
	}

	public boolean isManager() {
		return manager;
	}

	public void setManager(boolean manager) {
		this.manager = manager;
	}

	public boolean isReportHours() {
		return reportHours;
	}

	public void setReportHours(boolean reportHours) {
		this.reportHours = reportHours;
	}

	public int getMilesFromProject() {
		return milesFromProject;
	}

	public void setMilesFromProject(int milesFromProject) {
		this.milesFromProject = milesFromProject;
	}

	public long getCheckinDateTime() {
		return checkinDateTime;
	}

	public void setCheckinDateTime(long checkinDateTime) {
		this.checkinDateTime = checkinDateTime;
	}

	public int getCheckinDistance() {
		return checkinDistance;
	}

	public void setCheckinDistance(int checkinDistance) {
		this.checkinDistance = checkinDistance;
	}

	public String getProjectShiftId() {
		return projectShiftId;
	}

	public void setProjectShiftId(String projectShiftId) {
		this.projectShiftId = projectShiftId;
	}

	public String getShiftStart() {
		return shiftStart;
	}

	public void setShiftStart(String shiftStart) {
		this.shiftStart = shiftStart;
	}
}
