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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.services.standard.time.timeOffRequest;

/**
 *
 */
public class ListEventsReturnItemHourSegment {
	//eventDetail needs new fields, eventNumber (number), and totalEvents (number), eventName (string), eventType (number)
    // note that because in this version we are not breaking it down by person (it is already at a single event level), eventNumber would always be 0, totalEvents always 1, eventName would be same as eventName at item level

						
	private int startMinute;
	private int finalMinute;
	private int eventNumber=0;
	private int totalEvents=1;
	private String eventName;
	private int eventType;

	public int getFinalMinute() {
		return finalMinute;
	}

	public void setFinalMinute(int finalMinute) {
		this.finalMinute = finalMinute;
	}

	public int getStartMinute() {
		return startMinute;
	}

	public void setStartMinute(int startMinute) {
		this.startMinute = startMinute;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public int getEventNumber() {
		return eventNumber;
	}

	public void setEventNumber(int eventNumber) {
		this.eventNumber = eventNumber;
	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	public int getTotalEvents() {
		return totalEvents;
	}

	public void setTotalEvents(int totalEvents) {
		this.totalEvents = totalEvents;
	}
	
	
}
