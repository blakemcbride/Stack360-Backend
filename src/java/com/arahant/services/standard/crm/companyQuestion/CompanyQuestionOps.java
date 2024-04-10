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
package com.arahant.services.standard.crm.companyQuestion;
import com.arahant.beans.Right;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.CompanyQuestionReport;
import com.arahant.utils.ArahantConstants;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


/**
 * 
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardCrmCompanyQuestionOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class CompanyQuestionOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			CompanyQuestionOps.class);
	
	public CompanyQuestionOps() {
		super();
	}
	
        @WebMethod()
	public ListQuestionsReturn listQuestions(/*@WebParam(name = "in")*/final ListQuestionsInput in)		
	{
		final ListQuestionsReturn ret=new ListQuestionsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BCompanyQuestion.list(false));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public NewQuestionReturn newQuestion(/*@WebParam(name = "in")*/final NewQuestionInput in)		
	{
		final NewQuestionReturn ret=new NewQuestionReturn();
		try
		{
			checkLogin(in);
			
			final BCompanyQuestion x=new BCompanyQuestion();
			ret.setId(x.create());
			in.setData(x);
			x.insert();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public SaveQuestionReturn saveQuestion(/*@WebParam(name = "in")*/final SaveQuestionInput in)		
	{
		final SaveQuestionReturn ret=new SaveQuestionReturn();
		try
		{
			checkLogin(in);
			
			final BCompanyQuestion x=new BCompanyQuestion(in.getId());
			in.setData(x);
			x.update();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
        public DeleteQuestionsReturn deleteQuestions(/*@WebParam(name = "in")*/final DeleteQuestionsInput in)		
	{
		final DeleteQuestionsReturn ret=new DeleteQuestionsReturn();
		try
		{
			checkLogin(in);
			
			BCompanyQuestion.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
        public MoveQuestionReturn moveQuestion(/*@WebParam(name = "in")*/final MoveQuestionInput in)		
	{
		final MoveQuestionReturn ret=new MoveQuestionReturn();
		try
		{
			checkLogin(in);
			
			new BCompanyQuestion(in.getId()).move(in.getMoveUp());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in)		
	{
		final GetReportReturn ret=new GetReportReturn();
		try
		{
			checkLogin(in);
			
			ret.setReportUrl(new CompanyQuestionReport().build());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	


	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)		
	{
		final CheckRightReturn ret=new CheckRightReturn();
		try
		{
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight("AccessCRMSetup"));
			ret.setCanSeeAllCompanies(BRight.checkRight(Right.RIGHT_CAN_ACCESS_ALL_COMPANIES) == ArahantConstants.ACCESS_LEVEL_WRITE);
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
