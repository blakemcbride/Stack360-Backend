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
package com.arahant.services.standard.inventory.quote;

import com.arahant.beans.QuoteProduct;
import com.arahant.services.TransmitReturnBase;
import java.util.List;


public class NewQuoteFromTemplateReturn extends TransmitReturnBase {

	private String id;
	private String description;
	private String name;
	private Boolean finalized = false;

	private QuoteProductReturnItem[] item;

	public Boolean getFinalized() {
		return finalized;
	}

	public void setFinalized(Boolean finalized) {
		this.finalized = finalized;
	}
	
	public void setId(String i) {
		id = i;
	}
	
	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public QuoteProductReturnItem[] getItem() {
		return item;
	}

	public void setItem(QuoteProductReturnItem[] item) {
		this.item = item;
	}

	void setItem(final List<QuoteProduct> a) {
		item=new QuoteProductReturnItem[a.size()];
		int loop = 0;
		for (QuoteProduct i : a)
			item[loop++]=new QuoteProductReturnItem(i);
	}
}

	
