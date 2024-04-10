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


package com.arahant.services.standard.hr.eeo1Survey;

import com.arahant.business.BOrgGroup;

public class ListDefaultEstablishmentsReturnItem {
	
	public ListDefaultEstablishmentsReturnItem()
	{
		;
	}

	ListDefaultEstablishmentsReturnItem (BOrgGroup bc)
	{
		
		id = bc.getId();
		name = bc.getName();
		included = true;
		headquarters = bc.getEeo1Headquarters();
		filedLastYear = bc.getEeo1FiledLastYear();
		unitNumber = bc.getEeo1UnitId();

	}
	
	private String id;
	private String name;
	private boolean included;
	private boolean headquarters;
	private boolean filedLastYear;
	private String unitNumber;
	

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
	public boolean getIncluded()
	{
		return included;
	}
	public void setIncluded(boolean included)
	{
		this.included=included;
	}
	public boolean getHeadquarters()
	{
		return headquarters;
	}
	public void setHeadquarters(boolean headquarters)
	{
		this.headquarters=headquarters;
	}
	public boolean getFiledLastYear()
	{
		return filedLastYear;
	}
	public void setFiledLastYear(boolean filedLastYear)
	{
		this.filedLastYear=filedLastYear;
	}
	public String getUnitNumber()
	{
		return unitNumber;
	}
	public void setUnitNumber(String unitNumber)
	{
		this.unitNumber=unitNumber;
	}
}

	
