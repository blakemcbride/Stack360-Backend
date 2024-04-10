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

package com.arahant.services.standard.misc.reminder

import com.arahant.utils.StandardProperty
import com.arahant.business.BProperty
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import org.json.JSONObject
import org.kissweb.database.Connection

/**
 * Author: Blake McBride
 * Date: 7/18/21
 */
class GetReminders {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        Connection db = KissConnection.get()
        int highCap = BProperty.getInt(StandardProperty.SEARCH_MAX)
        ArrayList args = new ArrayList()
        String select = """
                        select r.*, 
                               rper.lname for_lname, rper.fname for_fname, rper.mname for_mname,
                               aper.lname about_lname, aper.fname about_fname, aper.mname about_mname,
                               aprj.description
                        from reminder r
                        left join person rper
                          on r.person_id = rper.person_id
                        left join person aper
                          on r.about_person = aper.person_id
                        left join project aprj
                          on r.about_project = aprj.project_id
                        where r.status = 'A'
                        """

        String remindType = injson.getString('remindType')
        if (remindType == 'M') {
            select += ' and r.person_id = ? '
            args.add(hsu.getCurrentPerson().getPersonId())
        } else if (remindType == 'E') {
            select += ' and r.person_id = ? '
            args.add(injson.getString('remindPerson'))
        }

        String aboutPerson = injson.getString('aboutPerson')
        if (aboutPerson != null && !aboutPerson.isEmpty()) {
            select += ' and r.about_person = ? '
            args.add(aboutPerson)
        }

        String aboutProject = injson.getString('aboutPerson')
        if (aboutProject != null && !aboutProject.isEmpty()) {
            select += ' and r.about_project = ? '
            args.add(aboutProject)
        }

        select += " order by r.reminder_date"
        outjson.put 'reminders', db.fetchAllJSON(highCap, select, args)
    }

}

