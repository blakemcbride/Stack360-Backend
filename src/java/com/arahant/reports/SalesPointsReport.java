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
package com.arahant.reports;

import com.arahant.beans.Employee;
import com.arahant.utils.StandardProperty;
import com.arahant.beans.ProspectLog;
import com.arahant.business.BEmployee;
import com.arahant.business.BProperty;
import com.arahant.business.BSalesActivity;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;

public class SalesPointsReport extends ReportBase {

	public SalesPointsReport() throws ArahantException {
		super("SalesPtsRpt", "Sales Points Report", true);
	}

	public String build(final int fromDate, final int toDate) throws DocumentException {

		try {

			PdfPTable table;

			addHeaderLine();

			BSalesActivity[] bsa = BSalesActivity.list(BProperty.getInt(StandardProperty.SEARCH_MAX));

			addColumnHeader("Sales Person", Element.ALIGN_LEFT, 15);

			for (BSalesActivity bs : bsa)
			{
				addColumnHeader(bs.getActivityCode(), Element.ALIGN_RIGHT, 10);
			}

			addColumnHeader("Total Points", Element.ALIGN_RIGHT, 10);

			table = makeTable(getColHeaderWidths());

			writeColHeaders(table);

			boolean alternateRow = true;

			int pts = 0;
			int total = 0;

			for (Employee e : ArahantSession.getHSU().createCriteria(Employee.class).orderBy(Employee.LNAME).orderBy(Employee.FNAME).list()) 
			{
				alternateRow = !alternateRow;
				BEmployee be = new BEmployee(e);
				write(table, be.getEmployee().getNameLF(), alternateRow);
				for (BSalesActivity bs : bsa) {
					//count all the logs where activity is bs and employee is be
					if ((fromDate != 0) && (toDate != 0)) {
						pts = ArahantSession.getHSU().createCriteria(ProspectLog.class).eq(ProspectLog.SALES_ACTIVITY, bs.getBean()).eq(ProspectLog.EMPLOYEE, be.getEmployee()).ge(ProspectLog.CONTACT_DATE, fromDate).le(ProspectLog.CONTACT_DATE, toDate).count();
					} else if ((fromDate != 0) && (toDate == 0)) {
						pts = ArahantSession.getHSU().createCriteria(ProspectLog.class).eq(ProspectLog.SALES_ACTIVITY, bs.getBean()).eq(ProspectLog.EMPLOYEE, be.getEmployee()).ge(ProspectLog.CONTACT_DATE, fromDate).count();
					} else if ((fromDate == 0) && (toDate != 0)) {
						pts = ArahantSession.getHSU().createCriteria(ProspectLog.class).eq(ProspectLog.SALES_ACTIVITY, bs.getBean()).eq(ProspectLog.EMPLOYEE, be.getEmployee()).le(ProspectLog.CONTACT_DATE, toDate).count();
					} else {
						pts = ArahantSession.getHSU().createCriteria(ProspectLog.class).eq(ProspectLog.SALES_ACTIVITY, bs.getBean()).eq(ProspectLog.EMPLOYEE, be.getEmployee()).count();
					}

					total += pts * bs.getSalesPoints();
					write(table, pts, alternateRow);
				}
				write(table, total, alternateRow);
				total = 0;
			}

			addTable(table);

		} finally {
			close();

		}

		return getFilename();
	}

	public static void main(String args[]) {
		try {
			new SalesPointsReport().build(0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
