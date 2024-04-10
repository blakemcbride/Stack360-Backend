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

import com.arahant.business.BProperty;
import com.arahant.services.SearchMetaOutput;
import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BProspectSource;
import com.arahant.business.BSearchOutput;

public class SearchProspectSourcesReturn extends TransmitReturnBase {
	private SearchMetaOutput searchMeta;
	private SearchProspectSourcesReturnItem[] item;
	private SearchProspectSourcesReturnItem selectedItem;
	private int lowCap = BProperty.getInt("Combo Max");

	public SearchProspectSourcesReturnItem[] getItem() {
		return item;
	}

	public void setItem(final SearchProspectSourcesReturnItem[] item) {
		this.item = item;
	}

	public SearchProspectSourcesReturnItem getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(SearchProspectSourcesReturnItem selectedItem) {
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
		BProspectSource[] items = (BProspectSource[])bSearchOutput.getItems();
		
		item = new SearchProspectSourcesReturnItem[items.length];
		
		for (int loop = 0; loop < items.length; loop++) {
			item[loop] = new SearchProspectSourcesReturnItem(items[loop]);
		}
		
		searchMeta = new SearchMetaOutput();
		// map sort type used in query to our web service specific name (from return item)
		switch (bSearchOutput.getSortType()) {
			case 1: searchMeta.setSortField("code"); break;
			case 2: searchMeta.setSortField("description"); break;
		}
		searchMeta.setSortAsc(bSearchOutput.isSortAsc());
		searchMeta.setItemCapForSmartChooser(bSearchOutput.getItemCapForSmartChooser());
		searchMeta.setFirstItemIndexPaging(bSearchOutput.getPagingFirstItemIndex());
		searchMeta.setTotalItemsPaging(bSearchOutput.getTotalItemsPaging());
		searchMeta.setItemsPerPage(bSearchOutput.getItemsPerPage());
	}
}

	
