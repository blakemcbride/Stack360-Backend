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
 * Created on Feb 11, 2007
*/

package com.arahant.services.standard.hr.hrParent;

import com.arahant.beans.Person;
import com.arahant.business.BEmployee;
import com.arahant.business.BHREmplDependent;
import com.arahant.business.BPerson;
import com.arahant.exceptions.ArahantException;
import org.kissweb.database.Command;
import org.kissweb.database.Record;

import java.sql.SQLException;
import java.util.List;


public class SearchPersonsReturnItem {

    private String personId;
    private String lname;
    private String fname;
    private String active;
    private String middleName;
    private String jobTitle;
    private String ssn;
    private String type;// as string (Active/Inactive)
    private String statusName;
    private boolean dirty = false;
    private String positionName;
    private String assignedProject;
    private String labels;
    private String i9p1;

    public SearchPersonsReturnItem() {
    }

    SearchPersonsReturnItem(final BPerson e) throws ArahantException {
        personId = e.getPersonId();
        lname = e.getLastName();
        fname = e.getFirstName();

        middleName = e.getMiddleName();
        jobTitle = e.getJobTitle();
        ssn = e.getSsn();

        if (e.isEmployee()) {
            BEmployee emp = new BEmployee(e);
            statusName = emp.getEmployeeStatusName();
            active = emp.isActive() < 1 ? "Y" : "N";
            type = "Emp";
            dirty = emp.getDirty();
        } else {
            statusName = BHREmplDependent.isActiveDependent(e.getPersonId()) ? "Active" : "Inactive";
            type = "Dep";
        }
    }

    SearchPersonsReturnItem(final Record rec, List<Record> ap, Command cmd) throws Exception {
        personId = rec.getString("person_id");
        lname = rec.getString("lname");
        fname = rec.getString("fname");

        middleName = rec.getString("mname");
        jobTitle = rec.getString("job_title");
        ssn = Person.decryptSsn(rec.getString("ssn"));
        positionName = rec.getString("position_name");

        statusName = rec.getString("status_name");
        active = rec.getString("active");
        type = "Emp";
        dirty = false;

        if (ap.isEmpty())
            assignedProject = "";
        else if (ap.size() > 1)
            assignedProject = "(" + ap.size() + " different projects)";
        else {
            Record r = ap.get(0);
            assignedProject = r.getString("description");
        }

        StringBuilder sb = new StringBuilder(30);
        List<Record> lbls = cmd.fetchAll("select el.name " +
                "from employee_label_association ela " +
                "join employee_label el " +
                "  on ela.employee_label_id = el.employee_label_id " +
                "where ela.employee_id = ? and ela.completed = 'N'" +
                "order by el.name", personId);
        for (Record lbl : lbls) {
            if (sb.length() > 0)
                sb.append(", ");
            sb.append(lbl.getString("name"));
        }
        labels = sb.toString();
        i9p1 = rec.getString("i9_part1").equals("Y") ? "Yes" : "";
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(final String middleName) {
        this.middleName = middleName;
    }

    public String getActive() {
        return active;
    }

    public void setActive(final String active) {
        this.active = active;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(final String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(final String lname) {
        this.lname = lname;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(final String personId) {
        this.personId = personId;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(final String ssn) {
        this.ssn = ssn;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getAssignedProject() {
        return assignedProject;
    }

    public void setAssignedProject(String assignedProject) {
        this.assignedProject = assignedProject;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getI9p1() {
        return i9p1;
    }

    public void setI9p1(String i9p1) {
        this.i9p1 = i9p1;
    }
}


	
