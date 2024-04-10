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

import com.arahant.exceptions.ArahantException
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.IDGenerator
import com.arahant.utils.KissConnection
import org.kissweb.database.Connection
import org.kissweb.database.Record
import org.json.JSONObject

/**
 * Author: Blake McBride
 * Date: 8/30/18
 */
class AddEmail {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        String project_id = injson.getString("project_id")
        String person_id = injson.getString("person_id")
        Connection con = KissConnection.get()
        if (con.fetchOne("select pie_id from project_inventory_email where project_id=? and person_id=?", project_id, person_id) != null)
            throw new ArahantException("Person is already getting the email")
        Record rec = con.newRecord("project_inventory_email")
        rec.set("pie_id", IDGenerator.generate("project_inventory_email", "pie_id"))
        rec.set("project_id", project_id)
        rec.set("person_id", person_id)
        rec.addRecord()
    }

}
