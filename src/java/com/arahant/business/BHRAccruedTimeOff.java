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
import com.arahant.utils.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 *
 *
 */
public class BHRAccruedTimeOff extends BusinessLogicBase implements IDBFunctions {
	
	private static final transient ArahantLogger logger = new ArahantLogger(BHRAccruedTimeOff.class);
	private HrAccrual hrAccrual;
	private boolean resetTotal = false;
	private double runningTotal = 0;
	private double capValue = 0;
	
	public BHRAccruedTimeOff() {
	}

	/**
	 * @param accrual
	 */
	public BHRAccruedTimeOff(final HrAccrual accrual) {
		hrAccrual = accrual;
	}

	public BHRAccruedTimeOff(final HrAccrual accrual, double runningTotal) {
		hrAccrual = accrual;
		this.runningTotal = (float) ((int) ((runningTotal) * 100)) / 100;
	}

	BHRAccruedTimeOff(Timesheet t, double runningTotal) {
		hrAccrual = new HrAccrual();
		hrAccrual.setAccrualDate(t.getEndDate());
		hrAccrual.setAccrualHours(-t.getTotalHours());
		hrAccrual.setDescription(t.getDescription());
		hrAccrual.setEmployee(t.getPerson());
		hrAccrual.setHrBenefit(new BHRBenefit(new BTimesheet(t).getBenefitId()).getBean());
		this.runningTotal = (double) ((int) ((runningTotal) * 100)) / 100;
	}

	public static BProject[] listAccrualProjects(String personId) {
		BEmployee be = new BEmployee(personId);
		BHRBenefitJoin[] bja = be.getApprovedBenefitJoins();
		LinkedList<Project> projList = new LinkedList<Project>();
		for (BHRBenefitJoin bj : bja) {
			BHRBenefitConfig config = bj.getBenefitConfig();
			if (config == null)
				continue;
			Set<Project> projs = config.getProjects();
			if (projs.size() > 0)
				//  if several projects are associated with the same benefit we only want one or the display will show duplicates
				projList.add((Project) projs.toArray()[0]);
		}
		return BProject.makeArray(projList);
	}

	static BHRAccruedTimeOff[] makeArray(List<HrAccrual> l) {
		BHRAccruedTimeOff[] ret = new BHRAccruedTimeOff[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BHRAccruedTimeOff(l.get(loop));
		return ret;
	}

	@Override
	public String create() throws ArahantException {
		logger.debug("Creating time off");

		hrAccrual = new HrAccrual();

		hrAccrual.setAccrualId(IDGenerator.generate(hrAccrual));

		return hrAccrual.getAccrualId();
	}

	@Override
	public void delete() throws ArahantDeleteException {
		ArahantSession.getHSU().delete(hrAccrual);
	}

	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(hrAccrual);
	}

	@Override
	public void load(final String key) throws ArahantException {
		hrAccrual = ArahantSession.getHSU().get(HrAccrual.class, key);
	}

	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(hrAccrual);
	}

	public int getAccrualDate() {
		return hrAccrual.getAccrualDate();
	}

	public double getAccrualHours() {
		return hrAccrual.getAccrualHours();
	}

	public String getAccrualId() {
		return hrAccrual.getAccrualId();
	}

	public String getDescription() {
		return hrAccrual.getDescription();
	}

	/**
	 * @param accrualDate
	 * @see com.arahant.beans.HrAccrual#setAccrualDate(int)
	 */
	public void setAccrualDate(final int accrualDate) {
		hrAccrual.setAccrualDate(accrualDate);
	}

	/**
	 * @param accrualHours
	 * @see com.arahant.beans.HrAccrual#setAccrualHours(int)
	 */
	public void setAccrualHours(final double accrualHours) {
		hrAccrual.setAccrualHours(accrualHours);
	}

	/**
	 * @param description
	 * @see com.arahant.beans.HrAccrual#setDescription(java.lang.String)
	 */
	public void setDescription(final String description) {
		hrAccrual.setDescription(description);
	}

	/**
	 * @param employeeId
	 */
	public void setEmployeeId(final String employeeId) {
		hrAccrual.setEmployee(ArahantSession.getHSU().get(Employee.class, employeeId));
	}

	/**
	 * @param accrualAccountId
	 */
	public void setAccrualAccountId(final String accrualAccountId) {
		hrAccrual.setHrBenefit(ArahantSession.getHSU().get(HrBenefit.class, accrualAccountId));
	}

	/**
	 * @param b
	 */
	public void setResetTotal(final boolean b) {
		resetTotal = b;
	}

	public double getRunningTotal() {
		return runningTotal;
	}

	public void setPreviousTotal(final double d) {
		if (resetTotal) {
			setAccrualHours(-d);
			runningTotal = 0;
		} else {
			runningTotal = getAccrualHours();

			runningTotal += d;

			if (capValue > 0 && runningTotal > capValue)
				runningTotal = capValue;
		}
	}

	/**
	 * @param timesheet
	 */
//	public void setTimesheet(final Timesheet timesheet) {
//		hrAccrual.setTimesheet(timesheet);
//	}
	boolean getResetTotal() {
		return resetTotal;
	}

	void setCapValue(double capValue) {
		this.capValue = capValue;
	}
}
