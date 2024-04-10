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

import com.arahant.beans.*;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.reports.HRCheckListDetailReport;
import com.arahant.utils.*;
import java.io.File;
import java.util.List;

public class BHRCheckListDetail extends BusinessLogicBase implements IDBFunctions {

	private HrChecklistDetail hrChecklistDetail;

	public BHRCheckListDetail() {
	}

	/**
	 * @param string
	 * @throws ArahantException
	 */
	public BHRCheckListDetail(final String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 * @param item
	 */
	public BHRCheckListDetail(final HrChecklistDetail item) {
		hrChecklistDetail = item;
	}

	public HrChecklistDetail getBean() {
		return hrChecklistDetail;
	}
	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#create()
	 */

	@Override
	public String create() throws ArahantException {

		hrChecklistDetail = new HrChecklistDetail();
		hrChecklistDetail.setChecklistDetailId(IDGenerator.generate(hrChecklistDetail));
		return hrChecklistDetail.getChecklistDetailId();
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#delete()
	 */
	@Override
	public void delete() throws ArahantDeleteException {
		ArahantSession.getHSU().delete(hrChecklistDetail);
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#insert()
	 */
	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(hrChecklistDetail);
	}

	private void internalLoad(final String key) throws ArahantException {
		hrChecklistDetail = ArahantSession.getHSU().get(HrChecklistDetail.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#update()
	 */
	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(hrChecklistDetail);
	}

	/**
	 * @param hsu
	 * @param ids
	 * @throws ArahantException
	 */
	public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantException {
		for (final String element : ids)
			new BHRCheckListDetail(element).delete();
	}

	/**
	 * @param hsu
	 * @return
	 */
	public static String getReport(final HibernateSessionUtil hsu, final String employeeId, final String employeeStatusId) throws Exception {
		final File fyle = FileSystemUtils.createTempFile("HRCLDRept", ".pdf");
		final BHRCheckListDetail[] details = BHRCheckListDetail.list(employeeId, employeeStatusId);

		new HRCheckListDetailReport().build(hsu, fyle, employeeId, employeeStatusId, details);

		return FileSystemUtils.getHTTPPath(fyle);
	}

	/*
	 * TODO: find a way to finish this and make it efficient public static
	 * BHRCheckListDetail[] list(String employeeStatusId) { HibernateSessionUtil
	 * hsu=ArahantSession.getHSU();
	 *
	 * // grab all possible items for the specified status final
	 * HibernateCriteriaUtil hcu = hsu.createCriteria(HrChecklistItem.class); if
	 * (!isEmpty(employeeStatusId))
	 * hcu.joinTo(HrChecklistItem.HREMPLOYEESTATUS).eq(HrEmployeeStatus.STATUSID,
	 * employeeStatusId); final List items=hcu.list();
	 *
	 * // grab details for this employee
	 *
	 * final HibernateCriteriaUtil
	 * hcu2=hsu.createCriteria(HrChecklistDetail.class)
	 * .in(HrChecklistDetail.HRCHECKLISTITEM, items)
	 * .joinTo(HrChecklistDetail.HRCHECKLISTITEM)
	 * .joinTo(HrChecklistItem.HREMPLOYEESTATUS) .eq(HrEmployeeStatus.STATUSID,
	 * employeeStatusId);
	 *
	 * final List details = hcu2.list();
	 *
	 * final BHRCheckListDetail[] ret=new BHRCheckListDetail[items.size()];
	 *
	 * // spin through the items and determine if a detail exists
	 * HrChecklistItem item; HrChecklistDetail detail; BHRCheckListDetail cddt;
	 * for (int idx = 0; idx < items.size(); idx++) { item =
	 * (HrChecklistItem)items.get(idx); cddt = null;
	 *
	 * // look for this item as a detail for (int idx2 = 0; idx2 <
	 * details.size(); idx2++) { detail = (HrChecklistDetail)details.get(idx2);
	 *
	 * // check candidate if
	 * (item.getItemId().equals(detail.getHrChecklistItem().getItemId())) { //
	 * we got a matching detail cddt = new BHRCheckListDetail(detail);
	 * details.remove(idx2); break; } }
	 *
	 * // did we get a matching detail? if (cddt == null) { // no so make an
	 * "empty" one cddt = new BHRCheckListDetail(); cddt.stub();
	 * cddt.setChecklistDetailId(""); cddt.setChecklistItemId(item.getItemId());
	 * cddt.setDateCompleted(0); cddt.setEmployeeId(item.get); }
	 *
	 * ret[idx] = cddt; }
	 *
	 *
	 *
	 *
	 * return ret; }
	 *
	 */
	/**
	 * @param hsu
	 * @param statusId
	 * @return
	 * @throws ArahantException
	 */
	public static BHRCheckListDetail[] list(final String employeeId, final String employeeStatusId) throws ArahantException {

		// grab all possible items for the specified status
		final HibernateCriteriaUtil hcu = ArahantSession.getHSU().createCriteria(HrChecklistItem.class);
		if (!isEmpty(employeeStatusId))
			hcu.joinTo(HrChecklistItem.HREMPLOYEESTATUS).eq(HrEmployeeStatus.STATUSID, employeeStatusId);
		final List items = hcu.list();

		// grab details for this employee
		final Person e = ArahantSession.getHSU().get(Employee.class, employeeId);
		final HibernateCriteriaUtil hcu2 = ArahantSession.getHSU().createCriteria(HrChecklistDetail.class).eq(HrChecklistDetail.EMPLOYEEBYEMPLOYEEID, e).in(HrChecklistDetail.HRCHECKLISTITEM, items);
		final List details = hcu2.list();

		final BHRCheckListDetail[] ret = new BHRCheckListDetail[items.size()];

		// spin through the items and determine if a detail exists
		HrChecklistItem item;
		HrChecklistDetail detail;
		BHRCheckListDetail cddt;
		for (int idx = 0; idx < items.size(); idx++) {
			item = (HrChecklistItem) items.get(idx);
			cddt = null;

			// look for this item as a detail
			for (int idx2 = 0; idx2 < details.size(); idx2++) {
				detail = (HrChecklistDetail) details.get(idx2);

				// check candidate
				if (item.getItemId().equals(detail.getHrChecklistItem().getItemId())) {
					// we got a matching detail
					cddt = new BHRCheckListDetail(detail);
					details.remove(idx2);
					break;
				}
			}

			// did we get a matching detail?
			if (cddt == null) {
				// no so make an "empty" one
				cddt = new BHRCheckListDetail();
				cddt.stub();
				cddt.setChecklistDetailId("");
				cddt.setChecklistItemId(item.getItemId());
				cddt.setDateCompleted(0);
				cddt.setEmployeeId(employeeId);
			}

			ret[idx] = cddt;
		}




		return ret;
	}

	/**
	 *
	 */
	private void stub() {
		hrChecklistDetail = new HrChecklistDetail();
	}

	/**
	 * @return @see com.arahant.beans.HrChecklistDetail#getChecklistDetailId()
	 */
	public String getChecklistDetailId() {
		return hrChecklistDetail.getChecklistDetailId();
	}

	/**
	 * @return @see com.arahant.beans.HrChecklistDetail#getDateCompleted()
	 */
	public int getDateCompleted() {
		return hrChecklistDetail.getDateCompleted();
	}

	/**
	 * @param checklistDetailId
	 * @see
	 * com.arahant.beans.HrChecklistDetail#setChecklistDetailId(java.lang.String)
	 */
	public void setChecklistDetailId(final String checklistDetailId) {
		hrChecklistDetail.setChecklistDetailId(checklistDetailId);
	}

	/**
	 * @param dateCompleted
	 * @see com.arahant.beans.HrChecklistDetail#setDateCompleted(int)
	 */
	public void setDateCompleted(final int dateCompleted) {
		hrChecklistDetail.setDateCompleted(dateCompleted);
	}

	/**
	 * @param employeeId
	 */
	public void setEmployeeId(final String employeeId) {
		hrChecklistDetail.setEmployeeByEmployeeId(ArahantSession.getHSU().get(Employee.class, employeeId));
	}

	/**
	 * @param supervisorId
	 */
	public void setSupervisorId(final String supervisorId) {
		Employee emp = ArahantSession.getHSU().get(Employee.class, supervisorId);
		if (emp == null)
			throw new ArahantWarning("Only employees may complete check list items.");
		hrChecklistDetail.setEmployeeBySupervisorId(emp);
	}

	/**
	 * @param checklistItemId
	 */
	public void setChecklistItemId(final String checklistItemId) {
		hrChecklistDetail.setHrChecklistItem(ArahantSession.getHSU().get(HrChecklistItem.class, checklistItemId));
	}

	/**
	 * @return
	 */
	public String getName() {

		return hrChecklistDetail.getHrChecklistItem().getName();
	}

	/**
	 * @return
	 */
	public String getSupervisorFName() {

		if (hrChecklistDetail.getEmployeeBySupervisorId() != null)
			return hrChecklistDetail.getEmployeeBySupervisorId().getFname();
		return "";
	}

	/**
	 * @return
	 */
	public String getSupervisorNameLFM() {

		if (hrChecklistDetail.getEmployeeBySupervisorId() != null)
			return hrChecklistDetail.getEmployeeBySupervisorId().getNameLFM();
		return "";
	}

	/**
	 * @return
	 */
	public String getSupervisorLName() {

		if (hrChecklistDetail.getEmployeeBySupervisorId() != null)
			return hrChecklistDetail.getEmployeeBySupervisorId().getLname();
		return "";
	}

	/**
	 * @return
	 */
	public String getSupervisorId() {
		if (hrChecklistDetail.getEmployeeBySupervisorId() != null)
			return hrChecklistDetail.getEmployeeBySupervisorId().getPersonId();
		return "";
	}

	/**
	 * @return
	 */
	public String getChecklistItemId() {

		return hrChecklistDetail.getHrChecklistItem().getItemId();
	}

	public int getTimeCompleted() {
		return hrChecklistDetail.getTimeCompleted();
	}

	public void setTimeCompleted(final int timeCompleted) {
		hrChecklistDetail.setTimeCompleted(timeCompleted);
	}
}
