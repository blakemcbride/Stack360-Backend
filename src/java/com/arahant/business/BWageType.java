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

import com.arahant.beans.GlAccount;
import com.arahant.beans.WageType;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.Collections;
import java.util.List;

public class BWageType extends SimpleBusinessObjectBase<WageType> {

	public BWageType() {
	}

	public BWageType(String key) {
		super(key);
	}

	public BWageType(WageType o) {
		bean = o;
	}

	public static String findOrMake(String name) {
		WageType wt = ArahantSession.getHSU().createCriteria(WageType.class).eq(WageType.NAME, name).first();

		if (wt != null)
			return wt.getWageTypeId();

		BWageType bwt = new BWageType();
		String ret = bwt.create();
		bwt.setName(name);
		bwt.setPeriodType(WageType.PERIOD_ONE_TIME);
		bwt.setType(WageType.TYPE_VACATION);
		bwt.setIsDeduction(false);
		bwt.insert();

		return ret;
	}

	public static String findOrMakeType(String name, short type, short periodType) {
		WageType wt = ArahantSession.getHSU().createCriteria(WageType.class).eq(WageType.NAME, name).eq(WageType.PERIOD_TYPE, periodType).eq(WageType.WAGE_CAT, type).first();

		if (wt != null)
			return wt.getWageTypeId();

		BWageType bwt = new BWageType();
		String ret = bwt.create();
		bwt.setName(name);
		bwt.setPeriodType(periodType);
		bwt.setType(type);
		bwt.setIsDeduction(false);
		bwt.insert();

		return ret;
	}

	static BWageType[] makeArray(List<WageType> l) {
		BWageType[] ret = new BWageType[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BWageType(l.get(loop));
		return ret;
	}

	public static void delete(String[] ids) {
		for (String id : ids)
			new BWageType(id).delete();
	}

	public static BWageType[] list(int activeType) {
		HibernateCriteriaUtil<WageType> hcu = ArahantSession.getHSU().createCriteria(WageType.class).orderBy(WageType.NAME);

		switch (activeType) {
			case 1:
				hcu.dateInside(WageType.FIRSTACTIVEDATE, WageType.LASTACTIVEDATE, DateUtils.now());
				break;
			case 2:
				hcu.dateOutside(WageType.FIRSTACTIVEDATE, WageType.LASTACTIVEDATE, DateUtils.now());
				break;
		}

		return makeArray(hcu.list());
	}

	public static BWageType[] list() {
		return makeArray(ArahantSession.getHSU().createCriteria(WageType.class).orderBy(WageType.NAME).list());
	}

	public static BWageType[] listActiveNonDedutionsPlus(String wageTypeId) {
		List<WageType> wageTypes = ArahantSession.getHSU().createCriteria(WageType.class).orderBy(WageType.NAME).dateInside(WageType.FIRSTACTIVEDATE, WageType.LASTACTIVEDATE, DateUtils.now()).eq(WageType.IS_DEDUCTION, 'N').list();

		if (!isEmpty(wageTypeId)) {
			WageType current = ArahantSession.getHSU().get(WageType.class, wageTypeId);

			if (!wageTypes.contains(current)) {
				wageTypes.add(current);
				Collections.sort(wageTypes);
			}
		}
		return makeArray(wageTypes);
	}

	@Override
	public String create() throws ArahantException {
		bean = new WageType();
		bean.setOrgGroup(ArahantSession.getHSU().getCurrentCompany());
		return bean.generateId();
	}

	public int getFirstActiveDate() {
		return bean.getFirstActiveDate();
	}

	public String getExpenseAccountId() {

		if (bean.getExpenseAccount() == null)
			return "";
		return bean.getExpenseAccount().getGlAccountId();
	}

	public String getLiabilityAccountId() {
		if (bean.getLiabilityAccount() == null)
			return "";
		return bean.getLiabilityAccount().getGlAccountId();
	}

	public String getId() {
		return bean.getWageTypeId();
	}

	public boolean getIsDeduction() {
		return bean.getIsDeduction() == 'Y';
	}

	public int getLastActiveDate() {
		return bean.getLastActiveDate();
	}

	public String getName() {
		return bean.getWageName();
	}

	public String getPayrollInterfaceCode() {
		return bean.getPayrollInterfaceCode();
	}

	public int getPeriodType() {
		return bean.getPeriodType();
	}

	public int getType() {
		return bean.getWageType();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(WageType.class, key);
	}

	public void setExpenseAccount(GlAccount account) {
		bean.setExpenseAccount(account);
	}

	public void setFirstActiveDate(int firstActiveDate) {
		bean.setFirstActiveDate(firstActiveDate);
	}

	public void setExpenseAccountId(String glAccountId) {
		bean.setExpenseAccount(ArahantSession.getHSU().get(GlAccount.class, glAccountId));
	}

	public void setLiabilityAccount(GlAccount account) {
		bean.setLiabilityAccount(account);
	}

	public void setLiabilityAccountId(String glAccountId) {
		bean.setLiabilityAccount(ArahantSession.getHSU().get(GlAccount.class, glAccountId));
	}

	public void setIsDeduction(boolean deduction) {
		bean.setIsDeduction(deduction ? 'Y' : 'N');
	}

	public void setLastActiveDate(int lastActiveDate) {
		bean.setLastActiveDate(lastActiveDate);
	}

	public void setName(String name) {
		bean.setWageName(name);
	}

	public void setPayrollInterfaceCode(String edCode) {
		bean.setPayrollInterfaceCode(edCode);
	}

	public void setPeriodType(int periodType) {
		bean.setPeriodType((short) periodType);
	}

	public void setType(int type) {
		bean.setWageType((short) type);
	}

	public static String findOrMake(String name, GlAccount wagesAccount, short periodType, short type) {
		WageType wt = ArahantSession.getHSU().createCriteria(WageType.class).eq(WageType.NAME, name).eq(WageType.ORG_GROUP, ArahantSession.getHSU().getCurrentCompany()).first();

		if (wt != null)
			return wt.getWageTypeId();

		BWageType wageType = new BWageType();
		String ret = wageType.create();
		wageType.setIsDeduction(false);
		wageType.setName(name);
		wageType.setPeriodType(periodType);
		wageType.setType(type);
		wageType.setExpenseAccount(wagesAccount);

		wageType.insert();

		return ret;
	}
}
