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

package com.arahant.services.standard.hr.sendemail

import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import com.arahant.utils.SendEmailGeneric
import com.arahant.utils.SendEmailProvider
import org.kissweb.DateUtils
import org.kissweb.database.Connection
import org.kissweb.database.Record
import org.json.JSONObject

/**
 * Author: Blake McBride
 * Date: 4/22/19
 */
class SendEmail {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        int begdate = injson.getInt("begdate")
        int enddate = injson.getInt("enddate")
        String msg = injson.getString("msg")
        String subject = injson.getString("subject")
        int today = DateUtils.today()

        SendEmailGeneric em = null

        try {
            em = SendEmailProvider.newEmail()
            em.setHTMLMessage(msg)
            Connection con = KissConnection.get()
            List<Record> recs = con.fetchAll("""
 
                with empl_status_history as (
                select t2.* from hr_empl_status_history t2
                inner join
                  (select distinct t0.employee_id, max(t0.effective_date) effective_date
                  from hr_empl_status_history t0
                  where t0.effective_date <= ?
                  group by t0.employee_id) t1
                on t2.employee_id = t1.employee_id and t2.effective_date = t1.effective_date)
 
                select p.fname, p.lname, p.personal_email
                from person p
                join empl_status_history esh
                  on p.person_id = esh.employee_id
                join hr_employee_status es
                  on esh.status_id = es.status_id
                where person_id in
                        (
                           select distinct person_id 
                           from timesheet
                           where end_date >= ? AND end_date <= ?
                                 and total_hours > .001
                        )
                    and es.active = 'Y'
                """, today, begdate, enddate)
            for (Record rec : recs) {
                String email = rec.getString("personal_email")
                if (email != null && email.trim().length() >= 3 && email.contains("@")) {
            //        println "Sending email to " + email + " " +  rec.getString("fname") + " " + rec.getString("lname")
                    em.sendEmail(email, rec.getString("fname") + " " + rec.getString("lname"), subject)
                }
            }
        } finally {
            if (em != null)
                em.close()
        }
    }
}
