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
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;


@Entity
@Table(name = ProjectType.TABLE_NAME)
public class ProjectType extends ArahantBean implements java.io.Serializable, ArahantSaveNotify {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "project_type";
	public static final String SCOPE = "scope";
	public static char SCOPE_GLOBAL = 'G';
	public static char SCOPE_INTERNAL = 'I';
	// Fields
	private String projectTypeId;
	public static final String PROJECTTYPEID = "projectTypeId";
	private String code;
	public static final String CODE = "code";
	private String description;
	public static final String DESCRIPTION = "description";
	private Set<StandardProject> standardProjects = new HashSet<StandardProject>(0);
	public static final String STANDARDPROJECTS = "standardProjects";
	private Set<Project> projects = new HashSet<Project>(0);
	public static final String PROJECTS = "projects";
	private Set<RouteTypeAssoc> routeTypeAssociations = new HashSet<RouteTypeAssoc>(0);
	public static final String ROUTETYPEASSOCIATIONS = "routeTypeAssociations";
	private char scope;
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

	// Constructors
	/**
	 * default constructor
	 */
	public ProjectType() {
	}

	@Column(name = "scope")
	public char getScope() {
		return scope;
	}

	public void setScope(char scope) {
		this.scope = scope;
	}

	// Property accessors
	@Id
	@Column(name = "project_type_id")
	public String getProjectTypeId() {
		return this.projectTypeId;
	}

	public void setProjectTypeId(final String projectTypeId) {
		this.projectTypeId = projectTypeId;
	}

	@Column(name = "code")
	public String getCode() {
		return this.code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@OneToMany(mappedBy = StandardProject.PROJECTTYPE, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<StandardProject> getStandardProjects() {
		return this.standardProjects;
	}

	public void setStandardProjects(final Set<StandardProject> standardProjects) {
		this.standardProjects = standardProjects;
	}

	@OneToMany(mappedBy = Project.PROJECTTYPE, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<Project> getProjects() {
		return this.projects;
	}

	public void setProjects(final Set<Project> projects) {
		this.projects = projects;
	}

	@Override
	public String keyColumn() {
		return "project_type_id";
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String generateId() throws ArahantException {
		setProjectTypeId(IDGenerator.generate(this));
		return projectTypeId;
	}

	/**
	 * @return Returns the routeTypeAssociations.
	 */
	@OneToMany(mappedBy = RouteTypeAssoc.PROJECT_TYPE, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<RouteTypeAssoc> getRouteTypeAssociations() {
		return routeTypeAssociations;
	}

	/**
	 * @param routeTypeAssociations The routeTypeAssociations to set.
	 */
	public void setRouteTypeAssociations(final Set<RouteTypeAssoc> routeTypeAssociations) {
		this.routeTypeAssociations = routeTypeAssociations;
	}

	@Override
	public boolean equals(Object o) {
		if (projectTypeId == null && o == null)
			return true;
		if (projectTypeId != null && o instanceof ProjectType)
			return projectTypeId.equals(((ProjectType) o).getProjectTypeId());

		return false;
	}

	@Override
	public int hashCode() {
		if (projectTypeId == null)
			return 0;
		return projectTypeId.hashCode();
	}

	@Override
	public String notifyId() {
		return projectTypeId;
	}

	@Override
	public String notifyClassName() {
		return "ProjectType";
	}

	@Override
	public String toString() {
		return this.description;
	}
}
