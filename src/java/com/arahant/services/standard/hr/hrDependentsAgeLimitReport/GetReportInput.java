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
 * 
 */
package com.arahant.services.standard.hr.hrDependentsAgeLimitReport;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;

 

/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class GetReportInput extends TransmitInputBase {

	@Validation (min=0,max=99,required=true)
	private int age;
	@Validation (min=1000,max=3000,required=true)
	private int year;
	@Validation (min=1,required=false)
	private String []benefitCategoryCategoryIds;
	@Validation (type="date",required=false)
	private int inactiveAsOf;
	@Validation (required=false)
	private boolean excludeHandicap;
	@Validation (required=false)
	private boolean excludeStudent;
	
	/**
	 * @return Returns the benefitCategoryCategoryIds.
	 */
	public String[] getBenefitCategoryCategoryIds() {
            if (benefitCategoryCategoryIds==null)
                return new String[0];
		return benefitCategoryCategoryIds;
	}
	/**
	 * @param benefitCategoryCategoryIds The benefitCategoryCategoryIds to set.
	 */
	public void setBenefitCategoryCategoryIds(final String[] benefitCategoryCategoryIds) {
		this.benefitCategoryCategoryIds = benefitCategoryCategoryIds;
	}
	/**
	 * @return Returns the minAge.
	 */
	public int getAge() {
		return age;
	}
	/**
	 * @param minAge The minAge to set.
	 */
	public void setAge(final int minAge) {
		this.age = minAge;
	}
	/**
	 * @return Returns the inactiveAsOf.
	 */
	public int getInactiveAsOf() {
		return inactiveAsOf;
	}
	/**
	 * @param inactiveAsOf The inactiveAsOf to set.
	 */
	public void setInactiveAsOf(final int inactiveAsOf) {
		this.inactiveAsOf = inactiveAsOf;
	}
	/**
	 * @return Returns the year.
	 */
	public int getYear() {
		return year;
	}
	/**
	 * @param year The year to set.
	 */
	public void setYear(final int year) {
		this.year = year;
	}

	public boolean getExcludeHandicap() {
		return excludeHandicap;
	}

	public void setExcludeHandicap(boolean excludeHandicap) {
		this.excludeHandicap = excludeHandicap;
	}

	public boolean getExcludeStudent() {
		return excludeStudent;
	}

	public void setExcludeStudent(boolean excludeStudent) {
		this.excludeStudent = excludeStudent;
	}
	
	

}

	
