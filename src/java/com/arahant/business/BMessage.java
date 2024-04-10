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
 */

package com.arahant.business;

import com.arahant.beans.Message;
import com.arahant.beans.Person;
import com.arahant.utils.StandardProperty;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.business.interfaces.IMessageSearchCriteria;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;
import org.kissweb.database.Command;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BMessage extends BusinessLogicBase implements IDBFunctions {

    private static final ArahantLogger logger = new ArahantLogger(BMessage.class);
    private Message message;

    public BMessage() {
    }



    /**
     * @param messageId
     * @throws ArahantException
     */
    public BMessage(final String messageId) throws ArahantException {
        internalLoad(messageId);
    }

    public BMessage(final Message msg) {
        message = msg;
    }

    @Override
    public String create() throws ArahantException {
        message = new Message();
        message.generateId();
        message.setCreatedDate(new Date());
        message.setFromShow('Y');
        return message.getMessageId();
    }

    @Override
    public void delete() throws ArahantDeleteException {
        try {
            ArahantSession.getHSU().delete(message);
        } catch (final RuntimeException e) {
            throw new ArahantDeleteException();
        }
    }

    @Override
    public void insert() throws ArahantException {
        ArahantSession.getHSU().insert(message);
    }

    private void internalLoad(final String key) throws ArahantException {
        logger.debug("Loading " + key);
        message = ArahantSession.getHSU().get(Message.class, key);
    }

    @Override
    public void load(final String key) throws ArahantException {
        internalLoad(key);
    }

    @Override
    public void update() throws ArahantException {
        ArahantSession.getHSU().saveOrUpdate(message);
    }

    public Date getCreatedDate() {
        return message.getCreatedDate();
    }

    public char getFromShow() {
        return message.getFromShow();
    }

    public char getSendInternal() {
        return message.getSendInternal();
    }

    public void setSendInternal(char c) {
        message.setSendInternal(c);
    }

    public String getMessage() {
        return message.getMessage();
    }

    public String getMessageId() {
        return message.getMessageId();
    }

    public String getSubject() {
        return message.getSubject();
    }

    public String getFromAddress() {
        return message.getFromAddress();
    }

    public BMessage setFromAddress(String ad) {
        message.setFromAddress(ad);
        return this;
    }

    /**
     * Retrieve the "from name".  If it is blank get it from the person record.
     */
    public String getFromName() {
        String name = message.getFromName();
		if (name == null || name.isEmpty()) {
			String fromPersonId = getFromPersonId();
			if (fromPersonId != null && !fromPersonId.isEmpty()) {
				BPerson bp = new BPerson(fromPersonId);
				name = bp.getFirstName() + " " + bp.getLastName();
			}
		}
		return name;
    }

    public BMessage setFromName(String ad) {
        message.setFromAddress(ad);
        return this;
    }

    public char getDontSentBody() {
        return message.getDontSendBody();
    }

    public BMessage setDontSendBody(char c) {
        message.setDontSendBody(c);
        return this;
    }

    public char getSendEmail() {
        return message.getSendEmail();
    }

    public BMessage setSendEmail(char c) {
        message.setSendEmail(c);
        return this;
    }

    public char getSendText() {
        return message.getSendText();
    }

    public BMessage setSendText(char c) {
        message.setSendText(c);
        return this;
    }

    /**
     * @param createdDate
     * @see com.arahant.beans.Message#setCreatedDate(java.util.Date)
     */
    public void setCreatedDate(final Date createdDate) {
        message.setCreatedDate(createdDate);
    }

    /**
     * @param fromShow
     * @see com.arahant.beans.Message#setFromShow(char)
     */
    public void setFromShow(final char fromShow) {
        message.setFromShow(fromShow);
    }

    public void setMessage(final String msg) {
        message.setMessage(msg);
    }

    /**
     * @param messageId
     * @see com.arahant.beans.Message#setMessageId(java.lang.String)
     */
    public void setMessageId(final String messageId) {
        message.setMessageId(messageId);
    }

    /**
     * @param subject
     * @see com.arahant.beans.Message#setSubject(java.lang.String)
     */
    public void setSubject(final String subject) {
        message.setSubject(subject);
    }

    public void setFromPerson(final BPerson bp) {
        message.setPersonByFromPersonId(bp.person);
    }

    public String getFromPersonId() {
        Person fp = message.getPersonByFromPersonId();
        return fp == null ? null : fp.getPersonId();
    }

    public static void delete(final HibernateSessionUtil hsu, final String[] ids, final BPerson p) throws ArahantException {
        final Connection db = KissConnection.get();
        String personId = p.getPersonId();
        for (final String messageId : ids) {
            final BMessage bm = new BMessage(messageId);

            if (bm.getFromPersonId().equals(personId))
                bm.setFromShow('N');
            hsu.saveOrUpdate(bm.message);

            try {
                db.execute("update message_to set to_show='N' where message_id=? and to_person_id=?", messageId, personId);
            } catch (SQLException throwables) {
                throw new ArahantException(throwables);
            }
        }
    }

    public Message getBean() {
        return message;
    }

    public static BMessage[] search(final HibernateSessionUtil hsu, final BPerson p, final IMessageSearchCriteria in, final int max, final boolean includeHandled) {
        final Connection db = hsu.getKissConnection();
        final Command cmd = db.newCommand();
        final ArrayList<Object> args = new ArrayList<>();
        String stmt;

        if (hsu.getCurrentPerson().getPersonId().equals(in.getSenderId())) {
            // sent to me
            stmt = "select m.message_id " +
                    "from message m " +
                    "where m.from_person_id = ? ";
            args.add(hsu.getCurrentPerson().getPersonId());
            if (!includeHandled)
                stmt += " and m.from_show = 'Y' ";

            if (in.getFromDate() > 0) {
                String op = SQLUtils.dateCompareOperator(in.getFromDateIndicator());
                if (op != null) {
                    stmt += "and m.created_date " + op + " ? ";
                    args.add(Connection.toTimestamp(in.getFromDate()));
                }
            }

            if (in.getToDate() > 0) {
                String op = SQLUtils.dateCompareOperator(in.getToDateIndicator());
                if (op != null) {
                    stmt += "and m.created_date " + op + " ? ";
                    args.add(Connection.toTimestamp(in.getToDate()));
                }
            }

            String str = in.getSubject();
            if (str != null && !str.isEmpty()) {
                stmt += "and lower(m.subject) like ? ";
                args.add(str.toLowerCase());
            }

            if (!isEmpty(in.getReceiverId())) {
                stmt += "and ? in (select to_person_id from message_to mt where mt.message_id = m.message_id) ";
                args.add(in.getReceiverId());
            }

            stmt += "order by m.created_date desc, m.message_id";
        } else {
            // received
            stmt = "select m.message_id " +
                    "from message_to mt " +
                    "join message m " +
                    "  on mt.message_id = m.message_id " +
                    "where mt.to_person_id = ? ";
            args.add(hsu.getCurrentPerson().getPersonId());
            if (!includeHandled)
                stmt += " and mt.to_show = 'Y' ";

            if (in.getFromDate() > 0) {
                String op = SQLUtils.dateCompareOperator(in.getFromDateIndicator());
                if (op != null) {
                    stmt += "and mt.date_received " + op + " ? ";
                    args.add(Connection.toTimestamp(in.getFromDate()));
                }
            }

            if (in.getToDate() > 0) {
                String op = SQLUtils.dateCompareOperator(in.getToDateIndicator());
                if (op != null) {
                    stmt += "and mt.date_received " + op + " ? ";
                    args.add(Connection.toTimestamp(in.getToDate()));
                }
            }

            String str = in.getSubject();
            if (str != null && !str.isEmpty()) {
                stmt += "and lower(m.subject) like ? ";
                args.add(str.toLowerCase());
            }

            if (!isEmpty(in.getSenderId())) {
                stmt += "and m.from_person_id = ? ";
                args.add(in.getSenderId());
            }

            stmt += "order by mt.date_received desc, m.message_id";
        }

        try {
            final List<Record> results = cmd.fetchAll(max, stmt, args);
            final BMessage[] ret = new BMessage[results.size()];
            int loop = 0;
            for (Record rec : results) {
                ret[loop++] = new BMessage(rec.getString("message_id"));
            }
            return ret;
        } catch (Exception throwables) {
            throw new ArahantException(throwables);
        }
    }

    /**
     * @param personId
     */
    public void setFromPersonId(final String personId) {
        message.setPersonByFromPersonId(ArahantSession.getHSU().get(Person.class, personId));
    }

    public static void copyMessageToEmail(String fromPersonId, String toPersonId, String message) {
        if (fromPersonId != null && !fromPersonId.isEmpty()) {
            BPerson bp = new BPerson(fromPersonId);
            String subject = ArahantSession.systemName() + " message from " + bp.getPerson().getNameFL();
            String[] toPersonIds = new String[1];
            toPersonIds[0] = toPersonId;
            copyMessageToEmail(fromPersonId, toPersonIds, null, null, subject, message, null);
        } else {
            String subject = ArahantSession.systemName() + " message from the system";
            String[] toPersonIds = new String[1];
            toPersonIds[0] = toPersonId;
            copyMessageToEmail(fromPersonId, toPersonIds, null, null, subject, message, null);
        }
    }

    private static String makeName(String fname, String lname) {
        if (fname == null)
            fname = "";
        if (lname != null && !lname.isEmpty()) {
            if (!fname.isEmpty())
                fname += " ";
            fname += lname;
        }
        return fname;
    }

    private static List<EmailAddress> makeInternetAddresses(Command cmd, String[] ids) {
        List<EmailAddress> tia = new ArrayList<>();
        try {
            if (ids != null)
                for (String toPersonId : ids) {
                    Record rec = cmd.fetchOne("select personal_email, lname, fname, mname from person where person_id=?", toPersonId);
                    if (rec == null)
                        continue;
                    String email = rec.getString("personal_email");
                    if (email == null || email.isEmpty())
                        continue;
                    tia.add(new EmailAddress(email, makeName(rec.getString("fname"), rec.getString("lname"))));
                }
        } catch (Exception e) {
            throw new ArahantException(e);
        }
        return tia;
    }

    /**
     * Sends a message via email if it is supposed to.  And, only sends the message body if it should.
     *
     * @param fromPersonId
     * @param toPersonIds
     * @param ccPersonIds
     * @param bccPersonIds
     * @param subject
     * @param message      text (not HTML)
     * @param attachments
     * @see #copyHTMLToEmail(String, String[], String[], String[], String, String, List)
     */
    public static void copyMessageToEmail(String fromPersonId, String[] toPersonIds, String[] ccPersonIds, String[] bccPersonIds, String subject, String message, List<EmailAttachment> attachments) {
        if (fromPersonId != null && fromPersonId.isEmpty()) {
            BPerson fp = new BPerson(fromPersonId);
            if (!Email.canSendEmail(fp.getPersonalEmail()))
                return;
            try {
                Connection db = ArahantSession.getKissConnection();
                Command cmd = db.newCommand();

                List<EmailAddress> toia = makeInternetAddresses(cmd, toPersonIds);
                List<EmailAddress> ccia = makeInternetAddresses(cmd, ccPersonIds);
                List<EmailAddress> bccia = makeInternetAddresses(cmd, bccPersonIds);
                if (toia.isEmpty() && ccia.isEmpty() && bccia.isEmpty())
                    return;
                SendEmailGeneric em = SendEmailProvider.newEmail();
                em.setTextMessage(message);
                em.setAttachments(attachments);
                em.sendEmail(fp.getPersonalEmail(), fp.getPerson().getNameFL(), toia, ccia, bccia, subject);
                em.close();
                cmd.close();
            } catch (Exception e) {
                throw new ArahantException(e);
            }
        } else {
            String fromEmailAddress = BProperty.get(StandardProperty.DEFAULT_FROM_EMAIL_ADDRESS, "do_not_reply@stack360.io");
            try {
                Connection db = ArahantSession.getKissConnection();
                Command cmd = db.newCommand();

                List<EmailAddress> toia = makeInternetAddresses(cmd, toPersonIds);
                List<EmailAddress> ccia = makeInternetAddresses(cmd, ccPersonIds);
                List<EmailAddress> bccia = makeInternetAddresses(cmd, bccPersonIds);
                if (toia.isEmpty() && ccia.isEmpty() && bccia.isEmpty())
                    return;
                SendEmailGeneric em = SendEmailProvider.newEmail();
                em.setTextMessage(message);
                em.setAttachments(attachments);
                em.sendEmail(fromEmailAddress, null, toia, ccia, bccia, subject);
                em.close();
                cmd.close();
            } catch (Exception e) {
                throw new ArahantException(e);
            }
        }
    }

    /**
     * Sends an HTML message via email if it is supposed to.  And, only sends the messag body if it should.
     *
     * @param fromPersonId
     * @param toPersonIds
     * @param ccPersonIds
     * @param bccPersonIds
     * @param subject
     * @param message      HTML (not text)
     * @param attachments
     * @see #copyMessageToEmail(String, String[], String[], String[], String, String, List)
     */
    public static void copyHTMLToEmail(String fromPersonId, String[] toPersonIds, String[] ccPersonIds, String[] bccPersonIds, String subject, String message, List<EmailAttachment> attachments) {
        if (fromPersonId != null && !fromPersonId.isEmpty()) {
            BPerson fp = new BPerson(fromPersonId);
            if (!Email.canSendEmail(fp.getPersonalEmail()))
                return;
            try {
                Connection db = ArahantSession.getKissConnection();
                Command cmd = db.newCommand();

                List<EmailAddress> toia = makeInternetAddresses(cmd, toPersonIds);
                List<EmailAddress> ccia = makeInternetAddresses(cmd, ccPersonIds);
                List<EmailAddress> bccia = makeInternetAddresses(cmd, bccPersonIds);
                if (toia.isEmpty() && ccia.isEmpty() && bccia.isEmpty())
                    return;
                SendEmailGeneric em = SendEmailProvider.newEmail();
                em.setHTMLMessage(message);
                em.setAttachments(attachments);
                em.sendEmail(fp.getPersonalEmail(), fp.getPerson().getNameFL(), toia, ccia, bccia, subject);
                em.close();
                cmd.close();
            } catch (Exception e) {
                throw new ArahantException(e);
            }
        } else {
            String fromEmailAddress = BProperty.get(StandardProperty.DEFAULT_FROM_EMAIL_ADDRESS, "do_not_reply@stack360.io");
            try {
                Connection db = ArahantSession.getKissConnection();
                Command cmd = db.newCommand();

                List<EmailAddress> toia = makeInternetAddresses(cmd, toPersonIds);
                List<EmailAddress> ccia = makeInternetAddresses(cmd, ccPersonIds);
                List<EmailAddress> bccia = makeInternetAddresses(cmd, bccPersonIds);
                if (toia.isEmpty() && ccia.isEmpty() && bccia.isEmpty())
                    return;
                SendEmailGeneric em = SendEmailProvider.newEmail();
                em.setHTMLMessage(message);
                em.setAttachments(attachments);
                em.sendEmail(fromEmailAddress, null, toia, ccia, bccia, subject);
                em.close();
                cmd.close();
            } catch (Exception e) {
                throw new ArahantException(e);
            }
        }
    }

    public static String send(final Person fromPerson, final Person toPerson, final String subject, final String message) throws ArahantException {
        if (BProperty.getBoolean("DontUseMessageSystem"))
            return "";

        final BMessage m = new BMessage();
        String ret = m.create();
        m.message.setPersonByFromPersonId(fromPerson);
        m.message.setMessage(message);
        m.message.setSubject(subject);
        m.message.setSendInternal('Y');
        m.insert();

        BMessage.createToRecord(m.message.getMessageId(), toPerson.getPersonId());

        if (!subject.contains("Task Failed") && BProperty.getBoolean(StandardProperty.SEND_MESSAGE_NOTIFICATIONS_BY_EMAIL))
            Mail.send(fromPerson, toPerson, subject, BProperty.get("MessageReceivedText", "You have received a message in the " + ArahantSession.systemName() + " system."));
        else if (BProperty.getBoolean(StandardProperty.SEND_MESSAGES_BY_EMAIL))
            Mail.send(fromPerson, toPerson, subject, message);

        copyMessageToEmail(fromPerson != null ? fromPerson.getPersonId() : null, toPerson.getPersonId(), message);

        return ret;
    }

    public static void sendFromAgent(final Person fromPerson, final String[] toPerson, final String subject, final String message) throws ArahantException {
        final Connection db = new Connection(ArahantSession.getHSU().getConnection());
        if (!BProperty.getBoolean("DontUseMessageSystem"))
            if (toPerson.length > 0)
                for (String s : toPerson) {
                    BPerson bp = new BPerson(s);
                    final BMessage m = new BMessage();
                    m.create();
                    m.message.setCreatedDate(new Date());
                    m.message.setFromShow('Y');
                    m.message.setPersonByFromPersonId(fromPerson);
                    m.message.setMessage(message);
                    m.message.setSubject(subject);
                    m.message.setSendInternal('Y');
                    m.insert();

                    final Record rec = db.newRecord("message_to");
                    rec.set("message_to_id", IDGenerator.generate("message_to", "message_to_id"));
                    rec.set("message_id", m.message.getMessageId());
                    rec.set("to_person_id", bp.getPersonId());
                    rec.set("send_type", "T");
                    rec.set("to_show", "Y");
                    rec.set("sent", "Y");
                    try {
                        rec.addRecord();
                    } catch (SQLException throwables) {
                        throw new ArahantException(throwables);
                    }

                    if (!subject.contains("Task Failed") && BProperty.getBoolean(StandardProperty.SEND_MESSAGE_NOTIFICATIONS_BY_EMAIL))
                        Mail.send(fromPerson, bp.getPerson(), subject, BProperty.get("MessageReceivedText", "You have received a message in the " + ArahantSession.systemName() + " system."));
                    else if (BProperty.getBoolean(StandardProperty.SEND_MESSAGES_BY_EMAIL))
                        Mail.send(fromPerson, bp.getPerson(), subject, message);

                }
            else {
                BAgent ba = new BAgent(fromPerson.getPersonId());
                for (BCompany bc : ba.getAgentCompanies()) {
                    BPerson bp = new BPerson(bc.getMainContact().getPerson());
                    final BMessage m = new BMessage();
                    m.create();
                    m.message.setCreatedDate(new Date());
                    m.message.setFromShow('Y');
                    m.message.setPersonByFromPersonId(fromPerson);
                    m.message.setMessage(message);
                    m.message.setSubject(subject);
                    m.message.setSendInternal('Y');
                    m.insert();

                    final Record rec = db.newRecord("message_to");
                    rec.set("message_to_id", IDGenerator.generate("message_to", "message_to_id"));
                    rec.set("message_id", m.message.getMessageId());
                    rec.set("to_person_id", bp.getPersonId());
                    rec.set("send_type", "T");
                    rec.set("to_show", "Y");
                    rec.set("sent", "Y");
                    try {
                        rec.addRecord();
                    } catch (SQLException throwables) {
                        throw new ArahantException(throwables);
                    }

                    if (!subject.contains("Task Failed") && BProperty.getBoolean(StandardProperty.SEND_MESSAGE_NOTIFICATIONS_BY_EMAIL))
                        Mail.send(fromPerson, bp.getPerson(), subject, BProperty.get("MessageReceivedText", "You have received a message in the " + ArahantSession.systemName() + " system."));
                    else if (BProperty.getBoolean(StandardProperty.SEND_MESSAGES_BY_EMAIL))
                        Mail.send(fromPerson, bp.getPerson(), subject, message);

                }
            }
    }

    /**
     * @throws ArahantException
     */
    public static void sendRecent(final String fromPerson, final String toPerson, final String subject, final String message) throws ArahantException {

        try {

            if (BProperty.getBoolean("DontUseMessageSystem"))
                return;

//			Only send if I haven't sent something recently between them with the same subject

            HibernateSessionUtil hsu = ArahantSession.getHSU();

            //trying to avoid breaking the flush - dropping to sql here
            PreparedStatement ps = hsu.getConnection().prepareStatement("select m.* from message m " +
                    "join message_to mt " +
                    "  on m.message_id = mt.message_id " +
                    "where m.from_person_id = ? " +
                    "  and mt.to_person_id=? " +
                    "  and m.subject=? " +
                    "  and m.created_date >= ?");

//			Lets say one every hour is enough...
            Calendar aWhileback = DateUtils.getNow();
            aWhileback.add(Calendar.HOUR_OF_DAY, -1);

            ps.setString(1, fromPerson);
            ps.setString(2, toPerson);
            ps.setString(3, subject);
            ps.setDate(4, new java.sql.Date(aWhileback.getTime().getTime()));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                rs.close();
                ps.close();
                return;
            }

            rs.close();
            ps.close();

            final BMessage m = new BMessage();
            m.create();
            m.message.setCreatedDate(new Date());
            m.message.setFromShow('Y');
            m.message.setPersonByFromPersonId(hsu.get(Person.class, fromPerson));
            m.message.setMessage(message);
            m.message.setSubject(subject);
            m.message.setSendInternal('Y');
            m.insert();

            final Connection db = new Connection(ArahantSession.getHSU().getConnection());
            final Record rec = db.newRecord("message_to");
            rec.set("message_to_id", IDGenerator.generate("message_to", "message_to_id"));
            rec.set("message_id", m.message.getMessageId());
            rec.set("to_person_id", toPerson);
            rec.set("send_type", "T");
            rec.set("to_show", "Y");
            rec.set("sent", "Y");
            rec.addRecord();
        } catch (Exception e) {
            throw new ArahantException(e);
        }
    }

    public static void sendToAccountants(final HibernateSessionUtil hsu, final String subj, final String msg) throws ArahantException {
        //get all accountants
        final List<Person> accts = BRight.getAllPeopleWithRight("NotifyUnpaidTime", ArahantConstants.ACCESS_LEVEL_WRITE);

        for (final Person person : accts)
            send(person, person, subj, msg);
    }

    public static void createToRecord(final String messageId, final String personId) {
        HibernateSessionUtil hsu = ArahantSession.getHSU();

        // make sure the message this record is pointing to has been written to the DB
        //hsu.commitTransaction();    I don't think these are necessary
        //hsu.beginTransaction();

        final Connection db = KissConnection.get();
        final Record rec = db.newRecord("message_to");
        rec.set("message_to_id", IDGenerator.generate("message_to", "message_to_id"));
        rec.set("message_id", messageId);
        rec.set("to_person_id", personId);
        rec.set("send_type", "T");
        rec.set("to_show", "Y");
        rec.set("sent", "Y");
        try {
            rec.addRecord();
        } catch (SQLException throwables) {
            throw new ArahantException(throwables);
        }
    }

    public static void sendToSupervisors(Person person, String subject, String message) {
        for (Person supervisor : getSupervisors(person))
            send(person, supervisor, subject, message);
    }

    /**
     *  Get the "from name" for a message.  If blank, obtains the name from the person record.
     *
     * @param rec A record containing the from_person_id and from_name fields
     * @return
     * @throws SQLException
     */
    public static String getFromName(Record rec) throws SQLException {
		String name = rec.getString("from_name");
		if (name == null || name.isEmpty()) {
			String personId = rec.getString("from_person_id");
			if (personId != null  &&  !personId.isEmpty()) {
				BPerson bp = new BPerson(personId);
				name = bp.getFirstName() + " " + bp.getLastName();
			}
		}
		return name;
	}

    /**
     *  Get the "to name" for a message.  If blank, obtains the name from the person record.
     *
     * @param rec A record containing the to_person_id and to_name fields
     * @return
     * @throws SQLException
     */
    public static String getToName(Record rec) throws SQLException {
		String name = rec.getString("to_name");
		if (name == null || name.isEmpty()) {
			String personId = rec.getString("to_person_id");
			if (personId != null  &&  !personId.isEmpty()) {
				BPerson bp = new BPerson(personId);
				name = bp.getFirstName() + " " + bp.getLastName();
			}
		}
		return name;
	}
}
