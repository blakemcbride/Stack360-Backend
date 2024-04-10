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

package com.arahant.services.standard.at.applicant

import com.arahant.business.BApplication
import com.arahant.business.BChangeLog
import com.arahant.business.BProperty
import com.arahant.exceptions.ArahantWarning
import com.arahant.servlets.REST
import com.arahant.utils.ExternalFile
import com.arahant.utils.FileSystemUtils
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.SendEmailGeneric
import com.arahant.utils.SendEmailProvider
import org.json.JSONObject
import org.kissweb.FileUtils
import org.kissweb.NumberFormat
import org.kissweb.database.Connection
import org.kissweb.database.Record

/**
 * Author: Blake McBride
 * Date: 4/10/23
 *
 * 1. email offer to candidate
 * 2. configure for online viewing and signature
 * 3. return a copy of the letter for immediate viewing
 */
class MakeOffer {

    private static boolean testMode

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        testMode = BProperty.getBoolean("TestEnvironment", false)
        final Connection db = hsu.getKissConnection()
        final String applicationId = injson.getString("applicationId")
        final String hrPositionId = injson.getString("hrPositionId")  // this is the job they are being offered, not the job they applied for

        final Record per = db.fetchOne("""select p.lname, p.fname, p.mname, app.pay_rate, pos.position_name, p.personal_email, app.person_id, app.applicant_position_id
                                          from applicant_application app
                                          join person p
                                            on app.person_id = p.person_id
                                          join hr_position pos
                                            on app.position_id = pos.position_id
                                          where app.applicant_application_id = ?""", applicationId)
        final String applicantPositionId = per.getString("applicant_position_id")
        final String fname = per.getString("fname")
        final String mname = per.getString("mname")
        final String lname = per.getString("lname")
        String name = fname + " "
        if (mname != null && !mname.isEmpty())
            name += mname + " "
        name += lname

        Record prec = db.fetchOne("select position_name from hr_position where position_id = ?", injson.getString("hrPositionId"))

        Float payRate = injson.getFloat("payRate")
        String position = prec.getString("position_name")

        final String masterTemplateFileName = ExternalFile.makeExternalFilePath(ExternalFile.APPLICANT_POSITION_OFFER_LETTER, applicantPositionId, "html")
        if (!(new File(masterTemplateFileName).exists()))
            throw new ArahantWarning("No offer letter defined for this applicant position.")
        final String masterTemplate = FileUtils.readFile(masterTemplateFileName)

        String offerLetter = generateOffer(masterTemplate, name, null, payRate, position, null, null)
        String partialTemplate = generatePartialTemplate(masterTemplate, name, null, payRate, position, null, null)

        // save the offer letter
        String fileName = ExternalFile.makeExternalFilePath(ExternalFile.APPLICANT_APPLICATION_OFFER_LETTER, applicationId, "html")
        FileUtils.write(fileName, offerLetter)

        // save template
        fileName = ExternalFile.makeExternalFilePath(ExternalFile.APPLICANT_APPLICATION_OFFER_TEMPLATE, applicationId, "html")
        FileUtils.write(fileName, partialTemplate)

        final String personId = per.getString("person_id")
        Record urec = db.fetchOne("select authentication_code from prophet_login where person_id = ?", personId)

        emailOffer(offerLetter, fname, lname, per.getString("personal_email"), position, urec.getString("authentication_code"))

        updateApplication(db, applicationId, hrPositionId, injson.getFloat("payRate"))

        BChangeLog.personLog(personId, "Offer extended")

        outjson.put("offerLetter", offerLetter)
    }

    private static void emailOffer(String ltrHtml, String fname, String lname, String email, String position, String authenticationCode) {
        SendEmailGeneric em
        em = SendEmailProvider.newEmail()
        String body = """<p>Dear FFFFFF,</p>
                         <p>Thank you for your recent application.  We would like to extend an offer for the position of PPPPPP.
                         A copy of that offer is attached to this email.
                         Please click the following link <a href="UUUUUU?auth=XXXXXX&user=YYYYYY">CLICK HERE</a> in order to view and accept this offer.</p>
                         <p>We look forward to working with you.</p>
                         <p>Please do not reply to this email message.  No one will answer it.</p>
                         <p>Thank you!</p>
                         <p>Way To Go</p>
                         <div style="margin-top: 50px; margin-bottom: 50px;">
                         ----------------------------------------------------------------
                         </div>
                       """
        if (testMode)
            body = body.replace("UUUUUU", "http://localhost:63342/Stack360Apply/src/main/frontend/index.html")
        else
            body = body.replace("UUUUUU", "https://waytogo.arahant.com/apply")
        body = body.replace("YYYYYY", email)
        body = body.replace("FFFFFF", fname)
        body = body.replace("LLLLLL", lname)
        body = body.replace("PPPPPP", position)
        body = body.replace("XXXXXX", authenticationCode)
        body += ltrHtml
        em.setHTMLMessage(body)
        try {
            em.sendEmail("do-not-reply@wtgmerch.com", "Way To Go", email,
                    fname + " " + lname, "Job Offer Letter From Way To Go")
        } finally {
            em.close()
        }
    }

    private static void updateApplication(final Connection db, final String applicationId, final String hrPositionId, final Float payRate) {
        Record rec = db.fetchOne("select * from applicant_application where applicant_application_id = ?", applicationId)
        rec.set("position_id", hrPositionId)
        rec.set("pay_rate", payRate)
        rec.set("phase", 2) // offer made
        BApplication.updateApplicationStatusId(rec, (short) 2)
        Date dt = new Date()
        if (rec.getDateTime("offer_first_generated") == null)
            rec.setDateTime("offer_first_generated", dt)
        rec.setDateTime("offer_last_generated", dt)
        rec.update()
    }

    private static String generateOffer(String template, String name, String signature, Float payRate, String title, String ip, String dateSigned) {
        String payText = NumberFormat.Format(payRate, "DC", 0, 2) + " (" + title + ")"
        template = template.replace('$PAY-RATE', payText)  //  dollar sign in payText must be escaped
        if (signature == null) {
            template = template.replace('$SIGNATURE', "")
            template = template.replace('$DATE', "")
            template = template.replace('$NAME', name)
        } else {
            template = template.replace('$SIGNATURE', "<i><b>" + signature + "</b></i> (electronically signed from " + ip + ")")
            template = template.replace('$DATE', dateSigned)
            template = template.replace('$NAME', name)
        }
        return template
    }

    private static String generatePartialTemplate(String template, String name, String signature, Float payRate, String title, String ip, String dateSigned) {
        String payText = NumberFormat.Format(payRate, "DC", 0, 2) + " (" + title + ")"
        template = template.replace('$PAY-RATE', payText)  //  dollar sign in payText must be escaped
        if (signature == null) {
            template = template.replace('$NAME', name)
        } else {
            template = template.replace('$SIGNATURE', "<i><b>" + signature + "</b></i> (electronically signed from " + ip + ")")
            template = template.replace('$DATE', dateSigned)
            template = template.replace('$NAME', name)
        }
        return template
    }
}
