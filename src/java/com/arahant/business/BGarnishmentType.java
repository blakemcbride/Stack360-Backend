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

import com.arahant.beans.GarnishmentType;
import com.arahant.beans.WageType;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.List;

/**
 *
 */
public class BGarnishmentType extends SimpleBusinessObjectBase<GarnishmentType> {

	public static void delete(String[] ids) {
		for (String id : ids)
			new BGarnishmentType(id).delete();
	}

	static BGarnishmentType[] makeArray(List<GarnishmentType> l) {
		BGarnishmentType []ret=new BGarnishmentType[l.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=new BGarnishmentType(l.get(loop));
		
		return ret;
	}

	public BGarnishmentType()
	{
		
	}
	
	public BGarnishmentType(String typeId) {
		super(typeId);
	}
	
	public BGarnishmentType(GarnishmentType o) {
		bean=o;
	}

	public String create() throws ArahantException {
		bean=new GarnishmentType();
		return bean.generateId();
	}

	public String getCode() {
		if (bean.getWageType()==null)
			return "";
		return bean.getWageType().getPayrollInterfaceCode();
	}

	public String getWageName() {
		if (bean.getWageType()==null)
			return "";
		return bean.getWageType().getWageName();
	}

	public String getDescription() {
		return bean.getDescription();
	}

	public String getId() {
		return bean.getGarnishmentTypeId();
	}

	public int getLastActiveDate() {
		return bean.getLastActiveDate();
	}

	public String getWageTypeId() {
		if(bean.getWageType() != null)
			return bean.getWageType().getWageTypeId();
		return "";
	}

	public void load(String key) throws ArahantException {
		bean=ArahantSession.getHSU().get(GarnishmentType.class, key);
	}
	
	public static BGarnishmentType[] search(String code, String description, int max)
	{
		return makeArray(ArahantSession.getHSU().createCriteria(GarnishmentType.class)
				.like(GarnishmentType.DESCRIPTION, description)
				.geOrEq(GarnishmentType.LAST_ACTIVE_DATE, DateUtils.now(),0)
				.orderBy(GarnishmentType.DESCRIPTION)
				.setMaxResults(max)
				.joinTo(GarnishmentType.WAGE_TYPE)
				.like(WageType.PAYROLL_CODE, code)
				.eq(WageType.ORG_GROUP, ArahantSession.getHSU().getCurrentCompany())
				.list());
	}
	
	public static BGarnishmentType[] list(int activeType)
	{
		HibernateCriteriaUtil<GarnishmentType> hcu=ArahantSession.getHSU().createCriteria(GarnishmentType.class)
				.orderBy(GarnishmentType.DESCRIPTION);
		
		if (activeType==1)
			hcu.geOrEq(GarnishmentType.LAST_ACTIVE_DATE, DateUtils.now(),0);
		if (activeType==2)
			hcu.ltAndNeq(GarnishmentType.LAST_ACTIVE_DATE, DateUtils.now(),0);

		hcu.joinTo(GarnishmentType.WAGE_TYPE).eq(WageType.ORG_GROUP, ArahantSession.getHSU().getCurrentCompany());
		
		return makeArray(hcu.list());
	}


	public void setDescription(String description) {
		bean.setDescription(description);
	}

	public void setLastActiveDate(int inactiveDate) {
		bean.setLastActiveDate(inactiveDate);
	}

	public void setWageType(WageType wt) {
		bean.setWageType(wt);
	}

	public void setWageTypeId(String wageTypeId) {
		bean.setWageType(ArahantSession.getHSU().get(WageType.class, wageTypeId));
	}

}
