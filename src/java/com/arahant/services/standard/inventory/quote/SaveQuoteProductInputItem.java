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

package com.arahant.services.standard.inventory.quote;

import com.arahant.beans.QuoteProduct;


public class SaveQuoteProductInputItem
{
	private String quoteProductId;
	private String productId;
	private double quantity;
	private double adjustedPrice;
	private String sellAs;

	public SaveQuoteProductInputItem() {
	}

	public SaveQuoteProductInputItem(QuoteProduct qp)
	{
		sellAs = qp.getSellAsType() + "";
		quoteProductId = qp.getQuoteProductId();
		productId = qp.getProduct().getProductId();
		quantity = qp.getQuantity();
	}

	public String getSellAs() {
		return sellAs;
	}

	public void setSellAs(String sellAs) {
		this.sellAs = sellAs;
	}

	public double getAdjustedPrice() {
		return adjustedPrice;
	}

	public void setAdjustedPrice(double adjustedPrice) {
		this.adjustedPrice = adjustedPrice;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getQuoteProductId() {
		return quoteProductId;
	}

	public void setQuoteProductId(String quoteProductId) {
		this.quoteProductId = quoteProductId;
	}
}
