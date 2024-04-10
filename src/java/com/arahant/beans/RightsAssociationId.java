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

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RightsAssociationId implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "rights_association";
	// Fields    
	private String securityGroupId;
	public static final String SECURITYGROUPID = "securityGroupId";
	private String rightId;
	public static final String RIGHTID = "rightId";

	// Constructors
	/**
	 * default constructor
	 */
	public RightsAssociationId() {
	}

	/**
	 * full constructor
	 */
	public RightsAssociationId(final String securityGroupId, final String rightId) {
		this.securityGroupId = securityGroupId;
		this.rightId = rightId;
	}

	// Property accessors
	@Column(name = "security_group_id")
	public String getSecurityGroupId() {
		return this.securityGroupId;
	}

	public void setSecurityGroupId(final String securityGroupId) {
		this.securityGroupId = securityGroupId;
	}

	@Column(name = "right_id")
	public String getRightId() {
		return this.rightId;
	}

	public void setRightId(final String rightId) {
		this.rightId = rightId;
	}

	@Override
	public boolean equals(final Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof RightsAssociationId))
			return false;
		final RightsAssociationId castOther = (RightsAssociationId) other;

		return ((this.getSecurityGroupId() == castOther.getSecurityGroupId()) || (this.getSecurityGroupId() != null
				&& castOther.getSecurityGroupId() != null && this.getSecurityGroupId().equals(castOther.getSecurityGroupId())))
				&& ((this.getRightId() == castOther.getRightId()) || (this.getRightId() != null
				&& castOther.getRightId() != null && this.getRightId().equals(castOther.getRightId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getSecurityGroupId() == null ? 0 : this.getSecurityGroupId().hashCode());
		result = 37 * result
				+ (getRightId() == null ? 0 : this.getRightId().hashCode());
		return result;
	}
}
