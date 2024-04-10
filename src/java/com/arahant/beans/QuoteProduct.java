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
package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = QuoteProduct.TABLE_NAME)
public class QuoteProduct extends ArahantBean implements java.io.Serializable {

	public static final String TABLE_NAME = "quote_product";
	public static final String QUOTE = "quote";
	public static final String SEQNO = "sequenceNumber";
	public static final String SELL_AS_TYPE = "sellAsType";
	private static final long serialVersionUID = 1L;
	private String quoteProductId;
	private QuoteTable quote;
	private Product product;
	private short sequenceNumber;
	private short quantity;
	private double retailPrice;
	private double adjustedRetailPrice;
	private char sellAsType;

	public QuoteProduct() {}

	@Column(name = "sell_as_type")
	public char getSellAsType() {
		return sellAsType;
	}

	public void setSellAsType(char sellAsType) {
		this.sellAsType = sellAsType;
	}

	@Column(name = "adjusted_retail_price")
	public double getAdjustedRetailPrice() {
		return adjustedRetailPrice;
	}

	public void setAdjustedRetailPrice(double adjustedRetailPrice) {
		this.adjustedRetailPrice = adjustedRetailPrice;
	}

	@ManyToOne
	@JoinColumn(name = "product_id")
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Column(name = "quantity")
	public short getQuantity() {
		return quantity;
	}

	public void setQuantity(short quantity) {
		this.quantity = quantity;
	}

	@ManyToOne
	@JoinColumn(name = "quote_id")
	public QuoteTable getQuote() {
		return quote;
	}

	public void setQuote(QuoteTable quote) {
		this.quote = quote;
	}

	@Id
	@Column(name = "quote_product_id")
	public String getQuoteProductId() {
		return quoteProductId;
	}

	public void setQuoteProductId(String quoteProductId) {
		this.quoteProductId = quoteProductId;
	}

	@Column(name = "retail_price")
	public double getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(double retailPrice) {
		this.retailPrice = retailPrice;
	}

	@Column(name = "seqno")
	public short getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(short sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "quote_product_id";
	}

	@Override
	public String generateId() throws ArahantException {
		setQuoteProductId(IDGenerator.generate(this));
		return getQuoteProductId();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final QuoteProduct other = (QuoteProduct) obj;
		if ((this.quoteProductId == null) ? (other.quoteProductId != null) : !this.quoteProductId.equals(other.quoteProductId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 29 * hash + (this.quoteProductId != null ? this.quoteProductId.hashCode() : 0);
		return hash;
	}
}
