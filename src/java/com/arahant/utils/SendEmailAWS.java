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

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * User: Blake McBride
 * Date: 7/29/18
 *
 * Send email over AWS.  This supersedes com.arahant.utils.Mail.
 *
 * In order for this to work, you do not need the underlying OS to be able to send email.
 * This works without the server needing to be able to send email.  You don't need Postfix or SendMail.
 *
 * This module does depend on javax.mail-1.6.1.jar
 */
class SendEmailAWS extends SendEmailGeneric {
    /**
     * People have email addresses which goes through their normal email process.
     * We keep their email address in their HR record.  What we're doing is if their email address is equal to the
     * corporate email domain, we're changing the domain so that any returns will come to the system.
     */
    private final String systemEmailDomain = BProperty.get(StandardProperty.EMAIL_DOMAIN);

    private static final String testEmail = "blake1024@gmail.com";

    private Session session;
    private Transport transport;

    // Replace smtp_username with your Amazon SES SMTP user name.
    private String SMTP_USERNAME;

    // Replace smtp_password with your Amazon SES SMTP password.
    private String SMTP_PASSWORD;

    // Amazon SES SMTP host name. This example uses the US West (Oregon) region.
    // See http://docs.aws.amazon.com/ses/latest/DeveloperGuide/regions.html#region-endpoints
    // for more information.
    private String HOST;

    // The port you will connect to on the Amazon SES SMTP endpoint.
    private int PORT = 25;

    private boolean testMode;
    private String message;
    private boolean isHTML;

    //  Do not make this public!
    SendEmailAWS() throws MessagingException {
        testMode = BProperty.getBoolean("TestEnvironment", false);
        HOST = BProperty.get(StandardProperty.SMTP_HOST);
        SMTP_USERNAME = BProperty.get(StandardProperty.SMTP_USERNAME);
        SMTP_PASSWORD = BProperty.get(StandardProperty.SMTP_PASSWORD);

        if (SMTP_USERNAME == null  ||  SMTP_USERNAME.isEmpty()  ||
                SMTP_PASSWORD == null  ||  SMTP_PASSWORD.isEmpty()  ||
                HOST == null  ||  HOST.isEmpty())
            throw new ArahantException("AWS email not configured");
        // Create a Properties object to contain connection configuration information.
        final Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");

        // Create a Session object to represent a mail session with the specified properties.
        session = Session.getDefaultInstance(props);
        // Create a transport.
        transport = session.getTransport();
        transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
    }

