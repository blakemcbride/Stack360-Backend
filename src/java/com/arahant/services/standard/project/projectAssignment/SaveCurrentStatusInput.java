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
 * Created on Feb 8, 2007
 */
package com.arahant.services.standard.project.projectAssignment;

import com.arahant.annotation.Validation;
import com.arahant.beans.Person;
import com.arahant.business.BProject;
import com.arahant.business.BProjectShift;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitInputBase;

import java.util.ArrayList;
import java.util.List;

public class SaveCurrentStatusInput extends TransmitInputBase {

    @Validation(required = true)
    private String projectStatusId;
    @Validation(required = true)
    private String projectCategoryId;
    @Validation(required = true)
    private String projectTypeId;
    @Validation(required = true)
    private String projectId;
    @Validation(table = "project", column = "route_stop_id", required = true)
    private String routeStopId;
    @Validation(min = 1, max = 10000, required = true)
    private int companyPriority;
    @Validation(min = 1, max = 10000, required = true)
    private int orgGroupPriority;
    @Validation(min = 1, max = 10000, required = true)
    private int clientPriority;
    @Validation(required = false)
    private String[] personId;  //  ?? - doesn't come from front end  ???
    @Validation(required = false)
    private String billable;
    @Validation(min = 0, required = false)
    private double billingRate;
    @Validation(required = false)
    private String primaryParentId;
    @Validation(table = "project", column = "estimate_hours", required = false)
    private double estimatedHours;
    private short requiredWorkers;
    private AssignedWorkers[] assigned;  // list of ALL assigned workers
    private String projectSubtypeId;
    private UnassignedItem[] unassignedItems;  // workers being unassigned
    private String shiftId;

    public SaveCurrentStatusInput() {
        super();
    }

    public String[] getPersonId() {
        if (personId == null)
            personId = new String[0];
        return personId;
    }

    public void setPersonId(String[] personId) {
        this.personId = personId;
    }

    public int getClientPriority() {
        return clientPriority;
    }

    public void setClientPriority(int clientPriority) {
        this.clientPriority = clientPriority;
    }

    public int getCompanyPriority() {
        return companyPriority;
    }

    public void setCompanyPriority(int companyPriority) {
        this.companyPriority = companyPriority;
    }

    public int getOrgGroupPriority() {
        return orgGroupPriority;
    }

    public void setOrgGroupPriority(int orgGroupPriority) {
        this.orgGroupPriority = orgGroupPriority;
    }

    /**
     * @return Returns the routeStopId.
     */
    public String getRouteStopId() {
        return routeStopId;
    }

    /**
     * @param routeStopId The routeStopId to set.
     */
    public void setRouteStopId(final String routeStopId) {
        this.routeStopId = routeStopId;
    }

