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
package com.arahant.services.standard.hr.wizardProjectDetail;

import com.arahant.business.BHRBenefitChangeReason;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BHRBenefitJoinH;
import com.arahant.business.BPerson;
import com.arahant.business.BWizardConfiguration;
import com.arahant.business.BWizardProject;
import com.arahant.utils.DateUtils;
import org.kissweb.StringUtils;


/**
 * 
 *
 *
 */
public class LoadSummaryReturnItem {

	public LoadSummaryReturnItem() {
	}

	

	LoadSummaryReturnItem(BWizardProject bWizardProject) {
		if(bWizardProject.getProjectAction() == 'A')
		{
			type = "Enrollment Approval";
			benefitJoinId = bWizardProject.getBenefitJoinId();
			if(!StringUtils.isEmpty(benefitJoinId))
				description = BWizardConfiguration.getBenefitJoinDescription(new BHRBenefitJoin(benefitJoinId).getPolicyAndDependentBenefitJoins(false));
			else if(!StringUtils.isEmpty(bWizardProject.getBenefitJoinHId()))
				description = BWizardConfiguration.getBenefitJoinDescription(new BHRBenefitJoinH((bWizardProject.getBenefitJoinHId())).getPolicyAndDependentBenefitJoins());

		}
		else if(bWizardProject.getProjectAction() == 'C')
		{
			type = "Qualifying Event Action";
			benefitJoinId = bWizardProject.getBenefitJoinId();
			if(!StringUtils.isEmpty(benefitJoinId))
			{
				BHRBenefitJoin bj = new BHRBenefitJoin(benefitJoinId);
				BHRBenefitChangeReason bcr = new BHRBenefitChangeReason(bj.getBenefitChangeReasonId());
				description = bcr.getDescription() + "\n      " + (StringUtils.isEmpty(bj.getEmployeeExplanation()) ? "" : "\"" + bj.getEmployeeExplanation()) + "\"" + "\n\n";
			}
			else if(!StringUtils.isEmpty(bWizardProject.getBenefitJoinHId()))
			{
				BHRBenefitJoinH bj = new BHRBenefitJoinH(bWizardProject.getBenefitJoinHId());
				BHRBenefitChangeReason bcr = new BHRBenefitChangeReason(bj.getBenefitChangeReasonId());
				description = bcr.getDescription() + "\n      " + (StringUtils.isEmpty(bj.getEmployeeExplanation()) ? "" : "\"" + bj.getEmployeeExplanation()) + "\"" + "\n\n";
			}
		}
		else if(bWizardProject.getProjectAction() == 'D')
		{
			type = "Demographic Approval";

			try {
				personId = bWizardProject.getProject().getSubjectPerson().getPersonId();
				BPerson bp = new BPerson(personId);
				description = bp.getDescription();
			} catch (Exception e) {
				description = "";
				personId = "";
			}
		}
		wizardProjectId = bWizardProject.getWizardProjectId();
		completed = bWizardProject.getCompleted() == 'Y' ? "Yes" : "No";
		if(completed.equals("Yes"))
		{
			personCompleted = bWizardProject.getPersonCompleted().getNameFL();
			dateCompleted = DateUtils.getDateAndTimeFormatted(bWizardProject.getDateComplated());
		}
		else
		{
			personCompleted = "";
			dateCompleted = "";
		}

	}



	private String type;
	private String description;
	private String benefitJoinId;
	private String personId;
	private String wizardProjectId;
	private String completed;
	private String personCompleted;
	private String dateCompleted;

	public String getCompleted() {
		return completed;
	}

	public void setCompleted(String completed) {
		this.completed = completed;
	}

	public String getDateCompleted() {
		return dateCompleted;
	}

	public void setDateCompleted(String dateCompleted) {
		this.dateCompleted = dateCompleted;
	}

	public String getPersonCompleted() {
		return personCompleted;
	}

	public void setPersonCompleted(String personCompleted) {
		this.personCompleted = personCompleted;
	}

	public String getWizardProjectId() {
		return wizardProjectId;
	}

	public void setWizardProjectId(String wizardProjectId) {
		this.wizardProjectId = wizardProjectId;
	}

	public String getBenefitJoinId() {
		return benefitJoinId;
	}

	public void setBenefitJoinId(String benefitJoinId) {
		this.benefitJoinId = benefitJoinId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

}

	
