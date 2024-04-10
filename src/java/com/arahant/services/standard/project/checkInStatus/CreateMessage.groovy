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

package com.arahant.services.standard.project.checkInStatus

import com.arahant.business.BMessage
import com.arahant.business.BProperty
import com.arahant.exceptions.ArahantException
import com.arahant.servlets.REST
import com.arahant.utils.ArahantLogger
import com.arahant.utils.Email
import com.arahant.utils.HibernateSessionUtil
import com.arahant.utils.IDGenerator
import com.arahant.utils.SendSMSTwilio
import org.json.JSONObject
import org.kissweb.StringUtils
import org.kissweb.database.Connection
import org.kissweb.database.Record

class CreateMessage {

    private static logger = new ArahantLogger(CreateMessage.class)

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
   // 	private void createMessage(HibernateSessionUtil hsu, HashMap<String, String> nameValuePairs, List<FileUploadServlet.ClientFile> clientFiles) throws Exception {
		if (BProperty.getBoolean("DontUseMessageSystem"))
			return;
		String subject = injson.getString("subject");
		String htmlMessage = injson.getString("htmlMessage");
		String fromPersonId = injson.getString("fromPersonId");
		String toPersonIds = injson.getString("toPersonIds");
		boolean sendEmail = injson.getBoolean("sendEmail");
		boolean sendText = injson.getBoolean("sendText");
		boolean sendInternal = false;
		boolean dontSendBody = false;
		SendSMSTwilio sm = null; // text message
		String detail = null;    // text message

		if (sendText) {
			sm = new SendSMSTwilio(false);
			if (sm.getStatus() != SendSMSTwilio.Status.OK)
				throw new ArahantException("Twilio is not configured. It is required to send text messages.");
			detail = subject == null ? "" : subject;
			if (htmlMessage != null && !htmlMessage.isEmpty())
				detail += ": " + StringUtils.htmlToText(htmlMessage);
		}

		final Connection db = hsu.getKissConnection();

		Record fprec = db.fetchOne("select personal_email, fname, lname from person where person_id=?", fromPersonId);
		String fromEmail = fprec.getString("personal_email");

		String name = fprec.getString("fname");
		if (name == null)
			name = "";
		if (!name.isEmpty())
			name += " ";
		String lname = fprec.getString("lname");
		if (lname != null && !lname.isEmpty())
			name += lname;

		if (sendEmail && !Email.canSendEmail(fromEmail)) {
			sendEmail = false;
			logger.warn("User " + name + " is attempting to send email but cannot.");
		}

		Record rec = db.newRecord("message");
		IDGenerator.generate(rec, "message_id");
		String messageId = rec.getString("message_id");
		rec.set("message", htmlMessage);
		rec.set("from_person_id", fromPersonId);
		rec.setDateTime("created_date", new Date());
		rec.set("subject", subject);
		rec.set("from_address", fromEmail);
		rec.set("from_name", name);
		rec.set("dont_send_body", dontSendBody ? "Y" : "N");
		rec.set("send_email", sendEmail ? "Y" : "N");
		rec.set("send_text", sendText ? "Y" : "N");
		rec.set("send_internal", sendInternal ? "Y" : "N");
		rec.addRecord();

		for (String toPersonId : StringUtils.split(toPersonIds, ",")) {
			rec = db.newRecord("message_to");
			IDGenerator.generate(rec, "message_to_id");
			rec.set("message_id", messageId);
			rec.set("to_person_id", toPersonId);
			rec.set("send_type", "T");
			rec.set("to_show", "Y");
			rec.set("sent", sendEmail ? "Y" : "N");

			if (sendText)
				sm.sendSMStoPerson(toPersonId, detail);

			Record prec = db.fetchOne("select personal_email, fname, lname from person where person_id=?", toPersonId);
			if (prec != null) {
				String toadd = prec.getString("personal_email");
				rec.set("to_address", toadd);
				if (sendEmail && !Email.isValidDomain(toadd))
					rec.set("sent", "N");
				name = prec.getString("fname");
				if (name == null)
					name = "";
				lname = prec.getString("lname");
				if (lname != null && !lname.isEmpty())
					name += " " + lname;
				if (!name.isEmpty())
					rec.set("to_name", name);
				rec.addRecord();
			}
		}

		if (sendEmail)
            BMessage.copyHTMLToEmail(fromPersonId, StringUtils.split(toPersonIds, ","), null, null, subject, htmlMessage, null);

	}
}
