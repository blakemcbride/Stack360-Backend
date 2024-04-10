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

import com.arahant.beans.HrBillingStatus;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.List;

/**
 *
 */
public class BBillingStatus extends SimpleBusinessObjectBase<HrBillingStatus> {

	public static void delete(String[] ids) {
		for (String id : ids)
			new BBillingStatus(id).delete();
	}

	public static BBillingStatus [] list()
	{
		return makeArray(ArahantSession.getHSU().createCriteria(HrBillingStatus.class)
				.orderBy(HrBillingStatus.NAME)
				.list());
	}

	private static BBillingStatus[] makeArray(List<HrBillingStatus> l) {
		BBillingStatus [] ret=new BBillingStatus[l.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=new BBillingStatus(l.get(loop));
		return ret;
	}

	public BBillingStatus() {
		super();
	}

	

	public BBillingStatus(String id) {
		super(id);
	}

	public BBillingStatus(HrBillingStatus o) {
		super();
		bean=o;
	}

	public String getId() {
		return bean.getBillingStatusId();
	}

	public String getName() {
		return bean.getName();
	}

	public void setName(String name) {
		bean.setName(name);
	}

	public String create() throws ArahantException {
		bean=new HrBillingStatus();
		return bean.generateId();
	}

	public void load(String key) throws ArahantException {
		bean=ArahantSession.getHSU().get(HrBillingStatus.class, key);
	}
		
	public static BBillingStatus[] search(String name, int max)
	{
		return makeArray(ArahantSession.getHSU().createCriteria(HrBillingStatus.class)
				.setMaxResults(max)
				.like(HrBillingStatus.NAME, name)
				.orderBy(HrBillingStatus.NAME)
				.list());
	}
}
