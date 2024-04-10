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
 * Created on Feb 8, 2007
*/

package com.arahant.services.standard.project.orgGroupProjectList;

import com.arahant.annotation.Validation;

import com.arahant.beans.Person;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitInputBase;
import com.arahant.business.BProject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SaveProjectInput extends TransmitInputBase {

	@Validation(required = true)
	private String id;
	@Validation(required = false)
	private String statusId;
	@Validation(min = 1, max = 10000, required = true)
	private int companyPriority;
	@Validation(min = 1, max = 10000, required = true)
	private int orgGroupPriority;
	@Validation(min = 1, max = 10000, required = true)
	private int clientPriority;
	@Validation(required = false)
	private String[] personIds;
	@Validation(required = false)
	private String personId;
	@Validation(required = false)
	private int personIdPriority;
	@Validation(required = false)
	private String billable;
	@Validation(min = 0, required = false)
	private double billingRate;
	@Validation(required = false)
	private String primaryParentId;


	void setData(BProject bp) {
	/*	if (bc.getBillable()=='U' && bc.getAssignedPersons().length>0)
		{
			boolean once=false;
			for (BProject.BAssignedPerson bap : bc.getAssignedPersons())
			{
				for (String p : getPersonIds())
				{
					if (p.equals(bap.getPersonId())&&!once)
					{
						once=true;
						ArahantSession.addReturnMessage("A person is assigned to this project but the billable state is set to Unknown.");
					}
					else
						throw new ArahantWarning("A person can not be assigned to this project while the billable state is set to Unknown.");
				}
			}
		}
		*/
		if (billable != null && !"".equals(billable)) {
			bp.setBillable(billable.charAt(0));
			bp.setBillingRate((float) billingRate);
			bp.setPrimaryParentId(primaryParentId);
		}

		List<Person> persons = bp.getAssignedPersons2();

		List<Person> remove = new ArrayList<>(persons);

		List<String> addPeople = new LinkedList<>();

		for (String p : getPersonIds()) {
			boolean found = false;

			for (Person person : persons) {
				if (person.getPersonId().equals(p)) {
					found = true;
					remove.remove(person);
					break;
				}
			}
			if (!found)
				addPeople.add(p);
			//bc.assignPerson(p, 10000);

		}

//  XXYY		bp.assignPeople(addPeople,10000);
		if (true)
			throw new ArahantException("XXYY");

		if (personId != null && !"".equals(personId))
			bp.changePriority(personId, personIdPriority);

		if (true) throw new ArahantException("XXYY");
//  XXYY		bp.removeAssignments(remove);

		bp.setProjectStatusId(statusId);
		bp.setCompanyPriority(companyPriority);
		bp.setOrgGroupPriority(orgGroupPriority);
		bp.setClientPriority(clientPriority);
	}

	public String[] getPersonIds() {
		if (personIds == null)
			personIds = new String[0];
		return personIds;
	}

	public void setPersonIds(String[] personIds) {
		this.personIds = personIds;
	}


	public int getClientPriority() {
		return clientPriority;
	}

	public void setClientPriority(int clientPriority) {
		this.clientPriority = clientPriority;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public int getCompanyPriority() {
		return companyPriority;
	}

	public void setCompanyPriority(int companyPriority) {
		this.companyPriority = companyPriority;
	}

	public int getOrgGroupPriority() {
		return orgGroupPriority;
	}

	public void setOrgGroupPriority(int orgGroupPriority) {
		this.orgGroupPriority = orgGroupPriority;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public int getPersonIdPriority() {
		return personIdPriority;
	}

	public void setPersonIdPriority(int personIdPriority) {
		this.personIdPriority = personIdPriority;
	}

	public String getBillable() {
		return billable;
	}

	public void setBillable(String billable) {
		this.billable = billable;
	}

	public double getBillingRate() {
		return billingRate;
	}

	public void setBillingRate(double billingRate) {
		this.billingRate = billingRate;
	}

	public String getPrimaryParentId() {
		return primaryParentId;
	}

	public void setPrimaryParentId(String primaryParentId) {
		this.primaryParentId = primaryParentId;
	}

}

	
