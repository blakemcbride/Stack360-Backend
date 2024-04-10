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
package com.arahant.services.standard.hr.benefitChangeReason;
import com.arahant.business.BHRBenefitChangeReason;


/**
 * 
 *
 *
 */
public class LoadQualifyingEventsReturnItem {

        public LoadQualifyingEventsReturnItem(){
            
        }

	LoadQualifyingEventsReturnItem (String eventName, short eventId)
	{
		
		this.setEventId(eventId);
                this.setEventName(eventName);

	}
	

	private String eventName;
        private short eventId;


    /**
     * @return the eventName
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * @param eventName the eventName to set
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * @return the eventId
     */
    public short getEventId() {
        return eventId;
    }

    /**
     * @param eventId the eventId to set
     */
    public void setEventId(short eventId) {
        this.eventId = eventId;
    }

}

	
