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
package com.arahant.services.standard.misc.companyOrgGroup;
import com.arahant.business.BScreen;
import com.arahant.business.BScreenGroup;
import com.arahant.business.BScreenOrGroup;


/**
 * 
 *
 * Created on Feb 11, 2007
 *
 */
public class SearchScreenGroupsReturnItem   {


	private String id;
	private String title;
	private String help="?";
	private String description;
	private String extId;

	
	public SearchScreenGroupsReturnItem() {

	}

	SearchScreenGroupsReturnItem(final BScreenOrGroup sg) {

		if (sg instanceof BScreenGroup)
		{
			final BScreenGroup group=(BScreenGroup)sg;
			this.setExtId(group.getExtId());
			this.setId(group.getScreenGroupId());
			this.setTitle(group.getName());
			description=group.getDescription();
		}
		else
		{
			final BScreen screen=(BScreen)sg;
			this.setExtId(screen.getName());
			this.setId(screen.getScreenId());
			this.setTitle(screen.getDescription());
			description=screen.getDescription();
		}
			
		
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHelp() {
		return help;
	}

	public void setHelp(String help) {
		this.help = help;
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

	
	
}

	
