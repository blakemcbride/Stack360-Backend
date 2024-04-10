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
import javax.persistence.*;

/**
 * Author: Blake McBride
 * Date: 2/15/18
 */
@Entity
@Table(name=Expense.TABLE_NAME)
public class Expense extends ArahantBean implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "expense";

    public static final String EXPENSE_ID = "expenseId";
    public static final String EMPLOYEE_ID = "employeeId";
    public static final String EMPLOYEE = "employee";
    public static final String PROJECT_ID = "projectId";
    public static final String DATE_PAID = "datePaid";
    public static final String WEEK_PAID_FOR = "weekPaidFor";

    private String expenseId;
    private Employee employee;
    private String employeeId;
    private Project project;
    private String projectId;
    private int datePaid = 0;
    private float perDiemAmount = 0;
    private float expenseAmount = 0;
    private float advanceAmount = 0;
    private float perDiemReturn = 0;
    private char paymentMethod;
    private String comments;
    private String schedulingComments;
    private int authDate;
    private Person authPerson;
    private String authPersonId;
    private int weekPaidFor;
    private float hotelAmount = 0;

    public Expense() {}

    @Id
    @Column(name = "expense_id")
    public String getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(final Employee employee) {
        this.employee = employee;
    }

    @Column(name = "employee_id", insertable = false, updatable = false)
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    @Column(name = "project_id", insertable = false, updatable = false)
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    public Project getProject() {
        return this.project;
    }

    public void setProject(final Project project) {
        this.project = project;
    }

    @Column(name = "date_paid")
    public int getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(int datePaid) {
        this.datePaid = datePaid;
    }

    @Column(name = "per_diem_amount")
    public float getPerDiemAmount() {
        return perDiemAmount;
    }

    public void setPerDiemAmount(float perDiemAmount) {
        this.perDiemAmount = perDiemAmount;
    }

    @Column(name = "expense_amount")
    public float getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(float expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    @Column(name = "advance_amount")
    public float getAdvanceAmount() {
        return advanceAmount;
    }

    public void setAdvanceAmount(float advanceAmount) {
        this.advanceAmount = advanceAmount;
    }

    @Column(name = "per_diem_return")
    public float getPerDiemReturn() {
        return perDiemReturn;
    }

    public void setPerDiemReturn(float perDiemReturn) {
        this.perDiemReturn = perDiemReturn;
    }

    @Column(name = "payment_method")
    public char getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(char paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Column(name = "comments")
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Column(name = "scheduling_comments")
    public String getSchedulingComments() {
        return schedulingComments;
    }

    public void setSchedulingComments(String scheduling_comments) {
        this.schedulingComments = scheduling_comments;
    }

    @Column(name = "auth_date")
    public int getAuthDate() {
        return authDate;
    }

    public void setAuthDate(int authDate) {
        this.authDate = authDate;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_person_id")
    public Person getAuthPerson() {
        return this.authPerson;
    }

    public void setAuthPerson(final Person person) {
        this.authPerson = person;
    }

    @Column(name = "auth_person_id", insertable = false, updatable = false)
    public String getAuthPersonId() {
        return authPersonId;
    }

    public void setAuthPersonId(String authPersonId) {
        this.authPersonId = authPersonId;
    }

    @Column(name = "week_paid_for")
    public int getWeekPaidFor() {
        return weekPaidFor;
    }

    public void setWeekPaidFor(int weekPaidFor) {
        this.weekPaidFor = weekPaidFor;
    }

    @Column(name = "hotel_amount")
    public float getHotelAmount() {
        return hotelAmount;
    }

    public void setHotelAmount(float hotelAmount) {
        this.hotelAmount = hotelAmount;
    }

    @Override
    public String tableName() {
        return TABLE_NAME;
    }

    @Override
    public String keyColumn() {
        return "expense_id";
    }

    @Override
    public String generateId() throws ArahantException {
        setExpenseId(IDGenerator.generate(this));
        return expenseId;
    }
}
