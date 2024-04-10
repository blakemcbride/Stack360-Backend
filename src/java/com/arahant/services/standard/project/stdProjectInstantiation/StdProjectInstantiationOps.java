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
 * Created on Feb 10, 2007
 * 
 */
package com.arahant.services.standard.project.stdProjectInstantiation;

import com.arahant.beans.ClientCompany;
import com.arahant.beans.Right;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.*;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantConstants;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;


/** 
 * 
 *
 * Created on Feb 10, 2007
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardProjectStdProjectInstantiationOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class StdProjectInstantiationOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			StdProjectInstantiationOps.class);

	
	public StdProjectInstantiationOps() {
		super();
	}
 	
	@WebMethod()
	public SearchCompanyReturn searchCompany(/*@WebParam(name = "in")*/final SearchCompanyInput in)	{
		
		final SearchCompanyReturn ret=new SearchCompanyReturn();
		
		try {
			checkLogin(in);
						
			ret.setItem(BCompanyBase.searchCompanySpecific(in.getName(),false,ret.getHighCap()));
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		
		return ret;
	}
	
	/*	
	3. createProjectFromStandard
	 
	Input: client id, standard project id
	Output: project id, generated project name
	Remarks: instantiates a project for the specified client from a standard project
	
	*/
	@WebMethod()
	public CreateProjectFromStandardReturn createProjectFromStandard(/*@WebParam(name = "in")*/final CreateProjectFromStandardInput in)	{
		final CreateProjectFromStandardReturn ret=new CreateProjectFromStandardReturn();
		
		try {			
			checkLogin(in);
			
			ret.setProjects(BCompanyBase.createProjectsFromStandard(hsu,in.getCompanyId(),in.getProjectId()));
			
			finishService(ret);
		} 
		catch (final Throwable e) {
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
	public ListStandardProjectsReturn listStandardProjects(/*@WebParam(name = "in")*/final ListStandardProjectsInput in)	{
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
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
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


	public static void main (String args[])
	{
		HibernateSessionUtil hsu=ArahantSession.getHSU();
		hsu.beginTransaction();
		hsu.setCurrentPersonToArahant();

		String projectIds[]=new String[3];
		int count=0;
		for (com.arahant.beans.StandardProject p : hsu.getAll(com.arahant.beans.StandardProject.class))
			projectIds[count++]=p.getProjectId();

		for (ClientCompany c : hsu.getAll(ClientCompany.class))
		{

				BCompanyBase.createProjectsFromStandard(hsu,c.getOrgGroupId(),projectIds);
		}

		hsu.commitTransaction();
	}
}

/*
CompanyEditorService.searchCompanyByTypeObj																																																																																																																																																																																																																																																													
ProjectManagementService.createProjectFromStandardObj																																																																																																																																																																																																																																																													
ProjectManagementService.deleteProjectObj																																																																																																																																																																																																																																																													
ProjectManagementService.listStandardProjectsObj																																																																																																																																																																																																																																																													
SecurityManagementService.checkRights																																																																																																																																																																																																																																																													
*/
