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

import com.arahant.beans.OrgGroup;
import com.arahant.utils.StandardProperty;
import com.arahant.beans.WageType;
import com.arahant.business.BCompany;
import com.arahant.business.BEmployee;
import com.arahant.business.BHREmployeeStatus;
import com.arahant.business.BHrEmergencyContact;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BProperty;
import com.arahant.business.BWageType;
import com.arahant.utils.ArahantSession;
import org.kissweb.DelimitedFileReader;
import com.arahant.utils.HibernateSessionUtil;
import org.kissweb.StringUtils;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Arahant
 */

public class BoxMakerImport {

    HibernateSessionUtil hsu = ArahantSession.getHSU();

    public void importEmployees(String filename) {
        try {
            DelimitedFileReader fr = new DelimitedFileReader(filename);
            //skip header line
            fr.nextLine();

			String statusId = BHREmployeeStatus.findOrMake("Active", true);
			hsu.flush();

			int count = 0;
            while (fr.nextLine()) {
                if (++count % 50 == 0) {
                    System.out.println(count);
                    hsu.commitTransaction();
                    hsu.beginTransaction();
                }
                String ssn = fr.nextString();
                String name = fr.nextString();
                String externalRef = fr.nextString();
                String wageTypeName = fr.nextString().trim();
				String wageStr = fr.nextString().trim();
                double wage = !StringUtils.isEmpty(wageStr)? Double.parseDouble((wageStr.replace("$", "")).replace(",", "")) : 0.00;
                String jobTitle = fr.nextString().trim();
                String department = fr.nextString().trim();
                String gender = fr.nextString();
                int hireDate = convertDateStringToInt(fr.nextString());
                int dob = convertDateStringToInt(fr.nextString());
                String address = fr.nextString();
                String address2 = fr.nextString();
                String city = fr.nextString();
                String state = fr.nextString();
                String zip = fr.nextString();
                String phone = fr.nextString();
				String fname = name.trim().substring(name.indexOf(",")+2);
				String lname = name.trim().substring(0,name.indexOf(","));

				BEmployee be = new BEmployee();
				be.create();
				be.setSsn(ssn); 
				be.setExtRef(externalRef);
				be.setJobTitle(jobTitle);
				be.setFirstName(fname);
				be.setLastName(lname);
				be.setSex(gender);
				be.setDob(dob);
				be.setCompanyId(ArahantSession.getHSU().getCurrentCompany().getCompanyId());

				be.setUserLogin(BEmployee.makeUserLogin(fname, lname.replace(" ", "_")));
				be.setUserPassword(BEmployee.makeUserPassword(), true);
				be.setSecurityGroupId(BProperty.get(StandardProperty.DEFAULT_SEC_GROUP));
				be.setScreenGroupId(BProperty.get(StandardProperty.DEFAULT_SCREEN_GROUP));
				be.setDob(dob);
				be.setStreet(address);
				be.setStreet2(address2);
				be.setCity(city);
				be.setState(state);
				be.setZip(zip);
				be.insert(true);

				hsu.flush();

				short wagePeriod = wageTypeName.contains("Monthly")? WageType.PERIOD_SALARY : WageType.PERIOD_HOURLY;
				

				String wageTypeId = BWageType.findOrMakeType(wageTypeName, wagePeriod, WageType.TYPE_REGULAR);
				be.setWageAndPosition(null, wageTypeId, wage, hireDate);
				be.setStatusId(statusId, hireDate);

				OrgGroup og = hsu.createCriteria(OrgGroup.class).eq(OrgGroup.ORGGROUPTYPE, BOrgGroup.COMPANY_TYPE).eq(OrgGroup.OWNINGCOMPANY, hsu.getCurrentCompany()).eq(OrgGroup.NAME, department).first();
				BOrgGroup bog = new BOrgGroup();
				if(og == null)
				{
					bog.create();
					bog.setCompanyId(hsu.getCurrentCompany().getCompanyId());
					bog.setName(department);
					bog.setOrgGroupType(BOrgGroup.COMPANY_TYPE);
					bog.insert();
					hsu.flush();
					bog.setParent(hsu.getCurrentCompany().getCompanyId());
					bog.update();
				}
				else
				{
					bog = new BOrgGroup(og);
				}

				String[] personIds = new String[1];
				personIds[0] = be.getPersonId();
				bog.assignPeopleToGroup(personIds);

				BHrEmergencyContact bec = new BHrEmergencyContact();
				bec.create();
				bec.setHomePhone(phone);
				bec.setName(name);
				bec.setPerson(be.getPerson());
				bec.setRelationship("self");
				bec.setSeqno(0);
				bec.insert();

			}
        } catch (IOException ex) {
            Logger.getLogger(BoxMakerImport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(BoxMakerImport.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

	private int convertDateStringToInt(String dateString)
	{
		int ret = 0;
		int year = Integer.parseInt(dateString.substring(dateString.length()-2));
		String monthStr = dateString.substring(dateString.indexOf("-")+1,dateString.indexOf("-")+4);
		int month = 0;
		if(monthStr.equals("Jan"))
		{
			month = 1;
		}
		else if(monthStr.equals("Feb"))
		{
			month = 2;
		}
		else if(monthStr.equals("Mar"))
		{
			month = 3;
		}
		else if(monthStr.equals("Apr"))
		{
			month = 4;
		}
		else if(monthStr.equals("May"))
		{
			month = 5;
		}
		else if(monthStr.equals("Jun"))
		{
			month = 6;
		}
		else if(monthStr.equals("Jul"))
		{
			month = 7;
		}
		else if(monthStr.equals("Aug"))
		{
			month = 8;
		}
		else if(monthStr.equals("Sep"))
		{
			month = 9;
		}
		else if(monthStr.equals("Oct"))
		{
			month = 10;
		}
		else if(monthStr.equals("Nov"))
		{
			month = 11;
		}
		else if(monthStr.equals("Dec"))
		{
			month = 12;
		}

		int day = Integer.parseInt(dateString.substring(0, dateString.indexOf("-")));

		if(year <= 10)
		{
			year = 2000 + year;
		}
		else
		{
			year = 1900 + year;
		}

		ret = (year * 10000) + (month * 100) + (day);

		return ret;
	}

   

    public static void main(String args[]) {
        BoxMakerImport imp = new BoxMakerImport();
        try {

            imp.hsu = ArahantSession.getHSU();
            imp.hsu.dontAIIntegrate();
            imp.hsu.setCurrentPersonToArahant();

			imp.hsu.setCurrentCompany(new BCompany("00001-0000072679").getBean());
			
			imp.importEmployees("/Users/arahant/Desktop/EElist.csv");

            imp.hsu.commitTransaction();
        } catch (Exception ex) {
            imp.hsu.rollbackTransaction();
            ex.printStackTrace();
            Logger.getLogger(ProspectImport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
