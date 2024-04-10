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

/*
 * All rights reserved.
 */

package com.arahant.services.standard.time.timeType

import com.arahant.exceptions.ArahantException
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONObject
import org.kissweb.database.Connection
import org.kissweb.database.Record
import com.arahant.utils.IDGenerator

/**
 *
 * @author blake
 */
class SaveTimeType {
    
    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        final String timeTypeId = injson.getString("time_type_id")
        final String description = injson.getString("description").trim()
        final int lastActiveDate = injson.getInt("last_active_date")
        final char defaultBillable = injson.getCharacter("default_billable")
        final char defaultType = injson.getCharacter("default_type")
        Connection db = hsu.getKissConnection()
        Record rec = db.fetchOne("select * from time_type where time_type_id = ?", timeTypeId)
        if (rec == null)
            throw new ArahantException("Time type not found")
        rec.set("description", description)
        rec.set("last_active_date", lastActiveDate)
        rec.set("default_billable", defaultBillable)
        rec.set("default_type", defaultType)
        rec.update()
        if (defaultType == (char)'Y')
            db.execute("update time_type set default_type = 'N' where time_type_id <> ?", timeTypeId)
    }
	
}

