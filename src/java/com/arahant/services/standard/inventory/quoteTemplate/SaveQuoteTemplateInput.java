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
package com.arahant.services.standard.inventory.quoteTemplate;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BQuoteTemplate;
import com.arahant.annotation.Validation;


public class SaveQuoteTemplateInput extends TransmitInputBase
{
	@Validation (table = "quote_template",column = "template_name",required = true)
	private String name;
	@Validation (table = "quote_template",column = "template_description",required = true)
	private String description;
	@Validation (table = "quote_template",column = "quote_template_id",required = true)
	private String id;
	

	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}

	void setData(BQuoteTemplate bc)
	{
		bc.setTemplateName(name);
		bc.setTemplateDescription(description);
	}
	
}

	
