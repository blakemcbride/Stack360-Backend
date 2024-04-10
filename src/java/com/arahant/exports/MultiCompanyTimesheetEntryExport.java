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

import com.arahant.beans.Person;
import com.arahant.beans.Project;
import com.arahant.beans.Timesheet;
import com.arahant.utils.*;
import org.kissweb.DelimitedFileWriter;

import java.io.File;

public class MultiCompanyTimesheetEntryExport {

	public String export(int startDate, int endDate) throws Exception {
		File csvFile = FileSystemUtils.createTempFile("TimesheetEntry", ".csv");
		DelimitedFileWriter dfw = new DelimitedFileWriter(csvFile.getAbsolutePath());

		HibernateSessionUtil hsu = ArahantSession.getHSU();

		dfw.writeField("Payroll Company (Billing Company)");
		dfw.writeField("Employee Name");
		dfw.writeField("Date");
		dfw.writeField("Begin Time");
		dfw.writeField("End Time");
		dfw.writeField("Hours");
		dfw.writeField("Project Id");
		dfw.writeField("Project Description");
		dfw.writeField("Project Client Company");
		dfw.writeField("Memo/Notes");
		dfw.endRecord();

		HibernateCriteriaUtil<Timesheet> hcu = hsu.createCriteriaNoCompanyFilter(Timesheet.class).in(Timesheet.STATE, new char[]{ArahantConstants.TIMESHEET_APPROVED, ArahantConstants.TIMESHEET_INVOICED}).dateBetween(Timesheet.WORKDATE, startDate, endDate);
		hcu.joinTo(Timesheet.PERSON).orderBy(Person.LNAME).orderBy(Person.FNAME).orderBy(Person.MNAME).orderBy(Person.SSN);
		hcu.orderBy(Timesheet.WORKDATE).orderBy(Timesheet.BEGINNINGTIME);


		HibernateScrollUtil<Timesheet> scr = hcu.scroll();

		while (scr.next()) {
			Timesheet ts = scr.get();
			Project p = ts.getProjectShift().getProject();

			dfw.writeField(ts.getPerson().getCompanyBase().getName());
			dfw.writeField(ts.getPerson().getNameLFM());
			dfw.writeField(DateUtils.getDateFormatted(ts.getWorkDate()));
			dfw.writeField(DateUtils.getTimeFormatted(ts.getBeginningTime()).trim());
			dfw.writeField(DateUtils.getTimeFormatted(ts.getEndTime()).trim());
			dfw.writeField(ts.getTotalHours());
			dfw.writeField(p.getProjectName().trim());
			dfw.writeField(p.getDescription());
			dfw.writeField(p.getRequestingOrgGroup().getOwningCompany().getName());
			dfw.writeField(ts.getDescription());
			dfw.endRecord();
		}

		dfw.close();

		scr.close();
		return FileSystemUtils.getHTTPPath(csvFile);
	}

	public static void main(String args[]) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		try {
			hsu.beginTransaction();

			hsu.setCurrentPersonToArahant();
			hsu.dontAIIntegrate();//no need for ai Engine

			MultiCompanyTimesheetEntryExport x = new MultiCompanyTimesheetEntryExport();
			x.export(0, 20100101);

			hsu.rollbackTransaction(); //should not change data
		} catch (Exception e) {
			hsu.rollbackTransaction();
			e.printStackTrace();
		}
	}
}
