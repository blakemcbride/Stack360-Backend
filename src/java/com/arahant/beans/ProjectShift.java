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

package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Blake McBride
 * Date: 3/18/22
 */
@Entity
@Table(name = ProjectShift.TABLE_NAME)
public class ProjectShift extends ArahantBean implements java.io.Serializable, ArahantSaveNotify {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "project_shift";

    private String projectShiftId;
    public static final String PROJECTSHIFTID = "projectShiftId";

    private Project project;
    private String projectId;
    public static final String PROJECT = "project";
    public static final String PROJECT_ID = "projectId";
    public static final String TIMESHEET = "timesheet";

    private Set<Timesheet> timesheets = new HashSet<>(0);
    public static final String TIMESHEETS = "timesheets";

    private short requiredWorkers = 0;
    private String shiftStart;
    private String description;
    private int defaultDate;

    @Id
    @Column(name = "project_shift_id")
    public String getProjectShiftId() {
        return this.projectShiftId;
    }

    public void setProjectShiftId(final String projectShiftId) {
        this.projectShiftId = projectShiftId;
    }

    @Column(name = "project_id", insertable = false, updatable = false)
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(final String projectId) {
        this.projectId = projectId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    public Project getProject() {
        return this.project;
    }

    public void setProject(final Project project) {
        this.project = project;
    }

    @Column(name = "required_workers")
    public short getRequiredWorkers() {
        return requiredWorkers;
    }

    public void setRequiredWorkers(short requiredWorkers) {
        this.requiredWorkers = requiredWorkers;
    }

    @Column(name = "shift_start")
    public String getShiftStart() {
        return shiftStart;
    }

    public void setShiftStart(String shiftStart) {
        this.shiftStart = shiftStart;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "default_date")
    public int getDefaultDate() {
        return defaultDate;
    }

    public void setDefaultDate(int defaultDate) {
        this.defaultDate = defaultDate;
    }

    @OneToMany(mappedBy = Timesheet.PROJECTSHIFT, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<Timesheet> getTimesheets() {
        return this.timesheets;
    }

    public void setTimesheets(final Set<Timesheet> timesheets) {
        this.timesheets = timesheets;
    }

    @Override
    public String keyColumn() {
        return "project_shift_id";
    }

    @Override
    public String tableName() {
        return TABLE_NAME;
    }

    @Override
    public String generateId() throws ArahantException {
        setProjectShiftId(IDGenerator.generate(this));
        return projectShiftId;
    }

    @Override
    public String notifyId() {
        return projectShiftId;
    }

    @Override
    public String notifyClassName() {
        return "ProjectShift";
    }

}
