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
package com.arahant.services.standard.hr.personHistory;

public class ListHistoryComparisonsReturnItem {
	
	public ListHistoryComparisonsReturnItem()
	{
		
	}

	ListHistoryComparisonsReturnItem (String field, String oldVal, String newVal)
	{
		fieldName=field;
		oldValue=oldVal;
		newValue=newVal;
	}
	
	private String fieldName;
	private String oldValue;
	private String newValue;
	

	public String getFieldName()
	{
		return fieldName;
	}
	public void setFieldName(String fieldName)
	{
		this.fieldName=fieldName;
	}
	public String getOldValue()
	{
		return oldValue;
	}
	public void setOldValue(String oldValue)
	{
		this.oldValue=oldValue;
	}
	public String getNewValue()
	{
		return newValue;
	}
	public void setNewValue(String newValue)
	{
		this.newValue=newValue;
	}

}

	
