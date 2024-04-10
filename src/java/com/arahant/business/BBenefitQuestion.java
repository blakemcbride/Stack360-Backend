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

package com.arahant.business;

import com.arahant.beans.BenefitAnswer;
import com.arahant.beans.BenefitQuestion;
import com.arahant.beans.BenefitQuestionChoice;
import com.arahant.beans.HrBenefit;
import com.arahant.beans.Person;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.util.List;

/**
 *
 */
public class BBenefitQuestion extends SimpleBusinessObjectBase<BenefitQuestion> {

	public static void delete(String[] ids) {
		for (String id : ids)
			new BBenefitQuestion(id).delete();
	}

	@Override
	public void delete() throws ArahantDeleteException {
		ArahantSession.getHSU().createCriteria(BenefitQuestionChoice.class)
			.eq(BenefitQuestionChoice.QUESTION, bean)
			.delete();
		super.delete();

	}

	static BBenefitQuestion[] makeArray(List<BenefitQuestion> l) {
		BBenefitQuestion [] ret=new BBenefitQuestion[l.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=new BBenefitQuestion(l.get(loop));
		return ret;
	}

	public BBenefitQuestion(BenefitQuestion o) {
		super();
		bean=o;
	}

	public BBenefitQuestion(String id) {
		super(id);
		load(id);
	}

	public BBenefitQuestion() {
		super();
	}

//	@Override
	@Override
	public String create() throws ArahantException {
		bean=new BenefitQuestion();
		bean.setQuestionOrder((short)1000);//set default
		return bean.generateId();
	}

	@Override
    public void insert() throws ArahantException {
        ArahantSession.getHSU().insert(bean);
    }

	public void deleteAnswersExcluding(List<String> ids) {
		ArahantSession.getHSU().createCriteria(BenefitQuestionChoice.class)
			.eq(BenefitQuestionChoice.QUESTION, bean)
			.notIn(BenefitQuestionChoice.ID, ids)
			.delete();
	}

	public BenefitAnswer getAnswer(Person p) {
		if (p==null)
			return null;
		
		BenefitAnswer aa=ArahantSession.getHSU().createCriteria(BenefitAnswer.class)
			.eq(BenefitAnswer.PERSON, p)
			.eq(BenefitAnswer.QUESTION, bean)
			.first();
		
		if (aa==null)
			return null;
		return aa;
	}

	public double getNumericAnswer(Person p) {
		if (p==null)
			return 0;
		
		BenefitAnswer aa=ArahantSession.getHSU().createCriteria(BenefitAnswer.class)
			.eq(BenefitAnswer.PERSON, p)
			.eq(BenefitAnswer.QUESTION, bean)
			.first();
		
		if (aa==null)
			return 0;
		
		if (bean.getDataType()==BenefitQuestion.TYPE_DATE)
			return aa.getDateAnswer().doubleValue();
		
		return aa.getNumericAnswer().doubleValue();
	}

	public String getAnswerType() {
		return bean.getDataType()+"";
	}

	public String getId() {
		return bean.getBenefitQuestionId();
	}

	public int getInactiveDate() {
		return bean.getLastActiveDate();
	}

	public String getQuestion() {
		return bean.getQuestion();
	}
	
	public char getAppliesToChildOther() {
		return bean.getAppliesToChildOther();
	}

	public void setAppliesToChildOther(char appliesToChildOther) {
		bean.setAppliesToChildOther(appliesToChildOther);
	}

	public char getAppliesToEmployee() {
		return bean.getAppliesToEmployee();
	}

	public void setAppliesToEmployee(char appliesToEmployee) {
		bean.setAppliesToEmployee(appliesToEmployee);
	}

	public char getAppliesToSpouse() {
		return bean.getAppliesToSpouse();
	}

	public void setAppliesToSpouse(char appliesToSpouse) {
		bean.setAppliesToSpouse(appliesToSpouse);
	}

	public char getIncludesExplanation() {
		return bean.getIncludesExplanation();
	}

	public void setIncludesExplanation(char includesExplanation) {
		bean.setIncludesExplanation(includesExplanation);
	}
	public String getExplanationText() {
		return bean.getExplanationText();
	}

	public void setExplanationText(String explanationText) {
		bean.setExplanationText(explanationText);
	}

	public String getInternalId() {
		return bean.getInternalId();
	}

	public void setInternalId(String internalId) {
		bean.setInternalId(internalId);
	}

	@Override
	public void load(String key) throws ArahantException {
		bean=ArahantSession.getHSU().get(BenefitQuestion.class, key);
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

	public void setMoveUp(boolean moveUp) {
		if (moveUp)
			moveUp();
		else
			moveDown();
	}

	public void setQuestion(String question) {
		bean.setQuestion(question);
	}

	public static BBenefitQuestion[] searchExternal (int activeType, String question, int cap)
	{
		HibernateCriteriaUtil<BenefitQuestion> hcu=ArahantSession.getHSU()
				.createCriteria(BenefitQuestion.class)
				.eq(BenefitQuestion.INTERNAL_USE,'N')
				.like(BenefitQuestion.QUESTION, question)
				.setMaxResults(cap)
				.orderBy(BenefitQuestion.SEQ);

		switch(activeType)
		{
			case 1 : hcu.eq(BenefitQuestion.LAST_ACTIVE_DATE, 0);
					break;
			case 2 : hcu.ne(BenefitQuestion.LAST_ACTIVE_DATE, 0);
					break;
		}

		return makeArray(hcu.list());
	}

	public static BBenefitQuestion[] listExternal (Person p)
	{
		HibernateCriteriaUtil<BenefitQuestion> hcu1=ArahantSession.getHSU()
				.createCriteria(BenefitQuestion.class)
				.eq(BenefitQuestion.INTERNAL_USE,'N')
				.orderBy(BenefitQuestion.SEQ);

		hcu1.geOrEq(BenefitQuestion.LAST_ACTIVE_DATE, DateUtils.now(),0);

		HibernateCriteriaUtil<BenefitQuestion> hcu=ArahantSession.getHSU()
				.createCriteria(BenefitQuestion.class)
				.eq(BenefitQuestion.INTERNAL_USE,'N')
				.orderBy(BenefitQuestion.SEQ);

		hcu.geOrEq(BenefitQuestion.LAST_ACTIVE_DATE, DateUtils.now(),0);

		List<BenefitQuestion> l=hcu1.list();
		l.addAll(hcu.list());

		if (p!=null)
		{
			List<BenefitAnswer> ansList=ArahantSession.getHSU()
				.createCriteria(BenefitAnswer.class)
				.eq(BenefitAnswer.PERSON, p)
				.joinTo(BenefitAnswer.QUESTION)
				.ltAndNeq(BenefitQuestion.LAST_ACTIVE_DATE, DateUtils.now(), 0)
				.orderBy(BenefitQuestion.SEQ)
				.list();

			for (BenefitAnswer ans : ansList)
				l.add(ans.getBenefitQuestion());

		}

		return makeArray(l);
	}

	public static BBenefitQuestion[] list(String benefitId, Person p, boolean activeOnly)
	{
		HibernateCriteriaUtil<BenefitQuestion> hcu1=ArahantSession.getHSU()
				.createCriteria(BenefitQuestion.class)
				.orderBy(BenefitQuestion.SEQ);

		if(!isEmpty(benefitId))
			hcu1.eq(BenefitQuestion.BENEFIT_ID, benefitId);
		
		if(activeOnly)
			hcu1.geOrEq(BenefitQuestion.LAST_ACTIVE_DATE, DateUtils.now(),0);
			
		List<BenefitQuestion> l=hcu1.list();
		
		if (p!=null)
		{
			List<BenefitAnswer> ansList=ArahantSession.getHSU()
				.createCriteria(BenefitAnswer.class)
				.eq(BenefitAnswer.PERSON, p)
				.joinTo(BenefitAnswer.QUESTION)
				.ltAndNeq(BenefitQuestion.LAST_ACTIVE_DATE, DateUtils.now(), 0)
				.orderBy(BenefitQuestion.SEQ)
				.list();
			
			for (BenefitAnswer ans : ansList)
				l.add(ans.getBenefitQuestion());
					
		}
		
		return makeArray(l);
	}

	
	public void moveUp()
	{
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		short questionOrder = bean.getQuestionOrder();

		if (questionOrder > 0)
		{

			BenefitQuestion pvj=hsu.createCriteria(BenefitQuestion.class)
				.eq(BenefitQuestion.SEQ, (short)(questionOrder-1))
				.first();

			pvj.setQuestionOrder((short)99999);
			hsu.saveOrUpdate(pvj);
			hsu.flush();
			bean.setQuestionOrder((short)(questionOrder-1));
			hsu.saveOrUpdate(bean);
			hsu.flush();
			pvj.setQuestionOrder(questionOrder);
			hsu.saveOrUpdate(pvj);
		}
		else //shift them all
		{
			List <BenefitQuestion> l=hsu.createCriteria(BenefitQuestion.class)
				.orderBy(BenefitQuestion.SEQ)
				.list();
			
			l.get(0).setQuestionOrder((short)99999);
			hsu.saveOrUpdate(l.get(0));
			hsu.flush();
			for (int loop=1;loop<l.size();loop++)
			{
				l.get(loop).setQuestionOrder((short)(l.get(loop).getQuestionOrder()-1));
				hsu.saveOrUpdate(l.get(loop));
				hsu.flush();
			}
			
			l.get(0).setQuestionOrder((short)(l.size()-1));
			hsu.saveOrUpdate(l.get(0));
		}
	}
	
	public void moveDown()
	{
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		if (bean.getQuestionOrder()!=hsu.createCriteria(BenefitQuestion.class)
				.count()-1)
		{
			BenefitQuestion pvj=hsu.createCriteria(BenefitQuestion.class)
				.eq(BenefitQuestion.SEQ, (short)(bean.getQuestionOrder()+1))
				.first();

			short temp=bean.getQuestionOrder();
			pvj.setQuestionOrder((short)999999);
			hsu.saveOrUpdate(pvj);
			hsu.flush();
			bean.setQuestionOrder((short)(bean.getQuestionOrder()+1));
			hsu.saveOrUpdate(bean);
			pvj.setQuestionOrder(temp);
			hsu.saveOrUpdate(pvj);
		}
		else //shift them all
		{
			List <BenefitQuestion> l=hsu.createCriteria(BenefitQuestion.class)
				.orderBy(BenefitQuestion.SEQ)
				.list();
			
			l.get(l.size()-1).setQuestionOrder((short)99999);
			hsu.saveOrUpdate(l.get(l.size()-1));
			hsu.flush();
			
			for (int loop=l.size()-1;loop>-1;loop--)
			{
				l.get(loop).setQuestionOrder((short)(loop+1));
				hsu.saveOrUpdate(l.get(loop));
				hsu.flush();
			}
			
			l.get(l.size()-1).setQuestionOrder((short)0);
			hsu.saveOrUpdate(l.get(l.size()-1));
		}
	}

	public void setAddAfterId(String addAfterId) {
		if (isEmpty(addAfterId))
		{
			bean.setQuestionOrder((short) ArahantSession.getHSU().createCriteria(BenefitQuestion.class)
					.count());
		}
		else
		{
			BBenefitQuestion bcq=new BBenefitQuestion(addAfterId);
			int initialSequence=bcq.bean.getQuestionOrder() + 1;
			bean.setQuestionOrder((short) ArahantSession.getHSU().createCriteria(BenefitQuestion.class).count());
			
			ArahantSession.getHSU().insert(bean);
			//move up until it gets there
			while (bean.getQuestionOrder()!=initialSequence)
				moveUp();
		}
		
	}

	public char getDataType() {
		return bean.getDataType();
	}

	public String addAnswer(String description)
	{
		BBenefitQuestionChoice c=new BBenefitQuestionChoice();
		String ret=c.create();
		c.setDescription(description);
		c.setQuestion(bean);
		//addPendingInsert(c);
		c.insert();
		return ret;
	}
	
	public String saveAnswer(String id, String description)
	{
		BBenefitQuestionChoice c=new BBenefitQuestionChoice(id);
		c.setDescription(description);
		//addPendingUpdate(c);
		c.update();
		return id;
	}

	public void setBenefit(String benefitId) {
		bean.setBenefit(ArahantSession.getHSU().get(HrBenefit.class, benefitId));
	}

	public void setBenefit(HrBenefit benefit) {
		bean.setBenefit(benefit);
	}

	public HrBenefit getBenefit() {
		return bean.getBenefit();
	}

	public String getBenefitId() {
		return bean.getBenefitId();
	}

	public List<BenefitQuestionChoice> getListOptions() {
		return ArahantSession.getHSU().createCriteria(BenefitQuestionChoice.class).eq(BenefitQuestionChoice.QUESTION, bean).list();
	}

	public void saveAnswerForPerson(String personId, Object value) {
		BenefitAnswer ba = ArahantSession.getHSU().createCriteria(BenefitAnswer.class).eq(BenefitAnswer.PERSON_ID, personId).eq(BenefitAnswer.QUESTION, bean).first();
		BBenefitAnswer bba = new BBenefitAnswer();
		String insertOrUpdate = "";
		if(ba != null)
		{
			bba = new BBenefitAnswer(ba);
			if(getAnswerType().equals(BenefitQuestion.TYPE_DATE + ""))
				bba.setDateAnswer(Integer.parseInt(value.toString()));
			else if(getAnswerType().equals(BenefitQuestion.TYPE_NUMERIC + ""))
				bba.setNumericAnswer(Double.parseDouble(value.toString()));
			else if(getAnswerType().equals(BenefitQuestion.TYPE_STRING + ""))
				bba.setStringAnswer(value.toString());
			else if(getAnswerType().equals(BenefitQuestion.TYPE_YES_NO_UNK + ""))
				bba.setStringAnswer(value.toString());
			else if(getAnswerType().equals(BenefitQuestion.TYPE_LIST + ""))
				bba.setAnswer(value.toString());
			insertOrUpdate = "update";
		}
		else
		{
			bba.create();
			bba.setPerson(ArahantSession.getHSU().get(Person.class, personId));
			bba.setQuestionId(getId());
			insertOrUpdate = "insert";
		}
		if(getAnswerType().equals(BenefitQuestion.TYPE_DATE + ""))
			bba.setDateAnswer(Integer.parseInt(value.toString()));
		else if(getAnswerType().equals(BenefitQuestion.TYPE_NUMERIC + ""))
			bba.setNumericAnswer(Double.parseDouble(value.toString()));
		else if(getAnswerType().equals(BenefitQuestion.TYPE_STRING + ""))
			bba.setStringAnswer(value.toString());
		else if(getAnswerType().equals(BenefitQuestion.TYPE_YES_NO_UNK + ""))
			bba.setStringAnswer(value.toString());
		else if(getAnswerType().equals(BenefitQuestion.TYPE_LIST + ""))
			bba.setAnswer(value.toString());

		if(insertOrUpdate.equals("insert"))
			bba.insert();
		else if(insertOrUpdate.equals("update"))
			bba.update();
	}
}
