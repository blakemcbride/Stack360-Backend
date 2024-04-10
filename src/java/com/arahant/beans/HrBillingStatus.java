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
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
@Entity
@Table(name = "hr_billing_status")
public class HrBillingStatus extends ArahantBean implements Serializable {

    public static final String NAME = "name";
    public static final String ID = "billingStatusId";
    private String billingStatusId;
    private String name;
    private Set<HrBillingStatusHistory> billingStatusHistories = new HashSet<HrBillingStatusHistory>(0);

    @Id
    @Column(name = "billing_status_id")
    public String getBillingStatusId() {
        return billingStatusId;
    }

    public void setBillingStatusId(String billingStatusId) {
        this.billingStatusId = billingStatusId;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = HrBillingStatusHistory.BILLING_STATUS, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<HrBillingStatusHistory> getBillingStatusHistories() {
        return billingStatusHistories;
    }

    public void setBillingStatusHistories(Set<HrBillingStatusHistory> billingStatusHistories) {
        this.billingStatusHistories = billingStatusHistories;
    }

    @Override
    public String tableName() {
        return "hr_billing_status";
    }

    @Override
    public String keyColumn() {
        return "billing_status_id";
    }

    @Override
    public String generateId() throws ArahantException {
        return billingStatusId = IDGenerator.generate(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HrBillingStatus other = (HrBillingStatus) obj;
        if (this.billingStatusId != other.getBillingStatusId() && (this.billingStatusId == null || !this.billingStatusId.equals(other.getBillingStatusId()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + (this.billingStatusId != null ? this.billingStatusId.hashCode() : 0);
        return hash;
    }
}
