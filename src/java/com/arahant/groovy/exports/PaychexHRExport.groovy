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

package com.arahant.groovy.exports


import com.arahant.utils.*
import com.arahant.beans.Employee
import com.arahant.beans.Address
import com.arahant.beans.Phone
import com.arahant.business.BEmployee
import com.arahant.business.BProperty
import org.kissweb.DelimitedFileWriter


/**
 *
 * @author Blake McBride
 */
class PaychexHRExport {
	
	private DelimitedFileWriter dfw
    private HibernateSessionUtil hsu
	private int nCols
	
	String export() throws Exception {
        hsu = ArahantSession.getHSU(false)

        File csvFile = FileSystemUtils.createTempFile("PaychexHRExport-", ".csv")
        dfw = new DelimitedFileWriter(csvFile.getAbsolutePath())
        dfw.setDateFormat("yyyyMMdd")
		
		writeColumnHeader()
		
		spinClients()

        dfw.close()
		return FileSystemUtils.getHTTPPath(csvFile)
    }
	
	private spinClients() {
		int i = 0
		HibernateScrollUtil<Employee> scr = hsu.createCriteria(Employee.class).orderBy(Employee.LNAME).orderBy(Employee.FNAME).scroll()
		while (scr.next()) {
			Employee emp = scr.get()
			BEmployee bemp = new BEmployee(emp)
			dfw.writeField BProperty.get(StandardProperty.PaychexClientID)		// Client ID
			dfw.writeField emp.getExtRef()
			char empType = emp.getEmploymentType()
			if (empType == (char) 'E')
				dfw.writeField "EE"
			else
				dfw.writeField "ICI"  //  ??
			dfw.writeField emp.getFname()
			String mname = emp.getMname()
			if (mname == null  ||  mname.length() == 0)
				dfw.writeField ""
			else
				dfw.writeField mname.charAt(0)
			dfw.writeField emp.getLname()
			dfw.writeField ""					// IC Company Name
			dfw.writeField emp.getUnencryptedSsn()
			dfw.writeField ""					// Federal ID Number
			dfw.writeField DateUtils.getDateFormatted(emp.getDob())
			dfw.writeField ""					// Clock ID
			char sex = emp.getSex()
			if (sex == (char) 'M'  ||  sex == (char) 'F')
				dfw.writeField sex
			else
				dfw.writeField ""
			dfw.writeField emp.getHrEeoRace()?.getName()
			
			Address addr = null
			for (t in emp.getAddresses())
				if (t.getRecordType() == (char) 'R') {
					addr = t
					break
				}
			if (addr != null) {
				dfw.writeField addr.getStreet()
				dfw.writeField addr.getStreet2()
				dfw.writeField ""				// PO Box
				dfw.writeField addr.getCity()
				dfw.writeField addr.getState()
				dfw.writeField addr.getZip()
				dfw.writeField "US"
			} else {
				dfw.writeField ""
				dfw.writeField ""
				dfw.writeField ""				// PO Box
				dfw.writeField ""
				dfw.writeField ""
				dfw.writeField ""
				dfw.writeField "US"				
			}
			
			Phone ph = null
			for (t in emp.getPhones())
				if (t.getRecordType() == (char) 'R'  &&  (t.getPhoneType() == 2  ||  t.getPhoneType() == 3)) {
					ph = t
					break
				}
			if (ph != null)
				dfw.writeField strip_phone(ph.getPhoneNumber())
			else
				dfw.writeField ''
				
			dfw.writeField ''					// Full/Part Time			
			dfw.writeField ''					// Eligable for Retirement Plan
			dfw.writeField ''					// PEO Class Code
			dfw.writeField ''					// Organizational Unit
			dfw.writeField ''					// Business Location Name
			
			dfw.writeField bemp.getPositionName()
			
			String eeocat = bemp.getEEOCategory()
			if (eeocat == null)
				dfw.writeField ''
			else {
				eeocat = eeocat.toLowerCase()
				if (eeocat.startsWith('exec'))
					dfw.writeField '1.1'
				else if (eeocat.startsWith('first'))
					dfw.writeField '1.2'
				else if (eeocat.startsWith('prof'))
					dfw.writeField '2'
				else if (eeocat.startsWith('tech'))
					dfw.writeField '3'
				else if (eeocat.startsWith('sales'))
					dfw.writeField '4'
				else if (eeocat.startsWith('admin')  ||  eeocat.startsWith('office'))
					dfw.writeField '5'
				else if (eeocat.startsWith('craft'))
					dfw.writeField '6'
				else if (eeocat.startsWith('operative'))
					dfw.writeField '7'
				else if (eeocat.startsWith('labor'))
					dfw.writeField '8'
				else if (eeocat.startsWith('serv'))
					dfw.writeField '9'
				else
					dfw.writeField ''
			}
			dfw.writeField ''					// work state
			dfw.writeField ''					// officer type
			dfw.writeField ''					// class code
			dfw.writeField ''					// class code suffix
			dfw.writeField ''					// waive code
			
			String status = bemp.getLastStatusName()  // Worker Status, reason, and status date
			if (status != null) {
				status = status.toLowerCase()
				if (status.startsWith('sch')) {
					dfw.writeField 'A'
					dfw.writeField '1'
					dfw.writeField DateUtils.getDateFormatted(bemp.getLastStatusDate())
				} else if (status.startsWith('not')) {
					dfw.writeField 'L'
					dfw.writeField '5'
					dfw.writeField DateUtils.getDateFormatted(bemp.getLastStatusDate())
				} else if (status.startsWith('do')) {
					dfw.writeField 'T'
					dfw.writeField '1'
					dfw.writeField DateUtils.getDateFormatted(bemp.getLastStatusDate())
				} else {
					dfw.writeField ''
					dfw.writeField ''
					dfw.writeField ''
				}
			} else {
					dfw.writeField ''
					dfw.writeField ''
					dfw.writeField ''
			}

			short salaryType = bemp.getCurrentSalaryType()
			if (salaryType == (short) 1) {  // hourly
				dfw.writeField 'HR'
				dfw.writeField bemp.getCurrentSalary()
			} else if (salaryType == (short) 2) {  // salary
				dfw.writeField 'AS'
				dfw.writeField bemp.getCurrentSalary()				
			} else if (salaryType == (short) 3) {  // one time
				dfw.writeField 'PR'
				dfw.writeField bemp.getCurrentSalary()				
			} else {  
				dfw.writeField ''
				dfw.writeField ''			
			}
			
			dfw.writeField ''		//  pay 2
			dfw.writeField ''			
			
			dfw.writeField ''		//  pay 3
			dfw.writeField ''			
			
			dfw.writeField ''		//  pay 4
			dfw.writeField ''			
			
			dfw.writeField ''		//  pay 5
			dfw.writeField ''			
			
			int ppy = bemp.getPayPeriodsPerYear()
			if (ppy != 0)
				dfw.writeField ppy
			else
				dfw.writeField ''
				
			short hpw = bemp.getHoursPerWeek()
			if (hpw != (short) 0  &&  ppy != 0) {
				int tmp = (hpw * 52) / ppy  // Groovy gives an error if tmp is not used
				dfw.writeField tmp
			} else
				dfw.writeField ''
				
			dfw.writeField ''			//  Overtime exempt
			dfw.writeField ''			//  Federal tax residency
			
			char et = bemp.getEmploymentType()	//  Federal taxability Status
			if (et == (char) 'E')
				dfw.writeField 'Y'
			else if (et == (char) 'C')
				dfw.writeField 'N'
			
			char w4status = bemp.getW4Status()			// Federal filing status
			if (w4status == (char) 'S'  ||  w4status == (char) 'M')
				dfw.writeField w4status
			else if (w4status == (char) 'H')
				dfw.writeField 'MWS'
			else
				dfw.writeField ''
				
			dfw.writeField bemp.getFederalExemptions()
			
			String addFedTaxType = bemp.getAddFederalIncomeTaxType()
			if (addFedTaxType == "A") {
				dfw.writeField bemp.getAddFederalIncomeTaxAmount()  //  Federal Additional Tax Amount
				dfw.writeField ''		//  Federal additional tax percent
				dfw.writeField ''		//  Federal override tax amount
			} else if (addFedTaxType == "P") {
				dfw.writeField ''		//  Federal Additional Tax Amount
				dfw.writeField bemp.getAddFederalIncomeTaxAmount()		//  Federal additional tax percent
				dfw.writeField ''		//  Federal override tax amount
			} else if (addFedTaxType == "F") {
				dfw.writeField ''		//  Federal Additional Tax Amount
				dfw.writeField ''		//  Federal additional tax percent
				dfw.writeField bemp.getAddFederalIncomeTaxAmount() ''		//  Federal override tax amount
			} else {
				dfw.writeField ''		//  Federal Additional Tax Amount
				dfw.writeField ''		//  Federal additional tax percent
				dfw.writeField ''		//  Federal override tax amount				
			}
			dfw.writeField ''		//  Federal override tax percent
			
			String stateTaxType = bemp.getAddStateIncomeTaxType()
			if (stateTaxType == "A") {
				dfw.writeField 'Y'	// State Taxability Status				
				dfw.writeField bemp.getUnemploymentState()	// Unemployment state
				dfw.writeField bemp.getTaxState()	// Income tax state
				dfw.writeField bemp.getUnemploymentState()	// Disability state
			} else if (stateTaxType == "P") {
				dfw.writeField 'Y'	// State Taxability Status								
				dfw.writeField bemp.getUnemploymentState()	// Unemployment state
				dfw.writeField bemp.getTaxState()	// Income tax state
				dfw.writeField bemp.getUnemploymentState()	// Disability state
			} else if (stateTaxType == "F") {
				dfw.writeField 'Y'	// State Taxability Status				
				dfw.writeField bemp.getUnemploymentState()	// Unemployment state
				dfw.writeField bemp.getTaxState()	// Income tax state
				dfw.writeField bemp.getUnemploymentState()	// Disability state
			} else {
				dfw.writeField 'N'	// State Taxability Status
				dfw.writeField ''	// Unemployment state
				dfw.writeField ''	// Income tax state
				dfw.writeField ''	// Disability state
			}

			if (w4status == (char) 'S'  ||  w4status == (char) 'M')  // State filing status
				dfw.writeField w4status
			else if (w4status == (char) 'H')
				dfw.writeField 'MWS'
			else
				dfw.writeField ''
				
			dfw.writeField 'EXEM'  //  State allowance name 1
			dfw.writeField bemp.getStateExemptions()  //  State allowance number 1
			dfw.writeField ''						// State allowance amount 1
			
			for (int n=2 ; n <= 3 ; n++) {
				dfw.writeField ''  // State allowance name
				dfw.writeField ''  // State allowance number
				dfw.writeField ''  // State allowance amount
			}

			String addStateTaxType = bemp.getAddStateIncomeTaxType()
			if (addStateTaxType == 'A') {
				dfw.writeField bemp.getAddStateIncomeTaxAmount()  //  State additional tax amount
				dfw.writeField ''		//  State additional tax percent
				dfw.writeField ''		//  State override tax amount
			} else if (addStateTaxType == 'F') {
				dfw.writeField ''  //  State additional tax amount
				dfw.writeField ''		//  State additional tax percent
				dfw.writeField bemp.getAddStateIncomeTaxAmount()		//  State override tax amount
			} else if (addStateTaxType == 'P') {
				dfw.writeField ''  //  State additional tax amount
				dfw.writeField bemp.getAddStateIncomeTaxAmount()		//  State additional tax percent
				dfw.writeField ''		//  State override tax amount
			} else {
				dfw.writeField ''	  //  State additional tax amount
				dfw.writeField ''		//  State additional tax percent
				dfw.writeField ''		//  State override tax amount				
			}
			dfw.writeField ''	// State override tax percent
			dfw.writeField ''	//  Reduced withholding amount
			dfw.writeField ''	// State withholding %
			dfw.writeField ''	// Dependent health insurance benefit indicator
			dfw.writeField ''	// Date dependent health insurance benefits available
			dfw.writeField ''	// Employee health insurance benefit indicator
			dfw.writeField ''	// Date employee health insurance benefits available
			dfw.writeField ''	// County
			dfw.writeField ''	// Family owned business indicator
			dfw.writeField ''	// Seasonal indicator
			dfw.writeField ''	// state juirtiction 1
			dfw.writeField ''	// local regulation name 1
			dfw.writeField ''	// local tax residency 1
			dfw.writeField ''	// PA live work status
			dfw.writeField ''	// Local taxability status
			dfw.writeField ''	// Local filing status 1
			dfw.writeField 'EXEM'  //  Local allowance name 1
			dfw.writeField bemp.getStateExemptions()  //  Local allowance number 1
			dfw.writeField ''	// % of earnings taxed 1
			dfw.writeField ''	// Ohio local residency tax rate 1
			
			String addLocalTaxType = bemp.getAddLocalIncomeTaxType()
			if (addLocalTaxType == 'A') {
				dfw.writeField bemp.getAddLocalIncomeTaxAmount()  //  Local additional tax amount
				dfw.writeField ''		//  Local additional tax percent
				dfw.writeField ''		//  Local override tax amount
			} else if (addLocalTaxType == 'F') {
				dfw.writeField ''  //  Local additional tax amount
				dfw.writeField ''		//  Local additional tax percent
				dfw.writeField bemp.getAddLocalIncomeTaxAmount()		//  Local override tax amount
			} else if (addLocalTaxType == 'P') {
				dfw.writeField ''  //  Local additional tax amount
				dfw.writeField bemp.getAddLocalIncomeTaxAmount()		//  Local additional tax percent
				dfw.writeField ''		//  Local override tax amount
			} else {
				dfw.writeField ''	  //  Local additional tax amount
				dfw.writeField ''		//  Local additional tax percent
				dfw.writeField ''		//  Local override tax amount				
			}
			dfw.writeField ''	// Local override tax percent 1
			
			for (int n=0 ; n < nCols ; n++)
				dfw.writeField ""
			dfw.endRecord()
			if (++i % 30 == 0)
				hsu.clear() 
		}
	}
	
