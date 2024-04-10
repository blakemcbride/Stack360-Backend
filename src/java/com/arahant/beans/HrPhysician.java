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
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "physician")
public class HrPhysician extends ArahantBean implements Serializable {

	public static final String TABLE_NAME = "physician";
	private static final long serialVersionUID = 1L;
	public static final String EMPL_BENEFIT_JOIN = "benefitJoin";
	public static final String BENEFIT_JOIN_ID = "benefitJoinId";
	public static final String ID = "physicianId";
	public static final String ADDRESS = "address";
	public static final String CHANGE_REASON = "changeReason";
	public static final String CODE = "physicianCode";
	public static final String NAME = "physicianName";
	private String physicianId;
	private String physicianName;
	private String physicianCode;
	private String address;
	private String changeReason;
	private int changeDate;
	private char annualVisit = 'N';
	private HrBenefitJoin benefitJoin;
	private String benefitJoinId;

	public HrPhysician() {
	}

	@Column(name = "annual_visit")
	public char getAnnualVisit() {
		return annualVisit;
	}

	public void setAnnualVisit(char annualVisit) {
		this.annualVisit = annualVisit;
	}

	@Column(name = "change_date")
	public int getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(int changeDate) {
		this.changeDate = changeDate;
	}

	@Column(name = "change_reason")
	public String getChangeReason() {
		return changeReason;
	}

	public void setChangeReason(String changeReason) {
		this.changeReason = changeReason;
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
		this.address = address;
	}

	/**
	 * @return Returns the ssn.
	 */
	@Column(name = "physician_code")
	public String getPhysicianCode() {
		return physicianCode;
	}

	/**
	 * @param ssn The ssn to set.
	 */
	public void setPhysicianCode(String physicianCode) {
		this.physicianCode = physicianCode;
	}

	/* (non-Javadoc)
	 * @see com.arahant.beans.ArahantBean#generateId()
	 */
	@Override
	public String generateId() throws ArahantException {
		physicianId = IDGenerator.generate(this);
		return physicianId;
	}

	/* (non-Javadoc)
	 * @see com.arahant.beans.ArahantBean#keyColumn()
	 */
	@Override
	public String keyColumn() {
		return "physician_id";
	}

	/* (non-Javadoc)
	 * @see com.arahant.beans.ArahantBean#tableName()
	 */
	@Override
	public String tableName() {
		return "physician";
	}

	/**
	 * @return Returns the beneficiary.
	 */
	@Column(name = "physician_name")
	public String getPhysicianName() {
		return physicianName;
	}

	/**
	 * @param beneficiary The beneficiary to set.
	 */
	public void setPhysicianName(String physicianName) {
		this.physicianName = physicianName;
	}

	/**
	 * @return Returns the beneficiaryId.
	 */
	@Id
	@Column(name = "physician_id")
	public String getPhysicianId() {
		return physicianId;
	}

	/**
	 * @param beneficiaryId The beneficiaryId to set.
	 */
	public void setPhysicianId(String physicianId) {
		this.physicianId = physicianId;
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
		this.benefitJoinId = benefitJoinId;
	}

	@Override
	public boolean equals(Object o) {
		if (physicianId == null && o == null)
			return true;
		if (physicianId != null && o instanceof HrPhysician)
			return physicianId.equals(((HrPhysician) o).getPhysicianId());

		return false;
	}

	@Override
	public int hashCode() {
		if (physicianId == null)
			return 0;
		return physicianId.hashCode();
	}

	public String keyValue() {
		return getPhysicianId();
	}
}
