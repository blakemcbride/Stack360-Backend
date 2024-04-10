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
import com.arahant.utils.StandardProperty;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "property")
public class Property extends ArahantBean implements Serializable {

	public static final String[] STANDARD_PROPERTIES = {
			StandardProperty.EMAIL_HOST,
			StandardProperty.ACCT_NAME,
			StandardProperty.DEFAULT_EMP_STAT_ID,
			StandardProperty.DEFAULT_SEC_GROUP,
			StandardProperty.DEFAULT_SCREEN_GROUP,
			StandardProperty.ANNOUNCEMENT,
			StandardProperty.COMBO_MAX,
			StandardProperty.SEARCH_MAX,
			StandardProperty.SEND_MESSAGES_BY_EMAIL
	};
	private static final long serialVersionUID = -1682363464897277260L;
	public static final String NAME = "propName";
	private String propName;
	private String propValue;
	private String description;

	public Property() {
	}

	/**
	 * @return Returns the propName.
	 */
	@Id
	@Column(name = "prop_name")
	public String getPropName() {
		return propName;
	}

	/**
	 * @param propName The propName to set.
	 */
	public void setPropName(final String propName) {
		firePropertyChange("propName", this.propName, propName);
		this.propName = propName;
	}

	/**
	 * @return Returns the propValue.
	 */
	@Column(name = "prop_value")
	public String getPropValue() {
		return propValue;
	}

	/**
	 * @param propValue The propValue to set.
	 */
	public void setPropValue(final String propValue) {
		firePropertyChange("propValue", this.propValue, propValue);
		this.propValue = propValue;
	}

	@Column(name = "prop_desc")
	public String getDescription() {
		if (description == null)
			return "";
		return description;
	}

	public void setDescription(String description) {
		firePropertyChange("description", this.description, description);
		this.description = description;
	}
	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#generateId()
	 */

	@Override
	public String generateId() throws ArahantException {
		throw new ArahantException("Can't generate key like that!");
	}
	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#keyColumn()
	 */

	@Override
	public String keyColumn() {
		return null;
	}
	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#tableName()
	 */

	@Override
	public String tableName() {
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (propName == null && o == null)
			return true;
		if (propName != null && o instanceof Property)
			return propName.equals(((Property) o).getPropName());

		return false;
	}

	@Override
	public int hashCode() {
		if (propName == null)
			return 0;
		return propName.hashCode();
	}
}
