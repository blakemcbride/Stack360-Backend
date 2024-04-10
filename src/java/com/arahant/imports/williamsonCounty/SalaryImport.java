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
 *
 */
package com.arahant.imports.williamsonCounty;

import com.arahant.beans.Employee;
import com.arahant.business.BEmployee;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileReader;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.MoneyUtils;
import java.text.DecimalFormat;

public class SalaryImport {

	public static void doImport(String filePath) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		try {
			hsu.beginTransaction();
			hsu.setCurrentPersonToArahant();

			DelimitedFileReader dfr = new DelimitedFileReader(filePath);

			dfr.skipLine();  //Unwanted data
			dfr.nextLine();  //Column Headers

			int count = 0;

			while (dfr.nextLine())
			{
				if (++count % 50 == 0) {
					System.out.println(count);
				}

				String ref = dfr.getString(0);
				//System.out.print("\'" + ref + "\'" + ", ");

				double sal = roundTwoDecimals(MoneyUtils.parseMoney(dfr.getString(4)));
				

				Employee emp = hsu.createCriteria(Employee.class).eq(Employee.SSN, ref).first();

				if (emp == null)
				{
					System.out.println("Missing ref " + ref + " " + dfr.getString(1));
					continue;
				}

				BEmployee bemp = new BEmployee(emp);

				bemp.setWageAndPosition(bemp.getPositionId(), "00001-0000000001", sal, DateUtils.now());

				bemp.update();
				System.out.println("Updated " + ref + " " + dfr.getString(1) + " " + dfr.getString(2) + " to " + sal);
			}

			//hsu.commitTransaction();
		} catch (Exception e) {
			hsu.rollbackTransaction();
			e.printStackTrace();
		}
	}

	public static double roundTwoDecimals(double d)
	{
        DecimalFormat twoDecimal = new DecimalFormat("#.##");
		return Double.valueOf(twoDecimal.format(d));
	}
}
