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

package com.arahant.services.standard.main

import com.arahant.business.BProperty
import com.arahant.exceptions.ArahantException
import com.arahant.exceptions.ArahantWarning
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.IDGenerator
import com.arahant.utils.KissConnection
import com.arahant.utils.SendEmailGeneric
import com.arahant.utils.SendEmailProvider
import org.json.JSONObject
import org.kissweb.DateUtils
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 10/31/22
 */
class NewApplicant {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        final String fname = injson.getString("fname")
        final String mname = injson.getString("mname")
        final String lname = injson.getString("lname")
        final String email = injson.getString("email")
        final String password = injson.getString("password")
        Connection db = KissConnection.get()
        Record rec = db.fetchOne("select person_id from prophet_login where lower(user_login) = lower(?)", email)
        if (rec != null)
            throw new ArahantWarning("That email address is already registered with the system.  You may want to reset your password.")
        rec = db.newRecord("person")
        IDGenerator.generate(rec, "person_id")
        final String person_id = rec.getString("person_id")
        rec.setDateTime("record_change_date", new Date())
        rec.set("record_change_type", "N")
        rec.set("record_person_id", rec.getString("person_id"))
        rec.set("lname", lname)
        rec.set("fname", fname)
        rec.set("mname", mname)
        rec.set("personal_email", email)
        rec.set("org_group_type", 1)  //  they're for the company

        // find the company_id
        Record rec2 = db.fetchOne("select org_group_id, group_name from org_group where org_group_type = 1 and owing_entity_id = org_group_id")
        final String companyName = rec2.getString("group_name")
        rec.set("company_id", rec2.getString("org_group_id"))
        rec.addRecord()

        rec = db.newRecord("prophet_login")
        rec.set("person_id", person_id)
        rec.set("can_login", "Y")
        rec.set("user_login", email)
        rec.set("user_password", password)
        rec.set("screen_group_id", "00000-0000000000")  //  fix
        rec.set("security_group_id", "00000-0000000000")  // fix
        rec.set("password_effective_date", DateUtils.today())
        rec.set("authentication_code", generateAuthentication())
        rec.set("user_type", "A")
        rec.addRecord()
    }

    private static String generateAuthentication() {
        Random r = new Random()
        StringBuilder sb = new StringBuilder()

        for (int i=0 ; i < 6 ; i++)
            sb.append("" + r.nextInt(10))
        return sb.toString()
    }

    private static void emailAuthenticationCode(String fname, String lname, String email, String authenticationCode, String companyName, String url) {
        SendEmailGeneric em = SendEmailProvider.newEmail()
        String body = """<p>Thank you for creating a $companyName applicant account.</p>
                         <p>Please go to <a href="https://$url">$url</a> and enter the following authentication code in order to complete your registration.</p>
                         <div style="font-size: larger; font-weight: bold;">Authentication Code: XXXXXX</div>
                         <p>Please do not reply to this email message.  No one will answer it.</p>
                         <p>Thank you!</p>
                         <p>$companyName</p>
                       """
        body = body.replace("XXXXXX", authenticationCode)
        em.setHTMLMessage(body)
        try {
            em.sendEmail("do-not-reply@arahant.com", companyName, email,
                    fname + " " + lname, "Your $companyName Authentication Code")
        } finally {
            em.close()
        }
    }
}
