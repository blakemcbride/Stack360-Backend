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
package com.arahant.services.standard.project.projectStatus;

import com.arahant.business.BProjectStatus;

/**
 * 
 *
 * Created on Feb 12, 2007
 *
 */
public class ListProjectStatusesReturnItem {

	public ListProjectStatusesReturnItem() {
		super();
	}
	private String projectStatusId;
	private String code;
	private String description;
	private int active;
	private String activeFormatted;
	private int lastActiveDate;
	private boolean allCompanies;

	public boolean getAllCompanies() {
		return allCompanies;
	}

	public void setAllCompanies(boolean allCompanies) {
		this.allCompanies = allCompanies;
	}

	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	

	/**
	 * @return Returns the active.
	 */
	public int getActive() {
		return active;
	}

	/**
	 * @param active The active to set.
	 */
	public void setActive(final int active) {
		this.active = active;
	}

	/**
	 * @return Returns the activeFormatted.
	 */
	public String getActiveFormatted() {
		return activeFormatted;
	}

	/**
	 * @param activeFormatted The activeFormatted to set.
	 */
	public void setActiveFormatted(final String activeFormatted) {
		this.activeFormatted = activeFormatted;
	}

	/**
	 * @param status
	 */
	ListProjectStatusesReturnItem(final BProjectStatus s) {
		super();
		projectStatusId = s.getProjectStatusId();
		code = s.getCode();
		description = s.getDescription();
		active = s.getActive();
		activeFormatted = ((active == 1) ? "Active" : (active == 0) ? "Inactive" : "Ongoing");
		lastActiveDate=s.getLastActiveDate();
		allCompanies=s.getCompany()==null;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IProjectStatusTransmit#getCode()
	 */
	public String getCode() {
		return code;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IProjectStatusTransmit#setCode(java.lang.String)
	 */
	public void setCode(final String code) {
		this.code = code;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IProjectStatusTransmit#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IProjectStatusTransmit#setDescription(java.lang.String)
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IProjectStatusTransmit#getProjectStatusId()
	 */
	public String getProjectStatusId() {
		return projectStatusId;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IProjectStatusTransmit#setProjectStatusId(java.lang.String)
	 */
	public void setProjectStatusId(final String projectStatusId) {
		this.projectStatusId = projectStatusId;
	}
}

	
