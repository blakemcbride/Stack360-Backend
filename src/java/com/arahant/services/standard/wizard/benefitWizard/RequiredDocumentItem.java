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

import com.arahant.beans.CompanyForm;
import com.arahant.beans.HrBenefitChangeReason;
import com.arahant.business.BCompanyForm;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BProperty;
import com.arahant.lisp.LispPackage;
import org.kissweb.StringUtils;

/**
 *
 * Arahant
 */
public class RequiredDocumentItem {

	private String path;
	private String filename;
	private String instructions;

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public RequiredDocumentItem(CompanyForm cf, String instructions, String benefitJoinId) {
		if (cf != null) {
			this.path = new BCompanyForm(cf).getForm();
			this.filename = cf.getSource().substring(cf.getSource().lastIndexOf("/") + 1);
		} else {
			this.path = "";
			this.filename = "(Instructions Only)";
		}
		this.instructions = instructions;
		if (BProperty.getBoolean("UsingLISP") && !StringUtils.isEmpty(this.instructions)) {
			BHRBenefitJoin bj = new BHRBenefitJoin(benefitJoinId);
			BEmployee be = new BEmployee(bj.getPayingPersonId());
			String s = this.instructions;
			s = s.toLowerCase();
			int i = s.indexOf("$date");
			if (i > -1) {
				this.instructions = this.instructions.substring(0, i) + "$date" + instructions.substring(i + 5, instructions.length());
				this.instructions = LispPackage.executeLispMethodReturnString("WmCoWizardRequiredDocumentDates", "WmCo-WizardRequiredDocumentDates", "CalculateSubmitByDate", this.instructions, bj.getChangeReasonType(), bj.getBenefitCategoryType(), be.getHireDate(), bj.getChangeReasonDate(), bj.getChangeReasonEndDate());
			}
		}
	}

	public RequiredDocumentItem(CompanyForm cf, String instructions, HrBenefitChangeReason bcr, int effectiveDate, int hireDate) {
		if (cf != null) {
			this.path = new BCompanyForm(cf).getForm();
			this.filename = cf.getSource().substring(cf.getSource().lastIndexOf("/") + 1);
		} else {
			this.path = "";
			this.filename = "(Instructions Only)";
		}
		this.instructions = instructions;
		if (BProperty.getBoolean("UsingLISP") && !StringUtils.isEmpty(this.instructions)) {
			String s = this.instructions;
			s = s.toLowerCase();
			int i = s.indexOf("$date");
			if (i > -1) {
				this.instructions = this.instructions.substring(0, i) + "$date" + instructions.substring(i + 5, instructions.length());
				this.instructions = LispPackage.executeLispMethodReturnString("WmCoWizardRequiredDocumentDates", "WmCo-WizardRequiredDocumentDates", "CalculateSubmitByDate", this.instructions, bcr.getType(), -1, hireDate, effectiveDate, bcr.getEndDate());
			}
		}
	}

	public RequiredDocumentItem() {
	}
}
