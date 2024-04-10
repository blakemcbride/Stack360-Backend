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
package com.arahant.services.standard.inventory.productInventory;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BItem;
import com.arahant.business.BItemInspection;


/**
 * 
 *
 *
 */
public class LoadProductInventoryReturn extends TransmitReturnBase {

	void setData(BItem b) {
		id=b.getId();
		lotNumber=b.getLotNumber();
		serialNumber=b.getSerialNumber();
	//	originalQuantity=b.getOriginalQuantity();
	//	remainingQuantity=b.getRemainingQuantity();
		productCost=b.getProductCost();
		dateReceived=b.getDateReceived();
		inventoryDescription=b.getInventoryDescription();
		lotNumberId=b.getLotId();
		originalQuantity=b.getOriginalQuantity();
		quantity=b.getQuantity();

		BItemInspection []details=b.getInspections();

		inspectionItem=new LoadProductInventoryReturnItem[details.length];

		for (int loop=0;loop<inspectionItem.length;loop++)
			inspectionItem[loop]=new LoadProductInventoryReturnItem(details[loop]);

		BItem []children=b.getChildren();

		childItem=new LoadProductInventoryReturnChild[children.length];
		for (int loop=0;loop<childItem.length;loop++)
			childItem[loop]=new LoadProductInventoryReturnChild(children[loop]);


	}
//id - string, lotNumber - string, serialNumber - string,  originalQuantity - number,
	//remainingQuantity - number, productCost - number, dateReceived - numbe

	private String id;
	private String lotNumber;
	private String serialNumber;
//	private int originalQuantity;
//	private int remainingQuantity;
	private double productCost;
	private int dateReceived;
	private String inventoryDescription;
	private LoadProductInventoryReturnChild []childItem;
	private String lotNumberId;
	private int originalQuantity;
	private int quantity;

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}



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



	public LoadProductInventoryReturnChild[] getChildItem() {
		if (childItem==null)
			childItem=new LoadProductInventoryReturnChild[0];
		return childItem;
	}

	public void setChildItem(LoadProductInventoryReturnChild[] childItem) {
		this.childItem = childItem;
	}

	public String getInventoryDescription() {
		return inventoryDescription;
	}

	public void setInventoryDescription(String inventoryDescription) {
		this.inventoryDescription = inventoryDescription;
	}



	public int getDateReceived() {
		return dateReceived;
	}

	public void setDateReceived(int dateReceived) {
		this.dateReceived = dateReceived;
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


	public double getProductCost() {
		return productCost;
	}

	public void setProductCost(double productCost) {
		this.productCost = productCost;
	}


	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}


	private LoadProductInventoryReturnItem[] inspectionItem;

	public LoadProductInventoryReturnItem[] getInspectionItem() {
		if (inspectionItem==null)
			inspectionItem=new LoadProductInventoryReturnItem[0];
		return inspectionItem;
	}

	public void setInspectionItem(LoadProductInventoryReturnItem[] inspectionItem) {
		this.inspectionItem = inspectionItem;
	}

}

	
