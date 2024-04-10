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
package com.arahant.services.standard.hr.hrEvaluationStatus;

import com.arahant.business.BHREmployeeEval;
import com.arahant.services.TransmitReturnBase;


public class SearchEvaluationsReturn extends TransmitReturnBase {

	private SearchEvaluationsReturnItem [] item;
	

	public SearchEvaluationsReturnItem [] getItem()
	{
		if (item == null)
			item = new SearchEvaluationsReturnItem [0];
		return item;
	}

	public void setItem(final SearchEvaluationsReturnItem[] item) {
		this.item = item;
	}

	void setItem(final BHREmployeeEval[] e) {
		item=new SearchEvaluationsReturnItem[e.length];
		for (int loop=0;loop<e.length;loop++)
			item[loop]=new SearchEvaluationsReturnItem(e[loop]);
	}

}

	
