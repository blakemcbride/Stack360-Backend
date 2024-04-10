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


package com.arahant.services.main;

import com.arahant.business.BScreen;
import com.arahant.business.BScreenGroup;
import com.arahant.business.BScreenOrGroup;
import com.arahant.business.BWizardScreen;
import com.arahant.utils.Crypto;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.IDGenerator;

import java.io.File;

/**
 *
 * Output: Array of objects that have:
 *
 * 1. title 2. type (Screen/Group) 3. screenFile (valid for type=Screen) 4.
 * groupId (valid for type=Group or parent screens - see below) 5. isDefault
 * (valid for type=Screen, should be true or false)
 *
 * Parent screen groups return type=Screen AND a groupId. All other screens
 * would not have a a groupId.
 */
public class ListScreensAndGroupsItem {

	private String title;
	private int type;
	private String screenFile;
	private String groupId;
	private boolean isDefault;
	private boolean placeHolderGroup = false;
	private String screenId;
	private String contextId;
	private boolean showNextButton;
	private boolean lastScreen;
	private String avatarPath;
	private String benefitIds[];
	private String wizardConfigurationId = "";

	public ListScreensAndGroupsItem() {
	}

	ListScreensAndGroupsItem(BWizardScreen wizardScreen, boolean isLast, String wizConfId) {
		BScreen screen = (BScreen) wizardScreen.getScreen();

		title = wizardScreen.getName().replace("Simple ", "").replace(" (Ben)", "").replace(" (Cat)", "");
		type = 1;
		screenFile = screen.getFilename();
		groupId = "";
		isDefault = wizardScreen.getIsDefault();
		placeHolderGroup = false;
		screenId = IDGenerator.shrinkKey(screen.getScreenId());
		contextId = wizardScreen.getContextId();
		showNextButton = true;
		lastScreen = isLast;
		avatarPath = screen.getAvatarPath();
		wizardConfigurationId = wizConfId;
		benefitIds = wizardScreen.getBenefitIds();
	}

	/**
	 * @param group
	 */
	ListScreensAndGroupsItem(final BScreenOrGroup group, final String screenGroupId, boolean isLocalHost) {
		final BScreenGroup parentSG = new BScreenGroup(screenGroupId);

		if (group instanceof BScreenGroup) {
			final BScreenGroup g = (BScreenGroup) group;
			final String label = parentSG.getScreenGroupButtonName(g.getScreenGroupId());
			title = (label == null || label.length() == 0) ? g.getName() : label;
			type = 0;
			screenFile = "";
//			screenId = "";
			screenId = "Group " + IDGenerator.shrinkKey(g.getScreenGroupId());
			groupId = g.getScreenGroupId();
			isDefault = g.getIsDefaultScreenFor(screenGroupId);
			if (g.hasParentScreen()) {
				screenFile = g.getParentScreenFile();
				screenId = IDGenerator.shrinkKey(g.getParentScreenId()) + " (parent screen to group " + IDGenerator.shrinkKey(g.getScreenGroupId()) + ")";
				type = 1;
				avatarPath = new BScreen(g.getParentScreenId()).getAvatarPath();
			} else
				avatarPath = "";

		} else {
			final BScreen s = (BScreen) group;
			final String label = parentSG.getScreenButtonName(s.getScreenId());
			title = (label == null || label.length() == 0) ? s.getName() : label;
			type = 1;

			screenFile = s.getFilename();
			screenId = IDGenerator.shrinkKey(s.getScreenId());

			//   kill check
//			try {
//				if (!isLocalHost) {
//					//check the code
//					final File curFile = new File(FileSystemUtils.getWorkingDirectory(), screenFile);
//
//					final byte[] md5Hash = Crypto.getMD5(curFile);
//					final byte[] encryptedMd5Hash = Crypto.encryptTripleDES(md5Hash);
//					final String encryptedMd5HashAsHex = Crypto.bytesToHex(encryptedMd5Hash);
//
//					if (!encryptedMd5HashAsHex.equals(s.getAuthCode()))
//						screenFile = "Authorization code not valid.";
//				}
//
//
//			} catch (Exception e) {
//			}

			if (s.getScreenType() == 2)
				groupId = s.getChildGroup();
			else
				groupId = "";
			isDefault = s.getIsDefaultScreenFor(screenGroupId);

			avatarPath = s.getAvatarPath();
		}
	}

