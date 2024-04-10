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
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.Crypto;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 *
 *
 */
@Entity
@Table(name = "hr_employee_beneficiary")
public class HrBeneficiary extends AuditedBean implements Serializable {
	
	public static final String TABLE_NAME = "hr_employee_beneficiary";

	private static final transient ArahantLogger logger = new ArahantLogger(HrBeneficiary.class);
	public static final char PRIMARY = 'P';
	public static final char CONTINGENT = 'C';
	private static final long serialVersionUID = 1998526087588590421L;
	public static final String EMPL_BENEFIT_JOIN = "benefitJoin";
	public static final String BENEFICIARY_TYPE = "beneficiaryType";
	public static final String ID = "beneficiaryId";
	public static final String SSN = "ssn";
	public static final String DOB = "dob";
	public static final String NAME = "beneficiary";
	private String beneficiaryId, beneficiary, relationship;
	public static final String BENEFIT_JOIN_ID = "benefitJoinId";
	private short benefitPercent;
	private String benefitJoinId;
	private HrBenefitJoin benefitJoin;
	private int dob;
	private String ssn;
	private String address;
	private char beneficiaryType;

	public HrBeneficiary() {
	}

	/**
	 * @return Returns the address.
	 */
	@Column(name = "address")
	public String getAddress() {
		if (address == null)
			address = "";
		return address;
	}

	/**
	 * @param address The address to set.
	 */
	public void setAddress(String address) {
		firePropertyChange("address", this.address, address);
		this.address = address;
	}

	/**
	 * @return Returns the beneficiaryType.
	 */
	@Column(name = "beneficiary_type")
	public char getBeneficiaryType() {
		return beneficiaryType;
	}

	/**
	 * @param beneficiaryType The beneficiaryType to set.
	 */
	public void setBeneficiaryType(char beneficiaryType) {
		firePropertyChange("beneficiaryType", this.beneficiaryType, beneficiaryType);
		this.beneficiaryType = beneficiaryType;
	}

	/**
	 * @return Returns the dob.
	 */
	@Column(name = "dob")
	public int getDob() {
		return dob;
	}

	/**
	 * @param dob The dob to set.
	 */
	public void setDob(int dob) {
		firePropertyChange("dob", this.dob, dob);
		this.dob = dob;
	}

	@Column(name = "ssn")
	public String getSsn() {
		if (ssn != null && ssn.length() > 11)
			try {
				return Crypto.decryptTripleDES(Person.encKey(), ssn);
			} catch (Exception e) {
				return ssn;
			}
		if (ssn == null)
			ssn = "";
		return ssn;
	}

	/**
	 * @param ssn The ssn to set.
	 */
	public void setSsn(String ssn) {
		String oldSSN = getSsn();
		firePropertyChange("ssn", oldSSN, ssn);
		if (ssn == null || ssn.length() == 0)
			this.ssn = ssn;
		else
			try {
				this.ssn = Crypto.encryptTripleDES(Person.encKey(), ssn);
			} catch (Exception e) {
				this.ssn = "";
				logger.error("error encrypting an SSN", e);
			}
	}

	/* (non-Javadoc)
	 * @see com.arahant.beans.ArahantBean#generateId()
	 */
	@Override
	public String generateId() throws ArahantException {
		beneficiaryId = IDGenerator.generate(this);
		return beneficiaryId;
	}

	/* (non-Javadoc)
	 * @see com.arahant.beans.ArahantBean#keyColumn()
	 */
	@Override
	public String keyColumn() {
		return "beneficiary_id";
	}

	/* (non-Javadoc)
	 * @see com.arahant.beans.ArahantBean#tableName()
	 */
	@Override
	public String tableName() {
		return "hr_employee_beneficiary";
	}

	/**
	 * @return Returns the beneficiary.
	 */
	@Column(name = "beneficiary")
	public String getBeneficiary() {
		if (beneficiary == null)
			beneficiary = "";
		return beneficiary;
	}

