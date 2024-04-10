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
package com.arahant.services.standard.misc.company;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchCompanyInput extends TransmitInputBase {

	@Validation (required=false)
	private String id;
	@Validation (min=2,max=5,required=false)
	private int idSearchType;
	@Validation (required=false)
	private String mainContactFirstName;
	@Validation (min=2,max=5,required=false)
	private int mainContactFirstNameSearchType;
	@Validation (required=false)
	private String mainContactLastName;
	@Validation (min=2,max=5,required=false)
	private int mainContactLastNameSearchType;
	@Validation (required=false)
	private String name;
	@Validation (min=2,max=5,required=false)
	private int nameSearchType;
	@Validation (required=false)
	private boolean sortAsc;
	@Validation (required=false)
	private String sortOn;
	

	public String getId()
	{
		return modifyForSearch(id,idSearchType);
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public int getIdSearchType()
	{
		return idSearchType;
	}
	public void setIdSearchType(int idSearchType)
	{
		this.idSearchType=idSearchType;
	}
	public String getMainContactFirstName()
	{
		return modifyForSearch(mainContactFirstName,mainContactFirstNameSearchType);
	}
	public void setMainContactFirstName(String mainContactFirstName)
	{
		this.mainContactFirstName=mainContactFirstName;
	}
	public int getMainContactFirstNameSearchType()
	{
		return mainContactFirstNameSearchType;
	}
	public void setMainContactFirstNameSearchType(int mainContactFirstNameSearchType)
	{
		this.mainContactFirstNameSearchType=mainContactFirstNameSearchType;
	}
	public String getMainContactLastName()
	{
		return modifyForSearch(mainContactLastName,mainContactLastNameSearchType);
	}
	public void setMainContactLastName(String mainContactLastName)
	{
		this.mainContactLastName=mainContactLastName;
	}
	public int getMainContactLastNameSearchType()
	{
		return mainContactLastNameSearchType;
	}
	public void setMainContactLastNameSearchType(int mainContactLastNameSearchType)
	{
		this.mainContactLastNameSearchType=mainContactLastNameSearchType;
	}
	public String getName()
	{
		return modifyForSearch(name,nameSearchType);
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public int getNameSearchType()
	{
		return nameSearchType;
	}
	public void setNameSearchType(int nameSearchType)
	{
		this.nameSearchType=nameSearchType;
	}
	public boolean getSortAsc()
	{
		return sortAsc;
	}
	public void setSortAsc(boolean sortAsc)
	{
		this.sortAsc=sortAsc;
	}
	public String getSortOn()
	{
		return sortOn;
	}
	public void setSortOn(String sortOn)
	{
		this.sortOn=sortOn;
	}


}

	
