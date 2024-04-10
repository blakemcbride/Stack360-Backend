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
package com.arahant.services.standard.time.timesheetAccountingReview;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class MarkTimesheetsRejectedInput extends TransmitInputBase {

	@Validation (min=1,required=true)
	private String []timesheetIds;
	@Validation (min=0,max=1000,required=false)
	private String message;

	public String[] getTimesheetIds()
	{
            if (timesheetIds==null)
                return new String[0];
		return timesheetIds;
	}
	public void setTimesheetIds(String[] timesheetIds)
	{
		this.timesheetIds=timesheetIds;
	}
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message=message;
	}


}

	
