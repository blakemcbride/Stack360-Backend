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

public class SearchScreensReturnItem {

	private String id;
	private String name;
	private String screenType;
	private String extId;
	private String extIdExpanded;
	private String fileName;
	private String hasAvatar;

	public SearchScreensReturnItem() {
	}

	SearchScreensReturnItem(BScreen bs) {
		id = bs.getScreenId();
		name = bs.getName();
		screenType = bs.getType();
		extId = bs.getExtId();
		extIdExpanded = bs.getExtIdExpanded();
		fileName = bs.getFilename();
		hasAvatar = (bs.getAvatarPath() != null && !bs.getAvatarPath().equals("")) ? "Yes" : "No";
	}

	public String getExtId() {
		return extId;
	}

	public void setExtId(String extId) {
		this.extId = extId;
	}

	public String getExtIdExpanded() {
		return extIdExpanded;
	}

	public void setExtIdExpanded(String extIdExpanded) {
		this.extIdExpanded = extIdExpanded;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScreenType() {
		return screenType;
	}

	public void setScreenType(String screenType) {
		this.screenType = screenType;
	}

	public String getHasAvatar() {
		return hasAvatar;
	}

	public void setHasAvatar(String hasAvatar) {
		this.hasAvatar = hasAvatar;
	}
}
