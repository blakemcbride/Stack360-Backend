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
import javax.persistence.*;

@Entity
@Table(name = "screen_group_hierarchy")
public class ScreenGroupHierarchy extends ArahantBean implements java.io.Serializable, Comparable<ScreenGroupHierarchy> {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "screen_group_hierarchy";
	// Fields    
	public static final String ID = "id";
	private Screen screen;
	public static final String SCREEN = "screen";
	private ScreenGroup childScreenGroup;
	public static final String CHILDSCREENGROUP = "childScreenGroup";
	private ScreenGroup parentScreenGroup;
	public static final String PARENTSCREENGROUP = "parentScreenGroup";
	private char defaultScreen;
	public static final String DEFAULTSCREEN = "defaultScreen";
	private short seqNo;
	public static final String SEQNO = "seqNo";
	private String screenGroupHierarchyId;
	public static final String SCREENGROUPHIERARCHYID = "screenGroupHierarchyId";
	private String buttonName;
	public static final String BUTTON_NAME = "buttonName";

	// Constructors
	/**
	 * @return Returns the seqNo.
	 */
	@Column(name = "seq_no")
	public short getSeqNo() {
		return seqNo;
	}

	/**
	 * @param seqNo The seqNo to set.
	 */
	public void setSeqNo(final short seqNo) {
		this.seqNo = seqNo;
	}

	/**
	 * default constructor
	 */
	public ScreenGroupHierarchy() {}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "screen_id")
	public Screen getScreen() {
		return this.screen;
	}

	public void setScreen(final Screen screen) {
		this.screen = screen;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "child_screen_group_id")
	public ScreenGroup getChildScreenGroup() {
		return this.childScreenGroup;
	}

	public void setChildScreenGroup(final ScreenGroup screenGroupByChildScreenGroupId) {
		this.childScreenGroup = screenGroupByChildScreenGroupId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_screen_group_id")
	public ScreenGroup getParentScreenGroup() {
		return this.parentScreenGroup;
	}

	public void setParentScreenGroup(final ScreenGroup screenGroupByParentScreenGroupId) {
		this.parentScreenGroup = screenGroupByParentScreenGroupId;
	}

	@Column(name = "default_screen")
	public char getDefaultScreen() {
		return this.defaultScreen;
	}

	public void setDefaultScreen(final char defaultScreen) {
		this.defaultScreen = defaultScreen;
	}

	/**
	 * @return Returns the screenGroupHierarchyId.
	 */
	@Id
	@Column(name = "screen_group_hierarchy_id")
	public String getScreenGroupHierarchyId() {
		return screenGroupHierarchyId;
	}

	/**
	 * @param screenGroupHierarchyId The screenGroupHierarchyId to set.
	 */
	public void setScreenGroupHierarchyId(final String screenGroupHierarchyId) {
		this.screenGroupHierarchyId = screenGroupHierarchyId;
	}
	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#keyColumn()
	 */

	@Override
	public String keyColumn() {
		return "screen_group_hierarchy_id";
	}

	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#tableName()
	 */
	@Override
	public String tableName() {
		return TABLE_NAME;
	}
	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#generateId()
	 */

	@Override
	public String generateId() throws ArahantException {
		setScreenGroupHierarchyId(IDGenerator.generate(this));
		return screenGroupHierarchyId;
	}

	/*
	 * (non-Javadoc) @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final ScreenGroupHierarchy o) {
		if (seqNo == o.seqNo)
			return 0;
		if (seqNo < o.seqNo)
			return -1;
		return 1;
	}

	@Column(name = "button_name")
	public String getButtonName() {
		return buttonName;
	}

	public void setButtonName(String buttonName) {
		firePropertyChange("buttonName", this.buttonName, buttonName);
		this.buttonName = buttonName;
	}

	@Override
	public boolean equals(Object o) {
		if (screenGroupHierarchyId == null && o == null)
			return true;
		if (screenGroupHierarchyId != null && o instanceof ScreenGroupHierarchy)
			return screenGroupHierarchyId.equals(((ScreenGroupHierarchy) o).getScreenGroupHierarchyId());

		return false;
	}

	@Override
	public int hashCode() {
		if (screenGroupHierarchyId == null)
			return 0;
		return screenGroupHierarchyId.hashCode();
	}
}
