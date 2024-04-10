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

import com.arahant.utils.*
import com.arahant.beans.Employee
import com.arahant.beans.Address
import com.arahant.beans.Phone
import com.arahant.business.BEmployee
import com.arahant.beans.OrgGroupAssociation
import org.kissweb.DelimitedFileWriter

/**
 *
 * @author Blake McBride
 * 
 * The screen for this is at:
 * com.arahant.dynamic.services.standard.hr.employeeExport.PersonExport
 * 
 */
class EmployeeExport {
	
	private DelimitedFileWriter dfw
    private HibernateSessionUtil hsu
	private int nCols
	
	public String export() throws Exception {
        hsu = ArahantSession.getHSU(false)

        File csvFile = FileSystemUtils.createTempFile("EmployeeExport-", ".csv")
        dfw = new DelimitedFileWriter(csvFile.getAbsolutePath())
        dfw.setDateFormat("yyyyMMdd")
		
		writeColumnHeader()
		
		spinClients()

        dfw.close()
		return FileSystemUtils.getHTTPPath(csvFile);
    }
	
	private spinClients() {
		int i = 0
		HibernateScrollUtil<Employee> scr = hsu.createCriteria(Employee.class).orderBy(Employee.LNAME).orderBy(Employee.FNAME).scroll()
		while (scr.next()) {
			Employee emp = scr.get()
			BEmployee bemp = new BEmployee(emp)
			
			dfw.writeField emp.getUnencryptedSsn()
			dfw.writeField emp.getExtRef()
			dfw.writeField emp.getFname()
			dfw.writeField emp.getMname()
			dfw.writeField emp.getLname()
			dfw.writeField DateUtils.getDateFormatted(emp.getDob())
			char sex = emp.getSex()
			if (sex == 'M'  ||  sex == 'F')
				dfw.writeField sex
			else
				dfw.writeField ""

			dfw.writeField bemp.getPersonalEmail()
			
			Set<OrgGroupAssociation> orgGroups = bemp.getOrgGroupAssociations()
			if (orgGroups.isEmpty()) {
				dfw.writeField ''
				dfw.writeField ''
			} else {
				OrgGroupAssociation [] oga = orgGroups.toArray(new OrgGroupAssociation[orgGroups.size()])
				dfw.writeField oga[0].getOrgGroup().getName()
				if (oga.length > 1)
					dfw.writeField oga[1].getOrgGroup().getName()
				else
					dfw.writeField ''
			}
			dfw.writeField bemp.getBenefitClass()?.getName()
			
			Address addr = null
			for (t in emp.getAddresses())
				if (t.getRecordType() == 'R') {
					addr = t;
					break;
				}
			if (addr != null) {
				dfw.writeField addr.getStreet()
				dfw.writeField addr.getStreet2()
				dfw.writeField addr.getCity()
				dfw.writeField addr.getState()
				dfw.writeField addr.getZip()
			} else {
				dfw.writeField ""
				dfw.writeField ""
				dfw.writeField ""
				dfw.writeField ""
				dfw.writeField ""			
			}
			
			dfw.writeField getPhone(1, emp)
			dfw.writeField getPhone(2, emp)
			dfw.writeField getPhone(3, emp)
			
			dfw.writeField bemp.getUserLogin()
			
			dfw.writeField ''  //  password
			
			String status = bemp.getLastStatusName()  // Worker Status, reason, and status date
			if (status != null) {
				dfw.writeField status
				dfw.writeField DateUtils.getDateFormatted(bemp.getLastStatusDate())
			} else {
					dfw.writeField ''
					dfw.writeField ''
			}
			
			short salaryType = bemp.getCurrentSalaryType()
			if (salaryType == 1) {  // hourly
				dfw.writeField 'Hourly'
				dfw.writeField bemp.getCurrentSalary()
			} else if (salaryType == 2) {  // salary
				dfw.writeField 'Salary'
				dfw.writeField bemp.getCurrentSalary()				
			} else if (salaryType == 3) {  // one time
				dfw.writeField 'One Time'
				dfw.writeField bemp.getCurrentSalary()				
			} else {  
				dfw.writeField ''
				dfw.writeField ''			
			}
			dfw.writeNoZero bemp.getPayPeriodsPerYear()
			dfw.writeNoZero bemp.getHoursPerWeek()

			dfw.writeField bemp.getPositionName()

			dfw.writeField bemp.getScreenGroupName()
			
			dfw.writeField bemp.getSecurityGroupName()

			if (bemp.getFirstActiveStatusHistory() != null)
				dfw.writeField DateUtils.getDateFormatted(bemp.getFirstActiveStatusHistory()?.getEffectiveDate())
			else
				dfw.writeField ''
			dfw.writeField bemp.getFirstActiveStatusHistory()?.getHrEmployeeStatus()?.getName()

			dfw.writeField bemp.getJobTitle()

			
			dfw.endRecord()
			if (++i % 30 == 0)
				hsu.clear() 
		}
	}
	
	private String getPhone(int type, Employee emp) {
		Phone ph = null
		for (t in emp.getPhones())
			if (t.getRecordType() == 'R'  &&  t.getPhoneType() == type) {
				ph = t;
				break;
			}
		return ph == null ? "" : ph.getPhoneNumber()
	}

    private void writeColumnHeader() {
        dfw.writeField "SSN"
		dfw.writeField "Worker ID"
        dfw.writeField "First Name"
        dfw.writeField "MI"
        dfw.writeField "Last Name"
        dfw.writeField "Date of Birth"
        dfw.writeField "Gender"
        dfw.writeField "Home Email"
        dfw.writeField "Org Group 1"
        dfw.writeField "Org Group 2"
        dfw.writeField "Employee Class"
        dfw.writeField "Street 1"
        dfw.writeField "Street 2"
        dfw.writeField "City"
        dfw.writeField "State"
        dfw.writeField "Zip Code"
		dfw.writeField "Work Phone"
		dfw.writeField "Home Phone"
        dfw.writeField "Cell Phone"
        dfw.writeField "Login ID"
        dfw.writeField "Password"
        dfw.writeField "Employment Status"
        dfw.writeField "Employment Status Date"
        dfw.writeField "Pay Type"
        dfw.writeField "Pay Amount"
        dfw.writeField "Pay Periods Per Year"
        dfw.writeField "Normal Hours Per Week"
        dfw.writeField "Position"
        dfw.writeField "Screen Group"
        dfw.writeField "Security Group"
        dfw.writeField "Hire Date"
        dfw.writeField "Hire Status"
        dfw.writeField "Job Title"
		
 		nCols = dfw.getFieldCount()
        dfw.endRecord()
    }
	
}

