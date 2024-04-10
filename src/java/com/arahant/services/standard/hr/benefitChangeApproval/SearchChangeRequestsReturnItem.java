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
package com.arahant.services.standard.hr.benefitChangeApproval;

import com.arahant.beans.Employee;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.Person;
import com.arahant.beans.PersonCR;
import com.arahant.business.BPerson;
import com.arahant.business.BProject;
import com.arahant.business.BWizardProject;
import com.arahant.utils.ArahantSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SearchChangeRequestsReturnItem {

	private String requestingPerson;
	private int changeDate;
	private String projectDescription;
	private String projectDetailDescription;
	private String projectId;
	private String projectStatus;
	private boolean benefitChange;
	private boolean demographicChange;
	private String employeeId;
	private String employeeFirstName;
	private String employeeLastName;
	private String pending;

	public SearchChangeRequestsReturnItem() {
	}

	SearchChangeRequestsReturnItem(BProject bc) {
		BPerson bp = new BPerson(bc.getBean().getDoneForPersonId());
		List<Character> benefitWizardStatuses = new ArrayList<Character>();
		benefitWizardStatuses.add(Employee.BENEFIT_WIZARD_STATUS_FINALIZED);
		benefitWizardStatuses.add(Employee.BENEFIT_WIZARD_STATUS_NO_CHANGE);
		benefitWizardStatuses.add(Employee.BENEFIT_WIZARD_STATUS_PROCESSED);
		
		char changeStatus = 'P';
		Set<PersonCR> pcrLst = bc.getBean().getPersonChangeRequest();
		if (!pcrLst.isEmpty()) {
			PersonCR pcr = pcrLst.iterator().next();
			changeStatus = pcr.getChangeStatus();
			requestingPerson = pcr.getRequestor().getNameLFM();
		} else {
			BWizardProject [] wplst = bc.getWizardProjects(true);
			for (BWizardProject wp : wplst) {
				HrBenefitJoin bj = wp.getBenefitJoin();
				if (bj == null)
					continue;
				char approved = bj.getBenefitApproved();
				switch (approved) {
					case 'Y':
						changeStatus = 'A';
						break;
					case 'N':
						changeStatus = 'P';
						break;
					case 'R':
						changeStatus = 'R';
						break;
				}
			}
			requestingPerson = bp.getLastName() + ", " + bp.getFirstName();
		}
		changeDate = bc.getDateReported();
		benefitChange = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class)
				.eq(HrBenefitJoin.APPROVED, 'N')
				.eq(HrBenefitJoin.PAYING_PERSON, bp.getPerson())
				.joinTo(HrBenefitJoin.PAYING_PERSON)
				.in(Employee.BENEFIT_WIZARD_STATUS, benefitWizardStatuses)
				.exists();
		demographicChange = ArahantSession.getHSU().createCriteria(Person.class).eq(Person.RECORD_TYPE, 'C').eq(Person.PERSONID, bp.getPersonId()).exists();
		projectDescription = bc.getDescription();
		projectDetailDescription = demographicChange ? bp.getDescription() : bc.getDetailDesc();
		projectId = bc.getProjectId();
		projectStatus = bc.getProjectStatus().getCode();

		employeeId = bp.getPersonId();
		employeeFirstName = bp.getFirstName();
		employeeLastName = bp.getLastName();

		if (changeStatus == 'P')
			pending = "Pending";
		else if (changeStatus == 'A')
			pending = "Approved";
		else if (changeStatus == 'R')
			pending = "Rejected";
	}

	public String getRequestingPerson() {
		return requestingPerson;
	}

	public void setRequestingPerson(String requestingPerson) {
		this.requestingPerson = requestingPerson;
	}

	public int getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(int changeDate) {
		this.changeDate = changeDate;
	}

	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	public String getProjectDetailDescription() {
		return projectDetailDescription;
	}

	public void setProjectDetailDescription(String projectDetailDescription) {
		this.projectDetailDescription = projectDetailDescription;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

	public boolean getBenefitChange() {
		return benefitChange;
	}

	public void setBenefitChange(boolean benefitChange) {
		this.benefitChange = benefitChange;
	}

	public boolean getDemographicChange() {
		return demographicChange;
	}

	public void setDemographicChange(boolean demographicChange) {
		this.demographicChange = demographicChange;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeFirstName() {
		return employeeFirstName;
	}

	public void setEmployeeFirstName(String employeeFirstName) {
		this.employeeFirstName = employeeFirstName;
	}

	public String getEmployeeLastName() {
		return employeeLastName;
	}

	public void setEmployeeLastName(String employeeLastName) {
		this.employeeLastName = employeeLastName;
	}

	public String getPending() {
		return pending;
	}

	public void setPending(String pending) {
		this.pending = pending;
	}
}
