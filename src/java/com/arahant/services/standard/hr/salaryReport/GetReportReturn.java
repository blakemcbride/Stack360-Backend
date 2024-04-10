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
 * Created on Mar 16, 2007
 * 
 */
package com.arahant.services.standard.hr.salaryReport;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Mar 16, 2007
 *
 */
public class GetReportReturn extends TransmitReturnBase {

	private String fileName;

	/**
	 * @return Returns the fileName.
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param name The fileName to set.
	 */
	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}
}

	
