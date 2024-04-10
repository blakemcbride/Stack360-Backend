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

/*
 *
 * This class is superseded by com.arahant.utils.SensEmailAWS.
*/


package com.arahant.utils;

import com.arahant.beans.Person;
import com.arahant.business.BProperty;
import com.arahant.exceptions.ArahantException;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.io.File;
import java.sql.SQLException;
import java.util.*;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

public class Mail {
	
	private final ArahantLogger logger = new ArahantLogger(Mail.class);
	private static String host = "";

	public Mail(final String host) {
		setHost(host);
	}

	public Mail() {
		if (host.equals("")) {
			final HibernateSessionUtil hsu = new HibernateSessionUtil();

			try {
				hsu.beginTransaction();

				host = BProperty.get(StandardProperty.EMAIL_HOST);

				hsu.commitTransaction();
			} catch (final Exception e) {
				hsu.rollbackTransaction();
			}
			hsu.close();
		}
	}

	public static void setHost(final String h) {
		host = h;
	}

	public static void main(final String [] args) {
		final Mail x = new Mail();

		setHost("admin.arahant.com");
		x.sendMail("arahant@arahant.com", "xxxxxx@gmail.com", "server test", "");
	}

	public boolean sendMail(final String from, final String to, final String subject, final String outMessage) {
		return sendMail(from, to, subject, outMessage, (List<String>) null);
	}

	public static boolean send(final String from, final String to, final String subject, final String outMessage) {
		final Mail m = new Mail();
		if (from == null || to == null || from.trim().equals("") || to.trim().equals(""))
			return true;

		return m.sendMail(from, to, subject, outMessage);
	}

	public static boolean send(final HibernateSessionUtil hsu, final Person p, final String subject, final String outMessage) throws ArahantException {
		if (p.getPersonalEmail() != null && !p.getPersonalEmail().equals(""))
			Mail.send(p.getPersonalEmail(), p.getPersonalEmail(), subject, outMessage);
		final com.arahant.beans.Message m = new com.arahant.beans.Message();
		m.setMessageId(IDGenerator.generate(m));
		m.setPersonByFromPersonId(p);
		m.setMessage(outMessage);
		m.setSubject(subject);
		m.setCreatedDate(new java.util.Date());
		m.setFromShow('Y');
		hsu.insert(m);

		final Connection db = new Connection(ArahantSession.getHSU().getConnection());
		final Record rec = db.newRecord("message_to");
		rec.set("message_to_id", IDGenerator.generate("message_to", "message_to_id"));
		rec.set("message_id", m.getMessageId());
		rec.set("to_person_id", p.getPersonId());
		rec.set("send_type", "T");
		rec.set("to_show", "Y");
		rec.set("sent", "Y");
		try {
			rec.addRecord();
		} catch (SQLException throwables) {
			throw new ArahantException(throwables);
		}

		return true;
	}

	public static boolean send(final Person from, final Person to, final String subject, final String outMessage) throws ArahantException {
		if (to.getPersonalEmail() != null && !to.getPersonalEmail().equals(""))
			Mail.send(from != null ? from.getPersonalEmail() : null, to.getPersonalEmail(), subject, outMessage);
		return true;
	}

	public boolean sendMail(final String from, final String to, final String subject, final String outMessage, final String attachFilename) {
		final List<String> list = new ArrayList<String>();
		list.add(attachFilename);
		return sendMail(from, to, subject, outMessage, list);
	}

	public static boolean send(final String from, final String to, final String subject, final String outMessage, final String attachFilename) {
		final Mail m = new Mail();
		return m.sendMail(from, to, subject, outMessage, attachFilename);
	}

	public static boolean send(final String from, final String to, final String subject, final String outMessage, final List<String> attachFilenames) {
		final Mail m = new Mail();
		return m.sendMail(from, to, subject, outMessage, attachFilenames);
	}

	private boolean sendMail(final String from, final String to, final String subject, final String outMessage, final List<String> attachFilenames) {
		MailSender s = new MailSender();
		s.from = from;
		s.to = to;
		s.subject = "[" + ArahantSession.systemName() + "] " + subject;
		s.outMessage = outMessage;
		s.attachFilenames = attachFilenames;
		s.start();
		return true;
	}

	private static class Authenticator extends javax.mail.Authenticator {

		private final PasswordAuthentication authentication;

		public Authenticator() {
			String username = BProperty.get(StandardProperty.EMailUser);
			String password = BProperty.get(StandardProperty.EMailPassword);
			authentication = new PasswordAuthentication(username, password);
		}

		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return authentication;
		}
	}

	class MailSender extends Thread {

		private String from, to, subject, outMessage;
		private List<String> attachFilenames;

		@Override
		public void run() {
			ArahantSession.openHSU();
			sendMail();
			ArahantSession.clearSession();
		}

		public boolean sendMail() {
			try {
				if ("".equals(host))
					return false;

				Authenticator authenticator = new Authenticator();

				// Get system properties
				final Properties props = System.getProperties();

				if (!"".equals(BProperty.get("EMailUser"))) {
					props.setProperty("mail.smtp.submitter", authenticator.getPasswordAuthentication().getUserName());
					props.setProperty("mail.smtp.auth", "true");
				}

				// Setup mail server
				props.put("mail.smtp.host", host);

				// Get session
				final Session session;

				if ("".equals(BProperty.get("EMailUser")))
					session = Session.getDefaultInstance(props, null);
				else
					session = Session.getInstance(props, authenticator);

				// Define message
				final MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress(from));

				final StringTokenizer tokens = new StringTokenizer(to, ",;");
				while (tokens.hasMoreTokens()) {
					String address = tokens.nextToken().trim();
					Message.RecipientType type = Message.RecipientType.TO;

					if (address.toLowerCase().startsWith("to:")) {
						address = address.substring(3);
						type = Message.RecipientType.TO;
					} else if (address.toLowerCase().startsWith("cc:")) {
						address = address.substring(3);
						type = Message.RecipientType.CC;
					} else if (address.toLowerCase().startsWith("bcc:")) {
						address = address.substring(4);
						type = Message.RecipientType.BCC;
					}

					message.addRecipient(type, new InternetAddress(address));
				}

				message.setSubject(subject);

				// Create the multi-part
				final Multipart multipart = new MimeMultipart();

				// Create part one
				boolean containsHTML = outMessage.matches(".*\\<[^>]+>.*");
				BodyPart messageBodyPart = new MimeBodyPart();

				if (containsHTML) {
					System.out.println("Sending HTML formatted email: " + outMessage);
					messageBodyPart.setContent(outMessage, "text/html");
				} else
					// Fill the message
					messageBodyPart.setText(outMessage);

				// Add the first part
				multipart.addBodyPart(messageBodyPart);

				if (attachFilenames != null)
					for (final String attachFilename : attachFilenames) {
						// Part two is attachment
						messageBodyPart = new MimeBodyPart();
						final DataSource source = new FileDataSource(attachFilename);
						messageBodyPart.setDataHandler(new DataHandler(source));

						final File attachedFile = new File(attachFilename);
						messageBodyPart.setFileName(attachedFile.getName());

						// Add the second part
						multipart.addBodyPart(messageBodyPart);
					}

				// Put parts in message
				message.setContent(multipart);

				// Send message
				Transport.send(message);
				return true;
			} catch (final MessagingException e) {
				logger.error(e);
			}

			return false;

		}
	}
}
