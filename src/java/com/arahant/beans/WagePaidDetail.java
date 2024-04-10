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
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "wage_paid_detail")
public class WagePaidDetail extends ArahantBean implements Serializable {

	public static final String WAGE_PAID = "wagePaid";
	public static final String WAGE_TYPE = "wageType";
	public static final String AMOUNT = "wageAmount";
	private String wageDetailId;
	private WagePaid wagePaid;
	private WageType wageType;
	private double wageAmount;
	private double wageBase;

	public WagePaidDetail() {
	}

	@Column(name = "wage_amount")
	public double getWageAmount() {
		return wageAmount;
	}

	public void setWageAmount(double wageAmount) {
		this.wageAmount = wageAmount;
	}

	@Column(name = "wage_base")
	public double getWageBase() {
		return wageBase;
	}

	public void setWageBase(double wageBase) {
		this.wageBase = wageBase;
	}

	@Id
	@Column(name = "wage_detail_id")
	public String getWageDetailId() {
		return wageDetailId;
	}

	public void setWageDetailId(String wageDetailId) {
		this.wageDetailId = wageDetailId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "wage_paid_id")
	public WagePaid getWagePaid() {
		return wagePaid;
	}

	public void setWagePaid(WagePaid wagePaid) {
		this.wagePaid = wagePaid;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "wage_type_id")
	public WageType getWageType() {
		return wageType;
	}

	public void setWageType(WageType wageType) {
		this.wageType = wageType;
	}

	@Override
	public String tableName() {
		return "wage_paid_detail";
	}

	@Override
	public String keyColumn() {
		return "wage_detail_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return wageDetailId = IDGenerator.generate(this);
	}
}
