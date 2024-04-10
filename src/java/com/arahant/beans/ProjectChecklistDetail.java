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


package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "project_checklist_detail")
public class ProjectChecklistDetail extends ArahantBean implements Serializable {

	public static final String PROJECT = "project";
	public static final String ROUTE_STOP_CHECKLIST = "routeStopChecklist";
	public static final String DATE = "dateCompleted";
	private String projectChecklistDetailId;
	private Project project;
	private RouteStopChecklist routeStopChecklist;
	private Person person;
	private String completedBy;
	private int dateCompleted;
	private Date entryTimestamp;
	private String entryComments;

	@Column(name = "completed_by")
	public String getCompletedBy() {
		return completedBy;
	}

	public void setCompletedBy(String completedBy) {
		this.completedBy = completedBy;
	}

	@Column(name = "date_completed")
	public int getDateCompleted() {
		return dateCompleted;
	}

	public void setDateCompleted(int dateCompleted) {
		this.dateCompleted = dateCompleted;
	}

	@Column(name = "entry_comments")
	public String getEntryComments() {
		return entryComments;
	}

	public void setEntryComments(String entryComments) {
		this.entryComments = entryComments;
	}

	@Column(name = "entry_timestamp")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getEntryTimestamp() {
		return entryTimestamp;
	}

	public void setEntryTimestamp(Date entryTimestamp) {
		this.entryTimestamp = entryTimestamp;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id")
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Id
	@Column(name = "project_checklist_detail_id")
	public String getProjectChecklistDetailId() {
		return projectChecklistDetailId;
	}

	public void setProjectChecklistDetailId(String projectChecklistDetailId) {
		this.projectChecklistDetailId = projectChecklistDetailId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "route_stop_checklist_id")
	public RouteStopChecklist getRouteStopChecklist() {
		return routeStopChecklist;
	}

	public void setRouteStopChecklist(RouteStopChecklist routeStopChecklist) {
		this.routeStopChecklist = routeStopChecklist;
	}

	@Override
	public String tableName() {
		return "project_checklist_detail";
	}

	@Override
	public String keyColumn() {
		return "project_checklist_detail_id";
	}

	@Override
	public String generateId() throws ArahantException {
		projectChecklistDetailId = IDGenerator.generate(this);
		return projectChecklistDetailId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ProjectChecklistDetail other = (ProjectChecklistDetail) obj;
		if (this.projectChecklistDetailId != other.getProjectChecklistDetailId() && (this.projectChecklistDetailId == null || !this.projectChecklistDetailId.equals(other.getProjectChecklistDetailId())))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 59 * hash + (this.projectChecklistDetailId != null ? this.projectChecklistDetailId.hashCode() : 0);
		return hash;
	}
}
