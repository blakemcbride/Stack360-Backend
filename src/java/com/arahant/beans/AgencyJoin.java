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

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * Arahant
 */
@Entity
@Table(name = AgencyJoin.TABLE_NAME)
public class AgencyJoin extends ArahantBean implements Serializable {

	public static final String TABLE_NAME = "agency_join";
	public static final String AGENCY = "agency";
	public static final String COMPANY = "company";
	private String agencyJoinId;
	private Agency agency;
	private CompanyDetail company;
	
	public AgencyJoin() { }

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "agency_join_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return agencyJoinId = IDGenerator.generate(this);
	}

	@ManyToOne
	@JoinColumn(name = "agency_id")
	public Agency getAgency() {
		return agency;
	}

	public void setAgency(Agency agency) {
		this.agency = agency;
	}

	@Id
	@Column(name = "agency_join_id")
	public String getAgencyJoinId() {
		return agencyJoinId;
	}

	public void setAgencyJoinId(String agencyJoinId) {
		this.agencyJoinId = agencyJoinId;
	}

	@ManyToOne
	@JoinColumn(name = "company_id")
	public CompanyDetail getCompany() {
		return company;
	}

	public void setCompany(CompanyDetail company) {
		this.company = company;
	}
}
