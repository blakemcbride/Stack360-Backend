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
 * Created on Jun 11, 2007
 * 
 */
package com.arahant.services.standard.misc.agencyHomePage;
import com.arahant.annotation.Validation;
import com.arahant.business.BHREmplDependent;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Jun 11, 2007
 *
 */
public class NewDependentInput extends TransmitInputBase  {

	@Validation (table="hr_empl_dependent",column="employee_id",required=true)
	private String employeeId;
	@Validation (table="person",column="fname",required=true)
	private String firstName;
	@Validation (table="person",column="lname",required=true)
	private String lastName;
	@Validation (table="person",column="mname",required=false)
	private String middleName;
	@Validation (min=1,max=1,required=true)
	private String sex;
	@Validation (table="hr_empl_dependent",column="ssn",required=true)
	private String ssn;
	@Validation (table="hr_empl_dependent",column="relationship",required=false)
	private String relationship;
	@Validation (type="date",required=true)
	private int dob;
	@Validation (required=true)
	private boolean handicap;
	@Validation (required=true)
	private boolean student;
	@Validation (table="hr_empl_dependent",column="relationshipType",required=true)
	private String relationshipType;
	@Validation (table="hr_empl_dependent",column="dependent_id",required=false)
	private String personId;
	@Validation (type="date",required=false)
	private int inactiveDate;
	@Validation (required=true)
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
	 * @return Returns the dependentEmployeeId.
	 */
	public String getPersonId() {
		return personId;
	}

	/**
	 * @param dependentEmployeeId The dependentEmployeeId to set.
	 */
	public void setPersonId(final String dependentEmployeeId) {
		this.personId = dependentEmployeeId;
	}

	/**
	 * @return Returns the dob.
	 */
	public int getDob() {
		return dob;
	}

	/**
	 * @param dob The dob to set.
	 */
	public void setDob(final int dob) {
		this.dob = dob;
	}

	/**
	 * @return Returns the employeeId.
	 */
	public String getEmployeeId() {
		return employeeId;
	}

	/**
	 * @param employeeId The employeeId to set.
	 */
	public void setEmployeeId(final String employeeId) {
		this.employeeId = employeeId;
	}

	/**
	 * @return Returns the firstName.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName The firstName to set.
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return Returns the lastName.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName The lastName to set.
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return Returns the middleName.
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName The middleName to set.
	 */
	public void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return Returns the relationship.
	 */
	public String getRelationship() {
		return relationship;
	}

	/**
	 * @param relationship The relationship to set.
	 */
	public void setRelationship(final String relationship) {
		this.relationship = relationship;
	}

	/**
	 * @return Returns the sex.
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @param sex The sex to set.
	 */
	public void setSex(final String sex) {
		this.sex = sex;
	}

	/**
	 * @return Returns the ssn.
	 */
	public String getSsn() {
		return ssn;
	}

	/**
	 * @param ssn The ssn to set.
	 */
	public void setSsn(final String ssn) {
		this.ssn = ssn;
	}

	/**
	 * @param d
	 * @throws ArahantException 
	 */
	void setData(final BHREmplDependent d) throws ArahantException {
		d.setSex(sex);
		d.setMiddleName(middleName);
		d.setEmployeeId(employeeId);
		d.setRelationship(relationship);
		d.setFirstName(firstName);
		d.setLastName(lastName);
		d.setSsn(ssn);
		d.setDob(dob);
		d.setRelationshipType(relationshipType);
		d.setHandicap(handicap);
		d.setStudent(student);
		d.setInactiveDate(inactiveDate);
		d.setStudentTermType(studentTermType);
	}

	/**
	 * @return Returns the relationshipType.
	 */
	public String getRelationshipType() {
		return relationshipType;
	}

	/**
	 * @param relationshipType The relationshipType to set.
	 */
	public void setRelationshipType(final String relationshipType) {
		this.relationshipType = relationshipType;
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

	/**
	 * @param d
	 * @throws ArahantException 
	 */
	void setDataSubset(BHREmplDependent d) throws ArahantException {
		d.setRelationship(relationship);
		d.setRelationshipType(relationshipType);
		d.setHandicap(handicap);
		d.setStudent(student);
		d.setInactiveDate(inactiveDate);
	}
}

	
