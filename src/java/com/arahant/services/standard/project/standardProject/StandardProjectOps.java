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
 * Created on Feb 4, 2007
 * 
 */
package com.arahant.services.standard.project.standardProject;

import com.arahant.beans.Right;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

import com.arahant.business.*;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantConstants;
import com.arahant.utils.ArahantLogger;


/**
 * 
 *
 * Created on Feb 4, 2007
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardProjectStandardProjectOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class StandardProjectOps extends ServiceBase {
	
	private static final ArahantLogger logger = new ArahantLogger(StandardProjectOps.class);


	@WebMethod()
	public SearchServicesReturn searchServices(/*@WebParam(name = "in")*/final SearchServicesInput in)	{

		final SearchServicesReturn ret=new SearchServicesReturn();
		
		try {
			checkLogin(in);
			
			ret.setProducts(BService.search(hsu, in.getAccountingSystemId(), in.getDescription(),ret.getHighCap()));
			
			if (!isEmpty(in.getStandardProjectId()))
			{
				BStandardProject sp=new BStandardProject(in.getStandardProjectId());
				if (sp.getProduct()!=null)
					ret.setSelectedItem(new SearchServicesReturnItem(sp.getProduct()));
			}
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}
	
	@WebMethod()
	public SearchEmployeesReturn searchEmployees(/*@WebParam(name = "in")*/final SearchEmployeesInput in)	{
		final SearchEmployeesReturn ret=new SearchEmployeesReturn();
		
		try
		{
			checkLogin(in);
			
			ret.setEmployees(BEmployee.searchEmployees(hsu, in.getSsn(),in.getFirstName(), in.getLastName(), null, 0, ret.getHighCap(),null));
			
			if (!isEmpty(in.getStandardProjectId()))
			{
				BStandardProject sp=new BStandardProject(in.getStandardProjectId());
				if (sp.getEmployee()!=null)
					ret.setSelectedItem(new SearchEmployeesReturnItem(sp.getEmployee()));
			}
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	/*
	deleteStandardProject
	 
	  Input: one or more standard project ids (e.g. multiple :)
	  Output: none
*/
	@WebMethod()
	public DeleteStandardProjectReturn deleteStandardProject(/*@WebParam(name = "in")*/final DeleteStandardProjectInput in )	{
		final DeleteStandardProjectReturn ret=new DeleteStandardProjectReturn();
	
		try {
			checkLogin(in);
			
			BStandardProject.delete(hsu,in.getProjectIds());

			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;

	}

	 
	/*
		1. listStandardProjects
		 
		Input: none
		Output: standard project id, standard project summary
		Remarks: gets the standard projects
	*/
	@WebMethod()
		public ListStandardProjectsReturn listStandardProjects(/*@WebParam(name = "in")*/final ListStandardProjectsInput in)		{
			final ListStandardProjectsReturn ret=new ListStandardProjectsReturn();

			try {			
				checkLogin(in);
				
				ret.setProjects(BStandardProject.list(hsu));
				
				finishService(ret);
			} 
			catch (final Throwable e) {
				handleError(hsu, e, ret, logger);
			}

			return ret;
		}
		
	@WebMethod()
		public LoadStandardProjectReturn loadStandardProject(/*@WebParam(name = "in")*/final LoadStandardProjectInput in)		{
			final LoadStandardProjectReturn ret=new LoadStandardProjectReturn();

			try {
				checkLogin(in);
				
				ret.setData(new BStandardProject(in.getProjectId()));
					
				finishService(ret);		
			} catch (final Throwable e) {
				handleError(hsu, e, ret, logger);
			}
			
			return ret;
		}
		
		/*
		saveStandardProject
		 
		  almost identical from my standpoint to saveProject, except that it does not take a requesting company id
		 */
	@WebMethod()
		public SaveStandardProjectReturn saveStandardProject(/*@WebParam(name = "in")*/final SaveStandardProjectInput in)		{
			logger.debug("In saveStandardProject");
			final SaveStandardProjectReturn ret=new SaveStandardProjectReturn();

			try {
				checkLogin(in);
				
				final BStandardProject sp=new BStandardProject(in.getProjectId());
				in.makeStandardProject(sp);
				sp.update();
			
				finishService(ret);
		
			} catch (final Throwable e) {
				handleError(hsu, e, ret, logger);
			}
			
			logger.debug("return from saveStandardProject");
			return ret;
		}
		
	@WebMethod()
		public SearchProjectCategoriesReturn searchProjectCategories(/*@WebParam(name = "in")*/final SearchProjectCategoriesInput in)		{
			final SearchProjectCategoriesReturn ret=new SearchProjectCategoriesReturn();

			try {
				checkLogin(in);
					
				ret.setProjectCategories(BProjectCategory.search(hsu,in.getCode(),in.getDescription(),ret.getHighCap()));
			
				if (!isEmpty(in.getStandardProjectId()))
				{
					BStandardProject sp=new BStandardProject(in.getStandardProjectId());
					ret.setSelectedItem(new SearchProjectCategoriesReturnItem(sp.getProjectCategory()));
				}
				
				finishService(ret);	
			} catch (final Throwable e) {
				handleError(hsu, e, ret, logger);
			}
						
			return ret;
		}
		

	@WebMethod()
		public SearchProjectTypesReturn searchProjectTypes(/*@WebParam(name = "in")*/final SearchProjectTypesInput in)		{
			final SearchProjectTypesReturn ret=new SearchProjectTypesReturn();

			try {
				checkLogin(in);
				
				ret.setProjectTypes(BProjectType.search(hsu,in.getCode(),in.getDescription(),ret.getHighCap()));
				
				if (!isEmpty(in.getStandardProjectId()))
				{
					BStandardProject sp=new BStandardProject(in.getStandardProjectId());
					ret.setSelectedItem(new SearchProjectTypesReturnItem(sp.getProjectType()));
				}
				
				finishService(ret);	
			} catch (final Throwable e) {
				handleError(hsu, e, ret, logger);
			}
			
			return ret;
		}
	@WebMethod()
		public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)		{
			final CheckRightReturn ret=new CheckRightReturn();
		
			try {
				checkLogin(in);
				
				ret.setAccessLevel(BRight.checkRight("AccessProjects"));
				ret.setCanSeeAllCompanies(BRight.checkRight(Right.RIGHT_CAN_ACCESS_ALL_COMPANIES) == ArahantConstants.ACCESS_LEVEL_WRITE);

				finishService(ret);
			} catch (final Throwable e) {
				handleError(hsu, e, ret, logger);
			}
			
			return ret;
		}
		
		
	@WebMethod()
		public NewStandardProjectReturn newStandardProject(/*@WebParam(name = "in")*/final NewStandardProjectInput in)		{
			final NewStandardProjectReturn ret=new NewStandardProjectReturn();
			
			try
			{
				checkLogin(in);
				
				final BStandardProject p=new BStandardProject();
				ret.setProjectId(p.create());
				in.makeStandardProject(p);
				p.insert();
				
				finishService(ret);
			}
			catch (final Throwable e) {
				handleError(hsu, e, ret, logger);
			}
			return ret;
		}
		

/*
 	AccountingManagementService.searchProductsAndServicesObj																																																																																																																																																																																																																																																													
		EmployeeEditorService.searchEmployeesObj																																																																																																																																																																																																																																																													
		ProjectManagementService.deleteStandardProjectObj																																																																																																																																																																																																																																																													
		ProjectManagementService.listStandardProjectsObj																																																																																																																																																																																																																																																													
		ProjectManagementService.loadStandardProjectObj																																																																																																																																																																																																																																																													
		ProjectManagementService.newStandardProjectObj																																																																																																																																																																																																																																																													
		ProjectManagementService.saveStandardProjectObj																																																																																																																																																																																																																																																													
		ProjectManagementService.searchProjectCategoriesObj																																																																																																																																																																																																																																																													
		ProjectManagementService.searchProjectTypesObj																																																																																																																																																																																																																																																													
		ProjectManagementService.searchProjectStatusesObj																																																																																																																																																																																																																																																													
		SecurityManagementService.checkRights																																																																																																																																																																																																																																																													

 */	
	@WebMethod()
	public GetDefaultsForCategoryAndTypeReturn getDefaultsForCategoryAndType(/*@WebParam(name = "in")*/final GetDefaultsForCategoryAndTypeInput in)			{
		final GetDefaultsForCategoryAndTypeReturn ret=new GetDefaultsForCategoryAndTypeReturn();
		try
		{
			checkLogin(in);
			BRouteTypeAssoc rta=new BRouteTypeAssoc(in.getProjectCategoryId(),in.getProjectTypeId());
			
			if (!isEmpty(rta.getRouteId()))
			{
				BRoute rt=new BRoute(rta.getRouteId());
				if (rt.hasInitialRouteStop() && rt.hasInitialStatus())
					ret.setData(rta);
			}
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
		
}

