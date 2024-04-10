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

import com.arahant.beans.CompanyDetail;
import com.arahant.beans.Employee;
import com.arahant.beans.LoginLog;
import com.arahant.beans.Person;
import com.arahant.exceptions.ArahantException;
import com.arahant.lisp.ABCL;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.List;

/**
 *
 */
public class ActiveEmployeesReport extends ReportBase {

	public ActiveEmployeesReport() {
		super("ActiveEmployees", "Active Employees Report", true);
	}

	public String build(int activeAsOfDate) {
		try {

			PdfPTable table = new PdfPTable(1);
			addTable(table);

			List<CompanyDetail> companies;

			//Decided not to put the activeAsOfDate option on the screen
			//but I left it here anyway just in case we want it later
			//or for internal use in the main method
			if (activeAsOfDate > 0)
				companies = hsu.createCriteria(CompanyDetail.class).orderBy(CompanyDetail.NAME).joinTo(CompanyDetail.PERSONS).joinTo(Person.LOGIN_LOGS).ge(LoginLog.DATE, DateUtils.getDate(activeAsOfDate)).list();
			else
				companies = hsu.createCriteriaNoCompanyFilter(CompanyDetail.class).orderBy(CompanyDetail.NAME).list();

			table = makeTable(new int[]{15, 55, 15, 15});

			write(table, "");
			writeColHeader(table, "Company", Element.ALIGN_LEFT);
			writeColHeader(table, "Employee Count", Element.ALIGN_RIGHT);
			write(table, "");

			boolean alternateRow = true;

			int total = 0;
			for (CompanyDetail cd : companies) {

				hsu.setCurrentCompany(cd);
				int empCount = hsu.createCriteria(Employee.class).activeEmployee().count();

				// toggle the alternate row
				alternateRow = !alternateRow;

				write(table, "", false);
				write(table, cd.getName(), alternateRow);
				write(table, empCount, alternateRow);
				write(table, "", false);
				total += empCount;
			}
			if (total > 0) {
				write(table, "", false);
				write(table, "", false);
				write(table, "", false);
				write(table, "", false);

				write(table, "", false);
				write(table, "", true);
				writeAlign(table, "Total:  " + total, Element.ALIGN_RIGHT, true);
				write(table, "", false);
			}

			addTable(table);

		} catch (Exception e) {
			throw new ArahantException(e);
		} finally {
			close();

		}
		return getFilename();
	}

	//Blake knows how to run main methods from the command line.  This allows him to pass in a date in the command line args.
	public static void main(String[] args) {
		ABCL.init();
		ArahantSession.getHSU().setCurrentPersonToArahant();
		try {
			new ActiveEmployeesReport().build(Integer.parseInt(args[0]));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
