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
import com.arahant.beans.QuoteTemplateProduct;
import com.arahant.business.BLocationCost;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BQuoteProduct;
import com.arahant.business.BQuoteTemplate;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;


public class NewQuoteFromTemplateInput extends TransmitInputBase {

	@Validation (table = "quote_template",column = "quote_template_id",required = true)
	private String quoteTemplateId;
	@Validation (table = "quote_table",column = "client_id",required = true)
	private String clientId;
	@Validation (table = "quote_table",column = "location_cost_id",required = false)
	private String locationId;
	

	public String getQuoteTemplateId()
	{
		return quoteTemplateId;
	}
	public void setQuoteTemplateId(String quoteTemplateId)
	{
		this.quoteTemplateId = quoteTemplateId;
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
		BQuoteTemplate bqt = new BQuoteTemplate(quoteTemplateId);

		bc.create();
		bc.setQuoteDescription(bqt.getTemplateDescription());
		bc.setQuoteName(bqt.getTemplateName());
		bc.setClient(new BOrgGroup(clientId).getOrgGroup());
		if(!isEmpty(locationId))
			bc.setLocationCost(new BLocationCost(locationId).getBean());
		else
			bc.setLocationCost(null);
		bc.setCreatedDate(DateUtils.now());
		bc.setCreatedByPerson(ArahantSession.getCurrentPerson());
		bc.insert();
		
		ArahantSession.getHSU().flush();

		for (QuoteTemplateProduct qtp : bqt.getQuoteTemplateProducts())
		{
			BQuoteProduct bqp = new BQuoteProduct();
			bqp.create();
			bqp.setSellAsType(qtp.getSellAsType() + "");
			bqp.setProduct(qtp.getProduct());
			bqp.setQuantity(qtp.getDefaultQuantity());
			bqp.setQuote(bc.getBean());
			bqp.setSequenceNumber(qtp.getSequenceNumber());
			bqp.setRetailPrice(qtp.getProduct().getRetailPrice());
			bqp.setAdjustedRetailPrice(qtp.getProduct().getRetailPrice());
			bqp.insert();
		}
	}
}

	
