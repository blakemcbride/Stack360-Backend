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
package com.arahant.services.standard.hrConfig.wizardConfigurator;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;
import com.arahant.business.BBenefitQuestion;
import com.arahant.utils.ArahantSession;
import java.util.LinkedList;
import java.util.List;


/**
 *    
 *    
 *
 */
public class SaveQuestionInput extends TransmitInputBase {

	void setData(BBenefitQuestion bc)
	{
		
		bc.setQuestion(question);
		bc.setInactiveDate(inactiveDate);
		bc.setAnswerType(answerType);
		bc.setExplanationText(explanationText);
		bc.setInternalId(internalId);
		if(includeExplanation)
			bc.setIncludesExplanation('Y');
		else
			bc.setIncludesExplanation('N');
		if(appliesToChildOther)
			bc.setAppliesToChildOther('Y');
		else
			bc.setAppliesToChildOther('N');
		if(appliesToEmployee)
			bc.setAppliesToEmployee('Y');
		else
			bc.setAppliesToEmployee('N');
		if(appliesToSpouse)
			bc.setAppliesToSpouse('Y');
		else
			bc.setAppliesToSpouse('N');

		ArahantSession.getHSU().flush();
		List<String> ids=new LinkedList<String>();

		for (SaveQuestionInputItem i : getListAnswer())
		{
			if (isEmpty(i.getId()))
				ids.add(bc.addAnswer(i.getName()));
			else
				ids.add(bc.saveAnswer(i.getId(),i.getName()));

		}

		//delete the ones that aren't in id list
		bc.deleteAnswersExcluding(ids);
	}

	@Validation (table="benefit_question",column="explanation_text",required=false)
	private String explanationText;
	@Validation (table="benefit_question",column="question",required=true)
	private String question;
	@Validation (type="date",table="benefit_question",column="last_active_date",required=false)
	private int inactiveDate;
	@Validation (min=1,max=16,required=true)
	private String id;
	@Validation (required=true, min=1, max=1)
	private String answerType;
	@Validation (required=false)
	private SaveQuestionInputItem []listAnswer;
	@Validation (required=false)
	private boolean includeExplanation;
	@Validation (required=false)
	private boolean appliesToEmployee;
	@Validation (required=false)
	private boolean appliesToSpouse;
	@Validation (required=false)
	private boolean appliesToChildOther;
	@Validation (table="benefit_question",column="internal_id",required=false)
	private String internalId;

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}

	public String getExplanationText() {
		return explanationText;
	}

	public void setExplanationText(String explanationText) {
		this.explanationText = explanationText;
	}

	public boolean getAppliesToChildOther() {
		return appliesToChildOther;
	}

	public void setAppliesToChildOther(boolean appliesToChildOther) {
		this.appliesToChildOther = appliesToChildOther;
	}

	public boolean getAppliesToEmployee() {
		return appliesToEmployee;
	}

	public void setAppliesToEmployee(boolean appliesToEmployee) {
		this.appliesToEmployee = appliesToEmployee;
	}

	public boolean getAppliesToSpouse() {
		return appliesToSpouse;
	}

	public void setAppliesToSpouse(boolean appliesToSpouse) {
		this.appliesToSpouse = appliesToSpouse;
	}

	public boolean getIncludeExplanation() {
		return includeExplanation;
	}

	public void setIncludeExplanation(boolean includeExplanation) {
		this.includeExplanation = includeExplanation;
	}

	public SaveQuestionInputItem[] getListAnswer() {
		if (listAnswer==null)
			listAnswer=new SaveQuestionInputItem[0];
		return listAnswer;
	}

	public void setListAnswer(SaveQuestionInputItem[] listAnswer) {
		this.listAnswer = listAnswer;
	}

	public String getAnswerType() {
		return answerType;
	}

	public void setAnswerType(String answerType) {
		this.answerType = answerType;
	}

	public String getQuestion()
	{
		return question;
	}
	public void setQuestion(String question)
	{
		this.question=question;
	}
	public int getInactiveDate()
	{
		return inactiveDate;
	}
	public void setInactiveDate(int inactiveDate)
	{
		this.inactiveDate=inactiveDate;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}

}

	
