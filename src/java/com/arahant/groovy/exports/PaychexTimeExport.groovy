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


package com.arahant.groovy.exports

import com.arahant.beans.Address
import com.arahant.beans.OrgGroup
import com.arahant.beans.ProjectShift
import com.arahant.business.BClientCompany
import com.arahant.utils.*
import com.arahant.beans.Employee
import com.arahant.beans.Project
import com.arahant.business.BEmployee
import com.arahant.business.BProperty
import com.arahant.beans.Timesheet
import org.kissweb.DelimitedFileWriter

/**
 *
 * @author Blake McBride
 */
class PaychexTimeExport {
	private DelimitedFileWriter dfw
    private HibernateSessionUtil hsu
	private int nCols
	
	public String export(int begDate, int endDate) throws Exception {
		
        hsu = ArahantSession.getHSU(false)

        File csvFile = FileSystemUtils.createTempFile("PaychexTimeExport-" + begDate + "-", ".csv")
        dfw = new DelimitedFileWriter(csvFile.getAbsolutePath())
        dfw.setDateFormat("yyyyMMdd")
		
		writeColumnHeader()
		
		spinClients(begDate, endDate)

        dfw.close()
		return FileSystemUtils.getHTTPPath(csvFile)
    }
	
	private spinClients(int begDate, int endDate) {
		int i = 0
		Timesheet subTS = null
		Employee subEmp = null
		double hours = 0.0
		double expenses = 0.0
		double pay = 0.0
        String project_id = ""
		double hoursPerWeek = 40.0D
		HibernateScrollUtil<Employee> scr = hsu.createCriteria(Employee.class).orderBy(Employee.LNAME).orderBy(Employee.FNAME).scroll()
		while (scr.next()) {
			Employee emp = scr.get()
			
			String wageType = (new BEmployee(emp)).getWageTypeName();
			
			if (wageType != null  &&  wageType.length() > 0  &&  wageType.charAt(0) == 'S'.charAt(0)) {
				System.out.println("Employee " + emp.getNameLF() + " not included in Paychex export because they are salary.")
				continue;
			}

			HibernateScrollUtil<Timesheet> scr2 = hsu.createCriteria(Timesheet.class).
					eq(Timesheet.PERSON_ID, emp.getPersonId()).
//				eq(Timesheet.STATE, 'A' as char).
		            ge(Timesheet.WORKDATE, begDate).
					le(Timesheet.WORKDATE, endDate).
					joinTo(Timesheet.PROJECTSHIFT).
					joinTo(ProjectShift.PROJECT).
					orderBy(Project.REFERENCE).
				
//				orderBy(Timesheet.PROJECT_ID).
//				orderBy(Timesheet.WORKDATE).
//				orderBy(Timesheet.BEGINNINGTIME).
				scroll()
			while (scr2.next()) {
				Timesheet ts = scr2.get()
//				println "Status = " + ts.getState()
				char state = ts.getState()
				if (state != 'A'  &&  state != 'I')
					continue


                // make sure lazy loaded stuff is loaded before the clear() below
                Address ad = ts.getProjectShift().getProject().getAddress()
                if (ad != null)
                    ad.getState() // cause lazy load to load


				if (subTS == null  ||  !ts.getPersonId().equals(subTS.getPersonId())  ||  project_id != ts.getProjectShift().getProjectId()) {
					if (subTS != null) {
						if (hours > hoursPerWeek + 0.001  &&  pay < 0.009) {
							writeRecord(subEmp, subTS, hoursPerWeek, false, expenses, pay)
							writeRecord(subEmp, subTS, hours - hoursPerWeek, true, expenses, pay)
						} else {
							writeRecord(subEmp, subTS, hours, false, 0.0d, pay)
							if (expenses > 0.009)
								writeRecord(subEmp, subTS, hours, false, expenses, 0.0d)
						}
						if (!ts.getPersonId().equals(subTS.getPersonId()))
							hoursPerWeek = 40.0
						else {
							hoursPerWeek -= hours
							if (hoursPerWeek < 0.0)
								hoursPerWeek = 0.0
						}
					}
					subEmp = emp
					subTS = ts
					hours = ts.getTotalHours()
					expenses = ts.getTotalExpenses()
					pay = ts.getFixedPay()
                    project_id = ts.getProjectShift().getProjectId()
				} else {
					hours += ts.getTotalHours()
					expenses += ts.getTotalExpenses()
					pay += ts.getFixedPay()
				}
				if (++i % 30 == 0)
					hsu.clear() 
			}
		}

		if (subTS != null) {
			if (hours > hoursPerWeek + 0.001  &&  pay < 0.009) {
				writeRecord(subEmp, subTS, hoursPerWeek, false, expenses, pay)
				writeRecord(subEmp, subTS, hours - hoursPerWeek, true, expenses, pay)
			} else {
				writeRecord(subEmp, subTS, hours, false, 0.0d, pay)
				if (expenses > 0.009)
					writeRecord(subEmp, subTS, hours, false, expenses, 0.0d)
			}
		}

	}
	
