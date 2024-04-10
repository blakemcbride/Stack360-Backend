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

import com.arahant.business.BEvent.Hours;

/**
 *
 */
public class ListEventsReturnItemHour {

	private ListEventsReturnItemHourSegment []eventDetail;
	
	public ListEventsReturnItemHour()
	{
		
	}

	ListEventsReturnItemHour(Hours hour, String eventName, int eventType) {
		if (hour.getStartMinute()!=-1)
		{
			eventDetail=new ListEventsReturnItemHourSegment[1];
			eventDetail[0]=new ListEventsReturnItemHourSegment();
			eventDetail[0].setFinalMinute(hour.getFinalMinute());
			eventDetail[0].setStartMinute(hour.getStartMinute());
			eventDetail[0].setEventName(eventName);
			eventDetail[0].setEventType(eventType);
		}
		else
			eventDetail=new ListEventsReturnItemHourSegment[0];
	}

	public ListEventsReturnItemHourSegment[] getEventDetail() {
		return eventDetail;
	}

	public void setEventDetail(ListEventsReturnItemHourSegment[] segments) {
		this.eventDetail = segments;
	}

	
}
