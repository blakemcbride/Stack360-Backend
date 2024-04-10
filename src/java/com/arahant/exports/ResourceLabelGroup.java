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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arahant.exports;

import com.arahant.beans.Employee;
import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitCategory;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.HrEmplDependent;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRBenefitCategory;
import com.arahant.business.BHRBenefitConfig;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BHREmplDependent;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileWriter;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResourceLabelGroup {

	final HibernateSessionUtil hsu = ArahantSession.getHSU();
	String outputLocation;
	DelimitedFileWriter writer;

	public ResourceLabelGroup() {
		createFile("ResourceLabelGroup_PrincipalLife.csv");
		writeHeader();
		List<Employee> emps = hsu.createCriteria(Employee.class).list();

		//get the maximum dependent counts an employee may have
		//int maxDependents = hsu.getConnection().
		String[] empids = new String[2];
		empids[0] = "00001-0000000105";
		empids[1] = "00001-0000000120";
//            BEmployee emp = new BEmployee(empids[1]);
//                writeEmployee(emp);
//                writeElection(emp);
//                writeSpouse(emp);
//                writeDepedent(emp);


		for (Employee e : emps) {
			BEmployee emp = new BEmployee(e);
			if (emp.isActive() == 0) {
				writeEmployee(emp);
				writeElection(emp);
				writeSpouse(emp);
				writeDepedent(emp);
			}
		}

		writer.close();
	}

	private int getMaxDependentCount() {

		org.hibernate.Query q = hsu.createQuery("select count(*) from HrEmplDependent where relationship_type = 'C' and record_type ='R' group by employee_id order by count(*) desc limit 1").setMaxResults(1);
		List list = q.list();
		Long x = (Long)list.get(0);
		return x.intValue();

	}

	private String convertDate(int date) {
		String month = DateUtils.month(date) + "";
		String day = DateUtils.day(date) + "";
		String year = DateUtils.year(date) + "";
		if (month.length() == 1) {
			month = "0" + month;
		}
		if (day.length() == 1) {
			day = "0" + day;
		}
		return month + day + year;
	}

	private void writeEmployee(BEmployee employee) {
		try {
			writer.writeField(employee.getNameLFM());
			writer.writeField(employee.getSsn());
			writer.writeField(employee.getHomeAddress().toString().replace('\n', ' '));
			writer.writeField(convertDate(employee.getDob()));
			writer.writeField(employee.getSex());
			writer.writeField(convertDate(employee.getHireDate()));
			writer.writeField(employee.getExpectedHoursPerPayPeriod());
			writer.writeField(employee.getJobTitle());
			writer.writeField(employee.getCurrentSalary());
			writer.writeField("");
			writer.writeField("");
			writer.writeField(employee.getCompany().getZip());
			writer.writeField("");
		} catch (Exception ex) {
			Logger.getLogger(ResourceLabelGroup.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void writeSpouse(BEmployee employee) {
		try {

			String depName = "";
			String depGender = "";
			String depStatus = "";
			int depDOB = 0;
			String depSSN = "";
			BHREmplDependent emps[] = null;
			if (employee.getDependents() == null) {
				writer.writeField("");
				writer.writeField("");
				writer.writeField("");
				writer.writeField("");
				writer.writeField("");
				return;
			}
			emps = employee.getDependents();
			for (BHREmplDependent dependents : emps) {
				if (dependents.getRelationship() == (HrEmplDependent.TYPE_SPOUSE)) {
					depName = dependents.getNameLFM();
					depGender = dependents.getSex();
					depStatus = "Spouse";
					depDOB = dependents.getDob();
					depSSN = dependents.getSsn();
				}
			}
			//write regardless if we have data or not

			writer.writeField(depName);
			writer.writeField(depGender);
			writer.writeField(depStatus);
			if (depDOB == 0) {
				writer.writeField("");
			} else {
				writer.writeField(convertDate(depDOB));
			}
			writer.writeField(depSSN);
		} catch (Exception ex) {
			Logger.getLogger(ResourceLabelGroup.class.getName()).log(Level.SEVERE, null, ex);
			System.out.println("NO Spouse found for Emp " + employee.getNameFML());

		}

	}

	private void writeDepedent(BEmployee employee) {
		//if there are no dependents then don't worry about it
		//no need to write anything out
		if (employee.getDependents() == null) {
			try {
				writer.endRecord();
				return;
			} catch (IOException ex) {
				Logger.getLogger(ResourceLabelGroup.class.getName()).log(Level.SEVERE, null, ex);
			} catch (Exception ex) {
				Logger.getLogger(ResourceLabelGroup.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		BHREmplDependent[] emps = employee.getDependents();
		String depName = "";
		String depGender = "";
		String depStatus = "";
		int depDOB = 0;
		int counter = 0;
		for (BHREmplDependent e : emps) {
			if (e.getRelationship() != (HrEmplDependent.TYPE_SPOUSE) && (e.isCurrentlyActive())) {
				try {

					depName = e.getNameLFM();
					depGender = e.getSex();
					depStatus = e.getStatus();
					depDOB = e.getDob();
					if (e.getRelationship() == HrEmplDependent.TYPE_CHILD) {
						depStatus = "Child";
					} else {
						depStatus = "Domestic Partner";
					}
					writer.writeField(depName);
					writer.writeField(depGender);
					writer.writeField(depStatus);
					writer.writeField(convertDate(depDOB));
					counter++;
				} catch (Exception ex) {
					Logger.getLogger(ResourceLabelGroup.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
		//we need to write 3 dependent info
		//write blanks for what we don't have
		try {
//            for (int i = 3; i < 3; i--) {
//                writer.writeField("");
//                writer.writeField("");
//                writer.writeField("");
//                writer.writeField("");
//            }
			writer.endRecord();
		} catch (Exception ex) {
			Logger.getLogger(ResourceLabelGroup.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private double getSpouseVTLAmount(BEmployee employee) {
		double amount = 0;

		HibernateCriteriaUtil<HrBenefitJoin> hcu = hsu.createCriteria(HrBenefitJoin.class).dateInside(HrBenefitJoin.POLICY_START_DATE, HrBenefitJoin.POLICY_END_DATE, DateUtils.now()).dateInside(HrBenefitJoin.COVERAGE_START_DATE, HrBenefitJoin.COVERAGE_END_DATE, DateUtils.now()).eq(HrBenefitJoin.PAYING_PERSON, employee.getEmployee());

		hcu.joinTo(HrBenefitJoin.RELATIONSHIP).eq(HrEmplDependent.RELATIONSHIP_TYPE, 'S');

		hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.BENEFIT_CONFIG_ID, "00001-0000000045");
		//.joinTo(HrBenefit.BENEFIT_CATEGORY)
		//.eq(HrBenefitCategory.TYPE, HrBenefitCategory.LIFE);

		if (hcu.first() != null) {
			//System.out.println("Amount is " + hcu.first().getAmountCovered());
			return hcu.first().getAmountCovered();
		}
		return amount;
	}

	private boolean getChildVTL(BEmployee employee) {

		HibernateCriteriaUtil<HrBenefitJoin> hcu = hsu.createCriteria(HrBenefitJoin.class).dateInside(HrBenefitJoin.POLICY_START_DATE, HrBenefitJoin.POLICY_END_DATE, DateUtils.now()).dateInside(HrBenefitJoin.COVERAGE_START_DATE, HrBenefitJoin.COVERAGE_END_DATE, DateUtils.now()).eq(HrBenefitJoin.PAYING_PERSON, employee.getEmployee());

		hcu.joinTo(HrBenefitJoin.RELATIONSHIP).eq(HrEmplDependent.RELATIONSHIP_TYPE, 'C');

		hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.TYPE, HrBenefitCategory.LIFE);

		if (hcu.first() != null) {
			return true;
		}
		return false;
	}

	private String determineCoverageLevel(BHRBenefitConfig config,BEmployee employee,BHRBenefitJoin bj) {
		String coverageLevel = "";

		//Employee Only
		try {
			if (config.getCoversEmployee()) {
				coverageLevel = "EE";
			}
			// Employee + Spouse
			if (config.getCoversEmployee() && (config.getCoversEmployeeSpouse() || config.getSpouseNonEmployee())) {
				coverageLevel = "ES";
			}
			// Employee + 1
			if (config.getCoversEmployee() && (config.getCoversEmployeeSpouseOrChildren() || config.getSpouseNonEmpOrChildren()) && config.getMaxChildren() == 1) {
				coverageLevel = "EC";
				if (employee.hasSpouse()) coverageLevel = "ES";
				//let's confirm that it is ES or EC
				if (bj.getDependentBenefitJoins() !=null){					
					List<HrBenefitJoin> depBen = bj.getDependentBenefitJoins();
					for (HrBenefitJoin join : depBen){
						if (join.getRelationship().getRelationshipType() == 'C'){
							coverageLevel = "EC";
						} else if (join.getRelationship().getRelationshipType() == 'S'){
							coverageLevel = "ES";
						}
						//System.out.println("Cover age is " + coverageLevel);
						//System.out.println("Dep type " + join.getRelationship().getRelationshipType() + "  " + join.getCoveredPerson().getFname() +  " Paying[== " + employee.getNameLFM());
					}
				}

			}
			// Family
			if (config.getCoversEmployee() && (config.getCoversEmployeeSpouseOrChildren() || config.getSpouseNonEmpOrChildren()) && config.getMaxChildren() == 0) {
				coverageLevel = "Family";
			}
		} catch (Exception e) {
			return coverageLevel;
		}
		// Employee + 1
//        if (config.getCoversEmployee() && (config.getCoversEmployeeSpouseOrChildren() || config.getSpouseNonEmpOrChildren()) && config.getMaxChildren() == 1) {
//            coverageLevel = "Emp+1";
//        }
		return coverageLevel;
	}

	private void writeElection(BEmployee employee) {
		boolean joinLife = false;
		String dentalCoverage = "";
		double lifeAmount = 0;
		boolean joinShortTerm = false;
		boolean joinLongTerm = false;

		BHRBenefitJoin[] benefits = employee.getBenefitJoins();

		BHRBenefitCategory bc;
		for (BHRBenefitJoin bj : benefits) {
			if (!bj.isBenefitDecline() && bj.getBenefitApproved()) {
				//System.out.println("BC is " + bj.getBenefitCategoryId());
				bc = new BHRBenefitCategory(bj.getBenefitCategoryId());
				//System.out.println("Ben " + bc.getTypeId() + " " + bc.getTypeName());
				if (bc.getTypeId() == HrBenefitCategory.DENTAL) {
					//if (bj.getBenefitId().length()>0)
					dentalCoverage = determineCoverageLevel(bj.getBenefitConfig(),employee,bj);

					//System.out.println("Dental for " + employee.getPersonId() +  " " + dentalCoverage);
				} else if (bc.getTypeId() == HrBenefitCategory.LIFE) {
					joinLife = true;
					if (bj.getBenefitConfig().getBenefitConfigId().equals("00001-0000000044")) {
						lifeAmount = bj.getAmountCovered();
						//System.out.println("LIFE amount for " + employee.getPersonId() + " = " + lifeAmount + " status = " + bj.getBenefitApproved());
					}

				} else if (bc.getTypeId() == HrBenefitCategory.LONG_TERM_CARE) {
					//if (bj.getBenefitId().length()==0)
					joinLongTerm = true;
				} else if (bc.getTypeId() == HrBenefitCategory.SHORT_TERM_CARE) {
					//if (bj.getBenefitId().length()==0)
					joinShortTerm = true;
				}
			}
			bc = null;
		}
		try {
			writer.writeField(joinLife ? "YES" : "NO");
			writer.writeField(lifeAmount);
			writer.writeField(getSpouseVTLAmount(employee));
			writer.writeField(getChildVTL(employee) ? "YES" : "NO");
			writer.writeField(joinLongTerm ? "YES" : "NO");
			writer.writeField(joinShortTerm ? "YES" : "NO");
			writer.writeField(dentalCoverage);
			writer.writeField(employee.getNameLFM());
		} catch (Exception ex) {
			Logger.getLogger(ResourceLabelGroup.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void writeHeader() {
		try {
			writer.writeField("EMPLOYEE NAME");
			writer.writeField("SOCIAL SECURITY NUMBER");
			writer.writeField("HOME ADDRESS");
			writer.writeField("DATE OF BIRTH");
			writer.writeField("SEX");
			writer.writeField("DATE OF HIRE");
			writer.writeField("HRS WORKED PER WK");
			writer.writeField("JOB TITLE");
			writer.writeField("SALARY");
			writer.writeField("MODE");
			writer.writeField("JOB CLASS");
			writer.writeField("WORK PLACE ZIP CODE");
			writer.writeField("OCC CODE");
			writer.writeField("LIFE");
			writer.writeField("EE VTL ELECTION AMT");
			writer.writeField("SP VTL ELECTION AMT");
			writer.writeField("CHILD VTL");
			writer.writeField("VLTD");
			writer.writeField("VSTD");
			writer.writeField("DENTAL");
			writer.writeField("EMPLOYEE NAME");
			writer.writeField("DEP NAME");
			writer.writeField("DEP GENDER");
			writer.writeField("DEP STATUS");
			writer.writeField("DEP. DATE(S) OF BIRTH");
			writer.writeField("SPOUSE SSN");
			//need to looop and create the max number of dependent header
			int count = getMaxDependentCount();
			for (int i = 0; i < count; i++) {
				writer.writeField("DEP NAME");
				writer.writeField("DEP GENDER");
				writer.writeField("DEP STATUS");
				writer.writeField("DEP. DATE(S) OF BIRTH");
			}
//			writer.writeField("DEP NAME");
//			writer.writeField("DEP GENDER");
//			writer.writeField("DEP STATUS");
//			writer.writeField("DEP. DATE(S) OF BIRTH");
//			writer.writeField("DEP NAME");
//			writer.writeField("DEP GENDER");
//			writer.writeField("DEP STATUS");
//			writer.writeField("DEP. DATE(S) OF BIRTH");
			writer.endRecord();

		} catch (Exception ex) {
			Logger.getLogger(ResourceLabelGroup.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private DelimitedFileWriter createFile(String filename) {

		String outputDirectory = "exportedCSVFiles";
		File csvFile;
		//create the directory to put the filled in PDF files
		File reportDir = new File(FileSystemUtils.getWorkingDirectory(), outputDirectory);
		if (!reportDir.exists()) {
			reportDir.mkdir();
		}

		this.outputLocation = reportDir.getAbsolutePath();

		csvFile = new File(reportDir + "/" + filename);
		try {
			csvFile.createNewFile();
			System.out.println("File is at " + csvFile.getAbsolutePath());
			try {
				writer = new DelimitedFileWriter(csvFile.getAbsolutePath(), false);
				return writer;
			} catch (IOException ex) {
				Logger.getLogger(CobraGuard.class.getName()).log(Level.SEVERE, null, ex);
			}
		} catch (IOException ex) {
			Logger.getLogger(CobraGuard.class.getName()).log(Level.SEVERE, null, ex);
		}

		return null;

	}

	public static void main(String[] args) {
		new ResourceLabelGroup();
	}
}
