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

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.kissweb.FileUtils;

import java.io.IOException;
import org.kissweb.Base64;

/**
 * User: Blake McBride
 * Date: 10/24/22
 *
 * My old account blake@arahant.com is disabled because I didn't use it for too long.
 * I created a new account blake@stack360.io
 * It works but is requiring me to set up Authy however their instructions do not work.
 * I tried several avenues at contacting them and was unable.
 * I therefore decided to switch vendors.
 *
 * The code below does work, however.
 */
public class SendGrid {

    private final static String SENDGRID_API_KEY = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

    public static void main(String[] args) throws IOException {
        Email from = new Email("blake@stack360.io");      // works
  //      Email from = new Email("don-not-reply@wtgmerch.com");
        String subject = "Sending with Twilio SendGrid is Fun";
        Email to = new Email("blake1024@gmail.com");


 //       Content content = new Content("text/plain", "Sample email body text");
        Content content = new Content("text/html", "<h1>The Title</h1>  <p>the body</p>");
        Mail mail = new Mail(from, subject, to, content);

        Attachments attachments = new Attachments();
        attachments.setContent(Base64.encode(FileUtils.readFileBytes("/home/blake/Personal/BlakeSmall.jpg")));
        attachments.setFilename("BlakeSmall.jpg");
        attachments.setType("image/jpeg");
        attachments.setDisposition("attachment");
        mail.addAttachments(attachments);


        com.sendgrid.SendGrid sg = new com.sendgrid.SendGrid(SENDGRID_API_KEY);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }
    }
}
