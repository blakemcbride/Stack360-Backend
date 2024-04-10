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


/**
 *
 *
 */

package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name=SalesActivityResult.TABLE_NAME)
public class SalesActivityResult extends ArahantBean implements Serializable{

	public static final String TABLE_NAME = "sales_activity_result";
	public static final String SALES_ACTIVITY_RESULT_ID = "salesActivityResultId";
	public static final String SALES_ACTIVITY = "salesActivity";
	public static final String DESCRIPTION = "description";
	public static final String SEQUENCE = "sequence";
	public static final String LAST_ACTIVE_DATE = "lastActiveDate";
	public static final String FIRST_FOLLOW_UP_DAYS = "firstFollowUpDays";
	public static final String FIRST_FOLLOW_UP_TASK = "firstFollowUpTask";
	public static final String SECOND_FOLLOW_UP_DAYS = "secondFollowUpDays";
	public static final String SECOND_FOLLOW_UP_TASK = "secondFollowUpTask";
	public static final String THIRD_FOLLOW_UP_DAYS = "thirdFollowUpDays";
	public static final String THIRD_FOLLOW_UP_TASK = "thirdFollowUpTask";

	private String salesActivityResultId;
	private SalesActivity salesActivity;
	private String description;
	private short sequence;
	private int lastActiveDate;
	private short firstFollowUpDays;
	private String firstFollowUpTask;
	private short secondFollowUpDays;
	private String secondFollowUpTask;
	private short thirdFollowUpDays;
	private String thirdFollowUpTask;

	@Column(name="description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name="last_active_date")
	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	@JoinColumn(name="sales_activity_id")
	@OneToOne
	public SalesActivity getSalesActivity() {
		return salesActivity;
	}

	public void setSalesActivity(SalesActivity salesActivity) {
		this.salesActivity = salesActivity;
	}

	@Id
	@Column(name="sales_activity_result_id")
	public String getSalesActivityResultId() {
		return salesActivityResultId;
	}

	public void setSalesActivityResultId(String salesActivityResultId) {
		this.salesActivityResultId = salesActivityResultId;
	}

	@Column(name="seqno")
	public short getSequence() {
		return sequence;
	}

	public void setSequence(short sequence) {
		this.sequence = sequence;
	}

	@Column(name="first_follow_up_days")
	public short getFirstFollowUpDays() {
		return firstFollowUpDays;
	}

	public void setFirstFollowUpDays(short firstFollowUpDays) {
		this.firstFollowUpDays = firstFollowUpDays;
	}

	@Column(name="first_follow_up_task")
	public String getFirstFollowUpTask() {
		return firstFollowUpTask;
	}

	public void setFirstFollowUpTask(String firstFollowUpTask) {
		this.firstFollowUpTask = firstFollowUpTask;
	}

	@Column(name="second_follow_up_days")
	public short getSecondFollowUpDays() {
		return secondFollowUpDays;
	}

	public void setSecondFollowUpDays(short secondFollowUpDays) {
		this.secondFollowUpDays = secondFollowUpDays;
	}

	@Column(name="second_follow_up_task")
	public String getSecondFollowUpTask() {
		return secondFollowUpTask;
	}

	public void setSecondFollowUpTask(String secondFollowUpTask) {
		this.secondFollowUpTask = secondFollowUpTask;
	}

	@Column(name="third_follow_up_days")
	public short getThirdFollowUpDays() {
		return thirdFollowUpDays;
	}

	public void setThirdFollowUpDays(short thirdFollowUpDays) {
		this.thirdFollowUpDays = thirdFollowUpDays;
	}

	@Column(name="third_follow_up_task")
	public String getThirdFollowUpTask() {
		return thirdFollowUpTask;
	}

	public void setThirdFollowUpTask(String thirdFollowUpTask) {
		this.thirdFollowUpTask = thirdFollowUpTask;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "sales_activity_result_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return salesActivityResultId = IDGenerator.generate(this);
	}
}
