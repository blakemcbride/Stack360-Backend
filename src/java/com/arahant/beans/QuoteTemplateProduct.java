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
@Table(name = QuoteTemplateProduct.TABLE_NAME)
public class QuoteTemplateProduct extends ArahantBean implements java.io.Serializable {

	public static final String TABLE_NAME = "quote_template_product";
	public static final String QUOTE_TEMPLATE = "quoteTemplate";
	public static final String PRODUCT = "product";
	public static final String SEQNO = "sequenceNumber";
	public static final String DEFAULT_QUANTITY = "defaultQuantity";
	private static final long serialVersionUID = 1L;
	private String quoteTemplateProductId;
	private QuoteTemplate quoteTemplate;
	private Product product;
	private short sequenceNumber;
	private short defaultQuantity;
	private char sellAsType;

	public QuoteTemplateProduct() {}

	@Column(name = "sell_as_type")
	public char getSellAsType() {
		return sellAsType;
	}

	public void setSellAsType(char sellAsType) {
		this.sellAsType = sellAsType;
	}

	@Column(name = "default_quantity")
	public short getDefaultQuantity() {
		return defaultQuantity;
	}

	public void setDefaultQuantity(short defaultQuantity) {
		this.defaultQuantity = defaultQuantity;
	}

	@ManyToOne
	@JoinColumn(name = "product_id")
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@ManyToOne
	@JoinColumn(name = "quote_template_id")
	public QuoteTemplate getQuoteTemplate() {
		return quoteTemplate;
	}

	public void setQuoteTemplate(QuoteTemplate quoteTemplate) {
		this.quoteTemplate = quoteTemplate;
	}

	@Id
	@Column(name = "quote_template_product_id")
	public String getQuoteTemplateProductId() {
		return quoteTemplateProductId;
	}

	public void setQuoteTemplateProductId(String quoteTemplateProductId) {
		this.quoteTemplateProductId = quoteTemplateProductId;
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
		return "quote_template_product_id";
	}

	@Override
	public String generateId() throws ArahantException {
		setQuoteTemplateProductId(IDGenerator.generate(this));
		return getQuoteTemplateProductId();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final QuoteTemplateProduct other = (QuoteTemplateProduct) obj;
		if ((this.quoteTemplateProductId == null) ? (other.quoteTemplateProductId != null) : !this.quoteTemplateProductId.equals(other.quoteTemplateProductId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 29 * hash + (this.quoteTemplateProductId != null ? this.quoteTemplateProductId.hashCode() : 0);
		return hash;
	}
}
