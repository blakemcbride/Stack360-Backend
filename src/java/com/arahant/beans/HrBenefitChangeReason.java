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
/**
/**
 * Created on Aug 23, 2007
 * 
 */
package com.arahant.beans;
import javax.persistence.*;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;


/**
 * 
 *
 * Created on Aug 23, 2007
 *
 */
@Entity
@Table(name="hr_benefit_change_reason")
public class HrBenefitChangeReason extends Filtered  implements java.io.Serializable, Comparable<HrBenefitChangeReason> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -454519606923267488L;
	public static final String NAME = "description";
	public static final String TYPE = "type";
	public static final String EVENT_TYPE = "eventType";
	
	public static final short QUALIFYING_EVENT=1;
	public static final short OPEN_ENROLLMENT=2;
	public static final short NEW_HIRE=3;
	public static final short INTERNAL_STAFF_EDIT=4;
	public static final String START_DATE = "startDate";
	public static final String END_DATE = "endDate";
	public static final String DESCRIPTION="description";
	public static final String ID="hrBenefitChangeReasonId";
	
	public static final short EVENT_TYPE_UNKNOWN=0;
	public static final short EVENT_TYPE_DIVORCE=1;
	public static final short EVENT_TYPE_DEPENDENT=2;
	public static final short EVENT_TYPE_DEATH=3;
	public static final short EVENT_TYPE_VOLUNTARY_TERM=4;
	public static final short EVENT_TYPE_INVOLUNTARY_TERM=5;
	public static final short EVENT_TYPE_ELECTED_COBRA=6;
	public static final short EVENT_TYPE_MARRIAGE=7;
	public static final short EVENT_TYPE_GAINED_OTHER_COVERAGE=8;
	public static final short EVENT_TYPE_DEPENDENT_INELIGIBLE=9;
	public static final short EVENT_TYPE_REINSTATEMENT=10;
	public static final short EVENT_TYPE_REDUCTION_IN_HOURS=11;
	public static final short EVENT_TYPE_ENTITLED_MEDICARE=12;

	public static final short EVENT_TYPE_RETIRED=13;
	public static final short EVENT_TYPE_LAIDOFF=14;
	public static final short EVENT_TYPE_NAME_CHANGE=15;
	public static final short EVENT_TYPE_TRANSFER=16;
	public static final short EVENT_TYPE_WORK_STATUS=17;
	public static final short EVENT_TYPE_PCP_CHANGE=18;
	public static final short EVENT_TYPE_FCR_DCCR=19;


	
	public HrBenefitChangeReason() {
		super();
	}
	
	private String hrBenefitChangeReasonId, description;
	private short type;
	private int startDate, endDate, effectiveDate;
	private short eventType;
	private String instructions;

	@Override
	@ManyToOne
	@JoinColumn(name="company_id")
	public OrgGroup getOrgGroup() {
		return orgGroup;
	}
	/* (non-Javadoc)
	 * @see com.arahant.beans.ArahantBean#generateId()
	 */
	@Override
	public String generateId() throws ArahantException {
		hrBenefitChangeReasonId=IDGenerator.generate(this);
		return hrBenefitChangeReasonId;
	}

	/* (non-Javadoc)
	 * @see com.arahant.beans.ArahantBean#keyColumn()
	 */
	@Override
	public String keyColumn() {

		return "bcr_id";
	}

	/* (non-Javadoc)
	 * @see com.arahant.beans.ArahantBean#tableName()
	 */
	@Override
	public String tableName() {
		return "hr_benefit_change_reason";
	}


	@Column (name="instructions")
	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}



	/**
	 * @return Returns the description.
	 */
	@Column (name="description")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		firePropertyChange("description", this.description, description);
		this.description = description;
	}

	/**
	 * @return Returns the effectiveDate.
	 */
	@Column (name="effective_date")
	public int getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * @param effectiveDate The effectiveDate to set.
	 */
	public void setEffectiveDate(int effectiveDate) {
		firePropertyChange("effectiveDate", this.effectiveDate, effectiveDate);
		this.effectiveDate = effectiveDate;
	}

	/**
	 * @return Returns the endDate.
	 */
	@Column (name="end_date")
	public int getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate The endDate to set.
	 */
	public void setEndDate(int endDate) {
		firePropertyChange("endDate", this.endDate, endDate);
		this.endDate = endDate;
	}

	/**
	 * @return Returns the hrBenefitChangeReasonId.
	 */
	@Id
	@Column (name="bcr_id")
	public String getHrBenefitChangeReasonId() {
		return hrBenefitChangeReasonId;
	}

	/**
	 * @param hrBenefitChangeReasonId The hrBenefitChangeReasonId to set.
	 */
	public void setHrBenefitChangeReasonId(String hrBenefitChangeReasonId) {
		firePropertyChange("hrBenefitChangeReasonId", this.hrBenefitChangeReasonId,
				hrBenefitChangeReasonId);
		this.hrBenefitChangeReasonId = hrBenefitChangeReasonId;
	}

	/**
	 * @return Returns the startDate.
	 */
	@Column (name="start_date")
	public int getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate The startDate to set.
	 */
	public void setStartDate(int startDate) {
		firePropertyChange("startDate", this.startDate, startDate);
		this.startDate = startDate;
	}

	/**
	 * @return Returns the type.
	 */
	@Column (name="bcr_type")
	public short getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(short type) {
		firePropertyChange("type", this.type, type);
		this.type = type;
	}

	@Override
	public boolean equals(Object o)
	{
		if (hrBenefitChangeReasonId==null && o==null)
			return true;
		if (hrBenefitChangeReasonId!=null && o instanceof HrBenefitChangeReason)
			return hrBenefitChangeReasonId.equals(((HrBenefitChangeReason)o).getHrBenefitChangeReasonId());
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		if (hrBenefitChangeReasonId==null)
			return 0;
		return hrBenefitChangeReasonId.hashCode();
	}

	@Override
	public int compareTo(HrBenefitChangeReason o) {
		if (description==null)
			description="";
		if (o==null)
			return -1;
		if (o.description==null)
			o.description="";
		return description.compareTo(o.description);
	}

	@Column(name="event_type")
	public short getEventType() {
		return eventType;
	}

	public void setEventType(short eventType) {
		this.eventType = eventType;
	}



}
	
