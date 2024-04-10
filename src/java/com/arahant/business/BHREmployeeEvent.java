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

import com.arahant.beans.Employee;
import com.arahant.beans.HrEmployeeEvent;
import com.arahant.beans.Person;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantSecurityException;
import com.arahant.reports.HREventReport;
import com.arahant.utils.*;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.io.File;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import static com.arahant.utils.ArahantSession.getHSU;

public class BHREmployeeEvent extends BusinessLogicBase implements IDBFunctions {

	private static final transient ArahantLogger logger = new ArahantLogger(BHREmployeeEvent.class);
	private HrEmployeeEvent hrEmployeeEvent;
	private int eventTime;

	/**
	 */
	public BHREmployeeEvent() {
	}

	/**
	 * @param event
	 */
	public BHREmployeeEvent(final HrEmployeeEvent event) {
		hrEmployeeEvent = event;
	}

	/**
	 * @param id
	 * @throws ArahantException
	 */
	public BHREmployeeEvent(final String id) throws ArahantException {
		internalLoad(id);
	}

	@Override
	public String create() throws ArahantException {
		hrEmployeeEvent = new HrEmployeeEvent();
		hrEmployeeEvent.generateId();
		return hrEmployeeEvent.getEventId();
	}

	@Override
	public void delete() throws ArahantDeleteException {
		try {
			checkAccess();
		} catch (final ArahantException e) {
			throw new ArahantDeleteException(e);
		}
		getHSU().delete(hrEmployeeEvent);
	}

	@Override
	public void insert() throws ArahantException {
		// this was causing ongoing problems for WTG
		// front-end should be doing this check
		//checkAccess();
		getHSU().insert(hrEmployeeEvent);
	}

	private void checkAccess() throws ArahantException {
		if (BRight.checkRight(EVENTS) != ACCESS_LEVEL_WRITE)
			throw new ArahantSecurityException("Employees don't have access to this feature.");
		if (BRight.checkRight(OLD_EVENTS) != ACCESS_LEVEL_WRITE) {
			final Calendar evd = DateUtils.getCalendar(hrEmployeeEvent.getEventDate());

			if (evd.after(DateUtils.getNow()))
				throw new ArahantException("Can't add future events.");

			final Calendar twoWeeksAgo = DateUtils.getNow();
			twoWeeksAgo.add(Calendar.DAY_OF_YEAR, -14);
			if (evd.before(twoWeeksAgo))
				throw new ArahantException("Can't change events over two weeks old.");
		}
	}

