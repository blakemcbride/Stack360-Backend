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
 * Created on November 24, 2016
*/

package com.arahant.services.standard.hr.billingRateSetup;

import com.arahant.annotation.Validation;
import com.arahant.business.BRateType;
import com.arahant.services.TransmitInputBase;

public class SaveInput extends TransmitInputBase {

	@Validation (required=true)
	private String id;
	private String type;
	private String code;
	@Validation (table="rate_type",column="description",required=true)
	private String description;

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	void setData(final BRateType x) {
		x.setRateType(type.charAt(0));
		x.setCode(code);
		x.setDescription(description);
	}
}

	
