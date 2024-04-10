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
package com.arahant.business;

import com.arahant.beans.Product;
import com.arahant.beans.QuoteProduct;
import com.arahant.beans.QuoteTable;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;

public class BQuoteProduct extends SimpleBusinessObjectBase<QuoteProduct> {

	public BQuoteProduct() {
	}

	public BQuoteProduct(final QuoteProduct bean) {
		this.bean = bean;
	}

	public BQuoteProduct(final String key) throws ArahantException {
		internalLoad(key);
	}

	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().update(bean);
	}

	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(bean);
	}

	@Override
	public String create() throws ArahantException {
		bean = new QuoteProduct();
		bean.generateId();
		return getQuoteProductId();
	}

	private void internalLoad(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(QuoteProduct.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	public static void delete(final String quoteProductId) throws ArahantException {
		new BQuoteProduct(quoteProductId).delete();
	}

	@Override
	public void delete() throws ArahantDeleteException {
		try {
			ArahantSession.getHSU().delete(bean);
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
	}

	public String getSellAsType() {
		return bean.getSellAsType() + "";
	}

	public void setSellAsType(String sellAsType) {
		bean.setSellAsType(sellAsType.charAt(0));
	}

	public double getAdjustedRetailPrice() {
		return bean.getAdjustedRetailPrice();
	}

	public void setAdjustedRetailPrice(double adjustedRetailPrice) {
		bean.setAdjustedRetailPrice(adjustedRetailPrice);
	}

	public Product getProduct() {
		return bean.getProduct();
	}

	public void setProduct(Product product) {
		bean.setProduct(product);
	}

	public short getQuantity() {
		return bean.getQuantity();
	}

	public void setQuantity(short quantity) {
		bean.setQuantity(quantity);
	}

	public QuoteTable getQuote() {
		return bean.getQuote();
	}

	public void setQuote(QuoteTable quote) {
		bean.setQuote(quote);
	}

	public String getQuoteProductId() {
		return bean.getQuoteProductId();
	}

	public void setQuoteProductId(String quoteProductId) {
		bean.setQuoteProductId(quoteProductId);
	}

	public double getRetailPrice() {
		return bean.getRetailPrice();
	}

	public void setRetailPrice(double retailPrice) {
		bean.setRetailPrice(retailPrice);
	}

	public short getSequenceNumber() {
		return bean.getSequenceNumber();
	}

	public void setSequenceNumber(short sequenceNumber) {
		bean.setSequenceNumber(sequenceNumber);
	}
}
