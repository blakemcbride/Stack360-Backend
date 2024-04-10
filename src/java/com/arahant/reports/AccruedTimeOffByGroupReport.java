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


package com.arahant.reports;

import com.arahant.beans.Employee;
import com.arahant.beans.HrBenefit;
import com.arahant.business.BEmployee;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BPerson;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.text.DecimalFormat;
import java.util.*;

// 7/29/2011
public class AccruedTimeOffByGroupReport extends ReportBase {

	private DecimalFormat df = new DecimalFormat("0.00");

	public AccruedTimeOffByGroupReport() {
		super("AccruedTimeOffReport", "Accrued Time Off Report");
	}

	public String Build(String orgGroupId, boolean includeSubGroups, String benefitId, String sortBy) {
		HibernateSessionUtil rrr = ArahantSession.getHSU();
		System.out.println(rrr.getCurrentCompany());
		String filename = null;

		if (sortBy == null || (!sortBy.equals("E") && !sortBy.equals("B")))
			throw new ArahantException(" Please select how to sort the report: 'E' for Employees or 'B' for Benefits. ");
		else if (sortBy.equals("E"))
			filename = BuildByEmployee(orgGroupId, includeSubGroups, benefitId);
		else if (sortBy.equals("B"))
			filename = BuildByBenefit(orgGroupId, includeSubGroups, benefitId);

		return filename;
	}

