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
import com.arahant.utils.IDGenerator;
import java.util.Date;


/**
 * ProjectStatus generated by hbm2java
 */
@Entity
@Table(name="prospect_log")
public class ProspectLog extends ArahantBean implements java.io.Serializable, Comparable<ProspectLog> {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "prospect_log";
	public static final String CONTACT_DATE="contactDate";
	public static final String TIME="contactTime";
	public static final String ORG_GROUP="orgGroup";
	public static final String SALES_ACTIVITY= "salesActivity";
	public static final String EMPLOYEE="employee";
	public static final String SALES_ACTIVITY_RESULT = "salesActivityResult";
	public static final String TASK_COMPLETION_DATE = "taskCompletionDate";
	

	// Fields    

	private String prospectLogId;
	private OrgGroup orgGroup;
	private int contactDate;
	private int contactTime;
	private String contactTxt;
	private String employees;
	private String prospectEmployees;
	private SalesActivity salesActivity;
	private Employee employee;
	private SalesActivityResult salesActivityResult;
	private Date entryDate = new Date();
	private int taskCompletionDate;
	

	@Override
	public boolean equals(Object o)
	{
		if (prospectLogId==null && o==null)
			return true;
		if (prospectLogId!=null && o instanceof ProspectLog)
			return prospectLogId.equals(((ProspectLog)o).getProspectLogId());
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		if (prospectLogId==null)
			return 0;
		return prospectLogId.hashCode();
	}

	@Override
	public int compareTo(ProspectLog o) {
		if (contactDate!=o.contactDate)
			return contactDate-o.contactDate;
		return contactTime-o.contactTime;
	}

	

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "prospect_log_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return prospectLogId=IDGenerator.generate(this);
	}

	@Column (name="contact_date")
	public int getContactDate() {
		return contactDate;
	}

	public void setContactDate(int contactDate) {
		this.contactDate = contactDate;
	}

	@Column (name="contact_time")
	public int getContactTime() {
		return contactTime;
	}

	public void setContactTime(int contactTime) {
		this.contactTime = contactTime;
	}

	@Column (name="contact_txt")
	public String getContactTxt() {
		return contactTxt;
	}

	public void setContactTxt(String contactTxt) {
		this.contactTxt = contactTxt;
	}

	@Column (name="employees")
	public String getEmployees() {
		return employees;
	}

	public void setEmployees(String employees) {
		this.employees = employees;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="org_group_id")
	public OrgGroup getOrgGroup() {
		return orgGroup;
	}

	public void setOrgGroup(OrgGroup orgGroup) {
		this.orgGroup = orgGroup;
	}

	@Column (name="prospect_employees")
	public String getProspectEmployees() {
		return prospectEmployees;
	}

	public void setProspectEmployees(String prospectEmployees) {
		this.prospectEmployees = prospectEmployees;
	}

	@Id
	@Column (name="prospect_log_id")
	public String getProspectLogId() {
		return prospectLogId;
	}

	public void setProspectLogId(String prospectLogId) {
		this.prospectLogId = prospectLogId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="sales_activity_id")
	public SalesActivity getSalesActivity() {
		return salesActivity;
	}

	public void setSalesActivity(SalesActivity salesActivity) {
		this.salesActivity = salesActivity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="person_id")
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}


	@JoinColumn(name="sales_activity_result_id")
	@ManyToOne(fetch = FetchType.LAZY)
	public SalesActivityResult getSalesActivityResult() {
		return salesActivityResult;
	}

	public void setSalesActivityResult(SalesActivityResult salesActivityResult) {
		this.salesActivityResult = salesActivityResult;
	}

	@Column(name="entry_date")
	@Temporal(TemporalType.DATE)
	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	@Column(name="task_completion_date")
	public int getTaskCompletionDate() {
		return taskCompletionDate;
	}

	public void setTaskCompletionDate(int taskCompletionDate) {
		this.taskCompletionDate = taskCompletionDate;
	}

}
