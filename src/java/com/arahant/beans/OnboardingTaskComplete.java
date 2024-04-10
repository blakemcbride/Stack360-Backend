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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * Arahant
 */
public class OnboardingTaskComplete extends ArahantBean implements Serializable{

	public static final String TABLE_NAME = "onboarding_task_complete";
	public static final String TASK_COMPLETE_ID = "taskCompleteId";
	public static final String PERSON = "person";
	public static final String ONBOARDING_TASK = "onboardingTask";
	public static final String COMPLETION_DATE = "completionDate";

	private String taskCompleteId;
	private OnboardingTask onboardingTask;
	private String onboardingTaskId;
	private Person person;
	private int completionDate;

	@Id
	@Column(name="task_complete_id")
	public String getTaskCompleteId() {
		return taskCompleteId;
	}

	public void setTaskCompleteId(String s) {
		taskCompleteId = s;
	}

	@Column(name="completion_date")
	public int getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(int d) {
		completionDate = d;
	}

	@Column(name="onboarding_task_id",updatable=false,insertable=false)
	public String getOnboardingTaskId() {
		return onboardingTaskId;
	}

	@OneToMany
	@JoinColumn(name="onboarding_task_id")
	public OnboardingTask getOnboardingTask() {
		return onboardingTask;
	}

	public void setOnboardingTask(OnboardingTask t) {
		this.onboardingTask = t;
	}

	@Column(name="person_id",updatable=false,insertable=false)
	public String getPersonId() {
		return onboardingTaskId;
	}

	@OneToMany
	@JoinColumn(name="person_id")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person p) {
		this.person = p;
	}
	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "task_complete_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return taskCompleteId = IDGenerator.generate(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final OnboardingTaskComplete other = (OnboardingTaskComplete) obj;
		if ((this.taskCompleteId == null) ? (other.taskCompleteId != null) : !this.taskCompleteId.equals(other.taskCompleteId)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 97 * hash + (this.taskCompleteId != null ? this.taskCompleteId.hashCode() : 0);
		return hash;
	}

}
