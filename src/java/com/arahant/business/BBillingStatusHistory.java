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
import com.arahant.beans.HrBillingStatusHistory;
import com.arahant.beans.Person;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import java.util.List;

/**
 *
 */
public class BBillingStatusHistory extends SimpleBusinessObjectBase<HrBillingStatusHistory>{

	public static void delete(String[] ids) {
		for (String id : ids)
			new BBillingStatusHistory(id).delete();
	}

	private static BBillingStatusHistory[] makeArray(List<HrBillingStatusHistory> l) {
		BBillingStatusHistory []ret=new BBillingStatusHistory[l.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=new BBillingStatusHistory(l.get(loop));
		return ret;
	}

	public BBillingStatusHistory() {
		super();
	}

	public BBillingStatusHistory(HrBillingStatusHistory o) {
		super();
		bean=o;
	}

	public BBillingStatusHistory(String id) {
		super(id);
	}

	public BBillingStatus getBillingStatus() {
		return new BBillingStatus(bean.getBillingStatus());
	}
	
	private void checkStatusHistories()
	{
		//do I have an old one that this should terminate?
		HrBillingStatusHistory old=ArahantSession.getHSU().createCriteria(HrBillingStatusHistory.class)
			.ne(HrBillingStatusHistory.ID, bean.getBillingStatusHistoryId())
			.eq(HrBillingStatusHistory.PERSON, bean.getPerson())
			.eq(HrBillingStatusHistory.FINAL_DATE, 0)
			.lt(HrBillingStatusHistory.DATE, bean.getStartDate())
			.first();
		
		if (old!=null)
		{
			old.setFinalDate(DateUtils.addDays(bean.getStartDate(), -1));
			ArahantSession.getHSU().saveOrUpdate(old);
		}
		
		//do I overlap anyone
		
		//overlap left side
		HrBillingStatusHistory overlap=ArahantSession.getHSU().createCriteria(HrBillingStatusHistory.class)
			.ne(HrBillingStatusHistory.ID, bean.getBillingStatusHistoryId())
			.eq(HrBillingStatusHistory.PERSON, bean.getPerson())
			.ge(HrBillingStatusHistory.FINAL_DATE, bean.getStartDate())
			.le(HrBillingStatusHistory.DATE, bean.getStartDate())
			.first();
		
		if (overlap!=null)
			throw new ArahantWarning("Dates overlap other billing status.");
		
		
		//overlap totally inside
		overlap=ArahantSession.getHSU().createCriteria(HrBillingStatusHistory.class)
			.ne(HrBillingStatusHistory.ID, bean.getBillingStatusHistoryId())
			.eq(HrBillingStatusHistory.PERSON, bean.getPerson())
			.le(HrBillingStatusHistory.FINAL_DATE, bean.getFinalDate())
			.ge(HrBillingStatusHistory.DATE, bean.getStartDate())
			.first();
		
		if (overlap!=null)
			throw new ArahantWarning("Dates overlap other billing status.");
		
		//overlap totally outside
		overlap=ArahantSession.getHSU().createCriteria(HrBillingStatusHistory.class)
			.ne(HrBillingStatusHistory.ID, bean.getBillingStatusHistoryId())
			.eq(HrBillingStatusHistory.PERSON, bean.getPerson())
			.ge(HrBillingStatusHistory.FINAL_DATE, bean.getFinalDate())
			.le(HrBillingStatusHistory.DATE, bean.getStartDate())
			.first();
		
		//if (overlap!=null)
		//	throw new ArahantWarning("Dates overlap other billing status.");
		
		//overlap right side
		overlap=ArahantSession.getHSU().createCriteria(HrBillingStatusHistory.class)
			.ne(HrBillingStatusHistory.ID, bean.getBillingStatusHistoryId())
			.eq(HrBillingStatusHistory.PERSON, bean.getPerson())
			.ge(HrBillingStatusHistory.FINAL_DATE, bean.getFinalDate())
			.le(HrBillingStatusHistory.DATE, bean.getFinalDate())
			.first();
		
		if (overlap!=null)
			throw new ArahantWarning("Dates overlap other billing status.");
	}
	
	@Override
	public void insert()
	{
		checkStatusHistories();
		super.insert();
	}
	
	@Override
	public void update() throws ArahantException
	{
		checkStatusHistories();
		super.update();
	}

	public int getFinalDate() {
		return bean.getFinalDate();
	}

	public String getId() {
		return bean.getBillingStatusHistoryId();
	}

	public String getName() {
		return bean.getBillingStatus().getName();
	}

	public int getStartDate() {
		return bean.getStartDate();
	}

	public void setBillingStatusId(String billingStatusId) {
		bean.setBillingStatus(ArahantSession.getHSU().get(HrBillingStatus.class, billingStatusId));
	}

	public void setFinalDate(int finalDate) {
		bean.setFinalDate(finalDate);
		
	}

	public void setPersonId(String personId) {
		bean.setPerson(ArahantSession.getHSU().get(Person.class,personId));
	}

	public void setStartDate(int startDate) {
		bean.setStartDate(startDate);
	}

	public String create() throws ArahantException {
		bean=new HrBillingStatusHistory();
		return bean.generateId();
	}

	public void load(String key) throws ArahantException {
		bean=ArahantSession.getHSU().get(HrBillingStatusHistory.class, key);
	}
	
	public static BBillingStatusHistory [] list(String personId, int cap)
	{
		return makeArray(ArahantSession.getHSU().createCriteria(HrBillingStatusHistory.class)
				.setMaxResults(cap)
				.orderByDesc(HrBillingStatusHistory.DATE)
				.joinTo(HrBillingStatusHistory.PERSON)
				.eq(Person.PERSONID, personId)
				.list());
	}

}
