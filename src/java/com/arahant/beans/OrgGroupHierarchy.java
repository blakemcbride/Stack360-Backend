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
@Table(name = "org_group_hierarchy")
public class OrgGroupHierarchy extends ArahantBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "org_group_hierarchy";
	public static final String CHILD = "orgGroupByChildGroupId";
	public static final String PARENT = "orgGroupByParentGroupId";
	// Fields    
	private OrgGroupHierarchyId id;
	public static final String ID = "id";
	private OrgGroup orgGroupByChildGroupId;
	public static final String ORGGROUPBYCHILDGROUPID = "orgGroupByChildGroupId";
	private OrgGroup orgGroupByParentGroupId;
	public static final String ORGGROUPBYPARENTGROUPID = "orgGroupByParentGroupId";
	private int orgGroupType;
	public static final String ORGGROUPTYPE = "orgGroupType";
	private String childGroupId;
	private String parentGroupId;
	public static final String CHILD_ID = "childGroupId";
	public static final String PARENT_ID = "parentGroupId";

	public OrgGroupHierarchy() {
	}

	@Column(name = "child_group_id", insertable = false, updatable = false)
	public String getChildGroupId() {
		return childGroupId;
	}

	public void setChildGroupId(String childGroupId) {
		firePropertyChange("childGroupId", this.childGroupId, childGroupId);
		this.childGroupId = childGroupId;
		if (id == null)
			id = new OrgGroupHierarchyId();
		id.setChildGroupId(childGroupId);
	}

	@Column(name = "parent_group_id", insertable = false, updatable = false)
	public String getParentGroupId() {
		return parentGroupId;
	}

	public void setParentGroupId(String parentGroupId) {
		firePropertyChange("parentGroupId", this.parentGroupId, parentGroupId);
		this.parentGroupId = parentGroupId;
		if (id == null)
			id = new OrgGroupHierarchyId();
		id.setParentGroupId(parentGroupId);
	}

	// Property accessors
	@Id
	public OrgGroupHierarchyId getId() {
		return this.id;
	}

	public void setId(final OrgGroupHierarchyId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "child_group_id", insertable = false, updatable = false)
	public OrgGroup getOrgGroupByChildGroupId() {
		return this.orgGroupByChildGroupId;
	}

	public void setOrgGroupByChildGroupId(final OrgGroup orgGroupByChildGroupId) {
		if (id == null)
			id = new OrgGroupHierarchyId();
		this.orgGroupByChildGroupId = orgGroupByChildGroupId;
		id.setChildGroupId(orgGroupByChildGroupId.getOrgGroupId());
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_group_id", insertable = false, updatable = false)
	public OrgGroup getOrgGroupByParentGroupId() {
		return this.orgGroupByParentGroupId;
	}

	public void setOrgGroupByParentGroupId(final OrgGroup orgGroupByParentGroupId) {
		if (id == null)
			id = new OrgGroupHierarchyId();
		this.orgGroupByParentGroupId = orgGroupByParentGroupId;
		id.setParentGroupId(orgGroupByParentGroupId.getOrgGroupId());
	}

	@Column(name = "org_group_type")
	public int getOrgGroupType() {
		return this.orgGroupType;
	}

	public void setOrgGroupType(final int orgGroupType) {
		this.orgGroupType = orgGroupType;
	}

	@Override
	public String keyColumn() {
		return "";
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String generateId() throws ArahantException {
		throw new ArahantException("Can't generate like this.");
	}

	@Override
	public boolean equals(Object o) {
		if (id == null && o == null)
			return true;
		if (id != null && o instanceof OrgGroupHierarchy)
			return id.equals(((OrgGroupHierarchy) o).getId());
		return false;
	}

	@Override
	public int hashCode() {
		if (id == null)
			return 0;
		return id.hashCode();
	}
}
