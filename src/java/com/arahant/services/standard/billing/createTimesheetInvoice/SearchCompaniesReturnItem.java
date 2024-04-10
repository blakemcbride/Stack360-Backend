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

package com.arahant.services.standard.billing.createTimesheetInvoice;

import com.arahant.business.BCompanyBase;
import com.arahant.exceptions.ArahantException;
import org.kissweb.database.Record;

import java.sql.SQLException;

public class SearchCompaniesReturnItem {

	private String orgGroupId;
	private String name;
	private String identifier, billable, type;

	/**
	 * @return Returns the billable.
	 */
	public String getBillable() {
		return billable;
	}

	/**
	 * @param billable The billable to set.
	 */
	public void setBillable(final String billable) {
		this.billable = billable;
	}

	/**
	 * @return Returns the identifier.
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(final String type) {
		this.type = type;
	}

	public SearchCompaniesReturnItem() {
	}

	/**
	 * @param rec
	 */
	SearchCompaniesReturnItem(final Record rec, int date) throws SQLException {
		orgGroupId = rec.getString("org_group_id");
		final int recentBillable = rec.getInt("recent_billable");
		switch (recentBillable) {
			case 1:
				name = "** " + rec.getString("group_name");
				break;
			case 2:
				name = "* " + rec.getString("group_name");
				break;
			case 0:
			default:
				name = rec.getString("group_name");
				break;
		}
		billable = recentBillable == 0 ? "no" : "Yes";
		identifier = rec.getString(("external_id"));
		switch (rec.getInt("org_group_type")) {
			case 1:
				type = "Company";
				break;
			case 2:
				type = "Client";
				break;
			case 4:
				type = "Vendor";
				break;
			case 8:
				type = "Prospect";
				break;
			case 16:
				type = "Agency";
				break;
			default:
				throw new ArahantException("Unknown company type");
		}
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return Returns the orgGroupId.
	 */
	public String getOrgGroupId() {
		return orgGroupId;
	}

	/**
	 * @param orgGroupId The orgGroupId to set.
	 */
	public void setOrgGroupId(final String orgGroupId) {
		this.orgGroupId = orgGroupId;
	}
}
