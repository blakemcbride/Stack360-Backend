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

import com.arahant.beans.TimeOffAccrualCalc;
import com.arahant.beans.TimeOffAccrualCalcSeniority;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.List;

public class BTimeOffAccrualCalcSeniority extends SimpleBusinessObjectBase<TimeOffAccrualCalcSeniority> {

	public BTimeOffAccrualCalcSeniority() {
	}

	public BTimeOffAccrualCalcSeniority(String id) {
		super(id);
	}

	public BTimeOffAccrualCalcSeniority(TimeOffAccrualCalcSeniority o) {
		bean = o;
	}

	public static void delete(String[] ids) {
		for (String id : ids)
			new BTimeOffAccrualCalcSeniority(id).delete();
	}

	@Override
	public String create() throws ArahantException {
		bean = new TimeOffAccrualCalcSeniority();
		return bean.generateId();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(TimeOffAccrualCalcSeniority.class, key);
	}

	public float getHoursAccrued() {
		return bean.getHoursAccrued();
	}

	public void setHoursAccrued(double hoursAccrued) {
		bean.setHoursAccrued((float) hoursAccrued);
	}

	public String getTimeOffAccrualCalcSeniorityId() {
		return bean.getTimeOffAccrualCalcSeniorityId();
	}

	public short getYearsOfService() {
		return bean.getYearsOfService();
	}

	public void setYearsOfService(int yearsOfService) {
		bean.setYearsOfService((short) yearsOfService);
	}

	public static BTimeOffAccrualCalcSeniority[] listSeniority(final String id) {
		HibernateCriteriaUtil<TimeOffAccrualCalcSeniority> hcu = ArahantSession.getHSU().createCriteria(TimeOffAccrualCalcSeniority.class)
				.orderBy(TimeOffAccrualCalcSeniority.YEARS_OF_SERVICE)
				.joinTo(TimeOffAccrualCalcSeniority.CALC)
				.eq(TimeOffAccrualCalc.CALC_ID, id);

		return makeArray(hcu.list());
	}

	public static BTimeOffAccrualCalcSeniority[] makeArray(List<TimeOffAccrualCalcSeniority> l) throws ArahantException {

		final BTimeOffAccrualCalcSeniority[] ret = new BTimeOffAccrualCalcSeniority[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BTimeOffAccrualCalcSeniority(l.get(loop));

		return ret;
	}

	public void setAccrualCalcId(String variantId) {
		bean.setTimeOffCalc(ArahantSession.getHSU().get(TimeOffAccrualCalc.class, variantId));
	}

	@Override
	public void insertChecks() {
		if (ArahantSession.getHSU().createCriteria(TimeOffAccrualCalcSeniority.class)
				.eq(TimeOffAccrualCalcSeniority.CALC, bean.getTimeOffCalc())
				.eq(TimeOffAccrualCalcSeniority.YEARS_OF_SERVICE, bean.getYearsOfService()).exists())
			throw new ArahantWarning("A tier already exists for the specified Years of Service.  Please enter a different value for Years of Service.");
	}

	@Override
	public void updateChecks() {
		if (ArahantSession.getHSU().createCriteria(TimeOffAccrualCalcSeniority.class)
				.ne(TimeOffAccrualCalcSeniority.ID, bean.getTimeOffAccrualCalcSeniorityId())
				.eq(TimeOffAccrualCalcSeniority.CALC, bean.getTimeOffCalc())
				.eq(TimeOffAccrualCalcSeniority.YEARS_OF_SERVICE, bean.getYearsOfService())
				.exists())
			throw new ArahantWarning("A tier already exists for the specified Years of Service.  Please enter a different value for Years of Service.");
	}
}
