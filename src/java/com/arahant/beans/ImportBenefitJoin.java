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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 */
@Entity
@Table(name=ImportBenefitJoin.TABLE_NAME)
public class ImportBenefitJoin extends AuditedBean implements Serializable {

	public static final String TABLE_NAME="drc_import_benefit_join";

	public static final String BENEFIT="benefit";
	public static final String ENROLLEE="enrollee";
	public static final String SPONSOR="subscriber";

	
	private String importBenefitJoinId;
	private ImportedEnrollee enrollee;
	private ImportedBenefit benefit;
	private ImportedEnrollee subscriber;
	private int coverageStartDate;
	private int coverageEndDate;

	@ManyToOne
	@JoinColumn(name="import_benefit_id")
	public ImportedBenefit getBenefit() {
		return benefit;
	}

	public void setBenefit(ImportedBenefit benefit) {
		this.benefit = benefit;
	}

	@Column(name="coverage_end_date")
	public int getCoverageEndDate() {
		return coverageEndDate;
	}

	public void setCoverageEndDate(int coverageEndDate) {
		this.coverageEndDate = coverageEndDate;
	}

	@Column(name="coverage_start_date")
	public int getCoverageStartDate() {
		return coverageStartDate;
	}

	public void setCoverageStartDate(int coverageStartDate) {
		this.coverageStartDate = coverageStartDate;
	}

	@ManyToOne
	@JoinColumn(name="enrollee_id")
	public ImportedEnrollee getEnrollee() {
		return enrollee;
	}

	public void setEnrollee(ImportedEnrollee enrollee) {
		this.enrollee = enrollee;
	}

	@Id
	@Column(name="import_benefit_join_id")
	public String getImportBenefitJoinId() {
		return importBenefitJoinId;
	}

	public void setImportBenefitJoinId(String importBenefitJoinId) {
		this.importBenefitJoinId = importBenefitJoinId;
	}

	@ManyToOne
	@JoinColumn(name="subscriber_id")
	public ImportedEnrollee getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(ImportedEnrollee subscriber) {
		this.subscriber = subscriber;
	}




	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "import_benefit_join_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return importBenefitJoinId=IDGenerator.generate(this);
	}

	@Override
	public ArahantHistoryBean historyObject() {
		return new ImportBenefitJoinH();
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
		ArahantSession.getHSU().createCriteria(ImportBenefitJoin.class).list();
	}

	@Override
	public String keyValue() {
		return getImportBenefitJoinId();
	}

}
