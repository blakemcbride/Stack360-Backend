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


package com.arahant.dynamic.services.standard.hr.benefitAssignment;

import com.arahant.business.BBenefitQuestion;
import com.arahant.services.standard.dynamicwebservices.DynamicWebServiceOps;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.dynamicwebservices.DataObject;
import com.arahant.utils.dynamicwebservices.DataObjectList;
import com.arahant.utils.dynamicwebservices.DataObjectMap;

public class SaveQuestionsAndAnswers {

	public static void main(DataObjectMap in, DataObjectMap out, HibernateSessionUtil hsu, DynamicWebServiceOps service)
	{
		DataObject questionsObject = in.get("questionsArray");
		//nothing to save, so just bail out of here
		if(questionsObject == null)
			return;
		DataObjectList questionsList = (DataObjectList)questionsObject.getValue();
		for(DataObject question : questionsList)
		{
			DataObjectMap questionMap = (DataObjectMap)question.getValue();

			String questionId = questionMap.getString("questionId");
			BBenefitQuestion bbq = new BBenefitQuestion(questionId);

			DataObject answersObject = questionMap.get("answersArray");
			DataObjectList answersList = (DataObjectList)answersObject.getValue();
			for(DataObject answer : answersList)
			{
				DataObjectMap answerMap = (DataObjectMap)answer.getValue();

				String personId = answerMap.getString("personId");
				DataObject qa = answerMap.get("answer");
				bbq.saveAnswerForPerson(personId, qa.getValue());
			}
		}
	}

}
