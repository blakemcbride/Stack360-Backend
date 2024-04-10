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
 * Created on Feb 10, 2007
 * 
 */
package com.arahant.services.standard.crm.prospectOrgGroup;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BProspectCompany;


/**
 * 
 *
 * Created on Feb 10, 2007
 *
 */
public class OrgGroups {

	private String orgGroupId;
	private String name;
	private boolean superOrgGroup;
	private String type;
	private String externalId;
	
	public OrgGroups() {
		
	}

	/**
	 * @param group
	 */
	public OrgGroups(final BOrgGroup group) {
		super();
		orgGroupId=group.getOrgGroupId();
		name=group.getName();
		superOrgGroup=group.isSuperGroup();
		type=group.getType();
		externalId=group.getExternalId();
		
	}

	OrgGroups(BProspectCompany group) {
		this(new BOrgGroup(group));
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
	 * @return Returns the orgGroupId.
	 */
	public String getOrgGroupId() {
		return orgGroupId;
	}

	/**
	 * @param orgGroupId The orgGroupId to set.
	 */
	public void setOrgGroupId(final String orgGroupId) {
		this.orgGroupId = orgGroupId;
	}

	/**
	 * @return Returns the superOrgGroup.
	 */
	public boolean getSuperOrgGroup() {
		return superOrgGroup;
	}

	/**
	 * @param superOrgGroup The superOrgGroup to set.
	 */
	public void setSuperOrgGroup(final boolean superOrgGroup) {
		this.superOrgGroup = superOrgGroup;
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

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(final String externalId) {
		this.externalId = externalId;
	}

	
}

	
