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

package com.arahant.timertasks;

import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import java.util.TimerTask;


public abstract class TimerTaskBase extends TimerTask {

	private static final ArahantLogger logger = new ArahantLogger(TimerTaskBase.class);
	protected HibernateSessionUtil hsu;

	@Override
	public void run() {

		hsu = ArahantSession.openHSU();

		try {
			try {
				logger.info("Starting " + this.getClass());
			} catch (Throwable t) {
				//I don't care if the logger isn't ready yet
			}
			try {
				hsu.beginTransaction();
				hsu.setCurrentPersonToArahant();

				execute();

				hsu.commitTransaction();
			} catch (final Exception e) {
				hsu.rollbackTransaction();
				logger.error(e);
			}
		} finally {
			ArahantSession.clearSession();
			hsu = null;
			logger.info("Finished " + this.getClass());
		}
	}

	public abstract void execute() throws Exception;
}
