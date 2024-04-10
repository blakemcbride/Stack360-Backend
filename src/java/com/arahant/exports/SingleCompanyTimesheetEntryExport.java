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
import com.arahant.beans.OrgGroup;
import com.arahant.beans.OrgGroupAssociation;
import com.arahant.beans.OrgGroupHierarchy;
import com.arahant.beans.Person;
import com.arahant.beans.Timesheet;
import com.arahant.utils.*;
import org.kissweb.DelimitedFileWriter;

import java.io.File;
import java.util.*;

public class SingleCompanyTimesheetEntryExport {

	public String export(int startDate, int endDate) throws Exception {
		File csvFile = FileSystemUtils.createTempFile("TimesheetEntry", ".csv");
		DelimitedFileWriter dfw = new DelimitedFileWriter(csvFile.getAbsolutePath());

		HibernateSessionUtil hsu = ArahantSession.getHSU();
		
		// pull the top level org groups for this company, ordered alphabetically
		HibernateCriteriaUtil<OrgGroupHierarchy> hcp = hsu.createCriteria(OrgGroupHierarchy.class).eq(OrgGroupHierarchy.PARENT_ID, hsu.getCurrentCompany().getOrgGroupId());
		hcp.joinTo(OrgGroupHierarchy.ORGGROUPBYCHILDGROUPID).orderBy(OrgGroup.NAME);
		
		String [] valueOrder = new String[hcp.set().size()];
		
		dfw.writeField("SSN");
		dfw.writeField("Employee Name");
		dfw.writeField("Project Id");
		dfw.writeField("Project Description");
		dfw.writeField("Begin Date");
		dfw.writeField("Begin Time");
		dfw.writeField("End Date");
		dfw.writeField("End Time");
		dfw.writeField("Hours");
		dfw.writeField("Memo/Notes");
		dfw.writeField("Finalized");
		dfw.writeField("Status");
		
		HibernateScrollUtil<OrgGroupHierarchy> scp = hcp.scroll();
		int valueOrderCount = 0;

		while (scp.next()) {
			OrgGroupHierarchy ogh = scp.get();
			

			dfw.writeField("Grp - " + ogh.getOrgGroupByChildGroupId().getName());
		    valueOrder[valueOrderCount] = ogh.getChildGroupId();  // hang onto this to serve as the parent id to seek when I work through the values
			valueOrderCount++;
		}
		
		scp.close();
		
		dfw.endRecord();
		

		// HibernateCriteriaUtil<Timesheet> hcu = hsu.createCriteria(Timesheet.class).in(Timesheet.STATE, new char[]{ArahantConstants.  ArahantConstants.TIMESHEET_APPROVED, ArahantConstants.TIMESHEET_INVOICED}).dateBetween(Timesheet.WORKDATE, startDate, endDate);
		HibernateCriteriaUtil<Timesheet> hcu = hsu.createCriteria(Timesheet.class).dateBetween(Timesheet.WORKDATE, startDate, endDate);	
		hcu.joinTo(Timesheet.PERSON).orderBy(Person.LNAME).orderBy(Person.FNAME).orderBy(Person.MNAME).orderBy(Person.SSN);
		hcu.orderBy(Timesheet.WORKDATE).orderBy(Timesheet.BEGINNINGTIME);


		HibernateScrollUtil<Timesheet> scr = hcu.scroll();

		while (scr.next()) {
			Timesheet ts = scr.get();

			dfw.writeField(ts.getPerson().getUnencryptedSsn());
			dfw.writeField(ts.getPerson().getNameLFM());
			dfw.writeField(ts.getProjectShift().getProject().getProjectName().trim());
			dfw.writeField(ts.getProjectShift().getProject().getDescription());
			dfw.writeField(DateUtils.getDateFormatted(ts.getWorkDate()));
			dfw.writeField(DateUtils.getTimeFormatted(ts.getBeginningTime()).trim());
			dfw.writeField(DateUtils.getDateFormatted(ts.getEndDate()));
			dfw.writeField(DateUtils.getTimeFormatted(ts.getEndTime()).trim());
			dfw.writeField(ts.getTotalHours());
			dfw.writeField(ts.getDescription());
			
			HibernateCriteriaUtil<Employee> hce = hsu.createCriteria(Employee.class).eq(Employee.PERSONID, ts.getPersonId());
	
			HibernateScrollUtil<Employee> sce = hce.scroll();
			int tsFinalizedDate = 0;
			while (sce.next()) {
			Employee tsee = (sce.get());
			tsFinalizedDate = tsee.getTimesheetFinalDate();
			}
			sce.close();
			dfw.writeField(((ts.getWorkDate() <= tsFinalizedDate) || (ts.getState() == 65 || ts.getState() == 68 || ts.getState() == 73)) ? "Yes" : "No");
			String stateSS = " ";
			switch (ts.getState()) {
				case 65: stateSS = "Approved";
					     break;
				case 78: stateSS = "New";
					     break;
				case 67: stateSS = "Changed";
					     break;
				case 70: stateSS = "Fixed";
					     break;
				case 82: stateSS = "Rejected";
					     break;
				case 80: stateSS = "Problem";
					     break;
				case 68: stateSS = "Deferred";
					     break;
				case 73: stateSS = "Invoiced";
					     break;
			}
			dfw.writeField(stateSS);

			int valueTarget = 0;
			while (valueTarget < valueOrderCount) {
				String orgGroupSet = "";
				boolean didWrite = false;
				Iterator<OrgGroupAssociation> ogai = ts.getPerson().getOrgGroupAssociations().iterator();
				while (ogai.hasNext()) {
					OrgGroup og = ogai.next().getOrgGroup();
					Iterator<OrgGroupHierarchy> oghi = og.getOrgGroupHierarchiesForChildGroupId().iterator();
					while (oghi.hasNext()) {
						OrgGroup ogi = oghi.next().getOrgGroupByParentGroupId();
						if (ogi.getOrgGroupId().equalsIgnoreCase(valueOrder[valueTarget])) {
							orgGroupSet = (didWrite ? orgGroupSet.concat(", " + og.getName()) : orgGroupSet.concat(og.getName()));
							didWrite = true;
						}
					}
				}
			dfw.writeField(orgGroupSet.length() == 0 ? " " : orgGroupSet);
			valueTarget++;
			}
		
			dfw.endRecord();
		}

		scr.close();

		dfw.close();
		return FileSystemUtils.getHTTPPath(csvFile);
	}

	public static void main(String args[]) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		try {
			hsu.beginTransaction();

			hsu.setCurrentPersonToArahant();
			hsu.dontAIIntegrate();//no need for ai Engine

			SingleCompanyTimesheetEntryExport x = new SingleCompanyTimesheetEntryExport();
			x.export(0, 20100101);

			hsu.rollbackTransaction(); //should not change data
		} catch (Exception e) {
			hsu.rollbackTransaction();
			e.printStackTrace();
		}
	}
}
