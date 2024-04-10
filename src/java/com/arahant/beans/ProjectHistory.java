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
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "project_history")
public class ProjectHistory extends ArahantBean implements java.io.Serializable {

	private static final long serialVersionUID = -3262970064721990404L;
	public static final String DATE = "dateChanged";
	public static final String TIME = "timeChanged";
	public static final String PROJECT = "project";
	public static final String CHANGED_BY = "person";
	public static final String FROM_STATUS = "fromStatus";
	public static final String TO_STATUS = "toStatus";
	public static final String FROM_STOP = "fromStop";
	public static final String TO_STOP = "toStop";
	private String projectHistoryId;
	private Project project;
	private Person person;
	private int dateChanged;
	private int timeChanged;
	private ProjectStatus fromStatus;
	private ProjectStatus toStatus;
	private RouteStop fromStop;
	private RouteStop toStop;

	/**
	 * @return Returns the dateChanged.
	 */
	@Column(name = "date_changed")
	public int getDateChanged() {
		return dateChanged;
	}

	/**
	 * @param dateChanged The dateChanged to set.
	 */
	public void setDateChanged(final int dateChanged) {
		this.dateChanged = dateChanged;
	}

	/**
	 * @return Returns the fromStatus.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_status_id")
	public ProjectStatus getFromStatus() {
		return fromStatus;
	}

	/**
	 * @param fromStatus The fromStatus to set.
	 */
	public void setFromStatus(final ProjectStatus fromStatus) {
		this.fromStatus = fromStatus;
	}

	/**
	 * @return Returns the fromStop.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_stop_id")
	public RouteStop getFromStop() {
		return fromStop;
	}

	/**
	 * @param fromStop The fromStop to set.
	 */
	public void setFromStop(final RouteStop fromStop) {
		this.fromStop = fromStop;
	}

	/**
	 * @return Returns the person.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id")
	public Person getPerson() {
		return person;
	}

	/**
	 * @param person The person to set.
	 */
	public void setPerson(final Person person) {
		this.person = person;
	}

	/**
	 * @return Returns the project.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id")
	public Project getProject() {
		return project;
	}

	/**
	 * @param project The project to set.
	 */
	public void setProject(final Project project) {
		this.project = project;
	}

	/**
	 * @return Returns the timeChanged.
	 */
	@Column(name = "time_changed")
	public int getTimeChanged() {
		return timeChanged;
	}

	/**
	 * @param timeChanged The timeChanged to set.
	 */
	public void setTimeChanged(final int timeChanged) {
		this.timeChanged = timeChanged;
	}

	/**
	 * @return Returns the toStatus.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_status_id")
	public ProjectStatus getToStatus() {
		return toStatus;
	}

	/**
	 * @param toStatus The toStatus to set.
	 */
	public void setToStatus(final ProjectStatus toStatus) {
		this.toStatus = toStatus;
	}

	/**
	 * @return Returns the toStop.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_stop_id")
	public RouteStop getToStop() {
		return toStop;
	}

	/**
	 * @param toStop The toStop to set.
	 */
	public void setToStop(final RouteStop toStop) {
		this.toStop = toStop;
	}

	/**
	 * @return Returns the projectHistoryId.
	 */
	@Id
	@Column(name = "project_history_id")
	public String getProjectHistoryId() {
		return projectHistoryId;
	}

	/**
	 * @param projectHistoryId The projectHistoryId to set.
	 */
	public void setProjectHistoryId(final String projectHistoryId) {
		this.projectHistoryId = projectHistoryId;
	}

	public ProjectHistory() {
		super();
	}

	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#generateId()
	 */
	@Override
	public String generateId() throws ArahantException {

		projectHistoryId = IDGenerator.generate(this);
		return projectHistoryId;
	}

	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#keyColumn()
	 */
	@Override
	public String keyColumn() {

		return "project_history_id";
	}

	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#tableName()
	 */
	@Override
	public String tableName() {

		return "project_history";
	}

	@Override
	public boolean equals(Object o) {
		if (projectHistoryId == null && o == null)
			return true;
		if (projectHistoryId != null && o instanceof ProjectHistory)
			return projectHistoryId.equals(((ProjectHistory) o).getProjectHistoryId());

		return false;
	}

	@Override
	public int hashCode() {
		if (projectHistoryId == null)
			return 0;
		return projectHistoryId.hashCode();
	}

}
