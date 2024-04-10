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

import com.arahant.beans.OnboardingTask;
import com.arahant.beans.OnboardingTaskComplete;
import com.arahant.beans.Person;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantJessException;
import com.arahant.exceptions.ArahantSecurityException;
import com.arahant.utils.ArahantSession;

public class BOnboardingTaskComplete extends SimpleBusinessObjectBase<OnboardingTaskComplete> {

	public BOnboardingTaskComplete() {
	}

	public BOnboardingTaskComplete(final OnboardingTaskComplete o) {
		bean = o;
	}

	public BOnboardingTaskComplete(final String key) throws ArahantException {
		internalLoad(key);
	}

	public String getTaskCompleteId() {
		return bean.getTaskCompleteId();
	}

	public OnboardingTask getOnboardingTask() {
		return bean.getOnboardingTask();
	}

	public void setOnboardingTask(OnboardingTask t) {
		bean.setOnboardingTask(t);
	}

	public Person getPerson() {
		return bean.getPerson();
	}

	public void setPerson(Person p) {
		bean.setPerson(p);
	}

	public int getCompletionDate() {
		return bean.getCompletionDate();
	}

	public void setCompletionDate(int d) {
		bean.setCompletionDate(d);
	}

	@Override
	public String create() throws ArahantException {
		bean = new OnboardingTaskComplete();
		bean.generateId();
		return getTaskCompleteId();
	}

	private void internalLoad(String key) throws ArahantException {
		logger.debug("Loading " + key);
		bean = ArahantSession.getHSU().get(OnboardingTaskComplete.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(bean);
	}

	@Override
	public void delete() throws ArahantDeleteException, ArahantSecurityException, ArahantJessException {
		try {
			ArahantSession.getHSU().delete(bean);
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException("Unable to delete current Onboarding Task Complete.");
		}
	}

	public static void delete(final String[] taskCompleteIds) throws ArahantException {
		for (final String element : taskCompleteIds)
			new BOnboardingTaskComplete(element).delete();
	}
}
