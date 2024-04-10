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

import com.arahant.business.BProperty
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.KissConnection
import com.arahant.utils.SendEmailGeneric
import com.arahant.utils.SendEmailProvider
import org.kissweb.database.Connection
import org.kissweb.database.Record
import org.json.JSONObject

/**
 * User: Blake McBride
 * Date: 7/30/18
 */
class SendEmail {

    private static boolean testMode = BProperty.getBoolean("TestEnvironment", false)

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {

        testMode = BProperty.getBoolean("TestEnvironment", false)

        int nUploadFiles = service.getUploadFileCount()

        String project_id = injson.getString("project_id")
        String shift_id = injson.getString("shift_id")
        String title = injson.getString("title")
        String detail = injson.getString("detail")

        if (title == null  ||  title.isEmpty())
            throw new Exception("Missing email title")
        if (detail == null  ||  detail.isEmpty())
            throw new Exception("Missing email message")

        SendEmailGeneric em = null

        try {
            if (!testMode) {
                em = SendEmailProvider.newEmail()
                em.setHTMLMessage(detail)
                for (int i = 0; i < nUploadFiles; i++)
                    em.addAttachement(service.saveUploadFile(i), service.getUploadFileName(i))
            }
            Connection con = KissConnection.get()
            List<Record> recs
            if (shift_id == null || shift_id.isEmpty())
                recs = con.fetchAll("select p.lname, p.fname, p.mname, p.personal_email " +
                        "from project_employee_join j " +
                        "join person p " +
                        "  on j.person_id = p.person_id " +
                        "join project_shift ps " +
                        "  on j.project_shift_id = ps.project_shift_id " +
                        "where ps.project_id = ? " +
                        "order by p.lname, p.fname", project_id)
            else
                recs = con.fetchAll("select p.lname, p.fname, p.mname, p.personal_email " +
                        "from project_employee_join j " +
                        "join person p " +
                        "  on j.person_id = p.person_id " +
                        "where j.project_shift_id = ? " +
                        "order by p.lname, p.fname", shift_id)
            int i = 0
            for (Record rec : recs) {
                String email = rec.getString("personal_email")
                if (email != null && email.trim().length() >= 3 && email.contains("@"))
                    if (testMode)
                        rec = rec
                    else
                        em.sendEmail(email, rec.getString("fname") + " " + rec.getString("lname"), title)
            }
        } finally {
            if (em != null)
                em.close()
        }
    }

}
