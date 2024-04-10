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
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.util.Date;
import java.util.List;

public class BTimeOffRequest extends SimpleBusinessObjectBase<TimeOffRequest> implements BEvent {

	private char orginalStatus = 'O';

	public static void delete(String[] ids) {
		for (String id : ids)
			new BTimeOffRequest(id).delete();
	}

	public static BTimeOffRequest[] listApprovedRequests(String personId) {
		return makeArray(ArahantSession.getHSU().createCriteria(TimeOffRequest.class).eq(TimeOffRequest.STATUS, TimeOffRequest.APPROVED).joinTo(TimeOffRequest.REQUESTING_PERSON).eq(Person.PERSONID, personId).list());
	}

	public static BTimeOffRequest[] listSubordinatesRequests(boolean includeApproved) {
		BPerson bp = BPerson.getCurrent();

		char[] includeStatuses;

		if (includeApproved)
			includeStatuses = new char[]{TimeOffRequest.APPROVED, TimeOffRequest.ORIGINATED};
		else
			includeStatuses = new char[]{TimeOffRequest.ORIGINATED};

		List<String> subs = BPerson.getSubordinateIds(false, true);

		return makeArray(ArahantSession.getHSU().createCriteria(TimeOffRequest.class).orderBy(TimeOffRequest.START_DATE).orderBy(TimeOffRequest.REQUEST_DATE).in(TimeOffRequest.STATUS, includeStatuses).joinTo(TimeOffRequest.REQUESTING_PERSON).in(Person.PERSONID, subs).list());
	}

	public static void markEntered(String[] ids) {
		for (String id : ids) {
			BTimeOffRequest tor = new BTimeOffRequest(id);
			tor.setStatus(TimeOffRequest.ENTERED);
			tor.update();
		}
	}

	public static BTimeOffRequest[] search(String personId, int startDate, int endDate, String[] excludeIds, int cap) {
		HibernateCriteriaUtil<TimeOffRequest> hcu = ArahantSession.getHSU().createCriteria(TimeOffRequest.class).setMaxResults(cap).orderByDesc(TimeOffRequest.START_DATE);
		hcu.dateSpanCompare(TimeOffRequest.START_DATE, TimeOffRequest.END_DATE, startDate, endDate);
		hcu.joinTo(TimeOffRequest.REQUESTING_PERSON).eq(Person.PERSONID, personId);
		hcu.notIn(TimeOffRequest.REQUEST_ID, excludeIds);

		return makeArray(hcu.list());
	}

	public static BTimeOffRequest[] search(String personId, int date, String[] excludeIds, int cap) {
		HibernateCriteriaUtil<TimeOffRequest> hcu = ArahantSession.getHSU().createCriteria(TimeOffRequest.class).setMaxResults(cap).orderByDesc(TimeOffRequest.START_DATE).dateInside(TimeOffRequest.START_DATE, TimeOffRequest.END_DATE, date);
		hcu.joinTo(TimeOffRequest.REQUESTING_PERSON).eq(Person.PERSONID, personId);
		hcu.notIn(TimeOffRequest.REQUEST_ID, excludeIds);

		return makeArray(hcu.list());
	}

