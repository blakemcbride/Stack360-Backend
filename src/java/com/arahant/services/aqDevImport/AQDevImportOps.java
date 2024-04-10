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
package com.arahant.services.aqDevImport;

import com.arahant.beans.Project;
import com.arahant.beans.RouteStop;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
//import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

import com.arahant.business.*;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;



/**
 * 
 *
 * Created on Feb 4, 2007
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="AqDevImportOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class AQDevImportOps extends ServiceBase {
	
	static ArahantLogger logger = new ArahantLogger(AQDevImportOps.class);

	@WebMethod()
	public ListProjectCategoriesReturn listProjectCategories(/*webparam*/final ListProjectCategoriesInput in)	{
		final ListProjectCategoriesReturn ret=new ListProjectCategoriesReturn();

		try {
			checkLogin(in);
			
			ret.setProjectCategories(BProjectCategory.list(hsu));
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
	
		return ret;
	}
	
	@WebMethod()
	public NewProjectCategoryReturn newProjectCategory(/*webparam*/final NewProjectCategoryInput in)	{
		final NewProjectCategoryReturn ret=new NewProjectCategoryReturn();
		try {
			checkLogin(in);
			
			final BProjectCategory bpc=new BProjectCategory();
			ret.setProjectCategoryId(bpc.create());
			bpc.setCode(in.getCode());
			bpc.setDescription(in.getDescription());
			bpc.setScope("G");
			bpc.insert();		

			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
		
	}
	
	@WebMethod()
	public NewProjectTypeReturn newProjectType(/*webparam*/final NewProjectTypeInput in)	{
		final NewProjectTypeReturn ret=new NewProjectTypeReturn();
		try {
			checkLogin(in);
			
			final BProjectType bp=new BProjectType();
			ret.setProjectTypeId(bp.create());
			bp.setCode(in.getCode());
			bp.setDescription(in.getDescription());
			bp.setScope("G");
			bp.insert();
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
		
	}
	
	@WebMethod()
	public ListProjectTypesReturn listProjectTypes(/*webparam*/final ListProjectTypesInput in)	{
		final ListProjectTypesReturn ret=new ListProjectTypesReturn();

		try {
			checkLogin(in);
			
			ret.setProjectTypes(BProjectType.list(hsu));
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}


	@WebMethod()
	public NewProjectStatusReturn newProjectStatus(/*webparam*/final NewProjectStatusInput in)	{

		final NewProjectStatusReturn ret=new NewProjectStatusReturn();
		try {
			checkLogin(in);
			
			final BProjectStatus bp=new BProjectStatus();
			ret.setProjectStatusId(bp.create());
			bp.setCode(in.getCode());
			bp.setDescription(in.getDescription());
			bp.setActive('Y');
			bp.insert();
		
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
		
	}
	
	@WebMethod()
	public ListProjectStatusesReturn listProjectStatuses(/*webparam*/final ListProjectStatusesInput in)	{
		final ListProjectStatusesReturn ret=new ListProjectStatusesReturn();

		try {
			checkLogin(in);
			
			ret.setProjectStatuses(BProjectStatus.list(hsu));
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		
		return ret;
	}
	
	@WebMethod()
	public SaveProjectReturn saveProject(/*webparam*/final SaveProjectInput in)	{
		logger.debug("In saveProject");

		final SaveProjectReturn ret=new SaveProjectReturn();

		try {
			checkLogin(in);
			
			final BProject bp=new BProject(in.getProjectId());
			
			in.makeProject(bp);
			
			bp.update();
		
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		logger.debug("return from saveProject");
		return ret;
	}
	
	@WebMethod()
	public NewProjectReturn newProject(/*webparam*/final NewProjectInput in)	{
		final NewProjectReturn ret=new NewProjectReturn();
		try
		{
			checkLogin(in);
			
                        Project p=hsu.createCriteria(Project.class)
                               .eq(Project.REFERENCE, in.getReference())
                               .first();
                       
                        BProject bp=null;
                        if (p==null)
                        {
                        
                            bp=new BProject();
                            bp.create();

                            in.makeProject(bp);
                            bp.setRouteStopId(hsu.getFirst(RouteStop.class).getRouteStopId());

                            bp.insert();
                        }
                        else
                        {
                            bp=new BProject(p);
                            in.makeProject(bp);
			
                            bp.update();
                        }
                        
                        logger.info("Did "+bp.getReference());
                        
			ret.setProjectId(bp.getProjectId());
			ret.setProjectName(bp.getProjectName());
                        
                        
			
			finishService(ret);

		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		
		return ret;

	}
	
	@WebMethod()
	public SearchCompanyByTypeReturn searchCompanyByType(/*webparam*/final SearchCompanyByTypeInput in)	{
		final SearchCompanyByTypeReturn ret=new SearchCompanyByTypeReturn();

		try {
			checkLogin(in);
						
			ret.setCompanies(BCompanyBase.getAll());
	
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public ListProjectsReturn listProjects(/*webparam*/final ListProjectInput in)	{
		final ListProjectsReturn ret=new ListProjectsReturn();

		try {
			checkLogin(in);
						
			ret.setItem(BProject.list(hsu));
	
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
}

/*
ProjectManagement.ProjectCategoryTransmit []pct= pms.listProjectCategories(userId, webPassword);
 pst = pms.newProjectCategory(userId, webPassword, pst);
pst = pms.newProjectType(userId, webPassword, pst);
 pst = pms.newProjectStatus(userId, webPassword, pst);
ProjectManagement.ProjectStatusTransmit[] pct = pms.listProjectStatuses(userId, webPassword);
ProjectManagement.ProjectTypeTransmit[] pct = pms.listProjectTypes(userId, webPassword);

pms.saveProject(userId, webPassword, pt);
ProjectManagement.ProjectTransmit projTran = pms.newProject(userId, webPassword, pt);
CompanyTransmit[] ct = ces.listCompanyByType(userId, webPassword, tloop);  <- all companies
AQDevTeamXMLImport.ProjectManagement.ProjectTransmit[] ct = pms.listProjects(userId, webPassword);





*/
