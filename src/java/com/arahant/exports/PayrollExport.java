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
 */
package com.arahant.exports;

import com.arahant.beans.*;
import com.arahant.business.BPerson;
import com.arahant.business.BProject;
import com.arahant.utils.*;
import org.kissweb.DelimitedFileWriter;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class PayrollExport {

	private HibernateSessionUtil hsu = ArahantSession.getHSU();

	public String build(int start, int end) throws Exception {
		File csvFile = FileSystemUtils.createTempFile("Payroll", ".csv");
		DelimitedFileWriter writer = new DelimitedFileWriter(csvFile.getAbsolutePath(), false);

		try {
			writer.writeField("Name");
			writer.writeField("SSN");
			writer.writeField("Regular");


			HibernateCriteriaUtil<Timesheet> hcu = hsu.createCriteria(Timesheet.class).dateBetween(Timesheet.WORKDATE, start, end).in(Timesheet.STATE, new char[]{ArahantConstants.TIMESHEET_APPROVED, ArahantConstants.TIMESHEET_INVOICED}).joinTo(Timesheet.PERSON).orderBy(Person.LNAME).orderBy(Person.FNAME).orderBy(Person.MNAME);

			HibernateScrollUtil<Timesheet> scr = hcu.scroll();

			int count = 0;

			List<HrBenefit> benefits = hsu.createCriteria(HrBenefit.class).list();

			List<Project> p = hsu.createCriteria(Project.class).joinTo(Project.HRBENEFITPROJECTJOINS).joinTo(HrBenefitProjectJoin.HR_BENEFIT_CONFIG).in(HrBenefitConfig.HR_BENEFIT, benefits).orderBy(HrBenefit.NAME).list();

			for (Project proj : p)
				writer.writeField(new BProject(proj).getBenefitName());

			writer.writeField("Total");
			writer.endRecord();

			String ssn = "";
			String name = "";
			double total = 0;
			double standards = 0;

			HashMap<String, Double> projectCounts = new HashMap<String, Double>();

			while (scr.next()) {
				count++;

				Timesheet ts = scr.get();

				if (!ssn.equals(ts.getPerson().getUnencryptedSsn())) {
					if (!isEmpty(name)) {
						writer.writeField(name);
						writer.writeField(ssn);
						writer.writeField(Formatting.formatNumber(standards, 2));

						for (Project pr : p) {
							String nam = new BProject(pr).getBenefitName();
							if (projectCounts.containsKey(nam))
								writer.writeField(Formatting.formatNumber(projectCounts.get(nam).doubleValue(), 2));
							else
								writer.writeField(Formatting.formatNumber(0.0, 2));

						}
						writer.writeField(Formatting.formatNumber(total, 2));
						writer.endRecord();
					}
					total = 0;
					ssn = scr.get().getPerson().getUnencryptedSsn();
					name = scr.get().getPerson().getNameLFM();
					standards = 0;
					projectCounts.clear();
				}


				String beneName = new BProject(ts.getProjectShift().getProject()).getBenefitName();

				if (!isEmpty(beneName)) {

					Double pt = projectCounts.get(beneName);
					if (pt == null)
						pt = 0.0;
					projectCounts.put(beneName, pt + ts.getTotalHours());
				} else
					standards += ts.getTotalHours();

				total += ts.getTotalHours();

			}

			writer.writeField(name);
			writer.writeField(ssn);
			writer.writeField(Formatting.formatNumber(standards, 2));

			for (Project pr : p) {
				String nam = new BProject(pr).getBenefitName();
				if (projectCounts.containsKey(nam))
					writer.writeField(Formatting.formatNumber(projectCounts.get(nam), 2));
				else
					writer.writeField(Formatting.formatNumber(0.0, 2));

			}
			writer.writeField(Formatting.formatNumber(total, 2));

			scr.close();
			writer.endRecord();

		} finally {
			writer.close();
		}
		return FileSystemUtils.getHTTPPath(csvFile);
	}

	protected boolean isEmpty(final String s) {
		return (s == null || s.trim().equals(""));
	}

	public static void main(String args[]) {
		try {
			ArahantSession.getHSU().setCurrentPerson(new BPerson("00001-0000000126").getPerson());
			//ArahantSession.getHSU().setCurrentPersonToArahant();
			//ArahantSession.multipleCompanySupport = true;
			PayrollExport pe = new PayrollExport();
			pe.build(20090101, 20100101);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
