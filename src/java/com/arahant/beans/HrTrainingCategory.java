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



import java.util.HashSet;
import java.util.Set;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;

/**
 * HrTrainingCategory generated by hbm2java
 */
@Entity
@Table(name = "hr_training_category")
public class HrTrainingCategory extends SetupWithEndDate implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "hr_training_category";
    // Fields
    private String catId;
    public static final String CATID = "catId";
    private String name;
    public static final String NAME = "name";
    private short trainingType;
    public static final String TRAININGTYPE = "trainingType";
    private Set<HrTrainingDetail> hrTrainingDetails = new HashSet<HrTrainingDetail>(0);
    public static final String HRTRAININGDETAILS = "hrTrainingDetails";
    private String clientId;
    public static final String CLIENTID = "clientId";
    private OrgGroup client;
    public static final String CLIENT = "client";
    private char required = 'N';
    private float hours = 0.0f;

    // Constructors
    /** default constructor */
    public HrTrainingCategory() {
    }

    // Property accessors
    @Id
    @Column(name = "cat_id")
    public String getCatId() {
        return this.catId;
    }

    public void setCatId(final String catId) {
        this.catId = catId;
    }

    @Column(name = "name")
    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Column(name = "training_type")
    public short getTrainingType() {
        return this.trainingType;
    }

    public void setTrainingType(final short trainingType) {
        this.trainingType = trainingType;
    }

    @OneToMany(mappedBy = HrTrainingDetail.HRTRAININGCATEGORY, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<HrTrainingDetail> getHrTrainingDetails() {
        return this.hrTrainingDetails;
    }

    public void setHrTrainingDetails(final Set<HrTrainingDetail> hrTrainingDetails) {
        this.hrTrainingDetails = hrTrainingDetails;
    }

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

    @Column(name = "client_id", insertable = false, updatable = false)
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    public OrgGroup getClient() {
        return client;
    }

    public void setClient(OrgGroup client) {
        this.client = client;
    }

    @Column(name = "required")
    public char getRequired() {
        return required;
    }

    public void setRequired(char required) {
        this.required = required;
    }

    @Column(name = "hours")
    public float getHours() {
        return hours;
    }

    public void setHours(float hours) {
        this.hours = hours;
    }

    /* (non-Javadoc)
     * @see com.arahant.beans.ArahantBean#keyColumn()
     */
    @Override
    public String keyColumn() {

        return "cat_id";
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
        setCatId(IDGenerator.generate(this));
        return catId;
    }

    @Override
    public boolean equals(Object o) {
        if (catId == null && o == null) {
            return true;
        }
        if (catId != null && o instanceof HrTrainingCategory) {
            return catId.equals(((HrTrainingCategory) o).getCatId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        if (catId == null) {
            return 0;
        }
        return catId.hashCode();
    }
}
