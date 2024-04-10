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
import com.arahant.reports.UnpaidTimesheetReport;
import com.arahant.utils.*;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.sql.SQLException;
import java.util.*;

public class BTimesheet extends BusinessLogicBase implements IDBFunctions {

	private static final transient ArahantLogger logger = new ArahantLogger(BTimesheet.class);
	private Timesheet timesheet;
	private int oldStartTime;
	private int oldStartDate;
	private int oldEndDate;
	private int oldEndTime;

	public static void clockIn(String personId) {
		clockIn(personId, new BPerson(personId).getDefaultProjectId(), 2000);
	}

	public static void clockIn(String personId, int machineTimeZone) {
		clockIn(personId, new BPerson(personId).getDefaultProjectId(), machineTimeZone);
	}

	public static void clockIn(String personId, String defaultProjectId, int machineTimeZone) {

		//am I already clocked in?
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		if (hsu.createCriteria(Timesheet.class)
				.le(Timesheet.WORKDATE, DateUtils.now())
				.ne(Timesheet.BEGINNINGTIME, -1)
				.eq(Timesheet.ENDTIME, -1)
				.joinTo(Timesheet.PERSON)
				.eq(Person.PERSONID, personId).exists())
			throw new ArahantWarning("You are already clocked in.");

		/*
		 * let it overlap, Don't prevent clock in if
		 * (hsu.createCriteria(Timesheet.class) .le(Timesheet.BEGINNINGTIME,
		 * DateUtils.nowTime()) .le(Timesheet.WORKDATE, DateUtils.now())
		 * .geOrEq(Timesheet.ENDDATE, DateUtils.now(),0)
		 * .geOrEq(Timesheet.ENDTIME, DateUtils.nowTime(),-1)
		 * .joinTo(Timesheet.PERSON) .eq(Person.PERSONID, personId) .exists())
		 * throw new ArahantWarning("Time overlaps other timesheets.");
		 */
		BPerson bp = new BPerson(personId);
		BProject bproj = new BProject(defaultProjectId);

		BTimesheet t = new BTimesheet();
		t.create();
		t.setBeginningTime(DateUtils.nowTimeToMinuteWithTimeZone(machineTimeZone));
		t.setBeginningTimeZoneOffset(machineTimeZone);
		t.setWorkDate(DateUtils.now());
//		t.setProjectId(bproj.getProjectId());
		if (true) throw new ArahantException("XXYY");
		t.setState(TIMESHEET_NEW);
//		t.setBeginningEntryDate(new Date());
		if (bp.getDefaultProject() == null)
			throw new ArahantWarning("Please set up a default project for current user.");
		t.setBillable(bproj.getBillable());
		t.setDescription("");
		t.setPersonId(bp.getPersonId());
		t.setEndTime(-1);
		t.insert();
	}

	public static void clockOut(String personId) {
		clockOut(personId, 2000);
	}

	public static void clockOut(String personId, int machineTimeZone) {
		//am I already clocked in?
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		Timesheet ts = hsu.createCriteria(Timesheet.class).le(Timesheet.WORKDATE, DateUtils.now()).ne(Timesheet.BEGINNINGTIME, -1).eq(Timesheet.ENDTIME, -1).joinTo(Timesheet.PERSON).eq(Person.PERSONID, personId).first();

		if (ts == null)
			throw new ArahantWarning("You are not clocked in.");
		/*
		 * if (hsu.createCriteria(Timesheet.class) .ne(Timesheet.TIMESHEETID,
		 * ts.getTimesheetId()) .le(Timesheet.BEGINNINGTIME,
		 * DateUtils.nowTime()) .le(Timesheet.WORKDATE, DateUtils.now())
		 * .geOrEq(Timesheet.ENDDATE, DateUtils.now(),0)
		 * .geOrEq(Timesheet.ENDTIME, DateUtils.nowTime(),-1)
		 * .joinTo(Timesheet.PERSON) .eq(Person.PERSONID, personId) .exists())
		 * throw new ArahantWarning("Time overlaps other timesheets.");
		 */

		BTimesheet t = new BTimesheet(ts);
		t.setEndTime(DateUtils.nowTimeToMinuteWithTimeZone(machineTimeZone));
		t.setEndDate(DateUtils.now());
		t.setEndTimeZoneOffset(machineTimeZone);

		if (t.getEndDate() == 0 && t.getEndTime() != 0)
			t.setEndDate(t.getStartDate());

		Date end = DateUtils.getDate(t.getEndDate(), t.getEndTime());
		Date start = DateUtils.getDate(t.getStartDate(), t.getStartTime());

		double timeDif = end.getTime() - start.getTime();
		timeDif = timeDif / 1000 / 60 / 60;

		if (BProperty.getBoolean("AutoSubBreaks"))
			//check to see if this is the first clockout
			if (!hsu.createCriteria(Timesheet.class).ne(Timesheet.TIMESHEETID, ts.getTimesheetId()).eq(Timesheet.WORKDATE, DateUtils.now()).joinTo(Timesheet.PERSON).eq(Person.PERSONID, personId).exists()) {
				//do subtract stuff
				BEmployee bp = new BEmployee(personId);
				if (timeDif > bp.getBreakHours())
					timeDif -= bp.getBreakHours();
			}

		t.setTotalHours(BTimesheet.round(timeDif));
		t.update();
	}

	public static void delete(String[] ids) {
		for (String id : ids)
			new BTimesheet(id).delete();
	}

	public static String getSpan(int span) {
		long hours = span / 60;
		long mins = span - hours * 60;

		String h = hours + "";
		StringBuilder m = new StringBuilder(mins + "");

		//	while (h.length()<2)
		//		h='0'+h;

		while (m.length() < 2)
			m.insert(0, '0');

		return h + ":" + m;
	}

	public static BTimesheet[] searchClockEntriesMultipleGroups(String personId, int fromDate, int toDate, int cap) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		Project pp = new Project();
		try {
			pp = new BPerson(personId).getDefaultProject();
			if (pp == null)
				throw new ArahantWarning("Please set up default project for user.");
		} catch (Exception e) {
			throw new ArahantWarning("Please set up default project for user.");
		}

		List<Project> pl = new ArrayList<>();
		if (pp.getRequestingOrgGroup() != null)
			pl.addAll(hsu.createCriteria(Project.class).eq(Project.REQUESTING_ORG_GROUP, pp.getRequestingOrgGroup()).list());


		//System.out.println(pp.getProjectId());
		//System.out.println(personId);