    // Just for testing
    private SendEmailAWS(String host, String user, String pw) throws Exception {
        testMode = true;
        HOST = host;
        SMTP_USERNAME = user;
        SMTP_PASSWORD = pw;

        // Create a Properties object to contain connection configuration information.
        final Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");

        // Create a Session object to represent a mail session with the specified properties.
        session = Session.getDefaultInstance(props);
        // Create a transport.
        transport = session.getTransport();
        transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
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

    private List<EmailAttachment> attachments = null;

    private static final String BODY =
            "<h1>Amazon SES SMTP Email Test</h1>" +
            "<p>This email was sent with Amazon SES using the " +
            "<a href='https://github.com/javaee/javamail'>Javamail Package</a>" +
            " for <a href='https://www.java.com'>Java</a>.";

    public static void main(String[] args) throws Exception {
//        SendEmailAWS em = new SendEmailAWS("email-smtp.us-east-2.amazonaws.com", "XXXXXXXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXXXXXXXXXXXX");
        SendEmailAWS em = new SendEmailAWS("email-smtp.us-east-2.amazonaws.com", "XXXXXXXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXXXXXXXXXXXX");
        em.addAttachement("/home/blake/Arahant/WP - Sales/Deep Dive Questions.pdf", "file1.pdf");
        em.addAttachement("/home/blake/Arahant/WP - Sales/Why_Arahant_2.pdf", "file2.pdf");
        em.setHTMLMessage(BODY);
        Date beg = new Date();
        for (int i=0 ; i < 1 ; i++) {
            System.out.println(i);
            ArrayList<EmailAddress> to = new ArrayList<>();
            to.add(new EmailAddress("blake1024@gmail.com", "Blake McBride(2)"));
            em.sendEmail("blake@arahant.com", "Blake(1)", to, null, null, (new Date()).toString() + " " + i);
        }
        Date end = new Date();
        em.close();
        long diff = end.getTime() - beg.getTime();
        System.out.println(diff / 1000L);
    }

    /**
     * This gets the sender info from the person logged in or the default sender.
     *
     * @param to
     * @param toname
     * @param subject
     * @return
     * @throws Exception
     */
    public SendEmailGeneric sendEmail(String to, String toname, String subject) throws Exception {
        EmailAddress ad = new EmailAddress(to, toname);
        List<EmailAddress> adl = new ArrayList<>();
        adl.add(ad);
        return sendEmail(adl, null, null, subject);
    }

    public SendEmailGeneric sendEmail(String from, String fromname, String to, String toname, String subject) throws MessagingException, UnsupportedEncodingException {
        EmailAddress ad = new EmailAddress(to, toname);
        List<EmailAddress> adl = new ArrayList<>();
        adl.add(ad);
        return sendEmail(from, fromname, adl, null, null, subject);
    }

    public SendEmailGeneric sendEmail(List<EmailAddress> toAddresses, List<EmailAddress> ccAddresses, List<EmailAddress> bccAddresses, String subject) throws Exception {
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
        from = extendEmailDomain(from);
        fromname = p.getNameFML();
        return sendEmail(from, fromname, toAddresses, ccAddresses, bccAddresses, subject);
    }

    public SendEmailGeneric sendEmail(String from, String fromname, List<EmailAddress> toAddresses, List<EmailAddress> ccAddresses, List<EmailAddress> bccAddresses, String subject) throws UnsupportedEncodingException, MessagingException {
        if ((toAddresses == null || toAddresses.isEmpty()) &&
                (ccAddresses == null || ccAddresses.isEmpty()) &&
                (bccAddresses == null || bccAddresses.isEmpty()))
            return null;
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from, fromname));

        if (testMode) {
            Address[] toAdd = new Address[1];
            toAdd[0] = new InternetAddress(testEmail, "(test)");
            msg.setRecipients(Message.RecipientType.TO, toAdd);
        } else {
            if (toAddresses != null && !toAddresses.isEmpty()) {
                Address[] toAdd = new Address[toAddresses.size()];
                for (int i = 0; i < toAddresses.size(); i++) {
                    EmailAddress ea = toAddresses.get(i);
                    toAdd[i] = new InternetAddress(ea.getAddress(), ea.getName());
                }
                msg.setRecipients(Message.RecipientType.TO, toAdd);
            }

            if (ccAddresses != null && !ccAddresses.isEmpty()) {
                Address[] ccAdd = new Address[ccAddresses.size()];
                for (int i = 0; i < ccAddresses.size(); i++) {
                    EmailAddress ea = ccAddresses.get(i);
                    ccAdd[i] = new InternetAddress(ea.getAddress(), ea.getName());
                }
                msg.setRecipients(Message.RecipientType.CC, ccAdd);
            }

            if (bccAddresses != null && !bccAddresses.isEmpty()) {
                Address[] bccAdd = new Address[bccAddresses.size()];
                for (int i = 0; i < bccAddresses.size(); i++) {
                    EmailAddress ea = bccAddresses.get(i);
                    bccAdd[i] = new InternetAddress(ea.getAddress(), ea.getName());
                }
                msg.setRecipients(Message.RecipientType.BCC, bccAdd);
            }
        }
        msg.setSubject(subject);
        if (attachments != null) {
            BodyPart messageBodyPart = new MimeBodyPart();
            if (isHTML)
                messageBodyPart.setContent(message, "text/html");
            else
                messageBodyPart.setText(message);
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            for (EmailAttachment att : attachments) {
                messageBodyPart = new MimeBodyPart();
                DataSource source;
                if (att.getDiskFileName() != null)
                    source = new FileDataSource(att.getDiskFileName());
                else
                    source = new ByteArrayDataSource(att.getByteArray(), att.getMimeType());
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(att.getAttachmentName());
                multipart.addBodyPart(messageBodyPart);
            }
            msg.setContent(multipart);
        } else
            if (isHTML)
                msg.setContent(message, "text/html");
            else
                msg.setText(message);
        transport.sendMessage(msg, msg.getAllRecipients());
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

    public void close() throws MessagingException {
        transport.close();
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
