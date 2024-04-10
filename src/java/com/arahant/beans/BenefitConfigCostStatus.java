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
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = BenefitConfigCostStatus.TABLE_NAME)
@Cache(region="arahant", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BenefitConfigCostStatus extends ArahantBean implements Serializable {

	public static final String TABLE_NAME = "benefit_config_cost_status";
	public static final String BENEFIT_CONFIG_COST_ID = "costId";
	public static final String STATUS = "status";
	public static final String CONFIG_COST = "cost";
	private String benefitConfigCostStatusId;
	private BenefitConfigCost cost;
	private String costId;
	private HrEmployeeStatus status;

	@Id
	@Column(name = "benefit_config_cost_status_id")
	public String getBenefitConfigCostStatusId() {
		return benefitConfigCostStatusId;
	}

	public void setBenefitConfigCostStatusId(String benefitConfigCostStatusId) {
		this.benefitConfigCostStatusId = benefitConfigCostStatusId;
	}

	@Column(name = "benefit_config_cost_id", insertable = false, updatable = false)
	public String getCostId() {
		return costId;
	}

	public void setCostId(String costId) {
		this.costId = costId;
	}

	@ManyToOne
	@JoinColumn(name = "benefit_config_cost_id")
	public BenefitConfigCost getCost() {
		return cost;
	}

	public void setCost(BenefitConfigCost cost) {
		this.cost = cost;
	}

	@ManyToOne
	@JoinColumn(name = "employee_status_id")
	public HrEmployeeStatus getStatus() {
		return status;
	}

	public void setStatus(HrEmployeeStatus status) {
		this.status = status;
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Override
	public String keyColumn() {
		return "benefit_config_cost_status_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return benefitConfigCostStatusId = IDGenerator.generate(this);
	}
}


/*
 * varchar 16 benefit_config_cost_id
varchar 16 employee_status_id
 */
