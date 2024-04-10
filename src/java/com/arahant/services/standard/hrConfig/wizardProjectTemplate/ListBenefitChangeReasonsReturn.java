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
package com.arahant.services.standard.hrConfig.wizardProjectTemplate;

import com.arahant.business.BHRBenefitChangeReason;
import com.arahant.services.TransmitReturnBase;


public class ListBenefitChangeReasonsReturn extends TransmitReturnBase {

	private ListBenefitChangeReasonsReturnItem[] item;
	
	public ListBenefitChangeReasonsReturnItem[] getItem()
	{
		if (item == null)
			item = new ListBenefitChangeReasonsReturnItem [0];
		return item;
	}
	public void setItem(ListBenefitChangeReasonsReturnItem[] item)
	{
		this.item = item;
	}

	public void setItem(BHRBenefitChangeReason[] bcr) {
		item = new ListBenefitChangeReasonsReturnItem[bcr.length];
		for (int i = 0; i < item.length; i++)
			item[i] = new ListBenefitChangeReasonsReturnItem(bcr[i]);
	}
}

	
