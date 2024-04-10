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

package com.arahant.services.standard.project.inventoryEmail

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.kissweb.database.Connection
import org.kissweb.database.Record
import org.json.JSONArray
import org.json.JSONObject

/**
 * Author: Blake McBride
 * Date: 8/30/18
 */
class GetAllEmail {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String project_id = injson.getString("project_id")
        Connection con = KissConnection.get()
        List<Record> recs = con.fetchAll("select pie.pie_id, p.fname, p.mname, p.lname, p.personal_email, e.person_id from project_inventory_email pie " +
                "inner join person p" +
                "  on pie.person_id = p.person_id " +
                "left join employee e" +
                "  on pie.person_id = e.person_id " +
                "where project_id=? " +
                "order by p.lname, p.fname, p.mname", project_id)
        JSONArray ary = new JSONArray()
        for (Record rec in recs) {
            JSONObject obj = new JSONObject()
            String fname = rec.getString "fname"
            String mname = rec.getString "mname"
            String lname = rec.getString "lname"
            String name
            if (lname != null  &&  !lname.isEmpty())
                name = lname
            else
                name = ""
            if (fname != null &&  !fname.isEmpty()) {
                if (!name.isEmpty())
                    name += ", "
                name += fname
            }
            if (mname != null  &&  !mname.isEmpty())
                name += " " + mname

            if (rec.getString("person_id") == null)  // not an employee - must be client contact
                name = "* " + name

            obj.put("pie_id", rec.getString("pie_id"))
            obj.put("name", name)
            obj.put("email", rec.getString("personal_email"))
            ary.put(obj)
        }
        outjson.put("list", ary)
    }

}
