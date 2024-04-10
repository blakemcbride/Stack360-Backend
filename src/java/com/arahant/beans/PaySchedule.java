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

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
@Entity
@Table(name = "pay_schedule")
public class PaySchedule extends ArahantBean {

    public static final String NAME = "scheduleName";
    public static final String ID = "payScheduleId";
    private String payScheduleId;
    private String scheduleName;
    private String description;
    private Set<PaySchedulePeriod> periods = new HashSet<PaySchedulePeriod>();
    private Set<OrgGroup> orgGroups = new HashSet<OrgGroup>();

	private String companyId;
	private CompanyDetail company;
	public static final String COMPANY="company";

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="company_id")
	public CompanyDetail getCompany() {
		return company;
	}

	public void setCompany(CompanyDetail company) {
		this.company = company;
	}

	@Column (name="company_id", insertable=false, updatable=false)
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Id
    @Column(name = "pay_schedule_id")
    public String getPayScheduleId() {
        return payScheduleId;
    }

    public void setPayScheduleId(String payScheduleId) {
        this.payScheduleId = payScheduleId;
    }

    @Column(name = "schedule_name")
    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    @OneToMany(mappedBy = PaySchedulePeriod.PAY_SCHEDULE, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<PaySchedulePeriod> getPeriods() {
        return periods;
    }

    public void setPeriods(Set<PaySchedulePeriod> periods) {
        this.periods = periods;
    }

    @OneToMany(mappedBy = OrgGroup.PAY_SCHEDULE, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<OrgGroup> getOrgGroups() {
        return orgGroups;
    }

    public void setOrgGroups(Set<OrgGroup> orgGroups) {
        this.orgGroups = orgGroups;
    }

    @Override
    public String tableName() {
        return "pay_schedule";
    }

    @Override
    public String keyColumn() {
        return "pay_schedule_id";
    }

    @Override
    public String generateId() throws ArahantException {
        return payScheduleId = IDGenerator.generate(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PaySchedule other = (PaySchedule) obj;
        if (this.payScheduleId != other.payScheduleId && (this.payScheduleId == null || !this.payScheduleId.equals(other.payScheduleId))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (this.payScheduleId != null ? this.payScheduleId.hashCode() : 0);
        return hash;
    }
}
