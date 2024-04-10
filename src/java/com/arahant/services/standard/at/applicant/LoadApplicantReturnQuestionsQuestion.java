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

package com.arahant.services.standard.at.applicant;

import com.arahant.business.BApplicant;
import com.arahant.business.BApplicantQuestion;

public class LoadApplicantReturnQuestionsQuestion {
    private String id;
    private String question;
    private String textBasedAnswer;
    private boolean common;
    private String answerType;
    private double numberBasedAnswer;
    private String listBasedAnswerId;
    private String listBasedAnswer;

    public LoadApplicantReturnQuestionsQuestion() {
    }

    LoadApplicantReturnQuestionsQuestion(BApplicantQuestion q, BApplicant applicant) {
        id = q.getId();
        question = q.getQuestion();
        textBasedAnswer = q.getAnswer(applicant);
        numberBasedAnswer = q.getNumericAnswer(applicant);
        answerType = q.getAnswerType();
        listBasedAnswer = q.getListAnswer(applicant);
        listBasedAnswerId = q.getListAnswerId(applicant);
    }

    public String getListBasedAnswer() {
        return listBasedAnswer;
    }

    public void setListBasedAnswer(String listBasedAnswer) {
        this.listBasedAnswer = listBasedAnswer;
    }

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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean getCommon() {
        return common;
    }

    public void setCommon(boolean common) {
        this.common = common;
    }
}
