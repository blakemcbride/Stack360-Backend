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

import com.arahant.services.SearchMetaOutput;
import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BProperty;
import com.arahant.business.BProspectStatus;
import com.arahant.business.BSearchOutput;

public class SearchProspectStatusesReturn extends TransmitReturnBase {
	private SearchMetaOutput searchMeta;
	private SearchProspectStatusesReturnItem[] item;
	private SearchProspectStatusesReturnItem selectedItem;
	private int lowCap = BProperty.getInt("Combo Max");

	public SearchProspectStatusesReturnItem[] getItem() {
		return item;
	}

	public void setItem(final SearchProspectStatusesReturnItem[] item) {
		this.item = item;
	}


	public SearchProspectStatusesReturnItem getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(final SearchProspectStatusesReturnItem selectedItem) {
		this.selectedItem = selectedItem;
	}

	public SearchMetaOutput getSearchMeta() {
		return searchMeta;
	}
	
	public void setSearchMeta(SearchMetaOutput searchMeta) {
		this.searchMeta = searchMeta;
	}

	public int getLowCap() {
		return lowCap;
	}

	public void setLowCap(int lowCap) {
		this.lowCap = lowCap;
	}

	void fillFromSearchOutput(final BSearchOutput bSearchOutput) {
		BProspectStatus[] items = (BProspectStatus[])bSearchOutput.getItems();
		
		item = new SearchProspectStatusesReturnItem[items.length];
		
		for (int loop = 0; loop < items.length; loop++) {
			item[loop] = new SearchProspectStatusesReturnItem(items[loop]);
		}
		
		searchMeta = new SearchMetaOutput();
		// map sort type used in query to our web service specific name (from return item)
		switch (bSearchOutput.getSortType()) {
			case 1: searchMeta.setSortField("code"); break;
			case 2: searchMeta.setSortField("description"); break;
			case 3: searchMeta.setSortField("status"); break;
			case 4: searchMeta.setSortField("sequence"); break;
		}
		searchMeta.setSortAsc(bSearchOutput.isSortAsc());
		searchMeta.setItemCapForSmartChooser(bSearchOutput.getItemCapForSmartChooser());
		searchMeta.setFirstItemIndexPaging(bSearchOutput.getPagingFirstItemIndex());
		searchMeta.setTotalItemsPaging(bSearchOutput.getTotalItemsPaging());
		searchMeta.setItemsPerPage(bSearchOutput.getItemsPerPage());
	}
}

	
