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
package com.arahant.services.standard.hr.hrEmployee;

import com.arahant.business.BEmployee;
import com.arahant.business.BPerson;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import org.kissweb.DateTime;
import org.kissweb.DateUtils;

public class LoadPersonStatusReturn extends TransmitReturnBase {

	private String employeeStatusName;
	private int employeeStatusDate;
	private String positionName;
	private String jobTitle;
	private double wageAmount;
	private String wageTypeName;
	private String eeoCategoryId;
	private String eeoRaceId;
	private String benefitClassId;
	private String citizenship;
	private double billingRate;
	private boolean i9Completed;
	private boolean i9Part1;
	private boolean i9Part2;
	private String visa;
	private int visaStatusDate;
	private int visaExpirationDate;
	private String workersCompCode;
	private String medicare;
	private int hireDate;
	private boolean hrAdmin;
	private String hicNumber;
	private String inheritedBenefitClass;
	private short hoursPerWeek;
	private String i9p1confirmation;
	private String i9p2confirmation;
	private String i9p1when;
	private String i9p1who;
	private String i9p2when;
	private String i9p2who;

	public LoadPersonStatusReturn() {
	}

	private static String strip(String s) {
		return s == null ? "" : s.trim();
	}

	public void setData(BPerson person) {
		jobTitle = person.getJobTitle();

		if (person.isEmployee()) {
			BEmployee emp = new BEmployee(person);
			eeoCategoryId = emp.getEEOCategoryId();
			positionName = emp.getPositionName();
			eeoRaceId = emp.getEEORaceId();
			wageTypeName = emp.getWageTypeName();
			wageAmount = emp.getWageAmount();
			employeeStatusName = emp.getEmployeeStatusName();
			employeeStatusDate = emp.getEmployeeStatusDate();
			billingRate = 0;  // no single employee billing rate anymore
			citizenship = emp.getCitizenship();
			i9Completed = emp.getI9Part1() && emp.getI9Part2();
			i9Part1 = emp.getI9Part1();
			i9Part2 = emp.getI9Part2();
			i9p1confirmation = strip(person.getI9p1Confirmation());
			i9p2confirmation = strip(person.getI9p2Confirmation());
			visa = emp.getVisa();
			visaStatusDate = emp.getVisaStatusDate();
			visaExpirationDate = emp.getVisaExpirationDate();
			benefitClassId = emp.getBenefitClassId(false);
			workersCompCode = emp.getWorkersCompCode();
			medicare = emp.getMedicare() + "";
			hireDate = emp.getHireDate();
			hrAdmin = emp.getHrAdmin() == 'Y';
			hicNumber = emp.getHicNumber();
			inheritedBenefitClass = emp.getInheritedBenefitClassName();
			if (isEmpty(inheritedBenefitClass))
				inheritedBenefitClass = "(None)";
			hoursPerWeek = emp.getHoursPerWeek();
			i9p1when = DateTime.format(emp.getI9p1When());
			i9p2when = DateTime.format(emp.getI9p2When());

			HibernateSessionUtil hsu = ArahantSession.getHSU();
			if (!isEmpty(emp.getI9p1Person())) {
				try {
					BEmployee be = new BEmployee(emp.getI9p1Person());
					i9p1who = be.getNameLFM();
				} catch (Exception e) {
					if (hsu.currentlyArahantUser())
						i9p1who = "System Administrator";
					else
						throw e;
				}
			}
			if (!isEmpty(emp.getI9p2Person())) {
				try {
					BEmployee be = new BEmployee(emp.getI9p2Person());
					i9p2who = be.getNameLFM();
				} catch (Exception e) {
					if (hsu.currentlyArahantUser())
						i9p2who = "System Administrator";
					else
						throw e;
				}
			}
		} else {
			eeoCategoryId = "(n/a)";
			positionName = "(n/a)";
			eeoRaceId = "(n/a)";
			wageTypeName = "(n/a)";
			employeeStatusName = "(n/a)";
			employeeStatusDate = 0;
			billingRate = 0;
			citizenship = "(n/a)";
			i9Completed = false;
			i9Part1 = false;
			i9Part2 = false;
			visa = "(n/a)";
			visaStatusDate = 0;
			visaExpirationDate = 0;
			workersCompCode = "";
			medicare = "U";
			hireDate = 0;
			hrAdmin = false;
			hicNumber = person.getHicNumber();
			inheritedBenefitClass = "(N/A)";
			hoursPerWeek = 0;
		}
	}

	public String getInheritedBenefitClass() {
		return inheritedBenefitClass;
	}

	public void setInheritedBenefitClass(String inheritedBenefitClass) {
		this.inheritedBenefitClass = inheritedBenefitClass;
	}

