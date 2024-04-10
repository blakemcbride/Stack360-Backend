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


package com.arahant.services.standard.crm.prospectSalesPersonParent;
import com.arahant.annotation.Validation;

import com.arahant.services.SearchMetaInput;
import com.arahant.business.BSearchMetaInput;
import com.arahant.services.TransmitInputBase;

/**
 * Created on Feb 8, 2007
 */
public class GetReportInput extends TransmitInputBase {
	@Validation (required=false)
	private SearchMetaInput searchMeta;
	@Validation (required=true)
	private String personId;
	@Validation (required=false)
	private int fromDate;
	@Validation (required=false)
	private int toDate;
	@Validation (required=false)
	private String[] statusIds;
	@Validation (required=false)
	private String[] sourceIds;
	@Validation (required=false)
	private boolean includeInactiveStatuses;
	@Validation (required=false)
	private String name;
	@Validation (required=false)
	private int nameSearchType;

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}
	
	public int getFromDate() {
		return fromDate;
	}
	
	public void setFromDate(int fromDate) {
		this.fromDate=fromDate;
	}
	
	public int getToDate() {
		return toDate;
	}
	
	public void setToDate(int toDate) {
		this.toDate=toDate;
	}

	public String[] getSourceIds() {
		if (sourceIds == null) {
			sourceIds = new String[0];
		}
		
		return sourceIds;
	}

	public void setSourceIds(String[] sourceIds) {
		this.sourceIds = sourceIds;
	}

	public String[] getStatusIds() {
		if (statusIds == null) {
			statusIds = new String[0];
		}
		
		return statusIds;
	}

	public void setStatusIds(String[] statusIds) {
		this.statusIds = statusIds;
	}

	public boolean getIncludeInactiveStatuses() {
		return includeInactiveStatuses;
	}

	public void setIncludeInactiveStatuses(boolean includeInactiveStatuses) {
		this.includeInactiveStatuses = includeInactiveStatuses;
	}

	public SearchMetaInput getSearchMeta() {
		return searchMeta;
	}
	
	public void setSearchMeta(SearchMetaInput searchMeta) {
		this.searchMeta = searchMeta;
	}
	
	public String getName() {
		return modifyForSearch(name, nameSearchType);
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNameSearchType() {
		return nameSearchType;
	}

	public void setNameSearchType(int nameSearchType) {
		this.nameSearchType = nameSearchType;
	}
	
	BSearchMetaInput getSearchMetaInput() {
		if (searchMeta == null) {
			return new BSearchMetaInput(0, true, false, 0);		
		} else {
			int sortType = 0;

			if (searchMeta.getSortField()!=null)
			{
				// map our web service specific name (from return item) to the sort value used by query
				if (searchMeta.getSortField().equals("name")) {
					sortType = 1;
				} else if (searchMeta.getSortField().equals("statusCode")) {
					sortType = 2;
				} else if (searchMeta.getSortField().equals("sourceCode")) {
					sortType = 3;
				} else if (searchMeta.getSortField().equals("lastLogDate")) {
					sortType = 4;
				} else if (searchMeta.getSortField().equals("firstContactDate")) {
					sortType = 5;
				} else if (searchMeta.getSortField().equals("lastName")) {
					sortType = 6;
				} else if (searchMeta.getSortField().equals("firstName")) {
					sortType = 7;
				}
			}
			return new BSearchMetaInput(sortType, searchMeta.isSortAsc(), false, 0);
		}
	}
}

	
