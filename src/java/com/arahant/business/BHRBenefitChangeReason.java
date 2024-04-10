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

import com.arahant.beans.HrBenefitChangeReason;
import com.arahant.exceptions.ArahantException;
import com.arahant.reports.BenefitChangeReasonReport;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateSessionUtil;
import com.itextpdf.text.DocumentException;
import java.io.FileNotFoundException;
import java.util.*;

public class BHRBenefitChangeReason extends SimpleBusinessObjectBase<HrBenefitChangeReason> {

	public BHRBenefitChangeReason() {}

	/**
	 * @param string
	 * @throws ArahantException
	 */
	public BHRBenefitChangeReason(final String string) throws ArahantException {
		super(string);
	}

	/**
	 * @param q
	 */
	public BHRBenefitChangeReason(final HrBenefitChangeReason q) {
		bean = q;
	}

	/**
	 * @return
	 */
	public String getHrBenefitChangeReasonId() {
		return bean.getHrBenefitChangeReasonId();
	}

	/**
	 * @return
	 */
	public String getDescription() {
		return bean.getDescription();
	}

	public String getId() {
		return bean.getHrBenefitChangeReasonId();
	}

	/**
	 * @param eventName
	 */
	public void setEventName(final String eventName) {
		bean.setDescription(eventName);
	}

	public int getStartDate() {
		return bean.getStartDate();
	}

	public void setStartDate(final int startDate) {
		bean.setStartDate(startDate);
	}

	public int getEndDate() {
		return bean.getEndDate();
	}

	public void setEndDate(final int endDate) {
		bean.setEndDate(endDate);
	}

	public int getEffectiveDate() {
		if (bean.getEffectiveDate() == 0)
			return DateUtils.now();

		return bean.getEffectiveDate();
	}

	public void setEffectiveDate(final int effectiveDate) {
		bean.setEffectiveDate(effectiveDate);
	}

	/**
	 * @param cap
	 * @return
	 */
	public static BHRBenefitChangeReason[] list(final int cap) {
		return makeArray(ArahantSession.getHSU().createCriteria(HrBenefitChangeReason.class).orderBy(HrBenefitChangeReason.NAME).list());
	}

	public static BHRBenefitChangeReason[] listQualifyingReasons() {
		return makeArray(ArahantSession.getHSU().createCriteria(HrBenefitChangeReason.class).eq(HrBenefitChangeReason.TYPE, HrBenefitChangeReason.QUALIFYING_EVENT).orderBy(HrBenefitChangeReason.NAME).list());
	}

	public static BHRBenefitChangeReason[] listActiveQualifyingReasons() {
		return makeArray(ArahantSession.getHSU().createCriteria(HrBenefitChangeReason.class).eq(HrBenefitChangeReason.TYPE, HrBenefitChangeReason.QUALIFYING_EVENT).dateInside(HrBenefitChangeReason.START_DATE, HrBenefitChangeReason.END_DATE, DateUtils.now()).orderBy(HrBenefitChangeReason.NAME).list());
	}

	public static BHRBenefitChangeReason[] listOpenEnrollmentChanges(final int cap) {
		return makeArray(ArahantSession.getHSU().createCriteria(HrBenefitChangeReason.class).orderBy(HrBenefitChangeReason.NAME).eq(HrBenefitChangeReason.TYPE, HrBenefitChangeReason.OPEN_ENROLLMENT).setMaxResults(cap).list());
	}

	public static BHRBenefitChangeReason[] listCobraTermChangeReasons(final int cap) {
		List<Short> eventTypes = new ArrayList<Short>();
		eventTypes.add(HrBenefitChangeReason.EVENT_TYPE_DEATH);
		eventTypes.add(HrBenefitChangeReason.EVENT_TYPE_INVOLUNTARY_TERM);
		eventTypes.add(HrBenefitChangeReason.EVENT_TYPE_VOLUNTARY_TERM);
		eventTypes.add(HrBenefitChangeReason.EVENT_TYPE_REDUCTION_IN_HOURS);
		eventTypes.add(HrBenefitChangeReason.EVENT_TYPE_DIVORCE);
		eventTypes.add(HrBenefitChangeReason.EVENT_TYPE_DEATH);
		eventTypes.add(HrBenefitChangeReason.EVENT_TYPE_ENTITLED_MEDICARE);
		eventTypes.add(HrBenefitChangeReason.EVENT_TYPE_DEPENDENT_INELIGIBLE);

		return makeArray(ArahantSession.getHSU().createCriteria(HrBenefitChangeReason.class).orderBy(HrBenefitChangeReason.NAME).in(HrBenefitChangeReason.EVENT_TYPE, eventTypes).list());
	}

