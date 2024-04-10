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
package com.arahant.services.standard.crm.prospectQuestionDetail;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.CompanyQuestionDetailReport;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardCrmProspectQuestionDetailOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ProspectQuestionDetailOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			ProspectQuestionDetailOps.class);
	
	public ProspectQuestionDetailOps() {
		super();
	}
	
        @WebMethod()
	public ListQuestionDetailsReturn listQuestionDetails(/*@WebParam(name = "in")*/final ListQuestionDetailsInput in)		
	{
		final ListQuestionDetailsReturn ret=new ListQuestionDetailsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BCompanyQuestionDetail.list(in.getId()));
			
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
			
			ret.setAccessLevel(BRight.checkRight("AccessCRM"));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
        @WebMethod()
	public LoadQuestionDetailReturn loadQuestionDetail(/*@WebParam(name = "in")*/final LoadQuestionDetailInput in)		
	{
		final LoadQuestionDetailReturn ret=new LoadQuestionDetailReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BCompanyQuestionDetail(in.getId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public NewQuestionDetailReturn newQuestionDetail(/*@WebParam(name = "in")*/final NewQuestionDetailInput in)		
	{
		final NewQuestionDetailReturn ret=new NewQuestionDetailReturn();
		try
		{
			checkLogin(in);
			
			final BCompanyQuestionDetail x=new BCompanyQuestionDetail();
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
	public SaveQuestionDetailReturn saveQuestionDetail(/*@WebParam(name = "in")*/final SaveQuestionDetailInput in)		
	{
		final SaveQuestionDetailReturn ret=new SaveQuestionDetailReturn();
		try
		{
			checkLogin(in);
			
			final BCompanyQuestionDetail x=new BCompanyQuestionDetail(in.getDetailId());
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
        public DeleteQuestionDetailsReturn deleteQuestionDetails(/*@WebParam(name = "in")*/final DeleteQuestionDetailsInput in)		
	{
		final DeleteQuestionDetailsReturn ret=new DeleteQuestionDetailsReturn();
		try
		{
			checkLogin(in);
			
			BCompanyQuestionDetail.delete(in.getIds());
			
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
			
			ret.setReportUrl(new CompanyQuestionDetailReport("Prospect Detail").build(in.getId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
