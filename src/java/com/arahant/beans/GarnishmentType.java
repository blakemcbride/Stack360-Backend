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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.beans;
import javax.persistence.*;
import org.hibernate.annotations.Fetch;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;

/**
 *
 */
@Entity
@Table(name="garnishment_type")
public class GarnishmentType extends ArahantBean {

	public static final String WAGE_TYPE="wageType";
	public static final String DESCRIPTION="description";
	public static final String ID="garnishmentTypeId";
	public static final String LAST_ACTIVE_DATE="lastActiveDate";
	
	private String garnishmentTypeId;
	//private String code;
	private String description;
	private int lastActiveDate;
	private WageType wageType;

	@ManyToOne
	@JoinColumn(name="wage_type_id")
	public WageType getWageType() {
		return wageType;
	}

	public void setWageType(WageType wageType) {
		this.wageType = wageType;
	}



	/*
	@Column (name="code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
*/
	@Column (name="description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Id
	@Column (name="garnishment_type_id")
	public String getGarnishmentTypeId() {
		return garnishmentTypeId;
	}

	public void setGarnishmentTypeId(String garnishmentTypeId) {
		this.garnishmentTypeId = garnishmentTypeId;
	}

	@Column (name="last_active_date")
	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}
	
	
	
	
	@Override
	public String tableName() {
		return "garnishment_type";
	}

	@Override
	public String keyColumn() {
		return "garnishment_type_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return garnishmentTypeId=IDGenerator.generate(this);
	}

}