	private static String strip_phone(String p) {
		if (p == null)
			return ''
		StringBuilder r = new StringBuilder()
		int n = p.length()
		for (int i=0 ; i < n ; i++) {
			char c = p.charAt(i)
			if (Character.isDigit(c))
				r.append(c)
		}
		return r.length() == 10 ? r.toString() : ""
	}

    private void writeColumnHeader() {
        dfw.writeField "Client ID"
        dfw.writeField "Worker ID"
        dfw.writeField "Worker Type"
        dfw.writeField "First Name"
        dfw.writeField "Middle Initial"
        dfw.writeField "Last Name"
        dfw.writeField "IC Company Name"
        dfw.writeField "SSN"
        dfw.writeField "Federal ID Number"
        dfw.writeField "Birth Date"
        dfw.writeField "Clock ID"
        dfw.writeField "Gender"
        dfw.writeField "Ethnic Origin"
        dfw.writeField "Address 1"
        dfw.writeField "Address 2"
        dfw.writeField "PO Box"
        dfw.writeField "City"
        dfw.writeField "State / Province"
        dfw.writeField "Zip / Postal Code"
        dfw.writeField "Country"
        dfw.writeField "Telephone"
        dfw.writeField "Full/Part Time"
        dfw.writeField "Eligible for Retirement plan"
        dfw.writeField "PEO Class Code"
        dfw.writeField "Organization Unit"
        dfw.writeField "Business Location Name"
        dfw.writeField "Position"
        dfw.writeField "EEO Job Category"
        dfw.writeField "Work State"
        dfw.writeField "Officer Type"
        dfw.writeField "Class Code"
        dfw.writeField "Class Code Suffix"
        dfw.writeField "Waive Code"
        dfw.writeField "Worker Status"
        dfw.writeField "Reason"
        dfw.writeField "Status Date"
        dfw.writeField "Pay Rate 1"
        dfw.writeField "Pay Rate Amount 1"
        dfw.writeField "Pay Rate 2"
        dfw.writeField "Pay Rate Amount 2"
        dfw.writeField "Pay Rate 3"
        dfw.writeField "Pay Rate Amount 3"
        dfw.writeField "Pay Rate 4"
        dfw.writeField "Pay Rate Amount 4"
        dfw.writeField "Pay Rate 5"
        dfw.writeField "Pay Rate Amount 5"
        dfw.writeField "Pay Frequency"
        dfw.writeField "Standard Pay Hours"
        dfw.writeField "Overtime Exempt"
        dfw.writeField "Federal Tax Residency"
        dfw.writeField "Federal Taxability Status"
        dfw.writeField "Federal Filing Status"
        dfw.writeField "Federal Allowances"
        dfw.writeField "Federal Additional Tax Amount"
        dfw.writeField "Federal Additional Tax Percent"
        dfw.writeField "Federal Override Tax Amount"
        dfw.writeField "Federal Override Tax Percent"
        dfw.writeField "State Taxability Status"
        dfw.writeField "State Unemployment"
        dfw.writeField "State Income Tax"
        dfw.writeField "State Disability"
        dfw.writeField "State Filing Status"
        dfw.writeField "State Allowance Name 1"
        dfw.writeField "State Allowance Number 1"
        dfw.writeField "State Allowance Amount 1"
        dfw.writeField "State Allowance Name 2"
        dfw.writeField "State Allowance Number 2"
        dfw.writeField "State Allowance Amount 2"
        dfw.writeField "State Allowance Name 3"
        dfw.writeField "State Allowance Number 3"
        dfw.writeField "State Allowance Amount 3"
        dfw.writeField "State Additional Tax Amount"
        dfw.writeField "State Additional Tax Percent"
        dfw.writeField "State Override Tax Amount"
        dfw.writeField "State Override Tax Percent"
        dfw.writeField "Reduced Withholding Amount"
        dfw.writeField "State Withholding %"
        dfw.writeField "Dependent Health Insurance Benefits Indicator"
        dfw.writeField "Date Dependent Health Insurance Benefits Are Available"
        dfw.writeField "Employee Health Insurance Benefits Indicator"
        dfw.writeField "Date Employee Health Insurance Benefits are Available"
        dfw.writeField "County"
        dfw.writeField "Family Owned Business Owner Indicator"
        dfw.writeField "Seasonal Indicator"
        dfw.writeField "State Jurisdiction 1"
        dfw.writeField "Local Regulation Name 1"
        dfw.writeField "Local Tax Residency 1"
        dfw.writeField "PA Live/Work Status 1"
        dfw.writeField "Local Taxability Status 1"
        dfw.writeField "Local Filing Status 1"
        dfw.writeField "Local Allowance Name 1"
        dfw.writeField "Local Allowance Number 1"
        dfw.writeField "% Of Earnings Taxed 1"
        dfw.writeField "Ohio Local Residence Tax Rate 1"
        dfw.writeField "Local Additional Tax Amount 1"
        dfw.writeField "Local Additional Tax Percent 1"
        dfw.writeField "Local Override Tax Amount 1"
        dfw.writeField "Local Override Tax Percent 1"
        dfw.writeField "Employee Waiver Indicator 1"
        dfw.writeField "State Jurisdiction 2"
        dfw.writeField "Local Regulation Name 2"
        dfw.writeField "Local Tax Residency 2"
        dfw.writeField "PA Live/Work Status 2"
        dfw.writeField "Local Taxability Status 2"
        dfw.writeField "Local Filing Status 2"
        dfw.writeField "Local Allowance Name 2"
        dfw.writeField "Local Allowance Number 2"
        dfw.writeField "% Of Earnings Taxed 2"
        dfw.writeField "Ohio Local Residence Tax Rate 2"
        dfw.writeField "Local Additional Tax Amount 2"
        dfw.writeField "Local Additional Tax Percent 2"
        dfw.writeField "Local Override Tax Amount 2"
        dfw.writeField "Local Override Tax Percent 2"
        dfw.writeField "Employee Waiver Indicator 2"
        dfw.writeField "State Jurisdiction 3"
        dfw.writeField "Local Regulation Name 3"
        dfw.writeField "Local Tax Residency 3"
        dfw.writeField "PA Live/Work Status 3"
        dfw.writeField "Local Taxability Status 3"
        dfw.writeField "Local Filing Status 3"
        dfw.writeField "Local Allowance Name 3"
        dfw.writeField "Local Allowance Number 3"
        dfw.writeField "% Of Earnings Taxed 3"
        dfw.writeField "Ohio Local Residence Tax Rate 3"
        dfw.writeField "Local Additional Tax Amount 3"
        dfw.writeField "Local Additional Tax Percent 3"
        dfw.writeField "Local Override Tax Amount 3"
        dfw.writeField "Local Override Tax Percent 3"
        dfw.writeField "Employee Waiver Indicator 3"
        dfw.writeField "Job Number"
        dfw.writeField "Home Labor Assignment"
			
		nCols = dfw.getFieldCount()
        dfw.endRecord()
    }

}

