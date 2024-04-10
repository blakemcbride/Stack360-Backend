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
import javax.persistence.Embeddable;

@Embeddable
public class SecurityGroupHierarchyId implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "security_group_hierarchy";
	// Fields    
	private String parentSecurityGroupId;
	public static final String PARENTSECURITYGROUPID = "parentSecurityGroupId";
	private String childSecurityGroupId;
	public static final String CHILDSECURITYGROUPID = "childSecurityGroupId";

	// Constructors
	/**
	 * default constructor
	 */
	public SecurityGroupHierarchyId() {
	}

	/**
	 * full constructor
	 */
	public SecurityGroupHierarchyId(final String parentSecurityGroupId,
			final String childSecurityGroupId) {
		this.parentSecurityGroupId = parentSecurityGroupId;
		this.childSecurityGroupId = childSecurityGroupId;
	}

	// Property accessors
	@Column(name = "parent_security_group_id")
	public String getParentSecurityGroupId() {
		return this.parentSecurityGroupId;
	}

	public void setParentSecurityGroupId(final String parentSecurityGroupId) {
		this.parentSecurityGroupId = parentSecurityGroupId;
	}

	@Column(name = "child_security_group_id")
	public String getChildSecurityGroupId() {
		return this.childSecurityGroupId;
	}

	public void setChildSecurityGroupId(final String childSecurityGroupId) {
		this.childSecurityGroupId = childSecurityGroupId;
	}

	@Override
	public boolean equals(final Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SecurityGroupHierarchyId))
			return false;
		final SecurityGroupHierarchyId castOther = (SecurityGroupHierarchyId) other;

		return ((this.getParentSecurityGroupId() == castOther.getParentSecurityGroupId()) || (this.getParentSecurityGroupId() != null
				&& castOther.getParentSecurityGroupId() != null && this.getParentSecurityGroupId().equals(
				castOther.getParentSecurityGroupId())))
				&& ((this.getChildSecurityGroupId() == castOther.getChildSecurityGroupId()) || (this.getChildSecurityGroupId() != null
				&& castOther.getChildSecurityGroupId() != null && this.getChildSecurityGroupId().equals(
				castOther.getChildSecurityGroupId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getParentSecurityGroupId() == null ? 0 : this.getParentSecurityGroupId().hashCode());
		result = 37
				* result
				+ (getChildSecurityGroupId() == null ? 0 : this.getChildSecurityGroupId().hashCode());
		return result;
	}
}
