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
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "user_attribute")
public class UserAttribute extends ArahantBean implements Serializable {

	private static final long serialVersionUID = 2802631301468737986L;
	public static final String PERSON = "person";
	public static final String ATTRIBUTE = "userAttribute";
	public static final String PRESET_COLOR_THEME = "Color Theme";
	public static final String USE_PRESET_COLOR_THEME = "UsePresetClrTheme";
	public static final String BACKGROUND_COLOR_1 = "BckGrndClr1";
	public static final String BACKGROUND_COLOR_2 = "BckGrndClr2";
	public static final String UNHIGHLIGHTED_BUTTON_COLOR = "UnhghlghtdBttnClr";
	public static final String HIGHLIGHTED_BUTTON_COLOR = "HghlghtdBttnClr";
	public static final String UNHIGHLIGHTED_TEXT_COLOR = "UnhghlghtdTxtClr";
	public static final String HIGHLIGHTED_TEXT_COLOR = "HghlghtdTxtClr";
	public static final String COLOR_THEME = "Color Theme";
	public static final String LAYOUT = "Layout";
	public static final String HISTORY_BAR_HIDE = "History Hide";
	public static final String HISTORY_BAR_SHOW = "History Show";
	public static final String MENU_BAR_HIDE = "Menu Hide";
	public static final String MENU_BAR_SHOW = "Menu Show";
	public static final String BENEFIT_ASSIGNMENT_APPROVED_BENEFIT_COLOR = "Approved Benefit";
	public static final String BENEFIT_ASSIGNMENT_APPROVED_DECLINE_COLOR = "Approved Decline";
	public static final String BENEFIT_ASSIGNMENT_NOT_YET_APPROVED_COLOR = "Not Yet Approved";
	public static final String EMAIL_ADDRESSES = "Email Addresses";
	public static final String INCLUDE_MESSAGE_BODY = "Include Message Body";
	public static final String PROJECT_VIEW_STATE = "Project View State";
	private String attributeValue;
	private Person person;
	private String userAttribute;
	private UserAttributeId id;

	/**
	 * @return Returns the id.
	 */
	@Id
	public UserAttributeId getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(final UserAttributeId id) {
		this.id = id;
	}

	/**
	 * @return Returns the person.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id", updatable = false, insertable = false)
	public Person getPerson() {
		return person;
	}

	/**
	 * @param person The person to set.
	 */
	public void setPerson(final Person person) {
		this.person = person;
		if (id == null)
			id = new UserAttributeId();
		id.setPersonId(person != null ? person.getPersonId() : null);
	}

	/**
	 * @return Returns the attributeValue.
	 */
	@Column(name = "attribute_value")
	public String getAttributeValue() {
		return attributeValue;
	}

	/**
	 * @param attributeValue The attributeValue to set.
	 */
	public void setAttributeValue(final String attributeValue) {
		this.attributeValue = attributeValue;

	}

	public UserAttribute() {
		super();
	}

	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#generateId()
	 */
	@Override
	public String generateId() throws ArahantException {
		throw new ArahantException("Can't make this like that.");
	}

	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#keyColumn()
	 */
	@Override
	public String keyColumn() {
		return "";
	}

	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#tableName()
	 */
	@Override
	public String tableName() {
		return "user_attribute";
	}

	/**
	 * @return Returns the userAttribute.
	 */
	@Column(name = "user_attribute", insertable = false, updatable = false)
	public String getUserAttribute() {
		return userAttribute;
	}

	/**
	 * @param userAttribute The userAttribute to set.
	 */
	public void setUserAttribute(final String userAttribute) {
		this.userAttribute = userAttribute;
		if (id == null)
			id = new UserAttributeId();
		id.setUserAttribute(userAttribute);
	}

	@Override
	public boolean equals(Object o) {
		if (id == null && o == null)
			return true;
		if (id != null && o instanceof UserAttribute)
			return id.equals(((UserAttribute) o).getId());

		return false;
	}

	@Override
	public int hashCode() {
		if (id == null)
			return 0;
		return id.hashCode();
	}
}
