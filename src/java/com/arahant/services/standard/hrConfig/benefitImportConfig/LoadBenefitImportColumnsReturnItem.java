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
import com.arahant.business.BImportColumn;
import com.arahant.imports.GenericFileImport;

public class LoadBenefitImportColumnsReturnItem {
	
	public LoadBenefitImportColumnsReturnItem()
	{
		
	}

	LoadBenefitImportColumnsReturnItem (BImportColumn bc)
	{
		super();
		name=bc.getName();
		startPos=""+ bc.getStartPos();
		lastPos="" + bc.getLastPos();
		if (bc.getDateFormat().equals(""))
			dateFormat="(Not A Date)";
		else
			dateFormat=bc.getDateFormat();
		id =bc.getImportColumnId();

		int loop = 0;
		if (bc.getImportFilter().getImportType().getImportProgramName().equals("DRC Benefit Import"))
		{

			for(String st: GenericFileImport.availableColumns)
			{
				if (st.equals(bc.getName()))
					break;
				else
					loop++;
			}
			required=BImportColumn.getAvailableColumnsFilter1Required()[loop];
		}
	}

	LoadBenefitImportColumnsReturnItem(final String string, final boolean b) {
		name = string;
		startPos="";
		lastPos="";
		dateFormat="";
		required=b;
		id="";
	}

	private String name;
	private boolean required;
	private String startPos;
	private String lastPos;
	private String dateFormat;
	private String id;
	

	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public boolean getRequired()
	{
		return required;
	}
	public void setRequired(boolean required)
	{
		this.required=required;
	}
	public String getStartPos()
	{
		return startPos;
	}
	public void setStartPos(String startPos)
	{
		this.startPos=startPos;
	}
	public String getLastPos()
	{
		return lastPos;
	}
	public void setLastPos(String lastPos)
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}

	
