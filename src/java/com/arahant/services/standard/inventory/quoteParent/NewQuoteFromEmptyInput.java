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
package com.arahant.services.standard.inventory.quoteParent;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BQuoteTable;
import com.arahant.annotation.Validation;
import com.arahant.beans.QuoteProduct;
import com.arahant.business.BLocationCost;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BQuoteProduct;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;


public class NewQuoteFromEmptyInput extends TransmitInputBase {

	@Validation (table = "quote_table",column = "client_id",required = true)
	private String clientId;
	@Validation (table = "quote_table",column = "location_cost_id",required = false)
	private String locationId;
	@Validation (required = true)
	private String name;
	@Validation (required = false)
	private String description;

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

	public String getClientId()
	{
		return clientId;
	}
	public void setClientId(String clientId)
	{
		this.clientId = clientId;
	}
	public String getLocationId()
	{
		return locationId;
	}
	public void setLocationId(String locationId)
	{
		this.locationId = locationId;
	}

	void setData(BQuoteTable bc) 
	{

		bc.create();
		bc.setQuoteDescription(description);
		bc.setQuoteName(name);
		bc.setClient(new BOrgGroup(clientId).getOrgGroup());
		if(!isEmpty(locationId))
			bc.setLocationCost(new BLocationCost(locationId).getBean());
		else
			bc.setLocationCost(null);
		bc.setCreatedDate(DateUtils.now());
		bc.setCreatedByPerson(ArahantSession.getCurrentPerson());
		bc.insert();

	}
}

	
