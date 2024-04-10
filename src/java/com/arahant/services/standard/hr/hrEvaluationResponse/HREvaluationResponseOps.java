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
 * Created on Apr 5, 2007
 * 
 */
package com.arahant.services.standard.hr.hrEvaluationResponse;

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
 * Created on Apr 5, 2007
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrHrEvaluationResponseOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class HREvaluationResponseOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			HREvaluationResponseOps.class);
	
	public HREvaluationResponseOps() {
		super();
	}
	@WebMethod()
	public ListEmployeeEvalsReturn listEmployeeEvals(/*@WebParam(name = "in")*/final ListEmployeeEvalsInput in)	{
		final ListEmployeeEvalsReturn ret=new ListEmployeeEvalsReturn();

		try {
			checkLogin(in);
			
			ret.setItem(BHREmployeeEval.list(hsu,BPerson.getCurrent().getPersonId()));
						
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
	public MarkEvaluationForSupervisorEditReturn markEvaluationForSupervisorEdit (/*@WebParam(name = "in")*/final MarkEvaluationForSupervisorEditInput in)	{
		final MarkEvaluationForSupervisorEditReturn ret=new MarkEvaluationForSupervisorEditReturn();

		try {
			checkLogin(in);
			
			new BHREmployeeEval(in.getEvaluationId()).markForSupervisorEdit();
			
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
			ret.setFileName(BHREmployeeEval.getEmployeeVersionReport(hsu,in.getEvalIds()));
			finishService(ret);
		}
		catch (final Throwable e) {
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
 * 
 
New screen EvaluationResponse as follows:
 
1. listEvaluations
 
A. input: employee id
B. output: evaluation id, evaluation date, description, status (Employee Edit, Supervisor Edit, Finalized on <date>), 
C. remarks: items with status of Supervisor Edit and Finalized on <date> will not be editable
 
 
2. loadEvaluation (see HrEvaluation.loadEvaluation, though I am cleaning up the return a bit to be only what i need)
 
A. input: evaluation id
B. output: supervisorFName, supervisorLName, evalDate, nextEvalDate, description, supervisor comments, employee comments
remarks: the internal comment should not be returned (see future email for this request)
 
 
3. saveEvaluation
 
A. input: evaluation id, employee comments
 
 
4. listEvaluationDetails (see HrEvaluation.listEmployeeEvalDetails, though I am cleaning up the return a bit to be only what i need)
 
input: evaluation id
output: evalCategoryName, evalCategoryId, supervisor score, employee score, evaluatonDetailId, employeeEvalId, employee note preview (100)
 
 
5. loadEvaluationDetail (see HrEvaluation.loadEvalDetail, though I am cleaning up the return a bit to be only what i need)
 
A. input: evaluationDetailId
B. output: supervisor notes, employee notes, category description
C. remarks: the internal notes should not be returned
 
 
6. saveEvaluationDetail
 
A. input: evaluationDetailId, employeeScore, employeeNotes
 
 
7. newEvaluationDetail
 
A. input: evaluationId, evaluationCategoryId, employeeScore, employeeNotes
 
 
8. deleteEvaluationDetail
 
A. input: evaluationDetailId
 
 
9. markEvaluationForSupervisorEdit
 
A. input: evaluation id
B. remarks: sets status to Supervisor Edit and sends a stock message to the supervisor specified on the evaluation that the evaluation is ready for edit from the employee
 

 */

	
