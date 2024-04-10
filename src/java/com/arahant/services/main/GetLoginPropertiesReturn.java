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
 * 
 */
package com.arahant.services.main;

import com.arahant.utils.StandardProperty;
import com.arahant.business.BProperty;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.SourceCodeRevision;

public class GetLoginPropertiesReturn extends TransmitReturnBase {
	private static final ArahantLogger logger = new ArahantLogger(GetLoginPropertiesReturn.class);

	private boolean showLoginInformation = BProperty.getBoolean(StandardProperty.loginInformation);
	private boolean showAvatar = BProperty.getBoolean(StandardProperty.showAvatar);
	private boolean displayTagLine = BProperty.getBoolean(StandardProperty.displayTagLine, true);
	private String colorThemeOverride = BProperty.get(StandardProperty.colorThemeOverride);
	private boolean displayCopyright = BProperty.getBoolean(StandardProperty.displayCopyright, true);
	private boolean displayPoweredBy = BProperty.getBoolean(StandardProperty.displayPoweredBy, true);
	private boolean displayNullAbout = BProperty.getBoolean(StandardProperty.displayNullAbout, false);
	private boolean displayUserID = BProperty.getBoolean(StandardProperty.displayUserID, true);
	private String revisionNumber = SourceCodeRevision.getSourceCodeRevisionNumber();

	public boolean isShowAvatar() {
		return showAvatar;
	}

	public boolean isShowLoginInformation() {
		return showLoginInformation;
	}

	public String getColorThemeOverride() {
		return colorThemeOverride;
	}

	public void setColorThemeOverride(String colorThemeOverride) {
		this.colorThemeOverride = colorThemeOverride;
	}

	public boolean getShowLoginInformation() {
		return showLoginInformation;
	}

	public void setShowLoginInformation(boolean showLoginInformation) {
		this.showLoginInformation = showLoginInformation;
	}

	public boolean getShowAvatar() {
		return showAvatar;
	}

	public void setShowAvatar(boolean showAvatar) {
		this.showAvatar = showAvatar;
	}

	public boolean isDisplayTagLine() {
		return displayTagLine;
	}

	public void setDisplayTagLine(boolean displayTagLine) {
		this.displayTagLine = displayTagLine;
	}

	public boolean isDisplayCopyright() {
		return displayCopyright;
	}

	public void setDisplayCopyright(boolean displayCopyright) {
		this.displayCopyright = displayCopyright;
	}

	public boolean isDisplayPoweredBy() {
		return displayPoweredBy;
	}

	public void setDisplayPoweredBy(boolean displayPoweredBy) {
		this.displayPoweredBy = displayPoweredBy;
	}

	public boolean isDisplayNullAbout() {
		return displayNullAbout;
	}

	public void setDisplayNullAbout(boolean displayNullAbout) {
		this.displayNullAbout = displayNullAbout;
	}

	public boolean isDisplayUserID() {
		return displayUserID;
	}

	public void setDisplayUserID(boolean displayUserID) {
		this.displayUserID = displayUserID;
	}

	public String getRevisionNumber() {
		return revisionNumber;
	}

	public void setRevisionNumber(String revnum) {
		this.revisionNumber = revnum;
	}

}


