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
package com.arahant.services.standard.project.projectParent;

import com.arahant.business.BProject;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.StandardProperty;
import com.arahant.business.BProperty;
import com.arahant.business.BSearchOutput;

import java.util.ArrayList;

/**
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchProjectsReturn extends TransmitReturnBase<BProject> {

	private int cap = BProperty.getInt(StandardProperty.SEARCH_MAX);
	private SearchProjectsReturnItem[] item;
	private boolean moreFound;


	/**
	 * @return Returns the cap.
	 */
	public int getCap() {
		return cap;
	}

	/**
	 * @param cap The cap to set.
	 */
	public void setCap(final int cap) {
		this.cap = cap;
	}

	public boolean isMoreFound() {
		return moreFound;
	}

	public void setMoreFound(boolean moreFound) {
		this.moreFound = moreFound;
	}

	public SearchProjectsReturn() {
		super();
	}

	public SearchProjectsReturnItem[] getItem() {
		return item;
	}

	public void setItem(SearchProjectsReturnItem[] item) {
		this.item = item;
	}

	void setProjects(final BSearchOutput<BProject> p, String projectSubtypeId) {
		moreFound = false;
		int n = p.getItems().length;
		final ArrayList<SearchProjectsReturnItem> lst = new ArrayList<>();
		for (int loop = 0; loop < n ; loop++) {
			if (lst.size() > cap) {
				moreFound = true;
				break;
			}
			BProject bp = p.getItems()[loop];
			if (projectSubtypeId != null && !isEmpty(projectSubtypeId)) {
				if (bp.getProjectSubtypeId().equals(projectSubtypeId))
					lst.add(new SearchProjectsReturnItem(bp));
			} else
				lst.add(new SearchProjectsReturnItem(bp));
		}
		item = lst.toArray(new SearchProjectsReturnItem[lst.size()]);
		setStandard(p);
	}
}

	
