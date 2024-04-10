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
 */
package com.arahant.services.standard.hr.hrEmployee;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;
import com.arahant.business.BEmployee;
import com.arahant.business.BPerson;
import com.arahant.exceptions.ArahantException;

public class SavePersonLoginInput extends TransmitInputBase {

	@Validation(required = true)
    private String personId;
	@Validation(table = "prophet_login", column = "user_login", required = false)
    private String login;
    @Validation(table = "prophet_login", column = "user_password", required = false)
    private String empPassword;
    @Validation(required = false)
    private boolean canLogin;
    @Validation(required = false)
    private String screenGroupId;
    @Validation(required = false)
    private String securityGroupId;
	@Validation(required = false)
	private String noCompanyScreenGroupId;

	void makeEmployee(final BEmployee emp) throws ArahantException
	{
		emp.setUserLogin(login);
		if (empPassword != null && !empPassword.isEmpty() && empPassword.equals(LoadPersonLoginReturn.fakePassword))
			empPassword = null;  // don't change
        emp.setUserPassword(empPassword, canLogin);
        emp.setScreenGroupId(screenGroupId);
        emp.setSecurityGroupId(securityGroupId);
		emp.setNoCompanyScreenGroupId(noCompanyScreenGroupId);
	}

	void makePerson(BPerson per) throws ArahantException
	{
		per.setUserLogin(login);
		if (empPassword != null && !empPassword.isEmpty() && empPassword.equals(LoadPersonLoginReturn.fakePassword))
			empPassword = null;  // don't change
        per.setUserPassword(empPassword, canLogin);
        per.setScreenGroupId(screenGroupId);
        per.setSecurityGroupId(securityGroupId);
	}

	public boolean isCanLogin() {
		return canLogin;
	}

	public void setCanLogin(boolean canLogin) {
		this.canLogin = canLogin;
	}

	public String getEmpPassword() {
		return empPassword;
	}

	public void setEmpPassword(String empPassword) {
		this.empPassword = empPassword;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getNoCompanyScreenGroupId() {
		return noCompanyScreenGroupId;
	}

	public void setNoCompanyScreenGroupId(String noCompanyScreenGroupId) {
		this.noCompanyScreenGroupId = noCompanyScreenGroupId;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getScreenGroupId() {
		return screenGroupId;
	}

	public void setScreenGroupId(String screenGroupId) {
		this.screenGroupId = screenGroupId;
	}

	public String getSecurityGroupId() {
		return securityGroupId;
	}

	public void setSecurityGroupId(String securityGroupId) {
		this.securityGroupId = securityGroupId;
	}
}

	
