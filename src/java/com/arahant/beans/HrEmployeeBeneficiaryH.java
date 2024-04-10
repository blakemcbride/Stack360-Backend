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

import com.arahant.utils.ArahantLogger;
import com.arahant.utils.Crypto;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 
 *
 * Created on Oct 9, 2007
 *
 */
@Entity
@Table(name="hr_employee_beneficiary_h")
public class HrEmployeeBeneficiaryH extends ArahantHistoryBean implements Serializable, Comparable<HrEmployeeBeneficiaryH> {
	
	public static final String TABLE_NAME = "hr_employee_beneficiary_h";
	
	private static final transient ArahantLogger logger = new ArahantLogger(HrEmployeeBeneficiaryH.class);
	private String beneficiaryId,beneficiary, relationship;
	private short benefitPercent;

	private String benefitJoinId;
	private int dob;
	private String ssn;
	private String address;
	private char beneficiaryType;
	
	private static final long serialVersionUID = -8571952159068260720L;
	public static final String BENEFIT_JOIN_ID = "benefitJoinId";

	public HrEmployeeBeneficiaryH() {
	}	

	@Id
	@Override
	public String getHistory_id() {
		return history_id;
	}
	/**
	 * @return Returns the recordChangeType.
	 */
	@Column (name="record_change_type")
	@Override
	public char getRecordChangeType() {
		return recordChangeType;
	}

	@Column (name="record_person_id")
	@Override
	public String getRecordPersonId() {
		return recordPersonId;
	}

	@Column (name="record_change_date")
	@Temporal(TemporalType.TIMESTAMP)
	@Override
	public Date getRecordChangeDate() {
		return recordChangeDate;
	}

	/* (non-Javadoc)
	 * @see com.arahant.beans.ArahantBean#keyColumn()
	 */
	@Override
	public String keyColumn() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.arahant.beans.ArahantBean#tableName()
	 */
	@Override
	public String tableName() {
		return null;
	}

	/**
	 * @return Returns the beneficiary.
	 */
	@Column (name="beneficiary")
	public String getBeneficiary() {
		return beneficiary;
	}

	/**
	 * @param beneficiary The beneficiary to set.
	 */
	public void setBeneficiary(final String beneficiary) {
		this.beneficiary = beneficiary;
	}

	/**
	 * @return Returns the beneficiaryId.
	 */
	@Column (name="beneficiary_id")
	public String getBeneficiaryId() {
		return beneficiaryId;
	}

	/**
	 * @param beneficiaryId The beneficiaryId to set.
	 */
	public void setBeneficiaryId(final String beneficiaryId) {
		this.beneficiaryId = beneficiaryId;
	}

	/**
	 * @return Returns the benefitPercent.
	 */
	@Column (name="benefit_percent")
	public short getBenefitPercent() {
		return benefitPercent;
	}

	/**
	 * @param benefitPercent The benefitPercent to set.
	 */
	public void setBenefitPercent(final short benefitPercent) {
		this.benefitPercent = benefitPercent;
	}

	/**
	 * @return Returns the relationship.
	 */
	@Column (name="relationship")
	public String getRelationship() {
		return relationship;
	}

	/**
	 * @param relationship The relationship to set.
	 */
	public void setRelationship(final String relationship) {
		this.relationship = relationship;
	}

	/**
	 * @return Returns the benefitJoinId.
	 */
	@Column (name="benefit_join_id")
	public String getBenefitJoinId() {
		return benefitJoinId;
	}

	/**
	 * @param benefitJoinId The benefitJoinId to set.
	 */
	public void setBenefitJoinId(String benefitJoinId) {
		if (benefitJoinId==null)
			return;
		firePropertyChange("benefitJoinId", this.benefitJoinId,
				benefitJoinId);
		this.benefitJoinId = benefitJoinId;
	}

	@Column (name="address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		firePropertyChange("address", this.address, address);
		this.address = address;
	}

	@Column (name="beneficiary_type")
	public char getBeneficiaryType() {
		return beneficiaryType;
	}

	public void setBeneficiaryType(char beneficiaryType) {
		firePropertyChange("beneficiaryType", this.beneficiaryType, beneficiaryType);
		this.beneficiaryType = beneficiaryType;
	}

	@Column (name="dob")
	public int getDob() {
		return dob;
	}

	public void setDob(int dob) {
		firePropertyChange("dob", this.dob, dob);
		this.dob = dob;
	}

    @Column(name = "ssn")
    public String getSsn() {
		if (ssn != null  &&  ssn.length() > 11) {
			try {
				return Crypto.decryptTripleDES(Person.encKey(), ssn);
			} catch (Exception e) {
				return ssn;
			}
		}
        return ssn;
    }

    public void setSsn(String ssn) {
		String oldSSN = getSsn();
        firePropertyChange("ssn", oldSSN, ssn);
		if (ssn == null  ||  ssn.length() == 0)
			this.ssn = ssn;
		else {
			try {
				this.ssn = Crypto.encryptTripleDES(Person.encKey(), ssn);
			} catch (Exception e) {
				this.ssn = null;
				logger.error("error encrypting an SSN", e);
			}
		}
    }

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(HrEmployeeBeneficiaryH o) {
		
		return o.getRecordChangeDate().compareTo(getRecordChangeDate());
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (beneficiaryId==null && o==null)
			return true;
		if (beneficiaryId!=null && o instanceof HrEmployeeBeneficiaryH)
			return beneficiaryId.equals(((HrEmployeeBeneficiaryH)o).getBeneficiaryId());
		return false;
	}
	
	@Override
	public int hashCode()
	{
		if (beneficiaryId==null)
			return 0;
		return beneficiaryId.hashCode();
	}

	/* (non-Javadoc)
	 * @see com.arahant.beans.ArahantHistoryBean#alreadyThere()
	 */
	@Override
	public boolean alreadyThere() {
		return false;
	}

	
}

	
