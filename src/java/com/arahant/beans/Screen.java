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


package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "screen")
public class Screen extends ArahantBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "screen";
	public static final short NORMAL_TYPE = 1;
	public static final short PARENT_TYPE = 2;
	public static final short CHILD_TYPE = 3;
	public static final short WIZARD_TYPE = 4;
	public static final short WIZARD_PAGE_TYPE = 5;
	// Fields
	private String screenId;
	public static final String SCREENID = "screenId";
	private String filename;
	public static final String FILENAME = "filename";
	private String authCode;
	public static final String AUTHCODE = "authCode";
	private String name;
	public static final String NAME = "name";
	private String description;
	public static final String DESCRIPTION = "description";
	private char technology = 'F';
	public static final String TECHNOLOGY = "technology";
//	private char defaultScreen;
//	public static final String DEFAULTSCREEN = "defaultScreen";
	private Set<ScreenGroupHierarchy> screenGroupHierarchies = new HashSet<ScreenGroupHierarchy>(0);
	public static final String SCREENGROUPHIERARCHIES = "screenGroupHierarchies";
	private short screenType;
	public static final String SCREENTYPE = "screenType";
	private Set<ScreenGroup> childScreenGroups = new HashSet<ScreenGroup>(0);
	public static final String CHILDSCREENGROUPS = "childScreenGroups";
	private String avatarPath;

	// Constructors
	public Screen() {
	}

	// Property accessors
	@Id
	@Column(name = "screen_id")
	public String getScreenId() {
		return this.screenId;
	}

	public void setScreenId(final String screenId) {
		this.screenId = screenId;
	}

	@Column(name = "filename")
	public String getFilename() {
		return this.filename;
	}

	public void setFilename(final String filename) {
		this.filename = filename;
	}

	@Column(name = "auth_code")
	public String getAuthCode() {
		return this.authCode;
	}

	public void setAuthCode(final String authCode) {
		this.authCode = authCode;
	}

	@Column(name = "screen_name")
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}


	/*
	 * public char getDefaultScreen() { return this.defaultScreen; }
	 *
	 * public void setDefaultScreen(final char defaultScreen) {
	 * this.defaultScreen = defaultScreen; }
	 */
	@OneToMany(mappedBy = ScreenGroupHierarchy.SCREEN, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<ScreenGroupHierarchy> getScreenGroupHierarchies() {
		return this.screenGroupHierarchies;
	}

	public void setScreenGroupHierarchies(final Set<ScreenGroupHierarchy> screenGroupHierarchies) {
		this.screenGroupHierarchies = screenGroupHierarchies;
	}

	/**
	 * @return Returns the screenType.
	 */
	@Column(name = "screen_type")
	public short getScreenType() {
		return screenType;
	}

	/**
	 * @param screenType The screenType to set.
	 */
	public void setScreenType(final short screenType) {
		this.screenType = screenType;
	}

	/**
	 * @return Returns the childScreenGroups.
	 */
	@OneToMany(mappedBy = ScreenGroup.PARENT_SCREEN, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<ScreenGroup> getChildScreenGroups() {
		return childScreenGroups;
	}

	public void setChildScreenGroups(final Set<ScreenGroup> childScreenGroups) {
		this.childScreenGroups = childScreenGroups;
	}

	@Column(name = "avatar_path")
	public String getAvatarPath() {
		return avatarPath;
	}

	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}

	@Column(name = "technology")
	public char getTechnology() {
		return technology;
	}

	public void setTechnology(char technology) {
		this.technology = technology;
	}

	@Override
	public String keyColumn() {

		return "screen_id";
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String generateId() throws ArahantException {
		setScreenId(IDGenerator.generate(this));
		return screenId;
	}

	@Override
	public boolean equals(Object o) {
		if (screenId == null && o == null)
			return true;
		if (screenId != null && o instanceof Screen)
			return screenId.equals(((Screen) o).getScreenId());
		return false;
	}

	@Override
	public int hashCode() {
		if (screenId == null)
			return 0;
		return screenId.hashCode();
	}
}
