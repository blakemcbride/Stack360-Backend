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
import com.arahant.beans.OrgGroup;
import com.arahant.beans.ProspectLog;
import com.arahant.beans.SalesActivity;
import com.arahant.beans.SalesActivityResult;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.services.standard.crm.salesTasks.ProspectLogResults;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BProspectLog extends SimpleBusinessObjectBase<ProspectLog> {

	public BProspectLog() {
	}

	public BProspectLog(String id) {
		super(id);
	}

	public BProspectLog(ProspectLog o) {
		bean = o;
	}

	public static void delete(String[] ids) {
		for (String id : ids)
			new BProspectLog(id).delete();
	}

	@Override
	public String create() throws ArahantException {
		bean = new ProspectLog();
		return bean.generateId();
	}

	@Override
	public void insert() throws ArahantException {
		insertChecks();

		if (bean.getSalesActivity() == null)
			bean.setSalesActivity(BSalesActivity.findOrMake("None").getBean());

		if (bean.getEmployee() == null) {
			BEmployee be = new BEmployee(BPerson.getCurrent());
			bean.setEmployee(be.getEmployee());
		}

		if (bean.getSalesActivityResult() != null) {
			int count = ArahantSession.getHSU().createCriteria(ProspectLog.class).eq(ProspectLog.SALES_ACTIVITY_RESULT, bean.getSalesActivityResult()).eq(ProspectLog.ORG_GROUP, bean.getOrgGroup()).count();

			if (count == 0)
				new BProspectCompany(bean.getOrgGroup().getCompanyId()).setNextContactDate(DateUtils.addDays(bean.getContactDate(), bean.getSalesActivityResult().getFirstFollowUpDays()));
			else if (count == 1)
				if (bean.getSalesActivityResult().getSecondFollowUpDays() > 0)
					new BProspectCompany(bean.getOrgGroup().getCompanyId()).setNextContactDate(DateUtils.addDays(bean.getContactDate(), bean.getSalesActivityResult().getSecondFollowUpDays()));
				else
					new BProspectCompany(bean.getOrgGroup().getCompanyId()).setNextContactDate(DateUtils.addDays(bean.getContactDate(), bean.getSalesActivityResult().getFirstFollowUpDays()));
			else if (count >= 2)
				if (bean.getSalesActivityResult().getThirdFollowUpDays() > 0)
					new BProspectCompany(bean.getOrgGroup().getCompanyId()).setNextContactDate(DateUtils.addDays(bean.getContactDate(), bean.getSalesActivityResult().getThirdFollowUpDays()));
				else if (bean.getSalesActivityResult().getSecondFollowUpDays() > 0)
					new BProspectCompany(bean.getOrgGroup().getCompanyId()).setNextContactDate(DateUtils.addDays(bean.getContactDate(), bean.getSalesActivityResult().getSecondFollowUpDays()));
				else
					new BProspectCompany(bean.getOrgGroup().getCompanyId()).setNextContactDate(DateUtils.addDays(bean.getContactDate(), bean.getSalesActivityResult().getFirstFollowUpDays()));
		}

		super.insert();
	}

	@Override
	public void insertChecks() throws ArahantException {
		if (ArahantSession.getHSU().createCriteria(ProspectLog.class).eq(ProspectLog.CONTACT_DATE, getContactDate()).eq(ProspectLog.TIME, getContactTime()).eq(ProspectLog.ORG_GROUP, bean.getOrgGroup()).exists())
			throw new ArahantWarning("There is already a Prospect Log created for this Prospect on the same date/time.");
	}

	public int getContactDate() {
		return bean.getContactDate();
	}

	public String getContactText() {
		return bean.getContactTxt();
	}

	public int getContactTime() {
		return bean.getContactTime();
	}

	public String getEmployees() {
		return bean.getEmployees();
	}

	public String getProspectEmployees() {
		return bean.getProspectEmployees();
	}

	public String getProspectLogId() {
		return bean.getProspectLogId();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ProspectLog.class, key);
	}

	public static BProspectLog[] makeArray(List<ProspectLog> l) {
		BProspectLog[] ret = new BProspectLog[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BProspectLog(l.get(loop));
		return ret;
	}

	public void setContactDate(int contactDate) {
		bean.setContactDate(contactDate);
	}

	public void setContactText(String contactText) {
		bean.setContactTxt(contactText);
	}

	public void setContactTime(int contactTime) {
		bean.setContactTime(contactTime);
	}

	public void setEmployees(String employees) {
		bean.setEmployees(employees);
	}

	public void setProspectEmployees(String prospectEmployees) {
		bean.setProspectEmployees(prospectEmployees);
	}

	public void setOrgGroupId(String orgGroupId) {
		bean.setOrgGroup(ArahantSession.getHSU().get(OrgGroup.class, orgGroupId));
	}

	public SalesActivity getSalesActivity() {
		return bean.getSalesActivity();
	}

	public void setSalesActivity(SalesActivity salesActivity) {
		bean.setSalesActivity(salesActivity);
	}

	public Employee getEmployee() {
		return bean.getEmployee();
	}

	public void setEmployee(Employee employee) {
		bean.setEmployee(employee);
	}

	public static BProspectLog[] list(String orgGroupId) {

		return makeArray(ArahantSession.getHSU().createCriteria(ProspectLog.class)
				.orderByDesc(ProspectLog.CONTACT_DATE)
				.orderByDesc(ProspectLog.TIME)
				.joinTo(ProspectLog.ORG_GROUP)
				.eq(OrgGroup.ORGGROUPID, orgGroupId)
				.list());
	}

	public static List<ProspectLogResults> searchProspectLogsByTask(int max, String taskName, boolean completed, boolean incomplete) {
		//Get all prospect logs that have a result
		HibernateCriteriaUtil<ProspectLog> hcu = ArahantSession.getHSU().createCriteria(ProspectLog.class)
				.orderBy(ProspectLog.ORG_GROUP)
				.notNull(ProspectLog.SALES_ACTIVITY_RESULT);

		if (!completed)
			hcu.eq(ProspectLog.TASK_COMPLETION_DATE, 0);

		if (!incomplete)
			hcu.gt(ProspectLog.TASK_COMPLETION_DATE, 0);

		HibernateScrollUtil<ProspectLog> scr = hcu.scroll();

		List<ProspectLogResults> results = new ArrayList<ProspectLogResults>();

		int resultCount = 0;

		//Loop through each prospect log
		while (scr.next()) {
			HibernateCriteriaUtil<ProspectLog> hcu2 = ArahantSession.getHSU().createCriteria(ProspectLog.class)
					.orderBy(ProspectLog.CONTACT_DATE)
					.orderBy(ProspectLog.TIME)
					.eq(ProspectLog.SALES_ACTIVITY_RESULT, scr.get().getSalesActivityResult())
					.eq(ProspectLog.ORG_GROUP, scr.get().getOrgGroup());

			HibernateScrollUtil<ProspectLog> scr2 = hcu2.scroll();

			SalesActivityResult sar = scr.get().getSalesActivityResult();

			resultCount = 0;

			while (scr2.next()) {
				resultCount++;

				if (scr.get() == scr2.get())
					switch (resultCount) {
						case 1:
							if (!isEmpty(sar.getFirstFollowUpTask()))
								if (sar.getFirstFollowUpTask().equals(taskName) || isEmpty(taskName)) {
									ProspectLogResults plr = new ProspectLogResults();
									plr.setOrgGroup(scr.get().getOrgGroup());
									plr.setTaskName(sar.getFirstFollowUpTask());
									plr.setDateCompleted(scr.get().getTaskCompletionDate());
									plr.setProspectLogId(scr.get().getProspectLogId());
									plr.setSalesActivityResult(sar);
									plr.setSalesActivity(sar.getSalesActivity());
									results.add(plr);
								}
							break;
						case 2:
							if (!isEmpty(sar.getSecondFollowUpTask()))
								if (sar.getSecondFollowUpTask().equals(taskName) || isEmpty(taskName)) {
									ProspectLogResults plr = new ProspectLogResults();
									plr.setOrgGroup(scr.get().getOrgGroup());
									plr.setTaskName(sar.getSecondFollowUpTask());
									plr.setDateCompleted(scr.get().getTaskCompletionDate());
									plr.setProspectLogId(scr.get().getProspectLogId());
									plr.setSalesActivityResult(sar);
									plr.setSalesActivity(sar.getSalesActivity());
									results.add(plr);
								}
							break;
						case 3:
							if (!isEmpty(sar.getThirdFollowUpTask()))
								if (sar.getThirdFollowUpTask().equals(taskName) || isEmpty(taskName)) {
									ProspectLogResults plr = new ProspectLogResults();
									plr.setOrgGroup(scr.get().getOrgGroup());
									plr.setTaskName(sar.getThirdFollowUpTask());
									plr.setDateCompleted(scr.get().getTaskCompletionDate());
									plr.setProspectLogId(scr.get().getProspectLogId());
									plr.setSalesActivityResult(sar);
									plr.setSalesActivity(sar.getSalesActivity());
									results.add(plr);
								}
							break;
					}
			}

		}

		return results;
	}

	public SalesActivityResult getSalesActivityResult() {
		return bean.getSalesActivityResult();
	}

	public void setSalesActivityResult(SalesActivityResult salesActivityResult) {
		bean.setSalesActivityResult(salesActivityResult);
	}

	public Date getEntryDate() {
		return bean.getEntryDate();
	}

	public void setEntryDate(Date entryDate) {
		bean.setEntryDate(entryDate);
	}

	public int getTaskCompletionDate() {
		return bean.getTaskCompletionDate();
	}

	public void setTaskCompletionDate(int taskCompletionDate) {
		bean.setTaskCompletionDate(taskCompletionDate);
	}
//	public static void main(String[] args)
//	{
//		for (ProspectCompany pc  : ArahantSession.getHSU().createCriteria(ProspectCompany.class).list())
//		{
//			BProspectCompany bpc = new BProspectCompany(pc);
//			if (pc.getLastLogDateWithResult()!=0)
//			{
//				if (bpc.getLastLog().getSalesActivityResult().getFollowUpDays() > 0)
//				{
//					bpc.setNextContactDate(DateUtils.addDays(bpc.getLastLog().getContactDate(), bpc.getLastLog().getSalesActivityResult().getFollowUpDays()));
//					bpc.update();
//					ArahantSession.getHSU().commitTransaction();
//				}
//
//			}
//		}
//
//	}
}
