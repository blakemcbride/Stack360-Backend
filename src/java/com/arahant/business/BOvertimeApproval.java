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
import com.arahant.beans.OvertimeApproval;
import com.arahant.beans.Timesheet;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import java.util.List;

/**
 *
 */
public class BOvertimeApproval extends SimpleBusinessObjectBase<OvertimeApproval> {

	public static void delete(String[] ids) {
		for (String id : ids)
			new BOvertimeApproval(id).delete();
	}

	@Override
	public void delete()
	{
		if (bean.getOvertimeDate()<DateUtils.now())
			throw new ArahantWarning("You may not delete an approval for a past date.");
		super.delete();
	}

	public int getDate() {
		return bean.getOvertimeDate();
	}

	public void setDate(int date) {
		bean.setOvertimeDate(date);
	}

	public void setHours(float hours) {
		bean.setOvertimeHours(hours);
	}

	public void setPersonId(String personId) {
		bean.setEmployee(ArahantSession.getHSU().get(Employee.class, personId));
	}

	@Override
	public void insertChecks()
	{
		if (bean.getOvertimeDate()<DateUtils.now())
			throw new ArahantWarning("You may not create an approval for a past date.");

		if (ArahantSession.getHSU().createCriteria(OvertimeApproval.class)
			.ne(OvertimeApproval.ID,bean.getOvertimeApprovalId())
			.eq(OvertimeApproval.EMPLOYEE, bean.getEmployee())
			.eq(OvertimeApproval.WORK_DATE, bean.getOvertimeDate())
			.exists())
			throw new ArahantWarning("There is already an overtime allowance for that person on that date.");
	}

	@Override
	public void updateChecks()
	{
		if (bean.getOvertimeDate()<DateUtils.now())
			throw new ArahantWarning("You may not edit an approval for a past date.");

		if (ArahantSession.getHSU().createCriteria(OvertimeApproval.class)
			.ne(OvertimeApproval.ID,bean.getOvertimeApprovalId())
			.eq(OvertimeApproval.EMPLOYEE, bean.getEmployee())
			.eq(OvertimeApproval.WORK_DATE, bean.getOvertimeDate())
			.exists())
			throw new ArahantWarning("There is already an overtime allowance for that person on that date.");
	}

	public BOvertimeApproval()
	{
		super();
	}
	public BOvertimeApproval(String id)
	{
		super(id);
	}
	public BOvertimeApproval(OvertimeApproval o)
	{
		super();
		bean=o;
	}

	@Override
	public String create() throws ArahantException {
		bean=new OvertimeApproval();
		bean.setSupervisor(BPerson.getCurrent().getBEmployee().getEmployee());
		bean.setRecordChangeDate(new java.util.Date());
		return bean.generateId();
	}

	public double getApprovedHours() {
		return bean.getOvertimeHours();
	}

	public String getDateFormatted() {
		return DateUtils.getDateFormatted(bean.getOvertimeDate());
	}

	public double getHoursTaken() {
		double r=ArahantSession.getHSU().createCriteria(Timesheet.class)
			.eq(Timesheet.PERSON, bean.getEmployee())
			.eq(Timesheet.WORKDATE, bean.getOvertimeDate())
			.sum(Timesheet.TOTALHOURS)
			.doubleVal();

		r-=bean.getEmployee().getLengthOfWorkDay();

		if (r<0)
			r=0;

		return r;
	}

	public String getId() {
		return bean.getOvertimeApprovalId();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean=ArahantSession.getHSU().get(OvertimeApproval.class, key);
	}

	public static BOvertimeApproval [] list(String personId, int startDate, int endDate)
	{
		return makeArray(ArahantSession.getHSU().createCriteria(OvertimeApproval.class)
			.dateBetween(OvertimeApproval.WORK_DATE, startDate, endDate)
			.orderByDesc(OvertimeApproval.WORK_DATE)
			.joinTo(OvertimeApproval.EMPLOYEE)
			.eq(Employee.PERSONID, personId)
			.list());
	}

	static BOvertimeApproval[] makeArray(List<OvertimeApproval> l) {
		BOvertimeApproval[] ret=new BOvertimeApproval[l.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=new BOvertimeApproval(l.get(loop));
		return ret;
	}
}
