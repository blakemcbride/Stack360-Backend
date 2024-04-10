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



package com.arahant.services.standard.wizard.benefitWizard;

import com.arahant.annotation.Validation;
import com.arahant.beans.HrEmplDependent;
import com.arahant.business.BEmployee;
import com.arahant.business.BHREmplDependent;
import com.arahant.business.BPerson;
import com.arahant.services.TransmitInputBase;
import com.arahant.utils.ArahantSession;

/**
 *
 *
 *
 */
public class SaveSmokerInput extends TransmitInputBase {

	@Validation(required = false)
	private String employeeId;
	@Validation(required = false)
	private boolean isNonSmoker;
	@Validation(required = false)
	private boolean isSpouseNonSmoker;
	@Validation(required = false)
	private boolean employeeProgram;
	@Validation(required = false)
	private boolean spouseProgram;

	void setData(BEmployee bc) {
		bc.setPersonId(employeeId);
		bc.setTabaccoUse(isNonSmoker ? "N" : "Y");
		bc.getEmployee().setSmokingProgram(employeeProgram ? 'Y' : 'N');
		bc.update();

		HrEmplDependent ed = ArahantSession.getHSU().createCriteria(HrEmplDependent.class).eq(HrEmplDependent.EMPLOYEE, bc.getEmployee()).eq(HrEmplDependent.RELATIONSHIP_TYPE, 'S').first();
		if (ed != null) {
			ed.getPerson().setSmoker(isSpouseNonSmoker ? 'N' : 'Y');
			ed.getPerson().setSmokingProgram(spouseProgram ? 'Y' : 'N');

			BHREmplDependent bed = new BHREmplDependent(ed);
			bed.update();
		} else {
			HrEmplDependent dep = ArahantSession.getHSU().createCriteria(HrEmplDependent.class).eq(HrEmplDependent.RECORD_TYPE, 'C').eq(HrEmplDependent.EMPLOYEE, bc.getEmployee()).eq(HrEmplDependent.RELATIONSHIP_TYPE, 'S').first();
			if (dep != null) {
				BPerson bpp = new BPerson(dep.getDependentId());
				if (bpp.getRecordType() != 'C')
					bpp.loadPending(dep.getDependentId());
				bpp.setSmoker(!isSpouseNonSmoker);
				bpp.setSmokingProgram(spouseProgram ? 'Y' : 'N');
			}
		}

	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public boolean getIsNonSmoker() {
		return isNonSmoker;
	}

	public void setIsNonSmoker(boolean isNonSmoker) {
		this.isNonSmoker = isNonSmoker;
	}

	public boolean getIsSpouseNonSmoker() {
		return isSpouseNonSmoker;
	}

	public void setIsSpouseNonSmoker(boolean isSpouseNonSmoker) {
		this.isSpouseNonSmoker = isSpouseNonSmoker;
	}

	public boolean getEmployeeProgram() {
		return employeeProgram;
	}

	public void setEmployeeProgram(boolean employeeProgram) {
		this.employeeProgram = employeeProgram;
	}

	public boolean getSpouseProgram() {
		return spouseProgram;
	}

	public void setSpouseProgram(boolean spouseProgram) {
		this.spouseProgram = spouseProgram;
	}
}
