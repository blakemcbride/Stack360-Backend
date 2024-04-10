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
import javax.persistence.*;
import org.hibernate.annotations.Fetch;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;

/**
 *
 */
@Entity
@Table(name="garnishment")
public class Garnishment extends ArahantBean {
	public static final String EMPLOYEE="employee";
	public static final String PRIORITY="priority";
	public static final String ID="garnishmentId";
	public static final String GARNISHMENT_TYPE="garnishmentType";
	public static final String DOCKET="docketNumber";

	private String garnishmentId;
	private Employee employee;
	private short priority;
	private String issueState;
	private String fipsCode;
	private String docketNumber;
	private Address remitTo;
	private String remitToName;
	private String collectionState;
	private float maxPercent;
	private double maxDollars;
	private int startDate;
	private int endDate;
	private double deductionAmount;
	private float deductionPercentage;
	private char netOrGross;
	private GarnishmentType garnishmentType;
	
	@Override
	public String tableName() {
		return "garnishment";
	}

	@Override
	public String keyColumn() {
		return "garnishment_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return garnishmentId=IDGenerator.generate(this);
	}

	@Column (name="collecting_state")
	public String getCollectionState() {
		return collectionState;
	}

	public void setCollectionState(String collectionState) {
		this.collectionState = collectionState;
	}

	@Column (name="deduction_amount")
	public double getDeductionAmount() {
		return deductionAmount;
	}

	public void setDeductionAmount(double deductionAmount) {
		this.deductionAmount = deductionAmount;
	}

	@Column (name="deduction_percentage")
	public float getDeductionPercentage() {
		return deductionPercentage;
	}

	public void setDeductionPercentage(float deductionPercentage) {
		this.deductionPercentage = deductionPercentage;
	}

	@Column (name="docket_number")
	public String getDocketNumber() {
		return docketNumber;
	}

	public void setDocketNumber(String docketNumber) {
		this.docketNumber = docketNumber;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="person_id")
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Column (name="end_date")
	public int getEndDate() {
		return endDate;
	}

	public void setEndDate(int endDate) {
		this.endDate = endDate;
	}

	@Column (name="fips_code")
	public String getFipsCode() {
		return fipsCode;
	}

	public void setFipsCode(String fipsCode) {
		this.fipsCode = fipsCode;
	}

	@Id
	@Column (name="garnishment_id")
	public String getGarnishmentId() {
		return garnishmentId;
	}

	public void setGarnishmentId(String garnishmentId) {
		this.garnishmentId = garnishmentId;
	}

	@Column (name="issue_state")
	public String getIssueState() {
		return issueState;
	}

	public void setIssueState(String issueState) {
		this.issueState = issueState;
	}

	@Column (name="max_dollars")
	public double getMaxDollars() {
		return maxDollars;
	}

	public void setMaxDollars(double maxDollars) {
		this.maxDollars = maxDollars;
	}

	@Column (name="max_percent")
	public float getMaxPercent() {
		return maxPercent;
	}

	public void setMaxPercent(float maxPercent) {
		this.maxPercent = maxPercent;
	}

	@Column (name="net_or_gross")
	public char getNetOrGross() {
		return netOrGross;
	}

	public void setNetOrGross(char netOrGross) {
		this.netOrGross = netOrGross;
	}

	@Column (name="priority")
	public short getPriority() {
		return priority;
	}

	public void setPriority(short priority) {
		this.priority = priority;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="remit_to")
	public Address getRemitTo() {
		return remitTo;
	}

	public void setRemitTo(Address remitTo) {
		this.remitTo = remitTo;
	}
	
	@Column (name="remit_to_name")
	public String getRemitToName() {
		return remitToName;
	}
	
	public void setRemitToName(String remitToName) {
		this.remitToName = remitToName;
	}

	@Column (name="start_date")
	public int getStartDate() {
		return startDate;
	}

	public void setStartDate(int startDate) {
		this.startDate = startDate;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="garnishment_type_id ")
	public GarnishmentType getGarnishmentType() {
		return garnishmentType;
	}

	public void setGarnishmentType(GarnishmentType garnishmentType) {
		this.garnishmentType = garnishmentType;
	}

	
}
