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
package com.arahant.services.standard.hr.hrCheckListDetail;
import com.arahant.business.BHRCheckListDetail;
import com.arahant.utils.DateUtils;


/**
 * 
 *
 * Created on Feb 25, 2007
 *
 */
public class ListChecklistDetailsItem {

	public ListChecklistDetailsItem() {
		super();
	}

	private String name;
	private String dateCompletedFormatted;
	private String supervisorLName;
	private String supervisorFName;
	private String checklistDetailId;
	private String supervisorId;
	private int dateCompleted;
	private String checklistItemId;
	
	/**
	 * @return Returns the checklistItemId.
	 */
	public String getChecklistItemId() {
		return checklistItemId;
	}

	/**
	 * @param checklistItemId The checklistItemId to set.
	 */
	public void setChecklistItemId(final String checklistItemId) {
		this.checklistItemId = checklistItemId;
	}

	/**
	 * @param item
	 */
	ListChecklistDetailsItem(final BHRCheckListDetail item) {
		super();
		
		this.name = item.getName();
		if (item.getDateCompleted()!=0)
			this.dateCompletedFormatted = DateUtils.getDateFormatted(item.getDateCompleted());
		supervisorFName=item.getSupervisorFName();
		supervisorLName=item.getSupervisorLName();
		checklistDetailId=item.getChecklistDetailId();
		dateCompleted=item.getDateCompleted();
		supervisorId=item.getSupervisorId();
		checklistItemId=item.getChecklistItemId();
		
	}

	/**
	 * @return Returns the checklistDetailId.
	 */
	public String getChecklistDetailId() {
		return checklistDetailId;
	}

	/**
	 * @param checklistDetailId The checklistDetailId to set.
	 */
	public void setChecklistDetailId(final String checklistDetailId) {
		this.checklistDetailId = checklistDetailId;
	}

	/**
	 * @return Returns the dateCompletedFormatted.
	 */
	public String getDateCompletedFormatted() {
		return dateCompletedFormatted;
	}

	/**
	 * @param dateCompletedFormatted The dateCompletedFormatted to set.
	 */
	public void setDateCompletedFormatted(final String dateCompletedFormatted) {
		this.dateCompletedFormatted = dateCompletedFormatted;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return Returns the supervisorFName.
	 */
	public String getSupervisorFName() {
		return supervisorFName;
	}

	/**
	 * @param supervisorFName The supervisorFName to set.
	 */
	public void setSupervisorFName(final String supervisorFName) {
		this.supervisorFName = supervisorFName;
	}

	/**
	 * @return Returns the supervisorLName.
	 */
	public String getSupervisorLName() {
		return supervisorLName;
	}

	/**
	 * @param supervisorLName The supervisorLName to set.
	 */
	public void setSupervisorLName(final String supervisorLName) {
		this.supervisorLName = supervisorLName;
	}

	/**
	 * @return Returns the dateCompleted.
	 */
	public int getDateCompleted() {
		return dateCompleted;
	}

	/**
	 * @param dateCompleted The dateCompleted to set.
	 */
	public void setDateCompleted(final int dateCompleted) {
		this.dateCompleted = dateCompleted;
	}

	/**
	 * @return Returns the supervisorId.
	 */
	public String getSupervisorId() {
		return supervisorId;
	}

	/**
	 * @param supervisorId The supervisorId to set.
	 */
	public void setSupervisorId(final String supervisorId) {
		this.supervisorId = supervisorId;
	}
	
}

	
