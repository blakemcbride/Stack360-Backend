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

package com.arahant.services.standard.project.sendEmail


import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.kissweb.database.Connection
import org.kissweb.database.Record
import org.json.JSONArray
import org.json.JSONObject

/**
 * Author: Blake McBride
 * Date: 7/30/18
 */
class GetWorkers {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String project_id = injson.getString("project_id")
        String shift_id = injson.getString("shift_id")
        Connection con = KissConnection.get()
        List<Record> recs
        if (shift_id == null  ||  shift_id.isEmpty())
            recs = con.fetchAll("""select p.lname, p.fname, p.mname, p.personal_email 
                                                from project_employee_join j 
                                                join person p 
                                                  on j.person_id = p.person_id 
                                                join project_shift ps
                                                  on j.project_shift_id = ps.project_shift_id
                                                where ps.project_id = ? 
                                                order by p.lname, p.fname""", project_id)
        else
            recs = con.fetchAll("""select p.lname, p.fname, p.mname, p.personal_email 
                                                from project_employee_join j 
                                                join person p 
                                                  on j.person_id = p.person_id 
                                                join project_shift ps
                                                  on j.project_shift_id = ps.project_shift_id
                                                where ps.project_shift_id = ? 
                                                order by p.lname, p.fname""", shift_id)
        JSONArray ary = new JSONArray()
        for (Record rec : recs) {
            String email = rec.getString("personal_email")
            if (email != null  &&  email.trim().length() >= 3  &&  email.contains("@"))
                ary.put rec.getString("lname") + ", " + rec.getString("fname") + " " + rec.getString("mname")
            else
                ary.put "* " + rec.getString("lname") + ", " + rec.getString("fname") + " " + rec.getString("mname")
        }
        outjson.put("workers", ary)
    }

}
