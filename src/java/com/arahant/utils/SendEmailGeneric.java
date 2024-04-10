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

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

/**
 * Author: Blake McBride
 * Date: 2/22/23
 */
public abstract class SendEmailGeneric implements AutoCloseable {

    public abstract SendEmailGeneric sendEmail(String to, String toname, String subject) throws Exception;

    public abstract SendEmailGeneric sendEmail(String from, String fromname, String to, String toname, String subject) throws IOException, MessagingException;

    public abstract SendEmailGeneric sendEmail(List<EmailAddress> toAddresses, List<EmailAddress> ccAddresses, List<EmailAddress> bccAddresses, String subject) throws Exception;

    public abstract SendEmailGeneric sendEmail(String from, String fromname, List<EmailAddress> toAddresses, List<EmailAddress> ccAddresses, List<EmailAddress> bccAddresses, String subject) throws IOException, MessagingException;
    public abstract SendEmailGeneric setTextMessage(String txt);

    public abstract SendEmailGeneric setHTMLMessage(String txt);

    public abstract SendEmailGeneric addAttachement(String diskFileName, String attachementName);

    public abstract SendEmailGeneric addAttachement(byte [] data, String attachementName, String type);

    public abstract SendEmailGeneric setAttachments(List<EmailAttachment> attachments);

    public abstract SendEmailGeneric addAttachement(byte [] data, String attachementName);

    @Override
    public abstract void close() throws Exception;
}
