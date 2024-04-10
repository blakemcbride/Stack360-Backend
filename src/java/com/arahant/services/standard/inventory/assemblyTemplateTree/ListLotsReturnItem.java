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
import com.arahant.business.BLot;
import com.arahant.business.BOrgGroup;

public class ListLotsReturnItem {
	
	public ListLotsReturnItem()
	{
	
	}

	ListLotsReturnItem (BLot bc, String locationId)
	{
		id=bc.getId();
        number = bc.getLotNumber();
		quantityAvailable = bc.getQuantityAt(new BOrgGroup(locationId));
	}

	ListLotsReturnItem (BLot bc)
	{
		id=bc.getId();
        number = bc.getLotNumber();
	}
	
	private String number;
	private String id;
	private int quantityAvailable;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getQuantityAvailable() {
		return quantityAvailable;
	}

	public void setQuantityAvailable(int quantityAvailable) {
		this.quantityAvailable = quantityAvailable;
	}

}

	
