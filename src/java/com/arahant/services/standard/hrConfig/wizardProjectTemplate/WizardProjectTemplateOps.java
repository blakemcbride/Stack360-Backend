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


package com.arahant.services.standard.hrConfig.wizardProjectTemplate;

import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitChangeReason;
import com.arahant.beans.ProjectStatus;
import com.arahant.beans.ProjectTemplateBenefit;
import com.arahant.beans.ProjectTemplateBenefitAssignment;
import com.arahant.beans.RouteStop;
import com.arahant.beans.RouteTypeAssoc;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

import com.arahant.business.*;
import com.arahant.business.BProjectTemplateBenefit;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.ArrayList;
import java.util.List;



/**
 *
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrConfigWizardProjectTemplateOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class WizardProjectTemplateOps extends ServiceBase {

	static ArahantLogger logger = new ArahantLogger(WizardProjectTemplateOps.class);

//	@WebMethod()
//	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
//		final CheckRightReturn ret=new CheckRightReturn();
//
//		try {
//			checkLogin(in);
//
//			ret.setAccessLevel(BRight.checkRight("AccessProjects"));
//
//			ret.setStatusAccessLevel(BRight.checkRight("AccessProjectStatus"));
//
//			if (ret.getStatusAccessLevel()!=ACCESS_LEVEL_WRITE)
//			{
//				BProject proj=new BProject(in.getProjectId());
//
//				if (isEmpty(proj.getOrgGroupId()))
//				{
//					BOrgGroup borg=new BOrgGroup(proj.getRequestingOrgGroupId());
//
//					List <String> ogl=borg.getAllOrgGroupsInHierarchy();
//
//					if (hsu.createCriteria(OrgGroupAssociation.class)
//						.eq(OrgGroupAssociation.PERSON, hsu.getCurrentPerson())
//						.joinTo(OrgGroupAssociation.ORGGROUP)
//						.in(OrgGroup.ORGGROUPID, ogl)
//						.exists())
//					{
//
//						ret.setStatusAccessLevel(BRight.checkRight("AccessProjectStatusInOrgGroup"));
//
//
//						if (ret.getStatusAccessLevel()!=ACCESS_LEVEL_WRITE)
//						{
//							//see if it's assigned
//							if (proj.getCurrentPersonId().equals(hsu.getCurrentPerson().getPersonId()))
//								ret.setStatusAccessLevel(BRight.checkRight("AccessProjStatusAssignInGroup"));
//
//						}
//					}
//				}
//				if (!isEmpty(proj.getOrgGroupId()))
//				{
//					BOrgGroup borg=new BOrgGroup(proj.getOrgGroupId());
//
//					List <String> ogl=borg.getAllOrgGroupsInHierarchy();
//
//					if (hsu.createCriteria(OrgGroupAssociation.class)
//						.eq(OrgGroupAssociation.PERSON, hsu.getCurrentPerson())
//						.joinTo(OrgGroupAssociation.ORGGROUP)
//						.in(OrgGroup.ORGGROUPID, ogl)
//						.exists())
//					{
//
//						ret.setStatusAccessLevel(BRight.checkRight("AccessProjectStatusInOrgGroup"));
//
//
//						if (ret.getStatusAccessLevel()!=ACCESS_LEVEL_WRITE)
//						{
//							//see if it's assigned
//							if (proj.getCurrentPersonId().equals(hsu.getCurrentPerson().getPersonId()))
//								ret.setStatusAccessLevel(BRight.checkRight("AccessProjStatusAssignInGroup"));
//
//						}
//					}
//				}
//			}
//
//			ret.setClientPriorityAccessLevel(BRight.checkRight("AccessClientPriority"));
//			ret.setCompanyPriorityAccessLevel(BRight.checkRight("AccessCompanyPriority"));
//			ret.setOrgGroupPriorityAccessLevel(BRight.checkRight("AccessOrgGroupPriority"));
//			finishService(ret);
//		} catch (final Throwable e) {
//			handleError(hsu, e, ret, logger);
//		}
//
//		return ret;
//	}


	@WebMethod()
	public SearchProjectCategoriesReturn searchProjectCategories(/*@WebParam(name = "in")*/final SearchProjectCategoriesInput in)	{
		final SearchProjectCategoriesReturn ret=new SearchProjectCategoriesReturn();

		try {
			checkLogin(in);

			boolean all=(hsu.currentlySuperUser() || hsu.getCurrentPerson().getOrgGroupType()==COMPANY_TYPE);

			ret.setProjectCategories(BProjectCategory.search(all,in.getCode(),in.getDescription(),null,ret.getHighCap()));

			if(!isEmpty(in.getProjectCategoryId()))
			{
				ret.setSelectedItem(new SearchProjectCategoriesReturnItem(new BProjectCategory(in.getProjectCategoryId())));
			}
			else if (!isEmpty(in.getProjectTemplateId())) {
				ret.setSelectedItem(new SearchProjectCategoriesReturnItem(new BProjectCategory(new BProjectTemplateBenefit(in.getProjectTemplateId()).getProjectCategoryId())));
			}
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}


		return ret;
	}

	@WebMethod()
	public SearchProjectTypesReturn searchProjectTypes(/*@WebParam(name = "in")*/final SearchProjectTypesInput in)	{
		final SearchProjectTypesReturn ret=new SearchProjectTypesReturn();

		try {
			checkLogin(in);

			boolean all=(hsu.currentlySuperUser() || hsu.getCurrentPerson().getOrgGroupType()==COMPANY_TYPE);

			ret.setProjectTypes(BProjectType.search(all,in.getCode(),in.getDescription(),ret.getHighCap()));

			if(!isEmpty(in.getProjectTypeId()))
			{
				ret.setSelectedItem(new SearchProjectTypesReturnItem(new BProjectType(in.getProjectTypeId())));
			}
			else if (!isEmpty(in.getProjectTemplateId())) {
				ret.setSelectedItem(new SearchProjectTypesReturnItem(new BProjectType(new BProjectTemplateBenefit(in.getProjectTemplateId()).getProjectTypeId())));
			}
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

			String projectTypeId = "";
			String projectCategoryId = "";
			RouteTypeAssoc rta;
			List<ProjectStatus> projectStatusList = new ArrayList<ProjectStatus>();
			BProject proj = null;

			if(!isEmpty(in.getProjectCategoryId()) && !isEmpty(in.getProjectTypeId()))
			{
				rta = hsu.createCriteria(RouteTypeAssoc.class).eq(RouteTypeAssoc.PROJECT_TYPE, new BProjectType(in.getProjectTypeId()).getBean())
																  .eq(RouteTypeAssoc.PROJECT_CATEGORY, new BProjectCategory(in.getProjectCategoryId()).getBean())
																  .joinTo(RouteTypeAssoc.ROUTE)
																  .first();

				if(rta == null)
					throw new ArahantWarning("This is not a valid Category/Type combination.");

				HibernateCriteriaUtil<RouteStop> routeStopHcu = hsu.createCriteria(RouteStop.class).eq(RouteStop.ROUTE, rta.getRoute());
				projectStatusList = hsu.createCriteria(ProjectStatus.class).setMaxResults(ret.getHighCap()).orderBy(ProjectStatus.CODE).like(ProjectStatus.CODE, in.getCode()).like(ProjectStatus.DESCRIPTION, in.getDescription()).joinTo(ProjectStatus.ALLOWED_ROUTE_STOPS).in(RouteStop.ROUTE_STOP_ID, routeStopHcu.selectFields(RouteStop.ROUTE_STOP_ID).list()).list();

				ret.setItem(BProjectStatus.makeArray(projectStatusList), proj);
			}
			else if (!isEmpty(in.getProjectTemplateId()))
			{
				projectTypeId = new BProjectTemplateBenefit(in.getProjectTemplateId()).getProjectTypeId();
				projectCategoryId = new BProjectTemplateBenefit(in.getProjectTemplateId()).getProjectCategoryId();
				
				rta = hsu.createCriteria(RouteTypeAssoc.class).eq(RouteTypeAssoc.PROJECT_TYPE, new BProjectType(projectTypeId).getBean())
																  .eq(RouteTypeAssoc.PROJECT_CATEGORY, new BProjectCategory(projectCategoryId).getBean())
																  .joinTo(RouteTypeAssoc.ROUTE)
																  .first();

				if(rta == null)
					throw new ArahantWarning("This is not a valid Category/Type combination.");
//				routeStopList = (List)hcu.selectFields(RouteTypeAssoc.ROUTE).list();
//				projectStatusList = (List)hcu.joinTo(RouteTypeAssoc.ROUTE).in(Route.ROUTE_STOPS, routeStopList)
//				projectStatusId = hcu.joinTo(RouteTypeAssoc.ROUTE).selectFields(Route.INITIAL_STATUS).stringVal();
				HibernateCriteriaUtil<RouteStop> routeStopHcu = hsu.createCriteria(RouteStop.class).eq(RouteStop.ROUTE, rta.getRoute());
				projectStatusList = hsu.createCriteria(ProjectStatus.class).setMaxResults(ret.getHighCap()).orderBy(ProjectStatus.CODE).like(ProjectStatus.CODE, in.getCode()).like(ProjectStatus.DESCRIPTION, in.getDescription()).joinTo(ProjectStatus.ALLOWED_ROUTE_STOPS).in(RouteStop.ROUTE_STOP_ID, routeStopHcu.selectFields(RouteStop.ROUTE_STOP_ID).list()).list();

//				proj = new BProject(hsu.createCriteria(Project.class).eq(Project.PROJECTSTATUS, new BProjectStatus(projectStatusId).getBean())
//																	 .eq(Project.PROJECTCATEGORY, new BProjectCategory(projectCategoryId).getBean())
//																	 .eq(Project.PROJECTTYPE, new BProjectType(projectTypeId).getBean())
//																	 .first());
				ret.setItem(BProjectStatus.makeArray(projectStatusList), proj);
			}
			else
				ret.setItem(BProjectStatus.makeArray(hsu.getAll(ProjectStatus.class)), proj);

			if(!isEmpty(in.getProjectStatusId()))
			{
				ret.setSelectedItem(new SearchProjectStatusesReturnItem(new BProjectStatus(in.getProjectStatusId())));
			}
			else if (!isEmpty(in.getProjectTemplateId())) 
			{
				ret.setSelectedItem(new SearchProjectStatusesReturnItem(new BProjectStatus(new BProjectTemplateBenefit(in.getProjectTemplateId()).getProjectStatusId())));
			}
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchWizardProjectTemplatesReturn searchWizardProjectTemplates(/*@WebParam(name = "in")*/final SearchWizardProjectTemplatesInput in) {

		final SearchWizardProjectTemplatesReturn ret = new SearchWizardProjectTemplatesReturn();

		try
		{
			checkLogin(in);

			if(!isEmpty(in.getProjectTypeId()))
			{
				ret.setProjectTemplate(BProjectTemplateBenefit.makeArray(hsu.createCriteria(ProjectTemplateBenefit.class).eq(ProjectTemplateBenefit.PROJECT_TYPE_ID, in.getProjectTypeId()).list()));
			}
			else
			{
				ret.setProjectTemplate(BProjectTemplateBenefit.makeArray(hsu.getAll(ProjectTemplateBenefit.class)));
			}

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public NewProjectTemplateReturn newProjectTemplate(/*@WebParam(name = "in")*/final NewProjectTemplateInput in) {

		final NewProjectTemplateReturn ret = new NewProjectTemplateReturn();

		try
		{
			checkLogin(in);
			BProjectTemplateBenefit ben = new BProjectTemplateBenefit();

			ben.create();
			ben.setBenefitId(in.getBenefitId());
			ben.setBcrId(in.getBcrId());
			ben.setProjectCategoryId(in.getProjectCategoryId());
			ben.setProjectTypeId(in.getProjectTypeId());
			ben.setProjectStatusId(in.getProjectStatusId());
			ben.setProjectDescription(in.getDescription());
			if(!isEmpty(in.getEmployeeStatusId()))
				ben.setEmployeeStatus(new BHREmployeeStatus(in.getEmployeeStatusId()).getBean());
			if(!isEmpty(in.getOrgGroupId()))
				ben.setOrgGroup(new BOrgGroup(in.getOrgGroupId()).getOrgGroup());
			ben.insert();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public SaveProjectTemplateReturn saveProjectTemplate(/*@WebParam(name = "in")*/final SaveProjectTemplateInput in) {

		final SaveProjectTemplateReturn ret = new SaveProjectTemplateReturn();

		try
		{
			checkLogin(in);
			
			BProjectTemplateBenefit ben = new BProjectTemplateBenefit(in.getProjectTemplateId());

			ben.setBenefitId(in.getBenefitId());
			ben.setBcrId(in.getBcrId());
			ben.setProjectCategoryId(in.getProjectCategoryId());
			ben.setProjectTypeId(in.getProjectTypeId());
			ben.setProjectStatusId(in.getProjectStatusId());
			ben.setProjectDescription(in.getDescription());
			if(!isEmpty(in.getEmployeeStatusId()))
				ben.setEmployeeStatus(new BHREmployeeStatus(in.getEmployeeStatusId()).getBean());
			else
				ben.setEmployeeStatus(null);
			if(!isEmpty(in.getOrgGroupId()))
				ben.setOrgGroup(new BOrgGroup(in.getOrgGroupId()).getOrgGroup());
			else
				ben.setOrgGroup(null);
			ben.update();
			
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

			List<HrBenefit> benes = hsu.createCriteria(HrBenefit.class).ne(HrBenefit.TIMERELATED, 'Y').orderBy(HrBenefit.NAME).list();

			ret.setItem(BHRBenefit.makeArray(benes));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public ListBenefitChangeReasonsReturn listBenefitChangeReasons(/*@WebParam(name = "in")*/final ListBenefitChangeReasonsInput in) {

		final ListBenefitChangeReasonsReturn ret = new ListBenefitChangeReasonsReturn();

		try
		{
			checkLogin(in);

			List<HrBenefitChangeReason> bcrs = hsu.createCriteria(HrBenefitChangeReason.class).orderBy(HrBenefitChangeReason.DESCRIPTION).list();

			ret.setItem(BHRBenefitChangeReason.makeArray(bcrs));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public DeleteWizardProjectTemplatesReturn deleteWizardProjectTemplates(/*@WebParam(name = "in")*/final DeleteWizardProjectTemplatesInput in) {

		final DeleteWizardProjectTemplatesReturn ret = new DeleteWizardProjectTemplatesReturn();

		try
		{
			checkLogin(in);

			BProjectTemplateBenefit.delete(in.getProjectTemplateBenefitIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public ListAssociatedOrgGroupsReturn listAssociatedOrgGroups(/*@WebParam(name = "in")*/final ListAssociatedOrgGroupsInput in)	{
		final ListAssociatedOrgGroupsReturn ret=new ListAssociatedOrgGroupsReturn();

		try
		{
			checkLogin(in);

			ret.setOrgGroups(BOrgGroup.listAssociatedCompanyGroups(hsu, in.getGroupId(), COMPANY_TYPE, ret.getCap()));

			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public ListVendorGroupsReturn listVendorGroups(/*@WebParam(name = "in")*/final ListVendorGroupsInput in)	{
		final ListVendorGroupsReturn ret=new ListVendorGroupsReturn();

		try
		{
			checkLogin(in);

			ret.setOrgGroups(BOrgGroup.listAssociatedGroups(hsu, in.getGroupId(), VENDOR_TYPE));

			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public ListEmployeesForOrgGroupReturn listEmployeesForOrgGroup(/*@WebParam(name = "in")*/final ListEmployeesForOrgGroupInput in) {
            final ListEmployeesForOrgGroupReturn ret=new ListEmployeesForOrgGroupReturn();

		try
		{
			checkLogin(in);

			BEmployee[] emps = BEmployee.listEmployees(hsu, in.getGroupId(),in.getLastName(),in.getSupervisor(),ret.getCap());
			List<BEmployee> goodEmps = new ArrayList<BEmployee>();
			List<String> excludeIds = new ArrayList<String>();
//			excludeIds.addAll((List)hsu.createCriteria(ProjectTemplateBCRAssignment.class).eq(ProjectTemplateBCRAssignment.PROJECT_TYPE_ID, in.getProjectTypeId())
//																				   .joinTo(ProjectTemplateBCRAssignment.PERSON)
//																				   .isEmployee()
//																				   .selectFields(Person.PERSONID)
//																				   .list());

			if(in.getExcludeIds() != null && in.getExcludeIds().length != 0) {
				for(String id : in.getExcludeIds())
					excludeIds.add(id);
			}

			for(BEmployee be : emps)
				if(!excludeIds.contains(be.getPersonId()))
					goodEmps.add(be);
			emps = new BEmployee[goodEmps.size()];
			for(int i = 0; i < goodEmps.size(); i++)
				emps[i] = goodEmps.get(i);
			
			ret.setEmployees(emps);

			if (!isEmpty(in.getGroupId()))
			{
				BOrgGroup grp=new BOrgGroup(in.getGroupId());
				ret.setCanAddOrEdit(grp.getCompanyId().equals(hsu.getCurrentCompany().getOrgGroupId()));
			}

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public  ListContactsReturn listContacts(/*@WebParam(name = "in")*/final ListContactsInput in)	{
		final ListContactsReturn ret=new ListContactsReturn();

		try
		{
			checkLogin(in);

			final BVendorContact []vcs=BVendorContact.list(hsu, in.getGroupId(),in.getLastNameStartsWith(),in.getPrimary(),ret.getCap(), in.getExcludeIds());

			ret.setPersons(vcs,in.getGroupId());

			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

    @WebMethod()
	public AssignToProjectTemplateReturn assignToProjectTemplate(/*@WebParam(name = "in")*/final AssignToProjectTemplateInput in) {

		final AssignToProjectTemplateReturn ret = new AssignToProjectTemplateReturn();

		try
		{
			checkLogin(in);

			ArahantSession.getHSU().createCriteria(ProjectTemplateBenefitAssignment.class).eq(ProjectTemplateBenefitAssignment.PROJECT_TEMPLATE_BENEFIT_ID, in.getProjectTemplateId()).delete();
			

			for(String id : in.getEmployeeIds()) {
					BProjectTemplateBenefitAssignment bta = new BProjectTemplateBenefitAssignment();
					bta.create();
					bta.setPerson(new BPerson(id).getPerson());
					bta.setProjectTemplateBenefit(new BProjectTemplateBenefit(in.getProjectTemplateId()).getBean());
					bta.insert();
			}
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public UnassignEmployeesFromProjectTemplateReturn unassignEmployeesFromProjectTemplate(/*@WebParam(name = "in")*/final UnassignEmployeesFromProjectTemplateInput in) {

		final UnassignEmployeesFromProjectTemplateReturn ret = new UnassignEmployeesFromProjectTemplateReturn();

		try
		{
			checkLogin(in);

			List<String> btaIds = (List)hsu.createCriteria(ProjectTemplateBenefitAssignment.class).eq(ProjectTemplateBenefitAssignment.PROJECT_TEMPLATE_BENEFIT_ID, in.getProjectTemplateId())
																					   .in(ProjectTemplateBenefitAssignment.PERSON_ID, in.getEmployeeIds())
																					   .selectFields(ProjectTemplateBenefitAssignment.ID)
																					   .list();
			String[] delIds = new String[btaIds.size()];
			for(int i = 0; i < btaIds.size(); i++)
				delIds[i] = btaIds.get(i);

			BProjectTemplateBenefitAssignment.delete(delIds);

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListEmployeeStatusesReturn listEmployeeStatuses(/*@WebParam(name = "in")*/final ListEmployeeStatusesInput in)		{
		final ListEmployeeStatusesReturn ret=new ListEmployeeStatusesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHREmployeeStatus.list(0)); //0 gets actives and inactives

			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchOrgGroupsReturn searchOrgGroups(/*@WebParam(name = "in")*/final SearchOrgGroupsInput in)
	{
		final SearchOrgGroupsReturn ret=new SearchOrgGroupsReturn();
		try
		{
			checkLogin(in);

			int type=COMPANY_TYPE;

			ret.setItem(BOrgGroup.searchOrgGroupsGeneric(hsu, in.getName(), 0, type, ret.getHighCap()));

			if(!isEmpty(in.getOrgGroupId()))
			{
				ret.setSelectedItem(new SearchOrgGroupsReturnItem(new BOrgGroup(in.getOrgGroupId())));
			}
			else if (!isEmpty(in.getProjectTemplateId()) && !isEmpty(new BProjectTemplateBenefit(in.getProjectTemplateId()).getOrgGroupId())) {
				ret.setSelectedItem(new SearchOrgGroupsReturnItem(new BOrgGroup(new BProjectTemplateBenefit(in.getProjectTemplateId()).getOrgGroupId())));
			}


			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListAssignedEmployeesReturn listAssignedEmployees(/*@WebParam(name = "in")*/final ListAssignedEmployeesInput in)		{
		final ListAssignedEmployeesReturn ret=new ListAssignedEmployeesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(ArahantSession.getHSU().createCriteria(ProjectTemplateBenefitAssignment.class).eq(ProjectTemplateBenefitAssignment.PROJECT_TEMPLATE_BENEFIT_ID, in.getProjectTemplateId()).list(), true);
				

			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

}
