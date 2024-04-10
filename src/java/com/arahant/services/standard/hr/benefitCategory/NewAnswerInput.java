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

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BHrBenefitCategoryAnswer;
import com.arahant.annotation.Validation;
import com.arahant.business.BHRBenefit;
import com.arahant.business.BHrBenefitCategoryQuestion;


public class NewAnswerInput extends TransmitInputBase {

	@Validation (required = true)
	private String questionId;
	@Validation (required = true)
	private String benefitId;
	@Validation (min = 0,max = 200,required = true)
	private String answer;
	

	public String getQuestionId()
	{
		return questionId;
	}
	public void setQuestionId(String questionId)
	{
		this.questionId = questionId;
	}
	public String getBenefitId()
	{
		return benefitId;
	}
	public void setBenefitId(String benefitId)
	{
		this.benefitId = benefitId;
	}
	public String getAnswer()
	{
		return answer;
	}
	public void setAnswer(String answer)
	{
		this.answer = answer;
	}


	void setData(BHrBenefitCategoryAnswer bc) {
		
		bc.setQuestionId(new BHrBenefitCategoryQuestion(questionId).getBean());
		bc.setBenefit(new BHRBenefit(benefitId).getBean());
		bc.setAnswer(answer);
	}
	
}

	
