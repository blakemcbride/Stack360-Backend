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
package com.arahant.services.standard.at.applicant;
import com.arahant.business.BApplicantQuestion;


/**
 * 
 *
 *
 */
public class SearchQuestionsForJobTypeReturnItem {
	
	public SearchQuestionsForJobTypeReturnItem()
	{
	}

	SearchQuestionsForJobTypeReturnItem (BApplicantQuestion bc)
	{
		
		id=bc.getId();
		question=bc.getQuestion();
		answerType=bc.getAnswerType();

	}
	
	private String id;
	private String question;
	private String answerType;
	

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getQuestion()
	{
		return question;
	}
	public void setQuestion(String question)
	{
		this.question=question;
	}
	public String getAnswerType()
	{
		return answerType;
	}
	public void setAnswerType(String answerType)
	{
		this.answerType=answerType;
	}

}

	
