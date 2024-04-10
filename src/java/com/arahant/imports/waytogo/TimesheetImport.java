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

package com.arahant.imports.waytogo;

import com.arahant.beans.CompanyDetail;
import com.arahant.beans.Employee;
import com.arahant.beans.HrEmployeeStatus;
import com.arahant.beans.Project;
import com.arahant.business.BEmployee;
import com.arahant.business.BTimesheet;
import com.arahant.exceptions.ArahantException;
import com.arahant.genericImport.GenericImport;
import static com.arahant.genericImport.GenericImport.APPEND_MODE;
import static com.arahant.genericImport.GenericImport.FULL_MODE;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateSessionUtil;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
// The following line allows the enum types to be used without ColumnName.
import static com.arahant.imports.waytogo.TimesheetImport.ColumnName.*;
import static com.arahant.utils.ArahantSession.getCurrentPerson;
import java.util.Date;

/**
 *
 * @author Blake McBride
 */
public class TimesheetImport  extends GenericImport {
	/**  These represent an index into the internal column representations
	 */
	protected enum ColumnName implements GenericImport.EnumInfo {
		WORKER_ID,
		FIRST_NAME,
		MIDDLE_NAME,
		LAST_NAME,
		PROJECT_ID,
		WORK_DATE,
		BEGINNING_TIME,
		ENDING_TIME,
		TOTAL_HOURS,
		WORK_DESCRIPTION;

		@Override
		public int size() {
			return ColumnName.values().length;
		}

		@Override
		public int index() {
			return this.ordinal();
		}
	}
	
	private HashSet<String> includeList;
	private HrEmployeeStatus inactiveStatus;


		
	public TimesheetImport(String filename, int mode) throws FileNotFoundException {
		super("timesheet", filename, mode);
	}
	
	@Override
	protected void initColumns() {
		addCol(WORKER_ID, "Worker ID", "Employee ID", "EmpID");
		addCol(FIRST_NAME, "FNAME", "First Name");
		addCol(MIDDLE_NAME, "MI", "Middle Name");
		addCol(LAST_NAME, "LNAME", "Last Name");
		addCol(PROJECT_ID, "Project", "Project ID", "Project External Reference", "Project Reference", "Shift", "Shift Code");
		addCol(WORK_DATE, "Work Date", "Date Worked", "Date");
		addCol(BEGINNING_TIME, "Beginning Time", "Beginning");
		addCol(ENDING_TIME, "Ending Time", "Ending");
		addCol(TOTAL_HOURS, "Total Hours", "Hours");
		addCol(WORK_DESCRIPTION, "Work Description", "Description", "Description Of Work", "Summary");
	}
		
	/**
	 * Validate that the expected required columns are present.
	 * 
	 */
	@Override
	protected void assureRequiredColumns() {
		if (inactiveStatus == null  &&  mode == FULL_MODE)
			addError("inactive status not set");
		colRequired(WORKER_ID);
		colRequired(FIRST_NAME);
		colRequired(LAST_NAME);
		colRequired(PROJECT_ID);
		colRequired(WORK_DATE);
		colRequired(BEGINNING_TIME);
		colRequired(ENDING_TIME);
	}
		
