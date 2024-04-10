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
package com.arahant.services.standard.inventory.location;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BItem;
import com.arahant.business.BItemInspection;
import com.arahant.business.BOrgGroup;
import java.util.ArrayList;
import java.util.Collections;


/**
 * 
 *
 *
 */
public class LoadItemReturn extends TransmitReturnBase {

	void setData(BItem bc)
	{
		
		productDescription=bc.getProductDescription();
		serialNumber=bc.getSerialNumber();
		quantity=bc.getQuantity();
		lotNumber=bc.getLotNumber();
		cost=bc.getCost();
		received=bc.getDateReceived();
		itemDescription=bc.getItemDescription();
		lotNumberId=bc.getLotId();
		
		
		BItem []kids=bc.getChildren();
		childItems=new LoadItemReturnChild[kids.length];

		for (int loop=0;loop<kids.length;loop++)
			childItems[loop]=new LoadItemReturnChild(kids[loop]);

		BItemInspection []insp=bc.getInspections();
		inspectionItems=new LoadItemReturnInspectionItem[insp.length];

		for (int loop=0;loop<insp.length;loop++)
			inspectionItems[loop]=new LoadItemReturnInspectionItem(insp[loop]);


		ArrayList<LoadItemReturnLocation> l=new ArrayList<LoadItemReturnLocation>();
		BOrgGroup borg;

		BItem bi=bc;

		l.add(new LoadItemReturnLocation(bi));

		while (bi.getParent()!=null)
		{
			l.add(new LoadItemReturnLocation(bi.getParent()));
			bi=bi.getParent();
		}

		borg=bi.getLocation();

		l.add(new LoadItemReturnLocation(borg));

		BOrgGroup parent;
		while ((parent=borg.getParent()) != null)
		{
			l.add(new LoadItemReturnLocation(parent));
			borg=parent;
		}

		LoadItemReturnLocation top=new LoadItemReturnLocation();
		top.setId("");
		top.setName("(top)");
		top.setType(0);
		l.add(top);
		//need to reverse the list now
		Collections.reverse(l);

		locationItems=l.toArray(new LoadItemReturnLocation[l.size()]);



	}
	
	private String productDescription;
	private String serialNumber;
	private int quantity;
	private String lotNumber;
	private double cost;
	private int received;
	private String itemDescription;
	private String lotNumberId;
	private LoadItemReturnChild []childItems;
	private LoadItemReturnInspectionItem []inspectionItems;
	private LoadItemReturnLocation []locationItems;

	public LoadItemReturnLocation[] getLocationItems() {
		return locationItems;
	}

	public void setLocationItems(LoadItemReturnLocation[] locationItems) {
		this.locationItems = locationItems;
	}

	public LoadItemReturnInspectionItem[] getInspectionItems() {
		return inspectionItems;
	}

	public void setInspectionItems(LoadItemReturnInspectionItem[] inspectionItems) {
		this.inspectionItems = inspectionItems;
	}



	public LoadItemReturnChild[] getChildItems() {
		return childItems;
	}

	public void setChildItems(LoadItemReturnChild[] childItems) {
		this.childItems = childItems;
	}



	public String getLotNumberId() {
		return lotNumberId;
	}

	public void setLotNumberId(String lotNumberId) {
		this.lotNumberId = lotNumberId;
	}

	public String getProductDescription()
	{
		return productDescription;
	}
	public void setProductDescription(String productDescription)
	{
		this.productDescription=productDescription;
	}
	public String getSerialNumber()
	{
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber)
	{
		this.serialNumber=serialNumber;
	}
	public int getQuantity()
	{
		return quantity;
	}
	public void setQuantity(int quantity)
	{
		this.quantity=quantity;
	}
	public String getLotNumber()
	{
		return lotNumber;
	}
	public void setLotNumber(String lotNumber)
	{
		this.lotNumber=lotNumber;
	}
	public double getCost()
	{
		return cost;
	}
	public void setCost(double cost)
	{
		this.cost=cost;
	}
	public int getReceived()
	{
		return received;
	}
	public void setReceived(int received)
	{
		this.received=received;
	}
	public String getItemDescription()
	{
		return itemDescription;
	}
	public void setItemDescription(String itemDescription)
	{
		this.itemDescription=itemDescription;
	}

}

	