	ListScreensAndGroupsItem(final BScreenOrGroup group, boolean isLocalHost) {

		if (group instanceof BScreenGroup) {
			final BScreenGroup g = (BScreenGroup) group;
			final String label = g.getName();
			title = (label == null || label.length() == 0) ? g.getName() : label;
			type = 0;
			screenFile = "";
			screenId = "";
			groupId = "Group " + g.getScreenGroupId();

			if (g.hasParentScreen()) {
				screenFile = g.getParentScreenFile();
				screenId = IDGenerator.shrinkKey(g.getParentScreenId()) + " (parent screen to group " + IDGenerator.shrinkKey(g.getScreenGroupId()) + ")";
				type = 1;
			}

			avatarPath = "";

		} else {
			final BScreen s = (BScreen) group;
			final String label = s.getName();
			title = (label == null || label.length() == 0) ? s.getName() : label;
			type = 1;

			screenFile = s.getFilename();
			screenId = IDGenerator.shrinkKey(s.getScreenId());

//   kill check
//			try {
//				if (!isLocalHost) {
//					//check the code
//					final File curFile = new File(FileSystemUtils.getWorkingDirectory(), screenFile);
//
//					final byte[] md5Hash = Crypto.getMD5(curFile);
//					final byte[] encryptedMd5Hash = Crypto.encryptTripleDES(md5Hash);
//					final String encryptedMd5HashAsHex = Crypto.bytesToHex(encryptedMd5Hash);
//
//					if (!encryptedMd5HashAsHex.equals(s.getAuthCode()))
//						screenFile = "Authorization code not valid.";
//				}
//
//
//			} catch (Exception e) {
//			}

			if (s.getScreenType() == 2)
				groupId = s.getChildGroup();
			else
				groupId = "";

			avatarPath = s.getAvatarPath();
		}
	}

	ListScreensAndGroupsItem(BScreenOrGroup group, int loop) {
		isDefault = loop == 0;
		if (group instanceof BScreenGroup) {
			final BScreenGroup g = (BScreenGroup) group;
			final String label = g.getName();
			title = (label == null || label.length() == 0) ? g.getName() : label;
			type = 0;
			screenFile = "";
			screenId = "";
			groupId = "Group " + g.getScreenGroupId();

			if (g.hasParentScreen()) {
				screenFile = g.getParentScreenFile();
				screenId = IDGenerator.shrinkKey(g.getParentScreenId()) + " (parent screen to group " + IDGenerator.shrinkKey(g.getScreenGroupId()) + ")";
				type = 1;
			}

			avatarPath = "";

		} else if (group instanceof BScreen) {
			final BScreen s = (BScreen) group;
			final String label = s.getName();
			title = (label == null || label.length() == 0) ? s.getName() : label;
			type = 1;

			screenFile = s.getFilename();
			screenId = IDGenerator.shrinkKey(s.getScreenId());

			if (s.getScreenType() == 2)
				groupId = s.getChildGroup();
			else
				groupId = "";

			avatarPath = s.getAvatarPath();
		} else {
		}
	}

	/**
	 * @return Returns the groupId.
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId The groupId to set.
	 */
	public void setGroupId(final String groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return Returns the isDefault.
	 */
	public boolean getIsDefault() {
		return isDefault;
	}

	/**
	 * @param isDefault The isDefault to set.
	 */
	public void setIsDefault(final boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * @return Returns the screenFile.
	 */
	public String getScreenFile() {
		return screenFile;
	}

	/**
	 * @param screenFile The screenFile to set.
	 */
	public void setScreenFile(final String screenFile) {
		this.screenFile = screenFile;
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
	public int getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(final int type) {
		this.type = type;
	}

	public boolean getPlaceHolderGroup() {
		return placeHolderGroup;
	}

	public void setPlaceHolderGroup(boolean placeHolderGroup) {
		this.placeHolderGroup = placeHolderGroup;
	}

	public String getScreenId() {
		return screenId;
	}

	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

	public String getContextId() {
		return contextId;
	}

	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	public boolean getShowNextButton() {
		return showNextButton;
	}

	public void setShowNextButton(boolean showNextButton) {
		this.showNextButton = showNextButton;
	}

	public boolean getLastScreen() {
		return lastScreen;
	}

	public void setLastScreen(boolean lastScreen) {
		this.lastScreen = lastScreen;
	}

	public String getAvatarPath() {
		return avatarPath;
	}

	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}

	public String[] getBenefitIds() {
		return benefitIds;
	}

	public void setBenefitIds(String[] benefitIds) {
		this.benefitIds = benefitIds;
	}

	public String getWizardConfigurationId() {
		return wizardConfigurationId;
	}

	public void setWizardConfigurationId(String wizardConfigurationId) {
		this.wizardConfigurationId = wizardConfigurationId;
	}
}