	public String getHicNumber() {
		return hicNumber;
	}

	public void setHicNumber(String hicNumber) {
		this.hicNumber = hicNumber;
	}

	public boolean isHrAdmin() {
		return hrAdmin;
	}

	public void setHrAdmin(boolean hrAdmin) {
		this.hrAdmin = hrAdmin;
	}

	public String getMedicare() {
		return medicare;
	}

	public void setMedicare(String medicare) {
		this.medicare = medicare;
	}

	public String getBenefitClassId() {
		return benefitClassId;
	}

	public void setBenefitClassId(String benefitClassId) {
		this.benefitClassId = benefitClassId;
	}

	public double getBillingRate() {
		return billingRate;
	}

	public void setBillingRate(double billingRate) {
		this.billingRate = billingRate;
	}

	public String getCitizenship() {
		return citizenship;
	}

	public void setCitizenship(String citizenship) {
		this.citizenship = citizenship;
	}

	public String getEeoCategoryId() {
		return eeoCategoryId;
	}

	public void setEeoCategoryId(String eeoCategoryId) {
		this.eeoCategoryId = eeoCategoryId;
	}

	public String getEeoRaceId() {
		return eeoRaceId;
	}

	public void setEeoRaceId(String eeoRaceId) {
		this.eeoRaceId = eeoRaceId;
	}

	public int getEmployeeStatusDate() {
		return employeeStatusDate;
	}

	public void setEmployeeStatusDate(int employeeStatusDate) {
		this.employeeStatusDate = employeeStatusDate;
	}

	public String getEmployeeStatusName() {
		return employeeStatusName;
	}

	public void setEmployeeStatusName(String employeeStatusName) {
		this.employeeStatusName = employeeStatusName;
	}

	public boolean isI9Completed() {
		return i9Completed;
	}

	public void setI9Completed(boolean i9Completed) {
		this.i9Completed = i9Completed;
	}

	public boolean isI9Part1() {
		return i9Part1;
	}

	public void setI9Part1(boolean i9Part1) {
		this.i9Part1 = i9Part1;
	}

	public boolean isI9Part2() {
		return i9Part2;
	}

	public void setI9Part2(boolean i9Part2) {
		this.i9Part2 = i9Part2;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public String getVisa() {
		return visa;
	}

	public void setVisa(String visa) {
		this.visa = visa;
	}

	public int getVisaExpirationDate() {
		return visaExpirationDate;
	}

	public void setVisaExpirationDate(int visaExpirationDate) {
		this.visaExpirationDate = visaExpirationDate;
	}

	public int getVisaStatusDate() {
		return visaStatusDate;
	}

	public void setVisaStatusDate(int visaStatusDate) {
		this.visaStatusDate = visaStatusDate;
	}

	public double getWageAmount() {
		return wageAmount;
	}

	public void setWageAmount(double wageAmount) {
		this.wageAmount = wageAmount;
	}

	public String getWageTypeName() {
		return wageTypeName;
	}

	public void setWageTypeName(String wageTypeName) {
		this.wageTypeName = wageTypeName;
	}

	public String getWorkersCompCode() {
		return workersCompCode;
	}

	public void setWorkersCompCode(String workersCompCode) {
		this.workersCompCode = workersCompCode;
	}

	public int getHireDate() {
		return hireDate;
	}

	public void setHireDate(int hireDate) {
		this.hireDate = hireDate;
	}

	public short getHoursPerWeek() {
		return hoursPerWeek;
	}

	public void setHoursPerWeek(short hoursPerWeek) {
		this.hoursPerWeek = hoursPerWeek;
	}

	public String getI9p1confirmation() {
		return i9p1confirmation;
	}

	public void setI9p1confirmation(String i9p1confirmation) {
		this.i9p1confirmation = i9p1confirmation;
	}

	public String getI9p2confirmation() {
		return i9p2confirmation;
	}

	public void setI9p2confirmation(String i9p2confirmation) {
		this.i9p2confirmation = i9p2confirmation;
	}

	public String getI9p1when() {
		return i9p1when;
	}

	public void setI9p1when(String i9p1when) {
		this.i9p1when = i9p1when;
	}

	public String getI9p1who() {
		return i9p1who;
	}

	public void setI9p1who(String i9p1who) {
		this.i9p1who = i9p1who;
	}

	public String getI9p2when() {
		return i9p2when;
	}

	public void setI9p2when(String i9p2when) {
		this.i9p2when = i9p2when;
	}

	public String getI9p2who() {
		return i9p2who;
	}

	public void setI9p2who(String i9p2who) {
		this.i9p2who = i9p2who;
	}
}
