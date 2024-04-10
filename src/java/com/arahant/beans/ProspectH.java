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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 */
@Entity
@Table(name="prospect_h")
public class ProspectH extends ArahantHistoryBean {

	public static final String PROSPECT_ID = "orgGroupId";

	private String orgGroupId;
	private int firstContactDate;
    private String prospectStatusId;
	private String prospectSourceId;
	private String prospectTypeId;
    private short certainty = 0;
	private double opportunity_value = 0.0;
    //private String primaryInterests;
    //private String objections;

    private String sourceDetail;
    private String salesPersonId;
    private int lastContactDate;

	protected Date statusChangeDate;

	private int nextContactDate;

	@Column(name="status_change_date")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getStatusChangeDate() {
		return statusChangeDate;
	}

	public void setStatusChangeDate(Date statusChangeDate) {
		this.statusChangeDate = statusChangeDate;
	}

	@Column(name="certainty")
	public short getCertainty() {
		return certainty;
	}

	public void setCertainty(short certainty) {
		this.certainty = certainty;
	}
	
	@Column(name="opportunity_value")
	public double getOpportunityValue() {
		return opportunity_value;
	}

	public void setOpportunityValue(double opportunity_value) {
		this.opportunity_value = opportunity_value;
	}

	@Column(name="first_contact_date")
	public int getFirstContactDate() {
		return firstContactDate;
	}

	public void setFirstContactDate(int firstContactDate) {
		this.firstContactDate = firstContactDate;
	}

	@Column(name="last_contact_date")
	public int getLastContactDate() {
		return lastContactDate;
	}

	public void setLastContactDate(int lastContactDate) {
		this.lastContactDate = lastContactDate;
	}


	@Column(name="org_group_id")
	public String getOrgGroupId() {
		return orgGroupId;
	}

	public void setOrgGroupId(String orgGroupId) {
		this.orgGroupId = orgGroupId;
	}

	@Column(name="prospect_source_id")
	public String getProspectSourceId() {
		return prospectSourceId;
	}

	public void setProspectSourceId(String prospectSourceId) {
		this.prospectSourceId = prospectSourceId;
	}

	@Column(name="prospect_status_id")
	public String getProspectStatusId() {
		return prospectStatusId;
	}

	public void setProspectStatusId(String prospectStatusId) {
		this.prospectStatusId = prospectStatusId;
	}

	@Column(name="person_id")
	public String getSalesPersonId() {
		return salesPersonId;
	}

	public void setSalesPersonId(String salesPersonId) {
		this.salesPersonId = salesPersonId;
	}

	@Column(name="source_detail")
	public String getSourceDetail() {
		return sourceDetail;
	}

	public void setSourceDetail(String sourceDetail) {
		this.sourceDetail = sourceDetail;
	}

	@Column(name="prospect_type_id")
	public String getProspectTypeId() {
		return prospectTypeId;
	}

	public void setProspectTypeId(String prospectTypeId) {
		this.prospectTypeId = prospectTypeId;
	}

	@Override
	@Column(name="record_change_date")
	@Temporal(TemporalType.TIMESTAMP)
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
	@Column(name="history_id")
	@Id
	public String getHistory_id() {
		return history_id;
	}

	@Column(name="next_contact_date")
	public int getNextContactDate() {
		return nextContactDate;
	}

	public void setNextContactDate(int nextContactDate) {
		this.nextContactDate = nextContactDate;
	}

	@Override
	public boolean alreadyThere() {
		try {
            PreparedStatement stmt = ArahantSession.getHSU().getConnection().prepareStatement("select * from prospect_h where org_group_id=? and record_change_date=?");

            stmt.setString(1, orgGroupId);
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
		return "prospect_h";
	}

	@Override
	public String keyColumn() {
		return "history_id";
	}

}
