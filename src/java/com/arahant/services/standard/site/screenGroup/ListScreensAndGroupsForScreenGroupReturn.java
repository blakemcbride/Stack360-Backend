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
package com.arahant.services.standard.site.screenGroup;

import com.arahant.business.BScreenOrGroup;
import com.arahant.services.TransmitReturnBase;

public class ListScreensAndGroupsForScreenGroupReturn extends TransmitReturnBase {

	private ScreensAndGroups[] screenDefs;

	public ListScreensAndGroupsForScreenGroupReturn() {
	}

	/**
	 * @return Returns the screenDefs.
	 */
	public ScreensAndGroups[] getScreenDefs() {
		return screenDefs;
	}

	/**
	 * @param screenDefs The screenDefs to set.
	 */
	public void setScreenDefs(final ScreensAndGroups[] screenDefs) {
		this.screenDefs = screenDefs;
	}

	/**
	 * @param groups
	 * @param screenGroupId
	 */
	void setScreenDefs(final BScreenOrGroup[] groups, final String screenGroupId) {
		screenDefs = new ScreensAndGroups[groups.length];
		for (int loop = 0; loop < groups.length; loop++)
			screenDefs[loop] = new ScreensAndGroups(groups[loop], screenGroupId);
	}
}
