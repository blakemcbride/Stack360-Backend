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

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BAppointment;
import com.arahant.business.BEmployee;
import com.arahant.business.BPerson;


/**
 * 
 *
 *
 */
public class LoadAppointmentReturn extends TransmitReturnBase {

	void setData(BAppointment bc)
	{
		
		attendees=bc.getAttendees();
		location=bc.getLocation();
		purpose=bc.getPurpose();
		/*
		BPerson [] emps=bc.getEmployees();
		employees=new LoadAppointmentReturnItem[emps.length];
		for (int loop=0;loop<employees.length;loop++)
			employees[loop]=new LoadAppointmentReturnItem(emps[loop], bc.isPrimary(emps[loop]));
		BPerson [] peps=bc.getContacts();
		contacts=new LoadAppointmentReturnItem[peps.length];
		for (int loop=0;loop<contacts.length;loop++)
			contacts[loop]=new LoadAppointmentReturnItem(peps[loop], bc.isPrimary(peps[loop]));
*/
	}
	
	private String attendees;
	private String location;
	private String purpose;
	//private LoadAppointmentReturnItem []employees;
	//private LoadAppointmentReturnItem []contacts;
/*
	public LoadAppointmentReturnItem []getContacts() {
		return contacts;
	}

	public void setContacts(LoadAppointmentReturnItem []contacts) {
		this.contacts = contacts;
	}

	public LoadAppointmentReturnItem []getEmployees() {
		return employees;
	}

	public void setEmployees(LoadAppointmentReturnItem []employees) {
		this.employees = employees;
	}
*/
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
	public String getPurpose()
	{
		return purpose;
	}
	public void setPurpose(String purpose)
	{
		this.purpose=purpose;
	}

}

	
