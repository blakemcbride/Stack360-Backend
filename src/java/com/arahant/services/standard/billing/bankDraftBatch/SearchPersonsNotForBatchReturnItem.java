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
package com.arahant.services.standard.billing.bankDraftBatch;
import com.arahant.business.BPerson;


/**
 * 
 *
 *
 */
public class SearchPersonsNotForBatchReturnItem {
	
	public SearchPersonsNotForBatchReturnItem()
	{
		;
	}

	SearchPersonsNotForBatchReturnItem (BPerson bc, String batchId)
	{
		
		id=bc.getId();
		firstName=bc.getFirstName();
		middleName=bc.getMiddleName();
		lastName=bc.getLastName();
		ssn=bc.getSsn();
		if (bc.isEmployee()) {
			type = "Emp";
		} else {
			type = "Dep";
		}
		existsInOtherBatch=bc.getExistsInOtherBatch(batchId);

	}
	
	private String id;
	private String firstName;
	private String middleName;
	private String lastName;
	private String ssn;
	private String type;
	private boolean existsInOtherBatch;
	

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public boolean getExistsInOtherBatch()
	{
		return existsInOtherBatch;
	}
	public void setExistsInOtherBatch(boolean existsInOtherBatch)
	{
		this.existsInOtherBatch=existsInOtherBatch;
	}

}

	
