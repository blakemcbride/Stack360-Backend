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

package com.arahant.business;

import com.arahant.beans.*;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.*;
import com.arahant.utils.*;
import org.kissweb.TimeUtils;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.sql.SQLException;
import java.util.*;

/**
 * Author: Blake McBride
 * Date: 3/18/22
 */
public class BProjectShift implements IDBFunctions {
    private static final ArahantLogger logger = new ArahantLogger(BProjectShift.class);

    private ProjectShift projectShift;

    public BProjectShift() {

    }

    public BProjectShift(final String key) throws ArahantException {
        internalLoad(key);
    }

    private void internalLoad(final String key) throws ArahantException {
        projectShift = ArahantSession.getHSU().get(ProjectShift.class, key);
    }

    public String create() throws ArahantException {
        projectShift = new ProjectShift();
        projectShift.generateId();
        return projectShift.getProjectShiftId();
    }

    @Override
    public void load(String key) throws ArahantException {
        projectShift = ArahantSession.getHSU().get(ProjectShift.class, key);
        if (projectShift == null)
            throw new ArahantWarning("Project shift not found.");
    }

    public String getProjectShiftId() {
        return projectShift.getProjectShiftId();
    }

    public BProjectShift setProjectShiftId(String id) {
        projectShift.setProjectShiftId(id);
        return this;
    }

    public ProjectShift getProjectShift() {
        return projectShift;
    }

    public BProjectShift(final ProjectShift ps) {
        projectShift = ps;
    }

    public BProject getProject() {
        return new BProject(projectShift.getProject());
    }

    public BProjectShift setProject(BProject bp) {
        projectShift.setProject(bp.getBean());
        return this;
    }

    public short getRequiredWorkers() {
        return projectShift.getRequiredWorkers();
    }

    public BProjectShift setRequiredWorkers(short rw) {
        projectShift.setRequiredWorkers(rw);
        return this;
    }

    public String getShiftStart() {
        return projectShift.getShiftStart();
    }

    public BProjectShift setShiftStart(String ss) {
        projectShift.setShiftStart(ss);
        return this;
    }

    public String getDescription() {
        return projectShift.getDescription();
    }

    public BProjectShift setDescription(String desc) {
        projectShift.setDescription(desc);
        return this;
    }

    public int getDefaultDate() {
        return projectShift.getDefaultDate();
    }

    public BProjectShift setDefaultDate(int dt) {
        projectShift.setDefaultDate(dt);
        return this;
    }

    @Override
    public void update() throws ArahantException {
        ArahantSession.getHSU().saveOrUpdate(projectShift);
    }

    @Override
    public void insert() throws ArahantException {
        ArahantSession.getHSU().insert(projectShift);
    }

    @Override
    public void delete() throws ArahantDeleteException, ArahantSecurityException, ArahantJessException {
        try {
            ArahantSession.getHSU().delete(projectShift);
        } catch (final RuntimeException e) {
            throw new ArahantDeleteException();
        }
    }

    /**
     * Be warned, although this method assigns a person to a project, it does not
     * save the records.  You must call the update() method for that.
     *
     * @param personId
     * @param priority
     * @param notify
     */
    public void assignPerson(String personId, int priority, boolean notify) {
        assignPerson(personId, priority, notify, 0);
    }

    /**
     * Be warned, although this method assigns a person to a project, it does not
     * save the records.  You must call the update() method for that.
     *
     * @param personId
     * @param priority
     * @param notify
     * @param startDate
     */
    public void assignPerson(String personId, int priority, boolean notify, int startDate) {
        int nowTime = DateUtils.nowTime();
        int nowDate = DateUtils.now();
        final Project project = projectShift.getProject();
        final HibernateSessionUtil hsu = ArahantSession.getHSU();

        if (project.getBillable() == 'U')
            throw new ArahantWarning("You may not assign anyone while the billing state is Unknown.");

        ProjectEmployeeJoin pej = new ProjectEmployeeJoin();
        pej.generateId();
        pej.setPersonPriority((short) priority);
        pej.setProjectShift(projectShift);
        pej.setPerson(ArahantSession.getHSU().get(Person.class, personId));
        pej.setDateAssigned(nowDate);
        pej.setTimeAssigned(nowTime);
        pej.setStartDate(startDate);
// XXYY        updates.add(pej);
        hsu.insert(pej);  //  XXYY
        BProjectShift.createAssignmentHistory(personId, projectShift.getProjectShiftId(), "A");

        try {
            if (BProperty.getBoolean("DRCMessaging"))
                if (!isEmpty(hsu.get(Person.class, personId).getPersonalEmail()) && project.getProjectStatus() != null)
                    if (project.getProjectStatus().getActive() == 'Y') {
                        String email = hsu.get(Person.class, personId).getPersonalEmail();
                        Mail.send(email, email, "Project Assigned", "Project Name: " + project.getProjectName() + "\nProject Description: " + project.getDetailDesc());
                    }
            if (notify)
                sendToAssignees("Project " + project.getProjectName() + " Assignments",
                        "The following people were assigned to " + project.getProjectName().trim() + "\n" + new BPerson(personId).getNameLFM());
        } catch (Exception e) {
            logger.debug(e);
        }
    }

    /**
     * Be warned, although this method assigns a persons to a project, it does not
     * save the records.  You must call the update() method for that.
     *
     * @param addPeople
     * @param priority
     */
    public void assignPeople(List<String> addPeople, int priority) {
        if (addPeople.isEmpty())
            return;

        for (String pid : addPeople)
            assignPerson(pid, priority, false);
        sendToAssignees(addPeople);
    }