	/**
	 * @param beneficiary The beneficiary to set.
	 */
	public void setBeneficiary(String beneficiary) {
		firePropertyChange("beneficiary", this.beneficiary, beneficiary);
		this.beneficiary = beneficiary;
	}

	/**
	 * @return Returns the beneficiaryId.
	 */
	@Id
	@Column(name = "beneficiary_id")
	public String getBeneficiaryId() {
		return beneficiaryId;
	}

	/**
	 * @param beneficiaryId The beneficiaryId to set.
	 */
	public void setBeneficiaryId(String beneficiaryId) {
		firePropertyChange("beneficiaryId", this.beneficiaryId, beneficiaryId);
		this.beneficiaryId = beneficiaryId;
	}

	/**
	 * @return Returns the benefitJoin.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "benefit_join_id")
	public HrBenefitJoin getBenefitJoin() {
		return benefitJoin;
	}

	/**
	 * @param benefitJoin The benefitJoin to set.
	 */
	public void setBenefitJoin(HrBenefitJoin benefitJoin) {
		this.benefitJoin = benefitJoin;
	}

	/**
	 * @return Returns the benefitPercent.
	 */
	@Column(name = "benefit_percent")
	public short getBenefitPercent() {
		return benefitPercent;
	}

	/**
	 * @param benefitPercent The benefitPercent to set.
	 */
	public void setBenefitPercent(short benefitPercent) {
		firePropertyChange("benefitPercent", this.benefitPercent, benefitPercent);
		this.benefitPercent = benefitPercent;
	}

	/**
	 * @return Returns the relationship.
	 */
	@Column(name = "relationship")
	public String getRelationship() {
		if (relationship == null)
			relationship = "";
		return relationship;
	}

	/**
	 * @param relationship The relationship to set.
	 */
	public void setRelationship(String relationship) {
		firePropertyChange("relationship", this.relationship, relationship);
		this.relationship = relationship;
	}

	/* (non-Javadoc)
	 * @see com.arahant.beans.AuditedBean#historyObject()
	 */
	@Override
	public ArahantHistoryBean historyObject() {

		HrEmployeeBeneficiaryH b = new HrEmployeeBeneficiaryH();
		b.setBenefitJoinId(benefitJoin.getBenefitJoinId());
		return b;
	}

	/**
	 * @return Returns the benefitJoinId.
	 */
	@Column(name = "benefit_join_id", insertable = false, updatable = false)
	public String getBenefitJoinId() {
		return benefitJoinId;
	}

	/**
	 * @param benefitJoinId The benefitJoinId to set.
	 */
	public void setBenefitJoinId(String benefitJoinId) {
		//	firePropertyChange("benefitJoinId", this.benefitJoinId,
		//			benefitJoinId);
		this.benefitJoinId = benefitJoinId;
	}

	@Override
	public boolean equals(Object o) {
		if (beneficiaryId == null && o == null)
			return true;
		if (beneficiaryId != null && o instanceof HrBeneficiary)
			return beneficiaryId.equals(((HrBeneficiary) o).getBeneficiaryId());

		return false;
	}

	@Override
	public int hashCode() {
		if (beneficiaryId == null)
			return 0;
		return beneficiaryId.hashCode();
	}

	@Column(name = "record_change_type")
	@Override
	public char getRecordChangeType() {
		return recordChangeType;
	}

	@Column(name = "record_person_id")
	@Override
	public String getRecordPersonId() {
		if (recordPersonId == null)
			if (ArahantSession.getHSU().getCurrentPerson() != null)
				recordPersonId = ArahantSession.getHSU().getCurrentPerson().getPersonId();
			else
				recordPersonId = ArahantSession.getHSU().getArahantPersonId();
		return recordPersonId;
	}

	@Column(name = "record_change_date")
	@Temporal(TemporalType.TIMESTAMP)
	@Override
	public Date getRecordChangeDate() {
		return recordChangeDate;
	}

	@Override
	public String keyValue() {
		return getBeneficiaryId();
	}
}
