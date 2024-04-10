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
package com.arahant.genericImport;

import com.arahant.beans.*;
import com.arahant.business.BEmployee;
import com.arahant.business.BHREmplStatusHistory;
import com.arahant.business.BHRWage;
import com.arahant.business.BPerson;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.arahant.genericImport.EmployeeImport.ColumnName.*;

/**
 *
 * @author Blake McBride
 * 
 * The screen for this is at:
 * standard.hr.genericApplicantImport
 * 
 */
public class ApplicantImport extends GenericImport {

	/**  These represent an index into the internal column representations
	 */
	protected enum ColumnName implements EnumInfo {
		SSN,
		FIRST_NAME,
		MIDDLE_NAME,
		LAST_NAME,
		DATE_OF_BIRTH,
		GENDER,
		HOME_EMAIL,
		STREET1,
		STREET2,
		CITY,
		STATE,
		ZIP,
		CELL_PHONE,
		JOB_TITLE,
		WORKER_ID;

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



	public ApplicantImport(String filename, int mode) throws FileNotFoundException {
		super("employee", filename, mode);
	}
	
	@Override
	protected void initColumns() {
		addCol(SSN, "SSN", "SSAN");
		addCol(FIRST_NAME, "FNAME", "First Name");
		addCol(MIDDLE_NAME, "MI", "Middle Name");
		addCol(LAST_NAME, "LNAME", "Last Name");
		addCol(DATE_OF_BIRTH, "DOB", "Date of birth", "Birth Date");
		addCol(GENDER, "Gender", "Sex");
		addCol(HOME_EMAIL, "home email", "email", "personal email");
		addCol(STREET1, "Street 1", "Street", "Address 1", "Address");
		addCol(STREET2, "Street 2", "Address 2");
		addCol(CITY, "City");
		addCol(STATE, "State", "ST");
		addCol(ZIP, "Zip", "Zip Code");
		addCol(CELL_PHONE, "Cell Phone", "Cell", "Moble Phone");
		addCol(JOB_TITLE, "Job Title", "Title");
		addCol(WORKER_ID, "Worker ID", "Employee ID", "External Reference");
	}
		
	/**
	 * Validate that the expected required columns are present.
	 * 
	 */
	@Override
	protected void assureRequiredColumns() {
		if (inactiveStatus == null  &&  mode == FULL_MODE)
			addError("inactive status not set");
		colRequired(SSN);
//		colRequired(DATE_OF_BIRTH);
		colRequired(LAST_NAME);
		colRequired(FIRST_NAME);
//		colRequired(GENDER);
//		colRequired(EMPLOYMENT_STATUS);
//		colRequired(EMPLOYMENT_STATUS_DATE);
//		colRequired(PAY_TYPE);
//		colRequired(PAY_AMOUNT);
//		colRequired(PAY_FREQUENCY);
//		colRequired(POSITION);
		if (colExists(HIRE_DATE))
			colRequired(HIRE_STATUS);
		if (colExists(HIRE_STATUS))
			colRequired(HIRE_DATE);
	}
		
