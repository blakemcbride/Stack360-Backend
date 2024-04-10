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
 * Created on Jun 5, 2007
 * 
 */
package com.arahant.services.standard.tutorial.tutorial16;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Jun 5, 2007
 *
 */
public class ListQuotesReturn extends TransmitReturnBase {

	private ListQuotesItem []item;

	/**
	 * @return Returns the item.
	 */
	public ListQuotesItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final ListQuotesItem[] item) {
		this.item = item;
	}
}

	
