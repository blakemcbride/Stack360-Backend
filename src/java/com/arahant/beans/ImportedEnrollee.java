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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 */
@Entity
@Table(name=ImportedEnrollee.TABLE_NAME)
public class ImportedEnrollee extends AuditedBean implements Serializable, CompanyFiltered {
	public static final String TABLE_NAME="drc_import_enrollee";
	public static final String BENEFIT_JOINS="benefitJoins";
	public static final String PROVIDED_JOINS="providedJoins";
	public static final String IMPORT_FILE_TYPE="importFileType";
	
	public static final String ID="enrolleeId";
	public static final String LNAME="lname";
	public static final String FNAME="fname";
	public static final String MNAME="mname";
	public static final String RELATIONSHIP="relationship";
	public static final String DOB="dob";
	public static final String SSN="ssn";
		
	private String enrolleeId;
	private String lname;
	private String fname;
	private String mname;
	private String relationship;
	private int dob;
	private String ssn;
	private String city;
	private String street1;
	private String street2;
	private String state;
	private String zip;
	private OrgGroup orgGroup;
	private ImportType importFileType;

	private Set<ImportBenefitJoin> benefitJoins=new HashSet<ImportBenefitJoin>();
	private Set<ImportBenefitJoin> providedJoins=new HashSet<ImportBenefitJoin>();

	@OneToMany
	@JoinColumn(name="enrollee_id")
	public Set<ImportBenefitJoin> getBenefitJoins() {
		return benefitJoins;
	}

	public void setBenefitJoins(Set<ImportBenefitJoin> benefitJoins) {
		this.benefitJoins = benefitJoins;
	}

	@OneToMany
	@JoinColumn(name="subscriber_id")
	public Set<ImportBenefitJoin> getProvidedJoins() {
		return providedJoins;
	}

	public void setProvidedJoins(Set<ImportBenefitJoin> providedJoins) {
		this.providedJoins = providedJoins;
	}


	@ManyToOne
	@JoinColumn(name="import_file_type_id")
	public ImportType getImportFileType() {
		return importFileType;
	}

	public void setImportFileType(ImportType importFileType) {
		this.importFileType = importFileType;
	}


	@Column(name="city")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name="state")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name="street1")
	public String getStreet1() {
		return street1;
	}

	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	@Column(name="street2")
	public String getStreet2() {
		return street2;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	@Column(name="zip")
	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	@Column(name="dob")
	public int getDob() {
		return dob;
	}

	public void setDob(int dob) {
		this.dob = dob;
	}

	@Id
	@Column(name="enrollee_id")
	public String getEnrolleeId() {
		return enrolleeId;
	}

	public void setEnrolleeId(String enrolleeId) {
		this.enrolleeId = enrolleeId;
	}

	@Column(name="fname")
	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	@Column(name="lname")
	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	@Column(name="mname")
	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	@Column(name="relationship")
	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	@Column(name="ssn")
	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "enrollee_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return enrolleeId=IDGenerator.generate(this);
	}

	@Override
	@ManyToOne
	@JoinColumn(name="company_id")
	public OrgGroup getOrgGroup() {
		return orgGroup;
	}

	@Override
	public void setOrgGroup(OrgGroup orgGroup) {
		this.orgGroup = orgGroup;
	}

	@Override
	public ArahantHistoryBean historyObject() {
		return new ImportedEnrolleeH();
	}

	@Override
	@Column(name="record_change_date")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getRecordChangeDate() {
		return recordChangeDate;
	}

	@Override
	@Column(name="record_person_id")
	public String getRecordPersonId() {
		return recordPersonId;
	}

	@Override
	@Column(name="record_change_type")
	public char getRecordChangeType() {
		return recordChangeType;
	}

	public static void main(String []args)
	{
		ArahantSession.getHSU().createCriteria(ImportedEnrollee.class).list();
	}

	@Override
	public String keyValue() {
		return getEnrolleeId();
	}

}
