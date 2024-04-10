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
 *
 * Created on Feb 8, 2007
 *
 */
package com.arahant.services.main;

import com.arahant.services.TransmitReturnBase;

public class LoginReturn extends TransmitReturnBase  {
    private String personId;
	private String personFName;
	private String personLName;
	private String screen;
	private String screenTitle;
	private boolean superUser;
    private boolean hasMinimumPasswordRequirements=false;
	private boolean meetsMinimumPasswordRequirements=true;
	private int expiredPasswordOlderThan=0;
	private LoginReturnItem []company;
	private String userAgreement;
	private String uuid;
	private String softwareRevision;
	private boolean canSendEmail;   //  can this user send emails

    public LoginReturn() {
    }

	public String getUserAgreement() {
		return userAgreement;
	}

	public void setUserAgreement(String userAgreement) {
		this.userAgreement = userAgreement;
	}

	public boolean getSuperUser() {
		return superUser;
	}

	public void setSuperUser(final boolean superUser) {
		this.superUser = superUser;
	}

	public String getPersonFName() {
		return personFName;
	}

	public void setPersonFName(final String personFName) {
		this.personFName = personFName;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(final String personId) {
		this.personId = personId;
	}

	public String getPersonLName() {
		return personLName;
	}

	public void setPersonLName(final String personLName) {
		this.personLName = personLName;
	}

	public String getScreen() {
		return screen;
	}

	public void setScreen(final String screen) {
		this.screen = screen;
	}

	public String getScreenTitle() {
		return screenTitle;
	}

	public void setScreenTitle(final String screenTitle) {
		this.screenTitle = screenTitle;
	}

    public boolean isHasMinimumPasswordRequirements() {
        return hasMinimumPasswordRequirements;
    }

    public void setHasMinimumPasswordRequirements(boolean hasMinimumPasswordRequirements) {
        this.hasMinimumPasswordRequirements = hasMinimumPasswordRequirements;
    }

	public boolean isMeetsMinimumPasswordRequirements() {
		return meetsMinimumPasswordRequirements;
	}

	public void setMeetsMinimumPasswordRequirements(boolean meetsMinimumPasswordRequirements) {
		this.meetsMinimumPasswordRequirements = meetsMinimumPasswordRequirements;
	}

    public int getExpiredPasswordOlderThan() {
        return expiredPasswordOlderThan;
    }

    public void setExpiredPasswordOlderThan(int expiredPasswordOlderThan) {
        this.expiredPasswordOlderThan = expiredPasswordOlderThan;
    }

	public LoginReturnItem[] getCompany() {
		return company;
	}

	public void setCompany(LoginReturnItem[] company) {
		this.company = company;
	}

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSoftwareRevision() {
        return softwareRevision;
    }

    public void setSoftwareRevision(String softwareRevision) {
        this.softwareRevision = softwareRevision;
    }

	public boolean isCanSendEmail() {
		return canSendEmail;
	}

	public void setCanSendEmail(boolean canSendEmail) {
		this.canSendEmail = canSendEmail;
	}
}

	
