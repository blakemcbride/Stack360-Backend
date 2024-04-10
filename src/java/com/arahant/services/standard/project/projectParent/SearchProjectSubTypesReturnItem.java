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

package com.arahant.services.standard.project.projectParent;

import org.kissweb.database.Record;

import java.sql.SQLException;

/**
 * Author: Blake McBride
 * Date: 10/25/23
 */
public class SearchProjectSubTypesReturnItem {

    private String projectSubTypeId;
    private String code;
    private String description;

    public SearchProjectSubTypesReturnItem(Record rec) throws SQLException {
        projectSubTypeId = rec.getString("project_subtype_id");
        code = rec.getString("code");
        description = rec.getString("description");

    }

    public String getProjectSubTypeId() {
        return projectSubTypeId;
    }

    public void setProjectSubTypeId(String projectSubTypeId) {
        this.projectSubTypeId = projectSubTypeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
