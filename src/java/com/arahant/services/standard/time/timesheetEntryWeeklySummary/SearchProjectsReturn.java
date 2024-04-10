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
package com.arahant.services.standard.time.timesheetEntryWeeklySummary;

import com.arahant.beans.Person;
import com.arahant.utils.StandardProperty;
import com.arahant.business.BProject;
import com.arahant.business.BProperty;
import com.arahant.services.TransmitReturnBase;

import java.util.LinkedList;
import java.util.List;

import static com.arahant.utils.ArahantConstants.ACCESS_LEVEL_NOT_VISIBLE;

public class SearchProjectsReturn extends TransmitReturnBase {

	private int highCap = BProperty.getInt(StandardProperty.SEARCH_MAX);
	private int lowCap = BProperty.getInt(StandardProperty.COMBO_MAX);

	private SearchProjectsReturnItem[] item;

	public SearchProjectsReturn() {
	}

	public SearchProjectsReturnItem[] getItem() {
		return item;
	}

	public void setItem(SearchProjectsReturnItem[] item) {
		this.item = item;
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

	void setProjects(List<BProject> pl, int accessLevel, Person pers) {
		LinkedList<SearchProjectsReturnItem> itmlst = new LinkedList<>();
		int n = 0;
		for (BProject bp : pl) {
			if (accessLevel != ACCESS_LEVEL_NOT_VISIBLE || bp.isPersonAssociateWithProject(pers)) {
				itmlst.add(new SearchProjectsReturnItem(bp));
			}
		}
		item = new SearchProjectsReturnItem[itmlst.size()];
		for (SearchProjectsReturnItem i : itmlst)
			item[n++] = i;
	}

}
