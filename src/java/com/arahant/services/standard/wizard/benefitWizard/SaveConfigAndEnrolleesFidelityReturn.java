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

import com.arahant.beans.BenefitDocument;
import com.arahant.business.BBenefitRider;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.services.TransmitReturnBase;
import java.util.List;

public class SaveConfigAndEnrolleesFidelityReturn extends TransmitReturnBase {

	private String benefitJoinId;
	private RequiredDocumentItem[] requiredDocument;
	private String[] benefitJoinIds;

	public String[] getBenefitJoinIds() {
		return benefitJoinIds;
	}

	public void setBenefitJoinIds(String[] benefitJoinIds) {
		this.benefitJoinIds = benefitJoinIds;
	}

	public String getBenefitJoinId() {
		return benefitJoinId;
	}

	public void setBenefitJoinId(String benefitJoinId) {
		this.benefitJoinId = benefitJoinId;
	}

	public RequiredDocumentItem[] getRequiredDocument() {
		return requiredDocument;
	}

	public void setRequiredDocument(RequiredDocumentItem[] requiredDocument) {
		this.requiredDocument = requiredDocument;
	}

	void setRequiredDocument(List<BenefitDocument> requiredDocuments, String benefitJoinId) {
		this.requiredDocument = new RequiredDocumentItem[requiredDocuments.size()];
		for (int loop = 0; loop < requiredDocuments.size(); loop++)
			requiredDocument[loop] = new RequiredDocumentItem(requiredDocuments.get(loop).getCompanyForm(), requiredDocuments.get(loop).getInstructions(), benefitJoinId);
	}
	private BenefitRider[] benefitRiders;

	public BenefitRider[] getBenefitRiders() {
		return benefitRiders;
	}

	public void setBenefitRiders(BenefitRider[] benefitRiders) {
		this.benefitRiders = benefitRiders;
	}

	public void setBenefitRiders(List<com.arahant.beans.BenefitRider> l, BHRBenefitJoin bj, int date) {
		this.benefitRiders = new BenefitRider[l.size()];
		for (int i = 0; i < l.size(); i++)
			benefitRiders[i] = new BenefitRider(new BBenefitRider(l.get(i)), bj, date);
	}
}
