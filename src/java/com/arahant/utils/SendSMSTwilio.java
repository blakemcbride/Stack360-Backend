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

package com.arahant.utils;

import com.arahant.beans.Person;
import com.arahant.business.BPerson;
import com.arahant.business.BProperty;
import com.arahant.lisp.ABCL;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.apache.log4j.Level;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Blake McBride
 * Date: 10/19/21
 */
public class SendSMSTwilio {

    private static final ArahantLogger logger = new ArahantLogger(SendSMSTwilio.class);
    private static final String testPhoneNumber = "615-394-6760";

    private static final boolean testMode = BProperty.getBoolean("TestEnvironment", false);

    {
       // logger.setLevel(Level.ALL);
    }

    public enum Status {
        OK,
        NO_LOGIN,
        NO_PASSWORD,
        NO_SID,
        BAD_PHONE_NUMBER,
        BAD_MESSAGE
    }

    private String TWILIO_SID;
    private final Status initStatus;
    private Status status;
    private boolean createMessageRecord;

    public SendSMSTwilio() {
        this(true);
    }

    public SendSMSTwilio(boolean createMessageRecord) {
        String TWILIO_LOGIN = BProperty.get(StandardProperty.TWILIO_LOGIN);
        if (isEmpty(TWILIO_LOGIN)) {
            initStatus = Status.NO_LOGIN;
            return;
        }
        String TWILIO_PASSWORD = BProperty.get(StandardProperty.TWILIO_PASSWORD);
        if (isEmpty(TWILIO_PASSWORD)) {
            initStatus = Status.NO_PASSWORD;
            return;
        }
        TWILIO_SID = BProperty.get(StandardProperty.TWILIO_SID);
        if (isEmpty(TWILIO_SID)) {
            initStatus = Status.NO_SID;
            return;
        }
        Twilio.init(TWILIO_LOGIN, TWILIO_PASSWORD);
        this.createMessageRecord = createMessageRecord;
        status = initStatus = Status.OK;
    }

    public String sendSMStoPerson(String toPersonId, String msg) throws Exception {
        return sendSMStoPerson(toPersonId, msg, null, null);
    }

    /**
     * Send a text message to a person and record it.
     *
     * @param toPersonId
     * @param msg
     * @return
     */
    public String sendSMStoPerson(String toPersonId, String msg, List<EmailAttachment> attachments, String backEndURL) throws Exception {
        if (msg == null || msg.isEmpty()) {
            status = Status.BAD_MESSAGE;
            return null;
        }

        String number, toName;
        if (testMode) {
            number = testPhoneNumber;
            toName = "Test";
        } else {
            BPerson bp = new BPerson(toPersonId);
            number = bp.getMobilePhone();
            if (number == null || number.isEmpty()) {
                status = Status.BAD_PHONE_NUMBER;
                return null;
            }
            toName = bp.getNameFML();
        }

        // add prefix and suffix to message
        final String prefix = BProperty.get(StandardProperty.TEXT_MESSAGE_PREFIX);
        if (prefix != null  &&  !prefix.isEmpty())
            msg = prefix + " " + msg;
        final String suffix = BProperty.get(StandardProperty.TEXT_MESSAGE_SUFFIX);
        if (suffix != null  &&  !suffix.isEmpty())
            msg += " " + suffix;

        String msg_id = sendSMS(number, msg, attachments, backEndURL);

        if (msg_id != null && createMessageRecord) {
            Connection db = KissConnection.get();
            Person fromPerson = ArahantSession.getHSU(false).getCurrentPerson();

            final Record mrec = db.newRecord("message");
            String messageId = IDGenerator.generate(mrec, "message_id");
            String subject = msg;
            if (subject.length() > 80) {
                subject = subject.substring(0, 80);
            } else {
                subject = msg;
                msg = null;
            }
            mrec.set("message", msg);
            mrec.set("from_person_id", fromPerson.getPersonId());
            mrec.set("subject", subject);
            mrec.set("from_name", fromPerson.getNameFL());
            mrec.set("send_text", "Y");
            mrec.addRecord();

            Record mtrec = db.newRecord("message_to");
            IDGenerator.generate(mtrec, "message_to_id");
            mtrec.set("message_id", messageId);
            mtrec.set("to_person_id", toPersonId);
            mtrec.set("send_type", "T");
            mtrec.set("sent", "Y");
            mtrec.set("to_address", number);
            mtrec.set("to_name", toName);
            mtrec.addRecord();
        }
        return msg_id;
    }

    /**
     * Send a text message to a phone number.
     * Mainly internally or for testing purposes.
     *
     * @param number
     * @param msg
     * @return
     */
    public String sendSMS(String number, String msg, List<EmailAttachment> attachments, String backEndURL) throws IOException {

        if (testMode)
            number = testPhoneNumber;

        if (initStatus != Status.OK) {
            status = initStatus;
            return null;
        }
        if (number == null || number.length() < 10) {
            status = Status.BAD_PHONE_NUMBER;
            return null;
        }
        if (msg == null || msg.isEmpty()) {
            status = Status.BAD_MESSAGE;
            return null;
        }

        // normalize number
        number = number.replaceAll("[^\\d]", "");
        int len = number.length();
        if (len < 10) {
            status = Status.BAD_PHONE_NUMBER;
            return null;
        }
        if (number.charAt(0) == '1')
            number = number.substring(1);
        len = number.length();
        if (len != 10) {
            status = Status.BAD_PHONE_NUMBER;
            return null;
        }
        number = "+1" + number;

        Message mc;
        if (attachments == null || attachments.isEmpty() || backEndURL == null || backEndURL.length() < 7) {
            mc = Message.creator(new PhoneNumber(number),
                            TWILIO_SID,
                            msg)
                    .create();
        } else {
            List<URI> uris = new ArrayList<>();
            for (EmailAttachment at : attachments) {
                File fp = FileSystemUtils.createReportFile("SMS-", at.getFileExtension());
                String file = fp.getAbsolutePath();
                if (at.getDiskFileName() != null)
                    org.kissweb.FileUtils.copy(at.getDiskFileName(), file);
                else {
                    byte [] data = at.getByteArray();
                    org.kissweb.FileUtils.write(file, data);
                }
                if (!backEndURL.endsWith("/"))
                    backEndURL += "/";
                logger.info("backEndURL: " + backEndURL + FileSystemUtils.getHTTPPath(fp));
                uris.add(URI.create(backEndURL + FileSystemUtils.getHTTPPath(fp)));
            }
            mc = Message.creator(new PhoneNumber(number),
                            TWILIO_SID,
                            msg)
                    .setMediaUrl(uris)
                    .create();
        }

        status = Status.OK;

        return mc.getSid();
    }

    public Status getStatus() {
        return status;
    }

    private static boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static void main(String [] args) throws Exception {
        ABCL.init();  //  this code initializes the hibernate system
        //new SendSMSTwilio().sendSMS("615-394-6760", "Test 5");
        new SendSMSTwilio().sendSMStoPerson("00001-0000011759", "Test 6");
        System.exit(0);  // required because of hibernate
    }

}
