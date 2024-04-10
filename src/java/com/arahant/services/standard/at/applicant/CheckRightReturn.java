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
 *  Created on Feb 8, 2007
*/

package com.arahant.services.standard.at.applicant;

import com.arahant.business.BRight;
import com.arahant.services.TransmitReturnBase;

public class CheckRightReturn extends TransmitReturnBase {

	private int applicantDelete;

	void setAccess() {
		applicantDelete = BRight.checkRight(BRight.APPLICANT_DELETE);
	}

	public int getApplicantDelete() {
		return applicantDelete;
	}

	public void setApplicantDelete(int applicantDelete) {
		this.applicantDelete = applicantDelete;
	}
}

	
