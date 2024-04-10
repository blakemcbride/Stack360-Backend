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

package com.arahant.services.standard.project.projectCurrentStatus;

import java.util.Objects;

public class UnassignedItem {

    private String personId;
    private int date;
    private int time;
    private String reason;
    private String comment;
    private String projectEmployeeJoin;

    public UnassignedItem() {
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        UnassignedItem ui = (UnassignedItem) obj;
        return Objects.equals(personId, ui.personId) &&
                date == ui.date && time == ui.time &&
                Objects.equals(reason, ui.reason) &&
                Objects.equals(comment, ui.comment);
    }

    public int hashCode() {
        int h = 0;
        if (personId != null)
            h = personId.hashCode();
        h += date;
        h += time;
        if (reason != null)
            h += reason.hashCode();
        if (comment != null)
            h += comment.hashCode();
        return h;
    }

    public String getProjectEmployeeJoin() {
        return projectEmployeeJoin;
    }

    public void setProjectEmployeeJoin(String projectEmployeeJoin) {
        this.projectEmployeeJoin = projectEmployeeJoin;
    }
}
