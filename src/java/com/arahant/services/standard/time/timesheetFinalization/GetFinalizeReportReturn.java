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
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Feb 9, 2007
 *
 */
public class GetFinalizeReportReturn extends TransmitReturnBase  {

	private String reportUrl;

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IReportTransmit#getReportUrl()
	 */
	public String getReportUrl() {
		return reportUrl;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IReportTransmit#setReportUrl(java.lang.String)
	 */
	public void setReportUrl(final String reportUrl) {
		this.reportUrl = reportUrl;
	}
}

	
