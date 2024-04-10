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


package com.arahant.services.standard.project.projectAssignment;

import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;

public class SavePrioritiesForProjectsInput extends TransmitInputBase {
	@Validation (min=1,max=4,required=true)
	private int priorityType; // 1=company  2=group  3=client  4=person
	@Validation (required=false)
	private String personId; // valid if priorityType=4
	@Validation (required=true)
	private SavePrioritiesForProjectsInputPriorities[] priorities;

	public SavePrioritiesForProjectsInputPriorities[] getPriorities() {
		return priorities;
	}

	public void setPriorities(SavePrioritiesForProjectsInputPriorities[] priorities) {
		this.priorities = priorities;
	}

	public int getPriorityType() {
		return priorityType;
	}

	public void setPriorityType(int priorityType) {
		this.priorityType = priorityType;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}
}

	
