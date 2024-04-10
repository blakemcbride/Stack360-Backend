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
import com.arahant.annotation.Validation;
import com.arahant.business.BHREmployeeStatus;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Feb 22, 2007
 *
 */
public class SaveEmployeeStatusInput extends TransmitInputBase {

	@Validation (required=true)
	private String id;
	@Validation (table="hr_employee_status",column="name",required=true)
	private String name;
	@Validation (required=true)
	private String type;
	@Validation (min=1,max=1,required=true)
	private String dateType;
	@Validation (required=false)
	private String benefitType;
	@Validation (type="date", required=false)
	private int lastActiveDate;

	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
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
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(final String id) {
		this.id = id;
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
	 * @param x
	 */
	void setData(final BHREmployeeStatus x) {
		x.setName(name);
		x.setActiveFlag(type.equals("Y")?'Y':'N');
		x.setDateType(dateType);
		x.setBenefitType(benefitType.equals("B")?'B':(benefitType.equals("C")?'C':'N'));
		x.setLastActiveDate(lastActiveDate);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	

}

	
