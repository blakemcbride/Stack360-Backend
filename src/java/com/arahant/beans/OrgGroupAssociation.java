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

import javax.persistence.*;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import java.util.Date;

@Entity
@Table(name=OrgGroupAssociation.TABLE_NAME)
public class OrgGroupAssociation extends AuditedBean implements java.io.Serializable, ArahantSaveNotify {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "org_group_association";

	// Fields
	
	private OrgGroupAssociationId id;

	public static final String ID = "id";

	private Person person;

	public static final String PERSON = "person";

	private OrgGroup orgGroup;

	public static final String ORGGROUP = "orgGroup";
	public static final String ORG_GROUP_ID = "orgGroupId";

	private char primaryIndicator;

	public static final String PRIMARYINDICATOR = "primaryIndicator";

	private int orgGroupType;

	public static final String ORGGROUPTYPE = "orgGroupType";

	
	private String personId;
	private String orgGroupId;
	
	public static final String PERSONID = "personId";

    private int startDate;
    public static final String STARTDATE = "startDate";

	private int finalDate;
    public static final String FINALDATE = "finalDate";

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

	/**
	 * @return Returns the personId.
	 */
	@Column (name="person_id",insertable=false,updatable=false)
	public String getPersonId() {
		return personId;
	}



	/** default constructor */
	public OrgGroupAssociation() {
	}


	// Property accessors

	@Id
	public OrgGroupAssociationId getId() {
		return this.id;
	}

	public void setId(final OrgGroupAssociationId id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="person_id",updatable=false,insertable=false)
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(final Person person) {
		this.person = person;

		setPersonId((person!=null)?person.getPersonId():null);
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="org_group_id",updatable=false,insertable=false)
	public OrgGroup getOrgGroup() {
		return this.orgGroup;
	}

	public void setOrgGroup(final OrgGroup orgGroup) {
		this.orgGroup = orgGroup;

        setOrgGroupId((orgGroup!=null)?orgGroup.getOrgGroupId():null);
	}

	@Column (name="primary_indicator")
	public char getPrimaryIndicator() {
		return this.primaryIndicator;
	}

	public void setPrimaryIndicator(final char primaryIndicator) {
		this.primaryIndicator = primaryIndicator;
	}

	@Column (name="org_group_type")
	public int getOrgGroupType() {
		return this.orgGroupType;
	}


	/**
	 * @param orgGroupType The orgGroupType to set.
	 */
	public void setOrgGroupType(int orgGroupType) {
		firePropertyChange("orgGroupType", this.orgGroupType, orgGroupType);
		this.orgGroupType = orgGroupType;
	}

	/**
	 * @param personId The personId to set.
	 */
	public void setPersonId(String personId) {
		firePropertyChange("personId", this.personId, personId);
		this.personId = personId;
	}

	public String keyColumn() {
		return "";
	}

	public String tableName() {
		return TABLE_NAME;
	}

	public String generateId() throws ArahantException {
		throw new ArahantException("Can't generate like this.");
	}

	@Column (name="org_group_id",insertable=false,updatable=false)
	public String getOrgGroupId() {
		return orgGroupId;
	}

	public void setOrgGroupId(String orgGroupId) {
		firePropertyChange("orgGroupId", this.orgGroupId, orgGroupId);
		this.orgGroupId = orgGroupId;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (id==null && o==null)
			return true;
		if (id!=null && o instanceof OrgGroupAssociation)
			return id.equals(((OrgGroupAssociation)o).getId());
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		if (id==null)
			return 0;
		return id.hashCode();
	}

	@Override
	public ArahantHistoryBean historyObject() {
		if (personId==null)
            throw new ArahantException("Can't create history object for org group association!");
		return new OrgGroupAssociationH();
	}

	public String notifyId() {
		return id.toString();
	}

	public String notifyClassName() {
		return "OrgGroupAssociation";
	}

	@Column (name="record_change_type")
	public char getRecordChangeType() {
		return recordChangeType;
	}

	@Column (name="record_person_id")
	public String getRecordPersonId() {
		if (recordPersonId == null)
			if (ArahantSession.getHSU().getCurrentPerson() != null)
				recordPersonId = ArahantSession.getHSU().getCurrentPerson().getPersonId();
			else {
				recordPersonId = ArahantSession.getHSU().getArahantPersonId();
			}
		return recordPersonId;
	}

	@Column (name="record_change_date")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getRecordChangeDate() {
		return recordChangeDate;
	}

	@Override
	public Object keyValue() {
		return getId();
	}
}
