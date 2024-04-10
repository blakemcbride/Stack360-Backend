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
 * Created on Jun 11, 2007
 *
 */
package com.arahant.beans;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import org.hibernate.annotations.Where;

/**
 *
 *
 * Created on Jun 11, 2007
 *
 */
@Entity
@Table(name = "hr_empl_dependent")
public class HrEmplDependentWizard extends ArahantBean implements java.io.Serializable, Comparable<HrEmplDependentWizard> {

    public HrEmplDependentWizard() {
        super();
    }
    private String personId;
    private String employeeId;
    private String relationship;
    private char relationshipType;
    private String relationshipId;
    private int dateAdded;
    private int dateInactive;
    private Set<HrBenefitJoin> benefitJoins = new HashSet<HrBenefitJoin>(0);
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "hr_empl_dependent";
//	public static final String BENEFIT_JOINS = "benefitJoins";
    public static final String RELATIONSHIP_TYPE = "relationshipType";
    public static final String EMPLOYEE_ID = "employeeId";
    //public static final String PERSON = "person";
    public static final String DATE_INACTIVE = "dateInactive";
    public static final String DEP_KEY = "relationshipId";
    public static final String RELATIONSHIP_OTHER = "relationship";
    public static final String RELATIONSHIP_ID = "relationshipId";
	public static final String CHANGE_REQUESTS = "changeRequests";

    public static final char TYPE_SPOUSE = 'S';
	public static final char TYPE_OTHER = 'O';
	public static final char TYPE_CHILD = 'C';

	private char recordType='C';


	private Set<HrEmplDependentChangeRequest> changeRequests=new HashSet<HrEmplDependentChangeRequest>();

	@OneToMany
	@JoinColumn(name="change_record_id")
	public Set<HrEmplDependentChangeRequest> getChangeRequests() {
		return changeRequests;
	}

	public void setChangeRequests(Set<HrEmplDependentChangeRequest> changeRequests) {
		this.changeRequests = changeRequests;
	}


	@Column(name="record_type")
	public char getRecordType() {
		return recordType;
	}

	public void setRecordType(char recordType) {
		this.recordType = recordType;
	}




    /**
     * @return Returns the employee.
     */
    @Column(name = "employee_id")
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employee The employee to set.
     */
    public void setEmployeeId(final String employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return Returns the relationship.
     */
    @Column(name = "relationship")
    public String getRelationship() {
        return relationship;
    }

    /**
     * @param relationship The relationship to set.
     */
    public void setRelationship(final String relationship) {
        this.relationship = relationship;
    }

    /**
     * @return Returns the relationshipType.
     */
    @Column(name = "relationship_type")
    public char getRelationshipType() {
        return relationshipType;
    }

    /**
     * @param relationshipType The relationshipType to set.
     */
    public void setRelationshipType(final char relationshipType) {
        this.relationshipType = relationshipType;
    }

    /**
     * @return Returns the benefits.
     */
    @OneToMany(mappedBy = HrBenefitJoin.RELATIONSHIP, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<HrBenefitJoin> getBenefitJoins() {
        return benefitJoins;
    }

    /* (non-Javadoc)
     * @see com.arahant.beans.ArahantBean#generateId()
     */
    @Override
    public String generateId() throws ArahantException {

        relationshipId = IDGenerator.generate(this);
        return relationshipId;
    }

    /* (non-Javadoc)
     * @see com.arahant.beans.ArahantBean#keyColumn()
     */
    @Override
    public String keyColumn() {

        return "relationship_id";
    }

    /* (non-Javadoc)
     * @see com.arahant.beans.ArahantBean#tableName()
     */
    @Override
    public String tableName() {

        return TABLE_NAME;
    }

    /**
     * @return Returns the person.
     */

    @Column(name = "dependent_id")
    public String getPersonId() {
        return personId;
    }

    /**
     * @param person The person to set.
     */
    public void setPersonId(final String personId) {
        this.personId = personId;
    }

    /**
     * @return Returns the relationshipId.
     */
    @Id
    @Column(name = "relationship_id")
    public String getRelationshipId() {
        return relationshipId;
    }

    /**
     * @param relationshipId The relationshipId to set.
     */
    public void setRelationshipId(final String relationshipId) {
        this.relationshipId = relationshipId;
    }

    /**
     * @return Returns the dateAdded.
     */
    @Column(name = "date_added")
    public int getDateAdded() {
        return dateAdded;
    }

    /**
     * @param dateAdded The dateAdded to set.
     */
    public void setDateAdded(final int dateAdded) {
        this.dateAdded = dateAdded;
    }

    /**
     * @return Returns the dateInactive.
     */
    @Column(name = "date_inactive")
    public int getDateInactive() {
        return dateInactive;
    }

    /**
     * @param dateInactive The dateInactive to set.
     */
    public void setDateInactive(final int dateInactive) {
        this.dateInactive = dateInactive;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final HrEmplDependentWizard o) {

        try {
            if (getRelationshipType() == 'E') {
                return -1;
            }

            if (o.getRelationshipType() == 'E') {
                return 1;
            }

            //first, check for spouse
            if (getRelationshipType() == 'S') {
                return -1;
            }

            if (getRelationshipType() == 'C' && o.getRelationshipType() == 'O') {
                return -1;
            }

            if (getRelationshipType() == 'O' && o.getRelationshipType() == 'C') {
                return 1;
            }
			return 0;
        } catch (Exception e) {
            return 0;
        }

    }

    public void setBenefitJoins(Set<HrBenefitJoin> benefitJoins) {
        this.benefitJoins = benefitJoins;
    }

    @Override
    public boolean equals(Object o) {
        if (relationshipId == null && o == null) {
            return true;
        }
        if (relationshipId != null && o instanceof HrEmplDependentWizard) {
            return relationshipId.equals(((HrEmplDependentWizard) o).getRelationshipId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        if (relationshipId == null) {
            return 0;
        }
        return relationshipId.hashCode();
    }

}


