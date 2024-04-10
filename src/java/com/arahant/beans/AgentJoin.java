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
import java.util.Date;
import javax.persistence.*;

/**
 *
 * Arahant
 */
@Entity
@Table(name = AgentJoin.TABLE_NAME)
public class AgentJoin extends ArahantBean implements Serializable {

	public static final String TABLE_NAME = "agent_join";
	public static final String AGENT = "agent";
	public static final String COMPANY = "company";
	public static final String APPROVED = "approved";
	private String agentJoinId;
	private Agent agent;
	private CompanyDetail company;
	private char approved;
	private String approvedByPersonId;
	private Date approvedDate;
	
	public AgentJoin() { }

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "agent_join_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return agentJoinId = IDGenerator.generate(this);
	}

	@ManyToOne
	@JoinColumn(name = "agent_id")
	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	@Id
	@Column(name = "agent_join_id")
	public String getAgentJoinId() {
		return agentJoinId;
	}

	public void setAgentJoinId(String agentJoinId) {
		this.agentJoinId = agentJoinId;
	}

	@ManyToOne
	@JoinColumn(name = "company_id")
	public CompanyDetail getCompany() {
		return company;
	}

	public void setCompany(CompanyDetail company) {
		this.company = company;
	}

	@Column(name = "approved")
	public char getApproved() {
		return approved;
	}

	public void setApproved(char approved) {
		this.approved = approved;
	}

	@Column(name = "approved_by_person_id")
	public String getApprovedByPersonId() {
		return approvedByPersonId;
	}

	public void setApprovedByPersonId(String approvedByPersonId) {
		this.approvedByPersonId = approvedByPersonId;
	}

	@Column(name = "approved_date")
	@Temporal(javax.persistence.TemporalType.DATE)
	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}
}
