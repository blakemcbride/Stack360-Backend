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
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class BAppointment extends SimpleBusinessObjectBase<Appointment> implements BEvent {

	public static void delete(String[] ids) {
		for (String id : ids)
			new BAppointment(id).delete();
	}

	static BAppointment[] makeArray(List<Appointment> l) {
		BAppointment[] ret = new BAppointment[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BAppointment(l.get(loop));
		return ret;
	}

	public BAppointment(Appointment o) {
		super();
		bean = o;
	}

	public BAppointment() {
		super();
	}

	public BAppointment(String key) {
		super(key);
	}

	public void addAttendee(String id, boolean primary) {
		Person p = ArahantSession.getHSU().get(Person.class, id);
		AppointmentPersonJoin apj = ArahantSession.getHSU().createCriteria(AppointmentPersonJoin.class).eq(AppointmentPersonJoin.PERSON, p).eq(AppointmentPersonJoin.APPOINTMENT, bean).first();
		if (apj == null) {
			apj = new AppointmentPersonJoin();
			apj.generateId();
			apj.setAppointment(bean);
			apj.setPerson(p);
		}
		apj.setPrimaryPerson(primary ? 'Y' : 'N');
		updates.add(apj);
	}

	@Override
	public String create() throws ArahantException {
		bean = new Appointment();
		return bean.generateId();
	}

	public void deleteAttendeeJoins() {
		ArahantSession.getHSU().createCriteria(AppointmentPersonJoin.class).eq(AppointmentPersonJoin.APPOINTMENT, bean).delete();
	}

	public String getAttendees() {
		return bean.getProspectAttendees();
	}

	public String getCompanyId() {
		return bean.getCompany().getOrgGroupId();
	}

	public String getCompanyName() {
		return bean.getCompany().getName();
	}

	public BPerson[] getContacts() {
		return BPerson.makeArray(ArahantSession.getHSU().createCriteria(Person.class).orderBy(Person.LNAME).orderBy(Person.FNAME).ne(Person.ORGGROUPTYPE, COMPANY_TYPE).joinTo(Person.APPOINTMENT_PERSON_JOIN).eq(AppointmentPersonJoin.APPOINTMENT, bean).list());
	}

	public int getDate() {
		return bean.getMeetingDate();
	}

	public BPerson[] getEmployees() {
		return BPerson.makeArray(ArahantSession.getHSU().createCriteria(Person.class).orderBy(Person.LNAME).orderBy(Person.FNAME).eq(Person.ORGGROUPTYPE, COMPANY_TYPE).joinTo(Person.APPOINTMENT_PERSON_JOIN).eq(AppointmentPersonJoin.APPOINTMENT, bean).list());
	}

	public String getId() {
		return bean.getAppointmentId();
	}

	public int getLength() {
		return bean.getMeetingLength();
	}

	public String getLocation() {
		return bean.getMeetingLocation();
	}

	public String getMeetingLocation() {
		return getLocation();
	}

	public int getMeetingTime() {
		return getTime();
	}

	public int getMeetingDate() {
		return getDate();
	}

	public String getPurpose() {
		return bean.getPurpose();
	}

	public String getStatus() {
		return bean.getStatus() + "";
	}

	public int getTime() {
		return bean.getMeetingTime();
	}

	public String getType() {
		return bean.getMeetingTimeType() + "";
	}

	public static String getTypeName(String t) {
		char c = t.charAt(0);
		switch (c) {
			case 'P':
				return "Appointment";
			case 'A':
				return "Reminder";
			default:
				return "Unknown";
		}
	}

	public static String getStatusName(String t) {
		char c = t.charAt(0);
		switch (c) {
			case 'C':
				return "Cancelled";
			case 'D':
				return "Done";
			case 'A':
				return "Active";
			default:
				return "Unknown";
		}
	}

	public String getTypeName() {
		return getTypeName(getType());
	}

	public String getStatusName() {
		return getStatusName(getStatus());
	}

	public boolean isPrimary(BPerson bPerson) {
		return ArahantSession.getHSU().createCriteria(AppointmentPersonJoin.class).eq(AppointmentPersonJoin.APPOINTMENT, bean).eq(AppointmentPersonJoin.PERSON, bPerson.person).eq(AppointmentPersonJoin.PRIMARY, 'Y').exists();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(Appointment.class, key);
	}

	public void setAttendees(String attendees) {
		bean.setProspectAttendees(attendees);
	}

	public void setDate(int date) {
		bean.setMeetingDate(date);
	}

	public void setLength(int length) {
		bean.setMeetingLength((short) length);
	}

	public void setLocation(String location) {
		bean.setMeetingLocation(location);
	}

	public void setLocationId(String locationId) {
		bean.setLocation(ArahantSession.getHSU().get(AppointmentLocation.class, locationId));
	}

	public void setOrgGroupId(String orgGroupId) {
		bean.setCompany(BCompanyBase.get(orgGroupId).company_base);
	}

	public void setPersonId(String personId) {
		if (bean.getEmployees().isEmpty()
				|| !bean.getEmployees().iterator().next().getPerson().getPersonId().equals(personId)) {
			deleteAttendeeJoins();
			addAttendee(personId, true);
		}
	}

	public void setPurpose(String purpose) {
		bean.setPurpose(purpose);
	}

	public void setStatus(String status) {
		if (!isEmpty(status))
			bean.setStatus(status.charAt(0));
	}

	public void setTime(int time) {
		bean.setMeetingTime(time);
	}

	public void setType(String type) {
		if (!isEmpty(type))
			bean.setMeetingTimeType(type.charAt(0));
	}

	public String getProspectAttendees() {
		return bean.getProspectAttendees();
	}

	public Set<AppointmentPersonJoin> getEmployeesSet() {
		return bean.getEmployees();
	}

	public static BAppointment[] list(String id, int fromDate, int toDate, String type, String status, boolean attendeeOnly, int max) {
		//order by date then time asc
	/*
		 * String query="select distinct apt from Appointment apt join
		 * apt."+Appointment.COMPANY+" comp " ; String where="where
		 * comp."+CompanyBase.ORGGROUPID+"='"+id+"' and
		 * apt."+Appointment.DATE+">="+fromDate;
		 *
		 * if (toDate>0) where+=" and apt."+Appointment.DATE+"<="+toDate;
		 *
		 * if (!isEmpty(type)) where+=" and
		 * apt."+Appointment.TYPE+"='"+type+"'";
		 *
		 * if (!isEmpty(status)) where+=" and
		 * apt."+Appointment.STATUS+"='"+status+"'";
		 *
		 * String order=" order by
		 * apt."+Appointment.DATE+",apt."+Appointment.TYPE+",apt."+Appointment.TIME;
		 *
		 * if (attendeeOnly) { query+=" left join
		 * apt."+Appointment.PERSON_JOINS+" perj left join
		 * perj."+AppointmentPersonJoin.PERSON+" per "; where+=" and
		 * (apt."+Appointment.TYPE+"='A' or (apt."+Appointment.TYPE+"='P' and
		 * per.personId='"+ArahantSession.getCurrentPerson().getPersonId()+"'))";
		 * }
		 *
		 *
		 * List<Appointment> l =
		 * (List<Appointment>)(List)ArahantSession.getHSU().doQuery(query +
		 * where + order,max);
		 *
		 * return makeArrayEx(l);
		 */
		//  This should work, builds query ok, but hibernate failes when data comes back
		HibernateCriteriaUtil<Appointment> hcu = ArahantSession.getHSU().createCriteria(Appointment.class).setMaxResults(max).orderBy(Appointment.DATE).orderBy(Appointment.TYPE).orderBy(Appointment.TIME);

		hcu.joinTo(Appointment.COMPANY).eq(CompanyBase.ORGGROUPID, id);

		hcu.ge(Appointment.DATE, fromDate);

		if (toDate > 0)
			hcu.le(Appointment.DATE, toDate);

		if (!isEmpty(type))
			hcu.eq(Appointment.TYPE, type.charAt(0));

		if (!isEmpty(status))
			hcu.eq(Appointment.STATUS, status.charAt(0));

		if (attendeeOnly)
			hcu.joinTo(Appointment.PERSON_JOINS).eq(AppointmentPersonJoin.PERSON, ArahantSession.getHSU().getCurrentPerson()); //only apply the join if this is a P type
		/*
		 * HibernateCriterionUtil hcru=hcu.makeCriteria();
		 * HibernateCriterionUtil hcru1=hcu.makeCriteria();
		 * HibernateCriterionUtil hcru2=hcu.makeCriteria();
		 * HibernateCriterionUtil hcru4=hcu.makeCriteria();
		 *
		 * HibernateCriteriaUtil
		 * pjHcu=hcu.leftJoinTo(Appointment.PERSON_JOINS,"PJOINS");
		 * HibernateCriterionUtil hcru3=pjHcu.makeCriteria();
		 * hcru3.eq("PJOINS."+AppointmentPersonJoin.PERSON,ArahantSession.getHSU().getCurrentPerson());
		 *
		 * hcru.or(hcru1.eq(Appointment.TYPE, 'A'),
		 * hcru2.and(hcru4.eq(Appointment.TYPE, 'P'), hcru3));
		 *
		 * hcru.add();
		 *
		 */
		return makeArray(hcu.list());

	}

	public String getLocationId() {
		return bean.getLocation().getLocationId();
	}

	@Override
	public void delete() {
		deleteAttendeeJoins();
		super.delete();
	}

	public static BAppointment[] search(int fromDate, int toDate, String companyId, int type, String personId, String apptType, String status, int cap) {
		HibernateCriteriaUtil<Appointment> hcu = ArahantSession.getHSU().createCriteria(Appointment.class).setMaxResults(cap).orderBy(Appointment.DATE).orderBy(Appointment.TYPE).orderBy(Appointment.TIME);

		if (fromDate > 0 || toDate > 0)
			hcu.dateBetween(Appointment.DATE, fromDate, toDate);

		if (!isEmpty(companyId))
			hcu.joinTo(Appointment.COMPANY).eq(CompanyBase.ORGGROUPID, companyId);
		else
			switch (type) //0=both, 1=prospect, 2=client)
			{
				case 0:
					hcu.joinTo(Appointment.COMPANY).in(CompanyBase.ORGGROUPTYPE, new Integer[]{CLIENT_TYPE, PROSPECT_TYPE});
					break;
				case 1:
					hcu.joinTo(Appointment.COMPANY).eq(CompanyBase.ORGGROUPTYPE, PROSPECT_TYPE);
					break;
				case 2:
					hcu.joinTo(Appointment.COMPANY).eq(CompanyBase.ORGGROUPTYPE, CLIENT_TYPE);
					break;
			}

		if (!isEmpty(apptType))
			hcu.eq(Appointment.TYPE, apptType.charAt(0));

		if (!isEmpty(status))
			hcu.eq(Appointment.STATUS, status.charAt(0));

		if (!isEmpty(personId))
			hcu.joinTo(Appointment.PERSON_JOINS).joinTo(AppointmentPersonJoin.PERSON).eq(Person.PERSONID, personId);

		return makeArray(hcu.list());

	}

	@Override
	public String getEventName() {
		if (bean.getCompany() != null)
			return "Appointment (" + bean.getCompany().getName() + ")";
		return "Appointment";
	}

	@Override
	public Hours[] getHours(int date) {
		Calendar cal = Calendar.getInstance();
		Date dt = DateUtils.getDate(bean.getMeetingDate(), bean.getMeetingTime());
		cal.setTime(dt);
		cal.add(Calendar.MINUTE, bean.getMeetingLength());

		return Hours.construct(bean.getMeetingDate(), bean.getMeetingTime(), bean.getMeetingDate(), DateUtils.getTime(cal), date);
	}

	@Override
	public int getEventType() {
		return EVENT_APPOINTMENT;
	}
}
