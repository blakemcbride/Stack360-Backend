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
import com.arahant.reports.HRCheckListReport;
import com.arahant.utils.*;
import java.io.File;
import java.util.List;

public class BHRCheckListItem extends BusinessLogicBase implements IDBFunctions {

	private HrChecklistItem hrChecklistItem;

	public BHRCheckListItem() {
	}

	public BHRCheckListItem(final String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 * @param item
	 */
	public BHRCheckListItem(final HrChecklistItem item) {
		hrChecklistItem = item;
	}

	public HrChecklistItem getBean() {
		return hrChecklistItem;
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#create()
	 */
	@Override
	public String create() throws ArahantException {
		hrChecklistItem = new HrChecklistItem();
		hrChecklistItem.setOrgGroup(ArahantSession.getHSU().getCurrentCompany());
		hrChecklistItem.generateId();
		return getItemId();
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#delete()
	 */
	@Override
	public void delete() throws ArahantDeleteException {
		ArahantSession.getHSU().delete(hrChecklistItem);
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#insert()
	 */
	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(hrChecklistItem);
	}

	private void internalLoad(final String key) throws ArahantException {
		hrChecklistItem = ArahantSession.getHSU().get(HrChecklistItem.class, key);
	}

	/*
	 * (non-Javadoc) @see
	 * com.arahant.business.interfaces.IDBFunctions#load(java.lang.String)
	 */
	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#update()
	 */
	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(hrChecklistItem);
	}

	/**
	 * @return @see com.arahant.beans.HrChecklistItem#getItemId()
	 */
	public String getItemId() {
		return hrChecklistItem.getItemId();
	}

	/**
	 * @return @see com.arahant.beans.HrChecklistItem#getName()
	 */
	public String getName() {
		return hrChecklistItem.getName();
	}

	/**
	 * @return @see com.arahant.beans.HrChecklistItem#getSeq()
	 */
	public short getSeq() {
		return hrChecklistItem.getSeq();
	}

	/**
	 * @param itemId
	 * @see com.arahant.beans.HrChecklistItem#setItemId(java.lang.String)
	 */
	public void setItemId(final String itemId) {
		hrChecklistItem.setItemId(itemId);
	}

	/**
	 * @param name
	 * @see com.arahant.beans.HrChecklistItem#setName(java.lang.String)
	 */
	public void setName(final String name) {
		hrChecklistItem.setName(name);
	}

	/**
	 * @param seq
	 * @see com.arahant.beans.HrChecklistItem#setSeq(short)
	 */
	public void setSeq(final short seq) {
		hrChecklistItem.setSeq(seq);
	}

	/**
	 * @param hsu
	 * @param ids
	 * @throws ArahantException
	 */
	public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantException {
		for (final String element : ids)
			new BHRCheckListItem(element).delete();
	}

	/**
	 * @param hsu
	 * @return
	 */
	public static String getReport(final HibernateSessionUtil hsu, final String statusId) throws Exception {
		final File fyle = FileSystemUtils.createTempFile("HRCLRept", ".pdf");

		new HRCheckListReport().build(hsu, fyle, statusId);

		return FileSystemUtils.getHTTPPath(fyle);
	}

	public static BHRCheckListItem[] list(final HibernateSessionUtil hsu, final String employeeStatusId) {

		final int now = DateUtils.getDate(new java.util.Date());

		final HibernateCriteriaUtil hcu = hsu.createCriteria(HrChecklistItem.class).le(HrChecklistItem.FIRSTACTIVEDATE, now).ge(HrChecklistItem.LASTACTIVEDATE, now);

		if (!isEmpty(employeeStatusId))
			hcu.joinTo(HrChecklistItem.HREMPLOYEESTATUS).eq(HrEmployeeStatus.STATUSID, employeeStatusId);

		return BHRCheckListItem.makeArray(hcu.list());

	}

	static BHRCheckListItem[] makeArray(final List l) {

		final BHRCheckListItem[] ret = new BHRCheckListItem[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BHRCheckListItem((HrChecklistItem) l.get(loop));
		return ret;
	}

	/**
	 * @return
	 */
	public String getEmployeeStatusId() {

		return hrChecklistItem.getHrEmployeeStatus().getStatusId();
	}

	/**
	 * @param employeeStatusId
	 */
	public void setEmployeeStatusId(final String employeeStatusId) {
		hrChecklistItem.setHrEmployeeStatus(ArahantSession.getHSU().get(HrEmployeeStatus.class, employeeStatusId));
	}

	/**
	 * @return
	 */
	public int getActiveDate() {

		return hrChecklistItem.getFirstActiveDate();
	}

	/**
	 * @return
	 */
	public int getInactiveDate() {
		if (hrChecklistItem.getLastActiveDate() == 99999999)
			return 0;

		return hrChecklistItem.getLastActiveDate();
	}

	/**
	 * @param activeDate
	 */
	public void setActiveDate(final int activeDate) {
		hrChecklistItem.setFirstActiveDate(activeDate);
	}

	/**
	 * @param inactiveDate
	 */
	public void setInactiveDate(int inactiveDate) {
		if (inactiveDate == 0)
			inactiveDate = 99999999;
		hrChecklistItem.setLastActiveDate(inactiveDate);
	}

	public char getResponsibility() {
		return hrChecklistItem.getResponsibility();
	}

	public void setResponsibility(char responsibility) {
		hrChecklistItem.setResponsibility(responsibility);
	}

	public Screen getScreen() {
		return hrChecklistItem.getScreen();
	}

	public void setScreen(String screenId) {
		if (!isEmpty(screenId))
			hrChecklistItem.setScreen(ArahantSession.getHSU().get(Screen.class, screenId));
		else
			hrChecklistItem.setScreen(null);
	}

	public ScreenGroup getScreenGroup() {
		return hrChecklistItem.getScreenGroup();
	}

	public void setScreenGroup(String screenGroupId) {
		if (!isEmpty(screenGroupId))
			hrChecklistItem.setScreenGroup(ArahantSession.getHSU().get(ScreenGroup.class, screenGroupId));
		else
			hrChecklistItem.setScreenGroup(null);
	}

	public CompanyForm getCompanyForm() {
		return hrChecklistItem.getCompanyForm();
	}

	public void setCompanyForm(CompanyForm companyForm) {
		hrChecklistItem.setCompanyForm(companyForm);
	}

	public String getCompanyFormId() {
		return hrChecklistItem.getCompanyFormId();
	}

	public void setCompanyFormId(String companyFormId) {
		if (!isEmpty(companyFormId))
			hrChecklistItem.setCompanyForm(ArahantSession.getHSU().get(CompanyForm.class, companyFormId));
		else
			hrChecklistItem.setCompanyForm(null);
	}

	public static boolean checkComplete(BScreenOrGroup screenOrGroup) throws ArahantException {
		if (!ArahantSession.getHSU().currentlyArahantUser()) {
			BEmployee be = new BEmployee(ArahantSession.getCurrentPerson().getPersonId());
			if (be.getLastActiveStatusHistory() == null)
				return false;
			HibernateCriteriaUtil<HrChecklistItem> hcu = ArahantSession.getHSU().createCriteria(HrChecklistItem.class).eq(HrChecklistItem.HREMPLOYEESTATUS, be.getLastActiveStatusHistory().getHrEmployeeStatus()).eq(HrChecklistItem.RESPONSIBILITY, HrChecklistItem.RESPONSIBILITY_EMPLOYEE);
			if (screenOrGroup != null && screenOrGroup instanceof BScreen)
				hcu.eq(HrChecklistItem.SCREEN, new BScreen(screenOrGroup.getId()).getBean());
			else if (screenOrGroup != null && screenOrGroup instanceof BScreenGroup)
				hcu.eq(HrChecklistItem.SCREEN_GROUP, new BScreenGroup(screenOrGroup.getId()).getBean());
			else
				return false;

			HrChecklistItem cli = hcu.first();
			if (cli != null) {
				if (!ArahantSession.getHSU().createCriteria(HrChecklistDetail.class).eq(HrChecklistDetail.HRCHECKLISTITEM, cli).eq(HrChecklistDetail.EMPLOYEEBYEMPLOYEEID, be.getPerson()).exists()) {
					BHRCheckListDetail cld = new BHRCheckListDetail();
					cld.create();
					cld.setChecklistItemId(cli.getItemId());
					cld.setDateCompleted(DateUtils.now());
					cld.setEmployeeId(be.getPersonId());
					cld.setTimeCompleted(DateUtils.nowTime());
					cld.setSupervisorId(be.getPersonId());
					cld.insert();
				}
				return true;
			}
			return false;
		}
		return false;
	}
}
