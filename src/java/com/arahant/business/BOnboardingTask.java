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

import com.arahant.beans.OnboardingConfig;
import com.arahant.beans.OnboardingTask;
import com.arahant.beans.Screen;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantJessException;
import com.arahant.exceptions.ArahantSecurityException;
import com.arahant.services.standard.misc.onboardingTasks.OnboardingScreen;
import com.arahant.utils.ArahantSession;
import java.util.List;

public class BOnboardingTask extends SimpleBusinessObjectBase<OnboardingTask> {

	public BOnboardingTask() {
	}

	public BOnboardingTask(final OnboardingTask o) {
		bean = o;
	}

	public BOnboardingTask(final String key) throws ArahantException {
		internalLoad(key);
	}

	public String getOnboardingTaskId() {
		return bean.getOnboardingTaskId();
	}

	public String getOnboardingTaskName() {
		return bean.getTaskName();
	}

	public void setOnboardingTaskName(String name) {
		bean.setTaskName(name);
	}

	public OnboardingConfig getOnboardingConfig() {
		return bean.getOnboardingConfig();
	}

	public void setOnboardingConfig(OnboardingConfig o) {
		bean.setOnboardingConfig(o);
	}

	public Screen getScreen() {
		return bean.getScreen();
	}

	public void setScreen(Screen s) {
		bean.setScreen(s);
	}

	public int getCompletedByDays() {
		return bean.getCompleteByDays();
	}

	public void setCompletedByDays(int d) {
		bean.setCompleteByDays(d);
	}

	public int getSeqno() {
		return bean.getSeqno();
	}

	public void setSeqno(int s) {
		bean.setSeqno(s);
	}

	public String getDescription() {
		return bean.getDescription();
	}

	public void setDescription(String s) {
		bean.setDescription(s);
	}

	@Override
	public String create() throws ArahantException {
		bean = new OnboardingTask();
		bean.generateId();
		return getOnboardingTaskId();
	}

	private void internalLoad(String key) throws ArahantException {
		logger.debug("Loading " + key);
		bean = ArahantSession.getHSU().get(OnboardingTask.class, key);
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
			throw new ArahantDeleteException("Unable to delete current Onboarding Task.");
		}
	}

	public static void delete(final String[] onboardingTaskIds) throws ArahantException {
		for (final String element : onboardingTaskIds)
			new BOnboardingTask(element).delete();
	}

	public static List<OnboardingTask> list(String configId, int cap) {
		return ArahantSession.getHSU().createCriteria(OnboardingTask.class).orderBy(OnboardingTask.SEQ_NO).eq(OnboardingTask.ONBOARDING_CONFIG, new BOnboardingConfig(configId).getBean()).list();
	}

	public static BOnboardingTask[] makeArray(List<OnboardingTask> l) {
		BOnboardingTask[] ret = new BOnboardingTask[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BOnboardingTask(l.get(loop));
		return ret;
	}

	public static OnboardingScreen[] listOnboardingScreens() {
		OnboardingScreen[] oss = new OnboardingScreen[1];
		OnboardingScreen os;

		os = new OnboardingScreen();
		os.setScreenId(BScreen.getByFilename("com/arahant/app/screen/standard/misc/message/MessageScreen.swf").getId());
		os.setScreenName(BScreen.getByFilename("com/arahant/app/screen/standard/misc/message/MessageScreen.swf").getName());
		oss[0] = os;

		return oss;
	}
}
