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
 * Created on May 14, 2007
 * 
 */
package com.arahant.services.standard.hr.benefitCategory;

import com.arahant.business.BScreen;


/**
 * 
 *
 * Created on May 14, 2007
 *
 */
public class SearchScreensReturnItem {

	private String name;
	private String fileName;
	private String screenId;
        private String extId;

    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }
	
	public SearchScreensReturnItem()
	{
		;
	}
	/**
	 * @param s
	 */
	SearchScreensReturnItem(final BScreen s) {
		name=s.getName();
		fileName=s.getFilename();
		screenId=s.getScreenId();
                extId=s.getExtId();
		
	}
	/**
	 * @return Returns the fileName.
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName The fileName to set.
	 */
	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}
	/**
	 * @return Returns the screenId.
	 */
	public String getScreenId() {
		return screenId;
	}
	/**
	 * @param screenId The screenId to set.
	 */
	public void setScreenId(final String screenId) {
		this.screenId = screenId;
	}

}

	
