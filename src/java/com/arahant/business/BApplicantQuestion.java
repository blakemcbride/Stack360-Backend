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

package com.arahant.business;

import com.arahant.beans.ApplicantAnswer;
import com.arahant.beans.ApplicantQuestion;
import com.arahant.beans.ApplicantQuestionChoice;
import com.arahant.beans.HrPosition;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.util.List;

public class BApplicantQuestion extends SimpleBusinessObjectBase<ApplicantQuestion> {

    public static void delete(String[] ids) {
        for (String id : ids)
            new BApplicantQuestion(id).delete();
    }

    @Override
    public void delete() throws ArahantDeleteException {
        ArahantSession.getHSU().createCriteria(ApplicantQuestionChoice.class)
                .eq(ApplicantQuestionChoice.QUESTION, bean)
                .delete();
        super.delete();
    }

    static BApplicantQuestion[] makeArray(List<ApplicantQuestion> l) {
        BApplicantQuestion[] ret = new BApplicantQuestion[l.size()];
        for (int loop = 0; loop < ret.length; loop++)
            ret[loop] = new BApplicantQuestion(l.get(loop));
        return ret;
    }

    public BApplicantQuestion(ApplicantQuestion o) {
        super();
        bean = o;
    }

    public BApplicantQuestion(String id) {
        super(id);
    }

    public BApplicantQuestion() {
        super();
    }

    //	@Override
    @Override
    public String create() throws ArahantException {
        bean = new ApplicantQuestion();
        bean.setQuestionOrder((short) 1000);//set default
        return bean.generateId();
    }

    @Override
    public void insert() throws ArahantException {
        bean.setCompany(ArahantSession.getHSU().getCurrentCompany());
        ArahantSession.getHSU().insert(bean);
    }

    public void deleteAnswersExcluding(List<String> ids) {
        ArahantSession.getHSU().createCriteria(ApplicantQuestionChoice.class)
                .eq(ApplicantQuestionChoice.QUESTION, bean)
                .notIn(ApplicantQuestionChoice.ID, ids)
                .delete();
    }

    public String getAnswer(BApplicant contact) {
        if (contact == null)
            return "";

        ApplicantAnswer aa = ArahantSession.getHSU().createCriteria(ApplicantAnswer.class)
                .eq(ApplicantAnswer.APPLICANT, contact.applicant)
                .eq(ApplicantAnswer.QUESTION, bean)
                .first();

        if (aa == null)
            return "";
        return aa.getStringAnswer();
    }

    public String getListAnswer(BApplicant contact) {
        if (contact == null)
            return "";

        ApplicantAnswer aa = ArahantSession.getHSU().createCriteria(ApplicantAnswer.class)
                .eq(ApplicantAnswer.APPLICANT, contact.applicant)
                .eq(ApplicantAnswer.QUESTION, bean)
                .first();

        if (aa == null)
            return "";
        if (aa.getListAnswer() == null)
            return "";
        return aa.getListAnswer().getDescription();
    }

    public String getListAnswerId(BApplicant contact) {
        if (contact == null)
            return "";

        ApplicantAnswer aa = ArahantSession.getHSU().createCriteria(ApplicantAnswer.class)
                .eq(ApplicantAnswer.APPLICANT, contact.applicant)
                .eq(ApplicantAnswer.QUESTION, bean)
                .first();

        if (aa == null)
            return "";
        if (aa.getListAnswer() == null)
            return "";
        return aa.getListAnswer().getApplicantQuestionChoiceId();
    }

    public double getNumericAnswer(BApplicant contact) {
        if (contact == null)
            return 0;

        ApplicantAnswer aa = ArahantSession.getHSU().createCriteria(ApplicantAnswer.class)
                .eq(ApplicantAnswer.APPLICANT, contact.applicant)
                .eq(ApplicantAnswer.QUESTION, bean)
                .first();

        if (aa == null)
            return 0;

        if (bean.getDataType() == ApplicantQuestion.TYPE_DATE)
            return aa.getDateAnswer().doubleValue();

        return aa.getNumericAnswer().doubleValue();
    }

    public String getAnswerType() {
        return bean.getDataType() + "";
    }

    public String getId() {
        return bean.getApplicantQuestionId();
    }

    public int getInactiveDate() {
        return bean.getLastActiveDate();
    }

    public String getQuestion() {
        return bean.getQuestion();
    }

    @Override
    public void load(String key) throws ArahantException {
        bean = ArahantSession.getHSU().get(ApplicantQuestion.class, key);
    }

    public void setAnswerType(char type) {
        bean.setDataType(type);
    }

    public void setAnswerType(String answerType) {
        bean.setDataType(answerType.charAt(0));
    }

