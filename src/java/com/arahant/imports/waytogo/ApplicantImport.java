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

import com.arahant.beans.BenefitClass;
import com.arahant.beans.CompanyDetail;
import com.arahant.beans.Employee;
import com.arahant.beans.HrEmplStatusHistory;
import com.arahant.beans.HrEmployeeStatus;
import com.arahant.beans.HrPosition;
import com.arahant.beans.HrWage;
import com.arahant.beans.OrgGroup;
import com.arahant.beans.Project;
import com.arahant.beans.ProjectEmployeeJoin;
import com.arahant.beans.ScreenGroup;
import com.arahant.beans.SecurityGroup;
import com.arahant.beans.WageType;
import com.arahant.business.BEmployee;
import com.arahant.business.BHREmplStatusHistory;
import com.arahant.business.BHRWage;
import com.arahant.business.BPerson;
import com.arahant.business.BProject;
import com.arahant.exceptions.ArahantException;
import com.arahant.genericImport.*;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

// The following line allows the enum types to be used without ColumnName.
import static com.arahant.imports.waytogo.EmployeeImport.ColumnName.*;
import com.arahant.utils.IDGenerator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Blake McBride
 * 
 * The screen for this is at:
 * custom.waytogo.applicantImport
 * 
 */
public class ApplicantImport extends GenericImport {
	
	private boolean shiftsExist = false;
	
