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
package com.arahant.services.standard.hr.hrCheckList;

import com.arahant.business.BCompanyForm;
import com.arahant.services.TransmitReturnBase;


public class ListAvailableDocumentsReturn extends TransmitReturnBase {

	private ListAvailableDocumentsReturnItem[] item;
	private ListAvailableDocumentsReturnItem selectedItem;

	public ListAvailableDocumentsReturnItem[] getItem()
	{
		if (item == null)
			item = new ListAvailableDocumentsReturnItem[0];
		return item;
	}
	public void setItem(ListAvailableDocumentsReturnItem[] item)
	{
		this.item = item;
	}
	public void setItem(BCompanyForm[] forms) {
		item = new ListAvailableDocumentsReturnItem[forms.length];
		for (int i = 0; i < item.length; i++)
			item[i] = new ListAvailableDocumentsReturnItem(forms[i]);
	}

	public ListAvailableDocumentsReturnItem getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(ListAvailableDocumentsReturnItem selectedItem) {
		this.selectedItem = selectedItem;
	}
}

	
