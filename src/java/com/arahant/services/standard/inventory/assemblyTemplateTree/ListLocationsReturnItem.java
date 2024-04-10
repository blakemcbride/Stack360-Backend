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
package com.arahant.services.standard.inventory.assemblyTemplateTree;

import com.arahant.business.BOrgGroup;
import com.arahant.business.BProduct;


/**
 * 
 *
 *
 */
public class ListLocationsReturnItem {
	
	public ListLocationsReturnItem()
	{
		
	}

	private String id;
	private String name;
	private int quantityAvailable;

	ListLocationsReturnItem(BOrgGroup bo, String productId)
	{
		id=bo.getId();
		name=bo.getName();
		quantityAvailable=new BProduct(productId).getQuantityAt(bo);
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

	public int getQuantityAvailable() {
		return quantityAvailable;
	}

	public void setQuantityAvailable(int quantityAvailable) {
		this.quantityAvailable = quantityAvailable;
	}
}

	
