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

import com.arahant.annotation.Validation;
import com.arahant.business.BScreen;
import com.arahant.services.TransmitInputBase;

public class SaveScreenInput extends TransmitInputBase {

	@Validation(required = true)
	private String screenId;
	@Validation(table = "screen", column = "filename", required = true)
	private String fileName;
	@Validation(table = "screen", column = "name", required = true)
	private String name;
	@Validation(required = false)
	private String authCode;
	@Validation(table = "screen", column = "description", required = true)
	private String description;
	@Validation(min = 1, max = 5, required = true)
	private int screenType;
	@Validation(table = "screen", column = "avatar_path", required = false)
	private String avatarPath;
	@Validation(min = 1, max = 1, required = true)
	private String techType;

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

	public String getAvatarPath() {
		return avatarPath;
	}

	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}

	public String getTechType() {
		return techType;
	}

	public void setTechType(String techType) {
		this.techType = techType;
	}

	public SaveScreenInput() {
	}

	/**
	 * @param s
	 */
	void setData(final BScreen s) {
		s.setAuthCode(getAuthCode());
		s.setDescription(getDescription());
		s.setFilename(getFileName());
		s.setName(getName());
		s.setScreenType(screenType);
		s.setAvatarPath(avatarPath);
		s.setTechnology(getTechType().charAt(0));
	}
}
