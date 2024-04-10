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
package com.arahant.services.standard.hr.hrDependentsAgeLimitReport;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BHRBenefitCategory;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;


/**
 * 
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrHrDependentsAgeLimitReportOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class HrDependentsAgeLimitReportOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			HrDependentsAgeLimitReportOps.class);
	
	public HrDependentsAgeLimitReportOps() {
		super();
	}
	
	@WebMethod()
	public ListBenefitCategoryCategoriesReturn listBenefitCategoryCategories(/*@WebParam(name = "in")*/final ListBenefitCategoryCategoriesInput in)			{
		final ListBenefitCategoryCategoriesReturn ret=new ListBenefitCategoryCategoriesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHRBenefitCategory.list());
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	  
   
	@WebMethod()
	public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in)			{
		final GetReportReturn ret=new GetReportReturn();
		try
		{
			checkLogin(in);

			ret.setReportUrl(BHRBenefitCategory.getDependentReport(in.getAge(),in.getBenefitCategoryCategoryIds(), in.getYear(), in.getInactiveAsOf(),in.getExcludeHandicap(),in.getExcludeStudent()));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
 
/*
	public static void main (String args[])
	{
		GetReportInput in=new GetReportInput();
		in.setAge(25);
		in.setYear(2007);
		in.setBenefitCategoryCategoryIds(new String[]{"00001-0000000001","00001-0000000002","00001-0000000003"});
		in.setUser("mikej");
		in.setPassword("password");
		HrDependentsAgeLimitReportOps op=new HrDependentsAgeLimitReportOps();
		GetReportReturn ret=op.getReport(in);
		logger.info(ret.getReportUrl());
	}
	
*/
}
