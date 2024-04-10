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

/**
 * Author: Blake McBride
 * Date: 2/13/21
 */
public class SearchProjectSubtypesReturnItem {

    private String projectSubtypeId;
    private String code;

    public SearchProjectSubtypesReturnItem() {}

    SearchProjectSubtypesReturnItem(String id, String code) {
        this.projectSubtypeId = id;
        this.code = code;
    }

    public String getProjectSubtypeId() {
        return projectSubtypeId;
    }

    public void setProjectSubtypeId(String projectSubtypeId) {
        this.projectSubtypeId = projectSubtypeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
