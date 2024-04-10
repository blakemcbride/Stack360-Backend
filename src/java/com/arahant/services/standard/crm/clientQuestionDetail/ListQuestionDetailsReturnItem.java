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
import com.arahant.business.BCompanyQuestionDetail;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 *
 */
public class ListQuestionDetailsReturnItem {
	
	public ListQuestionDetailsReturnItem()
	{

	}

	ListQuestionDetailsReturnItem (BCompanyQuestionDetail bc)
	{
		
		questionId=bc.getQuestionId();
		detailId=bc.getDetailId();
		question=bc.getQuestion();
		response=TransmitReturnBase.preview(bc.getResponse());
		
		whenAddedFormatted=bc.getWhenAddedFormatted();
		sequence=bc.getSequence();

	}
	
	private String questionId;
	private String detailId;
	private String question;
	private String response;
	private String whenAddedFormatted;
	private int sequence;


	public String getQuestionId()
	{
		return questionId;
	}
	public void setQuestionId(String questionId)
	{
		this.questionId=questionId;
	}
	public String getDetailId()
	{
		return detailId;
	}
	public void setDetailId(String detailId)
	{
		this.detailId=detailId;
	}
	public String getQuestion()
	{
		return question;
	}
	public void setQuestion(String question)
	{
		this.question=question;
	}
	public String getResponse()
	{
		return response;
	}
	public void setResponse(String response)
	{
		this.response=response;
	}
	public String getWhenAddedFormatted()
	{
		return whenAddedFormatted;
	}
	public void setWhenAddedFormatted(String whenAddedFormatted)
	{
		this.whenAddedFormatted=whenAddedFormatted;
	}
	public int getSequence()
	{
		return sequence;
	}
	public void setSequence(int sequence)
	{
		this.sequence=sequence;
	}

}

	
