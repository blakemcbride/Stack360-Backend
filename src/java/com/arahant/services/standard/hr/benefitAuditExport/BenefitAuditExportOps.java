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
package com.arahant.services.standard.hr.benefitAuditExport;

import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.exports.BenefitAuditFileExport;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrBenefitAuditExportOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class BenefitAuditExportOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(BenefitAuditExportOps.class);
	
	public BenefitAuditExportOps() {
		super();
	}
	
    @WebMethod()
	public GetExportReturn getExport(/*@WebParam(name = "in")*/final GetExportInput in) {

		final GetExportReturn ret = new GetExportReturn();

		try
		{
			checkLogin(in);

			ret.setExportUrl((new BenefitAuditFileExport()).build((in.getCategoryIds()), in.getBenefitIds(), in.getConfigIds(), in.getEmployeeStatus(), in.getAsOfDate()));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public ListCategoriesReturn listCategories(/*@WebParam(name = "in")*/final ListCategoriesInput in) {

		final ListCategoriesReturn ret = new ListCategoriesReturn();

		try
		{
			checkLogin(in);

			ret.setItem(BHRBenefitCategory.list(in.getExcludeIdsArray(), ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public ListBenefitsReturn listBenefits(/*@WebParam(name = "in")*/final ListBenefitsInput in) {

		final ListBenefitsReturn ret = new ListBenefitsReturn();

		try
		{
			checkLogin(in);

			BHRBenefitCategory bcat = new BHRBenefitCategory(in.getCategoryId());
			ret.setItem(bcat.listBenefits(in.getExcludeIdsArray(),ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public ListConfigsReturn listConfigs(/*@WebParam(name = "in")*/final ListConfigsInput in) {

		final ListConfigsReturn ret = new ListConfigsReturn();

		try
		{
			checkLogin(in);

			BHRBenefit bben = new BHRBenefit(in.getBenefitId());
			ret.setItem(bben.listConfigs(in.getExcludeIdsArray(), ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
