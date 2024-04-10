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
package com.arahant.services.standard.hr.hrEvaluationResponse;
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
		d.setEmployeeScore(employeeScore);
		d.setEmployeeNotes(employeeNotes);
	}
	
	@Validation (required=true)
	private String employeeEvalId;
	@Validation (required=true)
	private String evalCategoryId;
	@Validation (min=1,max=100,table="hr_empl_eval_detail",column="e_score",required=false)
	private short employeeScore;
	@Validation (table="hr_empl_eval_detail",column="e_notes",required=false)
	private String employeeNotes;
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
	 * @return Returns the employeeNotes.
	 */
	public String getEmployeeNotes() {
		return employeeNotes;
	}

	/**
	 * @param employeeNotes The employeeNotes to set.
	 */
	public void setEmployeeNotes(final String employeeNotes) {
		this.employeeNotes = employeeNotes;
	}

	/**
	 * @return Returns the employeeScore.
	 */
	public short getEmployeeScore() {
		return employeeScore;
	}

	/**
	 * @param employeeScore The employeeScore to set.
	 */
	public void setEmployeeScore(final short employeeScore) {
		this.employeeScore = employeeScore;
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
	
	
}

	
