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

/**
 *
 * 
 *  questionDetails (array of id-string, question-string, answer-string)
            - questions should be ordered by question order
            - if answer is empty, question is not answered and there should not be a detail record
            - inactive questions should not be shown unless there is an answer already
            - id is the question id, not question detail id
 * 
 */
public class SaveApplicantInputQuestion {
	private String id;
	private String textBasedAnswer;
	private String answerType;
	private double numberBasedAnswer;
	private String listBasedAnswerId;

	public String getListBasedAnswerId() {
		return listBasedAnswerId;
	}

	public void setListBasedAnswerId(String listBasedAnswerId) {
		this.listBasedAnswerId = listBasedAnswerId;
	}

	public String getAnswerType() {
		return answerType;
	}

	public void setAnswerType(String answerType) {
		this.answerType = answerType;
	}

	public double getNumberBasedAnswer() {
		return numberBasedAnswer;
	}

	public void setNumberBasedAnswer(double numberBasedAnswer) {
		this.numberBasedAnswer = numberBasedAnswer;
	}

	public String getTextBasedAnswer() {
		return textBasedAnswer;
	}

	public void setTextBasedAnswer(String textBasedAnswer) {
		this.textBasedAnswer = textBasedAnswer;
	}

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
