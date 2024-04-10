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
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Feb 25, 2007
 *
 */
public class LoadEmployeeEvalDetailReturn extends TransmitReturnBase  {

	
	public LoadEmployeeEvalDetailReturn() {
		super();
	}
	
	private String evalCategoryName;
	//private String scoreFormatted;
	//private String detailId;
	//private String employeeEvalId;
	//private String evalCategoryId;
	private short supervisorScore;
	private String supervisorNotes;
	private String employeeNotes;
	private short employeeScore;
	private boolean hasSupervisorComments;

	
	
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
	 * @return Returns the internalSupervisorNotes.
	 *
	public String getInternalSupervisorNotes() {
		return internalSupervisorNotes;
	}

	/**
	 * @param internalSupervisorNotes The internalSupervisorNotes to set.
	 *
	public void setInternalSupervisorNotes(String internalSupervisorNotes) {
		this.internalSupervisorNotes = internalSupervisorNotes;
	}
*/
	void setData(final BHREmployeeEvalDetail d)
	{
		evalCategoryName=d.getEvalCategoryName();
		//scoreFormatted=d.getScore()+"";
		//detailId=d.getDetailId();
		//employeeEvalId=d.getEmployeeEvalId();
		//evalCategoryId=d.getEvalCategoryId();
		supervisorScore=d.getScore();
		supervisorNotes=d.getNotes();
		employeeNotes=d.getEmployeeNotes();
		employeeScore=d.getEmployeeScore();
		//internalSupervisorNotes=d.getInternalSupervisorNotes();
		hasSupervisorComments=d.getInternalSupervisorNotes()!=null && !d.getInternalSupervisorNotes().trim().equals("");
	}
	
	/**
	 * @return Returns the detailId.
	 *
	public String getDetailId() {
		return detailId;
	}
	/**
	 * @param detailId The detailId to set.
	 *
	public void setDetailId(final String detailId) {
		this.detailId = detailId;
	}
	/**
	 * @return Returns the employeeEvalId.
	 *
	public String getEmployeeEvalId() {
		return employeeEvalId;
	}
	/**
	 * @param employeeEvalId The employeeEvalId to set.
	 *
	public void setEmployeeEvalId(final String employeeEvalId) {
		this.employeeEvalId = employeeEvalId;
	}
	/**
	 * @return Returns the evalCategoryId.
	 *
	public String getEvalCategoryId() {
		return evalCategoryId;
	}
	/**
	 * @param evalCategoryId The evalCategoryId to set.
	 *
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
	/**
	 * @return Returns the scoreFormatted.
	 *
	public String getScoreFormatted() {
		return scoreFormatted;
	}
	/**
	 * @param scoreFormatted The scoreFormatted to set.
	 *
	public void setScoreFormatted(final String scoreFormatted) {
		this.scoreFormatted = scoreFormatted;
	}
	
	*/

	/**
	 * @return Returns the hasSupervisorComments.
	 */
	public boolean getHasSupervisorComments() {
		return hasSupervisorComments;
	}

	/**
	 * @param hasSupervisorComments The hasSupervisorComments to set.
	 */
	public void setHasSupervisorComments(final boolean hasSupervisorComments) {
		this.hasSupervisorComments = hasSupervisorComments;
	}
}

	
