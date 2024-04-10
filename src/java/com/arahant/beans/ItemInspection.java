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
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 */
@Entity
@Table(name=ItemInspection.TABLE_NAME)
public class ItemInspection extends ArahantBean {
	public static final String TABLE_NAME="item_inspection";
	public static final String ITEM="item";
	public static final String DATE="inspectionDate";
	private String itemInspectionId;
	private Item item;
	private int inspectionDate;
	private Person personInspecting;
	private Date recordChangeDate;
	private Person recordPerson;
	private char inspectionStatus;
	private String inspectionComments;

	@Column(name="inspection_comments")
	public String getInspectionComments() {
		return inspectionComments;
	}

	public void setInspectionComments(String inspectionComments) {
		this.inspectionComments = inspectionComments;
	}

	@Column(name="inspection_date")
	public int getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(int inspectionDate) {
		this.inspectionDate = inspectionDate;
	}

	@Column (name="inspection_status")
	public char getInspectionStatus() {
		return inspectionStatus;
	}

	public void setInspectionStatus(char inspectionStatus) {
		this.inspectionStatus = inspectionStatus;
	}

	@ManyToOne()
	@JoinColumn(name="item_id")
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	@Id
	@Column(name="item_inspection_id")
	public String getItemInspectionId() {
		return itemInspectionId;
	}

	public void setItemInspectionId(String itemInspectionId) {
		this.itemInspectionId = itemInspectionId;
	}

	@ManyToOne
	@JoinColumn(name="person_inspecting")
	public Person getPersonInspecting() {
		return personInspecting;
	}

	public void setPersonInspecting(Person personInspecting) {
		this.personInspecting = personInspecting;
	}

	@Column(name="record_change_date")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getRecordChangeDate() {
		return recordChangeDate;
	}

	public void setRecordChangeDate(Date recordChangeDate) {
		this.recordChangeDate = recordChangeDate;
	}

	@ManyToOne
	@JoinColumn(name="record_person_id")
	public Person getRecordPerson() {
		return recordPerson;
	}

	public void setRecordPerson(Person recordPerson) {
		this.recordPerson = recordPerson;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "item_inspection_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return itemInspectionId=IDGenerator.generate(this);
	}




}
