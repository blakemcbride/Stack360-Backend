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
package com.arahant.services.standard.hr.studentStatusReport;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class GetReportInput extends TransmitInputBase {

	@Validation (required=true)
	private String termType;
	@Validation (required=true)
	private String term;
	@Validation (min=1900,max=3000,required=true)
	private int year;
	@Validation (required=true)
	private String reportType;
	@Validation (type="date",required=false)
	private int noticeDueDate;
	@Validation (type="date",required=false)
	private int noticeTermDate;
	@Validation (required=false)
	private String noticeType;
	@Validation (required=false)
	private Boolean excludeHandicap;
	

	public String getTermType()
	{
		return termType;
	}
	public void setTermType(String termType)
	{
		this.termType=termType;
	}
	public String getTerm()
	{
		return term;
	}
	public void setTerm(String term)
	{
		this.term=term;
	}
	public int getYear()
	{
		return year;
	}
	public void setYear(int year)
	{
		this.year=year;
	}
	public String getReportType()
	{
		return reportType;
	}
	public void setReportType(String reportType)
	{
		this.reportType=reportType;
	}
	public int getNoticeDueDate()
	{
		return noticeDueDate;
	}
	public void setNoticeDueDate(int noticeDueDate)
	{
		this.noticeDueDate=noticeDueDate;
	}
	public int getNoticeTermDate()
	{
		return noticeTermDate;
	}
	public void setNoticeTermDate(int noticeTermDate)
	{
		this.noticeTermDate=noticeTermDate;
	}
	public String getNoticeType()
	{
		return noticeType;
	}
	public void setNoticeType(String noticeType)
	{
		this.noticeType=noticeType;
	}

    public Boolean getExcludeHandicap() {
        return excludeHandicap;
    }

    public void setExcludeHandicap(Boolean excludeHandicap) {
        this.excludeHandicap = excludeHandicap;
    }


}

	
