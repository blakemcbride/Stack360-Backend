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

import com.arahant.beans.Employee;
import com.arahant.beans.HrBenefit;
import com.arahant.business.BEmployee;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BPerson;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import org.kissweb.DelimitedFileWriter;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import java.io.File;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

// 8/2/2011
public class AccruedTimeOffByGroupExport {

	private DecimalFormat df = new DecimalFormat("#0.00");
	private String fileName = "AccruedTimeOffByGroupExport";

	public String build(String orgGroupId, boolean includeSubGroups, String benefitId, String sortBy) {
		if (sortBy == null || (!sortBy.equals("E") && !sortBy.equals("B")))
			throw new ArahantException(" Please select how to sort the report: 'E' for Employees or 'B' for Benefits. ");
		else if (sortBy.equals("E"))
			fileName = BuildByEmployee(orgGroupId, includeSubGroups, benefitId);
		else if (sortBy.equals("B"))
			fileName = BuildByBenefit(orgGroupId, includeSubGroups, benefitId);

		return FileSystemUtils.getHTTPPath(fileName);
	}

	private String BuildByBenefit(String orgGroupId, boolean includeSubGroups, String benefitId) {

		try {
			File csvFile = FileSystemUtils.createTempFile(fileName, ".csv");
			DelimitedFileWriter writer = new DelimitedFileWriter(csvFile.getAbsolutePath(), false);

			fileName = csvFile.getName();

			boolean restrictSearchToSingleBenefit = false;

			if (benefitId != null && !benefitId.equals("") && !benefitId.equals(" "))
				restrictSearchToSingleBenefit = true;

			if (orgGroupId == null || orgGroupId.equals("0"))
				throw new ArahantException(" Please include an Organization Group ID. ");

			try {

				BOrgGroup borgy = new BOrgGroup(orgGroupId);

				Set<String> set = new HashSet<String>();
				Iterator<String> ity;

				if (includeSubGroups)
					set = borgy.getAllPersonIdsForOrgGroupHierarchy(true);
				else {
					BPerson[] bpers = borgy.listPeople(0);

					for (int loop = 0; loop < bpers.length; loop++)
						set.add(bpers[loop].getPersonId());
				}

				ity = set.iterator();

				List<Employee> empList = new ArrayList<Employee>();

				while (ity.hasNext())
					empList.add(new BEmployee(ity.next()).getEmployee());

				Collections.sort(empList);

				writer.writeField(" ");

				for (Employee emp : empList) {
					BEmployee bemp = new BEmployee(emp);

					if (bemp.getTimeOffTypes().length > 0)
						writer.writeField(bemp.getNameLFM());
				}

				writer.endRecord();

				HibernateCriteriaUtil<HrBenefit> beneQuery = ArahantSession.getHSU().createCriteria(HrBenefit.class);
				beneQuery.eq(HrBenefit.TIMERELATED, 'Y');

				if (restrictSearchToSingleBenefit)
					beneQuery.eq(HrBenefit.BENEFITID, benefitId);
				List<HrBenefit> beneList = beneQuery.list();

				for (HrBenefit benefit : beneList) {

					writer.writeField(benefit.getName());

					for (Employee emp : empList)
						try {
							BEmployee bemp = new BEmployee(emp);
							System.out.println(bemp.getNameLFM());

							if (bemp.getTimeOffTypes().length > 0) {
								Double hoursLeft = bemp.getHoursLeftOnBenefit(benefit.getName());
								writer.writeField(df.format(hoursLeft));
							}
						} catch (Exception e) {
							writer.writeField("");
							System.out.println(e + emp.getNameLFM());
						}
					writer.endRecord();
				}

				writer.endRecord();
				writer.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return fileName;

	}

	private String BuildByEmployee(String orgGroupId, boolean includeSubGroups, String benefitId) {

		try {
			File csvFile = FileSystemUtils.createTempFile(fileName, ".csv");
			DelimitedFileWriter writer = new DelimitedFileWriter(csvFile.getAbsolutePath(), false);

			fileName = csvFile.getName();

			boolean restrictSearchToSingleBenefit = false;

			if (benefitId != null && !benefitId.equals("") && !benefitId.equals(" "))
				restrictSearchToSingleBenefit = true;

			if (orgGroupId == null || orgGroupId.equals("0"))
				throw new ArahantException(" Please include an Organization Group ID. ");

			BOrgGroup borgy = new BOrgGroup(orgGroupId);

			Set<String> set = new HashSet<String>();
			Iterator<String> ity;

			if (includeSubGroups)
				set = borgy.getAllPersonIdsForOrgGroupHierarchy(true);
			else {
				BPerson[] bpers = borgy.listPeople(0);

				for (int loop = 0; loop < bpers.length; loop++)
					set.add(bpers[loop].getPersonId());
			}

			ity = set.iterator();

			List<Employee> empList = new ArrayList<Employee>();

			while (ity.hasNext())
				empList.add(new BEmployee(ity.next()).getEmployee());

			Collections.sort(empList);

			HibernateCriteriaUtil<HrBenefit> beneQuery = ArahantSession.getHSU().createCriteria(HrBenefit.class);
			beneQuery.eq(HrBenefit.TIMERELATED, 'Y');
			if (restrictSearchToSingleBenefit)
				beneQuery.eq(HrBenefit.BENEFITID, benefitId);

			List<HrBenefit> beneList = beneQuery.list();

			writer.writeField(" ");

			for (HrBenefit hrbn : beneList)
				writer.writeField(hrbn.getName());

			writer.endRecord();

			for (Employee emp : empList) {
				BEmployee bemp = new BEmployee(emp);

				if (bemp.getTimeOffTypes().length > 0) {
					writer.writeField(bemp.getNameLFM());

					for (HrBenefit hrbn : beneList) {
						Double hoursLeft = bemp.getHoursLeftOnBenefit(hrbn.getName());
						writer.writeField(df.format(hoursLeft));
					}
					writer.endRecord();
				}
			}

			writer.close();
		} catch (Exception ex) {
			System.out.println(ex);
		}

		return fileName;
	}

	public static void main(String[] args) {

		AccruedTimeOffByGroupExport ATO = new AccruedTimeOffByGroupExport();

		try {
			ATO.build("00001-0000000016", true, "", "E");
		} catch (Exception ex) {
			System.out.println(ex);
			Logger.getLogger(DependentExport.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
