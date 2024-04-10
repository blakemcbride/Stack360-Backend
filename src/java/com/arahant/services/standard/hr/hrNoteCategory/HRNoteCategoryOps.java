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

package com.arahant.services.standard.hr.hrNoteCategory;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BHRNoteCategory;
import com.arahant.business.BRight;
import com.arahant.reports.HRNoteCategoryReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;


/**
 * 
 *
 * Created on Feb 22, 2007
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrHrNoteCategoryOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class HRNoteCategoryOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(HRNoteCategoryOps.class);

	public HRNoteCategoryOps() {
	}
	
	@WebMethod()
	public DeleteNoteCategoriesReturn deleteNoteCategories(/*@WebParam(name = "in")*/final DeleteNoteCategoriesInput in)	{
		final DeleteNoteCategoriesReturn ret=new DeleteNoteCategoriesReturn();
		try
		{
			checkLogin(in);
			
			BHRNoteCategory.delete(hsu,in.getIds());
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetNoteCategoriesReportReturn getNoteCategoriesReport(/*@WebParam(name = "in")*/final GetNoteCategoriesReportInput in)	{
		final GetNoteCategoriesReportReturn ret=new GetNoteCategoriesReportReturn();
		try
		{
			checkLogin(in);
			
			ret.setFileName(new HRNoteCategoryReport().build(in.getActiveType()));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListNoteCategoriesReturn listNoteCategories(/*@WebParam(name = "in")*/final ListNoteCategoriesInput in)	{
		final ListNoteCategoriesReturn ret=new ListNoteCategoriesReturn();
		try
		{
			checkLogin(in);
			
			ret.setItem(BHRNoteCategory.list(in.getActiveType()));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewNoteCategoryReturn newNoteCategory(/*@WebParam(name = "in")*/final NewNoteCategoryInput in)			{
		final NewNoteCategoryReturn ret=new NewNoteCategoryReturn();
		try
		{
			checkLogin(in);
			
			final BHRNoteCategory x=new BHRNoteCategory();
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
	public SaveNoteCategoryReturn saveNoteCategory(/*@WebParam(name = "in")*/final SaveNoteCategoryInput in)	{
		final SaveNoteCategoryReturn ret=new SaveNoteCategoryReturn();
		try
		{
			checkLogin(in);
			
			final BHRNoteCategory x=new BHRNoteCategory(in.getId());
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

	
