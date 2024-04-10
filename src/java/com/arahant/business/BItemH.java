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

import com.arahant.beans.ItemH;
import com.arahant.beans.Person;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import java.util.List;

public class BItemH extends SimpleBusinessObjectBase<ItemH> {

	public static BItemH[] search(String id, int startDate, int endDate, int cap) {
		if (startDate == 0)
			startDate = 19000101;
		if (endDate == 0)
			endDate = 30000101;
		return makeArray(ArahantSession.getHSU().createCriteria(ItemH.class)
				.setMaxResults(cap)
				.eq(ItemH.ID, id)
				.dateBetween(ItemH.HISTORY_DATE, DateUtils.getDate(startDate), DateUtils.getDate(endDate))
				.orderByDesc(ItemH.HISTORY_DATE)
				.list());
	}

	static BItemH[] makeArray(List<ItemH> l) {
		BItemH[] ret = new BItemH[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BItemH(l.get(loop));
		return ret;
	}

	public BItemH(String id) {
		super(id);
	}

	public BItemH(ItemH o) {
		bean = o;
	}

	public BItemH() {
	}

	@Override
	public String create() throws ArahantException {
		return "";
	}

	public String getChangeMadeBy() {
		try {
			return new BPerson(bean.getRecordPersonId()).getNameLFM();
		} catch (Exception e) {
			return "Missing";
		}
	}

	public String getChangeType() {
		return bean.getRecordChangeType() + "";
	}

	public int getDateReceived() {
		return bean.getLot().getDateReceived();
	}

	public String getDateTimeFormatted() {
		return DateUtils.getDateFormatted(bean.getRecordChangeDate());
	}

	public boolean getHasParentItem() {
		return bean.getParentItem() != null;
	}

	public String getId() {
		return bean.getHistory_id();
	}

	public String getInventoryDescription() {
		return bean.getProduct().getDescription();
	}

	public String getItemDescription() {
		return bean.getItemParticulars();
	}

	public String getLotNumber() {
		return bean.getLot().getLotNumber();
	}

	public String getOrgGroupName() {

		if (bean.getLocation() == null) {
			if (bean.getParentItem() == null)
				return "";
			return new BItem(bean.getParentItem()).getOrgGroupName();
		}

		return bean.getLocation().getName();
	}

	public int getOriginalQuantity() {
		return bean.getLot().getOriginalQuantity();
	}

	public String getParentItemDescription() {
		try {
			return bean.getParentItem().getItemParticulars();
		} catch (Exception e) {
			return "";
		}
	}

	public String getParentProductDescription() {
		try {
			return bean.getParentItem().getProduct().getDescription();
		} catch (Exception e) {
			return "";
		}
	}

	public double getProductCost() {
		return bean.getLot().getLotCost();
	}

	public String getProductDescription() {
		return bean.getProduct().getDescription();
	}

	public int getQuantity() {
		return bean.getQuantity();
	}

	public String getItemName() {
		return bean.getItemName();
	}

	public void setItemName(String name) {
		bean.setItemName(name);
	}

	public String getManufacturer() {
		return bean.getManufacturer();
	}

	public void setManufacturer(String name) {
		bean.setManufacturer(name);
	}

	public String getModel() {
		return bean.getModel();
	}

	public void setModel(String name) {
		bean.setModel(name);
	}

	public int getDatePurchased() {
		return bean.getDatePurchased();
	}

	public void setDatePurchased(int d) {
		bean.setDatePurchased(d);
	}

	public float getOriginalCost() {
		return bean.getOriginalCost();
	}

	public void setOriginalCost(float d) {
		bean.setOriginalCost(d);
	}

	public String getPurchasedFrom() {
		return bean.getPurchasedFrom();
	}

	public void setPurchasedFrom(String name) {
		bean.setPurchasedFrom(name);
	}

	public String getNotes() {
		return bean.getNotes();
	}

	public void setNotes(String name) {
		bean.setNotes(name);
	}

	public char getItemStatus() {
		return bean.getItemStatus();
	}

	public void setItemStatus(char name) {
		bean.setItemStatus(name);
	}

	public String getRetirementNotes() {
		return bean.getRetirementNotes();
	}

	public void setRetirementNotes(String name) {
		bean.setRetirementNotes(name);
	}

	public int getRetirementDate() {
		return bean.getRetirementDate();
	}

	public void setRetirementDate(int name) {
		bean.setRetirementDate(name);
	}

	public char getRetirementStatus() {
		return bean.getRetirementStatus();
	}

	public void setRetirementStatus(char name) {
		bean.setRetirementStatus(name);
	}

	public Person getReimbursementPerson() {
		return bean.getReimbursementPerson();
	}

	public void setReimbursementPerson(Person name) {
		bean.setReimbursementPerson(name);
	}

	public float getRequestedReimbursementAmount() {
		return bean.getRequestedReimbursementAmount();
	}

	public void setRequestedReimbursementAmount(float name) {
		bean.setRequestedReimbursementAmount(name);
	}

	public float getReimbursementAmountReceived() {
		return bean.getReimbursementAmountReceived();
	}

	public void setReimbursementAmountReceived(float amt) {
		bean.setReimbursementAmountReceived(amt);
	}

	public int getDateReimbursementReceived() {
		return bean.getDateReimbursementReceived();
	}

	public void setDateReimbursementReceived(int amt) {
		bean.setDateReimbursementReceived(amt);
	}

	public Person getPersonAcceptingReimbursement() {
		return bean.getPersonAcceptingReimbursement();
	}

	public void setPersonAcceptingReimbursement(Person name) {
		bean.setPersonAcceptingReimbursement(name);
	}

	public String getSerialNumber() {
		return bean.getSerialNumber();
	}

	public boolean hasParent() {
		return bean.getParentItem() != null;
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ItemH.class, key);
	}
}
