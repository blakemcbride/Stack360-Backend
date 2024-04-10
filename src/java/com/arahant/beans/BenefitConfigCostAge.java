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
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "benefit_config_cost_age")
@Cache(region="arahant", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BenefitConfigCostAge extends ArahantBean implements Serializable {
	private static final transient ArahantLogger logger = new ArahantLogger(BenefitConfigCostAge.class);

	public static final String BENEFIT_CONFIG_COST = "cost";
	public static final String BENEFIT_CONFIG_COST_ID = "costId";
	public static final String AGE = "maxAge";
	private String benefitConfigCostAgeId;
	private BenefitConfigCost cost;
	private String costId;
	private short maxAge;
	private double eeValue;
	private double erValue;
	private String insuranceId;

	
	public BenefitConfigCostAge() {
	}

	@Id
	@Column(name = "benefit_config_cost_age_id")
	public String getBenefitConfigCostAgeId() {
		return benefitConfigCostAgeId;
	}

	public void setBenefitConfigCostAgeId(String benefitConfigCostAgeId) {
		this.benefitConfigCostAgeId = benefitConfigCostAgeId;
	}

	@Column(name = "benefit_config_cost_id", insertable = false, updatable = false)
	public String getCostId() {
		return costId;
	}

	public void setCostId(String costId) {
		this.costId = costId;
	}

	//(fetch = FetchType.LAZY)
	@ManyToOne
	@JoinColumn(name = "benefit_config_cost_id")
	public BenefitConfigCost getCost() {
		return cost;
	}

	public void setCost(BenefitConfigCost cost) {
		this.cost = cost;
	}

	@Column(name = "max_age")
	public short getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(short maxAge) {
		this.maxAge = maxAge;
	}

	@Deprecated
	public float deprecatedGetMultiplier() {
		logger.deprecated();
		return 1;
	}

	@Deprecated
	public void setMultiplier(float multiplier) {
		logger.deprecated();
	}

	@Column(name = "ee_value")
	public double getEeValue() {
		return eeValue;
	}

	public void setEeValue(double eeValue) {
		this.eeValue = eeValue;
	}

	@Column(name = "er_value")
	public double getErValue() {
		return erValue;
	}

	public void setErValue(double erValue) {
		this.erValue = erValue;
	}

	@Column(name = "insurance_id")
	public String getInsuranceId() {
		return insuranceId;
	}

	public void setInsuranceId(String insuranceId) {
		this.insuranceId = insuranceId;
	}

	@Override
	public String tableName() {
		return "benefit_config_cost_age";
	}

	@Override
	public String keyColumn() {
		return "benefit_config_cost_age_id";
	}

	@Override
	public String generateId() throws ArahantException {
		return benefitConfigCostAgeId = IDGenerator.generate(this);
	}
}

