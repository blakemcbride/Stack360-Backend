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

package com.arahant.services.standard.at.applicantQuestion;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BProperty;
import com.arahant.utils.StandardProperty;
import com.arahant.utils.ArahantSession;
import org.kissweb.DateUtils;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.util.List;

public class SearchPositionsReturn extends TransmitReturnBase {
	
	private int lowCap=BProperty.getInt(StandardProperty.COMBO_MAX);
	private int highCap=BProperty.getInt(StandardProperty.SEARCH_MAX);
	private SearchPositionsReturnItem [] item;

	public void fillItems() throws Exception {
		Connection db = ArahantSession.getKissConnection();
		int today = DateUtils.today();
		List<Record> recs = db.fetchAll("select position_id, position_name, first_active_date, last_active_date " +
				"from hr_position " +
				"where (first_active_date = 0 or first_active_date <= ?) " +
				"      and (last_active_date = 0 or last_active_date >= ?)" +
				"order by seqno", today, today);
		item = new SearchPositionsReturnItem[recs.size()];
		int i = 0;
		for (Record rec : recs)
			item[i++] = new SearchPositionsReturnItem(rec.getString("position_id"),
					rec.getString("position_name"),
					rec.getInt("first_active_date"),
					rec.getInt("last_active_date"));
	}

	public int getHighCap() {
		return highCap;
	}

	public void setHighCap(int highCap) {
		this.highCap = highCap;
	}

	public int getLowCap() {
		return lowCap;
	}

	public void setLowCap(int lowCap) {
		this.lowCap = lowCap;
	}

	public SearchPositionsReturnItem[] getItem() {
		return item;
	}

	public void setItem(SearchPositionsReturnItem[] item) {
		this.item = item;
	}
}

	
