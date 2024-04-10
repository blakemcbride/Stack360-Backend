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

package timedTasks

import com.arahant.utils.HibernateSessionUtil
import org.kissweb.DateTime
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 12/28/21
 */
class EveryMinute {

    static void start(Object obj) {
        HibernateSessionUtil hsu = (HibernateSessionUtil) obj
        println("EveryMinute is running at " + DateTime.currentDateTimeFormatted())
        Connection db = hsu.getKissConnection()
        List<Record> recs = db.fetchAll("select fname from person where fname like 'B%'")
        recs.forEach(rec -> {
            println rec.getString("fname")
        })
    }
}
