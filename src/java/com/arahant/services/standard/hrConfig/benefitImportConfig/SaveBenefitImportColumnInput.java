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
package com.arahant.services.standard.hrConfig.benefitImportConfig;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BImportColumn;
import com.arahant.annotation.Validation;

public class SaveBenefitImportColumnInput extends TransmitInputBase {

	void setData(BImportColumn bc)
	{
		bc.setStartPos(startPos);
		bc.setLastPos(lastPos);
		bc.setDateFormat(dateFormat);
	}
	
	@Validation (table="import_column",column="import_column_id",required=true)
	private String id;
	@Validation (table="import_column",column="column_name",required=true)
	private String name;
	@Validation (table="import_column",column="start_pos",required=true)
	private int startPos;
	@Validation (table="import_column",column="last_pos",required=false)
	private int lastPos;
	@Validation (table="import_column",column="date_format",required=false)
	private String dateFormat;
	

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public int getStartPos()
	{
		return startPos;
	}
	public void setStartPos(int startPos)
	{
		this.startPos=startPos;
	}
	public int getLastPos()
	{
		return lastPos;
	}
	public void setLastPos(int lastPos)
	{
		this.lastPos=lastPos;
	}
	public String getDateFormat()
	{
		return dateFormat;
	}
	public void setDateFormat(String dateFormat)
	{
		this.dateFormat=dateFormat;
	}

}

	
