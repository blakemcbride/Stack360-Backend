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

import com.arahant.beans.ContactQuestion;
import com.arahant.beans.ContactQuestionDetail;
import com.arahant.beans.Person;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import java.util.List;

/**
 *
 */
public class BContactQuestionDetail extends SimpleBusinessObjectBase<ContactQuestionDetail>
{

	public static BContactQuestionDetail[] list(String personId) {
		//for every possible question, I need to either have, or fake a detail
		List <ContactQuestion> l=ArahantSession.getHSU().createCriteria(ContactQuestion.class)
			.geOrEq(ContactQuestion.LAST_ACTIVE_DATE, DateUtils.now(), 0)
			.orderBy(ContactQuestion.SEQ).list();
		
		// if we got a person, load his/her question details
		Person p = null;
		List <ContactQuestionDetail> q = null;
		if (!isEmpty(personId)) 
		{
			p=ArahantSession.getHSU().get(Person.class, personId);
		
			q = ArahantSession.getHSU().createCriteria(ContactQuestionDetail.class)
				.eq(ContactQuestionDetail.PERSON, p)
				.list();
		}
				
		BContactQuestionDetail [] ret=new BContactQuestionDetail[l.size()];
		for (int loop=0;loop<ret.length;loop++)
		{
			ret[loop]=new BContactQuestionDetail();
			
			if (q != null) 
			{
				for (ContactQuestionDetail qd : q)
				{
					if (qd.getQuestion().equals(l.get(loop)))
					{
						ret[loop].bean=qd;
						break;
					}
				}
			}
			
			if (ret[loop].bean==null)
			{
				//fake one
				ret[loop].bean=new ContactQuestionDetail();
				ret[loop].bean.setPerson(p);
				ret[loop].bean.setQuestion(l.get(loop));
				ret[loop].bean.setResponse("");
			}
		}
		
		return ret;
		
	}

	public BContactQuestionDetail() {
		super();
	}
	
	public BContactQuestionDetail(String id)
	{
		super(id);
	}
	
	public BContactQuestionDetail(ContactQuestionDetail o)
	{
		super();
		bean=o;
	}

	public String create() throws ArahantException {
		bean=new ContactQuestionDetail();
		return bean.generateId();
	}

	public String getId() {
		return bean.getContactQuestionDetId();
	}

	public String getQuestion() {
		return bean.getQuestion().getQuestion();
	}

	public String getQuestionId() {
		return bean.getQuestion().getContactQuestionId();
	}

	public String getResponse() {
		return bean.getResponse();
	}

	public int getSequence() {
		return bean.getQuestion().getSeqno();
	}

	public String getWhenAddedFormatted() {
		return DateUtils.getDateTimeFormatted(bean.getWhenAdded());
	}

	public void load(String key) throws ArahantException {
		bean=ArahantSession.getHSU().get(ContactQuestionDetail.class, key);
		oldResponse=bean.getResponse();
	}

	static BContactQuestionDetail [] makeArray(List<ContactQuestionDetail> l)
	{
		BContactQuestionDetail [] ret=new BContactQuestionDetail[l.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=new BContactQuestionDetail(l.get(loop));
		
		return ret;
	}
	
	String oldResponse=null;
	public static void delete(String []ids)
	{
		for (String id : ids)
			new BContactQuestionDetail(id).delete();
	}

	public void setPersonId(String personId) {
		bean.setPerson(ArahantSession.getHSU().get(Person.class, personId));
	}

	public void setQuestionId(String questionID) {
		bean.setQuestion(ArahantSession.getHSU().get(ContactQuestion.class, questionID));
	}

	public void setResponse(String response) {
		if (!response.equals(oldResponse))
			bean.setWhenAdded(new java.util.Date());
		bean.setResponse(response);
	}
}
