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
import javax.persistence.*;

/**
 *
 */
@Entity
@Table(name = ProphetLoginOverride.TABLE_NAME)
public class ProphetLoginOverride extends ArahantBean implements Serializable{

	public static final String TABLE_NAME="prophet_login_exception";

	public static final String ID="loginExceptionId";
	public static final String PERSON="person";
	public static final String COMPANY="company";
	public static final String PERSON_ID = "personId";
	public static final String SCREEN_GROUP_ID = "screenGroupId";
	public static final String SECURITY_GROUP_ID = "securityGroupId";

	private String loginExceptionId;
	private String screenGroupId;
	private String securityGroupId;
	private String personId;
	private Person person;
	private CompanyDetail company;
	private ScreenGroup screenGroup;
	private SecurityGroup securityGroup;

	@Id
	@Column(name="login_exception_id")
	public String getLoginExceptionId() {
		return loginExceptionId;
	}

	public void setLoginExceptionId(String loginExceptionId) {
		this.loginExceptionId = loginExceptionId;
	}


	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="company_id")
	public CompanyDetail getCompany() {
		return company;
	}

	public void setCompany(CompanyDetail company) {
		this.company = company;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="person_id")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Column(name="person_id", updatable=false, insertable=false)
	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="screen_group_id")
	public ScreenGroup getScreenGroup() {
		return screenGroup;
	}

	public void setScreenGroup(ScreenGroup screenGroup) {
		this.screenGroup = screenGroup;
	}

	@Column(name="screen_group_id", updatable=false, insertable=false)
	public String getScreenGroupId() {
		return screenGroupId;
	}

	public void setScreenGroupId(String screenGroupId) {
		this.screenGroupId = screenGroupId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="security_group_id")
	public SecurityGroup getSecurityGroup() {
		return securityGroup;
	}

	public void setSecurityGroup(SecurityGroup securityGroup) {
		this.securityGroup = securityGroup;
	}

	@Column(name="security_group_id", updatable=false, insertable=false)
	public String getSecurityGroupId() {
		return securityGroupId;
	}

	public void setSecurityGroupId(String securityGroupId) {
		this.securityGroupId = securityGroupId;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "login_exception_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return loginExceptionId=IDGenerator.generate(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ProphetLoginOverride other = (ProphetLoginOverride) obj;
		if ((this.loginExceptionId == null) ? (other.loginExceptionId != null) : !this.loginExceptionId.equals(other.loginExceptionId)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 97 * hash + (this.loginExceptionId != null ? this.loginExceptionId.hashCode() : 0);
		return hash;
	}

	

}
