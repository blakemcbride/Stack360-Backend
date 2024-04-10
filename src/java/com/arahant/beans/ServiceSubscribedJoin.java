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

@Entity
@Table(name = ServiceSubscribedJoin.TABLE_NAME)
public class ServiceSubscribedJoin extends ArahantBean implements Serializable {

	public static final String TABLE_NAME = "service_subscribed_join";
	public static final String COMPANY = "company";
	private String serviceJoinId;
	public static final String FIRSTDATE = "firstDate";
	private int firstDate;
	public static final String LASTDATE = "lastDate";
	private int lastDate;
	private CompanyDetail company;
	public static final String SERVICE = "service";
	private ServiceSubscribed service;
	private String externalId;
	public static final String EXTERNAL_ID = "externalId";

	public ServiceSubscribedJoin() {
	}

	@Column(name = "external_id")
	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "service_join_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return serviceJoinId = IDGenerator.generate(this);
	}

	@ManyToOne
	@JoinColumn(name = "org_group_id")
	public CompanyDetail getCompany() {
		return company;
	}

	public void setCompany(CompanyDetail company) {
		this.company = company;
	}

	@ManyToOne
	@JoinColumn(name = "service_id")
	public ServiceSubscribed getService() {
		return service;
	}

	public void setService(ServiceSubscribed service) {
		this.service = service;
	}

	@Id
	@Column(name = "service_join_id")
	public String getServiceJoinId() {
		return serviceJoinId;
	}

	public void setServiceJoinId(String serviceJoinId) {
		this.serviceJoinId = serviceJoinId;
	}

	@Column(name = "first_date")
	public int getFirstDate() {
		return firstDate;
	}

	public void setFirstDate(int firstDate) {
		this.firstDate = firstDate;
	}

	@Column(name = "last_date")
	public int getLastDate() {
		return lastDate;
	}

	public void setLastDate(int lastDate) {
		this.lastDate = lastDate;
	}
}
