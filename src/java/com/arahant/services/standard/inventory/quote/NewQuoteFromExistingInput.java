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

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BQuoteTable;
import com.arahant.annotation.Validation;
import com.arahant.beans.QuoteAdjustment;
import com.arahant.beans.QuoteProduct;
import com.arahant.business.BLocationCost;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BQuoteAdjustment;
import com.arahant.business.BQuoteProduct;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;


public class NewQuoteFromExistingInput extends TransmitInputBase {

	@Validation (table = "quote_table",column = "quote_id",required = true)
	private String quoteId;
	@Validation (table = "quote_table",column = "client_id",required = true)
	private String clientId;
	@Validation (table = "quote_table",column = "location_cost_id",required = true)
	private String locationId;

	public String getQuoteId() {
		return quoteId;
	}
	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
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
		BQuoteTable bqt = new BQuoteTable(quoteId);

		bc.create();
		bc.setQuoteDescription(bqt.getQuoteDescription());
		bc.setQuoteName(bqt.getQuoteName());
		bc.setClient(new BOrgGroup(clientId).getOrgGroup());
		bc.setLocationCost(new BLocationCost(locationId).getBean());
		bc.setCreatedDate(DateUtils.now());
		bc.setCreatedByPerson(ArahantSession.getCurrentPerson());
		bc.setAdditionalCost(bqt.getAdditionalCost());
		bc.insert();

		
		ArahantSession.getHSU().flush();

		for (QuoteProduct qp : bqt.getQuoteProducts())
		{
			BQuoteProduct bqp = new BQuoteProduct();
			bqp.create();
			bqp.setProduct(qp.getProduct());
			bqp.setQuantity(qp.getQuantity());
			bqp.setQuote(bc.getBean());
			bqp.setSequenceNumber(qp.getSequenceNumber());
			bqp.setRetailPrice(qp.getProduct().getRetailPrice());
			bqp.setAdjustedRetailPrice(qp.getProduct().getRetailPrice());
			bqp.setSellAsType(qp.getSellAsType() + "");
			bqp.insert();
		}

		for (QuoteAdjustment qa : bqt.getQuoteAdjustments())
		{
			BQuoteAdjustment bqa = new BQuoteAdjustment(qa);
			bqa.create();
			bqa.setAdjustmentDescription(qa.getAdjustmentDescription());
			bqa.setAdjustedCost(qa.getAdjustedCost());
			bqa.setQuantity(qa.getQuantity());
			bqa.setQuote(bc.getBean());
			bqa.setSequenceNumber(qa.getSequenceNumber());
			bqa.insert();
		}
	}
}

	
