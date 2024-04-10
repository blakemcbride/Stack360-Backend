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

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BEmployee;
import com.arahant.business.BSearchOutput;


/**
 * 
 *
 *
 */
public class SearchSalesPersonsReturn extends TransmitReturnBase<BEmployee> {
//	private SearchMetaOutput searchMeta;
	private SearchSalesPersonsReturnItem item[];
	private SearchSalesPersonsReturnItem selectedItem;

	/**
	 * @return Returns the item.
	 */
	public SearchSalesPersonsReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final SearchSalesPersonsReturnItem[] item) {
		this.item = item;
	}


	public SearchSalesPersonsReturnItem getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(final SearchSalesPersonsReturnItem selectedItem) {
		this.selectedItem = selectedItem;
	}
/*
	public SearchMetaOutput getSearchMeta() {
		return searchMeta;
	}
	
	public void setSearchMeta(SearchMetaOutput searchMeta) {
		this.searchMeta = searchMeta;
	}
*/
	void fillFromSearchOutput(final BSearchOutput<BEmployee> bSearchOutput) {
		BEmployee[] items = bSearchOutput.getItems();
		
		item = new SearchSalesPersonsReturnItem[items.length];
		
		for (int loop = 0; loop < items.length; loop++) {
			item[loop] = new SearchSalesPersonsReturnItem(items[loop]);
		}
		
		setStandard(bSearchOutput);
		
	/*	searchMeta = new SearchMetaOutput();
		// map sort type used in query to our web service specific name (from return item)
		switch (bSearchOutput.getSortType()) {
			case 1: searchMeta.setSortField("lastName"); break;
			case 2: searchMeta.setSortField("firstName"); break;
		}
		searchMeta.setSortAsc(bSearchOutput.isSortAsc());
		searchMeta.setItemCapForSmartChooser(bSearchOutput.getItemCapForSmartChooser());
		searchMeta.setFirstItemIndexPaging(bSearchOutput.getPagingFirstItemIndex());
		searchMeta.setTotalItemsPaging(bSearchOutput.getTotalItemsPaging());
		searchMeta.setItemsPerPage(bSearchOutput.getItemsPerPage());
	 * */
	}
}

	
