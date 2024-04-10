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

package com.arahant.services.standard.hr.hrEmployee;

import com.arahant.business.BPerson;
import com.arahant.services.TransmitReturnBase;

public class LoadPersonLoginReturn extends TransmitReturnBase {

	final static String fakePassword = "***********";
	private String login;
	private String password;
	private boolean canLogin;

	public LoadPersonLoginReturn() {
	}

	public void setData(BPerson person)
	{
		login = person.getUserLogin();
        password = person.getUserPassword();
        if (password != null && !password.isEmpty())
        	password = fakePassword;  //  don't send the real password!
        canLogin = person.getCanLogin() == 'Y';
	}

	public boolean isCanLogin() {
		return canLogin;
	}

	public void setCanLogin(boolean canLogin) {
		this.canLogin = canLogin;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}

	
