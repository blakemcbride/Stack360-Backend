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
package com.arahant.services.standard.time.timeOffRequestReview;

import com.arahant.business.BEvent;
import com.arahant.business.BEvent.Hours;

public class ListEventsReturnItemHourEventDetail {

	private int eventNumber;
	private int totalEvents;
	private String eventName;
	private int eventType;
	private int startMinute;
	private int finalMinute;

	public ListEventsReturnItemHourEventDetail() {
	}

	ListEventsReturnItemHourEventDetail(BEvent req, Hours hours, int number, int total) {
		eventName = req.getEventName();
		eventType = req.getEventType();
		startMinute = hours.getStartMinute();
		finalMinute = hours.getFinalMinute();
		eventNumber = number;
		totalEvents = total;
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

	public int getTotalEvents() {
		return totalEvents;
	}

	public void setTotalEvents(int totalEvents) {
		this.totalEvents = totalEvents;
	}
}
