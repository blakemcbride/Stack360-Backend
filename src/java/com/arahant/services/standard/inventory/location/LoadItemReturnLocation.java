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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.services.standard.inventory.location;

import com.arahant.business.BItem;
import com.arahant.business.BOrgGroup;

/**
 *
 */
public class LoadItemReturnLocation {
	private String id;
	private int type;
	private String name;

	public LoadItemReturnLocation()
	{

	}

	LoadItemReturnLocation (BItem bc)
	{

		id=bc.getId();
		name=bc.getProductDescription();
		type=2;

	}


	LoadItemReturnLocation (BOrgGroup bc)
	{

		id=bc.getId();
		name=bc.getName();
		type=1;

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}


}
