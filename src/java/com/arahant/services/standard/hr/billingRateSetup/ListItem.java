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

import com.arahant.business.BRateType;

public class ListItem {

	private String rateTypeId;
	private String type;
	private String code;
	private String description;

	public String getRateTypeId() {
		return rateTypeId;
	}

	public void setRateTypeId(final String rateTypeId) {
		this.rateTypeId = rateTypeId;
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

	ListItem(final BRateType account) {
		rateTypeId = account.getRateTypeId();
		type = account.getRateType().getRateType() + "";
		code = account.getCode().trim();
		description = account.getDescription();
	}
}

	
