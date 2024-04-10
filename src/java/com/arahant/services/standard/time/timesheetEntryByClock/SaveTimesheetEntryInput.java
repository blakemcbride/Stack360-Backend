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
package com.arahant.services.standard.time.timesheetEntryByClock;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BTimesheet;
import com.arahant.annotation.Validation;
import com.arahant.utils.DateUtils;
import java.util.Date;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SaveTimesheetEntryInput extends TransmitInputBase {

	void setData(BTimesheet bc)
	{
		bc.setEndDate(finalDate);
		bc.setEndTime(finalTime);
		bc.setStartDate(startDate);
		bc.setStartTime(startTime);

		if (finalTime!=-1)
		{
			Date end=DateUtils.getDate(bc.getEndDate(), bc.getEndTime());
			Date start=DateUtils.getDate(bc.getStartDate(), bc.getStartTime());

			double timeDif=end.getTime()-start.getTime();
			timeDif=timeDif/1000/60/60;

			bc.setTotalHours(BTimesheet.round(timeDif));			
		}
	}
	
	@Validation (required=false, type="date")
	private int finalDate;
	@Validation (required=false, type="time")
	private int finalTime;
	@Validation (required=true, type="date")
	private int startDate;
	@Validation (required=true, type="time")
	private int startTime;
	@Validation (min=1,max=16,required=true)
	private String id;
	

	public int getFinalDate()
	{
		return finalDate;
	}
	public void setFinalDate(int finalDate)
	{
		this.finalDate=finalDate;
	}
	public int getFinalTime()
	{
		return finalTime;
	}
	public void setFinalTime(int finalTime)
	{
		this.finalTime=finalTime;
	}
	public int getStartDate()
	{
		return startDate;
	}
	public void setStartDate(int startDate)
	{
		this.startDate=startDate;
	}
	public int getStartTime()
	{
		return startTime;
	}
	public void setStartTime(int startTime)
	{
		this.startTime=startTime;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}

}

	
