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
package com.arahant.services.standard.crm.appointment;
import com.arahant.business.BAppointment;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.DateUtils;
import java.text.SimpleDateFormat;


/**
 * 
 *
 *
 */
public class SearchAppointmentsReturnItem {
	
	public SearchAppointmentsReturnItem()
	{
		
	}
	SearchAppointmentsReturnItem (BAppointment bc, boolean group)
	{
		
		date=bc.getDate();
		time=bc.getTime();
		length=bc.getLength();
		status=bc.getStatus();
		type=bc.getType();
		id=bc.getId();
		purpose=TransmitReturnBase.preview(bc.getPurpose());

		SimpleDateFormat sdf=new SimpleDateFormat("EEEEEEEEEE");
		day=sdf.format(DateUtils.getDate(date));
		
		if (date==DateUtils.now())
			day="Today";
		
		
		companyName=bc.getCompanyName();
		companyId=bc.getCompanyId();
		alternateGroup=group;
	}
	
	private String companyId;
	private int date;
	private int time;
	private int length;
	private String status;
	private String type;
	private String id;
	private String purpose;
	private String day;
	private String companyName;
	private boolean alternateGroup;

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public int getDate()
	{
		return date;
	}
	public void setDate(int date)
	{
		this.date=date;
	}
	public int getTime()
	{
		return time;
	}
	public void setTime(int time)
	{
		this.time=time;
	}
	public int getLength()
	{
		return length;
	}
	public void setLength(int length)
	{
		this.length=length;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status=status;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type=type;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public boolean getAlternateGroup() {
		return alternateGroup;
	}

	public void setAlternateGroup(boolean alternateGroup) {
		this.alternateGroup = alternateGroup;
	}
	
	

}

	
