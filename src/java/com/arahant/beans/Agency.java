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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = Agency.TABLE_NAME)
@PrimaryKeyJoinColumn(name = "agency_id")
public class Agency extends CompanyBase implements java.io.Serializable, ArahantSaveNotify {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	public static final String TABLE_NAME = "agency";
	// Fields
	private String agencyExternalId;
	public static final String AGENCY_EXTERNAL_ID = "agencyExternalId";

	public Agency() {
	}

	@Column(name = "agency_external_id")
	public String getAgencyExternalId() {
		return this.agencyExternalId;
	}

	public void setAgencyExternalId(final String agencyExternalId) {
		this.agencyExternalId = agencyExternalId;
	}

	@Override
	public boolean equals(Object o) {
		if (getOrgGroupId() == null && o == null)
			return true;
		if (getOrgGroupId() != null && o instanceof Agency)
			return getOrgGroupId().equals(((Agency) o).getOrgGroupId());

		return false;
	}

	@Override
	public int hashCode() {
		if (getOrgGroupId() == null)
			return 0;
		return getOrgGroupId().hashCode();
	}

	@Override
	public String notifyId() {
		return getOrgGroupId();
	}

	@Override
	public String notifyClassName() {
		return "Agency";
	}
}