	private void writeRecord(Employee emp, Timesheet ts, double hours, boolean overtime, double expenses, double pay) {
		BEmployee bemp = new BEmployee(emp)
		
		dfw.writeField BProperty.get(StandardProperty.PaychexClientID)			// Client ID
		dfw.writeField emp.getExtRef()									// Employee ID
		
		dfw.writeField emp.getLname()
		dfw.writeField emp.getFname()

		Project proj = ts.getProjectShift().getProject()
		
		//dfw.writeField DateUtils.getDateFormatted(ts.getWorkDate())		// fields for Tony's reference
		dfw.writeField proj.getDescription()                 // Shift code

        String pc = proj.getProjectCode()
        if (pc == null  ||  pc.isEmpty()) {
            OrgGroup og = proj.getRequestingOrgGroup()
            try {
                BClientCompany bcomp = new BClientCompany(og.getOrgGroupId())
                pc = bcomp.getDefaultProjectCode()
			if (pc == null)
					pc = ""
            } catch (Exception e) {
                pc = ""
            }
        }
        dfw.writeField pc		//  Org

		dfw.writeField ''		// Job Number

		if (overtime)
			dfw.writeField 'Overtime'
		else {
			short salaryType = bemp.getCurrentSalaryType()
			if (expenses > 0.009)
				dfw.writeField 'Reimbursement'
			else if (salaryType == (short) 1) {  // hourly
				dfw.writeField 'Hourly'
			} else if (salaryType == (short) 2) {  // salary
				dfw.writeField 'Salary'				
			} else if (salaryType == (short) 3) {  // one time
				dfw.writeField ''			
			} else {
				dfw.writeField ''		
			}	
		}
		if (expenses > 0.009)
			dfw.writeField ''
		else
			dfw.writeField bemp.getCurrentSalary()

		dfw.writeField ''		//  rate number

		if (expenses > 0.009)
			dfw.writeField ''
		else
			dfw.writeField hours

		dfw.writeField ''	// Units
		dfw.writeField ''	// Line Date
		if (pay > 0.009)
			dfw.writeField pay  // Amount
		else if (expenses > 0.009)
			dfw.writeField expenses
		else
			dfw.writeField ''	// Amount
		dfw.writeField '1';	// Check Seq Number

        Address ad = proj.getAddress()
        if (ad != null)
            dfw.writeField ad.getState()
        else {
			String ps = proj.getProjectState()
			if (ps == null)
				dfw.writeField ""  // for old data
			else
				dfw.writeField ps  // for old data
		}

		dfw.writeField ''	// Override Local
		dfw.writeField ''	// Override Local Juristiction
		dfw.writeField ''	// Labor Assignment
		for (int n=0 ; n < nCols ; n++)
			dfw.writeField ""
		dfw.endRecord()
	}

    private void writeColumnHeader() {
        dfw.writeField "Client ID"
        dfw.writeField "Worker ID"
		
		dfw.writeField "Last Name"
		dfw.writeField "First Name"
		
		//dfw.writeField "Date Worked"   // Added for Tony's reference
		dfw.writeField "Shift Code"
		
		dfw.writeField "Org"
		dfw.writeField "Job Number"
		dfw.writeField "Pay Component"
		dfw.writeField "Rate"
		dfw.writeField "Rate Number"
		dfw.writeField "Hours"
		dfw.writeField "Units"
		dfw.writeField "Line Date"
		dfw.writeField "Amount"
		dfw.writeField "Check Seq Number"
		dfw.writeField "Override State"
		dfw.writeField "Override Local"
		dfw.writeField "Override Local  Jurisdiction"
		dfw.writeField "Labor Assignment"
			
		nCols = dfw.getFieldCount()
        dfw.endRecord()
    }
	
}