	/**
	 * Check all data lines in import file.
	 * 
	 */
	@SuppressWarnings({"null", "ConstantConditions", "ResultOfObjectAllocationIgnored"})
	@Override
	protected void checkRows() {
		int recnum = 0;  // keep track of number of records we are dealing with for Hibernate flush purposes
		int today = DateUtils.today();
		
		while (true) {
			int workDate = 0, beg_time=0, end_time=0;
			double total_hours = -1.0;

			try {
				if (!df.nextLine())
					break;
			} catch (IOException ioe) {
//				throw ioe;
			} catch (Exception e) {
				addError(e.getMessage());
				continue;
			}
			
			rowsRead++;

			String workerID = df.getString(getColumnInfo(WORKER_ID).ecolnum);
			if (workerID == null  ||  workerID.isEmpty()) {
				addError("import file has missing Worker ID: " + df.getRow());
				continue;
			}
			flush(++recnum);
			List<Employee> empList = hsu.createCriteria(Employee.class).eq(Employee.EXTREF, workerID).list();
			if (empList.isEmpty()) {
				addError("import file has unknown Worker ID: " + workerID);
				continue;
			} else if (empList.size() > 1) {
				addError("database has more than one worker with the same Worker ID: " + workerID);
				continue;
			}
			Employee emp = empList.get(0);
			
			BEmployee be = new BEmployee(emp);
			if (be.getWageTypeId().isEmpty()) {
				addError("worker is missing a valid wage type: " + workerID);
				continue;				
			}
			if (be.getPositionId().isEmpty()) {
				addError("worker is missing a valid position: " + workerID);
				continue;				
			}


			for (ColumnInfo ci : ecols) {
				String fld = df.getString(ci.ecolnum).trim();
				
				if (ci.equals(WORKER_ID)) {
					//  already done
				} else if (ci.equals(FIRST_NAME)) {
					if (fld.isEmpty())
						addError("import file row missing first name: " + workerID);
					else if (!fld.trim().equalsIgnoreCase(emp.getFname().trim()))
						addError("import file row incorrect first name: " + workerID + " (" + fld.trim() + "/" + emp.getFname().trim() + ")");
				} else if (ci.equals(LAST_NAME)) {
					if (fld.isEmpty())
						addError("import file row missing last name: " + workerID);
					else if (!fld.trim().equalsIgnoreCase(emp.getLname().trim()))
						addError("import file row incorrect last name: " + workerID + " (" + fld.trim() + "/" + emp.getLname().trim() + ")");
				} else if (ci.equals(WORK_DATE)) {
					if (fld.isEmpty())
						addError("import file row missing work date: " + workerID);
					else {
						workDate = DateUtils.getDate(fld);
						if (workDate < 20141101  ||  workDate > today)
							addError("import file row invalid work date (" + fld + "): " + workerID);
					}
				} else if (ci.equals(PROJECT_ID)) {
					if (fld.isEmpty())
						addError("import file row missing project / shift code: " + workerID);
					else {
						List<Project> projects = hsu.createCriteria(Project.class).eq(Project.REFERENCE, fld).list();
						if (projects.isEmpty())
							addError("import file row invalid project / shift code (" + fld + "): " + workerID);
						else if (projects.size() > 1)
							addError("import file row project / shift code is used by more than one project (" + fld + "): " + workerID);
					}
				} else if (ci.equals(TOTAL_HOURS)) {
					if (!fld.isEmpty())
						try {
							total_hours = Double.parseDouble(fld);
						} catch (NumberFormatException ex) {
							addError("import file row invalid total hours (" + fld + "): " + workerID);
							total_hours = -1.0;
						}
				} else if (ci.equals(BEGINNING_TIME)) {
					if (fld.isEmpty())
						addError("import file row missing beginning time: " + workerID);
					else if (-1 == (beg_time=DateUtils.getTime(fld)))
						addError("import file row invalid beginning time (" + fld + "): " + workerID);
				} else if (ci.equals(ENDING_TIME)) {
					if (fld.isEmpty())
						addError("import file row missing ending time: " + workerID);
					else if (-1 == (end_time=DateUtils.getTime(fld)))
						addError("import file row invalid ending time (" + fld + "): " + workerID);
				}
			}
			if (total_hours > -.1) {
				try {
					int end_date = end_time < beg_time ? DateUtils.addDays(workDate, 1) : workDate;
					double max_hours = DateUtils.differenceInHours(workDate, beg_time, end_date, end_time);
					if (total_hours > max_hours + .01)
						addError("import file row invalid total hours (" + total_hours + "): " + workerID);
				} catch (Throwable e) {
					// ignore
				}
			}
			flush(recnum);
		}
	}
	