		return makeArray(hsu.createCriteria(Timesheet.class).dateSpanCompare(Timesheet.WORKDATE, Timesheet.ENDDATE, fromDate, toDate).joinTo(Timesheet.PROJECTSHIFT).in(ProjectShift.PROJECT, pl).orderByDesc(Timesheet.WORKDATE).orderByDesc(Timesheet.BEGINNINGTIME).joinTo(Timesheet.PERSON).eq(Person.PERSONID, personId).list());
	}

	public static BTimesheet[] searchClockEntries(String personId, int fromDate, int toDate, int cap) {
		Person p = ArahantSession.getHSU().get(Person.class, personId);
		BPerson bp = new BPerson(p);
		Project pr = bp.getDefaultProject();

		if (pr == null)
			throw new ArahantWarning("Please set up default project for user.");

//		System.out.println(pr.getProjectId());
//		System.out.println(personId);

		return makeArray(ArahantSession.getHSU().createCriteria(Timesheet.class).dateSpanCompare(Timesheet.WORKDATE, Timesheet.ENDDATE, fromDate, toDate).joinTo(Timesheet.PROJECTSHIFT).eq(ProjectShift.PROJECT, pr).orderByDesc(Timesheet.WORKDATE).orderByDesc(Timesheet.BEGINNINGTIME).joinTo(Timesheet.PERSON).eq(Person.PERSONID, personId).list());
	}

	public static BTimesheet[] search(String personId, int fromDate, int toDate, int cap) {
		return makeArray(ArahantSession.getHSU().createCriteria(Timesheet.class).dateSpanCompare(Timesheet.WORKDATE, Timesheet.ENDDATE, fromDate, toDate).orderByDesc(Timesheet.WORKDATE).orderByDesc(Timesheet.BEGINNINGTIME).joinTo(Timesheet.PERSON).eq(Person.PERSONID, personId).list());
	}

	public static BTimesheet[] searchFinalized(String personId, int fromDate, int toDate, int cap) {
		BEmployee e = new BEmployee(personId);

		return makeArray(ArahantSession.getHSU().createCriteria(Timesheet.class).dateSpanCompare(Timesheet.WORKDATE, Timesheet.ENDDATE, fromDate, toDate).le(Timesheet.ENDDATE, e.getTimesheetFinalDate()).notIn(Timesheet.STATE, new char[]{'I', 'A'}).orderByDesc(Timesheet.WORKDATE).orderByDesc(Timesheet.BEGINNINGTIME).joinTo(Timesheet.PERSON).eq(Person.PERSONID, personId).list());
	}

	public BTimesheet() {
	}

	public BTimesheet(final Timesheet ts) {
		timesheet = ts;
	}

	public BTimesheet(final String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 * @return @see com.arahant.beans.Timesheet#getBeginningTime()
	 */
	public int getBeginningTime() {
		return timesheet.getBeginningTime();
	}

	/**
	 * @return @see com.arahant.beans.Timesheet#getBillable()
	 */
	public char getBillable() {
		return timesheet.getBillable();
	}

	public String getCurrentStatus() {
		if (timesheet.getEndTime() == -1)
			return "IN";

		return "OUT";
	}

	public String getCurrentStatusSince() {
		if (timesheet.getEndTime() == -1)
			return DateUtils.getDateTimeFormatted(timesheet.getWorkDate(), timesheet.getBeginningTime());
		return DateUtils.getDateTimeFormatted(timesheet.getEndDate(), timesheet.getEndTime());
	}

	/**
	 * @return @see com.arahant.beans.Timesheet#getDescription()
	 */
	public String getDescription() {
		return timesheet.getDescription();
	}

	public String getPrivateDescription() {
		return timesheet.getPrivateDescription();
	}

	public int getElapsedTime() {
		if (timesheet.getEndTime() == -1)
			return 0;

		if (timesheet.getEndDate() == 0 && timesheet.getEndTime() != 0)
			timesheet.setEndDate(timesheet.getWorkDate());

		Date end = DateUtils.getDate(timesheet.getEndDate(), timesheet.getEndTime());
		if (end == null)
			return 0;
		Date start = DateUtils.getDate(timesheet.getWorkDate(), timesheet.getBeginningTime());
		long dif = end.getTime() / 1000 / 60 - start.getTime() / 1000 / 60;

		//don't care about milliseconds MOVED to calc above so it works on minutes and not milliseconds
		//	dif=dif/1000;

		//now I have seconds - convert to minutes
		//	dif=dif/60;

		return (int) dif;
	}

	public int getElapsedTime(int date) {
		if (timesheet.getEndTime() == -1)
			return 0;
		Date end = DateUtils.getDate(timesheet.getEndDate(), timesheet.getEndTime());
		Date start = DateUtils.getDate(timesheet.getWorkDate(), timesheet.getBeginningTime());

		Calendar dt = DateUtils.getCalendar(date);
		dt.set(Calendar.HOUR_OF_DAY, 0);
		dt.set(Calendar.MINUTE, 0);
		dt.set(Calendar.SECOND, 0);
		dt.set(Calendar.MILLISECOND, 0);

		if (start.before(dt.getTime()))
			start = dt.getTime();

		Calendar eod = DateUtils.getCalendar(date);
		eod.set(Calendar.HOUR_OF_DAY, 23);
		eod.set(Calendar.MINUTE, 60);
		eod.set(Calendar.SECOND, 0);
		eod.set(Calendar.MILLISECOND, 0);

		if (end.after(eod.getTime()))
			end = eod.getTime();

		long dif = end.getTime() / 1000 / 60 - start.getTime() / 1000 / 60;

		//don't care about milliseconds MOVED to calc above so it works on minutes and not milliseconds
		//	dif=dif/1000;

		//now I have seconds - convert to minutes
		//	dif=dif/60;

		return (int) dif;
	}

	public String getElapsedTimeFormatted() {
		if (timesheet.getEndTime() == -1)
			return "";
		return getSpan(getElapsedTime());
	}

	public String getElapsedTimeFormatted(int date) {
		if (timesheet.getEndTime() == -1)
			return "";
		return getSpan(getElapsedTime(date));
	}

	public int getEndDate() {
		return timesheet.getEndDate();
	}

	public int getEndTime() {
		return timesheet.getEndTime();
	}

	public Date getBeginningEntryDate() {
		return timesheet.getBeginningEntryDate();
	}

	public Date getEndEntryDate() {
		return timesheet.getEndEntryDate();
	}

	public String getId() {
		return timesheet.getTimesheetId();
	}

	public int getStartDate() {
		return timesheet.getWorkDate();
	}

	public int getStartTime() {
		return timesheet.getBeginningTime();
	}

	public char getState() {
		return timesheet.getState();
	}

	public Timesheet getTimesheet() {
		return timesheet;
	}

	public String getTimesheetId() {
		return timesheet.getTimesheetId();
	}

	public double getTotalHours() {
		return timesheet.getTotalHours();
	}

	public int getWorkDate() {
		return timesheet.getWorkDate();
	}

	public boolean isTimeOff() {
		return timesheet.getProjectShift().getProject().getHrBenefitProjectJoins().size() > 0;
	}

	public void setBeginningTime(final int beginningTime) {
		timesheet.setBeginningTime(beginningTime);
	}

	public void setBillable(final char billable) {
		timesheet.setBillable(billable);
	}

	public void setDescription(final String description) {
		timesheet.setDescription(description);
	}

	public void setPrivateDescription(final String privateDescription) {
		timesheet.setPrivateDescription(privateDescription);
	}

	public void setEndTime(final int endTime) {
		timesheet.setEndTime(endTime);
	}

	public void setBeginningEntryDate(final Date beginningEntryDate) {
		timesheet.setBeginningEntryDate(beginningEntryDate);
	}

	public void setEndEntryDate(final Date endEntryDate) {
		timesheet.setEndEntryDate(endEntryDate);
	}

	public void setEndDate(int finalDate) {
		timesheet.setEndDate(finalDate);
	}

	public void setStartDate(int startDate) {
		timesheet.setWorkDate(startDate);
	}

	public void setStartTime(int startTime) {
		timesheet.setBeginningTime(startTime);
	}

	public void setState(final char state) {
		timesheet.setState(state);
	}

	public void setTimesheetId(final String timesheetId) {
		timesheet.setTimesheetId(timesheetId);
	}

	public void setTotalHours(final double totalHours) {
		timesheet.setTotalHours(totalHours);
	}

	public void setWorkDate(final int workDate) {
		timesheet.setWorkDate(workDate);
	}

	public String getCompanyName() {
		return new BProject(timesheet.getProjectShift().getProject()).getCompanyName();
	}

	public String getProjectName() {
		return timesheet.getProjectShift().getProject().getProjectName();
	}

	public double getBillingRate() {
		return getCalculatedBillingRate(); //timesheet.getProject().getBillingRate();
	}

	public double getCalculatedBillingRate() {
		return new BProject(timesheet.getProjectShift().getProject()).getCalculatedBillingRate(ArahantSession.getHSU().get(Employee.class, timesheet.getPerson().getPersonId()));
	}

	public String getFirstName() {
		return timesheet.getPerson().getFname();
	}

	public String getLastName() {
		return timesheet.getPerson().getLname();
	}

	public String getProjectDescription() {
		return timesheet.getProjectShift().getProject().getDescription();
	}

	public String getAcctSystemAccount() {
		final Project p = timesheet.getProjectShift().getProject();
		if (p.getProductService() != null)
			return p.getProductService().getExpenseAccount().getAccountNumber();
		return "";
	}

	public String getExternalReference() {
		return timesheet.getProjectShift().getProject().getReference();
	}

	public String getAcctSystemId() {
		final Project p = timesheet.getProjectShift().getProject();
		if (p.getProductService() != null)
			return p.getProductService().getAccsysId();
		return "";
	}

	public String getProductProphetId() {
		final Project p = timesheet.getProjectShift().getProject();
		if (p.getProductService() != null)
			return p.getProductService().getProductId();
		return "";
	}

	public String getPersonId() {
		return timesheet.getPerson() == null ? timesheet.getPersonId() : timesheet.getPerson().getPersonId();
	}

	public float getTotalExpenses() {
		return timesheet.getTotalExpenses();
	}

	public void setTotalExpenses(float totalExpenses) {
		timesheet.setTotalExpenses(totalExpenses);
	}

	public double getFixedPay() {
		return timesheet.getFixedPay();
	}

	public void setFixedPay(double pay) {
		timesheet.setFixedPay(pay);
	}

    public TimeType getTimeType() {
        return timesheet.getTimeType();
    }
    
    public void setTimeType(TimeType timeType) {
        timesheet.setTimeType(timeType);
    }
    
    public String getTimeTypeId() {
        return timesheet.getTimeTypeId();
    }
    
    public void setTimeTypeId(String timeTypeId) {
        timesheet.setTimeTypeId(timeTypeId);
    }
    
	@Override
	public String create() throws ArahantException {
		timesheet = new Timesheet();
		timesheet.generateId();
		timesheet.setState('N');
		timesheet.setBeginningEntryPerson(ArahantSession.getCurrentPerson());
		return timesheet.getTimesheetId();
	}

	@Override
	public void delete() throws ArahantDeleteException {
		try {
			//hsu.delete(timesheet.getHrAccruals());
			ArahantSession.getHSU().delete(timesheet);
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
	}

	@Override
	public void insert() throws ArahantException {
		String err = saveChecks();
		if (err != null)
			throw new ArahantWarning(err);
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		if (timesheet.getWorkDate() != 0) {
			timesheet.setBeginningEntryPerson(hsu.getCurrentPerson());
			timesheet.setBeginningEntryDate(new Date());
		}

		if (timesheet.getEndDate() == 0 && timesheet.getEndTime() != -1)
			timesheet.setEndDate(timesheet.getWorkDate());

		if (timesheet.getEndDate() != 0) {
			timesheet.setEndingEntryPerson(hsu.getCurrentPerson());
			timesheet.setEndEntryDate(new Date());
		}

		if (timesheet.getWageType() == null) {
			BPerson bp = new BPerson(timesheet.getPerson());
			if (!bp.isEmployee())
				throw new ArahantWarning("Can't log time for a non-employee");


			BEmployee bemp = bp.getBEmployee();
			if (isEmpty(bemp.getWageTypeId()))
				throw new ArahantWarning("Can't log time until employee has a valid Wage & Position.");

			setWageTypeId(bemp.getWageTypeId());
		}

		hsu.insert(timesheet);

		postSaveChecks();
	}

	private void internalLoad(final String key) throws ArahantException {
		logger.debug("Loading " + key);
		timesheet = ArahantSession.getHSU().get(Timesheet.class, key);
		timesheet.getProjectShift();
		oldEndDate = timesheet.getEndDate();
		oldEndTime = timesheet.getEndTime();
		oldStartDate = timesheet.getWorkDate();
		oldStartTime = timesheet.getBeginningTime();
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	@Override
	public void update() throws ArahantException {
		String err = saveChecks();
		if (err != null)
			throw new ArahantWarning(err);
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		if (oldEndDate != timesheet.getEndDate() || oldEndTime != timesheet.getEndTime()) {
			timesheet.setEndingEntryPerson(hsu.getCurrentPerson());
			timesheet.setEndEntryDate(new Date());
		}
		if (oldStartDate != timesheet.getWorkDate() || oldStartTime != timesheet.getBeginningTime()) {
			timesheet.setBeginningEntryPerson(hsu.getCurrentPerson());
			timesheet.setBeginningEntryDate(new Date());
		}

		hsu.saveOrUpdate(timesheet);

		postSaveChecks();
	}

	/*
	 * handle change project on time related benefits Note that insert could
	 * change the project, which can mean both backing out accrued entries
	 * against a time related and adding accrued entries against a different
	 * time related project. In fact there would be a lot of combinations here,
	 * especially when you throw in maybe the hours are just changing. Fun!
	 */
	private void postSaveChecks() throws ArahantException {
		final String benefitId = getBenefitId();

		//delete accrued time if project changed
//		if (!isEmpty(oldProjectId) && (timesheet.getProject()==null || !oldProjectId.equals(timesheet.getProject().getProjectId())))
//			hsu.delete(timesheet.getHrAccruals());

//		if (timesheet.getHrAccruals().size()>0)
//		{
//			final HrAccrual hra=timesheet.getHrAccruals().iterator().next();
//			hra.setAccrualHours(-timesheet.getTotalHours());
//                        hsu.saveOrUpdate(hra);
//		}
//		else
//			if (!isEmpty(benefitId))
//			{
//					final BHRAccruedTimeOff b=new BHRAccruedTimeOff();
//					b.create();
//					b.setAccrualDate(timesheet.getWorkDate());
//					b.setAccrualHours(-timesheet.getTotalHours());
//					b.setDescription(timesheet.getDescription());
//					b.setEmployeeId(timesheet.getPerson().getPersonId());
//					b.setAccrualAccountId(benefitId);
//					b.setTimesheet(timesheet);
//					b.insert();
//			}

		final Project p = timesheet.getProjectShift().getProject();
		double estimate = p.getEstimateHours();
		if (estimate > .1) {
			double current = ArahantSession.getHSU().createCriteria(Timesheet.class).sum(Timesheet.TOTALHOURS).eq(Timesheet.PROJECTSHIFT, timesheet.getProjectShift()).doubleVal();

			if (current > estimate && p.getEmployee() != null) {
				BMessage.send(null, p.getEmployee(), "Project Over Estimate", "The project " + p.getProjectName().trim()
						+ " is over estimate.  The estimated hours were " + Formatting.formatNumber(estimate, 2) + ".  The hours added were " + Formatting.formatNumber(timesheet.getTotalHours(), 2) + ".  The total hours are now " + Formatting.formatNumber(current, 2) + ".");
				ArahantSession.addReturnMessage("Project is over estimate!");
			}
		}
	}

	public String saveChecks() throws ArahantException {
		//Let them do it
//		if (timesheet.getTotalHours()>24)
//			throw new ArahantWarning("Can't work more than 24 hours in a day.");

		timesheet.setBeginningEntryDate(new Date());
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		final Employee emp = hsu.get(Employee.class, timesheet.getPerson().getPersonId());

		if (emp == null)
			return "Unable to load employee record for id " + timesheet.getPerson().getPersonId();

		if (BRight.checkRight(CHANGE_TIME_AFTER_LOCK_DATE) != ACCESS_LEVEL_WRITE)
			if (timesheet.getWorkDate() < emp.getTimesheetFinalDate())
				//see if day was rejected
				if (hsu.createCriteria(TimeReject.class).eq(TimeReject.PERSON, emp).eq(TimeReject.REJECTDATE, timesheet.getWorkDate()).first() == null)
					return "Can't change after lock date!";

		switch (timesheet.getState()) {
			case 0:
				timesheet.setState(TIMESHEET_NEW);
				break;
			case TIMESHEET_NEW:
				timesheet.setState(TIMESHEET_NEW);
				break;
			case TIMESHEET_REJECTED:
				timesheet.setState(TIMESHEET_FIXED);
				break;
			case TIMESHEET_PROBLEM:
				timesheet.setState(TIMESHEET_FIXED);
				break;
			case TIMESHEET_SUBMITTED:
				timesheet.setState(TIMESHEET_CHANGED);
				break;
			case TIMESHEET_CHANGED:
				timesheet.setState(TIMESHEET_CHANGED);
				break;
			case TIMESHEET_INVOICED:
				if (BRight.checkRight(CHANGE_INVOICED_TIMESHEET) != ACCESS_LEVEL_WRITE)
					return "Can't change invoiced timesheet!";
				break;
			case TIMESHEET_APPROVED:
				if (BRight.checkRight(CHANGE_INVOICED_TIMESHEET) != ACCESS_LEVEL_WRITE)
					return "Can't change approved timesheet!";
				break;
		}

		if (timesheet.getEndDate() == 0 && timesheet.getEndTime() != 0)
			timesheet.setEndDate(timesheet.getWorkDate());
		return null;
	}

	private void reject(final Message m) throws ArahantException {
		final Timesheet t = timesheet;

		Person person = t.getPerson();
//		if (t.getState() == TIMESHEET_APPROVED)
//			//person should point to the manager
//			if (!isSupervisor(person))
//				person = getSupervisor(person);  //  FIXME some kind of logic error here.  What is person used for?

		if (t.getState() == TIMESHEET_INVOICED)
			throw new ArahantException("Can't change state from Invoiced!");


		if (BRight.checkRight(ACCESS_ACCOUNTING) == ACCESS_LEVEL_WRITE)
			if (t.getState() == TIMESHEET_PROBLEM)
				t.setState(TIMESHEET_REJECTED);
			else
				t.setState(TIMESHEET_PROBLEM);
		else
			t.setState(TIMESHEET_REJECTED);

		t.setMessage(m);

		boolean found = false;

		for (TimeReject tr : t.getPerson().getTimeRejects())
			if (tr.getRejectDate() == t.getWorkDate()) {
				found = true;
				break;
			}

		if (!found) {
			final TimeReject tr = new TimeReject();
			tr.setPerson(t.getPerson());
			tr.setRejectDate(t.getWorkDate());
			tr.generateId();
			ArahantSession.getHSU().insert(tr);
			t.getPerson().getTimeRejects().add(tr);
		}
	}

	public static void rejectTimesheets(final String[] timesheetId, final String message) throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		final Timesheet ts = hsu.get(Timesheet.class, timesheetId[0]);
		Person person = ts.getPerson();
		String subjectSupp = "";

		if (ts.getState() == TIMESHEET_APPROVED)
			//person should point to the manager
			if (!isSupervisor(person)) {
				subjectSupp = subjectSupp.concat(": " + person.getLname() + ", " + person.getFname());
				person = getSupervisor(person);
			}
		final Message m = internalCreateMessage(hsu.getCurrentPerson().getPersonId(),
				person.getPersonId(), message, "Timesheet Rejection" + subjectSupp);
		for (final String element : timesheetId) {
			final BTimesheet bt = new BTimesheet(element);
			bt.reject(m);
		}
	}

	public static HibernateScrollUtil<Timesheet> getTimesheetsForProjectReport(final HibernateSessionUtil hsu, final BPerson bperson, final String clientCompanyId,
			final String projectId, final String orgGroupId, final String employeeId, final int startDate, final int endDate, final boolean approved,
			final boolean notApproved, final boolean invoiced) throws ArahantException {
		final String user = bperson.getUserLogin();

		final HibernateCriteriaUtil<Timesheet> hcTime = hsu.createCriteria(Timesheet.class);

		final HibernateCriteriaUtil<Timesheet> hcPerson = hcTime.joinTo(Timesheet.PERSON);
		final CompanyBase c = bperson.person.getCompanyBase();

		String companyId = "";
		if (c != null)
			companyId = c.getOrgGroupId();

		if (!isEmpty(companyId))
			hcPerson.joinTo(Person.COMPANYBASE).eq(OrgGroup.ORGGROUPID, companyId);

		if (!isEmpty(clientCompanyId) || !isEmpty(projectId)) {
			final HibernateCriteriaUtil<Timesheet> hcu = hcTime.joinTo(Timesheet.PROJECTSHIFT).joinTo(ProjectShift.PROJECT);

			if (!isEmpty(clientCompanyId))
				hcu.joinTo(Project.REQUESTING_ORG_GROUP).eq(OrgGroup.ORGGROUPID, clientCompanyId);

			if (!isEmpty(projectId))
				hcu.eq(Project.PROJECTID, projectId);
		}

		if (!isEmpty(employeeId))
			hcPerson.eq(Person.PERSONID, employeeId);
		else {
			final Collection<Person> subordList = getSubordinateList(true);

			if (user.equals(ARAHANT_SUPERUSER))
				subordList.add(bperson.person);

			Collection<Person> personList = new LinkedList<Person>();

			if (!isEmpty(orgGroupId)) {
				Collection<Person> orgList = getAllPeopleInOrgGroupHierarchy(hsu, hsu.get(OrgGroup.class, orgGroupId));

				//			so now I have 2 collections, I need the intersection of them

				for (Person o : orgList) {
					if (subordList.contains(o))
						personList.add(o);
				}
			} else
				personList = subordList;

			hcTime.in(Timesheet.PERSON, personList);
		}

		if (startDate > 0)
			hcTime.ge(Timesheet.WORKDATE, startDate);

		if (endDate > 0)
			hcTime.le(Timesheet.WORKDATE, endDate);

		final List<Character> stateList = new LinkedList<Character>();

		if (notApproved) {
			stateList.add(TIMESHEET_NEW);
			stateList.add(TIMESHEET_CHANGED);
			stateList.add(TIMESHEET_FIXED);
			stateList.add(TIMESHEET_REJECTED);
			stateList.add(TIMESHEET_PROBLEM);
			stateList.add(TIMESHEET_SUBMITTED);
		}

		if (invoiced)
			stateList.add(TIMESHEET_INVOICED);

		if (approved)
			stateList.add(TIMESHEET_APPROVED);

		hcTime.in(Timesheet.STATE, stateList);

		hcTime.joinTo(Timesheet.PROJECTSHIFT).joinTo(ProjectShift.PROJECT).orderBy(Project.PROJECTNAME);

		hcTime.distinct();

		return hcTime.scroll();
	}

	@SuppressWarnings("unchecked")
	public static BTimesheet[] getNewTimesheetsForProjectReport(final HibernateSessionUtil hsu, final BPerson bperson, final String clientCompanyId,
			final String projectId, final String orgGroupId, final String employeeId, final int startDate, final int endDate, final boolean approved,
			final boolean notApproved, final boolean invoiced, final int max) throws ArahantException {
		final String user = bperson.getUserLogin();

		final HibernateCriteriaUtil<Project> hcu = hsu.createCriteria(Project.class);

		final HibernateCriteriaUtil hcTime = hcu.joinTo(Project.TIMESHEETS);

		hcTime.sum(Timesheet.TOTALHOURS);

		final int max_timesheets = 5000;

		if (max != -1)
			hcTime.setMaxResults(max);
		else
			hcTime.setMaxResults(max_timesheets);


		final HibernateCriteriaUtil hcPerson = hcTime.joinTo(Timesheet.PERSON);
		final CompanyBase c = bperson.person.getCompanyBase();

		String companyId = "";
		if (c != null)
			companyId = c.getOrgGroupId();

		if (!isEmpty(companyId))
			hcPerson.joinTo(Person.COMPANYBASE).eq(OrgGroup.ORGGROUPID, companyId);

		if (!isEmpty(clientCompanyId) || !isEmpty(projectId)) {
			final HibernateCriteriaUtil hcu2 = hcTime.joinTo(Timesheet.PROJECTSHIFT).joinTo(ProjectShift.PROJECT);

			if (max != -1)
				hcu2.setMaxResults(max);

			if (!isEmpty(clientCompanyId))
				hcu2.joinTo(Project.REQUESTING_ORG_GROUP).eq(OrgGroup.ORGGROUPID, clientCompanyId);

			if (!isEmpty(projectId))
				hcu2.eq(Project.PROJECTID, projectId);
		}

		if (!isEmpty(employeeId))
			hcPerson.eq(Person.PERSONID, employeeId);
		else {
			final Collection<Person> subordList = getSubordinateList(true);

			if (user.equals(ARAHANT_SUPERUSER))
				subordList.add(bperson.person);

			Collection<Person> personList = new LinkedList<Person>();

			if (!isEmpty(orgGroupId)) {
				Collection<Person> orgList = getAllPeopleInOrgGroupHierarchy(hsu, hsu.get(OrgGroup.class, orgGroupId));

				//			so now I have 2 collections, I need the intersection of them

				for (Person o : orgList)
					if (subordList.contains(o))
						personList.add(o);
			} else
				personList = subordList;

			hcTime.in(Timesheet.PERSON, personList);
		}

		if (startDate > 0)
			hcTime.ge(Timesheet.WORKDATE, startDate);

		if (endDate > 0)
			hcTime.le(Timesheet.WORKDATE, endDate);

		final List<Character> stateList = new LinkedList<Character>();

		if (notApproved) {
			stateList.add(TIMESHEET_NEW);
			stateList.add(TIMESHEET_CHANGED);
			stateList.add(TIMESHEET_FIXED);
			stateList.add(TIMESHEET_REJECTED);
			stateList.add(TIMESHEET_PROBLEM);
			stateList.add(TIMESHEET_SUBMITTED);
		}

		if (invoiced)
			stateList.add(TIMESHEET_INVOICED);

		if (approved)
			stateList.add(TIMESHEET_APPROVED);

		hcTime.in(Timesheet.STATE, stateList);

		hcTime.distinct();

		final List<Timesheet> timesheetList = hcTime.list();

		if (timesheetList.size() >= max_timesheets)
			throw new ArahantException("The Project Search Results contained too many associated Timesheet Entries.  Please narrow your search.");

		return makeArray(timesheetList);
	}

	public static HibernateScrollUtil getTimesheetsForProjectReport(final HibernateSessionUtil hsu, final BPerson bperson, final String clientCompanyId,
			final String projectId, final int startDate, final int endDate, final boolean approved,
			final boolean notApproved, final boolean invoiced, final int max) throws ArahantException {
		return getTimesheetsForProjectReport(hsu, bperson, clientCompanyId,
				projectId, startDate, endDate, approved,
				notApproved, invoiced, max, "");
	}

	public static HibernateScrollUtil getTimesheetsForProjectReport(final HibernateSessionUtil hsu, final BPerson bperson, final String clientCompanyId,
			final String projectId, final int startDate, final int endDate, final boolean approved,
			final boolean notApproved, final boolean invoiced, final int max, String projectName) throws ArahantException {
		final String user = bperson.getUserLogin();

		final HibernateCriteriaUtil<Timesheet> hcTime = hsu.createCriteria(Timesheet.class);

		hcTime.sum(Timesheet.TOTALHOURS, Timesheet.PROJECTSHIFT, Timesheet.BILLABLE);

		final HibernateCriteriaUtil<Timesheet> hcPerson = hcTime.joinTo(Timesheet.PERSON);

		//hcPerson.eq(Person.COMPANYBASE,bperson.person.getCompanyBase());

		if (!isEmpty(clientCompanyId) || !isEmpty(projectId) || !isEmpty(projectName)) {
			final HibernateCriteriaUtil<Timesheet> hcu = hcTime.joinTo(Timesheet.PROJECTSHIFT).joinTo(ProjectShift.PROJECT);

			if (!isEmpty(clientCompanyId))
				hcu.joinTo(Project.REQUESTING_ORG_GROUP).in(OrgGroup.ORGGROUPID, new BOrgGroup(clientCompanyId).getAllGroupsForCompany());

			if (!isEmpty(projectId))
				hcu.eq(Project.PROJECTID, projectId);
			else if (!isEmpty(projectName))
				hcu.eq(Project.PROJECTNAME, projectName);
		}

		if (startDate > 0)
			hcTime.ge(Timesheet.WORKDATE, startDate);

		if (endDate > 0)
			hcTime.le(Timesheet.WORKDATE, endDate);

		final List<Character> stateList = new LinkedList<Character>();

		if (notApproved) {
			stateList.add(TIMESHEET_NEW);
			stateList.add(TIMESHEET_CHANGED);
			stateList.add(TIMESHEET_FIXED);
			stateList.add(TIMESHEET_REJECTED);
			stateList.add(TIMESHEET_PROBLEM);
			stateList.add(TIMESHEET_SUBMITTED);
		}

		if (invoiced)
			stateList.add(TIMESHEET_INVOICED);

		if (approved)
			stateList.add(TIMESHEET_APPROVED);

		hcTime.in(Timesheet.STATE, stateList);

		return hcTime.scroll();
	}

	public static HibernateScrollUtil getTimesheetsForProjectReport(final HibernateSessionUtil hsu, final BPerson bperson, final String clientCompanyId,
			final String projectId, final String orgGroupId, final String employeeId, final int startDate, final int endDate, final boolean approved,
			final boolean notApproved, final boolean invoiced, final int max) throws ArahantException {
		final String user = bperson.getUserLogin();

		final HibernateCriteriaUtil<Timesheet> hcTime = hsu.createCriteria(Timesheet.class);

		hcTime.sum(Timesheet.TOTALHOURS, Timesheet.PROJECTSHIFT, Timesheet.BILLABLE);


		final HibernateCriteriaUtil<Timesheet> hcPerson = hcTime.joinTo(Timesheet.PERSON);

		//hcPerson.eq(Person.COMPANYBASE,bperson.person.getCompanyBase());

		if (!isEmpty(clientCompanyId) || !isEmpty(projectId)) {
			final HibernateCriteriaUtil<Timesheet> hcu = hcTime.joinTo(Timesheet.PROJECTSHIFT).joinTo(ProjectShift.PROJECT);

			if (!isEmpty(clientCompanyId))
				hcu.joinTo(Project.REQUESTING_ORG_GROUP).in(OrgGroup.ORGGROUPID, new BOrgGroup(clientCompanyId).getAllGroupsForCompany());

			if (!isEmpty(projectId)) {
				//if this project has billable subprojects - 'I' include them				
				List<String> projectIds = new BProject(projectId).getAllInBillableTree();

				hcu.in(Project.PROJECTID, projectIds);
			}
		}

		if (!isEmpty(employeeId))
			hcPerson.eq(Person.PERSONID, employeeId);
		else {

			final Collection<Person> subordList = getSubordinateList(true);

			if (user.equals(ARAHANT_SUPERUSER))
				subordList.add(bperson.person);

			Collection<Person> personList = new LinkedList<Person>();

			if (!isEmpty(orgGroupId)) {
				Collection<Person> orgList = getAllPeopleInOrgGroupHierarchy(hsu, hsu.get(OrgGroup.class, orgGroupId));

				//			so now I have 2 collections, I need the intersection of them

				for (Person o : orgList)
					if (subordList.contains(o))
						personList.add(o);
			} else
				personList = subordList;
			hcTime.in(Timesheet.PERSON, personList);
		}

		if (startDate > 0)
			hcTime.ge(Timesheet.WORKDATE, startDate);

		if (endDate > 0)
			hcTime.le(Timesheet.WORKDATE, endDate);

		final List<Character> stateList = new LinkedList<Character>();

		if (notApproved) {
			stateList.add(TIMESHEET_NEW);
			stateList.add(TIMESHEET_CHANGED);
			stateList.add(TIMESHEET_FIXED);
			stateList.add(TIMESHEET_REJECTED);
			stateList.add(TIMESHEET_PROBLEM);
			stateList.add(TIMESHEET_SUBMITTED);
		}

		if (invoiced)
			stateList.add(TIMESHEET_INVOICED);

		if (approved)
			stateList.add(TIMESHEET_APPROVED);

		hcTime.in(Timesheet.STATE, stateList);

		return hcTime.scroll();
	}

	public BProject getProject() {
		return new BProject(timesheet.getProjectShift().getProject());
	}

	public static void delete(final HibernateSessionUtil hsu, final String[] timesheetIds) throws ArahantDeleteException, ArahantException {
		for (final String element : timesheetIds)
			new BTimesheet(element).delete();
	}

	public String getFinalized() {
		Employee e;
		if (timesheet.getPerson() instanceof Employee)
			e = (Employee) timesheet.getPerson();
		else if (timesheet.getPerson() == null)
			e = ArahantSession.getHSU().get(Employee.class, timesheet.getPersonId());
		else
			e = ArahantSession.getHSU().get(Employee.class, timesheet.getPerson().getPersonId());
		if (e == null)
			return "Y";
		return e.getTimesheetFinalDate() >= timesheet.getWorkDate() ? "Y" : "N";
	}

	public String getMessageId() {
		if (timesheet.getMessage() != null)
			return timesheet.getMessage().getMessageId();
		return "";
	}

	public String getCompanyId() {
		return timesheet.getProjectShift().getProject().getRequestingOrgGroup().getOrgGroupId();
	}

	public String getMessage() {
		if (timesheet.getMessage() != null)
			return timesheet.getMessage().getMessage();
		return "";
	}

	public String getProjectID() {
		return timesheet.getProjectShift().getProject().getProjectId();
	}

	public static void approve(final String[] timesheetIds) throws ArahantException {
		for (final String element : timesheetIds)
			new BTimesheet(element).approve(false);
	}

	public static void approveAllowFuture(final String[] timesheetIds) throws ArahantException {
		for (final String element : timesheetIds)
			new BTimesheet(element).approve(true);
	}

	public static void approveAll(final String[] employeeIds, final int fromDate, final int toDate) throws ArahantException {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		for (String s : employeeIds) {
			List<Timesheet> l = hsu.createCriteria(Timesheet.class).dateSpanCompare(Timesheet.WORKDATE, Timesheet.ENDDATE, fromDate, toDate).orderByDesc(Timesheet.WORKDATE).orderByDesc(Timesheet.BEGINNINGTIME).joinTo(Timesheet.PERSON).eq(Person.PERSONID, s).list();
			for (final Timesheet element : l)
				new BTimesheet(element).approve();
		}
	}
	
	private void approve() throws ArahantException {
		approve(false);
	}

	private void approve(boolean allowFuture) throws ArahantException {
		final Timesheet t = timesheet;
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		BEmployee bemp = new BPerson(t.getPerson()).getBEmployee();

		//  allow time to be approved if the start date has been finalized
//		if (Math.max(t.getEndDate(), t.getWorkDate()) > bemp.getTimesheetFinalDate())
		if (!allowFuture  &&  t.getWorkDate() > bemp.getTimesheetFinalDate())
			throw new ArahantWarning("You may not approve time until it has been finalized.");

		if (t.getState() == TIMESHEET_INVOICED)
			throw new ArahantWarning("Can't change state from Invoiced!");

		if (t.getBillable() == 'U')
			throw new ArahantWarning("Can't approve time with unknown billable.");

		t.setState(TIMESHEET_APPROVED);

		hsu.saveOrUpdate(t);

		final Project p = t.getProjectShift().getProject();
		if (p != null) {
			final Iterator<Timesheet> timeItr = p.getTimesheets().iterator();

			double totalHours = 0;

			while (timeItr.hasNext()) {
				final Timesheet time = (Timesheet) timeItr.next();
				totalHours += time.getTotalHours();
			}
			try {
				if ((p.getBillable() == 'Y') && (p.getDollarCap() > 0) && (p.getBillingRate() * totalHours > p.getDollarCap())) {
					final BMessage m = new BMessage();
					m.create();
					m.setMessage("Project " + p.getProjectName() + " is over budget.");
					m.setFromPersonId(t.getPerson().getPersonId());
					m.setSubject("Project over budget");
					m.setCreatedDate(new Date());
					m.setFromShow('Y');
					m.insert();

					final Connection db = KissConnection.get();
					final Record rec = db.newRecord("message_to");
					rec.set("message_to_id", IDGenerator.generate("message_to", "message_to_id"));
					rec.set("message_id", m.getMessageId());
					rec.set("to_person_id", p.getEmployee().getPersonId());
					rec.set("send_type", "T");
					rec.set("to_show", "Y");
					rec.set("sent", "Y");
					try {
						rec.addRecord();
					} catch (SQLException throwables) {
						throw new ArahantException(throwables);
					}

				}

				//	When time is approved that is logged against a project that is associated 
				//to a benefit with unpaid = true, a stock message should be sent to all 
				//accountants in the system that indicates the unpaid time has been approved 
				//and includes at least the person, date, hours, project id, and project summary

				final Project p2 = timesheet.getProjectShift().getProject();
				if (hsu.createCriteria(HrBenefit.class).eq(HrBenefit.PAIDBENEFIT, 'N').joinTo(HrBenefit.HR_BENEFIT_CONFIGS).joinTo(HrBenefitConfig.HRBENEFITPROJECTJOINS).eq(HrBenefitProjectJoin.PROJECT, p).first() != null)
					BMessage.sendToAccountants(hsu, "Unpaid time notice", "Unpaid time approved for " + timesheet.getPerson().getNameLFM()
							+ " on " + DateUtils.getDateFormatted(timesheet.getWorkDate())
							+ " for " + timesheet.getTotalHours() + " hours.  Project was " + p2.getReference()
							+ ":" + p2.getDescription());

			} catch (final Exception e) {
				logger.error(e);
				//if there is a notification issue, just keep going
			}
		}
	}

	public void setPersonId(final String personId) {
		timesheet.setPerson(ArahantSession.getHSU().get(Person.class, personId));
		if (timesheet.getPerson() == null)
			throw new ArahantException("Can't set timesheet person to nobody.");
	}

	public boolean hasRunOver() throws ArahantException {
		final String benefitId = getBenefitId();
		if (isEmpty(benefitId))
			return false;

		final BEmployee be = new BEmployee(timesheet.getPerson().getPersonId());
		return be.getTimeOffCurrentPeriod(benefitId) < 0;
	}

	public String getBenefitId() throws ArahantException {
		if (timesheet.getProjectShift().getProject() == null)
			return "";
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		//timesheet.getProject().getHrBenefitProjectJoins().size()==0
		if (!hsu.createCriteria(HrBenefitConfig.class).joinTo(HrBenefitConfig.HRBENEFITPROJECTJOINS).eq(HrBenefitProjectJoin.PROJECT, timesheet.getProjectShift().getProject()).exists())
			return "";
		final HibernateCriteriaUtil<HrBenefit> hcu = hsu.createCriteria(HrBenefit.class);
		HibernateCriteriaUtil<HrBenefit> confHcu = hcu.joinTo(HrBenefit.HR_BENEFIT_CONFIGS);
		confHcu.joinTo(HrBenefitConfig.HRBENEFITPROJECTJOINS).eq(HrBenefitProjectJoin.PROJECT, timesheet.getProjectShift().getProject());
		confHcu.joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).eq(HrBenefitJoin.PAYING_PERSON, timesheet.getPerson());

		HrBenefit b = (HrBenefit) hcu.first();

		//The employee doesn't have an active benefit, so check history to see if they used to have it
		if (b == null) {
			final HibernateCriteriaUtil<HrBenefit> hcu2 = hsu.createCriteria(HrBenefit.class);
			HibernateCriteriaUtil<HrBenefit> confHcu2 = hcu2.joinTo(HrBenefit.HR_BENEFIT_CONFIGS);
			confHcu2.joinTo(HrBenefitConfig.HRBENEFITPROJECTJOINS).eq(HrBenefitProjectJoin.PROJECT, timesheet.getProjectShift().getProject());
			confHcu2.joinTo(HrBenefitConfig.HR_BENEFIT_JOINS_H).eq(HrBenefitJoinH.PAYING_PERSON, timesheet.getPerson()).dateAfter(HrBenefitJoinH.COVERAGE_END_DATE, timesheet.getWorkDate());

			b = (HrBenefit) hcu2.first();
		}

		if (b == null)
			throw new ArahantWarning("Employee does not have benefit associated to this project.");

		return b.getBenefitId();
	}

	public static void markUnpaidHandled(final HibernateSessionUtil hsu, final String[] unpaidTimesheetIds) throws ArahantException {
		for (final String element : unpaidTimesheetIds)
			new BTimesheet(element).markUnpaidHandled();
	}

	private void markUnpaidHandled() {
		timesheet.setState('I');
	}

	public static BTimesheet[] listUnpaidTime(final HibernateSessionUtil hsu) {
		final HibernateCriteriaUtil<Timesheet> hcu = hsu.createCriteria(Timesheet.class);
		hcu.eq(Timesheet.STATE, TIMESHEET_APPROVED).joinTo(Timesheet.PROJECTSHIFT).joinTo(ProjectShift.PROJECT).joinTo(Project.HRBENEFITPROJECTJOINS).joinTo(HrBenefitProjectJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.PAIDBENEFIT, 'N');
		hcu.joinTo(Timesheet.PERSON).orderBy(Person.LNAME).orderBy(Person.FNAME);
		return makeArray(hcu.list());
	}

	public String getEmployeeFirstName() {
		return timesheet.getPerson().getFname();
	}

	public String getEmployeeLastName() {
		return timesheet.getPerson().getLname();
	}

	public String getEmployeeNameLFM() {
		return timesheet.getPerson().getNameLFM();
	}

	public static String getUnpaidReport(final HibernateSessionUtil hsu) throws ArahantException {
		return new UnpaidTimesheetReport().getReport(listUnpaidTime(hsu));
	}

	static BTimesheet[] makeArray(final HibernateScrollUtil<Timesheet> scr) {
		List<Timesheet> l = new ArrayList<Timesheet>();
		while (scr.next()) {
			Timesheet ts = scr.get();
			l.add(ts);
		}
		return BTimesheet.makeArray(l);
	}

	static BTimesheet[] makeArray(final List<Timesheet> l) {
		final Iterator<Timesheet> timeItr = l.iterator();
		final BTimesheet[] ret = new BTimesheet[l.size()];
		for (int loop = 0; loop < l.size(); loop++)
			ret[loop] = new BTimesheet(timeItr.next());
		return ret;
	}

	public static BSearchOutput<BTimesheet> search(BSearchMetaInput metaInput, int billableItemsIndicator, String companyId, String[] excludedTimesheetIds,
			String personId, String projectId, int timesheetEndDate, int timesheetStartDate, int cap) {

		BOrgGroup borg = new BOrgGroup(companyId);
		String[] ids = borg.getAllGroupsForCompany();

		HibernateCriteriaUtil<Timesheet> hcu = ArahantSession.getHSU().createCriteria(Timesheet.class);

		HibernateCriteriaUtil<Timesheet> projHcu = hcu.joinTo(Timesheet.PROJECTSHIFT).joinTo(ProjectShift.PROJECT);

		projHcu.joinTo(Project.REQUESTING_ORG_GROUP).in(CompanyBase.ORGGROUPID, ids);

		if (billableItemsIndicator == 1)
			hcu.eq(Timesheet.BILLABLE, 'Y');

		if (billableItemsIndicator == 2)
			hcu.ne(Timesheet.BILLABLE, 'Y');

		if (excludedTimesheetIds.length > 0)
			hcu.notIn(Timesheet.TIMESHEETID, excludedTimesheetIds);

		HibernateCriteriaUtil<Timesheet> perHcu = hcu.joinTo(Timesheet.PERSON);

		if (!isEmpty(personId))
			perHcu.eq(Person.PERSONID, personId);

		if (!isEmpty(projectId))
			projHcu.eq(Project.PROJECTID, projectId);

		hcu.ge(Timesheet.WORKDATE, timesheetStartDate);

		if (timesheetEndDate > 0)
			hcu.le(Timesheet.WORKDATE, timesheetEndDate);

		hcu.eq(Timesheet.STATE, TIMESHEET_APPROVED);

		//	hcu.orderBy(Timesheet.WORKDATE).orderBy(Timesheet.BEGINNINGTIME).setMaxResults(cap);

		////externalReference, projectName, billable, billingRate, workDate, totalHours, timeDescription, lastName, firstName
		switch (metaInput.getSortType()) {
			case 1:
            case 7:  // WTG displays project summary in the timesheet description column
				projHcu.orderBy(Project.PROJECTNAME, metaInput.isSortAsc());
				break;
			case 2:
				projHcu.orderBy(Project.REFERENCE, metaInput.isSortAsc());
				break;
			case 3:
				hcu.orderBy(Timesheet.BILLABLE, metaInput.isSortAsc());
				break;
			case 4:
				projHcu.orderBy(Project.BILLINGRATE, metaInput.isSortAsc());
				break;
			case 5:
				hcu.orderBy(Timesheet.WORKDATE, metaInput.isSortAsc());
				break;
			case 6:
				hcu.orderBy(Timesheet.TOTALHOURS, metaInput.isSortAsc());
				break;
				/*
			case 7:    //  WTG displays project summary in the timesheet description column
				hcu.orderBy(Timesheet.DESCRIPTION, metaInput.isSortAsc());
				break;
				*/
			case 8:
				perHcu.orderBy(Person.LNAME, metaInput.isSortAsc()).orderBy(Person.FNAME, metaInput.isSortAsc());
				break;
			case 9:
				perHcu.orderBy(Person.FNAME, metaInput.isSortAsc());
				break;
			default:
				projHcu.orderBy(Project.PROJECTNAME, metaInput.isSortAsc());
                break;
		}
        hcu.orderBy(Timesheet.WORKDATE, metaInput.isSortAsc());

		//return makeArray(hcu.list());
		return makeSearchOutput(metaInput, hcu);
	}

	public static BSearchOutput<BTimesheet> makeSearchOutput(BSearchMetaInput searchMeta, HibernateCriteriaUtil<Timesheet> hcu) {
		BSearchOutput<BTimesheet> ret = new BSearchOutput<BTimesheet>(searchMeta);

		HibernateScrollUtil<Timesheet> scr = hcu.getPage(searchMeta);

		if (searchMeta.isUsingPaging())
			ret.setTotalItemsPaging(hcu.countNoOrder());

		// set output
		ret.setItems(BTimesheet.makeArray(scr));

		return ret;
	}

	public static void markDeferred(String[] timesheetIds) {
		for (String id : timesheetIds) {
			BTimesheet t = new BTimesheet(id);
			t.setState('D');
			t.update();
		}
	}

	public static HibernateScrollUtil<Timesheet> searchForAccountingReview(int billableItemsIndicator, String companyId, String personId,
                                                                           String projectId, int timesheetEndDate, int timesheetStartDate, String timesheetState, String invoiceIdentifier, int cap) {

		HibernateCriteriaUtil<Timesheet> hcu = ArahantSession.getHSU().createCriteria(Timesheet.class);

		HibernateCriteriaUtil<Timesheet> projHcu = hcu.joinTo(Timesheet.PROJECTSHIFT).joinTo(ProjectShift.PROJECT);

		if (!isEmpty(companyId))
			projHcu.joinTo(Project.REQUESTING_ORG_GROUP).eq(CompanyBase.ORGGROUPID, companyId);

		if (billableItemsIndicator == 1)
			hcu.eq(Timesheet.BILLABLE, 'Y');

		if (billableItemsIndicator == 2)
			hcu.ne(Timesheet.BILLABLE, 'Y');

		if (!isEmpty(timesheetState)) {
			char tsState = timesheetState.charAt(0);
			if (tsState != 'U')
				hcu.eq(Timesheet.STATE, tsState);
			else
				hcu.in(Timesheet.STATE, "NRFCP".toCharArray());
		}

		if (!isEmpty(invoiceIdentifier) && !invoiceIdentifier.equals("*") && !invoiceIdentifier.equals("%"))
			hcu.joinTo(Timesheet.INVOICELINEITEM).joinTo(InvoiceLineItem.INVOICE).like(Invoice.ACCOUNTINGINVOICEIDENTIFIER, invoiceIdentifier);

		HibernateCriteriaUtil<Timesheet> persHcu = hcu.joinTo(Timesheet.PERSON);
		if (!isEmpty(personId))
			persHcu.eq(Person.PERSONID, personId);

		if (!isEmpty(projectId))
			projHcu.eq(Project.PROJECTID, projectId);

		hcu.ge(Timesheet.WORKDATE, timesheetStartDate);

		if (timesheetEndDate > 0)
			hcu.le(Timesheet.WORKDATE, timesheetEndDate);

		hcu.orderBy(Timesheet.WORKDATE).orderBy(Timesheet.BEGINNINGTIME).setMaxResults(cap);
		persHcu.orderBy(Person.LNAME).orderBy(Person.FNAME);
		return hcu.scroll();
	}

    public static HibernateScrollUtil<Timesheet> searchForAccountingReviewExport(int billableItemsIndicator, String companyId, String personId,
                                                                           String projectId, int timesheetEndDate, int timesheetStartDate, String timesheetState, String invoiceIdentifier, int cap) {

        HibernateCriteriaUtil<Timesheet> hcu = ArahantSession.getHSU().createCriteria(Timesheet.class);

        HibernateCriteriaUtil<Timesheet> projHcu = hcu.joinTo(Timesheet.PROJECTSHIFT).joinTo(ProjectShift.PROJECT);

        if (!isEmpty(companyId))
            projHcu.joinTo(Project.REQUESTING_ORG_GROUP).eq(CompanyBase.ORGGROUPID, companyId);

        if (billableItemsIndicator == 1)
            hcu.eq(Timesheet.BILLABLE, 'Y');

        if (billableItemsIndicator == 2)
            hcu.ne(Timesheet.BILLABLE, 'Y');

        if (!isEmpty(timesheetState)) {
            char tsState = timesheetState.charAt(0);
            if (tsState != 'U')
                hcu.eq(Timesheet.STATE, tsState);
            else
                hcu.in(Timesheet.STATE, "NRFCP".toCharArray());
        }

        if (!isEmpty(invoiceIdentifier) && !invoiceIdentifier.equals("*") && !invoiceIdentifier.equals("%"))
            hcu.joinTo(Timesheet.INVOICELINEITEM).joinTo(InvoiceLineItem.INVOICE).like(Invoice.ACCOUNTINGINVOICEIDENTIFIER, invoiceIdentifier);

        HibernateCriteriaUtil<Timesheet> personHcu = hcu.joinTo(Timesheet.PERSON);
        if (!isEmpty(personId))
            personHcu.eq(Person.PERSONID, personId);
        personHcu.orderBy(Person.LNAME).orderBy(Person.FNAME);

        if (!isEmpty(projectId))
            projHcu.eq(Project.PROJECTID, projectId);
        projHcu.orderBy(Project.PROJECTID);

        hcu.ge(Timesheet.WORKDATE, timesheetStartDate);

        if (timesheetEndDate > 0)
            hcu.le(Timesheet.WORKDATE, timesheetEndDate);

        hcu.orderBy(Timesheet.WORKDATE).orderBy(Timesheet.BEGINNINGTIME).setMaxResults(cap);
        return hcu.scroll();
    }

	// For Goldstein and Sacks
	public static HibernateScrollUtil<Timesheet> searchForAccountingReview(int billableItemsIndicator, String companyId, String personId,
                                                                           String projectId, int timesheetEndDate, int timesheetStartDate, String timesheetState, String invoiceIdentifier, int cap, String summary) {

		HibernateCriteriaUtil<Timesheet> hcu = ArahantSession.getHSU().createCriteria(Timesheet.class).gt(Timesheet.TOTALHOURS, 0.0);

		HibernateCriteriaUtil<Timesheet> projHcu = hcu.joinTo(Timesheet.PROJECTSHIFT).joinTo(ProjectShift.PROJECT);

		if (!isEmpty(companyId))
			projHcu.joinTo(Project.REQUESTING_ORG_GROUP).eq(CompanyBase.ORGGROUPID, companyId);

		if (!isEmpty(summary))
			projHcu.like(Project.DESCRIPTION, summary);

		if (billableItemsIndicator == 1)
			hcu.eq(Timesheet.BILLABLE, 'Y');

		if (billableItemsIndicator == 2)
			hcu.ne(Timesheet.BILLABLE, 'Y');

		if (!isEmpty(timesheetState))
			hcu.eq(Timesheet.STATE, timesheetState.charAt(0));

		if (!isEmpty(invoiceIdentifier) && !invoiceIdentifier.equals("*") && !invoiceIdentifier.equals("%"))
			hcu.joinTo(Timesheet.INVOICELINEITEM).joinTo(InvoiceLineItem.INVOICE).like(Invoice.ACCOUNTINGINVOICEIDENTIFIER, invoiceIdentifier);

		/*
		 * HibernateCriteriaUtil
		 * personHcu=hcu.joinTo(Timesheet.PERSON).orderBy(Person.LNAME).orderBy(Person.FNAME);
		 * //.orderBy(Person.MNAME).orderBy(Person.PERSONID);
		 *
		 * if (!isEmpty(personId)) personHcu.eq(Person.PERSONID, personId);
		 */
		if (!isEmpty(personId))
			hcu.joinTo(Timesheet.PERSON).eq(Person.PERSONID, personId);

		if (!isEmpty(projectId))
			projHcu.eq(Project.PROJECTID, projectId);

		hcu.ge(Timesheet.WORKDATE, timesheetStartDate);

		if (timesheetEndDate > 0)
			hcu.le(Timesheet.WORKDATE, timesheetEndDate);

		hcu.orderBy(Timesheet.WORKDATE).orderBy(Timesheet.BEGINNINGTIME).setMaxResults(cap);
		return hcu.scroll();
	}

	public String getInvoiceIdentifier() {
		try {
			return timesheet.getInvoiceLineItem().getInvoice().getAccountingInvoiceIdentifier();
		} catch (Exception e) {
			return "";
		}
	}

	public static BTimesheet[] search(int startDate, int finalDate, boolean notApproved, boolean invoiced, boolean approved, String projectId, int cap, String shiftId) {
		HibernateCriteriaUtil<Timesheet> hcTime = ArahantSession.getHSU().createCriteria(Timesheet.class).setMaxResults(cap);

		if (startDate > 0)
			hcTime.ge(Timesheet.WORKDATE, startDate);
		if (finalDate > 0)
			hcTime.le(Timesheet.WORKDATE, finalDate);
		final List<Character> stateList = new LinkedList<Character>();

		if (notApproved) {
			stateList.add(TIMESHEET_NEW);
			stateList.add(TIMESHEET_CHANGED);
			stateList.add(TIMESHEET_FIXED);
			stateList.add(TIMESHEET_REJECTED);
			stateList.add(TIMESHEET_PROBLEM);
			stateList.add(TIMESHEET_SUBMITTED);
		}

		if (invoiced)
			stateList.add(TIMESHEET_INVOICED);
		if (approved)
			stateList.add(TIMESHEET_APPROVED);
		hcTime.in(Timesheet.STATE, stateList);


		hcTime.orderBy(Timesheet.WORKDATE).orderBy(Timesheet.BEGINNINGTIME);


		hcTime = hcTime.joinTo(Timesheet.PROJECTSHIFT);

		if (shiftId != null  &&  !shiftId.isEmpty())
			hcTime.eq(ProjectShift.PROJECTSHIFTID, shiftId);

		if (projectId != null && !projectId.isEmpty())
			hcTime.joinTo(ProjectShift.PROJECT).eq(Project.PROJECTID, projectId);

		hcTime.distinct();

		return makeArray(hcTime.list());
	}

	public static BTimesheet[] search(int fromDate, int toDate, int type, String orgGroupId, int cap) {
		HibernateCriteriaUtil<Timesheet> hcu = ArahantSession.getHSU().createCriteria(Timesheet.class).setMaxResults(cap).orderBy(Timesheet.WORKDATE).dateBetween(Timesheet.WORKDATE, fromDate, toDate);
		switch (type) {
			case 0:
				break;
			case 1:
				hcu.notIn(Timesheet.STATE, new char[]{TIMESHEET_APPROVED, TIMESHEET_INVOICED});//not finalized
				break;
			case 2:
				hcu.in(Timesheet.STATE, new char[]{TIMESHEET_APPROVED, TIMESHEET_INVOICED});//finalized;
				break;
			default:
				throw new ArahantException("Bad type passed into timesheet search " + type);
		}

		if (!isEmpty(orgGroupId))
			hcu.joinTo(Timesheet.PERSON).joinTo(Person.ORGGROUPASSOCIATIONS).joinTo(OrgGroupAssociation.ORGGROUP).eq(OrgGroup.ORGGROUPID, orgGroupId);

		return makeArray(hcu.list());
	}

	/**
	 * Round time to the nearest value based on system properties
	 */
	public static int round(int time) {
		int roundIncrement = BProperty.getInt(StandardProperty.TIME_ROUND, 15);
		int roundAt = BProperty.getInt(StandardProperty.TIME_ROUND_CUTOFF, 8);

		int ret = (time / roundIncrement) * roundIncrement;
		if (time % roundIncrement > roundAt)
			ret += roundIncrement;

		return ret;
	}

	public static double round(double timeDif) {
		//convert to minutes
		return ((double) round((int) (timeDif * 60))) / 60;
	}

	public void setWageTypeId(String wageTypeId) {
		timesheet.setWageType(ArahantSession.getHSU().get(WageType.class, wageTypeId));
	}

	public String getWageTypeId() {
		if (timesheet.getWageType() == null)
			return "";
		return timesheet.getWageType().getWageTypeId();
	}

	public Person getBeginningEntryPerson() {
		return timesheet.getBeginningEntryPerson();
	}

	public void setBeginningEntryPerson(Person beginningEntryPerson) {
		timesheet.setBeginningEntryPerson(beginningEntryPerson);
	}

	public Person getEndingEntryPerson() {
		return timesheet.getEndingEntryPerson();
	}

	public void setEndEntryPerson(Person endingEntryPerson) {
		timesheet.setEndingEntryPerson(endingEntryPerson);
	}

	//======
	public static HibernateScrollUtil getTimesheetsForEmployee(
			final HibernateSessionUtil hsu,
			final String employeeId,
			final int startDate,
			final int endDate) throws ArahantException {

		final HibernateCriteriaUtil<Timesheet> hcTime = hsu.createCriteria(Timesheet.class);
		hcTime.sum(Timesheet.TOTALHOURS, Timesheet.PROJECTSHIFT, Timesheet.BILLABLE);
		final HibernateCriteriaUtil<Timesheet> hcPerson = hcTime.joinTo(Timesheet.PERSON);
		hcPerson.eq(Person.PERSONID, employeeId);


		if (startDate > 0)
			hcTime.ge(Timesheet.WORKDATE, startDate);

		if (endDate > 0)
			hcTime.le(Timesheet.WORKDATE, endDate);

		return hcTime.scroll();
	}

	public int getBeginningTimeZoneOffset() {
		return timesheet.getBeginningTimeZoneOffset();
	}

	public void setBeginningTimeZoneOffset(int beginningTimeZoneOffset) {
		timesheet.setBeginningTimeZoneOffset(beginningTimeZoneOffset);
	}

	public int getEndTimeZoneOffset() {
		return timesheet.getEndTimeZoneOffset();
	}

	public void setEndTimeZoneOffset(int endTimeZoneOffset) {
		timesheet.setEndTimeZoneOffset(endTimeZoneOffset);
	}

	public ProjectShift getProjectShift() {
		return timesheet.getProjectShift();
	}

	public BTimesheet setProjectShift(ProjectShift ps) {
		timesheet.setProjectShift(ps);
		return this;
	}

	public String getProjectShiftId() {
		return timesheet.getProjectShiftId();
	}

	public BTimesheet setProjectShiftId(String id) {
		timesheet.setProjectShift(new BProjectShift(id).getProjectShift());
		return this;
	}
}
