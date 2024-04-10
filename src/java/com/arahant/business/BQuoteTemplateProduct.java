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
import com.arahant.beans.QuoteTemplate;
import com.arahant.beans.QuoteTemplateProduct;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import java.util.List;

public class BQuoteTemplateProduct extends SimpleBusinessObjectBase<QuoteTemplateProduct> {

	public BQuoteTemplateProduct() {
	}

	public BQuoteTemplateProduct(final QuoteTemplateProduct bean) {
		this.bean = bean;
	}

	public BQuoteTemplateProduct(final String key) throws ArahantException {
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
		bean = new QuoteTemplateProduct();
		bean.generateId();
		return getQuoteTemplateProductId();
	}

	private void internalLoad(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(QuoteTemplateProduct.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	public String getSellAsType() {
		return bean.getSellAsType() + "";
	}

	public void setSellAsType(String sellAsType) {
		bean.setSellAsType(sellAsType.charAt(0));
	}

	public short getDefaultQuantity() {
		return bean.getDefaultQuantity();
	}

	public void setDefaultQuantity(short defaultQuantity) {
		bean.setDefaultQuantity(defaultQuantity);
	}

	public Product getProduct() {
		return bean.getProduct();
	}

	public void setProduct(Product product) {
		bean.setProduct(product);
	}

	public QuoteTemplate getQuoteTemplate() {
		return bean.getQuoteTemplate();
	}

	public void setQuoteTemplate(QuoteTemplate quoteTemplate) {
		bean.setQuoteTemplate(quoteTemplate);
	}

	public String getQuoteTemplateProductId() {
		return bean.getQuoteTemplateProductId();
	}

	public short getSequenceNumber() {
		return bean.getSequenceNumber();
	}

	public void setSequenceNumber(short sequenceNumber) {
		bean.setSequenceNumber(sequenceNumber);
	}

	public static BQuoteTemplateProduct[] list(int max, String quoteTemplateId) {
		return makeArray(ArahantSession.getHSU().createCriteria(QuoteTemplateProduct.class)
				.setMaxResults(max)
				.orderBy(QuoteTemplateProduct.SEQNO)
				.eq(QuoteTemplateProduct.QUOTE_TEMPLATE, new BQuoteTemplate(quoteTemplateId).getBean())
				.list());
	}

	private static BQuoteTemplateProduct[] makeArray(List<QuoteTemplateProduct> l) {
		final BQuoteTemplateProduct[] ret = new BQuoteTemplateProduct[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BQuoteTemplateProduct(l.get(loop));
		return ret;
	}
}
