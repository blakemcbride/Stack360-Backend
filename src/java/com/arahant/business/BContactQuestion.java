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

import com.arahant.beans.CompanyDetail;
import com.arahant.beans.ContactQuestion;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;

import java.util.List;

/**
 *
 */
public class BContactQuestion extends SimpleBusinessObjectBase<ContactQuestion> 
{

	public String create() throws ArahantException {
		bean=new ContactQuestion();
		return bean.generateId();
	}

	@Override
    public void insert() throws ArahantException {
		//bean.setCompany(ArahantSession.getHSU().getCurrentCompany());
        ArahantSession.getHSU().insert(bean);
    }

	public String getId() {
		return bean.getContactQuestionId();
	}

	public String getQuestion() {
		return bean.getQuestion();
	}

	public int getSequence() {
		return bean.getSeqno();
	}

	public void load(String key) throws ArahantException {
		bean=ArahantSession.getHSU().get(ContactQuestion.class, key);
	}
	
	public BContactQuestion()
	{
		super();
	}
	
	public BContactQuestion(String id)
	{
		super(id);
	}
	
	public BContactQuestion(ContactQuestion o)
	{
		super();
		bean=o;
	}
	
	static BContactQuestion [] makeArray(List<ContactQuestion> l )
	{
		BContactQuestion []ret=new BContactQuestion[l.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=new BContactQuestion(l.get(loop));
		
		return ret;
	}
	
	public static void delete(String ids[])
	{
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		for (String id : ids)
			new BContactQuestion(id).delete();
		hsu.flush();
		
		// renumber the items from 0
		List<ContactQuestion> questions = hsu.createCriteria(ContactQuestion.class).orderBy(ContactQuestion.SEQ).list();
		for (short newSeq = 0; newSeq < questions.size(); newSeq++) {
			ContactQuestion question = questions.get(newSeq);
			
			question.setSeqno(newSeq);
			hsu.saveOrUpdate(question);
		}
	}

	public void move(boolean moveUp) {
		if (moveUp)
			moveUp();
		else
			moveDown();
	}
	
	public void moveUp()
	{
		if (bean.getSeqno()>0)
		{
			ContactQuestion pvj=ArahantSession.getHSU().createCriteria(ContactQuestion.class)
				.eq(ContactQuestion.SEQ, (short)(bean.getSeqno()-1))
				.first();

			short temp=bean.getSeqno();
			pvj.setSeqno((short)99999);
			ArahantSession.getHSU().saveOrUpdate(pvj);
			ArahantSession.getHSU().flush();
			bean.setSeqno((short)(bean.getSeqno()-1));
			ArahantSession.getHSU().saveOrUpdate(bean);
			ArahantSession.getHSU().flush();
			pvj.setSeqno(temp);
			ArahantSession.getHSU().saveOrUpdate(pvj);
		}
		else //shift them all
		{
			List <ContactQuestion> l=ArahantSession.getHSU().createCriteria(ContactQuestion.class)
				.orderBy(ContactQuestion.SEQ)
				.list();
			
			l.get(0).setSeqno((short)99999);
			ArahantSession.getHSU().saveOrUpdate(l.get(0));
			ArahantSession.getHSU().flush();
			for (int loop=1;loop<l.size();loop++)
			{
				l.get(loop).setSeqno((short)(l.get(loop).getSeqno()-1));
				ArahantSession.getHSU().saveOrUpdate(l.get(loop));
				ArahantSession.getHSU().flush();
			}
			
			l.get(0).setSeqno((short)(l.size()-1));
			ArahantSession.getHSU().saveOrUpdate(l.get(0));
		}
	}
	
	public void moveDown()
	{
		if (bean.getSeqno()!=ArahantSession.getHSU().createCriteria(ContactQuestion.class)
				.count()-1)
		{
			ContactQuestion pvj=ArahantSession.getHSU().createCriteria(ContactQuestion.class)
				.eq(ContactQuestion.SEQ, (short)(bean.getSeqno()+1))
				.first();

			short temp=bean.getSeqno();
			pvj.setSeqno((short)999999);
			ArahantSession.getHSU().saveOrUpdate(pvj);
			ArahantSession.getHSU().flush();
			bean.setSeqno((short)(bean.getSeqno()+1));
			ArahantSession.getHSU().saveOrUpdate(bean);
			pvj.setSeqno(temp);
			ArahantSession.getHSU().saveOrUpdate(pvj);
		}
		else //shift them all
		{
			List <ContactQuestion> l=ArahantSession.getHSU().createCriteria(ContactQuestion.class)
				.orderBy(ContactQuestion.SEQ)
				.list();
			
			l.get(l.size()-1).setSeqno((short)99999);
			ArahantSession.getHSU().saveOrUpdate(l.get(l.size()-1));
			ArahantSession.getHSU().flush();
			
			for (int loop=l.size()-1;loop>-1;loop--)
			{
				l.get(loop).setSeqno((short)(loop+1));
				ArahantSession.getHSU().saveOrUpdate(l.get(loop));
				ArahantSession.getHSU().flush();
			}
			
			l.get(l.size()-1).setSeqno((short)0);
			ArahantSession.getHSU().saveOrUpdate(l.get(l.size()-1));
		}
	}

	public void setAddAfterId(String addAfterId) {
		if (isEmpty(addAfterId))
		{
			bean.setSeqno((short) ArahantSession.getHSU().createCriteria(ContactQuestion.class).count());
		}
		else
		{
			BContactQuestion bcq=new BContactQuestion(addAfterId);
			int initialSequence=bcq.getSequence() + 1;
			bean.setSeqno((short) ArahantSession.getHSU().createCriteria(ContactQuestion.class).count());
			
			ArahantSession.getHSU().insert(bean);
			//move up until it gets there
			while (bean.getSeqno()!=initialSequence)
				moveUp();
		}
		
	}


	public void setQuestion(String question) {
		bean.setQuestion(question);
	}
	
	public static BContactQuestion[] list(boolean activesOnly)
	{
		HibernateCriteriaUtil<ContactQuestion> hcu = ArahantSession.getHSU().createCriteria(ContactQuestion.class)
				.orderBy(ContactQuestion.SEQ);
		if(activesOnly)
			hcu.geOrEq(ContactQuestion.LAST_ACTIVE_DATE, DateUtils.now(), 0);
		return makeArray(hcu.list());
	}

	public void setLastActiveDate(int lastActiveDate) {
		bean.setLastActiveDate(lastActiveDate);
	}

	public int getLastActiveDate() {
		return bean.getLastActiveDate();
	}

	public CompanyDetail getCompany() {
		return bean.getCompany();
	}

	public void setCompany(CompanyDetail cd ) {
		bean.setCompany(cd);
	}
}
