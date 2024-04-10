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

import com.arahant.exports.CompuPayExport;
import com.arahant.utils.ArahantSession;
import java.util.TimerTask;
import org.apache.log4j.Logger;

//@ThreadScope()
public class ExportToCompuPay extends TimerTask //CompuPayExport implements Runnable 
{
	private static boolean first = false;
	private static final Logger logger = Logger.getLogger(ExportToCompuPay.class);

	@Override
	public void run() {

		//while (true)
		{
			try {
				//	Thread.sleep(1000*60*2);
				logger.info("Running CompuPay Export");
				ArahantSession.openHSU().setCurrentPersonToArahant();
				CompuPayExport cpe = new CompuPayExport();
				cpe.exportDemog();
				cpe.exportDeductions();
				cpe.exportTaxes();
				logger.info("Ran export.");
			} catch (Throwable e) {
				if (!first)
					logger.error(e);
				first = false;
			}
		}
		ArahantSession.clearSession();    //  this closes the HSU session
	/*	try
		{
			logger.debug("Doing CompuPay export.");
			
			ArahantSession.getHSU().setCurrentPersonToArahant();
			CompuPayExport cpe=new CompuPayExport();
			cpe.exportDemog();
			cpe.exportTaxes();
			cpe.exportDeductions();

		}
		catch (final Exception e)
		{
			e.printStackTrace();
			logger.error(e);
		}

		ArahantSession.clearSession();
	 */
	}
/*
	public static void main (String args[])
	{
		new ExportToCompuPay().run();
		//CompuPayExport.main(args);
	}
 * */
}


