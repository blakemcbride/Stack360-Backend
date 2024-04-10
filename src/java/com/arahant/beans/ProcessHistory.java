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
import javax.persistence.*;
import org.hibernate.annotations.Fetch;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.util.Date;

/**
 *
 */
@Entity
@Table(name="process_history")
public class ProcessHistory extends ArahantBean {
	public static final String SCHEDULE="schedule";
	public static final String RUN_TIME="runTime";

	private String processHistoryId; 
	private ProcessSchedule schedule;
	private Date runTime;
	private char success;

	@Id
	@Column (name="process_history_id")
	public String getProcessHistoryId() {
		return processHistoryId;
	}

	public void setProcessHistoryId(String processHistoryId) {
		this.processHistoryId = processHistoryId;
	}

	@Column (name="run_time")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getRunTime() {
		return runTime;
	}

	public void setRunTime(Date runTime) {
		this.runTime = runTime;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="process_schedule_id")
	public ProcessSchedule getSchedule() {
		return schedule;
	}

	public void setSchedule(ProcessSchedule schedule) {
		this.schedule = schedule;
	}

	@Column (name="success")
	public char getSuccess() {
		return success;
	}

	public void setSuccess(char success) {
		this.success = success;
	}
	
	
	
	@Override
	public String tableName() {
		return "process_history";
	}

	@Override
	public String keyColumn() {
		return "process_history_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return processHistoryId=IDGenerator.generate(this);
	}

}
