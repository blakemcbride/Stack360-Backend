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
 * Created on Feb 21, 2007
 * 
 */
package com.arahant.services.standard.site.screenGroup;
import com.arahant.annotation.Validation;
import com.arahant.business.BScreenGroup;
import com.arahant.business.BWizardConfiguration;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Feb 21, 2007
 *
 */
public class NewScreenGroupInput extends TransmitInputBase {

	public NewScreenGroupInput() {
		super();
	}
	
	@Validation (table="screen_group",column="name",required=false)
	private String name;
	@Validation (required=false)
	private String parentScreenGroupId;
	@Validation (required=false)
	private String parentScreenId;
	@Validation (table="screen_group_hierarchy",column="button_name",required=false)
	private String label;
	@Validation (required=false)
	private boolean defaultScreen;	
	@Validation (table="screen_group",column="description",required=false)
	private String description;
	@Validation (required=true)
	private String wizardType;
	
	/**
	 * @return Returns the defaultScreen.
	 */
	public boolean getDefaultScreen() {
		return defaultScreen;
	}

	/**
	 * @param defaultScreen The defaultScreen to set.
	 */
	public void setDefaultScreen(boolean defaultScreen) {
		this.defaultScreen = defaultScreen;
	}

	/**
	 * @return Returns the parentScreenId.
	 */
	public String getParentScreenId() {
		return parentScreenId;
	}
	/**
	 * @param parentScreenId The parentScreenId to set.
	 */
	public void setParentScreenId(final String parentScreenId) {
		this.parentScreenId = parentScreenId;
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
	 * @return Returns the parentScreenGroupId.
	 */
	public String getParentScreenGroupId() {
		return parentScreenGroupId;
	}
	/**
	 * @param parentScreenGroupId The parentScreenGroupId to set.
	 */
	public void setParentScreenGroupId(final String parentScreenGroupId) {
		this.parentScreenGroupId = parentScreenGroupId;
	}
	/**
	 * @param s
	 */
	void setData(final BScreenGroup s) {
		s.setName(getName());
		s.setParentScreenId(parentScreenId);
		s.setDescription(getDescription());
		s.setWizardType(wizardType);
		s.setTechnology(BScreenGroup.getCurrentTechnology());
	}
	/**
	 * @return Returns the label.
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label The label to set.
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	public String getWizardType() {
		return wizardType;
	}

	public void setWizardType(String wizardType) {
		this.wizardType = wizardType;
	}
	

	
}

	
