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

import com.arahant.beans.Agency;
import com.arahant.beans.AgencyJoin;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;

public class BAgencyJoin extends SimpleBusinessObjectBase<AgencyJoin> {

	public BAgencyJoin() {
	}

	public BAgencyJoin(AgencyJoin o) {
		bean = o;
	}

	public BAgencyJoin(String key) {
		super(key);
	}

	@Override
	public String create() throws ArahantException {
		bean = new AgencyJoin();
		return bean.generateId();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(AgencyJoin.class, key);
	}

	public void setAgency(Agency agency) {
		bean.setAgency(agency);
	}

	public void setCompany(BCompany company) {
		bean.setCompany(company.getBean());
	}
}
