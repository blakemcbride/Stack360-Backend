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

import com.arahant.utils.ArahantSession;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
@Table(name=ImportBenefitJoinH.TABLE_NAME)
public class ImportBenefitJoinH extends ArahantHistoryBean implements Serializable {


	public static final String TABLE_NAME="drc_import_benefit_join_h";

	public static final String BENEFIT="benefit";
	public static final String ENROLLEE="enrollee";


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


	@Override
	public String getHistory_id() {
		return history_id;
	}

	@Override
	public boolean alreadyThere() {
		try {
            PreparedStatement stmt = ArahantSession.getHSU().getConnection().prepareStatement("select * from drc_import_benefit_join_h where import_benefit_join_id=? and record_change_date=?");

            stmt.setString(1, importBenefitJoinId);
            stmt.setTimestamp(2, new java.sql.Timestamp(getRecordChangeDate().getTime()));
            ResultSet rs = stmt.executeQuery();
            boolean found = rs.next();

            rs.close();
            stmt.close();

            return found;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
	}

	@Override
	public String tableName() {
		return null;
	}

	@Override
	public String keyColumn() {
		return "";
	}

}
