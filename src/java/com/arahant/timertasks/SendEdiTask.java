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
import com.arahant.business.BVendorCompany;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import org.kissweb.StringUtils;
import java.util.Calendar;
import java.util.TimerTask;

public class SendEdiTask extends TimerTask {

	private static final ArahantLogger logger = new ArahantLogger(SendEdiTask.class);
	private String vendorId;
	private short interfaceId;

	public SendEdiTask(short interfaceId, String vendorId) {
		this.interfaceId = interfaceId;
		this.vendorId = vendorId;
	}

	@Override
	public void run() {

		logger.info("Running EDI transmission");

		//String vendorId = ArahantSession.openHSU().createCriteria(VendorCompany.class).selectFields(VendorCompany.ORGGROUPID).eq(VendorCompany.INTERFACE_ID, interfaceId).stringVal();

		if (!StringUtils.isEmpty(vendorId)) {
			HibernateSessionUtil hsu = null;
			try {
				hsu = ArahantSession.openHSU();
				BVendorCompany vendor = new BVendorCompany(vendorId);
				if (vendor.getDaysToSend().charAt(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1) == 'Y') {
					System.out.println("EDI for " + vendor.getName() + " starting.");
					hsu.beginTransaction();
					hsu.setCurrentPersonToArahant();

					final BEDITransaction x = new BEDITransaction();

					//  Create EDITransaction bean and set to 6 (Start)
					x.create();

					x.setReceiver(vendorId);

					x.sendExport();
				} else
					System.out.println("EDI for " + vendor.getName() + " not active for this day of week.");

			} catch (final Exception e) {
				if (hsu != null)
					hsu.rollbackTransaction();
				logger.error(e);
			} finally {
				if (hsu != null)
					ArahantSession.clearSession();  //  this closes the HSU session
			}
			logger.info("Completed EDI transmission");
		}
	}

	public static void main(String args[]) {
//		ArahantSession.multipleCompanySupport = true;
//		SendEdiTask ed=new SendEdiTask(VendorCompany.BCN);
//		ed.run();
	}
}
