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

package com.arahant.services.standard.project.task

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONObject
import org.kissweb.database.Connection

/**
 * Author: Blake McBride
 * Date: 12/1/23
 */
class ListPictures {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        final Connection db = KissConnection.get()
        final String projectTaskDetailId = injson.getString("projectTaskDetailId")
        outjson.put("pictures", db.fetchAllJSON("""select ptp.*, p.fname, p.mname, p.lname
                                                   from project_task_picture ptp
                                                   left join person p
                                                     on ptp.who_uploaded = p.person_id
                                                   where ptp.project_task_detail_id = ?""", projectTaskDetailId))
    }

}
