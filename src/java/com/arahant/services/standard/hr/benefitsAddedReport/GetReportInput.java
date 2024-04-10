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
package com.arahant.services.standard.hr.benefitsAddedReport;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class GetReportInput extends TransmitInputBase {

	@Validation (type="date",required=true)
	private int startDate;
	@Validation (type="date",required=true)
	private int finalDate;
	@Validation (required=false)
	private String [] reasonIds;
	@Validation (required=false)
	private String [] benefitIds;
	

	public int getStartDate()
	{
		return startDate;
	}
	public void setStartDate(int startDate)
	{
		this.startDate=startDate;
	}
	public int getFinalDate()
	{
		return finalDate;
	}
	public void setFinalDate(int finalDate)
	{
		this.finalDate=finalDate;
	}
	public String [] getReasonIds()
	{
		if (reasonIds==null)
			reasonIds= new String [0];
		return reasonIds;
	}
	public void setReasonIds(String [] reasonIds)
	{
		this.reasonIds=reasonIds;
	}

	public String[] getBenefitIds() {
		if (benefitIds==null)
			benefitIds=new String[0];
		return benefitIds;
	}

	public void setBenefitIds(String[] benefitIds) {
		this.benefitIds = benefitIds;
	}


}

	
