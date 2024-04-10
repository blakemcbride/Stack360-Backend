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
import javax.persistence.*;

@Entity
@Table(name = "security_group_hierarchy")
public class SecurityGroupHierarchy extends ArahantBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "security_group_hierarchy";
	// Fields    
	private SecurityGroupHierarchyId id;
	public static final String ID = "id";
	private SecurityGroup securityGroupByParentSecurityGroupId;
	public static final String SECURITYGROUPBYPARENTSECURITYGROUPID = "securityGroupByParentSecurityGroupId";
	private SecurityGroup securityGroupByChildSecurityGroupId;
	public static final String SECURITYGROUPBYCHILDSECURITYGROUPID = "securityGroupByChildSecurityGroupId";

	// Constructors
	/**
	 * default constructor
	 */
	public SecurityGroupHierarchy() {
	}

	// Property accessors
	@Id
	public SecurityGroupHierarchyId getId() {
		return this.id;
	}

	public void setId(final SecurityGroupHierarchyId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_security_group_id", insertable = false, updatable = false)
	public SecurityGroup getSecurityGroupByParentSecurityGroupId() {
		return this.securityGroupByParentSecurityGroupId;
	}

	public void setSecurityGroupByParentSecurityGroupId(
			final SecurityGroup securityGroupByParentSecurityGroupId) {
		this.securityGroupByParentSecurityGroupId = securityGroupByParentSecurityGroupId;
		if (id == null)
			id = new SecurityGroupHierarchyId();
		id.setParentSecurityGroupId(securityGroupByParentSecurityGroupId.getSecurityGroupId());
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "child_security_group_id", insertable = false, updatable = false)
	public SecurityGroup getSecurityGroupByChildSecurityGroupId() {
		return this.securityGroupByChildSecurityGroupId;
	}

	public void setSecurityGroupByChildSecurityGroupId(
			final SecurityGroup securityGroupByChildSecurityGroupId) {
		if (id == null)
			id = new SecurityGroupHierarchyId();
		this.securityGroupByChildSecurityGroupId = securityGroupByChildSecurityGroupId;
		id.setChildSecurityGroupId(securityGroupByChildSecurityGroupId.getSecurityGroupId());
	}

	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#keyColumn()
	 */
	@Override
	public String keyColumn() {
		return "";
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
		throw new ArahantException("Can't generate like this.");
	}

	@Override
	public boolean equals(Object o) {
		if (id == null && o == null)
			return true;
		if (id != null && o instanceof SecurityGroupHierarchy)
			return id.equals(((SecurityGroupHierarchy) o).getId());

		return false;
	}

	@Override
	public int hashCode() {
		if (id == null)
			return 0;
		return id.hashCode();
	}
}
