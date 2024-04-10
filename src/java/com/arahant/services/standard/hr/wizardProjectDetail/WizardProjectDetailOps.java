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
package com.arahant.services.standard.hr.wizardProjectDetail;

import com.arahant.beans.OrgGroup;
import com.arahant.beans.OrgGroupAssociation;
import com.arahant.beans.Person;
import com.arahant.beans.ProjectTemplateBenefitAssignment;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.*;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 
 * 
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrWizardProjectDetailOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class WizardProjectDetailOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			WizardProjectDetailOps.class); 
	
	public WizardProjectDetailOps() {
		super();
	}
	@WebMethod()
	public ListEmployeesForAssignmentReturn listEmployeesForAssignment(/*@WebParam(name = "in")*/final ListEmployeesForAssignmentInput in) {
		final ListEmployeesForAssignmentReturn ret=new ListEmployeesForAssignmentReturn();
		try
		{
			checkLogin(in);

//			HibernateCriteriaUtil<ProjectTemplateBCRAssignment> hcu = hsu.createCriteria(ProjectTemplateBCRAssignment.class);
//			Set<Person> emps = new HashSet<Person>();
//			if(!isEmpty(in.getProjectId())) {
//				hcu.joinTo(ProjectTemplateBCRAssignment.PROJECT_TYPE).joinTo(ProjectType.PROJECTS).eq(Project.PROJECTID, in.getProjectId());
//
//				emps = (Set)hcu.selectFields(ProjectTemplateBCRAssignment.PERSON).set();
//			}
//			else {
//				List<ProjectType> types = (List)hcu.eq(ProjectTemplateBCRAssignment.PERSON, hsu.getCurrentPerson())
//												   .selectFields(ProjectTemplateBCRAssignment.PROJECT_TYPE)
//												   .list();
//				emps = (Set)hsu.createCriteria(ProjectTemplateBCRAssignment.class).in(ProjectTemplateBCRAssignment.PROJECT_TYPE, types)
//																			.selectFields(ProjectTemplateBCRAssignment.PERSON)
//																			.set();
//			}

			//Set<Person> emps = (Set)hsu.createCriteria(ProjectTemplateBenefitAssignment.class).selectFields(ProjectTemplateBenefitAssignment.PERSON).joinTo(ProjectTemplateBenefitAssignment.PERSON).orderBy(Person.LNAME).orderBy(Person.FNAME).set();
			//ret.setItem(BPerson.makeArray(emps));

			Set<Person> empsSet = (Set)hsu.createCriteria(ProjectTemplateBenefitAssignment.class).selectFields(ProjectTemplateBenefitAssignment.PERSON).joinTo(ProjectTemplateBenefitAssignment.PERSON).orderBy(Person.LNAME).orderBy(Person.FNAME).set();

			List<Person> emps = new ArrayList<Person>();
			for (Person p : empsSet)
			{
				emps.add(p);
			}

			List<OrgGroup> ogl = ArahantSession.getHSU().createCriteria(OrgGroup.class).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, ArahantSession.getCurrentPerson()).list();
			if(ogl.size() > 0)
			{
				emps.addAll(ArahantSession.getHSU().createCriteria(Person.class).joinTo(Person.ORGGROUPASSOCIATIONS).in(OrgGroupAssociation.ORGGROUP, ogl).list());

			}
			//remove dups
			HashSet<Person> hs = new HashSet<Person>();
			hs.addAll(emps);
			emps.clear();
			emps.addAll(hs);

			//sort
			PersonComparator comparator = new PersonComparator();
			Collections.sort(emps, comparator);

			ret.setItem(BPerson.makeArray(emps));

			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	private class PersonComparator implements Comparator {
		@Override
		public int compare(Object ts1, Object ts2) {
			if(((Person)ts1).getLname().charAt(0) < ((Person)ts2).getLname().charAt(0))
				return -1;
			else if(((Person)ts1).getLname().charAt(0) > ((Person)ts2).getLname().charAt(0))
				return 1;
			else if(((Person)ts1).getLname().charAt(0) < ((Person)ts2).getLname().charAt(0))
				return -1;
			else
				return 1;
		}
	}

	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();
	
		try {
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight("AccessProjects"));
			//if this is a vendor, return 1, otherwise return 2
			if (hsu.getCurrentPerson().getOrgGroupType()==VENDOR_TYPE)
				ret.setTypeAccessLevel(1);
			else
				ret.setTypeAccessLevel(2);
			
			ret.setDetailAccessLevel(1); // WMCO
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public ListStatusesReturn listStatuses(/*@WebParam(name = "in")*/final ListStatusesInput in)			{
		final ListStatusesReturn ret=new ListStatusesReturn();
		try
		{
			checkLogin(in);

			BProject proj=new BProject(in.getProjectId()); //WMCO WORKFLOW
			
			if (!isEmpty(proj.getRouteStopId()))
			{
				BRouteStop brs=new BRouteStop(proj.getRouteStopId());
				ret.setItem(brs.listProjectStatuses(ret.getCap()));
			}
			else
				ret.setItem(BProjectStatus.list(hsu));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public SaveProjectReturn saveProject(/*@WebParam(name = "in")*/final SaveProjectInput in)			{
		final SaveProjectReturn ret=new SaveProjectReturn();
		try
		{
			checkLogin(in);
			
			final BProject x=new BProject(in.getProjectId());
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
	public LoadSummaryReturn loadSummary(/*@WebParam(name = "in")*/final LoadSummaryInput in)			{
		final LoadSummaryReturn ret=new LoadSummaryReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BProject(in.getProjectId()));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public ListProjectTypesReturn listProjectTypes(/*@WebParam(name = "in")*/final ListProjectTypesInput in)	{
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
	public GetAssignedEmployeeReturn getAssignedEmployee(/*@WebParam(name = "in")*/final GetAssignedEmployeeInput in)		
	{
		final GetAssignedEmployeeReturn ret=new GetAssignedEmployeeReturn();
		try
		{
			checkLogin(in);
			
			ret.setEmployeeId(new BProjectType(in.getProjectTypeId()).getRoutedEmployee(in.getPersonId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}


	@WebMethod()
	public ApproveChangesReturn approveChanges(/*@WebParam(name = "in")*/final ApproveChangesInput in)			{
		final ApproveChangesReturn ret=new ApproveChangesReturn();
		try
		{
			checkLogin(in);

			for(String id : in.getWizardProjectIds())
			{
				BWizardProject bwp = new BWizardProject(id);
				if(in.getApprove())
				{
					bwp.approve();
				}
				else
				{
					bwp.reject();
				}
			}

			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
