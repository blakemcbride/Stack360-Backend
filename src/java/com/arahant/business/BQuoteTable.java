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

import com.arahant.beans.LocationCost;
import com.arahant.beans.OrgGroup;
import com.arahant.beans.Person;
import com.arahant.beans.QuoteAdjustment;
import com.arahant.beans.QuoteProduct;
import com.arahant.beans.QuoteTable;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.List;

public class BQuoteTable extends SimpleBusinessObjectBase<QuoteTable> {

	public BQuoteTable() {
	}

	public BQuoteTable(final QuoteTable bean) {
		this.bean = bean;
	}

	public BQuoteTable(final String key) throws ArahantException {
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
		bean = new QuoteTable();
		bean.generateId();
		return getQuoteId();
	}

	private void internalLoad(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(QuoteTable.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	public static void delete(final String quoteId) throws ArahantException {
		new BQuoteTable(quoteId).delete();
	}

	@Override
	public void delete() throws ArahantDeleteException {
		try {
			ArahantSession.getHSU().delete(bean);
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
	}

	public double getAdditionalCost() {
		return bean.getAdditionalCost();
	}

	public void setAdditionalCost(double additionalCost) {
		bean.setAdditionalCost(additionalCost);
	}

	public double getMarkupPercent() {
		return bean.getMarkupPercent();
	}

	public void setMarkupPercent(double markupPercent) {
		bean.setMarkupPercent(markupPercent);
	}

	public String getAcceptedByClient() {
		return bean.getAcceptedByClient();
	}

	public void setAcceptedByClient(String acceptedByClient) {
		bean.setAcceptedByClient(acceptedByClient);
	}

	public int getAcceptedDate() {
		return DateUtils.getDate(bean.getAcceptedDate());
	}

	public void setAcceptedDate(int acceptedDate) {
		bean.setAcceptedDate(DateUtils.getDate(acceptedDate));
	}

	public Person getAcceptedPerson() {
		return bean.getAcceptedPerson();
	}

	public void setAcceptedPerson(Person acceptedPerson) {
		bean.setAcceptedPerson(acceptedPerson);
	}

	public OrgGroup getClient() {
		return bean.getClient();
	}

	public void setClient(OrgGroup client) {
		bean.setClient(client);
	}

	public Person getCreatedByPerson() {
		return bean.getCreatedByPerson();
	}

	public void setCreatedByPerson(Person createdByPerson) {
		bean.setCreatedByPerson(createdByPerson);
	}

	public int getCreatedDate() {
		return DateUtils.getDate(bean.getCreatedDate());
	}

	public void setCreatedDate(int createdDate) {
		bean.setCreatedDate(DateUtils.getDate(createdDate));
	}

	public Person getFinalizedByPerson() {
		return bean.getFinalizedByPerson();
	}

	public void setFinalizedByPerson(Person finalizedByPerson) {
		bean.getFinalizedByPerson();
	}

	public int getFinalizedDate() {
		return DateUtils.getDate(bean.getFinalizedDate());
	}

	public void setFinalizedDate(int finalizedDate) {
		bean.setFinalizedDate(DateUtils.getDate(finalizedDate));
	}

	public LocationCost getLocationCost() {
		return bean.getLocationCost();
	}

	public void setLocationCost(LocationCost locationCost) {
		bean.setLocationCost(locationCost);
	}

	public String getQuoteDescription() {
		return bean.getQuoteDescription();
	}

	public void setQuoteDescription(String quoteDescription) {
		bean.setQuoteDescription(quoteDescription);
	}

	public String getQuoteId() {
		return bean.getQuoteId();
	}

	public void setQuoteId(String quoteId) {
		bean.setQuoteId(quoteId);
	}

	public String getQuoteName() {
		return bean.getQuoteName();
	}

	public void setQuoteName(String quoteName) {
		bean.setQuoteName(quoteName);
	}

	public List<QuoteProduct> getQuoteProducts() {
		return getQuoteProducts(new char[]{'S', 'P', 'B'});
	}

	public List<QuoteProduct> getQuoteProducts(char[] types) {
		return ArahantSession.getHSU().createCriteria(QuoteProduct.class).eq(QuoteProduct.QUOTE, bean).in(QuoteProduct.SELL_AS_TYPE, types).orderBy(QuoteProduct.SEQNO).list();
	}

	public static BQuoteTable[] searchQuotes(final int max, final String name, final String description, final int createdFromDate,
			final int createdToDate, final int finalizedFromDate, final int finalizedToDate, final String clientId, final String locationId) {
		HibernateCriteriaUtil<QuoteTable> hcu = ArahantSession.getHSU().createCriteria(QuoteTable.class).setMaxResults(max).orderBy(QuoteTable.NAME);

		if (!isEmpty(name))
			hcu.like(QuoteTable.NAME, name);

		if (!isEmpty(description))
			hcu.like(QuoteTable.DESCRIPTION, description);

		if (!isEmpty(clientId))
			hcu.eq(QuoteTable.CLIENT, new BOrgGroup(clientId).getOrgGroup());

		if (!isEmpty(locationId))
			hcu.eq(QuoteTable.LOCATION_COST, new BLocationCost(locationId).getBean());

		if (createdFromDate > 0 && createdToDate > 0)
			hcu.dateBetween(QuoteTable.CREATED_DATE, DateUtils.getDate(createdFromDate), DateUtils.getDate(createdToDate));
		else if (createdFromDate > 0 && createdToDate == 0)
			hcu.dateOnOrAfter(QuoteTable.CREATED_DATE, DateUtils.getDate(createdFromDate));
		else if (createdFromDate == 0 && createdToDate > 0)
			hcu.dateOnOrBefore(QuoteTable.CREATED_DATE, DateUtils.getDate(createdToDate));

		if (finalizedFromDate > 0 && finalizedToDate > 0)
			hcu.dateBetween(QuoteTable.FINALIZED_DATE, DateUtils.getDate(finalizedFromDate), DateUtils.getDate(finalizedToDate));
		else if (finalizedFromDate > 0 && finalizedToDate == 0)
			hcu.dateOnOrAfter(QuoteTable.FINALIZED_DATE, DateUtils.getDate(finalizedFromDate));
		else if (finalizedFromDate == 0 && finalizedToDate > 0)
			hcu.dateOnOrBefore(QuoteTable.FINALIZED_DATE, DateUtils.getDate(finalizedToDate));

		return makeArray(hcu.list());
	}

	private static BQuoteTable[] makeArray(List<QuoteTable> l) {
		final BQuoteTable[] ret = new BQuoteTable[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BQuoteTable(l.get(loop));
		return ret;
	}

	public List<QuoteAdjustment> getQuoteAdjustments() {
		return ArahantSession.getHSU().createCriteria(QuoteAdjustment.class).eq(QuoteAdjustment.QUOTE, bean).orderBy(QuoteAdjustment.SEQUENCE).list();
	}

	public double calcTotalCost() {
		double totalCost = 0;

		for (QuoteProduct qp : getQuoteProducts()) {
			short quantity = qp.getQuantity();
			char sellAs = qp.getSellAsType();
			double hours = qp.getProduct().getManHours();
			double adjustedPrice = qp.getAdjustedRetailPrice();
			double total = 0;
			double defaultRate = new BClientCompany(getClient().getOrgGroupId()).getBillingRate();
			if (defaultRate == 0)
				defaultRate = ArahantSession.getHSU().getCurrentCompany().getBillingRate();

			if (sellAs == 'P')
				total = adjustedPrice * quantity;
			else if (sellAs == 'S')
				total = adjustedPrice * quantity * hours;
			else if (sellAs == 'B')
				total = (qp.getRetailPrice() * quantity * hours) + (defaultRate * quantity);

			totalCost += total;
		}

		for (QuoteAdjustment qa : getQuoteAdjustments()) {
			double quantity = qa.getQuantity();
			double rate = qa.getAdjustedCost();
			double adjustmentCost = quantity * rate;

			totalCost += adjustmentCost;
		}

		double markup = (getMarkupPercent() / 100) + 1;
		double locationCost = getLocationCost().getLocationCost();
		double grandTotal = (totalCost + locationCost + getAdditionalCost()) * markup;

		return grandTotal;
	}
}
