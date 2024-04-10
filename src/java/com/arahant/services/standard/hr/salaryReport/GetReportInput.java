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
 * Created on Mar 16, 2007
 * 
 */
package com.arahant.services.standard.hr.salaryReport;
import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Mar 16, 2007
 *
 */
public class GetReportInput extends TransmitInputBase {

	@Validation (required=false)
	private boolean employeeName;
	@Validation (required=false)
	private boolean startDate; 
	@Validation (required=false)
	private boolean lastRaiseDate;
	@Validation (required=false)
	private boolean currentSalary;
	@Validation (required=false)
	private boolean lastRaiseAmount;
	@Validation (required=false)
	private boolean lastEvaluationDate;
	@Validation (required=false)
	private boolean timeWithCompany;
	@Validation (required=false)
	private boolean timeSinceLastRaise;
	@Validation (required=false)
	private boolean timeSinceLastReview;
	@Validation (required=false)
	private boolean ssn;
	
	@Validation (min=1,max=4,required=false)
	private int sortType; //last name = 0 current salary=1 last raise date=2 start date=3 last evaluation date=4
	@Validation (required=false)
	private boolean sortAsc;
	
	/**
	 * @return Returns the sortAsc.
	 */
	public boolean isSortAsc() {
		return sortAsc;
	}
	/**
	 * @param sortAsc The sortAsc to set.
	 */
	public void setSortAsc(final boolean sortAsc) {
		this.sortAsc = sortAsc;
	}
	/**
	 * @return Returns the sortType.
	 */
	public int getSortType() {
		return sortType;
	}
	/**
	 * @param sortType The sortType to set.
	 */
	public void setSortType(final int sortType) {
		this.sortType = sortType;
	}
	/**
	 * @return Returns the currentSalary.
	 */
	public boolean isCurrentSalary() {
		return currentSalary;
	}
	/**
	 * @param currentSalary The currentSalary to set.
	 */
	public void setCurrentSalary(final boolean currentSalary) {
		this.currentSalary = currentSalary;
	}
	/**
	 * @return Returns the employeeName.
	 */
	public boolean isEmployeeName() {
		return employeeName;
	}
	/**
	 * @param employeeName The employeeName to set.
	 */
	public void setEmployeeName(final boolean employeeName) {
		this.employeeName = employeeName;
	}
	/**
	 * @return Returns the lastEvaluationDate.
	 */
	public boolean isLastEvaluationDate() {
		return lastEvaluationDate;
	}
	/**
	 * @param lastEvaluationDate The lastEvaluationDate to set.
	 */
	public void setLastEvaluationDate(final boolean lastEvaluationDate) {
		this.lastEvaluationDate = lastEvaluationDate;
	}
	/**
	 * @return Returns the lastRaiseAmount.
	 */
	public boolean isLastRaiseAmount() {
		return lastRaiseAmount;
	}
	/**
	 * @param lastRaiseAmount The lastRaiseAmount to set.
	 */
	public void setLastRaiseAmount(final boolean lastRaiseAmount) {
		this.lastRaiseAmount = lastRaiseAmount;
	}
	/**
	 * @return Returns the lastRaiseDate.
	 */
	public boolean isLastRaiseDate() {
		return lastRaiseDate;
	}
	/**
	 * @param lastRaiseDate The lastRaiseDate to set.
	 */
	public void setLastRaiseDate(final boolean lastRaiseDate) {
		this.lastRaiseDate = lastRaiseDate;
	}
	/**
	 * @return Returns the startDate.
	 */
	public boolean isStartDate() {
		return startDate;
	}
	/**
	 * @param startDate The startDate to set.
	 */
	public void setStartDate(final boolean startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return Returns the timeSinceLastRaise.
	 */
	public boolean isTimeSinceLastRaise() {
		return timeSinceLastRaise;
	}
	/**
	 * @param timeSinceLastRaise The timeSinceLastRaise to set.
	 */
	public void setTimeSinceLastRaise(final boolean timeSinceLastRaise) {
		this.timeSinceLastRaise = timeSinceLastRaise;
	}
	/**
	 * @return Returns the timeSinceLastReview.
	 */
	public boolean isTimeSinceLastReview() {
		return timeSinceLastReview;
	}
	/**
	 * @param timeSinceLastReview The timeSinceLastReview to set.
	 */
	public void setTimeSinceLastReview(final boolean timeSinceLastReview) {
		this.timeSinceLastReview = timeSinceLastReview;
	}
	/**
	 * @return Returns the timeWithCompany.
	 */
	public boolean isTimeWithCompany() {
		return timeWithCompany;
	}
	/**
	 * @param timeWithCompany The timeWithCompany to set.
	 */
	public void setTimeWithCompany(final boolean timeWithCompany) {
		this.timeWithCompany = timeWithCompany;
	}
	/**
	 * @return Returns the ssn.
	 */
	public boolean isSsn() {
		return ssn;
	}
	/**
	 * @param ssn The ssn to set.
	 */
	public void setSsn(final boolean ssn) {
		this.ssn = ssn;
	}
}

	
