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


package com.arahant.exports;

import com.arahant.beans.*;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.utils.ArahantSession;
import org.kissweb.DelimitedFileWriter;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.HibernateSessionUtil;
import java.io.File;
import java.util.*;

public class ExportWizardPayroll {

	public String build(int asOfDate) throws Exception {
		File csvFile = FileSystemUtils.createTempFile("WizardPayroll", ".csv");

		HibernateSessionUtil hsu = ArahantSession.getHSU();

		DelimitedFileWriter dfw = new DelimitedFileWriter(csvFile.getAbsolutePath(), false);

		HashSet<HrBenefit> beneSet = new HashSet<HrBenefit>();

		//find all wizard benefits
		beneSet.addAll(hsu.createCriteria(HrBenefit.class).eq(HrBenefit.OPEN_ENROLLMENT_WIZARD, 'Y').list());

		beneSet.addAll(hsu.createCriteria(HrBenefit.class).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.OPEN_ENROLLMENT_WIZARD, 'Y').list());

		List<HrBenefit> benes = new ArrayList<HrBenefit>(beneSet.size());
		benes.addAll(beneSet);

		Collections.sort(benes, new Comparator<HrBenefit>() {

			@Override
			public int compare(HrBenefit o1, HrBenefit o2) {
				int x = o1.getHrBenefitCategory().getSequence() - o2.getHrBenefitCategory().getSequence();
				if (x != 0)
					return x;

				return o1.getSequence() - o2.getSequence();
			}
		});

		//write header
		dfw.writeField("Last Name");
		dfw.writeField("First Name");

		for (HrBenefit b : benes) {
			dfw.writeField(b.getName());
			dfw.writeField(b.getName() + " Cost");
		}

		dfw.endRecord();

		for (Employee emp : hsu.createCriteria(Employee.class).orderBy(Employee.LNAME).orderBy(Employee.FNAME).list()) {
			dfw.writeField(emp.getLname());
			dfw.writeField(emp.getFname());

			for (HrBenefit b : benes) {
				HrBenefitJoin bj = hsu.createCriteria(HrBenefitJoin.class).dateInside(HrBenefitJoin.POLICY_START_DATE, HrBenefitJoin.POLICY_END_DATE, asOfDate).eq(HrBenefitJoin.APPROVED, 'Y').eq(HrBenefitJoin.PAYING_PERSON, emp).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, b).first();

				if (bj != null) {
					dfw.writeField(bj.getHrBenefitConfig().getName());
					if (bj.getHrBenefitConfigId().equals("00001-0000000045"))
						System.out.println("Here");
					dfw.writeField(new BHRBenefitJoin(bj).getAmountPaid());
				} else {
					dfw.writeField("");
					dfw.writeField(0);
				}
			}

			dfw.endRecord();
		}

		dfw.close();
		return FileSystemUtils.getHTTPPath(csvFile);
	}

	public static void main(String args[]) {
		try {
			ArahantSession.getHSU().beginTransaction();
			ArahantSession.getHSU().setCurrentPersonToArahant();

			//new ExportWizardPayroll().build("/Users/ztemp/test.csv", 20100602);


			ArahantSession.getHSU().rollbackTransaction();//should not have made changes
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