	static BTimeOffRequest[] makeArray(List<TimeOffRequest> l) {
		BTimeOffRequest[] ret = new BTimeOffRequest[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BTimeOffRequest(l.get(loop));
		return ret;
	}

	public BTimeOffRequest(String id) {
		super(id);
	}

	public BTimeOffRequest() {
		super();
	}

	public BTimeOffRequest(TimeOffRequest o) {
		super();
		bean = o;
	}

	public String getBenefitName() {
		return new BProject(bean.getProject()).getBenefitName();
	}

	public String getPersonId() {
		return bean.getRequestingPerson().getPersonId();
	}

	public String getProjectId() {
		return bean.getProject().getProjectId();
	}

	public String getProjectName() {
		return bean.getProject().getProjectName().trim();
	}

	public int getRequestTime() {
		return DateUtils.getTime(bean.getRequestDate());
	}

	@Override
	public void insertChecks() throws ArahantException {
		if ((bean.getStartTime() != -1 || bean.getReturnTime() != -1)
				&& (bean.getStartDate() != bean.getReturnDate()))
			throw new ArahantWarning("Start and end times may not span multiple days.");

		BMessage.sendToSupervisors(bean.getRequestingPerson(), "Time Off Requested", bean.getRequestingPerson().getNameLFM() + " has requested time off from "
				+ DateUtils.getDateTimeFormatted(bean.getStartDate(), bean.getStartTime()) + " to " + DateUtils.getDateTimeFormatted(bean.getReturnDate(), bean.getReturnTime()) + ".");
	}

	public void setProjectId(String projectId) {
		bean.setProject(ArahantSession.getHSU().get(Project.class, projectId));
	}

	@Override
	public void updateChecks() throws ArahantException {
		if ((bean.getStartTime() != -1 || bean.getReturnTime() != -1)
				&& (bean.getStartDate() != bean.getReturnDate()))
			throw new ArahantWarning("Start and end times may not span multiple days.");


		switch (bean.getRequestStatus()) {
			case TimeOffRequest.APPROVED: {
				HibernateSessionUtil hsu = ArahantSession.getHSU();
				BMessage.send(bean.getApprovingPerson(), bean.getRequestingPerson(), "Time Off Approved", bean.getApprovingPerson().getNameLFM() + " has approved your time off from "
						+ DateUtils.getDateTimeFormatted(bean.getStartDate(), bean.getStartTime()) + " to " + DateUtils.getDateTimeFormatted(bean.getReturnDate(), bean.getReturnTime()) + ".");
				// if a company that sets the accrual right away, set the accrual
				if (hsu.getCurrentCompany().getMarkTimeOffOnApproval() == 'Y')
					for (int date = bean.getStartDate(); date <= bean.getReturnDate(); date = DateUtils.addDays(date, 1)) {
						double accrualHours;
						if (bean.getStartTime() != -1) {
							Date start = DateUtils.getDate(bean.getStartDate(), bean.getStartTime());
							Date end = DateUtils.getDate(bean.getReturnDate(), bean.getReturnTime());

							double mils = end.getTime() - start.getTime();

							accrualHours = mils / 1000 / 60 / 60.0;
						} else
							accrualHours = new BPerson(bean.getRequestingPerson()).getBEmployee().getExpectedHoursPerPayPeriod() / 5.0;
						/*
						 * BHRAccruedTimeOff toff=new BHRAccruedTimeOff();
						 * toff.create();
						 * toff.setAccrualDate(bean.getStartDate());
						 * toff.setEmployeeId(bean.getRequestingPerson().getPersonId());
						 * String comment=bean.getRequestingComment(); if
						 * (comment.length()>80) comment=comment.substring(0,
						 * 80); toff.setDescription(comment);
						 *
						 * if (bean.getStartTime()!=-1) { Date
						 * start=DateUtils.getDate(bean.getStartDate(),bean.getStartTime());
						 * Date
						 * end=DateUtils.getDate(bean.getReturnDate(),bean.getReturnTime());
						 *
						 * double mils=end.getTime()-start.getTime();
						 *
						 * toff.setAccrualHours(mils/1000/60/60); } else {
						 * toff.setAccrualHours(new
						 * BPerson(bean.getRequestingPerson()).getBEmployee().getExpectedHoursPerPayPeriod()/5);
						 * }
						 *
						 * HibernateCriteriaUtil<HrBenefit>
						 * hcu=hsu.createCriteria(HrBenefit.class);
						 * HibernateCriteriaUtil
						 * conHcu=hcu.joinTo(HrBenefit.HR_BENEFIT_CONFIGS);
						 * conHcu.joinTo(HrBenefitConfig.HR_BENEFIT_JOINS)
						 * .eq(HrBenefitJoin.PAYING_PERSON,bean.getRequestingPerson());
						 * conHcu.joinTo(HrBenefitConfig.HRBENEFITPROJECTJOINS)
						 * .eq(HrBenefitProjectJoin.PROJECT,bean.getProject());
						 *
						 * HrBenefit bene=hcu.first();
						 * toff.setAccrualAccountId(bene.getBenefitId());
						 * toff.insert();
						 */

						HibernateCriteriaUtil<HrBenefit> hcu = hsu.createCriteria(HrBenefit.class);
						HibernateCriteriaUtil conHcu = hcu.joinTo(HrBenefit.HR_BENEFIT_CONFIGS);
						conHcu.joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).eq(HrBenefitJoin.PAYING_PERSON, bean.getRequestingPerson());
						conHcu.joinTo(HrBenefitConfig.HRBENEFITPROJECTJOINS).eq(HrBenefitProjectJoin.PROJECT, bean.getProject());

						HrBenefit bene = hcu.first();

						//make the timesheet record for them
						BTimesheet timesheet = new BTimesheet();
						timesheet.create();
						timesheet.setBeginningTime(bean.getStartTime());
						timesheet.setEndTime(bean.getReturnTime());
						timesheet.setWorkDate(date);
						timesheet.setEndDate(date);
						timesheet.setTotalHours(accrualHours);
						timesheet.setState(TIMESHEET_NEW);
						timesheet.setDescription(bene.getName());
						timesheet.setPersonId(bean.getRequestingPerson().getPersonId());
						timesheet.setWageTypeId(bene.getWageType().getWageTypeId());
						timesheet.setBillable(bean.getProject().getBillable());
						timesheet.setBeginningEntryDate(new Date());
				//		timesheet.setProjectId(bean.getProject().getProjectId());
						if (true) throw new ArahantException("XXYY");
						timesheet.setEndTime(bean.getReturnTime());
						timesheet.insert();
						if (BProperty.getBoolean("ApproveTimeOffTimesheet", true))
							BTimesheet.approveAllowFuture(new String[]{timesheet.getTimesheetId()});
					}
			}
			break;
			case TimeOffRequest.REJECTED:
				BMessage.send(bean.getApprovingPerson(), bean.getRequestingPerson(), "Time Off Rejected", bean.getApprovingPerson().getNameLFM() + " has rejected your time off from "
						+ DateUtils.getDateTimeFormatted(bean.getStartDate(), bean.getStartTime()) + " to " + DateUtils.getDateTimeFormatted(bean.getReturnDate(), bean.getReturnTime()) + ".");
				break;
		}
	}

