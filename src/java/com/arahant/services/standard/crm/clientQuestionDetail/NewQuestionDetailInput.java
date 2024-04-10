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
package com.arahant.services.standard.crm.clientQuestionDetail;
import com.arahant.annotation.Validation;

import com.arahant.business.BClientCompany;
import com.arahant.services.TransmitInputBase;
import com.arahant.business.BCompanyQuestionDetail;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewQuestionDetailInput extends TransmitInputBase {

	void setData(BCompanyQuestionDetail bc)
	{
		
		bc.setQuestionId(questionId);
		bc.setResponse(response);
		bc.setCompanyId(id);

	}
	
	@Validation (min=1,max=16,required=true)
	private String id;
	@Validation (min=1,max=16,required=true)
	private String questionId;
	@Validation (table="company_question_detail",column="response",required=true)
	private String response;


	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getQuestionId()
	{
		return questionId;
	}
	public void setQuestionId(String questionId)
	{
		this.questionId=questionId;
	}
	public String getResponse()
	{
		return response;
	}
	public void setResponse(String response)
	{
		this.response=response;
	}

}

	
