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
package com.arahant.services.standard.misc.employerHomePage;

import com.arahant.beans.Employee;
import com.arahant.business.BEmployee;
import com.arahant.business.BHREmplDependent;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.ArahantSession;


/**
 * 
 *
 *
 */
public class LoadDependentReturn extends TransmitReturnBase {

	void setData(final BHREmplDependent bc) throws ArahantException
	{
		inactiveDate=bc.getInactiveDate();
		dob=bc.getDob();
		firstName=bc.getFirstName();
		lastName=bc.getLastName();
		middleName=bc.getMiddleName();
		relationship=bc.getOtherRelationshipDescription();
		relationshipType=bc.getRelationshipType();
		sex=bc.getSex();
		ssn=bc.getSsn();
		student=bc.getStudent();
		handicap=bc.getHandicap();
		studentTermType=bc.getStudentTermType();
		
		final Employee emp=ArahantSession.getHSU().get(Employee.class, bc.getPersonId());
		if (emp!=null)
		{
			isEmployee=true;
			employeeStatus=new BEmployee(emp).getLastStatusName();
		}
		else
		{
			isEmployee=false;
			employeeStatus="";
		}
	}
	
	private int dob;
	private String firstName;
	private String lastName;
	private String middleName;
	private String relationship;
	private String relationshipType;
	private String sex;
	private String ssn;
	private boolean student;
	private boolean handicap;
	private int inactiveDate;
	private String employeeStatus;
	private boolean isEmployee;
	private String studentTermType;

	public String getStudentTermType() {
		return studentTermType;
	}

	public void setStudentTermType(String studentTermType) {
		this.studentTermType = studentTermType;
	}


	
	/**
	 * @return Returns the inactiveDate.
	 */
	public int getInactiveDate() {
		return inactiveDate;
	}
	/**
	 * @param inactiveDate The inactiveDate to set.
	 */
	public void setInactiveDate(final int inactiveDate) {
		this.inactiveDate = inactiveDate;
	}
	/**
	 * @return Returns the handicap.
	 */
	public boolean isHandicap() {
		return handicap;
	}
	/**
	 * @param handicap The handicap to set.
	 */
	public void setHandicap(final boolean handicap) {
		this.handicap = handicap;
	}
	/**
	 * @return Returns the student.
	 */
	public boolean isStudent() {
		return student;
	}
	/**
	 * @param student The student to set.
	 */
	public void setStudent(final boolean student) {
		this.student = student;
	}
	public int getDob()
	{
		return dob;
	}
	public void setDob(final int dob)
	{
		this.dob=dob;
	}
	public String getFirstName()
	{
		return firstName;
	}
	public void setFirstName(final String firstName)
	{
		this.firstName=firstName;
	}
	public String getLastName()
	{
		return lastName;
	}
	public void setLastName(final String lastName)
	{
		this.lastName=lastName;
	}
	public String getMiddleName()
	{
		return middleName;
	}
	public void setMiddleName(final String middleName)
	{
		this.middleName=middleName;
	}
	public String getRelationship()
	{
		return relationship;
	}
	public void setRelationship(final String relationship)
	{
		this.relationship=relationship;
	}
	public String getRelationshipType()
	{
		return relationshipType;
	}
	public void setRelationshipType(final String relationshipType)
	{
		this.relationshipType=relationshipType;
	}
	public String getSex()
	{
		return sex;
	}
	public void setSex(final String sex)
	{
		this.sex=sex;
	}
	public String getSsn()
	{
		return ssn;
	}
	public void setSsn(final String ssn)
	{
		this.ssn=ssn;
	}
	/**
	 * @return Returns the dependentEmployeeStatus.
	 */
	public String getEmployeeStatus() {
		return employeeStatus;
	}
	/**
	 * @param dependentEmployeeStatus The dependentEmployeeStatus to set.
	 */
	public void setEmployeeStatus(final String dependentEmployeeStatus) {
		this.employeeStatus = dependentEmployeeStatus;
	}
	public boolean getIsEmployee() {
		return isEmployee;
	}
	public void setIsEmployee(boolean isEmployee) {
		this.isEmployee = isEmployee;
	}

}

	
