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

import com.arahant.beans.QuoteAdjustment;
import com.arahant.beans.QuoteTable;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import java.util.List;

public class BQuoteAdjustment extends SimpleBusinessObjectBase<QuoteAdjustment> {

	public BQuoteAdjustment() {
	}

	public BQuoteAdjustment(final QuoteAdjustment bean) {
		this.bean = bean;
	}

	public BQuoteAdjustment(final String key) throws ArahantException {
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
		bean = new QuoteAdjustment();
		bean.generateId();
		return getQuoteAdjustmentId();
	}

	private void internalLoad(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(QuoteAdjustment.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	public static void delete(final String[] adjustmentIds) throws ArahantException {
		for (String s : adjustmentIds)
			new BQuoteAdjustment(s).delete();
	}

	@Override
	public void delete() throws ArahantDeleteException {
		try {
			ArahantSession.getHSU().delete(bean);
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
	}

	public double getAdjustedCost() {
		return bean.getAdjustedCost();
	}

	public void setAdjustedCost(double adjustedCost) {
		bean.setAdjustedCost(adjustedCost);
	}

	public String getAdjustmentDescription() {
		return bean.getAdjustmentDescription();
	}

	public void setAdjustmentDescription(String adjustmentDescription) {
		bean.setAdjustmentDescription(adjustmentDescription);
	}

	public short getQuantity() {
		return bean.getQuantity();
	}

	public void setQuantity(short quantity) {
		bean.setQuantity(quantity);
	}

	public String getQuoteAdjustmentId() {
		return bean.getQuoteAdjustmentId();
	}

	public void setQuoteAdjustmentId(String quoteAdjustmentId) {
		bean.setQuoteAdjustmentId(quoteAdjustmentId);
	}

	public QuoteTable getQuote() {
		return bean.getQuote();
	}

	public void setQuote(QuoteTable quote) {
		bean.setQuote(quote);
	}

	public short getSequenceNumber() {
		return bean.getSequenceNumber();
	}

	public void setSequenceNumber(short sequenceNumber) {
		bean.setSequenceNumber(sequenceNumber);
	}

	private static BQuoteAdjustment[] makeArray(List<QuoteAdjustment> l) {
		final BQuoteAdjustment[] ret = new BQuoteAdjustment[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BQuoteAdjustment(l.get(loop));
		return ret;
	}

	public static BQuoteAdjustment[] listQuoteAdjustments(final int max, final String quoteId) {
		return makeArray(ArahantSession.getHSU().createCriteria(QuoteAdjustment.class)
				.setMaxResults(max).orderBy(QuoteAdjustment.SEQUENCE)
				.eq(QuoteAdjustment.QUOTE, new BQuoteTable(quoteId).getBean()).list());
	}
}
