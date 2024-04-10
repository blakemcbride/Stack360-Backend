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
package com.arahant.services.standard.project.phase;
import com.arahant.beans.Right;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.ProjectPhaseReport;
import com.arahant.utils.ArahantConstants;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


/**
 * 
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardProjectPhaseOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class PhaseOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			PhaseOps.class);
	
	public PhaseOps() {
		super();
	}
	
        @WebMethod()
	public ListPhasesReturn listPhases(/*@WebParam(name = "in")*/final ListPhasesInput in)		
	{
		final ListPhasesReturn ret=new ListPhasesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BProjectPhase.list());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public NewPhaseReturn newPhase(/*@WebParam(name = "in")*/final NewPhaseInput in)		
	{
		final NewPhaseReturn ret=new NewPhaseReturn();
		try
		{
			checkLogin(in);
			
			final BProjectPhase x=new BProjectPhase();
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
	public SavePhaseReturn savePhase(/*@WebParam(name = "in")*/final SavePhaseInput in)		
	{
		final SavePhaseReturn ret=new SavePhaseReturn();
		try
		{
			checkLogin(in);
			
			final BProjectPhase x=new BProjectPhase(in.getId());
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
        public DeletePhasesReturn deletePhases(/*@WebParam(name = "in")*/final DeletePhasesInput in)		
	{
		final DeletePhasesReturn ret=new DeletePhasesReturn();
		try
		{
			checkLogin(in);
			
			BProjectPhase.delete(in.getIds());
			
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
			
			ret.setReportUrl(new ProjectPhaseReport().build());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public ListSecurityCategoriesReturn listSecurityCategories(/*@WebParam(name = "in")*/final ListSecurityCategoriesInput in)		
	{
		final ListSecurityCategoriesReturn ret=new ListSecurityCategoriesReturn();
		try
		{
			checkLogin(in);
			
			String [] cats=BProjectPhase.getSecurityCategoryList();
			
			ListSecurityCategoriesReturnItem [] arr=new ListSecurityCategoriesReturnItem[cats.length];
			
			for (int loop=0;loop<cats.length;loop++)
			{
				arr[loop]=new ListSecurityCategoriesReturnItem();
				arr[loop].setId(loop+1);
				arr[loop].setName(cats[loop]);
			}
			ret.setItem(arr);
			
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

			ret.setCanSeeAllCompanies(BRight.checkRight(Right.RIGHT_CAN_ACCESS_ALL_COMPANIES) == ArahantConstants.ACCESS_LEVEL_WRITE);

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

}
