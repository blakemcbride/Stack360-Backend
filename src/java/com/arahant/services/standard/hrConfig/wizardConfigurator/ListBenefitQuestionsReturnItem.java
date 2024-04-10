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
import com.arahant.business.BBenefitQuestion;
import com.arahant.utils.DateUtils;


/**
 * 
 *
 *
 */
public class ListBenefitQuestionsReturnItem {
	
	public ListBenefitQuestionsReturnItem()
	{
	}

	ListBenefitQuestionsReturnItem (final BBenefitQuestion bc)
	{
		question = bc.getQuestion();
		benefitQuestionId = bc.getId();
		answerType = bc.getAnswerType();
		inactiveDate = bc.getInactiveDate();
		inactive = (inactiveDate != 0 && inactiveDate < DateUtils.now());
		includeExplanation = bc.getIncludesExplanation() + "";
		appliesToEmployee = bc.getAppliesToEmployee() + "";
		appliesToChildOther = bc.getAppliesToChildOther() + "";
		appliesToSpouse = bc.getAppliesToSpouse() + "";
		explanationText = bc.getExplanationText();
		internalId = bc.getInternalId();
	}

	private String explanationText;
	private String question;
	private String benefitQuestionId;
	private String answerType;
	private int inactiveDate;
	private boolean inactive;
	private String includeExplanation;
	private String appliesToEmployee;
	private String appliesToSpouse;
	private String appliesToChildOther;
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

	public String getAppliesToChildOther() {
		return appliesToChildOther;
	}

	public void setAppliesToChildOther(String appliesToChildOther) {
		this.appliesToChildOther = appliesToChildOther;
	}

	public String getAppliesToEmployee() {
		return appliesToEmployee;
	}

	public void setAppliesToEmployee(String appliesToEmployee) {
		this.appliesToEmployee = appliesToEmployee;
	}

	public String getAppliesToSpouse() {
		return appliesToSpouse;
	}

	public void setAppliesToSpouse(String appliesToSpouse) {
		this.appliesToSpouse = appliesToSpouse;
	}

	public String getIncludeExplanation() {
		return includeExplanation;
	}

	public void setIncludeExplanation(String includeExplanation) {
		this.includeExplanation = includeExplanation;
	}


	public boolean getInactive() {
		return inactive;
	}

	public void setInactive(boolean inactive) {
		this.inactive = inactive;
	}

	public int getInactiveDate() {
		return inactiveDate;
	}

	public void setInactiveDate(int inactiveDate) {
		this.inactiveDate = inactiveDate;
	}

	public String getAnswerType() {
		return answerType;
	}

	public void setAnswerType(String answerType) {
		this.answerType = answerType;
	}

	public String getBenefitQuestionId() {
		return benefitQuestionId;
	}

	public void setBenefitQuestionId(String benefitQuestionId) {
		this.benefitQuestionId = benefitQuestionId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

}

	
