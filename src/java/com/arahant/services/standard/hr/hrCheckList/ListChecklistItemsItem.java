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
import com.arahant.business.BHRCheckListItem;
import com.arahant.business.BScreen;
import com.arahant.business.BScreenGroup;
import com.arahant.utils.DateUtils;


/**
 * 
 *
 * Created on Feb 25, 2007
 *
 */
public class ListChecklistItemsItem {


	public ListChecklistItemsItem() {
		super();
	}

	/**
	 * @param item
	 */
	ListChecklistItemsItem(final BHRCheckListItem item) {
		super();
		itemId=item.getItemId();
		name=item.getName();
		employeeStatusId=item.getEmployeeStatusId();
		activeDate=item.getActiveDate();
		activeDateFormatted=DateUtils.getDateFormatted(activeDate);
		inactiveDate=item.getInactiveDate();
		inactiveDateFormatted=DateUtils.getDateFormatted(inactiveDate);
		responsibility=item.getResponsibility() + "";
		screenId=item.getScreen() == null?"":item.getScreen().getScreenId();
		screenGroupId=item.getScreenGroup() == null?"":item.getScreenGroup().getScreenGroupId();

		if(screenId != null && !screenId.equals(""))
		{
			BScreen bs = new BScreen(screenId);
			screenOrGroupName=bs.getName();
		}
		else if (screenGroupId != null && !screenGroupId.equals(""))
		{
			BScreenGroup bsg = new BScreenGroup(screenGroupId);
			screenOrGroupName=bsg.getName();
		}
		else
			screenOrGroupName="";
	}
	
	private String itemId;
	private String employeeStatusId;
	private String name;
	private String activeDateFormatted;
	private int activeDate;
	private String inactiveDateFormatted;
	private int inactiveDate;
	private String responsibility;
	private String screenId;
	private String screenGroupId;
	private String screenOrGroupName;

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
	 * @return Returns the itemId.
	 */
	public String getItemId() {
		return itemId;
	}

	/**
	 * @param itemId The itemId to set.
	 */
	public void setItemId(final String itemId) {
		this.itemId = itemId;
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
	 * @return Returns the activeDateFormatted.
	 */
	public String getActiveDateFormatted() {
		return activeDateFormatted;
	}

	/**
	 * @param activeDateFormatted The activeDateFormatted to set.
	 */
	public void setActiveDateFormatted(final String activeDateFormatted) {
		this.activeDateFormatted = activeDateFormatted;
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

	/**
	 * @return Returns the inactiveDateFormatted.
	 */
	public String getInactiveDateFormatted() {
		return inactiveDateFormatted;
	}

	/**
	 * @param inactiveDateFormatted The inactiveDateFormatted to set.
	 */
	public void setInactiveDateFormatted(final String inactiveDateFormatted) {
		this.inactiveDateFormatted = inactiveDateFormatted;
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

	public String getScreenOrGroupName() {
		return screenOrGroupName;
	}

	public void setScreenOrGroupName(String screenOrGroupName) {
		this.screenOrGroupName = screenOrGroupName;
	}
}

	
