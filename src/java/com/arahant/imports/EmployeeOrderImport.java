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
package com.arahant.imports;

import com.arahant.beans.Person;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import org.kissweb.DelimitedFileReader;
import com.arahant.utils.HibernateSessionUtil;
import java.io.File;

/**
 *
 */
public class EmployeeOrderImport {
	private static final ArahantLogger logger=new ArahantLogger(EmployeeOrderImport.class);
	public void updateOrder(String employeeIdColumn, String orderColumn, String filename, String userId) {
		try {
		
			int idCol = Integer.parseInt(employeeIdColumn) - 1;
			int orderCol = Integer.parseInt(orderColumn) - 1;

			HibernateSessionUtil hsu=ArahantSession.getHSU();
		
			hsu.beginTransaction();
			hsu.dontAIIntegrate();
			
			hsu.doSQL("update person set sort_order=0");


			DelimitedFileReader dfr = new DelimitedFileReader(new File(filename));
			dfr.nextLine();

			int count=0;

			while (dfr.nextLine()) {
				
				if (++count%50==0)
				{
					logger.info(count);
					hsu.flush();
					hsu.commitTransaction();
					hsu.beginTransaction();
				}
				String personId = dfr.getString(idCol);
				int order = dfr.getInt(orderCol);


				try
				{
					Person bp = hsu.get(Person.class, personId);
							
					bp.setSortOrder(order);
					hsu.saveOrUpdate(bp);
					bp.removeFromAIEngine();
					
				}
				catch (Exception e)
				{
					logger.info("skipping "+personId);
					continue;
				}
			}
			
			//TODO: send update message
			hsu.commitTransaction();
			
		} catch (Exception e) {
			ArahantSession.getHSU().rollbackTransaction();
			throw new ArahantException(e);
		}
	}
	
	public static void main(String []args)
	{
		ArahantSession.getHSU().setCurrentPersonToArahant();
		EmployeeOrderImport imp=new EmployeeOrderImport();
		imp.updateOrder("9", "8", "/Users/mailingSorted.txt", "");
	}
}
