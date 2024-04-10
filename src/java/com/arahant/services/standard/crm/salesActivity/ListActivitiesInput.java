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
package com.arahant.services.standard.crm.salesActivity;

import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class ListActivitiesInput extends TransmitInputBase {

	@Validation(required=false)
	private boolean showActive;
	@Validation(required=false)
	private boolean showInactive;

	public boolean getShowActive() {
		return showActive;
	}

	public void setShowActive(boolean showActive) {
		this.showActive = showActive;
	}

	public boolean getShowInactive() {
		return showInactive;
	}

	public void setShowInactive(boolean showInactive) {
		this.showInactive = showInactive;
	}

}

	
