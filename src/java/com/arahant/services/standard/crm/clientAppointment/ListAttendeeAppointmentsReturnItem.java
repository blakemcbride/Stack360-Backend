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


/**
 * 
 */
package com.arahant.services.standard.crm.clientAppointment;
import com.arahant.beans.Appointment;
import com.arahant.beans.AppointmentPersonJoin;
import com.arahant.beans.Person;
import com.arahant.business.BAppointment;
import com.arahant.business.BPerson;
import com.arahant.services.TransmitReturnItemBase;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;


/**
 * 
 *
 *
 */
public class ListAttendeeAppointmentsReturnItem {
	
	public ListAttendeeAppointmentsReturnItem()
	{
		
	}

	ListAttendeeAppointmentsReturnItem (BPerson bc, int date, String apptId, String primaryEmployeeId, String primaryContactId)
	{
		
		personId=bc.getPersonId();
		firstName=bc.getFirstName();
		lastName=bc.getLastName();
		employee=bc.isEmployee();
		hours=new ListAttendeeAppointmentsReturnItemHour[24];
		
		for (int loop=0;loop<hours.length;loop++)
			hours[loop]=new ListAttendeeAppointmentsReturnItemHour();
		
		if (apptId!=null && !apptId.trim().equals(""))
		{
			//set primary if primary for this appointment
			HibernateCriteriaUtil<AppointmentPersonJoin> hcu=ArahantSession.getHSU()
				.createCriteria(AppointmentPersonJoin.class)
				.eq(AppointmentPersonJoin.PRIMARY, 'Y');
			
			hcu.joinTo(AppointmentPersonJoin.APPOINTMENT)
				.eq(Appointment.APPOINTMENT_ID, apptId);
			
			hcu.joinTo(AppointmentPersonJoin.PERSON)
				.eq(Person.PERSONID, personId);
			
			primary=hcu.exists();
		}
		
		if (employee && !isEmpty(primaryEmployeeId))
			primary=bc.getPersonId().equals(primaryEmployeeId);
		if (!employee && !isEmpty(primaryContactId))
			primary=bc.getPersonId().equals(primaryContactId);
		
		//I need all appointments for this person on this date, except for excludes
		BAppointment[] apts=bc.getActiveAppointments(date);
		
		for (BAppointment apt : apts)
		{
			int time=apt.getTime();
			int hourIdx=DateUtils.getHour(time);
			ListAttendeeAppointmentsReturnItemHour hour=hours[hourIdx];
			
			ListAttendeeAppointmentsReturnItemHourSegment seg=new ListAttendeeAppointmentsReturnItemHourSegment();
			
			int minu=DateUtils.getMinutes(time);
			seg.setStartMinute(minu);
			int remain=apt.getLength();
			if (apt.getLength()+minu<60)
			{
				seg.setFinalMinute(remain+minu);
				remain=0;
			}
			else
			{
				seg.setFinalMinute(59);
				remain-=(60-minu);
			}
			
			seg.setCurrent(apt.getId().equals(apptId));
				
			hour.segments.add(seg);
			
			while (remain>0)
			{
				if (++hourIdx>23)
					break;
				hour=hours[hourIdx];
				seg=new ListAttendeeAppointmentsReturnItemHourSegment();
				seg.setStartMinute(0);
				
				if (remain<60)
				{
					seg.setFinalMinute(remain);
					remain=0;
				}
				else
				{
					seg.setFinalMinute(59);
					remain-=60;
				}
				
				seg.setCurrent(apt.getId().equals(apptId));
				hour.segments.add(seg);
				
			}
			
			
		}
		//for every hour, set it's segments
		for (int loop=0;loop<hours.length;loop++)
			hours[loop].setSegments();
	}
	
	private String personId;
	private String firstName;
	private String lastName;
	private ListAttendeeAppointmentsReturnItemHour [] hours;
	private boolean primary;
	private boolean employee;

	

	public String getPersonId()
	{
		return personId;
	}
	public void setPersonId(String personId)
	{
		this.personId=personId;
	}
	public String getFirstName()
	{
		return firstName;
	}
	public void setFirstName(String firstName)
	{
		this.firstName=firstName;
	}
	public String getLastName()
	{
		return lastName;
	}
	public void setLastName(String lastName)
	{
		this.lastName=lastName;
	}
	public ListAttendeeAppointmentsReturnItemHour [] getHours()
	{
		if (hours==null)
			hours= new ListAttendeeAppointmentsReturnItemHour [0];
		return hours;
	}
	public void setHours(ListAttendeeAppointmentsReturnItemHour [] hours)
	{
		this.hours=hours;
	}

	public boolean getEmployee() {
		return employee;
	}

	public void setEmployee(boolean employee) {
		this.employee = employee;
	}

	public boolean getPrimary() {
		return primary;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	protected boolean isEmpty(String str)
	{
		return str==null || "".equals(str.trim());
	}
}

	
