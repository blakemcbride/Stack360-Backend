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
import com.arahant.beans.QuoteProduct;
import com.arahant.business.BLocationCost;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BProduct;
import com.arahant.business.BQuoteProduct;
import com.arahant.utils.ArahantSession;
import java.util.List;


public class SaveQuoteAndProductsInput extends TransmitInputBase {

	@Validation (table = "quote_table",column = "quote_id",required = true)
	private String quoteId;
	@Validation (table = "quote_table",column = "quote_name",required = true)
	private String name;
	@Validation (table = "quote_table",column = "quote_description",required = false)
	private String description;
	@Validation (table = "quote_table",column = "client_id",required = true)
	private String clientId;
	@Validation (table = "quote_table",column = "location_cost_id",required = false)
	private String locationId;
	@Validation (table = "quote_table",column = "markup_percent",required = false)
	private double markupPercent;
	@Validation (table = "quote_table",column = "additional_cost",required = false)
	private double additionalCost;

	private SaveQuoteProductInputItem[] products;

	public double getAdditionalCost() {
		return additionalCost;
	}

	public void setAdditionalCost(double additionalCost) {
		this.additionalCost = additionalCost;
	}

	public double getMarkupPercent() {
		return markupPercent;
	}

	public void setMarkupPercent(double markupPercent) {
		this.markupPercent = markupPercent;
	}
	
	public String getQuoteId()
	{
		return quoteId;
	}
	public void setQuoteId(String quoteId)
	{
		this.quoteId = quoteId;
	}
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

	public SaveQuoteProductInputItem[] getProducts() {
		return products;
	}

	public void setProducts(SaveQuoteProductInputItem[] products) {
		this.products = products;
	}

	void setProducts(final List<QuoteProduct> a) {
		products=new SaveQuoteProductInputItem[a.size()];
		int loop = 0;
		for (QuoteProduct i : a)
			products[loop++]=new SaveQuoteProductInputItem(i);
	}

	void setData(BQuoteTable bc)
	{
		bc.setQuoteName(name);
		bc.setQuoteDescription(description);
		bc.setClient(new BOrgGroup(clientId).getOrgGroup());
		if(!isEmpty(locationId))
			bc.setLocationCost(new BLocationCost(locationId).getBean());
		else
			bc.setLocationCost(null);
		bc.setMarkupPercent(markupPercent);
		bc.setAdditionalCost(additionalCost);

		bc.update();

		ArahantSession.getHSU().flush();

		short count = 10000;

		for (QuoteProduct qp : bc.getQuoteProducts())
		{
			BQuoteProduct bqp = new BQuoteProduct(qp);
			bqp.setSequenceNumber(count++);
			bqp.update();
		}

		ArahantSession.getHSU().flush();

		count = 0;

		if(products != null)
		{
			for (SaveQuoteProductInputItem qp : products)
			{
				if(!isEmpty(qp.getQuoteProductId()))
				{
					BQuoteProduct bqp = new BQuoteProduct(qp.getQuoteProductId());
					bqp.setQuantity((short)qp.getQuantity());
					bqp.setAdjustedRetailPrice(qp.getAdjustedPrice());
					bqp.setSequenceNumber(count++);
					bqp.setSellAsType(qp.getSellAs());
					bqp.update();
				}
				else
				{
					BProduct bp = new BProduct(qp.getProductId());
					BQuoteProduct bqp = new BQuoteProduct();
					bqp.create();
					bqp.setQuote(bc.getBean());
					bqp.setRetailPrice(bp.getRetailPrice());
					bqp.setAdjustedRetailPrice(bp.getRetailPrice());
					bqp.setProduct(bp.getBean());
					bqp.setQuantity((short)qp.getQuantity());
					bqp.setSequenceNumber(count++);
					bqp.setSellAsType(qp.getSellAs());
					bqp.insert();
				}
			}
		}

		ArahantSession.getHSU().flush();

	}
	
}

	
