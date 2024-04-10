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
 *
 */

package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name=LocationCost.TABLE_NAME)
public class LocationCost extends ArahantBean implements java.io.Serializable
{
	public static final String TABLE_NAME = "location_cost";

	public static final String LOCATION_COST_ID = "locationCostId";
	public static final String DESCRIPTION = "description";
	public static final String COST = "locationCost";

	private String locationCostId;
	private String description;
	private double locationCost;

	@Column(name="description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name="location_cost")
	public double getLocationCost() {
		return locationCost;
	}

	public void setLocationCost(double locationCost) {
		this.locationCost = locationCost;
	}

	@Id
	@Column(name="location_cost_id")
	public String getLocationCostId() {
		return locationCostId;
	}

	public void setLocationCostId(String locationCostId) {
		this.locationCostId = locationCostId;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "location_cost_id";
	}

	@Override
	public String generateId() throws ArahantException {
		setLocationCostId(IDGenerator.generate(this));
		return getLocationCostId();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final LocationCost other = (LocationCost) obj;
		if ((this.locationCostId == null) ? (other.locationCostId != null) : !this.locationCostId.equals(other.locationCostId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 13 * hash + (this.locationCostId != null ? this.locationCostId.hashCode() : 0);
		return hash;
	}

}
