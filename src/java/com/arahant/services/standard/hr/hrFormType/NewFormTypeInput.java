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
package com.arahant.services.standard.hr.hrFormType;

import com.arahant.annotation.Validation;

import com.arahant.business.BFormType;
import com.arahant.services.TransmitInputBase;

/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewFormTypeInput extends TransmitInputBase {
    @Validation(table = "form_type", column = "form_code", required = true)
    private String code;
    @Validation(table = "form_type", column = "description", required = true)
    private String description;
    @Validation(required = true)
    private boolean applyToAll;
	@Validation (required=false, type="date")
	private int lastActiveDate;
    private String internal;

    void setData(final BFormType bc) {
        bc.setCode(code);
        bc.setDescription(description);
        bc.setFormType('E');
		bc.setLastActiveDate(lastActiveDate);
        bc.setInternal(internal.charAt(0));
    }

	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}
	
    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public boolean isApplyToAll() {
        return applyToAll;
    }

    public void setApplyToAll(boolean applyToAll) {
        this.applyToAll = applyToAll;
    }

    public String getInternal() {
        return internal;
    }

    public void setInternal(String internal) {
        this.internal = internal;
    }
}

	
