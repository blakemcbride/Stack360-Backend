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


package com.arahant.services;

/**
 * Created on Oct 30, 2008
 */
public class SearchMetaOutput {
    private String sortField;
    private boolean sortAsc;
    private int itemCapForSmartChooser;
    private int firstItemIndexPaging;
    private int totalItemsPaging;
    private int itemsPerPage;

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
	
	public int getItemCapForSmartChooser() {
		return itemCapForSmartChooser;
	}

	public void setItemCapForSmartChooser(int itemCapForSmartChooser) {
		this.itemCapForSmartChooser = itemCapForSmartChooser;
	}
	
	public int getFirstItemIndexPaging() {
		return firstItemIndexPaging;
	}

	public void setFirstItemIndexPaging(int firstItemIndexPaging) {
		this.firstItemIndexPaging = firstItemIndexPaging;
	}

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	public int getTotalItemsPaging() {
		return totalItemsPaging;
	}

	public void setTotalItemsPaging(int totalItemsPaging) {
		this.totalItemsPaging = totalItemsPaging;
	}
}

	
