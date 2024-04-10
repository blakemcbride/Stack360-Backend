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
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "service_subscribed")
public class ServiceSubscribed extends Setup implements Serializable {

	public static final String TABLE_NAME = "service_subscribed";
	private String serviceId;
	public static final String SERVICEID = "serviceId";
	private String serviceName;
	public static final String SERVICENAME = "serviceName";
	private String description;
	public static final String DESCRIPTION = "description";
	public static final String SUBSCRIBED_JOINS = "subscribedJoins";
	private Set<ServiceSubscribedJoin> subscribedJoins = new HashSet<ServiceSubscribedJoin>();
	private short interfaceCode = 0;
	public static final String INTERFACE_CODE = "interfaceCode";
	public static final short NO_INTERFACE = 0;
	public static final short COBRA_GUARD_INTERFACE = 1;
	public static final String INTERFACES[] = {"None", "Cobra Guard"};

	public ServiceSubscribed() {
	}

	@Column(name = "interface_code")
	public short getInterfaceCode() {
		return interfaceCode;
	}

	public void setInterfaceCode(short interface_code) {
		this.interfaceCode = interface_code;
	}

	@Id
	@Column(name = "service_id")
	public String getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(final String serviceId) {
		this.serviceId = serviceId;
	}

	@OneToMany
	@JoinColumn(name = "service_id")
	public Set<ServiceSubscribedJoin> getSubscribedJoins() {
		return subscribedJoins;
	}

	public void setSubscribedJoins(Set<ServiceSubscribedJoin> subscribedJoins) {
		this.subscribedJoins = subscribedJoins;
	}

	@Column(name = "service_name")
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "first_active_date")
	@Override
	public int getFirstActiveDate() {
		return firstActiveDate;
	}

	@Column(name = "last_active_date")
	@Override
	public int getLastActiveDate() {
		return lastActiveDate;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "service_id";
	}

	@Override
	public String generateId() throws ArahantException {
		setServiceId(IDGenerator.generate(this));
		return serviceId;
	}

	@Override
	@ManyToOne
	@JoinColumn(name = "org_group_id")
	public OrgGroup getOrgGroup() {
		return orgGroup;
	}
}
