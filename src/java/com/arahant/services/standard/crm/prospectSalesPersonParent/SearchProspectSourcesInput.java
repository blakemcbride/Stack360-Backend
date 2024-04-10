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
package com.arahant.services.standard.crm.prospectSalesPersonParent;
import com.arahant.annotation.Validation;

import com.arahant.services.SearchMetaInput;
import com.arahant.business.BSearchMetaInput;
import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchProspectSourcesInput extends TransmitInputBase {
	@Validation (required=false)
	private SearchMetaInput searchMeta;
	@Validation (required=false)
	private String code;
	@Validation (min=2,max=5,required=false)
	private int codeSearchType;
	@Validation (required=false)
	private String description;
	@Validation (min=2,max=5,required=false)
	private int descriptionSearchType;
	@Validation (required=false)
	private String sourceId;
	@Validation (required=false)
	private String []excludeIds;

	public String getCode() {
		return modifyForSearch(code,codeSearchType);
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getCodeSearchType() {
		return codeSearchType;
	}

	public void setCodeSearchType(int codeSearchType) {
		this.codeSearchType = codeSearchType;
	}

	public String getDescription() {
		return modifyForSearch(description,descriptionSearchType);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDescriptionSearchType() {
		return descriptionSearchType;
	}

	public void setDescriptionSearchType(int descriptionSearchType) {
		this.descriptionSearchType = descriptionSearchType;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String[] getExcludeIds() {
		return excludeIds;
	}

	public void setExcludeIds(String[] excludeIds) {
		this.excludeIds = excludeIds;
	}

	public SearchMetaInput getSearchMeta() {
		return searchMeta;
	}
	
	public void setSearchMeta(SearchMetaInput searchMeta) {
		this.searchMeta = searchMeta;
	}
	
	BSearchMetaInput getSearchMetaInput() {
		if (searchMeta == null) {
			return new BSearchMetaInput(0, true, false, 0);		
		} else {
			int sortType = 0;

			// map our web service specific name (from return item) to the sort type used by query
			if (searchMeta.getSortField().equals("code")) {
				sortType = 1;
			} else if (searchMeta.getSortField().equals("description")) {
				sortType = 2;
			}

			return new BSearchMetaInput(sortType, searchMeta.isSortAsc(), searchMeta.isUsingPaging(), searchMeta.getFirstItemIndexPaging());
		}
	}

}

	
