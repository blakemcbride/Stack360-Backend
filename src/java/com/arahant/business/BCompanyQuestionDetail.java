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

import com.arahant.beans.CompanyBase;
import com.arahant.beans.CompanyQuestion;
import com.arahant.beans.CompanyQuestionDetail;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import java.util.List;

/**
 *
 */
public class BCompanyQuestionDetail extends SimpleBusinessObjectBase<CompanyQuestionDetail>
{

	public String create() throws ArahantException {
		bean=new CompanyQuestionDetail();
		return bean.generateId();
	}

	public String getDetailId() {
		return bean.getCompanyQuesDetId();
	}

	public String getQuestion() {
		return bean.getQuestion().getQuestion();
	}

	public String getQuestionId() {
		return bean.getQuestion().getCompanyQuesId();
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
		bean=ArahantSession.getHSU().get(CompanyQuestionDetail.class, key);
		oldResponse=bean.getResponse();
	}
	
	public BCompanyQuestionDetail(CompanyQuestionDetail o)
	{
		super();
		bean=o;
	}
	
	public BCompanyQuestionDetail(String key)
	{
		super(key);
	}

	public BCompanyQuestionDetail() {
		super();
	}

	static BCompanyQuestionDetail [] makeArray(List<CompanyQuestionDetail> l)
	{
		BCompanyQuestionDetail []ret=new BCompanyQuestionDetail[l.size()];
		
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=new BCompanyQuestionDetail(l.get(loop));
		
		return ret;
	}
	
	public static void delete(String []ids)
	{
		for (String id : ids)
			new BCompanyQuestionDetail(id).delete();
	}

	public void setQuestionId(String id) {
		bean.setQuestion(ArahantSession.getHSU().get(CompanyQuestion.class, id));
	}

	private String oldResponse="";
	
	public void setResponse(String response) {
		if (!oldResponse.equals(response))
			bean.setWhenAdded(new java.util.Date());
		bean.setResponse(response);
	}
	
	public void setCompanyId(String id) {
		bean.setCompany(ArahantSession.getHSU().get(CompanyBase.class, id));
	}
	
	public static BCompanyQuestionDetail [] list (String companyId)
	{
		//for every possible question, I need to either have, or fake a detail
		List<CompanyQuestion> l = ArahantSession.getHSU().createCriteria(CompanyQuestion.class)
														 .geOrEq(CompanyQuestion.LAST_ACTIVE_DATE, DateUtils.now(), 0)
														 .orderBy(CompanyQuestion.SEQ).list();
		
		CompanyBase c=ArahantSession.getHSU().get(CompanyBase.class, companyId);
		
		List <CompanyQuestionDetail> q = ArahantSession.getHSU().createCriteria(CompanyQuestionDetail.class)
				.eq(CompanyQuestionDetail.COMPANY, c)
				.list();
				
		BCompanyQuestionDetail [] ret=new BCompanyQuestionDetail[l.size()];
		
		for (int loop=0;loop<ret.length;loop++)
		{
			ret[loop]=new BCompanyQuestionDetail();
			
			for (CompanyQuestionDetail qd : q)
			{
				if (qd.getQuestion().equals(l.get(loop)))
				{
					ret[loop].bean=qd;
					break;
				}
			}
			
			if (ret[loop].bean==null)
			{
				//fake one
				ret[loop].bean=new CompanyQuestionDetail();
				ret[loop].bean.setCompany(c);
				ret[loop].bean.setQuestion(l.get(loop));
				ret[loop].bean.setResponse("");
			}
		}
		
		return ret;
	}
	
}
