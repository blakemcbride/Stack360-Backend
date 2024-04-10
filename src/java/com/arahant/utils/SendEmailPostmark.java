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
import com.arahant.business.BProperty;
import com.arahant.exceptions.ArahantException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.kissweb.Base64;
import org.kissweb.FileUtils;
import org.kissweb.RestClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Blake McBride
 * Date: 2/13/23
 *
 * Email interface for SendEmailPostmark.
 *
 *  This is the first email provider that was done generically so that it'll be easier to switch email providers in the future.
 */

//  should not be public!
class SendEmailPostmark extends SendEmailGeneric {

    /**
     * People have email addresses which goes through their normal email process.
     * We keep their email address in their HR record.  What we're doing is if their email address is equal to the
     * corporate email domain, we're changing the domain so that any returns will come to the system.
     */
    private final String systemEmailDomain = BProperty.get(StandardProperty.EMAIL_DOMAIN);

    private static final String testEmail = "blake1024@gmail.com";

    private static final String URL = "https://api.postmarkapp.com/email";
    private static final String STACK360_API_KEY = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

    private final boolean testMode;
    private String apiKey;
    private String message;
    private boolean isHTML;
    private List<EmailAttachment> attachments = null;

    //  Do not make this public!
    SendEmailPostmark() {
        testMode = BProperty.getBoolean(StandardProperty.TestEnvironment, false);
        apiKey = BProperty.get(StandardProperty.POSTMARK_API_KEY);
        if (apiKey == null || apiKey.isEmpty())
            throw new ArahantException("POSTMARK_API_KEY not set.");
    }

    public SendEmailPostmark sendEmail(String to, String toname, String subject) throws Exception {
        EmailAddress ad = new EmailAddress(to, toname);
        List<EmailAddress> adl = new ArrayList<>();
        adl.add(ad);
        return sendEmail(adl, null, null, subject);
    }

    public SendEmailPostmark sendEmail(String from, String fromname, String to, String toname, String subject) throws IOException {
        EmailAddress ad = new EmailAddress(to, toname);
        List<EmailAddress> adl = new ArrayList<>();
        adl.add(ad);
        return sendEmail(from, fromname, adl, null, null, subject);
    }

    public SendEmailPostmark sendEmail(List<EmailAddress> toAddresses, List<EmailAddress> ccAddresses, List<EmailAddress> bccAddresses, String subject) throws Exception {
        if ((toAddresses == null || toAddresses.isEmpty()) &&
                (ccAddresses == null || ccAddresses.isEmpty()) &&
                (bccAddresses == null || bccAddresses.isEmpty()))
            throw new ArahantException("No recipient address.");
        HibernateSessionUtil hsu = ArahantSession.getHSU();
        Person p = hsu.getCurrentPerson();
        String from = p.getPersonalEmail();
        String fromname;
        if (!Email.canSendEmail(from)) {
            p = hsu.createCriteria(Person.class).eq(Person.DEFAULT_EMAIL_SENDER, "Y").first();
            if (p == null)
                return null;
            from = p.getPersonalEmail();
            if (!Email.canSendEmail(from))
                throw new ArahantException("Default email sender " + p.getLname()  + " has an invalid or missing email address.");
        }
        fromname = p.getNameFML();
        return sendEmail(from, fromname, toAddresses, ccAddresses, bccAddresses, subject);
    }

