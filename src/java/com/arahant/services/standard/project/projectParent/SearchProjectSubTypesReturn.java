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

import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.KissConnection;
import org.kissweb.DateUtils;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.util.List;

/**
 * Author: Blake McBride
 * Date: 10/25/23
 */
public class SearchProjectSubTypesReturn extends TransmitReturnBase {

    private SearchProjectSubTypesReturnItem[] projectSubTypes;

    public SearchProjectSubTypesReturnItem[] getProjectSubTypes() {
        return projectSubTypes;
    }

    public void setProjectSubTypes(SearchProjectSubTypesReturnItem[] projectSubTypes) {
        this.projectSubTypes = projectSubTypes;
    }

    void setProjectSubTypes() throws Exception {
        final Connection db = KissConnection.get();
        final List<Record> recs = db.fetchAll("select project_subtype_id, code, description " +
                "from project_subtype " +
                "where last_active_date = 0 or last_active_date >= ? " +
                "order by code", DateUtils.today());
        final int n = recs.size();
        projectSubTypes = new SearchProjectSubTypesReturnItem[n];
        for (int loop = 0; loop < n; loop++)
            projectSubTypes[loop] = new SearchProjectSubTypesReturnItem(recs.get(loop));
    }
}
