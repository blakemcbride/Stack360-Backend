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
 *
*/


package com.arahant.business;

import com.arahant.beans.Employee;
import com.arahant.beans.HrEmplStatusHistory;
import com.arahant.beans.HrEmployeeStatus;
import com.arahant.beans.Person;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.reports.HRStatusHistoryReport;
import com.arahant.utils.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BHREmplStatusHistory extends BusinessLogicBase implements IDBFunctions, Comparable<BHREmplStatusHistory> {

	private HrEmplStatusHistory hrEmplStatusHistory;

	public BHREmplStatusHistory() {
	}

	public BHREmplStatusHistory(final String key) throws ArahantException {
		internalLoad(key);
	}

	public BHREmplStatusHistory(final HrEmplStatusHistory x) {
		hrEmplStatusHistory = x;
	}

	public HrEmplStatusHistory getBean() {
		return hrEmplStatusHistory;
	}

	@Override
	public String create() throws ArahantException {
		hrEmplStatusHistory = new HrEmplStatusHistory();
		hrEmplStatusHistory.setStatusHistId(IDGenerator.generate(hrEmplStatusHistory));
		hrEmplStatusHistory.setRecordChangeDate(new Date());
		return hrEmplStatusHistory.getStatusHistId();
	}

	@Override
	public void delete() throws ArahantDeleteException {
		this.delete(false);
	}

	public void delete(boolean skipCascadedUpdates) throws ArahantDeleteException {
		ArahantSession.getHSU().delete(hrEmplStatusHistory);

		if (!skipCascadedUpdates) {
			BEmployee bEmployee = new BEmployee(hrEmplStatusHistory.getEmployee().getPersonId());
			bEmployee.updateForStatusChange(this, false);
		}
	}

	@Override
	public void insert() throws ArahantException {
		this.insert(false);
	}

	public void insert(boolean skipCascadedUpdates) throws ArahantException {
		validate();
		ArahantSession.getHSU().insert(hrEmplStatusHistory);

		if (!skipCascadedUpdates) {
			BEmployee bEmployee = new BEmployee(hrEmplStatusHistory.getEmployee().getPersonId());
			bEmployee.updateForStatusChange(this, true);
		}
	}

	public void validate() {
		HrEmplStatusHistory hist = ArahantSession.getHSU().createCriteria(HrEmplStatusHistory.class)
				.eq(HrEmplStatusHistory.EMPLOYEE, hrEmplStatusHistory.getEmployee())
                .ne(HrEmplStatusHistory.STATUSHISTID, hrEmplStatusHistory.getStatusHistId())
                .eq(HrEmplStatusHistory.EFFECTIVEDATE, hrEmplStatusHistory.getEffectiveDate())
                .first();
		if (hist != null)
			throw new ArahantWarning("Status effective date conflicts with employee status of " + hist.getHrEmployeeStatus().getName() +
					" on the same date.");
	}

	private void internalLoad(final String key) throws ArahantException {
		hrEmplStatusHistory = ArahantSession.getHSU().get(HrEmplStatusHistory.class, key);
		if (hrEmplStatusHistory == null)
			throw new ArahantException("Bad key passed for status history '" + key + "'");
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	@Override
	public void update() throws ArahantException {
		this.update(false);
	}

	public void update(boolean skipCascadedUpdates) throws ArahantException {
		validate();
		ArahantSession.getHSU().saveOrUpdate(hrEmplStatusHistory);

		if (!skipCascadedUpdates) {
			BEmployee bEmployee = new BEmployee(hrEmplStatusHistory.getEmployee().getPersonId());
			bEmployee.updateForStatusChange(this, false);
		}
	}

	public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantException {
		BHREmplStatusHistory.delete(hsu, ids, false);
	}

	public static void delete(final HibernateSessionUtil hsu, final String[] ids, boolean skipCascadedUpdates) throws ArahantException {
		BEmployee bEmployee = null;

		for (final String element : ids) {
			BHREmplStatusHistory bStatusHistory = new BHREmplStatusHistory(element);
			bEmployee = new BEmployee(bStatusHistory.hrEmplStatusHistory.getEmployee().getPersonId());
			bStatusHistory.delete(true); // we will directly do the cascaded updates, cutting down redundant processing
		}

		if (bEmployee != null) {
			if (isEmpty(bEmployee.getLastStatusId()))
				throw new ArahantWarning("At least one Employee Status is required");

			// TODO:
			// This trigger has logic problems.  Blake is commenting it out since it's not used now anyway.
            // The problem is it is trying to read a record that was just deleted
            // Also, is the trigger being passed the new status or the old?  It should get both!
            // This code works in the Williamson county repo.  Fiind out why.
//			if (!skipCascadedUpdates)
//				bEmployee.updateForStatusChange(new BHREmplStatusHistory(bEmployee.getLastStatusHistory().getStatusHistId()), false);
		}
	}

	public static BHREmplStatusHistory[] list(final HibernateSessionUtil hsu, final String employeeId) {

		HibernateCriteriaUtil<HrEmplStatusHistory> hcu = hsu.createCriteria(HrEmplStatusHistory.class).orderByDesc(HrEmplStatusHistory.EFFECTIVEDATE);

		hcu.joinTo(HrEmplStatusHistory.HREMPLOYEESTATUS).eq(HrEmployeeStatus.ORG_GROUP, hsu.getCurrentCompany());

		hcu.joinTo(HrEmplStatusHistory.EMPLOYEE).eq(Person.PERSONID, employeeId);

		return makeArray(hcu.list());
	}

	static BHREmplStatusHistory[] makeArray(final List<HrEmplStatusHistory> l) {
		final BHREmplStatusHistory[] ret = new BHREmplStatusHistory[l.size()];
		for (int loop = 0; loop < l.size(); loop++)
			ret[loop] = new BHREmplStatusHistory(l.get(loop));

		final List<BHREmplStatusHistory> hl = new ArrayList<BHREmplStatusHistory>(ret.length);

		Collections.addAll(hl, ret);

		//java.util.Collections.sort(hl);

		return hl.toArray(ret);
	}

	/**
	 * @param employeeId
	 * @return
	 */
	public static String getReport(final HibernateSessionUtil hsu, final String employeeId) throws Exception {
		final File fyle = FileSystemUtils.createTempFile("HRStaHistRept", ".pdf");
		new HRStatusHistoryReport().build(hsu, fyle, employeeId);
		return FileSystemUtils.getHTTPPath(fyle);
	}

	/**
	 * @param employeeId
	 */
	public void setEmployeeId(final String employeeId) {
		hrEmplStatusHistory.setEmployee(ArahantSession.getHSU().get(Employee.class, employeeId));
	}

	public int getEffectiveDate() {
		if (hrEmplStatusHistory.getHrEmployeeStatus().getDateType() == 'F')
			return DateUtils.addDays(hrEmplStatusHistory.getEffectiveDate(), -1);
		return hrEmplStatusHistory.getEffectiveDate();
	}

	public HrEmployeeStatus getHrEmployeeStatus() {
		return hrEmplStatusHistory.getHrEmployeeStatus();
	}

	public String getNotes() {
		return (hrEmplStatusHistory.getNotes() != null) ? hrEmplStatusHistory.getNotes() : "";
	}

	public String getStatusHistId() {
		return hrEmplStatusHistory.getStatusHistId();
	}

	/**
	 * @param effectiveDate
	 * @see com.arahant.beans.HrEmplStatusHistory#setEffectiveDate(int)
	 */
	public void setEffectiveDate(final int effectiveDate) {
		hrEmplStatusHistory.setEffectiveDate(effectiveDate);
		doAdustment();
	}

	/**
	 * @param hrEmployeeStatus
	 * @see
	 * com.arahant.beans.HrEmplStatusHistory#setHrEmployeeStatus(com.arahant.beans.HrEmployeeStatus)
	 */
	public void setHrEmployeeStatus(final HrEmployeeStatus hrEmployeeStatus) {
		hrEmplStatusHistory.setHrEmployeeStatus(hrEmployeeStatus);
	}

	/**
	 * @param notes
	 * @see com.arahant.beans.HrEmplStatusHistory#setNotes(java.lang.String)
	 */
	public void setNotes(final String notes) {
		hrEmplStatusHistory.setNotes(notes);
	}

	public void setStatusHistId(final String statusHistId) {
		hrEmplStatusHistory.setStatusHistId(statusHistId);
	}

	public String getStatusName() {
		HrEmployeeStatus es = hrEmplStatusHistory.getHrEmployeeStatus();
		return es == null ? "" : es.getName();
	}

	public void setStatusId(final String statusId) {
		hrEmplStatusHistory.setHrEmployeeStatus(ArahantSession.getHSU().get(HrEmployeeStatus.class, statusId));
		doAdustment();
	}

	public boolean getReadOnly() {
		java.util.Date dt = DateUtils.getDate(hrEmplStatusHistory.getEffectiveDate());
		final java.util.Date now = new Date();
		dt = new java.util.Date(dt.getTime() + (30 * 24 * 60 * 60 * 1000));
		return now.before(dt);
	}

	public String getStatusId() {
		try {
			return hrEmplStatusHistory.getHrEmployeeStatus().getStatusId();
		} catch (final Exception e) {
			return "";
		}
	}

	private void doAdustment() {
		if (hrEmplStatusHistory.getEffectiveDate() != 0 && hrEmplStatusHistory.getHrEmployeeStatus() != null && hrEmplStatusHistory.getHrEmployeeStatus().getDateType() == 'F')
			hrEmplStatusHistory.setEffectiveDate(DateUtils.addDays(hrEmplStatusHistory.getEffectiveDate(), 1));
	}

	public void setEmployee(final Person employee) {
		hrEmplStatusHistory.setEmployee(employee);
	}

	@Override
	public int compareTo(final BHREmplStatusHistory o) {

		final int ret = getEffectiveDate() - o.getEffectiveDate();

		if (ret == 0) {
			if (hrEmplStatusHistory.getHrEmployeeStatus().getDateType() == 'F')
				return -1;
			if (o.getHrEmployeeStatus().getDateType() == 'F')
				return 1;
		}
		return ret;
	}

	public BEmployee getEmployee() {
		return new BEmployee(
				ArahantSession.getHSU().createCriteria(Employee.class).joinTo(Employee.HREMPLSTATUSHISTORIES).eq(HrEmplStatusHistory.STATUSHISTID, this.getStatusHistId()).first());
	}

	public static BHREmplStatusHistory[] listOnboardingEmps() {
		//Create an employee status onboarding if it does not already exist.
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		if (!hsu.createCriteria(HrEmployeeStatus.class).eq(HrEmployeeStatus.NAME, "Onboarding").exists()) {
			BHREmployeeStatus bes = new BHREmployeeStatus();
			bes.create();
			bes.setActiveFlag('Y');
			bes.setBenefitType('B');
			bes.setName("Onboarding");
			bes.setOrgGroupId(hsu.getCurrentCompany().getOrgGroupId());
			bes.insert();
		}

		return makeArray(hsu.createCriteria(HrEmplStatusHistory.class).orderBy(HrEmplStatusHistory.EFFECTIVEDATE).joinTo(HrEmplStatusHistory.HREMPLOYEESTATUS).eq(HrEmployeeStatus.NAME, "Onboarding").list());
	}

	public Date getRecordChangeDate() {
		return hrEmplStatusHistory.getRecordChangeDate();
	}
}