    void setData(final BProject bp) throws ArahantDeleteException {

        if (shiftId == null ||  shiftId.isEmpty())
            throw new ArahantException("shiftId is empty");

        BProjectShift bps = new BProjectShift(shiftId);

        if (billable != null && !"".equals(billable)) {
            bp.setBillable(billable.charAt(0));
            bp.setBillingRate((float) billingRate);
            bp.setPrimaryParentId(primaryParentId);
            bp.setEstimateHours(estimatedHours);
        }

        List<Person> persons = bp.getAssignedPersons2(shiftId);

        List<Person> remove = new ArrayList<>(persons);

        /*
              One place that uses this service passes in personIds.
              The other user of this service passes in assigned.
              Both are never used in the same call.
         */
        String [] personIds = getPersonId();
        if (personId != null  &&  personIds.length > 0) {
            List<String> addPeople = new ArrayList<>();

            for (String p : personIds) {
                boolean found = false;

                for (Person person : persons)
                    if (person.getPersonId().equals(p)) {
                        found = true;
                        remove.remove(person);
                        break;
                    }
                if (!found)
                    addPeople.add(p);
                //bp.assignPerson(p, 10000);
            }
            bps.assignPeople(addPeople, 10000);
        }

        /* This is the other case.  */
        AssignedWorkers[] awa = getAssigned();
        if (awa != null && awa.length > 0) {
            List<String> addPeople = new ArrayList<>();

            for (AssignedWorkers aw : awa) {
                boolean found = false;
                for (Person person : persons) {
                    if (person.getPersonId().equals(aw.getPersonId())) {
                        found = true;
                        remove.remove(person);
                        break;
                    }
                }
                if (!found) {
                    bps.assignPerson(aw.getPersonId(), 10000, false, aw.getStartDate());
                    addPeople.add(aw.getPersonId());
                }
            }
            bp.sendToAssignees(addPeople);
        }

        bps.removeAssignments(remove);

        if (projectStatusId != null && !projectStatusId.isEmpty())
            bp.setProjectStatusId(projectStatusId);
        bp.setProjectCategoryId(projectCategoryId);
        bp.setProjectTypeId(projectTypeId);
        bp.setProjectId(projectId);
        if (routeStopId != null && !routeStopId.isEmpty())
            bp.setRouteStopId(routeStopId);
        if (projectSubtypeId != null && !projectSubtypeId.isEmpty())
            bp.setProjectSubtypeId(projectSubtypeId);

        bp.setCompanyPriority(companyPriority);
        bp.setOrgGroupPriority(orgGroupPriority);
        bp.setClientPriority(clientPriority);
        bps.setRequiredWorkers(requiredWorkers);
    }

    /**
     * @return Returns the projectCategoryId.
     */
    public String getProjectCategoryId() {
        return projectCategoryId;
    }


    /**
     * @param projectCategoryId The projectCategoryId to set.
     */
    public void setProjectCategoryId(final String projectCategoryId) {
        this.projectCategoryId = projectCategoryId;
    }


    /**
     * @return Returns the projectId.
     */
    public String getProjectId() {
        return projectId;
    }


    /**
     * @param projectId The projectId to set.
     */
    public void setProjectId(final String projectId) {
        this.projectId = projectId;
    }


    /**
     * @return Returns the projectStatusId.
     */
    public String getProjectStatusId() {
        return projectStatusId;
    }


    /**
     * @param projectStatusId The projectStatusId to set.
     */
    public void setProjectStatusId(final String projectStatusId) {
        this.projectStatusId = projectStatusId;
    }


    /**
     * @return Returns the projectTypeId.
     */
    public String getProjectTypeId() {
        return projectTypeId;
    }


    /**
     * @param projectTypeId The projectTypeId to set.
     */
    public void setProjectTypeId(final String projectTypeId) {
        this.projectTypeId = projectTypeId;
    }

    public String getBillable() {
        return billable;
    }

    public void setBillable(String billable) {
        this.billable = billable;
    }

    public double getBillingRate() {
        return billingRate;
    }

    public void setBillingRate(double billingRate) {
        this.billingRate = billingRate;
    }

    public String getPrimaryParentId() {
        return primaryParentId;
    }

    public void setPrimaryParentId(String primaryParentId) {
        this.primaryParentId = primaryParentId;
    }

    public double getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(double estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public UnassignedItem[] getUnassignedItems() {
        return unassignedItems;
    }

    public void setUnassignedItems(UnassignedItem[] unassignedItems) {
        this.unassignedItems = unassignedItems;
    }

    public short getRequiredWorkers() {
        return requiredWorkers;
    }

    public void setRequiredWorkers(short requiredWorkers) {
        this.requiredWorkers = requiredWorkers;
    }

    public AssignedWorkers[] getAssigned() {
        return assigned;
    }

    public void setAssigned(AssignedWorkers[] assigned) {
        this.assigned = assigned;
    }

    public String getProjectSubtypeId() {
        return projectSubtypeId;
    }

    public void setProjectSubtypeId(String projectSubtypeId) {
        this.projectSubtypeId = projectSubtypeId;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }
}

	
