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

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = CompanyBase.TABLE_NAME)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CompanyBase extends OrgGroup implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
    public static final String TABLE_NAME = "company_base";
    public static final String ARAHANT_URL = "arahantURL";
    // Fields
    public static final String IDENTIFIER = EXTERNAL_REF;
    private String federalEmployerId;
    public static final String FEDERALEMPLOYERID = "federalEmployerId";
    private Set<Person> persons = new HashSet<Person>(0);
    public static final String PERSONS = "persons";
    private Set<OrgGroup> orgGroups = new HashSet<OrgGroup>(0);
    public static final String ORGGROUPS = "orgGroups";
    private Set<Invoice> invoices = new HashSet<Invoice>(0);
    public static final String INVOICES = "invoices";
	private String daysToSend = "NNNNNNN";
	public static final String DAYSTOSEND = "daysToSend";
	private short timeToSend;
	public static final String TIMETOSEND = "timeToSend";
	private char ediActivated = 'N';
	public static final String EDIACTIVATED = "ediActivated";
    private Set<CompanyQuestionDetail> questionDetails = new HashSet<CompanyQuestionDetail>();
    private String publicEncryptionKey;
    private String applicationSenderId;
    private String applicationReceiverId;
    private String interchangeSenderId;
    private String interchangeReceiverId;
    private String comUrl;
	public static final String COM_URL = "comUrl";
    private String comPassword;
    private String comDirectory;
    private String encryptionKeyId;
    private String arahantURL;
    private String billToName;
    private Set<EDITransaction> ediTransactions = new HashSet<EDITransaction>();
    private Set<Appointment> appointments = new HashSet<Appointment>(0);
    private char ediFileType = 'U';
    private char ediFileStatus = 'U';

	@Column(name="edi_file_status")
	public char getEdiFileStatus() {
		return ediFileStatus;
	}

	public void setEdiFileStatus(char ediFileStatus) {
		this.ediFileStatus = ediFileStatus;
	}

	@Column(name="edi_file_type")
	public char getEdiFileType() {
		return ediFileType;
	}

	public void setEdiFileType(char ediFileType) {
		this.ediFileType = ediFileType;
	}
    @OneToMany(mappedBy = Appointment.COMPANY, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<Appointment> getAppointments() {
        return appointments;
    }

    @Transient
    public String getIdentifier() {
        return getExternalId();
    }

    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
    }

    @OneToMany(mappedBy = EDITransaction.COMPANY, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<EDITransaction> getEdiTransactions() {
        return ediTransactions;
    }

    public void setEdiTransactions(Set<EDITransaction> ediTransactions) {
        this.ediTransactions = ediTransactions;
    }

    @Column(name = "application_receiver_id")
    public String getApplicationReceiverId() {
        return applicationReceiverId;
    }

    public void setApplicationReceiverId(String applicationReceiverId) {
        this.applicationReceiverId = applicationReceiverId;
    }

    @Column(name = "application_sender_id")
    public String getApplicationSenderId() {
        return applicationSenderId;
    }

    public void setApplicationSenderId(String applicationSenderId) {
        this.applicationSenderId = applicationSenderId;
    }

    @Column(name = "com_directory")
    public String getComDirectory() {
        return comDirectory;
    }

    public void setComDirectory(String comDirectory) {
        this.comDirectory = comDirectory;
    }

    @Column(name = "com_password")
    public String getComPassword() {
        return comPassword;
    }

    public void setComPassword(String comPassword) {
        this.comPassword = comPassword;
    }

    @Column(name = "com_url")
    public String getComUrl() {
        return comUrl;
    }

    public void setComUrl(String comUrl) {
        this.comUrl = comUrl;
    }

    @Column(name = "encryption_key_id")
    public String getEncryptionKeyId() {
        return encryptionKeyId;
    }

    public void setEncryptionKeyId(String encryptionKeyId) {
        this.encryptionKeyId = encryptionKeyId;
    }

    @Column(name = "interchange_receiver_id")
    public String getInterchangeReceiverId() {
        return interchangeReceiverId;
    }

    public void setInterchangeReceiverId(String interchangeReceiverId) {
        this.interchangeReceiverId = interchangeReceiverId;
    }

    @Column(name = "interchange_sender_id")
    public String getInterchangeSenderId() {
        return interchangeSenderId;
    }

    public void setInterchangeSenderId(String interchangeSenderId) {
        this.interchangeSenderId = interchangeSenderId;
    }

    // Constructors
    /** default constructor */
    public CompanyBase() {
        super();
    }

    /**
     * @return Returns the federalEmployerId.
     */
    @Column(name = "federal_employer_id")
    public String getFederalEmployerId() {
        return federalEmployerId;
    }

    /**
     * @param federalEmployerId The federalEmployerId to set.
     */
    public void setFederalEmployerId(String federalEmployerId) {
        firePropertyChange("federalEmployerId", this.federalEmployerId,
                federalEmployerId);
        this.federalEmployerId = federalEmployerId;
    }

    /**
     * @return Returns the invoices.
     */
    @OneToMany(mappedBy = Invoice.COMPANYBASE, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<Invoice> getInvoices() {
        return invoices;
    }

    /**
     * @param invoices The invoices to set.
     */
    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
    }

    /**
     * @return Returns the orgGroups.
     */
    @OneToMany(mappedBy = OrgGroup.OWNINGCOMPANY, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<OrgGroup> getOrgGroups() {
        return orgGroups;
    }

    /**
     * @param orgGroups The orgGroups to set.
     */
    public void setOrgGroups(Set<OrgGroup> orgGroups) {
        this.orgGroups = orgGroups;
    }

    /**
     * @return Returns the persons.
     */
    @OneToMany(mappedBy = Person.COMPANYBASE, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<Person> getPersons() {
        return persons;
    }

    /**
     * @param persons The persons to set.
     */
    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }

    @Column(name = "public_encryption_key")
    public String getPublicEncryptionKey() {
        return publicEncryptionKey;
    }

    public void setPublicEncryptionKey(String publicEncryptionKey) {
        this.publicEncryptionKey = publicEncryptionKey;
    }

    @OneToMany(mappedBy = CompanyQuestionDetail.COMPANY, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<CompanyQuestionDetail> getQuestionDetails() {
        return questionDetails;
    }

    public void setQuestionDetails(Set<CompanyQuestionDetail> questionDetails) {
        this.questionDetails = questionDetails;
    }

    @Column(name = "arahant_url")
    public String getArahantURL() {
        return arahantURL;
    }

    public void setArahantURL(String arahantURL) {
        this.arahantURL = arahantURL;
    }


    /*
     * needed for db mapping
     */
    @Column(name = "org_group_type")
    public int getOgt() {
        return getOrgGroupType();
    }

    public void setOgt(int ogt) {
        setOrgGroupType(ogt);
    }

    /**
     * @return the clientDisplayName
     */
    @Column(name="bill_to_name")

    /**
     * @return the billToName
     */
    public String getBillToName() {
        return billToName;
    }

    /**
     * @param billToName the billToName to set
     */
    public void setBillToName(String billToName) {
        this.billToName = billToName;
    }
	
	@Column(name="days_to_send")
	public String getDaysToSend() {
		return daysToSend;
	}

	public void setDaysToSend(String daysToSend) {
		this.daysToSend = daysToSend;
	}

	@Column(name="edi_activated")
	public char getEdiActivated() {
		return ediActivated;
	}

	public void setEdiActivated(char ediActivated) {
		this.ediActivated = ediActivated;
	}

	@Column(name="time_to_send")
	public short getTimeToSend() {
		return timeToSend;
	}

	public void setTimeToSend(short timeToSend) {
		this.timeToSend = timeToSend;
	}

}
