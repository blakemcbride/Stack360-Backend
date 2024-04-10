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
package com.arahant.services.standard.hrConfig.wizardRequiredDocuments;

import com.arahant.business.BBenefitDocument;
import com.arahant.services.TransmitReturnBase;


public class ListBenefitDocumentsReturn extends TransmitReturnBase {

	private ListBenefitDocumentsReturnItem[] item;
	

	public ListBenefitDocumentsReturnItem[] getItem()
	{
		if (item == null)
			item = new ListBenefitDocumentsReturnItem[0];
		return item;
	}
	public void setItem(ListBenefitDocumentsReturnItem[] item)
	{
		this.item = item;
	}
	public void setItem(BBenefitDocument[] bd) {
		item = new ListBenefitDocumentsReturnItem[bd.length];
		for (int i = 0; i < item.length; i++)
			item[i] = new ListBenefitDocumentsReturnItem(bd[i]);
	}

}

	
