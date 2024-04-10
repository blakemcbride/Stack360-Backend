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

package com.arahant.services.standard.inventory.productInventory;

import com.arahant.business.BItem;

/**
 *
 */
public class LoadProductInventoryReturnChild {

	LoadProductInventoryReturnChild(BItem b) {
		id=b.getId();
		lotNumber=b.getLotNumber();
		serialNumber=b.getSerialNumber();
		totalRemainingQuantity=b.getQuantity();
		description=b.getDescription();
		orgGroupName=b.getOrgGroupName();
		lotNumberId=b.getLotId();
		originalQuantity=b.getOriginalQuantity();
	}

	public LoadProductInventoryReturnChild()
	{

	}

	private String id;
	private String lotNumber;
	private String serialNumber;
	private int totalRemainingQuantity;
	private String description;
	private String orgGroupName;
	private String lotNumberId;
	private int originalQuantity;

	public int getOriginalQuantity() {
		return originalQuantity;
	}

	public void setOriginalQuantity(int originalQuantity) {
		this.originalQuantity = originalQuantity;
	}



	public String getLotNumberId() {
		return lotNumberId;
	}

	public void setLotNumberId(String lotNumberId) {
		this.lotNumberId = lotNumberId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOrgGroupName() {
		return orgGroupName;
	}

	public void setOrgGroupName(String orgGroupName) {
		this.orgGroupName = orgGroupName;
	}

	public int getTotalRemainingQuantity() {
		return totalRemainingQuantity;
	}

	public void setTotalRemainingQuantity(int totalRemainingQuantity) {
		this.totalRemainingQuantity = totalRemainingQuantity;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}



	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

}
