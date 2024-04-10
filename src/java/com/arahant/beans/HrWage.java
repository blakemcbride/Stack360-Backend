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



@Entity
@Table(name="hr_wage")
public class HrWage extends ArahantBean implements java.io.Serializable, ArahantSaveNotify {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "hr_wage";

	//public static final short BONUS_TYPE=3;
	//public static final short HOURLY_TYPE=1;
	//public static final short ANNUAL_TYPE=2;

	// Fields    

	private String wageId;
	public static final String WAGEID = "wageId";
	private Person employee;
	public static final String EMPLOYEE = "employee";
	private HrPosition hrPosition;
	public static final String HRPOSITION = "hrPosition";
	//private short wageType;
	public static final String WAGETYPE = "wageType";
	private WageType wageType;
	private double wageAmount;
	public static final String WAGEAMOUNT = "wageAmount";
	private int effectiveDate;
	public static final String EFFECTIVEDATE = "effectiveDate";
	private String notes;
	public static final String NOTES = "notes";
	private String employeeId;
	public static final String EMPLOYEEID = "employeeId";
	

	// Constructors

	/** default constructor */
	public HrWage() {
	}

	
	// Property accessors
	@Id
	@Column (name="wage_id")
	public String getWageId() {
		return this.wageId;
	}

	public void setWageId(final String wageId) {
		firePropertyChange("wageId", this.wageId, wageId);
		this.wageId = wageId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="employee_id")
	public Person getEmployee() {
		return this.employee;
	}

	public void setEmployee(final Person employee) {
		if (employee!=null)
			setEmployeeId(employee.getPersonId());
		this.employee = employee;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="position_id")
	public HrPosition getHrPosition() {
		return this.hrPosition;
	}

	public void setHrPosition(final HrPosition hrPosition) {
		this.hrPosition = hrPosition;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="wage_type_id")
	public WageType getWageType() {
		return wageType;
	}

	public void setWageType(WageType wageType) {
		this.wageType = wageType;
	}

	@Column (name="wage_amount")
	public double getWageAmount() {
		firePropertyChange("wageAmount", this.wageAmount, wageAmount);
		return this.wageAmount;
	}

	public void setWageAmount(final double wageAmount) {
		this.wageAmount = wageAmount;
	}

	@Column (name="effective_date")
	public int getEffectiveDate() {
		return this.effectiveDate;
	}

	public void setEffectiveDate(final int effectiveDate) {
		firePropertyChange("effectiveDate", this.effectiveDate, effectiveDate);
		this.effectiveDate = effectiveDate;
	}

	@Column (name="employee_id",insertable=false,updatable=false)
	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		firePropertyChange("employeeId", this.employeeId, employeeId);
		this.employeeId = employeeId;
	}

	
	@Column (name="notes")
	public String getNotes() {
		return this.notes;
	}

	public void setNotes(final String notes) {
		firePropertyChange("notes", this.notes, notes);
		this.notes = notes;
	}
	/* (non-Javadoc)
	 * @see com.arahant.beans.ArahantBean#keyColumn()
	 */
	@Override
	public String keyColumn() {
		return "wage_id";
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
		setWageId(IDGenerator.generate(this));	
		return wageId;
	}
	

    @Override
	public boolean equals(Object o)
	{
		if (wageId==null && o==null)
			return true;
		if (wageId!=null && o instanceof HrWage)
			return wageId.equals(((HrWage)o).getWageId());
		
		return false;
	}
	
    @Override
	public int hashCode()
	{
		if (wageId==null)
			return 0;
		return wageId.hashCode();
	}

	@Override
	public String notifyId() {
		return wageId;
	}

	@Override
	public String notifyClassName() {
		return "HrWage";
	}
	
	
}
