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

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BAssemblyTemplateDetail;
import com.arahant.annotation.Validation;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewDetailInput extends TransmitInputBase {

	void setData(BAssemblyTemplateDetail bc)
	{
		bc.setProductId(productId);
		bc.setQuantity(quantity);
		bc.setTrackToItem(trackToItem);
		bc.setDescription(description);

	}
	
	@Validation (required=false)
	private String templateId;
	@Validation (required=false)
	private String parentId;
	@Validation (required=true)
	private String productId;
	@Validation (min=0,required=false)
	private int quantity;
	@Validation (required=false)
	private boolean trackToItem;
	@Validation (required=false, table="assembly_template_detail", column="item_particulars")
	private String description;
	

	public String getTemplateId()
	{
		return templateId;
	}
	public void setTemplateId(String templateId)
	{
		this.templateId=templateId;
	}
	public String getParentId()
	{
		return parentId;
	}
	public void setParentId(String parentId)
	{
		this.parentId=parentId;
	}
	public String getProductId()
	{
		return productId;
	}
	public void setProductId(String productId)
	{
		this.productId=productId;
	}
	public int getQuantity()
	{
		return quantity;
	}
	public void setQuantity(int quantity)
	{
		this.quantity=quantity;
	}
	public boolean getTrackToItem()
	{
		return trackToItem;
	}
	public void setTrackToItem(boolean trackToItem)
	{
		this.trackToItem=trackToItem;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}

}

	
