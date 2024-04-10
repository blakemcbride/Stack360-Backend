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
import com.arahant.reports.HRWageHistoryReport;
import com.arahant.utils.*;
import java.io.File;
import java.util.Date;
import java.util.List;

public class BHRWage extends BusinessLogicBase implements IDBFunctions {

	private HrWage hrWage;

	public BHRWage() {
	}

	/**
	 * @param string
	 * @throws ArahantException
	 */
	public BHRWage(final String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 * @param account
	 */
	public BHRWage(final HrWage account) {
		hrWage = account;
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#create()
	 */
	@Override
	public String create() throws ArahantException {
		hrWage = new HrWage();
		hrWage.generateId();

		return getWageId();
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#delete()
	 */
	@Override
	public void delete() throws ArahantDeleteException {
		ArahantSession.getHSU().delete(hrWage);
	}

	public String getWageTypeId() {
		if (hrWage.getWageType() == null)
			return "";
		return hrWage.getWageType().getWageTypeId();
	}

	public String getWageTypeName() {
		if (hrWage.getWageType() == null)
			return "";
		return hrWage.getWageType().getWageName();
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#insert()
	 */
	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(hrWage);
	}

	private void internalLoad(final String key) throws ArahantException {
		hrWage = ArahantSession.getHSU().get(HrWage.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	public void setWageTypeId(String wageTypeId) {
		hrWage.setWageType(ArahantSession.getHSU().get(WageType.class, wageTypeId));
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#update()
	 */
	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(hrWage);
	}

	/**
	 * @return @see com.arahant.beans.HrWage#getWageId()
	 */
	public String getWageId() {
		return hrWage.getWageId();
	}

	/**
	 * @param WageId
	 * @see com.arahant.beans.HrWage#setWageId(java.lang.String)
	 */
	public void setWageId(final String WageId) {
		hrWage.setWageId(WageId);
	}

	/**
	 * @param hsu
	 * @param ids
	 * @throws ArahantException
	 */
	public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantException {
		for (final String element : ids)
			new BHRWage(element).delete();
	}

	/**
	 * @param hsu
	 * @param employeeId
	 * @return
	 */
	public static BHRWage[] list(final HibernateSessionUtil hsu, final String employeeId) {

		final List l = hsu.createCriteria(HrWage.class).orderByDesc(HrWage.EFFECTIVEDATE).joinTo(HrWage.EMPLOYEE).eq(Person.PERSONID, employeeId).list();

		final BHRWage[] ret = new BHRWage[l.size()];
		for (int loop = 0; loop < l.size(); loop++)
			ret[loop] = new BHRWage((HrWage) l.get(loop));
		return ret;
	}

	/**
	 * @param employeeId
	 * @return
	 */
	public static String getReport(final HibernateSessionUtil hsu, final String employeeId) throws Exception {
		final File fyle = FileSystemUtils.createTempFile("HRWageHistRept", ".pdf");

		new HRWageHistoryReport().build(hsu, fyle, employeeId);

		return FileSystemUtils.getHTTPPath(fyle);
	}

	/**
	 * @return @see com.arahant.beans.HrWage#getEffectiveDate()
	 */
	public int getEffectiveDate() {
		return hrWage.getEffectiveDate();
	}

	/**
	 * @return @see com.arahant.beans.HrWage#getNotes()
	 */
	public String getNotes() {
		if (hrWage.getNotes() == null)
			return "";
		return hrWage.getNotes();
	}

	/**
	 * @return @see com.arahant.beans.HrWage#getWageAmount()
	 */
	public double getWageAmount() {
		return hrWage.getWageAmount();
	}

	/**
	 * @return @see com.arahant.beans.HrWage#getWageType()
	 */
	public short getWageType() {
		if (hrWage.getWageType() == null)
			return 0;
		return hrWage.getWageType().getPeriodType();
	}

	/**
	 * @param effectiveDate
	 * @see com.arahant.beans.HrWage#setEffectiveDate(int)
	 */
	public void setEffectiveDate(final int effectiveDate) {
		hrWage.setEffectiveDate(effectiveDate);
	}

	/**
	 * @param notes
	 * @see com.arahant.beans.HrWage#setNotes(java.lang.String)
	 */
	public void setNotes(final String notes) {
		hrWage.setNotes(notes);
	}

	/**
	 * @param wageAmount
	 * @see com.arahant.beans.HrWage#setWageAmount(double)
	 */
	public void setWageAmount(final double wageAmount) {
		hrWage.setWageAmount(wageAmount);
	}

	/**
	 * @return
	 */
	public String getPositionName() {
		if (hrWage.getHrPosition() != null)
			return hrWage.getHrPosition().getName();
		return "";
	}

	/**
	 * @return
	 */
	public String getPositionId() {
		if (hrWage.getHrPosition() != null)
			return hrWage.getHrPosition().getPositionId();
		return "";
	}

	/**
	 * @param positionId
	 */
	public void setPositionId(final String positionId) {
		hrWage.setHrPosition(ArahantSession.getHSU().get(HrPosition.class, positionId));
	}

	/**
	 * @param employeeId
	 */
	public void setEmployeeId(final String employeeId) {
		hrWage.setEmployee(ArahantSession.getHSU().get(Employee.class, employeeId));
	}

	/**
	 * @return
	 */
	public boolean getReadOnly() {
		java.util.Date dt = DateUtils.getDate(hrWage.getEffectiveDate());
		final java.util.Date now = new Date();
		dt = new java.util.Date(dt.getTime() + (30 * 24 * 60 * 60 * 1000));
		return now.before(dt);
	}

	public static double getLatestWageAmountAnnual(String employeeId) {
		HibernateCriteriaUtil<HrWage> hcu = ArahantSession.getHSU().createCriteria(HrWage.class).orderByDesc(HrWage.EFFECTIVEDATE);
		hcu.joinTo(HrWage.WAGETYPE).ne(WageType.PERIOD_TYPE, WageType.PERIOD_ONE_TIME);
		hcu.joinTo(HrWage.EMPLOYEE).eq(Employee.PERSONID, employeeId);
		HrWage w = hcu.first();

		if (w.getWageType().getPeriodType() == WageType.PERIOD_SALARY)
			return w.getWageAmount();
		if (w.getWageType().getPeriodType() == WageType.PERIOD_HOURLY)
			return w.getWageAmount() * 2080;
		return 0;
	}

	public static double getLatestWageAmountAnnualRounded(String employeeId) {
		int wage = (int) getLatestWageAmountAnnual(employeeId);

		if (wage % 1000 > 0)
			wage = ((wage / 1000) + 1) * 1000;

		return wage;
	}

	public static double getLatestWageAmountAnnualRoundedWithCap(String employeeId, int cap) {
		int wage = (int) getLatestWageAmountAnnual(employeeId);

		if (wage % 1000 > 0)
			wage = ((wage / 1000) + 1) * 1000;

		if (wage > cap)
			wage = cap;

		return wage;
	}

	public static double getLatestWageAmountWeeklyWithCap(String employeeId, int cap) {
		double wage = getLatestWageAmountAnnual(employeeId);

		if (wage > cap)
			wage = cap;

		return wage;
	}
}
