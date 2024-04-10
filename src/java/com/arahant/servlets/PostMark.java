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

package com.arahant.servlets;

import com.arahant.business.BProperty;
import com.arahant.utils.*;
import org.apache.log4j.Level;
import org.json.JSONArray;
import org.json.JSONObject;
import org.kissweb.Base64;
import org.kissweb.FileUtils;
import org.kissweb.RestServerBase;
import org.kissweb.database.*;
import org.kissweb.database.Record;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static com.arahant.utils.ExternalFile.makeExternalFilePath;

/**
 * Author: Blake McBride
 * Date: 10/30/23
 * <br><br>
 * Receive and record incoming email through PostMark.
 * <br><br>
 * In order to connect an external URL to this servlet, see
 * <code>@WebServlet</code> below.
 * <br><br>
 * The hook on the PostMark side will look like this:
 * https://[URL]/[backend-location]/PostMark
 *
 */
@WebServlet(urlPatterns="/PostMark")
@MultipartConfig
public class PostMark extends RestServerBase {

    private static final ArahantLogger logger = new ArahantLogger(PostMark.class);
    private final static boolean debug = true;
    /**
     * People have email addresses which goes through their normal email process.
     * We keep their email address in their HR record.  What we're doing is if their email address is equal to the
     * corporate email domain, we're cutting off the prefix so we can look up their regular email address.
     */
    private final String emailDomain = BProperty.get(StandardProperty.EMAIL_DOMAIN);

    static {
        if (debug)
            logger.setLevel(Level.ALL);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("* * * * * * * * * * * * * * * * begin receive PostMark email");
        ArahantSession.openHSU(false);
        final Connection db = KissConnection.get();
        try {
            final String u = BProperty.get("POSTMARK_USERNAME");
            if (u == null || u.isEmpty()) {
                logger.error("POSTMARK_USERNAME is not set");
                return;
            }
            final String p = BProperty.get("POSTMARK_PASSWORD");
            if (p == null || p.isEmpty()) {
                logger.error("POSTMARK_PASSWORD is not set");
                return;
            }
            if (!basicAuthenticate(request, response, null, u, p))
                return;
            JSONObject msg = getBodyJson(request);
            if (msg == null)
                return;
            logger.info(msg.toString(2));

            String messageId = null;
            JSONArray toFull = msg.getJSONArray("ToFull");
            for (int i=0 ; i < toFull.length() ; i++) {
                JSONObject to = toFull.getJSONObject(i);
                String toEmail = to.getString("Email");
                String personId = getPersonId(db, toEmail);
                if (personId == null)
                    continue;
                if (messageId == null)
                    messageId = createMessage(db, msg);
                Record mtrec = db.newRecord("message_to");
                IDGenerator.generate(mtrec, "message_to_id");
                mtrec.set("message_id", messageId);
                mtrec.set("to_person_id", personId);
                mtrec.set("send_type", "T");
                mtrec.set("sent", "Y");
                mtrec.set("to_address", to.getString("Email"));
                mtrec.set("to_name", to.getString("Name"));
                mtrec.addRecord();
            }

            JSONArray ccFull = msg.getJSONArray("CcFull");
            for (int i=0 ; i < ccFull.length() ; i++) {
                JSONObject to = ccFull.getJSONObject(i);
                String toEmail = to.getString("Email");
                String personId = getPersonId(db, toEmail);
                if (personId == null)
                    continue;
                if (messageId == null)
                    messageId = createMessage(db, msg);
                Record mtrec = db.newRecord("message_to");
                IDGenerator.generate(mtrec, "message_to_id");
                mtrec.set("message_id", messageId);
                mtrec.set("to_person_id", personId);
                mtrec.set("send_type", "C");
                mtrec.set("sent", "Y");
                mtrec.set("to_address", to.getString("Email"));
                mtrec.set("to_name", to.getString("Name"));
                mtrec.addRecord();
            }

            if (messageId != null) {
                JSONArray attFull = msg.getJSONArray("Attachments");
                for (int i = 0; i < attFull.length(); i++) {
                    JSONObject att = attFull.getJSONObject(i);
                    String contentType = att.getString("ContentType");
                    String textContent = att.getString("Content");
                    String fileName = att.getString("Name");
                    String ext = ExternalFile.fileExtension(fileName);
                    boolean isBinary = RestServerBase.isBinaryContentType(contentType) && Base64.mightBeBase64(textContent);
                    Record atRec = db.newRecord("message_attachment");
                    atRec.set("message_id", messageId);
                    atRec.set("source_file_name", fileName);
                    String messageAttachmentId = IDGenerator.generate(atRec, "message_attachment_id");
                    String extFname = makeExternalFilePath(ExternalFile.MESSAGE_ATTACHMENT_ATTACHMENT, messageAttachmentId, ext);
                    atRec.addRecord();
                    if (isBinary) {
                        byte [] binaryContent = Base64.decode(textContent);
                        FileUtils.write(extFname, binaryContent);
                    } else {
                        FileUtils.write(extFname, textContent);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            logger.info("* * * * * * * * * * * * * * * * end receive PostMark email");
            try {
                db.commit();
            } catch (SQLException ignore) {
            }
            ArahantSession.clearSession();
        }
    }

    private String createMessage(Connection db, JSONObject msg) throws Exception {
        Record mrec = db.newRecord("message");
        String messageId = IDGenerator.generate(mrec, "message_id");
        mrec.set("message", msg.getString("HtmlBody"));
        mrec.set("from_person_id", getPersonId(db, msg.getString("From")));
        mrec.set("subject", msg.getString("Subject"));
        mrec.set("from_address", msg.getString("From"));
        mrec.set("from_name", msg.getString("FromName"));
        mrec.set("send_email", "Y");
        mrec.addRecord();
        return messageId;
    }

    private String getPersonId(Connection db, String email) throws Exception {
        if (email == null || !email.contains("@"))
            return null;
        email = email.toLowerCase();
        if (emailDomain != null  &&  !emailDomain.isEmpty()) {
            String domain = email.substring(email.indexOf("@") + 1);
            if (domain.equals(emailDomain))
                email = email.substring(0, email.indexOf("@")+1) + domain.substring( domain.indexOf(".") + 1);
        }
        List<Record> recs = db.fetchAll("select person_id from person where lower(personal_email) = ?", email);
        return recs.size() == 1 ? recs.get(0).getString("person_id") : null;
    }

}