	/**
	 * @param name
	 * @return
	 */
	private static BHRBenefitChangeReason[] makeArray(final Collection<HrBenefitChangeReason> l) {
		final BHRBenefitChangeReason[] ret = new BHRBenefitChangeReason[l.size()];
		int loop = 0;

		for (final HrBenefitChangeReason q : l)
			ret[loop++] = new BHRBenefitChangeReason(q);
		return ret;
	}

	/**
	 * @return @throws ArahantException
	 */
	@Override
	public String create() throws ArahantException {
		bean = new HrBenefitChangeReason();
		return bean.generateId();
	}

	/**
	 * @param hsu
	 * @param ids
	 * @throws ArahantException
	 */
	public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantException {
		for (String element : ids)
			new BHRBenefitChangeReason(element).delete();
	}

	/**
	 * @return @throws ArahantException
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 */
	public String getReport() throws FileNotFoundException, DocumentException, ArahantException {
		return new BenefitChangeReasonReport().build();
	}

	/*
	 * (non-Javadoc) @see
	 * com.arahant.business.interfaces.IDBFunctions#load(java.lang.String)
	 */
	@Override
	public void load(final String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(HrBenefitChangeReason.class, key);
	}

	/**
	 * @param reasonName
	 */
	public void setDescription(final String reasonName) {
		bean.setDescription(reasonName);
	}

	public void setInstructions(final String instructions) {
		bean.setInstructions(instructions);
	}

	public String getInstructions() {
		return bean.getInstructions();
	}

	/**
	 * @return
	 */
	public String getTypeName() {
		return getTypeName(bean.getType());
	}

	public short getType() {
		return bean.getType();
	}

	public static String getTypeName(final int val) {
		switch (val) {
			case HrBenefitChangeReason.INTERNAL_STAFF_EDIT:
				return "Internal Staff Edit";
			case HrBenefitChangeReason.NEW_HIRE:
				return "New Hire";
			case HrBenefitChangeReason.OPEN_ENROLLMENT:
				return "Open Enrollment";
			case HrBenefitChangeReason.QUALIFYING_EVENT:
				return "Qualifying Event";
			default:
				return "Unknown";
		}
	}

	/**
	 * @return
	 */
	public int getTypeId() {
		try {
			return bean.getType();
		} catch (Exception e) {
			return 0; //for dummy change reasons
		}
	}

	/**
	 * @param typeId
	 */
	public void setTypeId(final int typeId) {
		bean.setType((short) typeId);
	}

	public static class BenefitChangeReasonType {

		/**
		 * @param loop
		 * @param typeName
		 */
		public BenefitChangeReasonType(final int t, final String typeName) {
			type = t;
			name = typeName;
		}
		public int type;
		public String name;
	}

	/**
	 * @param cap
	 * @return
	 */
	public static BenefitChangeReasonType[] listTypes() {
		final BenefitChangeReasonType[] ret = new BenefitChangeReasonType[4];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BenefitChangeReasonType(loop + 1, getTypeName(loop + 1));
		return ret;
	}

