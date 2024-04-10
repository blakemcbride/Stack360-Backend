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

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BVendorGroup;
import com.arahant.annotation.Validation;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BVendorCompany;


public class NewVendorGroupInput extends TransmitInputBase {

	@Validation (table = "vendor",column = "org_group_id",required = true)
	private String vendorId;
	@Validation (table = "org_group",column = "org_group_id",required = true)
	private String orgGroupId;
	@Validation (min = 1,max = 20,required = true)
	private String groupVendorId;
	

	public String getVendorId()
	{
		return vendorId;
	}
	public void setVendorId(String vendorId)
	{
		this.vendorId = vendorId;
	}
	public String getOrgGroupId()
	{
		return orgGroupId;
	}
	public void setOrgGroupId(String orgGroupId)
	{
		this.orgGroupId = orgGroupId;
	}

	public String getGroupVendorId() {
		return groupVendorId;
	}

	public void setGroupVendorId(String groupVendorId) {
		this.groupVendorId = groupVendorId;
	}


	void setData(BVendorGroup bc) {
		
		bc.setVendor(new BVendorCompany(vendorId).getBean());
		bc.setOrgGroup(new BOrgGroup(orgGroupId).getOrgGroup());
		bc.setGroupVendorId(groupVendorId);
	}
	
}

	
