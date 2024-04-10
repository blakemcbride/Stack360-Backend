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
package com.arahant.services.standard.hr.wizardProjectApprovals;

import com.arahant.beans.AIProperty;
import com.arahant.beans.CompanyDetail;
import com.arahant.beans.OrgGroup;
import com.arahant.beans.OrgGroupAssociation;
import com.arahant.beans.Person;
import com.arahant.beans.ProjectTemplateBenefitAssignment;
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
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardHrWizardProjectApprovalsOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class WizardProjectApprovalsOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(WizardProjectApprovalsOps.class);

	public WizardProjectApprovalsOps() {
	}

	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in) {
		final CheckRightReturn ret = new CheckRightReturn();

		try {
			checkLogin(in);

			ret.setAccessLevel(BRight.checkRight("AccessProjects"));
			ret.setAddAccessLevel(BRight.checkRight("canCreateProjects"));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	@SuppressWarnings("unchecked")
	public ListEmployeesForAssignmentReturn listEmployeesForAssignment(/*@WebParam(name = "in")*/final ListEmployeesForAssignmentInput in) {
		final ListEmployeesForAssignmentReturn ret = new ListEmployeesForAssignmentReturn();
		try {
			checkLogin(in);

//			HibernateCriteriaUtil<ProjectTemplateBenefitAssignment> hcu = hsu.createCriteria(ProjectTemplateBenefitAssignment.class);
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
			Set<Person> empsSet = (Set) hsu.createCriteria(ProjectTemplateBenefitAssignment.class).selectFields(ProjectTemplateBenefitAssignment.PERSON).joinTo(ProjectTemplateBenefitAssignment.PERSON).orderBy(Person.LNAME).orderBy(Person.FNAME).set();

			List<Person> emps = new ArrayList<Person>();
			for (Person p : empsSet)
				emps.add(p);

			List<OrgGroup> ogl = ArahantSession.getHSU().createCriteria(OrgGroup.class).joinTo(OrgGroup.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.PERSON, ArahantSession.getCurrentPerson()).list();
			if (ogl.size() > 0)
				emps.addAll(ArahantSession.getHSU().createCriteria(Person.class).joinTo(Person.ORGGROUPASSOCIATIONS).in(OrgGroupAssociation.ORGGROUP, ogl).list());
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
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListProjectsReturn listProjects(/*@WebParam(name = "in")*/final ListProjectsInput in) {
		final ListProjectsReturn ret = new ListProjectsReturn();
		try {
			checkLogin(in);

			if (!isEmpty(in.getEmployeeId()))
				ret.setItem(new BPerson(in.getEmployeeId()).listProjects(ret.getCap(), in.getStart(), in.getProjectTypeId(), in.getLocationId()));
			else
				ret.setItem(new BPerson(hsu.getCurrentPerson().getPersonId()).listProjectsForGroup(ret.getCap(), in.getStart(), in.getProjectTypeId(), in.getLocationId()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewProjectReturn newProject(/*@WebParam(name = "in")*/final NewProjectInput in) {
		final NewProjectReturn ret = new NewProjectReturn();
		try {
			checkLogin(in);

			final BProject x = new BProject();
			ret.setProjectId(x.create());
			in.setData(x);
			//	x.setProjectCategoryId(hsu.getFirst(ProjectCategory.class).getProjectCategoryId());
			x.setProjectTypeId(in.getProjectTypeId());
			x.setRequestingOrgGroupId(hsu.getFirst(CompanyDetail.class).getOrgGroupId());
			x.calculateRouteAndStatus();
			x.insert();

			ret.setProjectName(x.getProjectName());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListProjectTypesReturn listProjectTypes(/*@WebParam(name = "in")*/final ListProjectTypesInput in) {
		final ListProjectTypesReturn ret = new ListProjectTypesReturn();

		try {
			checkLogin(in);

//			if(!isEmpty(in.getEmployeeId()))
//				ret.setProjectTypes(new BEmployee(in.getEmployeeId()).listProjectTypeAssignments(), (isEmpty(in.getEmployeeId()))?null:new BPerson(in.getEmployeeId()));
//			else
//				ret.setProjectTypes(new BProjectType[0], null);

			ret.setProjectTypes(BProjectType.list(hsu), (isEmpty(in.getEmployeeId())) ? null : new BPerson(in.getEmployeeId()));
			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchPersonsReturn searchPersons(/*@WebParam(name = "in")*/final SearchPersonsInput in) {
		final SearchPersonsReturn ret = new SearchPersonsReturn();
		try {
			checkLogin(in);

			ret.setItem(BPerson.search2(in.getFirstName(), in.getLastName(), in.getSsn(), in.getSearchType(), ret.getCap()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetAssignedEmployeeReturn getAssignedEmployee(/*@WebParam(name = "in")*/final GetAssignedEmployeeInput in) {
		final GetAssignedEmployeeReturn ret = new GetAssignedEmployeeReturn();
		try {
			checkLogin(in);

			ret.setEmployeeId(new BProjectType(in.getProjectTypeId()).getRoutedEmployee(in.getPersonId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListStatusesReturn listStatuses(/*@WebParam(name = "in")*/final ListStatusesInput in) {
		final ListStatusesReturn ret = new ListStatusesReturn();
		try {
			checkLogin(in);


			AIProperty prop = new AIProperty("CalculateDefaultCategory", in.getProjectTypeId(), in.getPersonId());

			BRouteTypeAssoc rta = new BRouteTypeAssoc(prop.getValue(), in.getProjectTypeId());

			if (!isEmpty(rta.getRouteId())) {
				BRoute br = new BRoute(rta.getRouteId());
				ret.setItem(br.getInitialRouteStopStatuses());
			} else
				ret.setItem(new ListStatusesReturnItem[0]);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListLocationsReturn listLocations(/*@WebParam(name = "in")*/final ListLocationsInput in) {
		final ListLocationsReturn ret = new ListLocationsReturn();
		try {
			checkLogin(in);

			ret.setItem(BOrgGroup.getLocations());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	private class PersonComparator implements Comparator {

		@Override
		public int compare(Object ts1, Object ts2) {
			if (((Person) ts1).getLname().charAt(0) < ((Person) ts2).getLname().charAt(0))
				return -1;
			else if (((Person) ts1).getLname().charAt(0) > ((Person) ts2).getLname().charAt(0))
				return 1;
			else if (((Person) ts1).getLname().charAt(0) < ((Person) ts2).getLname().charAt(0))
				return -1;
			else
				return 1;
		}
	}
}