	/**
	 * @return
	 */
	public static String getOpenEnrollmentId() {
		try {
			return ArahantSession.getHSU().createCriteria(HrBenefitChangeReason.class).eq(HrBenefitChangeReason.TYPE, HrBenefitChangeReason.OPEN_ENROLLMENT).le(HrBenefitChangeReason.START_DATE, DateUtils.now()).orderBy(HrBenefitChangeReason.START_DATE).first().getHrBenefitChangeReasonId();
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * @return
	 */
	public static String getNewHireId() {
		try {
			return ArahantSession.getHSU().createCriteria(HrBenefitChangeReason.class).eq(HrBenefitChangeReason.TYPE, HrBenefitChangeReason.NEW_HIRE).le(HrBenefitChangeReason.START_DATE, DateUtils.now()).orderBy(HrBenefitChangeReason.START_DATE).first().getHrBenefitChangeReasonId();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getNewHireIdOrCreate() {
		String newHireId;
		try {

			HrBenefitChangeReason bc = ArahantSession.getHSU().createCriteria(HrBenefitChangeReason.class).eq(HrBenefitChangeReason.TYPE, HrBenefitChangeReason.NEW_HIRE).le(HrBenefitChangeReason.START_DATE, DateUtils.now()).orderBy(HrBenefitChangeReason.START_DATE).first();
			if (bc == null) {
				//create new hire type in hr_benefit_change_reason
				this.create();
				this.setTypeId(HrBenefitChangeReason.NEW_HIRE);
				this.setDescription("New Hire");
				this.update();
				newHireId = this.getId();
			} else
				newHireId = bc.getHrBenefitChangeReasonId();

		} catch (final Exception e) {
			return "";
		}
		return newHireId;
	}

	public static BHRBenefitChangeReason[] listActives(String benefitJoinId) {
		//list all active ones, plus the one the join is to, in case it was inactivated
		Set<HrBenefitChangeReason> reasons = new HashSet<HrBenefitChangeReason>();

		reasons.addAll(ArahantSession.getHSU().createCriteria(HrBenefitChangeReason.class).dateInside(HrBenefitChangeReason.START_DATE, HrBenefitChangeReason.END_DATE, DateUtils.now()).list());

		if (!isEmpty(benefitJoinId)) {
			BHRBenefitJoin bj = new BHRBenefitJoin(benefitJoinId);
			HrBenefitChangeReason bcr = ArahantSession.getHSU().createCriteria(HrBenefitChangeReason.class).eq(HrBenefitChangeReason.DESCRIPTION, bj.getChangeReason()).first();

			if (bcr != null)
				reasons.add(bcr);
		}

		List<HrBenefitChangeReason> ret = new ArrayList<HrBenefitChangeReason>(reasons.size());
		ret.addAll(reasons);
		Collections.sort(ret);
		return makeArray(ret);
	}

	public static int getActiveOpenEffectiveDate() {
		HrBenefitChangeReason hb = ArahantSession.getHSU().createCriteria(HrBenefitChangeReason.class).eq(HrBenefitChangeReason.TYPE, HrBenefitChangeReason.OPEN_ENROLLMENT).dateInside(HrBenefitChangeReason.START_DATE, HrBenefitChangeReason.END_DATE, DateUtils.now()).orderBy(HrBenefitChangeReason.NAME).first();

		if (hb != null)
			return hb.getEffectiveDate();

		return DateUtils.now();
	}

	public static String getActiveOpenEnrollmentId() {
		HrBenefitChangeReason hb = ArahantSession.getHSU().createCriteria(HrBenefitChangeReason.class).eq(HrBenefitChangeReason.TYPE, HrBenefitChangeReason.OPEN_ENROLLMENT).dateInside(HrBenefitChangeReason.START_DATE, HrBenefitChangeReason.END_DATE, DateUtils.now()).orderBy(HrBenefitChangeReason.NAME).first();

		if (hb != null)
			return hb.getHrBenefitChangeReasonId();

		return "";
	}

	public static BHRBenefitChangeReason findOrMake(String name) {
		BHRBenefitChangeReason b = new BHRBenefitChangeReason();
		HrBenefitChangeReason bcr = ArahantSession.getHSU().createCriteria(HrBenefitChangeReason.class).eq(HrBenefitChangeReason.DESCRIPTION, name).first();
		if (bcr == null) {
			b.create();
			b.setDescription(name);
			b.setTypeId(4);
			b.insert();
		} else
			b = new BHRBenefitChangeReason(bcr);
		return b;
	}

	public static BHRBenefitChangeReason[] makeArray(final List<HrBenefitChangeReason> l) {
		final BHRBenefitChangeReason[] ret = new BHRBenefitChangeReason[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BHRBenefitChangeReason(l.get(loop));
		return ret;
	}
}
