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


public class SaveVendorGroupInput extends TransmitInputBase {

	@Validation (min = 1,max = 20,required = true)
	private String groupVendorId;
	@Validation (min = 1,max = 16,required = true)
	private String id;

	public String getGroupVendorId() {
		return groupVendorId;
	}

	public void setGroupVendorId(String groupVendorId) {
		this.groupVendorId = groupVendorId;
	}
	

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}


	void setData(BVendorGroup bc) {
		
		bc.setGroupVendorId(groupVendorId);

	}
	
}

	