    static private boolean isEmpty(final String str) {
        return str == null || str.trim().equals("");
    }

    public void sendToAssignees(List<String> addPeople) {
        StringBuilder names = new StringBuilder();
        HibernateSessionUtil hsu = ArahantSession.getHSU();
        Project project = projectShift.getProject();

        for (String pid : addPeople) {
            Person p = hsu.get(Person.class, pid);
            names.append(p.getNameLFM()).append("\n");
        }
        sendToAssignees("Project " + project.getProjectName().trim() + " Assignments",
                "The following people were assigned to " + project.getProjectName().trim() + "\n" + names);
    }

    /**
     * Send an email to all people assigned to the project.
     *
     * @param subject
     * @param message
     */
    private void sendToAssignees(String subject, String message) {
        if (!BProperty.getBoolean("CommentMessages"))
            return;

        HibernateSessionUtil hsu = ArahantSession.getHSU();
        Project project = projectShift.getProject();

        subject = "[Project] " + subject;

        List<Person> l = hsu.createCriteria(Person.class)
                .joinTo(Person.PROJECT_PERSON_JOIN)
                .joinTo(ProjectEmployeeJoin.PROJECTSHIFT)
                .eq(ProjectShift.PROJECTSHIFTID, projectShift.getProjectShiftId())
                .list();

        Set<Person> ps = new HashSet<>(l);
        /* XXYY
        //add any new ones from updates
        for (ArahantBean b : updates)
            if (b instanceof ProjectEmployeeJoin)
                ps.add(((ProjectEmployeeJoin) b).getPerson());
         */

        //add the managing employee
        if (project.getEmployee() != null)
            ps.add(project.getEmployee());

        for (Person p : ps) {
            if (p.getPersonId().equals(hsu.getCurrentPerson().getPersonId()))
                continue;
            /*
             * if(!hsu.createCriteria(Message.class) .eq(Message.SUBJECT,
             * subject) .ge(Message.CREATEDDATE, DateUtils.now())
             * .eq(Message.PERSONBYTOPERSONID, p) .exists())
             */
            BMessage.send(hsu.getCurrentPerson(), p, subject, message);
        }

    }

    /**
     * Create a project employee assignment history record for an assignment or de-assignment employee.
     *
     * @param workerId
     * @param shiftId
     * @param type "A" or "D"
     */
    public static void createAssignmentHistory(String workerId, String shiftId, String type) {
        HibernateSessionUtil hsu = ArahantSession.getHSU();
        Connection db = hsu.getKissConnection();
        Record rec = db.newRecord("project_employee_history");
        IDGenerator.generate(rec, "project_employee_history_id");
        rec.set("person_id", workerId);
        rec.set("project_shift_id", shiftId);
        rec.set("change_person_id", hsu.getCurrentPerson().getPersonId());
        rec.set("change_date", DateUtils.today());
        rec.set("change_time", TimeUtils.now());
        rec.set("change_type", type);
        try {
            rec.addRecord();
        } catch (SQLException throwables) {
            throw new ArahantException(throwables);
        }
    }

    public void removeAssignment(String personId) {
        final HibernateSessionUtil hsu = ArahantSession.getHSU();
        final Connection db = hsu.getKissConnection();
        try {
            db.execute("delete from project_employee_join where person_id=? and project_shift_id=?", personId, projectShift.getProjectShiftId());
        } catch (SQLException e) {
            throw new ArahantException(e);
        }
        BProjectShift.createAssignmentHistory(personId, projectShift.getProjectShiftId(), "D");
    }

    public void removeAssignments2(List<Person> remove) {
        if (remove.isEmpty())
            return;
        Project project = projectShift.getProject();

        StringBuilder names = new StringBuilder();
        for (Person bap : remove)
            names.append(bap.getNameLFM()).append("\n");
        sendToAssignees("Project " + project.getProjectName().trim() + " (" + projectShift.getShiftStart() + ") Unassignments",
                "The following people were unassigned from " +
                        project.getProjectName().trim() + " (" + projectShift.getShiftStart() + ")\n" + names);

        for (Person bap : remove)
            removeAssignment(bap.getPersonId());
    }

    public void removeAssignments(List<Person> remove) {
        for (Person p : remove)
            removeAssignment(p.getPersonId());
    }

    public List<ProjectForm> getProjectForms() {
        final HibernateSessionUtil hsu = ArahantSession.getHSU();
        return hsu.createCriteria(ProjectForm.class).eq(ProjectForm.PROJECTSHIFT, projectShift).orderBy(ProjectForm.FORM_DATE).list();
    }

    static BProjectShift[] makeArray(HibernateScrollUtil<ProjectShift> scr) {
        List<ProjectShift> l = new ArrayList<>();
        while (scr.next())
            l.add(scr.get());
        return BProjectShift.makeArray(l);
    }

    static public BProjectShift[] makeArray(HibernateScrollUtil<ProjectShift> scr, int max) {
        List<ProjectShift> l = new ArrayList<>();
        while (scr.next() && max-- > 0)
            l.add(scr.get());
        return BProjectShift.makeArray(l);
    }

    static public BProjectShift[] makeArray(final Collection<ProjectShift> l) {
        final BProjectShift[] ret = new BProjectShift[l.size()];
        int i = 0;
        for (ProjectShift p : l)
            ret[i++] = new BProjectShift(p);
        return ret;
    }

    public String getProjectId() {
        return projectShift.getProjectId();
    }
}

