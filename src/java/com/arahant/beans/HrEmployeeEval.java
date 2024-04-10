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
 * HrEmployeeEval generated by hbm2java
 */
@Entity
@Table(name = "hr_employee_eval")
public class HrEmployeeEval extends ArahantBean implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "hr_employee_eval";
    // Fields
    private String employeeEvalId;
    public static final String EMPLOYEEEVALID = "employeeEvalId";
    private Employee employeeByEmployeeId;
    public static final String EMPLOYEEBYEMPLOYEEID = "employeeByEmployeeId";
    private Employee employeeBySupervisorId;
    public static final String EMPLOYEEBYSUPERVISORID = "employeeBySupervisorId";
    private int evalDate;
    public static final String EVALDATE = "evalDate";
    private int nextEvalDate;
    public static final String NEXTEVALDATE = "nextEvalDate";
    private String description;
    public static final String DESCRIPTION = "description";
    private String comments;
    public static final String COMMENTS = "comments";
    private char state;
    public static final String STATE = "state";
    private String EComments;
    public static final String ECOMMENTS = "EComments";
    private String PComments;
    public static final String PCOMMENTS = "PComments";
    private int finalDate;
    public static final String FINALDATE = "finalDate";
    private Set<HrEmplEvalDetail> hrEmplEvalDetails = new HashSet<HrEmplEvalDetail>(0);
    public static final String HREMPLEVALDETAILS = "hrEmplEvalDetails";

    // Constructors
    /** default constructor */
    public HrEmployeeEval() {
    }

    // Property accessors
    @Id
    @Column(name = "employee_eval_id")
    public String getEmployeeEvalId() {
        return this.employeeEvalId;
    }

    public void setEmployeeEvalId(final String employeeEvalId) {
        this.employeeEvalId = employeeEvalId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    public Employee getEmployeeByEmployeeId() {
        return this.employeeByEmployeeId;
    }

    public void setEmployeeByEmployeeId(final Employee employeeByEmployeeId) {
        this.employeeByEmployeeId = employeeByEmployeeId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisor_id")
    public Employee getEmployeeBySupervisorId() {
        return this.employeeBySupervisorId;
    }

    public void setEmployeeBySupervisorId(final Employee employeeBySupervisorId) {
        this.employeeBySupervisorId = employeeBySupervisorId;
    }

    @Column(name = "eval_date")
    public int getEvalDate() {
        return this.evalDate;
    }

    public void setEvalDate(final int evalDate) {
        this.evalDate = evalDate;
    }

    @Column(name = "next_eval_date")
    public int getNextEvalDate() {
        return this.nextEvalDate;
    }

    public void setNextEvalDate(final int nextEvalDate) {
        this.nextEvalDate = nextEvalDate;
    }

    @Column(name = "description")
    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @Column(name = "comments")
    public String getComments() {
        return this.comments;
    }

    public void setComments(final String comments) {
        this.comments = comments;
    }

    @Column(name = "state")
    public char getState() {
        return this.state;
    }

    public void setState(final char state) {
        this.state = state;
    }

    @Column(name = "e_comments")
    public String getEComments() {
        return this.EComments;
    }

    public void setEComments(final String EComments) {
        this.EComments = EComments;
    }

    @Column(name = "p_comments")
    public String getPComments() {
        return this.PComments;
    }

    public void setPComments(final String PComments) {
        this.PComments = PComments;
    }

    @Column(name = "final_date")
    public int getFinalDate() {
        return this.finalDate;
    }

    public void setFinalDate(final int finalDate) {
        this.finalDate = finalDate;
    }

    @OneToMany(mappedBy = HrEmplEvalDetail.EMPOYEE_EVAL, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<HrEmplEvalDetail> getHrEmplEvalDetails() {
        return this.hrEmplEvalDetails;
    }

    public void setHrEmplEvalDetails(final Set<HrEmplEvalDetail> hrEmplEvalDetails) {
        this.hrEmplEvalDetails = hrEmplEvalDetails;
    }
    /* (non-Javadoc)
     * @see com.arahant.beans.ArahantBean#keyColumn()
     */

    @Override
    public String keyColumn() {

        return "employee_eval_id";
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
        setEmployeeEvalId(IDGenerator.generate(this));
        return employeeEvalId;
    }

    @Override
    public boolean equals(Object o) {
        if (employeeEvalId == null && o == null) {
            return true;
        }
        if (employeeEvalId != null && o instanceof HrEmployeeEval) {
            return employeeEvalId.equals(((HrEmployeeEval) o).getEmployeeEvalId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        if (employeeEvalId == null) {
            return 0;
        }
        return employeeEvalId.hashCode();
    }
}
