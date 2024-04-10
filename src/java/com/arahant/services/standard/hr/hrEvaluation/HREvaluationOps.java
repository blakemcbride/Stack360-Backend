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
 * Created on Feb 25, 2007
 * 
 */
package com.arahant.services.standard.hr.hrEvaluation;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.*;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;


/**
 * 
 *
 * Created on Feb 25, 2007
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrHrEvaluationOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class HREvaluationOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			HREvaluationOps.class);

	
	
	public HREvaluationOps() {
		super();
	}
	
	@WebMethod()
	public SearchEmployeesReturn searchEmployees(/*@WebParam(name = "in")*/final SearchEmployeesInput in)	{
		final SearchEmployeesReturn ret=new SearchEmployeesReturn();

		try
		{
			checkLogin(in);
			
			ret.setEmployees(BEmployee.searchEmployees(hsu, in.getSsn(),in.getFirstName(), in.getLastName(), null, 0, ret.getHighCap(),null));
			
			if (!isEmpty(in.getPersonId()))
			{
				ret.setSelectedItem(new SearchEmployeesReturnItem(new BPerson(in.getPersonId())));
			}
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	
	
	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();
	
		try {
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight(HR_EVALUATION));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	} 
	
	@WebMethod()
	public DeleteEmployeeEvalReturn deleteEmployeeEval(/*@WebParam(name = "in")*/final DeleteEmployeeEvalInput in)	{
		final DeleteEmployeeEvalReturn ret=new DeleteEmployeeEvalReturn();

		try {
			checkLogin(in);
			
			BHREmployeeEval.delete(hsu,in.getIds());
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public DeleteEmployeeEvalDetailsReturn deleteEmployeeEvalDetails(/*@WebParam(name = "in")*/final DeleteEmployeeEvalDetailsInput in)	{
		final DeleteEmployeeEvalDetailsReturn ret=new DeleteEmployeeEvalDetailsReturn();

		try {
			checkLogin(in);
			
			BHREmployeeEvalDetail.delete(hsu,in.getIds());
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public GetEmployeeEvalsReportReturn getEmployeeEvalsReport (/*@WebParam(name = "in")*/final GetEmployeeEvalsReportInput in)	{
		final GetEmployeeEvalsReportReturn ret=new GetEmployeeEvalsReportReturn();
		try
		{
			checkLogin(in);
			ret.setFileName(BHREmployeeEval.getReport(hsu,in.getEvalIds(),in.isShowPrivate()));
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	
	@WebMethod()
	public ListEmployeeEvalsReturn listEmployeeEvals(/*@WebParam(name = "in")*/final ListEmployeeEvalsInput in)	{
		final ListEmployeeEvalsReturn ret=new ListEmployeeEvalsReturn();

		try {
			checkLogin(in);
			
			ret.setItem(BHREmployeeEval.list(hsu,in.getEmployeeId()));
						
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	} 
	
	@WebMethod()
	public ListEmployeeEvalDetailsReturn  listEmployeeEvalDetails(/*@WebParam(name = "in")*/final ListEmployeeEvalDetailsInput in)	{
		final ListEmployeeEvalDetailsReturn ret=new ListEmployeeEvalDetailsReturn();

		try {
			checkLogin(in);		
			
			ret.setItem(new BHREmployeeEval(in.getEmployeeEvalId()).listDetails());
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	} 
	
	
	@WebMethod()
	public LoadEmployeeEvalDetailReturn loadEmployeeEvalDetail(/*@WebParam(name = "in")*/final LoadEmployeeEvalDetailInput in)	{
		final LoadEmployeeEvalDetailReturn ret=new LoadEmployeeEvalDetailReturn();

		try {
			checkLogin(in);	
			
			ret.setData(new BHREmployeeEvalDetail(in.getId()));
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	
	@WebMethod()
	public NewEmployeeEvalDetailReturn newEmployeeEvalDetail(/*@WebParam(name = "in")*/final NewEmployeeEvalDetailInput in)	{
		final NewEmployeeEvalDetailReturn ret=new NewEmployeeEvalDetailReturn();

		try {
			checkLogin(in);	
			
			final BHREmployeeEvalDetail d=new BHREmployeeEvalDetail();
			ret.setId(d.create());
			in.setData(d);
			d.insert();
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	
	@WebMethod()
	public SaveEmployeeEvalDetailReturn saveEmployeeEvalDetail(/*@WebParam(name = "in")*/final SaveEmployeeEvalDetailInput in)	{
		final SaveEmployeeEvalDetailReturn ret=new SaveEmployeeEvalDetailReturn();

		try {
			checkLogin(in);	
			
			final BHREmployeeEvalDetail d=new BHREmployeeEvalDetail(in.getDetailId());
			in.setData(d);
			d.update();
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public LoadEmployeeEvalReturn loadEmployeeEval(/*@WebParam(name = "in")*/final LoadEmployeeEvalInput in)	{

		final LoadEmployeeEvalReturn ret=new LoadEmployeeEvalReturn();

		try {
			checkLogin(in);
			
			final BHREmployeeEval ev=new BHREmployeeEval(in.getId());
			ret.setData(ev);
		
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public NewEmployeeEvalReturn newEmployeeEval(/*@WebParam(name = "in")*/final NewEmployeeEvalInput in)	{
		final NewEmployeeEvalReturn ret=new NewEmployeeEvalReturn();
		try {
			checkLogin(in);

			final BHREmployeeEval ev=new BHREmployeeEval();
			ret.setId(ev.create());
			in.setData(ev);
			ev.insert();
		
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public SaveEmployeeEvalReturn saveEmployeeEval(/*@WebParam(name = "in")*/final SaveEmployeeEvalInput in)	{
		final SaveEmployeeEvalReturn ret=new SaveEmployeeEvalReturn();
		try
		{
			checkLogin(in);
			
			final BHREmployeeEval ev=new BHREmployeeEval(in.getEmployeeEvalId());
			in.setData(ev);
			ev.update();
		
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	
	
	@WebMethod()
	public MarkEvaluationForEmployeeEditReturn markEvaluationForEmployeeEdit (/*@WebParam(name = "in")*/final MarkEvaluationForEmployeeEditInput in)	{
		final MarkEvaluationForEmployeeEditReturn ret=new MarkEvaluationForEmployeeEditReturn();
		try
		{
			checkLogin(in);
			
			new BHREmployeeEval(in.getEvaluationId()).markForEmployeeEdit();
		
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public FinalizeEvaluationReturn finalizeEvaluation (/*@WebParam(name = "in")*/final FinalizeEvaluationInput in)	{
		final FinalizeEvaluationReturn ret=new FinalizeEvaluationReturn();
		try
		{
			checkLogin(in);
			
			new BHREmployeeEval(in.getEvaluationId()).finalizeEvaluation();
		
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public LoadEvalDescriptionReturn loadEvalDescription(/*@WebParam(name = "in")*/final LoadEvalDescriptionInput in)	{
		final LoadEvalDescriptionReturn ret=new LoadEvalDescriptionReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BHREvalCategory(in.getEvalCatId()));
		
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
}


/*
 * HrEvaluation		EmployeeEditorService.searchEmployeesObj																																																																																																																																																																																																																																																													
		HumanResourcesManagementService.deleteEmployeeEvalObj																																																																																																																																																																																																																																																													
		HumanResourcesManagementService.deleteEmployeeEvalDetailsObj																																																																																																																																																																																																																																																													
		HumanResourcesManagementService.getEmployeeEvalsReportObj	*** MISSING																																																																																																																																																																																																																																																												
		HumanResourcesManagementService.listEmployeeEvalsObj	*** NEEDS ordering																																																																																																																																																																																																																																																												
		HumanResourcesManagementService.listEmployeeEvalDetailsObj	*** NEEDS ordering																																																																																																																																																																																																																																																												
		HumanResourcesManagementService.loadEmployeeEvalDetailObj																																																																																																																																																																																																																																																													
		HumanResourcesManagementService.newEmployeeEvalDetailObj																																																																																																																																																																																																																																																													
		HumanResourcesManagementService.saveEmployeeEvalDetailObj																																																																																																																																																																																																																																																													
		SecurityManagementService.checkRights	*** MISSING AccessHR			
		
		
8. markEvaluationForEmployeeEdit
 
A. input: evaluation id
B. remarks: sets status to Supervisor Edit and sends a stock message to the employee specified on the evaluation that the evaluation is ready for edit from the supervisor
 
 
9. finalizeEvaluation
 
A. input evaluation id
B. remarks: clears status, sets final date, and sends a stock message to the employee specified on the evaluation that the evaluation has been finalized
 
 
																																																																																																																																																																																																																																																									
																																																																																																																																																																																																																																																											

 */	
