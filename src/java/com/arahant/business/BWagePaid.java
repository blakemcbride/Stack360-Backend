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

import com.arahant.beans.Employee;
import com.arahant.beans.WagePaid;
import com.arahant.beans.WagePaidDetail;
import com.arahant.beans.WageType;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.Date;
import java.util.List;

public class BWagePaid extends SimpleBusinessObjectBase<WagePaid> {

	public BWagePaid() {
	}

	public BWagePaid(String key) {
		super(key);
	}

	public BWagePaid(WagePaid o) {
		bean = o;
	}

	public static BWagePaid[] search(String personId, int checkNumber, int fromPayDate, int toPayDate, int cap) {
		HibernateCriteriaUtil<WagePaid> hcu = ArahantSession.getHSU().createCriteria(WagePaid.class)
				.setMaxResults(cap)
				.orderBy(WagePaid.PAY_DATE)
				.orderBy(WagePaid.CHECK_NUMBER);
		hcu.joinTo(WagePaid.EMPLOYEE)
				.eq(Employee.PERSONID, personId);

		hcu.dateBetween(WagePaid.PAY_DATE, fromPayDate, toPayDate);

		if (checkNumber > 0)
			hcu.eq(WagePaid.CHECK_NUMBER, checkNumber);

		return makeArray(hcu.list());
	}

	static BWagePaid[] makeArray(List<WagePaid> l) {
		BWagePaid[] ret = new BWagePaid[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BWagePaid(l.get(loop));
		return ret;
	}

	@Override
	public String create() throws ArahantException {
		bean = new WagePaid();
		return bean.generateId();
	}

	public int getCheckNumber() {
		return bean.getCheckNumber();
	}

	public String getId() {
		return bean.getWagePaidId();
	}

	public int getPayDate() {
		return bean.getDatePaid();
	}

	public int getPaymentMethod() {
		return bean.getPaymentMethod();
	}

	public String getPaymentMethodString() {
		switch (bean.getPaymentMethod()) {
			case WagePaid.METHOD_CASH:
				return "Cash";
			case WagePaid.METHOD_CHECK:
				return "Check";
			case WagePaid.METHOD_DEPOSIT:
				return "Deposit";
			default:
				return "Unknown";
		}
	}

	public double getTotalAmount() {
		return ArahantSession.getHSU().createCriteria(WagePaidDetail.class)
				.sum(WagePaidDetail.AMOUNT)
				.eq(WagePaidDetail.WAGE_PAID, bean)
				.doubleVal();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(WagePaid.class, key);
	}

	public void setCheckNo(String checkNo) {
		if (isEmpty(checkNo))
			bean.setCheckNumber(0);
		else
			bean.setCheckNumber(Integer.parseInt(checkNo));
	}

	public void setDate(Date date) {
		bean.setDatePaid(DateUtils.getDate(date));
	}

	public void setEmployeeId(String empId) {
		bean.setEmployee(ArahantSession.getHSU().get(Employee.class, empId));
	}

	public void setPaymentMethod(short method) {
		bean.setPaymentMethod(method);
	}

	public String getEmployeeNameLFM() {
		return bean.getEmployee().getNameLFM();
	}

	public BWagePaidDetail[] listDetail() {

		HibernateCriteriaUtil<WagePaidDetail> hcu = ArahantSession.getHSU().createCriteria(WagePaidDetail.class)
				.eq(WagePaidDetail.WAGE_PAID, bean)
				.orderByDesc(WagePaidDetail.AMOUNT);

		return BWagePaidDetail.makeArray(
				hcu.joinTo(WagePaidDetail.WAGE_TYPE)
				.orNull(WageType.EXPENSE_ACCOUNT, WageType.LIABILITY_ACCOUNT)
				.orderBy(WageType.NAME)
				.list());
	}
}
