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
package com.arahant.services.standard.hr.benefitAssignment;

import com.arahant.business.BHREmplDependent;
import com.arahant.services.TransmitReturnBase;



/**
 * 
 *
 *
 */
public class NewAssignedBenefitConfigReturn extends TransmitReturnBase {
	private SponsoringEmployees[] sponsoringEmployees;
  
	private String personConfigId;
	private String []warnings;
	private String []errors;
	
	public void setPersonConfigId(final String i)
	{
		personConfigId=i;
	}
	
	public String getPersonConfigId()
	{
		return personConfigId;
	}

	/**
	 * @return Returns the errors.
	 */
	public String[] getErrors() {
		return errors;
	}

	/**
	 * @param errors The errors to set.
	 */
	public void setErrors(String[] errors) {
		this.errors = errors;
	}

	/**
	 * @return Returns the warnings.
	 */
	public String[] getWarnings() {
		return warnings;
	}

	/**
	 * @param warnings The warnings to set.
	 */
	public void setWarnings(String[] warnings) {
		this.warnings = warnings;
	}

	/**
	 * @param relationships
	 */
	void setSponsoringEmployees(BHREmplDependent[] relationships) {
		sponsoringEmployees=new SponsoringEmployees[relationships.length];
		for (int loop=0;loop<relationships.length;loop++)
			sponsoringEmployees[loop]=new SponsoringEmployees(relationships[loop]);
	}
	
        
        public void setSponsoringEmployees(SponsoringEmployees[] sponsoringEmployees)
	{
		this.sponsoringEmployees=sponsoringEmployees;
	}
        
	public SponsoringEmployees[] getSponsoringEmployees()
	{
		return this.sponsoringEmployees;
	}
	
	void addErrors(String[] errors) {
		if (errors!=null && errors.length>0) {
			if (this.errors==null || this.errors.length==0) {
				this.errors = errors;
			} else {
				String[] allErrors = new String[this.errors.length + errors.length];
				int allIdx = 0;
				
				for (int idx = 0; idx < this.errors.length; idx++, allIdx++) {
					allErrors[allIdx] = this.errors[idx];
				}
				for (int idx = 0; idx < errors.length; idx++, allIdx++) {
					allErrors[allIdx] = this.errors[idx];
				}
				
				this.errors = allErrors;
			}
		}
	}

	void addWarnings(String[] warnings) {
		if (warnings!=null && warnings.length>0) {
			if (this.warnings==null || this.warnings.length==0) {
				this.warnings = warnings;
			} else {
				String[] allWarnings = new String[this.warnings.length + warnings.length];
				int allIdx = 0;
				
				for (int idx = 0; idx < this.warnings.length; idx++, allIdx++) {
					allWarnings[allIdx] = this.warnings[idx];
				}
				for (int idx = 0; idx < warnings.length; idx++, allIdx++) {
					allWarnings[allIdx] = this.warnings[idx];
				}
				
				this.warnings = allWarnings;
			}
		}
	}
	
	boolean hasErrorsOrWarnings() {
		return (this.warnings!=null && this.warnings.length>0) || (this.errors!=null && this.errors.length>0); 
	}
	
}

	
