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
 *
 *  Created on Feb 8, 2007
 */

package com.arahant.services.standard.at.applicantQuestion;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BApplicantQuestion;
import com.arahant.annotation.Validation;

import java.util.LinkedList;
import java.util.List;

public class SaveQuestionInput extends TransmitInputBase {

    @Validation(table = "applicant_question", column = "question", required = true)
    private String question;
    private String positionId;
    @Validation(type = "date", table = "applicant_question", column = "last_active_date", required = false)
    private int inactiveDate;
    @Validation(min = 1, max = 16, required = true)
    private String id;
    @Validation(required = true, min = 1, max = 1)
    private String answerType;
    @Validation(required = false)
    private boolean internalUse;
    @Validation(required = false)
    private SaveQuestionInputItem[] listAnswer;

    void setData(BApplicantQuestion bc) {
        bc.setQuestion(question);
        bc.setInactiveDate(inactiveDate);
        bc.setAnswerType(answerType);
        bc.setInternalUse(internalUse);
        bc.setHRPosition(positionId);

        List<String> ids = new LinkedList<String>();

        for (SaveQuestionInputItem i : getListAnswer()) {
            if (isEmpty(i.getId()))
                ids.add(bc.addAnswer(i.getName()));
            else
                ids.add(bc.saveAnswer(i.getId(), i.getName()));
        }

        //delete the ones that aren't in id list
        bc.deleteAnswersExcluding(ids);
    }

    public SaveQuestionInputItem[] getListAnswer() {
        if (listAnswer == null)
            listAnswer = new SaveQuestionInputItem[0];
        return listAnswer;
    }

    public void setListAnswer(SaveQuestionInputItem[] listAnswer) {
        this.listAnswer = listAnswer;
    }

    public boolean isInternalUse() {
        return internalUse;
    }

    public void setInternalUse(boolean internalUse) {
        this.internalUse = internalUse;
    }

    public String getAnswerType() {
        return answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public int getInactiveDate() {
        return inactiveDate;
    }

    public void setInactiveDate(int inactiveDate) {
        this.inactiveDate = inactiveDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}

	
