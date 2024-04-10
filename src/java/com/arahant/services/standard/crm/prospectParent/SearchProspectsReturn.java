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

package com.arahant.services.standard.crm.prospectParent;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BProspectCompany;
import com.arahant.business.BProperty;
import com.arahant.utils.StandardProperty;
import org.kissweb.database.Record;

import java.sql.SQLException;
import java.util.List;

public class SearchProspectsReturn extends TransmitReturnBase {

	private SearchProspectsReturnItem[] item;
	private SearchProspectsReturnItem selectedItem;
	private int cap = BProperty.getInt(StandardProperty.SEARCH_MAX);

	public SearchProspectsReturnItem getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(final SearchProspectsReturnItem selectedItem) {
		this.selectedItem = selectedItem;
	}

	public void setCap(int x) {
		cap = x;
	}

	public int getCap() {
		return cap;
	}

	public SearchProspectsReturnItem[] getItem() {
		return item;
	}

	public void setItem(final SearchProspectsReturnItem[] item) {
		this.item = item;
	}

	void setItem(final BProspectCompany[] a) {
		item = new SearchProspectsReturnItem[a.length];
		for (int loop = 0; loop < a.length; loop++)
			item[loop] = new SearchProspectsReturnItem(a[loop]);
	}

	void fillFromSearchOutput(final List<Record> recs) throws SQLException {
		int sz = recs.size();
		item = new SearchProspectsReturnItem[sz];

		for (int loop = 0; loop < sz; loop++)
			item[loop] = new SearchProspectsReturnItem(recs.get(loop));

	//	setStandard(bSearchOutput);
		/*
		searchMeta = new SearchMetaOutput();
		// map sort type used in query to our web service specific name (from return item)
		switch (bSearchOutput.getSortType()) {
			case 1: searchMeta.setSortField("name"); break;
			case 2: searchMeta.setSortField("statusCode"); break;
			case 3: searchMeta.setSortField("sourceCode"); break;
			case 4: searchMeta.setSortField("lastLogDate"); break;
			case 5: searchMeta.setSortField("firstContactDate"); break;
			case 6: searchMeta.setSortField("lastName"); break;
			case 7: searchMeta.setSortField("firstName"); break;
		}
		searchMeta.setSortAsc(bSearchOutput.isSortAsc());
		searchMeta.setItemCapForSmartChooser(bSearchOutput.getItemCapForSmartChooser());
		searchMeta.setFirstItemIndexPaging(bSearchOutput.getPagingFirstItemIndex());
		searchMeta.setTotalItemsPaging(bSearchOutput.getTotalItemsPaging());
		searchMeta.setItemsPerPage(bSearchOutput.getItemsPerPage());
		 */
	}
}

	
