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
 * Created on Feb 25, 2007
 * 
 */
package com.arahant.services.standard.hr.hrEvaluation;
import com.arahant.business.BHREmployeeEval;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Feb 25, 2007
 *
 */
public class ListEmployeeEvalsReturn extends TransmitReturnBase {

	private ListEmployeeEvalsItem item[];
	/**
	 * @return Returns the item.
	 */
	public ListEmployeeEvalsItem[] getItem() {
		return item;
	}
	/**
	 * @param item The item to set.
	 */
	public void setItem(final ListEmployeeEvalsItem[] item) {
		this.item = item;
	}
	public ListEmployeeEvalsReturn() {
		super();
	}
	/**
	 * @param evals
	 */
	void setItem(final BHREmployeeEval[] e) {
		item=new ListEmployeeEvalsItem[e.length];
		for (int loop=0;loop<e.length;loop++)
			item[loop]=new ListEmployeeEvalsItem(e[loop]);
	}
	
	
}

	