	@SuppressWarnings({"null", "ConstantConditions"})
	@Override
	protected void parseRows() {
		int recnum = 0;  // keep track of number of records we are dealing with for Hibernate flush purposes
		int today = DateUtils.today();
		Date td = DateUtils.getDate(today);
		if (mode == FULL_MODE)
			includeList = new HashSet<>();
		
		df.moveToStart();
		try {
			df.nextLine();  // skip header line
		} catch (Exception ex) {
			Logger.getLogger(TimesheetImport.class.getName()).log(Level.SEVERE, null, ex);
			return;
		}
		
		while (true) {
			Project project = null;
			int workDate = 0, beg_time=0, end_time=0;
			double total_hours = -1.0;
			String desc = "";
			
			try {
				if (!df.nextLine())
					break;
			} catch (IOException ioe) {
//				throw ioe;
			} catch (Exception e) {
				addError(e.getMessage());
				continue;
			}
			
			rowsRead++;
			
			String workerID = df.getString(getColumnInfo(WORKER_ID).ecolnum);
			if (workerID == null) {
				addError("import file has missing Worker ID: " + df.getRow());
				continue;
			}
			Employee emp = hsu.createCriteria(Employee.class).eq(Employee.EXTREF, workerID).first();

			recnum++;

			if (mode == FULL_MODE)
					includeList.add(workerID);
			
			for (ColumnInfo ci : ecols) {
				String fld = df.getString(ci.ecolnum).trim();
				try {
					if (ci.equals(WORKER_ID)) {
						//  already done
					} else if (ci.equals(FIRST_NAME)) {
						// ignore
					} else if (ci.equals(MIDDLE_NAME)) {
						// ignore
					} else if (ci.equals(LAST_NAME)) {
						// ignore
					} else if (ci.equals(PROJECT_ID)) {
						project = hsu.createCriteria(Project.class).eq(Project.REFERENCE, fld).first();
					} else if (ci.equals(WORK_DATE)) {
						workDate =  DateUtils.getDate(fld);
					} else if (ci.equals(BEGINNING_TIME)) {
						beg_time = DateUtils.getTime(fld);
					} else if (ci.equals(ENDING_TIME)) {
						end_time = DateUtils.getTime(fld);
					} else if (ci.equals(TOTAL_HOURS)) {
						if (!fld.isEmpty())
							total_hours = Double.parseDouble(fld);
					} else if (ci.equals(WORK_DESCRIPTION)) {
						desc = fld;
					}
				} catch (ArahantException | NumberFormatException e) {
					addError("error processing worker ID " + workerID + " - " + e.getMessage());					
				}
			}

			recordsAdded++;
			
			BTimesheet bts = new BTimesheet();
			bts.create();
			bts.setPersonId(emp.getPersonId());
//			bts.setProjectId(project.getProjectId());
			if (true) throw new ArahantException("XXYY");
			bts.setStartDate(workDate);
			int end_date = end_time < beg_time ? DateUtils.addDays(workDate, 1) : workDate;
			bts.setEndDate(end_date);
			bts.setStartTime(beg_time);
			bts.setEndTime(end_time);
			bts.setState('A');
			bts.setBillable('Y');
			bts.setBeginningEntryPerson(getCurrentPerson());
			bts.setBeginningEntryDate(td);
			if (total_hours > -.1)
				bts.setTotalHours(total_hours);
			else {
				double hours = DateUtils.differenceInHours(workDate, beg_time, end_date, end_time) - 1.0;
				bts.setTotalHours(Math.max(hours, 0.0));
			}
			bts.setDescription(desc);
			
			bts.insert();			
			
			flush(recnum);
		}
	}
	
	@Override
	protected void disableMissingRecords() {

	}
	
	public static void main(String [] argv) {
		try {
			ArahantSession.multipleCompanySupport = true;
			HibernateSessionUtil hsu = ArahantSession.getHSU();
			hsu.setCurrentCompany(hsu.createCriteriaNoCompanyFilter(CompanyDetail.class).eq(CompanyDetail.NAME, "BAS").first());
			hsu.getCurrentCompany();
			String reportFile = new TimesheetImport("/Users/blake/Arahant/Clients/Five Points/Employee Import Test Files/File 002.csv", APPEND_MODE).parse();
			System.out.println("output file = " + reportFile);
		} catch (Exception ex) {
			Logger.getLogger(TimesheetImport.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
