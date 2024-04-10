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

import com.arahant.beans.Employee;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRAccruedTimeOff;
import com.arahant.business.BHRBenefit;
import com.arahant.business.BHRBenefitChangeReason;
import com.arahant.business.BHRBenefitConfig;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileReader;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.Utils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Arahant
 */

public class AccrualImport {

    HibernateSessionUtil hsu = ArahantSession.getHSU();

    public void importAccruals(String filename, String benefitId, String configId) throws FileNotFoundException, IOException, Exception
	{
		hsu.beginTransaction();
		BHRBenefitChangeReason bcr = BHRBenefitChangeReason.findOrMake("Internal Administration");
		hsu.commitTransaction();

		DelimitedFileReader fr = new DelimitedFileReader(filename);
		//skip header line
		fr.nextLine();

		while (fr.nextLine())
		{
			hsu.beginTransaction();
			String ssn = fr.getString(0);
			double total = fr.getDouble(1);
			String asOfDateStr = fr.getString(2);
			int asOfDate = DateUtils.getDate(asOfDateStr);

			if(ssn.length() != 11 && ssn.length() != 9)
			{
				System.out.println("SSN formatted incorrectly for " + ssn);
				continue;
			}
			if(ssn.length() == 9)
			{
				System.out.println("SSN changed from " + ssn + " to " + ssn.substring(0,3)+"-"+ssn.substring(3,5)+"-"+ssn.substring(5));
				ssn = ssn.substring(0,3)+"-"+ssn.substring(3,5)+"-"+ssn.substring(5);
			}

			Employee e = hsu.createCriteriaNoCompanyFilter(Employee.class).eq(Employee.SSN, ssn).first();
			if(e == null)
			{
				System.out.println("Employee not found for " + ssn);
				continue;
			}
			BEmployee be = new BEmployee(e);
			int hireDate = be.getHireDate();
			//does this employee have this time related benefit?
			if(!be.enrolledInApprovedConfig(configId))
			{
				BHRBenefitJoin bj = new BHRBenefitJoin();
				bj.create();
				bj.setPayingPerson(e);
				bj.setCoveredPerson(e);
				bj.setChangeReason(bcr.getHrBenefitChangeReasonId());
				bj.setPolicyStartDate(hireDate);
				bj.setCoverageStartDate(hireDate);
				bj.setBenefitConfigId(configId);
				bj.insert();
				hsu.flush();
				System.out.println(be.getNameFML() + " enrolled in " + new BHRBenefitConfig(configId).getConfigName());
			}
			double actualTotal = be.getTimeOffByDates(benefitId, hireDate, asOfDate);
			if(!Utils.doubleEqual(total, actualTotal, 0.0001))
			{
				BHRAccruedTimeOff adjustment = new BHRAccruedTimeOff();
				adjustment.create();
				adjustment.setAccrualDate(asOfDate);
				adjustment.setEmployeeId(be.getPersonId());
				adjustment.setAccrualHours(total - actualTotal);
				adjustment.setDescription("Accrual Import Adjustment");
				adjustment.setAccrualAccountId(benefitId);
				adjustment.insert();
				System.out.println((total - actualTotal) + " added on " + asOfDate + " for " + be.getNameFML());
			}
			hsu.commitTransaction();
		}
		hsu.beginTransaction();
	}
}