	private String BuildByEmployee(String orgGroupId, boolean includeSubGroups, String benefitId) {
		boolean restrictSearchToSingleBenefit = false;

		if (benefitId != null && !benefitId.equals("") && !benefitId.equals(" "))
			restrictSearchToSingleBenefit = true;

		if (orgGroupId == null || orgGroupId.equals("0"))
			throw new ArahantException(" Please include an Organization Group ID. ");

		try {

			PdfPTable table = makeTable(new int[]{30, 15, 14, 50});

			writeHeaderLine("Accrued Time Off By Employees: ", ArahantSession.getHSU().getCurrentCompany().getName());

			BOrgGroup borgy = new BOrgGroup(orgGroupId);

			// A set that will hold the employee's ID
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

			List<Employee> list = new ArrayList<Employee>();

			while (ity.hasNext())
				list.add(new BEmployee(ity.next()).getEmployee());

			Collections.sort(list);

			for (Employee emp : list) {
				BEmployee bemp = new BEmployee(emp);
				BPerson pers = new BPerson(bemp.getPerson());

				String[] string = bemp.getTimeOffTypes();

				if (string.length > 0) {
					writeAlign(table, pers.getNameLFM(), Element.ALIGN_LEFT, true, 4, 18);

					writeAlign(table, " ", Element.ALIGN_CENTER, false, 4);
					writeColHeader(table, "Time Off Type", 1);
					write(table, " ");
					writeColHeader(table, "Hours Remaining", 1);
					writeAlign(table, " ", Element.ALIGN_LEFT, false, 4);

					String nameOfBenefit = "null";

					if (restrictSearchToSingleBenefit == true) {

						HibernateCriteriaUtil beneName = ArahantSession.getHSU().createCriteria(HrBenefit.class);

						beneName.eq(HrBenefit.BENEFITID, benefitId);
						List<HrBenefit> beneList = beneName.list(); // this should only return one  result.

						nameOfBenefit = beneList.get(0).getName();
					}

					// I made these arrays 44 long, because I do not know how many results will be returned each time and there are 44 different benefits in the database
					String[] employeeBenefits = new String[44];
					double[] timeLeft = new double[44];

					for (int loop = 0; loop < string.length; loop++) {
						System.out.println(bemp.getNameLFM() + ", " + string[loop]);
						try {

							employeeBenefits[loop] = string[loop];
							timeLeft[loop] = bemp.getHoursLeftOnBenefit(employeeBenefits[loop]);

							if (restrictSearchToSingleBenefit == true) {
								if (employeeBenefits[loop].equals(nameOfBenefit)) {
									writeAlign(table, employeeBenefits[loop], Element.ALIGN_LEFT, false, 2);
									System.out.println(nameOfBenefit + ", " + timeLeft[loop]);
									writeAlign(table, "" + df.format(timeLeft[loop]), Element.ALIGN_RIGHT, false, 1);
									write(table, " ");
								}
							} else {

								writeAlign(table, employeeBenefits[loop], Element.ALIGN_LEFT, false, 2);
								writeAlign(table, "" + df.format(timeLeft[loop]), Element.ALIGN_RIGHT, false, 1);
								write(table, " ");
							}
						} catch (Exception e) {
							System.out.println(e);
						}
					}
					writeAlign(table, " ", Element.ALIGN_CENTER, false, 4);
				}
			}
			addTable(table);
			close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return getFilename();
	}

	private String BuildByBenefit(String orgGroupId, boolean includeSubGroups, String benefitId) {

		boolean restrictSearchToSingleBenefit = false;
		if (benefitId != null && !benefitId.equals("") && !benefitId.equals(" "))
			restrictSearchToSingleBenefit = true;

		if (orgGroupId == null || orgGroupId.equals("0") || orgGroupId.equals(" "))
			throw new ArahantException(" Please include an Organization Group ID. ");

		try {

			PdfPTable table = makeTable(new int[]{30, 15, 13, 50});
			writeHeaderLine("Accrued Time Off By Benefits: ", ArahantSession.getHSU().getCurrentCompany().getName());

			BOrgGroup borgy = new BOrgGroup(orgGroupId);

			Set<String> set = new HashSet<String>();
			Iterator<String> ity;

			if (includeSubGroups)
				set = borgy.getAllPersonIdsForOrgGroupHierarchy(true, false);
			else {
				BPerson[] bpers = borgy.listPeople(0);
				for (int loop = 0; loop < bpers.length; loop++)
					set.add(bpers[loop].getPersonId());
			}

			ity = set.iterator();
			List<Employee> list = new ArrayList<Employee>();
			while (ity.hasNext())
				list.add(new BEmployee(ity.next()).getEmployee());

			Collections.sort(list);

			// This retieves all the employees in the orgGroup.           
			HibernateCriteriaUtil<HrBenefit> beneQuery = ArahantSession.getHSU().createCriteria(HrBenefit.class);
			beneQuery.eq(HrBenefit.TIMERELATED, 'Y');
			if (restrictSearchToSingleBenefit)
				beneQuery.eq(HrBenefit.BENEFITID, benefitId);
			List<HrBenefit> beneList = beneQuery.list();

			// This retieves all the time related benefits
			for (HrBenefit hrbn : beneList) {
				writeAlign(table, hrbn.getName(), Element.ALIGN_LEFT, true, 4, 18);
				writeAlign(table, " ", Element.ALIGN_CENTER, false, 4);

				List<BEmployee> bempList = new ArrayList<BEmployee>();
				List<String> hourList = new ArrayList<String>();

				for (Employee emp : list) {

					BEmployee bemp = new BEmployee(emp);

					bempList.add(bemp);
					try {
						double hours = bemp.getHoursLeftOnBenefit(hrbn.getName());
						hourList.add(df.format(hours) + "");

					} catch (Exception ex) {
						hourList.add("error retreiving hours");
					}
				}

				boolean hasPrinted_ColumnNames = false;
				int numberOfEmployeesWithBeneift = 0;

				for (int loop = 0; loop < bempList.size(); loop++) {

					boolean hasBenefit = false;

					String[] timeOffTypes = bempList.get(loop).getTimeOffTypes();
					for (int ALTloop = 0; ALTloop < timeOffTypes.length; ALTloop++)
						if (timeOffTypes[ALTloop].equals(hrbn.getName()))
							hasBenefit = true;

					if (hasBenefit && hasPrinted_ColumnNames == false) {
						writeColHeader(table, "Employee", Element.ALIGN_CENTER);

						write(table, " ");
						writeColHeader(table, "Hours Remaining", Element.ALIGN_CENTER);
						write(table, " ");
						hasPrinted_ColumnNames = true;
					}

					if (hasBenefit) {

						numberOfEmployeesWithBeneift++;
						writeAlign(table, bempList.get(loop).getNameLFM(), Element.ALIGN_LEFT, false, 2);
						writeAlign(table, hourList.get(loop), Element.ALIGN_RIGHT, false, 1);
						write(table, " ");
					}
				}

				if (numberOfEmployeesWithBeneift <= 0)
					writeAlign(table, "No employees have this benefit", Element.ALIGN_CENTER, false, 4);

				writeAlign(table, " ", Element.ALIGN_CENTER, false, 4);
			}
			addTable(table);
			close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return getFilename();
	}

	public static void main(String[] args) {

		AccruedTimeOffByGroupReport dbt = new AccruedTimeOffByGroupReport();

		System.out.println(dbt.Build("00001-0000000016", true, "", "B"));

	}
}
