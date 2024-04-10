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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.services.standard.at.applicant;

import com.arahant.business.BApplicant;
import com.arahant.business.BApplicantQuestion;

public class LoadApplicantReturnQuestions {

	private LoadApplicantReturnQuestionsQuestion []question;

	
	public LoadApplicantReturnQuestions()
	{
		
	}
	
	LoadApplicantReturnQuestions(BApplicant applicant)
	{
		BApplicantQuestion []qs=applicant.getApplicantAnsweredQuestions();
		question=new LoadApplicantReturnQuestionsQuestion[qs.length];
		for (int loop=0;loop<qs.length;loop++)
			question[loop]=new LoadApplicantReturnQuestionsQuestion(qs[loop], applicant);
	}

	public LoadApplicantReturnQuestionsQuestion[] getQuestion() {
		return question;
	}

	public void setQuestion(LoadApplicantReturnQuestionsQuestion[] question) {
		this.question = question;
	}
}
