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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "zipcode_lookup")
public class ZipcodeLookup extends ArahantBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "zipcode_lookup";
	// Fields    
	@Id
	@Column(name = "zipcode_id")
	private String zipcodeId;
	public static final String ZIPCODEID = "zipcodeId";
	@Column
	private String zipcode;
	public static final String ZIPCODE = "zipcode";
	@Column
	private String city;
	public static final String CITY = "city";
	@Column
	private String county;
	public static final String COUNTY = "county";
	@Column
	private String state;
	public static final String STATE = "state";

	// Constructors
	/**
	 * default constructor
	 */
	public ZipcodeLookup() {
	}

	// Property accessors
	public String getZipcodeId() {
		return this.zipcodeId;
	}

	public void setZipcodeId(final String zipcodeId) {
		this.zipcodeId = zipcodeId;
	}

	public String getZipcode() {
		return this.zipcode;
	}

	public void setZipcode(final String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(final String city) {
		this.city = city;
	}

	public String getCounty() {
		return this.county;
	}

	public void setCounty(final String county) {
		this.county = county;
	}

	public String getState() {
		return this.state;
	}

	public void setState(final String state) {
		this.state = state;
	}
	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#keyColumn()
	 */

	@Override
	public String keyColumn() {
		return "zipcode_id";
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
		setZipcodeId(IDGenerator.generate(this));
		return zipcodeId;
	}

	@Override
	public boolean equals(Object o) {
		if (zipcodeId == null && o == null)
			return true;
		if (zipcodeId != null && o instanceof ZipcodeLookup)
			return zipcodeId.equals(((ZipcodeLookup) o).getZipcodeId());

		return false;
	}

	@Override
	public int hashCode() {
		if (zipcodeId == null)
			return 0;
		return zipcodeId.hashCode();
	}
}
