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
 * Created on Oct 30, 2008
*/


package com.arahant.business;

import com.arahant.utils.StandardProperty;
import com.arahant.services.SearchMetaInput;
import com.arahant.services.standard.webservices.dynamicWebServices.DataObject;

/**
 *  Use the BSearchMetaInput2 class instead
 *
 */
@Deprecated
public class BSearchMetaInput {
	private int sortType;
	private boolean sortAsc;
	private boolean usingPaging;
	private int pagingFirstItemIndex;
	private int itemsPerPage = 50;
	private String[] sortFields;

	public BSearchMetaInput(SearchMetaInput searchMeta, String[] sortFields) {
		for (int loop = 0; loop < sortFields.length; loop++)
			if (sortFields[loop].equals(searchMeta.getSortField()))
				sortType = loop + 1;

		this.sortFields = sortFields;

		this.sortAsc = searchMeta.isSortAsc();
		this.usingPaging = searchMeta.isUsingPaging();
		this.pagingFirstItemIndex = searchMeta.getFirstItemIndexPaging();
	}

	public BSearchMetaInput(int sortType, boolean sortAsc, boolean usingPaging, int pagingFirstItemIndex) {
		this(sortType, sortAsc, usingPaging, pagingFirstItemIndex, 0);
	}

	public BSearchMetaInput(int sortType, boolean sortAsc, boolean usingPaging, int pagingFirstItemIndex, int itemsPerPage) {
		int searchMax = BProperty.getInt(StandardProperty.SEARCH_MAX);
		sortFields = new String[0];
		this.sortType = sortType;
		this.sortAsc = sortAsc;
		this.usingPaging = usingPaging;
		this.pagingFirstItemIndex = pagingFirstItemIndex;
		if (usingPaging)
			this.itemsPerPage = itemsPerPage > 0 ? itemsPerPage : (searchMax > 0 ? searchMax : 50);
	}

	public BSearchMetaInput(DataObject in, String[] sortFields) {
		this(new SearchMetaInput(in), sortFields);
	}

	public boolean isSortAsc() {
		return sortAsc;
	}

	public void setSortAsc(boolean sortAsc) {
		this.sortAsc = sortAsc;
	}

	public int getSortType() {
		return sortType;
	}

	public void setSortType(int sortType) {
		this.sortType = sortType;
	}

	public boolean isUsingPaging() {
		return usingPaging;
	}

	public void setUsingPaging(boolean usingPaging) {
		this.usingPaging = usingPaging;
	}

	public int getPagingFirstItemIndex() {
		return pagingFirstItemIndex;
	}

	public void setPagingFirstItemIndex(int pagingFirstItemIndex) {
		this.pagingFirstItemIndex = pagingFirstItemIndex;
	}

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	int getSortFieldCount() {
		return sortFields.length;
	}

	String getSortField(int sortType) {
		if (sortFields == null || sortFields.length == 0)
			return "";
		return sortFields[sortType - 1];
	}

}

	
