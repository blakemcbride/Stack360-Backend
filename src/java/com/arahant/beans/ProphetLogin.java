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
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "prophet_login")
public class ProphetLogin extends ArahantBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "prophet_login";
	// Fields    
	private String personId;
	public static final String PERSONID = "personId";
	private Person person;
	public static final String PERSON = "person";
	private ScreenGroup screenGroup;
	public static final String SCREENGROUP = "screenGroup";
	private SecurityGroup securityGroup;
	public static final String SECURITYGROUP = "securityGroup";
	private Character canLogin;
	public static final String CANLOGIN = "canLogin";
	private String userLogin;
	public static final String USERLOGIN = "userLogin";
	private String userPassword;
	public static final String USERPASSWORD = "userPassword";
	private int passwordEffectiveDate;
	private ScreenGroup noCompanyScreenGroup;
	private Date whenCreated = new Date();
	private String authenticationCode;
	private short numberOfResends;
	private short numberOfAuthentications;
	private Date resetPasswordDate;
	private char userType = 'R';

	// Constructors
	/**
	 * default constructor
	 */
	public ProphetLogin() {
	}

	// Property accessors
	@Id
	@Column(name = "person_id")
	public String getPersonId() {
		return this.personId;
	}

	public void setPersonId(final String personId) {
		this.personId = personId;
	}

	@Column(name = "password_effective_date")
	public int getPasswordEffectiveDate() {
		return passwordEffectiveDate;
	}

	public void setPasswordEffectiveDate(int passwordEffectiveDate) {
		this.passwordEffectiveDate = passwordEffectiveDate;
	}

	@OneToOne
	@JoinColumn(name = "person_id", insertable = false, updatable = false)
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(final Person person) {
		this.person = person;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "no_company_screen_group_id")
	public ScreenGroup getNoCompanyScreenGroup() {
		return noCompanyScreenGroup;
	}

	public void setNoCompanyScreenGroup(ScreenGroup noCompanyScreenGroup) {
		this.noCompanyScreenGroup = noCompanyScreenGroup;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "screen_group_id")
	public ScreenGroup getScreenGroup() {
		return this.screenGroup;
	}

	public void setScreenGroup(final ScreenGroup screenGroup) {
		this.screenGroup = screenGroup;
	}

	@Column(name = "can_login")
	public Character getCanLogin() {
		return this.canLogin;
	}

	public void setCanLogin(final Character canLogin) {
		this.canLogin = canLogin;
	}

	@Column(name = "user_login")
	public String getUserLogin() {
		return this.userLogin;
	}

	public void setUserLogin(final String userLogin) {
		this.userLogin = userLogin;
	}

	/**
	 * This method return the encrypted password.
	 *
	 * @return the encrypted password
	 */
	@Column(name = "user_password")
	public String getUserPassword() {
		return this.userPassword;
	}

	/**
	 * Sets the user login password. It is assumed that the value being passed
	 * in is already encrypted.
	 *
	 * @param userPassword
	 */
	public void setUserPassword(final String userPassword) {
		this.userPassword = userPassword;
	}

	@Column(name = "when_created")
	public Date getWhenCreated() {
		return whenCreated;
	}

	public void setWhenCreated(Date whenCreated) {
		this.whenCreated = whenCreated;
	}

	@Column(name = "authentication_code")
	public String getAuthenticationCode() {
		return authenticationCode;
	}

	public void setAuthenticationCode(String authenticationCode) {
		this.authenticationCode = authenticationCode;
	}

	@Column(name = "number_of_resends")
	public short getNumberOfResends() {
		return numberOfResends;
	}

	public void setNumberOfResends(short numberOfResends) {
		this.numberOfResends = numberOfResends;
	}

	@Column(name = "number_of_authentications")
	public short getNumberOfAuthentications() {
		return numberOfAuthentications;
	}

	public void setNumberOfAuthentications(short numberOfAuthentications) {
		this.numberOfAuthentications = numberOfAuthentications;
	}

	@Column(name = "reset_password_date")
	public Date getResetPasswordDate() {
		return resetPasswordDate;
	}

	public void setResetPasswordDate(Date resetPasswordDate) {
		this.resetPasswordDate = resetPasswordDate;
	}

	@Column(name = "user_type")
	public char getUserType() {
		return userType;
	}

	public void setUserType(char userType) {
		this.userType = userType;
	}

	@Override
	public String keyColumn() {
		return "person_id";
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String generateId() throws ArahantException {
		setPersonId(person.getPersonId());
		return personId;
	}

	/**
	 * @return Returns the securityGroup.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "security_group_id")
	public SecurityGroup getSecurityGroup() {
		return securityGroup;
	}

	/**
	 * @param securityGroup The securityGroup to set.
	 */
	public void setSecurityGroup(final SecurityGroup securityGroup) {
		this.securityGroup = securityGroup;
	}

	@Override
	public boolean equals(Object o) {
		if (personId == null && o == null)
			return true;
		if (personId != null && o instanceof ProphetLogin)
			return personId.equals(((ProphetLogin) o).getPersonId());

		return false;
	}

	@Override
	public int hashCode() {
		if (personId == null)
			return 0;
		return personId.hashCode();
	}
}
