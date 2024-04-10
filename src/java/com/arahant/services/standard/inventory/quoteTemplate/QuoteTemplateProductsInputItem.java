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
 *
 */

package com.arahant.services.standard.inventory.quoteTemplate;


public class QuoteTemplateProductsInputItem
{
	private String templateProductId;
	private String productId;
	private short defaultQuantity;
	private String sellAs;

	public QuoteTemplateProductsInputItem() {
	}

	public String getSellAs() {
		return sellAs;
	}

	public void setSellAs(String sellAs) {
		this.sellAs = sellAs;
	}

	public short getDefaultQuantity() {
		return defaultQuantity;
	}

	public void setDefaultQuantity(short defaultQuantity) {
		this.defaultQuantity = defaultQuantity;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getTemplateProductId() {
		return templateProductId;
	}

	public void setTemplateProductId(String templateProductId) {
		this.templateProductId = templateProductId;
	}

}
