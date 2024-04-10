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

import com.arahant.beans.BenefitAnswer;
import com.arahant.beans.BenefitQuestion;
import com.arahant.beans.BenefitQuestionChoice;
import com.arahant.beans.HrEmplDependent;
import com.arahant.business.BBenefitQuestion;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRBenefit;
import com.arahant.business.BHRBenefitConfig;
import com.arahant.business.BPerson;
import com.arahant.services.standard.dynamicwebservices.DynamicWebServiceOps;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import org.kissweb.StringUtils;
import com.arahant.utils.dynamicwebservices.DataObjectList;
import com.arahant.utils.dynamicwebservices.DataObjectMap;

public class LoadQuestionsAndAnswers {

	public static void main(DataObjectMap in, DataObjectMap out, HibernateSessionUtil hsu, DynamicWebServiceOps service)
	{
		String empId = in.getString("employeeId");
		if(StringUtils.isEmpty(empId))
			empId = hsu.getCurrentPerson().getPersonId();

		BHRBenefitConfig bc = new BHRBenefitConfig(in.getString("configId"));
		BHRBenefit bb = new BHRBenefit(bc.getBenefit());
		BBenefitQuestion[] questions = BBenefitQuestion.list(bb.getBenefitId(), null, true);
		DataObjectList dol = new DataObjectList();
		BEmployee be;
		
		try {
			be = new BEmployee(empId);
		} catch (Throwable t1) {
			be = null;  //  empId represented a dependent
		}
		for (BBenefitQuestion bbq : questions)
		{
			DataObjectMap item = new DataObjectMap();
			item.put("question", bbq.getQuestion());
			item.put("questionId", bbq.getId());
			item.put("answerType", bbq.getAnswerType());
			item.put("appliesToEmployee", bbq.getAppliesToEmployee() == 'Y');
			item.put("appliesToSpouse", bbq.getAppliesToSpouse() == 'Y');
			item.put("appliesToChildOther", bbq.getAppliesToChildOther() == 'Y');
			boolean includeExplanation = bbq.getIncludesExplanation() != 'N';
			item.put("includeExplanation", includeExplanation);
			if(bbq.getAnswerType().equals(BenefitQuestion.TYPE_LIST + ""))
			{
				DataObjectList listOptions = new DataObjectList();
				for(BenefitQuestionChoice bqc : bbq.getListOptions())
				{
					DataObjectMap listOption = new DataObjectMap();
					listOption.put("data", bqc.getBenefitQuestionChoiceId());
					listOption.put("label", bqc.getDescription());
					listOptions.add(listOption);
				}
				item.put("listItems", listOptions);
			}

			//if this doesn't apply to the employee, N/A is the flag to send
			if(bbq.getAppliesToEmployee() == 'N'  ||  be == null)
			{
				item.put(empId, "N/A");
			}
			else
			{
				BenefitAnswer empAnswer = bbq.getAnswer(be.getPerson());
				if(empAnswer != null)
				{
					if(bbq.getAnswerType().equals(BenefitQuestion.TYPE_DATE + ""))
						item.put(empId, empAnswer.getDateAnswer());
					else if(bbq.getAnswerType().equals(BenefitQuestion.TYPE_NUMERIC + ""))
						item.put(empId, empAnswer.getNumericAnswer());
					else if(bbq.getAnswerType().equals(BenefitQuestion.TYPE_YES_NO_UNK + "") && includeExplanation
							&& empAnswer.getStringAnswer().length() > 0)
						item.put(empId, empAnswer.getStringAnswer().charAt(0));
					else
						item.put(empId, empAnswer.getStringAnswer());
				}
				else
				{
					item.put(empId, "");
				}
			}
			String[] enrolleeIds = in.getStringArray("enrolleeIds");
			if(enrolleeIds != null)
			{
				for(String depId : enrolleeIds)
				{
					BPerson dep = new BPerson(depId);
					HrEmplDependent depType = ArahantSession.getHSU().createCriteria(HrEmplDependent.class).eq(HrEmplDependent.EMPLOYEE_ID, empId).eq(HrEmplDependent.PERSON, dep.getPerson()).first();
					if(depType != null && ((depType.getRelationshipType() == 'S' && bbq.getAppliesToSpouse() == 'N') || (depType.getRelationshipType() != 'S' && bbq.getAppliesToChildOther() == 'N')))
					{
						item.put(depId, "N/A");
					}
					else
					{
						BenefitAnswer depAnswer = bbq.getAnswer(dep.getPerson());
						if(depAnswer != null)
						{
							if(bbq.getAnswerType().equals(BenefitQuestion.TYPE_DATE + ""))
								item.put(depId, depAnswer.getDateAnswer());
							else if(bbq.getAnswerType().equals(BenefitQuestion.TYPE_NUMERIC + ""))
								item.put(depId, depAnswer.getNumericAnswer());
							else if(bbq.getAnswerType().equals(BenefitQuestion.TYPE_YES_NO_UNK + "") && includeExplanation
									&& depAnswer.getStringAnswer().length() > 0)
								item.put(depId, depAnswer.getStringAnswer().charAt(0));
							else
								item.put(depId, depAnswer.getStringAnswer());
						}
						else
						{
							item.put(dep.getPersonId(), "");
						}
					}
				}
			}
			dol.add(item);

			//if this is a Yes/No question with an explanation required, add the new line for the explanation
			if(includeExplanation)
			{
				DataObjectMap explanation = new DataObjectMap();
				if(!StringUtils.isEmpty(bbq.getExplanationText()))
					explanation.put("question", bbq.getExplanationText());
				else
					explanation.put("question", "If you answered 'Yes' on previous question, please explain: ");
				explanation.put("questionId", bbq.getId());
				explanation.put("answerType", "S");
				BenefitAnswer empAnswer = be != null ? bbq.getAnswer(be.getPerson()) : null;
				if(empAnswer != null && empAnswer.getStringAnswer().contains("::") && empAnswer.getStringAnswer().split("::").length > 1)
				{
					explanation.put(empId, empAnswer.getStringAnswer().split("::")[1]);
				}
				else
				{
					explanation.put(empId, "");
				}
				if(enrolleeIds != null)
				{
					for(String depId : enrolleeIds)
					{
						BPerson dep = new BPerson(depId);
						HrEmplDependent depType = ArahantSession.getHSU().createCriteria(HrEmplDependent.class).eq(HrEmplDependent.EMPLOYEE_ID, empId).eq(HrEmplDependent.PERSON, dep.getPerson()).first();
						if(depType != null && ((depType.getRelationshipType() == 'S' && bbq.getAppliesToSpouse() == 'N') || (depType.getRelationshipType() != 'S' && bbq.getAppliesToChildOther() == 'N')))
						{
							explanation.put(depId, "N/A");
						}
						else
						{
							BenefitAnswer depAnswer = bbq.getAnswer(dep.getPerson());
							if(depAnswer != null && depAnswer.getStringAnswer().contains("::") && depAnswer.getStringAnswer().split("::").length > 1)
							{
								explanation.put(depId, depAnswer.getStringAnswer().split("::")[1]);
							}
							else
							{
								explanation.put(depId, "");
							}
						}
					}
				}
				dol.add(explanation);
			}
		}
		out.put("personAnswerItems", dol);
	}

}
