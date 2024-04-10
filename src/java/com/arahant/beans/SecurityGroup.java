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
@Table(name = "security_group")
public class SecurityGroup extends ArahantBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "security_group";
	// Fields
	private String securityGroupId;
	public static final String SECURITYGROUPID = "securityGroupId";
	private String id;
	public static final String ID = "id";
	private String name;
	public static final String NAME = "name";
	private Set<SecurityGroupHierarchy> securityGroupHierarchiesForParentSecurityGroupId = new HashSet<SecurityGroupHierarchy>(0);
	public static final String SECURITYGROUPHIERARCHIESFORPARENTSECURITYGROUPID = "securityGroupHierarchiesForParentSecurityGroupId";
	private Set<ProphetLogin> prophetLogins = new HashSet<ProphetLogin>(0);
	public static final String PROPHETLOGINS = "prophetLogins";
	private Set<SecurityGroupHierarchy> securityGroupHierarchiesForChildSecurityGroupId = new HashSet<SecurityGroupHierarchy>(0);
	public static final String SECURITYGROUPHIERARCHIESFORCHILDSECURITYGROUPID = "securityGroupHierarchiesForChildSecurityGroupId";
	private Set<RightsAssociation> rightsAssociations = new HashSet<RightsAssociation>(0);
	public static final String RIGHTSASSOCIATIONS = "rightsAssociations";
	private Set<ScreenGroup> screenGroups = new HashSet<ScreenGroup>();
	public static final String SCREEN_GROUPS = "screenGroups";
	private Set<SecurityGroup> securityGroups = new HashSet<SecurityGroup>();
	public static final String SECURITY_GROUPS = "securityGroups";
	private Set<SecurityGroup> allowedGroups = new HashSet<SecurityGroup>();
	public static final String ALLOWED_GROUPS = "screenGroups";

	// Constructors
	/**
	 * default constructor
	 */
	public SecurityGroup() {
	}

	/**
	 * minimal constructor
	 */
	public SecurityGroup(final String securityGroupId, final String id) {
		this.securityGroupId = securityGroupId;
		this.id = id;
	}

	// Property accessors
	@Id
	@Column(name = "security_group_id")
	public String getSecurityGroupId() {
		return this.securityGroupId;
	}

	public void setSecurityGroupId(final String securityGroupId) {
		this.securityGroupId = securityGroupId;
	}

	@Column(name = "id")
	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@OneToMany(mappedBy = SecurityGroupHierarchy.SECURITYGROUPBYPARENTSECURITYGROUPID, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<SecurityGroupHierarchy> getSecurityGroupHierarchiesForParentSecurityGroupId() {
		return this.securityGroupHierarchiesForParentSecurityGroupId;
	}

	public void setSecurityGroupHierarchiesForParentSecurityGroupId(
			final Set<SecurityGroupHierarchy> securityGroupHierarchiesForParentSecurityGroupId) {
		this.securityGroupHierarchiesForParentSecurityGroupId = securityGroupHierarchiesForParentSecurityGroupId;
	}

	/**
	 * @return Returns the prophetLogins.
	 */
	@OneToMany(mappedBy = ProphetLogin.SECURITYGROUP, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<ProphetLogin> getProphetLogins() {
		return prophetLogins;
	}

	/**
	 * @param prophetLogins The prophetLogins to set.
	 */
	public void setProphetLogins(final Set<ProphetLogin> prophetLogins) {
		this.prophetLogins = prophetLogins;
	}

	@OneToMany(mappedBy = SecurityGroupHierarchy.SECURITYGROUPBYCHILDSECURITYGROUPID, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<SecurityGroupHierarchy> getSecurityGroupHierarchiesForChildSecurityGroupId() {
		return this.securityGroupHierarchiesForChildSecurityGroupId;
	}

	public void setSecurityGroupHierarchiesForChildSecurityGroupId(
			final Set<SecurityGroupHierarchy> securityGroupHierarchiesForChildSecurityGroupId) {
		this.securityGroupHierarchiesForChildSecurityGroupId = securityGroupHierarchiesForChildSecurityGroupId;
	}

	@OneToMany(mappedBy = RightsAssociation.SECURITYGROUP, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<RightsAssociation> getRightsAssociations() {
		return this.rightsAssociations;
	}

	public void setRightsAssociations(final Set<RightsAssociation> rightsAssociations) {
		this.rightsAssociations = rightsAssociations;
	}

	@ManyToMany
	@JoinTable(name = "screen_group_access",
	joinColumns = {
		@JoinColumn(name = "security_group_id")},
	inverseJoinColumns = {
		@JoinColumn(name = "can_access_screen_group_id")})
	public Set<ScreenGroup> getScreenGroups() {
		return screenGroups;
	}

	public void setScreenGroups(Set<ScreenGroup> screenGroups) {
		this.screenGroups = screenGroups;
	}

	@ManyToMany
	@JoinTable(name = "security_group_access",
	joinColumns = {
		@JoinColumn(name = "security_group_id")},
	inverseJoinColumns = {
		@JoinColumn(name = "can_access_security_group_id")})
	public Set<SecurityGroup> getAllowedGroups() {
		return allowedGroups;
	}

	public void setAllowedGroups(Set<SecurityGroup> allowedGroups) {
		this.allowedGroups = allowedGroups;
	}

	@ManyToMany
	@JoinTable(name = "security_group_access",
	joinColumns = {
		@JoinColumn(name = "can_access_security_group_id")},
	inverseJoinColumns = {
		@JoinColumn(name = "security_group_id")})
	public Set<SecurityGroup> getSecurityGroups() {
		return securityGroups;
	}

	public void setSecurityGroups(Set<SecurityGroup> securityGroups) {
		this.securityGroups = securityGroups;
	}

	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#keyColumn()
	 */
	@Override
	public String keyColumn() {

		return "security_group_id";
	}

	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#tableName()
	 */
	@Override
	public String tableName() {

		return TABLE_NAME;
	}

	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#generateId()
	 */
	@Override
	public String generateId() throws ArahantException {
		setSecurityGroupId(IDGenerator.generate(this));
		return securityGroupId;
	}

	@Override
	public boolean equals(Object o) {
		if (securityGroupId == null && o == null)
			return true;
		if (securityGroupId != null && o instanceof SecurityGroup)
			return securityGroupId.equals(((SecurityGroup) o).getSecurityGroupId());

		return false;
	}

	@Override
	public int hashCode() {
		if (securityGroupId == null)
			return 0;
		return securityGroupId.hashCode();
	}
}
