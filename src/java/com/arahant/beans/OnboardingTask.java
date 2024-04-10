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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * Arahant
 */
@Entity
@Table(name=OnboardingTask.TABLE_NAME)
public class OnboardingTask extends ArahantBean implements Serializable{


	public static final String TABLE_NAME = "onboarding_task";
	public static final String ONBOARDING_TASK_ID = "onboardingTaskId";
	public static final String ONBOARDING_CONFIG = "onboardingConfig";
	public static final String SEQ_NO = "seqno";
	public static final String SCREEN = "screen";
	public static final String TASK_NAME = "taskName";
	public static final String DESCRIPTION = "description";
	public static final String COMPLETE_BY_DAYS = "completeByDays";
	

	private String onboardingTaskId;
	private OnboardingConfig onboardingConfig;
	private int seqno;
	private Screen screen;
	private String taskName;
	private String description;
	private int completeByDays;

	@Column(name="completed_by")
	public int getCompleteByDays() {
		return completeByDays;
	}

	public void setCompleteByDays(int completeByDays) {
		this.completeByDays = completeByDays;
	}

	@Column(name="description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="onboarding_config_id")
	public OnboardingConfig getOnboardingConfig() {
		return onboardingConfig;
	}

	public void setOnboardingConfig(OnboardingConfig onboardingConfig) {
		this.onboardingConfig = onboardingConfig;
	}

	@Id
	@Column(name="onboarding_task_id")
	public String getOnboardingTaskId() {
		return onboardingTaskId;
	}

	public void setOnboardingTaskId(String onboardingTaskId) {
		this.onboardingTaskId = onboardingTaskId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="screen_id")
	public Screen getScreen() {
		return screen;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	@Column(name="seqno")
	public int getSeqno() {
		return seqno;
	}

	public void setSeqno(int seqno) {
		this.seqno = seqno;
	}

	@Column(name="task_name")
	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}



	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "onboarding_task_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return onboardingTaskId = IDGenerator.generate(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final OnboardingTask other = (OnboardingTask) obj;
		if ((this.onboardingTaskId == null) ? (other.onboardingTaskId != null) : !this.onboardingTaskId.equals(other.onboardingTaskId)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 97 * hash + (this.onboardingTaskId != null ? this.onboardingTaskId.hashCode() : 0);
		return hash;
	}


}
