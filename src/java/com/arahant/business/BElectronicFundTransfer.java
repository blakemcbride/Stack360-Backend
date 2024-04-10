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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.business;

import com.arahant.beans.ElectronicFundTransfer;
import com.arahant.beans.WageType;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import java.util.List;

/**
 *
 */
public class BElectronicFundTransfer extends SimpleBusinessObjectBase<ElectronicFundTransfer> {

	public static void deleteNotIn(BEmployee bc, List<String> eftIds) {
		ArahantSession.getHSU().createCriteria(ElectronicFundTransfer.class)
			.eq(ElectronicFundTransfer.PERSON, bc.employee)
			.notIn(ElectronicFundTransfer.ID, eftIds)
			.delete();
	}

	public static BElectronicFundTransfer[] getEFTs(BEmployee bc) {
		return makeArray(ArahantSession.getHSU().createCriteria(ElectronicFundTransfer.class)
			.eq(ElectronicFundTransfer.PERSON, bc.employee)
			.orderBy(ElectronicFundTransfer.SEQNO)
			.list());
	}

	private static BElectronicFundTransfer[] makeArray(List<ElectronicFundTransfer> l) {
		BElectronicFundTransfer []ret=new BElectronicFundTransfer[l.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=new BElectronicFundTransfer(l.get(loop));
		
		return ret;
	}

	public BElectronicFundTransfer(ElectronicFundTransfer o) {
		bean=o;
	}
	public BElectronicFundTransfer() {
		super();
	}
	public BElectronicFundTransfer(String key) {
		super(key);
	}

	public String create() throws ArahantException {
		bean=new ElectronicFundTransfer();
		return bean.generateId();
	}

	public String getAccountNumber() {
		return bean.getBankAccount();
	}

	public String getAccountType() {
		return bean.getAccountType()+"";
	}

	public double getAmount() {
		return bean.getAmount();
	}

	public String getAmountType() {
		return bean.getAmountType()+"";
	}

	public String getId() {
		return bean.getEftId();
	}

	public String getPayrollCode() {
		return bean.getWageType().getPayrollInterfaceCode();
	}

	public String getRoutingTransitNumber() {
		return bean.getBankRoute();
	}

	public String getWageTypeId() {
		return bean.getWageType().getWageTypeId();
	}

	public void load(String key) throws ArahantException {
		bean=ArahantSession.getHSU().get(ElectronicFundTransfer.class, key);
	}

	public void setAccountNumber(String accountNumber) {
		bean.setBankAccount(accountNumber);
	}

	public void setAccountType(String accountType) {
		bean.setAccountType(accountType.charAt(0));
	}

	public void setAmount(double amount) {
		bean.setAmount((float)amount);
	}

	public void setAmountType(String amountType) {
		bean.setAmountType(amountType.charAt(0));
	}


	public void setPerson(BPerson bc) {
		bean.setPerson(bc.person);
	}

	public void setRoutingTransitNumber(String routingTransitNumber) {
		bean.setBankRoute(routingTransitNumber);
	}

	public void setSeq(short seq) {
		bean.setSeqno(seq);
	}

	public void setWageType(WageType wt) {
		bean.setWageType(wt);
	}

	public void setWageTypeId(String wageTypeId) {
		setWageType(ArahantSession.getHSU().get(WageType.class, wageTypeId));
	}

}
