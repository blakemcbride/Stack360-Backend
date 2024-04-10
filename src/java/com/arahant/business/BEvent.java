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
package com.arahant.business;

/**
 *
 */
public interface BEvent {

	public static final int EVENT_APPROVED_TIME_OFF=0;
	public static final int EVENT_OPEN_TIME_OFF=1;
	public static final int EVENT_APPOINTMENT=2;
	public static final int EVENT_REJECTED_TIME_OFF=3;
	public static final int EVENT_ENTERED_TIME_OFF=4;
	
	public String getEventName();
	public int getEventType();
	
	class Hours {

		static Hours[] construct(int startDate, int startTime, int endDate, int endTime, int date) {
			Hours[] ret = new Hours[24];
			
			if (startTime==-1)
				startTime=0;
			else
				startTime=startTime/100000;
			
			if (endTime==-1)
				endTime=2359;
			else
				endTime=endTime/100000;
			
			for (int loop=0;loop<ret.length;loop++)
				ret[loop]=new Hours();

			//start and ends on day
			if (startDate == date && endDate == date) {
				for (int loop = 0; loop < 24; loop++) {
					int currentHourStart = loop * 100;
					int currentHourEnd = loop * 100 + 59;

					if (startTime > currentHourEnd) //hasn't started yet
					{
						ret[loop].startMinute = -1;
						ret[loop].finalMinute = -1;
						continue;
					}

					if (endTime < currentHourStart) //already ended
					{
						ret[loop].startMinute = -1;
						ret[loop].finalMinute = -1;
						continue;
					}

					//so I intersect somehow
					if (currentHourStart >= startTime && currentHourEnd <= endTime) //totally contained
					{
						ret[loop].startMinute = 0;
						ret[loop].finalMinute = 59;
						continue;
					}

					if (currentHourStart <= startTime && startTime < currentHourEnd) //starts in block
					{

						ret[loop].startMinute = startTime % 100;
						ret[loop].finalMinute = 59;
						
						if (ret[loop].startMinute==ret[loop].finalMinute)
						{
							ret[loop].startMinute = -1;
							ret[loop].finalMinute = -1;
						}
						continue;
					}

					//must end in block
					ret[loop].startMinute = 0;
					ret[loop].finalMinute = endTime % 100;
					
					if (ret[loop].startMinute==ret[loop].finalMinute)
					{
						ret[loop].startMinute = -1;
						ret[loop].finalMinute = -1;
					}
				}
			}


			//starts on day, doesn't end on same day
			if (startDate == date && endDate > date) {
				for (int loop = 0; loop < 24; loop++) {
					int currentHourStart = loop * 100;
					int currentHourEnd = loop * 100 + 59;

					if (startTime > currentHourEnd) //hasn't started yet
					{
						ret[loop].startMinute = -1;
						ret[loop].finalMinute = -1;
						continue;
					}


					//so I intersect somehow
					if (currentHourStart >= startTime && currentHourEnd <= 2400) //totally contained
					{
						ret[loop].startMinute = 0;
						ret[loop].finalMinute = 59;
						continue;
					}

					if (currentHourStart <= startTime && currentHourStart <= 2400) //starts in block
					{

						ret[loop].startMinute = startTime % 100;
						ret[loop].finalMinute = 59;
						if (ret[loop].startMinute==ret[loop].finalMinute)
						{
							ret[loop].startMinute = -1;
							ret[loop].finalMinute = -1;
						}
						continue;
					}

					//must end in block
					ret[loop].startMinute = 0;
					ret[loop].finalMinute = 59;
				}
			}

			//all day
			if (startDate < date && endDate > date) {
				for (int loop = 0; loop < 24; loop++) {
					ret[loop].startMinute = 0;
					ret[loop].finalMinute = 59;
				}

			}

			//ends on day didn't start on
			if (startDate < date && endDate == date) {
				for (int loop = 0; loop < 24; loop++) {
					int currentHourStart = loop * 100;
					int currentHourEnd = loop * 100 + 59;

					if (endTime==-1)
					{
						ret[loop].startMinute = 0;
						ret[loop].finalMinute = 59;
						continue;
					}
					
					if (endTime < currentHourStart) //already ended
					{
						ret[loop].startMinute = -1;
						ret[loop].finalMinute = -1;
						continue;
					}

					//so I intersect somehow
					if (currentHourEnd <= endTime) //totally contained
					{
						ret[loop].startMinute = 0;
						ret[loop].finalMinute = 59;
						continue;
					}


					//must end in block
					ret[loop].startMinute = 0;
					ret[loop].finalMinute = endTime % 100;
					if (ret[loop].startMinute==ret[loop].finalMinute)
					{
						ret[loop].startMinute = -1;
						ret[loop].finalMinute = -1;
					}
				}
			}


			return ret;
		}
		int startMinute;
		int finalMinute;

		public int getFinalMinute() {
			return finalMinute;
		}

		public int getStartMinute() {
			return startMinute;
		}
	}

	public Hours[] getHours(int date);
}
