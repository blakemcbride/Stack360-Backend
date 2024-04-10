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
 * Created on Feb 11, 2007
 * 
 */
package com.arahant.services.standard.hr.hrParent;
import com.arahant.business.BScreen;
import com.arahant.business.BScreenGroup;
import com.arahant.business.BScreenOrGroup;


/**
 * 
 *
 * Created on Feb 11, 2007
 *
 */
public class FoundScreenGroups  {

	private String type;
	private String id;
	private String title;
	private String file;
	private String extId;
	private String screenType;
	
	public FoundScreenGroups() {

	}

	FoundScreenGroups(final BScreenOrGroup sg) {

		if (sg instanceof BScreenGroup)
		{
			final BScreenGroup group=(BScreenGroup)sg;
			this.setExtId(group.getExtId());
			this.setId(group.getScreenGroupId());
			this.setType("Group");
			this.setTitle(group.getName());
		}
		else
		{
			final BScreen screen=(BScreen)sg;
			this.setFile(screen.getFilename());
			this.setExtId(screen.getName());
			this.setId(screen.getScreenId());
			this.setTitle(screen.getDescription());
			this.setType(screen.getType());
		}
			
		
	}

	/**
	 * @return Returns the extId.
	 */
	public String getExtId() {
		return extId;
	}

	/**
	 * @param extId The extId to set.
	 */
	public void setExtId(final String extId) {
		this.extId = extId;
	}

	/**
	 * @return Returns the file.
	 */
	public String getFile() {
		return file;
	}

	/**
	 * @param file The file to set.
	 */
	public void setFile(final String file) {
		this.file = file;
	}

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * @return Returns the screenType.
	 */
	public String getScreenType() {
		return screenType;
	}

	/**
	 * @param screenType The screenType to set.
	 */
	public void setScreenType(final String screenType) {
		this.screenType = screenType;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title The title to set.
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(final String type) {
		this.type = type;
	}
	
}

	
