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
import com.arahant.business.BImportFilter;
import com.arahant.annotation.Validation;

public class NewBenefitImportFilterInput extends TransmitInputBase {

	void setData(BImportFilter bc)
	{
		bc.setImportFilterName(name);
		bc.setImportFilterDesc(description);
		bc.setFilterValue(value);
		bc.setImportTypeId(importTypeId);

	}
	
	@Validation (table="import_filter",column="import_filter_name",required=true)
	private String name;
	@Validation (table="import_filter",column="import_filter_desc",required=false)
	private String description;
	@Validation (table="import_filter",column="filter_value",required=true)
	private String value;
	@Validation (table="import_filter",column="import_type_id",required=true)
	private String importTypeId;
	

	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value=value;
	}

	public String getImportTypeId() {
		return importTypeId;
	}

	public void setImportTypeId(String importTypeId) {
		this.importTypeId = importTypeId;
	}
}

	
