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

import com.arahant.business.BEDITransaction;
import com.arahant.business.BProperty;
import com.arahant.exports.AuditMedicalBenefits;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import java.util.Calendar;
import java.util.TimerTask;
import org.apache.log4j.Logger;

public class SendWmCoEDI extends TimerTask {

	private static final Logger logger = Logger.getLogger(SendWmCoEDI.class);

	/*
	 * (non-Javadoc) @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {

		HibernateSessionUtil hsu = ArahantSession.openHSU();

		if (!BProperty.getBoolean("EDIProduction")) {
			logger.info("Not doing EDI run because production flag is FALSE.");
			ArahantSession.clearSession();    //  this closes the HSU session
			return;
		}

		logger.info("Running EDI transmission");

		//Never send a feed on the weekend.
		Calendar cal = Calendar.getInstance();
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			ArahantSession.clearSession();    //  this closes the HSU session
			return;
		}

		//sends every weekday
		String ddi = BProperty.get("ConsociatesId");
		if (ddi != null && !"".equals(ddi))
			try {
				logger.info("Doing Consociates EDI transmission");
				hsu.beginTransaction();
				hsu.setCurrentPersonToArahant();

				final BEDITransaction x = new BEDITransaction();

				x.create();

				x.setReceiver(BProperty.get("ConsociatesId"));

				//	x.insert();

				hsu.commitTransaction();

				hsu.beginTransaction();

				x.sendExport();

				hsu.commitTransaction();

				hsu.beginTransaction();

				new AuditMedicalBenefits().doAuditHealthCurrent();
			} catch (final Exception e) {
				hsu.rollbackTransaction();
				logger.error(e);
			}

		ddi = BProperty.get("AmeritasID");
		//sends only on Thursdays
		if (ddi != null && !"".equals(ddi) && cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY)
			try {
				logger.info("Doing Ameritas EDI transmission");
				hsu.beginTransaction();
				hsu.setCurrentPersonToArahant();

				final BEDITransaction x = new BEDITransaction();

				x.create();

				x.setReceiver(BProperty.get("AmeritasID"));

				//x.insert();

				hsu.commitTransaction();

				hsu.beginTransaction();

				x.sendExport();

			} catch (final Exception e) {
				hsu.rollbackTransaction();
				logger.error(e);
			}


		//if today isn't tuesday or thursday, return
		//Calendar cal=Calendar.getInstance();
//		if (cal.get(Calendar.DAY_OF_WEEK)!=Calendar.TUESDAY && cal.get(Calendar.DAY_OF_WEEK)!=Calendar.THURSDAY) {
//			ArahantSession.clearSession();    //  this closes the HSU session
//			return;
//		}

		ddi = BProperty.get("DeltaDentalId");
		//sends only on tues/thurs
		if (ddi != null && !"".equals(ddi) && (cal.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY))
			try {
				logger.info("Doing Delta Dental EDI transmission");
				hsu.beginTransaction();
				hsu.setCurrentPersonToArahant();

				final BEDITransaction x = new BEDITransaction();

				x.create();

				x.setReceiver(BProperty.get("DeltaDentalId"));

				//x.insert();

				hsu.commitTransaction();

				hsu.beginTransaction();

				x.sendExport();

			} catch (final Exception e) {
				hsu.rollbackTransaction();
				logger.error(e);
			}

		ArahantSession.clearSession();  //  this closes the HSU session

		logger.info("Completed EDI transmission");
	}

	public static void main(String args[]) {
		SendWmCoEDI ed = new SendWmCoEDI();
		ed.run();
	}
}
