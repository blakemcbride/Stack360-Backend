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
package com.arahant.services.standard.hr.studentStatus;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.StudentVerificationReport;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrStudentStatusOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class StudentStatusOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			StudentStatusOps.class);
	
	public StudentStatusOps() {
		super();
	}
	
    @WebMethod()
	public LoadStudentStatusReturn loadStudentStatus(/*@WebParam(name = "in")*/final LoadStudentStatusInput in)		
	{
		final LoadStudentStatusReturn ret=new LoadStudentStatusReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BPerson(in.getPersonId()));
			
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
			
			ret.setReportUrl(new StudentVerificationReport().build(in.getPersonId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public SaveStudentStatusReturn saveStudentStatus(/*@WebParam(name = "in")*/final SaveStudentStatusInput in)		
	{
		final SaveStudentStatusReturn ret=new SaveStudentStatusReturn();
		try
		{
			checkLogin(in);
			
			final BPerson x=new BPerson(in.getPersonId());
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
    public DeleteVerificationsReturn deleteVerifications(/*@WebParam(name = "in")*/final DeleteVerificationsInput in)		
	{
		final DeleteVerificationsReturn ret=new DeleteVerificationsReturn();
		try
		{
			checkLogin(in);
			
			BStudentVerification.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public NewVerificationReturn newVerification(/*@WebParam(name = "in")*/final NewVerificationInput in)		
	{
		final NewVerificationReturn ret=new NewVerificationReturn();
		try
		{
			checkLogin(in);
			
			final BStudentVerification x=new BStudentVerification();
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
	public SaveVerificationReturn saveVerification(/*@WebParam(name = "in")*/final SaveVerificationInput in)		
	{
		final SaveVerificationReturn ret=new SaveVerificationReturn();
		try
		{
			checkLogin(in);
			
			final BStudentVerification x=new BStudentVerification(in.getId());
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
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();
	
		try {
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight(ACCESS_HRSETUPS));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	} 		
}
