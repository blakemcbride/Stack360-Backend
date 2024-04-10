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
public class SearchSalesPersonsInput extends TransmitInputBase {
	@Validation (required=false)
	private SearchMetaInput searchMeta;
	@Validation (required=false)
	private String firstName;
	@Validation (min=2,max=5,required=false)
	private int firstNameSearchType;
	@Validation (required=false)
	private String lastName;
	@Validation (min=2,max=5,required=false)
	private int lastNameSearchType;
	@Validation (min=1,max=16,required=false)
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName()
	{
		return modifyForSearch(firstName,firstNameSearchType);
	}
	public void setFirstName(String firstName)
	{
		this.firstName=firstName;
	}
	public int getFirstNameSearchType()
	{
		return firstNameSearchType;
	}
	public void setFirstNameSearchType(int firstNameSearchType)
	{
		this.firstNameSearchType=firstNameSearchType;
	}
	public String getLastName()
	{
		return modifyForSearch(lastName,lastNameSearchType);
	}
	public void setLastName(String lastName)
	{
		this.lastName=lastName;
	}
	public int getLastNameSearchType()
	{
		return lastNameSearchType;
	}
	public void setLastNameSearchType(int lastNameSearchType)
	{
		this.lastNameSearchType=lastNameSearchType;
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
		/*	int sortType = 0;

			// map our web service specific name (from return item) to the sort type used by query
			if (searchMeta.getSortField().equals("lastName")) {
				sortType = 1;
			} else if (searchMeta.getSortField().equals("firstName")) {
				sortType = 2;
			}

			return new BSearchMetaInput(sortType, searchMeta.isSortAsc(), searchMeta.isUsingPaging(), searchMeta.getFirstItemIndexPaging());
		 */
			return new BSearchMetaInput(searchMeta,new String[]{"lastName", "firstName"});

		}
	}

}

	
