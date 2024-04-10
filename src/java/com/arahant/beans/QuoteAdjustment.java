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
@Table(name = QuoteAdjustment.TABLE_NAME)
public class QuoteAdjustment extends ArahantBean implements java.io.Serializable {

	public static final String TABLE_NAME = "quote_adjustment";
	public static final String ID = "quoteAdjustmentId";
	public static final String QUOTE = "quote";
	public static final String DESCRIPTION = "adjustmentDescription";
	public static final String SEQUENCE = "sequenceNumber";
	public static final String QUANTITY = "quantity";
	public static final String ADJUSTED_COST = "adjustedCost";
	private static final long serialVersionUID = 1L;
	private String quoteAdjustmentId;
	private QuoteTable quote;
	private String adjustmentDescription;
	private short sequenceNumber;
	private short quantity;
	private double adjustedCost;

	public QuoteAdjustment() {}

	@Column(name = "adjusted_cost")
	public double getAdjustedCost() {
		return adjustedCost;
	}

	public void setAdjustedCost(double adjustedCost) {
		this.adjustedCost = adjustedCost;
	}

	@Column(name = "adjustment_description")
	public String getAdjustmentDescription() {
		return adjustmentDescription;
	}

	public void setAdjustmentDescription(String adjustmentDescription) {
		this.adjustmentDescription = adjustmentDescription;
	}

	@Column(name = "quantity")
	public short getQuantity() {
		return quantity;
	}

	public void setQuantity(short quantity) {
		this.quantity = quantity;
	}

	@Id
	@Column(name = "quote_adjustment_id")
	public String getQuoteAdjustmentId() {
		return quoteAdjustmentId;
	}

	public void setQuoteAdjustmentId(String quoteAdjustmentId) {
		this.quoteAdjustmentId = quoteAdjustmentId;
	}

	@ManyToOne
	@JoinColumn(name = "quote_id")
	public QuoteTable getQuote() {
		return quote;
	}

	public void setQuote(QuoteTable quote) {
		this.quote = quote;
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
		return "quote_adjustment_id";
	}

	@Override
	public String generateId() throws ArahantException {
		setQuoteAdjustmentId(IDGenerator.generate(this));
		return getQuoteAdjustmentId();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final QuoteAdjustment other = (QuoteAdjustment) obj;
		if ((this.quoteAdjustmentId == null) ? (other.quoteAdjustmentId != null) : !this.quoteAdjustmentId.equals(other.quoteAdjustmentId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 97 * hash + (this.quoteAdjustmentId != null ? this.quoteAdjustmentId.hashCode() : 0);
		return hash;
	}
}