    public void setInactiveDate(int inactiveDate) {
        bean.setLastActiveDate(inactiveDate);
    }

    public BHRPosition getHRPosition() {
        return new BHRPosition(bean.getHrPosition());
    }

    public void setHRPosition(BHRPosition hrPosition) {
        bean.setHrPosition(hrPosition.getBean());
    }

    public void setHRPosition(String positionId) {
        bean.setHrPosition(new BHRPosition(positionId).getBean());
    }

    public void setMoveUp(boolean moveUp) {
        if (moveUp)
            moveUp();
        else
            moveDown();
    }

    public void setQuestion(String question) {
        bean.setQuestion(question);
    }

    public static BApplicantQuestion[] list(String positionId, int activeType, int cap) {
        HibernateCriteriaUtil<ApplicantQuestion> hcu = ArahantSession.getHSU()
                .createCriteria(ApplicantQuestion.class)
                .setMaxResults(cap)
                .orderBy(ApplicantQuestion.SEQ);

        switch (activeType) {
            case 1:
                hcu.geOrEq(ApplicantQuestion.LAST_ACTIVE_DATE, DateUtils.today(), 0);
                break;
            case 2:
                hcu.leOrEq(ApplicantQuestion.LAST_ACTIVE_DATE, DateUtils.today(), 0);
                break;
        }
        if (positionId != null && !positionId.isEmpty())
            hcu.eq(ApplicantQuestion.HRPOSITION, new BHRPosition(positionId).getBean());
        else
            hcu.eq(ApplicantQuestion.HRPOSITION, (HrPosition) null);
        return makeArray(hcu.list());
    }

    public static BApplicantQuestion[] searchExternal(String positionId, int activeType, String question, int cap) {
        HibernateCriteriaUtil<ApplicantQuestion> hcu = ArahantSession.getHSU()
                .createCriteria(ApplicantQuestion.class)
                .eq(ApplicantQuestion.INTERNAL_USE, 'N')
                .like(ApplicantQuestion.QUESTION, question)
                .setMaxResults(cap)
                .orderBy(ApplicantQuestion.SEQ);

        switch (activeType) {
            case 1:
                hcu.geOrEq(ApplicantQuestion.LAST_ACTIVE_DATE, DateUtils.today(), 0);
                break;
            case 2:
                hcu.leOrEq(ApplicantQuestion.LAST_ACTIVE_DATE, DateUtils.today(), 0);
                break;
        }
        if (positionId != null && !positionId.isEmpty())
            hcu.eq(ApplicantQuestion.HRPOSITION, new BHRPosition(positionId).getBean());
        else
            hcu.eq(ApplicantQuestion.HRPOSITION, (HrPosition) null);
        return makeArray(hcu.list());
    }

    public static BApplicantQuestion[] listExternal(String positionId, BApplicant cont) {
        HibernateCriteriaUtil<ApplicantQuestion> hcu1 = ArahantSession.getHSU()
                .createCriteria(ApplicantQuestion.class)
                .eq(ApplicantQuestion.INTERNAL_USE, 'N')
                .orderBy(ApplicantQuestion.SEQ);

        hcu1.geOrEq(ApplicantQuestion.LAST_ACTIVE_DATE, DateUtils.now(), 0);

        HibernateCriteriaUtil<ApplicantQuestion> hcu = ArahantSession.getHSU()
                .createCriteria(ApplicantQuestion.class)
                .eq(ApplicantQuestion.INTERNAL_USE, 'N')
                .orderBy(ApplicantQuestion.SEQ);

        hcu.geOrEq(ApplicantQuestion.LAST_ACTIVE_DATE, DateUtils.now(), 0);

        List<ApplicantQuestion> l = hcu1.list();
        l.addAll(hcu.list());

        if (cont != null) {
            List<ApplicantAnswer> ansList = ArahantSession.getHSU()
                    .createCriteria(ApplicantAnswer.class)
                    .eq(ApplicantAnswer.APPLICANT, cont.applicant)
                    .joinTo(ApplicantAnswer.QUESTION)
                    .ltAndNeq(ApplicantQuestion.LAST_ACTIVE_DATE, DateUtils.now(), 0)
                    .orderBy(ApplicantQuestion.SEQ)
                    .list();

            for (ApplicantAnswer ans : ansList)
                l.add(ans.getApplicantQuestion());

        }
        return makeArray(l);
    }

