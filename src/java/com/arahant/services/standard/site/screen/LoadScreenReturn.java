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
package com.arahant.services.standard.site.screen;

import com.arahant.business.BScreen;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.IDGenerator;

public class LoadScreenReturn extends TransmitReturnBase {
	//private String parentScreenFileName;

	private int screenType;
	private String screenId;
	private String fileName;
	private String name;
	private String authCode;
	private String description;
	private String extId;
	private String avatarPath;
	//private String parentScreenId;

	public LoadScreenReturn() {
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
	public void setExtId(String extId) {
		this.extId = extId;
	}

	/**
	 * @param screen
	 */
	void setValues(final BScreen screen) {
		//	parentScreenFileName=screen.getParentScreenFileName();
		screenType = screen.getScreenType();
		screenId = screen.getScreenId();
		fileName = screen.getFilename();
		name = screen.getName();
		authCode = screen.getAuthCode();
		description = screen.getDescription();
		extId = IDGenerator.shrinkKey(screen.getScreenId());
		avatarPath = screen.getAvatarPath();
		//	parentScreenId=screen.getParentScreenId();
	}

	/**
	 * @return Returns the authCode.
	 */
	public String getAuthCode() {
		return authCode;
	}

	/**
	 * @param authCode The authCode to set.
	 */
	public void setAuthCode(final String authCode) {
		this.authCode = authCode;
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
	 * @return Returns the parentScreenFileName.
	 *
	 * public String getParentScreenFileName() { return parentScreenFileName; }
	 *
	 *
	 * /
	 **
	 * @param parentScreenFileName The parentScreenFileName to set.
	 *
	 * public void setParentScreenFileName(final String parentScreenFileName) {
	 * this.parentScreenFileName = parentScreenFileName; }
	 *
	 *
	 * /
	 **
	 * @return Returns the parentScreenId.
	 *
	 * public String getParentScreenId() { return parentScreenId; }
	 *
	 *
	 * /
	 **
	 * @param parentScreenId The parentScreenId to set.
	 *
	 * public void setParentScreenId(final String parentScreenId) {
	 * this.parentScreenId = parentScreenId; }
	 *
	 *
	 * /
	 **
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

	/**
	 * @return Returns the screenType.
	 */
	public int getScreenType() {
		return screenType;
	}

	/**
	 * @param screenType The screenType to set.
	 */
	public void setScreenType(final int screenType) {
		this.screenType = screenType;
	}

	public String getAvatarPath() {
		return avatarPath;
	}

	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}
}
