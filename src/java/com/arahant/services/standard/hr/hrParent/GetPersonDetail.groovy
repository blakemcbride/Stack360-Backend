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

package com.arahant.services.standard.hr.hrParent

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONObject
import org.kissweb.DateUtils
import org.kissweb.database.Connection

/**
 * Author: Blake McBride
 * Date: 12/19/20
 */
class GetPersonDetail {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        Connection db = KissConnection.get()
        String personId = injson.getString("personId")
        int today = DateUtils.today()
        outjson.put "info", db.fetchAllJSON("""
                with proj_now as (
                  select p.project_id, p.description, p.estimated_first_date, p.estimated_last_date
                  from project p
                  join project_status ps
                    on p.project_status_id = ps.project_status_id
                  where (p.estimated_last_date >= ? or p.estimated_last_date = 0)
                      --  and (p.estimated_first_date <= ? or  p.estimated_first_date = 0)
                        and ps.active = 'Y'),
                  
                mobile_phone as (
                  select person_join, phone_number
                  from phone
                  where phone_type = 3 and record_type = 'R'),
                  
                home_address as (
                  select person_join, city, state
                  from address
                  where address_type = 2 and record_type = 'R')
                
                select p.lname, p.fname, p.mname, pos.position_name, ph.phone_number, ad.city, ad.state,
                    proj.description, proj.estimated_first_date, proj.estimated_last_date,
                    es.name status_name, ps.shift_start
                from person p
                
                join current_employee_wage wh
                  on p.person_id = wh.employee_id
                  
                join hr_position pos
                  on wh.position_id = pos.position_id
                  
                join current_employee_status esh
                  on p.person_id = esh.employee_id
                
                join hr_employee_status es
                  on esh.status_id = es.status_id
                
                left join mobile_phone ph
                  on p.person_id = ph.person_join
                
                left join home_address ad
                  on p.person_id = ad.person_join
                
                left join project_employee_join pej
                  on p.person_id = pej.person_id
                  
                left join project_shift ps
                  on pej.project_shift_id = ps.project_shift_id
                  
                left join proj_now proj
                  on ps.project_id = proj.project_id
                
                where p.person_id = ?
                
                order by proj.estimated_first_date
                                            """, today, today, personId)

        Integer assignedFrom = injson.getInt('assigned-from')
        Integer assignedTo = injson.getInt('assigned-from')
        if (assignedFrom == null || assignedTo == null || assignedFrom == 0 || assignedTo == 0)
            assignedFrom = assignedTo = today
        outjson.put "unavailableDates", db.fetchAllJSON("""select start_date, end_date 
                     from employee_not_available 
                     where employee_id = ?
                           and (start_date <= ? and ? <= end_date
                                or start_date <= ? and ? <= end_date
                                or ? < start_date and ? > end_date)""",
                personId, assignedFrom, assignedFrom, assignedTo, assignedTo, assignedFrom, assignedTo)

    }

}
