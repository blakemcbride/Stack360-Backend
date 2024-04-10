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

import com.arahant.beans.BenefitConfigCost;
import com.arahant.beans.BenefitConfigCostStatus;
import com.arahant.beans.HrEmployeeStatus;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import java.util.Collection;

/**
 *
 */
public class BBenefitConfigCostStatus extends SimpleBusinessObjectBase<BenefitConfigCostStatus> {

	static BBenefitConfigCostStatus[] makeArray(Collection<BenefitConfigCostStatus> l) {
		BBenefitConfigCostStatus []ret=new BBenefitConfigCostStatus[l.size()];
		int loop=0;
		for (BenefitConfigCostStatus s : l)
			ret[loop++]=new BBenefitConfigCostStatus(s);

		return ret;
	}

	public BBenefitConfigCostStatus(BenefitConfigCostStatus o) {
		super();
		bean=o;
	}

	public BBenefitConfigCostStatus(String key)
	{
		super(key);
	}

	public BBenefitConfigCostStatus()
	{
		super();
	}

	/* Do not use until Java 6 @Override */
	public String create() throws ArahantException {
		bean=new BenefitConfigCostStatus();
		return bean.generateId();
	}

	public String getStatusId() {
		return bean.getStatus().getStatusId();
	}

	public String getStatusName() {
		return bean.getStatus().getName();
	}

	/* Do not use until Java 6 @Override */
	public void load(String key) throws ArahantException {
		bean=ArahantSession.getHSU().get(BenefitConfigCostStatus.class, key);
	}

	void setCost(BenefitConfigCost cost) {
		bean.setCost(cost);
	}

	void setStatusId(String id) {
		bean.setStatus(ArahantSession.getHSU().get(HrEmployeeStatus.class,id));
	}

}
