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
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = QuoteTable.TABLE_NAME)
public class QuoteTable extends ArahantBean implements java.io.Serializable {

	public static final String TABLE_NAME = "quote_table";
	public static final String ID = "quoteId";
	public static final String CLIENT = "client";
	public static final String LOCATION_COST = "locationCost";
	public static final String NAME = "quoteName";
	public static final String DESCRIPTION = "quoteDescription";
	public static final String CREATED_DATE = "createdDate";
	public static final String CREATED_BY_PERSON = "createdByPerson";
	public static final String FINALIZED_DATE = "finalizedDate";
	public static final String FINALIZED_BY_PERSON = "finalizedByPerson";
	public static final String ACCEPTED_DATE = "acceptedDate";
	public static final String ACCEPTED_PERSON = "acceptedPerson";
	public static final String ACCEPTED_BY_CLIENT = "acceptedByClient";
	public static final String MARKUP_PERCENT = "markupPercent";
	public static final String ADDITIONAL_COST = "additionalCost";
	private static final long serialVersionUID = 1L;
	private String quoteId;
	private OrgGroup client;
	private LocationCost locationCost;
	private String quoteName;
	private String quoteDescription;
	private Date createdDate;
	private Person createdByPerson;
	private Date finalizedDate;
	private Person finalizedByPerson;
	private Date acceptedDate;
	private Person acceptedPerson;
	private String acceptedByClient;
	private double markupPercent;
	private double additionalCost;

	public QuoteTable() {}

	@Column(name = "markup_percent")
	public double getMarkupPercent() {
		return markupPercent;
	}

	public void setMarkupPercent(double markupPercent) {
		this.markupPercent = markupPercent;
	}

	@Column(name = "additional_cost")
	public double getAdditionalCost() {
		return additionalCost;
	}

	public void setAdditionalCost(double additionalCost) {
		this.additionalCost = additionalCost;
	}

	@Column(name = "accepted_by_client")
	public String getAcceptedByClient() {
		return acceptedByClient;
	}

	public void setAcceptedByClient(String acceptedByClient) {
		this.acceptedByClient = acceptedByClient;
	}

	@Column(name = "accepted_date")
	@Temporal(value = javax.persistence.TemporalType.DATE)
	public Date getAcceptedDate() {
		return acceptedDate;
	}

	public void setAcceptedDate(Date acceptedDate) {
		this.acceptedDate = acceptedDate;
	}

	@ManyToOne
	@JoinColumn(name = "accepted_person")
	public Person getAcceptedPerson() {
		return acceptedPerson;
	}

	public void setAcceptedPerson(Person acceptedPerson) {
		this.acceptedPerson = acceptedPerson;
	}

	@ManyToOne
	@JoinColumn(name = "client_id")
	public OrgGroup getClient() {
		return client;
	}

	public void setClient(OrgGroup client) {
		this.client = client;
	}

	@ManyToOne
	@JoinColumn(name = "created_by_person")
	public Person getCreatedByPerson() {
		return createdByPerson;
	}

	public void setCreatedByPerson(Person createdByPerson) {
		this.createdByPerson = createdByPerson;
	}

	@Column(name = "created_date")
	@Temporal(value = javax.persistence.TemporalType.DATE)
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@ManyToOne
	@JoinColumn(name = "finalized_by_person")
	public Person getFinalizedByPerson() {
		return finalizedByPerson;
	}

	public void setFinalizedByPerson(Person finalizedByPerson) {
		this.finalizedByPerson = finalizedByPerson;
	}

	@Column(name = "finalized_date")
	@Temporal(value = javax.persistence.TemporalType.DATE)
	public Date getFinalizedDate() {
		return finalizedDate;
	}

	public void setFinalizedDate(Date finalizedDate) {
		this.finalizedDate = finalizedDate;
	}

	@ManyToOne
	@JoinColumn(name = "location_cost_id")
	public LocationCost getLocationCost() {
		return locationCost;
	}

	public void setLocationCost(LocationCost locationCost) {
		this.locationCost = locationCost;
	}

	@Column(name = "quote_description")
	public String getQuoteDescription() {
		return quoteDescription;
	}

	public void setQuoteDescription(String quoteDescription) {
		this.quoteDescription = quoteDescription;
	}

	@Id
	@Column(name = "quote_id")
	public String getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}

	@Column(name = "quote_name")
	public String getQuoteName() {
		return quoteName;
	}

	public void setQuoteName(String quoteName) {
		this.quoteName = quoteName;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "quote_id";
	}

	@Override
	public String generateId() throws ArahantException {
		setQuoteId(IDGenerator.generate(this));
		return getQuoteId();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final QuoteTable other = (QuoteTable) obj;
		if ((this.quoteId == null) ? (other.quoteId != null) : !this.quoteId.equals(other.quoteId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 79 * hash + (this.quoteId != null ? this.quoteId.hashCode() : 0);
		return hash;
	}
}
