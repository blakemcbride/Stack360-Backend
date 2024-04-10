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
package com.arahant.services.standard.hr.benefitCategory;

import com.arahant.business.BHRBenefit;
import com.arahant.business.BHRBenefitCategory;
import com.arahant.business.BHrBenefitCategoryAnswer;


public class ListAnswersForQuestionReturnItem {

	private String benefitName;
	private String benefitId;
	private String answerId;
	private String answer;

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getAnswerId() {
		return answerId;
	}

	public void setAnswerId(String answerId) {
		this.answerId = answerId;
	}

	public String getBenefitId() {
		return benefitId;
	}

	public void setBenefitId(String benefitId) {
		this.benefitId = benefitId;
	}

	public String getBenefitName() {
		return benefitName;
	}

	public void setBenefitName(String benefitName) {
		this.benefitName = benefitName;
	}


	
	public ListAnswersForQuestionReturnItem() {
		
	}

	ListAnswersForQuestionReturnItem(BHrBenefitCategoryAnswer bc) {
		benefitName = bc.getBenefit().getName();
		benefitId = bc.getBenefit().getBenefitId();
		answerId = bc.getAnswerId();
		answer = bc.getAnswer();

	}

	ListAnswersForQuestionReturnItem(BHRBenefit b) {
		benefitName = b.getName();
		benefitId = b.getBenefitId();
		answerId = "";
		answer = "(Not Specified)";

	}
	
}

	
