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
package com.arahant.services.standard.hr.dynamicReport;

import com.arahant.business.BVendorCompany;

/**
 * 
 *
 *
 */
public class SearchVendorsReturnItem {

	public SearchVendorsReturnItem() {
		;
	}

	SearchVendorsReturnItem(BVendorCompany bc) {

		id = bc.getOrgGroupId();
		name = bc.getName();
		ediFileStatus = bc.getEdiFileStatus();
		ediFileType = bc.getEdiFileType();

	}
	private String id;
	private String name;
	private String ediFileType;
	private String ediFileStatus;

	public String getEdiFileStatus() {
		return ediFileStatus;
	}

	public void setEdiFileStatus(String ediFileStatus) {
		this.ediFileStatus = ediFileStatus;
	}

	public String getEdiFileType() {
		return ediFileType;
	}

	public void setEdiFileType(String ediFileType) {
		this.ediFileType = ediFileType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

	
