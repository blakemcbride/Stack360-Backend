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
package com.arahant.services.standard.hr.hrCheckList;
import com.arahant.annotation.Validation;
import com.arahant.business.BHRCheckListItem;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Feb 25, 2007
 *
 */
public class NewChecklistItemInput extends TransmitInputBase {

	@Validation (required=true)
	private String employeeStatusId;
	@Validation (table="hr_checklist_item",column="name",required=true)
	private String name;
	@Validation (type="date",required=false)
	private int activeDate;
	@Validation (type="date",required=false)
	private int inactiveDate;
	@Validation (table="hr_checklist_item", column="responsibility", required=true)
	private String responsibility;
	@Validation (table="hr_checklist_item", column="screen_id", required=false)
	private String screenId;
	@Validation (table="hr_checklist_item", column="screen_group_id", required=false)
	private String screenGroupId;
	@Validation (table="hr_checklist_item", column="company_form_id", required=false)
	private String companyFormId;

	public String getCompanyFormId() {
		return companyFormId;
	}

	public void setCompanyFormId(String companyFormId) {
		this.companyFormId = companyFormId;
	}
	
	/**
	 * @return Returns the employeeStatusId.
	 */
	public String getEmployeeStatusId() {
		return employeeStatusId;
	}

	/**
	 * @param employeeStatusId The employeeStatusId to set.
	 */
	public void setEmployeeStatusId(final String employeeStatusId) {
		this.employeeStatusId = employeeStatusId;
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

	public NewChecklistItemInput() {
		super();
	}

	/**
	 * @param cli
	 */
	void setData(final BHRCheckListItem cli) {
		cli.setName(name);
		cli.setEmployeeStatusId(employeeStatusId);
		cli.setActiveDate(activeDate);
		cli.setInactiveDate(inactiveDate);
		cli.setResponsibility(responsibility.charAt(0));

		if (!isEmpty(companyFormId))
			cli.setCompanyFormId(companyFormId);
		else
			cli.setCompanyForm(null);

		if (!isEmpty(screenId))
			cli.setScreen(screenId);
		else
			cli.setScreen(null);

		if (!isEmpty(screenGroupId))
			cli.setScreenGroup(screenGroupId);
		else
			cli.setScreenGroup(null);
	}

	/**
	 * @return Returns the activeDate.
	 */
	public int getActiveDate() {
		return activeDate;
	}

	/**
	 * @param activeDate The activeDate to set.
	 */
	public void setActiveDate(final int activeDate) {
		this.activeDate = activeDate;
	}

	/**
	 * @return Returns the inactiveDate.
	 */
	public int getInactiveDate() {
		return inactiveDate;
	}

	/**
	 * @param inactiveDate The inactiveDate to set.
	 */
	public void setInactiveDate(final int inactiveDate) {
		this.inactiveDate = inactiveDate;
	}

	public String getResponsibility() {
		return responsibility;
	}

	public void setResponsibility(String responsibility) {
		this.responsibility = responsibility;
	}

	public String getScreenGroupId() {
		return screenGroupId;
	}

	public void setScreenGroupId(String screenGroupId) {
		this.screenGroupId = screenGroupId;
	}

	public String getScreenId() {
		return screenId;
	}

	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}
}

	
