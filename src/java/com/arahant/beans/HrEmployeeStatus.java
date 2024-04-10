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
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "hr_employee_status")
@Cache(region="arahant", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HrEmployeeStatus extends SetupWithEndDate implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "hr_employee_status";
	// Fields
	private String statusId;
	public static final String STATUSID = "statusId";
	private String name;
	public static final String NAME = "name";
	private char active;
	private char benefitType;
	public static final String ACTIVE = "active";
	public static final String BENEFIT_TYPE = "benefitType";
	private Set<HrEmplStatusHistory> hrEmplStatusHistories = new HashSet<HrEmplStatusHistory>(0);
	public static final String HREMPLSTATUSHISTORIES = "hrEmplStatusHistories";
	private Set<HrChecklistItem> hrChecklistItems = new HashSet<HrChecklistItem>(0);
	public static final String HRCHECKLISTITEMS = "hrChecklistItems";
	private char dateType = 'S';
	public static String ORGGROUPID = "orgGroupId";
	private String orgGroupId;

//	private Set <HrBenefitConfig> benefitConfigs=new HashSet<HrBenefitConfig>();
	
	public HrEmployeeStatus() {
	}

	// Constructors
	/**
	 * @return Returns the dateType.
	 */
	@Column(name = "date_type")
	public char getDateType() {
		return dateType;
	}

	/**
	 * @param dateType The dateType to set.
	 */
	public void setDateType(final char dateType) {
		this.dateType = dateType;
	}

	// Property accessors
	@Id
	@Column(name = "status_id")
	public String getStatusId() {
		return this.statusId;
	}

	public void setStatusId(final String statusId) {
		this.statusId = statusId;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Column(name = "active")
	public char getActive() {
		return this.active;
	}

	public void setActive(final char active) {
		this.active = active;
	}

	@Column(name = "benefit_type")
	public char getBenefitType() {
		return this.benefitType;
	}

	public void setBenefitType(final char benefitType) {
		this.benefitType = benefitType;
	}

	@OneToMany(mappedBy = HrEmplStatusHistory.HREMPLOYEESTATUS, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<HrEmplStatusHistory> getHrEmplStatusHistories() {
		return this.hrEmplStatusHistories;
	}

	public void setHrEmplStatusHistories(final Set<HrEmplStatusHistory> hrEmplStatusHistories) {
		this.hrEmplStatusHistories = hrEmplStatusHistories;
	}

	@OneToMany(mappedBy = HrChecklistItem.HREMPLOYEESTATUS, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<HrChecklistItem> getHrChecklistItems() {
		return this.hrChecklistItems;
	}

	public void setHrChecklistItems(final Set<HrChecklistItem> hrChecklistItems) {
		this.hrChecklistItems = hrChecklistItems;
	}
	/* (non-Javadoc)
	 * @see com.arahant.beans.ArahantBean#keyColumn()
	 */

	@Override
	public String keyColumn() {

		return "status_id";
	}

	/* (non-Javadoc)
	 * @see com.arahant.beans.ArahantBean#tableName()
	 */
	@Override
	public String tableName() {

		return TABLE_NAME;
	}

	/* (non-Javadoc)
	 * @see com.arahant.beans.ArahantBean#generateId()
	 */
	@Override
	public String generateId() throws ArahantException {
		setStatusId(IDGenerator.generate(this));
		return statusId;
	}

	/**
	 * @return Returns the benefitConfigs.
	 *
	 * public Set<HrBenefitConfig> getBenefitConfigs() { return benefitConfigs;
	 * }
	 *
	 * /
	 **
	 * @param benefitConfigs The benefitConfigs to set.
	 *
	 * public void setBenefitConfigs(final Set<HrBenefitConfig> benefitConfigs)
	 * { this.benefitConfigs = benefitConfigs; }
	 */
	@Column(name = "last_active_date")
	@Override
	public int getLastActiveDate() {
		return lastActiveDate;
	}

	@ManyToOne
	@JoinColumn(name = "org_group_id")
	@Override
	public OrgGroup getOrgGroup() {
		return orgGroup;
	}

	@Column(name = "org_group_id", insertable = false, updatable = false)
	public String getOrgGroupId() {
		return orgGroupId;
	}

	public void setOrgGroupId(String id) {
		orgGroupId = id;
	}

	@Override
	public boolean equals(Object o) {
		if (statusId == null && o == null)
			return true;
		if (statusId != null && o instanceof HrEmployeeStatus)
			return statusId.equals(((HrEmployeeStatus) o).getStatusId());

		return false;
	}

	@Override
	public int hashCode() {
		if (statusId == null)
			return 0;
		return statusId.hashCode();
	}
}
