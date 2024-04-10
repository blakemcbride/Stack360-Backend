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
 *
 * Created on Feb 5, 2007
 * 
 */
package com.arahant.services.standard.hr.hrParent;

import com.arahant.annotation.Validation;
import com.arahant.business.BSearchMetaInput;
import com.arahant.services.SearchMetaInput;
import com.arahant.services.TransmitInputBase;


public class SearchPersonsInput extends TransmitInputBase  {
	@Validation (table="employee",column="ssn",required=false)
	private String ssn;
	@Validation (required=false)
	private SearchMetaInput searchMeta;
	@Validation (table="person",column="fname",required=false)
	private String firstName;
	@Validation (table="person",column="lname",required=false)
	private String lastName;
	@Validation (min=1,max=3,required=false)
	private int activeIndicator;
	@Validation (min=2,max=5,required=false)
	private int firstNameSearchType;
	@Validation (min=2,max=5,required=false)
	private int lastNameSearchType;
	@Validation (required=false)
	private boolean searchEmployees;
	@Validation (table="hr_employee_status",column="status_id",required=false)
	private String employeeStatusId;
	private String workerType;
	private String assigned;
	private int assignedFrom;
	private int assignedTo;
	private String firstPerson;
	private String lastPerson;
	private int searchType; // 0=search, 1=previous group, 2=next group
	private String [] labels;

	public String getEmployeeStatusId() {
		return employeeStatusId;
	}

	public void setEmployeeStatusId(String employeeStatusId) {
		this.employeeStatusId = employeeStatusId;
	}
	
	public boolean getSearchEmployees() {
		return searchEmployees;
	}

	public void setSearchEmployees(boolean searchEmployees) {
		this.searchEmployees = searchEmployees;
	}

	/**
	 * @return Returns the firstNameSearchType.
	 */
	public int getFirstNameSearchType() {
		return firstNameSearchType;
	}

	/**
	 * @param firstNameSearchType The firstNameSearchType to set.
	 */
	public void setFirstNameSearchType(final int firstNameSearchType) {
		this.firstNameSearchType = firstNameSearchType;
	}

	/**
	 * @return Returns the lastNameSearchType.
	 */
	public int getLastNameSearchType() {
		return lastNameSearchType;
	}

	/**
	 * @param lastNameSearchType The lastNameSearchType to set.
	 */
	public void setLastNameSearchType(final int lastNameSearchType) {
		this.lastNameSearchType = lastNameSearchType;
	}

	/**
	 * @return Returns the activeIndicator.
	 */
	public int getActiveIndicator() {
		return activeIndicator;
	}

	/**
	 * @param activeIndicator The activeIndicator to set.
	 */
	public void setActiveIndicator(final int activeIndicator) {
		this.activeIndicator = activeIndicator;
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
	 * @return Returns the firstName.
	 */
	public String getFirstName() {
		return firstName ; // modifyForSearch(firstName, firstNameSearchType);
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
		return lastName; // modifyForSearch(lastName, lastNameSearchType);
	}

	public String getWorkerType() {
		return workerType;
	}

	public void setWorkerType(String workerType) {
		this.workerType = workerType;
	}

	public String getAssigned() {
		return assigned;
	}

	public void setAssigned(String assigned) {
		this.assigned = assigned;
	}

	public int getAssignedFrom() {
		return assignedFrom;
	}

	public void setAssignedFrom(int assignedFrom) {
		this.assignedFrom = assignedFrom;
	}

	public int getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(int assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getFirstPerson() {
		return firstPerson;
	}

	public void setFirstPerson(String firstPerson) {
		this.firstPerson = firstPerson;
	}

	public String getLastPerson() {
		return lastPerson;
	}

	public void setLastPerson(String lastPerson) {
		this.lastPerson = lastPerson;
	}

	public int getSearchType() {
		return searchType;
	}

	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}

	public String[] getLabels() {
		return labels;
	}

	public void setLabels(String[] labels) {
		this.labels = labels;
	}

	/**
	 * @param lastName The lastName to set.
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public SearchMetaInput getSearchMeta() {
		return searchMeta;
	}

	public void setSearchMeta(SearchMetaInput searchMeta) {
		this.searchMeta = searchMeta;
	}
	
	BSearchMetaInput getSearchMetaInput() {
		if (searchMeta == null) {
			return new BSearchMetaInput(0, true, false, 0);		
		} else {
			return new BSearchMetaInput(searchMeta,new String[]{"lname", "fname", "middleName", "ssn",
				"jobTitle", "statusName", "type"});
			// lname, fname, middleName, ssn, jobTitle, statusName, type
		}
	}
	
}

	
