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

import com.arahant.beans.ApplicantPosition;
import com.arahant.beans.HrPosition;
import com.arahant.beans.OrgGroup;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.LinkedList;
import java.util.List;

public class BApplicantPosition extends SimpleBusinessObjectBase<ApplicantPosition> {

	public static void delete(String[] ids) {
		for (String id : ids)
			new BApplicantPosition(id).delete();
	}

	public static BApplicantPosition[] search(boolean includeAccepting, boolean includeCancelled, boolean includeFilled,
			boolean includeNew, boolean includeSuspended, String jobTypeId, String orgGroupId, String position, int highCap) {
		HibernateCriteriaUtil<ApplicantPosition> hcu = ArahantSession.getHSU().createCriteria(ApplicantPosition.class).orderBy(ApplicantPosition.ACCEPT_APPLICANT_DATE).like(ApplicantPosition.JOB_TITLE, position).setMaxResults(highCap);

		if (!isEmpty(orgGroupId))
			hcu.joinTo(ApplicantPosition.ORG_GROUP).eq(OrgGroup.ORGGROUPID, orgGroupId);

		//New Accepting applicants Suspended Filled Cancelled

		List<Character> incls = new LinkedList<Character>();

		if (includeAccepting)
			incls.add('A'); //New Accepting applicants Suspended Filled Cancelled
		if (includeCancelled)
			incls.add('C');
		if (includeFilled)
			incls.add('F');
		if (includeNew)
			incls.add('N');
		if (includeSuspended)
			incls.add('S');

		hcu.in(ApplicantPosition.POSITION_STATUS, incls);

		return makeArray(hcu.list());
	}

	public static BApplicantPosition[] search(int acceptingFrom, int acceptingTo, int jobStartFrom, int jobStartTo, boolean includeAccepting,
			boolean includeCancelled, boolean includeFilled, boolean includeNew, boolean includeSuspended,
			String orgGroupId, String positionId, int max) {
		HibernateCriteriaUtil<ApplicantPosition> hcu = ArahantSession.getHSU().createCriteria(ApplicantPosition.class)
				.setMaxResults(max).orderBy(ApplicantPosition.ACCEPT_APPLICANT_DATE)
				.dateBetween(ApplicantPosition.ACCEPT_APPLICANT_DATE, acceptingFrom, acceptingTo)
				.dateBetween(ApplicantPosition.JOB_START_DATE, jobStartFrom, jobStartTo);

		if (!isEmpty(positionId))
			hcu.eq(ApplicantPosition.POSITION_TYPE, new BHRPosition(positionId).getBean());

		if (!isEmpty(orgGroupId))
			hcu.joinTo(ApplicantPosition.ORG_GROUP).eq(OrgGroup.ORGGROUPID, orgGroupId);


		//New Accepting applicants Suspended Filled Cancelled

		List<Character> incls = new LinkedList<Character>();

		if (includeAccepting)
			incls.add('A'); //New Accepting applicants Suspended Filled Cancelled
		if (includeCancelled)
			incls.add('C');
		if (includeFilled)
			incls.add('F');
		if (includeNew)
			incls.add('N');
		if (includeSuspended)
			incls.add('S');

		hcu.in(ApplicantPosition.POSITION_STATUS, incls);

		return makeArray(hcu.list());
	}

	public BApplicantPosition(ApplicantPosition o) {
		bean = o;
	}

	public BApplicantPosition(String id) {
		bean = ArahantSession.getHSU().get(ApplicantPosition.class, id);
	}

	public BApplicantPosition() {
	}

	@Override
	public String create() throws ArahantException {
		bean = new ApplicantPosition();
		return bean.generateId();
	}

	public int getAcceptApplicationDate() {
		return bean.getAcceptApplicantDate();
	}

	public String getId() {
		return bean.getApplicantPositionId();
	}

	public int getJobStartDate() {
		return bean.getJobStartDate();
	}

	public String getOrgGroupId() {
		return bean.getOrgGroup().getOrgGroupId();
	}

	public String getOrgGroupName() {
		return bean.getOrgGroup().getName();
	}

	public String getJobTitle() {
		return bean.getJobTitle();
	}

	public String getStatusType() {
		return bean.getPositionStatus() + "";
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ApplicantPosition.class, key);
	}

	public void setAcceptApplicationDate(int acceptApplicationDate) {
		bean.setAcceptApplicantDate(acceptApplicationDate);
	}

	public void setJobStartDate(int jobStartDate) {
		bean.setJobStartDate(jobStartDate);
	}

	public void setOrgGroupId(String orgGroupId) {
		bean.setOrgGroup(ArahantSession.getHSU().get(OrgGroup.class, orgGroupId));
	}

	public void setJobTitleName(String jobTitle) {
		bean.setJobTitle(jobTitle);
	}

	public HrPosition getPosition() {
		return bean.getPosition();
	}

	public void setPosition(HrPosition position) {
		bean.setPosition(position);
	}

	public void setStatusType(String statusType) {
		bean.setPositionStatus(statusType.charAt(0));
	}

	public static BApplicantPosition[] list() {
		return makeArray(ArahantSession.getHSU().createCriteria(ApplicantPosition.class).orderBy(ApplicantPosition.ACCEPT_APPLICANT_DATE).list());
	}

	static BApplicantPosition[] makeArray(List<ApplicantPosition> l) {
		BApplicantPosition[] ret = new BApplicantPosition[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BApplicantPosition(l.get(loop));
		return ret;
	}
}