	/**  These represent an index into the internal column representations
	 */
	protected enum ColumnName implements GenericImport.EnumInfo {
		SSN,
		FIRST_NAME,
		MIDDLE_NAME,
		LAST_NAME,
		DATE_OF_BIRTH,
		GENDER,
		HOME_EMAIL,
		DEPARTMENT,
		LOCATION,
		BENEFIT_CLASS,
		STREET1,
		STREET2,
		CITY,
		STATE,
		ZIP,
		CELL_PHONE,
		LOGIN_ID,
		PASSWORD,
		EMPLOYMENT_STATUS,
		EMPLOYMENT_STATUS_DATE,
		PAY_TYPE,
		PAY_AMOUNT,
		PAY_FREQUENCY,
		HOURS_PER_WEEK,
		POSITION,
		SCREEN_GROUP,
		SECURITY_GROUP,
		HIRE_DATE,
		HIRE_STATUS,
		JOB_TITLE,
		WORKER_ID,
		SHIFT_1,
		SHIFT_2,
		SHIFT_3,
		SHIFT_4;

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


		
	public ApplicantImport(String filename,int mode) throws FileNotFoundException {
		super("applicant", filename, mode);
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
		addCol(DEPARTMENT, "Department", "Division", "org group", "organizational group", "org group 1");
		addCol(LOCATION, "Location", "org group 2");
		addCol(BENEFIT_CLASS, "Class", "Employee Class", "Benefit Class", "Benefit Classification", "Benefits Classification");
		addCol(STREET1, "Street 1", "Street", "Address 1", "Address");
		addCol(STREET2, "Street 2", "Address 2");
		addCol(CITY, "City");
		addCol(STATE, "State", "ST");
		addCol(ZIP, "Zip", "Zip Code");
		addCol(CELL_PHONE, "Cell Phone", "Cell", "Moble Phone");
		addCol(LOGIN_ID, "Login ID", "User Name", "User", "Login");
		addCol(PASSWORD, "Password", "PW", "Login Password", "User Password");
		addCol(EMPLOYMENT_STATUS, "Status", "Employment Status", "Employee Status");
		addCol(EMPLOYMENT_STATUS_DATE, "Status Date", "Employment Status Date", "Employee Status Date");
		addCol(PAY_TYPE, "Pay Type");
		addCol(PAY_AMOUNT, "Pay", "Pay amount", "Salary", "Rate");
		addCol(PAY_FREQUENCY, "Pay Frequency", "Payment Frequency", "Pay Periods Per Year");
		addCol(HOURS_PER_WEEK, "Hours Per Week", "Normal Hours Per Week");
		addCol(POSITION, "Position");
		addCol(SCREEN_GROUP, "Screen Group", "Screen");
		addCol(SECURITY_GROUP, "Security Group", "Security");
		addCol(HIRE_DATE, "Hire Date");
		addCol(HIRE_STATUS, "Hire Status");
		addCol(JOB_TITLE, "Job Title", "Title");
		addCol(WORKER_ID, "Worker ID", "Employee ID", "External Reference");
		addCol(SHIFT_1, "Shift 1");
		addCol(SHIFT_2, "Shift 2");
		addCol(SHIFT_3, "Shift 3");
		addCol(SHIFT_4, "Shift 4");
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
		if (colExists(SHIFT_1)  ||  colExists(SHIFT_2)  ||  colExists(SHIFT_3)  ||  colExists(SHIFT_4))
			shiftsExist = true;
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

				} else if (ci.equals(DEPARTMENT)) {
					
					if (fld.length() > 0) {
						OrgGroup rec = hsu.createCriteria(OrgGroup.class).eq(OrgGroup.NAME, fld).first();
						if (rec == null)
							addError("import file row invalid department (" + fld + "): " + ssn);
					}

				} else if (ci.equals(LOCATION)) {
					
					if (fld.length() > 0) {
						OrgGroup rec = hsu.createCriteria(OrgGroup.class).eq(OrgGroup.NAME, fld).first();
						if (rec == null)
							addError("import file row invalid location (" + fld + "): " + ssn);
					}

				} else if (ci.equals(BENEFIT_CLASS)) {
					
					if (fld.length() > 0) {
						BenefitClass rec = hsu.createCriteria(BenefitClass.class).eq(BenefitClass.NAME, fld).first();
						if (rec == null)
							addError("import file row invalid employee benefit class (" + fld + "): " + ssn);
					}
					

				} else if (ci.equals(STREET1)) {

				} else if (ci.equals(STREET2)) {

				} else if (ci.equals(CITY)) {

				} else if (ci.equals(STATE)) {

				} else if (ci.equals(ZIP)) {

				} else if (ci.equals(CELL_PHONE)) {

				} else if (ci.equals(LOGIN_ID)) {

				} else if (ci.equals(PASSWORD)) {

				} else if (ci.equals(SCREEN_GROUP)) {
						if (fld.length() > 0) {
							String id = IDGenerator.expandKey(fld);
//							if (id.equals("00000-0000000000"))
//								addError("import file row unauthorized screen group (" + fld + "): " + ssn);
//							else 
								if (null == hsu.get(ScreenGroup.class, id))
								addError("import file row invalid screen group (" + fld + "): " + ssn);
						}
				} else if (ci.equals(SECURITY_GROUP)) {
						if (fld.length() > 0) {
							String id = IDGenerator.expandKey(fld);
							if (id.equals("00000-0000000004"))
								addError("import file row unauthorized security group (" + fld + "): " + ssn);
							else if (null == hsu.get(SecurityGroup.class, IDGenerator.expandKey(fld)))
								addError("import file row invalid security group (" + fld + "): " + ssn);		
						}
				} else if (ci.equals(EMPLOYMENT_STATUS)) {
					
					if (fld.length() > 0) {
						HrEmployeeStatus rec = hsu.createCriteria(HrEmployeeStatus.class).eq(HrEmployeeStatus.NAME, fld).first();
						if (rec == null)
							addError("import file row invalid employee status (" + fld + "): " + ssn);
					} 
//					else
//							addError("import file row missing employee status: " + ssn);

				} else if (ci.equals(EMPLOYMENT_STATUS_DATE)) {
//					if (fld.length() == 0)
//						addError("import file row missing employment status date: " + ssn);
//					else 
					if (fld.length() != 0) {
						int dt = DateUtils.getDate(fld);
						if (dt < 19500101  ||  dt > today)
							addError("import file row invalid employment status date (" + fld + "): " + ssn);
					}
				} else if (ci.equals(PAY_TYPE)) {
					
//					if (fld.length() == 0)
//						addError("import file row missing payment type: " + ssn);
//					else 
					if (fld.length() != 0) {
						char pt = Character.toUpperCase(fld.charAt(0));
						if (pt != 'H'  &&  pt != 'S')		
							addError("import file row invalid payment type (" + fld + "): " + ssn);
						else if (pt == 'H'  &&  !hourlyReported  &&  null == hsu.createCriteria(WageType.class).eq(WageType.PERIOD_TYPE, WageType.PERIOD_HOURLY)
								.le(WageType.FIRSTACTIVEDATE, today)
								.geOrEq(WageType.LASTACTIVEDATE, today, 0)
								.first()) {
							addError("system setup does not have a wage type for hourly");
							hourlyReported = true;
						} else if (pt == 'S'  &&  !salaryReported  &&  null == hsu.createCriteria(WageType.class).eq(WageType.PERIOD_TYPE, WageType.PERIOD_SALARY)
								.le(WageType.FIRSTACTIVEDATE, today)
								.geOrEq(WageType.LASTACTIVEDATE, today, 0)
								.first()) {
							addError("system setup does not have a wage type for salary");	
							salaryReported = true;
						}
					}

				} else if (ci.equals(PAY_AMOUNT)) {
					
//					if (fld.length() == 0)
//						addError("import file row missing payment amount: " + ssn);
//					else 
					if (fld.length() != 0) {
						double amt;
						try {
							amt = Double.valueOf(fixNum(fld));
						} catch (Exception e) {
							addError("import file row invalid payment amount (" + fld + "): " + ssn);
							amt = 1.0;
						}
						if (amt < .01  ||  amt > 1000000.0)						
							addError("import file row invalid payment amount (" + fld + "): " + ssn);					
					}

				} else if (ci.equals(PAY_FREQUENCY)) {
					
//					if (fld.length() == 0)
//						addError("import file row missing payment frequency: " + ssn);
//					else 
					if (fld.length() != 0) {
						int amt;
						try {
							amt = Integer.valueOf(fld);
						} catch (Exception e) {
							addError("import file row invalid payment frequency (" + fld + "): " + ssn);
							amt = 1;
						}
						if (amt < 1  ||  amt > 365)						
							addError("import file row invalid payment frequency (" + fld + "): " + ssn);					
					}

				} else if (ci.equals(HOURS_PER_WEEK)) {
					if (fld.length() > 1)
						try {
							short s = Short.valueOf(fld);
							if (s < 0  ||  s > 80)
								throw new Exception();
						} catch (Exception e) {
							addError("import file row invalid hours per week (" + fld + "): " + ssn);																										
						}
							
				} else if (ci.equals(POSITION)) {
//					if (fld.length() == 0)
//						addError("import file row missing employee position: " + ssn);
//					else 
					if (fld.length() != 0) {
						HrPosition rec = hsu.createCriteria(HrPosition.class).eq(BenefitClass.NAME, fld)
								.le(HrPosition.FIRSTACTIVEDATE, today)
								.geOrEq(HrPosition.LASTACTIVEDATE, today, 0)
								.first();
						if (rec == null)
							addError("import file row invalid employee position (" + fld + "): " + ssn);				
					}
				} else if (ci.equals(HIRE_DATE)) {
					// then we are guaranteed to have HIRE_STATUS column too
					// Do the columns contain data?
					boolean i1 = columnHasData(HIRE_DATE);
					boolean i2 = columnHasData(HIRE_STATUS);
					if (i1 && !i2  ||  !i1  &&  i2)
						addError("import file row missing hire date or hire status: " + ssn);
					else if (i1) {
						String status = getString(HIRE_STATUS);
						String date = getString(HIRE_DATE);
						HrEmployeeStatus rec = hsu.createCriteria(HrEmployeeStatus.class).eq(HrEmployeeStatus.NAME, status).first();
						if (rec == null)
							addError("import file row invalid employee hire status (" + status + "): " + ssn);
						
						int dt = DateUtils.getDate(date);
						if (dt < 19500101  ||  dt > today)
							addError("import file row invalid employment hire date (" + date + "): " + ssn);
					}
				} else if (ci.equals(JOB_TITLE)) {
					
				} else if (ci.equals(SHIFT_1) ||  ci.equals(SHIFT_2) ||  ci.equals(SHIFT_3) ||  ci.equals(SHIFT_4)) {
					if (!fld.isEmpty()) {
						List<Project> projects = hsu.createCriteria(Project.class).eq(Project.REFERENCE, fld).list();
						if (projects.isEmpty())
							addError("import file row invalid project / shift code (" + fld + "): " + ssn);
						else if (projects.size() > 1)
							addError("import file row project / shift code is used by more than one project (" + fld + "): " + ssn);

					}
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
		String [] shiftCodes = new String[4];
		if (mode == FULL_MODE)
			includeList = new HashSet<>();
		
		df.moveToStart();
		try {
			df.nextLine();  // skip header line
		} catch (Exception ex) {
			Logger.getLogger(EmployeeImport.class.getName()).log(Level.SEVERE, null, ex);
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
			
			int nShiftCodes = 0;  // number of shift codes read in
			
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
					} else if (ci.equals(DEPARTMENT)) {

						// this field was pre-validated

						if (fld.length() > 0) {
							OrgGroup og = hsu.createCriteria(OrgGroup.class).eq(OrgGroup.NAME, fld).first();
							be.assignToOrgGroup(og.getOrgGroupId(), existingEmployee);
						}						
						
					} else if (ci.equals(LOCATION)) {

						// this field was pre-validated

						if (fld.length() > 0) {
							OrgGroup og = hsu.createCriteria(OrgGroup.class).eq(OrgGroup.NAME, fld).first();
							be.assignToOrgGroup(og.getOrgGroupId(), existingEmployee);
						}						
						
					} else if (ci.equals(BENEFIT_CLASS)) {

						// this field was pre-validated

						if (fld.length() > 0) {
							BenefitClass rec = hsu.createCriteria(BenefitClass.class).eq(BenefitClass.NAME, fld).first();
							be.setBenefitClassId(rec.getBenefitClassId());
						}						
						
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
					} else if (ci.equals(LOGIN_ID)) {
						if (fld.length() > 0)
							be.setUserLogin(login_id=fld);
					} else if (ci.equals(PASSWORD)) {
						if (fld.length() > 0)
							be.setUserPassword(password=fld, true);
					} else if (ci.equals(SCREEN_GROUP)) {
						if (fld.length() > 0)
							be.setScreenGroupId(screen_group=IDGenerator.expandKey(fld));
					} else if (ci.equals(SECURITY_GROUP)) {
						if (fld.length() > 0)
							be.setSecurityGroupId(security_group=IDGenerator.expandKey(fld));
					} else if (ci.equals(EMPLOYMENT_STATUS)) {
										
						if (fld.length() > 0)
							employeeStatus = hsu.createCriteria(HrEmployeeStatus.class).eq(HrEmployeeStatus.NAME, fld).first();

					} else if (ci.equals(EMPLOYMENT_STATUS_DATE)) {

						// this field was pre-validated

						if (fld.length() > 0)
							employeeStatusDate = DateUtils.getDate(fld);
						
					} else if (ci.equals(PAY_TYPE)) {
						if (fld.length() > 0)
							payType = Character.toUpperCase(fld.charAt(0));
					} else if (ci.equals(PAY_AMOUNT)) {
						if (fld.length() > 0)
							payAmount = Double.parseDouble(fixNum(fld));
					} else if (ci.equals(PAY_FREQUENCY)) {
						if (fld.length() > 0)
							be.setPayPeriodsPerYear(Integer.valueOf(fld));
					} else if (ci.equals(HOURS_PER_WEEK)) {
						if (fld.length() > 1)
							be.setHoursPerWeek(Short.valueOf(fld));
					} else if (ci.equals(POSITION)) {
						if (fld.length() > 0)
							position = hsu.createCriteria(HrPosition.class).eq(BenefitClass.NAME, fld)
															.le(HrPosition.FIRSTACTIVEDATE, today)
															.geOrEq(HrPosition.LASTACTIVEDATE, today, 0)
															.first();
					} else if (ci.equals(HIRE_DATE)) {
						if (fld.length() > 0) {
							// we know from previous checks that at this point HIRE_DATE & HIRE_STATUS both exist and both contain valid data
							hireDate = DateUtils.getDate(fld);
							hireStatus = hsu.createCriteria(HrEmployeeStatus.class).eq(HrEmployeeStatus.NAME, getString(HIRE_STATUS)).first();
						}
					} else if (ci.equals(JOB_TITLE)) {
						if (fld.length() > 0)
							be.setJobTitle(fld);
					} else if (ci.equals(WORKER_ID)) {
						if (fld.length() > 0)
							be.setExtRef(fld);
					} else if (ci.equals(SHIFT_1)  ||  ci.equals(SHIFT_2)  ||  ci.equals(SHIFT_3)  ||  ci.equals(SHIFT_4)) {
						if (!fld.isEmpty()  &&  nShiftCodes < 4)
							shiftCodes[nShiftCodes++] = fld;	
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
			
			if (shiftsExist)
				adjustShifts(be, nShiftCodes, shiftCodes);

			
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
	
	private void adjustShifts(BEmployee be, int nShiftCodes, String [] shiftCodes) {
//		List<Project> projects = hsu.createCriteria(Project.class).eq(Project.REFERENCE, fld).list();

		if (true)     //  XXYY
			throw new ArahantException("XXYY code");  // XXYY

		/*  XXYY
		
		// Projects we end up with
		LinkedList<String> shiftCodeList = new LinkedList<>();
		for (int i=0 ; i < nShiftCodes ; i++)
			shiftCodeList.addFirst(shiftCodes[i]);
		List<Project> projectsEnd = hsu.createCriteria(Project.class).in(Project.REFERENCE, shiftCodeList).list();
		
		// projects employee is currently assigned to
		@SuppressWarnings("unchecked")
		List<Project> projectsBeg = (List) hsu.createCriteria(ProjectEmployeeJoin.class).eq(ProjectEmployeeJoin.PERSON, be.getPerson())
				.selectFields(ProjectEmployeeJoin.PROJECT).list();
		
		for (Project b : projectsBeg) {
			boolean found = false;
			for (Project e : projectsEnd)
				if (b.getProjectId().equals(e.getProjectId())) {
					found = true;
					break;
				}
			if (!found) {
				BProject bp = new BProject(b);
				bp.removeAssignment(be.getPersonId());
				bp.update();
			}
		}
		
		for (Project e : projectsEnd) {
			boolean found = false;
			for (Project b : projectsBeg)
				if (b.getProjectId().equals(e.getProjectId())) {
					found = true;
					break;
				}
			if (!found) {
				e.setBillable('Y');
				BProject bp = new BProject(e);
				bp.assignPerson(be.getPersonId(), 10, false);
				bp.update();
			}
		}
		 */
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
			String reportFile = new EmployeeImport("/Users/blake/Arahant/Clients/Five Points/Employee Import Test Files/File 002.csv", APPEND_MODE).parse();
			System.out.println("output file = " + reportFile);
		} catch (Exception ex) {
			Logger.getLogger(EmployeeImport.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
