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
package com.arahant.services.standard.crm.appointment;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BAppointment;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewAppointmentInput extends TransmitInputBase {

	void setData(BAppointment bc)
	{
		
		bc.setOrgGroupId(orgGroupId);
		bc.setDate(date);
		bc.setTime(time);
		bc.setLength(length);
		bc.setType("P");
		bc.setAttendees(attendees);
		bc.setLocation(location);
		bc.setStatus(status);
		bc.setPurpose(purpose);
		bc.setLocationId(locationId);
		bc.deleteAttendeeJoins();
		
		for (int loop=0;loop<getEmployees().length;loop++)
			bc.addAttendee(employees[loop].getId(),employees[loop].getPrimary());
		for (int loop=0;loop<getContacts().length;loop++)
			bc.addAttendee(contacts[loop].getId(), contacts[loop].getPrimary());
	}
	
	@Validation (required=true)
	private String orgGroupId;
	@Validation (type="date",required=true)
	private int date;
	@Validation (type="time",required=true)
	private int time;
	@Validation (required=false)
	private int length;
	@Validation (table="appointment",column="attendees",required=false)
	private String attendees;
	@Validation (table="appointment",column="meeting_location",required=false)
	private String location;
	@Validation (table="appointment",column="status",required=true)
	private String status;
	@Validation (table="appointment",column="purpose",required=true)
	private String purpose;
	@Validation (required=true)
	private String locationId;
	@Validation (required=false)
	private NewAppointmentInputItem []employees;
	@Validation (required=false)
	private NewAppointmentInputItem []contacts;

	public NewAppointmentInputItem[] getContacts() {
		if (contacts==null)
			contacts=new NewAppointmentInputItem[0];
		return contacts;
	}

	public void setContacts(NewAppointmentInputItem[] contacts) {
		this.contacts = contacts;
	}

	public NewAppointmentInputItem[] getEmployees() {
		if (employees==null)
			employees=new NewAppointmentInputItem[0];
		return employees;
	}

	public void setEmployees(NewAppointmentInputItem[] employees) {
		this.employees = employees;
	}
	
	public String getOrgGroupId()
	{
		return orgGroupId;
	}
	public void setOrgGroupId(String orgGroupId)
	{
		this.orgGroupId=orgGroupId;
	}
	public int getDate()
	{
		return date;
	}
	public void setDate(int date)
	{
		this.date=date;
	}
	public int getTime()
	{
		return time;
	}
	public void setTime(int time)
	{
		this.time=time;
	}
	public int getLength()
	{
		return length;
	}
	public void setLength(int length)
	{
		this.length=length;
	}
	public String getAttendees()
	{
		return attendees;
	}
	public void setAttendees(String attendees)
	{
		this.attendees=attendees;
	}
	public String getLocation()
	{
		return location;
	}
	public void setLocation(String location)
	{
		this.location=location;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status=status;
	}
	public String getPurpose()
	{
		return purpose;
	}
	public void setPurpose(String purpose)
	{
		this.purpose=purpose;
	}
	public String getLocationId()
	{
		return locationId;
	}
	public void setLocationId(String locationId)
	{
		this.locationId=locationId;
	}

}

	
