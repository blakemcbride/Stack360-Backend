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
package com.arahant.services.standard.inventory.productAssemblyTemplate;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BAssemblyTemplate;
import com.arahant.annotation.Validation;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SaveTemplateInput extends TransmitInputBase {

	void setData(BAssemblyTemplate bc)
	{
	/*
		bc.setProductId(productId);
		bc.setItemParticulars(itemParticulars);
		bc.setQuantity(quantity);
		bc.setTrackToItem(trackToItem);
	 */
	}
	
	@Validation (required=true)
	private String productId;
	@Validation (table="assembly_template",column="item_particulars",required=true)
	private String itemParticulars;
	@Validation (required=false)
	private int quantity;
	@Validation (min=1,max=16,required=true)
	private String id;
	@Validation (required=false)
	private boolean trackToItem;

	public boolean getTrackToItem() {
		return trackToItem;
	}

	public void setTrackToItem(boolean trackToItem) {
		this.trackToItem = trackToItem;
	}
	

	public String getProductId()
	{
		return productId;
	}
	public void setProductId(String productId)
	{
		this.productId=productId;
	}
	public String getItemParticulars()
	{
		return itemParticulars;
	}
	public void setItemParticulars(String itemParticulars)
	{
		this.itemParticulars=itemParticulars;
	}
	public int getQuantity()
	{
		return quantity;
	}
	public void setQuantity(int quantity)
	{
		this.quantity=quantity;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}

}

	
