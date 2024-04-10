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


/**
 * 
 */
package com.arahant.services.standard.misc.vendor;

import com.arahant.business.BVendorGroup;


public class ListVendorGroupDetailsReturnItem {

	private String vendorId;
	private String orgGroupId;
	private String vendorGroupId;
	private String groupVendorId;
	private String orgGroupName;

	public String getGroupVendorId() {
		return groupVendorId;
	}

	public void setGroupVendorId(String groupVendorId) {
		this.groupVendorId = groupVendorId;
	}

	public String getOrgGroupId() {
		return orgGroupId;
	}

	public void setOrgGroupId(String orgGroupId) {
		this.orgGroupId = orgGroupId;
	}

	public String getVendorGroupId() {
		return vendorGroupId;
	}

	public void setVendorGroupId(String vendorGroupId) {
		this.vendorGroupId = vendorGroupId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getOrgGroupName() {
		return orgGroupName;
	}

	public void setOrgGroupName(String orgGroupName) {
		this.orgGroupName = orgGroupName;
	}


	
	public ListVendorGroupDetailsReturnItem() {
		
	}

	ListVendorGroupDetailsReturnItem(BVendorGroup bc) {
		vendorId = bc.getVendor().getOrgGroupId();
		vendorGroupId = bc.getVendorGroupId();
		orgGroupId = bc.getOrgGroup().getOrgGroupId();
		groupVendorId = bc.getGroupVendorId();
		orgGroupName = bc.getOrgGroup().getName();
	}
	
}

	
