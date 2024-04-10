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
@Table(name = Right.TABLE_NAME)
public class Right extends ArahantBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "security_token";
	// Fields
	private String rightId;
	public static final String RIGHTID = "rightId";
	private String identifier;
	public static final String IDENTIFIER = "identifier";
	private String description;
	public static final String DESCRIPTION = "description";
	private Set<RightsAssociation> rightsAssociations = new HashSet<RightsAssociation>(0);
	public static final String RIGHTSASSOCIATIONS = "rightsAssociations";
	public static final String RIGHT_CAN_ACCESS_ALL_COMPANIES = "CanAccessAllCompanies";
	public static final String RIGHT_SUPER_USER = "SuperUser";

	// Constructors
	/**
	 * default constructor
	 */
	public Right() {
	}

	// Property accessors
	@Id
	@Column(name = "right_id")
	public String getRightId() {
		return this.rightId;
	}

	public void setRightId(final String rightId) {
		this.rightId = rightId;
	}

	@Column(name = "identifier")
	public String getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}

	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@OneToMany(mappedBy = RightsAssociation.RIGHT, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<RightsAssociation> getRightsAssociations() {
		return this.rightsAssociations;
	}

	public void setRightsAssociations(final Set<RightsAssociation> rightsAssociations) {
		this.rightsAssociations = rightsAssociations;
	}

	@Override
	public String keyColumn() {
		return "right_id";
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String generateId() throws ArahantException {
		setRightId(IDGenerator.generate(this));
		return rightId;
	}

	@Override
	public boolean equals(Object o) {
		if (rightId == null && o == null)
			return true;
		if (rightId != null && o instanceof Right)
			return rightId.equals(((Right) o).getRightId());
		return false;
	}

	@Override
	public int hashCode() {
		if (rightId == null)
			return 0;
		return rightId.hashCode();
	}
}
