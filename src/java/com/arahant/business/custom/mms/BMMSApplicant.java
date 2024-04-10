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

package com.arahant.business.custom.mms;

import com.arahant.beans.ApplicantAnswer;
import com.arahant.beans.ApplicantQuestion;
import com.arahant.beans.ApplicantQuestionChoice;
import com.arahant.business.BApplicant;
import com.arahant.business.BApplicantAnswer;
import com.arahant.business.BApplicantQuestionChoice;
import com.arahant.business.BProperty;
import com.arahant.utils.ArahantSession;
import java.util.List;

/**
 *
 */
public class BMMSApplicant extends BApplicant {

	public BMMSApplicant() {
		super();
	}

	public BMMSApplicant(String id) {
		super(id);
	}

	private String getAnswerId(String propName)
	{
		ApplicantAnswer ans=ArahantSession.getHSU().createCriteria(ApplicantAnswer.class)
			.eq(ApplicantAnswer.APPLICANT, applicant)
			.joinTo(ApplicantAnswer.QUESTION)
			.eq(ApplicantQuestion.ID, BProperty.get(propName))
			.first();

		if (ans==null)
			return "";

		return ans.getListAnswer().getApplicantQuestionChoiceId();
	}

	public String getCurrentPositionAnswerId() {
		return getAnswerId("CurrentPositionQuestionId");
	}

	public String getDpCertificationAnswerId() {
		return getAnswerId("DpCertificationQuestionId");
	}

	public String getHighestLicenseTypeAnswerId() {
		return getAnswerId("HighestLicenseTypeQuestionId");
	}

	public String getVesselTypeAnswerId() {
		return getAnswerId("VesselTypeQuestionId");
	}

	private void setAnswerId(String propName, String id)
	{
		//delete old answer if there
		ArahantSession.getHSU().createCriteria(ApplicantAnswer.class)
			.eq(ApplicantAnswer.APPLICANT, applicant)
			.joinTo(ApplicantAnswer.QUESTION)
			.eq(ApplicantQuestion.ID, BProperty.get(propName))
			.delete();

		BApplicantAnswer bans=new BApplicantAnswer();
		bans.create();
		bans.setQuestionId(BProperty.get(propName));
		bans.setApplicant(this);
		bans.setAnswer("L", "", 0, id);
		addPendingInsert(bans);
	}

        private void setAnswerIds(String propName, String ids[])
	{
		//delete old answer if there
		ArahantSession.getHSU().createCriteria(ApplicantAnswer.class)
			.eq(ApplicantAnswer.APPLICANT, applicant)
			.joinTo(ApplicantAnswer.QUESTION)
			.eq(ApplicantQuestion.ID, BProperty.get(propName))
			.delete();

                for (String id: ids)
                {
                    BApplicantAnswer bans=new BApplicantAnswer();
                    bans.create();
                    bans.setQuestionId(BProperty.get(propName));
                    bans.setApplicant(this);
                    bans.setAnswer("L", "", 0, id);
                    addPendingInsert(bans);
                }
	}

	public void setCurrentPositionAnswerId(String currentPositionAnswerId) {
		setAnswerId("CurrentPositionQuestionId", currentPositionAnswerId);
	}

	public void setHighestLicenseTypeAnswerId(String highestLicenseTypeAnswerId) {
		setAnswerId("HighestLicenseTypeQuestionId", highestLicenseTypeAnswerId);
	}

	public void setDpCertificationAnswerId(String dpCertificationAnswerId) {
		setAnswerId("DpCertificationQuestionId", dpCertificationAnswerId);
	}

	public void setVesselTypeAnswerId(String vesselTypeAnswerId) {
		setAnswerId("VesselTypeQuestionId", vesselTypeAnswerId);
	}

        public void setVesselTypeAnswerIds(String vesselTypeAnswerIds[]) {
		setAnswerIds("VesselTypeQuestionId", vesselTypeAnswerIds);
	}


        public BApplicantQuestionChoice[] getVesselNotSelectedChoices() {

            List chosen=ArahantSession.getHSU().createCriteria(ApplicantQuestionChoice.class)
                    .selectFields(ApplicantQuestionChoice.ID)
                    .joinTo(ApplicantQuestionChoice.ANSWERS)
                    .eq(ApplicantAnswer.APPLICANT, applicant)
                    .joinTo(ApplicantAnswer.QUESTION)
                    .eq(ApplicantQuestion.ID, BProperty.get("VesselTypeQuestionId"))
                    .list();


            List<ApplicantQuestionChoice> choices=ArahantSession.getHSU().createCriteria(ApplicantQuestionChoice.class)
                    .notIn(ApplicantQuestionChoice.ID, chosen)
                    .joinTo(ApplicantQuestionChoice.QUESTION)
                    .eq(ApplicantQuestion.ID,  BProperty.get("VesselTypeQuestionId"))
                    .list();

            return BApplicantQuestionChoice.makeArray(choices);
        }

        public BApplicantQuestionChoice[] getVesselAnswers() {

            List<ApplicantQuestionChoice> ans=ArahantSession.getHSU().createCriteria(ApplicantQuestionChoice.class)
                        .joinTo(ApplicantQuestionChoice.ANSWERS)
			.eq(ApplicantAnswer.APPLICANT, applicant)
			.joinTo(ApplicantAnswer.QUESTION)
			.eq(ApplicantQuestion.ID, BProperty.get("VesselTypeQuestionId"))
			.list();

            return BApplicantQuestionChoice.makeArray(ans);

        }



}
