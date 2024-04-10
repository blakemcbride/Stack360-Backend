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
 * Created on Feb 4, 2007
 * 
 */
package com.arahant.services;

import com.arahant.services.main.UserCache;
import com.arahant.utils.SearchConstants;


public class TransmitInputBase {
	protected String user;              // ignored except for login
	protected String password;          // ignored except for login
	protected String contextCompanyId;  // Shouldn't be used except for login.  Security risk.
	protected boolean sendValidations = false;  // only used in Flash
	protected String frontEndType;      //  I don't think this is used
	protected String uuid;             // main authentication after login
	protected String _service;   // for informational purposes only
	protected String _method;    // for informational purposes only
	protected String loginType;  // NORMAL, WORKER, or APPLICANT

	public TransmitInputBase() {
	}

	public TransmitInputBase(String user, String password, String uuid, String contextCompanyId, boolean sendValidations, String frontEndType, String loginType) {
		this.user = user;
		this.password = password;
		this.uuid = uuid;
		this.contextCompanyId = contextCompanyId;
		this.sendValidations = sendValidations;
		this.frontEndType = frontEndType == null ? "Flash" : frontEndType;
		this.loginType = loginType;
	}

	public boolean getSendValidations() {
		return sendValidations;
	}

	public void setSendValidations(boolean sendValidations) {
		this.sendValidations = sendValidations;
	}
	/**
	 * @return Returns the login.
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @param login The login to set.
	 */
	public void setUser(final String login) {
		this.user = login;
	}
	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password The password to set.
	 */
	public void setPassword(final String password) {
		this.password = password;
	}

	public String getFrontEndType() {
		return frontEndType;
	}

	public void setFrontEndType(String frontEndType) {
		this.frontEndType = frontEndType;
	}

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    protected String modifyForSearch(String str, final int searchType) {
        if (str == null)
            str = "";
        str = str.trim();
        switch (searchType) {
            case SearchConstants.ENDS_WITH_VALUE:
                return "%" + str;
            case SearchConstants.STARTS_WITH_VALUE:
                return str + "%";
            case SearchConstants.CONTAINS_VALUE:
                return "%" + str + "%";
            case SearchConstants.ALL_VALUE:
                return "%";
            case SearchConstants.EXACT_MATCH_VALUE:
                return str;
            default:
                return str;
        }
    }
	
	protected boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

	public String getContextCompanyId() {
		return contextCompanyId;
	}

	public void setContextCompanyId(String companyId) {
		this.contextCompanyId = companyId;
	}

	public String get_service() {
		return _service;
	}

	public void set_service(String _service) {
		this._service = _service;
	}

	public String get_method() {
		return _method;
	}

	public void set_method(String _method) {
		this._method = _method;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
}

	
