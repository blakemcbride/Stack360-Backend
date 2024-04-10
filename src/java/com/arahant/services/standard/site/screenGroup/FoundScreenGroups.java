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
 * Created on Feb 12, 2007
 * 
 */
package com.arahant.services.standard.site.screenGroup;
import com.arahant.business.BScreen;
import com.arahant.business.BScreenGroup;
import com.arahant.business.BScreenOrGroup;


/**
 * 
 *
 * Created on Feb 12, 2007
 *
 */
public class FoundScreenGroups   {



	public FoundScreenGroups() {
		super();
	}


	public FoundScreenGroups(final BScreenOrGroup group) {
		super();
		
		setHelp("?");
		
		if (group instanceof BScreenGroup)
		{
			final BScreenGroup bsg=(BScreenGroup)group;
			setTitle(bsg.getName());
			setFile("");
			setId(bsg.getScreenGroupId());
			setExtId(bsg.getExtId());
			setType("Group");
			if (bsg.getParentScreenId() == null || bsg.getParentScreenId().length() == 0)
				setTypeDetail("");
			else
				setTypeDetail(bsg.getParentScreenName());
			setDescription(bsg.getDescription());
		}
		else
		{
			final BScreen s=(BScreen)group;
			setTitle(s.getName());
			setFile(s.getFilename());
			setId(s.getScreenId());
			setExtId(s.getExtId());	
			setType("Screen");
			setTypeDetail(s.getType());			
			setDescription(s.getDescription());
		}
	}

	private String type;
	private String typeDetail;
	private String id;
	private String title;
	private String file;
	private String extId;
	private String description;
	private String help;

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

	/**
	 * @return Returns the typeDetail.
	 */
	public String getTypeDetail() {
		return typeDetail;
	}

	/**
	 * @param typeDetail The typeDetail to set.
	 */
	public void setTypeDetail(final String typeDetail) {
		this.typeDetail = typeDetail;
	}
	
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(final String description) {
		this.description = description;
	}
	
	/**
	 * @return Returns the description.
	 */
	public String getHelp() {
		return help;
	}

	/**
	 * @param help The help to set.
	 */
	public void setHelp(final String help) {
		this.help = help;
	}
}

	
