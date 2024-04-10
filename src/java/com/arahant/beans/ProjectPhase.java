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
@Table(name = "project_phase")
public class ProjectPhase extends ArahantBean implements Serializable {

	public static final String CODE = "code";
	public static final String DESCRIPTION = "description";
	public static String[] SECURITY_CATEGORIES = {"Estimates", "Development", "QA", "Implementation"};
	public static final short ESTIMATE = 0;
	public static final short DEVELOPMENT = 1;
	public static final short QA = 2;
	public static final short IMPLEMENTATION = 3;
	private String projectPhaseId;
	private String code;
	private String description;
	private Set<RouteStop> routeStops = new HashSet<RouteStop>();
	private short securityLevel;
	public static final String COMPANY_ID = "companyId";
	private String companyId;
	public static final String COMPANY = "company";
	private CompanyDetail company;
	public static final String LAST_ACTIVE_DATE = "lastActiveDate";
	private int lastActiveDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	public CompanyDetail getCompany() {
		return company;
	}

	public void setCompany(CompanyDetail company) {
		this.company = company;
	}

	@Column(name = "company_id", insertable = false, updatable = false)
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@Column(name = "last_active_date")
	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	@Column(name = "security_level")
	public short getSecurityLevel() {
		return securityLevel;
	}

	public void setSecurityLevel(short securityLevel) {
		this.securityLevel = securityLevel;
	}

	@Override
	public String tableName() {
		return "project_phase";
	}

	@Override
	public String keyColumn() {
		return "project_phase_id";
	}

	@Override
	public String generateId() throws ArahantException {
		projectPhaseId = IDGenerator.generate(this);
		return projectPhaseId;
	}

	@Column(name = "code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Id
	@Column(name = "project_phase_id")
	public String getProjectPhaseId() {
		return projectPhaseId;
	}

	@OneToMany(mappedBy = RouteStop.PROJECT_PHASE, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<RouteStop> getRouteStops() {
		return routeStops;
	}

	public void setRouteStops(Set<RouteStop> routeStops) {
		this.routeStops = routeStops;
	}

	public void setProjectPhaseId(String projectPhaseId) {
		this.projectPhaseId = projectPhaseId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ProjectPhase other = (ProjectPhase) obj;
		if (this.projectPhaseId != other.getProjectPhaseId() && (this.projectPhaseId == null || !this.projectPhaseId.equals(other.getProjectPhaseId())))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		if (projectPhaseId == null)
			return 0;
		return projectPhaseId.hashCode();
	}

	@Override
	public String toString() {
		return this.description;
	}
}
