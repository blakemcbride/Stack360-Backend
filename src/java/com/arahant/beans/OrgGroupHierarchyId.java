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
public class OrgGroupHierarchyId implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "org_group_hierarchy";

	private String parentGroupId;
	public static final String PARENTGROUPID = "parentGroupId";
	private String childGroupId;
	public static final String CHILDGROUPID = "childGroupId";

	/** default constructor */
	public OrgGroupHierarchyId() {
	}

	/** full constructor */
	public OrgGroupHierarchyId(final String parentGroupId, final String childGroupId) {
		this.parentGroupId = parentGroupId;
		this.childGroupId = childGroupId;
	}

	// Property accessors
	@Column(name="parent_group_id")
	public String getParentGroupId() {
		return this.parentGroupId;
	}

	public void setParentGroupId(final String parentGroupId) {
		this.parentGroupId = parentGroupId;
	}

	@Column(name="child_group_id")
	public String getChildGroupId() {
		return this.childGroupId;
	}

	public void setChildGroupId(final String childGroupId) {
		this.childGroupId = childGroupId;
	}

	@Override
	public boolean equals(final Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof OrgGroupHierarchyId))
			return false;
		final OrgGroupHierarchyId castOther = (OrgGroupHierarchyId) other;

		return ((this.getParentGroupId() == castOther.getParentGroupId()) || (this
				.getParentGroupId() != null
				&& castOther.getParentGroupId() != null && this
				.getParentGroupId().equals(castOther.getParentGroupId())))
				&& ((this.getChildGroupId() == castOther.getChildGroupId()) || (this
						.getChildGroupId() != null
						&& castOther.getChildGroupId() != null && this
						.getChildGroupId().equals(castOther.getChildGroupId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getParentGroupId() == null ? 0 : this.getParentGroupId()
						.hashCode());
		result = 37
				* result
				+ (getChildGroupId() == null ? 0 : this.getChildGroupId()
						.hashCode());
		return result;
	}

}
