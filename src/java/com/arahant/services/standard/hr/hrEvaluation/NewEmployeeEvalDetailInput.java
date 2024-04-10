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
 * Created on Feb 25, 2007
 * 
 */
package com.arahant.services.standard.hr.hrEvaluation;
import com.arahant.annotation.Validation;
import com.arahant.business.BHREmployeeEvalDetail;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Feb 25, 2007
 *
 */
public class NewEmployeeEvalDetailInput extends TransmitInputBase {

	public NewEmployeeEvalDetailInput() {
		super();
	}
	
	void setData(final BHREmployeeEvalDetail d)
	{
		d.setEmployeeEvalId(employeeEvalId);
		d.setEvalCategoryId(evalCategoryId);
		d.setScore(supervisorScore);
		d.setNotes(supervisorNotes);
		d.setInternalSupervisorNotes(internalSupervisorNotes);
	}
	
	@Validation (required=true)
	private String employeeEvalId;
	@Validation (required=true)
	private String evalCategoryId;
	@Validation (min=1,max=100,table="hr_empl_eval_detail",column="score",required=false)
	private short supervisorScore;
	@Validation (required=false)
	private String supervisorNotes;
	@Validation (required=false)
	private String internalSupervisorNotes;
	/**
	 * @return Returns the internalSupervisorNotes.
	 */
	public String getInternalSupervisorNotes() {
		return internalSupervisorNotes;
	}

	/**
	 * @param internalSupervisorNotes The internalSupervisorNotes to set.
	 */
	public void setInternalSupervisorNotes(final String internalSupervisorNotes) {
		this.internalSupervisorNotes = internalSupervisorNotes;
	}

	/**
	 * @return Returns the employeeEvalId.
	 */
	public String getEmployeeEvalId() {
		return employeeEvalId;
	}
	/**
	 * @param employeeEvalId The employeeEvalId to set.
	 */
	public void setEmployeeEvalId(final String employeeEvalId) {
		this.employeeEvalId = employeeEvalId;
	}
	/**
	 * @return Returns the evalCategoryId.
	 */
	public String getEvalCategoryId() {
		return evalCategoryId;
	}
	/**
	 * @param evalCategoryId The evalCategoryId to set.
	 */
	public void setEvalCategoryId(final String evalCategoryId) {
		this.evalCategoryId = evalCategoryId;
	}
	/**
	 * @return Returns the notes.
	 */
	public String getSupervisorNotes() {
		return supervisorNotes;
	}
	/**
	 * @param notes The notes to set.
	 */
	public void setSupervisorNotes(final String notes) {
		this.supervisorNotes = notes;
	}
	/**
	 * @return Returns the score.
	 */
	public short getSupervisorScore() {
		return supervisorScore;
	}
	/**
	 * @param score The score to set.
	 */
	public void setSupervisorScore(final short score) {
		this.supervisorScore = score;
	}
}

	
