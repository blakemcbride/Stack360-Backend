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

import com.arahant.beans.CompanyDetail;
import com.arahant.beans.InterfaceLog;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.Date;
import java.util.List;

public class BInterfaceLog extends SimpleBusinessObjectBase<InterfaceLog> {

	public BInterfaceLog(InterfaceLog i) {
		bean = i;
	}

	public BInterfaceLog() {
	}

	@Override
	public String create() throws ArahantException {
		bean = new InterfaceLog();
		return bean.generateId();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(InterfaceLog.class, key);
	}

	public void setInterface(short interfaceCode) {
		bean.setInterfaceCode(interfaceCode);
	}

	public void setLastRun(Date runDate) {
		bean.setLastRun(runDate);
	}

	public void setMessage(String message) {
		bean.setStatusMessage(message);
	}

	public void setStatus(short code) {
		bean.setStatusCode(code);
	}

	public int getInterface() {
		return bean.getInterfaceCode();
	}

	public Date getLastRun() {
		return bean.getLastRun();
	}

	public String getMessage() {
		return bean.getStatusMessage();
	}

	public int getStatus() {
		return bean.getStatusCode();
	}

	public String getCompanyId() {
		return bean.getCompanyId();
	}

	public void setCompanyId(String companyId) {
		bean.setCompanyId(companyId);
	}

	public CompanyDetail getCompany() {
		return bean.getCompany();
	}

	public void setCompany(CompanyDetail company) {
		bean.setCompany(company);
	}

	public static BInterfaceLog[] list(String companyId, int cap) {
		HibernateCriteriaUtil<InterfaceLog> hcu = ArahantSession.getHSU().createCriteria(InterfaceLog.class).orderByDesc(InterfaceLog.LAST_RUN);

		if (!isEmpty(companyId))
			hcu.eq(InterfaceLog.COMPANY_ID, companyId);

		return makeArray(hcu.list());
	}

	public static BInterfaceLog[] makeArray(List<InterfaceLog> l) {
		BInterfaceLog[] ret = new BInterfaceLog[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BInterfaceLog(l.get(loop));
		return ret;
	}
}