	@Override
	public String create() throws ArahantException {
		bean = new TimeOffRequest();
		bean.setRequestStatus(TimeOffRequest.ORIGINATED);
		bean.setRequestDate(new Date());
		return bean.generateId();
	}

	@Override
	public void delete() {
		if (bean.getRequestStatus() == TimeOffRequest.ORIGINATED) {
			super.delete();
			return;
		}
		//if I can't approve it, I can't delete it
		List<String> subs = BPerson.getSubordinateIds(false, true);

		if (bean.getRequestStatus() != TimeOffRequest.ORIGINATED && !subs.contains(bean.getRequestingPerson().getPersonId()))
			throw new ArahantWarning("Request can not be deleted after it has been submitted.");

		if (!subs.contains(bean.getRequestingPerson().getPersonId()))
			throw new ArahantWarning("You may not delete requests that are not from your subordinates.");

		super.delete();
	}

	public String getApprovingComment() {
		return bean.getApprovalComment();
	}

	public String getApprovingPersonFormatted() {
		if (bean.getApprovingPerson() == null)
			return "";
		return bean.getApprovingPerson().getNameLFM();
	}

	public int getFirstDateOff() {
		return bean.getStartDate();
	}

	public String getFirstDateTimeFormatted() {
		return DateUtils.getDateFormatted(bean.getStartDate());
	}

	public int getFirstTimeOff() {
		return bean.getStartTime();
	}

	public String getId() {
		return bean.getRequestId();
	}

	public int getLastDateOff() {
		return bean.getReturnDate();
	}

