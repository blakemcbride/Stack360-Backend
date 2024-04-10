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

import javax.persistence.*;

import com.arahant.exceptions.ArahantException;


@Entity
@Table(name="login_log")
public class LoginLog extends ArahantBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "login_log";

	// Fields    

	private LoginLogId id;

	public static final String ID = "id";

	private Character successful;

	public static final String SUCCESSFUL = "successful";

	private String personId;

	private java.util.Date ltime;
	
	public static final String PERSONID = "personId";

	public static final String DATE = "ltime";

	public static final String LOGIN_NAME = LoginLogId.LOGNAME;

	private String addressIp;
	private String addressUrl;
	// Constructors

	/** default constructor */
	public LoginLog() {
	}

	@Column(name="address_ip")
	public String getAddressIp() {
		return addressIp;
	}

	public void setAddressIp(String addressIp) {
		this.addressIp = addressIp;
	}

	@Column(name="address_url")
	public String getAddressUrl() {
		return addressUrl;
	}

	public void setAddressUrl(String addressUrl) {
		this.addressUrl = addressUrl;
	}

	// Property accessors

	@Id
	public LoginLogId getId() {
		return this.id;
	}

	public void setId(final LoginLogId id) {
		this.id = id;
	}

	@Column (name="successful")
	public Character getSuccessful() {
		return this.successful;
	}

	public void setSuccessful(final Character successful) {
		this.successful = successful;
	}

	@Column (name="person_id")
	public String getPersonId() {
		return this.personId;
	}

	public void setPersonId(final String personId) {
		this.personId = personId;
	}

	@Override
	public String keyColumn() {
		return "";
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String generateId() throws ArahantException {
		throw new ArahantException("Can't generate this ID");	
	}

	@Column (name="ltime",insertable=false,updatable=false)
	@Temporal(TemporalType.TIMESTAMP)
	public java.util.Date getLtime() {
		return ltime;
	}

	public void setLtime(java.util.Date ltime) {
		this.ltime = ltime;
	}
	
	@Column (name="log_name",insertable=false,updatable=false)
	public String getLogName() {
		return id.getLogName();
	}

	public void setLogName(final String logName) {
		if (id==null)
			id=new LoginLogId();
		id.setLogName(logName);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (id==null && o==null)
			return true;
		if (id!=null && o instanceof LoginLog)
			return id.equals(((LoginLog)o).getId());
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		if (id==null)
			return 0;
		return id.hashCode();
	}
}
