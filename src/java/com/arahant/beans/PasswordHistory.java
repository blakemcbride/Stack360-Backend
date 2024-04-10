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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 *   password_history_id character(16) NOT NULL,
  person_id character(16) NOT NULL,
  date_retired integer NOT NULL,
  user_password character varying(48) NOT NULL,
 */
@Entity
@Table(name=PasswordHistory.TABLE_NAME)
public class PasswordHistory extends ArahantBean implements Serializable {
	public static final String TABLE_NAME="password_history";
	public static final String PERSON="person";
	public static final String PASSWORD="userPassword";
	public static final String RETIRED_DATE="dateRetired";

	private String passwordHistoryId;
	private Person person;
	private int dateRetired;
	private String userPassword;

	@Column(name="date_retired")
	public int getDateRetired() {
		return dateRetired;
	}

	public void setDateRetired(int dateRetired) {
		this.dateRetired = dateRetired;
	}

	@Id
	@Column(name="password_history_id")
	public String getPasswordHistoryId() {
		return passwordHistoryId;
	}

	public void setPasswordHistoryId(String passwordHistoryId) {
		this.passwordHistoryId = passwordHistoryId;
	}

	@ManyToOne
	@JoinColumn(name="person_id")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Column(name="user_password")
	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	
	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "password_history_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return passwordHistoryId=IDGenerator.generate(this);
	}


}
