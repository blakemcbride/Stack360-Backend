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
 * Created on Oct 9, 2009
 * 
 */
package com.arahant.services.standard.misc.agencyOrgGroup;

import com.arahant.utils.StandardProperty;
import com.arahant.business.BProperty;
import com.arahant.business.BScreenOrGroup;
import com.arahant.services.TransmitReturnBase;




/**
 * 
 *
 * Created on Oct 9, 2009
 *
 */
public class SearchScreenGroupsReturn extends TransmitReturnBase {

	private SearchScreenGroupsReturnItem [] screenDef;
	private int cap=BProperty.getInt(StandardProperty.SEARCH_MAX);

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

	/**
	 * @return Returns the screenDef.
	 */
	public SearchScreenGroupsReturnItem[] getScreenDef() {
		return screenDef;
	}

	/**
	 * @param screenDef The screenDef to set.
	 */
	public void setScreenDef(final SearchScreenGroupsReturnItem[] screenDef) {
		this.screenDef = screenDef;
	}

	/**
	 * @param groups
	 */
	void setScreenDef(final BScreenOrGroup[] groups) {
		screenDef=new SearchScreenGroupsReturnItem[groups.length];
		for (int loop=0;loop<groups.length;loop++)
			screenDef[loop]=new SearchScreenGroupsReturnItem(groups[loop]);
	}
}

	
