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

import com.arahant.beans.Person;
import com.arahant.business.BProperty;
import com.arahant.utils.*;
import org.apache.log4j.Level;
import org.kissweb.database.*;
import org.kissweb.database.Record;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

/**
 * Author: Blake McBride
 * Date: 8/15/23
 *
 * Receive and record incoming text messages through Twilio.
 *
 * In order to connect an external URL to this servlet, see servletNames in
 * com/arahant/utils/generators/wsconfiggen/WsConfigGen.java
 *
 * The hook on the Twilio side will look like this:
 * https://[URL]/[backend-location]/SMSTwilio
 *
 */
@MultipartConfig
public class SMSTwilio extends HttpServlet {

    private static final ArahantLogger logger = new ArahantLogger(SMSTwilio.class);
    private static final int MAX_MESSAGE_SIZE = 1024;
    private final static boolean debug = false;

    static {
        if (debug)
            logger.setLevel(Level.ALL);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HibernateSessionUtil hsu = ArahantSession.openHSU(false);
        Connection db = KissConnection.get();
        try {
            BufferedReader br = request.getReader();
            char[] buf = new char[MAX_MESSAGE_SIZE];
            int offset = 0;
            int len = buf.length;
            int n;
            do {
                n = br.read(buf, offset, len);
                if (n > 0) {
                    offset += n;
                    len -= n;
                }
            } while (n > 0 && len > 0);
            br.close();
            String[] msg = new String(buf, 0, offset).split("&");
            String from = null;
            String body = null;
            String messageSid = null;
            logger.debug("* * * * * * * * * * * * * * * * begin receive text message");
            for (String s : msg) {
                if (s.startsWith("From=")) {
                    from = Formatting.formatPhoneNumber(s.substring(5).replace("%2B1", ""));
                } else if (s.startsWith("Body=")) {
                    s = s.substring(5);
                    body = URLDecoder.decode(s, StandardCharsets.UTF_8);
                    body = body.replace('+', ' ');
                } else if (s.startsWith("MessagingServiceSid=")) {
                    messageSid = s.substring(20);
                } else {
                    logger.debug(s);
                }
            }
            if (from != null)
                logger.debug("From: " + from);
            if (body != null)
                logger.debug("Message: " + body);
            final String twilioSid = BProperty.get("TWILIO_SID");
            if (twilioSid == null || twilioSid.isEmpty()) {
                logger.error("Missing TWILIO_SID");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
            if (messageSid == null || messageSid.isEmpty()) {
                logger.error("MessagingServiceSid missing from Twilio");
                return;
            }
            if (!messageSid.equals(twilioSid)) {
                logger.error("Invalid Twilio MessageServiceSId (doesn't match TWILIO_SID property): " + messageSid);
                return;
            }
            if (from == null || from.isEmpty()) {
                logger.error("Empty From");
                return;
            }
            if (body == null || body.isEmpty()) {
                logger.error("Empty Body");
                return;
            }
            final List<Record> recs = db.fetchAll("select ph.person_join, per.fname, per.lname, per.ssn " +
                    "from phone ph " +
                    "join person per " +
                    "  on ph.person_join = per.person_id " +
                    "where ph.phone_number like ? " +
                    "      and ph.person_join is not null", from + "%");
            if (recs.isEmpty()) {
                logger.error("No person found for phone number: " + from);
                return;
            }
            if (recs.size() > 1) {
                logger.error("Multiple persons found for phone number: " + from);
                return;
            }
            final Record phrec = recs.get(0);

            String name = "Person: " + phrec.getString("fname") + " " + phrec.getString("lname");
            String ssn = Person.decryptSsn(phrec.getString("ssn"));
            if (ssn != null && !ssn.isEmpty())
                name += " (" + ssn + ")";
            logger.debug(name);
            final String personId = phrec.getString("person_join");
            final Record mrec = db.newRecord("message");
            IDGenerator.generate(mrec, "message_id");
            mrec.set("message", body);
            mrec.set("from_person_id", personId);
            mrec.set("from_address", from);
            mrec.set("from_name", phrec.getString("fname") + " " + phrec.getString("lname"));
            mrec.set("send_text", "Y");
            mrec.addRecord();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            logger.debug("* * * * * * * * * * * * * * * * end receive text message");
            try {
                db.commit();
            } catch (SQLException ignore) {
            }
            ArahantSession.clearSession();
        }
    }

}
