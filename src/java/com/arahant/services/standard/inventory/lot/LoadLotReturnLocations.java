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

package com.arahant.services.standard.inventory.lot;

/**
 *
 */
public class LoadLotReturnLocations {
	private String orgGroupName;
	private int quantity;

	public String getOrgGroupName() {
		return orgGroupName;
	}

	public void setOrgGroupName(String orgGroupName) {
		this.orgGroupName = orgGroupName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
