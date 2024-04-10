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
package com.arahant.services.standard.project.subProjectParent;
import com.arahant.beans.ProjectStatus;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.SubProjectReport;
import com.arahant.utils.HibernateCriteriaUtil;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardProjectSubProjectParentOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class SubProjectParentOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			SubProjectParentOps.class);
	
	public SubProjectParentOps() {
		super();
	}
	
        @WebMethod()
	public ListSubProjectsForProjectReturn listSubProjectsForProject(/*@WebParam(name = "in")*/final ListSubProjectsForProjectInput in)		
	{
		final ListSubProjectsForProjectReturn ret=new ListSubProjectsForProjectReturn();
		try
		{
			checkLogin(in);

			ret.setItem(new BProject(in.getParentId()).listSubProjects());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public ListParentProjectsForProjectReturn listParentProjectsForProject(/*@WebParam(name = "in")*/final ListParentProjectsForProjectInput in)		
	{
		final ListParentProjectsForProjectReturn ret=new ListParentProjectsForProjectReturn();
		try
		{
			checkLogin(in);

			ret.setItem(new BProject(in.getSubProjectId()).listParentProjects());
			
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
			
			ret.setReportUrl(new SubProjectReport().build(in.getId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public NewSubProjectForParentProjectReturn newSubProjectForParentProject (/*@WebParam(name = "in")*/final NewSubProjectForParentProjectInput in)	{
		final NewSubProjectForParentProjectReturn ret=new NewSubProjectForParentProjectReturn();
		try
		{
			checkLogin(in);
			
			final BProject bp=new BProject();
			String id=bp.create();
			
			in.makeProject(bp);
			
			bp.insert();
			
			ret.setProjectId(bp.getProjectId());
			ret.setProjectName(bp.getProjectName());
			
			
			new BProject(in.getParentId()).addChildren(new String[]{id});
			finishService(ret);

		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		
		return ret;

	}
        @WebMethod()
	public SearchForProjectsToAssociateReturn searchForProjectsToAssociate(/*@WebParam(name = "in")*/final SearchForProjectsToAssociateInput in)		
	{
		final SearchForProjectsToAssociateReturn ret=new SearchForProjectsToAssociateReturn();
		try
		{
			checkLogin(in);

			
			BProject [] items=new BProject(in.getParentId()).search(in.getSummary(), in.getCategory(), in.getStatus(), in.getType(), in.getCompanyId(), in.getProjectName(),
					ret.getCap());
			
			ret.setItem(items);
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public AssociateSubProjectsToParentProjectReturn associateSubProjectsToParentProject(/*@WebParam(name = "in")*/final AssociateSubProjectsToParentProjectInput in)		
	{
		final AssociateSubProjectsToParentProjectReturn ret=new AssociateSubProjectsToParentProjectReturn();
		try
		{
			checkLogin(in);

			new BProject(in.getParentId()).addChildren(in.getSubProjectIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public DissassociateSubProjectsFromParentProjectReturn dissassociateSubProjectsFromParentProject(/*@WebParam(name = "in")*/final DissassociateSubProjectsFromParentProjectInput in)		
	{
		final DissassociateSubProjectsFromParentProjectReturn ret=new DissassociateSubProjectsFromParentProjectReturn();
		try
		{
			checkLogin(in);

			new BProject(in.getParentId()).removeChidren(in.getSubProjectIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public SearchCompanyByTypeReturn searchCompanyByType(/*@WebParam(name = "in")*/final SearchCompanyByTypeInput in)			{
		final SearchCompanyByTypeReturn ret=new SearchCompanyByTypeReturn();
		try
		{
			checkLogin(in);
			
			if (hsu.getCurrentPerson().getOrgGroupType()==CLIENT_TYPE)
			{
				BPerson bp=new BPerson(hsu.getCurrentPerson());
				BCompanyBase []ar=new BCompanyBase[1];
				ar[0]=bp.getCompany();
				ret.setCompanies(ar);
			}
			else
				ret.setCompanies(BCompanyBase.searchCompanySpecific(in.getName(),false,ret.getHighCap()));
		
			if (in.getAutoDefault())	
			{
				ret.setSelectedItem(new SearchCompanyByTypeReturnItem(new BPerson(hsu.getCurrentPerson()).getCompany()));
			}
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	@WebMethod()
	public SearchOrgGroupsForCompanyReturn searchOrgGroupsForCompany(/*@WebParam(name = "in")*/final SearchOrgGroupsForCompanyInput in)		
	{
		final SearchOrgGroupsForCompanyReturn ret=new SearchOrgGroupsForCompanyReturn();
		try
		{
			checkLogin(in);
		
			if ("ReqCo".equals(in.getCompanyId()))
			{
				SearchOrgGroupsForCompanyReturnItem ri=new SearchOrgGroupsForCompanyReturnItem();
				ri.setId("ReqOrg");
				ri.setName("Requesting Organizational Group");
				SearchOrgGroupsForCompanyReturnItem []items=new SearchOrgGroupsForCompanyReturnItem[1];
				items[0]=ri;
				ret.setItem(items);
				ret.setSelectedItem(ri);
			}
			else
			{

				ret.setItem(BCompanyBase.get(in.getCompanyId())
						.searchOrgGroups(in.getName(),ret.getHighCap()));
			}

			if (in.getAutoDefault()&&hsu.getCurrentPerson().getOrgGroupAssociations().size()==1)
				ret.setSelectedItem(new SearchOrgGroupsForCompanyReturnItem(
						new BOrgGroup(hsu.getCurrentPerson().getOrgGroupAssociations()
						.iterator().next().getOrgGroup())));
		

			hsu.rollbackTransaction();
			hsu.beginTransaction();
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	@WebMethod()
	public SearchProjectCategoriesReturn searchProjectCategories(/*@WebParam(name = "in")*/final SearchProjectCategoriesInput in)			{
		final SearchProjectCategoriesReturn ret=new SearchProjectCategoriesReturn();
		try
		{
			checkLogin(in);

			boolean all=(hsu.currentlySuperUser() || hsu.getCurrentPerson().getOrgGroupType()==COMPANY_TYPE);
			
			ret.setProjectCategories(BProjectCategory.search(all,in.getCode(),in.getDescription(),ret.getHighCap()));
			
			if (!isEmpty(in.getParentId()))
			{
				BProject p=new BProject(in.getParentId());
				ret.setSelectedItem(new SearchProjectCategoriesReturnItem(new BProjectCategory(p.getProjectCategoryId())));
			}
			
			if (!isEmpty(in.getCategoryId()))
				ret.setSelectedItem(new SearchProjectCategoriesReturnItem(new BProjectCategory(in.getCategoryId())));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	@WebMethod()
	public SearchProjectTypesReturn searchProjectTypes(/*@WebParam(name = "in")*/final SearchProjectTypesInput in)			{
		final SearchProjectTypesReturn ret=new SearchProjectTypesReturn();
		try
		{
			checkLogin(in);
			
			boolean all=(hsu.currentlySuperUser() || hsu.getCurrentPerson().getOrgGroupType()==COMPANY_TYPE);

			ret.setProjectTypes(BProjectType.search(all,in.getCode(),in.getDescription(),ret.getHighCap()));

			if (!isEmpty(in.getParentId()))
			{
				BProject p=new BProject(in.getParentId());
				ret.setSelectedItem(new SearchProjectTypesReturnItem(new BProjectType(p.getProjectTypeId())));
			}
			
			if (!isEmpty(in.getTypeId()))
				ret.setSelectedItem(new SearchProjectTypesReturnItem(new BProjectType(in.getTypeId())));
			
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
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
	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();
	
		try {
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight("AccessProjects"));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	@WebMethod()
	public SearchProjectStatusesReturn searchProjectStatuses(/*@WebParam(name = "in")*/final SearchProjectStatusesInput in) {
		final SearchProjectStatusesReturn ret = new SearchProjectStatusesReturn();
		try {
			checkLogin(in);

			//	ret.setItem(BProjectStatus.search(hsu, in.getCode(), in.getDescription(),in.getFromRouteStopId(), in.getStatusType(),ret.getHighCap()));


			//TODO: refactor into biz object
			BProject proj=null;
			if (!isEmpty(in.getProjectId()))
				proj=new BProject(in.getProjectId());
			//add to query, code and description

			HibernateCriteriaUtil<ProjectStatus> hcu = hsu.createCriteria(ProjectStatus.class).setMaxResults(ret.getHighCap()).orderBy(ProjectStatus.CODE).like(ProjectStatus.CODE, in.getCode()).like(ProjectStatus.DESCRIPTION, in.getDescription());

			if (!isEmpty(in.getStatusId())) {
				ret.setSelectedItem(new SearchProjectStatusesReturnItem(new BProjectStatus(in.getStatusId()),proj));
			}
			ret.setItem(BProjectStatus.makeArray(hcu.list()),proj);

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	@WebMethod()
	public GetProjectDetailReturn getProjectDetail(/*@WebParam(name = "in")*/final GetProjectDetailInput in)		
	{
		final GetProjectDetailReturn ret=new GetProjectDetailReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BProject(in.getProjectId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
