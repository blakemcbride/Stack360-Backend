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

import com.arahant.beans.HrBenefitChangeReason;
import com.arahant.beans.LifeEvent;
import com.arahant.beans.Person;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;

public class BLifeEvent extends SimpleBusinessObjectBase<LifeEvent> {

	public BLifeEvent(LifeEvent o) {
		bean = o;
	}

	public BLifeEvent() {
	}

	public BLifeEvent(String id) {
		super(id);
	}

	@Override
	public String create() throws ArahantException {
		bean = new LifeEvent();
		bean.setEventDate(DateUtils.now());
		bean.setReportingPerson(ArahantSession.getHSU().getCurrentPerson());
		return bean.generateId();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(LifeEvent.class, key);
	}

	public void setEmployeeId(String employeeId) {
		bean.setPerson(ArahantSession.getHSU().get(Person.class, employeeId));
	}

	public void setEventDate(int eventDate) {
		bean.setEventDate(eventDate);
	}

	public void setEventId(String eventId) {
		bean.setChangeReason(ArahantSession.getHSU().get(HrBenefitChangeReason.class, eventId));
	}

	public String getLifeEventId() {
		return bean.getLifeEventId();
	}

	public void setLifeEventId(String lifeEventId) {
		bean.setLifeEventId(lifeEventId);
	}

	public String getDescription() {
		return bean.getDescription();
	}

	public void setDescription(String description) {
		bean.setDescription(description);
	}

	public HrBenefitChangeReason getChangeReason() {
		return bean.getChangeReason();
	}

	public static LifeEvent getLifeEvent(Person person, int eventDate, String bcrId) {
		return ArahantSession.getHSU().createCriteria(LifeEvent.class).eq(LifeEvent.PERSON, person).eq(LifeEvent.EVENT_DATE, eventDate).eq(LifeEvent.CHANGE_REASON, new BHRBenefitChangeReason(bcrId).getBean()).first();
	}
}
