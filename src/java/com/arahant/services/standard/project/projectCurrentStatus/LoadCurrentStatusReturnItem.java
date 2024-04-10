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

package com.arahant.services.standard.project.projectCurrentStatus;

import com.arahant.beans.Person;
import com.arahant.business.BEmployee;
import com.arahant.exceptions.ArahantException;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

public class LoadCurrentStatusReturnItem {
	private String personId;
	private String lastName; 
	private String middleName;
	private String firstName;
	private int projectHours;
	private int allHours;
	private String position;

	public LoadCurrentStatusReturnItem()
	{
	}

	LoadCurrentStatusReturnItem(Connection db, String projectId, Person p) {
		personId = p.getPersonId();
		lastName = p.getLname();
		middleName = p.getMname();
		firstName = p.getFname();
		try {
			BEmployee bemp = new BEmployee(p.getPersonId());
			position = bemp.getPositionName();
		} catch (Exception e) {
			position = "";
		}
		try {
			Record rec = db.fetchOne("select sum(ts.total_hours) total_hours " +
					"from timesheet ts " +
					"join project_shift ps " +
					"  on ts.project_shift_id = ps.project_shift_id " +
					"where ps.project_id = ? and ts.billable = 'Y' and ts.person_id = ?", projectId, personId);
			if (rec != null) {
				Double v = rec.getDouble("total_hours");
				if (v != null)
					projectHours = (int) v.doubleValue();
			}
			rec = db.fetchOne("select sum(total_hours) total_hours from timesheet where billable = 'Y' and person_id = ?", personId);
			if (rec != null) {
				Double v = rec.getDouble("total_hours");
				if (v != null)
					allHours = (int) v.doubleValue();
			}
		} catch (Exception throwables) {
			projectHours = allHours = 0;
		}
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
}
