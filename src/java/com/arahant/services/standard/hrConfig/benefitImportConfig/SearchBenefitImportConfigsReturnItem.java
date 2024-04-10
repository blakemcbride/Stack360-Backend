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
import com.arahant.business.BImportType;

public class SearchBenefitImportConfigsReturnItem {
	
	public SearchBenefitImportConfigsReturnItem()
	{
		
	}

	SearchBenefitImportConfigsReturnItem (BImportType bift)
	{
		name=bift.getImportName();
		id=bift.getImportFileTypeId();
		typex=bift.getFileFormat();
		source=bift.getImportSource();
		allCompanies = bift.getAllCompanies();
	}
	
	private String name;
	private String id;
	private String typex;
	private String source;
	private boolean allCompanies;
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getTypex()
	{
		return typex;
	}
	public void setTypex(String typex)
	{
		this.typex=typex;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public boolean getAllCompanies() {
		return allCompanies;
	}

	public void setAllCompanies(boolean allCompanies) {
		this.allCompanies = allCompanies;
	}
}

	
