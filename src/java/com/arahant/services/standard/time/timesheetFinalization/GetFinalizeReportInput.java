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
 * Created on Feb 9, 2007
 * 
 */
package com.arahant.services.standard.time.timesheetFinalization;
import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Feb 9, 2007
 *
 */
public class GetFinalizeReportInput extends TransmitInputBase{


	@Validation (type="date",required=false)
	private int cutoffDate;
	boolean includeSelf;
	
	public GetFinalizeReportInput() {
		super();
	}

	/**
	 * @return Returns the cutoffDate.
	 */
	public int getCutoffDate() {
		return cutoffDate;
	}

	/**
	 * @param cutoffDate The cutoffDate to set.
	 */
	public void setCutoffDate(final int cutoffDate) {
		this.cutoffDate = cutoffDate;
	}

	/**
	 * @return Returns the includeSelf.
	 */
	public boolean isIncludeSelf() {
		return includeSelf;
	}

	/**
	 * @param includeSelf The includeSelf to set.
	 */
	public void setIncludeSelf(final boolean includeSelf) {
		this.includeSelf = includeSelf;
	}
}

	
