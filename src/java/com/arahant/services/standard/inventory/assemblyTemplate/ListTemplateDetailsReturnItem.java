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
package com.arahant.services.standard.inventory.assemblyTemplate;
import com.arahant.business.BAssemblyTemplateDetail;


/**
 * 
 *
 *
 */
public class ListTemplateDetailsReturnItem {
	
	public ListTemplateDetailsReturnItem()
	{
		
	}

	ListTemplateDetailsReturnItem (BAssemblyTemplateDetail bc)
	{
		
		id=bc.getId();
		itemParticulars=bc.getItemParticulars();
		productDescription=bc.getProductDescription();
		productSku=bc.getProductSku();
		quantity=bc.getQuantity();
		trackToItem=bc.getTrackToItem();

	}
	
	private String id;
	private String itemParticulars;
	private String productDescription;
	private String productSku;
	private int quantity;
	private boolean trackToItem;
	

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getItemParticulars()
	{
		return itemParticulars;
	}
	public void setItemParticulars(String itemParticulars)
	{
		this.itemParticulars=itemParticulars;
	}
	public String getProductDescription()
	{
		return productDescription;
	}
	public void setProductDescription(String productDescription)
	{
		this.productDescription=productDescription;
	}
	public String getProductSku()
	{
		return productSku;
	}
	public void setProductSku(String productSku)
	{
		this.productSku=productSku;
	}
	public int getQuantity()
	{
		return quantity;
	}
	public void setQuantity(int quantity)
	{
		this.quantity=quantity;
	}
	public boolean  getTrackToItem()
	{
		return trackToItem;
	}
	public void setTrackToItem(boolean trackToItem)
	{
		this.trackToItem=trackToItem;
	}

}

	
