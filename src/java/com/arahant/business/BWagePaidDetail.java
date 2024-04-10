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

import com.arahant.beans.WagePaidDetail;
import com.arahant.beans.WageType;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import java.util.List;

public class BWagePaidDetail extends SimpleBusinessObjectBase<WagePaidDetail> {

	public BWagePaidDetail() {
	}

	public BWagePaidDetail(String key) {
		super(key);
	}

	public BWagePaidDetail(WagePaidDetail o) {
		bean = o;
	}

	static BWagePaidDetail[] makeArray(List<WagePaidDetail> l) {
		BWagePaidDetail[] ret = new BWagePaidDetail[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BWagePaidDetail(l.get(loop));
		return ret;
	}

	@Override
	public String create() throws ArahantException {
		bean = new WagePaidDetail();
		return bean.generateId();
	}

	public double getAmount() {
		return bean.getWageAmount();
	}

	public double getBase() {
		return bean.getWageBase();
	}

	public String getTypeName() {
		return bean.getWageType().getWageName();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(WagePaidDetail.class, key);
	}

	public void setAmount(double amount) {
		bean.setWageAmount(amount);
	}

	public void setBaseAmount(double base) {
		bean.setWageBase(base);
	}

	public void setType(WageType wt) {
		bean.setWageType(wt);
	}

	public void setWagePaid(BWagePaid currentWagePaid) {
		bean.setWagePaid(currentWagePaid.bean);
	}
}
