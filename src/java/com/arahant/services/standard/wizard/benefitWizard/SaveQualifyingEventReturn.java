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

import com.arahant.beans.BenefitChangeReasonDoc;
import com.arahant.beans.HrBenefitChangeReason;
import com.arahant.services.TransmitReturnBase;
import java.util.List;

public class SaveQualifyingEventReturn extends TransmitReturnBase {

	private RequiredDocumentItem[] docs;

	public RequiredDocumentItem[] getDocs() {
		return docs;
	}

	public void setDocs(RequiredDocumentItem[] docs) {
		this.docs = docs;
	}

	void setDocs(List<BenefitChangeReasonDoc> docs, HrBenefitChangeReason bj, int effectiveDate, int hireDate) {
		this.docs = new RequiredDocumentItem[docs.size()];
		for (int loop = 0; loop < docs.size(); loop++)
			this.docs[loop] = new RequiredDocumentItem(docs.get(loop).getCompanyForm(), docs.get(loop).getInstructions(), bj, effectiveDate, hireDate);
	}
}
