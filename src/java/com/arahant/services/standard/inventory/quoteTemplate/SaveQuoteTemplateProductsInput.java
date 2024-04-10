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
import com.arahant.annotation.Validation;
import com.arahant.beans.QuoteTemplate;
import com.arahant.business.BProduct;
import com.arahant.business.BQuoteTemplateProduct;


public class SaveQuoteTemplateProductsInput extends TransmitInputBase {

	@Validation (table = "quote_template",column = "quote_template_id",required = true)
	private String quoteTemplateId;

	private QuoteTemplateProductsInputItem[] products;
	
	public String getQuoteTemplateId()
	{
		return quoteTemplateId;
	}
	public void setQuoteTemplateId(String quoteTemplateId)
	{
		this.quoteTemplateId = quoteTemplateId;
	}

	public QuoteTemplateProductsInputItem[] getProducts() {
		return products;
	}

	public void setProducts(QuoteTemplateProductsInputItem[] products) {
		this.products = products;
	}

	void setData(QuoteTemplateProductsInputItem bc, QuoteTemplate quoteTemplate, short sequenceNumber)
	{
		if (!isEmpty(bc.getTemplateProductId())) //save existing one
		{
			BQuoteTemplateProduct bqtp = new BQuoteTemplateProduct(bc.getTemplateProductId());
			bqtp.setDefaultQuantity(bc.getDefaultQuantity());
			bqtp.setProduct(new BProduct(bc.getProductId()).getBean());
			bqtp.setSequenceNumber(sequenceNumber);
			bqtp.setSellAsType(bc.getSellAs());
			bqtp.update();
		}
		else //make a new one
		{
			BQuoteTemplateProduct bqtp = new BQuoteTemplateProduct();
			bqtp.create();
			bqtp.setDefaultQuantity(bc.getDefaultQuantity());
			bqtp.setProduct(new BProduct(bc.getProductId()).getBean());
			bqtp.setSequenceNumber(sequenceNumber);
			bqtp.setQuoteTemplate(quoteTemplate);
			bqtp.setSellAsType(bc.getSellAs());
			bqtp.insert();
		}
	}
}

	
