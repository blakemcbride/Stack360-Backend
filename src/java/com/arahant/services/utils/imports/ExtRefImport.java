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
package com.arahant.services.utils.imports;

import com.arahant.beans.Employee;
import com.arahant.utils.ArahantSession;
import org.kissweb.DelimitedFileReader;
import com.arahant.utils.HibernateSessionUtil;
import java.util.HashMap;

/**
 *
 * Arahant
 */
public class ExtRefImport {

	static HashMap<String, String> countryCodes = new HashMap<String, String>();
	private final static int EMPLOYEE_EXT_REF_COL = 0;
	private final static int EMPLOYEE_SSN_COL = 1;

	public static final String getString(DelimitedFileReader fr, int col) {
		if (col == -1) {
			return "";
		}
		return fr.getString(col);
	}

	public static String max(String s, int m) {
		if (s.length() <= m) {
			return s;
		}
		return s.substring(0, m);
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {

		//this next line will also start the first transaction automatically.
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		//Expert System is excluded, AI engine is OFF, normally gives us something similar to db triggers
		//Uses "RETI" algorithm developed by NASA
		hsu.dontAIIntegrate();

		//This is needed to give root-like access that will bypass program-level access restrictions to db.
		hsu.setCurrentPersonToArahant();
		try {
			DelimitedFileReader fr = new DelimitedFileReader("/Users/Arahant/WmCo/ssnandppl.txt");

			for (int loop = 0; loop < 1; loop++) //skip first line since file contains column headings.
			{
				fr.skipLine();
			}

			int count = 0;
			while (fr.nextLine()) {

				if (++count % 50 == 0) {
					//print progress on screen (after 50 rows)
					System.out.println(count);
					//commit work for prior transaction
					hsu.commitTransaction();
					//here is a good place to designate garbage collection
					//begin work for next
					hsu.beginTransaction();
				}

				// Lets skip the record if the SSN is not included
				if (getString(fr, EMPLOYEE_SSN_COL) == null || getString(fr, EMPLOYEE_SSN_COL).trim().equals("")) {
					continue;
				}

				// Lets skip the record if the External Reference is not included
				if (getString(fr, EMPLOYEE_EXT_REF_COL) == null || getString(fr, EMPLOYEE_EXT_REF_COL).trim().equals("")) {
					continue;
				}

				//does this employee exist?

				//first get data from current file record
				String employeeSSN = getString(fr, EMPLOYEE_SSN_COL);
				String employeeExtRef = getString(fr, EMPLOYEE_EXT_REF_COL);

				Employee emp = hsu.createCriteria(Employee.class).eq(Employee.SSN, employeeSSN).first();

				if (emp != null) {       //employee exists - Lets update it

					//update employee record
					emp.setExtRef(employeeExtRef);
					hsu.saveOrUpdate(emp);
					System.out.println("Found one");

				} else {
					System.out.println("Missing SSN*******" + employeeSSN);
				//later need to add this error to log
				}
			}
			hsu.commitTransaction();
		} catch (Exception ex) {
			hsu.rollbackTransaction();
			ex.printStackTrace();
		// need this later - Logger.getLogger(ProspectImport.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
