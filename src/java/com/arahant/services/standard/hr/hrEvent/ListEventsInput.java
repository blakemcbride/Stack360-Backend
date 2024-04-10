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
 * Created on Mar 19, 2007
 */
package com.arahant.services.standard.hr.hrEvent;

import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;

public class ListEventsInput extends TransmitInputBase {

	@Validation (required=true)
	private String personId;
	private Boolean includeAssignments;
	private Boolean includeStatusChanges;
	private Boolean includeCheckins;

	public ListEventsInput() {
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(final String personId) {
		this.personId = personId;
	}

	public Boolean getIncludeAssignments() {
		return includeAssignments;
	}

	public void setIncludeAssignments(Boolean includeAssignments) {
		this.includeAssignments = includeAssignments;
	}

	public Boolean getIncludeStatusChanges() {
		return includeStatusChanges;
	}

	public void setIncludeStatusChanges(Boolean includeStatusChanges) {
		this.includeStatusChanges = includeStatusChanges;
	}

	public Boolean getIncludeCheckins() {
		return includeCheckins;
	}

	public void setIncludeCheckins(Boolean includeCheckins) {
		this.includeCheckins = includeCheckins;
	}
}

	
