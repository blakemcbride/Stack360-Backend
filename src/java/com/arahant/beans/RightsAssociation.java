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
import javax.persistence.*;

@Entity
@Table(name = "rights_association")
public class RightsAssociation extends ArahantBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "rights_association";
	// Fields    
	private RightsAssociationId id = new RightsAssociationId();
	public static final String ID = "id";
	private SecurityGroup securityGroup;
	public static final String SECURITYGROUP = "securityGroup";
	private Right right;
	public static final String RIGHT = "right";
	public static final String ACCESSLEVEL = "accessLevel";
	private short accessLevel;

	// Constructors
	/**
	 * @return Returns the accessLevel.
	 */
	@Column(name = "access_level")
	public short getAccessLevel() {
		return accessLevel;
	}

	/**
	 * @param accessLevel The accessLevel to set.
	 */
	public void setAccessLevel(final short accessLevel) {
		this.accessLevel = accessLevel;
	}

	/**
	 * default constructor
	 */
	public RightsAssociation() {
	}

	// Property accessors
	@Id
	public RightsAssociationId getId() {
		return this.id;
	}

	public void setId(final RightsAssociationId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "security_group_id", updatable = false, insertable = false)
	public SecurityGroup getSecurityGroup() {
		return this.securityGroup;
	}

	public void setSecurityGroup(final SecurityGroup securityGroup) {
		this.securityGroup = securityGroup;
		if (id == null)
			id = new RightsAssociationId();
		id.setSecurityGroupId(securityGroup.getSecurityGroupId());
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "right_id", updatable = false, insertable = false)
	public Right getRight() {
		return this.right;
	}

	public void setRight(final Right right) {
		this.right = right;
		if (id == null)
			id = new RightsAssociationId();
		id.setRightId(right.getRightId());
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

		return TABLE_NAME;
	}

	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#generateId()
	 */
	@Override
	public String generateId() throws ArahantException {
		throw new ArahantException("Can't generate like this.");
	}

	@Override
	public boolean equals(Object o) {
		if (id == null && o == null)
			return true;
		if (id != null && o instanceof RightsAssociation)
			return id.equals(((RightsAssociation) o).getId());

		return false;
	}

	@Override
	public int hashCode() {
		if (id == null)
			return 0;
		return id.hashCode();
	}
}
