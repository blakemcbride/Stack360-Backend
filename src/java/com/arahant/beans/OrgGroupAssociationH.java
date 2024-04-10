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


/**
 * Created on Feb 6, 2008
 * 
 */
package com.arahant.beans;
import java.io.Serializable;
import javax.persistence.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantLogger;
import java.util.Date;


/**
 * 
 *
 * Created on Feb 6, 2008
 *
 */
@Entity
@Table(name="org_group_association_h")
public class OrgGroupAssociationH extends ArahantHistoryBean implements Serializable {

	@Id
	public String getHistory_id() {
		return history_id;
	}
	/**
	 * @return Returns the recordChangeType.
	 */
	@Column (name="record_change_type")
	public char getRecordChangeType() {
		return recordChangeType;
	}

	@Column (name="record_person_id")
	public String getRecordPersonId() {
		return recordPersonId;
	}

	@Column (name="record_change_date")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getRecordChangeDate() {
		return recordChangeDate;
	}

	public static final String PERSON_ID = "personId";

	private static transient ArahantLogger logger = new ArahantLogger(
			OrgGroupAssociationH.class);
	
	private char primaryIndicator;
	private int orgGroupType;
	private String personId;
	private String orgGroupId;
    private int startDate;
	private int finalDate;

	@Column (name="final_date")
    public int getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(int finalDate) {
        this.finalDate = finalDate;
    }

	@Column (name="start_date")
    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }
	
	
	@Column (name="org_group_id")
	public String getOrgGroupId() {
		return orgGroupId;
	}

	public void setOrgGroupId(String orgGroupId) {
		firePropertyChange("orgGroupId", this.orgGroupId, orgGroupId);
		this.orgGroupId = orgGroupId;
	}

	@Column (name="org_group_type")
	public int getOrgGroupType() {
		return orgGroupType;
	}

	public void setOrgGroupType(int orgGroupType) {
		firePropertyChange("orgGroupType", this.orgGroupType, orgGroupType);
		this.orgGroupType = orgGroupType;
	}

	@Column (name="person_id")
	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		firePropertyChange("personId", this.personId, personId);
		this.personId = personId;
	}

	@Column (name="primary_indicator")
	public char getPrimaryIndicator() {
		return primaryIndicator;
	}

	public void setPrimaryIndicator(char primaryIndicator) {
		firePropertyChange("primaryIndicator", this.primaryIndicator,
				primaryIndicator);
		this.primaryIndicator = primaryIndicator;
	}

	public OrgGroupAssociationH() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.arahant.beans.ArahantHistoryBean#alreadyThere()
	 */
	@Override
	public boolean alreadyThere() {

		return false;
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
}

	
