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

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.sql.Timestamp;
import javax.persistence.*;


@Entity
@Table(name="e_signature")
public class ESignature extends ArahantBean implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "e_signature";
	public static final String ESIGNATURE_ID = "eSignatureId";
	private String eSignatureId;
	public static final String PERSON = "person";
	private Person person;
	public static final String PERSON_ID = "personId";
	private String personId;
	public static final String TIME_SIGNED = "timeSigned";
	private Timestamp timeSigned;
	public static final String ADDRESS_IP = "addressIP";
	private String addressIP;
	public static final String FORM_TYPE = "formType";
	private String formType;
	public static final String XML_SUM = "xmlSum";
	private String xmlSum;
	public static final String XML_DATA = "xmlData";
	private String xmlData;
	public static final String FORM_DATA = "formData";
	private byte [] formData;
	public static final String FORM_SUM = "formSum";
	private String formSum;
	public static final String SIGNATURE = "signature";
	private String signature;
	public static final String SIGNATURE_DATE = "sigDate";
	private String sigDate;

	//Default constructor
	public ESignature() {}

	@Column(name = "address_ip")
	public String getAddressIP() {
		return addressIP;
	}

	public void setAddressIP(String addressIP) {
		this.addressIP = addressIP;
	}

	@Id
	@Column(name = "e_signature_id")
	public String getESignatureId() {
		return eSignatureId;
	}

	public void setESignatureId(String eSignatureId) {
		this.eSignatureId = eSignatureId;
	}

	@Column(name = "xml_sum")
	public String getXmlSum() {
		return xmlSum;
	}

	public void setXmlSum(String xmlSum) {
		this.xmlSum = xmlSum;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id")
	public Person getPerson() {
		return person;
	}

	@Column(name = "person_id", updatable = false, insertable = false)
	public String getPersonId() {
		return personId;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	@Column(name = "form_type")
	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	@Column(name = "time_signed")
	public Timestamp getTimeSigned() {
		return timeSigned;
	}

	public void setTimeSigned(Timestamp timeSigned) {
		this.timeSigned = timeSigned;
	}

	@Column(name = "xml_data")
	public String getXmlData() {
		return xmlData;
	}

	public void setXmlData(String xmlData) {
		this.xmlData = xmlData;
	}

	@Column(name = "form_data")
	public byte[] getFormData() {
		return formData;
	}

	public void setFormData(byte[] formData) {
		this.formData = formData;
	}

	@Column(name = "form_sum")
	public String getFormSum() {
		return formSum;
	}

	public void setFormSum(String formSum) {
		this.formSum = formSum;
	}

	@Column(name = "signature")
	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	@Column(name = "sig_date")
	public String getSigDate() {
		return sigDate;
	}

	public void setSigDate(String sigDate) {
		this.sigDate = sigDate;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "e_signature_id";
	}

	@Override
	public String generateId() throws ArahantException {
		setESignatureId(IDGenerator.generate(this));
        return eSignatureId;
	}

	@Override
    public ESignature clone() {
        ESignature es = new ESignature();
		es.generateId();
		es.setAddressIP(addressIP);
		es.setXmlSum(xmlSum);
		es.setPerson(person);
		es.setFormType(formType);
		es.setTimeSigned(timeSigned);
		es.setXmlData(xmlData);
		es.setFormData(formData);
		es.setFormSum(formSum);
		es.setSignature(signature);
		es.setSigDate(sigDate);
        return es;
    }

	@Override
    public boolean equals(Object o) {
        if (eSignatureId == null && o == null)
            return true;
        if (eSignatureId != null && o instanceof ESignature)
            return eSignatureId.equals(((ESignature) o).getESignatureId());

        return false;
    }
	
	@Override
    public int hashCode() {
        if (eSignatureId == null)
            return 0;
        return eSignatureId.hashCode();
    }
}
