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

package com.arahant.services.standard.at.applicant

import com.arahant.servlets.REST
import com.arahant.utils.DateUtils
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONObject
import org.kissweb.database.Connection

/**
 * Author: Blake McBride
 * Date: 2/20/23
 */
class GetPositions {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        Connection db = hsu.getKissConnection()
        int today = DateUtils.today()
        outjson.put("positions", db.fetchAllJSON("""select position_id, position_name
                                                    from hr_position
                                                    where (first_active_date = 0 or first_active_date <= ?)
                                                          and (last_active_date = 0 or last_active_date >= ?)
                                                    order by seqno, position_name""", today, today))
        outjson.put("defaultPosition", db.fetchOneJSON("""select position_id
                                                    from hr_position
                                                    where (first_active_date = 0 or first_active_date <= ?)
                                                          and (last_active_date = 0 or last_active_date >= ?)
                                                          and applicant_default = 'Y'""", today, today))
    }
}
