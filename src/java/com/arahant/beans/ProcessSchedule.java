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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arahant.beans;

import java.io.Serializable;
import javax.persistence.*;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
@Entity
@Table(name = "process_schedule")
public class ProcessSchedule extends ArahantBean implements Serializable {

    public static final String NAME = "javaClass";
    private String processScheduleId;
    private String javaClass;
    private int startDate;
    private int startTime;
    private char performMissingRuns;// Yes or No
    private String runMinutes;// See CRON documentation
    private String runHours; // See CRON documentation
    private String runDaysOfMonth; // See CRON documentation
    private String runMonths; // See CRON documentation
    private String runDaysOfWeek; //See CRON documentation
    private Set<ProcessHistory> histories = new HashSet<ProcessHistory>();

    @OneToMany(mappedBy = ProcessHistory.SCHEDULE, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<ProcessHistory> getHistories() {
        return histories;
    }

    public void setHistories(Set<ProcessHistory> histories) {
        this.histories = histories;
    }

    @Column(name = "java_class")
    public String getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(String javaClass) {
        this.javaClass = javaClass;
    }

    @Column(name = "perform_missing_runs")
    public char getPerformMissingRuns() {
        return performMissingRuns;
    }

    public void setPerformMissingRuns(char performMissingRuns) {
        this.performMissingRuns = performMissingRuns;
    }

    @Id
    @Column(name = "process_schedule_id")
    public String getProcessScheduleId() {
        return processScheduleId;
    }

    public void setProcessScheduleId(String processScheduleId) {
        this.processScheduleId = processScheduleId;
    }

    @Column(name = "run_days_of_month")
    public String getRunDaysOfMonth() {
        return runDaysOfMonth;
    }

    public void setRunDaysOfMonth(String runDaysOfMonth) {
        this.runDaysOfMonth = runDaysOfMonth;
    }

    @Column(name = "run_days_of_week")
    public String getRunDaysOfWeek() {
        return runDaysOfWeek;
    }

    public void setRunDaysOfWeek(String runDaysOfWeek) {
        this.runDaysOfWeek = runDaysOfWeek;
    }

    @Column(name = "run_hours")
    public String getRunHours() {
        return runHours;
    }

    public void setRunHours(String runHours) {
        this.runHours = runHours;
    }

    @Column(name = "run_minutes")
    public String getRunMinutes() {
        return runMinutes;
    }

    public void setRunMinutes(String runMinutes) {
        this.runMinutes = runMinutes;
    }

    @Column(name = "run_months")
    public String getRunMonths() {
        return runMonths;
    }

    public void setRunMonths(String runMonths) {
        this.runMonths = runMonths;
    }

    @Column(name = "start_date")
    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    @Column(name = "start_time")
    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    @Override
    public String tableName() {
        return "process_schedule";
    }

    @Override
    public String keyColumn() {
        return "process_schedule_id";
    }

    @Override
    public String generateId() throws ArahantException {
        return processScheduleId = IDGenerator.generate(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProcessSchedule other = (ProcessSchedule) obj;
        if (this.processScheduleId != other.getProcessScheduleId() && (this.processScheduleId == null || !this.processScheduleId.equals(other.getProcessScheduleId()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.processScheduleId != null ? this.processScheduleId.hashCode() : 0);
        return hash;
    }
}
