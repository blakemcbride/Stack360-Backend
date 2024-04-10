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

package com.arahant.services.standard.at.applicant;

import com.arahant.business.BApplication;
import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BApplicant;
import com.arahant.business.BProperty;
import com.arahant.utils.StandardProperty;
import com.arahant.business.BSearchOutput;
import com.arahant.utils.KissConnection;
import org.kissweb.database.Connection;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchApplicantsReturn extends TransmitReturnBase<BApplicant> {
	private SearchApplicantsReturnItem[] item;

	private int cap = BProperty.getInt(StandardProperty.SEARCH_MAX);

	public void setCap(int x) {
		cap = x;
	}

	public int getCap() {
		return cap;
	}

	/**
	 * @return Returns the item.
	 */
	public SearchApplicantsReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final SearchApplicantsReturnItem[] item) {
		this.item = item;
	}

	void setItem(final BSearchOutput<BApplicant> a, final ArrayList<String> empStat, boolean sortLastEventFirst, boolean sortLastEventLast) throws Exception {
		final Connection db = KissConnection.get();
		final BApplicant[] applicants = a.getItems();
		item = new SearchApplicantsReturnItem[applicants.length];
		for (int loop = 0; loop < item.length; loop++) {
			BApplicant ba = applicants[loop];
			BApplication[] bapps = ba.getApplications();
			if (bapps == null || bapps.length == 0) {
				item[loop] = new SearchApplicantsReturnItem(db, ba, null, empStat.get(loop));
				if (sortLastEventLast)
					item[loop].setSortReverse();
			} else
				for (BApplication bapp : bapps) {
					item[loop] = new SearchApplicantsReturnItem(db, ba, bapp, empStat.get(loop));
					if (sortLastEventLast)
						item[loop].setSortReverse();
				}
		}
		/*
		if (sortLastEventFirst || sortLastEventLast)
			Arrays.sort(item);  // sort for lastEventDate (this is NOT the main place this is taking place.  It is only for cases where there is no last event.)
		*/
		setStandard(a);
	}
}

	
