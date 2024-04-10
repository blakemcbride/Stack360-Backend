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
 *
 * Created on Feb 4, 2007
 */
package com.arahant.services.standard.project.projectCurrentStatus;

import com.arahant.beans.*;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import com.arahant.rest.GroovyService;
import com.arahant.utils.*;
import org.kissweb.TimeUtils;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

import com.arahant.business.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.services.ServiceBase;

import java.util.*;
import java.util.Collections;


@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardProjectProjectCurrentStatusOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class ProjectCurrentStatusOps extends ServiceBase {

    static ArahantLogger logger = new ArahantLogger(ProjectCurrentStatusOps.class);

    @WebMethod()
    public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in) {
        final CheckRightReturn ret = new CheckRightReturn();

        try {
            checkLogin(in);

            ret.setAccessLevel(BRight.checkRight("AccessProjects"));

            ret.setStatusAccessLevel(BRight.checkRight("AccessProjectStatus"));

            if (ret.getStatusAccessLevel() != ACCESS_LEVEL_WRITE) {
                BProject proj = new BProject(in.getProjectId());

                if (isEmpty(proj.getOrgGroupId())) {
                    BOrgGroup borg = new BOrgGroup(proj.getRequestingOrgGroupId());

                    List<String> ogl = borg.getAllOrgGroupsInHierarchy();

                    if (hsu.createCriteria(OrgGroupAssociation.class)
                            .eq(OrgGroupAssociation.PERSON, hsu.getCurrentPerson())
                            .joinTo(OrgGroupAssociation.ORGGROUP)
                            .in(OrgGroup.ORGGROUPID, ogl)
                            .exists()) {

                        ret.setStatusAccessLevel(BRight.checkRight("AccessProjectStatusInOrgGroup"));


                        if (ret.getStatusAccessLevel() != ACCESS_LEVEL_WRITE) {
                            //see if it's assigned
                            if (proj.getCurrentPersonId().equals(hsu.getCurrentPerson().getPersonId()))
                                ret.setStatusAccessLevel(BRight.checkRight("AccessProjStatusAssignInGroup"));

                        }
                    }
                }
                if (!isEmpty(proj.getOrgGroupId())) {
                    BOrgGroup borg = new BOrgGroup(proj.getOrgGroupId());

                    List<String> ogl = borg.getAllOrgGroupsInHierarchy();

                    if (hsu.createCriteria(OrgGroupAssociation.class)
                            .eq(OrgGroupAssociation.PERSON, hsu.getCurrentPerson())
                            .joinTo(OrgGroupAssociation.ORGGROUP)
                            .in(OrgGroup.ORGGROUPID, ogl)
                            .exists()) {

                        ret.setStatusAccessLevel(BRight.checkRight("AccessProjectStatusInOrgGroup"));


                        if (ret.getStatusAccessLevel() != ACCESS_LEVEL_WRITE) {
                            //see if it's assigned
                            if (proj.getCurrentPersonId().equals(hsu.getCurrentPerson().getPersonId()))
                                ret.setStatusAccessLevel(BRight.checkRight("AccessProjStatusAssignInGroup"));

                        }
                    }
                }
            }

            ret.setClientPriorityAccessLevel(BRight.checkRight("AccessClientPriority"));
            ret.setCompanyPriorityAccessLevel(BRight.checkRight("AccessCompanyPriority"));
            ret.setOrgGroupPriorityAccessLevel(BRight.checkRight("AccessOrgGroupPriority"));
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

            boolean all = (hsu.currentlySuperUser() || hsu.getCurrentPerson().getOrgGroupType() == COMPANY_TYPE);

            ret.setProjectCategories(BProjectCategory.search(all, in.getCode(), in.getDescription(), null, ret.getHighCap()));

            if (!isEmpty(in.getProjectId()))
                ret.setSelectedItem(new SearchProjectCategoriesReturnItem(new BProject(in.getProjectId()).getProjectCategory()));
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

            boolean all = (hsu.currentlySuperUser() || hsu.getCurrentPerson().getOrgGroupType() == COMPANY_TYPE);

            ret.setProjectTypes(BProjectType.search(all, in.getCode(), in.getDescription(), ret.getHighCap()));

            if (!isEmpty(in.getProjectId())) {
                ret.setSelectedItem(new SearchProjectTypesReturnItem(new BProject(in.getProjectId()).getProjectType()));
            }
            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }

        return ret;
    }

    @WebMethod()
    public SearchProjectSubtypesReturn searchProjectSubtypes(/*@WebParam(name = "in")*/final SearchProjectSubtypesInput in) {
        final SearchProjectSubtypesReturn ret = new SearchProjectSubtypesReturn();

        try {
            checkLogin(in);

            boolean all = (hsu.currentlySuperUser() || hsu.getCurrentPerson().getOrgGroupType() == COMPANY_TYPE);
            Connection db = KissConnection.get();

            List<Record> subtypes = db.fetchAll("select project_subtype_id, code " +
                    "from project_subtype " +
                    "where (company_id = ? or company_id is null) " +
                    "      and (last_active_date = 0 or last_active_date >= ?)" +
                    "order by code", hsu.getCurrentCompany().getCompanyId(), DateUtils.today());
            SearchProjectSubtypesReturnItem [] psts = new SearchProjectSubtypesReturnItem[subtypes.size()];
            for (int i=0 ; i < subtypes.size() ; i++) {
                Record r = subtypes.get(i);
                SearchProjectSubtypesReturnItem st = new SearchProjectSubtypesReturnItem(r.getString("project_subtype_id"), r.getString("code"));
                psts[i] = st;
            }
            ret.setItem(psts);

            Record r = db.fetchOne("select project_subtype_id from project where project_id = ?", in.getProjectId());
            ret.setSelectedSubtypeId(r.getString("project_subtype_id"));

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }

        return ret;
    }

    @WebMethod()
    public ListTimeRelatedBenefitsReturn listTimeRelatedBenefits(/*@WebParam(name = "in")*/final ListTimeRelatedBenefitsInput in) {
        final ListTimeRelatedBenefitsReturn ret = new ListTimeRelatedBenefitsReturn();

        try {
            checkLogin(in);

            ret.setItem(BHRBenefit.listTimeRelatedBenefits(hsu));

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }

        return ret;
    }

    @WebMethod()
    public GetDefaultsForCategoryAndTypeReturn getDefaultsForCategoryAndType(/*@WebParam(name = "in")*/final GetDefaultsForCategoryAndTypeInput in) {
        final GetDefaultsForCategoryAndTypeReturn ret = new GetDefaultsForCategoryAndTypeReturn();
        try {
            checkLogin(in);

            ret.setData(new BRouteTypeAssoc(in.getProjectCategoryId(), in.getProjectTypeId()));

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }


    @WebMethod()
    public GetDefaultsForRouteReturn getDefaultsForRoute(/*@WebParam(name = "in")*/final GetDefaultsForRouteInput in) {
        final GetDefaultsForRouteReturn ret = new GetDefaultsForRouteReturn();
        try {
            checkLogin(in);

            ret.setData(new BRoute(in.getRouteId()));

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }


    @WebMethod()
    public SearchForRouteReturn searchForRoute(/*@WebParam(name = "in")*/final SearchForRouteInput in) {
        final SearchForRouteReturn ret = new SearchForRouteReturn();
        try {
            checkLogin(in);

            ret.setItem(BRoute.search(in.getName(), ret.getHighCap()));

            if (!isEmpty(in.getProjectId())) {
                BProject proj = new BProject(in.getProjectId());
                if (!isEmpty(proj.getRouteId()))
                    ret.setSelectedItem(new SearchForRouteReturnItem(new BRoute(proj.getRouteId())));
            }
            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public ListPossibleRoutesReturn listPossibleRoutes(/*@WebParam(name = "in")*/final ListPossibleRoutesInput in) {
        final ListPossibleRoutesReturn ret = new ListPossibleRoutesReturn();
        try {
            checkLogin(in);

            Connection db = KissConnection.get();
            List<Record> recs = db.fetchAll(
                    "select r.route_id, r.name from route r " +
                            "inner join route_type_assoc rta " +
                            "  on r.route_id = rta.route_id " +
                            "where rta.project_category_id=? " +
                            "      and rta.project_type_id=?", in.getProjectCategoryId(), in.getProjectTypeId());
            ret.setItem(recs);

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public LoadCurrentStatusReturn loadCurrentStatus(/*@WebParam(name = "in")*/final LoadCurrentStatusInput in) {
        final LoadCurrentStatusReturn ret = new LoadCurrentStatusReturn();

        try {
            checkLogin(in);

            final BProject bp = new BProject(in.getProjectId());

            ret.setData(bp, in.getShiftId());

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

	/*
	private static boolean once = true;
	private void fix_records() {
	    if (!once)
	        return;
	    once = false;

	    hsu.commitTransaction();

	    Person currentPerson = hsu.getCurrentPerson();

	    Runnable task = () -> {
	        try {
                System.err.println("Correcting all projects");
                HibernateSessionUtil hsu = ArahantSession.openHSU(false);
                hsu.setCurrentPerson(currentPerson);
                HibernateScrollUtil<Project> scr = hsu.createCriteria(Project.class).scroll();
                while (scr.next()) {
                    Project proj = scr.get();
                    BProject bp = new BProject(proj);
                    if (!bp.getActive()) {
                        List<Person> assignedPeople = bp.getAssignedPersons2();
                        if (assignedPeople.size() > 0) {
                            bp.removeAssignments2(assignedPeople);
                            bp.update();
                        }
                    }
                }
                scr.close();
                hsu.commitTransaction();
            } finally {
                ArahantSession.clearSession();
                System.err.println("Done correcting all projects");
            }
        };
	    new Thread(task).start();
    }
*/

    @WebMethod()
    public SaveCurrentStatusReturn saveCurrentStatus(/*@WebParam(name = "in")*/final SaveCurrentStatusInput in) {
        final SaveCurrentStatusReturn ret = new SaveCurrentStatusReturn();

        try {
            checkLogin(in);

            BProject bp = new BProject(in.getProjectId());

            final AssignedWorkers[] assigned = in.getAssigned();
            if (assigned != null && assigned.length > 0) {
                for (AssignedWorkers aw : assigned) {
                    String pejId = aw.getProjectEmployeeJoinId();
                    ProjectShift newShift = new BProjectShift(aw.getShiftId()).getProjectShift();
                    Person person = new BPerson(aw.getPersonId()).getPerson();
                    if (pejId == null || pejId.isEmpty()) {
                        //  new assignment
                        // first, be sure the assignment doesn't already exist
                        ProjectEmployeeJoin pej = hsu.createCriteria(ProjectEmployeeJoin.class)
                                .eq(ProjectEmployeeJoin.PROJECTSHIFT, newShift)
                                .eq(ProjectEmployeeJoin.PERSON, person)
                                .first();
                        if (pej == null) {
                            pej = new ProjectEmployeeJoin();
                            pej.generateId();
                            pej.setPerson(person);
                            pej.setDateAssigned(DateUtils.today());
                            pej.setTimeAssigned(TimeUtils.now());
                            pej.setStartDate(aw.getStartDate());
                            pej.setProjectShift(newShift);
                            hsu.insert(pej);

                            //GroovyService.run(true, "", "Project", "notifyNewWorker", null, bp.getRequestingCompanyId(), aw.getPersonId());
                        }
                    } else {
                        final String shiftId = aw.getShiftId();
                        final String originalShiftId = aw.getOriginalShiftId();
                        boolean alreadyExists = false;
                        if (shiftId != null && originalShiftId != null && !shiftId.isEmpty() && !originalShiftId.isEmpty() && !shiftId.equals(originalShiftId)) {
                            // shift change
                            // first, make sure the new assignment doesn't already exist
                            ProjectEmployeeJoin pej = hsu.createCriteria(ProjectEmployeeJoin.class)
                                    .eq(ProjectEmployeeJoin.PROJECTSHIFT, newShift)
                                    .eq(ProjectEmployeeJoin.PERSON, person)
                                    .first();
                            alreadyExists = pej != null;
                        }
                        ProjectEmployeeJoin pej = hsu.createCriteria(ProjectEmployeeJoin.class)
                                .eq(ProjectEmployeeJoin.PROJECT_EMPLOYEE_JOIN_ID, pejId)
                                .first();
                        // we need this test because we've seen a problem when multiple people are
                        // editing the assignments at the same time.
                        if (pej != null)
                            if (!alreadyExists) {
                                //  change to existing assignment
                                pej.setProjectShift(newShift);
                                pej.setStartDate(aw.getStartDate());
                                pej.setHours("Y");
                                pej.setManager("N");
                                hsu.update(pej);
                            } else {
                                hsu.delete(pej);  //  new assignment already exists.  delete old
                            }
                    }
                }
            }

            final UnassignedItem[] unassignedItems = in.getUnassignedItems();
            if (unassignedItems != null)
                for (UnassignedItem ui : unassignedItems) {
                    final String pejId = ui.getProjectEmployeeJoin();
                    ProjectEmployeeJoin pej = hsu.createCriteria(ProjectEmployeeJoin.class)
                            .eq(ProjectEmployeeJoin.PROJECT_EMPLOYEE_JOIN_ID, pejId)
                            .first();
                    if (pej != null)
                        hsu.delete(pej);


                    //  Create event
                    Person person = hsu.get(Employee.class, ui.getPersonId());

                    // Check whether the person is an employee. If not skip the current iteration.
                    if (person == null)
                        continue;

                    final BHREmployeeEvent be = new BHREmployeeEvent();
                    be.create();
                    be.setSupervisorId(hsu.getCurrentPerson().getPersonId());

                    be.setEmployeeId(ui.getPersonId());
                    be.setEventDate(ui.getDate());
                    be.setEventType('N');

                    // TODO: Needs to change
                    be.setEmployeeNotified('Y');
                    be.setDateNotified(DateUtils.today());

                    be.setSummary(ui.getReason());

                    String unassignTime = DateUtils.getTimeFormatted(ui.getTime());
                    be.setDetail("(Unassigned at - " + unassignTime + ") " + ui.getComment());

                    // Save the record.
                    be.insert();
                }
            in.setData(bp);

/*
            private String projectEmployeeJoin;

            final AssignedWorkers [] assigned = in.getAssigned();
            if (assigned != null && assigned.length > 0) {
                for (AssignedWorkers aw : assigned)
                    if (aw.isStartDateChanged()) {
                        ProjectEmployeeJoin pej = hsu.createCriteria(ProjectEmployeeJoin.class)
                                .eq(ProjectEmployeeJoin.PERSON, (new BPerson(aw.getPersonId())).getPerson())
                                .joinTo(ProjectEmployeeJoin.PROJECTSHIFT)
                                .eq(ProjectShift.PROJECT, bp.getBean())
                                .first();
                        pej.setStartDate(aw.getStartDate());
                        hsu.update(pej);
                    }
            }

            boolean changingAssignments = false;

            LinkedList<String> p1 = new LinkedList<>();  // old list
            LinkedList<String> p2 = new LinkedList<>();  // new list
            for (Person per : bp.getAssignedPersons2(null))
                p1.add(per.getPersonId());
            Collections.addAll(p2, in.getPersonId());

            if (in.getPersonId().length > 0)
                changingAssignments = !(p1.containsAll(p2) && p2.containsAll(p1));

            // Filter only changed entries.
            if (in.getUnassignedItems() != null && in.getUnassignedItems().length > 0) {
                // Filter out non saved, unassigned items.
                final List<String> unassignedIds = p1.stream().filter(i -> !p2.contains(i)).collect(Collectors.toList());
                for (UnassignedItem u : in.getUnassignedItems())
                    if (unassignedIds.contains(u.getPersonId()))
                        unassignedItems.add(u);
            }

            LinkedList<String> newPeople = (LinkedList) p2.clone();
            newPeople.removeAll(p1);
            GroovyService.run(true, "", "Project", "notifyNewWorkers", null, bp.getRequestingCompanyId(), newPeople);

            if (in.getUnassignedItems() != null)
                for (UnassignedItem unassignedItem : unassignedItems) {
                    Person person = hsu.get(Employee.class, unassignedItem.getPersonId());

                    // Check whether the person is an employee. If not skip the current iteration.
                    if (person == null)
                        continue;

                    BHREmployeeEvent be = new BHREmployeeEvent();
                    be.create();
                    be.setSupervisorId(hsu.getCurrentPerson().getPersonId());

                    be.setEmployeeId(unassignedItem.getPersonId());
                    be.setEventDate(unassignedItem.getDate());
                    be.setEventType('N');

                    // TODO: Needs to change
                    be.setEmployeeNotified('Y');
                    be.setDateNotified(DateUtils.today());

                    be.setSummary(unassignedItem.getReason());

                    String unassignTime = DateUtils.getTimeFormatted(unassignedItem.getTime());
                    be.setDetail("(Unassigned at - " + unassignTime + ") " + unassignedItem.getComment());

                    // Save the record.
                    be.insert();
                }

            if (bp.getBillable() == 'U' && isEmpty(in.getBillable()) && changingAssignments)
                ret.setNeedsBillable(true);
            else {
                in.setData(bp);
                bp.update();
				/*
				if (BProperty.getBoolean("AutoProjectUnassign", false)) {
                    boolean newActive = (new BProjectStatus(in.getProjectStatusId())).getActive() > 0;
                    if (oldActive && !newActive)
                        bp.removeAssignments2(bp.getAssignedPersons2());
                }
                * /
            }

 */
            //fix_records();
            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }

        logger.debug("return from saveProject");
        return ret;
    }


    @WebMethod()
    public SearchCompanyByTypeReturn searchCompanyByType(/*@WebParam(name = "in")*/final SearchCompanyByTypeInput in) {
        final SearchCompanyByTypeReturn ret = new SearchCompanyByTypeReturn();
        try {
            checkLogin(in);

            BRoute route = null;
            BCompanyBase[] comps;
            if (isEmpty(in.getRouteId()))
                comps = BCompanyBase.searchCompanySpecific(in.getName(), false, ret.getHighCap());
            else {
                route = new BRoute(in.getRouteId());
                comps = route.searchCompanies(in.getName(), ret.getHighCap());
            }
            List<BCompanyBase> l = new ArrayList<>(comps.length + 1);
            BCompanyBase bcb;
            if (in.getIncludeRequesting() && (route == null || route.hasRequestingCompanies()))
                l.add(BCompanyBase.get("ReqCo"));

            Collections.addAll(l, comps);

            Collections.sort(l, new Comparator<BCompanyBase>() {

                @Override
                public int compare(BCompanyBase arg0, BCompanyBase arg1) {

                    if (arg0.getOrgGroupType() - (arg1.getOrgGroupType()) == 0)
                        return arg0.getName().compareTo(arg1.getName());

                    return arg0.getOrgGroupType() - (arg1.getOrgGroupType());
                }
            });

            ret.setCompanies(l.toArray(new BCompanyBase[0]));
            if (!isEmpty(in.getCompanyId())) {
                bcb = BCompanyBase.get(in.getCompanyId());
                if (bcb != null)
                    ret.setSelectedItem(new SearchCompanyByTypeReturnItem(bcb));
            }
            hsu.rollbackTransaction();
            hsu.beginTransaction();
            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public SearchOrgGroupsForCompanyReturn searchOrgGroupsForCompany(/*@WebParam(name = "in")*/final SearchOrgGroupsForCompanyInput in) {
        final SearchOrgGroupsForCompanyReturn ret = new SearchOrgGroupsForCompanyReturn();
        try {
            checkLogin(in);

            if ("ReqCo".equals(in.getCompanyId())) {
                SearchOrgGroupsForCompanyReturnItem ri = new SearchOrgGroupsForCompanyReturnItem();
                ri.setId("ReqOrg");
                ri.setName("Requesting Organizational Group");
                SearchOrgGroupsForCompanyReturnItem[] items = new SearchOrgGroupsForCompanyReturnItem[1];
                items[0] = ri;
                ret.setItem(items);
                ret.setSelectedItem(ri);
            } else {
                if (isEmpty(in.getRouteId()))
                    ret.setItem(BCompanyBase.get(in.getCompanyId())
                            .searchOrgGroups(in.getName(), ret.getHighCap()));
                else
                    ret.setItem(new BRoute(in.getRouteId()).searchOrgGroups(in.getCompanyId(), in.getName(), ret.getHighCap()));
                if (!isEmpty(in.getOrgGroupId()))
                    ret.setSelectedItem(new SearchOrgGroupsForCompanyReturnItem(new BOrgGroup(in.getOrgGroupId())));
            }
            hsu.rollbackTransaction();
            hsu.beginTransaction();
            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public SearchRouteStopsReturn searchRouteStops(/*@WebParam(name = "in")*/final SearchRouteStopsInput in) {
        final SearchRouteStopsReturn ret = new SearchRouteStopsReturn();
        try {
            checkLogin(in);

            ret.setItem(new BRoute(in.getRouteId()).search(in.getOrgGroupType(), in.getRouteStopName(), in.getOrgGroupName(),
                    in.getCompanyName(), in.getSearchType(), in.getCompanyId(), in.getOrgGroupId(), ret.getHighCap()));


            if (!isEmpty(in.getRouteStopId()))
                ret.setSelectedItem(new SearchRouteStopsReturnItem(new BRouteStop(in.getRouteStopId())));

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
            BProject proj = null;
            if (!isEmpty(in.getProjectId()))
                proj = new BProject(in.getProjectId());
            //add to query, code and description

            HibernateCriteriaUtil<ProjectStatus> hcu = hsu.createCriteria(ProjectStatus.class).setMaxResults(ret.getHighCap()).orderBy(ProjectStatus.CODE).like(ProjectStatus.CODE, in.getCode()).like(ProjectStatus.DESCRIPTION, in.getDescription());


            if (!isEmpty(in.getRouteStopId())) {
				/*the routeStopId is used to filter the statuses down to those statuses that are allowed for that route stop
				- if routeStopId is passed then excludeAlreadyUsed and routePathId should be examined, otherwise ignore them
				- excludeAlreadyUsed and routePathId are really used in tandem as follows:
				- excludeAlreadyUsed = false means you can ignore routePathId and no further filtering is needed
				- excludeAlreadyUsed = true means that i am searching for from statues for a route path:
				- do the routeStopId filtering above for allowed statuses
				- also exclude statuses already used in route paths with 
				the specified from routeStopId, unless the route path is the specified routePathId ***
				
				 *** This is done that way to handle new and existing route paths.  
				For new ones, I won't have a routePathId to send you.  
				For existing ones, I don't want you to exclude the status used as the
				from status of that route path even though it is already used, since it 
				is in fact being used by this one and the user needs to see it.  Therefore,
				I give you the routePathId so you can know to not filter that one out.
				 */

                hcu.joinTo(ProjectStatus.ALLOWED_ROUTE_STOPS).eq(RouteStop.ROUTE_STOP_ID, in.getRouteStopId());

                if (in.getExcludeAlreadyUsed()) {

                    List<ProjectStatus> ids = hsu.createCriteria(ProjectStatus.class)
                            .selectFields(ProjectStatus.PROJECTSTATUSID)
                            .joinTo(ProjectStatus.FROMROUTEPATHS)
                            .ne(RoutePath.ROUTE_PATH_ID, in.getRoutePathId())
                            .joinTo(RoutePath.FROM_ROUTE_STOP)
                            .eq(RouteStop.ROUTE_STOP_ID, in.getRouteStopId())
                            .list();
                    hcu.notIn(ProjectStatus.PROJECTSTATUSID, ids);
                }
            }
            if (!isEmpty(in.getStatusId())) {
                ret.setSelectedItem(new SearchProjectStatusesReturnItem(new BProjectStatus(in.getStatusId()), proj));
            }
            ret.setItem(BProjectStatus.makeArray(hcu.list()), proj);

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public SearchAssignablePersonsForProjectReturn searchAssignablePersonsForProject(/*@WebParam(name = "in")*/final SearchAssignablePersonsForProjectInput in) {
        final SearchAssignablePersonsForProjectReturn ret = new SearchAssignablePersonsForProjectReturn();
        try {
            checkLogin(in);

            if (in.getCompanyId().equals("ReqCo")) {
                BProject bp = new BProject(in.getProjectId());
                in.setCompanyId(bp.getCompanyId());
                in.setOrgGroupId(bp.getOrgGroupId());
            }

//			if (isEmpty(in.getOrgGroupId()))
//				in.setOrgGroupId(in.getCompanyId());

            String fname = in.getFirstName();
            String lname = in.getLastName();

            int max = ret.getCap() + 1 + in.getExcludePersonIds().length;

            BEmployee[] emps = BEmployee.searchEmployees(hsu, null, fname, lname, null, 0, max, 1, null, null);

            ret.setItem(emps, in.getExcludePersonIds());

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }


    @WebMethod()
    public GetProjectDetailReturn getProjectDetail(/*@WebParam(name = "in")*/final GetProjectDetailInput in) {
        final GetProjectDetailReturn ret = new GetProjectDetailReturn();
        try {
            checkLogin(in);

            ret.setData(new BProject(in.getProjectId()));

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public ListProjectsForPersonReturn listProjectsForPerson(/*@WebParam(name = "in")*/final ListProjectsForPersonInput in) {
        final ListProjectsForPersonReturn ret = new ListProjectsForPersonReturn();
        try {
            checkLogin(in);

            BPerson bp = new BPerson(in.getPersonId());

            ret.setItem(bp.listAssignedProjects(ret.getCap()), bp.getPerson());

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }


    @WebMethod()
    public SaveProjectForPersonReturn saveProjectForPerson(/*@WebParam(name = "in")*/final SaveProjectForPersonInput in) {
        final SaveProjectForPersonReturn ret = new SaveProjectForPersonReturn();
        try {
            checkLogin(in);

            HibernateCriteriaUtil<ProjectEmployeeJoin> hcu = hsu.createCriteria(ProjectEmployeeJoin.class);
            hcu.joinTo(ProjectEmployeeJoin.PROJECTSHIFT).joinTo(ProjectShift.PROJECT).eq(Project.PROJECTID, in.getProjectId());
            hcu.joinTo(ProjectEmployeeJoin.PERSON).eq(Person.PERSONID, in.getPersonId());

            ProjectEmployeeJoin pej = hcu.first();

            if (pej == null)
                throw new ArahantWarning(("Project is not assigned to that employee."));
            pej.setPersonPriority((short) in.getEmployeePriority());

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }


    @WebMethod()
    public CheckRightForBillingReturn checkRightForBilling(/*@WebParam(name = "in")*/final CheckRightForBillingInput in) {
        final CheckRightForBillingReturn ret = new CheckRightForBillingReturn();
        try {
            checkLogin(in);

            ret.setAccessLevel(BRight.checkRight("AccessOtherBillableLevel"));
            ret.setBillableAccessLevel(BRight.checkRight("AccessBillableLevel"));

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public ListParentProjectsReturn listParentProjects(/*@WebParam(name = "in")*/final ListParentProjectsInput in) {
        final ListParentProjectsReturn ret = new ListParentProjectsReturn();
        try {
            checkLogin(in);

            ret.setItem(new BProject(in.getId()).listParentProjects(), new BProject(in.getId()));

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public LoadBillingReturn loadBilling(/*@WebParam(name = "in")*/final LoadBillingInput in) {
        final LoadBillingReturn ret = new LoadBillingReturn();

        try {
            checkLogin(in);

            final BProject bp = new BProject(in.getProjectId());

            ret.setData(bp);

            finishService(ret);
        } catch (final Exception e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public SavePrioritiesForProjectsReturn savePrioritiesForProjects(/*@WebParam(name = "in")*/final SavePrioritiesForProjectsInput in) {
        final SavePrioritiesForProjectsReturn ret = new SavePrioritiesForProjectsReturn();
        try {
            checkLogin(in);

            for (SavePrioritiesForProjectsInputPriorities priority : in.getPriorities()) {
                BProject project = new BProject(priority.getProjectId());

                switch (in.getPriorityType()) {
                    case 1:
                        project.setCompanyPriority(priority.getPriority());
                        break;
                    case 2:
                        project.setOrgGroupPriority(priority.getPriority());
                        break;
                    case 3:
                        project.setClientPriority(priority.getPriority());
                        break;
                    case 4:
                        project.changePriority(in.getPersonId(), priority.getPriority());
                        break;
                    default:
                        throw new ArahantException("Unexpected priority type received.");
                }

                project.update();
            }

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

    @WebMethod()
    public CheckForWorkerCollisionReturn checkForWorkerCollision(/*@WebParam(name = "in")*/final CheckForWorkerCollisionInput in) {
        final CheckForWorkerCollisionReturn ret = new CheckForWorkerCollisionReturn();
        try {
            checkLogin(in);

            String projectId = in.getProjectId();  // the project they're trying to add
            String personId = in.getPersonId();

            Connection db = KissConnection.get();

            Record empRec = db.fetchOne("select p.i9_part1, e.employment_type " +
                    "from person p " +
                    "join employee e " +
                    "  on p.person_id = e.person_id " +
                    "where p.person_id = ?", personId);
            //  Assume not null.  If null, let system handle.
            ret.setI9part1Complete(empRec.getChar("employment_type") == 'C' || empRec.getString("i9_part1").equals("Y"));

            Record newProject = db.fetchOne("select estimated_first_date, estimated_last_date from project where project_id=?", projectId);
            if (newProject == null) {
                ret.setHasConflict(false);
                finishService(ret);
                return ret;
            }

            int firstDate = newProject.getInt("estimated_first_date");
            int lastDate = newProject.getInt("estimated_last_date");
            if (firstDate == 0  &&  lastDate == 0) {
                ret.setHasConflict(false);
                finishService(ret);
                return ret;
            }

            if (firstDate == 0)
                firstDate = lastDate;
            else if (lastDate == 0)
                lastDate = firstDate;

            List<Record> recs = db.fetchAll(
                    "select pej.project_employee_join_id " +
                    "from project_employee_join pej " +
                    "join project p " +
                    "  on pej.project_id = p.project_id " +
                    "join project_status s " +
                    "  on p.project_status_id = s.project_status_id " +
                    "where pej.project_id <> ? " +
                    "  and pej.person_id = ? " +
                    "  and p.estimated_first_date >= ? " +
                    "  and p.estimated_last_date <= ? " +
                    "  and s.active = 'Y'", projectId, personId, firstDate, lastDate);
            ret.setHasConflict(!recs.isEmpty());

            finishService(ret);
        } catch (final Throwable e) {
            handleError(hsu, e, ret, logger);
        }
        return ret;
    }

}
