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
package com.arahant.services.standard.wizard.benefitWizard;

import com.arahant.beans.HrEmplDependentWizard;
import com.arahant.business.BBenefitEnrollments;
import com.arahant.business.BEmployee;
import com.arahant.business.BHREmplDependent;
import com.arahant.business.BPerson;

public class ListDependentsAndEmployeeReturnItem {

	private boolean select;
	private String personId;
	private String lastName;
	private String firstName;
	private String middleName;
	private String relationship;
	private String sex;
	private String ssn;
	private int age;
	private boolean selectPending;
	private boolean selectApproved;

	public ListDependentsAndEmployeeReturnItem() {
	}

	ListDependentsAndEmployeeReturnItem(HrEmplDependentWizard ew, String benefitId, String categoryId) {
		if (ew.getRecordType() == 'C') {
			BHREmplDependent bc = new BHREmplDependent(ew.getRelationshipId());
			personId = bc.getDependentId();
			lastName = bc.getLastName();
			firstName = bc.getFirstName();
			middleName = bc.getMiddleName();
			relationship = bc.getTextRelationship();
			sex = bc.getSex();
			ssn = bc.getSsn();
			select = false;
			selectPending = false;
			selectApproved = false;

			if (categoryId != null && !categoryId.equals("")) {
				selectPending = bc.enrolledInCategory(categoryId);
				selectApproved = bc.enrolledInApprovedCategory(categoryId);
				select = bc.enrolledInCategory(categoryId);
				if (!select && !BBenefitEnrollments.enrolledInCategory(categoryId, new String[]{bc.getEmployeeId()}))
					select = bc.enrolledInApprovedCategory(categoryId);
			} else if (benefitId != null && !benefitId.equals("")) {
				selectPending = bc.enrolledInBenefit(benefitId);
				selectApproved = bc.enrolledInApprovedBenefit(benefitId);
				select = bc.enrolledInBenefit(benefitId);
				if (!select && !BBenefitEnrollments.enrolledInPendingBenefit(benefitId, new String[]{bc.getEmployeeId()}))
					select = bc.enrolledInApprovedBenefit(benefitId);
			}

			try {
				if (bc.getDob() > 0)
					age = Integer.parseInt(BPerson.getAge(bc.getDob()));
				else
					age = -1;
			} catch (Exception e) {
				age = -1;
			}
		} else {
			BHREmplDependent bc = new BHREmplDependent(ew.getRelationshipId());
			personId = bc.getDependentId();
			lastName = bc.getLastName();
			firstName = bc.getFirstName();
			middleName = bc.getMiddleName();
			relationship = bc.getTextRelationship();
			sex = bc.getSex();
			ssn = bc.getSsn();

			select = false;
			selectPending = false;
			selectApproved = false;


			if (categoryId != null && !categoryId.equals("")) {
				selectPending = bc.enrolledInCategory(categoryId);
				selectApproved = bc.enrolledInApprovedCategory(categoryId);
				select = bc.enrolledInCategory(categoryId);
				if (!select && !BBenefitEnrollments.enrolledInCategory(categoryId, new String[]{bc.getEmployeeId()}))
					select = bc.enrolledInApprovedCategory(categoryId);
			} else if (benefitId != null && !benefitId.equals("")) {
				selectPending = bc.enrolledInBenefit(benefitId);
				selectApproved = bc.enrolledInApprovedBenefit(benefitId);
				select = bc.alreadyEnrolledInConfig(benefitId);
				if (!select && !BBenefitEnrollments.enrolledInPendingBenefit(benefitId, new String[]{bc.getEmployeeId()}))
					select = bc.enrolledInApprovedBenefit(benefitId);
			}

			try {
				if (bc.getDob() > 0)
					age = Integer.parseInt(BPerson.getAge(bc.getDob()));
				else
					age = -1;
			} catch (Exception e) {
				age = -1;
			}
		}
	}

	ListDependentsAndEmployeeReturnItem(BPerson bpp, BEmployee be, String benefitId, String categoryId) {
		personId = be.getPersonId();
		lastName = be.getLastName();
		firstName = be.getFirstName();
		middleName = be.getMiddleName();
		relationship = "Employee";
		sex = be.getSex();
		ssn = be.getSsn();

		select = false;
		selectPending = false;
		selectApproved = false;



		if (categoryId != null && !categoryId.equals("")) {
			selectPending = BBenefitEnrollments.enrolledInCategory(categoryId, new String[]{be.getPersonId()});
			selectApproved = BBenefitEnrollments.enrolledInApprovedCategory(categoryId, new String[]{be.getPersonId()});
			select = BBenefitEnrollments.enrolledInCategory(categoryId, new String[]{be.getPersonId()});
			if (!select)
				select = BBenefitEnrollments.enrolledInApprovedCategory(categoryId, new String[]{be.getPersonId()});
		} else if (benefitId != null && !benefitId.equals("")) {
			selectPending = BBenefitEnrollments.enrolledInPendingBenefit(benefitId, new String[]{be.getPersonId()});
			selectApproved = BBenefitEnrollments.enrolledInApprovedBenefit(benefitId, new String[]{be.getPersonId()});
			select = BBenefitEnrollments.enrolledInPendingBenefit(benefitId, new String[]{be.getPersonId()});
			if (!select)
				select = BBenefitEnrollments.enrolledInApprovedBenefit(benefitId, new String[]{be.getPersonId()});
		}

		try {
			if (bpp.getDob() > 0)
				age = Integer.parseInt(BPerson.getAge(bpp.getDob()));
			else
				age = -1;
		} catch (Exception e) {
			age = -1;
		}
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

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public boolean getSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public boolean isSelectApproved() {
		return selectApproved;
	}

	public void setSelectApproved(boolean selectApproved) {
		this.selectApproved = selectApproved;
	}

	public boolean isSelectPending() {
		return selectPending;
	}

	public void setSelectPending(boolean selectPending) {
		this.selectPending = selectPending;
	}
}
