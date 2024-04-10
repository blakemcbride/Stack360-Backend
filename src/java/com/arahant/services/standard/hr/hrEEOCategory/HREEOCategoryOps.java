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
 * Created on Feb 22, 2007
 * 
 */
package com.arahant.services.standard.hr.hrEEOCategory;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BHREEOCategory;
import com.arahant.business.BRight;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;


/**
 * 
 *
 * Created on Feb 22, 2007
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrHrEEOCategoryOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class HREEOCategoryOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			HREEOCategoryOps.class);

	public HREEOCategoryOps() {
		super();
	}


	
	@WebMethod()
	public DeleteEEOCategoriesReturn deleteEEOCategories(/*@WebParam(name = "in")*/final DeleteEEOCategoriesInput in)	{
		final DeleteEEOCategoriesReturn ret=new DeleteEEOCategoriesReturn();
		try
		{
			checkLogin(in);
			
			BHREEOCategory.delete(hsu,in.getIds());
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	@WebMethod()
	public GetEEOCategoriesReportReturn getEEOCategoriesReport(/*@WebParam(name = "in")*/final GetEEOCategoriesReportInput in)	{
		final GetEEOCategoriesReportReturn ret=new GetEEOCategoriesReportReturn();
		try
		{
			checkLogin(in);
			
			ret.setFileName(BHREEOCategory.getReport(hsu));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	@WebMethod()
	public ListEEOCategoriesReturn listEEOCategories(/*@WebParam(name = "in")*/final ListEEOCategoriesInput in)	{
		final ListEEOCategoriesReturn ret=new ListEEOCategoriesReturn();
		try
		{
			checkLogin(in);
			
			ret.setItem(BHREEOCategory.list(hsu));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	@WebMethod()
	public NewEEOCategoryReturn newEEOCategory(/*@WebParam(name = "in")*/final NewEEOCategoryInput in)			{
		final NewEEOCategoryReturn ret=new NewEEOCategoryReturn();
		try
		{
			checkLogin(in);
			
			final BHREEOCategory x=new BHREEOCategory();
			ret.setId(x.create());
			in.setData(x);
			x.insert();
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	@WebMethod()
	public SaveEEOCategoryReturn saveEEOCategory(/*@WebParam(name = "in")*/final SaveEEOCategoryInput in)	{
		final SaveEEOCategoryReturn ret=new SaveEEOCategoryReturn();
		try
		{
			checkLogin(in);
			
			final BHREEOCategory x=new BHREEOCategory(in.getId());
			in.setData(x);
			x.update();
			
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
			
			ret.setAccessLevel(BRight.checkRight(ACCESS_HRSETUPS));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	} 		
}
/*
 * HrEEOCategory		HumanResourcesManagementService.deleteEEOCategoriesObj																																																																																																																																																																																																																																																													
		HumanResourcesManagementService.getEEOCategoriesReportObj	*** MISSING																																																																																																																																																																																																																																																												
		HumanResourcesManagementService.listEEOCategoriesObj	*** NEEDS ordering																																																																																																																																																																																																																																																												
		HumanResourcesManagementService.newEEOCategoryObj																																																																																																																																																																																																																																																													
		HumanResourcesManagementService.saveEEOCategoryObj																																																																																																																																																																																																																																																													
		SecurityManagementService.checkRights	*** MISSING AccessHRSetups																																																																																																																																																																																																																																																												

 */
	