    public static BApplicantQuestion[] list(String jobType, BApplicant cont) {
        HibernateCriteriaUtil<ApplicantQuestion> hcu1 = ArahantSession.getHSU()
                .createCriteria(ApplicantQuestion.class)
                .orderBy(ApplicantQuestion.SEQ);

        hcu1.geOrEq(ApplicantQuestion.LAST_ACTIVE_DATE, DateUtils.now(), 0);

        HibernateCriteriaUtil<ApplicantQuestion> hcu = ArahantSession.getHSU()
                .createCriteria(ApplicantQuestion.class)
                .orderBy(ApplicantQuestion.SEQ);

        hcu.geOrEq(ApplicantQuestion.LAST_ACTIVE_DATE, DateUtils.now(), 0);

        List<ApplicantQuestion> l = hcu1.list();
        l.addAll(hcu.list());

        if (cont != null) {
            List<ApplicantAnswer> ansList = ArahantSession.getHSU()
                    .createCriteria(ApplicantAnswer.class)
                    .eq(ApplicantAnswer.APPLICANT, cont.applicant)
                    .joinTo(ApplicantAnswer.QUESTION)
                    .ltAndNeq(ApplicantQuestion.LAST_ACTIVE_DATE, DateUtils.now(), 0)
                    .orderBy(ApplicantQuestion.SEQ)
                    .list();

            for (ApplicantAnswer ans : ansList)
                l.add(ans.getApplicantQuestion());

        }
        return makeArray(l);
    }

    public void moveUp() {
        HibernateSessionUtil hsu = ArahantSession.getHSU();
        Connection db = KissConnection.get();
        try {
            Record rec = db.fetchOne("select * from applicant_question where applicant_question_id=?", bean.getApplicantQuestionId());
            short order = rec.getShort("question_order");
            String positionId = rec.getString("position_id");
            Record prec;
            if (positionId != null)
                prec = db.fetchOne("select * from applicant_question where company_id=? and position_id = ? and question_order < ? order by question_order desc", hsu.getCurrentCompany().getCompanyId(), positionId, order);
            else
                prec = db.fetchOne("select * from applicant_question where company_id=? and position_id is null and question_order < ? order by question_order desc", hsu.getCurrentCompany().getCompanyId(), order);
            if (prec == null)
                return;
            short newOrder = prec.getShort("question_order");
            prec.set("question_order", -1);
            prec.update();
            rec.set("question_order", newOrder);
            prec.set("question_order", order);
            rec.update();
            prec.update();
        } catch (Exception throwables) {
            throw new ArahantDeleteException(throwables);
        }
    }

    public void moveDown() {
        HibernateSessionUtil hsu = ArahantSession.getHSU();
        Connection db = KissConnection.get();
        try {
            Record rec = db.fetchOne("select * from applicant_question where applicant_question_id=?", bean.getApplicantQuestionId());
            short order = rec.getShort("question_order");
            String positionId = rec.getString("position_id");
            Record nrec;
            if (positionId != null)
                nrec = db.fetchOne("select * from applicant_question where company_id=? and position_id = ? and question_order > ? order by question_order", hsu.getCurrentCompany().getCompanyId(), positionId, order);
            else
                nrec = db.fetchOne("select * from applicant_question where company_id=? and position_id is null and question_order > ? order by question_order", hsu.getCurrentCompany().getCompanyId(), order);
            if (nrec == null)
                return;
            rec.set("question_order", -1);
            rec.update();
            rec.set("question_order", nrec.getShort("question_order"));
            nrec.set("question_order", order);
            nrec.update();
            rec.update();
        } catch (Exception throwables) {
            throw new ArahantDeleteException(throwables);
        }
    }

    public void setAddAfterId(String addAfterId) {
        if (isEmpty(addAfterId)) {
            bean.setQuestionOrder((short) ArahantSession.getHSU().createCriteria(ApplicantQuestion.class)
                    .count());
        } else {
            BApplicantQuestion bcq = new BApplicantQuestion(addAfterId);
            int initialSequence = bcq.bean.getQuestionOrder() + 1;
            /*
            bean.setQuestionOrder((short) ArahantSession.getHSU().createCriteria(ApplicantQuestion.class)
                    ).count());
             */

            ArahantSession.getHSU().insert(bean);
            //move up until it gets there
            while (bean.getQuestionOrder() != initialSequence)
                moveUp();
        }

    }

    public char getDataType() {
        return bean.getDataType();
    }

    public boolean getInternalUse() {
        return bean.getInternalUse() == 'Y';
    }

    public void setInternalUse(boolean internalUse) {
        bean.setInternalUse(internalUse ? 'Y' : 'N');
    }

    public String addAnswer(String description) {
        BApplicantQuestionChoice c = new BApplicantQuestionChoice();
        String ret = c.create();
        c.setDescription(description);
        c.setQuestion(bean);
        addPendingInsert(c);

        return ret;
    }

    public String saveAnswer(String id, String description) {
        BApplicantQuestionChoice c = new BApplicantQuestionChoice(id);
        c.setDescription(description);
        addPendingUpdate(c);

        return id;
    }

}