    /**
     * This is the method that actually sends the message.
     *
     * @param from
     * @param fromname
     * @param toAddresses
     * @param ccAddresses
     * @param bccAddresses
     * @param subject
     * @return
     */
    public SendEmailPostmark sendEmail(String from, String fromname, List<EmailAddress> toAddresses, List<EmailAddress> ccAddresses, List<EmailAddress> bccAddresses, String subject) throws IOException {
        from = extendEmailDomain(from);
        if ((toAddresses == null || toAddresses.isEmpty()) &&
                (ccAddresses == null || ccAddresses.isEmpty()) &&
                (bccAddresses == null || bccAddresses.isEmpty()))
            return null;
        RestClient rc = new RestClient();
        JSONObject headers = new JSONObject();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("X-Postmark-Server-Token", apiKey);
        JSONObject data = new JSONObject();
        data.put("MessageStream", "outbound");
        data.put("Subject", subject);
        if (fromname != null && !fromname.isEmpty())
            data.put("From", fromname + " <" + from + ">");
        else
            data.put("From", from);

        if (!testMode) {
            if (toAddresses != null && !toAddresses.isEmpty()) {
                boolean addComma = false;
                StringBuilder to = new StringBuilder();
                for (EmailAddress t : toAddresses) {
                    if (addComma)
                        to.append(", ");
                    else
                        addComma = true;
                    if (t.getName() != null && !t.getName().isEmpty())
                        if (t.getName().contains(","))
                            to.append("\"").append(t.getName()).append("\"").append(" <").append(t.getAddress()).append(">");
                        else
                            to.append(t.getName()).append(" <").append(t.getAddress()).append(">");
                    else
                        to.append(t.getAddress());
                }
                data.put("To", to.toString());
            }
        } else {
            data.put("To", testEmail);
        }

        if (!testMode && ccAddresses != null && !ccAddresses.isEmpty()) {
            boolean addComma = false;
            StringBuilder cc = new StringBuilder();
            for (EmailAddress t : ccAddresses) {
                if (addComma)
                    cc.append(", ");
                else
                    addComma = true;
                if (t.getName() != null && !t.getName().isEmpty())
                    cc.append(t.getName()).append(" <").append(t.getAddress()).append(">");
                else
                    cc.append(t.getAddress());
            }
            data.put("Cc", cc.toString());
        }

        if (!testMode && bccAddresses != null && !bccAddresses.isEmpty()) {
            boolean addComma = false;
            StringBuilder bcc = new StringBuilder();
            for (EmailAddress t : bccAddresses) {
                if (addComma)
                    bcc.append(", ");
                else
                    addComma = true;
                if (t.getName() != null && !t.getName().isEmpty())
                    bcc.append(t.getName()).append(" <").append(t.getAddress()).append(">");
                else
                    bcc.append(t.getAddress());
            }
            data.put("Bcc", bcc.toString());
        }

        if (message != null && !message.isEmpty())
            if (isHTML)
                data.put("HtmlBody", message);
            else
                data.put("TextBody", message);

        if (attachments != null && !attachments.isEmpty()) {
            JSONArray att = new JSONArray();
            for (EmailAttachment a : attachments) {
                JSONObject aobj = new JSONObject();
                if (a.getDiskFileName() == null && a.getByteArray() != null) {
                    aobj.put("Content", Base64.encode(a.getByteArray()));
                    aobj.put("Name", a.getAttachmentName());
                    aobj.put("ContentType", FileUtils.getMimeType(a.getAttachmentName()));
                } else if (a.getDiskFileName() != null && a.getByteArray() == null) {
                    aobj.put("Content", Base64.encode(FileUtils.readFileBytes(a.getDiskFileName())));
                    aobj.put("Name", a.getAttachmentName());
                    aobj.put("ContentType", org.kissweb.FileUtils.getMimeType(a.getDiskFileName()));
                } else
                    continue;
                att.put(aobj);
            }
            data.put("Attachments", att);
        }

        JSONObject res = rc.jsonCall("POST", URL, data, headers);
        String rs = rc.getResponseString();
        if (rs != null) {
            try {
                JSONObject json = new JSONObject(rs);
                int ec = json.getInt("ErrorCode");
                if (ec != 0)
                    System.out.println("Postmark error response - " + rs);
            } catch (Exception e) {
                // ignore
            }
        }

        return this;
    }

    public SendEmailGeneric setTextMessage(String txt) {
        message = txt;
        isHTML = false;
        return this;
    }

    public SendEmailGeneric setHTMLMessage(String txt) {
        message = txt;
        isHTML = true;
        return this;
    }

    public SendEmailGeneric addAttachement(String diskFileName, String attachementName) {
        if (attachments == null)
            attachments = new ArrayList<>();
        attachments.add(new EmailAttachment(diskFileName, attachementName));
        return this;
    }

    public SendEmailGeneric addAttachement(byte [] data, String attachementName, String type) {
        if (attachments == null)
            attachments = new ArrayList<>();
        attachments.add(new EmailAttachment(data, attachementName, type));
        return this;
    }

    public SendEmailGeneric setAttachments(List<EmailAttachment> attachments) {
        this.attachments = attachments;
        return this;
    }

    public SendEmailGeneric addAttachement(byte [] data, String attachementName) {
        if (attachments == null)
            attachments = new ArrayList<>();
        attachments.add(new EmailAttachment(data, attachementName, org.kissweb.FileUtils.getMimeType(attachementName)));
        return this;
    }

    public static void main(String[] args) throws IOException {
        RestClient rc = new RestClient();
        JSONObject headers = new JSONObject();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("X-Postmark-Server-Token", STACK360_API_KEY);
        JSONObject data = new JSONObject();
//        data.put("From", "blake@stack360.io");
        data.put("From", "Sam Jones <do-not-reply@stack360.io>");
        data.put("To", "John Smith <blake@stack360.io>, Jane Doe <blake1024@gmail.com>");
        data.put("Subject", "Hello 1");
      //  data.put("TextBody", "Hello message body");
        data.put("HtmlBody", "<h1>The Title</h1><p>Hello message body</p>");
        data.put("MessageStream", "outbound");

        JSONArray attachments = new JSONArray();
        JSONObject attachment = new JSONObject();
        attachment.put("Content", Base64.encode(FileUtils.readFileBytes("/home/blake/Personal/BlakeSmall.jpg")));
        attachment.put("Name", "BlakeSmall.jpg");
        attachment.put("ContentType", "image/jpeg");
        attachments.put(attachment);
        data.put("Attachments", attachments);

        JSONObject res = rc.jsonCall("POST", URL, data, headers);
        String rs = rc.getResponseString();
        int i = 1;
    }

    @Override
    public void close() throws Exception {
        apiKey = null;
    }

    private String extendEmailDomain(String email) {
        if (systemEmailDomain == null || systemEmailDomain.isEmpty())
            return email;
        String fromDomain = email.substring(email.indexOf("@") + 1);
        String subDomain = systemEmailDomain.substring(systemEmailDomain.indexOf(".") + 1);
        if (fromDomain.equals(subDomain))
            return email.substring(0, email.indexOf("@") + 1) + systemEmailDomain;
        return email;
    }
}
