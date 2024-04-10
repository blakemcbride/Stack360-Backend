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
 *
 *  Created on Oct 30, 2008
*/


package com.arahant.services;

import com.arahant.annotation.Validation;
import com.arahant.services.standard.webservices.dynamicWebServices.DataObject;

public class SearchMetaInput {
	@Validation (required=false)
    private String sortField;
	@Validation (required=false)
    private boolean sortAsc;
	@Validation (required=false)
	private boolean usingPaging;
	@Validation (required=false)
	private int firstItemIndexPaging;

	public SearchMetaInput()
	{
		
	}
	
	public SearchMetaInput(DataObject d) {
		setData(d.get("searchMeta"));
	}

	public boolean isSortAsc() {
		return sortAsc;
	}

	public void setSortAsc(boolean sortAsc) {
		this.sortAsc = sortAsc;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public boolean isUsingPaging() {
		return usingPaging;
	}

	public void setUsingPaging(boolean usingPaging) {
		this.usingPaging = usingPaging;
	}
	
	public int getFirstItemIndexPaging() {
		return firstItemIndexPaging;
	}

	public void setFirstItemIndexPaging(int firstItemIndexPaging) {
		this.firstItemIndexPaging = firstItemIndexPaging;
	}

	public void setData(DataObject d) {
		setFirstItemIndexPaging(d.getInt("firstItemIndexPaging"));
		setSortAsc(d.getBoolean("sortAsc"));
		setSortField(d.getString("sortField"));
		setUsingPaging(d.getBoolean("usingPaging"));
	}
}

	
