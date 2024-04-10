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
package com.arahant.services.utils.imports;

import com.arahant.beans.Person;
import com.arahant.beans.ProphetLogin;
import com.arahant.business.BEmployee;
import com.arahant.exceptions.ArahantException;
import com.arahant.imports.glaggs.DRCMBImport;
import com.arahant.utils.ArahantSession;
import org.kissweb.DelimitedFileReader;
import com.arahant.utils.HibernateSessionUtil;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StandardImport {

	public void importFile(String fileName) throws Exception {
		DelimitedFileReader dfr = new DelimitedFileReader(fileName);

		dfr.nextLine();
		//dfr.nextLine();

		while (dfr.nextLine()) {
			HibernateSessionUtil hsu = ArahantSession.getHSU();
			int pos = 0;
			String ssn = dfr.getString(pos++);
			String firstName = dfr.getString(pos++);
			String middleName = dfr.getString(pos++);
			String lastName = dfr.getString(pos++);
			String sex = dfr.getString(pos++);
			int dob = Integer.valueOf(dfr.getString(pos++)).intValue();
			String homePhone = dfr.getString(pos++);
			String mobilePhone = dfr.getString(pos++);
			String street = dfr.getString(pos++);
			String street2 = dfr.getString(pos++);
			String city = dfr.getString(pos++);
			String state = dfr.getString(pos++);
			String zip = dfr.getString(pos++);
			String country = dfr.getString(pos++);
			String jobTitle = dfr.getString(pos++);
			double expHours = Double.valueOf(dfr.getString(pos++)).doubleValue();
			String empId = dfr.getString(pos++);
			if (empId.length() > 11)
				empId = empId.substring(0, 11);

			if (hsu.createCriteria(Person.class).eq(Person.SSN, ssn).exists())
				continue;

			BEmployee be = new BEmployee();
			be.create();
			be.setSsn(ssn);
			be.setFirstName(firstName);
			be.setMiddleName(middleName);
			be.setLastName(lastName);
			be.setSex(sex);
			be.setDob(dob);
			be.setHomePhone(homePhone);
			be.setMobilePhone(mobilePhone);
			be.setStreet(street);
			be.setStreet2(street2);
			be.setCity(city);
			be.setState(state);
			be.setZip(zip);
			be.setCountry(country);
			be.setJobTitle(jobTitle);
			be.setExpectedHoursPerPayPeriod(expHours);
			be.setExtRef(empId);

			String user = firstName.charAt(0) + lastName + ssn.substring(ssn.length() - 2);

			user = user.toLowerCase();

			while (hsu.createCriteria(ProphetLogin.class).eq(ProphetLogin.USERLOGIN, user).exists())
				throw new ArahantException("LOGIN COLLISION!");
			//user=data.getFirstName().charAt(0)+data.getLastName()+(int)(Math.random()*10)+""+(int)(Math.random()*10);

			be.setUserLogin(user);

			String password;

			password = ssn;

			be.setUserPassword(password, true);
			//be.setSecurityGroupId(data.getSecurityGroupId());
			//be.setScreenGroupId(data.getScreenGroupId());

			be.insert();
		}
	}

	public static void main(String args[]) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		try {
			hsu.beginTransaction();
			hsu.setCurrentPersonToArahant();

			StandardImport x = new StandardImport();
			x.importFile("/home/brad/Desktop/test.csv");

			hsu.commitTransaction();

		} catch (Exception ex) {
			hsu.rollbackTransaction();
			Logger.getLogger(DRCMBImport.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
