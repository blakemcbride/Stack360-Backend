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
import com.arahant.business.BHREmployeeEvalDetail;


/**
 * 
 *
 * Created on Feb 25, 2007
 *
 */
public class ListEmployeeEvalDetailsItem {

	public ListEmployeeEvalDetailsItem() {
		super();
	}
	
	
	/**
	 * @param details
	 */
	ListEmployeeEvalDetailsItem(final BHREmployeeEvalDetail d) {
		super();
		evalCategoryName=d.getEvalCategoryName();
		evalDetailId=d.getDetailId();
		employeeEvalId=d.getEmployeeEvalId();
		evalCategoryId=d.getEvalCategoryId();
		supervisorScore=d.getScore();
		employeeNotePreview=d.getEmployeeNotes();
		employeeScore=d.getEmployeeScore();
		if (employeeNotePreview!=null && employeeNotePreview.length()>100)
			employeeNotePreview=employeeNotePreview.substring(0,100);
		
	}


	private String evalCategoryName;
	private String evalDetailId;
	private String employeeEvalId;
	private String evalCategoryId;
	private short supervisorScore;
	private String employeeNotePreview;
	private short employeeScore;
	/**
	 * @return Returns the detailId.
	 */
	public String getEvalDetailId() {
		return evalDetailId;
	}
	/**
	 * @param detailId The detailId to set.
	 */
	public void setEvalDetailId(final String detailId) {
		this.evalDetailId = detailId;
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
	 * @return Returns the evalCategoryName.
	 */
	public String getEvalCategoryName() {
		return evalCategoryName;
	}
	/**
	 * @param evalCategoryName The evalCategoryName to set.
	 */
	public void setEvalCategoryName(final String evalCategoryName) {
		this.evalCategoryName = evalCategoryName;
	}
	/**
	 * @return Returns the notes.
	 */
	public String getEmployeeNotePreview() {
		return employeeNotePreview;
	}
	/**
	 * @param notes The notes to set.
	 */
	public void setEmployeeNotePreview(final String notes) {
		this.employeeNotePreview = notes;
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
}

	
