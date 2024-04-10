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

import com.arahant.beans.*;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import java.util.Iterator;
import java.util.Set;

public class BInvoiceLineItem extends BusinessLogicBase implements IDBFunctions {

	private static final transient ArahantLogger logger = new ArahantLogger(BInvoiceLineItem.class);
	InvoiceLineItem invoiceLineItem;

	public static void delete(Set<String> lineItemIds) {
		ArahantSession.getHSU().createCriteria(InvoiceLineItem.class).in(InvoiceLineItem.INVOICELINEITEMID, lineItemIds).delete();
	}

	public BInvoiceLineItem() {
		logger.debug("line item");
	}

	/**
	 * @param item
	 */
	public BInvoiceLineItem(final InvoiceLineItem item) {
		invoiceLineItem = item;
	}

	public BInvoiceLineItem(String id) {
		invoiceLineItem = ArahantSession.getHSU().get(InvoiceLineItem.class, id);
	}

	@Override
	public String create() throws ArahantException {
		invoiceLineItem = new InvoiceLineItem();
		invoiceLineItem.generateId();
		return invoiceLineItem.getInvoiceLineItemId();
	}

	@Override
	public void delete() throws ArahantDeleteException {
		ArahantSession.getHSU().delete(invoiceLineItem);
	}

	public String getGlAccountName() {
		return invoiceLineItem.getGlAccount().getAccountName();
	}

	public String getId() {
		return invoiceLineItem.getInvoiceLineItemId();
	}

	public String getProductServiceAcctId() {
		return invoiceLineItem.getProductService().getAccsysId();
	}

	public String getProductServiceId() {
		return invoiceLineItem.getProductService().getProductId();
	}

	public String getProductServiceName() {
		return invoiceLineItem.getProductService().getDescription();
	}

	public double getAmount() {
		return invoiceLineItem.getAmount();
	}

	public boolean isFlatAmount() {
		return invoiceLineItem.getBillingType() == 'D';
	}

	public double getTotal() {
		if (invoiceLineItem.getBillingType() == 'D')
			return invoiceLineItem.getAmount();
		else
			return invoiceLineItem.getAdjHours() * invoiceLineItem.getAdjRate();
	}

	public double getUnitPrice() {
		return invoiceLineItem.getAdjRate();
	}

	public double getUnits() {
		return invoiceLineItem.getAdjHours();
	}

	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(invoiceLineItem);
	}

	@Override
	public void load(final String key) throws ArahantException {
		invoiceLineItem = ArahantSession.getHSU().get(InvoiceLineItem.class, key);
	}

	public void setAmount(double amount) {
		invoiceLineItem.setAmount(amount);
	}

	public void setGLAccountId(String glAccountId) {
		invoiceLineItem.setGlAccount(ArahantSession.getHSU().get(GlAccount.class, glAccountId));
	}

	public void setProductId(String productServiceId) {
		invoiceLineItem.setProductService(ArahantSession.getHSU().get(ProductService.class, productServiceId));
	}

	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(invoiceLineItem);
	}

	public float getAdjHours() {
		return invoiceLineItem.getAdjHours();
	}

	public double getAdjRate() {
		return invoiceLineItem.getAdjRate();
	}

	public String getDescription() {
		return invoiceLineItem.getDescription();
	}

	public String getInvoiceLineItemId() {
		return invoiceLineItem.getInvoiceLineItemId();
	}

	public void setAdjHours(final float adjHours) {
		invoiceLineItem.setAdjHours(adjHours);
	}

	public void setAdjRate(final double adjRate) {
		invoiceLineItem.setAdjRate(adjRate);
	}

	public void setDescription(final String description) {
		invoiceLineItem.setDescription(description);
	}

	public void setInvoiceLineItemId(final String invoiceLineItemId) {
		invoiceLineItem.setInvoiceLineItemId(invoiceLineItemId);
	}

	public String getProjectId() {
		return invoiceLineItem.getProjectId();
	}

	public void setProjectId(String pid) {
		invoiceLineItem.setProjectId(pid);
	}

	public String getGlAccountId() {
		return invoiceLineItem.getGlAccount().getGlAccountId();
	}

	public String getGlAccountNumber() {
		return invoiceLineItem.getGlAccount().getAccountNumber();
	}

	public double getOrigHours() {
		double hrs = 0;

		final Iterator titr = invoiceLineItem.getTimesheets().iterator();
		while (titr.hasNext()) {
			final Timesheet ts = (Timesheet) titr.next();
			hrs += ts.getTotalHours();
		}
		return hrs;
	}

	public double getOrigRate() {
		try {
			final ClientCompany cc = (ArahantSession.getHSU().createCriteria(ClientCompany.class).eq(OrgGroup.ORGGROUPID, invoiceLineItem.getInvoice().getCompanyBase().getOrgGroupId())).first();
			return cc.getBillingRate();
		} catch (final Exception e) {
			return 0;
		}
	}

	public String getAccSysGLId() {
		return invoiceLineItem.getGlAccount().getAccountNumber();
	}

	public String getAcctSysInvoiceLineItemId() {
		return invoiceLineItem.getProductService().getAccsysId();
	}
}
