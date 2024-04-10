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
@Table(name = "project_view_join")
public class ProjectViewJoin extends ArahantBean implements Serializable {

	public static final String PARENT = "parent";
	public static final String CHILD = "child";
	public static final String SEQ = "seqNum";
	public static final String ID = "projectViewJoinId";
	public static final String PRIMARY_BILLING = "primaryBilling";
	private String projectViewJoinId;
	private ProjectView child;
	private ProjectView parent;
	private int seqNum;
	private char primaryBilling = 'N';
	
	public ProjectViewJoin() {}

	@Column(name = "primary_billing")
	public char getPrimaryBilling() {
		return primaryBilling;
	}

	public void setPrimaryBilling(char primaryBilling) {
		this.primaryBilling = primaryBilling;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "child_project_view_id")
	public ProjectView getChild() {
		return child;
	}

	public void setChild(ProjectView child) {
		this.child = child;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_project_view_id")
	public ProjectView getParent() {
		return parent;
	}

	public void setParent(ProjectView parent) {
		this.parent = parent;
	}

	@Id
	@Column(name = "project_view_join_id")
	public String getProjectViewJoinId() {
		return projectViewJoinId;
	}

	public void setProjectViewJoinId(String projectViewJoinId) {
		this.projectViewJoinId = projectViewJoinId;
	}

	@Column(name = "sequence_num")
	public int getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}

	@Override
	public boolean equals(Object o) {
		if (projectViewJoinId == null && o == null)
			return true;
		if (projectViewJoinId != null && o instanceof ProjectViewJoin)
			return projectViewJoinId.equals(((ProjectViewJoin) o).getProjectViewJoinId());

		return false;
	}

	@Override
	public int hashCode() {
		if (projectViewJoinId == null)
			return 0;
		return projectViewJoinId.hashCode();
	}

	@Override
	public String tableName() {
		return "project_view_join";
	}

	@Override
	public String keyColumn() {
		return "project_view_join_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return projectViewJoinId = IDGenerator.generate(this);
	}
}