	/**
	 * Check all data lines in import file.
	 * 
	 */
	@SuppressWarnings({"null", "ConstantConditions", "ResultOfObjectAllocationIgnored"})
	@Override
	protected void checkRows() {
		ColumnInfo ssnCol = getColumnInfo(SSN);
		int recnum = 0;  // keep track of number of records we are dealing with for Hibernate flush purposes
		int today = DateUtils.today();
		boolean hourlyReported = false, salaryReported = false;
		
		while (true) {
			
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

			String ssn = BPerson.formatSsn(df.getString(ssnCol.ecolnum));
			if (ssn == null) {
				addError("import file has missing ssn: " + df.getRow());
				continue;
			}
			BPerson bp = BPerson.findPerson(ssn);
			boolean existingEmployee = bp != null;
			if (existingEmployee) {
				recnum++;
				try {
					new BEmployee(bp);  //  throws an exception
				} catch (Exception e) {
					addError("import file has a person who is not an employee: " + ssn);
					continue;
				}
				if (mode == APPEND_MODE) {
					flush(recnum);
					continue;
				}
			}

			for (ColumnInfo ci : ecols) {
				String fld = df.getString(ci.ecolnum).trim();
				
				if (ci.equals(SSN)) {
					//  already done
				} else if (ci.equals(FIRST_NAME)) {
					if (fld.length() == 0)
						addError("import file row missing first name: " + ssn);						
				} else if (ci.equals(MIDDLE_NAME)) {

				} else if (ci.equals(LAST_NAME)) {
					if (fld.length() == 0)
						addError("import file row missing last name: " + ssn);
				} else if (ci.equals(DATE_OF_BIRTH)) {
//					if (fld.length() == 0)
//						addError("import file row missing date of birth: " + ssn);
//					else 
					if (fld.length() != 0) {
						int dt = DateUtils.getDate(fld);
						if (dt < 19100101  ||  dt > today)
							addError("import file row invalid date of birth (" + fld + "): " + ssn);
					}
				} else if (ci.equals(GENDER)) {
//					if (fld.length() == 0)
//						addError("import file row missing gender: " + ssn);
//					else 
					if (fld.length() != 0) {
						char s = Character.toUpperCase(fld.charAt(0));
						if (s != 'M'  &&  s != 'F')
							addError("import file row invalid gender (" + fld + "): " + ssn);
					}
				} else if (ci.equals(HOME_EMAIL)) {

				} else if (ci.equals(STREET1)) {

				} else if (ci.equals(STREET2)) {

				} else if (ci.equals(CITY)) {

				} else if (ci.equals(STATE)) {

				} else if (ci.equals(ZIP)) {

				} else if (ci.equals(CELL_PHONE)) {

				} else if (ci.equals(JOB_TITLE)) {
					
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
		if (mode == FULL_MODE)
			includeList = new HashSet<>();
		
		df.moveToStart();
		try {
			df.nextLine();  // skip header line
		} catch (Exception ex) {
			Logger.getLogger(ApplicantImport.class.getName()).log(Level.SEVERE, null, ex);
			return;
		}
		
		while (true) {
			
			HrEmployeeStatus employeeStatus = null, hireStatus=null;
			int employeeStatusDate = 0, hireDate=0;
			char payType = ' ';
			double payAmount = -1.0;
			HrPosition position = null;
			String login_id = null;
			String password = null;
			String screen_group = null;
			String security_group = null;
			
			try {
				if (!df.nextLine())
					break;
			} catch (IOException ioe) {
//				throw ioe;
			} catch (Exception e) {
				addError(e.getMessage());
				continue;
			}
			
			BEmployee be;
			rowsRead++;
			
			String ssn = BPerson.formatSsn(getString(SSN));
			if (ssn == null) {
				addError("import file has missing ssn: " + df.getRow());
				continue;
			}
			BPerson bp = BPerson.findPerson(ssn);
			boolean existingEmployee = bp != null;
			if (existingEmployee) {
				recnum++;
				try {
					be = new BEmployee(bp);  //  throws an exception
				} catch (Exception e) {
					addError("person not an employee:  " + ssn);
					continue;
				}
				if (mode == APPEND_MODE) {
					flush(recnum);
					continue;
				}
			} else {
				be = new BEmployee();
				be.create();	
				be.setSsn(ssn);
				be.setRecordType('R');
				be.insertNoChecks();
			}
			
			if (mode == FULL_MODE)
					includeList.add(ssn);
			
			for (ColumnInfo ci : ecols) {
				String fld = df.getString(ci.ecolnum).trim();
				try {
					if (ci.equals(SSN)) {
						//  already done
					} else if (ci.equals(FIRST_NAME)) {
						be.setFirstName(fld);
					} else if (ci.equals(MIDDLE_NAME)) {
						be.setMiddleName(fld);
					} else if (ci.equals(LAST_NAME)) {
						be.setLastName(fld);
					} else if (ci.equals(DATE_OF_BIRTH)) {
						if (fld.length() > 0)
							be.setDob(DateUtils.getDate(fld));
					} else if (ci.equals(GENDER)) {
						if (fld.length() > 0)
							be.setSex(fld);
					} else if (ci.equals(HOME_EMAIL)) {
						be.setPersonalEmail(fld);
					} else if (ci.equals(STREET1)) {
						be.setStreet(fld);
					} else if (ci.equals(STREET2)) {
						be.setStreet2(fld);
					} else if (ci.equals(CITY)) {
						be.setCity(fld);
					} else if (ci.equals(STATE)) {
						be.setState(fld);
					} else if (ci.equals(ZIP)) {
						be.setZip(fld);
					} else if (ci.equals(CELL_PHONE)) {
						if (fld.length() > 0)
							be.setMobilePhone(fld);
					} else if (ci.equals(POSITION)) {
						if (fld.length() > 0)
							position = hsu.createCriteria(HrPosition.class).eq(BenefitClass.NAME, fld)
															.le(HrPosition.FIRSTACTIVEDATE, today)
															.geOrEq(HrPosition.LASTACTIVEDATE, today, 0)
															.first();
					} else if (ci.equals(JOB_TITLE)) {
						if (fld.length() > 0)
							be.setJobTitle(fld);
					} else if (ci.equals(WORKER_ID)) {
						if (fld.length() > 0)
							be.setExtRef(fld);
					}
				} catch (ArahantException | NumberFormatException e) {
					addError("error procesing row " + ssn + "\n" + e.getMessage());					
				}
			}
			if (!existingEmployee) {
//				be.insert();
				recnum++;
				recordsAdded++;
			} else 
				recordsUpdated++;
			
			// Since these columns can appear in any order and I need more than one piece of information to complete this record,
			// I must wait to go through all the columns and process them afterwords
			if (employeeStatus != null  &&  employeeStatusDate > 0) {
				HrEmplStatusHistory hist = hsu.createCriteria(HrEmplStatusHistory.class).eq(HrEmplStatusHistory.EMPLOYEE, be.getEmployee()).eq(HrEmplStatusHistory.EFFECTIVEDATE, employeeStatusDate).first();
				if (hist == null) {
					BHREmplStatusHistory bsh = new BHREmplStatusHistory();
					bsh.create();
					bsh.setEffectiveDate(employeeStatusDate);
					bsh.setEmployee(be.getEmployee());
					bsh.setHrEmployeeStatus(employeeStatus);
					bsh.insert();
				} else
					hist.setStatusId(employeeStatus.getStatusId());
			}
			if (hireStatus != null  &&  hireDate > 0  &&  hireDate != employeeStatusDate) {
				HrEmplStatusHistory hist = hsu.createCriteria(HrEmplStatusHistory.class).eq(HrEmplStatusHistory.EMPLOYEE, be.getEmployee()).eq(HrEmplStatusHistory.EFFECTIVEDATE, hireDate).first();
				if (hist == null) {
					BHREmplStatusHistory bsh = new BHREmplStatusHistory();
					bsh.create();
					bsh.setEffectiveDate(hireDate);
					bsh.setEmployee(be.getEmployee());
					bsh.setHrEmployeeStatus(hireStatus);
					bsh.insert();
				} else
					hist.setStatusId(hireStatus.getStatusId());
			}
			
			//  see previous comment
			if (payType != ' '  &&  payAmount > 0.01  &&  position != null) {
				List<HrWage> wr = hsu.createCriteria(HrWage.class).eq(HrWage.EMPLOYEEID, be.getPersonId()).eq(HrWage.EFFECTIVEDATE, employeeStatusDate).list();
				if (wr.isEmpty()) {
					BHRWage bw = new BHRWage();
					bw.create();
					bw.setEmployeeId(be.getPersonId());
					bw.setWageAmount(payAmount);
					bw.setEffectiveDate(employeeStatusDate);
					bw.setPositionId(position.getPositionId());

					if (payType == 'H') {
						WageType wt = hsu.createCriteria(WageType.class).eq(WageType.PERIOD_TYPE, WageType.PERIOD_HOURLY)
									.le(WageType.FIRSTACTIVEDATE, today)
									.geOrEq(WageType.LASTACTIVEDATE, today, 0)
									.first();
						bw.setWageTypeId(wt.getWageTypeId());
					} else if (payType == 'S') {
						WageType wt = hsu.createCriteria(WageType.class).eq(WageType.PERIOD_TYPE, WageType.PERIOD_SALARY)
									.le(WageType.FIRSTACTIVEDATE, today)
									.geOrEq(WageType.LASTACTIVEDATE, today, 0)
									.first();
						bw.setWageTypeId(wt.getWageTypeId());					
					}
					bw.insert();
				} else {
					BHRWage bw = new BHRWage(wr.get(0));
					bw.setWageAmount(payAmount);
					bw.setEffectiveDate(employeeStatusDate);
					bw.setPositionId(position.getPositionId());

					if (payType == 'H') {
						WageType wt = hsu.createCriteria(WageType.class).eq(WageType.PERIOD_TYPE, WageType.PERIOD_HOURLY)
									.le(WageType.FIRSTACTIVEDATE, today)
									.geOrEq(WageType.LASTACTIVEDATE, today, 0)
									.first();
						bw.setWageTypeId(wt.getWageTypeId());
					} else if (payType == 'S') {
						WageType wt = hsu.createCriteria(WageType.class).eq(WageType.PERIOD_TYPE, WageType.PERIOD_SALARY)
									.le(WageType.FIRSTACTIVEDATE, today)
									.geOrEq(WageType.LASTACTIVEDATE, today, 0)
									.first();
						bw.setWageTypeId(wt.getWageTypeId());					
					}
				}
			}
			
			if (login_id == null  &&  password == null  &&  security_group != null  &&  screen_group != null) {
				String fname = be.getFirstName();
				String lname = be.getLastName();
				String ssn2 = be.getSsn();
				if (exists(fname)  &&  exists(lname)  && exists(ssn2)) {
					String user = fname.charAt(0) + lname + ssn2.substring(ssn.length()-4);
					be.setUserLogin(user);
					be.setUserPassword(password, true);
				}
			}
			
			be.update();
			flush(recnum);
		}
	}
	
	private boolean exists(String x) {
		return x != null  &&  x.length() > 0;
	}
	
	@Override
	protected void disableMissingRecords() {
//		HrEmployeeStatus inactiveStatus = hsu.createCriteria(HrEmployeeStatus.class).eq(HrEmployeeStatus.ACTIVE, 'N').first();
		HibernateScrollUtil<Employee> scr = hsu.createCriteria(Employee.class).scroll();
		int recnum = 0;   // keep track of number of records we are dealing with for Hibernate flush purposes
		int today = DateUtils.today();
		while (scr.next()) {
			Employee emp = scr.get();
			recnum++;
			String ssn = emp.getSsn();
			if (includeList.contains(ssn)) {
				flush(recnum);
				continue;
			}
			
			BEmployee be = new BEmployee(emp);
			char active = be.getLastStatusHistory().getHrEmployeeStatus().getActive();
			if (active == 'N') {
				flush(recnum);
				continue;
			}

			//  create inactive status history record
			BHREmplStatusHistory sh = new BHREmplStatusHistory();
			sh.create();
			sh.setEffectiveDate(today);
			sh.setEmployee(emp);
			sh.setHrEmployeeStatus(inactiveStatus);
			sh.insert();
			recordsDeleted++;
			
			flush(recnum);
		}
	}
	
	public static void main(String [] argv) {
		try {
			ArahantSession.multipleCompanySupport = true;
			HibernateSessionUtil hsu = ArahantSession.getHSU();
			hsu.setCurrentCompany(hsu.createCriteriaNoCompanyFilter(CompanyDetail.class).eq(CompanyDetail.NAME, "BAS").first());
			hsu.getCurrentCompany();
			String reportFile = new ApplicantImport("/Users/blake/Arahant/Clients/Five Points/Employee Import Test Files/File 002.csv", APPEND_MODE).parse();
			System.out.println("output file = " + reportFile);
		} catch (Exception ex) {
			Logger.getLogger(ApplicantImport.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
