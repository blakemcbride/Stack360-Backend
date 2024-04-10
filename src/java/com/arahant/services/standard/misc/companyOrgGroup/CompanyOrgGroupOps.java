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
package com.arahant.services.standard.misc.companyOrgGroup;

import com.arahant.beans.Right;
import com.arahant.business.*;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardMiscCompanyOrgGroupOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class CompanyOrgGroupOps extends ServiceBase {

	static ArahantLogger logger = new ArahantLogger(CompanyOrgGroupOps.class);

	@WebMethod()
	public DeleteEmployeeReturn deleteEmployee(/*@WebParam(name = "in")*/final DeleteEmployeeInput in) {
		final DeleteEmployeeReturn ret = new DeleteEmployeeReturn();

		try {
			checkLogin(in);

			BEmployee.delete(hsu, in.getPersonIds());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public ListEmployeesForOrgGroupReturn listEmployeesForOrgGroup(/*@WebParam(name = "in")*/final ListEmployeesForOrgGroupInput in) {
		final ListEmployeesForOrgGroupReturn ret = new ListEmployeesForOrgGroupReturn();

		try {
			checkLogin(in);

			ret.setEmployees(BEmployee.listEmployees(hsu, in.getGroupId(), in.getLastName(), in.getSupervisor(), ret.getCap()));

			if (!isEmpty(in.getGroupId())) {
				BOrgGroup grp = new BOrgGroup(in.getGroupId());
				ret.setCanAddOrEdit(grp.getCompanyId().equals(hsu.getCurrentCompany().getOrgGroupId()));
			}

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;

	}

	@WebMethod()
	public LoadEmployeeReturn loadEmployee(/*@WebParam(name = "in")*/final LoadEmployeeInput in) {
		LoadEmployeeReturn ret = new LoadEmployeeReturn();

		try {
			checkLogin(in);

			ret = new LoadEmployeeReturn(new BEmployee(in.getPersonId(), in.getGroupId()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public NewEmployeeReturn newEmployee(/*@WebParam(name = "in")*/final NewEmployeeInput in) {
		final NewEmployeeReturn ret = new NewEmployeeReturn();

		try {
			checkLogin(in);

			final BEmployee be = new BEmployee(hsu);
			be.create();
			in.makeEmployee(be);
			be.insert();
			be.setStatusId(in.getEmployeeStatusId(), in.getEmployeeStatusDate());
			be.setWageAndPosition(in.getPositionId(), in.getWageTypeId(), in.getWageAmount(), in.getEmployeeStatusDate());
			be.assignToOrgGroup(in.getOrgGroupId(), in.isPrimaryIndicator());

			ret.setPersonId(be.getPersonId());

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}


		return ret;
	}

	@WebMethod()
	public SaveEmployeeReturn saveEmployee(/*@WebParam(name = "in")*/final SaveEmployeeInput in) {
		final SaveEmployeeReturn ret = new SaveEmployeeReturn();
		try {
			checkLogin(in);

			final BEmployee be = new BEmployee(in.getPersonId());
			in.makeEmployee(be);
			be.update();
			be.assignToOrgGroup(in.getOrgGroupId(), in.isPrimaryIndicator());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchEmployeesReturn searchEmployees(/*@WebParam(name = "in")*/final SearchEmployeesInput in) {
		final SearchEmployeesReturn ret = new SearchEmployeesReturn();

		try {
			checkLogin(in);

			ret.setEmployees(BEmployee.searchEmployeesAssignScreen(hsu, in.getSsn(), in.getFirstName(), in.getLastName(), in.getAssociatedIndicator(), ret.getCap(), in.getOrgGroupId()));

			/*
			 if (!isEmpty(in.getOrgGroupId()))
			 {
			 BOrgGroup grp=new BOrgGroup(in.getOrgGroupId());
			 ret.setCanAddOrEdit(grp.getCompanyId().equals(hsu.getCurrentCompany().getOrgGroupId()));
			 }
			 */
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public AssignPersonToOrgGroupReturn assignPersonToOrgGroup(/*@WebParam(name = "in")*/final AssignPersonToOrgGroupInput in) {
		final AssignPersonToOrgGroupReturn ret = new AssignPersonToOrgGroupReturn();

		try {
			checkLogin(in);

			final BOrgGroup borg = new BOrgGroup(in.getGroupId());
			borg.assignPeopleToGroup(in.getPersonIds());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;

	}

	@WebMethod()
	public RemovePersonFromOrgGroupReturn removePersonFromOrgGroup(/*@WebParam(name = "in")*/final RemovePersonFromOrgGroupInput in) {
		final RemovePersonFromOrgGroupReturn ret = new RemovePersonFromOrgGroupReturn();
		try {
			checkLogin(in);

			final BOrgGroup borg = new BOrgGroup(in.getGroupId());
			borg.removePeopleFromGroup(in.getPersonIds());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public AddGroupToGroupReturn addGroupToGroup(/*@WebParam(name = "in")*/final AddGroupToGroupInput in) {
		final AddGroupToGroupReturn ret = new AddGroupToGroupReturn();
		try {
			checkLogin(in);

			final BOrgGroup borg = new BOrgGroup(in.getParentGroupID());
			borg.assignToThisGroup(in.getChildGroupID());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public DeleteGroupReturn deleteGroup(/*@WebParam(name = "in")*/final DeleteGroupInput in) {
		final DeleteGroupReturn ret = new DeleteGroupReturn();

		try {
			checkLogin(in);

			BOrgGroup.delete(hsu, in.getGroupIds());

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public ListAssociatedOrgGroupsReturn listAssociatedOrgGroups(/*@WebParam(name = "in")*/final ListAssociatedOrgGroupsInput in) {
		final ListAssociatedOrgGroupsReturn ret = new ListAssociatedOrgGroupsReturn();

		try {
			checkLogin(in);

			ret.setOrgGroups(BOrgGroup.listAssociatedCompanyGroups(hsu, in.getGroupId(), COMPANY_TYPE, ret.getCap()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public LoadGroupReturn loadGroup(/*@WebParam(name = "in")*/final LoadGroupInput in) {
		LoadGroupReturn ret = new LoadGroupReturn();

		try {
			checkLogin(in);

			ret = new LoadGroupReturn(new BOrgGroup(in.getGroupId()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public NewGroupReturn newGroup(/*@WebParam(name = "in")*/final NewGroupInput in) {
		final NewGroupReturn ret = new NewGroupReturn();

		try {
			checkLogin(in);

			final BOrgGroup bog = new BOrgGroup();
			bog.create();
			bog.setOrgGroupType(COMPANY_TYPE);
			bog.setName(in.getName());
			bog.setExternalId(in.getExternalId());
			bog.setPayPeriodsPerYear(in.getPayPeriodsPerYear());
			bog.setDefaultProjectId(in.getDefaultProjectId());
			bog.setPayScheduleId(in.getPayScheduleId());

			bog.setStreet(in.getAddressLine1());
			bog.setStreet2(in.getAddressLine2());
			bog.setCity(in.getCity());
			bog.setState(in.getStateProvince());
			bog.setZip(in.getZipPostalCode());
			bog.setCountry(in.getCountry());
			bog.setCounty(in.getCounty());
			bog.setMainPhoneNumber(in.getMainPhoneNumber());
			bog.setMainFaxNumber(in.getMainFaxNumber());
			bog.setEeo1UnitId(in.getEeoUnitNumber());
			bog.setEeo1Establishment(in.getEeoEstablishment());
			bog.setEeo1FiledLastYear(in.getEeoFiledLastYear());
			bog.setEeo1Headquarters(in.getEeoHQ());
			bog.setEvalEmailFirstDays(in.getEvalEmailFirstDays());
			bog.setEvalEmailNotify(in.getEvalEmailNotify());
			bog.setEvalEmailNotifyDays(in.getEvalEmailNotifyDays());
			bog.setEvalEmailNotifySendDays(in.getEvalEmailNotifySendDays());
			bog.setNewWeekBeginDay(in.getNewWeekBeginDay());
			if (!isEmpty(in.getBenefitClassId()))
				bog.setDefaultBenefitClass(new BBenefitClass(in.getBenefitClassId()).getBean());
			else
				bog.setDefaultBenefitClass(null);
			bog.setTimesheetPeriodType(in.getTimesheetPeriodType().charAt(0));
			bog.setTimesheetPeriodStartDate(in.getTimesheetPeriodStartDate());
			bog.setTimesheetShowBillable(in.getTimesheetShowBillable().charAt(0));
			bog.setCompanyId(in.getContextCompanyId());

			bog.insert();

			if (!isEmpty(in.getParentGroupId()))
				new BOrgGroup(in.getParentGroupId()).assignToThisGroup(bog.getOrgGroupId());
			else
				//parent is empty - If I'm not allowed to access all companies, then I need to assign 
				//current user to group, or it will vanish on them		
				if (BRight.checkRight(Right.RIGHT_CAN_ACCESS_ALL_COMPANIES) != ACCESS_LEVEL_WRITE)
					bog.assignPeopleToGroup(new String[]{hsu.getCurrentPerson().getPersonId()});
			ret.setOrgGroupId(bog.getOrgGroupId());

			//		bog.assignProjectStatuses(in.getStatusIds());

			bog.update();

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SaveGroupReturn saveGroup(/*@WebParam(name = "in")*/final SaveGroupInput in) {
		final SaveGroupReturn ret = new SaveGroupReturn();

		try {
			checkLogin(in);

			final BOrgGroup bog = new BOrgGroup(in.getOrgGroupId());
			bog.setName(in.getName());
			bog.setExternalId(in.getExternalId());
			bog.setPayPeriodsPerYear(in.getPayPeriodsPerYear());
			bog.setDefaultProjectId(in.getDefaultProjectId());
			bog.setPayScheduleId(in.getPayScheduleId());
			//		bog.replaceProjectStatuses(in.getStatusIds());
			bog.setStreet(in.getAddressLine1());
			bog.setStreet2(in.getAddressLine2());
			bog.setCity(in.getCity());
			bog.setState(in.getStateProvince());
			bog.setZip(in.getZipPostalCode());
			bog.setCountry(in.getCountry());
			bog.setCounty(in.getCounty());
			bog.setMainPhoneNumber(in.getMainPhoneNumber());
			bog.setMainFaxNumber(in.getMainFaxNumber());
			bog.setEeo1UnitId(in.getEeoUnitNumber());
			bog.setEeo1Establishment(in.getEeoEstablishment());
			bog.setEeo1FiledLastYear(in.getEeoFiledLastYear());
			bog.setEeo1Headquarters(in.getEeoHQ());
			bog.setEvalEmailFirstDays(in.getEvalEmailFirstDays());
			bog.setEvalEmailNotify(in.getEvalEmailNotify());
			bog.setEvalEmailNotifyDays(in.getEvalEmailNotifyDays());
			bog.setEvalEmailNotifySendDays(in.getEvalEmailNotifySendDays());
			bog.setNewWeekBeginDay(in.getNewWeekBeginDay());
			if (!isEmpty(in.getBenefitClassId()))
				bog.setDefaultBenefitClass(new BBenefitClass(in.getBenefitClassId()).getBean());
			else
				bog.setDefaultBenefitClass(null);
			bog.setTimesheetPeriodType(in.getTimesheetPeriodType().charAt(0));
			bog.setTimesheetPeriodStartDate(in.getTimesheetPeriodStartDate());
			bog.setTimesheetShowBillable(in.getTimesheetShowBillable().charAt(0));

			bog.update();

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}


	/*
	 * 1. search org groups generically (used so I can find an org group in the system and navigate it, edit the tree)
 
	 - name filter
	 - dissassociated means all org groups in any company or no company that do not have a parent
	 - associated means all org groups in in any company or no company that do have a parent
	 */
	@WebMethod()
	public SearchOrgGroupsGenericReturn searchOrgGroupsGeneric(/*@WebParam(name = "in")*/final SearchOrgGroupsGenericInput in) {
		final SearchOrgGroupsGenericReturn ret = new SearchOrgGroupsGenericReturn();

		try {
			checkLogin(in);
			if (BRight.checkRight(Right.RIGHT_CAN_ACCESS_ALL_COMPANIES) == ACCESS_LEVEL_WRITE)
				ret.setOrgGroups(BOrgGroup.searchOrgGroupsGenericNoFilter(hsu, in.getName(), in.getAssociatedIndicator(), COMPANY_TYPE, ret.getCap()));
			else
				ret.setOrgGroups(BOrgGroup.searchOrgGroupsGeneric(hsu, in.getName(), in.getAssociatedIndicator(), COMPANY_TYPE, ret.getCap()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}


		return ret;
	}

	/*2. search org groups for purpose of associating
	 
	 - pass the current org group the user is in or "" for no current org group (top)
	 
	 assuming the org group passed in was a company org group (is or has a parent that is company)
	 
	 - name filter
	 - dissassociated means all org groups in the company that do not have a parent
	 - associated means all org groups in the company that do have a parent
	 - should never see in the results the current org group the user is in
	 - should never see in the results the parents of the org group the user is in
	 - should never see in the results the org groups that are or have as a child the current org group
	 
	 assuming the org group passed in was not a company org group
	 
	 - name filter
	 - dissassociated means all org groups in any company or no company that do not have a parent
	 - associated means all org groups in in any company or no company that do have a parent
	 - should never see in the results the current org group the user is in
	 - should never see in the results the parents of the org group the user is in
	 - should never see in the results the org groups that are or have as a child the current org group
	 */
	@WebMethod()
	public SearchOrgGroupsReturn searchOrgGroups(/*@WebParam(name = "in")*/final SearchOrgGroupsInput in) {
		final SearchOrgGroupsReturn ret = new SearchOrgGroupsReturn();

		try {
			checkLogin(in);

			ret.setOrgGroups(BOrgGroup.searchOrgGroups(hsu, in.getName(), in.getOrgGroupId(), in.getAssociatedIndicator(), COMPANY_TYPE, ret.getCap()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public RemoveGroupFromGroupReturn removeGroupFromGroup(/*@WebParam(name = "in")*/final RemoveGroupFromGroupInput in) {
		final RemoveGroupFromGroupReturn ret = new RemoveGroupFromGroupReturn();
		try {
			checkLogin(in);

			new BOrgGroup(in.getParentGroupID()).removeGroups(in.getChildGroupIDs());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchScreenGroupsReturn searchScreenGroups(/*@WebParam(name = "in")*/final SearchScreenGroupsInput in) {
		final SearchScreenGroupsReturn ret = new SearchScreenGroupsReturn();

		try {
			checkLogin(in);

			ret.setScreenDef(BScreenGroup.searchScreenGroups(hsu, in.getName(), in.getExtId(), in.getSearchTopLevelOnly() ? 2 : 0, "", 2, ret.getCap()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public ListEEOCategoriesReturn listEEOCategories(/*@WebParam(name = "in")*/final ListEEOCategoriesInput in) {
		final ListEEOCategoriesReturn ret = new ListEEOCategoriesReturn();
		try {
			checkLogin(in);

			ret.setItem(BHREEOCategory.list(hsu));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListEEORacesReturn listEEORaces(/*@WebParam(name = "in")*/final ListEEORacesInput in) {
		final ListEEORacesReturn ret = new ListEEORacesReturn();
		try {
			checkLogin(in);

			ret.setItem(BHREEORace.list(hsu));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListW4StatusesReturn listW4Statuses(/*@WebParam(name = "in")*/final ListW4StatusesInput in) {
		final ListW4StatusesReturn ret = new ListW4StatusesReturn();
		try {
			checkLogin(in);

			//ret.setItem(BHRW4Status.list(hsu));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchSecurityGroupsReturn searchSecurityGroups(/*@WebParam(name = "in")*/final SearchSecurityGroupsInput in) {
		final SearchSecurityGroupsReturn ret = new SearchSecurityGroupsReturn();

		try {
			checkLogin(in);
			ret.setSecurityDef(BSecurityGroup.searchSecurityGroups(in.getName(), ret.getCap()));
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public ListEmployeeStatusesReturn listEmployeeStatuses(/*@WebParam(name = "in")*/final ListEmployeeStatusesInput in) {
		final ListEmployeeStatusesReturn ret = new ListEmployeeStatusesReturn();
		try {
			checkLogin(in);

			ret.setItem(BHREmployeeStatus.list(hsu));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetParentPayPeriodsReturn getParentPayPeriods(/*@WebParam(name = "in")*/final GetParentPayPeriodsInput in) {
		final GetParentPayPeriodsReturn ret = new GetParentPayPeriodsReturn();
		try {
			checkLogin(in);

			if (!isEmpty(in.getId())) {

				BOrgGroup borg = new BOrgGroup(in.getId());
				if (borg.getPayPeriodsPerYear() > 0)
					ret.setPayPeriodsPerYear(borg.getPayPeriodsPerYear());
				else
					ret.setPayPeriodsPerYear(borg.getParentPayPeriodsPerYear());
			}

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadMetaReturn loadMeta(/*@WebParam(name = "in")*/final LoadMetaInput in) {
		final LoadMetaReturn ret = new LoadMetaReturn();
		try {
			checkLogin(in);

			ret.setNewPersonDefaultExternalId(BProperty.getInt("DefaultEmployeeIDType"));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchCompanyReturn searchCompany(/*@WebParam(name = "in")*/final SearchCompanyInput in) {
		final SearchCompanyReturn ret = new SearchCompanyReturn();

		try {
			checkLogin(in);

			ret.setCompanies(BCompanyBase.search(in.getName(), false, ret.getHighCap()));

			if (!isEmpty(in.getCompanyId()))
				ret.setSelectedItem(new SearchCompanyReturnItem(BCompanyBase.get(in.getCompanyId())));

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchProjectCategoriesReturn searchProjectCategories(/*@WebParam(name = "in")*/final SearchProjectCategoriesInput in) {
		final SearchProjectCategoriesReturn ret = new SearchProjectCategoriesReturn();

		try {
			checkLogin(in);

			ret.setProjectCategories(BProjectCategory.search(hsu, in.getCode(), in.getDescription(), ret.getHighCap()));

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

			ret.setProjectStatuses(BProjectStatus.search(hsu, in.getCode(), in.getDescription(), ret.getHighCap()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchProjectTypesReturn searchProjectTypes(/*@WebParam(name = "in")*/final SearchProjectTypesInput in) {
		final SearchProjectTypesReturn ret = new SearchProjectTypesReturn();

		try {
			checkLogin(in);

			ret.setProjectTypes(BProjectType.search(hsu, in.getCode(), in.getDescription(), ret.getHighCap()));
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchProjectsReturn searchProjects(/*@WebParam(name = "in")*/final SearchProjectsInput in) {
		final SearchProjectsReturn ret = new SearchProjectsReturn();

		try {
			checkLogin(in);

			ret.setProjects(BProject.search(hsu, in.getSummary(), in.getCategory(), in.getStatus(), in.getType(), in.getCompanyId(), in.getProjectName(),
					0, 0, null, false, in.getUser(), null, null, ret.getHighCap(), true));


			if (!isEmpty(in.getOrgGroupId())) {
				String defaultProjectId = new BOrgGroup(in.getOrgGroupId()).getExplicitProjectId();

				if (!isEmpty(defaultProjectId))
					ret.setSelectedItem(new SearchProjectsReturnItem(new BProject(defaultProjectId)));
			} else if (!isEmpty(in.getPersonId())) {
				String defaultProjectId = new BEmployee(in.getPersonId()).getExplicitProjectId();

				if (!isEmpty(defaultProjectId))
					ret.setSelectedItem(new SearchProjectsReturnItem(new BProject(defaultProjectId)));
			}
			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetInheritedDefaultProjectReturn getInheritedDefaultProject(/*@WebParam(name = "in")*/final GetInheritedDefaultProjectInput in) {
		final GetInheritedDefaultProjectReturn ret = new GetInheritedDefaultProjectReturn();
		try {
			checkLogin(in);

			String id = null;
			if (!isEmpty(in.getPersonId()))
				id = new BPerson(in.getPersonId()).getInheritedDefaultProjectId();

			if (!isEmpty(in.getOrgGroupId()))
				id = new BOrgGroup(in.getOrgGroupId()).getDefaultProjectId();


			if (!isEmpty(id))
				ret.setData(new BProject(id));

			if (!isEmpty(in.getOrgGroupId()))
				ret.setInheritedNewWeekBeginDay(new BOrgGroup(in.getOrgGroupId()).getInheritedNewWeekBeginDayFormatted());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetInheritedPayScheduleReturn getInheritedPaySchedule(/*@WebParam(name = "in")*/final GetInheritedPayScheduleInput in) {
		final GetInheritedPayScheduleReturn ret = new GetInheritedPayScheduleReturn();
		try {
			checkLogin(in);

			if (!isEmpty(in.getOrgGroupId()))
				ret.setPayScheduleName(new BOrgGroup(in.getOrgGroupId()).getDefaultPayScheduleName());
			else
				ret.setPayScheduleName("");

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListPaySchedulesReturn listPaySchedules(/*@WebParam(name = "in")*/final ListPaySchedulesInput in) {
		final ListPaySchedulesReturn ret = new ListPaySchedulesReturn();
		try {
			checkLogin(in);

			ret.setItem(BPaySchedule.makeArray(BPaySchedule.list(in.getOrgGroupId())));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListPositionsReturn listPositions(/*@WebParam(name = "in")*/final ListPositionsInput in) {
		final ListPositionsReturn ret = new ListPositionsReturn();
		try {
			checkLogin(in);

			ret.setItem(BHRPosition.list(hsu));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListWageTypesReturn listWageTypes(/*@WebParam(name = "in")*/final ListWageTypesInput in) {
		final ListWageTypesReturn ret = new ListWageTypesReturn();
		try {
			checkLogin(in);

			ret.setItem(BWageType.listActiveNonDedutionsPlus(null));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListBenefitClassesReturn listBenefitClasses(/*@WebParam(name = "in")*/final ListBenefitClassesInput in) {
		final ListBenefitClassesReturn ret = new ListBenefitClassesReturn();
		try {
			checkLogin(in);

			ret.setItem(BBenefitClass.listActive());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchAssociatedOrgGroupsReturn searchAssociatedOrgGroups(/*@WebParam(name = "in")*/final SearchAssociatedOrgGroupsInput in) {
		final SearchAssociatedOrgGroupsReturn ret = new SearchAssociatedOrgGroupsReturn();

		try {
			checkLogin(in);

			ret.setOrgGroups(BOrgGroup.listAssociatedOrgGroups(hsu, in.getGroupId(), EMPLOYEE_TYPE, in.getName(), ret.getCap()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchAssociatedEmployeesReturn searchAssociatedEmployees(/*@WebParam(name = "in")*/final SearchAssociatedEmployeesInput in) {
		final SearchAssociatedEmployeesReturn ret = new SearchAssociatedEmployeesReturn();

		try {
			checkLogin(in);

			final BEmployee[] be = BEmployee.listEmployees(hsu, in.getGroupId(), ret.getCap(), in.getName());

			ret.setPersons(be, in.getGroupId());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}
}