	private void internalLoad(final String key) throws ArahantException {
		hrEmployeeEvent = getHSU().get(HrEmployeeEvent.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		hrEmployeeEvent = getHSU().get(HrEmployeeEvent.class, key);
	}

	@Override
	public void update() throws ArahantException {
		checkAccess();
		getHSU().saveOrUpdate(hrEmployeeEvent);
	}

	public int getDateNotified() {
		return hrEmployeeEvent.getDateNotified();
	}

	public String getDetail() {
		return hrEmployeeEvent.getDetail();
	}

	public char getEmployeeNotified() {
		return hrEmployeeEvent.getEmployeeNotified();
	}

	public int getEventDate() {
		return hrEmployeeEvent.getEventDate();
	}

	public String getEventId() {
		return hrEmployeeEvent.getEventId();
	}

	public String getSummary() {
		return hrEmployeeEvent.getSummary();
	}

	public void setDateNotified(final int dateNotified) {
		hrEmployeeEvent.setDateNotified(dateNotified);
	}

	public void setDetail(final String detail) {
		hrEmployeeEvent.setDetail(detail);
	}

	public void setEmployeeNotified(final char employeeNotified) {
		hrEmployeeEvent.setEmployeeNotified(employeeNotified);
	}

	/**
	 * @param eventDate
	 * @see com.arahant.beans.HrEmployeeEvent#setEventDate(int)
	 */
	public void setEventDate(final int eventDate) {
		hrEmployeeEvent.setEventDate(eventDate);
	}

	/**
	 * @param eventId
	 * @see com.arahant.beans.HrEmployeeEvent#setEventId(java.lang.String)
	 */
	public void setEventId(final String eventId) {
		hrEmployeeEvent.setEventId(eventId);
	}

	/**
	 * @param summary
	 * @see com.arahant.beans.HrEmployeeEvent#setSummary(java.lang.String)
	 */
	public void setSummary(final String summary) {
		hrEmployeeEvent.setSummary(summary);
	}

	public char getEventType() {
		return hrEmployeeEvent.getEventType().charAt(0);
	}

	public void setEventType(char eventType) {
		hrEmployeeEvent.setEventType(eventType+"");
	}

	/**
	 * @param hsu
	 * @param personId
	 * @return
	 */
	public static BHREmployeeEvent[] list(final HibernateSessionUtil hsu, final String personId, int max) {
		HibernateCriteriaUtil<HrEmployeeEvent> hcu = hsu.createCriteria(HrEmployeeEvent.class).orderByDesc(HrEmployeeEvent.EVENTDATE).joinTo(HrEmployeeEvent.EMPLOYEEID).eq(Person.PERSONID, personId);
		if (max > 0)
			hcu.setMaxResults(max);
		final List<HrEmployeeEvent> lst = hcu.list();
		final BHREmployeeEvent[] ret = makeArray(lst);
		return ret;
	}

	public static String getReport(final HibernateSessionUtil hsu, final String employeeId, final int startDate, final int endDate, final boolean asc, int max) throws Exception {
		final File fyle = FileSystemUtils.createTempFile("HREventRept", ".pdf");
		final HibernateCriteriaUtil<HrEmployeeEvent> hcu = hsu.createCriteria(HrEmployeeEvent.class);
		if (asc)
			hcu.orderBy(HrEmployeeEvent.EVENTDATE);
		else
			hcu.orderByDesc(HrEmployeeEvent.EVENTDATE);

		hcu.joinTo(HrEmployeeEvent.EMPLOYEEID).eq(Person.PERSONID, employeeId);

		if (startDate > 0)
			hcu.ge(HrEmployeeEvent.EVENTDATE, startDate);
		if (endDate > 0)
			hcu.le(HrEmployeeEvent.EVENTDATE, endDate);
		if (max > 0)
			hcu.setMaxResults(max);

		new HREventReport().build(hsu, fyle, employeeId, makeArray(hcu.list()), startDate, endDate, asc, max);

		return FileSystemUtils.getHTTPPath(fyle);
	}

	/**
	 * @param l
	 * @return
	 */
	static BHREmployeeEvent[] makeArray(final List<HrEmployeeEvent> l) {
		final BHREmployeeEvent[] events = new BHREmployeeEvent[l.size()];
		for (int loop = 0; loop < events.length; loop++)
			events[loop] = new BHREmployeeEvent(l.get(loop));
		return events;
	}

	public String getSupervisorFirstName() {
		return hrEmployeeEvent.getSupervisorId().getFname();
	}

	public String getSupervisorLastName() {
		return hrEmployeeEvent.getSupervisorId().getLname();
	}

	public String getSupervisorId() {
		if (hrEmployeeEvent == null)
			return null;
		Person sid = hrEmployeeEvent.getSupervisorId();
		if (sid == null)
			return null;
		return sid.getPersonId();
	}

	/**
	 * @param employeeId
	 */
	public void setEmployeeId(final String employeeId) {
		hrEmployeeEvent.setEmployeeId(getHSU().get(Employee.class, employeeId));
	}

	/**
	 * @param supervisorId
	 */
	public void setSupervisorId(final String supervisorId) {
		hrEmployeeEvent.setSupervisorId(getHSU().get(Person.class, supervisorId));
	}

	/**
	 * @param hsu
	 * @param ids
	 * @throws ArahantException
	 * @throws ArahantDeleteException
	 */
	public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantDeleteException, ArahantException {
		for (final String element : ids)
			new BHREmployeeEvent(element).delete();
	}

	public String getSupervisorNameLFM() {
		return hrEmployeeEvent.getSupervisorId().getNameLFM();
	}

	public void sendEmailNotification(char notificationType) throws Exception {
		sendEmailNotification(notificationType, null, null);
	}

	public void sendEmailAccidentReport(char notificationType) throws Exception {
		sendEmailAccidentReport(notificationType, null, null);
	}
	public void sendEmailNotification(char notificationType, String attachmentType, byte [] attachment) throws Exception {
		HibernateSessionUtil hsu = getHSU();
		Connection db = KissConnection.get();

		List<Record> recs = db.fetchAll("select * from email_notifications where notification_type=?", notificationType);
		if (recs.isEmpty())
			return;

		SendEmailGeneric em = SendEmailProvider.newEmail();
		BPerson bp = new BPerson(hrEmployeeEvent.getEmployeeId());
		String worker = bp.getNameLFM();
		String msg = "Reported by: " + hsu.getCurrentPerson().getNameLFM() + "<br><br>";
		msg += "Worker: " + worker + "<br>";
		msg += "Event Date: " + DateUtils.getDateFormatted(getEventDate()) + "<br>";
		msg += "Worker Notified: " + (getEmployeeNotified() == 'Y' ? "Yes" : "No") + "<br>";
		if (getEmployeeNotified() == 'Y')
			msg += "Date Worker Notified: " + DateUtils.getDateFormatted(getDateNotified()) + "<br>";
		msg += "Summary: " + getSummary() + "<br>";
		msg += "Detail: " + getDetail();
		em.setHTMLMessage(msg);
		if (attachmentType != null && attachment != null)
			em.addAttachement(attachment, "image." + attachmentType);
		for (Record rec : recs)
			em.sendEmail(rec.getString("email"), rec.getString("name"), "Worker writeup - " + worker);
		em.close();
	}

	public void sendEmailAccidentReport(char notificationType, String attachmentType, byte [] attachment) throws Exception {
		final HibernateSessionUtil hsu = getHSU();
		final Connection db = hsu.getKissConnection();

		final List<Record> recs = db.fetchAll("select * from email_notifications where notification_type=?", notificationType);

		final List<String> ids = BProperty.getIDList("EMAIL_ACCIDENT_REPORT", "person", "person_id");

		if (recs.isEmpty() && ids.isEmpty())
			return;

		SendEmailGeneric em = SendEmailProvider.newEmail();
		BPerson bp = new BPerson(hrEmployeeEvent.getEmployeeId());
		String worker = bp.getNameLFM();
		String msg = "Reported by: " + hsu.getCurrentPerson().getNameLFM() + "<br><br>";
		msg += "Worker: " + worker + "<br>";
		msg += "Event Date: " + DateUtils.getDateFormatted(getEventDate()) + "<br>";
		msg += "Worker Notified: " + (getEmployeeNotified() == 'Y' ? "Yes" : "No") + "<br>";
		if (getEmployeeNotified() == 'Y')
			msg += "Date Worker Notified: " + DateUtils.getDateFormatted(getDateNotified()) + "<br>";
		msg += "Summary: " + getSummary() + "<br>";
		msg += "Detail: " + getDetail();
		em.setHTMLMessage(msg);
		if (attachmentType != null && attachment != null)
			em.addAttachement(attachment, "image." + attachmentType);
		for (Record rec : recs)
			em.sendEmail(rec.getString("email"), rec.getString("name"), "ACCIDENT REPORT - " + worker);

		for (String id : ids) {
			Record rec = db.fetchOne("select fname, lname, personal_email from person where person_id=?", id);
			String name = rec.getString("fname") + " " + rec.getString("lname");
			em.sendEmail(rec.getString("personal_email"), name, "ACCIDENT REPORT - " + worker);
		}
		em.close();
	}

	/**
	 * Returns a string containing all of the events that occurred for a given employee on a particular date
	 * while skipping the labels.
	 *
	 * @param db
	 * @param personId
	 * @param workDate
	 * @return
	 * @throws SQLException
	 */
	public static String getNonLabelEventString(Connection db, String personId, int workDate) throws Exception {
		List<Record> events = db.fetchAll("select summary, detail " +
				"from hr_employee_event " +
				"where employee_id = ? " +
				"      and event_type <> 'L' " +
				"      and event_date = ? ", personId, workDate);
		StringBuilder eventS = new StringBuilder();
		String prevSummary = "";
		String prevDetail = "";
		for (Record evt : events) {
			String summary = evt.getString("summary");
			String detail = evt.getString("detail");
			if (detail != null && !detail.isEmpty())
				detail = detail.replaceAll("\n", " ");
			if (!summary.contains(" Label Added") && !summary.contains(" Label Removed") &&
					(!prevSummary.equalsIgnoreCase(summary) || !prevDetail.equalsIgnoreCase(detail))) {
				if (eventS.length() > 0)
					eventS.append(", ");
				if (detail == null  ||  detail.isEmpty())
					eventS.append(summary);
				else
					eventS.append(summary).append(" - ").append(detail);
				prevDetail = detail;
				prevSummary = summary;
			}
		}
		return eventS.toString();
	}

	/**
	 * Returns a string containing all of the events that occurred for a given employee on a particular date
	 * that came from the mobile app.
	 *
	 * @param db
	 * @param personId
	 * @param workDate
	 * @return
	 * @throws SQLException
	 */
	public static String getMobileEventString(Connection db, String personId, int workDate) throws Exception {
		List<Record> events = db.fetchAll("select summary, detail " +
				"from hr_employee_event " +
				"where employee_id = ? " +
				"      and event_type = 'M' " +
				"      and event_date = ? ", personId, workDate);
		StringBuilder eventS = new StringBuilder();
		String prevSummary = "";
		String prevDetail = "";
		for (Record evt : events) {
			String summary = evt.getString("summary");
			String detail = evt.getString("detail");
			if (detail != null && !detail.isEmpty())
				detail = detail.replaceAll("\n", " ");
			if ((!prevSummary.equalsIgnoreCase(summary) || !prevDetail.equalsIgnoreCase(detail))) {
				if (eventS.length() > 0)
					eventS.append(", ");
				if (detail == null  ||  detail.isEmpty())
					eventS.append(summary);
				else
					eventS.append(summary).append(" - ").append(detail);
				prevDetail = detail;
				prevSummary = summary;
			}
		}
		return eventS.toString();
	}

	public int getEventTime() {
		return eventTime;
	}

	public void setEventTime(int eventTime) {
		this.eventTime = eventTime;
	}
}
