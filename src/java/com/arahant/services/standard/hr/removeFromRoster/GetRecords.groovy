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

package com.arahant.services.standard.hr.removeFromRoster

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONObject
import org.kissweb.database.*

/**
 * Author: Blake McBride
 * Date: 8/6/23
 */
class GetRecords {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        final Connection db = KissConnection.get()
        outjson.put "records", db.fetchAllJSON("""
                    select rfr.rfr_id, rfr.when_removed, rfr.description, rfr.comments, p.description project_description, ps.shift_start,
                           w.fname w_fname, w.mname w_mname, w.lname w_lname,
                           s.fname s_fname, s.mname s_mname, s.lname s_lname
                    from removed_from_roster rfr
                    left join person w
                      on rfr.person_id = w.person_id
                    left join person s
                      on rfr.supervisor_id = s.person_id
                    join project_shift ps
                      on rfr.shift_id = ps.project_shift_id
                    join project p
                      on ps.project_id = p.project_id
                    order by rfr.when_removed desc
                """, injson.getString("employeeId"))
    }
}
