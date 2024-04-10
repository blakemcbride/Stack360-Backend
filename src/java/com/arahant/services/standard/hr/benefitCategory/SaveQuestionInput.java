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
import com.arahant.business.BHrBenefitCategoryQuestion;
import com.arahant.annotation.Validation;


public class SaveQuestionInput extends TransmitInputBase {

	@Validation (table = "hr_benefit_category_question",column = "question_id",required = true)
	private String questionId;
	@Validation (table = "hr_benefit_category_question",column = "question",required = true)
	private String question;
	

	public String getQuestionId()
	{
		return questionId;
	}
	public void setQuestionId(String questionId)
	{
		this.questionId = questionId;
	}
	public String getQuestion()
	{
		return question;
	}
	public void setQuestion(String question)
	{
		this.question = question;
	}


	void setData(BHrBenefitCategoryQuestion bc) {
		
		bc.setQuestionId(questionId);
		bc.setQuestion(question);

	}
	
}

	
