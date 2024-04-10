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
package com.arahant.services.standard.project.projectBenefit;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BHRBenefitCategory;
import com.arahant.business.BProject;
import com.arahant.business.BRight;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.DateUtils;


/**
 * 
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardProjectProjectBenefitOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ProjectBenefitOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			ProjectBenefitOps.class);
	
	public ProjectBenefitOps() {
		super();
	}
	
	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();
	
		try {
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight("ProjectBenefit"));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	
	
	@WebMethod()
	public ListAssociatedBenefitConfigsReturn listAssociatedBenefitConfigs(/*@WebParam(name = "in")*/final ListAssociatedBenefitConfigsInput in)			{
		final ListAssociatedBenefitConfigsReturn ret=new ListAssociatedBenefitConfigsReturn();
		try
		{
			checkLogin(in);

			final BProject project = new BProject(in.getProjectId());
			
			ret.setItem(project.getAssociatedBenefitConfigs());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public AssociateBenefitConfigsReturn associateBenefitConfigs(/*@WebParam(name = "in")*/final AssociateBenefitConfigsInput in)			{
		final AssociateBenefitConfigsReturn ret=new AssociateBenefitConfigsReturn();
		try
		{
			checkLogin(in);
			
			final BProject project = new BProject(in.getProjectId());
			project.associateBenefitConfigIds(in.getConfigIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public DissassociateBenefitConfigsReturn dissassociateBenefitConfigs(/*@WebParam(name = "in")*/final DissassociateBenefitConfigsInput in)			{
		final DissassociateBenefitConfigsReturn ret=new DissassociateBenefitConfigsReturn();
		try
		{
			checkLogin(in);

			final BProject project = new BProject(in.getProjectId());
			project.dissassociateBenefitConfigIds(in.getConfigIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
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

			ret.setItem(new BHRBenefitCategory(in.getCategoryId()).listActiveBenefits(null, false, DateUtils.now()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public ListAvailableBenefitConfigsReturn listAvailableBenefitConfigs(/*@WebParam(name = "in")*/final ListAvailableBenefitConfigsInput in)			{
		final ListAvailableBenefitConfigsReturn ret=new ListAvailableBenefitConfigsReturn();
		try
		{
			checkLogin(in);

			final BProject project = new BProject(in.getProjectId());
			
			ret.setItem(project.getDissassociatedBenefitConfigs(in.getBenefitId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	

}
