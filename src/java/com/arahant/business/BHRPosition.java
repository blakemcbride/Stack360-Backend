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

import com.arahant.beans.BenefitClass;
import com.arahant.beans.HrPosition;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.util.List;

public class BHRPosition extends SimpleBusinessObjectBase<HrPosition> {

	public BHRPosition() {
	}

	public BHRPosition(final String key) throws ArahantException {
		internalLoad(key);
	}

	public BHRPosition(final HrPosition pos) {
        bean = pos;
    }

	/**
	 * @param hsu
	 * @param account
	 */
	public BHRPosition(final HibernateSessionUtil hsu, final HrPosition account) {
		bean = account;
	}

	@Override
	public String create() throws ArahantException {
		bean = new HrPosition();
		bean.setOrgGroup(ArahantSession.getHSU().getCurrentCompany());
		bean.generateId();
		return getPositionId();
	}

	public String getBenefitClassId() {
		if (bean.getBenefitClass() == null)
			return "";
		return bean.getBenefitClass().getBenefitClassId();
	}

	public String getBenefitClassName() {
		if (bean.getBenefitClass() == null)
			return "";
		return bean.getBenefitClass().getName();
	}

	public int getFirstActiveDate() {
		return bean.getFirstActiveDate();
	}

	public int getLastActiveDate() {
		return bean.getLastActiveDate();
	}

	private void internalLoad(final String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(HrPosition.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	public String getPositionId() {
		if (bean == null)
			return "";
		return bean.getPositionId();
	}

	public String getName() {
		if (bean == null)
			return "";
		return bean.getName();
	}

	public void setBenefitClassId(String benefitClassId) {
		bean.setBenefitClass(ArahantSession.getHSU().get(BenefitClass.class, benefitClassId));
	}

	public float getWeeklyPerDiem() {
		return bean.getWeeklyPerDiem();
	}

	public BHRPosition setWeeklyPerDiem(float perDiem) {
		bean.setWeeklyPerDiem(perDiem);
		return this;
	}

	public short getSeqno() {
		return bean.getSeqno();
	}

	public BHRPosition setSeqno(short seqno) {
		bean.setSeqno(seqno);
		return this;
	}

	public void setFirstActiveDate(int firstActiveDate) {
		bean.setFirstActiveDate(firstActiveDate);
	}

	public void setLastActiveDate(int lastActiveDate) {
		bean.setLastActiveDate(lastActiveDate);
	}

	public void setPositionId(final String PositionId) {
		bean.setPositionId(PositionId);
	}

	public void setName(final String name) {
		bean.setName(name);
	}

	public char getApplicantDefault() {
		return bean.getApplicantDefault();
	}

	public void setApplicantDefault(char applicantDefault) {
		bean.setApplicantDefault(applicantDefault);
	}

	public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantException {
		for (final String element : ids)
			new BHRPosition(element).delete();
	}

	@SuppressWarnings("unchecked")
	public static BHRPosition[] list(final HibernateSessionUtil hsu) {
		final List<HrPosition> l = hsu.createCriteria(HrPosition.class)
				.orderBy(HrPosition.NAME)
				.dateInside(HrPosition.FIRSTACTIVEDATE, HrPosition.LASTACTIVEDATE, DateUtils.now())
				.list();

		return makeArray(l);
	}

	public static BHRPosition[] list(int activeType) {

		HibernateCriteriaUtil<HrPosition> hcu = ArahantSession.getHSU()
				.createCriteria(HrPosition.class)
				.orderBy(HrPosition.SEQNO)
				.orderBy(HrPosition.NAME);

		if (activeType == 1) //active

			hcu.dateInside(HrPosition.FIRSTACTIVEDATE, HrPosition.LASTACTIVEDATE, DateUtils.now());

		if (activeType == 2)
			hcu.dateOutside(HrPosition.FIRSTACTIVEDATE, HrPosition.LASTACTIVEDATE, DateUtils.now());

		return makeArray(hcu.list());
	}

	static BHRPosition[] makeArray(List<HrPosition> l) {
		final BHRPosition[] ret = new BHRPosition[l.size()];
		for (int loop = 0; loop < l.size(); loop++)
			ret[loop] = new BHRPosition(ArahantSession.getHSU(), l.get(loop));
		return ret;
	}

	public static String findOrMake(String name) {
		HrPosition hrp = ArahantSession.getHSU().createCriteria(HrPosition.class)
				.eq(HrPosition.NAME, name)
				.eq(HrPosition.ORG_GROUP, ArahantSession.getHSU().getCurrentCompany())
				.first();

		if (hrp != null)
			return hrp.getPositionId();

		BHRPosition position = new BHRPosition();
		String ret = position.create();
		position.setName(name);
		position.insert();

		return ret;
	}
}
