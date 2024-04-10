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
package com.arahant.services.standard.inventory.lot;

import com.arahant.business.BItemInspection;
import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BLot;
import com.arahant.business.BOrgGroup;


/**
 * 
 *
 *
 */
public class LoadLotReturn extends TransmitReturnBase {

	void setData(BLot bc)
	{
		
		dateReceived=bc.getDateReceived();
	//	description=bc.getDescription();
//		inspection=bc.getInspection();
		lotCost=bc.getLotCost();
		lotDescription=bc.getLotDescription();
		lotNumber=bc.getLotNumber();
		originalQuantity=bc.getOriginalQuantity();
//		locationBreakdown=bc.getLocationBreakdown();

		BItemInspection []details=bc.getInspections();

		inspection=new LoadLotReturnItem[details.length];

		for (int loop=0;loop<inspection.length;loop++)
			inspection[loop]=new LoadLotReturnItem(details[loop]);


		BOrgGroup []locs=bc.getLocations();
		locationBreakdown=new LoadLotReturnLocations[locs.length];
		for (int loop=0;loop<locs.length;loop++)
		{
			locationBreakdown[loop]=new LoadLotReturnLocations();
			locationBreakdown[loop].setOrgGroupName(locs[loop].getName());
			locationBreakdown[loop].setQuantity(bc.getQuantityAtWithChildren(locs[loop]));
		}



	}
	
	private int dateReceived;
//	private String description;
	private LoadLotReturnItem [] inspection;
	private double lotCost;
	private String lotDescription;
	private String lotNumber;
	private int originalQuantity;
	private LoadLotReturnLocations [] locationBreakdown;
	

	public int getDateReceived()
	{
		return dateReceived;
	}
	public void setDateReceived(int dateReceived)
	{
		this.dateReceived=dateReceived;
	}
/*	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}
 * */
	public LoadLotReturnItem [] getInspection()
	{
		if (inspection==null)
			inspection= new LoadLotReturnItem [0];
		return inspection;
	}
	public void setInspection(LoadLotReturnItem [] inspection)
	{
		this.inspection=inspection;
	}
	public double getLotCost()
	{
		return lotCost;
	}
	public void setLotCost(double lotCost)
	{
		this.lotCost=lotCost;
	}
	public String getLotDescription()
	{
		return lotDescription;
	}
	public void setLotDescription(String lotDescription)
	{
		this.lotDescription=lotDescription;
	}
	public String getLotNumber()
	{
		return lotNumber;
	}
	public void setLotNumber(String lotNumber)
	{
		this.lotNumber=lotNumber;
	}
	public int getOriginalQuantity()
	{
		return originalQuantity;
	}
	public void setOriginalQuantity(int originalQuantity)
	{
		this.originalQuantity=originalQuantity;
	}
	public LoadLotReturnLocations [] getLocationBreakdown()
	{
		if (locationBreakdown==null)
			locationBreakdown= new LoadLotReturnLocations [0];
		return locationBreakdown;
	}
	public void setLocationBreakdown(LoadLotReturnLocations [] locationBreakdown)
	{
		this.locationBreakdown=locationBreakdown;
	}

}

	
