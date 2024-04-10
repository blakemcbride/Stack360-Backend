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
import javax.persistence.*;

@Entity
@Table(name = "vendor_group")
public class VendorGroup extends ArahantBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "vendor_group";
	private String vendorGroupId;
	private VendorCompany vendor;
	private OrgGroup orgGroup;
	private String groupVendorId;
	public static final String VENDOR = "vendor";
	public static final String ORG_GROUP = "orgGroup";
	public static final String VENDOR_GROUP_ID = "vendorGroupId";
	public static final String GROUP_VENDOR_ID = "groupVendorId";
	// Constructors

	/**
	 * default constructor
	 */
	public VendorGroup() {
	}

	@Override
	public boolean equals(Object o) {
		if (getVendorGroupId() == null && o == null)
			return true;
		if (getVendorGroupId() != null && o instanceof VendorGroup)
			return getVendorGroupId().equals(((VendorGroup) o).getVendorGroupId());

		return false;
	}

	@Override
	public int hashCode() {
		if (getVendorGroupId() == null)
			return 0;
		return getVendorGroupId().hashCode();
	}

	@Column(name = "group_vendor_id")
	public String getGroupVendorId() {
		return groupVendorId;
	}

	public void setGroupVendorId(String groupVendorId) {
		this.groupVendorId = groupVendorId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_group_id")
	public OrgGroup getOrgGroup() {
		return orgGroup;
	}

	public void setOrgGroup(OrgGroup orgGroup) {
		this.orgGroup = orgGroup;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id")
	public VendorCompany getVendor() {
		return vendor;
	}

	public void setVendor(VendorCompany vendor) {
		this.vendor = vendor;
	}

	@Id
	@Column(name = "vendor_group_id")
	public String getVendorGroupId() {
		return vendorGroupId;
	}

	public void setVendorGroupId(String vendorGroupId) {
		this.vendorGroupId = vendorGroupId;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "vendor_group_id";
	}

	@Override
	public String generateId() throws ArahantException {
		vendorGroupId = IDGenerator.generate(this);
		return vendorGroupId;
	}
}
