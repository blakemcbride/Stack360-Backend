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

package com.arahant.beans;

import javax.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;

@Entity
@Table(name = Message.TABLE_NAME)
public class Message extends ArahantBean implements java.io.Serializable {

    public final static String[] TYPE_IDS = {"Project", "ProjComments"};
    public final static String[] TYPE_DESCRIPTIONS = {"Project Notifications", "Project Comments"};
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "message";
    // Fields
    private String messageId;
    public static final String MESSAGEID = "messageId";
    private Person personByFromPersonId;
    public static final String PERSONBYFROMPERSONID = "personByFromPersonId";
    private String message;
    public static final String MESSAGE = "message";
    private Date createdDate;
    public static final String CREATEDDATE = "createdDate";
    private String subject;
    public static final String SUBJECT = "subject";
    private char fromShow;
    public static final String FROMSHOW = "fromShow";
    private String fromAddress;
    private String fromName;
    private char dontSendBody = 'N';
    private char sendEmail = 'N';
    private char sendText = 'N';
    private char sendInternal = 'N';
    private Set<Timesheet> timesheets = new HashSet<Timesheet>(0);
    public static final String TIMESHEETS = "timesheets";

    // Constructors
    /** default constructor */
    public Message() {
    }

    // Property accessors
    @Id
    @Column(name = "message_id")
    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(final String messageId) {
        this.messageId = messageId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_person_id")
    public Person getPersonByFromPersonId() {
        return this.personByFromPersonId;
    }

    public void setPersonByFromPersonId(final Person personByFromPersonId) {
        this.personByFromPersonId = personByFromPersonId;
    }

    @Column(name = "message")
    public String getMessage() {
        return this.message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

    @Column(name = "subject")
    public String getSubject() {
        return this.subject;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }

    @Column(name = "from_show")
    public char getFromShow() {
        return this.fromShow;
    }

    public void setFromShow(final char fromShow) {
        this.fromShow = fromShow;
    }

    @Column(name = "from_address")
    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    @Column(name = "from_name")
    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    @Column(name = "dont_send_body")
    public char getDontSendBody() {
        return dontSendBody;
    }

    public void setDontSendBody(char dontSendBody) {
        this.dontSendBody = dontSendBody;
    }

    @Column(name = "send_email")
    public char getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(char sendEmail) {
        this.sendEmail = sendEmail;
    }

    @Column(name = "send_text")
    public char getSendText() {
        return sendText;
    }

    public void setSendText(char sendText) {
        this.sendText = sendText;
    }

    @Column(name = "send_internal")
    public char getSendInternal() {
        return sendInternal;
    }

    public void setSendInternal(char sendInternal) {
        this.sendInternal = sendInternal;
    }

    @OneToMany(mappedBy = Timesheet.MESSAGE, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<Timesheet> getTimesheets() {
        return this.timesheets;
    }

    public void setTimesheets(final Set<Timesheet> timesheets) {
        this.timesheets = timesheets;
    }

    @Override
    public String keyColumn() {
        return "message_id";
    }

    @Override
    public String tableName() {
        return TABLE_NAME;
    }

    @Override
    public String generateId() throws ArahantException {
        setMessageId(IDGenerator.generate(this));
        return messageId;
    }

    @Override
    public boolean equals(Object o) {
        if (messageId == null && o == null) {
            return true;
        }
        if (messageId != null && o instanceof Message) {
            return messageId.equals(((Message) o).getMessageId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        if (messageId == null) {
            return 0;
        }
        return messageId.hashCode();
    }
}
