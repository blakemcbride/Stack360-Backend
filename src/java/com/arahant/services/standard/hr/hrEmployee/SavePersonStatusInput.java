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

import com.arahant.annotation.Validation;
import com.arahant.beans.Person;
import com.arahant.business.BEmployee;
import com.arahant.business.BPerson;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitInputBase;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;

import java.util.Date;

public class SavePersonStatusInput extends TransmitInputBase {

	@Validation(required = true)
    private String personId;
	@Validation(table = "person", column = "job_title", required = false)
    private String jobTitle;
	@Validation(required = false)
    private String eeoCategoryId;
    @Validation(required = false)
    private String eeoRaceId;
	@Validation(table = "person", column = "citizenship", required = false)
    private String citizenship;
	@Validation(required = false)
    private String benefitClassId;
	@Validation(table = "person", column = "visa", required = false)
    private String visa;
    @Validation(table = "person", column = "visa_status_date", type = "date", required = false)
    private int visaStatusDate;
    @Validation(table = "person", column = "visa_exp_date", type = "date", required = false)
    private int visaExpirationDate;
	@Validation(required = false)
    private double billingRate;

    private boolean i9Completed = false;
	private boolean i9Part1 = false;
	private boolean i9Part2 = false;
	private String i9p1confirmation;
	private String i9p2confirmation;

	@Validation(table="employee", column="workers_comp_code", required=false)
	private String workersCompCode;
    @Validation(required = true)
    private String medicare;
    @Validation(required = false)
	private boolean hrAdmin;
    @Validation(table="person", column="hic_number", required = false)
    private String hicNumber;
	@Validation(required = false)
	private short hoursPerWeek;

	public String getHicNumber() {
		return hicNumber;
	}

	public void setHicNumber(String hicNumber) {
		this.hicNumber = hicNumber;
	}

	public boolean getHrAdmin() {
		return hrAdmin;
	}

	public void setHrAdmin(boolean hrAdmin) {
		this.hrAdmin = hrAdmin;
	}

	private static boolean isPresent(String s) {
		return s != null && !s.isEmpty();
	}

	private static boolean hasChanged(String a, String b) {
		if (a != null)
			a = a.trim();
		if (b != null)
			b = b.trim();
		boolean ap = isPresent(a);
		boolean bp = isPresent(b);
		return ap && !bp ||
				!ap && bp ||
				ap && !a.equals(b);
	}

	void makeEmployee(final BEmployee emp) throws ArahantException
	{
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		Person p = emp.getPerson();
		emp.setJobTitle(jobTitle);
		emp.setEEOCategoryId(eeoCategoryId);
        emp.setEEORaceId(eeoRaceId);
        //emp.setBillingRate((float) billingRate);  //  employees no longer have direct billing rates
        emp.setCitizenship(citizenship);

        if (emp.getI9Part1() != i9Part1 ||
				hasChanged(i9p1confirmation, emp.getI9p1Confirmation())) {
        	emp.setI9Part1(i9Part1);
        	emp.setI9p1Confirmation(i9p1confirmation);
        	emp.setI9p1When(new Date());
        	emp.setI9p1Person(hsu.getCurrentPerson().getPersonId());
		}

		if (emp.getI9Part2() != i9Part2 ||
				hasChanged(i9p2confirmation, emp.getI9p2Confirmation())) {
			emp.setI9Part2(i9Part2);
			emp.setI9p2Confirmation(i9p2confirmation);
			emp.setI9p2When(new Date());
			emp.setI9p2Person(hsu.getCurrentPerson().getPersonId());
		}

		emp.setVisa(visa);
        emp.setVisaStatusDate(visaStatusDate);
        emp.setVisaExpirationDate(visaExpirationDate);
		emp.setBenefitClassId(benefitClassId);
		emp.setWorkersCompCode(workersCompCode);
		emp.setMedicare(medicare.charAt(0));
		emp.setHrAdmin(hrAdmin?'Y':'N');
		emp.setHicNumber(hicNumber);
		emp.setHoursPerWeek(hoursPerWeek);
	}

	void makePerson(BPerson per) throws ArahantException
	{
		per.setJobTitle(jobTitle);
		per.setHicNumber(hicNumber);
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

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
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
	
	public String getWorkersCompCode() {
		return workersCompCode;
	}

	public void setWorkersCompCode(String workersCompCode) {
		this.workersCompCode = workersCompCode;
	}

	public String getMedicare() {
		return medicare;
	}

	public void setMedicare(String medicare) {
		this.medicare = medicare;
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
}

	
