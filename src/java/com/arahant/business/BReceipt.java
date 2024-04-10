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

import com.arahant.beans.BankDraftHistory;
import com.arahant.beans.Invoice;
import com.arahant.beans.Person;
import com.arahant.beans.Receipt;
import com.arahant.beans.ReceiptJoin;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import java.util.ArrayList;
import java.util.List;

public class BReceipt extends SimpleBusinessObjectBase<Receipt> {

	public static void delete(String[] ids) {
		for (String id : ids)
			new BReceipt(id).delete();
	}

	public static BReceipt[] searchAdjustments(int fromDate, int toDate, String personId, int max) {
		return makeArray(ArahantSession.getHSU().createCriteria(Receipt.class)
				.setMaxResults(max)
				.dateBetween(Receipt.DATE, fromDate, toDate)
				.eq(Receipt.TYPE, Receipt.TYPE_ADJUSTMENT)
				.joinTo(Receipt.PERSON)
				.eq(Person.PERSONID, personId)
				.list());
	}

	public static BReceipt[] search(int fromDate, int toDate, String personId, int max) {
		return makeArray(ArahantSession.getHSU().createCriteria(Receipt.class)
				.setMaxResults(max)
				.dateBetween(Receipt.DATE, fromDate, toDate)
				.joinTo(Receipt.PERSON)
				.eq(Person.PERSONID, personId)
				.list());
	}

	public static BReceipt[] search(int fromDate, int toDate, String personId, boolean excludeFullyApplied, int max) {
		BReceipt[] barr = makeArray(ArahantSession.getHSU().createCriteria(Receipt.class)
				.ne(Receipt.TYPE, Receipt.TYPE_ADJUSTMENT)
				.setMaxResults(max)
				.dateBetween(Receipt.DATE, fromDate, toDate)
				.joinTo(Receipt.PERSON)
				.eq(Person.PERSONID, personId)
				.list());

		@SuppressWarnings("unchecked")
		ArrayList<BReceipt> bl = new ArrayList();

		//narrow by exclude fully applied
		for (int loop = 0; loop < barr.length; loop++)
			if (!excludeFullyApplied || barr[loop].getBalance() >= .01)
				bl.add(barr[loop]);


		return bl.toArray(new BReceipt[bl.size()]);
	}

	static BReceipt[] getOutstandingReceipts(Person person) {
		//TODO: This is not the most efficient way to do this
		List<Receipt> rlist = ArahantSession.getHSU()
				.createCriteria(Receipt.class)
				.eq(Receipt.PERSON, person)
				.orderBy(Receipt.DATE)
				.list();

		ArrayList<BReceipt> brl = new ArrayList<BReceipt>();

		for (Receipt r : rlist) {
			BReceipt br = new BReceipt(r);
			if (br.getAvailableAmount() > 0)
				brl.add(br);
		}

		return brl.toArray(new BReceipt[brl.size()]);
	}

	static BReceipt[] makeArray(List<Receipt> l) {
		BReceipt[] ret = new BReceipt[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BReceipt(l.get(loop));
		return ret;
	}

	public BReceipt(String id) {
		super(id);
	}

	public BReceipt() {
	}

	private BReceipt(Receipt o) {
		bean = o;
	}

	@Override
	public String create() throws ArahantException {
		bean = new Receipt();
		return bean.generateId();
	}

	public void deleteAppliedPayments() {
		ArahantSession.getHSU().createCriteria(ReceiptJoin.class)
				.eq(ReceiptJoin.RECEIPT, bean)
				.delete();
	}

	public String getAccountingInvoiceId() {
		try {
			if (bean.getReceiptJoins().size() > 1)
				return "SPLIT";
			return bean.getReceiptJoins().iterator().next().getInvoice().getAccountingInvoiceIdentifier();
		} catch (Exception e) {
			return "";
		}
	}

	public double getAmount() {
		return bean.getAmount();
	}

	public double getAppliedTo(BInvoice inv) {

		return ArahantSession.getHSU().createCriteria(ReceiptJoin.class)
				.sum(ReceiptJoin.AMOUNT)
				.eq(ReceiptJoin.RECEIPT, bean)
				.eq(ReceiptJoin.INVOICE, inv.bean)
				.doubleVal();

	}

	public double getAvailableAmount(BInvoice inv) {

		double n = ArahantSession.getHSU().createCriteria(ReceiptJoin.class)
				.sum(ReceiptJoin.AMOUNT)
				.eq(ReceiptJoin.RECEIPT, bean)
				.ne(ReceiptJoin.INVOICE, inv.bean)
				.doubleVal();

		return bean.getAmount() - n;

	}

	public double getAvailableAmount() {

		return bean.getAmount() - ArahantSession.getHSU().createCriteria(ReceiptJoin.class)
				.sum(ReceiptJoin.AMOUNT)
				.eq(ReceiptJoin.RECEIPT, bean)
				.doubleVal();
	}

	/**
	 *
	 * @return how much of payment is not applied
	 */
	public double getBalance() {
		double bal = bean.getAmount();

		for (ReceiptJoin rj : bean.getReceiptJoins())
			bal -= rj.getAmount();

		return bal;
	}

	public int getDate() {
		return bean.getReceiptDate();
	}

	public String getDescription() {
		return bean.getReference();
	}

	public String getId() {
		return bean.getReceiptId();
	}

	public String getInvoiceId() {
		try {
			return bean.getReceiptJoins().iterator().next().getInvoice().getInvoiceId();
		} catch (Exception e) {
			return "";
		}
	}

	public BInvoice[] getInvoices() {
		return BInvoice.makeArray(
				ArahantSession.getHSU().createCriteria(Invoice.class)
				.joinTo(Invoice.RECEIPT_JOINS)
				.eq(ReceiptJoin.RECEIPT, bean)
				.list());
	}

	public char getType() {
		return bean.getReceiptType();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(Receipt.class, key);
	}

	public void setDescription(String description) {
		bean.setReference(description);
	}

	public void setAmount(double amt) {
		bean.setAmount(amt);
	}

	public void setPersonId(String personId) {
		Person p = ArahantSession.getHSU().get(Person.class, personId);
		bean.setPerson(p);
		if (p != null)
			bean.setSource('P');
	}

	public void setSource(String source) {
		if (!isEmpty(source))
			bean.setSource(source.charAt(0));
	}

	public void setType(String type) {
		if (isEmpty(type))
			return;
		bean.setReceiptType(type.charAt(0));
	}

	void setBankDraftHistory(BankDraftHistory bdh) {
		bean.setBankDraftHistory(bdh);
	}

	void setConfirmationNumber(String confirmationNumber) {
		bean.setReference(confirmationNumber);
	}

	public void setDate(int date) {
		bean.setReceiptDate(date);
	}

	void setPerson(Person p) {
		bean.setPerson(p);
	}

	void setReceiptType(char c) {
		bean.setReceiptType(c);
	}

	void setSource(char c) {
		bean.setSource(c);
	}
}
