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

import com.arahant.beans.BankDraftBatch;
import com.arahant.beans.BankDraftHistory;
import com.arahant.beans.Receipt;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import java.util.List;

public class BBankDraftHistory extends SimpleBusinessObjectBase<BankDraftHistory> {

	private static BBankDraftHistory[] makeArray(List<BankDraftHistory> l) {
		BBankDraftHistory[] ret = new BBankDraftHistory[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BBankDraftHistory(l.get(loop));
		return ret;
	}

	public BBankDraftHistory(BankDraftHistory o) {
		bean = o;
	}

	public BBankDraftHistory() {
	}

	public BBankDraftHistory(String key) {
		super(key);
	}

	@Override
	public String create() throws ArahantException {
		bean = new BankDraftHistory();
		return bean.generateId();
	}

	public double getAmount() {
		double total = 0;

		for (Receipt r : bean.getReceipts())
			total += r.getAmount();
		return total;
	}

	public String getConfirmationNumber() {
		return bean.getReceipt();
	}

	public int getDate() {
		return bean.getDateMade();
	}

	public String getId() {
		return bean.getBankDraftHistoryId();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(BankDraftHistory.class, key);
	}

	void setBankDraft(BankDraftBatch bdb) {
		bean.setBatch(bdb);
	}

	void setDate(int date) {
		bean.setDateMade(date);
	}

	void setReceipt(String confirmationNumber) {
		bean.setReceipt(confirmationNumber);
	}

	public static BBankDraftHistory[] list(String batchId) {
		return makeArray(ArahantSession.getHSU().createCriteria(BankDraftHistory.class).orderByDesc(BankDraftHistory.DATE).joinTo(BankDraftHistory.BATCH).eq(BankDraftBatch.BATCH_ID, batchId).list());
	}
}
