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
 * Created on Feb 22, 2007
 * 
 */
package com.arahant.services.standard.hr.hrStatus;
import com.arahant.business.BHREmployeeStatus;


/**
 * 
 *
 * Created on Feb 22, 2007
 *
 */
public class ListEmployeeStatusesItem {

	private String EmployeeStatusId;
	private String name;
	private String type;
	private String typeFormatted;
	private String dateType, dateTypeFormatted;
	private String benefitType, benefitTypeFormatted;
	private int lastActiveDate;
	
	
	public ListEmployeeStatusesItem()
	{
		
	}
	/**
	 * @return Returns the EmployeeStatusId.
	 */
	public String getEmployeeStatusId() {
		return EmployeeStatusId;
	}

	/**
	 * @param EmployeeStatusId The EmployeeStatusId to set.
	 */
	public void setEmployeeStatusId(final String EmployeeStatusId) {
		this.EmployeeStatusId = EmployeeStatusId;
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
	 * @param stat
	 */
	ListEmployeeStatusesItem(final BHREmployeeStatus stat) {
		EmployeeStatusId=stat.getEmployeeStatusId();
		name=stat.getName();
		type = stat.getActiveFlag() + "";
		
		if (type.equals("Y"))
			typeFormatted="Active";
		else
			typeFormatted="Inactive";
		
		dateType=stat.getDateType();
		dateTypeFormatted=stat.getDateTypeFormatted();
		
		benefitType = stat.getBenefitType()+"";
		benefitTypeFormatted = stat.getBenefitTypeFormatted();
		lastActiveDate=stat.getLastActiveDate();
	}

	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	/**
	 * @return Returns the typeFormatted.
	 */
	public String getTypeFormatted() {
		return typeFormatted;
	}
	/**
	 * @param typeFormatted The typeFormatted to set.
	 */
	public void setTypeFormatted(final String typeFormatted) {
		this.typeFormatted = typeFormatted;
	}

	/**
	 * @return Returns the benefitType.
	 */
	public String getBenefitType() {
		return benefitType;
	}
	/**
	 * @param benefitType The benefitType to set.
	 */
	public void setBenefitType(final String benefitType) {
		this.benefitType = benefitType;
	}
	/**
	 * @return Returns the benefitTypeFormatted.
	 */
	public String getBenefitTypeFormatted() {
		return benefitTypeFormatted;
	}
	/**
	 * @param benefitTypeFormatted The benefitTypeFormatted to set.
	 */
	public void setBenefitTypeFormatted(final String benefitTypeFormatted) {
		this.benefitTypeFormatted = benefitTypeFormatted;
	}
	/**
	 * @return Returns the dateType.
	 */
	public String getDateType() {
		return dateType;
	}
	/**
	 * @param dateType The dateType to set.
	 */
	public void setDateType(final String dateType) {
		this.dateType = dateType;
	}
	/**
	 * @return Returns the dateTypeFormatted.
	 */
	public String getDateTypeFormatted() {
		return dateTypeFormatted;
	}
	/**
	 * @param dateTypeFormatted The dateTypeFormatted to set.
	 */
	public void setDateTypeFormatted(final String dateTypeFormatted) {
		this.dateTypeFormatted = dateTypeFormatted;
	}
	
}

	
