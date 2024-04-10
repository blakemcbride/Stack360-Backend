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
package com.arahant.services.standard.hr.benefitPriceUpload;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrBenefitPriceUploadOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class BenefitPriceUploadOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			BenefitPriceUploadOps.class);
	
	public BenefitPriceUploadOps() {
		super();
	}
	

	@WebMethod()
	public ListBenefitCategoriesReturn listBenefitCategories(/*@WebParam(name = "in")*/final ListBenefitCategoriesInput in)			{
		final ListBenefitCategoriesReturn ret=new ListBenefitCategoriesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHRBenefitCategory.list());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListBenefitsReturn listBenefits(/*@WebParam(name = "in")*/final ListBenefitsInput in)			{
		final ListBenefitsReturn ret=new ListBenefitsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHRBenefit.list(in.getBenefitCategoryId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
        @WebMethod()
	public ListBenefitConfigsReturn listBenefitConfigs(/*@WebParam(name = "in")*/final ListBenefitConfigsInput in)		
	{
		final ListBenefitConfigsReturn ret=new ListBenefitConfigsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHRBenefitConfig.list(in.getBenefitId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}