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

@Entity
@Table(name = "hr_position")
public class HrPosition extends Setup implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "hr_position";
    // Fields
    private String positionId;
    public static final String POSITIONID = "positionId";
    private String name;
    public static final String NAME = "name";
    private Set<HrWage> hrWages = new HashSet<>(0);
    public static final String HRWAGES = "hrWages";
    private BenefitClass benefitClass;
    private float weeklyPerDiem = 0;
    private short seqno = 0;
    public static final String SEQNO = "seqno";
    private char applicantDefault = 'N';

    // Constructors
    /** default constructor */
    public HrPosition() {
    }

    // Property accessors
    @Id
    @Column(name = "position_id")
    public String getPositionId() {
        return this.positionId;
    }

    public void setPositionId(final String positionId) {
        this.positionId = positionId;
    }

    @Column(name = "position_name")
    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = HrWage.HRPOSITION, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<HrWage> getHrWages() {
        return this.hrWages;
    }

    public void setHrWages(final Set<HrWage> hrWages) {
        this.hrWages = hrWages;
    }

    @Override
    public String keyColumn() {
        return "position_id";
    }

    @Override
    public String tableName() {
        return TABLE_NAME;
    }

    @Column(name = "last_active_date")
    @Override
    public int getLastActiveDate() {
        return lastActiveDate;
    }

    @Column(name = "first_active_date")
    @Override
    public int getFirstActiveDate() {
        return firstActiveDate;
    }

    @ManyToOne
    @JoinColumn(name = "org_group_id")
    @Override
    public OrgGroup getOrgGroup() {
        return orgGroup;
    }

    @ManyToOne
    @JoinColumn(name = "benefit_class_id")
    public BenefitClass getBenefitClass() {
        return benefitClass;
    }

    public void setBenefitClass(BenefitClass benefitClass) {
        this.benefitClass = benefitClass;
    }

    @Column(name = "weekly_per_diem")
    public float getWeeklyPerDiem() {
        return weeklyPerDiem;
    }

    public void setWeeklyPerDiem(float weeklyPerDiem) {
        this.weeklyPerDiem = weeklyPerDiem;
    }

    @Column(name = "seqno")
    public short getSeqno() {
        return seqno;
    }

    public void setSeqno(short seqno) {
        this.seqno = seqno;
    }

    @Column(name = "applicant_default")
    public char getApplicantDefault() {
        return applicantDefault;
    }

    public void setApplicantDefault(char applicantDefault) {
        this.applicantDefault = applicantDefault;
    }

    @Override
    public String generateId() throws ArahantException {
        setPositionId(IDGenerator.generate(this));
        return positionId;
    }

    @Override
    public boolean equals(Object o) {
        if (positionId == null && o == null) {
            return true;
        }
        if (positionId != null && o instanceof HrPosition) {
            return positionId.equals(((HrPosition) o).getPositionId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        if (positionId == null) {
            return 0;
        }
        return positionId.hashCode();
    }
}
