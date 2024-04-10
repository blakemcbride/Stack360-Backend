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

import com.arahant.beans.BankAccount;
import com.arahant.beans.OrgGroup;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.HashSet;
import java.util.List;

public class BBankAccount extends SimpleBusinessObjectBase<BankAccount> {

	public static void delete(String[] ids) {
		for (String id : ids)
			new BBankAccount(id).delete();
	}

	static BBankAccount[] makeArray(List<BankAccount> l) {
		BBankAccount[] ret = new BBankAccount[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BBankAccount(l.get(loop));
		return ret;
	}

	public BBankAccount(String id) {
		super(id);
	}

	public BBankAccount() {
		super();
	}

	public BBankAccount(BankAccount o) {
		bean = o;
	}

	public String getAccountNumber() {
		return bean.getBankAccount();
	}

	public String getCode() {
		return bean.getBankId();
	}

	public String getId() {
		return bean.getBankAccountId();
	}

	public static BBankAccount[] list(int activeType) {
		HibernateCriteriaUtil<BankAccount> hcu = ArahantSession.getHSU().createCriteria(BankAccount.class).orderBy(BankAccount.BANK_NAME);

		if (activeType == 1) //active
			hcu.geOrEq(BankAccount.LAST_ACTIVE_DATE, DateUtils.now(), 0);

		if (activeType == 2) //inactive
			hcu.ltAndNeq(BankAccount.LAST_ACTIVE_DATE, DateUtils.now(), 0);

		return makeArray(hcu.list());
	}

	public static BBankAccount[] list(String personId) {
		HibernateCriteriaUtil<BankAccount> hcu = ArahantSession.getHSU().createCriteria(BankAccount.class);


		hcu.geOrEq(BankAccount.LAST_ACTIVE_DATE, DateUtils.now(), 0);

		HashSet<String> ids = new HashSet<String>();
		BEmployee bemp = new BEmployee(personId);
		ids.add(bemp.getBankAccountId());

		for (BankAccount ba : hcu.list())
			ids.add(ba.getBankAccountId());

		hcu = ArahantSession.getHSU().createCriteria(BankAccount.class).orderBy(BankAccount.BANK_NAME).in(BankAccount.BANK_ACCOUNT_ID, ids);

		return makeArray(hcu.list());
	}

	public int getInactiveDate() {
		return bean.getLastActiveDate();
	}

	public String getName() {
		return bean.getBankName();
	}

	public String getOrgGroupId() {
		return bean.getOrgGroup().getOrgGroupId();
	}

	public String getOrgGroupName() {
		return bean.getOrgGroup().getName();
	}

	public String getRoutingNumber() {
		return bean.getBankRoute();
	}

	public String getType() {
		return bean.getAccountType() + "";
	}

	public void setAccountNumber(String accountNumber) {
		bean.setBankAccount(accountNumber);
	}

	public void setCode(String code) {
		bean.setBankId(code);
	}

	@Override
	public String create() throws ArahantException {
		bean = new BankAccount();
		return bean.generateId();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(BankAccount.class, key);
	}

	public void setInactiveDate(int inactiveDate) {
		bean.setLastActiveDate(inactiveDate);
	}

	public void setName(String name) {
		bean.setBankName(name);
	}

	public void setOrgGroupId(String orgGroupId) {
		bean.setOrgGroup(ArahantSession.getHSU().get(OrgGroup.class, orgGroupId));
	}

	public void setRoutingNumber(String routingNumber) {
		bean.setBankRoute(routingNumber);
	}

	public void setType(String type) {
		if (!isEmpty(type))
			bean.setAccountType(type.charAt(0));
	}
}