	public String getLastDateTimeFormatted() {
		return DateUtils.getDateFormatted(bean.getReturnDate());
	}

	public int getLastTimeOff() {
		return bean.getReturnTime();
	}

	public int getRequestDate() {
		return DateUtils.getDate(bean.getRequestDate());
	}

	public String getRequestDateFormatted() {
		return DateUtils.getDateFormatted(bean.getRequestDate());
	}

	public char getRequestState() {
		return bean.getRequestStatus();
	}

	public String getRequestingComment() {
		return bean.getRequestingComment();
	}

	public String getRequestingPersonFormatted() {
		return bean.getRequestingPerson().getNameLFM();
	}

	public String getStatus() {
		return bean.getRequestStatus() + "";
	}

	public String getStatusFormatted() {
		switch (bean.getRequestStatus()) {
			case TimeOffRequest.ORIGINATED:
				return "Pending";
			case TimeOffRequest.APPROVED:
				return "Approved";
			case TimeOffRequest.ENTERED:
				return "Entered";
			case TimeOffRequest.REJECTED:
				return "Rejected";
			default:
				return "Unknown";
		}
	}

	public int getStatusDate() {
		if (bean.getApprovalDate() == null)
			return DateUtils.getDate(bean.getRequestDate());
		return DateUtils.getDate(bean.getApprovalDate());
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(TimeOffRequest.class, key);
		orginalStatus = bean.getRequestStatus();
	}

	public void setApprovingComments(String approvingComments) {
		bean.setApprovalComment(approvingComments);
	}

	public void setFirstDateOff(int firstDateOff) {
		bean.setStartDate(firstDateOff);
	}

	public void setFirstTimeOff(int firstTimeOff) {
		bean.setStartTime(firstTimeOff);
	}

	public void setLastDateOff(int lastDateOff) {
		bean.setReturnDate(lastDateOff);
	}

	public void setLastTimeOff(int lastTimeOff) {
		bean.setReturnTime(lastTimeOff);
	}

	public void setPersonId(String personId) {
		bean.setRequestingPerson(ArahantSession.getHSU().get(Person.class, personId));
	}

	public void setRequestingComment(String requestingComment) {
		bean.setRequestingComment(requestingComment);
	}

	public void setStatus(char c) {
		bean.setRequestStatus(c);
	}

	@Override
	public String getEventName() {
		if (bean.getRequestStatus() == TimeOffRequest.APPROVED)
			return "Time Off Request (Approved)";
		if (bean.getRequestStatus() == TimeOffRequest.ENTERED)
			return "Time Off Request (Entered)";
		if (bean.getRequestStatus() == TimeOffRequest.ORIGINATED)
			return "Time Off Request (Pending)";
		if (bean.getRequestStatus() == TimeOffRequest.REJECTED)
			return "Time Off Request (Rejected)";
		return "Time Off Request";
	}

	@Override
	public Hours[] getHours(int date) {
		return Hours.construct(bean.getStartDate(), bean.getStartTime(), bean.getReturnDate(), bean.getReturnTime(), date);
	}

	public void setStatus(String status) {
		if (status.length() > 0)
			setStatus(status.charAt(0));

		if (orginalStatus != bean.getRequestStatus()) {
			bean.setApprovingPerson(ArahantSession.getHSU().getCurrentPerson());
			bean.setApprovalDate(new Date());
		}
	}

	@Override
	public int getEventType() {
		if (bean.getRequestStatus() == TimeOffRequest.ENTERED)
			return EVENT_ENTERED_TIME_OFF;
		if (bean.getRequestStatus() == TimeOffRequest.APPROVED)
			return EVENT_APPROVED_TIME_OFF;
		if (bean.getRequestStatus() == TimeOffRequest.ORIGINATED)
			return EVENT_OPEN_TIME_OFF;
		if (bean.getRequestStatus() == TimeOffRequest.REJECTED)
			return EVENT_REJECTED_TIME_OFF;
		throw new ArahantException("Unknown event type requested");
	}
}
